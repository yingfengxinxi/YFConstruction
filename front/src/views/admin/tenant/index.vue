<template>
  <div class="role-authorization">
    <xr-header
      ref="xrHeader"
      show-search
      label="租户管理"
      icon-class="wk wk-customer"
      icon-color="#19B5F6" />
    <div class="role-box">
      <!-- 左边导航 -->
      <div
        v-loading="tenantLoading"
        class="nav">
        <div class="nav__hd">
          租户列表
          <el-button
            type="text"
            icon="el-icon-circle-plus"
            class="add-btn"
            @click="addStruc">创建租户</el-button>
        </div>
        <!-- 部门列表 -->
        <div class="section">
          <div class="section__content">
            <el-tree
                    ref="tree"
                    :data="showDepData"
                    node-key="deptId"
                    highlight-current
                    default-expand-all
                    @node-click="changeDepClick">
              <flexbox
                slot-scope="{ node }"
                :class="{ 'is-current': node.isCurrent}"
                class="node-data">
                <i
                  v-if="node.level == 1"
                  class="wk wk-department" />
                <span
                  v-else
                  class="node-data__mark" />

                <div class="node-data__label text-one-line ">{{ node.label }}</div>
                <i
                  v-if="node.childNodes && node.childNodes.length"
                  :class="{ 'is-close': !node.expanded }"
                  class="wk wk-up-unfold" />
              </flexbox>
            </el-tree>
          </div>
        </div>
      </div>
      <!-- 导航新增部门 -->
      <el-dialog
              v-loading="deptCreateLoading"
              :visible.sync="depCreateDialog"
              :close-on-click-modal="false"
              title="新增租户"
              :before-close="depCreateClose"
              width="30%">
        <div class="nav-dialog-div">
          <label>新增租户：</label>
          <el-input
                  v-model="depCreateLabelValue"
                  :maxlength="20"
                  placeholder="请输入内容" />
        </div>
        <div class="nav-dialog-div">
          <label>上级部门：</label>
          <el-input disabled placeholder="顶级租户" />
        </div>
        <span
                slot="footer"
                class="dialog-footer">
        <el-button @click="depCreateDialog = false">取 消</el-button>
        <el-button
                type="primary"
                @click="add">确 定</el-button>
      </span>
      </el-dialog>
      <!-- 右边内容 -->
      <div class="content-box">
        <el-tabs v-model="mainMenuIndex">
          <el-tab-pane
                  label="租户权限"
                  name="rule">
            <!-- 权限管理 -->
            <div
                v-loading="ruleLoading"
                class="jurisdiction-box">
              <el-button
                      size="small"
                      type="primary"
                      class="jurisdiction-edit"
                      @click="ruleSubmit"> 保存 </el-button>
              <el-tabs  v-model="ruleMenuIndex">
                <el-tab-pane
                        v-for="(item, index) in ruleMenuList"
                        :key="index"
                        :label="item.label"
                        :name="item.index">
                  <div
                          v-if="item.type == 'tree'"
                          :style="{ height: treeHeight + 'px'}"
                          class="jurisdiction-content">
                    <div class="jurisdiction-content-checkbox">
                      <el-tree
                              :data="item.data"
                              :ref="'tree' + item.index"
                              :indent="0"
                              :expand-on-click-node="false"
                              :props="defaultProps"
                              show-checkbox
                              node-key="menuId"
                              style="height: 0;"
                              empty-text=""
                              default-expand-all>
                        <!--<span
                            slot-scope="{ node }"
                            :class="{ 'node-label': node.level == 1 || node.level == 2} ">{{ node.label }}
                            <el-button
                                v-if="node.level == 2 && canSetField(node.data.realm)"
                                icon="wk wk-manage"
                                type="text"
                                @click="fieldSetClick(node)" >字段授权</el-button>
                        </span>-->
                      </el-tree>
                    </div>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
          </el-tab-pane>
          <el-tab-pane
                  label="基本信息"
                  name="baseInfo">
            <div
                v-loading="deptUpdateLoading"
                class="content-table" style="margin: 0 auto;padding:50px;width: 600px">
                  <div class="nav-dialog-div">
                    <label>租户名称：</label>
                    <el-input
                            v-model="roleActive.name"
                            :maxlength="20"
                            placeholder="请输入内容" />
                  </div>
                  <div class="nav-dialog-div">
                    <label>上级部门：</label>
                    <el-input disabled placeholder="顶级租户" />
                  </div>
                  <div class="nav-dialog-div">
                    <label>状&nbsp; &nbsp; &nbsp; &nbsp;态：</label>
                    <el-radio-group v-model="roleActive.status">
                      <el-radio
                              v-for="dict in deptStatusOptions"
                              :key="dict.dictValue"
                              :label="dict.dictValue"
                      >{{ dict.dictLabel }}</el-radio>
                    </el-radio-group>
                  </div>
                  <div class="nav-dialog-div">
                    <label>有效开始日期：</label>
                    <el-date-picker style="width:calc(100% - 110px)"
                            v-model="roleActive.effectiveStartDate"
                            value-format="yyyy-MM-dd"
                            type="date"
                            default-time="00:00:00"
                            placeholder="选择开始日期"/>
                  </div>
                  <div class="nav-dialog-div">
                    <label>有效结束日期：</label>
                    <el-date-picker style="width:calc(100% - 110px)"
                            v-model="roleActive.effectiveEndDate"
                            value-format="yyyy-MM-dd"
                            type="date"
                            default-time="23:59:59"
                            placeholder="选择结束日期"/>
                  </div>
<!--                  <div class="nav-dialog-div">-->
<!--                    <label>有效日期：</label>-->
<!--                    <el-date-picker style="width: calc(100% - 80px);"-->
<!--                            v-model="roleActive.effectiveDate"-->
<!--                            type="daterange"-->
<!--                            range-separator="至"-->
<!--                            start-placeholder="开始日期"-->
<!--                            end-placeholder="结束日期"-->
<!--                            :default-time="['00:00:00', '23:59:59']">-->
<!--                    </el-date-picker>-->
<!--                  </div>-->
                  <div
                      slot="footer" style="text-align: center"
                      class="dialog-footer">
                    <el-button
                            type="primary"
                            @click="update">保 存</el-button>
                    <el-button
                            type="normal" style="margin-left: 100px"
                            @click="deleteTenant">删 除</el-button>
                  </div>
            </div>
          </el-tab-pane>
          <el-tab-pane
                  label="员工列表"
                  name="user">
            <div class="content-table">
              <div style="float: right;margin-right: 20px;">
                <el-button type="text" icon="el-icon-circle-plus" @click="addAdmin">添加租户管理员</el-button>
              </div>
              <div class="flex-box">
                <el-table
                        id="userTable"
                        :data="tableData"
                        :height="tableHeight">
                  <el-table-column
                          prop="realname"
                          width="140"
                          show-overflow-tooltip
                          label="姓名">
                    <template slot-scope="scope">
                      <div class="status-name">
                        <div :style="{'background-color' : getStatusColor(scope.row.status)}" />
                        {{ scope.row.realname }}
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column
                          v-for="(item, index) in fieldList"
                          :key="index"
                          :prop="item.field"
                          :label="item.value"
                          :width="item.width"
                          :formatter="tableFormatter"
                          show-overflow-tooltip />
                  <el-table-column
                          fixed="right"
                          label="操作"
                          width="250">
                    <template slot-scope="scope">
<!--                      <el-button-->
<!--                              type="text"-->
<!--                              size="small"-->
<!--                              @click="handleClick('edit', scope)">编辑</el-button>-->
                      <el-button
                              type="text"
                              size="small"
                              @click="handleClick('delete', scope)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="p-contianer">
                  <div class="status-des">
                    <div
                            v-for="item in userStatusOptions"
                            :key="item.value"
                            class="status-des-item">
                      <div :style="{'background-color' : getStatusColor(item.value)}" />
                      {{ item.label }}
                    </div>
                  </div>
                  <el-pagination
                          :current-page="currentPage"
                          :page-sizes="pageSizes"
                          :page-size.sync="pageSize"
                          :total="total"
                          class="p-bar"
                          background
                          layout="prev, pager, next, sizes, total, jumper"
                          @size-change="handleSizeChange"
                          @current-change="handleCurrentChange" />
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane
                  label="应用配置"
                  name="config">
            <div class="content-table">
              <div style="float: right;margin-right: 20px;">
                <el-button type="text" icon="el-icon-circle-plus" @click="addConfig">添加配置</el-button>
              </div>
              <div class="flex-box">
                <el-table
                        id="configTable"
                        :data="tableData2"
                        :height="tableHeight">
                  <el-table-column
                          v-for="(item, index) in configList"
                          :key="index"
                          :prop="item.field"
                          :label="item.value"
                          :width="item.width"
                          :formatter="configTableFormatter"
                          show-overflow-tooltip />
                  <el-table-column
                          fixed="right"
                          label="操作"
                          width="120">
                    <template slot-scope="scope">
                      <el-button
                              type="text"
                              size="small"
                              @click="updateConfig(scope)">编辑</el-button>
                      <el-button
                              type="text"
                              size="small"
                              @click="deleteConfig(scope)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="p-contianer">
                  <el-pagination
                          :current-page="currentPage2"
                          :page-sizes="pageSizes2"
                          :page-size.sync="pageSize2"
                          :total="total2"
                          class="p-bar"
                          background
                          layout="prev, pager, next, sizes, total, jumper"
                          @size-change="handleSizeChange2"
                          @current-change="handleCurrentChange2" />
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
        <!-- 新建租户管理员 -->
        <el-dialog
                v-loading="loading"
                v-if="employeeCreateDialog"
                title="新建租户管理员"
                :visible.sync="employeeCreateDialog"
                :close-on-click-modal="false"
                :modal-append-to-body="true"
                :append-to-body="true"
                :before-close="newHandleClose"
                width="40%">
          <el-form
                  ref="dialogRef"
                  :inline="true"
                  :model="formInline"
                  :rules="dialogRules"
                  class="new-dialog-form"
                  label-width="80px"
                  label-position="top">
            <el-form-item
                    v-for="(item, index) in tableList"
                    :label="item.value"
                    :prop="item.field"
                    :key="index">
              <span slot="label">{{ item.value }}</span>
              <el-tooltip
                      v-if="item.tips"
                      slot="label"
                      :content="item.tips"
                      effect="dark"
                      placement="top">
                <i class="wukong wukong-help_tips" />
              </el-tooltip>
              <template v-if="item.type == 'select'">
                <el-select
                        v-model="formInline[item.field]"
                        filterable>
                  <el-option
                          v-for="optionItem in optionsList[item.field].list"
                          :key="optionItem.id"
                          :label="optionItem.name"
                          :value="optionItem.id" />
                </el-select>
              </template>
              <el-input
                      v-else
                      v-model="formInline[item.field]"
                      :maxlength="100"/>
            </el-form-item>
          </el-form>
          <span
                  slot="footer"
                  class="dialog-footer">
            <el-button
                    type="primary"
                    @click="newDialogSubmit">保 存</el-button>
            <el-button @click="employeeCreateDialog = false">取 消</el-button>
          </span>
        </el-dialog>
        <!-- 新建和编辑应用配置 -->
        <el-dialog
                v-loading="configLoading"
                v-if="configCreateDialog"
                title="应用配置管理"
                :visible.sync="configCreateDialog"
                :close-on-click-modal="false"
                :modal-append-to-body="true"
                :append-to-body="true"
                :before-close="configClose"
                width="30%">
          <el-form
                  ref="dialog2Ref"
                  :model="configFormInline"
                  :rules="dialog2Rules"
                  label-width="80px"
                  label-position="left">
            <el-form-item
                    v-for="(item, index) in configList"
                    v-if="item.field!='createTime'"
                    :label="item.value"
                    :prop="item.field"
                    :key="index">
              <span slot="label">{{ item.value }}</span>
              <el-tooltip
                      v-if="item.tips"
                      slot="label"
                      :content="item.tips"
                      effect="dark"
                      placement="top">
                <i class="wukong wukong-help_tips" />
              </el-tooltip>
              <template v-if="item.type == 'select'">
                <el-select v-if="item.field == 'type'" style="width: 100%"
                        v-model="configFormInline[item.field]"
                        filterable>
                  <el-option
                          v-for="dict in configOptions"
                          :key="dict.dictValue"
                          :label="dict.dictLabel"
                          :value="dict.dictValue"/>
                </el-select>
                <el-select v-if="item.field == 'messageType'" style="width: 100%"
                        v-model="configFormInline[item.field]" multiple filterable>
                  <el-option
                          v-for="dict in messageTypeOptions"
                          :key="dict.dictValue"
                          :label="dict.dictLabel"
                          :value="dict.dictValue"/>
                </el-select>
              </template>
              <template v-else-if="item.type == 'radio'">
                  <el-radio-group v-model="configFormInline[item.field]">
                    <el-radio
                            v-for="dict in statusOptions"
                            :key="dict.dictValue"
                            :label="dict.dictValue"
                    >{{ dict.dictLabel }}</el-radio>
                  </el-radio-group>
              </template>
              <el-input
                      v-else-if="item.type == 'textarea'"
                      v-model="configFormInline[item.field]"
                      type="textarea"
                      :rows="6"
                      :maxlength="1000"/>
              <el-input
                      v-else
                      v-model="configFormInline[item.field]"
                      :maxlength="100"/>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button
                    type="primary"
                    @click="configSubmit">保 存</el-button>
            <el-button @click="configCreateDialog = false">取 消</el-button>
          </span>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script>
import {
  depDeleteAPI,
  depEditAPI,
  userDelAPI
} from '@/api/admin/employeeDep'

import {
  updateTenantMenuAPI,
  systemRuleByTypeAPI,
  depListAPI,
  userListAPI,
  queryConfigList,
  getInfo,
  addOrUpdateConfig,
  delConfig,
  tenantAddAPI,
  tenantAdminAddAPI
} from '@/api/admin/tenant'

import { chinaMobileRegex, objDeepCopy } from '@/utils'
import XrHeader from '@/components/XrHeader'

export default {
  components: {
    XrHeader
  },
  data() {
    return {
      //   加载
      tenantLoading: false,
      // 右边导航
      depCreateDialog: false, // 控制部门新增 编辑 数据
      deptCreateLoading: false,
      depCreateLabelValue: '',
      showDepData: [],

      pid: '1',
      treeHeight: document.documentElement.clientHeight - 230, // 表的3度

      mainMenuIndex: 'rule',
      ruleMenuIndex: 'data',
      ruleMenuList: [],
      defaultProps: {
        children: 'childMenu',
        label: 'menuName'
      },
      // 选中的角色
      roleActive: {name:''},
      ruleLoading: false,
      deptUpdateLoading: false,
      // 租户状态数据字典
      deptStatusOptions: [],
      //员工列表
      userStatusOptions: [
        { value: '0', label: '禁用' },
        { value: '1', label: '正常' }
        // { value: '2', label: '正常' }
      ],
      fieldList: [
        { field: 'username', value: '手机号（登录名）', width: '160' },
        { field: 'sex', value: '性别', type: 'select', width: '80' },
        { field: 'email', value: '邮箱', width: '160' },
        // { field: 'deptName', value: '部门', type: 'select', width: '100' },
        { field: 'post', value: '岗位', width: '160' },
        {
          field: 'parentName',
          value: '直属上级',
          type: 'select',
          width: '160'
        },
        {
          field: 'roleName',
          value: '角色',
          type: 'selectCheckout',
          width: null
        }
      ],
      tableData: [],
      tableHeight: document.documentElement.clientHeight - 280, // 表的高度
      /** 分页逻辑 */
      currentPage: 1,
      pageSize: 15,
      pageSizes: [15, 30, 45, 60],
      total: 0,
      optionsList: {
        sex: {
          field: 'sex',
          list: [
            { id: 0, name: '请选择' },
            { id: 1, name: '男' },
            { id: 2, name: '女' }
          ]
        }
      },
      // 新建租户管理员
      loading: false,
      employeeCreateDialog: false,
      formInline: {},
      dialogRules: {
        realname: [
          { required: true, message: '姓名不能为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          {
            pattern: /^(?=.*[a-zA-Z])(?=.*\d).{6,20}$/,
            message: '密码由6-20位字母、数字组成'
          }
        ],
        username: [
          { required: true, message: '手机号码不能为空', trigger: 'blur' },
          {
            pattern: chinaMobileRegex,
            message: '目前只支持中国大陆的手机号码',
            trigger: 'blur'
          }
        ],
        email: [
          {
            pattern: /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/,
            message: '请输入正确的邮箱格式',
            trigger: 'blur'
          }
        ],
        // parentId: [
        //   { required: true, message: '直属上级不能为空', trigger: 'change' }
        // ],
        deptId: [
          { required: true, message: '部门不能为空', trigger: 'change' }
        ],
        roleId: [{ required: true, message: '角色不能为空', trigger: 'change' }]
      },
      //应用配置
      tableData2: [],
      currentPage2: 1,
      pageSize2: 15,
      pageSizes2: [15, 30, 45, 60],
      total2: 0,
      configLoading: false,
      configCreateDialog: false,
      //数据字典
      statusOptions: [],
      configOptions: [],
      messageTypeOptions: [],
      configFormInline: {},
      dialog2Rules: {
        type: [
          { required: true, message: '配置类型不能为空', trigger: 'blur' },
        ],
        name: [
          { required: true, message: '应用名称不能为空', trigger: 'blur' },
        ],
        params: [
          { required: true, message: '配置参数不能为空', trigger: 'blur' },
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'blur' },
        ],
        messageType: [
          { required: true, message: '消息类型不能为空', trigger: 'blur' },
        ]
      },
    }
  },
  created() {
    this.getDicts('sys_api_status').then(response => {
      this.deptStatusOptions = response.data
    })
    this.getDicts('sys_api_status').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('sys_api_config').then(response => {
      this.configOptions = response.data
    })
    this.getDicts('sys_message_type').then(response => {
      this.messageTypeOptions = response.data
    })
  },
  mounted() {
    /** 控制table的高度 */
    window.onresize = () => {
      this.tableHeight = document.documentElement.clientHeight - 280
      this.treeHeight = document.documentElement.clientHeight - 230
    }
    // 部门树形列表
    this.getDepTreeList()
  },
  computed: {
    /** 添加列表 */
    tableList: function() {
      return [
        { field: 'username', value: '手机号（登录名）' },
        { field: 'password', value: '登录密码' },
        { field: 'realname', value: '姓名' },
        { field: 'sex', value: '性别', type: 'select' },
        { field: 'email', value: '邮箱' },
        { field: 'post', value: '岗位' }
      ]
    },
    configList: function() {
      return [
        { field: 'name', value: '应用名称',width: '200' },
        { field: 'type', value: '配置类型', type: 'select',width: '200' },
        { field: 'messageType', value: '消息类型', type: 'select'},
        { field: 'status', value: '状态', type: 'radio',width: '80' },
        // { field: 'params', value: '配置参数', type: 'textarea',width: null},
        { field: 'createTime', value: '创建时间',width: '160' }
      ]
    }
  },
  methods: {
    // 获取树形列表
    getDepTreeList() {
      this.tenantLoading = true
      depListAPI()
              .then(response => {
                this.showDepData = response.data || []
                if(!this.roleActive.rules && this.showDepData.length>0){
                  this.roleActive = this.showDepData[0]
                }else{
                  this.showDepData.forEach((i,n) => {
                    if (this.showDepData[i].deptId==this.roleActive) {
                      this.roleActive = this.showDepData[i]
                    }
                  });
                }
                // this.roleActive.effectiveDate = [new Date(this.roleActive.effectiveStartDate),new Date(this.roleActive.effectiveEndDate)]
                this.tenantLoading = false
                this.getAllMenuByDept()
                this.getUserList()
                this.getConfigList()
              })
              .catch(() => {
                this.tenantLoading = false
              })
    },
    /**
     * 选择部门
     */
    changeDepClick(data) {
      this.roleActive = data
      // this.roleActive.effectiveDate = [new Date(),new Date()]
      // Vue.set(this.roleActive,'effectiveDate ', [new Date(this.roleActive.effectiveStartDate),new Date(this.roleActive.effectiveEndDate)] )
      // this.roleActive.effectiveDate = [new Date(this.roleActive.effectiveStartDate),new Date(this.roleActive.effectiveEndDate)]
      // console.log("==this.roleActive.effectiveDate="+JSON.stringify(this.roleActive.effectiveDate))
      this.getAllMenuByDept()
      this.getUserList()
      this.getConfigList()
    },

    /**
     * 创建部门
     */
    addStruc() {
      this.depCreateLabelValue = ''
      this.depCreateDialog = true
    },

    /**
     * 删除部门
     */
    deleteTenant() {
      this.$confirm(`此操作将删除${this.roleActive.name}租户，是否继续？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
          this.deptUpdateLoading = true
          depDeleteAPI({ id: this.roleActive.deptId })
                  .then(res => {
                    this.tenantLoading = true
                    depListAPI()
                            .then(response => {
                              this.showDepData = response.data || []
                              this.roleActive = this.showDepData[0]
                              this.tenantLoading = false
                              this.getAllMenuByDept();
                            })
                            .catch(() => {
                              this.tenantLoading = false
                            })
                    this.$message.success('删除成功')
                    this.deptUpdateLoading = false
                  })
                  .catch(() => {
                    this.deptUpdateLoading = false
                  })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },
    // 关闭新增或编辑
    depCreateClose() {
      this.depCreateDialog = false
    },
    // 新增或编辑确定按钮
    add(){
      this.deptCreateLoading = true
      tenantAddAPI({ name: this.depCreateLabelValue, pid: '1' })
            .then(
              res => {
                // this.getDepList() // 增加了新部门 刷新数据
                this.getDepTreeList()
                this.depCreateClose()
                this.deptCreateLoading = false
              })
              .catch(() => {
                this.deptCreateLoading = false
              })
    },
    update() {
      // if(this.roleActive.effectiveDate.length>1){
      //   this.roleActive.effectiveStartDate = this.roleActive.effectiveDate[0]
      //   this.roleActive.effectiveEndDate = this.roleActive.effectiveDate[1]
      // }
      // console.log("==this.roleActive.effectiveDate="+JSON.stringify(this.roleActive.effectiveDate))
      // console.log("===roleActive.effectiveDate==="+Object.prototype.toString.call(this.roleActive.effectiveDate).toLowerCase())
        this.deptUpdateLoading = true
        depEditAPI({
          name: this.roleActive.name,
          deptId: this.roleActive.deptId,
          status: this.roleActive.status,
          effectiveStartDate: this.roleActive.effectiveStartDate,
          effectiveEndDate: this.roleActive.effectiveEndDate,
          pid: '1'
        }).then(res => {
          this.$message.success('操作成功')
          this.deptUpdateLoading = false
          this.getDepTreeList()
          // this.depCreateClose()
        })
        .catch(() => {
          this.deptUpdateLoading = false
        })
    },
    /**
     * 获取租户下全部菜单
     */
    getAllMenuByDept() {
      if(!this.roleActive.deptId){
        return;
      }
      this.tenantLoading = true
      this.ruleMenuList = []
      //传参固定为0，获取全部菜单
      systemRuleByTypeAPI(this.roleActive.deptId).then(res => {
        if (res.data.all) {
          for(let i=0; i<res.data.all.length; i++){
            let menu = res.data.all[i];
            if(i===0){
              this.ruleMenuIndex = menu.realm;
            }
            this.ruleMenuList.push({
              label: menu.menuName,
              index: menu.realm,
              type: 'tree',
              value: [],
              data: [menu]
            })
          }
        } else {
          this.ruleMenuList = []
        }
        this.getDeptMenuRole();
        this.tenantLoading = false
      })
      .catch(() => {
        this.tenantLoading = false
      })
    },
    getDeptMenuRole() {
      if (this.roleActive.rules) {
        for (let index = 0; index < this.ruleMenuList.length; index++) {
          const element = this.ruleMenuList[index]
          if (element.type == 'tree') {
            // element.rules = this.getRoleRules(
            //         this.roleActive.rules[element.index],
            //         element.data[0]
            // )
            // element.rules = this.roleActive.rules[element.index]
            // console.log("==this.roleActive.rules："+JSON.stringify(this.roleActive.rules))
            this.$nextTick(() => {
              const treeRefs = this.$refs['tree' + element.index]
              if (treeRefs) {
                let treeRef;
                if (
                  Object.prototype.toString.call(treeRefs) == '[object Array]'
                ) {
                  treeRef = treeRefs[0]
                } else {
                  treeRef = treeRefs
                }
                treeRef.setCheckedKeys([])
                if(this.roleActive.rules && this.roleActive.rules[element.index]){
                  this.roleActive.rules[element.index].forEach((i,n) => {
                    var node = treeRef.getNode(i);
                    // console.log(node.isLeaf)
					setTimeout(function(){
					  if(node.isLeaf){
						  treeRef.setChecked(node, true);
					  }
					}, 500)
                  });
                }
              }

            })
          } else {
            element.value = this.roleActive.dataType
          }
        }
      }
    },
    /**
     * 获得check的实际数据

    getRoleRules(array, tree) {
      if (!array) {
        array = []
      }
      var hasRemove = false
      var copyArray = this.copyItem(array)
      for (
              let firstIndex = 0;
              firstIndex < tree.childMenu.length;
              firstIndex++
      ) {
        const firstItem = tree.childMenu[firstIndex]
        if (!firstItem.hasOwnProperty('children')) {
          if (firstItem.length + 1 != copyArray.length) {
            this.removeItem(copyArray, tree.id)
          }
          return copyArray
        }

        for (let index = 0; index < array.length; index++) {
          const element = array[index]
          var temps = []
          for (
                  let secondIndex = 0;
                  secondIndex < firstItem.childMenu.length;
                  secondIndex++
          ) {
            const secondItem = firstItem.childMenu[secondIndex]
            if (secondItem.id == element) {
              temps.push(secondItem)
            }
          }
          if (temps.length != firstItem.childMenu.length) {
            hasRemove = true
            this.removeItem(copyArray, firstItem.id)
          }
        }
      }

      if (hasRemove) {
        this.removeItem(copyArray, tree.id)
      }

      var checkedKey = []
      for (let index = 0; index < copyArray.length; index++) {
        const element = copyArray[index]
        if (element) {
          checkedKey.push(parseInt(element))
        }
      }
      return checkedKey
    },
    copyItem(array) {
      var temps = []
      for (let index = 0; index < array.length; index++) {
        temps.push(array[index])
      }
      return temps
    },
    removeItem(array, item) {
      var removeIndex = -1
      for (let index = 0; index < array.length; index++) {
        if (item == array[index]) {
          removeIndex = index
          break
        }
      }
      if (removeIndex > 0) {
        array.splice(removeIndex, 1)
      }
    },
    containItem(array, item) {
      for (let index = 0; index < array.length; index++) {
        if (item == array[index]) {
          return true
        }
      }
      return false
    },*/

    // 权限提交
    ruleSubmit() {
      this.ruleLoading = true

      let rules = []
      let infoData = ''
      for (let index = 0; index < this.ruleMenuList.length; index++) {
        const element = this.ruleMenuList[index]
        if (element.type == 'tree') {
          const treeRefs = this.$refs['tree' + element.index]
          if (treeRefs) {
            if (Object.prototype.toString.call(treeRefs) == '[object Array]') {
              rules = rules.concat(treeRefs[0].getCheckedKeys()).concat(treeRefs[0].getHalfCheckedKeys())
            } else {
              rules = rules.concat(treeRefs.getCheckedKeys()).concat(treeRefs.getHalfCheckedKeys())
            }
          }
        } else {
          infoData = element.value
        }
      }

      updateTenantMenuAPI({
        menuIds: rules,
        dataType: infoData,
        deptId: this.roleActive.deptId,
      })
      .then(res => {
        this.getDepTreeList()
        this.$message.success('保存成功')
        this.ruleLoading = false
      })
      .catch(() => {
        this.ruleLoading = false
      })
    },

    /**
     * 用户列表
     */
    getUserList() {
      // this.loading = true
      const params = {
        page: this.currentPage,
        limit: this.pageSize,
        deptId: this.roleActive.deptId
      }
      userListAPI(params)
        .then(res => {
          this.tableData = res.data.list
          this.total = res.data.totalRow
          // this.loading = false
        })
        .catch(() => {
          // this.loading = false
        })
    },

    // 获取状态颜色 0 禁用 1 启用 2未激活
    getStatusColor(status) {
      if (status == 0) {
        return '#FF6767'
      } else if (status == 1) {
        return '#46CDCF'
      } else if (status == 2) {
        return '#CCCCCC'
      }
    },
    // 列表信息格式化
    tableFormatter(row, column) {
      if (column.property == 'sex') {
        return { 1: '男', 2: '女' }[row.sex]
      }
      return row[column.property]
    },
    /**
     * 更改每页展示数量
     */
    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 1
      this.getUserList()
    },
    /**
     * 更改当前页数
     */
    handleCurrentChange(val) {
      this.currentPage = val
      this.getUserList()
    },
    // 新建租户管理员
    newHandleClose() {
      this.employeeCreateDialog = false
    },
    addAdmin() {
      this.formInline = {
        deptId:this.roleActive.deptId
      }
      this.employeeCreateDialog = true
    },
    // 用户新建
    newDialogSubmit() {
      this.$refs.dialogRef.validate(valid => {
        if (valid) {
          this.loading = true
          const formInline = objDeepCopy(this.formInline)
          tenantAdminAddAPI(formInline)
                  .then(res => {
                    this.$message.success('新增成功')
                    this.employeeCreateDialog = false
                    this.getUserList()
                    this.loading = false
                  })
                  .catch(() => {
                    this.loading = false
                  })
        } else {
          // 提示第一个error
          if (this.$refs.dialogRef.fields) {
            for (
                    let index = 0;
                    index < this.$refs.dialogRef.fields.length;
                    index++
            ) {
              const ruleField = this.$refs.dialogRef.fields[index]
              if (ruleField.validateState == 'error') {
                this.$message.error(ruleField.validateMessage)
                break
              }
            }
          }
          return false
        }
      })
    },
    handleClick(type, scope) {
      if (type === 'delete') {
        userDelAPI(scope.row.userId).then(res => {
          this.$message.success('删除成功')
          this.getUserList()
        })
      }
    },

    /**
     * 应用配置列表
     */
    getConfigList() {
      const params = {
        page: this.currentPage2,
        limit: this.pageSize2,
        tenantId: this.roleActive.deptId
      }
      queryConfigList(params).then(res => {
          this.tableData2 = res.data.list
          this.total2 = res.data.totalRow
        })
    },
    // 列表信息格式化
    configTableFormatter(row, column) {
      if (column.property == 'type') {
        return this.selectDictLabel(this.configOptions, row.type)
      }
      if (column.property == 'messageType') {
        return this.selectDictLabels(this.messageTypeOptions, row.messageType)
      }
      if (column.property == 'status') {
        return this.selectDictLabel(this.statusOptions, row.status)
      }
      return row[column.property]
    },
    /**
     * 更改每页展示数量
     */
    handleSizeChange2(val) {
      this.pageSize2 = val
      this.currentPage2 = 1
      this.getConfigList()
    },
    /**
     * 更改当前页数
     */
    handleCurrentChange2(val) {
      this.currentPage2 = val
      this.getConfigList()
    },
    configClose() {
      this.configCreateDialog = false
    },
    addConfig() {
      this.configFormInline = {
        tenantId:this.roleActive.deptId,
        status:'1'
      }
      this.configCreateDialog = true
    },
    updateConfig(scope){
      getInfo(scope.row.id).then(res => {
        this.configFormInline = res.data
        this.configFormInline.messageType = res.data.messageType
                ? res.data.messageType.split(',') : []
        this.configCreateDialog = true
      })
    },
    deleteConfig(scope){
      this.$confirm(`此操作将删除应用配置？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delConfig({id:scope.row.id,tenant_id:this.roleActive.deptId}).then(res => {
          this.$message.success('删除成功')
          this.getConfigList()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    configSubmit() {
      this.$refs.dialog2Ref.validate(valid => {
        if (valid) {
          this.configLoading = true
          const formInline = objDeepCopy(this.configFormInline)
          if(formInline.messageType){
            formInline.messageType = formInline.messageType.join(',')
          }
          addOrUpdateConfig(formInline)
              .then(res => {
                if(res.code == 0){
                  this.$message.success('操作成功')
                  this.configCreateDialog = false
                  this.getConfigList()
                  this.configLoading = false
                }else {
                  this.$message.error(res.msg)
                  this.configLoading = false
                }
              })
              .catch(() => {
                this.configLoading = false
              })
        } else {
          // 提示第一个error
          if (this.$refs.dialog2Ref.fields) {
            for (
                    let index = 0;
                    index < this.$refs.dialog2Ref.fields.length;
                    index++
            ) {
              const ruleField = this.$refs.dialog2Ref.fields[index]
              if (ruleField.validateState == 'error') {
                this.$message.error(ruleField.validateMessage)
                break
              }
            }
          }
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>

.role-authorization {
  padding: 0 15px;
  height: 100%;
  box-sizing: border-box;
  overflow: hidden;
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
// .el-tree /deep/ .el-tree-node.is-current > .el-tree-node__content {
//   background-color: #ebf3ff;
//   border-right: 2px solid #46cdcf;
//   .node-label-set {
//     display: block;
//   }
// }


/* 详情 */
.employee-dep-management /deep/ .el-dialog__wrapper {
  margin-top: 60px !important;
}

.el-form {
  padding: 0;
}

/* 新建和编辑 */
.new-dialog-form {
  height: 40vh;
  overflow-y: auto;
  padding: 20px;
}
.new-dialog-form /deep/ .el-form-item {
  width: 50%;
  margin: 0;
  padding-bottom: 10px;
}
.new-dialog-form /deep/ .el-form-item .el-form-item__label {
  padding: 0;
}
.new-dialog-form {
/deep/ .el-form-item:nth-child(even) {
  padding-left: 15px;
}

/deep/ .el-form-item:nth-child(odd) {
  padding-right: 15px;
}
}
.nav-dialog-div {
  margin-bottom: 20px;
}
.nav-dialog-div {
.el-input,
.el-select {
  width: calc(100% - 80px);
}
}
/*-----------------*/

.role-box {
  height: calc(100% - 60px);
  overflow: hidden;
  position: relative;
}
.nav {
  width: 280px;
  background: #fff;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  overflow: auto;
}
.nav__hd {
  position: relative;
  padding: 15px;
  font-size: 16px;
  font-weight: 600;
  border-bottom: 1px solid $xr-border-line-color;

  .el-button {
    position: absolute;
    top: 10px;
    right: 15px;
  }
}

.content-box {
  background: #fff;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
  margin-left: 295px;
  height: 100%;
  overflow: hidden;
  padding-top: 10px;
  position: relative;
}
.content-table {
  overflow: hidden;
}
.content-table > .el-button {
  float: right;
  margin-bottom: 15px;
  margin-right: 30px;
}
.content-box .content-table-span {
  color: #2362FB;
  margin-left: 5px;
  cursor: pointer;
}

.content-table-header {
  padding: 0 15px 5px;
  .content-table-header-reminder {
    flex: 1;
    margin-right: 5px;
  }
}

/* 权限管理 */
.jurisdiction-content {
  height: calc(100% - 61px);
  position: relative;
  overflow: hidden;
}
.content-left {
  height: 100%;
  margin-right: 250px;
  overflow: hidden;
}
.content-right {
  width: 250px;
  position: absolute;
  top: 0;
  right: 0;
  height: 100%;
}
.jurisdiction-box {
  padding-bottom: 15px;
  height: calc(100% - 61px);
  overflow: hidden;
  position: relative;
}
.jurisdiction-content-checkbox {
  border-right: 1px dashed $xr-border-line-color;
  height: calc(100% - 47px);
  overflow-y: scroll;
  padding: 20px;

  /deep/ .el-tree-node__content:hover {
    background-color: white;
    color: $xr-color-primary;
  }
}
.jurisdiction-content-checkbox
  .el-tree
  /deep/
  .el-tree-node
  > .el-tree-node__content {
  margin-bottom: 20px;
  width: 150px;
}
.jurisdiction-content-checkbox /deep/ .el-tree .el-tree-node {
  white-space: inherit;
  margin-bottom: 5px;
}
.jurisdiction-content-checkbox
  /deep/
  .el-tree
  > .el-tree-node
  > .el-tree-node__children
  > .is-expanded
  > .el-tree-node__children
  > .is-expanded {
  display: inline-block;
}
.role-authorization /deep/ .el-tree-node__expand-icon {
  display: none;
}
.data-radio {
  padding: 5px 30px;
}
.data-radio .el-radio {
  display: block;
  margin: 20px 0;
}
/* 新建角色 */
.input-role {
  padding: 10px 0 20px;
  width: 100%;
}
.role-nav-box {
  line-height: 30px;
  overflow-y: auto;
  padding: 20px 0;
  height: calc(100% - 50px);
}
// 菜单
.menu-item {
  color: #333;
  font-size: 13px;
  padding: 0 15px;
  height: 40px;
  line-height: 40px;
  cursor: pointer;
  position: relative;
  .icon-close {
    position: absolute;
    top: 0;
    right: 8px;
    z-index: 1;
    display: none;
  }
}

.menu-item:hover,
.menu-item.is-select {
  background-color: $xr--background-color-base;
}

.menu-item:hover::before,
.menu-item.is-select::before {
  content: ' ';
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  width: 2px;
  background-color: #5383ed;
}

.role-nav-box .menu-item:hover .icon-close {
  display: block;
  float: right;
}
.jurisdiction-edit {
  text-align: right;
  padding: 10px 30px;
  position: absolute;
  top: 70px;
  right: 20px;
  z-index: 3;
}
.status-des {
  font-size: 12px;
  color: #777777;
  margin: 0 5px;
  position: absolute;
  left: 0;
  top: 7px;
  .status-des-item {
    margin: 8px;
    display: inline-block;
    div {
      display: inline-block;
      width: 6px;
      height: 6px;
      border-radius: 3px;
      margin-right: 5px;
    }
  }
}
/** 分页布局 */
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

// .el-tabs /deep/ .el-tabs__nav-wrap::after {
//   display: none !important;
// }

.el-tabs /deep/ .el-tabs__header {
  padding: 0 17px;
  margin: 0 0 15px !important;
}

.el-tabs /deep/ .el-tabs__item {
  font-size: 13px !important;
  height: 40px !important;
  line-height: 40px !important;
}

.node-label {
  font-weight: bold;
  font-size: 15px;
  position: relative;
  .el-button {
    position: absolute;
    top: -8px;
    right: -80px;
    /deep/ span {
      margin-left: 3px;
    }
  }
}
/* 设置占位 */
.flex-box {
  flex: 1;
// border-bottom: 1px solid $xr-border-line-color;
}
.new-dialog-form /deep/ .el-select {
  display: block;
}

/** 分页布局 */
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
@import '../styles/table.scss';
</style>
