<template>
  <BasicDrawer
    v-bind="$attrs"
    @register="registerDrawer"
    showFooter
    title="添加子服务"
    width="40%"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm">
      <template #serviceChildren="{ model, field }">
        <Row v-for="(item, index) in serviceChildren" :key="item.id" class="mb-2">
          <Col :span="18">
            <Input
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
              @click="removeChildren(index)"
              style="font-size: 22px; padding-top: 5px"
            />
          </Col>
        </Row>
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-07-27 16:02:25
   * description : 添加子服务
   */
  import { ref, defineEmits } from 'vue';
  import { addChildrenService } from '/@@/api/applicationServiceApi';
  import { Row, Col, Button, Input } from 'ant-design-vue';
  import { PlusOutlined, MinusCircleOutlined } from '@ant-design/icons-vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { useMessage } from '/@/hooks/web/useMessage';

  const emit = defineEmits(['success']);

  const { createMessage } = useMessage();
  let propsRecord: any = {};

  const serviceChildren = ref([{ id: 0, code: '' }]);

  const [registerForm, { resetFields, validate, setFieldsValue }] = useForm({
    labelWidth: 150,
    schemas: [
      {
        field: 'code',
        label: '服务编码',
        component: 'Input',
        dynamicDisabled: true,
      },
      {
        field: 'name',
        label: '服务名称',
        component: 'Input',
        dynamicDisabled: true,
      },
      {
        field: 'serviceChildren',
        label: '子服务编码',
        component: 'Input',
        slot: 'serviceChildren',
        rules: [
          {
            required: true,
          },
          {
            validator: (_, value) => {
              if (value && serviceChildren.value.length) {
                let isTrime = false;
                const arr = serviceChildren.value.map((item) => {
                  if (item.code === '' || item.code.trim() === '') {
                    isTrime = true;
                  }
                  return item.code;
                });
                if (isTrime) {
                  return Promise.reject('请输入子服务编码！');
                } else if (new Set(arr).size != arr.length) {
                  return Promise.reject('子服务编码重复！');
                }
              }
              return Promise.resolve();
            },
          },
        ],
      },
    ],
    showActionButtonGroup: false,
    baseColProps: { lg: 22, md: 22 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    resetFields();
    setDrawerProps({ confirmLoading: false });
    propsRecord = data.record;

    if (data.record.id) {
      setFieldsValue(data.record);
    }

    serviceChildren.value = [{ id: 0, code: '' }];
  });

  // 添加子服务
  function addChildren() {
    serviceChildren.value.push({
      id: serviceChildren.value.length,
      code: '',
    });
  }
  // 移除子服务
  function removeChildren(index) {
    serviceChildren.value.splice(index, 1);
  }
  // 子服务添加内容
  function setMavenChild(model, field) {
    model[field] = JSON.stringify(serviceChildren.value);
  }

  function handleSubmit() {
    validate()
      .then(() => {
        setDrawerProps({ confirmLoading: true });
        addChildrenService(
          propsRecord.id,
          serviceChildren.value.map(({ code }) => code),
        )
          .then(() => {
            createMessage.success('添加成功！');
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
  }
</script>
<style lang="less" scoped></style>
