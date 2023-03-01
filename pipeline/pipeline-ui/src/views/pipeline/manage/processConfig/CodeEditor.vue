<template>
  <div class="pb-4" ref="codeRef">
    <div class="pb-2 flex flex-row justify-between" :class="isFullscreen ? 'px-4 pt-4' : ''">
      <div>
        <span class="text-red-500 text-md pr-1">*</span>
        <span class="pr-2">脚本命令</span>
        <template v-if="!isFullscreen">
          <Tooltip title="该任务阶段执行的脚本">
            <QuestionCircleOutlined />
          </Tooltip>
        </template>
        <span v-else> (该任务阶段执行的脚本) </span>
      </div>
      <div>
        <Tooltip :title="getTitle" placement="bottom" :mouseEnterDelay="0.5">
          <span @click="toggle" class="cursor-pointer">
            <FullscreenOutlined v-if="!isFullscreen" />
            <FullscreenExitOutlined v-else />
          </span>
        </Tooltip>
      </div>
    </div>
    <div class="relative !h-full w-full overflow-hidden" ref="el"></div>
  </div>
</template>
<script lang="ts" setup>
  import { ref, computed, onMounted, onUnmounted, watchEffect, watch, unref, nextTick } from 'vue';
  import { useDebounceFn } from '@vueuse/core';
  import { useAppStore } from '/@/store/modules/app';
  import { useWindowSizeFn } from '/@/hooks/event/useWindowSizeFn';
  import CodeMirror from 'codemirror';
  import { MODE } from '/@/components/CodeEditor/src/typing';
  import { Tooltip } from 'ant-design-vue';
  import {
    QuestionCircleOutlined,
    FullscreenOutlined,
    FullscreenExitOutlined,
  } from '@ant-design/icons-vue';
  import { useFullscreen } from '@vueuse/core';

  // css
  import '/@/components/CodeEditor/src/codemirror/codemirror.css';
  import 'codemirror/theme/cobalt.css';
  /*   import 'codemirror/theme/material-palenight.css'; */
  // modes
  import 'codemirror/mode/javascript/javascript';
  import 'codemirror/mode/css/css';
  import 'codemirror/mode/htmlmixed/htmlmixed';

  const props = defineProps({
    mode: {
      type: String as PropType<MODE>,
      default: MODE.HTML,
      validator(value: any) {
        // 这个值必须匹配下列字符串中的一个
        return Object.values(MODE).includes(value);
      },
    },
    value: { type: [String, Array], default: '' },
    readonly: { type: Boolean, default: false },
  });

  const emit = defineEmits(['change']);

  const codeRef = ref();
  const { toggle, isFullscreen } = useFullscreen(codeRef);
  const el = ref();
  let editor: Nullable<CodeMirror.Editor>;

  const getTitle = computed(() => {
    return unref(isFullscreen) ? '退出全屏' : '全屏';
  });

  const debounceRefresh = useDebounceFn(refresh, 100);
  const appStore = useAppStore();

  function getValue(value) {
    let str = '';
    if (Array.isArray(value)) {
      value.forEach((val) => {
        str += val + '\n';
      });
    } else {
      str = value ? value : '';
    }
    return str;
  }

  watch(
    () => props.value,
    async (value) => {
      await nextTick();
      const oldValue = editor?.getValue();
      if (value !== oldValue) {
        editor?.setValue(getValue(value));
      }
    },
    { flush: 'post' },
  );

  watchEffect(() => {
    editor?.setOption('mode', props.mode);
  });

  watch(
    () => appStore.getDarkMode,
    async () => {
      setTheme();
    },
    {
      immediate: true,
    },
  );

  function setTheme() {
    unref(editor)?.setOption('theme', 'cobalt');
  }

  function refresh() {
    editor?.refresh();
  }

  async function init() {
    const addonOptions = {
      autoCloseBrackets: true,
      autoCloseTags: true,
      foldGutter: true,
      gutters: ['CodeMirror-linenumbers'],
    };

    editor = CodeMirror(el.value!, {
      value: '',
      mode: props.mode,
      readOnly: props.readonly,
      tabSize: 2,
      theme: 'cobalt',
      lineWrapping: true,
      lineNumbers: true,
      ...addonOptions,
    });
    editor?.setValue(getValue(props.value));
    setTheme();
    editor?.on('change', () => {
      const value = editor?.getValue();
      const arrValue = value?.split('\n');
      const newArr: string[] = [];
      arrValue?.forEach((val: string) => {
        if (val && val.trim() !== '') {
          newArr.push(val);
        }
      });
      emit('change', newArr);
    });
  }

  onMounted(async () => {
    await nextTick();
    init();
    useWindowSizeFn(debounceRefresh);
  });

  onUnmounted(() => {
    editor = null;
  });
</script>
