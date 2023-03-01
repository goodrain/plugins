<template>
  <Upload
    :accept="getStringAccept"
    :multiple="multiple"
    :before-upload="beforeUpload"
    :show-upload-list="true"
    :max-count="maxNumber"
    v-model:file-list="fileList"
    :customRequest="customRequest"
    @change="handleChange"
    v-bind="$attrs"
  >
    <slot>
      <a-button type="primary">
        {{ text }}
      </a-button>
    </slot>
  </Upload>
</template>
<script lang="ts">
  import { defineComponent, ref, toRefs, PropType, watch } from 'vue';
  import { Upload } from 'ant-design-vue';
  // hooks
  import { useUploadType } from '/@/components/Upload/src/useUpload';
  import { useMessage } from '/@/hooks/web/useMessage';
  //   types
  import type { UploadChangeParam } from 'ant-design-vue';
  import { basicProps } from '/@/components/Upload/src/props';

  // utils
  import { getFileSuffix } from '/@/utils/file/fileUtil';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { isArray, isObject, isString } from '/@/utils/is';

  export default defineComponent({
    name: 'BasicUpload',
    components: { Upload },
    props: {
      ...basicProps,
      previewFileList: {
        type: Array as PropType<string[]>,
        default: () => [],
      },
      value: { type: [String, Array] },
      text: { type: String, default: () => '上传文件' },
    },
    emits: ['change', 'update:value', 'beforeUploadChange'],
    setup(props, { emit }) {
      const fileList = ref([]);
      const { accept, helpText, maxNumber, maxSize } = toRefs(props);

      const { t } = useI18n();

      const { getStringAccept } = useUploadType({
        acceptRef: accept,
        helpTextRef: helpText,
        maxNumberRef: maxNumber,
        maxSizeRef: maxSize,
      });

      const { createMessage } = useMessage();

      // 上传前校验
      function beforeUpload(file: File, fileList: File[]) {
        let vilidate = true;
        const { maxSize } = props;
        // 限制文件名长度
        if (file.name.length > 50) {
          createMessage.error('文件名称太长');
          vilidate = false;
        }
        // 限制文件数量
        const limit = props.maxNumber ?? 10;
        if (props.maxNumber !== 0 && fileList.length > limit) {
          createMessage.warning(t('component.upload.maxNumber', [limit]));
          vilidate = false;
        }
        // 限制文件类型
        const fileType = getFileSuffix(file.name);

        /* if (getStringAccept && getStringAccept.value.indexOf('.' + fileType) < 0) {
          createMessage.error(`文件类型不支持`);
          vilidate = false;
        } */
        // 设置最大值，则判断
        if (maxSize && file.size / 1024 / 1024 >= maxSize) {
          createMessage.error(t('component.upload.maxSizeMultiple', [maxSize]));
          vilidate = false;
        }
        emit('beforeUploadChange', vilidate);
        return vilidate;
      }

      const handleChange = (info: UploadChangeParam) => {
        if (info.file.status !== 'uploading') {
          //console.log(info.file, info.fileList);
        }
        if (info.file.status === 'done') {
          createMessage.success(`${info.file.name} 成功`);
          emit(
            'update:value',
            info.fileList.map((item) => {
              return item.response.data.data.id;
            }),
          );
        } else if (info.file.status === 'error') {
          createMessage.error(`${info.file.name} 失败.`);
        }

        emit('change', info);
      };

      function customRequest(options) {
        let { file, filename, onError, onProgress, onSuccess } = options;
        onProgress({ percent: 1 });
        props
          .api?.(
            {
              data: {
                ...(props.uploadParams || {}),
              },
              file: file,
              name: filename,
              filename: file.name,
            },
            function onUploadProgress(progressEvent: ProgressEvent) {
              const complete = ((progressEvent.loaded / progressEvent.total) * 100) | 0;
              onProgress({ percent: complete });
            },
          )
          .then(onSuccess)
          .catch(onError);
      }

      watch(
        () => props.value,
        (val: never) => {
          if (isArray(val) && !isString(val[0])) {
            fileList.value = val;
          } else if (isObject(val)) {
            fileList.value = [val];
          }
        },
        {
          immediate: true,
        },
      );

      return {
        t,
        fileList,
        getStringAccept,
        customRequest,
        beforeUpload,
        handleChange,
      };
    },
  });
</script>
