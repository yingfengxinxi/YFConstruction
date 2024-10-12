<template>
  <el-dialog
    :title="title"
    :visible="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    :close-on-click-modal="false"
    class="create-dialog"
    @close="hidenView">

      <el-table ref="menuTable" 
      :data="tableData" 
      @cell-click="cellClick"
      style="width: 100%" 
      :height="tableHeight" 
      border v-loading="loading" 
      :cell-class-name="getRowColumn"
      highlight-current-row >
        
        <el-table-column type="index" align="center" label="序号" width="70"></el-table-column>

        <el-table-column prop="name" align="center" label="任务名称"></el-table-column>
        <el-table-column prop="relatedBuildingId" align="center" label="单体楼座" width="120" :formatter="relatedBuildingIdFormat"></el-table-column>
        <el-table-column prop="typeId" align="center" label="种类" width="140">
            <template  slot-scope="scope">
                <el-select
                    v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex "
                    v-model="scope.row.typeId"
                    placeholder="请选择"
                    clearable
                    size="small"
                    style="width: 120px">
                    <el-option
                            v-for="dict in typeDict"
                            :key="dict.dictValue"
                            :label="dict.dictLabel"
                            :value="dict.dictValue" />
                </el-select>
                <span v-else>{{ifMilestoneFormat(scope.row)}}</span>
            </template>
        </el-table-column>
        <el-table-column prop="dayFinish" align="center" label="当日完成量">
            <template  slot-scope="scope">
                <el-input v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex " size="small" v-model="scope.row.dayFinish" 
                    placeholder="请输入">
                </el-input>
                <span v-else>{{scope.row.dayFinish}}</span>
            </template>
        </el-table-column>
        <el-table-column prop="unit" align="center" label="单位">
            <template  slot-scope="scope">
                <el-input v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex " size="small" v-model="scope.row.unit" 
                    placeholder="请输入">
                </el-input>
                <span v-else>{{scope.row.unit}}</span>
            </template>
        </el-table-column>
        <el-table-column prop="dayDate" align="center" label="日期"></el-table-column>
			
		</el-table>

    <span
        slot="footer"
        class="dialog-footer">
        <el-button
          type="primary"
          @click.native="saveInfo">保 存</el-button>
      <el-button @click.native="hidenView">取 消</el-button>
    </span>

  </el-dialog>
</template>
<script type="text/javascript">

  import {saveDayFillin , selectByMap} from '@/views/build/progress/api/planDayFillin'
	import {selectLoft } from '@/views/build/progress/api/planCreate'

  export default {
    name: 'CreateMenu', // 所有新建效果的view
    components: {//声明组件
       
    },
    filters: {//过滤器
    },
    props: {//属性
     form:{},
     
    },
    
    data() {
      return {
        loading: true,
        tableHeight: document.documentElement.clientHeight - 450, // 表的高度

        typeDict:[],//种类
        loftList:[],//单体楼座

        tableData:[],

        clickCellRowIndex:null,//用于判断点击是哪行
		clickCellColumnIndex:null,//用于判断点击是哪列
        
      }
    },
    created(){
      
      //  种类
	  this.getDicts('b_progress_day_fillin_type').then(response => {
			this.typeDict = response.data
      })

      this.selectLoft()

      this.selectByMap()

      
      this.loading = false
    },
    computed: {//监听属性
      title() {
        return '每日填报';
      },
      

    },
    mounted() {//渲染完成后
      
    },
    destroyed() {//销毁后
    },
    watch:{

    },
    methods: {//声明方法

        //输入框 失去焦点
        inputBlur(index,row){
            this.clickCellRowIndex = null
            this.clickCellColumnIndex = null

        },
        //点击cell
        cellClick(row,column,cell,event){
            this.clickCellRowIndex = row.index
            this.clickCellColumnIndex = column.index

        },
        //table初始化的时候给行和列 赋index
        getRowColumn({row,column,rowIndex,columnIndex}){
            row.index = rowIndex+1
            column.index = columnIndex+1
        },

      //根据周计划id获取每日填报数据
	    selectByMap() {

            let map = {planWeekId : this.form.id }
            selectByMap(map).then(res => {
                
                this.tableData = res.data;
                
            }).catch(() => {
                this.loading = false
            })

        
		},
      
      saveInfo() {

        this.loading = true;

        saveDayFillin(this.tableData)
            .then(res => {
            this.msgSuccess('编辑成功')
            this.$emit('save');
            this.hidenView();
            this.loading = false;
            })
            .catch(() => {
            this.loading = false
            })

      },

        //查询 单体楼座下拉 
        selectLoft(){
            selectLoft().then(res=>{
                
                this.loftList = res.data.loftList
                //console.log(this.loftList)

            })
        },
        
        //关联楼座 -- table显示name
			relatedBuildingIdFormat(row, column){
				let value = row.relatedBuildingId
				//console.log(value)
				let datas = this.loftList

				if(datas == undefined || datas == null){
					return null
				}

				var actions = []
				if(value == null || value == undefined){
					return null
				}else{

					Object.keys(value).some((index) => {
							Object.keys(datas).some((key) => {
								if (datas[key].id == value[index] ) {
								actions.push(datas[key].building)
								actions.push(',')
								return true
								}
							})
					})

					return actions.join('')
				}
				

			},

        //种类
        ifMilestoneFormat(row, column){
            return this.selectDictLabel(this.typeDict, row.typeId)
        },

      hidenView() {
        this.$emit('hiden-view')
      },
      
    }
  }
</script>
<style lang="scss" scoped>
  .crm-create-container {
    position: relative;
    height: 100%;
  }

  .crm-create-flex {
    position: relative;
    overflow-x: hidden;
    overflow-y: auto;
    flex: 1;
  }

  .crm-create-header {
    height: 40px;
    margin-bottom: 20px;
    padding: 0 10px;
    flex-shrink: 0;
    .close {
      display: block;
      font-size: 24px;
      color: #909399;
      margin-right: -10px;
      padding: 10px;
      cursor: pointer;
    }
    .close:hover {
      color: $xr-color-primary;
    }
  }

  .create-name {
    font-size: 14px;
    color: #333;
    font-weight: 600;
    padding-left: 12px;
    position: relative;
    margin-left: 15px;
    margin-bottom: 10px;
  }

  .create-name::before {
    content: ' ';
    position: absolute;
    top: 0;
    left: 0;
    height: 14px;
    border-radius: 2px;
    width: 4px;
    background-color: $xr-color-primary;
  }

  .crm-create-body {
    flex: 1;
    overflow-x: hidden;
    overflow-y: auto;
  }

  /** 将其改变为flex布局 */
  .crm-create-box {
    display: flex;
    flex-wrap: wrap;
    padding: 0 20px;
  }

  .crm-create-item {
    flex: 0 0 50%;
    flex-shrink: 0;
    padding-bottom: 10px;
  }

  // 占用一整行
  .crm-create-block-item {
    flex: 0 0 100%;
    flex-shrink: 0;
    padding-bottom: 10px;
  }

  .el-form-item /deep/ .el-form-item__label {
    font-size: 13px;
    color: #333333;
    position: relative;
    padding-left: 8px;
    padding-bottom: 0;
  }

  .el-form /deep/ .el-form-item {
    margin-bottom: 0px;
  }


  .form-label {
    margin: 5px 0;
    font-size: 13px;
    word-wrap: break-word;
    word-break: break-all;
  }

  .examine-items {
    padding: 10px 0;
  }

  .examine-item {
    padding-bottom: 8px;
    .examine-item-name {
      width: 60px;
      padding-left: 20px;
      font-size: 13px;
      margin-right: 20px;
    }
    .examine-item-select {
      margin-right: 20px;
    }
    .examine-item-user {
      flex: 1;
      margin-right: 42px;
    }
    .examine-item-delete {
      color: #ff6767;
      font-size: 22px;
      margin: 0 10px;
      display: none;
    }
  }

  .examine-item:hover {
    .examine-item-delete {
      display: block;
    }
    .examine-item-user {
      margin-right: 0px;
    }
  }

  .examine-items-add {
    padding: 5px 0 20px 0;
    font-size: 13px;
    color: $xr-color-primary;
  }

  .examine-add-des {
    font-size: 12px;
    background-color: #fffcf0;
    padding: 10px;
    line-height: 23px;
    color: #999;
    margin-bottom: 10px;
    .examine-add-required {
      color: #ff6767;
    }
  }

  .handle-bar {
    position: relative;
    .handle-button {
      float: right;
      margin-top: 5px;
      margin-right: 20px;
    }
  }

  .form
  /deep/
  .el-date-editor, .el-select{
    width: 100%;
  }


</style>
