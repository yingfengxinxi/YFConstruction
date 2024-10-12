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
                <el-form-item label="所属项目" prop="pointCode">
                  <el-select style="width: 100%;" v-model="form.projectId" placeholder="请选择监测项类型">
                    <el-option
                      v-for="item in projects"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="设备名称" prop="equipmentId">
                    <el-select style="width: 100%;" v-model="form.equipmentId" placeholder="请选择设备">
                        <el-option
                                v-for="item in equipmentOptions"
                                :key="item.assetId"
                                :label="item.assetName"
                                :value="item.assetId">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="监测点编号" prop="pointCode">
              <el-input v-model="form.pointCode" placeholder="请输入监测点编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监测点名称" prop="pointName">
              <el-input v-model="form.pointName" placeholder="请输入监测点名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
            <el-col :span="12">
                <el-form-item label="监测类型" prop="monitorSonType">
                    <el-select style="width: 100%;" v-model="form.monitorSonType" placeholder="请选择">
                        <el-option
                                v-for="item in dictData.monitorTypeDict_9"
                                :key="item.dictValue"
                                :label="item.dictLabel"
                                :value="item.dictValue">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-col>
			<el-col :span="12">
			  <el-form-item label="统计类型" prop="statisticalType">
				<el-select style="width: 100%;" v-model="form.statisticalType" placeholder="请选择统计类型">
				  <el-option
					v-for="item in dictData.statisticalTypeDict"
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
            <el-form-item label="单位" prop="unit">
              <el-input v-model="form.unit" placeholder="请输入单位"></el-input>
            </el-form-item>
          </el-col>
        	<el-col :span="12">
        	  <el-form-item label="排序" prop="sort">
        	    <el-input v-model.number="form.sort" type="number" placeholder="请输入排序"></el-input>
        	  </el-form-item>
        	</el-col>
        </el-row>

<!--        <el-row> -->
<!--		  <el-col :span="12">-->
<!--		    <el-form-item label="是否关联喷淋系统" prop="isSpraySystem">-->
<!--		      <el-select style="width: 100%;" v-model="form.isSpraySystem"-->
<!--		          			@change="spraySystemChange" placeholder="请选择">-->
<!--		        <el-option-->
<!--		      	v-for="item in spraySystemOptions"-->
<!--		      	:key="item.value"-->
<!--		      	:label="item.label"-->
<!--		      	:value="item.value">-->
<!--		        </el-option>-->
<!--		      </el-select>-->
<!--		    </el-form-item>-->
<!--		  </el-col>-->
<!--        </el-row>-->

<!--		<el-row>-->
<!--		  <el-col :span="12">-->
<!--		    <el-form-item label="启动喷淋值" prop="startSprayValue">-->
<!--		      <el-input v-model.number="form.startSprayValue" type="number" placeholder="请输入报警上限阀值"></el-input>-->
<!--		    </el-form-item>-->
<!--		  </el-col>-->
<!--		  <el-col :span="12">-->
<!--		    <el-form-item label="关闭喷淋值" prop="closeSprayValue">-->
<!--		      <el-input v-model.number="form.closeSprayValue" type="number" placeholder="请输入报警下限阀值"></el-input>-->
<!--		    </el-form-item>-->
<!--		  </el-col>-->
<!--		</el-row>-->

		<el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="预警上限阀值" prop="warnUpper">
            <el-input v-model.number="form.warnUpper" type="number" placeholder="请输入预警上限阀值"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预警下限阀值" prop="warnLower">
            <el-input v-model.number="form.warnLower" type="number" placeholder="请输入预警下限阀值"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
		  <el-col :span="12">
		    <el-form-item label="报警上限阀值" prop="alarmUpper">
		      <el-input v-model.number="form.alarmUpper" type="number" placeholder="请输入报警上限阀值"></el-input>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="报警下限阀值" prop="alarmLower">
		      <el-input v-model.number="form.alarmLower" type="number" placeholder="请输入报警下限阀值"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="控制上限阀值" prop="contrUpper">
              <el-input v-model.number="form.contrUpper" type="number" placeholder="请输入控制上限阀值"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="控制下限阀值" prop="contrLower">
              <el-input v-model.number="form.contrLower" type="number" placeholder="请输入控制下限阀值"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

      <el-row>
          <!--      <el-col :span="12">-->
          <!--        <el-form-item label="提醒类型" prop="pointType">-->
          <!--          <el-select style="width: 100%;" v-model="form.pointType"-->
          <!--                      placeholder="请选择">-->
          <!--            <el-option-->
          <!--              v-for="item in pointType"-->
          <!--              :key="item.value"-->
          <!--              :label="item.label"-->
          <!--              :value="item.value">-->
          <!--            </el-option>-->
          <!--          </el-select>-->
          <!--        </el-form-item>-->
          <!--      </el-col>-->
          <el-col :span="12">
              <el-form-item label="报警持续时间范围（秒），为0时立即报警" prop="rangeTime">
                  <el-input v-model.number="form.rangeTime" type="number" placeholder="请输入警报范围"></el-input>
              </el-form-item>
          </el-col>
      </el-row>

<!--		<el-row>-->
<!--		  <el-col :span="12">-->
<!--		    <el-form-item label="经度" prop="longitude">-->
<!--		      <el-input v-model.number="form.longitude" type="number" placeholder="请输入经度"></el-input>-->
<!--		    </el-form-item>-->
<!--		  </el-col>-->
<!--		  <el-col :span="12">-->
<!--		    <el-form-item label="纬度" prop="latitude">-->
<!--		      <el-input v-model.number="form.latitude" type="number" placeholder="请输入纬度"></el-input>-->
<!--		    </el-form-item>-->
<!--		  </el-col>-->
<!--		</el-row>-->

      </el-form>
    </create-sections>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById,getProjectsList} from "../api/monitorPoint"
  import {selectEquOptions } from "../api/assets"
  export default {
    // 新建编辑
    name: 'PlanBgCreate',
    components: {
      XrCreate,
      CreateSections
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
	  dictData: {},
    },
    data() {
      return {
        loading: false,
        form: {
        },
		// 是否关联喷淋系统
		spraySystemOptions: [
			{
				label: '是',
				value: '1'
			},
			{
				label: '否',
				value: '0'
			},
		],
        pointType: [
        {
          label: '警报提醒',
          value: '1'
        },
        {
          label: '预警提醒',
          value: '0'
        },
      ],
        projects:[],
        equipmentOptions:[],
        rules: {
          pointCode: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          pointName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          monitorType: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          equipmentId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          unit: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          sort: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          monitorSonType: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
        },
		// 默认监测项类型为 9
		monitorType: '9',
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑监测点' : '新建监测点'
      },

    },
    watch: {
      'form.projectId'() {
        //console.log(this.form.monitorType)
        if (this.form.projectId) {
          selectEquOptions({projectId:this.form.projectId}).then(res => {
            this.equipmentOptions = res.data == null ? [] : res.data;
          }).catch(() => {
          });
        } else {
          this.equipmentOptions = [];
        }

      }
    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
      if (this.action.type == "update") {
        this.getDetail();
      }
      getProjectsList().then(res => {
        console.log(res)
        this.projects = res.data;
      })
        .catch(() => {
        })
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

			this.form.monitorType = this.monitorType
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
        console.log("close")
        this.$emit('close')
      },
	  //
	  spraySystemChange(value) {
		  if (value === '1') {
			 this.rules.startSprayValue = [
			   {required: true, message: '必填项不得为空', trigger: 'blur'}
			 ]
			 this.rules.closeSprayValue = [
			   {required: true, message: '必填项不得为空', trigger: 'blur'}
			 ]
		  } else {
			 delete this.rules.startSprayValue
			 delete this.rules.closeSprayValue
		  }
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
</style>
