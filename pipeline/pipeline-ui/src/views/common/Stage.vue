<template>
  <div class="flex flex-row items-center justify-start">
    <div v-for="(item, index) in stages" :key="index" class="flex flex-row items-center">
      <Popover :title="getStatus(item.status).name">
        <template #content>
          <div v-for="(job, j) in item.jobs" :key="j">
            <Popover :title="job.name">
              <template #content>
                <div>
                  <p>开始时间：{{ getStatus(job.status).startTime || '-' }}</p>
                  <p>结束时间：{{ getStatus(job.status).endTime || '-' }}</p>
                </div>
              </template>
              <a :href="job.url" target="_blank">
                <Icon
                  :icon="getStatus(job.status).icon"
                  :color="getStatus(job.status).color"
                  style="font-size: 20px"
                />
              </a>
            </Popover>
          </div>
        </template>
        <div class="text-center cursor-pointer">
          <Icon
            :icon="getStatus(item.status).icon"
            :color="getStatus(item.status).color"
            style="font-size: 30px"
          />
          <br />
          {{ item.name }}
        </div>
      </Popover>
      <div
        style="width: 30px; margin: 0 3px; height: 25px; border-top: 1px solid #ddd"
        v-show="index !== stages.length - 1"
      ></div>
    </div>
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-23 10:20:56
   * description : 阶段
   */
  import { Popover } from 'ant-design-vue';
  import { Icon } from '/@/components/Icon';
  import { stageStatus } from '/@@/utils/lib/util';
  defineProps({
    stages: {
      type: Array as any,
      required: true,
      default: () => [],
    },
  });

  function getStatus(text) {
    return stageStatus[text] || stageStatus.running;
  }
</script>
<style lang="less" scoped></style>
