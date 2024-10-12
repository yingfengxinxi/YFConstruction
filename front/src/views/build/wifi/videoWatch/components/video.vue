<template>
<div>
    <el-dialog
        :visible="true"
        :modal-append-to-body="true"
        :append-to-body="true"
        :close-on-click-modal="false"
        class="create-dialog"
        @close="close"
        >

        <video
      
            class="avatar"
            controls="controls"
            >
            <source :src="'api'+fileUrl">
            您的浏览器不支持视频播放
        </video>

        <span
        slot="footer"
        class="dialog-footer">
        <el-button @click.native="close">取 消</el-button>
        </span>
    </el-dialog>
</div>
   

</template>

<script>
  
    export default {
      name: "showvideo",
      components: {
 
      },
      props: {
        fileUrl:'',
      },
      data() {
        return {
          loading: false,
          
        }
      },
      created() {
          console.log(this.fileUrl)
      },
      methods: {

        /**
         * 关闭
         */
        close() {
          this.$emit('close')
        }
      }
    }
</script>

<style scoped>
  .avatar {
  width: 600px;
  height: 600px;
  display: block;
  margin: 0 auto;
}

</style>
