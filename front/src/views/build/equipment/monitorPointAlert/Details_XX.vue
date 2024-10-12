<template>
 <slide-view
    :body-style="{padding: 0, height: '100%'}"
    xs-empty-icon="nopermission"
    xs-empty-text="暂无权限"
    @afterEnter="getDetail"
    @close="hideView">
    
    <div
      v-loading="loading"
      ref="crmDetailMain"
      class="detail-main">
      <flexbox
        direction="column"
        align="stretch"
        class="d-container">

          <div class="d-container-bd">
          <el-tabs
            v-model="tabCurrentName"
            type="border-card"
            class="d-container-bd--left">
            <el-tab-pane
              v-for="(item, index) in tabNames"
              :key="index"
              :label="item.label"
              :name="item.name"
              style="overflow: auto;"
              lazy>
              <div v-if="item.isDetail">         
                <create-sections title="基本信息">
                  <el-form ref="form" :model="form" :disabled="true" label-position="top" label-width="100px"
                          class="form">
                    <el-row>
                      <el-col :span="12">
						<el-form-item label="报警类型" prop="monitorType">
						  <el-select
						  v-model="form.monitorType"
						  placeholder="请选择"
						  clearable
						  size="small">
							<el-option
							  v-for="dict in monitorTypeDict"
							  :key="dict.dictValue"
							  :label="dict.dictLabel"
							  :value="dict.dictValue" />
						  </el-select>
						</el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="监测点名称" prop="pointName">
                          <el-input v-model="form.pointName" placeholder="请输入"></el-input>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="12">
                        <el-form-item label="设备名称" prop="assetName">
                          <el-input v-model="form.assetName" placeholder="请输入"></el-input>
                        </el-form-item>
                      </el-col>
					  <el-col :span="12">
					    <el-form-item label="报警位置" prop="locationName">
					      <el-input v-model="form.locationName" placeholder="请输入"></el-input>
					    </el-form-item>
					  </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="12">
                        <el-form-item label="报警内容" prop="content">
                          <el-input v-model="form.content" placeholder="请输入"></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="报警值" prop="monitorData">
                          <el-input v-model="form.monitorData" type="number" placeholder="请输入"></el-input>
                        </el-form-item>
                      </el-col>

                    </el-row>
                    <el-row>  
					  <el-col :span="12">
					    <el-form-item label="报警上限值" prop="alarmUpper">
					      <el-input v-model="form.alarmUpper" type="number" placeholder="请输入"></el-input>
					    </el-form-item>
					  </el-col>
					  <el-col :span="12">
					    <el-form-item label="报警下限值" prop="alarmLower">
					      <el-input v-model="form.alarmLower" type="number" placeholder="请输入"></el-input>
					    </el-form-item>
					  </el-col>

                    </el-row>
                    <el-row>
						<el-col :span="12">
						  <el-form-item label="报警时间" prop="createTime">          
							<el-date-picker
									v-model="form.createTime"
									type="datetime" placeholder="选择时间" value-format="yyyy-MM-dd HH:mm:ss">
							</el-date-picker>
						  </el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="处理状态" prop="handleState">
							  <el-select
							  v-model="form.handleState"
							  placeholder="请选择"
							  clearable
							  size="small">
								<el-option
								  v-for="dict in handleStateDict"
								  :key="dict.dictValue"
								  :label="dict.dictLabel"
								  :value="dict.dictValue" />
							  </el-select>
							</el-form-item>
						</el-col>
                    </el-row>
                  </el-form>
                </create-sections>
				<create-sections title="处理信息">
				    <el-form
				        ref="form"
				        :model="form"
						:disabled="!(form.handleState == '0' && action.type == 'save')"
				        :rules="rules"
				        label-position="top"
				        label-width="100px"
				        class="form"
				    >
				        <el-row>
				            <el-col :span="12">
				                <el-form-item
				                    label="处理时间"
				                    prop="handleTime"
				                >
				                    <el-date-picker
				                        v-model="form.handleTime"
				                        type="datetime"
				                        placeholder="请选择处理时间"
				                        value-format="yyyy-MM-dd HH:mm:ss"
				                    >
				                    </el-date-picker>
				                </el-form-item>
				            </el-col>
				            <el-col :span="12">
				                <el-form-item
				                    label="处理人"
				                    prop="handleBy"
				                >
				                    <el-input
				                        v-model="form.handleBy"
				                        placeholder="请输入处理人"
				                    ></el-input>
				                </el-form-item>
				            </el-col>
				        </el-row>
				
				        <el-row>
				            <el-col :span="24">
				                <el-form-item
				                    label="处理措施"
				                    prop="handleMeasures"
				                >
				                    <el-input
				                        type="textarea"
				                        :rows="4"
				                        v-model="form.handleMeasures"
				                        placeholder="请输入处理措施"
				                    ></el-input>
				                </el-form-item>
				            </el-col>
				            <el-col :span="24">
				                <el-form-item
				                    label="处理后结论"
				                    prop="handleConclu"
				                >
				                    <el-input
				                        type="textarea"
				                        :rows="4"
				                        v-model="form.handleConclu"
				                        placeholder="请输入处理后结论"
				                    ></el-input>
				                </el-form-item>
				            </el-col>
				        </el-row>
				
				        <el-row>
				
				        </el-row>
				
				    </el-form>
				    <wk-upload-file
				        ref="wkUploadFile"	
						:disabled="!(form.handleState == '0' && action.type == 'save')"
				        name="file"
				        multiple
				        accept="image/*"
				        :autoUpload="false"
				        :batchId="batchId"
				    ></wk-upload-file>
					
					<el-button
					style="margin-top: 20px;"
					v-if="form.handleState == '0' && action.type == 'save'"
					  type="primary"
					  @click="save">保存
					</el-button>
				</create-sections>
              </div>
            </el-tab-pane>
          </el-tabs>
	  </div>     
      </flexbox>
    </div>

  
  </slide-view>
  
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import SlideView from '@/components/SlideView'

  import WkUploadFile from '@/components/CreateCom/WkUploadFile'

  import {guid} from '@/utils'

import { selectById, handleSave } from "../api/monitorPointAlert";

  export default {
    // 新建编辑
    name: 'monitersetCreate',
    components: {
      XrCreate,
      CreateSections,
      WkUploadFile,
      SlideView
    },

    props: ["action", "id"],
    data() {
      return {
        tabCurrentName: 'Detail',
        loading: false,
        form: {//主表
        },
        list: [],
        batchId: '',
        tableHeight: document.documentElement.clientHeight - 320, // 表的高度
		
		rules: {
		    handleTime: [
		        { required: true, message: '必填项不得为空', trigger: 'blur' }
		    ],
		    handleBy: [
		        { required: true, message: '必填项不得为空', trigger: 'blur' }
		    ],
		    handleMeasures: [
		        { required: true, message: '必填项不得为空', trigger: 'blur' }
		    ],
		    handleConclu: [
		        { required: true, message: '必填项不得为空', trigger: 'blur' }
		    ],
		},
        
        // 监测项类型字典项
        monitorTypeDict: [],
		// 处理状态
		handleStateDict: [
			{
				dictValue:'0',
				dictLabel:'未处理'
			},
			{
				dictValue:'1',
				dictLabel:'已处理'
			},
		]
      }
    },
    computed: {
      title() {
        return '查看应急事件处置'
      },
     

      //声明有多少Tab
    tabNames() {
      var tempsTabs = [
        {label: '详细资料', name: 'Detail', isDetail: true},
      ]
      return tempsTabs
    }

    },
    watch: {},
    created() {
      
      //事故类型
      this.getDicts('b_equipment_monitor_type').then(response => {
        this.monitorTypeDict = response.data
      })

      this.getDetail()

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

        selectById(this.id)
          .then(res => {
            
            let data = res.data
            this.batchId = res.data.batchId;
            this.form = data;
			if (!this.batchId) {
			  this.batchId = guid();
			}

          })
          .catch(() => {
          })
      },
	  
	  /**
	   * 保存
	   */
	  save(isSubmit) {
	      this.$refs['form'][1].validate(valid => {
	          if (valid) {
				  
				  this.form.batchId = this.batchId;
				  console.log("打印")
				  console.log(this.batchId)
				  console.log(this.form.batchId)
				  console.log("哈哈哈")
				  console.log(this.$refs['wkUploadFile'])
	              var fileFlag = this.$refs['wkUploadFile'][0].submitAsync();
	              if (!fileFlag) {
	                  this.$message({
	                      type: 'error',
	                      message: '图片上传异常'
	                  })
	                  return;
	              }
	              var params = this.form;
	              params.handleState='1'
	              params.handleTime = this.getNowFormatDate(params.handleTime);
	              // return ;
	              this.loading = true;
	              handleSave(params).then(response => {
	                  if (response.code === 0) {
	                      this.$message.success(
	                          '处理成功'
	                      )
	                      this.close()
	                      // 保存成功
	                      this.$emit('save-success', {
	                          type: 'receivables'
	                      })
						  
						  // 关闭页面
						  this.hideView()
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
      hideView() {
        this.$emit('close')
      },
	  getNowFormatDate(time) {
	      var date = new Date(time);
	      var seperator1 = "-";
	      var seperator2 = ":";
	      var month = date.getMonth() + 1;
	      var strDate = date.getDate();
	      if (month >= 1 && month <= 9) {
	          month = "0" + month;
	      }
	      if (strDate >= 0 && strDate <= 9) {
	          strDate = "0" + strDate;
	      }
	      var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	          + " " + date.getHours() + seperator2 + date.getMinutes()
	          + seperator2 + date.getSeconds();
	      return currentdate;
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
    width: 99%;
  }
  // #crm-table{
  //   height: 120px!important;
  // }
  /deep/
  .el-input{
    width: calc(100% - 10px);
  }

  /deep/
  .el-select{
    width: 100%;
  }
  .el-textarea{
    width: 99%;
  }

  /deep/
  .el-card__body{
    overflow-y: auto;
  }
</style>
