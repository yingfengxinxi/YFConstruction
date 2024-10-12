<template>
	<div class="main">
	
			<div class="main-table-header">

				<el-form v-show="true" ref="queryForm" :model="queryForm" :rules="ruless" :inline="true" label-width="80px"
             style="padding: 0px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
				
				<el-form-item  label="选择年月" class='special' prop="planMonth" >
					<el-date-picker
						class="timecom"
						v-model="queryForm.planMonth"
						type="month"
						placeholder="请选择"
						align="right"
						value-format="yyyy-MM"
						:picker-options="pickerOptions">
					</el-date-picker>
					<el-button class="main-table-header-button xr-btn--orange" type="primary" icon="el-icon-plus" size="mini" @click="getRootMenuList('1')">生成计划</el-button>
					<el-button class="main-table-header-button xr-btn--blue" type="primary" icon="el-icon-search" size="mini" @click="getRootMenuList('2')">查询计划</el-button>
				 
				</el-form-item>

				<el-form-item style="float: right">
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
						<el-input v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1 " size="small" v-model="scope.row.name" @blur="inputBlur" 
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.name}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="level" align="center" label="等级" width="80">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.level"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.level}}</span>
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
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.planGqiNumber"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.planGqiNumber}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="ifMilestone" align="center" label="是否里程碑" width="100" >
					<template  slot-scope="scope">
						<el-select
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1"
							v-model="scope.row.ifMilestone"
							placeholder="请选择"
							clearable
							size="small"
							style="width: 80px">
							<el-option
									v-for="dict in ifMilestoneOptions"
									:key="dict.dictValue"
									:label="dict.dictLabel"
									:value="dict.dictValue" />
						</el-select>
						<span v-else>{{ifMilestoneFormat(scope.row)}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="ifCriticalRoute" align="center" label="是否关键路线" width="120">
					<template  slot-scope="scope">
						<el-select
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1"
							v-model="scope.row.ifCriticalRoute"
							placeholder="请选择"
							clearable
							size="small"
							style="width: 90px">
							<el-option
									v-for="dict in ifMilestoneOptions"
									:key="dict.dictValue"
									:label="dict.dictLabel"
									:value="dict.dictValue" />
						</el-select>
						<span v-else>{{ifCriticalRouteFormat(scope.row)}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="preNode" align="center" label="前置节点" width="140" >
					<template  slot-scope="scope">
						<select-tree 
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1"
							:options="TreeData" 
                            v-model="scope.row.preNode"
                            :props="defaultProps" 
                            :expandAll="true"/>
						<span v-else>{{PreNodeListFormat(scope.row)}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="relatedBuildingId" align="center" label="关联楼座" width="100" >
					<template  slot-scope="scope">
						<el-select
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1"
							v-model="scope.row.relatedBuildingId"
							placeholder="请选择"
							clearable
							size="small"
							style="width: 80px">
							<el-option
									v-for="dict in queryLoftMap.loftList"
									:key="dict.id"
									:label="dict.building"
									:value="dict.id" />
						</el-select>
						<span v-else>{{relatedBuildingIdFormat(scope.row)}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="constructionSite" align="center" label="施工部位" width="140">
					<template  slot-scope="scope">
						<el-input  v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.constructionSite"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.constructionSite}}</span>
					</template>
				</el-table-column>

				<el-table-column label="审核状态" v-if="showCZ" align="center" prop="checkStatus" width="150px">
					<template slot-scope="scope">
						<el-popover v-if="scope.row.checkStatus != null"
							width="300"
							v-model="scope.row.showPopover">
							<check-flow
							:show="scope.row.showPopover"
							:id="scope.row.examineRecordId"
							@close="scope.row.showPopover = false"/>
							<el-button
								@click="scope.row.showPopover=!scope.row.showPopover"
								slot="reference"
								type="text">
								<span :style="getStatusStyle(scope.row.checkStatus)" class="status-mark"/>
								<span >{{ getStatusName(scope.row.checkStatus) }}</span>
								</el-button>
						</el-popover>
						<!-- <span :style="getStatusStyle(scope.row.checkStatus)" class="status-mark"/>
						<span>{{ getStatusName(scope.row.checkStatus) }}</span> -->
						</template>

				</el-table-column>

				<el-table-column prop="planStartDate" align="center" label="计划开始日期" width="160" >
					<template  slot-scope="scope">
						<el-date-picker
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1"
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
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1"
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
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1"
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
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1"
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
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.actualGqiNumber"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.actualGqiNumber}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="delayDays" align="center" label="延误工期(天)" width="100">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.delayDays"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.delayDays}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="designTotal" align="center" label="设计总量" width="100">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.designTotal"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.designTotal}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="unit" align="center" label="计量单位" width="100">
					<template  slot-scope="scope">
						<el-input v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.unit"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.unit}}</span>
					</template>
				</el-table-column>

				<el-table-column prop="currentMonthPlan" align="center" label="本月计划" width="100">
					<template  slot-scope="scope">
						<el-input v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.currentMonthPlan"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.currentMonthPlan}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="monthTired" align="center" label="月累完成" width="100">
					<template  slot-scope="scope">
						<el-input v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.monthTired"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.monthTired}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="currentMonthPercent" align="center" label="本月占比%" width="100">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.currentMonthPercent"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.currentMonthPercent}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="finishTired" align="center" label="开累完成" width="100">
					<template  slot-scope="scope">
						<el-input v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.finishTired"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.finishTired}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="finishPercent" align="center" label="开累完成%" width="100">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.finishPercent"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.finishPercent}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="planOutputValue" align="center" label="计划产值(万元)" width="130">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.planOutputValue"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.planOutputValue}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="actualOutputValue" align="center" label="实际产值(万元)" width="130">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.actualOutputValue"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.actualOutputValue}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="planFinishPersent" align="center" label="计划完成比例%" width="130">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.planFinishPersent"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.planFinishPersent}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="actualFinishPersent" align="center" label="实际完成比例%" width="130">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.actualFinishPersent"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.actualFinishPersent}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="delayDuty" align="center" label="延期责任" width="120">
					<template  slot-scope="scope">
						<el-input  v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.delayDuty"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.delayDuty}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="responsibleParty" align="center" label="责任单位" width="140">
					<template  slot-scope="scope">
						<el-input  v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.responsibleParty"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.responsibleParty}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="delayReason" align="center" label="延期原因" width="140">
					<template  slot-scope="scope">
						<el-input  v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.delayReason"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.delayReason}}</span>
					</template>
				</el-table-column>
				
				<el-table-column prop="responsibleUser" align="center" label="责任人" width="120">
					<template  slot-scope="scope">
						<el-input  v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.responsibleUser"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.responsibleUser}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="participateUser" align="center" label="参与人" width="120">
					<template  slot-scope="scope">
						<el-input  v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.participateUser"  
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.participateUser}}</span>
					</template>
				</el-table-column>

				<el-table-column prop="note" align="center" label="备注说明" >
					<template  slot-scope="scope">
						<el-input v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex && scope.row.editFlag == 1" size="small" v-model="scope.row.note" @blur="inputBlur" 
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.note}}</span>
					</template>
				</el-table-column>

				<el-table-column fixed="right" align="center" v-if="showCZ" label="操作" width="120">
					<template slot-scope="scope">
						<el-button type="text" icon="el-icon-edit" size="small" @click="handleClick('edit', scope)">进度填报</el-button>
						
					</template>
				</el-table-column>
				
			</el-table>
	
		
		<plan-progress v-if="showCreate" 
					:id="currentId" 
					:businessType="businessType" 
					@hiden-view="showCreate = false"
      				@save="getRootMenuList('2')" />


	</div>

</template>

<script>
	import {
		selectTree,
		selectByMidAll
	} from '@/views/build/progress/api/modelset'
	import {selectLoft, selectByMapForMonthPlan,getMaxMinFormLastPlan } from '@/views/build/progress/api/planCreate'
	
	import {selectPlan,savePlan ,selectById ,selectByLast,exportExcel} from '@/views/build/progress/api/planMonth'
	import SelectTree from '@/components/SelectTree'

	import CheckStatusMixin from '@/mixins/CheckStatusMixin'//审核状态
	import CheckFlow from './../../../../../components/common/examine/components/CheckFlow' // 审批流程
	  

	import planProgress from './Update'
	import {guid} from '@/utils'
	import { downloadExcelWithResData } from '@/utils/index'

	export default {
		/** 月计划计划管理 */
		name: 'planMonthDetail',
		components: {
			SelectTree,
			planProgress,
			CheckFlow
		},
		mixins: [CheckStatusMixin],
		props:{
			//选择的月份
			queryForm:{
				planMonth:'',
				mid:'',
				startMonth:'',//选择月份范围的开始
				endMonth:'',//选择月份范围的结束
			},
			
		},
		data() {
			return {
				businessType:10030,

				tableHeight:document.documentElement.clientHeight - 280,

				//树型开始
				loading: false, // 加载动画

				saveFlag:true,//是否显示导出按钮
				currentMenuFlag: false, //是否选中某一行
				menuData: [], //table数据
				handleData:[],//保存时，处理后的table数据

				ifMilestoneOptions:[],//是否里程碑

				PreNodeList:[],//前置节点
				TreeData:[],//前置节点树
				defaultProps: {//前置节点默认字段
					parent: 'pid',   // 父级唯一标识
					value: 'id',          // 唯一标识
					label: 'name',       // 标签显示
					children: 'children', // 子级
				},

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
				//新增节点的planDetailMdId
				addMdid:"0",

				delayDutyOptions:[],//延期责任
				responsiblePartyList:[],//责任单位
				delayReasonOptions:[],//延期原因

				showCZ:false,//是否展示操作列
				showCreate:false,//填报进度页面是否展示

				currentId:'',

				queryLoftMap:{}

			}
		},
		computed:{
       
			//月份选择 限制操作
			pickerOptions(){
				let start = this.queryForm.startMonth?new Date(this.queryForm.startMonth).getTime():'';
				let end = this.queryForm.endMonth?new Date(this.queryForm.endMonth).getTime():'';
				return{
					disabledDate(time) {
						return time.getTime() < start-8.64e7|| time.getTime() > end;//如果没有后面的-8.64e7就是不可以选择今天的
						}
				}
			},
		},


		created(){
			
			//关联楼层
			this.selectLoft()

			//  是否里程碑字典项
			this.getDicts('sys_yes_no').then(response => {
				this.ifMilestoneOptions = response.data
			})

			//前置节点下拉树
			this.getMenuList();

			//前置节点id 和name
			this.selectIdAndNameByMidAll()

			//查询此模板最近生成的计划
			this.selectByLast()

		},
		watch:{
			
		},
		mounted() {

		},
		methods: {
			
			//行标记
			rowKey(row) {
				return row.planDetailMdId;
			},

			//导出
			exportExcel(){

				exportExcel(this.queryForm).then(res=>{
					downloadExcelWithResData(res)
          			this.loading = false
				})
			},

			selectByLast(){
				// console.log(this.queryForm)
				selectByLast(this.queryForm.mid).then(res=>{
					let data = res.data

					this.menuData = data;
					if(this.menuData != null){
						this.queryForm.planMonth = this.menuData[0].planMonth
						this.showCZ = true
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
				
				if (this.queryForm.planMonth == null || this.queryForm.planMonth == '') {
					this.$message({
						type: 'error',
						message: '请选择月份'
					})
					return
				}

				
				//查询计划,不论type是1还是2，先查询已经生成的计划，判断是否已经生成过，已经生成过，给提示
				selectPlan(this.queryForm).then(res => {

					let dataList = res.data;
					
					//当查询计划的数据部位null的时候，展示操作列
					if(dataList != null ){
						this.showCZ = true
					}else{
						this.showCZ = false
					}

					

					if(type == '1'){//生成计划
						
						if(dataList == null ){
							selectByMapForMonthPlan(this.queryForm).then(resp => {
							
								this.menuData = resp.data;
								this.cusIndex(this.menuData,"")

								this.saveFlag = false
									
							}).catch(() => {
								this.loading = false
							})
						}else{
							
							const h = this.$createElement;
							this.$msgbox({
							title: '提示',
							message: h('p', null, [
								h('span', null, '此月份已生成相应进度计划，不可再次生成，是否展示已生成的进度计划？ ')
							]),
							showCancelButton: true,
							confirmButtonText: '确定',
							cancelButtonText: '取消',
							beforeClose: (action, instance, done) => {
								
								if (action === 'confirm') {
									this.menuData = dataList;
									if(dataList != null && dataList.length > 0){
										this.saveFlag = true
										this.cusIndex(this.menuData,"")

									}else{
										this.saveFlag = false
									}

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

						this.menuData = dataList;
						if(dataList != null && dataList.length > 0){
							this.saveFlag = true
							this.cusIndex(this.menuData,"")
						}else{
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

						let handleDElData = this.handleDataProDel(this.$refs.menuTable.store.states.currentRow)
						
						for(let i = 0; i<handleDElData.length;i++){
							if(handleDElData[i].editFlag == 0){
								this.$message({
									type: 'error',
									message: '当前节点数据或者子级节点已经提交流程，不允许删除'
								})
								return false
							}
						}
						
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

				this.addMdid = guid()
					
				if(type == '1'){//根节点

					let map = {mid:this.menuData[0].mid,planDetailMdId:this.addMdid,parentId:"0",planMonth:this.queryForm.planMonth,editFlag:1}

					this.menuData.push(map)

				}else{
					let a = this.$refs.menuTable.store.states.currentRow

					let map = {mid:a.mid,planDetailMdId:this.addMdid,parentId:a.planDetailMdId,planMonth:this.queryForm.planMonth,editFlag:1}

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
						let ifpreNodeEqualsCur = false //前置节点是否和当前节点一致

						this.handleData = []

						this.handleMenuData(this.menuData)//对menutada进行处理，处理后的数据放入this.handleData
						
						for(let i = 0;i < this.handleData.length;i++){
							let dataFo = this.handleData[i]
							
						
							if(dataFo.name == null || dataFo.name == '' || dataFo.name == ""){
								nameNum = nameNum + 1
							}
							if(dataFo.preNode == dataFo.planDetailMdId){
								ifpreNodeEqualsCur = true
							}

							if(dataFo.planMonth == null || dataFo.planMonth == '' || dataFo.planMonth == ""){
								dataFo.planMonth = this.queryForm.planMonth
							}

							if(dataFo.mid == null || dataFo.mid == '' || dataFo.mid == ""){
								dataFo.mid = this.queryForm.mid
							}

							dataFo.batchId = guid()

						}
						


						if (nameNum > 0) {
							this.$message({
								type: 'error',
								message: '请填写任务名称'
							})
							return;
						}

						if (ifpreNodeEqualsCur) {
							this.$message({
								type: 'error',
								message: '前置节点不能选择当前节点，请重新选择'
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

			//删除 前对数据进行处理--》list，方便判断是否可以删除
			handleDataProDel(data){
				let handleDElData = []
				
				handleDElData.push(data)
				let chData = data.children

				if(chData != null){
					Object.keys(chData).some((key) => {
						let da = this.handleDataProDel(chData[key])

						if(da != null){
							Object.keys(da).some((key2) => {
								handleDElData.push(da[key2])
							})
						}
						

					})
				}
				
				
				return handleDElData
				
			},

			//获取前置节点树
			getMenuList() {
			
				selectTree(this.queryForm.mid).then(res => {
					if(res.data != null){
					
						this.TreeData = res.data;
					}

				})
				.catch(() => {
					this.loading = false
				})
			},
			
			//是否关键路线
			ifCriticalRouteFormat(row, column){
				return this.selectDictLabel(this.ifMilestoneOptions, row.ifCriticalRoute)
			},

			//是否里程碑
			ifMilestoneFormat(row, column){
				return this.selectDictLabel(this.ifMilestoneOptions, row.ifMilestone)
			},

			//查询全部的的id和name ，用于前置节点展示name
			selectIdAndNameByMidAll(){
			
				selectByMidAll(this.queryForm.mid).then(res => {

					this.PreNodeList = res.data
					this.loading = false;
				})
				.catch(() => {
					this.loading = false
				})
			},

			//前置节点 -- table显示name
			PreNodeListFormat(row, column){
					let value = row.preNode
					let datas = this.PreNodeList

					var actions = []

					Object.keys(datas).some((key) => {
						if (datas[key].id == ('' + value)) {
						actions.push(datas[key].name)
						return true
						}
					})
					return actions.join('')
					
			},
			//关联楼座 -- table显示name
			relatedBuildingIdFormat(row, column){
				let value = row.relatedBuildingId
				let datas = this.queryLoftMap.loftList

				if(datas == undefined || datas == null){
					return null
				}

				var actions = []
				if(value == null || value == undefined){
					return null
				}else{
					Object.keys(datas).some((key) => {
						if (datas[key].id == ('' + value)) {
						actions.push(datas[key].building)
						return true
						}
					})
					return actions.join('')
				}
				

			},

			//查询 月份选择范围，mid
			getMaxMinFormLastPlan(){
				getMaxMinFormLastPlan().then(res=>{
			
					this.queryForm.mid = res.data.mid
					this.queryForm.startMonth = res.data.startDate
					this.queryForm.endMonth = res.data.endDate

					

				})
			},

			//查询 关联楼层下拉 
			selectLoft(){
				selectLoft().then(res=>{
					
					this.queryLoftMap = res.data

					
				})
			},

			// 0待审核、1审核中、2审核通过、3已拒绝 4已撤回 5未提交
			getStatusStyle(status) {
				return {
					backgroundColor: this.getStatusColor(status)
				}
			},

			async handleClick(event, scope) {
				//此处需同步调用
				// await selectById(scope.row.id)
				// 	.then(res => {
				// 		this.currentMenu = res.data;
				// 	})

				this.currentId = scope.row.id

				if (event == 'edit') {//进度填报
					this.actionType = 'update'
					this.showCreate = true;
				} 

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
