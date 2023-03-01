<template>
  <PageWrapper title="修改当前用户密码" content="修改成功后会自动退出当前登录！">
    <div class="py-2 bg-white flex flex-col justify-center items-center">
      <Spin :spinning="loading">
        <BasicForm @register="register" />
        <div class="flex justify-center">
          <a-button @click="resetFields"> 重置 </a-button>
          <a-button class="!ml-4" type="primary" @click="handleSubmit"> 确认 </a-button>
        </div>
      </Spin>
    </div>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { PageWrapper } from '/@/components/Page';
  import { BasicForm, useForm } from '/@/components/Form';
  import { UpdatePwd } from '/@/api/user/userApi';
  import { useUserStore } from '/@/store/modules/user';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { Spin } from 'ant-design-vue';
  import { encryptByMd5 } from '/@/utils/cipher';
  const { createMessage } = useMessage();

  import { formSchema } from './pwd.data';
  export default defineComponent({
    name: 'ChangePassword',
    components: { BasicForm, PageWrapper, Spin },
    setup() {
      const loading = ref(false);

      const [register, { validate, resetFields }] = useForm({
        size: 'large',
        labelWidth: 100,
        showActionButtonGroup: false,
        schemas: formSchema,
        baseColProps: {
          span: 24,
        },
      });

      const userStore = useUserStore();

      async function handleSubmit() {
        try {
          const values = await validate();
          const { passwordOld, passwordNew } = values;
          loading.value = true;
          UpdatePwd({
            id: userStore.userInfo?.id,
            password: encryptByMd5(passwordOld),
            newPassword: encryptByMd5(passwordNew),
          })
            .then(() => {
              createMessage.success('修改成功！');
              userStore.logout(true);
            })
            .finally(() => {
              loading.value = false;
            });
          // TODO custom api
          // console.log(passwordOld, passwordNew);
          // const { router } = useRouter();
          // router.push(pageEnum.BASE_LOGIN);
        } catch (error) {}
      }

      return { loading, register, resetFields, handleSubmit };
    },
  });
</script>
