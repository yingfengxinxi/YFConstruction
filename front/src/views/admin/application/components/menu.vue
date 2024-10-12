<template>
  <div class="main">
    <xr-header
      icon-class="wk wk-associate"
      icon-color="#FFB940"
      label="应用管理" />
    <div class="main-body">
      <el-table
        ref="menuTable"
        :data="menuData"
        style="width: 100%"
        :row-key="rowKey"
        border
        lazy
        :load="getChildMenuList"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        :expand-row-keys="expands"
        @expand-change="expandChange"
      >
        <el-table-column
          prop="menuName"
          label="名称"
          >
          <template slot-scope="scope">
            <i :class="scope.row.icon" />
            {{scope.row.menuName }}
          </template>
        </el-table-column>
        <el-table-column
          prop="menuType"
          label="类型"
          >
          <template slot-scope="scope">
            {{getMenuTypeText(scope.row.menuType) }}
          </template>
        </el-table-column>

        <el-table-column
          prop="status"
          label="状态"
          >
          <template slot-scope="scope">
            {{getStatusText(scope.row.status) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="sort"
          label="排序"
          width="120">
        </el-table-column>

        <el-table-column
          fixed="right"
          label="操作"
          width="250">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="handleClick('edit', scope)">编辑</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleClick('status', scope)">{{ scope.row['status'] === 0 ? '启用' : '停用' }}</el-button>

          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 编辑菜单 -->
    <EditMenu
      v-if="showEditMenu"
      :menu = "currentMenu"
      @save="reloadParent"
      @hiden-view="showEditMenu=false"/>
  </div>
</template>

<script>
  import {
    queryById,
    queryByParentIdTenant,
    updateMenu,
  } from '@/api/admin/menu'
  import XrHeader from '@/components/XrHeader'

  import EditMenu from './EditMenu'


export default {
  /** 系统管理 的 菜单管理 */
  name: 'SystemMenu',
  components: {
    XrHeader,
    EditMenu,
  },
  data() {
    return {
      //树型开始
      loading: false, // 加载动画
      currentPid: 0,//当前父级ID
      currentMenu:{},//当前菜单
      menuData: [],//最上级菜单
      expands:[],
      //编辑
      showEditMenu:false,
      //字典项
      menuTypeOptions:{'1':'应用','2':'菜单','3':'按钮','4':'特殊','5':'手机端'},
      statusOptions:{'1':'启用','0':'禁用'},
      parentId:{},
    }
  },
  computed: {},
  mounted() {
    this.parentId = this.$route.params.parentId;
    console.log(this.parentId)
    this.getRootMenuList();
  },
  methods: {

    //换取字典
    getMenuTypeText(menuType){
      return this.menuTypeOptions[menuType];
    },
    getStatusText(status){
      return this.statusOptions[status];
    },
    //树表开始
    test(){
      this.$refs.menuTable.toggleRowExpansion(this.currentMenu, true)
    },
    //行标记
    rowKey(row){
      return row.menuId;
    },
    expandChange(row, expandedRows){//row:被操作的行，expandedRows：是否是被展开
      row.children = [];
      console.log(expandedRows)
    },
    //获取根级列表
    getRootMenuList() {
      this.menuData = []
      queryById(this.parentId)
        .then(res => {
          this.menuData.push(res.data);

        })
        .catch(() => {
          this.loading = false
        })
    },
    // 获取子节点列表
    getChildMenuList(tree, treeNode, resolve) {
      queryByParentIdTenant(tree.menuId)
        .then(res => {
          //console.log(res)
          resolve(res.data);
        })
        .catch(() => {
          this.loading = false
        })
    },
    reloadSelf(){//本级重载
      queryByParentId(this.currentMenu.menuId)
        .then(res => {
          //console.log(res)
          this.reloadNode(this.currentMenu.menuId,res.data)
        })
        .catch(() => {
          this.loading = false
        })
    },
    reloadParent(){//上级重载
      if(this.currentMenu.parentId == 0){
        this.getRootMenuList()
        return;
      }
      queryByParentId(this.currentMenu.parentId)
        .then(res => {
          //console.log(res)
          this.reloadNode(this.currentMenu.parentId,res.data)
        })
        .catch(() => {
          this.loading = false
        })
    },
    // 重载节点数据
    reloadNode(parentId,data) {
      this.$set(this.$refs.menuTable.store.states.lazyTreeNodeMap, parentId, data);
    },
    handleClick(event,scope){
      this.currentMenu = scope.row;
      this.currentPid = this.currentMenu.menuId;
      if(event == 'edit'){
        this.showEditMenu = true;
      }else if(event == 'status'){//启用/禁用
        var status = scope.row.status;
        var conText = '';
        var toStatus = 0;
        if(status == '1'){
          conText = '停用';
          toStatus = 0;
        }else{
          conText = '启用';
          toStatus = 1;
        }
        this.$confirm('您确定'+conText+'吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            updateMenu({menuId:scope.row.menuId,status:toStatus})
              .then(res => {
                this.reloadParent()
              })
              .catch(() => {
                this.loading = false
              })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消'
            })
          })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.main {
  height:100%;

  /deep/ .xr-header {
    padding: 15px 30px;
  }
}

.main-body {
  background-color: white;
  /*border-top: 1px solid $xr-border-line-color;*/
  border-bottom: 1px solid $xr-border-line-color;
  padding: 10px;
}

.main-table-header {
  height: 50px;
  background-color: white;
  position: relative;
  .main-table-header-button {
    float: right;
    margin-right: 20px;
    margin-top: 10px;
  }
}

.p-contianer {
  position: relative;
  background-color: white;
  height: 44px;
  .p-bar {
    float: right;
    margin: 5px 100px 0 0;
    font-size: 14px !important;
  }
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
@import '../../styles/table.scss';
</style>
