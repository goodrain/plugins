<template>
  <BasicDrawer
    v-bind="$attrs"
    title="1分钟了解代码质量如何评价"
    width="50%"
    :bodyStyle="{ background: '#E9F1F7' }"
    :showDetailBack="false"
  >
    <div class="evalute">
      <dl>
        <dt>1.概述</dt>
        <dd>
          代码质量页面用于直观地展示应用服务代码通过SonarQube检测后的结果详情。以便您实时了解应用服务中代码的健康程度。
        </dd>
      </dl>
      <dl>
        <dt>2.质量阈</dt>
        <dd>
          质量阈可以帮助我们实时了解项目是否已经满足生产要求了，代码质量检查会给出一个标识：通过/不通过。项目中的新代码和全部代码的条件都需要通过质量阈
          <table>
            <tr>
              <th>覆盖率</th>
              <th>小于</th>
              <th>80.0%</th>
            </tr>
            <tr>
              <td>重复行（%）</td>
              <td>大于</td>
              <td>3.0%</td>
            </tr>
            <tr>
              <td>SAQALE评级</td>
              <td>劣于</td>
              <td>A</td>
            </tr>
            <tr>
              <td>可靠性比率</td>
              <td>劣于</td>
              <td>A</td>
            </tr>
            <tr>
              <td>安全热点复审</td>
              <td>小于</td>
              <td>100%</td>
            </tr>
            <tr>
              <td>安全比率</td>
              <td>劣于</td>
              <td>A</td>
            </tr>
          </table>
        </dd>
      </dl>
      <dl>
        <dt>3.可靠性</dt>
        <dd>
          <h2>Bug对代码可靠性评级的影响</h2>
          <ol>
            <li>A = 0 Bug 最高等级A，表示代码无bug</li>
            <li>B = at least 1 Minor Bug 代码只要有一个次要bug，等级就为B</li>
            <li>C = at least 1 Major Bug 只要包含一个重要bug，等级将为C</li>
            <li>D = at least 1 Critical Bug 只要有一个严重bug，等级评估为D</li>
            <li>
              E = at least 1 Blocker Bug
              只要有一个最高等级的阻断级别的bug，可靠性评估为E，最低级别。
            </li>
          </ol>
          <h2>安全漏洞对代码安全度评级的影响</h2>
          <ol>
            <li>A = 0 Vulnerability （没有漏洞时，评估为最高级别A）</li>
            <li> B = at least 1 Minor Vulnerability （只要包含一个次要漏洞，评估为B） </li>
            <li> C = at least 1 Major Vulnerability （只要包含一个重要漏洞，评估为C） </li>
            <li> D = at least 1 Critical Vulnerability （只要包含一个严重漏洞，评估为D） </li>
            <li>
              E = at least 1 Blocker Vulnerability （只要包含一个阻断漏洞，评估为最低级别E）
            </li>
          </ol>
        </dd>
      </dl>
      <dl>
        <dt>4.可维护性</dt>
        <dd>
          <p>技术债务：当前不规范的代码，会对以后产品修改的成本造成影响。</p>
          <p>代码异味：不规范代码的数量</p>
          <p>
            开发成本：开发一行代码（LOC）的成本。 示例：如果开发1
            LOC的成本估计为30分钟，则此属性的值为30。目前采用的是系统默认值30。注意此处成本是指从零开始重写代码所需的成本。
          </p>
          <p>
            可维护性：可维护性等级范围从A（非常好）到E（非常差）。
            评级由技术债务比率的值决定，技术债务比率是将项目的技术债务与从零开始重写代码所需的成本进行比较。
            A到D的默认值为0.05,0.1,0.2,0.5。任何超过0.5评级就为E。
          </p>

          <p>
            举个例子：假设开发成本是30分钟，2,500
            LOC的技术债务为24,000分钟的项目将有技术债务比率为24000 /（30 * 2,500）= 0.32。
            因此项目的可维护性评级就是D。
          </p>
        </dd>
      </dl>
      <dl>
        <dt>4.重复度</dt>
        <dd>
          <p>
            SonarQube使用自己的复制/粘贴检测引擎，可以检测重复：
            <br />

            在源文件中
            <br />
            跨项目中的多个文件
            <br />
            项目的各个模块
            <br />
            跨多个项目
          </p>
          <p>
            重复率=重复行数/总行数*100
            <br />
            重复块：重复代码的行数
          </p>
        </dd>
      </dl>
    </div>
  </BasicDrawer>
</template>
<script>
  /**
   * author : bo.peng
   * createTime ： 2022-01-26 10:53:08
   * description : 了解代码质量评价
   */
  import { BasicDrawer } from '/@/components/Drawer';
  import { defineComponent } from 'vue';
  export default defineComponent({
    components: {
      BasicDrawer,
    },
    setup() {
      return {};
    },
  });
</script>
<style lang="less" scoped>
  .evalute {
    dl {
      padding: 20px;
      border-bottom: 1px solid #e9f1f7;
      background-color: #fff;

      dt {
        font-size: 18px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #333333;
        line-height: 25px;
        padding-bottom: 10px;
      }

      dd {
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #333333;
        line-height: 22px;

        h2 {
          font-size: 16px;
          padding: 10px 0;
        }

        p {
          padding-bottom: 10px;
        }
      }
    }
  }

  table {
    border-collapse: collapse;
  }

  th,
  td {
    width: 100px;

    height: 40px;

    border: 1px solid black;

    font-size: 12px;

    text-align: center;
  }
</style>
