<template>
  <div class="flow-box">
    <div class="box">
      <template v-for="(item, index) in stages" :key="item.id">
        <div class="flow">
          <a-button
            class="!rounded-3xl !border-white !px-10 !hover:border-blue-600 !hover:text-blue-600"
            @click="openFlow(item)"
            >{{ item.name }}</a-button
          >
        </div>
        <div class="create-flow" v-if="index < stages.length - 1">
          <div class="line"></div>
          <Tooltip title="添加新的阶段">
            <PlusCircleFilled
              @click="openFlow(newFlow, index + 1)"
              class="!text-blue-600 !text-xl !-mt-2 cursor-pointer pt-3"
            />
          </Tooltip>
          <div class="line"></div>
        </div>
      </template>
      <div class="create-flow">
        <div class="line"></div>
        <div class="line"></div>
      </div>
      <div class="flow">
        <a-button
          class="!rounded-3xl !border-white !px-10 !hover:border-blue-600 !hover:text-blue-600"
          @click="openFlow(newFlow, stages.length)"
        >
          <!-- <div class="flex flex-row items-center">
            <PlusCircleFilled class="!text-blue-600 !text-xl" style="margin-top: -5px" />
            <div class="ml-2">
              {{ newFlow.name }}
            </div>
          </div> -->
          <PlusCircleFilled class="!text-blue-600 !text-xl" style="vertical-align: -0.25em" />
          <span> {{ newFlow.name }}</span>
        </a-button>
      </div>
    </div>
    <EditorDrawer @register="registerEditorDrawer" />
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-10 11:34:02
   * description : 流程配置
   */
  import { computed } from 'vue';
  import { PlusCircleFilled } from '@ant-design/icons-vue';
  import EditorDrawer from './EditorDrawer.vue';
  import { useDrawer } from '/@/components/Drawer';
  import { flow } from '../usePipline';
  import { Tooltip } from 'ant-design-vue';
  const [registerEditorDrawer, { openDrawer: openEditor }] = useDrawer();

  const stages = computed(() => flow.stages);

  const newFlow = {
    id: 999999,
    name: '新的阶段',
  };

  function openFlow(item, index?: number) {
    item.index = index === undefined ? item.index : index;
    openEditor(true, item);
  }
</script>
<style lang="less" scoped>
  .flow-box {
    padding: 50px;

    .box {
      display: flex;
      align-items: center;
    }

    .flow {
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    h2 {
      font-size: 25px;
      font-weight: bold;
    }

    .create-flow {
      display: flex;
      align-items: center;

      .line {
        width: 20px;
        height: 1px;
        background-color: #a9abad;
      }

      .add {
        font-size: 18px;
        cursor: pointer;
      }
    }
  }
</style>
