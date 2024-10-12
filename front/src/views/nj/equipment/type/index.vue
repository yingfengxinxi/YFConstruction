<template>
  <div class="employee-dep-management">
    <!-- <xr-header
      :show-search="false"
      icon-class="wk wk-s-seas"
      icon-color="#26D4DA"
      label="设备类型管理"> -->

<!--    </xr-header>-->
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
                    class="wk wk-department" />
                  <span
                    v-else
                    class="node-data__mark" />

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
        <flexbox
          justify="space-between"
          class="table-top">
          <div>
            <el-button
              type="text"
              icon="el-icon-circle-plus"
              @click="add()">新增</el-button>
            <el-button
              type="text"
              icon="el-icon-delete-solid"
              @click="del()">删除</el-button>
          </div>
        </flexbox>

        <div class="flex-box">
          <el-form ref="equform" :model="form" :rules="rules" label-width="80px;background-color:#fff" style="padding: 40px 40% 0px 40px">
            <el-row style="border-bottom: 1px solid #e6e6e6;padding-bottom: 10px">
              <span>基本信息</span>
            </el-row>
            <el-row>
              <el-col :span="12" style="padding: 5px">
                <el-form-item label="上级类型编号">
                  <el-input v-model="form.typePcode" :disabled="true"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="padding: 5px">
                <el-form-item label="上级类型名称">
                  <el-input v-model="form.typePname" :disabled="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="padding: 5px">
                <el-form-item label="类型编号"  prop="typeCode">
                  <el-input v-model="form.typeCode" placeholder="请输入编号"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="padding: 5px">
                <el-form-item label="类型名称" prop="typeName">
                  <el-input v-model="form.typeName" placeholder="请输入名称"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12" style="padding: 5px">
                <el-form-item label="类型排序" prop="sort">
                  <el-input v-model="form.sort" type="number" placeholder="请输入排序"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="padding: 5px">
                <el-form-item label="报警时间(h)" prop="alarmHourTime">
                  <el-input v-model="form.alarmHourTime" type="number" placeholder="请输入报警时间"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row style="text-align: center;">
              <el-button type="primary" @click="save()">保存</el-button>
              <el-button type="info" @click="reset();">重置</el-button>
            </el-row>
            <el-row style="border-bottom: 1px solid #e6e6e6;padding-bottom: 10px">
              <span>系统信息</span>
            </el-row>
            <el-row>
              <el-col :span="12" style="padding: 5px">
                <el-form-item label="创建人">
                  <el-input v-model="form.createUserName" :disabled="true"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="padding: 5px">
                <el-form-item label="创建时间">
                  <el-input v-model="form.createTime" :disabled="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" style="padding: 5px">
                <el-form-item label="更新人">
                  <el-input v-model="form.updateUserName" :disabled="true"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="padding: 5px">
                <el-form-item label="更新时间">
                  <el-input v-model="form.updateTime" :disabled="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import XrHeader from '@/components/XrHeader'


import {
  typeList,
  typeGet,
  typeSave,
  typeDelete
} from '../api/type'

export default {
  /** 系统管理 的 员工部门管理 */
  name: 'equtype',
  components: {
    XrHeader
  },
  data() {
    return {
      treeLoading:false,
      showTreeData:[],
      formLoading:false,
      currTree:{},
      form:{},
      rules: {
        typeCode: [
          { required: true, message: '请输入类型编号', trigger: 'blur' },
        ],
        typeName: [
          { required: true, message: '请输入类型名称', trigger: 'blur' },
        ],
        sort: [
          { required: true, message: '请输入类型排序', trigger: 'blur' }
        ],

      }
    }
  },
  computed: {
  },
  mounted() {
    var self = this
    /** 控制table的高度 */
    window.onresize = function() {
      self.tableHeight = document.documentElement.clientHeight - 240
    }
    this.getTreeList()
  },
  methods: {
    //改变展开状态
    changeExpanded(node) {
      node.expanded ? node.expanded = false : node.expanded = true;
    },
    // 获取树形列表
    getTreeList() {
      this.treeLoading = true
      typeList()
        .then(response => {
          this.showTreeData = response.data || []
          this.treeLoading = false
        })
        .catch(() => {
          this.treeLoading = false
        })
    },
    /**
     * 选择部门
     */
    changeTreeClick(data, checked, indeterminate) {
      this.currTree = data;
      console.log(data)
      this.formLoading = true;
      typeGet(data.id)
        .then(data => {
          if(data.code == 0){
            this.form = data.data;
          }else{
            this.$message.error(data.msg);
          }
          this.formLoading = false;
        })
        .catch(() => {
          this.formLoading = false;
        })
    },
    add(){
      var data = this.form;
      this.form = {};
      this.form.typePid = data.typeId;
      this.form.typePcode = data.typeCode;
      this.form.typePname = data.typeName;

    },
    reset(){
      this.form = {};
    },
    save(){
      this.$refs['equform'].validate((valid) => {
        if (valid) {
          typeSave(this.form)
            .then(data => {
              if(data.code == 0){
                this.$message({
                  message: '保存成功',
                  type: 'success'
                });
                this.getTreeList();
                this.reset();
              }else{
                this.$message.error(data.msg);
              }
            })
            .catch(() => {
            })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    del(){

      if(!this.form.typeId){
        this.$message({
          type: 'info',
          message: '请选中左侧数据'
        });
        return;
      }
      if(this.currTree.children){
        this.$message({
          type: 'info',
          message: '存在下级数据，不得删除'
        });
        return;
      }
      this.$confirm('是否删除此数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        typeDelete(this.form)
          .then(data => {
            if(data.code == 0){
              this.$message({
                message: '删除成功',
                type: 'success'
              });
              this.getTreeList();
              this.reset();
            }else{
              this.$message.error(data.msg);
            }
          })
          .catch(() => {
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    }
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
  padding: 10px 15px;
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


</style>

