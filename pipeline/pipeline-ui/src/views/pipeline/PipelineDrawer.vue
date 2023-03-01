<template>
  <BasicDrawer
    v-bind="$attrs"
    @visible-change="visibleChange"
    showFooter
    title="选择流水线模板"
    width="70%"
    @ok="handleSubmit"
    @register="registerPipDrawer"
    okText="创建"
    wrapClassName="pipline-drawer"
    :ok-button-props="okButtonProp"
  >
    <div class="pipline-module">
      <div class="left-menu">
        <ul>
          <li
            v-for="(item, index) in list"
            :class="{ active: item.id === current.id }"
            :key="index"
            @click="menuSelect(item)"
          >
            <SvgIcon :name="item.icon" size="25" />
            {{ item.name }}
          </li>
        </ul>
      </div>
      <div ref="contentRef" class="right-content">
        <ul>
          <li
            v-for="(item, index) in list"
            @click="contentSelect(item)"
            :class="[{ active: item.id === current.id }, 'content' + item.id]"
            :key="index"
          >
            <div class="name">
              <SvgIcon :name="item.icon" size="25" />
              {{ item.description }}
            </div>
            <div class="flow-box">
              <div class="box" v-for="(stage, index) in item.stages" :key="index">
                <a-button class="btn">{{ stage }}</a-button>
                <div class="line"></div>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </BasicDrawer>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-07-27 16:02:25
   * description : 部署中件间
   */
  import { ref, reactive } from 'vue';
  import { BasicDrawer } from '/@/components/Drawer';
  import { SvgIcon } from '/@/components/Icon';
  import { useDrawer } from '/@/components/Drawer';
  import { router } from '/@/router';
  import { getPipelineTempList } from '/@@/api/pipelineApi';
  const [registerPipDrawer, { closeDrawer }] = useDrawer();

  const contentRef = ref<any>(null);
  const current = ref<any>({});

  const emits = defineEmits(['success']);

  const list = ref<any>([]);
  const okButtonProp = reactive({
    disabled: true,
  });

  function menuSelect(item) {
    current.value = item;
    okButtonProp.disabled = false;
    const content = document.querySelector('.content' + item.id) as HTMLElement;
    const ofssetTop = content.offsetTop + content.clientHeight;
    //const scrollHeight = contentRef.value.scrollHeight;
    const clientHeight = contentRef.value.clientHeight;
    const height = ofssetTop - clientHeight;
    // contentRef.value.style.transform = 'translateY(-' + height + 'px)';
    contentRef.value.scrollTop = height;
    // console.log(height, scrollHeight, clientHeight, ofssetTop);
  }
  function contentSelect(item) {
    current.value = item;
    okButtonProp.disabled = false;
  }

  getPipelineTempList().then((res) => {
    list.value = res.map((item) => {
      return {
        ...item,
        icon: ['War', 'gradle'].includes(item.name) ? 'maven单模块' : item.name.toLocaleLowerCase(),
      };
    });
  });

  function handleSubmit() {
    emits('success');
    closeDrawer();
    router.push({ name: 'pipelineManage', query: { id: current.value.id } });
  }
  function visibleChange(show: boolean) {
    if (show) {
    } else {
      current.value = {};
      okButtonProp.disabled = true;
    }
  }
</script>
<style lang="less" scoped>
  .pipline-module {
    display: flex;
    flex: 1;
    overflow: hidden;

    .left-menu {
      width: 200px;
      border-right: 1px solid #dde4ea;

      li {
        margin: 5px 0;
        display: flex;
        height: 48px;
        align-items: center;
        padding-left: 20px;
        border-left: 3px solid transparent;
        cursor: pointer;

        svg {
          margin-right: 5px;
        }

        &:hover,
        &.active {
          border-color: #0070ff;
          background: rgba(0, 112, 255, 0.1);
        }
      }
    }

    .right-content {
      flex: 1;
      overflow-y: auto;
      transition-duration: 0.3s;
      padding-left: 20px;

      li {
        margin-bottom: 20px;
        background: #ffffff;
        box-shadow: 0px 0px 6px 0px rgba(8, 21, 58, 0.12);
        border-radius: 4px;

        &.active {
          border: 1px solid #0070ff;
        }
      }

      .name {
        display: flex;
        align-items: center;
        padding: 10px 20px;
        background: #f6fbfe;
        border-radius: 4px 4px 0px 0px;

        svg {
          margin-right: 5px;
        }
      }

      .flow-box {
        display: flex;
        flex-direction: row;
        align-items: center;
        padding: 20px;

        .box {
          display: flex;
          flex-direction: row;
          align-items: center;

          &:last-child {
            .line {
              display: none;
            }
          }
        }

        .btn {
          background: #f1f9ff;
          border-radius: 16px;
          border: none;
          font-size: 14px;
          font-weight: 400;
          color: rgba(0, 0, 0, 0.85);
        }

        .line {
          width: 50px;
          height: 1px;
          background-color: #a9abad;
        }
      }
    }
  }
</style>
<style lang="less">
  .pipline-drawer {
    .scrollbar__wrap {
      height: 100%;
      display: flex;
      overflow: hidden !important;

      .scrollbar__view {
        flex: 1;
        display: flex;
      }
    }
  }
</style>
