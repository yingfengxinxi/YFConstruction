<template>
	<div class="main">
		<div class="main-body">

			<div class="main-table-header">

				<el-form v-show="true" ref="queryForm" :model="queryForm" :rules="ruless" :inline="true" label-width="80px"
             style="padding: 0px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">

					<el-form-item  label="开始日期"  class='special' prop="weekStartDate" >
						<el-date-picker
							class="timecom"
							v-model="queryForm.weekStartDate"
							type="date"
							placeholder="请选择"
							align="right"
							value-format="yyyy-MM-dd"
							@change="changeWeekStartDate"
							>
						</el-date-picker>

					</el-form-item>
					<el-form-item  label="结束日期"  class='special' prop="weekEndDate" >
						<el-date-picker
							class="timecom"
							v-model="queryForm.weekEndDate"
							type="date"
							placeholder="请选择"
							align="right"
							value-format="yyyy-MM-dd"
							:disabled="true"
							>
						</el-date-picker>
						<el-button class="main-table-header-button xr-btn--orange" type="primary" icon="el-icon-plus" size="mini" @click="getRootMenuList('1')">生成计划</el-button>
						<el-button class="main-table-header-button xr-btn--blue" type="primary" icon="el-icon-search" size="mini" @click="getRootMenuList('2')">查询计划</el-button>

					</el-form-item>
					<el-form-item style="float: right;">
						<el-button class="main-table-header-button export-btn" v-show="saveFlag" @click="exportExcel">导出</el-button>

						<el-button v-show="menuData != null && menuData.length > 0 " class="main-table-header-button" type="success" icon="el-icon-success" size="mini" @click="savePlan">保存</el-button>

						<el-dropdown v-show="menuData != null && menuData.length > 0 " trigger="click" @command="handleNode" style="margin:2px 10px">
							<el-button icon="el-icon-more" class="t-more" />
							<el-dropdown-menu slot="dropdown">
								<el-dropdown-item
									v-for="(item, index) in moreTypes"
									:key="index"
									:icon="item.icon | wkIconPre"
									:command="item.type"
								>{{ item.name }}
								</el-dropdown-item>
							</el-dropdown-menu>
						</el-dropdown>

					</el-form-item>


				</el-form>
			</div>
			<el-table ref="menuTable" :data="menuData" :height="tableHeight" style="width: 100%" :row-key="rowKey" border
			default-expand-all
			:tree-props="{children: 'children', hasChildren: 'hasChildren'}"
			@row-click="rowClick"
			@cell-click="cellClick"
			highlight-current-row
			:cell-class-name="getRowColumn">
				<!-- <el-table-column type="index" align="center" label="序号" width="60"></el-table-column> -->
				<el-table-column prop="cusIndex" align="left" label="序号">
					<template  slot-scope="scope">
						<span>{{scope.row.cusIndex}}</span>
					</template>
				</el-table-column>

				<el-table-column prop="name" align="left" header-align="center" label="任务名称" min-width="250" class-name="taskName">>
					<template  slot-scope="scope">
						<el-input v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex " size="small" v-model="scope.row.name" @blur="inputBlur"
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.name}}</span>
					</template>
				</el-table-column>

				<el-table-column prop="serialNumber" align="center" label="排序" width="80">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex" size="small" v-model="scope.row.serialNumber"
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.serialNumber}}</span>
					</template>
				</el-table-column>

				<el-table-column prop="planGqiNumber" align="center" label="计划工期(天)" width="100">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex" size="small" v-model="scope.row.planGqiNumber"
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.planGqiNumber}}</span>
					</template>
				</el-table-column>

				<el-table-column prop="relatedBuildingId" align="center" label="关联楼座" width="160" >
					<template  slot-scope="scope">
						<el-select
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex"
							v-model="scope.row.relatedBuildingId"
							placeholder="请选择"
							clearable
							multiple
							size="small"
							style="width: 150px">
							<el-option
									v-for="dict in loftList"
									:key="dict.id"
									:label="dict.building"
									:value="dict.id" />
						</el-select>
						<span v-else>{{relatedBuildingIdFormat(scope.row)}}</span>
					</template>
				</el-table-column>


				<el-table-column prop="planStartDate" align="center" label="计划开始日期" width="160" >
					<template  slot-scope="scope">
						<el-date-picker
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex"
							class="timecom"
							v-model="scope.row.planStartDate"
							type="date"
							placeholder="请选择"
							align="right"
							value-format="yyyy-MM-dd">
						</el-date-picker>
						<span v-else>{{scope.row.planStartDate}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="planEndDate" align="center" label="计划结束日期" width="160" >
					<template  slot-scope="scope">
						<el-date-picker
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex"
							class="timecom"
							v-model="scope.row.planEndDate"
							type="date"
							placeholder="请选择"
							align="right"
							value-format="yyyy-MM-dd">
						</el-date-picker>
						<span v-else>{{scope.row.planEndDate}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="actualStartDate" align="center" label="实际开始日期" width="160" >
					<template  slot-scope="scope">
						<el-date-picker
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex"
							class="timecom"
							v-model="scope.row.actualStartDate"
							type="date"
							placeholder="请选择"
							align="right"
							value-format="yyyy-MM-dd">
						</el-date-picker>
						<span v-else>{{scope.row.actualStartDate}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="actualEndDate" align="center" label="实际结束日期" width="160" >
					<template  slot-scope="scope">
						<el-date-picker
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex"
							class="timecom"
							v-model="scope.row.actualEndDate"
							type="date"
							placeholder="请选择"
							align="right"
							value-format="yyyy-MM-dd">
						</el-date-picker>
						<span v-else>{{scope.row.actualEndDate}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="actualGqiNumber" align="center" label="实际工期(天)" width="100">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex" size="small" v-model="scope.row.actualGqiNumber"
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.actualGqiNumber}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="materialType" align="center" label="材料种类" width="100">
					<template  slot-scope="scope">
						<el-input  v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex" size="small" v-model="scope.row.materialType"
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.materialType}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="planAmount" align="center" label="计划量" width="100">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex" size="small" v-model="scope.row.planAmount"
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.planAmount}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="actualAmount" align="center" label="实际量" width="100">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex" size="small" v-model="scope.row.actualAmount"
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.actualAmount}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="unit" align="center" label="单位" width="100">
					<template  slot-scope="scope">
						<el-input v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex" size="small" v-model="scope.row.unit"
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.unit}}</span>
					</template>
				</el-table-column>

				<el-table-column fixed="right" v-if="saveFlag" align="center" label="操作" width="120">
					<template slot-scope="scope">
						<el-button type="text" icon="el-icon-edit" size="small" @click="handleClick('edit', scope)">每日填报</el-button>

					</template>
				</el-table-column>

			</el-table>

			<dayFillin v-if="dayFillinShow"
					:form="currentRow"
					@save="getRootMenuList('2')"
					@hiden-view="dayFillinShow=false"/>

		</div>


	</div>

</template>

<script>

	import {queryByParentId} from '@/views/build/qual/api/buildManage'

	//根据日期和mid，查询月计划数据生成周计划;
	import {selectByMapForWeekPlan } from '@/views/build/progress/api/planMonth'

	//查询mid
	import {getMaxMinFormLastPlan,selectLoft } from '@/views/build/progress/api/planCreate'

	import {selectPlan,savePlan ,selectByLast ,exportExcel} from '@/views/build/progress/api/planWeek'
	import SelectTree from '@/components/SelectTree'

	import {guid} from '@/utils'
	import { downloadExcelWithResData } from '@/utils/index'

	import dayFillin from './dayFillin'

	export default {
		/** 周计划计划管理 */
		name: 'planWeek',
		components: {
			SelectTree,
			dayFillin
		},

		props:{

		},
		data() {
			return {
				tableHeight:document.documentElement.clientHeight - 220,

				//树型开始
				loading: false, // 加载动画

				saveFlag:true,//是否显示导出按钮
				currentMenuFlag: false, //是否选中某一行
				menuData: [], //table数据
				handleData:[],//保存时，处理后的table数据

				clickCellRowIndex:null,//用于判断点击是哪行
				clickCellColumnIndex:null,//用于判断点击是哪列

				ruless:{
					// mid:[{required: true, message: '查询错误请检查', trigger: 'blur'}],
					// planMonth:[{required: true, message: '必填项不得为空', trigger: 'blur'}],
				},
				//节点操作
				moreTypes: [
				  { type: "addRootNode", name: "新增根节点", icon: "" },
				  { type: "addNode", name: "新增节点", icon: "" },
				  { type: "delNode", name: "删除节点", icon: "" }
				],

				loftList:[],

				//选择的月份
				queryForm:{
					weekStartDate:'',
					weekEndDate:'',
					mid:''
				},
				startMonth:'',//选择月份范围的开始
				endMonth:'',//选择月份范围的结束

				currentRow:{},//当前选中行数据
				dayFillinShow:false,//是否显示每日填报页面

			}
		},
		computed:{

			//月份选择 限制操作
			// pickerOptions(){
			// 	let start = this.startMonth?new Date(this.startMonth).getTime():'';
			// 	let end = this.endMonth?new Date(this.endMonth).getTime():'';
			// 	return{
			// 		disabledDate(time) {
			// 			return time.getTime() < start-8.64e7|| time.getTime() > end;//如果没有后面的-8.64e7就是不可以选择今天的
			// 			}
			// 	}
			// },
		},

		created(){

			//单体楼座
			this.selectLoft()

			// 查询月计划最新的一条数据的mid
			this.selectLastMidFormMonth()

		},
		watch:{

		},
		mounted() {

		},
		methods: {

			//开始日期变化后，给结束日期增加7天
			changeWeekStartDate(data){

				let data2 = this.getNewData(data,6)

				this.queryForm.weekEndDate = data2

			},

			//给指定日期 添加天数
			getNewData(dateT, days) {

				let dateTemp = dateT.split("-");
				let nDate = new Date(dateTemp[0] + '-' + dateTemp[1] + '-' + dateTemp[2]); //转换为MM-DD-YYYY格式
				let millSeconds = Math.abs(nDate) + (days * 24 * 60 * 60 * 1000);
				let rDate = new Date(millSeconds);
				let year = rDate.getFullYear();
				let month = rDate.getMonth() + 1;
				if (month < 10) month = "0" + month;
				let date = rDate.getDate();
				if (date < 10) date = "0" + date;
				return (year + "-" + month + "-" + date);
			} ,


			//行标记
			rowKey(row) {
				return row.planDetailMdId;
			},

			//查询总计划最新的一条数据的mid
			selectLastMidFormMonth(){
				getMaxMinFormLastPlan().then(res=>{

					if(res.data != null && res.data != '' && res.data != undefined){
						this.queryForm.mid = res.data.mid
						//查询此模板最近生成的计划
						this.selectByLast()
					}

				})
			},

			selectByLast(){
				// console.log(this.queryForm)
				selectByLast(this.queryForm.mid).then(res=>{
					let data = res.data

					this.menuData = data;
					if(this.menuData != null){
						this.queryForm.weekStartDate = this.menuData[0].weekStartDate
						this.queryForm.weekEndDate = this.menuData[0].weekEndDate

						this.cusIndex(this.menuData,"")

					}else{
						this.saveFlag = false
					}

				}).catch(() => {
					this.loading = false
				})
			},

			//获取列表全部信息==>根据mid获取详细表的信息  type：1是生成计划，2是查询计划
			getRootMenuList(type) {

				if (this.queryForm.weekStartDate == null || this.queryForm.weekStartDate == '') {
					this.$message({
						type: 'error',
						message: '请选择开始日期'
					})
					return
				}

				if (this.queryForm.weekEndDate == null || this.queryForm.weekEndDate == '') {
					this.$message({
						type: 'error',
						message: '请选择结束日期'
					})
					return
				}
						//查询计划,不论type是1还是2，先查询已经生成的计划，判断是否已经生成过，已经生成过，给提示
				selectPlan(this.queryForm).then(res => {

					let dataList = res.data;

					if(type == '1'){//生成计划

						if(dataList == null ){
							selectByMapForWeekPlan(this.queryForm).then(resp => {

								if(resp.data != null && resp.data.length > 0){
									this.menuData = resp.data;

									this.saveFlag = false
									this.cusIndex(this.menuData,"")
								}else{
									this.menuData = []

									this.$message({
										type: 'info',
										message: '选择日期没有相应的月计划'
									});

								}



							}).catch(() => {
								this.loading = false
							})
						}else{

							const h = this.$createElement;
							this.$msgbox({
							title: '提示',
							message: h('p', null, [
								h('span', null, '此日期范围已生成相应周计划，是否覆盖？ ')
							]),
							showCancelButton: true,
							confirmButtonText: '确定',
							cancelButtonText: '取消',
							beforeClose: (action, instance, done) => {

								if (action === 'confirm') {

									selectByMapForWeekPlan(this.queryForm).then(resp => {



										if(resp.data != null && resp.data.length > 0){

											this.menuData = resp.data;
											this.saveFlag = false //是否显示导出

											// this.queryForm.weekStartDate = resp.data[0].weekStartDate

											this.cusIndex(this.menuData,"")

										}else{
											this.menuData = []

											this.$message({
												type: 'info',
												message: '选择日期没有相应的月计划'
											});

										}

									}).catch(() => {
										this.loading = false
									})

									done();
								}

								if(action === 'cancel'){
									done();
								}

							}
							}).then(action => {
								this.$message({
									type: 'info',
									message: '查询进度计划成功'
								});
							});
						}

					}else if(type == '2'){//查询计划


						if(dataList != null && dataList.length > 0){
							this.menuData = dataList;

							this.queryForm.weekStartDate = dataList[0].weekStartDate
							this.queryForm.weekEndDate = dataList[0].weekEndDate


							this.saveFlag = true
							this.cusIndex(this.menuData,"")
						}else{
							this.menuData = []
							this.saveFlag = false

						}


					}
				}).catch(() => {
					this.loading = false
				})


			},

			// 重载节点数据
			reloadNode(parentId, data) {
				this.$set(this.$refs.menuTable.store.states.lazyTreeNodeMap, parentId, data);
			},


			//行点击
			rowClick(row,column,event){

				this.currentMenuFlag = true
			},

			//节点操作
			handleNode(data){

				if(data == 'addNode'){
					if(this.currentMenuFlag){
						this.addNode('0')

					}else{
						const h = this.$createElement;
						this.$msgbox({
						title: '提示',
						message: h('p', null, [
							h('span', null, '未选择节点数据，是否新增根节点? ')
						]),
						showCancelButton: true,
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						beforeClose: (action, instance, done) => {

							if (action === 'confirm') {

								this.addNode('1')
								done();

							}else {
								done();
							}
						}
						}).then(action => {
							this.$message({
								type: 'info',
								message: '新增根节点成功'
							});
						});
					}


				}else if(data == 'delNode'){

					if(this.currentMenuFlag){

						this.delNode(this.menuData,this.$refs.menuTable.store.states.currentRow.planDetailMdId)

						this.$refs.menuTable.store.states.currentRow = null

					}else{
						this.$message({
							type: 'error',
							message: '请选择节点数据'
						})
					}

					this.currentMenuFlag = false

				}else if(data == 'addRootNode'){//新增根节点
					this.addNode('1')
				}

			},

			//新增节点
			addNode(type){

				let addMdid = guid()

				if(type == '1'){//根节点

					let map = {mid:this.menuData[0].mid,planDetailMdId:addMdid,parentId:"0",planMonth:this.queryForm.planMonth}

					this.menuData.push(map)

				}else{
					let a = this.$refs.menuTable.store.states.currentRow

					let map = {mid:a.mid,planDetailMdId:addMdid,parentId:a.planDetailMdId,planMonth:this.queryForm.planMonth}

					if(a.children != null){

						a.children.push(map)
					}else{

						let b = [map]
						this.$set(this.$refs.menuTable.store.states.currentRow,'children',b)
					}
				}

				this.cusIndex(this.menuData,"")

			},
			//删除节点
			delNode(data,id){

				for(let i =0 ; i < data.length; i++){
					if(data[i].children == null){
						if(data[i].planDetailMdId == id){
							data.splice(i,1)
						}
					}else{
						this.delNode(data[i].children,id)
						if(data[i].planDetailMdId == id){
							data.splice(i,1)
						}
					}

				}
			},
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


			//保存
			savePlan(){
				this.$refs['queryForm'].validate(valid => {
					if (valid) {

						let nameNum = 0;//任务名称
						this.handleData = []

						this.handleMenuData(this.menuData)//对menutada进行处理，处理后的数据放入this.handleData

						for(let i = 0;i < this.handleData.length;i++){
							let dataFo = this.handleData[i]

							if(dataFo.name == null || dataFo.name == '' || dataFo.name == ""){
								nameNum = nameNum + 1
							}


							if(dataFo.weekStartDate == null || dataFo.weekStartDate == '' || dataFo.weekStartDate == ""){
								dataFo.weekStartDate = this.queryForm.weekStartDate
							}

							if(dataFo.weekEndDate == null || dataFo.weekEndDate == '' || dataFo.weekEndDate == ""){
								dataFo.weekEndDate = this.queryForm.weekEndDate
							}

							if(dataFo.mid == null || dataFo.mid == '' || dataFo.mid == ""){
								dataFo.mid = this.queryForm.mid
							}

							if(dataFo.relatedBuildingId != null && dataFo.relatedBuildingId.length > 0){
								dataFo.relatedBuildingId = dataFo.relatedBuildingId.join(',')
								// console.log(dataFo.relatedBuildingId)
							}

						}


						if (nameNum > 0) {
							this.$message({
								type: 'error',
								message: '请填写任务名称'
							})
							return;
						}


						savePlan(this.handleData).then(res=>{

							if(res.code == '0'){

								this.getRootMenuList('2')

								this.$message({
									message: '保存成功',
									type: 'success'
								});

							}
						})
					}
				})


			},

			//保存前对 menuData进行处理
			handleMenuData(data){
				for(let i = 0 ; i < data.length; i++){
					if(data[i].children == null){
						this.handleData.push(data[i])
					}else{
						this.handleMenuData(data[i].children)
						this.handleData.push(data[i])
					}
				}
			},



			//关联楼座 -- table显示name
			relatedBuildingIdFormat(row, column){
				let value = row.relatedBuildingId
				// console.log(value)
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
          var strs = actions.join('');
					return strs.substr(0, strs.length - 1);
        }


			},

			//查询 月份选择范围，mid
			getMaxMinFormLastPlan(){
				getMaxMinFormLastPlan().then(res=>{

					this.queryForm.mid = res.data.mid
					this.startMonth = res.data.startDate
					this.endMonth = res.data.endDate



				})
			},

			//查询 单体楼座下拉
			selectLoft(){
				selectLoft().then(res=>{

					this.loftList = res.data.loftList

				})
			},

			//导出
			exportExcel(){
				exportExcel(this.queryForm).then(res=>{
					downloadExcelWithResData(res)
          			this.loading = false
				})
			},

			//序号 修改样式1.1   1.1.1格式
			cusIndex(obj,cusIndez){
					for(let i=0;i<obj.length;i++){
						let temp = (cusIndez=="")?(i+1):(cusIndez+"."+(i+1))
						obj[i].cusIndex = temp
						if(obj[i].children !=null && obj[i].children.length>0){
							this.cusIndex(obj[i].children,obj[i].cusIndex)
						}
					}
			},

			//每日填报
			handleClick(type,data){
				if(type == 'edit'){
					this.currentRow = data.row
					this.dayFillinShow = true;
				}
			}



		}
	}
</script>

<style lang="scss" scoped>
	.main {
		height: 100%;

		/*margin:0 15px;*/
		/deep/ .xr-header {
			padding: 15px 30px;
		}
	}

	.main-body {
		background-color: white;
		/*border-top: 1px solid $xr-border-line-color;*/
		border-bottom: 1px solid $xr-border-line-color;
		padding: 10px;
		height: 100%;
		overflow-y: auto;
	}

	.main-table-header {
		height: 50px;
		background-color: white;
		position: relative;

		.main-table-header-button {
			float: right;
			margin-right: 20px;
			margin-top: 6px;
		}
	}

	.p-contianer {
		position: relative;
		background-color: white;
		height: 44px;

		.p-bar {
			float: right;
			margin: 5px 100px 0 0;
			font-size: 14px !important;
		}
	}

	/** 树形结构 */
	.el-tree /deep/ .el-tree-node__expand-icon {
		display: none;
	}

	.el-tree /deep/ .el-tree-node__content {
		height: 40px;

		.node-data {
			// .node-img {
			//   width: 15px;
			//   height: 15px;
			//   display: block;
			//   margin-right: 8px;
			//   margin-left: 24px;
			// }
			height: 40px;
			padding: 0 15px;
			position: relative;
			border-radius: $xr-border-radius-base;

			.wk {
				font-size: 14px;
				color: #8a94a6;
				flex-shrink: 0;
			}

			.wk-department {
				margin-right: 8px;
			}

			&__mark {
				display: inline-block;
				width: 6px;
				height: 6px;
				border-radius: 50%;
				background-color: #e6e6e6;
				margin-right: 8px;
				flex-shrink: 0;
			}

			&__label {
				flex: 1;
				color: #333;
				font-size: 14px;
				margin-right: 8px;
			}

			.wk-up-unfold {
				margin-left: 8px;
				transition: transform 0.3s;
			}

			.wk-up-unfold.is-close {
				transform: rotateZ(180deg);
			}

			// .node-label-set {
			//   display: none;
			// }
		}

		.node-data.is-current,
		.node-data:hover {
			background-color: $xr--background-color-base;
		}

		// .node-data:hover .node-label-set {
		//   display: block;
		// }
	}

	.el-date-editor.el-input{
		width:140px
	}
	.taskName .cell .el-input.el-input--small{
		width:calc(100% - 80px)
	}
	/deep/
	.timecom {
		height: 38px; }

	/deep/
	.el-table__indent{
		padding-left: 0px !important;
	}


/* 用来设置当前页面element全局table 选中某行时的背景色*/
  .el-table__body tr.current-row>td{
     background-color: #ff784a !important;
     color: #fff;
  }

  /deep/
  .el-form-item {
	  margin-bottom: 0px;
  }

	@import 'src/views/styles/table.scss';
</style>
