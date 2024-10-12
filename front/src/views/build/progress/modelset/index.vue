<template>
	<div class="main">
		<div class="main-body">
			<div class="main-table-header">

				<el-form v-show="true" :inline="true" label-width="80px"
             style="padding: 0px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
				<el-form-item  label="选择模板" class='special' prop="" >
					<el-select
							v-model="action.mname"
							placeholder="请选择"
							clearable
							size="small"
							@change="changeSelectModels"
							style="width: 160px">
					<el-option
							v-for="dict in midOptions"
							:key="dict.id"
							:label="dict.name"
							:value="dict.id + '---' + dict.name" />
					</el-select>
				</el-form-item>



				<el-button class="main-table-header-button xr-btn--blue" type="primary" icon="el-icon-plus" @click="addMenu">新建任务</el-button>
				<el-button class="xr-btn--blue" style="margin-top: 4px;" type="primary" icon="el-icon-plus" @click="addModel">新建模板</el-button>
				<el-button class="xr-btn--orange" style="margin-top: 4px;" type="primary" icon="el-icon-delete" @click="deleteMudel">删除模板</el-button>

				</el-form>
			</div>
			<el-table ref="menuTable" :data="menuData" style="width: 100%" :row-key="rowKey" border lazy :load="getChildMenuList"
			 :tree-props="{children: 'children', hasChildren: 'hasChildren'}" :expand-row-keys="expands" @expand-change="expandChange">
				<!-- <el-table-column type="index" align="center" label="序号" width="90"></el-table-column> -->
				<!-- <el-table-column prop="serialNumber" align="center" label="序号" width="90"></el-table-column> -->
				<el-table-column prop="cusIndex" align="left" label="序号" width="120">
					<template  slot-scope="scope">
						<span>{{scope.row.cusIndex}}</span>
					</template>
				</el-table-column>

				<el-table-column prop="name" align="center" header-align="center" label="任务名称" show-overflow-tooltip></el-table-column>
				<el-table-column prop="level" align="center" label="等级" width="120"></el-table-column>
				<el-table-column prop="planGqiNumber" align="center" label="计划工期天数" width="120"></el-table-column>
				<el-table-column prop="ifMilestone" align="center" label="是否里程碑" width="120" :formatter="ifMilestoneFormat"></el-table-column>
				<el-table-column prop="ifCriticalRoute" align="center" label="是否关键路线" width="120" :formatter="ifCriticalRouteFormat"></el-table-column>
				<el-table-column prop="preNode" align="center" label="前置节点" :formatter="PreNodeListFormat" show-overflow-tooltip></el-table-column>
				<el-table-column prop="serialNumber" align="center" label="排序" width="120"></el-table-column>
				<el-table-column prop="note" align="center" label="备注说明"></el-table-column>
				<el-table-column fixed="right" align="center" label="操作" width="250">
					<template slot-scope="scope">
						<el-button type="text" icon="el-icon-edit" size="small" @click="handleClick('edit', scope)">编辑</el-button>
						<el-button type="text" icon="el-icon-delete" size="small" @click="handleClick('delete', scope)">删除</el-button>
						<el-button type="text" icon="el-icon-document" size="small" @click="handleClick('show', scope)">查看</el-button>

						<el-button type="text" size="small" @click="handleClick('next', scope)">新建下级</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>

		<!-- 新增模板-->
		<CreateModel v-if="showCreateModel" @save="selectMidOptions" @hiden-view="showCreateModel=false" />

		<!-- 新增根任务明细 -->
		<CreateMenu v-if="showCreateMenu" :action="action" @save="getRootMenuList" @hiden-view="showCreateMenu=false" />

		<!-- 新增下级节点 -->
		<CreateMenu v-if="showCreateNextMenu" :action="action" @save="reloadSelf" @hiden-view="showCreateNextMenu=false" />

		<!-- 编辑任务 -->
		<EditMenu v-if="showEditMenu" :menu="currentMenu" @save="reloadParent" @hiden-view="showEditMenu=false" />

		<!-- 查看任务  -->
		<DetailsMenu v-if="showShowMenu" :menu="currentMenu" @hiden-view="showShowMenu=false" />
	</div>

</template>

<script>
	import {
		queryById,
		queryByParentId,
		deleteMenu,
		updateMenu,
		queryByMap,
		selectModels,
		selectTree,
		selectByMidAll,
		deleteModel
	} from '@/views/build/progress/api/modelset'
	import CreateMenu from './components/CreateMenu'
	import EditMenu from './components/EditMenu'
	import CreateModel from './components/CreateModel'
	import DetailsMenu from './components/DetailsMenu'


	export default {
		/** 模板设置任务管理 */
		name: 'modelSet',
		components: {
			CreateModel,
			CreateMenu,
			EditMenu,
			DetailsMenu
		},
		data() {
			return {
				//树型开始
				loading: false, // 加载动画

				currentMenu: {}, //当前任务问题
				menuData: [], //最上级质量问题
				expands: [],
				//模板
				showCreateModel: false,
				//根
				showCreateMenu: false,
				//下级
				showCreateNextMenu: false,
				//编辑
				showEditMenu: false,
				//查看
				showShowMenu:false,
				//字典项
				midOptions:[],//选择的模板，从后台数据查询模板

				action:{mid:0,mname:'',pid:"0",pname:''},//新增任务 传递的参数,当前页面的mid

				ifMilestoneOptions:[],//是否里程碑

				PreNodeList:[],//前置节点

			}
		},
		computed: {

		},
		created(){
			//查询模板
			this.selectMidOptions();

			//  是否里程碑字典项
			this.getDicts('sys_yes_no').then(response => {
				this.ifMilestoneOptions = response.data
			})

		},

		mounted() {
			// this.getRootMenuList();
		},
		methods: {

			//树表开始
			test() {
				this.$refs.menuTable.toggleRowExpansion(this.currentMenu, true)
			},
			//行标记
			rowKey(row) {
				return row.id;
			},
			expandChange(row, expandedRows) { //row:被操作的行，expandedRows：是否是被展开
				row.children = [];
			},
			//获取根级列表==>根据mid获取详细表的信息
			getRootMenuList() {

				this.selectIdAndNameByMidAll()

				let map = {pid:this.action.pid,mid:this.action.mid}
				queryByMap(map)
					.then(res => {
						this.menuData = res.data;
						if(res.data.length > 0){

							this.cusIndex(this.menuData,"")


						}
					})
					.catch(() => {
						this.loading = false
					})
			},
			// 获取子节点列表
			getChildMenuList(tree, treeNode, resolve) {
				let map = {pid:tree.id,mid:this.action.mid}
				queryByMap(map)
					.then(res => {

						resolve(res.data);

						this.cusIndex(res.data,tree.cusIndex)

					})
					.catch(() => {
						this.loading = false

				})

			},
			reloadSelf() { //本级重载

				// if (this.currentMenu.pid == 0) {
				// 	this.action.pid = 0
				// 	this.getRootMenuList()

				// 	return;
				// }


				queryByParentId(this.currentMenu.id)
					.then(res => {

						this.reloadNode(this.currentMenu.id, res.data)

						//新增节点后展开节点
						let cur = this.$refs.menuTable.store.states.currentRow
						cur.hasChildren = true

						this.cusIndex(res.data,cur.cusIndex)

						let curStates = this.$refs.menuTable.store.states


						curStates.treeData[cur.id].loaded = false
						curStates.treeData[cur.id].expanded = true


					})
					.catch(() => {

						// console.log(this.$refs.menuTable.store.states)
						this.$refs.menuTable.store.states.treeData[this.currentMenu.id].loaded = false
						this.$refs.menuTable.store.states.treeData[this.currentMenu.id].expanded = true

						this.loading = false

					})
			},
			reloadParent() { //上级重载

				if (this.currentMenu.pid == "0") {
					this.getRootMenuList()
					return;
				}
				let map = {pid:this.currentMenu.pid,mid:this.action.mid}
				queryByParentId(this.currentMenu.pid)
					.then(res => {

						this.reloadNode(this.currentMenu.pid, res.data)


						let a = this.currentMenu.cusIndex.split('.')
						let b = a.slice(0,a.length-1)
						let c= b.join('.')

						this.cusIndex(res.data,c)

					})
					.catch(() => {
						this.loading = false
					})
			},
			// 导入后重载
			reloadImport() {
				// lazyTreeNodeMap 保存了所有的已经加载的节点子级数据
				// 所以需先清空该对象 再重新渲染根级数据
				this.$refs.menuTable.store.states.lazyTreeNodeMap = {}
				queryByParentId(0)
					.then(res => {
						this.menuData = res.data
						this.menuData.forEach(item => {
							// 重置加载状态
							this.resetLoadState(item.questionId)
						})
					})
					.catch(() => {
						this.loading = false
					})

			},
			// 重置加载状态
			resetLoadState(id) {
				// 重置加载状态
				this.$refs.menuTable.store.states.treeData[id].loaded = false
				this.$refs.menuTable.store.states.treeData[id].expanded = false
				//this.$refs.menuTable['store'].states.treeData[item.questionId].loaded = false;
				//this.$refs.menuTable['store'].states.treeData[item.questionId].expanded = false
			},
			// 重载节点数据
			reloadNode(parentId, data) {
				this.$set(this.$refs.menuTable.store.states.lazyTreeNodeMap, parentId, data);
			},


			//添加模板
			addModel(){
				this.showCreateModel = true;
			},

			//删除模板
			deleteMudel(){

				this.$confirm('您确定要删除模板【'+this.action.mname+'】吗?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				})
				.then(() => {
					deleteModel(this.action.mid)
						.then(res => {
							this.loading = false
							if (res.code == 0) {
								this.selectMidOptions()

							} else {
								this.$message({
									type: 'warn',
									message: res.msg
								})
							}
						})
						.catch(() => {
							this.loading = false
						})
				})
				.catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					})
				})
			},

			addMenu() { //添加根详细表信息 新建任务
				this.action.pid = "0";
				this.action.pname = '此处新建根任务';
				this.showCreateMenu = true;
			},
			async handleClick(event, scope) {
				//此处需同步调用
				await queryById(scope.row.id)
					.then(res => {
						this.currentMenu = res.data;
						this.currentMenu.cusIndex = scope.row.cusIndex
					})

				if (event == 'next') { //创建下级
					this.action.pid = this.currentMenu.id;
					this.action.pname = this.currentMenu.name;
					this.showCreateNextMenu = true;
				} else if (event == 'edit') {//修改
					this.showEditMenu = true;
				} else if (event == 'delete') { //删除
					this.$confirm('您确定要删除吗?', '提示', {
							confirmButtonText: '确定',
							cancelButtonText: '取消',
							type: 'warning'
						})
						.then(() => {
							deleteMenu(this.currentMenu.id)
								.then(res => {
									this.loading = false
									if (res.code == 0) {
										this.reloadParent()

									} else {
										this.$message({
											type: 'warn',
											message: res.msg
										})
									}
								})
								.catch(() => {
									this.loading = false
								})
						})
						.catch(() => {
							this.$message({
								type: 'info',
								message: '已取消删除'
							})
						})
				} else if(event == 'show'){//查看
					this.showShowMenu = true;
				}
			},

			//查询模板id和name
			selectMidOptions(){
				selectModels().then(res => {

					this.midOptions = res.data

					this.action.mid = res.data[0].id
					this.action.mname = res.data[0].name

					this.getRootMenuList();

					this.loading = false;
				})
				.catch(() => {
					this.loading = false
				})
			},

			//模板下拉框change
			changeSelectModels(data){
				this.$forceUpdate()//强制刷新

				this.action = {pid:"0",pname:''}

				let a= data.split('---')

				this.action.mid = Number(a[0])
				this.action.mname = a[1]

				this.getRootMenuList()

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

				selectByMidAll(this.action.mid).then(res => {

					this.PreNodeList = res.data
					this.loading = false;
				})
				.catch(() => {
					this.loading = false
				})
			},
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

			//序号 修改样式
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
			margin-top: 10px;
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

	/deep/
	.el-table__indent{
		padding-left: 0px !important;
	}

	@import 'src/views/styles/table.scss';
</style>
