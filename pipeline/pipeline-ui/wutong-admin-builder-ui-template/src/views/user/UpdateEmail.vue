<template>
  <BasicModal
    v-bind="$attrs"
    @register="register"
    :minHeight="100"
    :title="title"
    @ok="handleLogin"
  >
    <Spin :spinning="loading">
      <div class="pt-3px pr-3px">
        <Form
          class="p-4 enter-x"
          :model="formData"
          ref="formRef"
          :rules="rules"
          :label-col="{ span: userInfo.email ? 6 : 4 }"
          :wrapper-col="{ span: userInfo.email ? 16 : 18 }"
        >
          <FormItem name="email" class="enter-x" :label="(userInfo.email ? '新' : '') + '邮箱'">
            <Input
              size="large"
              v-model:value="formData.email"
              :placeholder="t('sys.login.emailPlaceholder')"
              class="fix-auto-fill"
            />
          </FormItem>
          <FormItem name="sms" class="enter-x" :label="(userInfo.email ? '新邮箱' : '') + '验证码'">
            <CountdownInput
              size="large"
              class="fix-auto-fill"
              v-model:value="formData.sms"
              :placeholder="t('sys.login.smsPlaceholder')"
              :sendCodeApi="sedSmsCod"
            />
          </FormItem>
        </Form>
      </div>
    </Spin>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, reactive, unref } from 'vue';
  import { BasicModal, useModal } from '/@/components/Modal';
  import { Form, Input, Spin } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { CountdownInput } from '/@/components/CountDown';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getEmailCode } from '/@/api/sys/user';
  import { bindEmail } from '/@/api/user/userApi';
  import { getUser, userInfo } from './basic/useAccount';
  const FormItem = Form.Item;
  const { t } = useI18n();

  const title = userInfo.value.email ? '修改邮箱' : '绑定邮箱';
  const { createMessage } = useMessage();

  const rules = {
    email: [
      {
        required: true,
        message: '请输入邮箱',
      },
      {
        type: 'email',
        message: '请输入正确的邮箱',
      },
    ],
    sms: [
      {
        required: true,
        type: 'string',
        message: '请输入验证码！',
      },
    ],
  };
  const [register, { closeModal }] = useModal();
  const formRef = ref();
  const loading = ref(false);
  const formData = reactive({
    email: '',
    sms: '',
  });
  let captchaKey = '';

  async function handleLogin() {
    const form = unref(formRef);
    loading.value = true;
    form
      .validateFields()
      .then(() => {
        bindEmail({
          key: captchaKey,
          email: formData.email,
          captchaCode: formData.sms,
        })
          .then(() => {
            createMessage.success('绑定成功！');
            getUser();
            closeModal();
          })
          .finally(() => {
            loading.value = false;
          });
      })
      .catch(() => {
        loading.value = false;
      });
  }
  async function sedSmsCod() {
    const form = unref(formRef);
    return new Promise<boolean>((reslove, reject) => {
      form
        .validateFields(['email'])
        .then(() => {
          getEmailCode(formData.email, 'BIND')
            .then((res) => {
              createMessage.success('发送成功');
              captchaKey = res.key;

              reslove(true);
            })
            .catch(() => {
              reject(false);
            });
        })
        .catch(() => {
          reject(false);
        });
    });
  }
</script>
