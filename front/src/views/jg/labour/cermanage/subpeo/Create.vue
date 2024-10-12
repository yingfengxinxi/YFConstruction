<template>
  <xr-create
    :loading="loading"
    :title="title"
    :showSave="this.action.type === 'showDetials'?false:true"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">
    
      <el-form ref="form" :disabled="disabledForm" :model="form" :rules="rules" label-position="top" label-width="100px"
               class="form">
        
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
              <el-form-item  label="分包单位" class="add-form-input subcontractorClass" prop="sid" >
                <el-select
                        v-model="form.sid"
                        placeholder="请选择"
                        clearable
                        size="small">
                  <el-option
                          v-for="dict in FBSList"
                          :key="dict.realName"
                          :label="dict.realName"
                          :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item  label="隶属人员" class="add-form-input subcontractorClass" prop="pid" >
                <el-select
                        v-model="form.pid"
                        @change="peopleChange"
                        placeholder="请选择"
                        clearable
                        size="small">
                  <el-option
                    v-for="dict in peopleList"
                    :key="dict.name+dict.id"
                    :label="dict.name"
                    :value="dict.id" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item  label="人员类型" class="add-form-input subcontractorClass" prop="peopleType" >
                <el-select
                        v-model="form.peopleType"
                        placeholder="请选择"
                        clearable
                        size="small">
                  <el-option
                    v-for="dict in peopleTypeList"
                    :key="dict.dictLabel+dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="岗位/职位" prop="postName">
                <el-input v-model="form.postName" placeholder="请输入岗位/职位"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="岗位证书名称" prop="cerName">
                <el-input v-model="form.cerName" placeholder="请输入岗位证书名称"></el-input>
              </el-form-item>
            </el-col>
            
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="证书编号" prop="cerCode">
                <el-input v-model="form.cerCode" placeholder="请输入证书编号"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="归档日期" prop="endDate">
                <el-date-picker
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                  v-model="form.endDate"
                  type="date"
                  placeholder="选择日期" >
                </el-date-picker>
              </el-form-item>

            </el-col>
            <el-col :span="8">
              <el-form-item label="有效期" prop="endEffectiveDate">
                <el-date-picker
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                  v-model="form.endEffectiveDate"
                  type="date"
                  placeholder="选择日期" >
                </el-date-picker>
              </el-form-item>

            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="备注" prop="note">
                <el-input type="textarea" v-model="form.note" placeholder="请输入备注"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
       
      </el-form>
    
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'

  import WkUploadFile from '@/components/CreateCom/WkUploadFile'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById, selectFBSInfo, selectInfoByMapNoPage, getProjectsList} from "./subpeo";


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
    data() {
      return {
        loading: false,
        form: {
        },
        //分包商list
        FBSList:[],
        //人员信息
        peopleList:[],
        //人员类型
        peopleTypeList:[],
        disabledForm:false,
		// 项目列表
		projectOptions:[],

        rules: {
          projectId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          sid: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          pid: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          peopleType: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
         
        },
      }
    },
    computed: {
      title() {
        if(this.action.type === 'showDetials'){
          return '分包管理人员证件信息'
        }else{
         return this.action.type === 'update' ? '编辑分包管理人员证件信息' : '添加分包管理人员证件信息'
        }
      },

    },
    watch: {},
    created() {
      
      //  人员类型 字典项
      this.getDicts('b_peopleType').then(response => {
        this.peopleTypeList = response.data
      })
	  
	  // 查询项目数据
	  getProjectsList().then(res => {
	  		this.projectOptions = res.data;
	  })

      this.selectFBSInfo();//分包商单位

      this.debouncedSaveField = debounce(300, this.save)

      if (this.action.type == "update") {
        this.getDetail();
      }
      if(this.action.type === 'showDetials'){
        this.disabledForm = true
        this.getDetail();
      }
    },

    mounted() {
    },

    beforeDestroy() {
    },

    methods: {
      /**
       * 获取详情
       * */
      getDetail() {
        getById(this.action.id)
          .then(res => {
            //根据分包商查询人员列表信息
            //this.selectInfoByMapNoPage(res.data.subcontractor);//根据分包商查询人员列表信息
            this.projectChange(res.data.projectId);//根据项目查询人员列表信息
            this.peopleChange(res.data.pid)//人员信息

            this.form = res.data;

          })
          .catch(() => {
          })
      },
      /**
       * 保存
       */
      save(isSubmit) {
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
      close() {
        // console.log("close")
        this.$emit('close')
      },

      //根据证书种类显示证书类型
      cerOptions:function(value){

        var actions = []
        var datas = this.cerCategoryTypeOptions

        Object.keys(datas).some((key) => {
          var a = datas[key].dictValue.split('-')[0]
          if (a == ('' + value)) {
            actions.push(datas[key])
          }
        })

        this.cerCategoryTypeOptions_ = actions
      },

       /** 查询所有分包商id和name */
      selectFBSInfo(data){
          // this.$forceUpdate()//强制刷新
          selectFBSInfo().then(response => {
          this.FBSList = response.data
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },
	  
	  //根据项目显示人员信息
	  projectChange(data){
	      // this.$forceUpdate()//强制刷新
	     let pam = {}        
	     pam.projectId = data
	    selectInfoByMapNoPage(pam).then(response =>{
	      this.peopleList = response.data
	      this.loading = false
	    }).catch(() => {
	      this.loading = false
	    })
	  
	  },

     /* //根据分包商显示人员信息
      selectInfoByMapNoPage(data){
          // this.$forceUpdate()//强制刷新
         let pam = {}        
         pam.subcontractor = data
        selectInfoByMapNoPage(pam).then(response =>{
          this.peopleList = response.data
          this.loading = false
        }).catch(() => {
          this.loading = false
        })

      }, */

      //根据选择的人员显示身份证
      peopleChange(date){
        
        let datas  = this.peopleList

        Object.keys(datas).some((key) => {
          var a = datas[key].id
          if (a == ('' + date)) {
            this.form.idCard = datas[key].idCard
            return true
          }
        })


      }


    }
  }
</script>

<style lang="scss" scoped>
  .form
  /deep/
  .el-form-item
  .el-form-item__content {
    width: calc(100% - 10px);
  }

  .form
  /deep/
  .el-form-item__label {
    padding: 0px;
  }
  .form
  /deep/
  .el-date-editor{
    width: 100%;
  }
  /deep/
  .el-select{
    width: 100%;
  }
</style>
