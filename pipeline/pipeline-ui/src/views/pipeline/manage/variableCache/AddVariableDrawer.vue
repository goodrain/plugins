<template>
  <BasicDrawer v-bind="$attrs" showFooter width="500px" title="新增变量" @ok="handleSubmit">
    <div class="pr-5">
      <BasicForm @register="registerForm" />
    </div>
  </BasicDrawer>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-03-22 10:50:05
   * description : 新增或修改
   */
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { BasicDrawer } from '/@/components/Drawer';

  const emit = defineEmits(['success']);

  const [registerForm, formMethod] = useForm({
    labelCol: { span: 24 },
    schemas: [
      {
        field: 'key',
        label: 'key',
        component: 'Input',
        required: true,
      },
      {
        field: 'value',
        label: 'value',
        component: 'Input',
        required: true,
      },
      {
        field: 'desc',
        label: '说明',
        component: 'Input',
      },
    ],
    showActionButtonGroup: false,
    baseColProps: { span: 24 },
  });

  function handleSubmit() {
    formMethod.validate().then((values) => {
      emit('success', values);
      formMethod.resetFields();
    });
  }
</script>
<style lang="less" scoped></style>
