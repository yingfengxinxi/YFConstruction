<template>
  <xr-create
    :loading="loading"
    :title="title"
    @close="close"
    :showSave="false">
    <create-sections title="基本信息">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px" >
      <el-row>
        <el-col :span="12">
          <el-form-item label="资料编号" prop="materialCode">
            <el-input v-model="form.materialCode" placeholder="请输入资料编号"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资料名称" prop="materialName">
            <el-input v-model="form.materialName" placeholder="请输入资料名称"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资料类型" prop="materialType">
            <el-input v-model="form.materialType" placeholder="请输入资料类型"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制单位" prop="preparedUnit">
            <el-input v-model="form.preparedUnit" placeholder="请输入编制单位"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制人员" prop="preparedBy">
            <el-input v-model="form.preparedBy" placeholder="请输入编制人员"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制日期" prop="preparedTime">
            <el-date-picker
              v-model="form.preparedTime"
              type="date"
              placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人" prop="personCharge">
            <el-input v-model="form.personCharge" placeholder="请输入负责人"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="质量资料" prop="batchId">
            <wk-upload-file
              ref="wkUploadFile"
              :auto-upload="false"
              :batch-id="form.batchId"
              name="file"
              multiple
              accept="*"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    </create-sections>
    <create-sections title="变更记录">
    <el-table
      :data="tableData"
      style="width: 100%">
      <el-table-column
        prop="versionCode"
        label="版本号"
        width="180">
      </el-table-column>
      <el-table-column
        prop="examineBy"
        label="审核人"
        width="180">
      </el-table-column>
      <el-table-column
        prop="changelogExplain"
        label="变更说明">
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="变更时间">
        <template slot-scope="scope">
          {{scope.row.createTime.substring(0,10)}}
        </template>
      </el-table-column>
    </el-table>
    </create-sections>
  </xr-create>
</template>

<script>
  import { getList, addCatalog, getCatalogTree ,deleteCatalog ,add,get,addChangelog,selectByPid} from '@/api/build/qualMaterial'
  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import {debounce} from 'throttle-debounce'
  import WkUploadFile from '@/components/CreateCom/WkUploadFile'
  import { guid } from '@/utils'
  export default {
    name: "Update",
    components: {
      XrCreate,
      CreateSections,
      WkUploadFile,
    },
    props: {
      catalogId: {
        type: Number,
        require: true
      },
      id: {
        type: Number,
        require: true
      },
    },
    data() {
      return {
        loading: false,
        title: "质量资料",
        form: {},
        modifyForm:{},
        rules: {},
        batchId:'',
        tableData:[],
      }
    },
    created() {
      //考虑附件ID
      console.log(this.id);
      this.get()
    },
    methods: {
      get(){
        get(this.id).then(response => {
          console.log(response)
          if (response.code === 0) {
            this.form = response.data;
          }
        }).catch(() => {

        })
        selectByPid(this.id).then(response => {
          console.log(response)
          if (response.code === 0) {
            this.tableData = response.data;
          }
        }).catch(() => {

        })
      },
      /**
       * 保存
       */
      save() {

      },
      /**
       * 关闭
       */
      close() {
        console.log("close")
        this.$emit('close')
      }
    }
  }
</script>

<style scoped>

</style>
