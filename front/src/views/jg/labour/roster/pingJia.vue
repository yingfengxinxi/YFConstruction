<template>

    <el-dialog
    :title="title"
    :visible="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    :close-on-click-modal="false"
    class="create-dialog"
    width="500px"
    @close="close"
    >

         <el-form ref="form" :model="form" label-width="100px" >
            <el-row>
                <el-col :span="24">
                <!-- <el-form-item label="项目评价" prop="evaluation"> -->
                    <el-input v-model="form.evaluation"
                                placeholder="请输入评价" 
                                :row="10"
                                type="textarea"></el-input>
                <!-- </el-form-item> -->
                </el-col>
            </el-row>
      
    </el-form>

  <span
      slot="footer"
      class="dialog-footer">
      <el-button @click.native="close">取 消</el-button>
      <el-button type="primary"  @click.native="save">确 定</el-button>
     
    </span>
  </el-dialog>

</template>

<script>
    import request from '@/utils/request'

    export default {
      name: "Create",
      components: {
 
      },
      props: {
        id:""
      },
      data() {
        return {
          loading: false,
          title: "项目评价",

          form:{},
        }
      },
      created() {
    
        this.getData()
      },
      methods: {

        getData(){
            request({
                url: 'build/labourInfocollection/queryEvaluationById/'+this.id,
                method: 'post',
                data: '',
                async: false,
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            }).then(response => {
                if (response.code === 0) {
                    this.form = response.data
                }
            })	
        },

        save(){
            request({
                url: 'build/labourInfocollection/update',
                method: 'post',
                data: this.form,
                async: false,
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            }).then(response => {
                this.msgSuccess('评价成功')
                this.loading = false;
                this.close()
            })
        },
        
        /**
         * 关闭
         */
        close() {
          this.$emit('close')
        }
      }

    }
</script>

<style lang="scss" scoped>
    
  /deep/ 
  .el-textarea{
      height: 200px;
  }
  /deep/
  .el-textarea__inner{
      height: 100%;
  }

  /deep/ .el-dialog{
      height: 360px !important;
      position: absolute;
      margin: auto;
      margin-top: auto!important;
  }
  /deep/ .el-dialog .el-dialog__body{
      height: 300px !important;
      overflow: auto;
  }

</style>
