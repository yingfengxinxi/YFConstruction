<template>
	<div class="main">
		<div class="main-body">
			<div class="main-table-header">

				<el-form v-show="true" :inline="true" label-width="80px"
             style="padding: 0px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">

				<el-button class="main-table-header-button xr-btn--blue" type="primary" icon="el-icon-plus" @click="addMenu">新建试题</el-button>
			
				</el-form>

			</div>
			<el-table ref="menuTable" :data="menuData" style="width: 100%" :row-key="rowKey" border lazy :load="getChildMenuList"
			 :tree-props="{children: 'children', hasChildren: 'hasChildren'}" :expand-row-keys="expands" @expand-change="expandChange">
				<el-table-column type="index" align="center" label="序号" width="90"></el-table-column>
				<el-table-column prop="typeName" align="left" header-align="center" label="试题名称" ></el-table-column>
				<el-table-column prop="typeCode" align="center" label="试题编码" width="120"></el-table-column>
				<el-table-column prop="typeDescribe" align="center" label="试题描述" ></el-table-column>
				<el-table-column prop="typeNum" align="center" label="排序号" width="110" ></el-table-column>
				
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
        queryChildrenByMap,
        deleteMenu,
        queryById
    } from '@/views/build/wifi/api/questType'
    
	import CreateMenu from './components/CreateMenu'
	import EditMenu from './components/EditMenu'
	import DetailsMenu from './components/DetailsMenu'

	export default {
		/** 问题类别管理 */
		name: 'wifiQuestType',
		components: {
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
			
				//根
				showCreateMenu: false,
				//下级
				showCreateNextMenu: false,
				//编辑
				showEditMenu: false,
				//查看
				showShowMenu:false,
				

				action:{parentId:"0",pname:''},//新增任务 传递的参数,当前页面的mid


			}
		},
		computed: {
			
		},
		created(){
            this.getRootMenuList()
		},

		mounted() {
			
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
            
			//获取根级列表==>根据parentId
			getRootMenuList() {
				let map = {parentId:this.action.parentId}
                queryChildrenByMap(map)
					.then(res => {
                
                        this.menuData = res.data;
						
					})
					.catch(() => {
						this.loading = false
					})
			},
			// 获取子节点列表
			getChildMenuList(tree, treeNode, resolve) {
				let map = {parentId:tree.id}
				queryChildrenByMap(map)
					.then(res => {
					
						resolve(res.data);
					})
					.catch(() => {
						this.loading = false
					})
			},
			reloadSelf() { //本级重载

				queryChildrenByMap({parentId:this.currentMenu.id})
					.then(res => {
					
						this.reloadNode(this.currentMenu.id, res.data)
						
						//新增节点后展开节点
						let cur = this.$refs.menuTable.store.states.currentRow
						cur.hasChildren = true

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
	
				if (this.currentMenu.parentId == "0") {
                    this.action.parentId = "0"
                    this.action.pname = ""
					this.getRootMenuList()
					return;
				}

				queryChildrenByMap({parentId:this.currentMenu.parentId})
					.then(res => {
					
						this.reloadNode(this.currentMenu.parentId, res.data)
					})
					.catch(() => {
						this.loading = false
					})
			},
			
	
			// 重载节点数据
			reloadNode(parentId, data) {
				this.$set(this.$refs.menuTable.store.states.lazyTreeNodeMap, parentId, data);
			},
			
			addMenu() { //添加根问题类别 新建问题类别
				this.action.parentId = "0";
				this.action.pname = '此处新建根问题类别';
				this.showCreateMenu = true;
            },
            
			async handleClick(event, scope) {
				//此处需同步调用
				await queryById(scope.row.id)
					.then(res => {
						this.currentMenu = res.data;
					})

				if (event == 'next') { //创建下级
					this.action.parentId = this.currentMenu.id;
					this.action.pname = this.currentMenu.typeName;
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
		padding: 10px 10px 10px 0px;
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

	@import 'src/views/styles/table.scss';
</style>
