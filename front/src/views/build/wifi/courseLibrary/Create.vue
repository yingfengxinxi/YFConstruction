<template>
  <xr-create :loading="loading" :title="title" :confirm-button-text="'保存'" @close="hidenView" @save="debouncedSaveField(true)" :showSave="!disabledT">

    <create-sections title="基本信息" style="margin-bottom: 20px;">
      <el-form v-loading="loading" class="form" ref="menuForm" :rules="memuRules" :model="menuForm" :disabled="disabledT" label-width="115px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="课程视频名称" prop="courseVideoName">
              <el-input v-model="menuForm.courseVideoName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="适用工种" class='special' prop="workTypeId">
              <el-select v-model="menuForm.workTypeId" placeholder="请选择" clearable size="small" style="width: 100%">
                <el-option v-for="dict in workTypeOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="margin-top: 20px;">
          <el-col :span="24">
            <el-form-item label="视频文件" prop="videoFile">
              <wk-upload-file ref="wkUploadFile" name="file" accept="video/*" :limit='1' :autoUpload="false" :batchId="batchId" :size="500"></wk-upload-file>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </create-sections>
  </xr-create>
</template>
<script type="text/javascript">

import XrCreate from '@/components/XrCreate'
import CreateSections from '@/components/CreateSections'
import { guid } from '@/utils'
import { debounce } from 'throttle-debounce'
import WkUploadFile from '@/components/CreateCom/WkUploadFile'

import { saveCourseVideo, updateCourseVideoById } from '@/views/build/wifi/api/CourseLibrary'
import { getWTList } from '@/api/build/infocollection/infocoll'
export default {
  name: 'CreateMenu', // 所有新建效果的view
  components: {//声明组件
    XrCreate,
    CreateSections,
    WkUploadFile
  },
  filters: {//过滤器
  },
  props: {//属性
    menuForm: {},
    actionType: ''
  },
  data () {
    return {
      //加载动画
      loading: true,
      // 表的高度
      tableHeight: document.documentElement.clientHeight - 450,
      // 工种数据
      worktypeList: [],
			workTypeOptions: [],//工种字典
      //附件id
      batchId: '',
      // 自定义字段验证规则
      memuRules: {
        courseVideoName: [
          { required: true, message: '必填项不能为空', trigger: 'blur' }
        ],
        workTypeId: [
          { required: true, message: '必填项不能为空', trigger: 'blur' }
        ]

      },

    }
  },
  created () {
    this.debouncedSaveField = debounce(300, this.save)
    //工种数据
    //this.getWorktypeList()
		// 工种字典
		this.getDicts('b_labour_work_type').then(response => {
		  this.workTypeOptions = response.data
		  this.loading = false
		})
    //考虑附件ID
    if (this.actionType == "update") {
      // this.getDetail();
      this.batchId = this.menuForm.batchId
    } else {
      this.batchId = guid();
    }
  },
  computed: {//监听属性
    title () {
      return this.actionType === 'update' ? '编辑课程库配置' : (this.actionType === 'show' ? '查看课程库配置' : '新建课程库配置')
    },
    disabledT () {
      return this.actionType === 'show' ? true : false
    },
  },
  mounted () { },//渲染完成后
  destroyed () { },//销毁后

  methods: {//声明方法
    save () {
      this.$refs.menuForm.validate(valid => {
        if (valid) {
          this.loading = true;
          this.menuForm.batchId = this.batchId;
          var fileFlag = this.$refs.wkUploadFile.submitAsync();
          if (!fileFlag) {
            this.loading = false;
            this.$message({
              type: 'error',
              message: '视频附件提交异常'
            })
            return;
          } else {
            if (this.actionType == 'create') {
              saveCourseVideo(this.menuForm)
                .then(res => {
                  this.msgSuccess('添加成功')
                  this.$emit('save');
                  this.hidenView();
                  this.loading = false;
                })
                .catch(() => {
                  this.loading = false
                })
            } else {
              updateCourseVideoById(this.menuForm)
                .then(res => {
                  this.msgSuccess('修改成功')
                  this.$emit('save');
                  this.hidenView();
                  this.loading = false;
                })
                .catch(() => {
                  this.loading = false
                })
            }
          }
        } else {
          this.$message.error('请完善必填信息')
          return false
        }
      })
    },




    hidenView () {
      this.$emit('hiden-view')
    },

    /** 查询所工种数据列表 */
    getWorktypeList () {
      getWTList().then(response => {
        this.worktypeList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
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
  content: " ";
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

// .el-form /deep/ .el-form-item.is-required .el-form-item__label:before {
// content: '*';
// color: #f56c6c;
// margin-right: 4px;
// position: absolute;
// left: 0;
// top: 5px;
// }

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

.form /deep/ .el-date-editor,
.el-select {
  width: 100%;
}
</style>
