<template>
  <BasicDrawer
    v-bind="$attrs"
    @register="registerDrawer"
    showFooter
    :title="getTitle"
    width="40%"
    @ok="handleSubmit"
    @visible-change="handleChange"
  >
    <BasicForm @register="registerForm">
      <template #serviceChildren="{ model, field }">
        <Row v-for="(item, index) in serviceChildren" :key="item.id" class="mb-2">
          <Col :span="18">
            <a-input
              v-model:value="item.code"
              @change="setMavenChild(model, field)"
              placeholder="请输入"
            />
          </Col>
          <Col :span="4" class="text-right">
            <Button @click="addChildren" type="primary">
              <template #icon><PlusOutlined /></template>
              新增
            </Button>
          </Col>
          <Col :span="2" class="pl-4" v-if="serviceChildren.length > 1">
            <MinusCircleOutlined
              @click="removeChildren(index, model, field)"
              style="font-size: 22px; padding-top: 5px"
            />
          </Col>
        </Row>
      </template>
      <!-- 流水线选择及新增流水线 新增 -->
      <template #pipeline="{ model, field }">
        <Row>
          <Col :span="18">
            <Select placeholder="请选择" :defaultValue="model[field]" v-model:value="model[field]">
              <Select.Option v-for="item in pipelineList" :key="item.id" :value="item.id">{{
                item.name
              }}</Select.Option>
            </Select>
          </Col>
          <Col :span="6">
            <a-button @click="goPipeline">新建流水线</a-button>
          </Col>
        </Row>
      </template>
      <!-- end 流水线选择及新增流水线 -->
      <!-- gitlab配置相关 -->
      <template #oauthConfig="{ model, field }">
        <div class="oauth-config">
          <div
            class="item-col"
            :class="{ active: model[field] && model[field].id === item.id }"
            v-for="item in authConfigOptions"
            @click="setOauthConfig(item, model, field)"
            :key="item.id"
          >
            <div class="title">{{ item.title }}</div>
            <div class="content">{{ item.content }}</div>
          </div>
        </div>
      </template>
      <template #test>
        <Button type="primary" @click="check">测试连通</Button>
        <div class="test success" v-if="test.status === 1">
          <CheckCircleOutlined style="color: green" :size="25" /> 检验成功
        </div>
        <div class="test fail" v-if="test.status === -1">
          <CloseCircleOutlined style="color: red" :size="25" /> 检验失败
          <span class="info" @click="openGitLabLog">查看错误信息</span>
        </div>
      </template>
      <!-- end gitlab配置相关 -->
      <!-- 子服务 自动构建 -->
      <template #subAppService>
        <Tabs>
          <TabPane v-for="item of envList" :key="item.id" :tab="item.regionAlias">
            <CheckboxGroup
              v-model="subAppServices[item?.regionName]"
              @change="(val) => selectServiceChildren(item?.regionName, val)"
              v-if="serviceChildren.length != 0"
              :options="serviceChildren.map((item) => item.code)"
            />
            <span v-else class="gray">暂无可自动部署的子服务</span>
          </TabPane>
        </Tabs>
      </template>
      <!-- end 子服务 自动构建 -->
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-03-22 10:50:05
   * description : 新增或修改
   */
  import { ref, computed, unref, reactive } from 'vue';
  import { Row, Col, Button, Checkbox, Tabs, Select, Input } from 'ant-design-vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import {
    formSchema,
    authConfigOptions,
    serviceSourcesOptions,
    serviceTypOptions,
    pipeLineTypeOptions,
  } from './service.data';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { useMessage } from '/@/hooks/web/useMessage';
  import {
    PlusOutlined,
    MinusCircleOutlined,
    CheckCircleOutlined,
    CloseCircleOutlined,
  } from '@ant-design/icons-vue';
  import { useEnvStore } from '/@@/store/modules/envStore';
  import { checkGitlab, update, serviceDetail } from '/@@/api/applicationServiceApi';
  import { getTemplate } from './useService';
  import { useProjectStore } from '/@@/store/modules/projectStore';
  import { MavenEnum } from '/@@/enums/mavenEnum';
  import { getPipelineList } from '/@@/api/pipelineApi';
  import { router } from '/@/router';

  const { createMessage, createConfirm } = useMessage();
  const TabPane = Tabs.TabPane;
  const CheckboxGroup = Checkbox.Group;

  const projectStore = useProjectStore();

  const envStore = useEnvStore();
  envStore.setList();

  const emit = defineEmits(['success']);

  const envList = computed(() => envStore.getList);

  const serviceChildren = ref([{ id: 0, code: '' }]);
  // 子服务是一个数组，收集子服务不同环境的数据
  const subAppServices = ref({});
  const test = reactive({
    status: 0,
    log: '',
  });
  // 流水线
  const pipelineList: any = ref([]);

  const isUpdate = ref(true);
  let propsRecord: any = ref({});
  const [
    registerForm,
    { resetFields, setFieldsValue, updateSchema, validate, validateFields, getFieldsValue },
  ] = useForm({
    labelWidth: 150,
    schemas: formSchema({
      // 服务来源
      productTypeChange(val) {
        if (val === serviceSourcesOptions[0].value) {
          updateSchema({
            field: 'serviceType',
            componentProps: {
              options: serviceTypOptions,
            },
          });
        } else {
          // 只能内置
          updateSchema({
            field: 'serviceType',
            componentProps: {
              options: [serviceTypOptions[0]],
            },
          });
        }
      },
    }),
    showActionButtonGroup: false,
    baseColProps: { lg: 22, md: 22 },
  });

  function goPipeline() {
    router.push('/pipeline');
  }

  const [registerDrawer, { setDrawerProps, closeDrawer, changeLoading }] = useDrawerInner(
    async (data) => {
      resetFields();
      isUpdate.value = data.isUpdate;
      setDrawerProps({ confirmLoading: false });
      propsRecord.value = data.record;

      // 添加服务模版数据
      getTemplate().then((res: any) => {
        const options = res.map((label) => {
          return {
            label,
            value: label,
          };
        });
        updateSchema({
          field: 'type',
          componentProps: {
            options,
          },
        });
      });

      // 选择环境
      updateSchema([
        {
          field: 'regionCodes',
          componentProps: {
            options: envStore.getList.map((item) => {
              return {
                label: item?.regionAlias,
                value: item?.regionName,
              };
            }),
          },
        },
      ]);

      // 流水线
      getPipelineList(projectStore.getCurrent.id).then((res) => {
        pipelineList.value = res;
      });

      if (data.record.id) {
        changeLoading(true);
        serviceDetail(data.record.id)
          .then((res) => {
            setFieldsValue({
              url: res.gitlabCodeUrl,
              ...res,
            });
            if (res.pipelineName && res.pipelineId) {
              // 因为已选择的流水线不在显示，所以在这里查询到后需要加进去
              pipelineList.value.push({
                id: res.pipelineId,
                name: res.pipelineName,
              });
            }
            if (res.subAppServiceDeployEnvVOS) {
              res.subAppServiceDeployEnvVOS.forEach((item) => {
                subAppServices.value[item.regionCode] = item.subAppServiceEntities
                  .filter((sub) => sub.autoDeploy === true)
                  .map(({ code }) => code);
              });
              serviceChildren.value = res.subAppServiceDeployEnvVOS[0].subAppServiceEntities;
            }
          })
          .finally(() => {
            changeLoading(false);
          });
      } else {
      }

      updateSchema([
        {
          field: 'code',
          show: !isUpdate.value,
        },
        {
          field: 'productType',
          required: !isUpdate.value,
          show: false,
        },
        {
          field: 'serviceType',
          show: false,
        },
        {
          field: 'type',
          show: !isUpdate.value,
        },
        {
          field: 'url',
          show: !isUpdate.value,
        },
        {
          field: 'oauthConfig',
          show: !isUpdate.value,
        },
        {
          field: 'username',
          show: !isUpdate.value,
        },
        {
          field: 'password',
          show: !isUpdate.value,
        },
        {
          field: 'token',
          show: !isUpdate.value,
        },
        {
          field: 'test',
          show: !isUpdate.value,
        },
        {
          field: 'autoBuild',
          ifShow: isUpdate.value,
        },
        {
          field: 'autoDeploy',
          ifShow: isUpdate.value,
        },
        /* {
          field: 'pipelineType',
          componentProps: {
            // 自定义时可以编辑
            disabled: isUpdate.value && data.record.pipelineType === pipeLineTypeOptions[1].value,
          },
        }, */
        /* {
          field: 'pipeline_id', // 只能编辑时操作
          ifShow: ({ values }) => {
            return values.pipelineType === pipeLineTypeOptions[0].value;
          },
        }, */
      ]);
    },
  );

  const getTitle = computed(() => (!unref(isUpdate) ? '创建应用服务' : '编辑应用服务'));

  // 添加子服务
  function addChildren() {
    serviceChildren.value.push({
      id: serviceChildren.value.length,
      code: '',
    });
  }
  // 移除子服务
  function removeChildren(index, model, field) {
    serviceChildren.value.splice(index, 1);
    model[field] = JSON.stringify(serviceChildren.value);
  }
  // 子服务添加内容
  function setMavenChild(model, field) {
    model[field] = JSON.stringify(serviceChildren.value);
  }
  // 选择部署环境
  function selectServiceChildren(regionCode, val) {
    subAppServices.value[regionCode] = val;
  }

  // 认证配置
  function setOauthConfig(item, model, field) {
    model[field] = item;
    test.status = 0;
  }

  // 验证信息
  function check() {
    if (isUpdate.value) return Promise.resolve();
    const fields = getFieldsValue();
    // if (fields.serviceType === serviceTypOptions[0].value) {
    //   return Promise.resolve();
    // }
    return new Promise((resolve, reject) => {
      // 验证验证码或token是否通过
      let fileds = ['url', 'token'];
      if (fields.oauthConfig.id === 0) {
        fileds = ['url', 'username', 'password'];
      }
      validateFields(fileds)
        .then((values) => {
          changeLoading(true);
          checkGitlab(values)
            .then((res) => {
              test.status = 1;
              resolve(res);
            })
            .catch((err: Error) => {
              test.status = -1;
              test.log = err.message;
              reject();
            })
            .finally(() => {
              changeLoading(false);
            });
        })
        .catch(reject);
    });
  }

  // 查看错误信息
  function openGitLabLog() {
    createConfirm({
      iconType: 'error',
      title: '错误日志',
      content: test.log,
    });
  }

  function handleChange(visible: boolean) {
    if (!visible) {
      serviceChildren.value = [{ id: 0, code: '' }];
    }
  }

  function handleSubmit() {
    validate().then((values) => {
      check()
        .then(() => {
          setDrawerProps({ confirmLoading: true });
          const params = {
            teamId: projectStore.getCurrent.id,
            teamCode: projectStore.getCurrent.teamCode,
            ...propsRecord.value,
            ...values,
            autoBuild: !!values.autoBuild,
            autoDeploy: !!values.autoDeploy,
          };
          delete params.serviceChildren;
          delete params.oauthConfig;
          delete params.test;
          if (params.id) {
            delete params.type;
            delete params.canDeploy;
            // 子服务 不同环境 是否自动构建
            params?.subAppServiceDeployEnvVOS?.forEach((sub) => {
              for (const [key, value] of Object.entries(subAppServices.value)) {
                if (sub.regionCode === key) {
                  sub.subAppServiceEntities.forEach((item) => {
                    const arr: any = value;
                    const isVal = arr.find((val) => val === item.code);
                    item.autoDeploy = !!isVal;
                  });
                }
              }
            });
          } else {
            if (params.type === true) {
              params.subAppServices = serviceChildren.value.map(({ code }) => {
                return {
                  code,
                };
              });
            }
          }
          update(params)
            .then(() => {
              createMessage.success(propsRecord.value?.id ? '修改成功！' : '添加成功！');
              emit('success');
              closeDrawer();
            })
            .finally(() => {
              setDrawerProps({ confirmLoading: false });
            });
        })
        .catch(() => {
          setDrawerProps({ confirmLoading: false });
        });
    });
  }
</script>
<style lang="less" scoped>
  .oauth-config {
    display: flex;
    overflow: hidden;

    .item-col {
      width: 49.99%;
      float: left;
      padding: 10px 15px;
      border: 1px solid #ddd;
      box-sizing: border-box;
      cursor: pointer;

      &.active {
        border-color: #2953e8;

        .title {
          color: #2953e8;
        }
      }
    }

    .title {
      font-size: 16px;
      font-weight: bold;
    }

    .content {
      font-size: 14px;
      line-height: 20px;
      color: #666;
    }
  }

  .test {
    display: inline-block;
    padding-left: 15px;
    font-size: 18px;

    &.success {
      color: forestgreen;
    }

    &.fail {
      .info {
        font-size: 14px;
        color: red;
        cursor: pointer;
      }
    }
  }
</style>
