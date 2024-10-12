<template>
  <xr-create
    :loading="loading"
    :title="title"
    :showSave="false"
    @close="close">
    
      <el-form ref="form" :model="form" :disabled="true" :rules="rules" label-position="top" label-width="100px"
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
        <el-row>
          <el-col :span="8">
            <el-form-item label="取样人员" prop="samplerName">
              <el-input v-model="form.samplerName" placeholder="请输入取样人员"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="见证人员" prop="witnessName">
              <el-input v-model="form.witnessName" placeholder="请输入见证人员"></el-input>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="8">
            <el-form-item label="经度" prop="longitude">
              <el-input v-model="form.longitude" placeholder="请输入经度"></el-input>
            </el-form-item>
          </el-col> -->
          
          <el-col :span="8">
            <el-form-item label="填报地点">
              <button @click="clickBaiduMap" style="color:blue;width:100px;height:34px;">点击查看地图</button>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="取样人员照片" >
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="form.samplerImgUrl" style="width:100px;height:100px;">
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="见证人员照片" >
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="form.witnessImgUrl" style="width:100px;height:100px;">

            </el-form-item>
          </el-col>
          <!-- <el-col :span="8">
            <el-form-item label="纬度" prop="latitude">
              <el-input v-model="form.latitude" placeholder="请输入纬度"></el-input>
            </el-form-item>
          </el-col> -->
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
          <el-table-column :show-overflow-tooltip="true" label="监测项编号" align="center" prop="code" />
          <el-table-column :show-overflow-tooltip="true" label="监测项名称" align="center" prop="name" />

          <el-table-column label="监测采集值" align="center" prop="sValue">
            <template  slot-scope="scope">

               <span 
                v-if="scope.row.alarmMax && scope.row.sValue>Number(scope.row.alarmMax)" 
                style="color:red;">{{scope.row.sValue}}</span>
                <span 
                v-else-if="scope.row.alarmMin && scope.row.sValue<Number(scope.row.alarmMin)" 
                style="color:red;">{{scope.row.sValue}}</span>
                <span
                v-else style="color:black;">{{scope.row.sValue}}</span>
              
            </template>
          </el-table-column>

          <el-table-column :show-overflow-tooltip="true" label="单位" align="center" prop="unit" />
          <el-table-column :show-overflow-tooltip="true" label="报警上限阀值" align="center" prop="alarmMax" />
          <el-table-column :show-overflow-tooltip="true" label="报警下限阀值" align="center" prop="alarmMin" />
          <el-table-column :show-overflow-tooltip="true" label="说明" align="center" prop="note" />

      </el-table>

      </el-form>
        
      <!-- 检测检验 地图 -->
    <jbaidu-map
      v-if="mapShow"
      :action="action"
      @close="mapShow = false"
      @save-success="getDetail"
    />

  </xr-create>
  
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import jbaiduMap from './JBaiduMap'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById, getDetailsByZHId} from "../api/moniterTB";
  import {getListNoPage} from "../api/moniterset";
  
  export default {
    // 新建编辑
    name: 'monitersetCreate',
    components: {
      XrCreate,
      CreateSections,
      jbaiduMap
    },

    props: {
      id:'',
      titleName:'',
      monitType:''
    },
    data() {
      return {
        loading: false,
        form: {
        },
        list: [],
        tableHeight: 320, // 表的高度
        rules: {
          code: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          dataDate: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          phone: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          samplerName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          witnessName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],

        },
        mapShow:false,
        action:{},

      }
    },
    computed: {
      title() {
       return this.titleName
        
      }

    },
    watch: {},
    created() {
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
        getById(this.id,this.monitType)
          .then(res => {
            this.form = res.data;
          
            getDetailsByZHId(this.id,this.monitType).then(res =>{
              this.list = res.data
            })

          })
          .catch(() => {
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
        queryParams.type = this.monitType

        getListNoPage(queryParams)
          .then(res => {
            this.list = res.data;
            this.loading = false
          })
          .catch(() => {
            this.loading = false
          })
      },

      //点击跳转地图
      clickBaiduMap(){
        this.mapShow = true
        this.action.longitude = this.form.longitude
        this.action.latitude = this.form.latitude

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
  /*#crm-table{*/
  /*  height: 480px!important;*/
  /*}*/
</style>
