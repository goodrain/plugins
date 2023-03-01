<template>
  <CollapseContainer title="安全设置" :canExpan="false">
    <Spin :spinning="loading">
      <List>
        <template v-for="item in secureSettingList" :key="item.key">
          <ListItem>
            <ListItemMeta>
              <template #title>
                {{ item.title }}
                <div class="extra" v-if="item.extra" @click="update(item)">
                  {{ item.extra }}
                </div>
              </template>
              <template #description>
                <div>{{ item.description }}</div>
              </template>
            </ListItemMeta>
          </ListItem>
        </template>
      </List>
    </Spin>
    <UpdateMobile @register="registerMobile" />
    <UpdateEmail @register="registerEmail" />
    <UpdatePwd @register="registerPwd" :width="800" :footer="null" />
  </CollapseContainer>
</template>
<script lang="ts" setup>
  import { List, Spin } from 'ant-design-vue';
  import { onMounted, ref } from 'vue';
  import { CollapseContainer } from '/@/components/Container/index';
  import { zxcvbn } from '@zxcvbn-ts/core';
  import { loading, userInfo, getUser } from './useAccount';
  import { router } from '/@/router';
  import UpdateMobile from '../UpdateMobile.vue';
  import UpdateEmail from '../UpdateEmail.vue';
  import UpdatePwd from '/@/views/user/updatePwd/Modal.vue';
  import { useModal } from '/@/components/Modal';
  const [registerMobile, { openModal: openModalMobile }] = useModal();
  const [registerEmail, { openModal: openModalEmail }] = useModal();
  const [registerPwd, { openModal: openPwdModal }] = useModal();
  const { Item: ListItem } = List;
  const { Meta: ListItemMeta } = ListItem;

  const secureSettingList = ref<any[]>([]);

  onMounted(() => {
    setSecureSettingList();
  });

  function update(item) {
    if (item.router) {
      router.push(item.router);
      return;
    }
    switch (item.key) {
      case '1':
        openPwdModal();
        break;
      case '2':
        openModalMobile();
        break;
      case '3':
        openModalEmail();
        break;
    }
  }

  async function setSecureSettingList() {
    await getUser();
    const user: any = userInfo.value;
    secureSettingList.value = [
      {
        key: '1',
        title: '登录密码',
        description: '当前密码强度: ' + getPasswordStrength(user.password),
        extra: user.password ? '修改' : '未设置密码',
        // router: '/user/updatePwd/index',
      },
      {
        key: '2',
        title: '手机绑定',
        description: user.mobile
          ? `已绑定手机：：${user.mobile.substring(0, 3)}****${user.mobile.substring(7, 11)}`
          : '未绑定手机',
        extra: user.mobile ? '修改' : '绑定',
      },
      {
        key: '3',
        title: '邮箱绑定',
        description: user.email
          ? `已绑定邮箱：：${user.email.substring(0, 3)}****${user.email.substring(
              user.email.length - 5,
              user.email.length,
            )}`
          : '未绑定邮箱',
        extra: '修改',
      },
    ];
  }

  // 密码强度
  const getPasswordStrength = (pwd: string | null) => {
    const score = pwd ? zxcvbn(pwd).score : -1;
    if (score === -1) return '未设置密码';
    const obj = {
      '-1': '未设置密码',
      0: '不安全',
      1: '弱',
      2: '一搬',
      3: '中等',
      4: '安全',
    };
    return obj[score];
  };
</script>
<style lang="less" scoped>
  .extra {
    float: right;
    margin-top: 10px;
    margin-right: 30px;
    font-weight: normal;
    color: #1890ff;
    cursor: pointer;
  }
</style>
