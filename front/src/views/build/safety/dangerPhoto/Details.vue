<template>
  <xr-create
    :loading="loading"
    :title="title"
    @close="close"
    :showSave="false">
    <create-sections title="隐患信息">
      <el-form :model="fieldForm" label-position="top" label-width="100px"
               class="form">
        <el-row>
          <el-col :span="8">
            <el-form-item label="巡检部位" >
              <el-input disabled v-model="fieldForm.place" placeholder="请输入巡检部位"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="问题描述" >
              <el-input disabled v-model="fieldForm.problemDescription" placeholder="请输入问题描述"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="巡检结论" >
              <el-input disabled v-model="fieldForm.inspectionConclusion" placeholder="请输入巡检结论"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="隐患等级" >
              <el-select
                v-model="fieldForm.level"
                placeholder="请选择"
                clearable
                disabled
              >
                <el-option
                  v-for="dict in hazardLevleDict"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="危险源" >
              <el-select disabled v-model="fieldForm.dangerSourceId" filterable
                         placeholder="请选择">
                <el-option :value="fieldForm.dangerSourceId" :label="fieldForm.dangerName"
                           style="width: 260px;height:120px;overflow:auto;background-color: #fff">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="实际整改人">
              <el-input disabled v-model="fieldForm.rectificationPeople" placeholder="请输入实际整改人"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="整改时间" >
              <el-date-picker
                v-model="fieldForm.rectifyDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择检查时间"
                disabled
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="整改效果说明" >
              <el-input disabled v-model="fieldForm.rectifyEffect" placeholder="请输入整改效果说明"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row >
          <el-col :span="8">
            <el-form-item label="复检时间" >
              <el-date-picker
                v-model="fieldForm.recheckDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择检查时间"
                disabled
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="复检人" >
              <el-select disabled v-model="fieldForm.recheckPeopleId" placeholder="选择复检人">
                <el-option
                  v-for="item in dictData.userListData"
                  :key="item.userId"
                  :label="item.realname"
                  :value="item.userId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="复检结论" >
              <el-input disabled v-model="fieldForm.recheckConclusion" placeholder="请输入复检结论"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!--图片上传-->
      <wk-upload-img
        name="file"
        multiple
        accept="image/*"
        list-type="picture-card"
        :autoUpload="false"
        :title="'图片上传'"
        :batchId="batchId"
      ></wk-upload-img>
    </create-sections>
  </xr-create>
</template>

<script>
  import CreateSections from '@/components/CreateSections'
  import WkForm from '@/components/NewCom/WkForm'
  import {guid} from '@/utils'
  import {CrmRelativeCell} from '@/components/CreateCom'
  import WkUploadImg from '@/components/CreateCom/WkUploadImg'
  import { selectById } from './api'
  import {depListAPI, userListAPI} from '@/api/common' // 直属上级接口  用户接口
  import XrCreate from '@/components/XrCreate'
  export default {
    name: "Update",
    components: {
      XrCreate,
      CrmRelativeCell,
      CreateSections,
      WkForm,
      WkUploadImg
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
        title: "隐患信息",
        fieldForm: {},
        batchId:'',
        tableData:[],
        // 隐患等级字典项
        hazardLevleDict: [],
        // 问题类型相关
        dangerOptions: [],
        defaultProps: {
          children: 'children',
          label: 'dangerName'
        },
        // 字典数据
        dictData: {
          // 计划等级
          planLevelDict: [],
          // 执行部门
          showDeptTreeData: [],
          // 用户列表
          userListData: []
        },
      }
    },
    created() {
      this.getDicts('b_hazard_level').then(response => {
        this.hazardLevleDict = response.data
      })
      //考虑附件ID
      console.log(this.id);
      this.get();
      // 获取字典数据
      this.initDicts()
      // 获取执行部门数据
      this.getDeptTreeList()
      // 查询用户数据
      this.getUserList()
    },
    methods: {
      get(){
        console.log("11:",this.id)
        selectById(this.id).then(response => {

          if (response.code === 0) {
            this.fieldForm = response.data;
            this.batchId = response.data.batchId
          }
        }).catch(() => {

        })
      },
      // 初始化字典数据
      initDicts() {
        this.getDicts('b_safety_plan_level').then(response => {
          this.dictData.planLevelDict = response.data
        })
      },

      // 执行部门 获取树形列表
      getDeptTreeList() {
        depListAPI({type: 'tree'})
          .then(response => {
            this.dictData.showDeptTreeData = response.data || []
          })
          .catch(() => {
          })
      },

      // 查询当前项目的用户
      getUserList() {
        var params = {
          pageType: 0
        }
        userListAPI(params).then(res => {
          this.dictData.userListData = res.data.list
        })
          .catch(() => {

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
  /deep/.el-input{
    width: 220px;
  }
  /deep/.el-select{
    width: 220px;
  }
  /deep/.el-textarea{
    width: 220px;
  }
</style>
