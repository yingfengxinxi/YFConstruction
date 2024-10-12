<template>
	<div class="main">
		<div class="main-body">
			<div class="main-table-header">

				<el-form v-show="true" ref="queryForm" :model="queryForm"  :inline="true" label-width="80px"
             style="padding: 0px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
					<el-form-item  label="选择模板" class='special' prop="mid" >
						<el-select
								v-model="queryForm.mid"
								placeholder="请选择"
								@change='selectPlan'
								clearable
								size="small"
								style="width: 160px">
						<el-option
								v-for="dict in midOptions"
								:key="dict.id"
								:label="dict.name"
								:value="dict.id" />
						</el-select>
					</el-form-item>
					<el-form-item  label="开始日期" class='special' prop="startDate" >
						<el-date-picker
							class="timecom"
							v-model="queryForm.startDate"
							type="date"
							placeholder="请选择"
							align="right"
							value-format="yyyy-MM-dd">
						</el-date-picker>
					</el-form-item>
					<el-form-item  label="最高楼层" class='special' prop="maxLoft" >
						<el-input
							type="number"
							v-model="queryForm.maxLoft"
							placeholder="请输入"
							clearable
							size="small"
							style="width:160px;"
							@keyup.enter.native="createPlan"
							/>

							<el-button class="main-table-header-button xr-btn--orange" type="primary" icon="el-icon-plus" size="mini" @click="createPlan" style="margin-left: 20px;">生成计划</el-button>
							<!--<el-button class="main-table-header-button xr-btn--blue" type="primary" icon="el-icon-search" size="mini" @click="selectPlan">查询计划</el-button>-->

					</el-form-item>

					<el-form-item style="float: right">

						<el-button v-show="menuData != null && menuData.length > 0 && saveFlag" class="main-table-header-button" type="success" icon="el-icon-success" size="mini" @click="savePlan">保存</el-button>

						<el-dropdown v-show="menuData != null && menuData.length > 0 && saveFlag" trigger="click" @command="handleNode" style="margin:2px 10px">
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
			<el-table ref="menuTable" :data="menuData" style="width: 100%" :row-key="rowKey" border
			default-expand-all
			:height="tableHeight"
			:tree-props="{children: 'children', hasChildren: 'hasChildren'}"
			@row-click="rowClick"
			@cell-click="cellClick"
			highlight-current-row
			:cell-class-name="getRowColumn">
				<el-table-column prop="cusIndex" align="left" label="序号">
					<template  slot-scope="scope">
						<span>{{scope.row.cusIndex}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="name" align="left" header-align="center" label="任务名称" min-width="100" class-name="taskName" show-overflow-tooltip>
					<template  slot-scope="scope">
						<el-input v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex" size="small" v-model="scope.row.name" @blur="inputBlur"
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.name}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="level" align="center" label="等级" width="80">
					<template  slot-scope="scope">
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex" size="small" v-model="scope.row.level"
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
						<el-input type="number" v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex" size="small" v-model="scope.row.planGqiNumber"
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.planGqiNumber}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="ifMilestone" align="center" label="是否里程碑" width="100" >
					<template  slot-scope="scope">
						<el-select
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex"
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
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex"
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
				<el-table-column prop="preNode" align="center" label="前置节点" width="140" show-overflow-tooltip>
					<template  slot-scope="scope">
						<select-tree
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex"
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
							v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex"
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
				<el-table-column prop="note" align="center" label="备注说明" >
					<template  slot-scope="scope">
						<el-input v-if="scope.row.index==clickCellRowIndex && scope.column.index==clickCellColumnIndex" size="small" v-model="scope.row.note" @blur="inputBlur"
              				placeholder="请输入">
						</el-input>
						<span v-else>{{scope.row.note}}</span>
					</template>
				</el-table-column>

			</el-table>
		</div>


	</div>

</template>

<script>
	import {
		queryById,
		deleteMenu,
		queryMapByMap,
		selectModels,
		selectTree,
		selectByMidAll
	} from '@/views/build/progress/api/modelset'
	import {savePlan, selectLoft, selectPlan ,selectPlanLastTime} from '@/views/build/progress/api/planCreate'
	import SelectTree from '@/components/SelectTree'

    import {selectIdAndName , deleteByMid } from '@/views/build/progress/api/planMonth'


	import {guid} from '@/utils'

	export default {
		/** 计划管理 */
		name: 'planCreate',
		components: {
			SelectTree
		},
		data() {
			return {
				//树型开始
				loading: false, // 加载动画
				tableHeight:document.documentElement.clientHeight - 220,

				saveFlag:true,
				currentMenuFlag: false, //是否选中某一行
				menuData: [], //table数据
				handleData:[],//保存时，处理后的table数据


				//字典项
				midOptions:[],//选择的模板，从后台数据查询模板

				ifMilestoneOptions:[],//是否里程碑

				PreNodeList:[],//前置节点
				TreeData:[],//前置节点树
				defaultProps: {//前置节点默认字段
					parent: 'pid',   // 父级唯一标识
					value: 'id',          // 唯一标识
					label: 'name',       // 标签显示
					children: 'children', // 子级
				},

				//选择模板，开始日期，最高楼层，
				queryForm:{
					maxLoft:null,
					startDate:null,
					mid:''
				},

				queryLoftMap:{},//查询的 最高楼层，开始日期，子表关联楼层下拉,最小mdid  的信息
				clickCellRowIndex:null,//用于判断点击是哪行
				clickCellColumnIndex:null,//用于判断点击是哪列

				//节点操作
				moreTypes: [
				  { type: "addRootNode", name: "新增根节点", icon: "" },
				  { type: "addNode", name: "新增节点", icon: "" },
				  { type: "delNode", name: "删除节点", icon: "" }
				],
				//新增节点的mdid
				addMdid:"0",

			}
		},
		computed: {

		},
		created(){
			//查询模板
			this.selectMidOptions();
			//查询 最高楼层，开始日期，子表关联楼层下拉
			this.selectLoft();
			//查询最近生成的计划
			this.selectPlanLastTime();

			//  是否里程碑字典项
			this.getDicts('sys_yes_no').then(response => {
				this.ifMilestoneOptions = response.data
			})

		},

		mounted() {

		},
		methods: {
			
			//行标记
			rowKey(row) {
				return row.mdid;
			},

			//获取列表全部信息==>根据mid获取详细表的信息  type：1是生成计划，2是查询计划
			getRootMenuList(type) {

				this.saveFlag = true

				//前置节点下拉数
				this.getMenuList();

				//前置节点id 和name
				this.selectIdAndNameByMidAll()

				let map = {}

				if(type == '1'){//生成计划
					map = {mid:this.queryForm.mid}
				}else{//查询计划
					map = {mid:this.queryForm.mid,startDate:this.queryForm.startDate}
				}
					//查询计划,不论type是1还是0，先查询已经生成的计划，判断是否已经生成过，已经生成过，给提示
					selectPlan(map).then(res => {
						if(type == '1'){//生成计划

							let map2 = {mid:this.queryForm.mid,startDate:this.queryForm.startDate}
							if(res.data == null){
								queryMapByMap(map2).then(res => {

									this.menuData = res.data;
									this.cusIndex(this.menuData,"")

								}).catch(() => {
									this.loading = false
								})
							}else{

								let messageInfo = '此模板已生成相应进度计划，是否进行覆盖?'

								let planMonthDataLists = []
								//生成计划前判断是否生成月计划===》》》 该模板已生成相应月计划，重新生成时将清空月计划
								selectIdAndName(this.queryForm.mid).then(res=>{
									planMonthDataLists = res.data

									// console.log(planMonthDataLists)

									if(planMonthDataLists.length > 0){

										messageInfo = '该模板已生成相应月计划，重新生成时将清空月计划'

									}

									this.$confirm(messageInfo, '提示', {
										confirmButtonText: '确定',
										cancelButtonText: '取消',
										type: 'warning'
									}).then(() => {

										//删除 相应模板的月计划
										deleteByMid(this.queryForm.mid).then(res=>{
											if(res.code == 0){
												queryMapByMap(map2).then(res => {

													this.menuData = res.data;
													this.cusIndex(this.menuData,"")

													this.$message({
														type: 'success',
														message: '重新生成计划成功!'
													});

												}).catch(() => {
													this.loading = false
												})

											}
										}).catch(() => {
											this.loading = false
										})



									}).catch(() => {
										this.$message({
											type: 'info',
											message: '已取消重新生成计划'
										});
									});


								})


							}

						}else if(type == '2'){//查询计划
							if(res.data != null && res.data.length > 0){
								this.queryForm.startDate = res.data[0].startDate
							}
							this.menuData = res.data;
							this.cusIndex(this.menuData,"")
						}
					}).catch(() => {
						this.loading = false
					})
			},
			cusIndex(obj,cusIndez){
					for(let i=0;i<obj.length;i++){
						let temp = (cusIndez=="")?(i+1):(cusIndez+"."+(i+1))
						obj[i].cusIndex = temp
						if(obj[i].children !=null && obj[i].children.length>0){
							this.cusIndex(obj[i].children,obj[i].cusIndex)
						}
					}
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

						this.delNode(this.menuData,this.$refs.menuTable.store.states.currentRow.mdid)

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

					let map = {mid:this.menuData[0].mid,mdid:this.addMdid,pmdid:"0"}

					this.menuData.push(map)

				}else{
					let a = this.$refs.menuTable.store.states.currentRow

					let map = {mid:a.mid,mdid:this.addMdid,pmdid:a.mdid}

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
						if(data[i].mdid == id){
							data.splice(i,1)
						}
					}else{
						this.delNode(data[i].children,id)
						if(data[i].mdid == id){
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

						let num = 0;//是里程碑的个数
						let nameNum = 0;//任务名称
						let ifpreNodeEqualsCur = false //前置节点是否和当前节点一致

						this.handleData = []

						this.handleMenuData(this.menuData)//对menutada进行处理，处理后的数据放入this.handleData

						for(let i = 0;i < this.handleData.length;i++){
							if(this.handleData[i].ifMilestone == 'Y'){
								num = num + 1
							}
							if(this.handleData[i].name == null || this.handleData[i].name == '' || this.handleData[i].name == ""){
								nameNum = nameNum + 1
							}
							if(this.handleData[i].preNode == this.handleData[i].mdid){
								ifpreNodeEqualsCur = true
							}
						}

						if (num < 6) {
							this.$message({
								type: 'error',
								message: '需至少6个里程碑，请检查'
							})
							return;
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

						let map = {"ZHUB":this.queryForm,"ZIB":this.handleData}
						savePlan(map).then(res=>{

							if(res.code == '0'){
								// this.saveFlag = false
								this.$message({
									message: '保存成功',
									type: 'success'
								});

								this.selectPlan()

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

			//查询模板id和name
			selectMidOptions(){
				selectModels().then(res => {

					this.midOptions = res.data

					this.loading = false;
				})
				.catch(() => {
					this.loading = false
				})
			},

			//查询 最高楼层，开始日期，子表关联楼层下拉
			selectLoft(){
				selectLoft().then(res=>{

					this.queryLoftMap = res.data

					this.queryForm.maxLoft = res.data.maxLoft

					if(res.data.date.length > 0){
						let map1 = res.data.date[0]
						this.queryForm.startDate = map1.sj != null ? map1.sj : (map1.ht != null ? map1.ht :map1.kgl)
					}

					this.$forceUpdate()//强制刷新

				})
			},

			//查询最近生成的计划
			selectPlanLastTime(){
				selectPlanLastTime().then(res => {

					if(res.data != null){
						let data = res.data

						this.menuData = data.ZIB
						this.cusIndex(this.menuData,"")
						this.queryForm.mid = data.ZUB.mid

						//前置节点下拉数
						this.getMenuList();

						//前置节点id 和name
						this.selectIdAndNameByMidAll()

					}
				})

			},
			//生成计划
			createPlan(){
				this.$refs['queryForm'].validate(valid => {
					if (valid) {

						if(this.queryForm.maxLoft > this.queryLoftMap.maxLoft){

							this.$confirm('所选模板适用最大楼层为【'+ this.queryLoftMap.maxLoft + '】，请问是否继续生成计划?', '提示', {
								confirmButtonText: '确定',
								cancelButtonText: '取消',
								type: 'warning'
							})
							.then(() => {

								//table列表
								this.getRootMenuList('1')

							})
							.catch(() => {
								this.$message({
									type: 'info',
									message: '已取消继续生成计划'
								})
							})


						}else{

							//table列表
							this.getRootMenuList('1')
						}

					}
				})



			},
			//查询计划
			selectPlan(){

				if (this.queryForm.mid == null || this.queryForm.mid == '') {
					this.$message({
						type: 'error',
						message: '请选择模板'
					})
				}else{
					this.getRootMenuList('2')
				}

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

				var actions = []
				if(value == null || value == undefined){
					return null
				}else{
					if(datas == null || datas == undefined){
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
