<template>
  <div class="employee-dep-management">
    <div class="system-content">
      <!-- 左边导航栏 -->
      <div
        v-loading="treeLoading"
        class="system-nav">
        <div class="system-nav__title">
          类型信息
        </div>
        <div class="system-nav__content">
          <div class="section">
            <div class="section__content">
              <el-tree
                ref="tree"
                :data="showTreeData"
                node-key="id"
                highlight-current
                default-expand-all
                :expand-on-click-node="false"
                @node-click="changeTreeClick">
                <flexbox
                  slot-scope="{ node }"
                  :class="{ 'is-current': node.isCurrent}"
                  class="node-data">
                  <i
                    v-if="node.level == 1"
                    class="wk wk-department"/>
                  <span
                    v-else
                    class="node-data__mark"/>

                  <div class="node-data__label text-one-line ">{{ node.label }}</div>
                  <i
                    v-if="node.childNodes && node.childNodes.length"
                    :class="{ 'is-close': !node.expanded }"
                    class="wk wk-up-unfold" @click="changeExpanded(node)"/>
                </flexbox>
              </el-tree>
            </div>
          </div>

        </div>
      </div>
      <!-- 右边内容 -->
      <div class="system-view-table flex-index" v-loading="formLoading">

        <div class="flex-box">
          <el-table
            :data="tableData"
            border
            highlight-current-row
            :height="tableHeight"
            @selection-change="handleChange"
            style="width: 100%">
            <el-table-column
              type="selection"
              :selectable="checkSelectable"
              width="55"></el-table-column>
            <el-table-column label="序号" type="index" show-overflow-tooltip width="70">
            </el-table-column>
            <el-table-column
              prop="assetName"
              label="资产名称"
              >
            </el-table-column>
            <el-table-column
              prop="assetCode"
              label="资产编号"
              width="120">
            </el-table-column>
            <el-table-column
              prop="locationName"
              label="资产位置"
              width="150">
            </el-table-column>
            <el-table-column
              prop="deptName"
              label="负责部门"
              width="150">
            </el-table-column>
            <el-table-column
              prop="typeName"
              label="资产类型"
              width="150">
            </el-table-column>
            <el-table-column
              prop="sort"
              label="排序"
              width="80">
            </el-table-column>
          </el-table>
          <div class="p-contianer">

            <el-pagination
              :current-page="currentPage"
              :page-sizes="pageSizes"
              :page-size.sync="pageSize"
              :total="total"
              class="p-bar"
              background
              layout="prev, pager, next, sizes, total, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"/>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>

  import {
    typeList,
  } from '../../api/type'

  import {depListAPI} from '@/api/common' // 直属上级接口

  import {
    assetsList,
  } from '../../api/assets'

  export default {
    /** 系统管理 的 员工部门管理 */
    name: 'EquType',
    props: {
      planId: {
        require: true,
        type: Number,
        default: -1
      },
      checkedRows: {
        required: false,
        type: Array,
        default: []
      }
    },
    watch: {
      planId() {
        this.getTableList();
      },
    },
    components: {},
    data() {
      return {
        tableHeight: document.documentElement.clientHeight - 415, // 表的高度
        statusOptions: [
          {value: '0', label: '报废'},
          {value: '1', label: '正常'}
          // { value: '2', label: '正常' }
        ],
        //类型
        treeLoading: false,
        showTreeData: [
          {
            id: 0,
            label: "全部类型",
            parentId: -1,
            children: []
          }
        ],
        currTree: {},
        //列表
        searchInput: '',
        tableLoading: false,
        tableData: [],
        // 分页
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 30, 40],
        total: 0,
        //新增编辑
        dialogTitle: '',
        formLoading: false,
        formDialog: false,
        form: {},
        rules: {
          assetCode: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          assetName: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          locationId: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          typeId: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          brand: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          specification: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          model: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          status: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          sort: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          deptId: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
        },
        // 数据默认字段
        defaultProps: {
          parent: 'parentId',   // 父级唯一标识
          value: 'id',          // 唯一标识
          label: 'label',       // 标签显示
          children: 'children', // 子级
        },
        showLocationTreeData: [],
        showDeptTreeData: [],
        levelOptions: [],
        statusOptions: [],
        //附件
        imgFileList: [],
      }
    },
    computed: {},
    mounted() {
      var self = this
      /** 控制table的高度 */
      window.onresize = function () {
        self.tableHeight = document.documentElement.clientHeight - 240
      }
      this.getTreeList();
      this.getTableList();
      //状态
      this.getDicts('equ_assets_status').then(response => {
        this.statusOptions = response.data
      })
      //重要程度
      this.getDicts('equ_assets_level').then(response => {
        this.levelOptions = response.data
      })
    },
    methods: {
      //禁止选中的条件
      checkSelectable(row) {
        var flag = true;
        this.checkedRows.forEach(checkedRow => {
          if(checkedRow.assetId == row.assetId){
            flag = false;
          }
        })
        return flag;
      },
      //改变展开状态
      changeExpanded(node) {
        node.expanded ? node.expanded = false : node.expanded = true;
      },
      //处理选中
      handleChange(rows) {
        this.$emit('change', rows)
      },
      /**
       * 头部搜索
       */
      headerSearch(search) {
        this.searchInput = search
      },
      // 获取树形列表
      getTreeList() {
        this.treeLoading = true
        typeList()
          .then(response => {
            this.showTreeData[0]['children'] = response.data || []
            this.treeLoading = false
          })
          .catch(() => {
            this.treeLoading = false
          })
      },

      /**
       * 选择类型
       */
      changeTreeClick(data, checked, indeterminate) {
        this.currTree = data;
        this.getTableList();
      },
      getTableList() {
        var data = {search: this.searchInput}
        if (this.currTree.id != 0) {
          data.typeId = this.currTree.id;
        }
        data.status = '1';
        data.page = this.currentPage
        data.limit = this.pageSize
        this.tableLoading = true;
        assetsList(data)
          .then(data => {
            if (data.code == 0) {
              this.tableData = data.data.list;
              this.total = data.data.totalRow
            } else {
              this.$message.error(data.msg);
            }
            this.tableLoading = false;
          })
          .catch(() => {
            this.tableLoading = false;
          })
      },
      // 更改每页展示数量
      handleSizeChange(val) {
        this.pageSize = val
        this.getTableList()
      },
      // 更改当前页数
      handleCurrentChange(val) {
        this.currentPage = val
        this.getTableList()
      },
      // 获取状态颜色 0 报废 1 正常
      getStatusColor(status) {
        if (status == '0') {
          return '#FF6767'
        } else {
          return '#46CDCF'
        }
        // else if (status == 2) {
        //   return '#CCCCCC'
        // }
      },
    }
  }
</script>

<style lang="scss" scoped>
  @import '@/views/login/index.scss';

  .verify-picture {
    margin-top: 20px;
  }

  .sms-box {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    flex-wrap: nowrap;
    .el-input {
      width: 210px;
    }
    .el-button {
      flex: 1;
      font-size: 12px;
      color: white;
      background-color: #3e6bea;
      border-radius: $xr-border-radius-base;
      border: 0 none;
      padding: 0;
      margin-left: 20px;

      .btn-content {
        width: 100%;
        height: 42px;
        @include center;
        .icon {
          font-size: 16px;
          margin-right: 5px;
        }
      }
      &:hover,
      &.is-disabled,
      &.is-disabled:hover {
        color: white;
        border-color: #517aec;
        background-color: #517aec;
      }
    }
  }

  .employee-dep-management {
    padding: 0 15px;
    height: 100%;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
  }

  .system-content {
    position: relative;
    height: 100%;
    flex: 1;
    display: flex;
    overflow: hidden;
  }

  .system-nav {
    width: 280px;
    height: 100%;
    overflow: auto;
    margin-right: 10px;
    background: #fff;
    border: 1px solid $xr-border-line-color;
    border-radius: $xr-border-radius-base;

    &__title {
      padding: 15px;
      font-size: 16px;
      font-weight: 600;
      border-bottom: 1px solid $xr-border-line-color;
    }

    &__content {
      overflow: auto;
      height: calc(100% - 50px);
      overflow-y: overlay;
      overflow-x: overlay;
    }
  }

  // 菜单
  .section {
    position: relative;
    &__title {
      position: relative;
      font-size: 14px;
      font-weight: 600;
      color: #333;
      padding: 15px;
      .add-btn {
        position: absolute;
        right: 10px;
        top: 10px;
      }
    }
  }

  .menu-item {
    position: relative;
    cursor: pointer;
    padding: 12px 15px;
    color: #333;

    &__icon {
      font-size: 14px;
      margin-right: 8px;
      color: #8a94a6;
    }

    &__content {
      font-size: 14px;
    }
  }

  .menu-item:hover,
  .menu-item.is-select {
    background-color: $xr--background-color-base;
  }

  .menu-item:hover::before,
  .menu-item.is-select::before {
    content: ' ';
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    width: 2px;
    background-color: #5383ed;
  }

  .system-view-table {
    background: #fff;
    border: 1px solid $xr-border-line-color;
    border-radius: $xr-border-radius-base;
    position: absolute;
    top: 0;
    left: 295px;
    bottom: 0;
    right: 0;
  }

  .table-top {
    padding: 0 30px;
    height: 50px;
    border-bottom: 1px solid #e6e6e6;
    &__title {
      font-size: 16px;
      color: #333;
    }

    .el-dropdown {
      margin-left: 7px;
    }
  }

  .all-user-reminder {
    width: auto;
    margin-left: 5px;
    display: inline-block;
  }

  .el-table /deep/ .el-table-column--selection .cell {
    padding-left: 14px;
  }

  // .status {
  //   display: inline-block;
  //   margin-left: 50px;
  // }
  // .status > span {
  //   margin-right: 10px;
  // }

  .status-name {
    div {
      display: inline-block;
      width: 6px;
      height: 6px;
      border-radius: 3px;
    }
    color: $xr-color-primary;
    cursor: pointer;
  }

  /** 树形结构 */
  .el-tree /deep/ .el-tree-node__expand-icon {
    display: none;
  }

  .el-tree /deep/ .el-tree-node__content {
    height: 40px;

    .node-data {
      // .node-img {
      //   width: 15px;
      //   height: 15px;
      //   display: block;
      //   margin-right: 8px;
      //   margin-left: 24px;
      // }
      height: 40px;
      padding: 0 15px;
      position: relative;
      border-radius: $xr-border-radius-base;

      .wk {
        font-size: 14px;
        color: #8a94a6;
        flex-shrink: 0;
      }

      .wk-department {
        margin-right: 8px;
      }

      &__mark {
        display: inline-block;
        width: 6px;
        height: 6px;
        border-radius: 50%;
        background-color: #e6e6e6;
        margin-right: 8px;
        flex-shrink: 0;
      }

      &__label {
        flex: 1;
        color: #333;
        font-size: 14px;
        margin-right: 8px;
      }

      .wk-up-unfold {
        margin-left: 8px;
        transition: transform 0.3s;
      }

      .wk-up-unfold.is-close {
        transform: rotateZ(180deg);
      }
      // .node-label-set {
      //   display: none;
      // }
    }

    .node-data.is-current,
    .node-data:hover {
      background-color: $xr--background-color-base;
    }

    // .node-data:hover .node-label-set {
    //   display: block;
    // }
  }

  // .el-tree /deep/ .el-tree-node.is-current > .el-tree-node__content {
  //   background-color: #ebf3ff;
  //   border-right: 2px solid #46cdcf;
  //   .node-label-set {
  //     display: block;
  //   }
  // }
  .system-nav /deep/ .el-tree-node > .el-tree-node__children {
    overflow: visible;
  }

  .system-nav /deep/ .el-tree > .el-tree-node {
    min-width: 100%;
    display: inline-block !important;
  }

  .system-nav
  /deep/
  .el-tree--highlight-current
  .el-tree-node.is-current
  > .el-tree-node__content {
    background-color: white;
  }

  .system-nav /deep/ .el-tree-node__content:hover {
    background-color: white;
  }

  /* 搜索框图标按钮 */
  .icon-search .el-icon-search {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    width: 40px;
    line-height: 40px;
    text-align: center;
    cursor: pointer;
    font-size: 20px;
    color: #ccc;
  }

  /* 设置flex布局 */
  .flex-index {
    display: flex;
    flex-direction: column;
  }

  /* 设置占位 */
  .flex-box {
    flex: 1;
    // border-bottom: 1px solid $xr-border-line-color;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content
  .el-select-group__wrap:not(:last-of-type)::after {
    display: none;
  }

  .new-dialog-form /deep/ .el-form-item .el-form-item__content .el-select-group {
    padding-left: 10px;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content
  .el-select-group__title {
    border-bottom: 1px solid #e4e7ed;
    padding: 0 0 7px;
    margin: 0 20px 5px;
  }

  .new-dialog-form
  /deep/
  .el-form-item {
    width: 100%;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__label {
    text-align: right;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content {
    width: calc(100% - 120px);
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-date-editor--date {
    width: 100%;
  }

  .status-des {
    font-size: 12px;
    color: #777777;
    margin: 0 5px;
    position: absolute;
    left: 0;
    top: 7px;
    .status-des-item {
      margin: 8px;
      display: inline-block;
      div {
        display: inline-block;
        width: 6px;
        height: 6px;
        border-radius: 3px;
        margin-right: 5px;
      }
    }
  }

  // 提示
  // 提示标志
  .wukong-help_tips {
    color: #999;
    font-size: 14px;
    margin-left: 3px;
    cursor: pointer;
  }

  .wukong-help_tips:hover {
    color: $xr-color-primary;
  }

  .tips {
    font-size: 13px;
    color: #999;
  }

  // 图片 附件
  .img-accessory {
    padding: 0 20px;
    font-size: 12px;
    img {
      width: 16px;
      vertical-align: middle;
    }
    .img-box /deep/ .el-upload {
      width: 80px;
      height: 80px;
      line-height: 90px;
    }
    .img-box /deep/ .el-upload-list {
      .el-upload-list__item {
        width: 80px;
        height: 80px;
      }
    }
    .img-box {
      position: relative;
      margin-top: 40px;
      .add-img {
        position: absolute;
        left: 0;
        top: -30px;
        height: 20px;
        line-height: 20px;
        margin-bottom: 10px;
        color: #2362FB;
      }
    }
    .add-accessory {
      margin-top: 25px;
      margin-bottom: 20px;
      color: #2362FB;

      .wukong-file {
        font-size: 13px;
      }
    }
  }

  @import '@/views/styles/table.scss';
</style>

