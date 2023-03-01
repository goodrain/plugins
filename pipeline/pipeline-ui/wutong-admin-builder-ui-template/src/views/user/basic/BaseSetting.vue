<template>
  <CollapseContainer title="基本设置" :canExpan="false">
    <Spin :spinning="loading">
      <Row :gutter="24">
        <Col :span="14">
          <BasicForm @register="register" />
        </Col>
        <Col :span="10">
          <div class="change-avatar">
            <div class="mb-2">头像</div>
            <CropperAvatar
              ref="avatar"
              :uploadApi="uploadApi"
              :value="avatarImg"
              btnText="更换头像"
              :btnProps="{ preIcon: 'ant-design:cloud-upload-outlined' }"
              width="150"
            />
            <!-- @change="updateAvatar" -->
          </div>
        </Col>
      </Row>
      <Row>
        <Col :offset="2">
          <Button type="primary" @click="handleSubmit"> 更新基本信息 </Button>
        </Col>
      </Row>
    </Spin>
  </CollapseContainer>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-09-09 17:52:38
   * description : 基本信息
   */
  import { Button, Row, Col, Spin } from 'ant-design-vue';
  import { onMounted, ref } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { CollapseContainer } from '/@/components/Container';
  import { CropperAvatar } from '/@/components/Cropper';

  import { baseSetschemas } from './data';
  import { useUserStore } from '/@/store/modules/user';
  import { useMessage } from '/@/hooks/web/useMessage';
  import img from '/@/assets/images/appIcon.png';
  import { UploadFileParams } from '/#/axios';

  import { loading, userInfo, getUser, updateUser } from './useAccount';
  import { defHttp } from '/@/utils/http/axios';
  import { account } from '/@/api/index';
  import { getFileUrl } from '/@/utils/file/fileUtil';

  const userStore = useUserStore();
  const avatar = ref<any>(null);
  const avatarImg = ref(img);
  const avatarImgOriginal = ref(img);
  const { createMessage } = useMessage();

  const [register, { setFieldsValue, validateFields }] = useForm({
    labelWidth: 120,
    schemas: baseSetschemas,
    showActionButtonGroup: false,
  });

  onMounted(async () => {
    await getUser();
    setFieldsValue(userInfo.value);
    const { avatar } = userStore.getUserInfo;
    avatarImgOriginal.value = avatar;
    avatarImg.value = avatar == '' ? img : getFileUrl(avatar);
  });

  function updateAvatar(src: string) {
    const userinfo = userStore.getUserInfo;
    userinfo.avatar = src;
    userStore.setUserInfo(userinfo);
  }

  async function handleSubmit() {
    const values = (await validateFields()) as any;
    updateUser({
      ...userInfo.value,
      ...values,
      avatar: avatarImgOriginal.value,
    });
  }

  function uploadApi(params: UploadFileParams) {
    params.data = {
      businessType: 'USER_ICON_UPLOAD',
    };
    return defHttp.uploadFile({ url: `${account}/file/fileupload` }, params).then((res: any) => {
      if ('0' === res.data.code) {
        avatarImg.value = getFileUrl(res.data.data.url);
        avatarImgOriginal.value = res.data.data.url;
        // setFieldsValue({
        //   avatar: res.data.data.url,
        // });
        // const userinfo = userStore.getUserInfo;
        // userinfo.avatar = res.data.data.url;
        avatar.value.closeModal();
      } else {
        createMessage.error('上传失败');
      }
    });
  }
</script>
<style lang="less" scoped></style>
