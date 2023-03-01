<template>
  <BasicDrawer
    v-bind="$attrs"
    @register="registerDrawer"
    showFooter
    title="修改仓库配置"
    width="40%"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm">
      <!-- end 子服务 自动构建 -->
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-03-22 10:50:05
   * description : 新增或修改
   */
  import { ref, computed, reactive } from 'vue';
  import { message } from 'ant-design-vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { hubFormSchema } from './service.data';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { useEnvStore } from '/@@/store/modules/envStore';
  import { getHubConfig, updateHubConfig } from '/@@/api/applicationServiceApi';

  const envStore = useEnvStore();
  envStore.setList();

  const emit = defineEmits(['success']);
  const appId = ref('');

  const [registerForm, { setFieldsValue, validate }] = useForm({
    labelWidth: 150,
    schemas: hubFormSchema,
    showActionButtonGroup: false,
    baseColProps: { lg: 22, md: 22 },
  });

  const [registerDrawer, { changeLoading, closeDrawer, setDrawerProps }] = useDrawerInner(
    async (data) => {
      const { id } = data?.record || {};
      appId.value = id;
      changeLoading(true);
      try {
        if (id) {
          const res = await getHubConfig({ id });
          if (res) {
            setFieldsValue(res);
          }
        }
      } catch (e) {
      } finally {
        changeLoading(false);
      }
    },
  );

  async function handleSubmit() {
    setDrawerProps({ confirmLoading: true });
    try {
      const value = await validate();
      value.id = appId.value;
      if (value) {
        await updateHubConfig(value);
        closeDrawer();
        emit('success');
      }
    } catch (e: any) {
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>
<style lang="less" scoped>
  .oauth-config {
    display: flex;
    overflow: hidden;

    .item-col {
      width: 49.99%;
      float: left;
      padding: 10px 15px;
      border: 1px solid #ddd;
      box-sizing: border-box;
      cursor: pointer;

      &.active {
        border-color: #2953e8;

        .title {
          color: #2953e8;
        }
      }
    }

    .title {
      font-size: 16px;
      font-weight: bold;
    }

    .content {
      font-size: 14px;
      line-height: 20px;
      color: #666;
    }
  }

  .test {
    display: inline-block;
    padding-left: 15px;
    font-size: 18px;

    &.success {
      color: forestgreen;
    }

    &.fail {
      .info {
        font-size: 14px;
        color: red;
        cursor: pointer;
      }
    }
  }
</style>
