<template>
  <div class="inline-block">
    <a-button style="border-right: none; color: #999"> 应用服务 </a-button>
    <Dropdown>
      <template #overlay>
        <Menu @click="selectService">
          <MenuItem v-for="item in appServiceList" :key="item.id">
            <span :class="item.id === currentService.id ? 'text-blue-600' : ''">{{
              item.name
            }}</span>
          </MenuItem>
        </Menu>
      </template>
      <a-button>
        {{ currentService.name }}
        <DownOutlined />
      </a-button>
    </Dropdown>
    <Dropdown class="ml-4" v-if="showGitUrl" :disabled="!currentService.id">
      <template #overlay>
        <Menu @click="selectClone">
          <MenuItem :key="cloneUrl.sshUrl">
            <div class="flex flex-row justify-between">
              <Tag>SSH地址：</Tag>
              <div class="text-gray-500 pr-4">{{ cloneUrl.sshUrl }}</div>
              <div><CopyOutlined /></div>
            </div>
          </MenuItem>
          <Divider class="!my-1" />
          <MenuItem :key="cloneUrl.httpUrl">
            <div class="flex flex-row justify-between">
              <Tag>HTTS地址：</Tag>
              <div class="text-gray-500 pr-4">{{ cloneUrl.httpUrl }}</div>
              <div><CopyOutlined /></div>
            </div>
          </MenuItem>
        </Menu>
      </template>
      <a-button>
        复制仓库地址
        <DownOutlined />
      </a-button>
    </Dropdown>
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-07-23 09:39:51
   * description : 选择服务
   */
  import { computed, unref } from 'vue';
  import { Dropdown, Menu, Tag, Divider } from 'ant-design-vue';
  import { DownOutlined, CopyOutlined } from '@ant-design/icons-vue';
  import { useAppServiceStore } from '/@@/store/modules/appServiceStore';
  import { useCopyToClipboard } from '/@/hooks/web/useCopyToClipboard';
  import { useMessage } from '/@/hooks/web/useMessage';

  const emits = defineEmits(['change']);

  const { Item: MenuItem } = Menu;
  const { createMessage } = useMessage();

  defineProps({
    showGitUrl: { type: Boolean, default: false },
  });

  // clone
  const { clipboardRef, copiedRef } = useCopyToClipboard();

  const appServiceStore = useAppServiceStore();
  appServiceStore.setServiceList();

  // 应用服务列表
  const appServiceList = computed(() => appServiceStore.getServiceList);

  // 当前应用服务
  const currentService = computed(() => appServiceStore.getCurrentService);

  // clone地址
  const cloneUrl: any = computed(() => appServiceStore.getCloneUrl);

  function selectService(target) {
    appServiceStore.setServiceCurrent(target.key);
    emits('change', appServiceStore.getCurrentService);
  }
  function selectClone(target) {
    clipboardRef.value = target.key;
    if (unref(copiedRef)) {
      createMessage.success(target.key + ' 成功！');
    }
  }
</script>
<style lang="less" scoped></style>
