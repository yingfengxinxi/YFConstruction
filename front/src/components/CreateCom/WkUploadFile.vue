<template>
  <!-- 文件附件 -->
  <div>
    <el-upload
      ref="fileUpload"
      :action="crmFileSaveUrl"
      :headers="httpHeader"
      :data="{type: 'file', batchId: batchId}"
      :on-preview="handleImgPreview"
      :on-success="imgUploadSuccess"
      :before-remove="beforeRemove"
      :on-change="changeFile"
      :on-exceed="handleExceed"
      :file-list="fileFileList"
      :auto-upload="autoUpload"
      :limit="limit"
	  :disabled="disabled"
      v-bind="$attrs">
      <el-button v-if="disabled" size="small" type="primary">禁止上传</el-button>
      <el-button v-else size="small" type="primary">点击上传</el-button>
      <div class="el-upload__tip" slot="tip">单个文件大小不超过{{size}}M</div>
    </el-upload>
  </div>
</template>
<script type="text/javascript">
  import {objDeepCopy, getImageData} from '@/utils'
  import {crmFileSaveAPI, crmFileDeleteAPI, crmFileSaveUrl} from '@/api/common'
  import {adminFileQueryFileListAPI} from '@/api/admin/file'
  import axios from 'axios'

  export default {
    name: 'WkUploadFile', // 文件
    components: {},
    props: {
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
      },
      size: {
        type: Number,
        default: 10
      },
      disabled: {
        type: Boolean,
        default: false
      },
      refInfex: { // 循环组件时父组件传递下标
        type: Number,
        default: 0
      },
	  callbackFile: { // 是否返回文件列表到父组件
        type: Boolean,
        default: false
      },
    },
    data() {
      return {
        fileFileList: [],
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
      batchId(newValue,oldValue){
        if(newValue!=undefined&&newValue!=oldValue){
          this.fileFileList=[];
        }
       
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
		let uploadFlag = true;
        if (this.autoUpload) {
          return flag;
        }
        try {
          this.empRemoveList.forEach(item => {
            this.fileDelete(item);
          });
          //考虑组件缓存问题，提交后清空
          this.empRemoveList = [];
		  
		  // 文件大小限制
		  for (let i = 0; i<this.empUploadList.length; i++) {
			uploadFlag = this.beforeUpload();
			// 文件大小超出限制返回 false，结束方法
			if (!uploadFlag) {
				return;
			}
		  }
		  
          this.empUploadList.forEach(item => {
            this.fileUpload(item);
            
          });
          //考虑组件缓存问题，提交后清空
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
            this.fileFileList = objDeepCopy(res.data || [])

            for (let index = 0; index < this.fileFileList.length; index++) {
              this.setImageList(this.fileFileList[index], index)
            }
			
			// 向父组件传递文件列表
			this.callbackFileList()
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
          this.fileFileList.splice(index, 1, item)
        }).catch(() => {
        })
      },
      // 上传图片
      imgUploadSuccess(response, file, fileList) {
        this.fileFileList = fileList
		// 向父组件传递文件列表
		this.callbackFileList()
      },
      // 查看图片
      handleImgPreview(file) {
        if (file.fileId) {
          file.url = "/adminFile/down/" + file.fileId;
        }else{//临时图片预览
          file.url = (window.URL) ? window.URL.createObjectURL(file.raw) :
            window.webkitURL.createObjectURL(file.raw)
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
                this.$refs.fileUpload.uploadFiles,
                fileId
              )
              if (removeIndex != -1) {
                this.$refs.fileUpload.uploadFiles.splice(removeIndex, 1)
              }
              removeIndex = this.getFileIndex(this.fileFileList, fileId)
              if (removeIndex != -1) {
                this.fileFileList.splice(removeIndex, 1)
				// 向父组件传递文件列表
				this.callbackFileList()
              }
              if (this.autoUpload) {
                this.fileDelete(fileId);
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
      fileUpload(file) {
        var data = {type: 'file', batchId: this.batchId, file: file};
        crmFileSaveAPI(data);
      },
      //图片删除
      fileDelete(fileId) {
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

      //文件大小限制--zy
      beforeUpload(){
        //限制文件大小 10M
        var flag = true;
        this.empUploadList.forEach(item => {

            const isLt2M = item.size / 1024 / 1024 < this.size;

            if (!isLt2M) {
              //this.$message.error('单个文件大小不能超过 ' + this.size +'M!');
			  this.$confirm('单个文件大小不能超过 ' + this.size +'M!', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			  })
              flag = false;
              return ;
            }
            
          });

         return flag;
            
      },
	  callbackFileList(){
		  if (this.callbackFile) {
			  this.$emit("callbackFileList",this.refInfex,this.fileFileList)
		  }
	  }

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
