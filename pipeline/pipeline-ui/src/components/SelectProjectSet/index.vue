<template>
  <Select v-model:value="value" placeholder="请选择" @change="change">
    {{ projectSetList }}
    <Select.Option v-for="item in projectSetList" :key="item.id" :value="item.id">{{
      item.name
    }}</Select.Option>
  </Select>
</template>
<script lang="ts" setup>
  /**
   * author : spion@qq.com
   * createTime ： 2022-10-13 14:45:41
   * description : 选择项目集
   */
  import { Select } from 'ant-design-vue';
  import { onMounted, ref, watch } from 'vue';
  import { getAllList } from '/@@/api/projectSetApi';

  const emits = defineEmits(['update:value']);
  const props = defineProps({
    value: Object as any,
  });

  const value = ref(props.value);

  // 项目集
  const projectSetList = ref<any>([]);

  function refreshProjectSet() {
    getAllList().then((res) => {
      projectSetList.value = res;
    });
  }

  function change(value) {
    emits('update:value', value);
  }

  watch(
    () => props.value,
    (val) => (value.value = val),
  );

  onMounted(() => {
    refreshProjectSet();
  });
</script>
<style lang="less" scoped></style>
