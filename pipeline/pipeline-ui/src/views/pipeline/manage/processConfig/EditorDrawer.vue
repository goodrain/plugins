<template>
  <BasicDrawer
    v-bind="$attrs"
    @register="registerDrawer"
    showFooter
    width="500px"
    @ok="handleSubmit"
    @visible-change="visibleChange"
    :destroyOnClose="true"
  >
    <template #title>
      编辑
      <DeleteOutlined
        v-if="detail.id !== 999999"
        @click="del"
        class="cursor-pointer !text-red-600"
      />
    </template>
    <div class="pr-5">
      <BasicForm @register="registerForm" />
      <CodeEditor :value="detail.script" @change="(val) => (inputDetail.script = val)" />
      <RunnerForm :tags="detail.tags" @change="(tags) => (inputDetail.tags = tags)" />
      <ConditionForm :detail="detail" @change="(obj) => (inputDetail.exceptAndOnly = obj)" />
      <Divider orientation="left">
        <div class="text-sm">制品产物</div>
      </Divider>
      <DirForm :paths="detail.artifacts?.paths" @change="(paths) => (inputDetail.paths = paths)" />
      <BasicForm @register="registerProductForm" />
      <Divider orientation="left">
        <div class="text-sm">缓存</div>
      </Divider>
      <CacheForm :paths="detail.cache?.paths" @change="(cache) => (inputDetail.cache = cache)" />
    </div>
  </BasicDrawer>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-03-22 10:50:05
   * description : 新增或修改
   */
  import { reactive, ref } from 'vue';
  import { Divider } from 'ant-design-vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema, productSchema } from './form.data';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { DeleteOutlined } from '@ant-design/icons-vue';
  import RunnerForm from './RunnerForm.vue';
  import ConditionForm from './conditionForm/index.vue';
  import DirForm from './DirForm.vue';
  import CacheForm from './CacheForm.vue';
  import CodeEditor from './CodeEditor.vue';
  import { flow, updateStages } from '../usePipline';

  const { createMessage, createConfirm } = useMessage();

  const detail = ref<any>({});
  const inputDetail = reactive({
    tags: [], // runner
    script: [], // 脚本
    exceptAndOnly: {}, // 权有或排队
    paths: [], //目录
    cache: [], //缓存
  });

  const [registerForm, formMethod] = useForm({
    labelCol: { span: 24 },
    schemas: formSchema,
    showActionButtonGroup: false,
    baseColProps: { span: 24 },
  });
  const [registerProductForm, productMethod] = useForm({
    labelCol: { span: 24 },
    schemas: productSchema,
    showActionButtonGroup: false,
    baseColProps: { span: 24 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    formMethod.resetFields();
    productMethod.resetFields();

    formMethod.setFieldsValue(data);
    productMethod.setFieldsValue({
      time: data.artifacts?.time,
    });

    setDrawerProps({ confirmLoading: false });
    detail.value = data;
    inputDetail.tags = data.tags;
    inputDetail.script = data.script;
    inputDetail.paths = data.artifacts?.paths || [];
  });

  function del() {
    if (flow.stages.length > 1) {
      createConfirm({
        title: '确认删除吗？',
        type: 'error',
        iconType: 'error',
        content: '该阶段及其任务将被彻底删除',
        onOk() {
          flow.stages.splice(detail.value.index, 1);
          updateStages();
          closeDrawer();
        },
      });
    } else {
      createMessage.error('最少保留一个阶段！');
    }
  }

  function visibleChange(visible: boolean) {
    if (visible) {
    } else {
    }
  }

  function handleSubmit() {
    setDrawerProps({ confirmLoading: true });
    formMethod
      .validate()
      .then((values) => {
        if (!inputDetail.script.length) {
          createMessage.error('脚本命令是必填项！');
          setDrawerProps({ confirmLoading: false });
          return;
        }
        productMethod
          .validate()
          .then((time) => {
            const params = {
              ...detail.value,
              ...values,
              ...inputDetail,
              ...inputDetail.exceptAndOnly,
              artifacts: {
                ...detail.value.artifacts,
                ...time,
                paths: inputDetail.paths,
              },
              cache: {
                ...detail.value.cache,
                paths: inputDetail.cache,
              },
            };
            delete params.exceptAndOnly;
            delete params.paths;
            if (params.id === 999999) {
              // 新增
              if (params.index) {
                // 插入
                flow.stages.splice(params.index, 0, {
                  ...params,
                  id: null,
                });
              } else {
                // 添加到最后面
                flow.stages.push({
                  ...params,
                  index: flow.stages.length,
                  id: null,
                });
              }
            } else {
              flow.stages.splice(params.index, 1, params);
            }
            updateStages();

            createMessage.success('暂存成功，保存后生效！');
            closeDrawer();
          })
          .finally(() => {
            setDrawerProps({ confirmLoading: false });
          });
      })
      .catch(() => {
        setDrawerProps({ confirmLoading: false });
      });
  }
</script>
<style lang="less" scoped></style>
