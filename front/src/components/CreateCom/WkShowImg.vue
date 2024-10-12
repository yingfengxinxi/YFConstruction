<template>
  <!-- 图片附件 -->
  <div class="img-accessory">
    <div class="img-box">
      <el-image v-if="this.imgFileList.length>0"
        :headers="httpHeader"
        :src="this.imgFileList[0].url"
        :data="{type: 'img', batchId: batchId}"
        v-bind="$attrs">
      </el-image>
      <el-image v-else>
      <div slot="error" class="image-slot">
        <i class="el-icon-picture-outline"></i>
      </div>
    </el-image>
    </div>
  </div>
</template>
<script type="text/javascript">
  import {objDeepCopy, getImageData} from '@/utils'
  import {adminFileQueryFileListAPI} from '@/api/admin/file'
  import axios from 'axios'

  export default {
    name: 'WkUploadImg', // 图片
    components: {},
    props: {
      batchId: {
        type: String,
        require: true
      },
    },
    data() {
      return {
        imgFileList: [],
      }
    },
    computed: {
      //附件
      httpHeader() {
        return {
          'Admin-Token': axios.defaults.headers['Admin-Token']
        }
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

      // 查看图片
      handleImgPreview(file) {
        if (file.fileId) {
          file.url = "/adminFile/down/" + file.fileId;
        }
        this.$bus.emit('preview-image-bus', {
          index: 0,
          data: [file]
        })
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
