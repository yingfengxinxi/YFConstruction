<template>
  <div class="main">
<!--     <xr-header-->
      <!--            icon-class="wk wk-project"-->
      <!--            icon-color="#2362FB"-->
      <!--            label="花名册" />-->
    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" class="topFormClass"  :model="queryParams" :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">

        <el-form-item  label="分包单位" class='special' prop="subcontractor" >
            <el-select
                    v-model="queryParams.subcontractor"
                    placeholder="请选择"
                    clearable
                    size="small"
                    style="width: 160px">
              <el-option
                      v-for="dict in FBSList"
                      :key="dict.realName"
                      :label="dict.realName"
                      :value="dict.id" />
            </el-select>
        </el-form-item>
		<!-- 劳务工种改为字典项  workTypeDict-->
        <el-form-item  label="劳务工种" class='special' prop="workTypeDict" >
            <el-select
                    v-model="queryParams.workTypeDict"
                    placeholder="请选择"
                    clearable
                    size="small"
                    style="width: 160px">
              <el-option
                      v-for="dict in workTypeOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue" />
            </el-select>
        </el-form-item>

        <el-form-item label="人员姓名" prop="name">
          <el-input
                  v-model="queryParams.name"
                  placeholder="请输入人员姓名"
                  clearable
                  size="small"
                  style="width: 160px"
                  @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input
                  v-model="queryParams.idCard"
                  placeholder="请输入身份证号"
                  clearable
                  size="small"
                  style="width: 160px"
                  @keyup.enter.native="handleQuery"
          />
        </el-form-item>

          <el-form-item label="是否零工" prop="ifOddjob"  >
            <el-select
                    v-model="queryParams.ifOddjob"
                    placeholder="请选择"
                    clearable
                    size="small"
                    style="width: 160px">
              <el-option
                      v-for="dict in ifOddjobOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue" />
            </el-select>
          </el-form-item>

        <!-- <el-form-item label="进场启日" prop="enterDate">
            <el-date-picker
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              v-model="form.enterDate"
              type="date"
              placeholder="选择日期" >
            </el-date-picker>
          </el-form-item>

          <el-form-item label="进场止日" prop="enterDate">
            <el-date-picker
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              v-model="form.enterDate"
              type="date"
              placeholder="选择日期" >
            </el-date-picker>
          </el-form-item> -->

           <!-- <el-form-item label="退场启日???" prop="expirationDate">
            <el-date-picker
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              v-model="form.expirationDate"
              type="date"
              placeholder="选择日期" >
            </el-date-picker>
          </el-form-item>

          <el-form-item label="退场止日???" prop="expirationDate">
            <el-date-picker
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              v-model="form.expirationDate"
              type="date"
              placeholder="选择日期" >
            </el-date-picker>
          </el-form-item> -->

          <!-- <el-form-item label="筛选" prop="sexOptions"  >
            <el-select
                    v-model="queryParams.sexOptions"
                    placeholder="请选择"
                    clearable
                    size="small"
                    style="width: 160px">
              <el-option
                      v-for="dict in sexOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue" />
            </el-select>
          </el-form-item>  -->


        <el-form-item style="float: right;margin-right: 10px!important;">
        	          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          <el-button type="primary" icon="el-icon-printer" size="mini" @click="exportFile(1)" >导出</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :height="tableHeight" :data="dataList" @sort-change="sortChange" >
        <el-table-column label="序号" type="index"   align='center' width="70"></el-table-column>
        <el-table-column label="头像" align="center" width="120px">
          <template slot-scope="scope">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="scope.row.imgCardUrl" style="height: 53px"/>
          </template>
        </el-table-column>
        <el-table-column label="人员姓名" align="center" prop="name"  width="120px" sortable="custom"/>
        <!-- <el-table-column label="提交平台" align="center" prop="uploadGovernmentPlatform" width="200px" :formatter="YNSCFormat"/> -->
<!--        <el-table-column label="是否上传" align="center" prop="uploadGovernmentPlatform" width="200px" :formatter="YNSCFormat" />-->
        <el-table-column label="性别" align="center" prop="sex" :formatter="sexFormat" sortable="custom"/>
        <el-table-column label="身份证号" align="center" prop="idCard" width="160px" sortable="custom"/>
        <el-table-column label="民族" align="center" prop="nation" :formatter="nationFormat" sortable="custom"/>
        <el-table-column label="出生日期" align="center" prop="birthday" width="100px" sortable="custom" />
        <el-table-column label="电话号码" align="center" prop="phone" width="120px" sortable="custom"/>
        <el-table-column label="家庭住址" align="center" prop="address" width="200px" show-overflow-tooltip sortable="custom"/>

<!--        <el-table-column label="发证机关" align="center" prop="issuingAuthority" width="160px"/>-->
<!--        <el-table-column label="有效期启" align="center" prop="dateStart" width="100px" />-->
<!--        <el-table-column label="有效期止" align="center" prop="dateEnd" width="100px" />-->
        <!-- <el-table-column label="人员编号??" align="center" prop="workTypeIfspecial"  width="200px" /> -->
        <el-table-column label="进场时间" align="center" prop="enterDate" width="100px" sortable="custom" />

        <el-table-column label="分包单位" align="center" prop="realNameName" width="200px" sortable="custom" />
        <el-table-column label="劳务工种" align="center" prop="workTypeName" width="120px" sortable="custom" />
        <!-- <el-table-column label="人员状态???" align="center" prop="workTypeJp" width="200px" /> -->
        <el-table-column label="班组名" align="center" prop="teamNameName" width="150px" show-overflow-tooltip sortable="custom"/>
        <el-table-column label="是否班组长" align="center" prop="ifTeamLeader" width="120px" :formatter="ifTeamLeaderFormat" sortable="custom"/>

        <el-table-column label="计价方式" align="center" prop="valuationMethod" width="120px" :formatter="valuationMethodFormat" sortable="custom"/>
        <!-- <el-table-column label="技术等级" align="center" prop="cerLevel" width="200px" :formatter="cerLevelFormat"/> -->
        <!-- <el-table-column label="上岗证号??" align="center" prop="workTypeJp" width="200px" />
        <el-table-column label="上岗证至??" align="center" prop="workTypeIfspecial" width="200px"/> -->
        <el-table-column label="合同签订日" align="center" prop="signingDate" width="120px" sortable="custom"/>

        <el-table-column label="是否零工" align="center" prop="ifOddjob" width="120px" :formatter="ifOddjobFormat" sortable="custom"/>
        <!-- <el-table-column label="退场时间??" align="center" prop="workTypeId" width="200px"/> -->
<!--        <el-table-column label="开户银行" align="center" prop="bankAccount" width="200px"  :formatter="bankAccountFormat"/>-->
<!--        <el-table-column label="银行账号" align="center" prop="bankCardNumber" width="200px" />-->

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="260px" fixed="right">
          <template slot-scope="scope">
            <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-collection"
                    @click="handleProjectLists(scope.row)"
            >项目履历</el-button>
            <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-document"
                    @click="handlePingJia(scope.row)"
            >评价</el-button>
            <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <el-pagination
                :current-page="currentPage"
                :page-sizes="pageSizes"
                :page-size.sync="pageSize"
                :total="total"
                class="p-bar"
                background
                layout="prev, pager, next, sizes, total, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"/>
      </div>
      <!-- 新增或修改参数配置对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="1200px" high="800px" append-to-body>
        <div v-if="ifOddJob == '0'">
          <infocollUpdate ref="updatainfo" :UID="Uid">
         </infocollUpdate>
        </div>
        <div v-else>
         <oddJobcollUpdate ref="updatainfo" :UID="Uid">
         </oddJobcollUpdate>
        </div>

        <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>

      </el-dialog>

      <!-- 项目履历 -->
      <projectLists
          v-if="projectListsFlag"
          :idCard="idCard"
          :projectId="projectId"
          @close="projectListsFlag = false"
       />
      <!-- 评价 -->
      <pingJia
          v-if="pingJiaFlag"
          :id="Uid"
          @close="pingJiaFlag = false"
          @save-success="getList"
       />

    </div>
  </div>
</template>

<script>

  // import Reminder from '@/components/Reminder'
  import XrHeader from '@/components/XrHeader'
  import request from '@/utils/request'
  import SelectCol from "@/views/admin/components/selectCol";
  import infocollUpdate from '@/views/build/labour/roster/infocollUpdate'
  import oddJobcollUpdate from '@/views/build/labour/roster/oddJobcollUpdate'

  import {getWTList,getListByFBS, getListByBZ} from '@/api/build/infocollection/infocoll'
  import {crmFileSaveAPI, crmFileRemoveByBatchIdAPI} from '@/api/common'

  import pingJia from '@/views/build/labour/roster/pingJia'
  import projectLists from '@/views/build/labour/roster/projectLists'

import { exportFile } from "@/api/build/exportFile";
import { downloadExcelWithResData,sortColumn } from '@/utils/index';

  export default {
    name: 'roster',
    components: {
      infocollUpdate,
      oddJobcollUpdate,
      XrHeader,
      SelectCol,
      projectLists,
      pingJia
    },
    data() {
      return {
        // 遮罩层
        loading: true,
				tableHeight: document.documentElement.clientHeight - 280, // 表的高度
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 分页
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 30, 40],
        total: 0,
        // 表格数据
        dataList: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,

        // 查询参数
        queryParams: {
          page: this.currentPage,
          limit: this.pageSize
        },

        // 工种数据
        worktypeList: [],
		workTypeOptions: [],
        // 分包商数据信息
        FBSList: [],
        // 班组数据信息
        BZList: [],
        //性别
        sexOptions: [],
        //民族
        nationOptions:[],
        //是否
        ynOptions: [],
        //文化程度
        educationLevelOptions:[],
        //计价方式
        valuationMethodOptions: [],
        //政治面貌
        politicalStatusOptions:[],
        //开户银行
        bankAccountOptions: [],

        //证书种类
        cerCategoryOptions:[],
        //证书类型
        cerCategoryTypeOptions:[],
        //证书类型_
        cerCategoryTypeOptions_:[],
        //证书等级
        cerLevelOptions:[],
        // 劳动合同
        laborContractOptions:[],
        // 进场状态
        enterStatusOptions:[],
        //修改ID
        Uid : "",
        //是否是零工，判断修改页面展示哪个
        ifOddJob:false,

        // 表单参数
        form: {},
        batchId:'',

        ifOddjobOptions:[{dictValue:'0',dictLabel:'否'},{dictValue:'1',dictLabel:'是'}],

        projectListsFlag:false,//项目履历页面是否展示
        pingJiaFlag:false,//员工评价页面是否展示
        projectId:""

      }
    },
    created() {
      this.getList()


      //  性别字典项
      this.getDicts('sys_user_sex').then(response => {
        this.sexOptions = response.data
      })
      //  民族字典项
      this.getDicts('b_nation').then(response => {
        this.nationOptions = response.data
      })
      //  是否字典项
      this.getDicts('sys_yes_no').then(response => {
        this.ynOptions = response.data
      })
      //  进场 劳动合同 字典项
      this.getDicts('b_laborContract').then(response => {
        this.laborContractOptions = response.data
      })
      //  文化程度字典项
      this.getDicts('b_educationLevel').then(response => {
        this.educationLevelOptions = response.data
      })
      //  计价方式字典项
      this.getDicts('b_valuation_method').then(response => {
        this.valuationMethodOptions = response.data
      })
      //  政治面貌 字典项
      this.getDicts('b_politicalStatus').then(response => {
        this.politicalStatusOptions = response.data
      })
      //  开户银行 字典项
      this.getDicts('b_bankAccount').then(response => {
        this.bankAccountOptions = response.data
      })

      //  证书种类 字典项
      this.getDicts('b_cerCategory').then(response => {
        this.cerCategoryOptions = response.data
      })
      //  证书类型 字典项
      this.getDicts('b_cerCategoryType').then(response => {
        this.cerCategoryTypeOptions = response.data
      })
      //  证书等级 字典项
      this.getDicts('b_cerLevel').then(response => {
        this.cerLevelOptions = response.data
      })
      //  进场状态
      this.getDicts('b_enterStatus').then(response => {
        this.enterStatusOptions = response.data
      })
      //分包商
      this.getListByFBS()
      //班组名称
      this.getListByBZ()
         //工种数据
      //this.getWorktypeList()
	  // 工种字典
      this.getDicts('b_labour_work_type').then(response => {
        this.workTypeOptions = response.data
      })
    },
    watch: {

		},
    methods: {
    	sortChange(column) {
	      sortColumn(this,column);
	    },
      /** 查询数据列表 */
      getList() {
        this.loading = true
        this.queryParams.page = this.currentPage
        this.queryParams.limit = this.pageSize

        request({
          url: 'build/labourInfocollection/list',
          method: 'post',
          data: this.queryParams,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          this.dataList = res.data.list
          // console.log(this.dataList)
          if (res.data.totalRow && Math.ceil(res.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1) {
              this.currentPage = this.currentPage - 1
              this.getList()
          } else {
            this.total = res.data.totalRow
            this.loading = false
          }

          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },
       //是否上传
      YNSCFormat(row, column){
        return this.selectDictLabel(this.ynOptions, row.uploadGovernmentPlatform)
      },
      //分包单位
      // FBSFormat(row, column){
      //   var datas  = this.FBSList
      //   var value = row.subcontractor
      //   var actions = []

      //   Object.keys(datas).some((key) => {
      //     if (datas[key].id == ('' + value)) {
      //       actions.push(datas[key].realName)
      //       return true
      //     }
      //   })
      //   return actions.join('')
      // },
      //劳务工种
      // WTFormat(row, column){
      //   var datas  = this.worktypeList
      //   var value = row.workTypeId
      //   var actions = []

      //   Object.keys(datas).some((key) => {
      //     if (datas[key].id == ('' + value)) {
      //       actions.push(datas[key].workType)
      //       return true
      //     }
      //   })
      //   return actions.join('')
      // },
      // 是否班组长
      ifTeamLeaderFormat(row, column){
         return this.selectDictLabel(this.ynOptions, row.ifTeamLeader)
      },
      //计价方式  零工
      valuationMethodFormat(row, column){
         return this.selectDictLabel(this.valuationMethodOptions, row.valuationMethod)
      },
      //技术等级  证书等级
      cerLevelFormat(row, column){
        return this.selectDictLabel(this.cerLevelOptions, row.cerLevel)
      },
      //班组
      // teamNameFormat(row, column){
      //   var datas  = this.BZList
      //   var value = row.teamName
      //   var actions = []

      //   Object.keys(datas).some((key) => {
      //     if (datas[key].id == ('' + value)) {
      //       actions.push(datas[key].teamName)
      //       return true
      //     }
      //   })
      //   return actions.join('')

      // },

       //性别
      sexFormat(row, column){
        return this.selectDictLabel(this.sexOptions, row.sex)
      },
       //民族
      nationFormat(row, column){
        return this.selectDictLabel(this.nationOptions, row.nation)
      },
      //开户银行
      bankAccountFormat(row, column){
        return this.selectDictLabel(this.bankAccountOptions, row.bankAccount)
      },
      //是否零工
      ifOddjobFormat(row, column){
        if(row.ifOddjob == '0'){
          return '否'
        }else{
          return '是'
        }
      },
       /** 查询所工种数据列表 */
      getWorktypeList() {
        getWTList().then(response => {
          this.worktypeList = response.data
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },

      /** 查询所有分包商id和name */
      getListByFBS() {
         getListByFBS().then(response => {
          this.FBSList = response.data
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },

      /** 查询所有班组名称 */
      getListByBZ() {
        getListByBZ().then(response => {
          this.BZList = response.data
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },


      // 取消按钮
      cancel() {
        //this.$refs.updatainfo.batchId = "-1"
        this.open = false
        this.Uid = ""
        // this.reset()
      },
      // 表单重置
      reset() {
        this.form = {}
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.page = 1
        this.queryParams.limit = this.pageSize
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },

      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        this.Uid = row.id
        this.ifOddJob = row.ifOddjob
        this.open = true
        this.title = '信息修改'
      },
      //项目履历
      handleProjectLists(row){
        this.idCard = row.idCard
        this.projectId = row.projectId
        this.projectListsFlag = true
      },
      //评价
      handlePingJia(row){
        this.Uid = row.id
        this.pingJiaFlag = true
      },
      /** 提交按钮 */
      submitForm: function() {
        let imgInfo = this.$refs.updatainfo.editedImage
        let systemImage = this.$refs.updatainfo.systemImage
        let bat = this.$refs.updatainfo.batchId

        if (!systemImage) {
          this.$message.error('身份证头像不能为空')
          return false
        }else{
          if(imgInfo){
            var data1 = {batchId: bat}
            //根据批次号删除就图片数据
            crmFileRemoveByBatchIdAPI(data1).then(response =>{

                if (response.code === 0) {
                    var data = {type: 'img', batchId: bat, file: imgInfo.blob};
                    //生成新的图片数据
                    crmFileSaveAPI(data).then(response => {
                      if (response.code === 0) {
                        this.$refs.updatainfo.form.imgCardUrl = response.data.url

                        this.submitFormatter()

                      }else{
                        this.$message.error('身份证头像上传失败')
                        return false
                      }
                    })
                }else{
                  this.$message.error('身份证头像上传失败')
                  return false
                }
            })
          }else{
            this.submitFormatter()
          }
        }
      },
      submitFormatter(){
        let url1 = 'build/labourInfocollection/addOddJob'//零工信息采集
        if(this.ifOddJob == '0'){
            url1 = 'build/labourInfocollection/add'//信息采集
        }

        if(this.$refs.updatainfo.submitForm()){
          request({
              url: url1,
              method: 'post',
              data: this.$refs.updatainfo.show,
              async: false,
              headers: {
                'Content-Type': 'application/json;charset=UTF-8'
              }
            }).then(response => {
              if (response.code === 0) {
                this.msgSuccess('修改成功')
                //this.$refs.updatainfo.batchId = "-1"
                this.open=false
                this.$forceUpdate()
                this.getList()
              }
            })

        }
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const id = row.id || this.ids
        const name = row.name
        this.$confirm('是否确认删除人员为【"' + name + '"】的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return request({
            url: 'build/labourInfocollection/delete/' + id,
            method: 'post'
          })
        }).then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        }).catch(function() {})
      },

      // /** 导出按钮操作 */
      // handleExport() {
      //   this.download('system/dict/type/export', {
      //     ...this.queryParams
      //   }, `type_${new Date().getTime()}.xlsx`)
      // },
      // 更改每页展示数量
      handleSizeChange(val) {
        this.pageSize = val
        this.getList()
      },
      // 更改当前页数
      handleCurrentChange(val) {
        this.currentPage = val
        this.getList()
      },
      /**
		 *
		 * 使用导出需引入js文件如下：
		 * import { exportFile } from "@/api/build/exportFile";
		 * import { downloadExcelWithResData } from '@/utils/index';
		 * 方法入参说明： type=1 全部导出  type=2 部分导出
		 *
		 */
		async  exportFile (type) {
			this.loading=true;
			let data = '';
			if (type == 1) {
				//请求不分页的列表数据 需替换为当前页面列表数据请求url
				var objString = JSON.stringify(this.queryParams);
				var obj2 = JSON.parse(objString);
				let reqParam = obj2;
				reqParam.pageType=0
				await request({
          url: 'build/labourInfocollection/list',
          method: 'post',
          data: reqParam,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          data = res.data.list
        }).catch(() => {
          this.loading = false
        })

			} else if (type == 2) {
				//获取当前选中行的数据
				data=this.selectionList;
			}
			/**
			 * 整理数据
			 * param中result是需要导出的数据(必填)
			 * param中fieldList是导出Excel的表头(可选)，默认当前表格的全部表头，格式及字段名参照this.fieldList(必填)
			 * 如果有字典需转换请添加dictType类型，
       * 如果字典是本页自定义的，请添加dictList，并添加所有字典项(注意：dictType,dictList这两个字段非必填)，
       * dictList中字段名必须严格设置为dictLabel，dictValue，如果不是，请处理
			 * param中title是导出Excel的一级表头(必填)
       * 注意：fieldList中字段值必须和列表中字段值相对应！！！
			 */
      let fbsList='';
      let  fieldList= [ //表头
				/*{ prop: "imgCardUrl", label: "头像"},*/
				{ prop: "name", label: "人员姓名"},
				{ prop: "sex", label: "性别",dictType:"sys_user_sex"},
				{ prop: "idCard", label: "身份证号"},
				{ prop: "nation", label: "民族" ,dictType:"b_nation"},
				{ prop: "birthday", label: "出生日期" },
				{ prop: "phone", label: "电话号码" },
				{ prop: "address", label: "家庭住址" },
				{ prop: "enterDate", label: "进场时间" },
        { prop: "realNameName", label: "分包单位" ,},
				{ prop: "workTypeName", label: "劳务工种" ,},
				{ prop: "teamNameName", label: "班组名" ,},
				{ prop: "ifTeamLeader", label: "是否班组组长" ,dictType:"sys_yes_no"},
        { prop: "valuationMethod", label: "计价方式" ,dictType:"b_valuation_method"},
				{ prop: "signingDate", label: "合同签订日" },
				{ prop: "ifOddjob", label: "是否零工" , dictList:[{"dictLabel":"是","dictValue":"1"},{"dictLabel":"否","dictValue":"0"}]},
			]
			let param = {};
			param.result = data;
			param.fieldList = fieldList;
			param.title = "花名册";
			await exportFile(param)
				.then(res => {
					// console.log(res);
					downloadExcelWithResData(res);
					this.loading=false;
				})
				.catch(err => {
					// console.log(err)
					this.$message.error("导出文件生成异常");
					this.loading=false;
				})
		}
    }
  }
</script>


<style lang="scss" scoped>
  .main {
    height:100%;
  }
  /deep/ .xr-header {
    padding: 15px 30px;
  }

  .main-body {
    background-color: white;
    /*border-top: 1px solid $xr-border-line-color;*/
    border-bottom: 1px solid $xr-border-line-color;
  }

  .main-table-header-button {
    float: right;
    margin-right: 20px;
    margin-top: 10px;
  }


  .add-form::after{
    content: "";
    display: block;
    clear: both;
  }

  .link-type,.link-type:focus{color:#337ab7;cursor:pointer}

  .link-type:focus:hover,.link-type:hover{color:#20a0ff}

  .el-table {
    border-top: 1px solid $xr-border-line-color;
  }
	/deep/ .special .el-form-item__label{
		width: 100px!important;
	}
	/deep/ .el-dialog{
  height: 700px;
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    margin: auto;
    margin-top: auto!important;
}
/deep/ .el-dialog .el-dialog__body{
 height: 600px;
    overflow: auto;
}

/deep/ .topFormClass .el-form-item{
  margin-right: 0px!important;
}


</style>
