<template>
  <BasicDrawer
    v-bind="$attrs"
    title="部署配置"
    width="50%"
    showFooter
    okText="开始构建"
    :showDetailBack="false"
    @register="registerDrawer"
    @ok="handleSubmit"
    :bodyStyle="{ background: '#E9F1F7' }"
  >
    <Card title="自定义环境变量" class="!mb-4" size="small">
      <template #extra>
        <Button @click="addField" type="primary">
          <template #icon><PlusOutlined /></template>
          新增
        </Button>
      </template>
      <BasicForm @register="register" :actionColOptions="{ span: 8 }">
        <template #add="{ field }">
          <div class="text-right">
            <Button @click="removeField(field)" danger>
              <template #icon>
                <MinusCircleOutlined />
              </template>
              移除
            </Button>
          </div>
        </template>
      </BasicForm>
    </Card>
  </BasicDrawer>
</template>
<script lang="ts" setup>
  import { ref, defineEmits } from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { Card, Button } from 'ant-design-vue';
  import { serviceRun, getEnvs } from '/@@/api/codeManageApi';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { formSchema } from './data';
  import { FormSchema } from '/@/components/Table';
  import { useAppServiceStore } from '/@@/store/modules/appServiceStore';

  const emits = defineEmits(['success']);
  const appServiceStore = useAppServiceStore();
  const { createMessage } = useMessage();

  const propItem = ref<any>({});
  let fieldValue = 0; // 新增表单

  const [register, formMethod] = useForm({
    schemas: [],
    labelWidth: 80,
    actionColOptions: { span: 24 },
    showActionButtonGroup: false,
  });

  const [registerDrawer, { closeDrawer, changeLoading }] = useDrawerInner((data: any) => {
    propItem.value = data;
    clearAllField();
    const params = {
      appServiceId: data.appServiceId,
      branch: data.branchName,
      module: data.module,
    };
    getEnvs(params).then((res) => {
      if (!res.length) return;
      res.forEach((item) => {
        const obj = {
          key: item.envKey,
          value: item.envValue,
        };
        addField(obj);
      });
    });
  });

  async function handleSubmit() {
    formMethod.validate().then((value) => {
      const variables: { key: string; value: string }[] = [];
      for (let i = 1; i <= fieldValue; i++) {
        variables.push({
          key: value['fielda' + i],
          value: value['fieldb' + i],
        });
      }
      const params = {
        appServiceId: appServiceStore.getCurrentService.id,
        branch: propItem.value.branchName,
        variables: variables,
        module: propItem.value.module,
      };
      changeLoading(true);
      serviceRun(params)
        .then(() => {
          createMessage.success('构建成功');
          closeDrawer();
          emits('success');
        })
        .finally(() => {
          changeLoading(false);
        });
    });
  }

  // 新增字段
  function addField(item) {
    fieldValue++;
    formSchema.forEach((field: FormSchema) => {
      let newField: string = field.field + fieldValue;
      if (field.field === '0') {
        newField = String(fieldValue);
      }
      let defaultValue: any = null;
      if (item) {
        if (field.field === 'fielda') {
          defaultValue = item.key;
        }
        if (field.field === 'fieldb') {
          defaultValue = item.value;
        }
      }
      formMethod.appendSchemaByField(
        {
          ...field,
          field: newField,
          defaultValue,
        },
        undefined,
      );
    });
  }

  // 移除
  function removeField(field) {
    formMethod.removeSchemaByFiled([
      `fielda${field}`,
      `fieldb${field}`,
      `fieldc${field}`,
      `${field}`,
    ]);
    fieldValue--;
  }

  // 清除所有变量
  function clearAllField() {
    while (fieldValue > 0) {
      removeField(fieldValue);
    }
  }
</script>
