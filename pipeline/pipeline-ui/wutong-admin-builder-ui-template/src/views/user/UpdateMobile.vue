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
          :label-col="{ span: userInfo.mobile ? 6 : 4 }"
          :wrapper-col="{ span: userInfo.mobile ? 16 : 18 }"
        >
          <FormItem name="mobile" class="enter-x" :label="(userInfo.mobile ? '新' : '') + '手机号'">
            <Input
              size="large"
              v-model:value="formData.mobile"
              :placeholder="t('sys.login.mobilePlaceholder')"
              class="fix-auto-fill"
              :maxlength="11"
            />
          </FormItem>
          <FormItem
            name="sms"
            class="enter-x"
            :label="(userInfo.mobile ? '新手机号' : '') + '验证码'"
          >
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
  import { getSmsCode } from '/@/api/sys/user';
  import { validPhone } from '/@/utils/lib/validate';
  import { bindMoblie } from '/@/api/user/userApi';
  import { getUser, userInfo } from './basic/useAccount';
  const FormItem = Form.Item;
  const { t } = useI18n();

  const title = userInfo.value.mobile ? '修改手机号' : '绑定手机号';

  const { createMessage } = useMessage();

  const rules = {
    mobile: [
      {
        required: true,
        validator: function (_, value) {
          if (!value) {
            return Promise.reject('请输入手机号');
          } else if (!validPhone(value)) {
            return Promise.reject('请输入正确的手机号');
          } else {
            return Promise.resolve();
          }
        },
      },
    ],
    sms: [
      {
        required: true,
        message: '请输入验证码！',
      },
    ],
  };

  const [register, { closeModal }] = useModal();

  const formRef = ref();
  const loading = ref(false);
  const formData = reactive({
    mobile: '',
    sms: '',
  });
  let captchaKey = '';

  async function handleLogin() {
    const form = unref(formRef);
    loading.value = true;
    form
      .validateFields()
      .then(() => {
        bindMoblie({
          key: captchaKey,
          mobile: formData.mobile,
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
        .validateFields(['mobile'])
        .then(() => {
          getSmsCode(formData.mobile, 'BIND')
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
