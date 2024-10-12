<template>
  <div>
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
             style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
      <el-form-item label="事件编号">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入事件编号"
          clearable
          size="small"
          @keyup.enter.native="getList"
        />
      </el-form-item>
      <el-form-item label="报警类型">
        <el-select v-model="queryParams.alertType" clearable placeholder="请选择报警类型">
          <el-option
            v-for="item in dictData.monitorTypeDict"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="报警时间">
        <el-date-picker
          v-model="queryParams.alertTimeStr"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 头部：搜索、操作 end -->
    <div
      v-empty="!canHandle('list')"
      xs-empty-icon="nopermission"
      xs-empty-text="暂无权限"
    >
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
        style="width: 100%"
        @selection-change="handleSelectionChange">
        <el-table-column
          show-overflow-tooltip
          type="selection"
          align="center"
          width="55"/>
		<el-table-column label="序号" type="index" show-overflow-tooltip width="70">
		</el-table-column>
        <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          :formatter="fieldFormatter"
          :sortable="false"
          :align="item.align"
          show-overflow-tooltip>

        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="detailsClick(scope.row)"
            >详情
            </el-button>
            <el-button
              v-if="scope.row.handleBy == null"
              size="mini"
              type="text"
              @click="handleClick(scope.row)"
            >处理
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <el-pagination
          :current-page="currentPage"
          :page-sizes="pageSizes"
          :page-size.sync="pageSize"
          :total="total"
          :pager-count="5"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div>
      <!-- 处置对话框 -->
      <el-dialog title="处置" :visible.sync="xqopen" width="900px" append-to-body>
        <el-form ref="form" :model="form"  :rules="rules"  label-width="160px" class="add-form">
          <create-sections title="抓拍图片">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../p1.jpg" width="600px"/>
          </create-sections>
          <create-sections title="基本信息">
          <el-row>
            <el-col :span="12">
              <el-form-item label="报警编号" >
                <el-input v-model="form.code" disabled placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="报警时间" >
                <el-input v-model="form.alertTime" disabled placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="报警位置"  class="add-form-input">
                <el-input v-model="form.alertPosition" disabled placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="报警类型"  class="add-form-input">
				<el-select v-model="form.alertType" disabled placeholder="请选择报警类型">
				  <el-option
					v-for="item in dictData.monitorTypeDict"
					:key="item.dictValue"
					:label="item.dictLabel"
					:value="item.dictValue">
				  </el-option>
				</el-select>
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="报警内容" class="add-form-input">
                <el-input v-model="form.alertContent" disabled type="textarea" placeholder="请输入" />
              </el-form-item>
            </el-col>
<!--            <el-col :span="24">-->
<!--              <el-form-item label="报警数据" class="add-form-input">-->
<!--&lt;!&ndash;                <el-input v-model="form.alertData" disabled type="textarea" placeholder="请输入" />&ndash;&gt;-->
<!--              </el-form-item>-->
<!--            </el-col>-->
            <el-col :span="12">
              <el-form-item label="处置人"  class="add-form-input">
                <el-input v-model="form.handleBy" disabled placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="处置时间"  class="add-form-input">
                <el-input v-model="form.handleTime" disabled placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="处置内容" prop="handleContent"  class="add-form-input">
                <el-input v-model="form.handleContent" disabled type="textarea" placeholder="" />
              </el-form-item>
            </el-col>
          </el-row>
          </create-sections>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancel">关闭</el-button>
        </div>
      </el-dialog>
      <!-- 处置对话框 -->
      <el-dialog title="详情" :visible.sync="open" width="900px" append-to-body>
        <el-form ref="form" :model="form"  :rules="rules"  label-width="160px" class="add-form">
          <create-sections title="抓拍图片">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../p1.jpg" width="600px"/>
          </create-sections>
          <create-sections title="基本信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="报警编号" >
                  <el-input v-model="form.code" disabled placeholder="请输入" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报警时间" >
                  <el-input v-model="form.alertTime" disabled placeholder="请输入" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报警位置"  class="add-form-input">
                  <el-input v-model="form.alertPosition" disabled placeholder="请输入" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报警类型"  class="add-form-input">
					<el-select v-model="form.alertType" disabled placeholder="请选择报警类型">
					  <el-option
						v-for="item in dictData.monitorTypeDict"
						:key="item.dictValue"
						:label="item.dictLabel"
						:value="item.dictValue">
					  </el-option>
					</el-select>
                </el-form-item>
              </el-col>

              <el-col :span="24">
                <el-form-item label="报警内容" class="add-form-input">
                  <el-input v-model="form.alertContent" disabled type="textarea" placeholder="请输入" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="报警数据" class="add-form-input">
                  <el-input v-model="form.alertData" disabled type="textarea" placeholder="请输入" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="处置内容" prop="handleContent"  class="add-form-input">
                  <el-input v-model="form.handleContent" type="textarea" placeholder="请输入" />
                </el-form-item>
              </el-col>
            </el-row>
          </create-sections>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">关闭</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'//权限
  import Lockr from 'lockr'//分页
  import {getList, getById} from "@/views/build/ai/api/videoAlert";
  import CreateSections from '@/components/CreateSections'
  import request from '@/utils/request'
  export default {
    /** DEMO列表 */
    name: 'aiVideoAlert',
    components: {
      CreateSections
    },
    data() {
      return {
        businessType: 10000,
        loading: false, // 加载动画
        /** 列表 */
        tableHeight: document.documentElement.clientHeight - 280, // 表的高度
        list: [],
        fieldList: [//表头
          {
            prop: "code",
            label: "事件编号",
            //width: "250"
          },
          {
            prop: "alertTime",
            label: "报警时间",
            //width: "250"
          },
          {
            prop: "alertType",
            label: "报警类型",
            //width: "250"
          },
          {
            prop: "alertPosition",
            label: "报警位置",
            //width: "250"
          },
          {
            prop: "alertContent",
            label: "报警内容",
            //width: "250"
          },
          {
            prop: "handleBy",
            label: "处理人",
            //width: "250"
          },
          {
            prop: "handleTime",
            label: "处理事件",
            //width: "250"
          },
          {
            prop: "handleContent",
            label: "处理内容",
            //width: "250"
          },

        ],
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 40, 80, 100],
        total: 0,
        queryParams:{},
        /** 新增*/
        createShow: false,
        /***/
        action: {type: 'save', data: null},
        /** 勾选行 */
        selectionList: [], // 勾选数据 用于全局导出
        /** 控制详情展示 */
        rowID: '', // 行信息,

        // 字典数据或关联下拉框数据
        dictData: {
          // 统计类型字典项
          statisticalTypeDict: [],
          // 监测项类型字典项
          monitorTypeDict: [],
        },
        //详情
        id:null,
        open: false,
        xqopen: false,
        form: {},
        rules: {
          handleContent: [
            { required: true, message: '必填项', trigger: 'change' }
          ]
        }
      }
    },
    computed: {
      //权限 start
      ...mapGetters(['allAuth']),
      //权限 end
      titleIcon() {
        return require(`@/assets/img/crm/customer.png`)
      },
    },
    mounted() {
      /** 控制table的高度 */
      window.onresize = () => {
        this.updateTableHeight()
      }
      // 获取字典数据
      this.initDicts()
      //获取列表数据
      this.getList()
    },
    methods: {
      /** 获取列表数据 */
      getList() {
        this.list =[
          {
            "id": 3,
            "code": "003",
            "alertTime": "2021-06-25 15:15:27",
            "alertType": "视频监控",
            "alertPosition": "A座停车场B-302车位",
            "alertContent": "违规停车",
            "handleBy": null,
            "handleContent": null,
            "handleTime": null,
            "createTime": null,
            "createUserId": null,
            "updateTime": "2021-05-25 17:55:43",
            "updateUserId": null,
            "tenantId": 1,
            "projectId": 4
          },
          {
            "id": 1,
            "code": "001",
            "alertTime": "2021-05-25 15:15:27",
            "alertType": "视频监控",
            "alertPosition": "C座A栋3F南走廊",
            "alertContent": "视频检测到未授权人员进入",
            "handleBy": "逄震",
            "handleContent": "派人前往劝阻",
            "handleTime": "2021-05-26 14:16:25",
            "createTime": null,
            "createUserId": null,
            "updateTime": "2021-05-25 17:55:43",
            "updateUserId": null,
            "tenantId": 1,
            "projectId": 4
          }
        ]
      },
      /**
       *更多操作点击
       */
      detailsClick(data){
        this.form = {};
        this.xqopen = true;
        this.form = data
        this.id = data.id;
        console.log(data)
      },
      handleClick(data){
        this.form = {};
        this.open = true;
        this.form = data
        this.id = data.id;
      },
      submitForm(){
        this.$refs['form'].validate(valid => {
          if (valid) {
            this.form.id = this.id;
            request({
              url: 'build/aiVideoAlert/update',
              method: 'post',
              data: this.form,
              headers: {
                'Content-Type': 'application/json;charset=UTF-8'
              }
            }).then(response => {
              if (response.code === 0) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              }
            })
          }
        })
      },
      cancel(){
        this.open = false;
        this.xqopen = false;
      },
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
        // if (column.property === 'alertType') {
        //   return this.selectDictLabel(this.dictData.monitorTypeDict, row.alertType)
        // } else
          if (column.property === 'statisticalType') {
          return this.selectDictLabel(this.dictData.statisticalTypeDict, row.statisticalType)
        }
        return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
      },
      //按钮操作权限
      canHandle(type) {
        return true;
        //return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
      },
      /** 勾选操作 */
      // 当选择项发生变化时会触发该事件
      handleSelectionChange(val) {
        this.selectionList = val // 勾选的行
        this.updateTableHeight();
      },

      // 更改当前页数
      handleCurrentChange(val) {
        this.currentPage = val
        this.getList()
      },

      // 更改每页展示数量
      handleSizeChange(val) {
        Lockr.set('crmPageSizes', val)
        this.pageSize = val
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams = {};
        this.getList()
      },
      /**
       * 更新表高
       */
      updateTableHeight() {
        var offsetHei = document.documentElement.clientHeight
        var removeHeight = this.selectionList.length > 0 ? 370 : 260
        this.tableHeight = offsetHei - removeHeight
      },
      // 初始化字典数据
      initDicts() {
        this.getDicts('b_equipment_statistical_type').then(res => {
          this.dictData.statisticalTypeDict = res.data
        })
        this.getDicts('b_video_alert_type').then(res => {
          this.dictData.monitorTypeDict = res.data
        })
      },
    }
  }
</script>

<style lang="scss" scoped>
  //表头
  .table-head-container {
    //border-bottom: 1px solid #e6e6e6;
    border-top: 1px solid #e6e6e6;
    background-color: white;
  }

  .th-container {
    font-size: 13px;
    height: 50px;
    padding: 0 20px;

    .scene-select {
      width: 180px;
      /deep/ .el-input__inner {
        cursor: pointer;
      }
    }

    .filter-button {
      margin-left: 20px;
      /deep/ i {
        font-size: 14px;
        margin-right: 5px;
      }
    }
  }

  /** 场景和筛选 */
  .condition_title {
    cursor: pointer;
  }

  .condition_title:hover {
    color: $xr-color-primary;
  }

  .m-arrow {
    margin: 0 8px;
  }

  .c-filtrate {
    margin: 0 10px 0 30px;
    width: 12px;
  }

  /** 勾选操作 */
  .selection-bar {
    font-size: 12px;
    height: 50px;
    padding: 0 20px;
    color: #777;

    .selected—title {
      flex-shrink: 0;
      padding-right: 20px;
      color: #333;
      .selected—count {
        color: $xr-color-primary;
      }
    }
  }

  .selection-items-box {
    overflow-x: auto;
    overflow-y: hidden;
    padding: 0 15px;

    .el-button {
      color: #666;
      background-color: #f9f9f9;
      border-color: #e5e5e5;
      font-size: 12px;
      height: 28px;
      border-radius: 4px;
      /deep/ i {
        font-size: 12px;
        margin-right: 5px;
      }
    }

    .el-button--primary:hover {
      background: $xr-color-primary;
      border-color: $xr-color-primary;
      color: #ffffff;
    }

    .el-button + .el-button {
      margin-left: 15px;
    }
  }

  //页面头部
  .c-container {
    height: 60px;
    position: relative;
    //z-index: 100;
    .title-icon {
      width: 30px;
      height: 30px;
      margin-right: 10px;
    }

    .title {
      float: left;
      width: auto;
      padding-left: 28px;
      font-size: 16px;
      font-weight: 600;
      margin-top: 15px;
    }
    .sc-container {
      width: 300px;
      margin: -16px 0 0 -150px;
      position: absolute;
      left: 50%;
      top: 50%;
    }

    .el-input {
      /deep/ .el-input-group__append {
        background-color: $xr-color-primary;
        border-color: $xr-color-primary;
        color: white;
      }
    }

    .right-container {
      margin-right: -10px;
      float: right;
      margin: 15px 20px 0 0;
      position: relative;

      .dup-check-btn,
      .xr-btn--orange {
        margin-left: 0;
        margin-right: 10px;
      }

      .right-item {
        float: left;
        margin-right: 10px;
        padding: 8px 10px;
        font-size: 13px;
        border-radius: 2px;
      }
    }
  }

  @import '@/views/styles/table.scss';
</style>
