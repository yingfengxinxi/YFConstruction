<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField">
  <el-form ref="form" :model="form" :rules="rules" label-width="100px" >
    <el-form-item label="上级名称" prop="materialCode">
      <el-input v-model="form.materialCode" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="楼层" prop="materialName">
      <el-input v-model="form.materialName" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="房间代码" prop="materialType">
      <el-input v-model="form.materialType" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="建筑面积" prop="preparedUnit">
      <el-input v-model="form.preparedUnit" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="房间状态" prop="preparedBy">
      <el-input v-model="form.preparedBy" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="初次验收日期" prop="preparedTime">
      <el-date-picker
      v-model="form.preparedTime"
      type="date"
      placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
    </el-form-item>
    <el-form-item label="家庭成员" prop="preparedNumber">
      <el-input type="textarea" v-model.number="form.preparedNumber" placeholder=""></el-input>
    </el-form-item>
  </el-form>
  </xr-create>
</template>

<script>
  import { getList, addCatalog, getCatalogTree ,deleteCatalog ,add} from '@/api/build/safetyMaterial'
  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import {debounce} from 'throttle-debounce'
  import WkUploadFile from '@/components/CreateCom/WkUploadFile'
  import { guid } from '@/utils'
    export default {
      name: "Create",
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
          title: "添加房间",
          form: {},
          batchId:'',
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
          },
        }
      },
      created() {
        this.form.batchId = guid()
        this.debouncedSaveField = debounce(300, this.save)
        //考虑附件ID
        console.log(this.catalogId);
      },
      methods: {
        /**
         * 保存
         */
        save() {
          this.form.catalogId = this.catalogId;
          var fileFlag = this.$refs.wkUploadFile.submitAsync()

          if(fileFlag){
            this.$refs['form'].validate(valid => {
              if(valid){
                add(this.form).then(response => {
                  if (response.code === 0) {
                    this.msgSuccess('添加成功')
                    this.close();
                    this.form = {};
                    this.$emit('save-success', {
                      type: 'receivables'
                    })
                  }
                }).catch(() => {

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
