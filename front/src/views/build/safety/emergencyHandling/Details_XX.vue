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
                        <el-form-item label="事故编号" prop="code">
                          <el-input v-model="form.code" placeholder="请输入"></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="事故发生时间" prop="happenedTime">          
                          <el-date-picker
                                  v-model="form.happenedTime"
                                  type="datetime" placeholder="选择时间" value-format="yyyy-MM-dd HH:mm:ss">
                          </el-date-picker>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="12">
                        <el-form-item label="事故发生地点" prop="happenedAddress">
                          <el-input v-model="form.happenedAddress" placeholder="请输入"></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="事故发生类型" prop="accidentType">
                          <el-select
                              v-model="form.accidentType"
                              placeholder="请选择"
                              clearable
                              size="small">
                          <el-option
                              v-for="dict in accidentTypeOptions"
                              :key="dict.dictValue"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                        </el-select>
                        </el-form-item>
                      </el-col>

                    </el-row>
                    <el-row>
                      <el-col :span="12">
                        <el-form-item label="死亡人数" prop="deathNumber">
                          <el-input v-model="form.deathNumber" type="number" placeholder="请输入"></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="重伤人数" prop="seriousInjuryNumber">
                          <el-input v-model="form.seriousInjuryNumber" type="number" placeholder="请输入"></el-input>
                        </el-form-item>
                      </el-col>

                    </el-row>
                    <el-row>
                      <el-col :span="12">
                        <el-form-item label="事故描述" prop="accidentNote">
                          <el-input type="textarea" :rows="2" v-model="form.accidentNote" placeholder="请输入"></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="事故原因" prop="accidentReason">
                          <el-input type="textarea" :rows="2" v-model="form.accidentReason" placeholder="请输入"></el-input>
                        </el-form-item>
                      </el-col>

                    </el-row>
                    <el-row>
                      <el-col :span="12">
                        <el-form-item label="关联应急预案" prop="eid">
                          <el-select
                              v-model="form.eid"
                              placeholder="请选择"
                              clearable
                              size="small">
                          <el-option
                              v-for="dict in eIdsOptions"
                              :key="dict.id"
                              :label="dict.name"
                              :value="dict.id" />
                        </el-select>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="消息推送人员"
                        :error="error.reviewMemberError"
                        prop="toUsersId">
                          <xh-struc-user-cell
                            :users="form.dep.ownerUserList"
                            style="width: 99%;"
                            @value-change="reviewMemberChange"
                          />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="24">
                        <el-form-item label="附件">
                          <wk-upload-file
                            ref="wkUploadFile"
                            name="file"
                            multiple
                            accept="*"
                            :autoUpload="false"
                            :batchId="batchId"
                          ></wk-upload-file>
                        </el-form-item>
                      </el-col>
                    </el-row>

                  </el-form>
                </create-sections>
                

                <create-sections title="详细信息">
                  
                  <el-table
                    v-loading="loading"
                    :data="list"
                    :height="tableHeight"
                    use-virtual
                    stripe
                    border
                    highlight-current-row
                    style="width: 100%">
                    
                    <el-table-column
                      v-for="(item, index) in fieldList"
                      :key="index"
                      :fixed="index==0"
                      :prop="item.prop"
                      :label="item.label"
                      :width="item.width"
                      align="center"
                      :formatter="fieldFormatter"
                      :sortable="false"
                      show-overflow-tooltip>

                    </el-table-column>
                    <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140px;" fixed="right">
                      <template slot-scope="scope">
                        <el-button
                          size="mini"
                          type="text"
                          icon="el-icon-document"
                          @click="showDetails(scope.row.id)"
                        >查看
                        </el-button>
                        
                        
                      </template>
                    </el-table-column>
                  </el-table>
                </create-sections>
              </div>

              <div v-else>
                <create-sections title="基本信息">
                  <el-form ref="Eform" :model="Eform"  :disabled="true"  label-position="top"  label-width="100px" 
                    class="add-form">
                    <el-row>
                      <el-col :span="12">
                        <el-form-item label="应急预案名称" prop="safetyEmergencyPlanName" class="add-form-input">
                          <el-input v-model="Eform.safetyEmergencyPlanName" placeholder="请输入" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="应急预案等级" prop="safetyEmergencyPlanLevel" class="add-form-input">
                          <el-select
                            v-model="Eform.safetyEmergencyPlanLevel"
                            placeholder="请选择"
                            clearable
                            size="small">
                            <el-option
                              v-for="dict in safetyEmergencyPlanLevel"
                              :key="dict.dictValue"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                          </el-select>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    
                    <el-row>
                      <el-col :span="24">
                        <el-form-item label="应急预案描述" prop="safetyEmergencyPlanDescribe">
                          <el-input type="textarea" :rows="2" v-model="Eform.safetyEmergencyPlanDescribe" placeholder="请输入应急预案描述"></el-input>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    
                    <el-row>
                      <el-col :span="24">
                        <el-form-item label="应急预案附件">
                          <wk-upload-file
                            ref="wkUploadFile"
                            :auto-upload="false"
                            :batch-id="EbatchId"
                            name="file"
                            multiple
                            accept="*"
                          ></wk-upload-file>
                        </el-form-item>
                      </el-col>
                    </el-row>
                  </el-form>
                </create-sections>

                <create-sections title="详细信息">
                   <el-table v-loading="loading" :data="membersList" >
                      <el-table-column label="人员名称" align="center" prop="membersName" />
                      <el-table-column label="职能" align="center" prop="membersFunction" />
                      <el-table-column label="联系电话" align="center" prop="membersTel" />
                      <el-table-column label="备注" align="center" prop="membersRemarks" />

                      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140px">
                        <template slot-scope="scope">
                          <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-edit"
                            @click="handleDetailMembers(scope.row)"
                          >查看</el-button>
                          
                        </template>
                      </el-table-column>
                    </el-table>
                </create-sections>
                
              </div>
            </el-tab-pane>
          </el-tabs>
          </div>

          
      </flexbox>
    </div>

    <create
      v-if="createShow"
      :action="actionDetail"
      @close="createShow = false"
      @save-success="getListDetail"
    />

    <el-dialog title="应急预案人员查看" :visible.sync="membersOpen" width="900px" append-to-body>
        <el-form ref="formMembers" :model="formMembers" :disabled="true" label-width="100px" class="add-form">
          <el-form-item label="人员名称" prop="membersName" class="add-form-input">
            <el-input v-model="formMembers.membersName" placeholder="请输入" />
          </el-form-item>
          <el-form-item label="职能" prop="membersFunction" class="add-form-input">
            <el-input v-model="formMembers.membersFunction" placeholder="请输入" />
          </el-form-item>
          <el-form-item label="联系电话" prop="membersTel" class="add-form-input">
            <el-input v-model="formMembers.membersTel" placeholder="请输入" />
          </el-form-item>
          <el-form-item label="备注" prop="membersRemarks" class="add-form-input">
            <el-input v-model="formMembers.membersRemarks" placeholder="请输入" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelMembers">取 消</el-button>
        </div>
      </el-dialog>

  </slide-view>
  
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import SlideView from '@/components/SlideView'
  import request from '@/utils/request'

  import WkUploadFile from '@/components/CreateCom/WkUploadFile'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {updateById, getById, selectEids} from "../api/emergencyHandling";
  import {getListDetail,delTrueByIdsDetail} from "../api/emergencyHandlingDetail"

  import { XhStrucUserCell } from "@/components/CreateCom";

  import Create from './CreateDetail'

  export default {
    // 新建编辑
    name: 'monitersetCreate',
    components: {
      XrCreate,
      CreateSections,
      WkUploadFile,
      Create,
      XhStrucUserCell,
      SlideView
    },

    props: {
      type: 'detail',
      id: [String, Number],
    },
    data() {
      return {
        tabCurrentName: 'Detail',
        error: { reviewMemberError: "" },
        loading: false,
        form: {//主表
          // happenedTime:this.initDataSel(),
          dep: {
            ownerUserList: []
          }
        },
        formDetail:{},//子表
        list: [],
        batchId: '',
        accidentTypeOptions : [],//事故类型
        handleTypeOptions : [],//处置类型
        eIdsOptions:[],//关联应急预案
        actionDetail: {type: 'show',pid:this.id, data: null},//新增修改子表传递参数
        createShow:false,
        tableHeight: document.documentElement.clientHeight - 720, // 表的高度
        rules: {
          code: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          happenedTime: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          happenedAddress: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          accidentType: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ]
        },
        fieldList: [//表头
          {
            prop: "handleType",
            label: "处置类型",
            width: "140"
          },
          {
            prop: "handleUserName",
            label: "处置人",
            width: "140"
          },
          {
            prop: "handleTime",
            label: "处置时间",
            width: "180"
          },
          {
            prop: "informationNote",
            label: "情况说明",
          },
        ],

        //应急预案tab页form
        Eform:{},
        //应急预案tab页---应急预案附件
        EbatchId:'',
        //应急预案人员list
        membersList:[],
        //应急预案  应急预案等级 字典表safety_emergency_level
        safetyEmergencyPlanLevel: [],
        //应急预案人员查看
        formMembers:{},
        // 是否显示应急预案人员查看弹出层
        membersOpen: false,
        
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
        {label: '应急预案', name: 'EDetail'},
      ]
      return tempsTabs
    }

    },
    watch: {},
    created() {
      
      //事故类型
      this.getDicts('b_safety_accident_Type').then(response => {
        this.accidentTypeOptions = response.data
      })

      //处置类型
      this.getDicts('b_safety_handle_type').then(response => {
        this.handleTypeOptions = response.data
      })

      //应急预案登等级
      this.getDicts('safety_emergency_level').then(response => {
        this.safetyEmergencyPlanLevel = response.data
      })

      //关联应急预案
      this.selectEids()

      this.getDetail()

    },

    mounted() {
    },

    beforeDestroy() {
    },

    methods: {
      //关联应急预案
      selectEids(){
        selectEids().then(res =>{
          this.eIdsOptions = res.data
        })
      },
      /**
       * 获取详情
       * */
      getDetail() {

        getById(this.id)
          .then(res => {
            
            let data = res.data

            //消息推送人员
            let a = data.ownerUserList
            let dep = {ownerUserList:a}
            data.dep = dep

            this.batchId = res.data.batchId;
          
            this.form = data;

            this.getListDetail()

            //应急预案
            this.handleDetail_E()

          })
          .catch(() => {
          })
      },
      getListDetail(){
        getListDetail(this.id).then(res =>{
            
            this.list = res.data.list
            
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

      //选择时间
      initDataSel(){
          //获取当前时间
          var now   = new Date()
          var monthn = now.getMonth()+1>9?now.getMonth()+1:"0"+(now.getMonth()+1)
          var yearn  = now.getFullYear()
          var dayn = now.getDate()>9?now.getDate():"0"+(now.getDate())
          var selectMonth = yearn+"-"+monthn +"-"+dayn
          
          return selectMonth
      },

       /**
       * 查看点击
       */
      showDetails(id){
          this.actionDetail.type = 'show';
          this.actionDetail.id = id;
          this.createShow = true
      },

      
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
        //console.log(row, column)
        //事故类型
        if(column.property == 'accidentType'){
          return this.selectDictLabel(this.accidentTypeOptions, row.accidentType)
        }
        //处置类型
        if(column.property == 'handleType'){
          return this.selectDictLabel(this.handleTypeOptions, row.handleType)
        }

        return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
      },

      /**添加消息推送 */
    reviewMemberChange(data) {
      if (data.value.users.length == 0) {
        this.error.reviewMemberError = "成员不能为空";
        return;
      }

      this.$set(this.form, "dep", { ownerUserList: data.value.users });
      var reviewMember = this.form.dep.ownerUserList
        .map(item => item.userId)
        .join(",");
      this.form.toUserIds = reviewMember;
      this.error.reviewMemberError = "";
    },

    /** 关联应急预案查询 */
    handleDetail_E() {
      
      const pipeId = this.form.eid
      request({
        url: 'build/bSafetySafetyEmergency/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.Eform = response.data
        this.EbatchId = response.data.batchId
        request({
          url: 'build/bSafetySafetyEmergencyMembers/selectByPId/' + response.data.batchId,
          method: 'get'
        }).then(response2 => {
          
          this.membersList = response2.data
          
        })
      })
    },

    //应急预案 人员查看
    handleDetailMembers(row){
      this.formMembers = {}
      const pipeId = row.id || this.ids0
      request({
        url: 'build/bSafetySafetyEmergencyMembers/selectById/' + pipeId,
        method: 'get'
      }).then(response => {
        this.formMembers = response.data
        this.membersOpen = true

      })
    },

    cancelMembers() {
      this.membersOpen = false
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
