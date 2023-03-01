<template>
  <div>
    <Form :model="dynamicValidateForm" layout="vertical">
      <FormItem>
        <div class="flex flex-row items-center">
          <Select placeholder="请选择" class="!w-28" v-model:value="dynamicValidateForm.filed">
            <Select.Option v-for="item in conditionOptions" :value="item.value" :key="item.id">{{
              item.label
            }}</Select.Option>
          </Select>
          <div class="flex-1 pl-2"> 分支为： </div>
        </div>
      </FormItem>
      <FormItem
        label=""
        v-for="(refs, index) in dynamicValidateForm.refs"
        :name="['refs', index, 'value']"
        :rules="{
          validator: () => validator(refs.value),
        }"
        :key="index"
      >
        <div class="flex flex-row items-center">
          <Input class="flex-1" placeholder="请输入" v-model:value="refs.value" @input="setValue" />
          <MinusCircleFilled
            v-if="dynamicValidateForm.refs.length > 1"
            class="dynamic-delete-button ml-2 text-2xl !text-red-500"
            :disabled="dynamicValidateForm.refs.length === 1"
            @click="removeFiled(refs)"
          />
        </div>
      </FormItem>
      <FormItem>
        <a-button @click="addFiled()">
          <PlusOutlined />
          添加
        </a-button>
      </FormItem>
    </Form>
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-12 14:45:46
   * description : 分支表单
   */
  import { reactive, watch } from 'vue';
  import { Form, Input, Select } from 'ant-design-vue';
  import { PlusOutlined, MinusCircleFilled } from '@ant-design/icons-vue';

  const FormItem = Form.Item;

  const emits = defineEmits(['change']);
  const props = defineProps({
    conditionOptions: { type: Object as any, required: true },
    detail: { type: Object as any, required: true },
  });

  const dynamicValidateForm = reactive<{ filed: string | null; refs: any[] }>({
    filed: null,
    refs: [{ value: '', key: Date.now() }],
  });

  function addFiled(value?) {
    dynamicValidateForm.refs.push({
      value,
      key: Date.now(),
    });
  }
  function removeFiled(item) {
    let index = dynamicValidateForm.refs.indexOf(item);
    if (index !== -1) {
      dynamicValidateForm.refs.splice(index, 1);
    }
  }

  function setValue() {
    if (dynamicValidateForm.filed !== '') {
      emits('change', dynamicValidateForm);
    }
  }

  function validator(value) {
    if (value) {
      const values = dynamicValidateForm.refs.filter((item) => item.value === value);
      if (values.length > 1) {
        return Promise.reject('名称不能重复！');
      }
    }
    return Promise.resolve();
  }
  watch(
    () => props.detail,
    (detail) => {
      dynamicValidateForm.refs = [{ value: '', key: Date.now() }];
      if (detail?.only?.refs?.length) {
        dynamicValidateForm.filed = 'only';
        dynamicValidateForm.refs = detail.only.refs.map((value) => {
          return { value, key: Date.now() };
        });
      }
      if (detail?.except?.refs?.length) {
        dynamicValidateForm.filed = 'except';
        dynamicValidateForm.refs = detail.except.refs.map((value) => {
          return { value, key: Date.now() };
        });
      }
    },
    {
      immediate: true,
    },
  );
</script>
