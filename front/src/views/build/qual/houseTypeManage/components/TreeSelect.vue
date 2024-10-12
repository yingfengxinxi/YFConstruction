<template>
  <div class="employee-department">
    <div class="select-input">
      <slot name="tabs" />
      <!-- 搜索员工列表 -->
      <el-tabs v-model="activeTabName">
        <el-tab-pane v-loading="buildingLoading" label="区域名称" name="building">
          <el-input v-model="searchInput" placeholder="搜索区域名称" size="small" style="margin-bottom:10px;" prefix-icon="el-icon-search" class="search-input" @input="SearchChange" />
          <div class="search-list">
            <el-breadcrumb separator-class="el-icon-arrow-right">
              <el-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index">
                <a href="javascript:;" @click="breadcrumbBtn(item, index)">{{ item.label }}</a>
              </el-breadcrumb-item>
            </el-breadcrumb>
            <div class="checkout-boxs">
              <div v-for="(buildItem, index) in buildingChildrens" v-if="!buildItem.hidden" :key="index" class="checkout-box">
                <el-checkbox :indeterminate="buildItem.isIndeterminate" v-model="buildItem.isCheck" @change="CheckboxChange(buildItem, index)" />
                <div @click="enterChildren(buildItem)">
                  <span>{{ buildItem.buildingName }}</span>
                  <span v-if="buildItem.children&&buildItem.children.length>0" class="el-icon-arrow-right" />
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div class="checked-content">
      <div class="checked-top">
        <span class="select-info">已选择<span class="select-info--num">{{SelectCount}}</span>项</span>
        <el-button type="text" @click="clearAll">清空</el-button>
      </div>
      <div class="select-content">
        <flexbox justify="stretch" wrap="wrap">
          <div v-for="(item, index) in checkedList" :key="index" class="select-item text-one-line">
            {{item.buildingName }}
            <i class="el-icon-close delete-icon" @click="selectDelete(item, index)" /></div>
        </flexbox>
      </div>
    </div>
  </div>
</template>
<script>
import { userListAPI, depListAPI } from '@/api/common'
import { queryQuestionsTreeList } from '@/views/build/qual/api/buildManage'
import PinyinMatch from 'pinyin-match'

export default {
  props: {
    parentKey: {
      type: String,
      default: ''
    },
    MainData: {
      type: Array,
      default: []
    },
  },
  data () {
    return {
      // 加载动画
      buildingLoading: false,
      activeTabName: 'building',
      //数据存储列表
      buildingChildrens: [],
      //搜索框筛选数据
      searchInput: '',
      // 面包屑数据
      breadcrumbList: [],
      //选中的列表
      checkedList: [],
    }
  },
  computed: {
    //展示已选择项
    SelectCount () {
      return this.checkedList.length
    },
  },
  watch: {
    parentKey: function (newValue, oldValue) {
      console.log("newValue==", newValue); console.log("oldValue==", oldValue);
      this.getBuildingDataList(newValue);
    },
  },
  created () {
  },
  methods: {
    /**
     * 获取楼栋数据
     */
    getBuildingDataList (value) {
      this.buildingLoading = true
      this.breadcrumbList = [];
      this.buildingChildrens = [];
      let newDataList = [];
      for (let i = 0; i < this.MainData.length; i++) {
        if (value == this.MainData[i].buildingId) {
          newDataList.push(this.MainData[i]);
          break;
        }
      }
      queryQuestionsTreeList(value).then(res => {
        newDataList[0].children = res.data;
        this.buildingChildrens = newDataList.map((item, index, array) => {
          item.isCheck = this.getItemCheckInfo(item)
          return item
        })
        this.breadcrumbList.push({ label: '全部', data: this.buildingChildrens })
        this.buildingLoading = false
      }).catch(() => {
        this.buildingLoading = false
      })
    },
    /**
     * 楼栋数节点下一级
     */
    enterChildren (buildItem) {
      if (buildItem.children) {
        if (buildItem.children.length > 0) {
          this.buildingChildrens = []
          this.buildingChildrens = buildItem.children.map((item, index, array) => {
            item.hidden = !PinyinMatch.match(item.buildingName, this.searchInput)
            item.isCheck = this.getItemCheckInfo(item)
            return item
          })
          this.breadcrumbList.push({
            label: buildItem.buildingName,
            data: this.buildingChildrens
          })
        }
      }
    },

    /**
     * 面包屑点击事件
     */
    breadcrumbBtn (item, index) {
      if (index + 1 <= this.breadcrumbList.length - 1) {
        this.breadcrumbList.splice(index + 1, this.breadcrumbList.length - 1)
      }
      this.buildingChildrens = []
      this.buildingChildrens = item.data.map((item, index, array) => {
        if (item.buildingName.indexOf(this.searchInput) != -1) {
          item.hidden = false
        } else {
          item.hidden = true
        }
        item.isCheck = this.getItemCheckInfo(item)
        return item
      })
    },
    /**
     * 获取事项标记信息
     */
    getItemCheckInfo (item) {
      if (this.checkedList.length == 0) {
        return false
      }
      var hasItem = false
      for (let index = 0; index < this.checkedList.length; index++) {
        const element = this.checkedList[index]
        if (item.buildingId == element.buildingId) {
          hasItem = true
          break
        }
      }
      return hasItem
    },
    /**
     * 勾选操作
     */
    CheckboxChange (item, aindex) {
      console.log(item, aindex);
      let newData = [];
      newData.push(item);
      this.recursionFunction(newData);
      // this.updateCheckedUserDepListByCheck(item)
      this.valueChange()
    },
    //操作：用户选择父级，自动勾选所有子级
    recursionFunction (item) {
      item.forEach(term => {
        this.updateCheckedUserDepListByCheck(term)
        this.$set(item, 'isCheck', true)
        if (term.children && term.children.length > 0) {
          this.recursionFunction(term.children)
        }
      })


    },
    /**
     *  回调数据
     */
    valueChange () {
      this.$emit('BuildingCheckList', this.checkedList)
    },
    /**
     * check 操作后的 存储数据刷新
     */
    updateCheckedUserDepListByCheck (item) {
      var removeIndex = -1
      for (let index = 0; index < this.checkedList.length; index++) {
        const element = this.checkedList[index]
        if (item.buildingId == element.buildingId) {
          removeIndex = index
        }
      }
      if (removeIndex == -1) {
        // this.$set(item,'isIndeterminate',true)
        this.checkedList.push(item)
      } else if (removeIndex != -1) {
        this.checkedList.splice(removeIndex, 1)
      }
    },
    /**
     * 清空按钮
     */
    clearAll () {
      this.checkedList = []
      this.buildingChildrens = this.buildingChildrens.map(item => {
        item.isCheck = false
        return item
      })
    },
    /**
     * 删除一个选择项
     */
    selectDelete (selectItem, index) {
      this.buildingChildrens.splice(index, 1)
      this.buildingChildrens = this.buildingChildrens.map((item, index, array) => {
        item.isCheck = this.getItemCheckInfo(item)
        return item
      })
    },
    /**
     * 搜索区域改变查询名称事件
     */
    SearchChange (val) {
      this.buildingChildrens = this.buildingChildrens.map(item => {
        item.hidden = !PinyinMatch.match(item.buildingName, val)
        return item
      })
    },
  }
}
</script>

<style scoped lang="scss">
@import "@/styles/mixin.scss";
.employee-department {
  display: flex;
  .select-input {
    flex: 1;
    overflow: hidden;
    border-right: 1px solid $xr-border-line-color;
    .select-input > .el-input {
      margin: 10px 0;
    }

    .el-checkbox {
      margin-left: 0;
      margin-right: 10px;
    }
    .el-breadcrumb {
      margin-bottom: 15px;
    }
    .checkout-box {
      display: flex;
      margin-bottom: 10px;
    }
    .checkout-box > div {
      flex: 1;
      .el-icon-arrow-right {
        float: right;
      }
      span {
        cursor: pointer;
      }
    }
  }

  .checked-content {
    flex: 1;
    .checked-top {
      padding: 0 12px;
      height: 40px;
      line-height: 40px;
      margin-bottom: 12px;
      border-bottom: 1px solid $xr-border-line-color;
      .title {
        color: #999999;
      }
    }
    .select-content {
      padding: 0 12px;
      height: 220px;
      overflow: auto;
      .select-item {
        position: relative;
        flex-shrink: 0;
        background-color: #f3f7ff;
        height: 28px;
        line-height: 28px;
        max-width: 80px;
        font-size: 12px;
        padding: 0 15px 0 5px;
        border-radius: $xr-border-radius-base;
        color: #333;
        margin: 0 10px 10px 0;

        i {
          cursor: pointer;
          color: #666;
          margin-left: 5px;
        }

        i:hover {
          color: $xr-color-primary;
        }
      }
    }
  }
}

.el-tabs /deep/ .el-tabs__nav-wrap {
  padding: 0 12px;
}

.el-tabs /deep/ .el-tabs__nav-wrap::after {
  height: 1px;
}

.el-tabs /deep/ .el-tabs__header {
  margin-bottom: 12px;
}

.el-tabs /deep/ .el-tabs__item {
  font-size: 12px;
  color: #333;
  padding-right: 0;
}

.el-tabs /deep/ .el-tab-pane {
  height: 100%;
}

.el-tabs /deep/ .el-tabs__content {
  margin: 0 12px;
  height: 220px;
}

/* 选择员工 */
.search-img {
  vertical-align: middle;
  margin-right: 8px;
}
.search-list {
  margin: 5px;
  height: 180px;
  overflow: auto;
  margin-right: -10px;
  padding-right: 10px;
}

.checkout-boxs {
  height: 150px;
  overflow-y: auto;
  overflow-x: hidden;
}

.colleagues-list {
  padding: 5px 0;
}

// 选择信息
.select-info {
  display: inline-block;
  width: calc(100% - 40px);

  &--num {
    color: $xr-color-primary;
  }
}

// check样式
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

.delete-icon {
  color: #999;
  cursor: pointer;
  position: absolute;
  top: 8px;
  right: 2px;

  &:hover {
    color: $xr-color-primary;
  }
}
</style>

