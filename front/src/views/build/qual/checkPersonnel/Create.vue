<template>
  <xr-create :loading="loading" :title="title" :confirm-button-text="'保存'" @close="close" @save="debouncedSaveField(!disabledT)">

    <el-form ref="form" :model="form" :disabled="disabledT" :rules="rules" label-position="top" label-width="100px" class="form">
      <el-row>
        <el-col :span="12">
          <el-form-item label="人员姓名" prop="personName">
            <el-input v-model="form.personName" placeholder="请输入人员姓名"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="personPhone">
            <el-input v-model="form.personPhone" placeholder="请输入联系电话"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="性别" prop="personSex">
            <el-select v-model="form.personSex" placeholder="请选择性别" clearable style="width: 405px">
              <el-option v-for="dict in acceptPersonSex" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位" prop="personCom">
            <el-input v-model="form.personCom" placeholder="请输入单位"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="工作职责" prop="workDuty">
            <el-input v-model="form.workDuty" placeholder="请输入工作职责"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="人员资质" prop="personSeniority">
            <el-input v-model="form.personSeniority" placeholder="请输入人员资质  "></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="验收内容" prop="acceptContent">
            <el-input v-model="form.acceptContent" type="textarea" :rows="2" placeholder="请输入验收内容  "></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="上传人员照片">
            <el-upload v-if="!QYsystemImage" :show-file-list="false" :http-request="fileUploadQY" drag class="upload" action="http" accept="image/png, image/jpeg, image/gif, image/jpg">
              <i class="el-icon-plus uploader-icon" />
            </el-upload>
            <div v-else class="upload-show">
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="QYsystemImage">
              <i class="el-icon-remove icon-delete" v-if="!disabledT" @click="deleteSystemImageQY" />
            </div>
            <edit-image :fixed-number="[20, 20]" :show="QYshowEditImage" :image="QYeditImage" title="上传人员照片" preview-width="200px" preview-height="200px" preview-radius="0" width="550px" save-button-title="确定" @save="submiteImageQY" @close="QYshowEditImage=false" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </xr-create>
</template>

<script>

import XrCreate from '@/components/XrCreate'
import CreateSections from '@/components/CreateSections'
import EditImage from '@/components/EditImage'

import { guid } from '@/utils'

import { debounce } from 'throttle-debounce'
import { saveOrUpdate, queryById } from "../api/checkPersonnel";
export default {
  // 新建编辑
  name: 'houseTypeMangeCreate',
  components: {
    XrCreate,
    CreateSections,
    EditImage,
  },

  props: {
    action: {
      type: Object,
      default: () => {
        return {
          type: 'save',
          id: '',
          monitType: '',
          data: {}
        }
      }
    },
    titleName: '',
  },
  data () {
    return {
      loading: false,
      form: {},
      list: [],
      tableHeight: 320, // 表的高度
      rules: {
        personName: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        personPhone: [
          { required: true, message: '必填项不得为空', trigger: 'blur' },
          { validator: function (rule, value, callback) {
              if (/^1[34578]\d{9}$/.test(value) == false) {
                callback(new Error("请输入正确的手机号"));
              } else {
                callback();
              }
            }, trigger: 'blur'
          },
        ],
        personSex: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        personCom: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        workDuty: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        acceptContent: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        personSeniority: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
      },
      QYshowEditImage: false,
      QYeditImage: null,
      QYsystemImage: '',
      QYeditedImage: null, // 编辑后的图片

      batchId: '',
      //table下动态绑定的值
      tableInput: {
        disk: {
          houseName: null,
          start: '0'
        },
      },
      acceptPersonSex: [],//验收人员性别列表
    }
  },
  computed: {
    title () {
      if (this.action.type === 'show') {
        return this.titleName
      } else {
        return this.action.type === 'update' ? '编辑' + this.titleName : '新建' + this.titleName
      }

    },
    disabledT () {
      // console.log("this.action.type === 'show'",this.action.type === 'show');
      return this.action.type === 'show' ? true : false
    },

  },
  watch: {
    'action.id': {
      handler (newValue, oldValue) {
        // console.log("newValue==", newValue); console.log("oldValue==", oldValue);
        if (newValue != undefined && newValue != '' && newValue != null && this.action.type == 'show') {
          this.getDetail(this.action.id);
        }
      },
      immediate: true
    }
  },
  created () {
    this.debouncedSaveField = debounce(300, this.save)

    if (this.action.type == "save") {
      this.batchId = guid();
    } else {
      this.getDetail(this.action.id)
    }
    this.getDicts('sys_user_sex').then(response => {
      this.acceptPersonSex = response.data
    })
  },

  mounted () {
  },

  beforeDestroy () {
  },

  methods: {

    /** 附件上传  人员照片*/
    fileUploadQY (content) {
      const reader = new FileReader()
      var self = this
      reader.onload = function (e) {
        let result
        if (typeof e.target.result === 'object') {
          // 把Array Buffer转化为blob 如果是base64不需要
          result = window.URL.createObjectURL(new Blob([e.target.result]))
        } else {
          result = e.target.result
        }
        self.QYeditImage = result
        self.QYshowEditImage = true
      }
      reader.readAsDataURL(content.file)
    },
    //删除图片 人员照片
    deleteSystemImageQY () {
      this.QYsystemImage = ''
      this.QYeditedImage = null
    },
    // 人员照片
    submiteImageQY (data) {
      this.QYeditedImage = data
      this.QYsystemImage = data.image
    },

    /**
     * 获取详情
     * */
    getDetail (id) {
      queryById(id)
        .then(res => {
          console.log(res)
          this.QYsystemImage = res.data.personPhoto
          // console.log(this.systemImage)
          this.batchId = res.data.batchId

          this.form = res.data;
        })
        .catch(() => {
        })
    },
    /**
     * 保存
     */
    save (isSubmit) {

      this.$refs['form'].validate(valid => {
        if (!this.QYsystemImage && !this.QYeditedImage) {
          this.$message.error('人员照片不能为空')
          return false
        }
        if (valid) {
          this.form.batchId = this.batchId
          console.log(this.form);
          var param = new FormData()
          param.append('ZHUB', JSON.stringify(this.form))
          if (this.QYeditedImage != null) {
            param.append('QYfile', this.QYeditedImage.blob)
          }
          console.log(param);

          this.loading = true;

          saveOrUpdate(param).then(response => {
            if (response.code === 0) {
              this.$message.success(
                this.action.type == 'update' ? '编辑成功' : '添加成功'
              )
              this.close()
              // 保存成功
              this.$emit('save-success', {
                type: 'receivables'
              })
            } else {
              this.$message.error(
                response.msg
              )
            }
            this.loading = false;
          }).catch(err => {
            this.loading = false;
          })

        }
      })
    },
    /**
     * 关闭
     */
    close () {
      // console.log("close")
      this.$emit('close')
    },

    //列格式化  如一些字典等
    fieldFormatter (row, column, cellValue) {
      //console.log(row, column)
      return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
    },
  }
}
</script>

<style lang="scss" scoped>
.form /deep/ .el-form-item .el-form-item__content {
  width: calc(100% - 10px);
}

.form /deep/ .el-form-item__label {
  padding: 0px;
}
.form /deep/ .el-date-editor {
  width: 100%;
}
/*#crm-table{*/
/*  height: 480px!important;*/
/*}*/

.uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
.upload /deep/ .el-upload-dragger {
  width: 100px;
  height: 100px;
}
.upload-show {
  position: relative;
  display: block;
  width: 100px;
  height: 100px;
  img {
    width: 100%;
    height: 100%;
  }

  .icon-delete {
    position: absolute;
    top: -10px;
    right: -8px;
    color: red;
    font-size: 20px;
    display: none;
  }
}
.upload-show:hover {
  .icon-delete {
    display: block;
  }
}
</style>
