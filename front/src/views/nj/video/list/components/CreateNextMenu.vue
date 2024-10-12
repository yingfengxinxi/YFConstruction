<template>
  <el-dialog
    title="新建视频监控"
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
        label="设备名称"
        prop="deviceName">
        <el-input v-model="menuForm.deviceName"/>
      </el-form-item>
      <el-form-item
        label="监控名称"
        prop="monitorName">
        <el-input v-model="menuForm.monitorName"/>
      </el-form-item>
      <el-form-item
        label="设备编号"
        prop="deviceCode">
        <el-input v-model="menuForm.deviceCode"/>
      </el-form-item>
      <el-form-item
        label="视频编号"
        prop="deviceCode">
        <el-input v-model="menuForm.videoCode"/>
      </el-form-item>
      <el-form-item
        label="类型"
        prop="type">
        <el-select v-model="menuForm.type" placeholder="请选择" >
          <el-option label="分组" value="1" />
          <el-option label="摄像头" value="2" />
        </el-select>
      </el-form-item>
      <!--<el-form-item-->
        <!--label="分组"-->
        <!--prop="deviceCode">-->
        <!--<el-input v-model="menuForm.group"/>-->
      <!--</el-form-item>-->
    </el-form>
    <span
      slot="footer"
      class="dialog-footer">
      <el-button
        type="primary"
        @click.native="saveRootMenu">保 存</el-button>
      <el-button @click.native="hidenView">取 消</el-button>
    </span>
  </el-dialog>
</template>
<script type="text/javascript">
  import {
    addVideoMonitor,
  } from '@/api/build/videoMonitor'
  export default {
    name: 'CreateNextMenu', // 所有新建效果的view
    components: {//声明组件
    },
    filters: {//过滤器
    },
    props: {//属性
      pquestionId: {
        type: Number,
        default: 0
      },
    },
    data() {
      return {
        loading: true,
        // 验证规则
        memuRules: {
          questionName: [
            {required: true, message: '质量问题不能为空', trigger: 'blur'}
          ]
        },
        // 自定义字段信息表单
        menuForm: {
          status: '1'
        },

      }
    },
    computed: {//监听属性

    },
    mounted() {//渲染完成后
      this.loading = false;
      this.menuForm.parentId = this.pquestionId;
      console.log(this.menuForm.parentId)
    },
    destroyed() {//销毁后
    },
    methods: {//声明方法
      saveRootMenu() {
        this.$refs.menuForm.validate(valid => {
          if (valid) {
            this.loading = true;
            addVideoMonitor(this.menuForm)
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
