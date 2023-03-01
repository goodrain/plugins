<template>
  <Card class="!mt-5 !pb-10">
    <div class="text-3xl py-10 text-center">欢迎使用流水线功能</div>
    <div class="flow-box">
      <div class="flow">
        <Image :src="img1" :width="284" :height="166" :preview="false" />
        <h2>01. 创建流水线</h2>
        <p>选择合适的模板</p>
        <a-button type="primary" @click="add">
          <div class="flex flex-row items-center">
            <PlusOutlined class="!text-xs" style="margin-top: -4px" />
            <div class="ml-2"> 创建流水线 </div>
          </div>
        </a-button>
      </div>
      <div class="flow">
        <Image :src="img2" :width="284" :height="166" :preview="false" />
        <h2>02. 配置流水线</h2>
        <p>填写必要参数</p>
      </div>
      <div class="arrow"></div>
      <div class="flow">
        <Image :src="img3" :width="284" :height="166" :preview="false" />
        <h2>03. 触发并查看结果</h2>
        <p>触发构建并查看步骤和日志</p>
      </div>
    </div>
  </Card>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-15 09:48:25
   * description : 流水线流程
   */
  import { Card, Image } from 'ant-design-vue';
  import img1 from '/@@/assets/images/pipeline/add1.png';
  import img2 from '/@@/assets/images/pipeline/add2.png';
  import img3 from '/@@/assets/images/pipeline/add3.png';
  import { PlusOutlined } from '@ant-design/icons-vue';

  const emits = defineEmits(['add']);
  function add() {
    emits('add');
  }
</script>
<style lang="less" scoped>
  .flow-box {
    display: flex;

    .flow {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    h2 {
      font-size: 16px;
      font-weight: 500;
      color: rgba(0, 0, 0, 0.85);
      line-height: 24px;
    }

    p {
      font-size: 14px;
      font-weight: 400;
      color: rgba(0, 0, 0, 0.35);
      line-height: 22px;
    }

    .arrow {
      display: flex;
      flex-direction: column;
      align-items: center;

      .anticon {
        font-size: 40px;
        color: #999;
      }
    }
  }
</style>
