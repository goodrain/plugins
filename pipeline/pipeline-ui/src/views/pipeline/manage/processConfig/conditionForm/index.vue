<template>
  <div>
    <Divider orientation="left">
      <div class="text-sm">执行条件</div>
    </Divider>
    <Collapse ghost class="condition" v-model:activeKey="activeKey">
      <Panel key="1">
        <template #header>
          <div class="flex flex-row items-center">
            <div class="pr-2">分支</div>
            <Tooltip>
              <template #title
                >该任务执行的分支条件。例如：仅当针对master分支，或排除master分支</template
              >
              <QuestionCircleOutlined />
            </Tooltip>
          </div>
        </template>
        <Refs @change="refsChange" :detail="detail" :conditionOptions="conditionOptions" />
      </Panel>
      <Panel key="2">
        <template #header>
          <div class="flex flex-row items-center">
            <div class="pr-2">变量</div>
            <Tooltip>
              <template #title
                >该任务执行的变量条件。仅当所定义条件成立时执行，或排除所定义的条件时执行</template
              >
              <QuestionCircleOutlined />
            </Tooltip>
          </div>
        </template>
        <Variables
          @change="variablesChange"
          :detail="detail"
          :conditionOptions="conditionOptions"
        />
      </Panel>
    </Collapse>
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-12 14:45:46
   * description : 执行条件表单
   */
  import { ref, watch } from 'vue';
  import { Collapse, Divider, Tooltip } from 'ant-design-vue';
  import { QuestionCircleOutlined } from '@ant-design/icons-vue';
  import Refs from './Refs.vue';
  import Variables from './Variables.vue';

  const { Panel } = Collapse;

  const conditionOptions = [
    {
      label: '仅当',
      value: 'only',
    },
    {
      label: '排除',
      value: 'except',
    },
  ];

  const emits = defineEmits(['change']);
  const props = defineProps({
    detail: { type: Object as any, required: true },
  });

  const activeKey = ref(['1', '2']);
  const condition = {
    only: {
      refs: [],
      variables: [],
      ...props.detail.only,
    },
    except: {
      refs: [],
      variables: [],
      ...props.detail.except,
    },
  };

  function variablesChange(value) {
    condition[value.filed].variables = value.variables.map(({ value }) => value);
    emits('change', condition);
  }
  function refsChange(value) {
    condition[value.filed].refs = value.refs.map(({ value }) => value);
    emits('change', condition);
  }
  watch(
    () => props.detail,
    () => {
      condition.except = {
        ...condition.except,
        ...props.detail.except,
      };
      condition.only = {
        ...condition.only,
        ...props.detail.only,
      };
      emits('change', condition);
    },
    {
      immediate: true,
    },
  );
</script>
<style lang="less">
  .condition {
    .ant-collapse-item {
      .ant-collapse-header {
        padding: 0;
      }
    }
  }
</style>
