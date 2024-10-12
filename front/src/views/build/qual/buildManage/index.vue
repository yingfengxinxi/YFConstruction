<template>
	<div class="main">
		<!--    <xr-header-->
		<!--      icon-class="wk wk-associate"-->
		<!--      icon-color="#FFB940"-->
		<!--      label="质量问题库管理" />-->
		<div class="main-body">
			<div class="main-table-header">
				<el-button class="main-table-header-button xr-btn--orange" type="primary" icon="el-icon-plus" @click="addMenu">新建楼栋</el-button>
				<!-- <el-button class="main-table-header-button" type="primary" @click="bulkImportClick">导入</el-button> -->
			</div>
			<el-table v-loading="loading" ref="menuTable" :data="menuData" style="width: 100%" :row-key="rowKey" border lazy @cell-click="pageto" :cell-class-name="cellClassName" :load="getChildMenuList" :tree-props="{children: 'children', hasChildren: 'hasChildren'}" :expand-row-keys="expands" @expand-change="expandChange">
				<el-table-column type="index" align="center" label="序号" width="120"></el-table-column>
				<el-table-column prop="buildingName" label="楼栋名称"></el-table-column>
				<el-table-column prop="distribHouseTypeId" :formatter="fieldFormatter" label="户型名称"></el-table-column>
				<el-table-column prop="sort" align="center" label="排序" width="120"></el-table-column>
				<el-table-column prop="status" align="center" label="状态" width="90">
					<template slot-scope="scope">
						{{getStatusText(scope.row.status) }}
					</template>
				</el-table-column>
				<el-table-column prop="remarks" align="center" label="备注说明" width="250"> </el-table-column>
				<el-table-column fixed="right" align="center" label="操作" width="300">
					<template slot-scope="scope">
						<el-button type="text" size="small" @click="handleClick('next', scope)">新建下级</el-button>
						<el-button type="text" size="small" @click="handleClick('copy', scope)">复制</el-button>
						<el-button type="text" icon="el-icon-edit" size="small" @click="handleClick('edit', scope)">编辑</el-button>
						<el-button type="text" icon="el-icon-delete" size="small" @click="handleClick('delete', scope)">删除</el-button>
						<el-button type="text" size="small" @click="handleClick('status', scope)">{{ scope.row['status'] === 0 ? '启用' : '停用' }}</el-button>

					</template>
				</el-table-column>
			</el-table>
		</div>

		<!-- 新增根节点 -->
		<CreateRootMenu v-if="showCreateRootMenu" @save="getRootMenuList" @hiden-view="showCreateRootMenu=false" />

		<!-- 新增下级节点 -->
		<CreateNextMenu v-if="showCreateNextMenu" :pquestionId="currentPid" :menu="currentMenu" @save="reloadSelf" @hiden-view="showCreateNextMenu=false" />

		<!-- 编辑节点菜单 -->
		<EditMenu v-if="showEditMenu" :menu="currentMenu" @save="reloadParent" @hiden-view="showEditMenu=false" />
		<!-- 复制节点菜单 -->
		<CopyMenu v-if="showCopyMenu" :menu="currentMenu" :CopyName="showCopyName" @save="getRootMenuList" @hiden-view="showCopyMenu=false" />
		<!-- 批量导入 -->
		<bulk-import-user :show="bulkImportShow" @reload="reloadImport" @close="bulkImportShow=false" />
		<!-- 户型详情查看 -->
		<create v-if="createShow" :action="action" @close="createShow = false" :businessType="businessType" :titleName="titleName" @save-success="getRootMenuList" />
	</div>

</template>

<script>
import XrHeader from '@/components/XrHeader'
import CreateRootMenu from './components/CreateRootMenu'
import CreateNextMenu from './components/CreateNextMenu'
import BulkImportUser from './components/BulkImportUser'
import {
	queryById,
	queryByParentId,
	deleteMenu,
	updateMenu,
} from '@/views/build/qual/api/buildManage'
import { getList } from "../api/houseTypeManage";

import EditMenu from './components/EditMenu'
import CopyMenu from './components/CopyMenu'
import Create from "../houseTypeManage/Create";
export default {
	/** 楼栋名称管理 */
	name: 'buildManage',
	components: {
		XrHeader,
		CreateRootMenu,
		CreateNextMenu,
		EditMenu,
		BulkImportUser,
		CopyMenu,
		Create
	},
	data () {
		return {
			//树型开始
			loading: false, // 加载动画
			currentPid: 0, //当前父级ID
			currentMenu: {}, //当前节点
			menuData: [], //最上级楼栋名称
			expands: [],
			//根
			showCreateRootMenu: false,
			//下级
			showCreateNextMenu: false,
			//编辑
			showEditMenu: false,
			//复制
			showCopyMenu: false,
			showCopyName: '',
			//字典项
			statusOptions: {
				'1': '启用',
				'0': '禁用'
			},
			allHouseTypes: [],
			// 批量导入
			bulkImportShow: false,
			//户型图编辑页面
			createShow: false,
			businessType: 0,
			titleName: '户型图管理',
			/** 子级参数*/
			action: { type: "save", data: null },

		}
	},
	computed: {},
	mounted () {
		this.getRootMenuList();
		this.getAllHouseType();
	},
	methods: {
		getAllHouseType () {
			getList()
				.then(res => {
					this.allHouseTypes = res.data.list;
					this.loading = false;
				})
				.catch(() => {
					this.loading = false;
				});
		},
		getStatusText (status) {
			return this.statusOptions[status];
		},
		//树表开始
		test () {
			this.$refs.menuTable.toggleRowExpansion(this.currentMenu, true)
		},
		//行标记
		rowKey (row) {
			return row.buildingId;
		},
		expandChange (row, expandedRows) { //row:被操作的行，expandedRows：是否是被展开
			row.children = [];
			console.log(expandedRows)
		},
		//获取根级列表
		getRootMenuList () {
			queryByParentId(0)
				.then(res => {
					this.menuData = res.data;
				})
				.catch(() => {
					this.loading = false
				})
		},
		// 获取子节点列表
		getChildMenuList (tree, treeNode, resolve) {
			queryByParentId(tree.buildingId)
				.then(res => {
					//console.log(res)
					resolve(res.data);
				})
				.catch(() => {
					this.loading = false
				})
		},
		reloadSelf () { //本级重载
			queryByParentId(this.currentMenu.buildingId)
				.then(res => {
					//console.log(res)
					this.reloadNode(this.currentMenu.buildingId, res.data)
				})
				.catch(() => {
					this.loading = false
				})
		},
		reloadParent () { //上级重载
			if (this.currentMenu.parentId == 0) {
				this.getRootMenuList()
				return;
			}
			queryByParentId(this.currentMenu.parentId)
				.then(res => {
					//console.log(res)
					this.reloadNode(this.currentMenu.parentId, res.data)
				})
				.catch(() => {
					this.loading = false
				})
		},
		// 导入后重载
		reloadImport () {
			// lazyTreeNodeMap 保存了所有的已经加载的节点子级数据
			// 所以需先清空该对象 再重新渲染根级数据
			this.$refs.menuTable.store.states.lazyTreeNodeMap = {}
			queryByParentId(0)
				.then(res => {
					this.menuData = res.data
					this.menuData.forEach(item => {
						// 重置加载状态
						this.resetLoadState(item.buildingId)
					})
				})
				.catch(() => {
					this.loading = false
				})

		},
		// 重置加载状态
		resetLoadState (id) {
			// 重置加载状态
			this.$refs.menuTable.store.states.treeData[id].loaded = false
			this.$refs.menuTable.store.states.treeData[id].expanded = false
			//this.$refs.menuTable['store'].states.treeData[item.questionId].loaded = false;
			//this.$refs.menuTable['store'].states.treeData[item.questionId].expanded = false
		},
		// 重载节点数据
		reloadNode (parentId, data) {
			this.$set(this.$refs.menuTable.store.states.lazyTreeNodeMap, parentId, data);
		},
		/**
		 * 批量导入
		 */
		bulkImportClick () {
			this.bulkImportShow = true
		},
		addMenu () { //添加根质量问题
			this.showCreateRootMenu = true;
		},
		async handleClick (event, scope) {
			//此处需同步调用
			await queryById(scope.row.buildingId)
				.then(res => {
					this.currentMenu = res.data;
				})
			// this.currentMenu = scope.row;
			this.currentPid = this.currentMenu.buildingId;
			if (event == 'next') { //创建下级
				this.showCreateNextMenu = true;
			} else if (event == 'copy') {
				this.showCopyName = scope.row.buildingName
				this.showCopyMenu = true;
			} else if (event == 'edit') {
				this.showEditMenu = true;
			} else if (event == 'delete') { //删除
				this.$confirm('您确定要删除吗?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				})
					.then(() => {
						this.loading = true;
						deleteMenu(this.currentMenu.buildingId)
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
			} else if (event == 'status') { //启用/禁用
				var status = scope.row.status;
				var conText = '';
				var toStatus = 0;
				if (status == '1') {
					conText = '停用';
					toStatus = 0;
				} else {
					conText = '启用';
					toStatus = 1;
				}
				this.$confirm('您确定' + conText + '吗?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				})
					.then(() => {
						updateMenu({
							buildingId: scope.row.buildingId,
							status: toStatus
						})
							.then(res => {
								this.reloadParent()
							})
							.catch(() => {
								this.loading = false
							})
					})
					.catch(() => {
						this.$message({
							type: 'info',
							message: '已取消'
						})
					})
			}
		},
		/**
		 * 格式化列
		 */
		fieldFormatter (row, column, cellValue) {
			console.log(column.property)
			if (column.property === 'distribHouseTypeId') {
				return this.CustomFormat_HouseType(this.allHouseTypes, row.distribHouseTypeId)
			}
			return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
		},
		/**
		 * 模板自定义格式化列表
		 */
		CustomFormat_HouseType (datas, value) {
			var actions = [];
			Object.keys(datas).some(key => {
				if (datas[key].id == "" + value) {
					actions.push(datas[key].houseTemplateName);
					return true;
				}
			});
			return actions.join("--");
		},
		pageto (row, column, cell, event) {
			if (column.label == "户型名称") {
				if (row.distribHouseTypeId != undefined) {
					this.createShow = true;
					this.action.type = "show";
					this.action.id = row.distribHouseTypeId
					console.log("跳转");
				}
			}
		},
		/**
		   * 通过回调控制class
		   */
		cellClassName ({ row, column, rowIndex, columnIndex }) {
			if (column.property == 'distribHouseTypeId') {
				return 'can-visit--underline can-visit--bold'
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

@import "src/views/styles/table.scss";
</style>
