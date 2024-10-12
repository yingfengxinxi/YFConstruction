<template>
  <xr-create :loading="loading" :title="title" :confirm-button-text="'保存'" :showSave="this.action.type === 'showDetials'?false:true" @close="close" @save="debouncedSaveField(true)">

    <el-form ref="form" :disabled="disabledForm" :model="form" :rules="rules" label-position="top" label-width="100px" class="form">
      <create-sections title="基本信息">
        <el-row>
			<el-col :span="8">
				<el-form-item  label="项目名称" class="add-form-input subcontractorClass" prop="projectId" >
				<el-select
						v-model="form.projectId"
						placeholder="请选择"
						@change="projectChange"
						clearable
						size="small">
					<el-option
					v-for="item in projectOptions"
					:key="item.id"
					:label="item.name"
					:value="item.id" />
				</el-select>
				</el-form-item>
			</el-col>
          <el-col :span="8">
            <el-form-item label="分包单位" class="add-form-input subcontractorClass" prop="subcontractor">
              <el-select v-model="form.subcontractor" placeholder="请选择" clearable size="small">
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
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" disabled placeholder="请输入身份证号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="所属工种" class="add-form-input subcontractorClass" prop="workTypeId">
              <el-select v-model="form.workTypeId" disabled @change="workTypeIdChangeSelect" placeholder="请选择" clearable size="small">
                <el-option v-for="dict in workTypeOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="工龄" prop="workAge">
              <el-input v-model="form.workAge" placeholder="请输入工龄"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

      </create-sections>

      <create-sections title="证件信息">
        <el-row>
          <el-col :span="8">
            <el-form-item label="证书名称" prop="certificateName" class="add-form-input">
              <!-- <el-input v-model="form.certificateName" placeholder="请输入" /> -->
              <el-select v-model="form.certificateName" @change="certificateNameChange" placeholder="请选择" clearable size="small">
                <el-option v-for="dict in certificateNameOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="证书编号" prop="certificateNo" class="add-form-input">
              <el-input v-model="form.certificateNo" placeholder="请输入" />
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
            <el-form-item label="证书类型" class="add-form-input" prop="certificateType">
              <el-select v-model="form.certificateType" placeholder="请选择" @change="cerCategoryTypeChange" clearable size="small">
                <el-option v-for="dict in cerCategoryTypeSelectOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="证书型号" class="add-form-input" prop="certificateModel">
              <el-select v-model="form.certificateModel" placeholder="请选择" @change="cerCategoryTypeChange" clearable size="small">
                <el-option v-for="dict in certificateModelOptionLists" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
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
            <el-form-item label="发证日期" prop="startTime">
              <el-date-picker format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="form.startTime" type="date" placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="有效期截止时间" prop="endTime">
              <el-date-picker format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="form.endTime" type="date" placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="发证机关" prop="issuingAuthority" class="add-form-input">
              <el-input v-model="form.issuingAuthority" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
      </create-sections>
    </el-form>

  </xr-create>
</template>

<script>

import XrCreate from '@/components/XrCreate'
import CreateSections from '@/components/CreateSections'

import WkUploadFile from '@/components/CreateCom/WkUploadFile'

import { debounce } from 'throttle-debounce'

import { saveOrUpdate, getById, selectFBSInfo, selectInfoByMapNoPage, selectWorkTypeInfo, getProjectsList } from "./safetymember";


export default {
  // 新建编辑
  name: 'monitersetCreate',
  components: {
    XrCreate,
    CreateSections,
    WkUploadFile,
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
      workTypeOptionLists: [],
	  workTypeOptions: [],
      //认证信息
      authStatusOptions: [{ 'dictLabel': '未认证', 'dictValue': '0' }, { 'dictLabel': '已认证', 'dictValue': '1' }],
      //岗位类型
      postTypeOptions: [{ 'dictLabel': '管理人员', 'dictValue': '10' }, { 'dictLabel': '建筑工人', 'dictValue': '20' }],
      //证书型号
      certificateModelOptionLists: [{ 'dictLabel': 'A证', 'dictValue': '01' }, { 'dictLabel': 'B证', 'dictValue': '02' }, { 'dictLabel': 'C证', 'dictValue': '03' }],
      //岗位名称
      postNameOptions: [],
      //证书名称
      certificateNameOptions: [],
	  // 项目列表
	  projectOptions:[],
      disabledForm: false,

      rules: {
        projectId: [
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
        workTypeId: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        certificateName: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        certificateNo: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        cerCategory: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        certificateType: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        certificateModel:[
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        authTime: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        authStatus: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        issuingAuthority: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        endTime: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],

      },
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
	// 查询项目数据
	getProjectsList().then(res => {
		this.projectOptions = res.data;
	})
    //  证书种类 字典项
    this.getDicts('b_cerCategory').then(response => {
      this.cerCategoryOptions = response.data
    })
    //证书名称
    this.getDicts('b_labour_certificate_name').then(response => {
      this.certificateNameOptions = response.data
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
	// 工种数据
    /* selectWorkTypeInfo().then(res => {
      this.workTypeOptionLists = res.data.list
    }) */
	// 工种字典
	this.getDicts('b_labour_work_type').then(response => {
	  this.workTypeOptions = response.data
	})
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
          console.log(res)
          this.cerOptions(res.data.cerCategory)//证书种类
          //this.selectInfoByMapNoPage(res.data.subcontractor);//根据分包商查询人员列表信息
          this.selectInfoByMapNoPage(res.data.projectId);//根据项目查询人员列表信息

          this.form = res.data;
          this.loading = false

        })
        .catch(() => {
        })
    },
    /**
     * 保存
     */
    save (isSubmit) {
      this.$refs['form'].validate(valid => {
        if (valid) {

          var params = JSON.parse(JSON.stringify(this.form));
          params.authTime = params.authTime + ' 00:00:00'
          params.startTime = params.startTime + ' 00:00:00'
          params.endTime = params.endTime + ' 00:00:00'
          console.log(this.form);
          console.log(params)
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
      this.form.certificateType = ''
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
	
	
	projectChange(data) {
		this.$forceUpdate()//强制刷新
		this.form.infocolId = ''
		this.form.idCard = ''
		this.selectInfoByMapNoPage(data)
	},
	// 根据项目显示人员信息
	selectInfoByMapNoPage(data){
		let params = {
			projectId: data
		}
		//params.subcontractor = data
		selectInfoByMapNoPage(params).then(response => {
		  this.peopleList = response.data
		  if (this.action.type == "update" || this.action.type == "showDetials") {
		    this.peopleChange(this.form.infocolId)//人员身份证
		  }
		  this.loading = false
		}).catch(() => {
		  this.loading = false
		})
	},

    /* selectInfoByMapNoPageChange (data) {
      this.$forceUpdate()//强制刷新
      this.form.infocolId = ''
      this.form.idCard = ''
      this.selectInfoByMapNoPage(data)

    }, */
    //根据选择的人员显示身份证和工种
    peopleChange (date) {
      this.$forceUpdate()//强制刷新
      let datas = this.peopleList

      Object.keys(datas).some((key) => {
        var a = datas[key].id
        if (a == ('' + date)) {
          this.form.idCard = datas[key].idCard
          this.$set(this.form, 'workTypeId', datas[key].workTypeDict)
          return true
        }
      })


    },
    certificateNameChange () {
      this.$forceUpdate()//强制刷新
    },
    workTypeIdChangeSelect () {
      this.$forceUpdate()//强制刷新
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
