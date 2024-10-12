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
          <el-col :span="24">
            <el-form-item label="计划名称" prop="planName">
              <el-input v-model="form.planName" placeholder="请输入计划名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="计划类型" prop="planType">
              <el-select v-model="form.planType" placeholder="请选择计划类型">
                <el-option
                  v-for="item in dictData.planTypeDict"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用" prop="isEnable">
              <el-select v-model="form.isEnable" placeholder="请选择是否启用">
                <el-option
                  v-for="item in enableOption"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="执行周期" prop="cycle">
              <el-input type="number" v-model="form.cycle" placeholder="请输入执行周期"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行周期单位" prop="cycleUnit">
              <el-select v-model="form.cycleUnit" placeholder="请选择执行周期单位"
              >
                <el-option
                  v-for="item in dictData.cycleUnitDict"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="计划等级" prop="planLevel">
              <el-select v-model="form.planLevel" placeholder="请选择计划等级">
                <el-option
                  v-for="item in dictData.planLevelDict"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行部门" prop="deptId">
              <select-tree :options="dictData.showDeptTreeData" 
			  v-model="form.deptId" :props="defaultProps" :expandAll="true"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="计划描述" prop="planDescribe">
              <el-input type="textarea" :rows="2" v-model="form.planDescribe" placeholder="请输入计划描述"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="启动日期" prop="startDate">
              <el-date-picker
                v-model="form.startDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择启动日期"
                :picker-options="pickerOptions"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col v-if="action.type === 'update'" :span="12">
            <el-form-item label="下次执行日期" prop="nextDate">
              <el-date-picker
				  v-model="form.nextDate"
				  type="date"
				  value-format="yyyy-MM-dd"
				  placeholder="下次执行日期"
				  :picker-options="pickerOptions"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
    </create-sections>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import SelectTree from '@/components/SelectTree'
  import CreateSections from '@/components/CreateSections'

  import WkUploadFile from '@/components/CreateCom/WkUploadFile'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById} from "../api/inspecitonPlan"

  //业务审批Copy:审批流程
  //import ExamineMixin from '@/components/common/examine/mixins/examine'
  //import CreateExamine from '@/components/common/examine/create'

  export default {
    // 新建编辑
    name: 'PlanCreate',
    components: {
      XrCreate,
      CreateSections,
      WkUploadFile,
	  SelectTree
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
        form: {
        },
		// 数据默认字段
		defaultProps: {
		  parent: 'parentId',   // 父级唯一标识
		  value: 'id',          // 唯一标识
		  label: 'label',       // 标签显示
		  children: 'children', // 子级
		},
		pickerOptions: { // 限制收货时间不让选择今天以前的
		  disabledDate(time) {
		    return time.getTime() < Date.now()
		  }
		},
		// 是否启用
		enableOption: [
			{
				label: '启用',
				value: '1'
			},
			{
				label: '禁用',
				value: '0'
			}
		],

        rules: {
          planName: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          planType: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          isEnable: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          cycle: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          cycleUnit: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          planLevel: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          deptId: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          planDescribe: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          startDate: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          nextDate: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
        },
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑计划' : '新建计划'
      },

    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
      if (this.action.type == "update") {
        this.getDetail();
      }
    },
	watch: {
	  /* 'form.cycle': function () {
	    this.planChange()
	  },
	  'form.cycleUnit': function () {
	    this.planChange()
	  },
	  'form.startDate': function () {
	    this.planChange()
	  }, */
	  'form.nextDate': function () {
	    if(this.action.type === 'save'){
	      var startDate = this.form.startDate;
	      var newNextDate = this.form.nextDate;
	      if (newNextDate <= startDate) {
	        this.$message({
	          message: '下次执行日期必须在启动日期【' + startDate + '】之后',
	          type: 'warning',
	          duration:3000
	        });
	        this.form.nextDate = null;
	      }
	    }else{
	      var oldNextDate = this.oldNextDate;
	      if (oldNextDate) {
	        var newNextDate = this.form.nextDate;
	        if (newNextDate < oldNextDate) {
	          this.$message({
	            message: '下次执行日期必须在原下次执行日期【' + this.oldNextDate + '】之后',
	            type: 'warning',
	            duration:3000
	          });
	          this.form.nextDate = oldNextDate;
	        }
	      }
	    }
	  }
	},
    methods: {
      /**
       * 获取详情
       * */
      getDetail() {
        getById(this.action.id)
          .then(res => {
            this.form = res.data
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
        this.$emit('close')
      },
	  //计划改变
	  planChange() {
	    if (this.action.type != 'save') {
	      return;
	    }
	    if (this.form.cycle && this.form.cycleUnit && this.form.startDate) {
	      //计算下次执行时间
	      var cycle = parseInt(this.form.cycle);
	      var startDate = new Date(this.form.startDate + ":00:00");
	      var nextDate = new Date();
	      var unit = this.form.cycleUnit;
	      if (unit == "1") {//小时
	        nextDate = startDate.setHours(startDate.getHours() + cycle);
	      }
	      if (unit == "2") {//天
	        nextDate = startDate.setDate(startDate.getDate() + cycle);
	      }
	      if (unit == "4") {//月
	        nextDate = startDate.setMonth(startDate.getMonth() + cycle);
	      }
	      if (unit == "5") {//年
	        nextDate = startDate.setFullYear(startDate.getFullYear() + cycle);
	      }
	      if (unit == "3") {//周
	        nextDate = startDate.setDate(startDate.getDate() + (7 * cycle));
	      }
	      nextDate = new Date(nextDate);
	      this.form.nextDate = nextDate.getFullYear() + "-" + (nextDate.getMonth() + 1) + "-" + nextDate.getDate() + " " + nextDate.getHours() + ":00:00";
	    } 
	  },
	  /* //是否本日
	  isToday() {
	    var today = new Date(new Date().toDateString() + " 00:00:00");
	    var nextDate = undefined;
	    if (this.form.nextDate.indexOf(":00:00") > 0) {
	      nextDate = new Date(new Date(this.form.nextDate).toDateString() + " 00:00:00");
	    } else {
	      nextDate = new Date(new Date(this.form.nextDate + ":00:00").toDateString() + " 00:00:00");
	    }
	    if (today.valueOf() == nextDate.valueOf()) {
	      return true;
	    }
	    return false;
	  }, */
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
