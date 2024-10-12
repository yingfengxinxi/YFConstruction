<template>
  <div class="section">
    <div class="examine-cell">
      <flexbox class="person">
        <xr-avatar
          v-if="data.createUser"
          :name="data.createUser.realname"
          :id="data.createUser.userId"
          :size="40"
          :src="data.createUser.img"
          :disabled="false"
          class="person__hd"/>
        <div class="person__bd">
          <div class="person__bd-name">{{ data.createUser.realname }}</div>
          <div class="person__bd-des">{{ `创建审批于 ${data.createTime}` }}</div>
        </div>
        <div class="rt-setting">
          <span class="dep">
            <span>{{ getStatusName(data.examineStatus) }}</span>
          </span>
          <span
            :style="{ 'background-color': getStatusColor(data.examineStatus) }"
            class="bg-color"/>
        </div>
      </flexbox>

      <div class="examine-cell__bd">
        <p
          v-if="data.content"
          class="examine-cell__bd--content">{{ data.content }}</p>

        <flexbox class="examine-content">
          <i
            :class="['wk', 'examine-content__hd', examineConfig[data.categoryType].icon.name]"
            :style="{ backgroundColor: examineConfig[data.categoryType].icon.color}"/>
          <div class="examine-content__bd">
            <div>
              <a @click="handleClick('detail')">【{{examineConfig[data.categoryType].name }}】{{ data.catagory }}</a>
            </div>
            <div
              v-if="data.causeTitle"
              class="examine-content__bd--des">{{ data.causeTitle }}
            </div>
          </div>
        </flexbox>

        <div class="examine-stage">
          <i :class="getStatusIcon(data.examineStatus)" :style="{ color: getStatusColor(data.examineStatus) }"/>
          <span>{{ `${data.examineName || ''}${getStatusName(data.examineStatus)}` }}</span>
        </div>
        <el-popover
          v-model="showFlowPopover"
          width="300"
          trigger="click">
          <check-flow
            :show="showFlowPopover"
            :id="data.examineRecordId"
            @close="showFlowPopover=false"/>
          <el-button
            slot="reference"
            type="text">查看审批流程</el-button>
        </el-popover>
      </div>
    </div>

    <div class="section__ft">
      <el-button
        v-if="canCheck"
        class="xr-btn--red handle-button"
        icon="wk wk-close"
        type="primary"
        @click="handleClick('reject')">驳回
      </el-button>
      <el-button
        v-if="canCheck"
        class="xr-btn--green handle-button"
        icon="wk wk-success"
        type="primary"
        @click.native="handleClick('pass')">通过
      </el-button>
      <el-dropdown
        v-if="moreTypes.length > 0"
        trigger="click"
        style="float: right;"
        @command="handleTypeDrop">
        <el-button
          icon="el-icon-more"
          class="handle-button"/>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item
            v-for="(item, index) in moreTypes"
            :key="index"
            :icon="item.icon | wkIconPre"
            :command="item.type">{{ item.name }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
  import Constent from '@/components/common/constent'
  import CheckStatus from '@/mixins/CheckStatusMixin'

  import CheckFlow from './CheckFlow' // 审批流程

  export default {
    // 审批布局
    name: 'ExamineCell',
    components: {
      CheckFlow
    },
    mixins: [Constent, CheckStatus],
    props: {
      data: Object,
      section: Number,
      index: Number
    },
    data() {
      return {
        showFlowPopover: false,
      }
    },
    computed: {
      moreTypes() {
        const mores = []
        if (this.data.permission) {
          if (this.data.permission.isRecheck) {
            mores.push({type: 'withdraw', name: '撤销', icon: 'reset'})
          }
        }
        return mores
      },

      /**
       * 是否能审核 通过拒绝
       */
      canCheck() {
        return this.data.permission && this.data.permission.isCheck
      }
    },
    watch: {},
    mounted() {
    },

    beforeDestroy() {
    },
    methods: {
      handleClick(type) {
        this.$emit('handle', type, this.data, this.index, this.section)
      },

      handleTypeDrop(command) {
        this.$emit('handle', command, this.data, this.index, this.section)
      }
    }
  }
</script>

<style lang="scss" scoped>
  .section {
    background-color: white;
    border-radius: $xr-border-radius-base;
    border: 1px solid $xr-border-line-color;
    margin-bottom: 15px;

    &__ft {
      background-color: #f4f7ff;
      overflow: hidden;
      padding-right: 10px;
    }
  }

  .handle-button {
    padding: 6px 10px;
    margin: 5px 5px 5px 0;
    float: right;
  }

  .examine-cell {
    padding: 15px;

    &__bd {
      margin-left: 40px;
      margin-top: 20px;

      &--content {
        font-size: 13px;
        color: #333;
      }
    }
  }

  // 审批内容
  .examine-content {
    margin-top: 15px;
    padding: 5px;
    background-color: #f4f7ff;
    &__hd {
      display: block;
      border-radius: 5px;
      margin-right: 8px;
      padding: 7px 8px;
      color: white;
    }

    &__bd {
      flex: 1;

      a {
        font-size: 12px;
        color: $xr-color-primary;
      }

      a:hover {
        text-decoration: underline;
      }

      &--des {
        font-size: 12px;
        color: #999;
        margin-top: 3px;
      }
    }
  }

  // 人 信息
  .person {
    &__hd {
      margin-right: 8px;
    }

    &__bd {
      flex: 1;

      &-name {
        font-size: 13px;
        color: #333;
      }

      &-des {
        font-size: 12px;
        color: #999;
        margin-top: 3px;
      }
    }
  }

  //审核阶段
  .examine-stage {
    color: #333;
    font-size: 12px;
    margin-top: 15px;

    i {
      color: #ffa22a;
      margin-right: 5px;
      font-size: 12px;
    }
  }

  // 状态展示
  .rt-setting {
    float: right;
    line-height: 30px;
    font-size: 12px;
    .dep {
      color: #333333;
      margin-right: 5px;
    }
    img {
      width: 16px;
      cursor: pointer;
      vertical-align: middle;
    }
    .bg-color {
      display: inline-block;
      width: 6px;
      height: 6px;
      border-radius: 50%;
      margin-right: 5px;
    }
  }

  /** 关联附件 联系人 客户 行布局 */
  .examine-relate {
    &__hd {
      margin-top: 10px;
      padding: 8px 0;
      font-size: 13px;
      color: #333;
      font-weight: 400;
    }
  }

  .relate-cell {
    padding: 5px 0;
    font-size: 12px;
    &__hd {
      flex-shrink: 0;
      color: #333;
      margin-right: 5px;
      i {
        font-size: 13px;
        color: #666;
      }
    }

    &__bd {
      &--item {
        margin-bottom: 3px;
        a {
          color: $xr-color-primary;
        }

        a:hover {
          text-decoration: underline;
        }
      }

      &--item + &--item {
        margin-bottom: 5px;
      }

      a {
        color: $xr-color-primary;
      }
      a:hover {
        text-decoration: underline;
      }
    }
  }
</style>
