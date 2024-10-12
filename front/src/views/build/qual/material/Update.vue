<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField">
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
          <el-form-item label="编制序号" prop="preparedNumber">
            <el-input v-model.number="form.preparedNumber" placeholder="请输入编制人员"></el-input>
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
    <el-form ref="modifyForm" :model="modifyForm" :rules="modifyRules" label-width="100px" >
      <el-row>
        <el-col :span="12">
          <el-form-item label="版本号" prop="versionCode">
            <el-input v-model="modifyForm.versionCode" placeholder="请输入版本号"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审核人" prop="examineBy">
            <el-input v-model="modifyForm.examineBy" placeholder="请输入审核人"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="变更说明" prop="changelogExplain">
            <el-input  type="textarea"  v-model="modifyForm.changelogExplain" placeholder="请输入变更说明"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </xr-create>
</template>

<script>
  import { getList, addCatalog, getCatalogTree ,deleteCatalog ,add,get,addChangelog} from '@/api/build/qualMaterial'
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
        title: "修改质量资料",
        form: {},
        modifyForm:{},
        rules: {
          materialCode: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          materialName: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          materialType: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          preparedUnit: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          preparedBy: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          preparedTime: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          personCharge: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          batchId: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          preparedNumber: [
            { type: 'number', message: '必须为数字值'},
            { required: true, message: '必填项', trigger: 'change' }
          ],
        },
        modifyRules:{
          versionCode: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          examineBy: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
          changelogExplain: [
            { required: true, message: '必填项', trigger: 'change' }
          ],
        },
        batchId:''
      }
    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)
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
      },
      /**
       * 保存
       */
      save() {
        this.form.id = this.id;
        this.form.catalogId = this.catalogId;
        var fileFlag = this.$refs.wkUploadFile.submitAsync()
        if(fileFlag){
          this.$refs['form'].validate(valid => {
            if(valid){
              this.$refs['modifyForm'].validate(valid2 => {
                if(valid2){
                  add(this.form).then(response => {
                    if (response.code === 0) {
                      this.modifyForm.parentId = this.form.id;
                      this.modifyForm.batchId = this.form.batchId
                      addChangelog(this.modifyForm).then(response => {
                        if (response.code === 0) {
                          this.msgSuccess('修改成功')
                          this.close();
                          this.form = {};
                          this.modifyForm = {};
                          this.$emit('save-success', {
                            type: 'receivables'
                          })
                        }
                      }).catch(() => {

                      })
                    }
                  }).catch(() => {

                  })
                }
              })
            }
          })
        }
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
