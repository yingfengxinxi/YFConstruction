<template>
  <!-- 图片附件 -->
  <div class="img-accessory">
    <div class="img-box">
      <el-upload
        ref="imageUpload"
        :action="crmFileSaveUrl"
        :headers="httpHeader"
        :data="{type: 'img', batchId: batchId}"
        :on-preview="handleImgPreview"
        :on-success="imgUploadSuccess"
        :before-remove="beforeRemove"
        :on-change="changeFile"
        :on-exceed="handleExceed"
        :file-list="imgFileList"
        :auto-upload="autoUpload"
        :limit="limit"
        v-bind="$attrs">
        <!-- <p class="add-img">
          <span class="el-icon-picture"/>
          <span>{{title}}</span>
        </p> -->
        <i class="el-icon-plus"/>
      </el-upload>
    </div>
  </div>
</template>
<script type="text/javascript">
  import {objDeepCopy, getImageData} from '@/utils'
  import {crmFileSaveAPI, crmFileDeleteAPI, crmFileSaveUrl} from '@/api/common'
  import {adminFileQueryFileListAPI} from '@/api/admin/file'
  import axios from 'axios'

  export default {
    name: 'WkUploadImg', // 图片
    components: {},
    props: {
      // title: {
      //   type: String,
      //   require: false,
      //   default: '图片上传'
      // },
      batchId: {
        type: String,
        require: true
      },
      limit: {
        type: Number,
        require: false,
        default: 0
      },
      autoUpload: {
        type: Boolean,
        require: false,
        default: true
      }
    },
    data() {
      return {
        imgFileList: [],
        empUploadList: [],//临时文件列表
        empRemoveList: [],
      }
    },
    computed: {
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
    watch: {
      batchId(){
        this.getUpdateOtherInfo();
      }
    },
    mounted() {
      //
      this.getUpdateOtherInfo();
    },
    methods: {
      //异步提交
      submitAsync() {
        var flag = true;
        if (this.autoUpload) {
          return flag;
        }
        try {
          this.empRemoveList.forEach(item => {
            this.imgDelete(item);
          });
          this.empUploadList.forEach(item => {
            this.imgUpload(item);
          });
          //考虑组件缓存问题，提交后清空
          this.empRemoveList = [];
          this.empUploadList = [];
        } catch (e) {
          console.log(e);
          flag = false;
        }
        return flag;
      },
      // 更新图片附件相关信息信息
      getUpdateOtherInfo() {
        if(this.batchId){
          adminFileQueryFileListAPI(this.batchId).then((res) => {
            this.imgFileList = objDeepCopy(res.data || [])

            for (let index = 0; index < this.imgFileList.length; index++) {
              this.setImageList(this.imgFileList[index], index)
            }
          }).catch(() => {
          })
        }else{
          console.log("附件ID为空")
        }
      },
      changeFile(file, fileList) {//存放临时文件
        //console.log('change', file)
        this.empUploadList.push(file.raw);
      },
      /**
       * 获取图片内容
       */
      setImageList(item, index) {
        getImageData(item.url).then((data) => {
          item.url = data.src
          this.imgFileList.splice(index, 1, item)
        }).catch(() => {
        })
      },
      // 上传图片
      imgUploadSuccess(response, file, fileList) {
        this.imgFileList = fileList
      },
      // 查看图片
      handleImgPreview(file) {
        if (file.fileId) {
          file.url = "/adminFile/down/" + file.fileId;
        }
        this.$bus.emit('preview-image-bus', {
          index: 0,
          data: [file]
        })
      },
      beforeRemove(file, fileList) {
        if (file.status == 'success' && file.fileId) {//是否是已上传的文件
          let fileId = file.fileId
          this.$confirm('您确定要删除该文件吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
            .then(() => {
              var removeIndex = this.getFileIndex(
                this.$refs.imageUpload.uploadFiles,
                fileId
              )
              if (removeIndex != -1) {
                this.$refs.imageUpload.uploadFiles.splice(removeIndex, 1)
              }
              removeIndex = this.getFileIndex(this.imgFileList, fileId)
              if (removeIndex != -1) {
                this.imgFileList.splice(removeIndex, 1)
              }
              if (this.autoUpload) {
                this.imgDelete(fileId);
              } else {
                this.empRemoveList.push(fileId);
              }
            })
            .catch(() => {
              this.$message({
                type: 'info',
                message: '已取消操作'
              })
            })
          return false
        } else {
          //移除临时文件
          var removeIndex = this.getEmpFileIndex(this.empUploadList, file.uid);
          this.empUploadList.splice(removeIndex, 1)
          return true
        }
      },
      imgUpload(file) {
        var data = {type: 'img', batchId: this.batchId, file: file};
        crmFileSaveAPI(data);
      },
      //图片删除
      imgDelete(fileId) {
        crmFileDeleteAPI({
          id: fileId
        });
      },
      // 临时附件索引
      getEmpFileIndex(files, uid) {
        var removeIndex = -1
        for (let index = 0; index < files.length; index++) {
          const item = files[index]
          if (item.uid == uid) {
            removeIndex = index
            break
          }
        }
        return removeIndex
      },
      // 已上传附件索引
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
      //超出限制
      handleExceed(files, fileList) {
        if (this.limit > 0) {
          this.$message.warning(`限制选择 ` + this.limit + ` 个文件，` +
            `本次选择了 ${files.length} 个文件，已选择了 ${fileList.length} 个文件`)
        }
      },
    }
  }
</script>
<style lang="scss" scoped>
  // 图片 附件
  .img-accessory {
    padding: 0 20px;
    font-size: 12px;
    img {
      width: 16px;
      vertical-align: middle;
    }
    .img-box /deep/ .el-upload {
      width: 200px;
      height: 200px;
      line-height: 200px;
    }
    .img-box /deep/ .el-upload-list {
      .el-upload-list__item {
        width: 200px;
        height: 200px;
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
        line-height: 200px;
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
