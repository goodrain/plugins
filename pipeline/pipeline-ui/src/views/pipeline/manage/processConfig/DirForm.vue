<template>
  <Form :model="dynamicValidateForm" name="dir_form_item" layout="vertical">
    <FormItem
      v-for="(dir, index) in dynamicValidateForm.dirs"
      :key="dir.key"
      :name="['dir', index, 'value']"
      :rules="{
        validator: () => validator(dir.value),
      }"
    >
      <template #label v-if="index === 0">
        <span class="pr-2">制品目录</span>
        <Tooltip title="该任务阶段生成制品的存放目录">
          <QuestionCircleOutlined />
        </Tooltip>
      </template>
      <div class="flex flex-row items-center">
        <Input class="flex-1" placeholder="请输入" v-model:value="dir.value" @input="change" />
        <MinusCircleFilled
          v-if="dynamicValidateForm.dirs.length > 1"
          class="dynamic-delete-button ml-2 text-2xl !text-red-500"
          :disabled="dynamicValidateForm.dirs.length === 1"
          @click="removeDir(dir)"
        />
      </div>
    </FormItem>
    <FormItem>
      <a-button @click="addDir()">
        <PlusOutlined />
        添加
      </a-button>
    </FormItem>
  </Form>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-18 16:16:53
   * description : 目录
   */
  import { MinusCircleFilled, PlusOutlined, QuestionCircleOutlined } from '@ant-design/icons-vue';
  import { Form, Input, Tooltip } from 'ant-design-vue';
  import { reactive, watch } from 'vue';
  const FormItem = Form.Item;

  const emits = defineEmits(['change']);

  const props = defineProps({
    paths: { type: Array as any },
  });

  const dynamicValidateForm = reactive<{ dirs: any[] }>({
    dirs: [{ value: '', key: Date.now() }],
  });

  function addDir(value = '') {
    dynamicValidateForm.dirs.push({
      value,
      key: Date.now(),
    });
  }
  function removeDir(item) {
    let index = dynamicValidateForm.dirs.indexOf(item);
    if (index !== -1) {
      dynamicValidateForm.dirs.splice(index, 1);
    }
  }

  function change() {
    emits(
      'change',
      dynamicValidateForm.dirs.map((item) => item.value),
    );
  }

  function validator(value) {
    if (value) {
      const values = dynamicValidateForm.dirs.filter((item) => item.value === value);
      if (values.length > 1) {
        return Promise.reject('名称不能重复！');
      }
    }
    return Promise.resolve();
  }

  /* onMounted(() => {
    dynamicValidateForm.dirs = [{ value: '', key: Date.now() }];
  }); */

  watch(
    () => props.paths,
    (paths) => {
      dynamicValidateForm.dirs = [];
      if (paths) {
        paths.forEach((val) => {
          addDir(val);
        });
      } else {
        dynamicValidateForm.dirs = [{ value: '', key: Date.now() }];
      }
    },
    {
      immediate: true,
    },
  );
</script>
