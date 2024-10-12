<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    :showSave="!isDetail"
    @close="close"
    @save="debouncedSaveField(true)">
    <create-sections title="基本信息">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
               class="form" :disabled="isDetail">
        <el-row>
          <el-col :span="24">
            <el-form-item label="工程名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入工程名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="工程类型" prop="type">
              <el-select v-model="form.type" clearable placeholder="请选择工程类型">
                <el-option label="基坑工程" value="10"></el-option>
                <el-option label="高支模工程" value="20"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工程状态" prop="status">
              <el-select v-model="form.status" clearable placeholder="请选择工程状态">
                <el-option
                  v-for="dict in statusDict"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="相对位置X" prop="planeCoordinateX">
              <el-input v-model="form.planeCoordinateX" type="number" placeholder="请输入相对位置X"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="相对位置Y" prop="planeCoordinateY">
              <el-input v-model="form.planeCoordinateY" type="number" placeholder="请输入相对位置Y"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="工程图片(只允许一张)" prop="imgId">
              <div class="img-accessory">
                <div class="img-box">
                  <el-upload
                    ref="imageUpload"
                    :action="crmFileSaveUrl"
                    :headers="httpHeader"
                    :data="{type: 'img', batchId: imgId}"
                    :on-preview="handleFilePreview"
                    :before-remove="beforeRemove"
                    :on-success="imgUploadSuccess"
                    :file-list="imgList"
                    name="file"
                    :limit="1"
                    accept="image/*"
                    list-type="picture-card">
                    <p class="add-img">
                      <span class="el-icon-picture"/>
                      <span>图片上传</span>
                    </p>
                    <i class="el-icon-plus"/>
                  </el-upload>
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="工程文件">
              <wk-upload-file
                ref="wkUploadFile"
                name="file"
                multiple
                accept="*"
                :limit="3"
                :autoUpload="false"
                :batchId="fileId"
              ></wk-upload-file>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="排序" prop="sort">
              <el-input v-model="form.sort" type="number" placeholder="请输入排序"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </create-sections>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import SelectTree from '@/components/SelectTree'
  import CreateSections from '@/components/CreateSections'

  import WkUploadFile from '@/components/CreateCom/WkUploadFile'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById} from "../api/eqpro"

  //附件
  import {guid, objDeepCopy, getImageData} from '@/utils'
  import {crmFileDeleteAPI, crmFileSaveUrl} from '@/api/common'
  import {adminFileQueryFileListAPI} from '@/api/admin/file'
  import axios from 'axios'

  export default {
    // 新建编辑
    name: 'ProCreate',
    components: {
      XrCreate,
      CreateSections,
      SelectTree,
      WkUploadFile
    },
    props: {
      action: {
        type: Object,
        default: () => {
          return {
            type: 'save',
            id: '',
            data: {}
          }
        }
      },
    },
    data() {
		
	  const validateFile = (rule, value, callback) => {
		if (!this.form.imgId) {  //当'this.resourceForm.imageUrl'不等于‘true’
		  callback(new Error("请上传图片"))
		} else {
		  callback()
		}
	  }
      return {
        loading: false,
        form: {},
        statusDict:[],
        rules: {
          name: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          type: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          status: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          imgId: [
            {required: true, validator: validateFile, trigger: 'change'},
          ],
          sort: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
        },
        //
        isDetail: false,
        //
        imgId: '',
        imgList: [],
        fileId: '',
        fileList: [],
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑' : '新建'
      },
      //附件
      httpHeader() {
        return {
          'Admin-Token': axios.defaults.headers['Admin-Token']
        }
      },
      crmFileSaveUrl() {
        return crmFileSaveUrl
      },
    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
      if (this.action.type == "update" || this.action.type == "detail") {
        if (this.action.type == "detail") {
          this.isDetail = true;
        }
        this.getDetail();
      }
      if (this.action.type == "save") {//新增
        this.imgId = guid();
        this.fileId = guid();
      }
      this.getDicts('b_equipment_pro_status').then(response => {
        this.statusDict = response.data
      })
    },
    watch: {},
    methods: {
      setUUID() {
        var s = [];
        var hexDigits = '0123456789abcdef';
        for (var i = 0; i < 36; i++) {
          s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = '4';
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
        s[8] = s[13] = s[18] = s[23] = '-';
        this.$set(this.form, 'secret', s.join(''))
      },
      /**
       * 获取详情
       * */
      getDetail() {
        getById(this.action.id)
          .then(res => {
            this.form = res.data
            if (this.form.imgId) {
              this.imgId = this.form.imgId;
            } else {
              this.imgId = guid();
            }
            if (this.form.fileId) {
              this.fileId = this.form.fileId;
            } else {
              this.fileId = guid();
            }
            //img
            this.initImgList();
          })
          .catch(() => {
          })
      },
      /**
       * 保存
       */
      save(isSubmit) {
        this.$refs['form'].validate(valid => {
          if (valid) {
            var fileFlag = this.$refs.wkUploadFile.submitAsync();
            if(!fileFlag){
              this.$message({
                type: 'error',
                message: '工程附件提交异常'
              })
              return;
            }
            var params = this.form;
            // 图片上传成功回调函数中绑定 imgId
            //params.imgId = this.imgId;
            params.fileId = this.fileId;

            this.loading = true;
            saveOrUpdate(params).then(response => {
              if (response.code === 0) {
                this.$message.success(
                  this.action.type == 'update' ? '编辑成功' : '添加成功'
                )
                this.close()
                // 保存成功
                this.$emit('save-success')
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
      close() {
		if (this.action.type == 'update') {
			this.$refs['form'].validate(valid => {
			  if (valid) {
			    this.$emit('close')
			  }
			})
		} else {
			this.$emit('close')
		}
      },
      //========= 附件部分 =======
      // 图片
      initImgList() {
        adminFileQueryFileListAPI(this.imgId).then((res) => {
          this.imgList = objDeepCopy(res.data || [])

          for (let index = 0; index < this.imgList.length; index++) {
            this.setImageList(this.imgList[index], index)
          }
        }).catch(() => {
        })


      },
      /**
       * 获取图片内容
       */
      setImageList(item, index) {
        getImageData(item.url).then((data) => {
          item.url = data.src
          this.imgList.splice(index, 1, item)
        }).catch(() => {
        })
      },
      // 上传图片
      imgUploadSuccess(response, file, fileList) {
        console.log(fileList)
        this.imgList = fileList
		this.form.imgId = this.imgId
		// 上传成功后，手动验证一次表单---关键区
		this.$refs.form.validateField('imgId');
      },
      //删除
      beforeRemove(file, fileList) {
        if (file.response || file.fileId) {
          let fileId
          if (file.response) {
            fileId = file.response.data.fileId
          } else {
            fileId = file.fileId
          }
          this.$confirm('您确定要删除该文件吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
            .then(() => {
              crmFileDeleteAPI({
                id: fileId
              })
                .then(res => {
                  this.$message.success('操作成功')
				  this.form.imgId = false //图片值为false
				  this.$refs.form.validateField('imgId')
                  var removeIndex = this.getFileIndex(
                    this.$refs.imageUpload.uploadFiles,
                    fileId
                  )
                  if (removeIndex != -1) {
                    this.$refs.imageUpload.uploadFiles.splice(removeIndex, 1)
                  }
                  removeIndex = this.getFileIndex(this.imgList, fileId)
                  if (removeIndex != -1) {
                    this.imgList.splice(removeIndex, 1)
                  }
                })
                .catch(() => {
                })
            })
            .catch(() => {
              this.$message({
                type: 'info',
                message: '已取消操作'
              })
            })
          return false
        } else {
          return true
        }
      },
      // 查看图片 通用
      handleFilePreview(file) {
        if (file.response || file.fileId) {
          let perviewFile
          if (file.response) {
            perviewFile = file.response.data
          } else {
            perviewFile = file
          }
          this.$bus.emit('preview-image-bus', {
            index: 0,
            data: [perviewFile]
          })
        }
      },

      // 附件索引 == 通用
      getFileIndex(files, fileId) {
        var removeIndex = -1
        for (let index = 0; index < files.length; index++) {
          const item = files[index]
          let itemFileId
          if (item.response) {
            itemFileId = item.response.data.fileId
          } else {
            itemFileId = item.fileId
          }
          if (itemFileId == fileId) {
            removeIndex = index
            break
          }
        }
        return removeIndex
      },

    }
  }
</script>

<style lang="scss" scoped>
  .form
  /deep/
  .el-form-item
  .el-form-item__content {
    width: calc(100% - 10px);
  }

  .form
  /deep/
  .el-form-item__label {
    padding: 0px;
  }

  .form
  /deep/
  .el-date-editor, .el-select {
    width: 100%;
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
</style>
