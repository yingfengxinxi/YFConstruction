<template>
  <el-dialog
    title="编辑质量问题"
    :visible="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    :close-on-click-modal="false"
    class="create-dialog"
    width="500px"
    @close="hidenView">
    <el-form
      v-loading="loading"
      ref="menuForm"
      :rules="memuRules"
      :model="menuForm"
      label-width="100px">
        <el-form-item
          label="质量问题"
          prop="questionName">
          <el-input v-model="menuForm.questionName" />
        </el-form-item>
        <el-form-item
                label="排  序"
                prop="sort">
          <el-input type="number" v-model="menuForm.sort" />
        </el-form-item>
        <el-form-item
          label="状  态"
          prop="status">
          <el-radio-group v-model="menuForm.status">
            <el-radio :label="0">停用</el-radio>
            <el-radio :label="1">启用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="备  注"
          prop="remarks">
          <el-input v-model="menuForm.remarks" :row="2"
                    :maxlength="300"
                    type="textarea"/>
        </el-form-item>
    </el-form>
    <span
      slot="footer"
      class="dialog-footer">
      <el-button
        type="primary"
        @click.native="updateMenu">保 存</el-button>
      <el-button @click.native="hidenView">取 消</el-button>
    </span>
  </el-dialog>
</template>
<script type="text/javascript">
  import {
    updateMenu,
  } from '@/views/build/qual/api/question'

export default {
  name: 'EditMenu', // 所有新建效果的view
  components: {//声明组件
  },
  filters: {//过滤器
  },
  props: {//属性
    menu: {
      type: Object,
      default: {}
    },
  },
  data() {
    return {
      loading: true,
      // 自定义字段验证规则
      memuRules: {
        questionName: [
          { required: true, message: '质量问题名称不能为空', trigger: 'blur' }
        ]
      },
      // 自定义字段信息表单
      menuForm: {
      },
      menuTypeDisabled:false,
    }
  },
  computed: {//监听属性

  },
  mounted() {//渲染完成后
    this.loading = false;
    this.menuForm = this.menu;
  },
  destroyed() {//销毁后
  },
  methods: {//声明方法
    updateMenu(){
      this.$refs.menuForm.validate(valid => {
        if (valid) {
          this.loading = true;
          updateMenu(this.menuForm)
            .then(res => {
              console.log(res)
              this.$emit('save');
              this.hidenView();
              this.loading = false;
            })
            .catch(() => {
              this.loading = false
            })
        } else {
          this.$message.error('请完善必填信息')
          return false
        }
      })
    },
    hidenView() {
      this.$emit('hiden-view')
    },
  }
}
</script>
<style lang="scss" scoped>
  .crm-create-container {
    position: relative;
    height: 100%;
  }

  .crm-create-flex {
    position: relative;
    overflow-x: hidden;
    overflow-y: auto;
    flex: 1;
  }

  .crm-create-header {
    height: 40px;
    margin-bottom: 20px;
    padding: 0 10px;
    flex-shrink: 0;
    .close {
      display: block;
      font-size: 24px;
      color: #909399;
      margin-right: -10px;
      padding: 10px;
      cursor: pointer;
    }
    .close:hover {
      color: $xr-color-primary;
    }
  }
  .create-name {
    font-size: 14px;
    color: #333;
    font-weight: 600;
    padding-left: 12px;
    position: relative;
    margin-left: 15px;
    margin-bottom: 10px;
  }

  .create-name::before {
    content: ' ';
    position: absolute;
    top: 0;
    left: 0;
    height: 14px;
    border-radius: 2px;
    width: 4px;
    background-color: $xr-color-primary;
  }

  .crm-create-body {
    flex: 1;
    overflow-x: hidden;
    overflow-y: auto;
  }

  /** 将其改变为flex布局 */
  .crm-create-box {
    display: flex;
    flex-wrap: wrap;
    padding: 0 20px;
  }

  .crm-create-item {
    flex: 0 0 50%;
    flex-shrink: 0;
    padding-bottom: 10px;
  }

  // 占用一整行
  .crm-create-block-item {
    flex: 0 0 100%;
    flex-shrink: 0;
    padding-bottom: 10px;
  }

  .el-form-item /deep/ .el-form-item__label {
    font-size: 13px;
    color: #333333;
    position: relative;
    padding-left: 8px;
    padding-bottom: 0;
  }

  .el-form /deep/ .el-form-item {
    margin-bottom: 0px;
  }

  .el-form /deep/ .el-form-item.is-required .el-form-item__label:before {
    content: '*';
    color: #f56c6c;
    margin-right: 4px;
    position: absolute;
    left: 0;
    top: 5px;
  }

  .form-label {
    margin: 5px 0;
    font-size: 13px;
    word-wrap: break-word;
    word-break: break-all;
  }

  .examine-items {
    padding: 10px 0;
  }

  .examine-item {
    padding-bottom: 8px;
    .examine-item-name {
      width: 60px;
      padding-left: 20px;
      font-size: 13px;
      margin-right: 20px;
    }
    .examine-item-select {
      margin-right: 20px;
    }
    .examine-item-user {
      flex: 1;
      margin-right: 42px;
    }
    .examine-item-delete {
      color: #ff6767;
      font-size: 22px;
      margin: 0 10px;
      display: none;
    }
  }

  .examine-item:hover {
    .examine-item-delete {
      display: block;
    }
    .examine-item-user {
      margin-right: 0px;
    }
  }

  .examine-items-add {
    padding: 5px 0 20px 0;
    font-size: 13px;
    color: $xr-color-primary;
  }

  .examine-add-des {
    font-size: 12px;
    background-color: #fffcf0;
    padding: 10px;
    line-height: 23px;
    color: #999;
    margin-bottom: 10px;
    .examine-add-required {
      color: #ff6767;
    }
  }
  .handle-bar {
    position: relative;
    .handle-button {
      float: right;
      margin-top: 5px;
      margin-right: 20px;
    }
  }
</style>
