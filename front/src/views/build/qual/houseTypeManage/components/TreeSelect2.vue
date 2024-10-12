<template>
  <div class="employee-department">
    <div class="select-input">
      <slot name="tabs" />
      <!-- 搜索员工列表 -->
      <el-tabs v-model="activeTabName">
        <el-tab-pane v-loading="buildingLoading" label="区域名称" name="building">
          <el-input placeholder="输入关键字" v-model="searchInput"> </el-input>
          <el-tree class="filter-tree" ref="tree" :data="buildingChildrens" :props="defaultProps" :node-key="defaultProps.id"
          @check="filterTreeCheck"  :style="{height: scrollerHeight,overflow:'auto',display: 'flex'}" show-checkbox default-expand-all :filter-node-method="filterNode" ></el-tree>
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
import { queryQuestionsTreeList2 } from '@/views/build/qual/api/buildManage'
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
      
      scrollerHeight:'300px',
      //数据存储列表
      buildingChildrens: [],
      //搜索框筛选数据
      searchInput: '',
   
      //选中的列表
      checkedList: [],
      defaultProps:{
          id:'buildingId',
          label: 'buildingName',
          children: 'children',
      }
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
      // console.log("newValue==", newValue); console.log("oldValue==", oldValue);
      this.getBuildingDataList(newValue);
    },
    searchInput:function(newValue, oldValue) {
        this.$refs.tree.filter(newValue);
      },
  },
  created () {
  },
  methods: {
      /**
       * 节点关键字过滤 
       */
      filterNode(value, data) {
        if (!value) return true;
        return data.buildingName.indexOf(value) !== -1;
      },
    filterTreeCheck(e,e2){
      // console.log(e,e2);
      // console.log(this.$refs.tree.getCheckedKeys());
      this.checkedList=[];
       for(let i in e2.halfCheckedNodes){
        this.checkedList.push(e2.halfCheckedNodes[i])
      }
      for(let i in e2.checkedNodes){
        this.checkedList.push(e2.checkedNodes[i])
      }
       this.valueChange ()
    },
    /**
     * 删除一个选择项,如果当前选择项有子级，则会删除当前选择项的所有子级
     */
    selectDelete (selectItem, index) {
     
      let DeleteArray=[];
      this.$refs.tree.setChecked(selectItem,false,true)
      DeleteArray=this.$refs.tree.getCheckedNodes(false,true);
      // console.log(DeleteArray)
       this.checkedList=[];
       this.checkedList=DeleteArray;
    },
     /**
     * 清空按钮
     */
    clearAll () {
      this.checkedList = []
      this.$refs.tree.setCheckedKeys([],false)
       this.valueChange ()
    },
     /**
     *  回调数据
     */
    valueChange () {
      this.$emit('BuildingCheckList', this.checkedList)
    },
    /**
     * 获取楼栋数据
     */
    getBuildingDataList (value) {
      this.buildingLoading = true
     this.checkedList=[];
      this.buildingChildrens = [];
      let newDataList = [];
      for (let i = 0; i < this.MainData.length; i++) {
        if (value == this.MainData[i].buildingId) {
          newDataList.push(this.MainData[i]);
          break;
        }
      }
      queryQuestionsTreeList2(value).then(res => {
        newDataList[0].children = res.data;
        this.buildingChildrens = newDataList.map((item, index, array) => {
          item.isCheck = this.getItemCheckInfo(item)
          return item
        })
      
        this.buildingLoading = false
      }).catch(() => {
        this.buildingLoading = false
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

   
  }
}
</script>

<style scoped lang="scss">
@import "@/styles/mixin.scss";
.employee-department {
  display: flex;
  height: 100% !important;
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
      height: 340px;
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
/deep/div .el-tree .filter-tree{
  height: 80%;
}
/deep/ .el-tabs .el-tabs__content{
  height: 100%;
}
</style>

