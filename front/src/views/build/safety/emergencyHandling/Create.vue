<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)"
    :showSave="!disabledT">
    
    <create-sections title="基本信息">
      <el-form ref="form" :model="form" :disabled="disabledT" :rules="rules" label-position="top" label-width="100px"
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
            <el-form-item label="关联应急预案" prop="eId">
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
                style="width: 100%;"
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
       <el-button
            class="xr-btn--orange"
            icon="el-icon-plus"
            type="primary"
            style="float: right"
            :disabled="disabledT"
            @click="createClick">新建
        </el-button>
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
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-document"
              @click="showDetails(scope.row.id)"
            >查看
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              :disabled="disabledT"
              @click="updateClick(scope.row.id)"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              :disabled="disabledT"
              @click="deleteClick(scope.row.id)"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </create-sections>

    <create
      v-if="createShow"
      :action="actionDetail"
      @close="createShow = false"
      @save-success="getListDetail"
    />

  </xr-create>
  
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'

  import WkUploadFile from '@/components/CreateCom/WkUploadFile'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {updateById, save, getById, selectEids} from "../api/emergencyHandling";
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
      XhStrucUserCell
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
      },
    },
    data() {
      return {
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
        actionDetail: {type: 'save',pid:this.action.id, data: null},//新增修改子表传递参数
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
            width: "120"
          },
          {
            prop: "handleUserName",
            label: "处置人",
            width: "120"
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
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑应急事件处置' : (this.action.type === 'show' ? '查看应急事件处置' : '新建应急事件处置')   
      },
      disabledT() {
        return this.action.type === 'show' ? true : false
      },

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)

      //事故类型
      this.getDicts('b_safety_accident_Type').then(response => {
        this.accidentTypeOptions = response.data
      })

      //处置类型
      this.getDicts('b_safety_handle_type').then(response => {
        this.handleTypeOptions = response.data
      })

      //关联应急预案
      this.selectEids()

      if (this.action.type == "save") {
         this.batchId = guid();
         this.form.id = guid();
         this.action.id = this.form.id
      }else{
         this.getDetail()
      }
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
        getById(this.action.id)
          .then(res => {
            
            let data = res.data

            //消息推送人员
            let a = data.ownerUserList
            let dep = {ownerUserList:a}
            data.dep = dep

            this.batchId = res.data.batchId;
          
            this.form = data;

            this.getListDetail()

          })
          .catch(() => {
          })
      },
      getListDetail(){
        getListDetail(this.action.id).then(res =>{
            
            this.list = res.data.list
            
          })
      },
      /**
       * 保存
       */
      save(isSubmit) {
        this.$refs['form'].validate(valid => {
          if (valid) {
            //如果为异步上传需要主动调用提交
            this.form.batchId = this.batchId;
            var fileFlag = this.$refs.wkUploadFile.submitAsync();
            if (!fileFlag) {
              this.$message({
                type: 'error',
                message: '方案附件提交异常'
              })
              return;
            }

            this.loading = true;

            if(this.action.type == 'update'){
             
                updateById(this.form).then(response => {
                if (response.code === 0) {
                  
                  this.$message.success(
                    '编辑成功'
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
            }else{
              save(this.form).then(response => {
                if (response.code === 0) {
                  
                  this.$message.success(
                    '添加成功'
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
            

          }
        })
       
      },

      /**
       * 关闭
       */
      close() {
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
       * 新建点击
       */
      createClick() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            //如果为异步上传需要主动调用提交
            // var fileFlag = this.$refs.wkUploadFile.submitAsync();
            // if (!fileFlag) {
            //   this.$message({
            //     type: 'error',
            //     message: '方案附件提交异常'
            //   })
            //   return;
            // }else{
            //   this.form.batchId = this.batchId;
            // }

            this.actionDetail.pid = this.form.id
            this.actionDetail.type = 'save';
            this.createShow = true

          }
        })
        
      },
      /**
       * 修改点击
       */
      updateClick(id) {
        this.actionDetail.type = 'update';
        this.actionDetail.id = id;
        this.createShow = true
      },
       /**
       * 查看点击
       */
      showDetails(id){
          this.actionDetail.type = 'show';
          this.actionDetail.id = id;
          this.createShow = true
      },

      deleteClick(id) {
        this.$confirm("确定删除选中的数据吗？", '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            const ids = [id];
            this.loading = true;
            delTrueByIdsDetail(ids).then(res => {
              if (res.code == 0) {
                this.$message.success("删除成功");
                this.getDetail();
              } else {
                this.$message.error(res.msg);
              }
              this.loading = false;
            }).catch(err => {
              this.loading = false;
            });
          })
          .catch(() => {
          })
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
  // #crm-table{
  //   height: 120px!important;
  // }
  /deep/
  .el-input{
    width: 395px;
  }
</style>
