//组件说明：
// 本组件默认是员工的多选，支持传入自定义请求方法，请求参数。
// openStatus为必传参数，如果不传会导致页面关闭之后已选中数据不清空问题
// 使用自定义请求方法，infoRequest、idKeyName、labelKeyName、titleName为必传参数
// 如果使用自定义请求 不传idKeyName字段值，则使用数据的默认id字段
<template>
  <div class="xh-user">
    <div class="xh-user__hd">
      {{titleName?titleName:'员工'}}
    </div>
    <div class="xh-user__bd">
      <el-input v-model="searchInput" :placeholder="placeholder" size="small" prefix-icon="el-icon-search" class="search-input" />
      <div v-loading="loading" class="search-list" v-if="show">
        <el-checkbox v-if="allowCheckAll" :indeterminate="isIndeterminate" :disabled="radio" v-model="checkAll" class="all-check" @change="handleCheckAllChange">全选
        </el-checkbox>
        <el-checkbox-group ref="checkboxGroup" v-model="selectIds" :max="max" @change="changeCheckout">
          <el-checkbox v-for="(item, i) in showList" :key="i" :label="infoRequest && idKeyName ? item[idKeyName]?item[idKeyName]:item.id : item.userId" class="colleagues-list" :disabled="disabledItem(item)">
            <xr-avatar :name="infoRequest && labelKeyName ? item[labelKeyName] : item.realname" :size="24" :src="item.img" class="user-img" />
            <span>{{ infoRequest && labelKeyName ? item[labelKeyName] : item.realname }}</span>
          </el-checkbox>
        </el-checkbox-group>
      </div>
    </div>
    <div class="xh-user__ft">
      <span class="select-info">已选择<span class="select-info--num">{{ selectIds.length }}</span>项</span>
      <el-button type="text" @click="clearAll">清空</el-button>
    </div>
  </div>
</template>
<script type="text/javascript">
import { userListAPI } from '@/api/common'
import { objDeepCopy } from '@/utils'
import PinyinMatch from 'pinyin-match'

export default {
  name: 'XhProject', // 新建 project复选
  components: {},
  props: {
    show: {
      type: Boolean,
      default: true
    },
    radio: {
      type: Boolean,
      default: false
    },
    allowCheckAll: {
      type: Boolean,
      default: true
    },
    // 已选信息
    selectedData: {
      type: Array,
      default: () => {
        return []
      }
    },
    // 禁用信息
    disabledData: {
      type: Array,
      default: () => {
        return []
      }
    },
    // 请求加强
    infoType: {
      type: String,
      default: 'default' // 返回全部  crm_contract crm_receivables oa_examine 合同审核人自选列表
    },
    // 自定义请求
    infoRequest: Function,
    infoParams: {
      type: Object,
      default: () => {
        return {}
      }
    },
    //父组件触发提交或关闭按钮，当前字段值发生变化，清空已选中数组
    openStatus: {
      type: Boolean,
      default: false
    },
    //自定义请求返回数据的id字段名
    idKeyName: {
      type: String,
      default: ''
    },
    //自定义请求返回数据需显示名称的字段名
    labelKeyName: {
      type: String,
      default: ''
    },
    //自定义请求返回数据列表的名称。
    titleName:{
      type: String,
      default: ''
    }
  },
  data () {
    return {
      list: [],
      selectIds: [], // 选择项
      loading: false, // 加载动画
      searchInput: '',
      checkAll: false,
      isIndeterminate: false,
      placeholder:"搜索"+(this.titleName?this.titleName:员工)
    }
  },
  computed: {
    showList () {
      return this.list.filter(item => {
        var flag = true;
        
        if ((this.infoRequest && this.labelKeyName ? item[this.labelKeyName] : item.realname).indexOf(this.searchInput) < 0) {
          flag = false;
        }
        return flag;
      })
    },

    max () {
      return this.radio ? 1 : undefined
    }
  },
  watch: {
    selectedData: function (value) {
      if (this.show) {
        this.checkItems(value)
      }
    },

    show (value) {
      if (value) {
        if (this.list && this.list.length == 0) {
          this.getUserList()
        } else {
          this.checkItems(this.selectedData)
        }
      }
    },
    openStatus (value) {
      if (!value) {
        this.selectIds = [];
        this.searchInput='';
      }
    }
  },
  mounted () {
    if (this.show) {
      this.getUserList()
    }
  },
  methods: {
    /**
     * 获取数据
     */
    getUserList () {
      this.loading = true
      this.getRequest()(this.getParams())
        .then(res => {
          console.log(res)
          this.list = res.data.list || res.data
          this.checkItems(this.selectedData)
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },

    /**
     * 请求
     */
    //TODO 人员选择控件业务扩展业务类型
    getRequest () {
      console.log(this.infoRequest)
      if (this.infoRequest) {
        return this.infoRequest
      } else if (this.infoType === 'default') {
        return userListAPI
      } else if (
        this.infoType === 'crm_contract' ||
        this.infoType === 'crm_invoice' ||
        this.infoType === 'crm_receivables' ||
        this.infoType === 'oa_examine' ||
        this.infoType === 'crm_expense'
      ) {
        return userListAPI
      }
    },

    getParams () {
      const params =
        this.infoParams && Object.keys(this.infoParams.length !== 0)
          ? this.infoParams
          : {}
      if (this.infoType === 'default') {
        params.pageType = 0
        return params
      } else if (
        this.infoType === 'crm_contract' ||
        this.infoType === 'crm_invoice' ||
        this.infoType === 'crm_receivables' ||
        this.infoType === 'oa_examine'
      ) {
        params.pageType = 0
        return params
      }
    },

    /**
     * 勾选
     */
    changeCheckout (val) {
      this.refreshAllCheck(val.length)
      const selectItems = []
      for (let index = 0; index < this.list.length; index++) {
        const item = this.list[index]
        if (val.includes(this.infoRequest && this.idKeyName ? item[this.idKeyName]?item[this.idKeyName]:item.id : item.userId)) {
          selectItems.push(item)
        }
      }
      this.$emit('changeCheckout', selectItems, val)
    },

    /**
     * 全部勾选
     */
    handleCheckAllChange (val) {
      this.selectIds = val
        ? this.list.map(item => {
          return this.infoRequest && this.idKeyName ? item[this.idKeyName]?item[this.idKeyName]:item.id : item.userId
        })
        : []
      this.isIndeterminate = false
      const selectItems = val ? this.list : []
      this.$emit('changeCheckout', objDeepCopy(selectItems), this.selectIds)
    },

    /**
     * 刷新总check
     */
    refreshAllCheck (checkedCount) {
      this.checkAll = checkedCount === this.list.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.list.length
    },

    /**
     * 标记勾选
     */
    checkItems (items) {
      this.selectIds = items.map(item => {
        return this.infoRequest && this.idKeyName ? item[this.idKeyName]?item[this.idKeyName]:item.id : item.userId
      })
      this.refreshAllCheck(this.selectIds.length)
    },
    /**
     * 标记勾选
     */
    disabledItem (item) {
      var flag = false;
      this.disabledData.forEach(i => {
        if ((this.infoRequest && this.idKeyName ? item[this.idKeyName]?item[this.idKeyName]:item.id : item.userId) == (this.infoRequest && this.idKeyName ? i[this.idKeyName]?i[this.idKeyName]:i.id : i.userId)) {
          flag = true;
        }
      })
      return flag;
    },
    /**
     * 清空全部
     */
    clearAll () {
      this.selectIds = []
      this.$emit('changeCheckout', [], [])
    }
  }
}
</script>
<style lang="scss" scoped>
/* 选择员工 */
.user-img {
  margin-right: 8px;
  vertical-align: middle;
}

.search-list {
  padding: 5px 0;
  height: 200px;
  overflow: auto;
}

.colleagues-list {
  padding: 5px 0;
  display: block;
  margin-left: 0;
}

.xh-user {
  color: #333;
  font-size: 12px;
  &__hd {
    padding: 0 15px;
    height: 40px;
    line-height: 40px;
    border-bottom: 1px solid $xr-border-line-color;
  }

  &__bd {
    padding: 10px 12px;
  }

  &__ft {
    padding: 5px 12px;
    background: #f7f8fa;
    border-top: 1px solid $xr-border-line-color;
    .el-button {
      font-size: 12px;
    }
  }
}

// 选择信息
.select-info {
  display: inline-block;
  width: calc(100% - 30px);

  &--num {
    color: $xr-color-primary;
  }
}

// check样式
.el-checkbox-group {
  overflow-x: hidden;
}

.el-checkbox {
  /deep/ .el-checkbox__label {
    color: #333;
  }
}

.all-check {
  display: inline-block;
  padding: 5px 0;
}

.search-input {
  /deep/ .el-input__inner {
    background-color: #f4f4f4;
    border: none;
  }
}
</style>
