<script lang="tsx">
  import type { PropType } from 'vue';
  import { Result } from 'ant-design-vue';
  import { defineComponent } from 'vue';

  export default defineComponent({
    name: 'ErrorPage',
    props: {
      // 状态码
      status: {
        type: String as PropType<string>,
        default: '403',
      },

      title: {
        type: String as PropType<string>,
        default: '401',
      },

      subTitle: {
        type: String as PropType<string>,
        default: '对不起，你没有权限访问系统',
      },

      full: {
        type: Boolean as PropType<boolean>,
        default: false,
      },
    },
    setup(props) {
      return () => {
        return (
          <Result status={props.status as any} title={props.title} sub-title={props.subTitle}></Result>
        );
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-app-exception-page';

  .@{prefix-cls} {
    display: flex;
    align-items: center;
    flex-direction: column;

    .ant-result-icon {
      img {
        max-width: 400px;
        max-height: 300px;
      }
    }
  }
</style>
