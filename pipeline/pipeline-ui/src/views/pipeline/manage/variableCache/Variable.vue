<template>
  <div>
    <BasicTable @register="registerTable" @edit-change="onEditChange" class="!m-0 !p-0">
      <template #tableTitle>
        <a-button type="primary" @click="openDrawer"> <PlusOutlined />添加变量 </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction :actions="createActions(record, column)" />
        </template>
      </template>
    </BasicTable>
    <AddVariableDrawer @register="registerEditorDrawer" @success="addVar" />
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-10 11:38:52
   * description : 变量
   */
  import { onMounted, ref, watch } from 'vue';
  import {
    BasicTable,
    useTable,
    TableAction,
    BasicColumn,
    ActionItem,
    EditRecordRow,
  } from '/@/components/Table';

  import { useMessage } from '/@/hooks/web/useMessage';
  import { columns } from './variable.data';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import AddVariableDrawer from './AddVariableDrawer.vue';
  import { useDrawer } from '/@/components/Drawer';
  import { flow } from '../usePipline';
  import { cloneDeep } from 'lodash-es';
  const [registerEditorDrawer, { openDrawer, closeDrawer }] = useDrawer();

  const { createMessage: msg } = useMessage();
  const currentEditKeyRef = ref('');

  const [registerTable, { setTableData }] = useTable({
    titleHelpMessage: [
      '本例中修改[数字输入框]这一列时，同一行的[远程下拉]列的当前编辑数据也会同步发生改变',
    ],
    columns: columns,
    showIndexColumn: false,
    showTableSetting: true,
    canResize: false,
    pagination: {
      pageSize: 999,
      hideOnSinglePage: true,
    },
    actionColumn: {
      width: 160,
      title: '操作',
      dataIndex: 'action',
      // slots: { customRender: 'action' },
    },
  });

  function addVar(values) {
    const isKey = flow.variables?.some((item) => item.key === values.key);
    if (isKey) {
      msg.error('变量名不能重复！');
      return;
    }
    closeDrawer();
    flow.variables?.push({
      ...values,
      id: null,
      itemKey: values.key,
      index: flow.variables?.length,
    });
  }

  function handleDel(record: EditRecordRow) {
    flow.variables.splice(record.index, 1);
  }

  function handleEdit(record: EditRecordRow) {
    currentEditKeyRef.value = record.key;
    record.onEdit?.(true);
  }

  function handleCancel(record: EditRecordRow) {
    currentEditKeyRef.value = '';
    record.onEdit?.(false, false);
  }

  async function handleSave(record: EditRecordRow) {
    // 校验
    msg.loading({ content: '正在保存...', duration: 0, key: 'saving' });
    const valid = true || (await record.onValid?.());
    if (valid) {
      try {
        const data: any = {
          desc: record.desc,
          itemKey: record.itemKey,
          value: record.value,
          index: record.index,
        };
        //TODO 此处将数据提交给服务器保存
        // ...
        // 保存之后提交编辑状态
        const keys = flow.variables.filter((item) => item.key === data?.itemKey);
        if (keys && keys.length > 1) {
          msg.error({ content: '变量名不能重复！', key: 'saving' });
          return;
        } else {
          //const pass = await record.onEdit?.(false, true);
          if (true) {
            currentEditKeyRef.value = '';
            flow.variables.splice(data.index, 1, {
              ...data,
              key: data.itemKey,
            });
          }
          msg.success({ content: '数据已保存', key: 'saving' });
        }
      } catch (error) {
        msg.error({ content: '保存失败', key: 'saving' });
      }
    } else {
      msg.error({ content: '请填写正确的数据', key: 'saving' });
    }
  }

  function createActions(record: EditRecordRow, column: BasicColumn): ActionItem[] {
    if (!record.editable) {
      return [
        {
          label: '编辑',
          disabled: currentEditKeyRef.value ? currentEditKeyRef.value !== record.key : false,
          onClick: handleEdit.bind(null, record),
        },
        {
          label: '删除',
          color: 'error',
          disabled: currentEditKeyRef.value ? currentEditKeyRef.value !== record.key : false,
          popConfirm: {
            title: '是否删除',
            confirm: handleDel.bind(null, record, column),
          },
        },
      ];
    }
    return [
      {
        label: '保存',
        onClick: handleSave.bind(null, record, column),
      },
      {
        label: '取消',
        onClick: handleCancel.bind(null, record, column),
      },
    ];
  }

  function onEditChange({ column, value, record }) {}

  onMounted(() => {
    watch(
      flow.variables,
      (value: any) => {
        const list = value?.map((item, index) => {
          return { ...item, index, itemKey: item.key };
        });
        setTableData(list);
      },
      {
        immediate: true,
      },
    );
  });
</script>
<style lang="less" scoped></style>
