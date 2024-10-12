<template>

    <el-dialog
    :title="title"
    :visible="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    :close-on-click-modal="false"
    class="create-dialog"
    width="85%"
    @close="close"
    >

         <el-table v-loading="loading" border :data="dataList" height="100%" >
            <el-table-column label="序号" type="index" align="center"  width="90"></el-table-column>
            <el-table-column label="项目名称" align="center" prop="projectName" width="180px"/>
            <el-table-column label="建设单位" align="center" prop="constructionUnitName" width="180px"/>
            <el-table-column label="项目地址" align="center" prop="projectAddress" width="220px"/>
            <el-table-column label="实际竣工日期" align="center" prop="actualCompletionDate" width="180px" />
            <el-table-column label="联系人" align="center" prop="contactsName" />
            <el-table-column label="联系电话" align="center" prop="contactsTel" width="140px" />
            <el-table-column label="项目评价" align="center" prop="evaluation"  width="220px" />

         </el-table>

  <span
      slot="footer"
      class="dialog-footer">
      <el-button @click.native="close">取 消</el-button>
     
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
        idCard:"",
        projectId:""//当前项目projectId
      },
      data() {
        return {
          loading: false,
          title: "项目履历",

          dataList:[],
        }
      },
      created() {
       
        this.getDataList()
      },
      methods: {

        //项目履历不展示当前项目信息
        getDataList(){
            request({
                url: 'build/labourInfocollection/queryProjectsByIdCard/'+this.idCard +'/'+this.projectId,
                method: 'post',
                data: '',
                async: false,
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            }).then(response => {
                if (response.code === 0) {
                    this.dataList = response.data
                }
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

<style scoped>
  .el-select{
    width: 100%;
  }
</style>
