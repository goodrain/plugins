<template>
  <Form ref="formRef" :model="dynamicValidateForm" layout="vertical">
    <FormItem
      v-for="(branch, index) in dynamicValidateForm.runners"
      :key="branch.key"
      :name="['branch', index, 'value']"
      :rules="{
        validator: () => validator(branch.value),
      }"
    >
      <template #label v-if="index === 0">
        <span class="pr-2">Runner</span>
        <Tooltip title="指定该任务在哪个runner执行，需填写注册runner时的tags">
          <QuestionCircleOutlined />
        </Tooltip>
      </template>
      <div class="flex flex-row items-center">
        <Input class="flex-1" placeholder="请输入" v-model:value="branch.value" @input="change" />
        <MinusCircleFilled
          v-if="dynamicValidateForm.runners.length > 1"
          class="dynamic-delete-button ml-2 text-2xl !text-red-500"
          :disabled="dynamicValidateForm.runners.length === 1"
          @click="removeRunner(branch)"
        />
      </div>
    </FormItem>
    <FormItem>
      <a-button @click="addRunner()">
        <PlusOutlined />
        添加
      </a-button>
    </FormItem>
  </Form>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-12 14:45:46
   * description : 执行条件表单
   */
  import { MinusCircleFilled, PlusOutlined, QuestionCircleOutlined } from '@ant-design/icons-vue';
  import { Form, Input, Tooltip } from 'ant-design-vue';
  import { ref, reactive, watch } from 'vue';
  const FormItem = Form.Item;

  const emits = defineEmits(['change']);

  const props = defineProps({
    tags: { type: Array as any },
  });

  const formRef = ref<any>();
  const dynamicValidateForm = reactive<{ runners: any[] }>({
    runners: [],
  });

  function addRunner(value = '') {
    dynamicValidateForm.runners.push({
      value,
      key: Date.now(),
    });
  }
  function removeRunner(item) {
    let index = dynamicValidateForm.runners.indexOf(item);
    if (index !== -1) {
      dynamicValidateForm.runners.splice(index, 1);
    }
  }

  function validator(value) {
    if (value) {
      const values = dynamicValidateForm.runners.filter((item) => item.value === value);
      if (values.length > 1) {
        return Promise.reject('名称不能重复！');
      }
    }
    return Promise.resolve();
  }

  function change() {
    emits(
      'change',
      dynamicValidateForm.runners.map((item) => item.value),
    );
  }
  watch(
    () => props.tags,
    (tags) => {
      dynamicValidateForm.runners = [];
      if (tags && tags.length > 0) {
        tags.forEach((val) => {
          addRunner(val);
        });
      } else {
        addRunner();
      }
    },
    {
      immediate: true,
    },
  );
</script>
