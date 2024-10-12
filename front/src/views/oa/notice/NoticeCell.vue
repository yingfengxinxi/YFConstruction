<template>
  <div
    :id="'notice-cell' + cellIndex"
    class="list">
    <div class="header">
      <xr-avatar
        :name="data.realname"
        :size="35"
        :src="data.img"
        class="user-img" />
      <div class="name-time">
        <p class="name">{{ data.realname }}</p>
        <p class="time">{{ data.createTime | moment("YYYY-MM-DD HH:mm") }}</p>
      </div>
    </div>
    <div
      class="title">公告标题：{{ data.title }}</div>
    <el-button style="float: right"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="rowDelete(data)">删除</el-button>
    <el-button style="float: right;margin-right: 10px"
               size="mini"
               type="text"
               icon="el-icon-view"
               @click="rowView(data)">预览</el-button>
    <el-button style="float: right;margin-right: 10px"
               size="mini"
               type="text"
               icon="el-icon-edit"
               @click="rowFun(data)">编辑</el-button>
    <div
      v-if="data.preShow"
      class="data-content">{{ data.content }}</div>
    <div
      v-else
      class="data-content">{{ data.contentSub }}</div>
    <div
      v-if="data.contentSub.length < data.content.length"
      class="load-more">
      <span
        v-if="!data.loadMore"
        @click="loadMoreBtn(data)">展开全文</span>
      <span
        v-else
        @click="data.loadMore = false, data.preShow = false">收起全文</span>
    </div>
    <v-details
            v-if="dialog"
            :title-list="titleList"
            @close="close"/>
  </div>
</template>

<script>
import { noticeIsReadAPI,noticeDeleteAPI } from '@/api/oa/notice'
import VDetails from './Details'

export default {
  components: {
    VDetails},

  props: {
    data: Object,
    cellIndex: Number
  },

  data() {
    return {
      dialog: false,
      titleList: {},
      // 父元素
      parentTarget: null,
      awaitMoment: false // 等客户浏览
    }
  },

  mounted() {
    if (this.data.isRead == 0) {
      this.$bus.on('notice-list-box-scroll', target => {
        this.observePreview(target)
      })
      this.observePreview(
        document.getElementById('notice-cell' + this.cellIndex).parentNode
      )
    }
  },

  beforeDestroy() {
    this.$bus.off('notice-list-box-scroll')
  },

  methods: {
    close() {
      this.dialog = false
    },
    /**
     * 观察预览
     */
    observePreview(target) {
      if (this.data.isRead == 0) {
        if (target) {
          this.parentTarget = target
        }
        const ispreview = this.whetherPreview()
        if (!this.awaitMoment && ispreview) {
          this.awaitMoment = true
          setTimeout(() => {
            this.awaitMoment = false
            const ispreview = this.whetherPreview()
            if (ispreview) {
              // this.submiteIsRead() //TODO
            }
          }, 3000)
        }
      }
    },

    /**
     * 是否预览
     */
    whetherPreview() {
      const dom = this.parentTarget.children[this.cellIndex]
      if (this.parentTarget.getBoundingClientRect()) {
        const offsetTop =
          this.parentTarget.getBoundingClientRect().top -
          dom.getBoundingClientRect().top
        let ispreview = false
        if (
          offsetTop <= 0 &&
          Math.abs(offsetTop) < this.parentTarget.clientHeight
        ) {
          ispreview = true
        } else if (offsetTop > 0 && offsetTop < dom.clientHeight) {
          ispreview = true
        }
        return ispreview
      } else {
        return false
      }
    },

    /**
     * 提交已读
     */
    submiteIsRead() {
      noticeIsReadAPI({
        announcementId: this.data.announcementId
      })
        .then(res => {
          // this.$store.dispatch('GetOAMessageNum', 'announcement')
          this.data.isRead = 1
        })
        .catch(() => {})
    },

    /**
     * 点击显示详情
     */
    rowFun(val) {
      this.$emit('handle', {
        type: 'detail',
        value: val
      })
    },
    rowView(val) {
      this.titleList = val
      this.dialog = true
    },
    /**
     * 点击删除
     */
    rowDelete(val) {
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then(() => {
        noticeDeleteAPI(val.announcementId).then(res => {
          // this.titleList = val
          this.$emit('update:titleList', val);
          this.$emit('deleteFun')
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
        })
      })
      .catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    loadMoreBtn(val) {
      this.$set(val, 'preShow', true)
      this.$set(val, 'loadMore', true)
    }
  }
}
</script>

<style scoped lang="scss">
.list {
  margin-bottom: 30px;
  padding-bottom: 30px;
  border-bottom: 1px solid #e6e6e6;
  .header {
    margin-bottom: 15px;
    .user-img {
      margin-right: 10px;
    }
    .name-time {
      display: inline-block;
      .time {
        color: #999;
        margin-top: 5px;
        font-size: 12px;
      }
    }
  }
  .title {
    display: inline-block;
  }
  .data-content {
    margin-top: 10px;
    color: #999;
    font-size: 12px;
    line-height: 18px;
    white-space: pre-wrap;
    word-wrap: break-word;
    background-color: #f0f7ff;
    padding: 15px;
    border-radius: 3px;
    color: #333;
    letter-spacing: 0.5px;
  }
  .load-more {
    text-align: left;
    margin-top: 15px;
    span {
      cursor: pointer;
      font-size: 13px;
      color: #8ab7f5;
    }
  }
}
</style>
