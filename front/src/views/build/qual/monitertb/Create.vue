<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">
    
      <el-form ref="form" :model="form" :disabled="disabledT" :rules="rules" label-position="top" label-width="100px"
               class="form">
        <el-row>
          <el-col :span="8">
            <el-form-item label="填报编号" prop="code">
              <el-input v-model="form.code" placeholder="请输入编号"></el-input>
            </el-form-item>
          </el-col>
           <el-col :span="8">
            <el-form-item label="填报日期" prop="dataDate">          
              <el-date-picker
                      v-model="form.dataDate"
                      type="date" placeholder="选择日期" value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-table
        v-loading="loading"
        id="crm-table"
        :data="list"
        :height="tableHeight"
        class="n-table--border"
        use-virtual
        stripe
        border
        highlight-current-row
        style="width: 100%">
          <el-table-column :show-overflow-tooltip="true" label="监测项1编号" align="center" prop="code" />
          <el-table-column :show-overflow-tooltip="true" label="监测项名称" align="center" prop="name" />

          <el-table-column label="监测采集值" align="center" prop="sValue">
            <template  slot-scope="scope">

              <el-input size="small" type="number" v-model="scope.row.sValue"  
              placeholder="请输入监测采集值" ></el-input>
              
            </template>
          </el-table-column>

          <el-table-column :show-overflow-tooltip="true" label="单位" align="center" prop="unit" />
          <el-table-column :show-overflow-tooltip="true" label="报警上限阀值" align="center" prop="alarmMax" />
          <el-table-column :show-overflow-tooltip="true" label="报警下限阀值" align="center" prop="alarmMin" />

          <el-table-column label="说明" align="center" prop="note">
            <template  slot-scope="scope">

              <el-input size="small" v-model="scope.row.note"  
              placeholder="请输入说明" ></el-input>
              
            </template>
          </el-table-column>

      </el-table>

      </el-form>
   

  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'

  import {validatePhone} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById, getDetailsByZHId} from "../api/moniterTB";
  import {getListNoPage} from "../api/moniterset";

  export default {
    // 新建编辑
    name: 'monitersetCreate',
    components: {
      XrCreate,
      CreateSections,
    },

    props: {
      action: {
        type: Object,
        default: () => {
          return {
            type: 'save',
            id: '',
            monitType:'',
            data: {}
          }
        }
      },
      titleName:''
    },
    data() {
      return {
        loading: false,
        form: {
          dataDate:this.initDataSel(),
        },
        list: [],
        tableHeight: document.documentElement.clientHeight, // 表的高度
        rules: {
          code: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          dataDate: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          phone: [
            {required: true, message: '必填项不得为空', trigger: 'blur'},
            { validator: validatePhone, trigger: 'blur'}
          ]
        },
      }
    },
    computed: {
      title() {
        if(this.action.type === 'show'){
            return this.titleName
        }else{
            return this.action.type === 'update' ? '编辑'+this.titleName : '新建'+this.titleName
        }
        
      },
      disabledT() {
        return this.action.type === 'show' ? true : false
      },

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)

      if (this.action.type == "save") {
        this.getList()
      }else{
         this.getDetail()
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
        getById(this.action.id,this.action.monitType)
          .then(res => {
            this.form = res.data;
          
            getDetailsByZHId(this.action.id,this.action.monitType).then(res =>{
              this.list = res.data
              console.log(this.list)
            })

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
            var param = new FormData()
            var s1 = JSON.stringify(this.form)
            var s2 = JSON.stringify(this.list)

            param.append(
              'ZHUB',
              s1
            ) 
            param.append(
              'ZIB',
              s2
            ) 

            param.append(
              'type',
              this.action.monitType
            )

            this.loading = true;

            saveOrUpdate(param,this.action.monitType).then(response => {
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

       /**
       * 通过回调控制class
       */
      cellClassName({row, column, rowIndex, columnIndex}) {
        if (column.property === 'code') {
          return 'can-visit--underline can-visit--bold'
        }
      },

      headerCellClassName({row, column, rowIndex, columnIndex}) {
        /*if (column.property === 'code') {
          return 'header-can-visit-backgroud'
        }*/
        return ''
      },
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
        //console.log(row, column)
        return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
      },

      //获取监测项
      getList(){
        let queryParams = {}
        queryParams.type = this.action.monitType

        getListNoPage(queryParams)
          .then(res => {
            this.list = res.data;
            this.loading = false
          })
          .catch(() => {
            this.loading = false
          })
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
  #crm-table{
    height: 600px!important;
  }
</style>
