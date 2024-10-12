<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">
    <create-sections title="基本信息">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
               class="form">
        <el-row>
          <el-col :span="12">
            <el-form-item label="上报人" prop="informer">
              <el-input v-model="form.informer" placeholder="请输入上报人"></el-input>
            </el-form-item>
          </el-col>
		  <el-col :span="12">
		    <el-form-item label="上报日期" prop="reportTime">
		      <el-date-picker
		        v-model="form.reportTime"
		        type="date"
		        value-format="yyyy-MM-dd"
		        placeholder="请选择上报日期"
				:default-value="new Date()"
		      >
		      </el-date-picker>
		    </el-form-item>
		  </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="内容" prop="content">
              <el-input v-model="form.content" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
		  <el-col :span="12">
		    <el-form-item label="备注" prop="remark">
		      <el-input v-model="form.remark" placeholder="请输入备注"></el-input>
		    </el-form-item>
		  </el-col>
        </el-row>
      </el-form>
	  
      <!--图片上传-->
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
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'

  import WkUploadImg from '@/components/CreateCom/WkUploadImg'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById} from "../api/imageDisplay"

  //业务审批Copy:审批流程
  //import ExamineMixin from '@/components/common/examine/mixins/examine'
  //import CreateExamine from '@/components/common/examine/create'

  export default {
    // 新建编辑
    name: 'PlanCreate',
    components: {
      XrCreate,
      CreateSections,
      WkUploadImg
    },
    //mixins: [ExamineMixin],//业务审批Copy:审批
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
      },
	  // 字典数据
	  dictData: {},
    },
    data() {
      return {
        loading: false,	
		batchId:'',
        form: {
			reportTime: ''
        },

        rules: {
          informer: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          reportTime: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          content: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
        },
      }
    },
    computed: {
      title() {
        return this.action.type === 'details' ? '查看详情' : '新建进度'
      },

    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)
	  this.initStartTime()
      //考虑附件ID
      if (this.action.type == "details") {
				this.getDetail();
      } else {  
		  this.batchId = guid()
		  this.form.batchId = this.batchId
	  }
    },
	watch: {
	  
	},
    methods: {
      /**
       * 获取详情
       * */
      getDetail() {
        getById(this.action.id)
          .then(res => {
            this.form = res.data
			this.batchId = res.data.batchId
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
			  
			var fileFlag = this.$refs['wkUploadImg'].submitAsync();
			if (!fileFlag) {
			    this.$message({
			        type: 'error',
			        message: '图片上传异常'
			    })
			    return;
			}
            
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
        this.$emit('close')
      }, 
	  // 默认查询时间为今天
	  initStartTime() {
	    var date = this.formatterDate(new Date())
	    this.form.reportTime = date
	  },
	  // 格式化时间字符串
	  formatterDate(date) {
	    if(date==null || date=="") return "";
	    let Y = date.getFullYear() + "-";
	    let M = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) + "-" : date.getMonth() + 1 + "-";
	    let D = date.getDate() < 10 ? "0" + date.getDate() + " " : date.getDate() + " ";
	    let h = date.getHours() < 10 ? "0" + date.getHours() + ":" : date.getHours() + ":";
	    let m = date.getMinutes()  < 10 ? "0" + date.getMinutes() + ":" : date.getMinutes() + ":";
	    let s = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
	  	let all = (Y + M + D)
	    return all;
	  },
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
  .el-date-editor, .el-select{
    width: 100%;
  }
</style>
