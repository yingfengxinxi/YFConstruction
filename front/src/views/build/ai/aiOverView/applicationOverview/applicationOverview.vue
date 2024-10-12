<template>
    <div class="aicontent">
    	<div class="ai_top_box" id="aitop">
    		<div class="ai_box ai_box1">
    			<div class="aiimg_box">
    				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../../static/img/aiicon1.png"/>
    				AI事件
    			</div>
    			<div class="qy_box">
    				已启用
    			</div>
    			<div class="qy_val">
    				{{aiEqumentStatistic.EnableAiEventType}}
    			</div>
    			<div class="qy_line"></div>
    			<div class="wqy_box">
    				未启用
    			</div>
    			<div class="wqy_val">
    				{{aiEqumentStatistic.notEnableAiEventType}}
    			</div>
    		</div>
    		<div class="ai_box ai_box2">
    			<div class="aiimg_box">
    				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../../static/img/aiicon2.png"/>
    				监控设备
    			</div>
    			<div class="qy_box">
    				在线
    			</div>
    			<div class="qy_val">
    				{{aiEqumentStatistic.onlineNum}}
    			</div>
    			<div class="qy_line"></div>
    			<div class="wqy_box">
    				离线
    			</div>
    			<div class="wqy_val">
    				{{aiEqumentStatistic.inlineNum}}
    			</div>
    		</div>
    		<div class="ai_box ai_box3">
    			<div class="aiimg_box">
    				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../../static/img/aiicon2.png"/>
    				AI设备
    			</div>
    			<div class="qy_box">
    				在线
    			</div>
    			<div class="qy_val">
    				{{aiEqumentStatistic.aiOnlineNum}}
    			</div>
    			<div class="qy_line"></div>
    			<div class="wqy_box">
    				离线
    			</div>
    			<div class="wqy_val">
    				{{aiEqumentStatistic.aiInlineNum}}
    			</div>
    		</div>
    	</div>
    	<div class="ai_police_box" id="aipolice">
    		<div class="top_top">
				<div class="span-img1">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="img1" src="../../../../../../static/img/towerCraneMonitor/top-left-1.png" />
				</div>
				<div class="title1">报警类型</div>
			</div>
			<div class="ai_police">
				<div class="ai_police_list" v-for="(item,index) in aiEventTypeStatistic" @click="ClickFunction(item.dictValue)">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.dictValue=='01'" src="../../../../../../static/img/aipolice1.png"/>
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.dictValue=='02'" src="../../../../../../static/img/aipolice2.png"/>
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.dictValue=='06'" src="../../../../../../static/img/aipolice3.png"/>
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.dictValue=='04'" src="../../../../../../static/img/aipolice6.png"/>
					<div class="sxt_znum">
						<div class="sxt_val">{{item.videoNum}}</div>
						<div class="sxt_tit">摄像头路数</div>
					</div>
					<div class="zx_line"></div>
					<div class="sxt_znum">
						<div class="sxt_val">{{item.onlineNum}}</div>
						<div class="sxt_tit">在线数</div>
					</div>
					<div class="aipol_name">{{item.dictLabel}}</div>
				</div>
			</div>
    	</div>
        <!-- 头部：搜索、操作 start -->
        <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;">
            <el-form-item label="设备编号">
                <el-input v-model="queryParams.code" placeholder="请输入设备编号" clearable size="small" @keyup.enter.native="getList" />
            </el-form-item>
            <el-form-item label="报警类型">
                <el-select v-model="queryParams.aiEventType" clearable placeholder="请选择报警类型" @change="$forceUpdate()">
                    <el-option v-for="item in dictData.eventTypeDict" :key="item.dictValue" :label="item.dictLabel" :value="item.dictValue">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="报警时间">
                <el-date-picker v-model="queryParams.alertTimeStr" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="getList">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 头部：搜索、操作 end -->
        <div style="margin: 0 auto;width: 16rem;" v-empty="!canHandle('list')" xs-empty-icon="nopermission" xs-empty-text="暂无权限">
            <el-table v-loading="loading" id="crm-table" :data="list" :height="tableHeight" class="n-table--border" use-virtual stripe border highlight-current-row style="width: 100%" @selection-change="handleSelectionChange" @sort-change="sortChange">

                <el-table-column label="序号" type="index" show-overflow-tooltip width="70">
                </el-table-column>
                <el-table-column v-for="(item, index) in fieldList" :key="index" :prop="item.prop" :label="item.label" :width="item.width" :formatter="fieldFormatter" sortable="custom" align="center" show-overflow-tooltip>

                </el-table-column>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
                    <template slot-scope="scope">
                        <el-button size="mini" type="text" @click="detailsClick(scope.row)">详情
                        </el-button>
                        <el-button v-if="scope.row.handleBy == null" size="mini" type="text" @click="handleClick(scope.row)">处理
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="p-contianer">
                <el-pagination :current-page="currentPage" :page-sizes="pageSizes" :page-size.sync="pageSize" :total="total" :pager-count="5" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
            </div>
            <!-- 处置对话框 -->
            <el-dialog title="处置" :visible.sync="xqopen" width="900px" append-to-body>
                <el-form ref="form" :model="form" :rules="rules" label-width="160px" class="add-form">
                    <create-sections title="抓拍图片">
                        <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    :src="form.alertImg" width="100%" />
                    </create-sections>
                    <create-sections title="基本信息">
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="报警编号">
                                    <el-input v-model="form.code" disabled placeholder="请输入" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="报警时间">
                                    <el-input v-model="form.alertTime" disabled placeholder="请输入" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="报警位置" class="add-form-input">
                                    <el-input v-model="form.alertPosition" disabled placeholder="请输入" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="报警类型" class="add-form-input">
                                    <el-select v-model="form.aiEventType" disabled placeholder="请选择报警类型">
                                        <el-option v-for="item in dictData.eventTypeDict" :key="item.dictValue" :label="item.dictLabel" :value="item.dictValue">
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
                            <el-col :span="12">
                                <el-form-item label="处置人" class="add-form-input">
                                    <el-input v-model="form.handleBy" disabled placeholder="" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="处置时间" class="add-form-input">
                                    <el-input v-model="form.handleTime" disabled placeholder="" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                <el-form-item label="处置内容" prop="handleContent" class="add-form-input">
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
                <el-form ref="form" :model="form" :rules="rules" label-width="160px" class="add-form">
                    <create-sections title="抓拍图片">
                        <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    :src="form.alertImg" width="100%" />
                    </create-sections>
                    <create-sections title="基本信息">
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="报警编号">
                                    <el-input v-model="form.code" disabled placeholder="请输入" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="报警时间">
                                    <el-input v-model="form.alertTime" disabled placeholder="请输入" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="报警位置" class="add-form-input">
                                    <el-input v-model="form.alertPosition" disabled placeholder="请输入" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="报警类型" class="add-form-input">
                                    <el-select v-model="form.aiEventType" disabled placeholder="请选择报警类型">
                                        <el-option v-for="item in dictData.eventTypeDict" :key="item.dictValue" :label="item.dictLabel" :value="item.dictValue">
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
                                <el-form-item label="处置内容" prop="handleContent" class="add-form-input">
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
import { mapGetters } from 'vuex'//权限
import Lockr from 'lockr'//分页
import { getList, getById,getaiEqumentStatistic,getEventTypeStatistic } from "../../api/videoAlert";
import CreateSections from '@/components/CreateSections'
import request from '@/utils/request'
import { sortColumn } from '@/utils/index';
export default {
    /** DEMO列表 */
    name: 'aiVideoAlert',
    components: {
        CreateSections
    },
    data () {
        return {
            businessType: 10000,
            loading: false, // 加载动画
            /** 列表 */
            tableHeight: 0, // 表的高度
            list: [],
            fieldList: [//表头
                {
                    prop: "code",
                    label: "设备编号",
                    //width: "250"
                },
                {
                    prop: "assetName",
                    label: "设备名称",
                    //width: "250"
                },
                {
                    prop: "alertTime",
                    label: "报警时间",
                    //width: "250"
                },
                {
                    prop: "aiEventType",
                    label: "报警类型",
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
            queryParams: {},
            /** 新增*/
            createShow: false,
            /***/
            action: { type: 'save', data: null },
            /** 勾选行 */
            selectionList: [], // 勾选数据 用于全局导出
            /** 控制详情展示 */
            rowID: '', // 行信息,

            // 字典数据或关联下拉框数据
            dictData: {
                // 统计类型字典项
                statisticalTypeDict: [],
                // 监测项类型字典项
                eventTypeDict: [],
            },
            //详情
            id: null,
            open: false,
            xqopen: false,
            form: {},
            rules: {
                handleContent: [
                    { required: true, message: '必填项', trigger: 'change' }
                ]
            },
            aiEventTypeStatistic: [{
            	videoNum:'',

            }],
            aiEqumentStatistic:{},
        }
    },
    computed: {
        //权限 start
        ...mapGetters(['allAuth']),
        //权限 end
        titleIcon () {
            return require(`@/assets/img/crm/customer.png`)
        },
    },
    created(){
    	  //获取ai设备统计数量
        this.getaiEqumentStatistic()
        //获取ai事件类型设备统计数量
        this.getEventTypeStatistic()
    },
    mounted () {
    	let aitopheight=document.getElementById("aitop").offsetHeight
    	let aipolice=document.getElementById("aipolice").offsetHeight



    	this.$nextTick(function(){
    		this.tableHeight=document.documentElement.clientHeight-aitopheight-aipolice-330
    	})
        /** 控制table的高度 */
//      window.onresize = () => {
//          this.updateTableHeight()
//      }
        // 获取字典数据
        this.initDicts()
        //获取列表数据
        this.getList()

        this.remsize()
    },
    methods: {
		remsize() {
			function setRemFontSize() {
				var remSize = window.innerWidth / 19.2;
				document.querySelector("html").style.fontSize = remSize + "px";
			}
			setRemFontSize();
			window.addEventListener("resize", function() {
				setTimeout(function() {
					setRemFontSize();
				}, 200)
			});
		},
        getaiEqumentStatistic () {
            getaiEqumentStatistic() .then(res => {
                    this.aiEqumentStatistic = res.data;
                })
                .catch(() => {

                })
        },
        getEventTypeStatistic () {
            getEventTypeStatistic() .then(res => {
                    this.aiEventTypeStatistic = res.data;
                })
                .catch(() => {

                })
        },
        ClickFunction(value){
             this.loading = true
             //分页
            this.queryParams.page = this.currentPage;
            this.queryParams.limit = this.pageSize;
            //添加ai事件类型
            this.queryParams.aiEventType=value
            this.getList();
        },
        /** 获取列表数据 */
        getList () {
            this.loading = true
            //分页
            this.queryParams.page = this.currentPage;
            this.queryParams.limit = this.pageSize;
            if (this.queryParams.alertTimeStr != null && this.queryParams.alertTimeStr != '') {
                this.queryParams.startTime = this.queryParams.alertTimeStr[0];
                this.queryParams.endTime = this.queryParams.alertTimeStr[1];
            } else {
                this.queryParams.startTime = '';
                this.queryParams.endTime = '';
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
                    this.$nextTick(() => {
                        document.querySelector('.el-table__body-wrapper').scrollTop = 1
                    })
                    this.loading = false
                })
                .catch(() => {
                    this.loading = false
                })
        },
        /**
         *更多操作点击
         */
        detailsClick (data) {
            this.form = {};
            this.xqopen = true;
            this.form = data
            this.id = data.id;
        },
        handleClick (data) {
            this.form = {};
            this.open = true;
            this.form = data
            this.id = data.id;
        },
        submitForm () {
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
        cancel () {
            this.open = false;
            this.xqopen = false;
        },
        //列格式化  如一些字典等
        fieldFormatter (row, column, cellValue) {
            if (column.property === 'aiEventType') {
                return this.selectDictLabel(this.dictData.eventTypeDict, row.aiEventType)
            } else if (column.property === 'statisticalType') {
                return this.selectDictLabel(this.dictData.statisticalTypeDict, row.statisticalType)
            }
            return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
        },
        //按钮操作权限
        canHandle (type) {
            return true;
            //return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
        },
        /** 勾选操作 */
        // 当选择项发生变化时会触发该事件
        handleSelectionChange (val) {
            this.selectionList = val // 勾选的行
            this.updateTableHeight();
        },

        // 更改当前页数
        handleCurrentChange (val) {
            this.currentPage = val
            this.getList()
        },

        // 更改每页展示数量
        handleSizeChange (val) {
            Lockr.set('crmPageSizes', val)
            this.pageSize = val
            this.getList()
        },
        /** 重置按钮操作 */
        resetQuery () {
            this.queryParams = {};
            this.getList()
        },
        /**
         * 更新表高
         */
        updateTableHeight () {
            var offsetHei = document.documentElement.clientHeight
            var removeHeight = this.selectionList.length > 0 ? 370 : 260
            this.tableHeight = offsetHei - removeHeight
        },
        // 初始化字典数据
        initDicts () {
            this.getDicts('b_equipment_statistical_type').then(res => {
                this.dictData.statisticalTypeDict = res.data
            })
            this.getDicts('b_ai_event_type').then(res => {
                this.dictData.eventTypeDict = res.data
            })
        },
        sortChange(column) {
          sortColumn(this,column);
        }
    }
}
</script>

<style lang="scss" scoped>
.top_top {
		margin-left: 0.14rem;
		overflow: hidden;
		margin-top: 12px;
		.span-img1 {
			float: left;
			display: block;
			margin-right: 0.10rem;
			.img1 {
				float: left;
				display: block;
				width: 16px;
				height: auto;
			}
		}
		.title1 {
			float: left;
			display: block;
			width: 1.50rem;
			height: 0.22rem;
			font-size: 0.16rem;
			font-family: PingFangSC-Medium, PingFang SC;
			font-weight: 500;
			color: #404040;
			line-height: 0.22rem;
		}
		.history_img {
			float: right;
			display: block;
			width: 0.24rem;
			height: 0.24rem;
			margin-right: 0.15rem;
		}
	}
.aicontent{
	height: 100%;
}
.ai_top_box{
	width: 16.13rem;
	height:11.1% ;
	display: flex;
	justify-content: space-between;
}
.ai_box{
	width: 5.32rem;
	height: 100%;
	display: flex;
	align-items: center;
}
.aiimg_box{
	width: 1.9rem;
	height: 100%;
	margin-left: 0.1rem;
	text-align: center;
	color: #FFFFFF;
	font-size: 0.14rem;
	display: flex;
	align-items: center;
}
.aiimg_box img{
	width: 0.5rem;
	height: auto;
	margin-left:0.3rem;
	margin-right:0.17rem

}
.qy_box{
	width: 0.7rem;
	color: #FFFFFF;
	font-size: 0.14rem;
	text-align: right;
}
.qy_val{
	width: 0.67rem;
	margin-left: 0.12rem;
	color: #FFFFFF;
	font-size: 0.36rem;
}
.qy_line{
	width: 1px;
	height: 20px;
	background: #E3FFF2;
}
.wqy_box{
	margin-left: 0.57rem;
	color: #FFFFFF;
	font-size: 0.14rem;
	margin-right: 0.1rem;
}
.wqy_val{
	color: #FFFFFF;
	font-size: 0.36rem;
}
.ai_police_box{
	height: 19.45%;
	width: 16rem;
	background: #FFFFFF;
	box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.44);
	border-radius: 5px;
	overflow: hidden;
	margin: 0 auto;
	margin-top: 1%;
	margin-bottom: 10px;
}
.ai_box1{
	background: url(../../../../../../static/img/aibg1.png) no-repeat;
	background-size:100% 100%;
}
.ai_box2{
	background: url(../../../../../../static/img/aibg2.png) no-repeat;
	background-size:100% 100%;
}
.ai_box3{
	background: url(../../../../../../static/img/aibg3.png) no-repeat;
	background-size:100% 100%;
}
.ai_police{
	height: 57.14%;
	width: 15.58rem;
	margin: 0 auto;
	display: flex;
	margin-top: 1%;
}
.ai_police_list{
	width: 2.48rem;
	height: 100%;
	background: #F8FBFF;
	box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.4400);
	border-radius: 5px;
	position: relative;
	display: flex;
	align-items: center;
	margin-right:0.17rem ;
}
.ai_police_list:nth-child(2){

margin-right: 0;

}
.ai_police_list img{
	width: 0.36rem;
	height:  auto;
	margin-left: 0.08rem;
	margin-right: 0.17rem;
	margin-top: 10px;
}
.aipol_name{
	position: absolute;
	height: 29%;
	width: 0.94rem;
	color: #4984E9;
	font-size: 0.12rem;
	text-align: center;
	display: flex;
	align-items: center;
	justify-content: center;
	background: url(../../../../../../static/img/aipolicexbg.png) no-repeat;
	background-size:100% 100%;
	top: 0;
	left: 0;
}
.sxt_znum{
	width: 0.98rem;
	text-align: center;
	margin-top: 10px;
}
.sxt_val{
	color: #649BF8;
	font-size: 0.36rem;
	margin-top: 10px;
}
.sxt_tit{
	color: #404040;
	font-size: 0.14rem;
}
.zx_line{
	width: 1px;
	height: 20px;
	background: #E1E1E1;
	margin-top: 10px;
}
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

/*@import "@/views/styles/table.scss";*/


</style>
