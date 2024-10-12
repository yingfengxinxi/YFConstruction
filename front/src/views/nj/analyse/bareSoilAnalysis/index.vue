<template>
  <div>
<!--    <div class="c-container">-->
<!--      <flexbox v-if="!$slots.icon" class="title"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'   -->
<!--        :src="titleIcon"-->
<!--        class="title-icon">质量方案管理-->
<!--      </flexbox>-->
<!--    </div>-->
    <!-- 头部：搜索、操作 start -->
    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
             style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
      <el-form-item label="项目名称" prop="projectId">
        <el-select
          v-model="queryParams.projectId"
          placeholder="请选择"
          clearable
          size="small"
          style="width: 200px"
        >
          <el-option
            v-for="item in projectInfoList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
	  <el-form-item label="报警类型" prop="alarmType">
	    <el-select
	      v-model="queryParams.alarmType"
	      placeholder="请选择"
	      clearable
	      size="small"
	      style="width: 200px"
	    >
	      <el-option
	        v-for="item in dictData.alarmTypeDict"
	        :key="item.dictValue"
	        :label="item.dictLabel"
	        :value="item.dictValue"
	      />
	    </el-select>
	  </el-form-item>
		<el-form-item label="报警位置">
			<el-input
			  v-model="queryParams.alarmLocation"
			  placeholder="请输入报警位置"
			  clearable
			  size="small"
			  @keyup.enter.native="getList"
			/>
		</el-form-item>
		<el-form-item label="报警时间">
		  <el-date-picker
		  v-model="executeDates"
		  type="datetimerange"
		  range-separator="至"
		  start-placeholder="开始日期"
		  end-placeholder="结束日期"
		  value-format="yyyy-MM-dd HH:mm:ss"
		  :default-time="['00:00:00', '23:59:59']">
		  </el-date-picker>
		</el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 头部：搜索、操作 end -->
    <div class="table-head-container">

      <el-table
        v-loading="loading"
        id="crm-table"
        :data="list"
        :height="tableHeight"
        :cell-class-name="cellClassName"
        class="n-table--border"
        stripe
        border
        highlight-current-row
        style="width: 100%"
		@sort-change="sortChange"
        @row-click="handleRowClick">

        <el-table-column label="序号" type="index" align='left' show-overflow-tooltip width="70">
        </el-table-column>
        <el-table-column label="项目名称" align="left" prop="projectName" sortable="custom" />
        <el-table-column label="报警位置" align="left" prop="alarmLocation" sortable="custom" />
		<el-table-column label="报警类型" :formatter="alarmTypeFormatter" align="left" prop="alarmType" sortable="custom" />
		<el-table-column label="报警内容" align="left" prop="alarmContent" sortable="custom" />
		<el-table-column label="报警时间" align="left" prop="alarmTime" sortable="custom" />
		<el-table-column label="报警图片" align="left" width="120px">
              <template slot-scope="scope">
                  <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="getImgSrc(scope.row)" style="height: 53px" @click="fileSelect(getImgSrc(scope.row))"/>
              </template>
		</el-table-column>
        <el-table-column label="报警视频" align="left" prop="videoUrl">
          <template slot-scope="scope">
          <!--  <a :href="scope.row.videoUrl" style="color: #1B65FA;text-decoration:underline">视频录像</a>-->
            <a @click="ts(scope.row)" style="color: #1B65FA;text-decoration:underline">视频录像</a>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="left" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >查看</el-button>
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
    </div>
    <!-- 新增或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form"  label-width="100px" class="add-form">
        <el-row>
          <el-col :span="12">
            <el-form-item label="项目名称" prop="projectId" class="add-form-input">
              <el-select
                v-model="form.projectId"
                :disabled="!isAble"
                placeholder="请选择"
                clearable
                size="small"
                style="width: 100%"
              >
                <el-option
                  v-for="item in projectInfoList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报警位置" prop="alarmLocation" class="add-form-input">
              <el-input v-model="form.alarmLocation" :disabled="!isAble"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="报警类型" prop="alarmType" class="add-form-input">
              <el-select
                v-model="dictData.alarmTypeDict"
                :disabled="!isAble"
                placeholder="请选择"
                clearable
                size="small"
                style="width: 100%"
              >
                <el-option
                  v-for="dict in dictData.catchTypeDict"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报警内容" prop="alarmContent" class="add-form-input">
              <el-input v-model="form.alarmContent" :disabled="!isAble"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="报警时间" prop="alarmTime" class="add-form-input">
              <el-input v-model="form.alarmTime" :disabled="!isAble"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="报警图片" prop="alarmImg" class="add-form-input">
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="getImgSrc(form)" style="height: 53px" @click="fileSelect(getImgSrc(form))"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="报警视频" prop="videoUrl" class="add-form-input">
              <!--<el-link :href="form.videoUrl" target="_blank">下载视频</el-link>-->
              <el-link @click="ts()" target="_blank">下载视频</el-link>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
        <el-dialog title="视频查看" :visible.sync="videoOpen" width="900px">
			<video id="video" ref='videoId' controls width="860" height="480" :src="videoUrl" type="video/mp4">
			</video>
    </el-dialog>
	<!-- 图片预览 -->
	<el-image-viewer
		v-if="fileimage"
		:on-close="()=>{fileimage=false,filepaths=''}"
		:url-list="[filepaths]" />
  </div>
</template>

<script>

  import {mapGetters} from 'vuex'//权限
  import Lockr from 'lockr'//分页
  import {wkPreviewFile,sortColumnNoToLowerCase } from '@/utils'

  import {getList,queryProjectList,selectById} from "../api/bareSoilAnalysis";


  export default {
    /** DEMO列表 */
    name: 'dregCarCleaning',
    components: {
		'el-image-viewer':()=>import('element-ui/packages/image/src/image-viewer')
    },
    data() {
      return {
      	conName:'',
        loading: false, // 加载动画
        /** 列表 */
        tableHeight: document.documentElement.clientHeight - 280, // 表的高度
        list: [],
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 40, 80, 100],
        total: 0,
        queryParams: {},
        // 项目信息列表
        projectInfoList: [],
		// 查询时间
		executeDates: '',
		// 表单参数
		form: {},
		// 弹出层标题
		title: '',
		// 是否显示弹出层
		open: false,
		isAble: false,
		// 字典数据
		dictData:{
			// 裸土报警类型
			alarmTypeDict: [],
		},
		// 图片预览
		fileimage: false, // 预览图片是否显示
		filepaths: '', // 图片列表
		videoOpen:false,
		videoUrl:''
      }
    },
    computed: {
      //权限 start
      ...mapGetters(['allAuth']),
      //权限 end
      titleIcon() {
        return require(`@/assets/img/crm/customer.png`)
      },
        getImgSrc() {
            return function(data){
                return data.alarmImg==null?data.photoUrl:data.alarmImg
            }
        },
    },
	created(){

	},
	mounted() {
		// 加载字典项
		this.initDicts()
		//获取列表数据
		this.getList()
		this.getProjectInfoList();
	},
	watch:{

	},
    methods: {
	  /** 获取列表数据 */
      getList() {
        this.loading = true
        //分页
        this.queryParams.page = this.currentPage;
        this.queryParams.limit = this.pageSize;

		// 执行时间范围
		if (this.executeDates != '' && this.executeDates != null) {
			this.queryParams.startTime = this.executeDates[0]
			this.queryParams.endTime = this.executeDates[1]
		} else {
		  this.queryParams.startTime = null
		  this.queryParams.endTime = null
		}

        getList(this.queryParams)
          .then(res => {
            this.list = res.data.list;
            if (res.data.totalRow && Math.ceil(res.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1) {
              this.currentPage = this.currentPage - 1
              this.getList()
            } else {
              this.total = res.data.totalRow
              this.loading = false
            }
            this.loading = false
          })
          .catch(() => {
            this.loading = false
          })
      },
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
		 if (column.property === 'carType') {
			return this.selectDictLabel(this.dictData.carTypeDict, row.carType)
		} else if (column.property === 'licensePlateColor') {
			return this.selectDictLabel(this.dictData.licensePlateColorDict, row.licensePlateColor)
		} else if (column.property === 'alarmType') {
             return this.selectDictLabel(this.dictData.catchTypeDict, row.alarmType)
         } else if (column.property === 'img') {
			return '预览'
		}
        return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
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
	  sortChange(column) {
		var list = []
		list.push("projectName");
		sortColumnNoToLowerCase(this,column,list);
	  },
		/**
		 * 通过回调控制class
		 */
		cellClassName({row, column, rowIndex, columnIndex}) {
			if (column.property === 'img') {
			  return 'can-visit--underline can-visit--bold'
			}
		},
      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams = {}
		this.executeDates = ''
        this.getList()
      },
      // 获取项目信息列表
      getProjectInfoList() {
        queryProjectList()
          .then(response => {
            this.projectInfoList = response.data || []
          })
          .catch(() => {
          })
      },
	  // 当某一行被点击时会触发该事件
	  handleRowClick(row, column, event) {
	    if (column.property == 'img') {
		  this.fileimage = true
		  this.filepaths = row.img
	    }
	  },
      fileSelect(imgDom) {
        this.fileimage = true
        this.filepaths = imgDom
      },
      ts(row) {
      	console.log(row)
      	if(row.alarmVideo==''){
      		this.$message({
	          message: '未上传视频',
	          type: 'warning'
	        });
      	}else{
	      	this.videoUrl=row.alarmVideo
	      	this.videoOpen=true
	      	this.$nextTick(() => {
	        	this.$refs.videoId.load()
	        })
      		
      	}
      },
	  // 初始化字典数据
	  initDicts() {
	  	// 裸土分析报警类型
	  	this.getDicts('b_bare_soil_alarm_type').then(res => {
	  		this.dictData.alarmTypeDict = res.data
	  	})
	  },
     // 车辆类型
     alarmTypeFormatter(row, column) {
       return this.selectDictLabel(this.dictData.alarmTypeDict, row.alarmType)
     },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        const pipeId = row.id || this.ids
        selectById(pipeId).then(response => {
            this.form = response.data
            this.open = true
            this.title = '详情'
          })
          .catch(() => {
          })
      },
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()
      },
      // 表单重置
      reset() {
        this.form = {}
        this.resetForm('form')
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
  /deep/.column-font-color-red .el-tooltip{
  	color: red !important;
  }

  /deep/ .el-icon-circle-close {
  color: white;
  }
  /deep/ .el-image-viewer__wrapper {
    z-index:2200!important;
  }
  @import '@/views/styles/table.scss';
</style>
