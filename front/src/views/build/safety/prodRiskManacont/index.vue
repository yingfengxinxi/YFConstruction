<template>
  <div class="main">
<!--    <xr-header-->
<!--      icon-class="wk wk-associate"-->
<!--      icon-color="#FFB940"-->
<!--      label="安全生产风险管控" />-->
    <div class="main-body">
      <div class="main-table-header">
        <el-button
          class="main-table-header-button xr-btn--orange"
          type="primary"
          icon="el-icon-plus"
          @click="addMenu">新建</el-button>
      </div>
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
        align="center"
      >
        <el-table-column
                type="index"
                label="序号"
                align="center"
                width="80">
        </el-table-column>
        <el-table-column
          prop="prodRiskName"
          label="管控源">
        </el-table-column>
        <el-table-column
          prop="sort"
          label="排序"
          align="center"
          width="120">
        </el-table-column>
        <el-table-column
                prop="status"
                label="状态"
                align="center"
                width="120">
          <template slot-scope="scope">
            {{getStatusText(scope.row.status) }}
          </template>
        </el-table-column>

        <el-table-column 
          prop="riskRating"
          label="风险评定等级"
          align="center"
          width="200"
          :formatter="riskRatingFormat">
        </el-table-column>

        <el-table-column 
          prop="riskIdenInstructions"
          label="风险辨识说明"
          align="center">
        </el-table-column>
        
        <el-table-column
          prop="remarks"
          label="备注说明"
          align="center"
          width="250">
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          align="center"
          width="250">
          <template slot-scope="scope">
            <el-button
              type="text"
              icon="el-icon-edit"
              size="small"
              @click="handleClick('edit', scope)">编辑</el-button>
            <el-button
              type="text"
              icon="el-icon-delete"
              size="small"
              @click="handleClick('delete', scope)">删除</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleClick('status', scope)">{{ scope.row['status'] === 0 ? '启用' : '停用' }}</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleClick('next', scope)">新建下级</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新增根管控源 -->
    <CreateRootMenu
      v-if="showCreateRootMenu"
      @save="getRootMenuList"
      @hiden-view="showCreateRootMenu=false"/>

    <!-- 新增下级节点 -->
    <CreateNextMenu
      v-if="showCreateNextMenu"
      :pid = "currentPid"
      @save="reloadSelf"
      @hiden-view="showCreateNextMenu=false"/>

    <!-- 编辑管控源 -->
    <EditMenu
      v-if="showEditMenu"
      :menu = "currentMenu"
      @save="reloadParent"
      @hiden-view="showEditMenu=false"/>
  </div>
</template>

<script>
  import{
    queryById,
	  queryByParentId,
	  deleteMenu,
	  updateMenu,
  } from '@/views/build/safety/api/prodRiskManacont'
  import XrHeader from '@/components/XrHeader'
  import CreateRootMenu from './components/CreateRootMenu'
  import CreateNextMenu from './components/CreateNextMenu'
  import EditMenu from './components/EditMenu'


export default {
  /** 安全生产风险管控 */
  name: 'prodRiskManacont',
  components: {
    XrHeader,
    CreateRootMenu,
    CreateNextMenu,
    EditMenu,
  },
  data() {
    return {
      //树型开始
      loading: false, // 加载动画
      currentPid: 0,//当前父级ID
      currentMenu:{},//当前管控源
      menuData: [],//最上级管控源
      expands:[],
      //根
      showCreateRootMenu: false,
      //下级
      showCreateNextMenu: false,
      //编辑
      showEditMenu:false,
      //字典项
      statusOptions:{'1':'启用','0':'禁用'},
      riskRatingOptions:[],//风险评定等级
    }
  },
  computed: {},
  created(){

    this.getDicts('b_safety_riskRating').then(response => {
      this.riskRatingOptions = response.data
    })
    
  },
  mounted() {
    this.getRootMenuList();
  },
  methods: {

    getStatusText(status){
      return this.statusOptions[status];
    },
    //管控风险评定等级
    riskRatingFormat(row, column){
      return this.selectDictLabel(this.riskRatingOptions, row.riskRating)
    },
    //树表开始
    test(){
      this.$refs.menuTable.toggleRowExpansion(this.currentMenu, true)
    },
    //行标记
    rowKey(row){
      return row.id;
    },
    expandChange(row, expandedRows){//row:被操作的行，expandedRows：是否是被展开
      row.children = [];
      // console.log(expandedRows)
    },
    //获取根级列表
    getRootMenuList() {
      queryByParentId(0)
        .then(res => {
          this.menuData = res.data;
        })
        .catch(() => {
          this.loading = false
        })
    },
    // 获取子节点列表
    getChildMenuList(tree, treeNode, resolve) {
      queryByParentId(tree.id)
        .then(res => {
          //console.log(res)
          resolve(res.data);
        })
        .catch(() => {
          this.loading = false
        })
    },
    reloadSelf(){//本级重载
      queryByParentId(this.currentMenu.id)
        .then(res => {
          this.reloadNode(this.currentMenu.id,res.data)
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
    addMenu(){//添加跟管控源
      this.showCreateRootMenu = true;
    },
    async handleClick(event,scope){
      //此处需同步调用
      await queryById(scope.row.id)
              .then(res => {
                this.currentMenu = res.data;
              })
      // this.currentMenu = scope.row;
      this.currentPid = this.currentMenu.id;
      if(event == 'next'){//创建下级
        this.showCreateNextMenu = true;
      }else if(event == 'edit'){
        this.showEditMenu = true;
      }else if(event == 'delete'){//删除
        this.$confirm('您确定要删除吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            deleteMenu(this.currentMenu.id)
              .then(res => {
                this.loading = false
                if(res.code == 0){
                  this.reloadParent()

                }else {
                  this.$message({
                    type: 'warn',
                    message: res.msg
                  })
                }
              })
              .catch(() => {
                this.loading = false
              })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            })
          })
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
            updateMenu({id:scope.row.id,status:toStatus})
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
  /*margin:0 15px;*/
  /deep/ .xr-header {
    padding: 15px 30px;
  }
}

.main-body {
  background-color: white;
  /*border-top: 1px solid $xr-border-line-color;*/
  border-bottom: 1px solid $xr-border-line-color;
  padding: 10px;
  height: 100%;
  overflow-y: auto;
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
@import 'src/views/styles/table.scss';
</style>
