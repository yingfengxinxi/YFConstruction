<template>
  <div class="main">
    <div class="main-body">
      <el-row style="background-color: #fff">
        <el-col :span="24" >
		  <!-- 头部：搜索、操作 start -->
		  <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px"
		           style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
		    <el-form-item label="隐患编号" prop="hiddenNum">
		      <el-input
		        v-model="queryParams.hiddenNum"
		        placeholder="请输入"
		        clearable
		        size="small"
		        @keyup.enter.native="getList"
		      />
		    </el-form-item>
		  <el-form-item label="隐患类别" prop="category">
		    <el-select v-model="queryParams.category" clearable placeholder="请选择">
		      <el-option
		    	v-for="item in dictData.hazardCategoryDict"
		    	:key="item.dictValue"
		    	:label="item.dictLabel"
		    	:value="item.dictValue">
		      </el-option>
		    </el-select>
		  </el-form-item>
		  <el-form-item label="隐患等级" prop="level">
		    <el-select v-model="queryParams.level" clearable placeholder="请选择">
		      <el-option
		    	v-for="item in dictData.hazardLevelDict"
		    	:key="item.dictValue"
		    	:label="item.dictLabel"
		    	:value="item.dictValue">
		      </el-option>
		    </el-select>
		  </el-form-item>
		    <el-form-item label="风险点名称" prop="riskName">
		      <el-input
		        v-model="queryParams.riskName"
		        placeholder="请输入"
		        clearable
		        size="small"
		        @keyup.enter.native="getList"
		      />
		    </el-form-item>
		  <el-form-item label="隐患状态" prop="infoState">
		    <el-select v-model="queryParams.infoState" clearable placeholder="请选择">
		      <el-option
		    	v-for="item in dictData.infoStateDict"
		    	:key="item.dictValue"
		    	:label="item.dictLabel"
		    	:value="item.dictValue">
		      </el-option>
		    </el-select>
		  </el-form-item>
		    <el-form-item>
		      <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
		      <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
		    </el-form-item>
		  </el-form>
          <el-card class="box-card" style="margin: 10px;height:calc(100vh - 270px);">
            <el-card v-for="item in dataList" :key="item.id" :body-style="{ padding: '0px' }" style="width: 180px;display: inline-block;margin: 8px">
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'   
                v-src="getImg(item.imgs)"
                style="width: 140px; height: 140px;margin: 18px"/>
              <div style="padding: 0px 14px 14px;">
<!--                <el-tooltip :content="item.problemDescription" class="item" effect="dark" placement="top-start">-->
                  <div class="bottom clearfix">
                    <time class="time" v-bind:title="item.riskDesc">{{ item.riskDesc }}</time>
                    <el-button type="text" class="button" @click="details(item.id)" >详情</el-button>
                  </div>
<!--                </el-tooltip>-->
              </div>
            </el-card>
          </el-card>
          <div class="p-contianer" style="margin-bottom: 0px">
            <el-pagination
                    :current-page="currentPage"
                    :page-size.sync="pageSize"
                    :total="total"
                    class="p-bar"
                    background
                    layout="prev, pager, next, sizes, total, jumper"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"/>
          </div>
        </el-col>
      </el-row>
    </div>

	<!-- 新建 -->
	<create
	  v-if="createShow"
	  :businessType="businessType"
	  :action="action"
	:dictData="dictData"
	  @close="createShow = false"
	  @save-success="getList"
	/>
  </div>
</template>

<script>
import { getList } from './api'
import XrHeader from '@/components/XrHeader'
import Create from './Create'
import { mapGetters } from 'vuex'

export default {
  name: 'dangerPhoto',
  components: {
    XrHeader,
    Create
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      tableHeight: document.documentElement.clientHeight - 280, // 表的高度
      // 是否显示弹出层
      open: false,
      title: '',
      // 分页
      currentPage: 1,
      pageSize: 10,
      pageSizes: [10, 20, 30, 40],
      total: 0,
      id: 0,
      catalogForm: {},
      dataList: [],
      queryParams: {},
	  /** 新增*/
	  createShow: false,
	  /***/
	  action: {type: 'details', data: null},
	  // 字典数据
	  dictData: {
		  // 隐患类别
		  hazardCategoryDict: [],
		  // 隐患等级
		  hazardLevelDict: [],
		  infoStateDict: [
			{
			dictLabel:'已检查',
			dictValue:'0'
			},
			{
			dictLabel:'已下发',
			dictValue:'1'
			},
			{
			dictLabel:'已整改',
			dictValue:'2'
			},
			{
			dictLabel:'已复检',
			dictValue:'3'
			},
		  ],
	  },
      url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
      srcList: [
        'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
        'https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg'
      ]
    }
  },

  created() {
	this.initDicts()
    this.getList()
  },
  computed: {
    ...mapGetters(['imageCache'])
  },
  methods: {
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize
      getList(this.queryParams).then(response => {
        this.dataList = response.data.list
        this.total = response.data.totalRow
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
	// 初始化字典数据
	initDicts() {
		this.getDicts('b_hazard_category').then(response => {
		  this.dictData.hazardCategoryDict = response.data
		})
		this.getDicts('b_hazard_level').then(response => {
		  this.dictData.hazardLevelDict = response.data
		})
	},
	/** 重置按钮操作 */
	resetQuery() {
		this.currentPage = 1;
		this.queryParams = {};
		this.getList()
	},
    getImg(imgData) {
      var url = ''
      if (imgData[0] != undefined) {
        url = imgData[0].url
      }
      return url
    },
    getIms(imgs) {
      var imgList = []
      imgs.forEach(function(element) {
        imgList.push('/api/' + element.url)
      })
      console.log(imgList)
      return imgList
    },
    details(id) {
        this.action.type = 'details';
        this.action.id = id;
        this.createShow = true
    },
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
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }
  .el-aside {
    color: #333;
  }
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
  .custom-tree-but{
    display: inline-block;
    position: relative;
    right: 10px;
    top: 0px;
  }
  .custom-tree-button{
    padding: 3px 10px;
  }
  .xl-but{
    background-color: #CCCCCC;
    color: #000000;
  }
  .time {
    display: inline-block;
    width: 100px;
    height: 13px;
    font-size: 13px;
    color: #999;
    white-space:nowrap;//不换行
    overflow: hidden;//超出隐藏
    text-overflow: ellipsis;//变成...
    line-height: 13px;
  }

  .bottom {
    /*margin-top: 13px;*/
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }
</style>
