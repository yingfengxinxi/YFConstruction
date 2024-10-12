<template>
  <xr-create :loading="loading" :title="title" :confirm-button-text="'保存'" :showSave="this.action.type === 'showDetials'?false:true" @close="close" @save="debouncedSaveField(true)">

    <el-form ref="form" :disabled="disabledForm" :model="form" :rules="rules" label-position="top" label-width="100px" class="form">
      <create-sections title="基本信息">
        <el-row>
          <el-col :span="8">
            <el-form-item label="分包单位" class="add-form-input subcontractorClass" prop="subcontractor">
              <el-select v-model="form.subcontractor" @change="selectInfoByMapNoPageChange" placeholder="请选择" clearable size="small">
                <el-option v-for="dict in FBSList" :key="dict.id" :label="dict.realName" :value="dict.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="隶属人员" class="add-form-input subcontractorClass" prop="infocolId">
              <el-select v-model="form.infocolId" @change="peopleChange" placeholder="请选择" clearable size="small">
                <el-option v-for="dict in peopleList" :key="dict.name+dict.id" :label="dict.name" :value="dict.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" disabled placeholder="请输入身份证号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="工龄" prop="workAge">
              <el-input v-model="form.workAge" placeholder="请输入工龄"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="特工证网络核验日期" prop="cerNetspeDate">
              <el-date-picker format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="form.cerNetspeDate" type="date" placeholder="选择日期">
              </el-date-picker>
            </el-form-item>

          </el-col>

          <el-col :span="8">
            <el-form-item label="备注" prop="note">
              <el-input type="textarea" v-model="form.note" placeholder="请输入备注"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

      </create-sections>

      <create-sections title="特工证信息">

        <el-row>
          <el-col :span="8">
            <el-form-item label="证书名称" prop="cerName" class="add-form-input">
              <el-input v-model="form.cerName" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="证书编号" prop="cerNumber" class="add-form-input">
              <el-input v-model="form.cerNumber" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="证书种类" class="add-form-input" prop="cerCategory">
              <el-select v-model="form.cerCategory" @change="cerOptionsChange" placeholder="请选择" clearable size="small">
                <el-option v-for="dict in cerCategoryOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="证书类型" class="add-form-input" prop="cerCategoryType">
              <el-select v-model="form.cerCategoryType" placeholder="请选择" @change="cerCategoryTypeChange" clearable size="small">
                <el-option v-for="dict in cerCategoryTypeSelectOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="证书等级" class="add-form-input" prop="cerLevel">
              <el-select v-model="form.cerLevel" placeholder="请选择" clearable size="small">
                <el-option v-for="dict in cerLevelOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="认证时间" class="add-form-input" prop="authTime">
              <el-date-picker format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="form.authTime" type="date" placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="认证状态" class="add-form-input" prop="authStatus">
              <el-select v-model="form.authStatus" placeholder="请选择" clearable size="small">
                <el-option v-for="dict in authStatusOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="岗位类型" class="add-form-input" prop="postType">
              <el-select v-model="form.postType" placeholder="请选择" clearable size="small">
                <el-option v-for="dict in postTypeOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="岗位名称" class="add-form-input" prop="positionTitle">
              <el-select v-model="form.positionTitle" placeholder="请选择" clearable size="small">
                <el-option v-for="dict in postNameOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <!--
           <el-col :span="8">
            <el-form-item label="岗位名称" prop="positionTitle" class="add-form-input">
              <el-input v-model="form.positionTitle" placeholder="请输入" />
            </el-form-item>
          </el-col> -->
          <el-col :span="8">
            <el-form-item label="第一次发证日期" prop="cerStartDate">
              <el-date-picker format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="form.cerStartDate" type="date" placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="有效期启" prop="cerUsestartDate">
              <el-date-picker format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="form.cerUsestartDate" @change="yxqq" type="date" placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="有效期止" prop="cerUseendDate">
              <el-date-picker format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="form.cerUseendDate" @change="yxqq" type="date" placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="发证机关" prop="cerIssuingAuthority" class="add-form-input">
              <el-input v-model="form.cerIssuingAuthority" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="复核日期" prop="cerReviewDate">
              <el-date-picker format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="form.cerReviewDate" type="date" placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="8">
            <el-form-item label="证书验证" class="add-form-input">
              <el-button type="primary">点击证书验证</el-button>
            </el-form-item>
          </el-col> -->
        </el-row>
        <wk-upload-img
          name="file"
          ref="wkUploadImg"
          multiple
          accept="image/*"
          list-type="picture-card"
          :autoUpload="false"
          :title="'图片上传'"
          :batchId="batchId"
        ></wk-upload-img>
      </create-sections>
    </el-form>

  </xr-create>
</template>

<script>

import XrCreate from '@/components/XrCreate'
import CreateSections from '@/components/CreateSections'
import WkUploadImg from '@/components/CreateCom/WkUploadImg'
import WkUploadFile from '@/components/CreateCom/WkUploadFile'
import {guid} from '@/utils'
import { debounce } from 'throttle-debounce'

import { saveOrUpdate, getById, selectFBSInfo, selectInfoByMapNoPage } from "./speworktype";


export default {
  // 新建编辑
  name: 'monitersetCreate',
  components: {
    XrCreate,
    CreateSections,
    WkUploadFile,
    WkUploadImg,
  },

  props: {
    action: {
      type: Object,
      default: () => {
        return {
          type: 'save',
          id: '',
          data: {}
        }
      }
    }
  },
  data () {
    return {
      loading: false,
      form: {
      },
      //证书种类
      cerCategoryOptions: [],
      //证书类型 下拉框使用
      cerCategoryTypeSelectOptions: [],
      //证书类型
      cerCategoryTypeOptions: [],
      //证书等级
      cerLevelOptions: [],
      //分包商list
      FBSList: [],
      //人员信息
      peopleList: [],
      //认证信息
      authStatusOptions: [{ 'dictLabel': '未认证', 'dictValue': '0' }, { 'dictLabel': '已认证', 'dictValue': '1' }],
      //岗位类型
      postTypeOptions: [{ 'dictLabel': '管理人员', 'dictValue': '10' }, { 'dictLabel': '建筑工人', 'dictValue': '20' }],
      //岗位名称
      postNameOptions: [],

      disabledForm: false,

      rules: {
        subcontractor: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        infocolId: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        idCard: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        workAge: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        cerNetspeDate: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],

        cerName: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        cerNumber: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        cerCategory: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        cerCategoryType: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        cerLevel: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        authTime: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        authStatus: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        postType: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        positionTitle: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        cerStartDate: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        cerUsestartDate: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        cerUseendDate: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        cerIssuingAuthority: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        cerReviewDate: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
      },
      batchId: '',
    }
  },
  computed: {
    title () {
      if (this.action.type === 'showDetials') {
        return '分包单位特殊工种证件信息'
      } else {
        return this.action.type === 'update' ? '编辑分包单位特殊工种证件信息' : '添加分包单位特殊工种证件信息'
      }

    },

  },
  watch: {},
  created () {
    this.batchId = guid()
    this.form.batchId = this.batchId
    //  证书种类 字典项
    this.getDicts('b_cerCategory').then(response => {
      this.cerCategoryOptions = response.data
    })
    this.getDicts('b_cerCategoryType').then(response => {
      this.cerCategoryTypeOptions = response.data
    })
    //  证书等级 字典项
    this.getDicts('b_cerLevel').then(response => {
      this.cerLevelOptions = response.data
    })
    //特种人员岗位名称  字典项
    this.getDicts('b_special _post_name').then(response => {
      this.postNameOptions = response.data
    })
    this.selectFBSInfo();//分包商单位

    this.debouncedSaveField = debounce(300, this.save)



    this.getDicts('b_cerCategoryType').then(response => {
      this.cerCategoryTypeOptions = response.data

      if (this.action.type == "update") {
        this.getDetail();
      }
      if (this.action.type == "showDetials") {
        this.getDetail();
        this.disabledForm = true
      }

    })


  },

  mounted () {
  },

  beforeDestroy () {
  },

  methods: {
    /**
     * 获取详情
     * */
    getDetail () {
      getById(this.action.id)
        .then(res => {

          // this.form = res.data;
          this.cerOptions(res.data.cerCategory)//证书种类
          this.selectInfoByMapNoPage(res.data.subcontractor);//根据分包商查询人员列表信息

          this.form = res.data;
          if(!this.form.batchId){
            this.batchId = guid()
            this.form.batchId = this.batchId
          }else {

            this.batchId = res.data.batchId
          }
          this.loading = false

        })
        .catch(() => {
        })
    },
    /**
     * 保存
     */
    save (isSubmit) {
      var fileFlag = this.$refs['wkUploadImg'].submitAsync();
      if (!fileFlag) {
        this.$message({
          type: 'error',
          message: '隐患图片上传异常'
        })
        return;
      }
      this.$refs['form'].validate(valid => {
        if (valid) {
          var params = this.form;
          this.loading = true;
          saveOrUpdate(params).then(response => {
            if (response.code === 0) {
              this.$message.success(
                this.action.type == 'update' ? '编辑成功' : '添加成功'
              )
              this.close()
              // 保存成功
              this.$emit('save-success', {
                type: 'receivables'
              })
            } else {
              this.$message.error(
                response.msg
              )
            }
            this.loading = false;
          }).catch(err => {
            this.loading = false;
          })

        }
      })
    },
    /**
     * 关闭
     */
    close () {
      this.$emit('close')
    },

    //根据证书种类显示证书类型
    cerOptions (value) {

      var actions = []
      var datas = this.cerCategoryTypeOptions

      Object.keys(datas).some((key) => {
        var a = datas[key].dictValue.split('-')[0]
        if (a == ('' + value)) {
          actions.push(datas[key])
        }
      })

      this.cerCategoryTypeSelectOptions = actions

      this.loading = false
    },
    //当证书种类的下拉框改变时
    cerOptionsChange (value) {
      this.$forceUpdate()//强制刷新
      this.form.cerCategoryType = ''
      this.cerOptions(value)
    },
    //证书类型刷新
    cerCategoryTypeChange () {
      this.$forceUpdate()//强制刷新
    },

    /** 查询所有分包商id和name */
    selectFBSInfo (data) {
      selectFBSInfo().then(response => {
        this.FBSList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    //根据分包商显示人员信息
    selectInfoByMapNoPage (data) {
      let pam = {}
      pam.subcontractor = data
      pam.workTypeIfspecial='Y'
      selectInfoByMapNoPage(pam).then(response => {
        this.peopleList = response.data
        if (this.action.type == "update" || this.action.type == "showDetials") {
          this.peopleChange(this.form.infocolId)//人员身份证
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })

    },
    selectInfoByMapNoPageChange (data) {
      this.$forceUpdate()//强制刷新
      this.form.infocolId = ''
      this.form.idCard = ''
      this.selectInfoByMapNoPage(data)

    },
    //根据选择的人员显示身份证
    peopleChange (date) {
      this.$forceUpdate()//强制刷新
      let datas = this.peopleList

      Object.keys(datas).some((key) => {
        var a = datas[key].id
        if (a == ('' + date)) {
          this.form.idCard = datas[key].idCard
          return true
        }
      })


    },
    yxqq(e){
      var kssj = this.form.cerUsestartDate
      var jssj = this.form.cerUseendDate
      if(kssj==""||kssj==null||kssj==undefined||jssj==""||jssj==null||jssj==undefined){
        return
      }else{
        var formatDate1 = new Date(kssj)
        var formatDate2 = new Date(jssj)
        if(formatDate1 > formatDate2){
          this.form.cerUseendDate = ""
          this.$message({
            message: '有效期止不能小于有效期启',
            type: 'warning'
          })
        }
      }
    }


  }
}
</script>

<style lang="scss" scoped>
.form /deep/ .el-form-item .el-form-item__content {
  width: calc(100% - 10px);
}

.form /deep/ .el-form-item__label {
  padding: 0px;
}
.form /deep/ .el-date-editor {
  width: 100%;
}
/deep/ .el-select {
  width: 100%;
}
</style>
