<template>
  <flexbox
    :class="{ 'is-unread' :data.ifRead == 0 }"
    class="message-cell"
    align="stretch">
    <div style="border-radius:14px;margin-right: 15px;text-align: center;width: 28px;height: 28px;
    line-height: 26px;border: 1px solid #c8c8c8;">
      <i
        :class="data.icon"
      />
    </div>

    <div class="message-cell__bd" @click="enterDetail" v-html="data.title">
    </div>

    <div class="message-cell__ft">
      <div>{{ data.createTime | formatTime }}</div>
      <div class="handle">
        <span class="check" @click="messageReadClick">标记已读</span>
        <span class="delete" @click="messageDeleteClick">删除</span>
      </div>
    </div>
  </flexbox>
</template>

<script>
  import Constent from '@/components/common/constent'

  export default {
    // 消息cell
    name: 'MessageCell',
    mixins: [Constent],
    props: {
      data: Object,
      dataIndex: Number
    },
    data() {
      return {}
    },
    computed: {
      typeObj() {
          let temp = this.messageConfig[this.data.type]
        return temp==null?{
            name: '',//名称描述
            icon: {
                name: 'wk wk-announcement',//
                color: '#6995FF',//背景色
            }}:temp
      },
    },
    watch: {},
    mounted() {
    },
    beforeDestroy() {
    },
    methods: {
      enterDetail() {
        // 未读触发读
        if (this.data.ifRead == 0) {
          this.$emit('read', this.data.id, this.dataIndex)
        }
        this.$emit('detail', this.data);
      },

      /**
       * 标记已读
       */
      messageReadClick() {
        this.$emit('read', this.data.id, this.dataIndex)
      },

      /**
       * 消息删除
       */
      messageDeleteClick() {
        this.$emit('delete', this.data.id, this.dataIndex)
      },
    }
  }
</script>

<style lang="scss" scoped>
  .message-cell {
    position: relative;
    font-size: 14px;
    color: #333;
    padding: 15px 20px 15px 15px;
    line-height: 1.5;

    &__hd {
      flex-shrink: 0;
      margin-right: 15px;
      background-color: $xr-color-primary;
      text-align: center;
      width: 28px;
      height: 28px;
      line-height: 28px;
      border-radius: 14px;

      .wk {
        color: white;
        font-size: 13px;
      }
    }

    &__bd {
      flex: 1;
      white-space: pre-wrap;
      word-wrap: break-word;
      word-break: break-all;
    }

    &__ft {
      flex-shrink: 0;
      font-size: 12px;
      color: #999;
      width: 85px;
      margin-left: 35px;
      text-align: right;
      position: relative;

      .handle {
        position: absolute;
        top: 20px;
        right: 0;
        font-size: 12px;
        color: #999;
        .check,
        .delete {
          visibility: hidden;
          cursor: pointer;
        }

        .check + .delete {
          margin-left: 5px;
        }

        .check:hover {
          color: $xr-color-primary;
        }
        .delete:hover {
          color: #F56C6C;
        }
      }
    }

    &.is-unread::before {
      content: '';
      position: absolute;
      top: 20px;
      right: 8px;
      width: 6px;
      height: 6px;
      border-radius: 3px;
      background-color: #f94e4e;
    }

    &:hover {
      background-color: #f7f8fa;
      .delete {
        visibility: visible;
      }
    }

    &.is-unread:hover {
      .check {
        visibility: visible;
      }
    }
  }

  .click-content {
    color: $xr-color-primary;
    cursor: pointer;

    &:hover {
      text-decoration: underline;
    }
  }

  .is-invalid {
    color: #999;
    pointer-events: none;
    cursor: initial;
  }
</style>
