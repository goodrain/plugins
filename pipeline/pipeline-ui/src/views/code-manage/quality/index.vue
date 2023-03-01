<template>
  <Spin :spinning="loading">
    <div class="h-10 mt-4">
      <div class="float-left text-2xl">质量阈&nbsp;</div>
      <div class="float-left mt-2">
        <Tag
          style="font-weight: normal"
          size="large"
          type="border"
          :color="codeQuality.alert_status === 'OK' ? 'green' : 'red'"
          >{{ codeQuality.alert_status === 'OK' ? '通过' : '未通过' }}
        </Tag>
      </div>
      <div class="float-right ml-5"
        >最近一次分析：{{ codeQuality.git_branch || '-' }},{{ codeQuality.createTime || '-' }}</div
      >
      <div class="float-right text-blue-500"
        ><span class="cursor-pointer" @click="evaluateOpen">了解代码质量如何评价?</span></div
      >
      <div>&nbsp;</div>
    </div>
    <div class="md:flex">
      <Card class="md:w-2/4 w-full !md:mt-2">
        <template #title>
          <span class="text-2xl">可靠性</span>
        </template>

        <div class="p-5 px-20 flex justify-between items-center">
          <Badge
            :count="bugGrade(codeQuality.reliabilityRate)"
            :color="bugGradeColor(codeQuality.reliabilityRate)"
            ><h2
              ><span class="text-4xl" @click="openWindow(codeQuality.reliabilityBugsAmountUrl)">
                <CountTo
                  prefix=""
                  :startVal="0"
                  :endVal="Number(codeQuality.reliabilityBugsAmount || 0)" /></span
            ></h2>
            <h2>bugs&nbsp;&nbsp;&nbsp;</h2>
          </Badge>
          <Badge
            :count="bugGrade(codeQuality.securityRate)"
            :color="bugGradeColor(codeQuality.securityRate)"
            ><h2
              ><span
                class="text-4xl"
                @click="openWindow(codeQuality.reliabilitySecurityleakAmountUrl)"
              >
                <CountTo
                  prefix=""
                  :startVal="0"
                  :endVal="Number(codeQuality.reliabilitySecurityleakAmount || 0)"
                /> </span
            ></h2>
            <h2>安全漏洞</h2>
          </Badge>
          <span class="divider"></span>
          <Badge
            :count="bugGrade(codeQuality.new_reliabilityRate)"
            :color="bugGradeColor(codeQuality.new_reliabilityRate)"
            ><h2
              ><span
                class="text-4xl"
                @click="openWindow(codeQuality.reliability_new_bugs_amount_url)"
              >
                <CountTo
                  prefix=""
                  :startVal="0"
                  :endVal="Number(codeQuality.reliability_new_bugs_amount || 0)"
                /> </span
            ></h2>
            <h2>新增bugs</h2>
          </Badge>
          <Badge
            :count="bugGrade(codeQuality.new_securityRate)"
            :color="bugGradeColor(codeQuality.new_securityRate)"
            ><h2
              ><span
                class="text-4xl"
                @click="openWindow(codeQuality.reliability_new_securityleak_amount_url)"
              >
                <CountTo
                  prefix=""
                  :startVal="0"
                  :endVal="Number(codeQuality.reliability_new_securityleak_amount || 0)" /></span
            ></h2>
            <h2>新增漏洞</h2>
          </Badge>
        </div>
      </Card>
      <Card class="md:w-2/4 w-full !md:mt-2 !md:ml-5">
        <template #title>
          <span class="text-2xl">可维护性</span>
        </template>
        <div class="p-5 px-20 flex justify-between items-center">
          <Badge
            :count="bugGrade(codeQuality.sqaleRate)"
            :color="bugGradeColor(codeQuality.sqaleRate)"
          >
            <h2>
              <span
                class="text-4xl"
                @click="openWindow(codeQuality.maintainTechnicaldebtAmountUrl)"
              >
                <CountTo
                  prefix=""
                  :startVal="0"
                  :decimals="2"
                  :endVal="Number(dateFilter(codeQuality.maintainTechnicaldebtAmount || 0))"
                />
              </span>
              天</h2
            >
            <h2>技术债务</h2>
          </Badge>
          <Badge count="">
            <h2>
              <span class="text-4xl" @click="openWindow(codeQuality.maintainCodesmellAmountUrl)">
                <CountTo
                  prefix=""
                  :startVal="0"
                  :endVal="Number(codeQuality.maintainCodesmellAmount || 0)"
                />
              </span>
            </h2>
            <h2>代码异味</h2>
          </Badge>
          <span class="divider"></span>
          <Badge
            :count="bugGrade(codeQuality.new_maintainability_rate)"
            :color="bugGradeColor(codeQuality.new_maintainability_rate)"
            ><h2
              ><span
                class="text-4xl"
                @click="openWindow(codeQuality.maintain_new_technicaldebt_amount_url)"
              >
                <CountTo
                  prefix=""
                  :startVal="0"
                  :endVal="Number(codeQuality.maintain_new_technicaldebt_amount || 0)"
              /></span>
            </h2>
            <h2>新增技术债务&nbsp;&nbsp;&nbsp;</h2>
          </Badge>
          <Badge count=""
            ><h2
              ><span
                class="text-4xl"
                @click="openWindow(codeQuality.maintain_new_codesmell_amount_url)"
              >
                <CountTo
                  prefix=""
                  :startVal="0"
                  :endVal="Number(codeQuality.maintain_new_codesmell_amount || 0)"
                />
              </span>
            </h2>
            <h2>新增代码异味</h2>
          </Badge>
        </div>
      </Card>
    </div>
    <div class="md:flex">
      <Card class="md:w-4/4 w-full !md:mt-6">
        <template #title>
          <span class="text-2xl">重复度</span>
        </template>
        <div class="md:flex">
          <div
            class="p-5 px-50 flex justify-between items-center"
            style="width: 50%; border-right: 1px solid #ddd"
          >
            <Badge count="●" color="yellow"
              ><h2
                ><span class="text-4xl" @click="openWindow(codeQuality.newCodeRepeatRateUrl)">
                  <CountTo
                    prefix=""
                    :startVal="0"
                    :endVal="Number(codeQuality.newCodeRepeatRate || 0)"
                  />
                </span>
                %</h2
              >
              <h2>重复率&nbsp;&nbsp;&nbsp;</h2>
            </Badge>
            <Badge count=""
              ><h2
                ><span class="text-4xl" @click="openWindow(codeQuality.repeatBlockUrl)">
                  <CountTo
                    prefix=""
                    :startVal="0"
                    :endVal="Number(codeQuality.repeatBlock || 0)"
                  /> </span
              ></h2>
              <h2>重复块</h2>
            </Badge>
          </div>
          <div class="flex-1 text-center self-center">
            <Badge
              ><h2
                ><span class="text-4xl">
                  <CountTo
                    prefix=""
                    :startVal="0"
                    :endVal="Number(codeQuality.newCodeRepeatRate || 0)"
                  /> </span
                >%</h2
              >
              <h2></h2>
            </Badge>
          </div>
        </div>
      </Card>
    </div>
    <EvaluteDrawer @register="dcRegister" />
  </Spin>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-07-22 10:02:27
   * description : 代码质量
   */
  import { onMounted, watch, ref } from 'vue';
  import { useAppServiceStore } from '/@@/store/modules/appServiceStore';
  import { Tag, Card, Badge, Spin } from 'ant-design-vue';
  import { useDrawer } from '/@/components/Drawer';
  import EvaluteDrawer from './evaluate/evaluateDrawer.vue';
  import { findCodeQualityLatest } from '/@@/api/appServiceApi';
  import { CountTo } from '/@/components/CountTo/index';
  import { bugGrade, bugGradeColor, dateFilter } from '/@@/utils/lib/codeQuality';

  const [dcRegister, { openDrawer: openDcDrawer }] = useDrawer();

  const appServiceStore = useAppServiceStore();

  const loading = ref(false);
  const codeQuality = ref<any>({});
  function refresh() {
    if (appServiceStore.getCurrentService.id) {
      setCodeQuality();
    }
  }

  //获取代码质量
  function setCodeQuality() {
    loading.value = true;
    findCodeQualityLatest(appServiceStore.getCurrentService.id)
      .then((res) => {
        codeQuality.value = res || {};
      })
      .finally(() => {
        loading.value = false;
      });
  }

  function openWindow(url) {
    window.open(url);
  }

  function evaluateOpen() {
    openDcDrawer();
  }

  onMounted(() => {
    // 应用服务改变时
    watch(() => appServiceStore.getCurrentService, refresh, { immediate: true });
  });

  defineExpose({
    refresh,
  });
</script>
<style lang="less" scoped>
  .master {
    width: 30px;
    height: 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #eef2ff;
    color: #315aea;
    border-radius: 100%;
    font-size: 14px;
  }

  .divider {
    display: inline-block;
    height: 60px;
    width: 1px;
    background: #ddd;
  }
</style>
