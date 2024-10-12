<template>
	<div class="tower_page">
		<div class="tower_left">
			<div class="tower_police_box">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
					<div>设备报警</div>
				</div>
				<div class="tower_police_info">
					<div class="twpolice" >
						<div class="pol_img">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/twoerpolic.png" />
							<div class="tower_num">
								<div class="tpl_tit">
									当日预警
								</div>
								<div class="tpl_val_box">
									<div class="tpl_val" style="color: #3397FE;">{{alarmNumber.dayEarly}}</div>
									<div class="tpl_unit">次</div>
								</div>

							</div>
						</div>
						<div class="police_line"></div>
						<div class="ljbj_box">
							<div class="lj_title">累计预警</div>
							<div class="ljbj_box2">
								<div class="lj_num">{{alarmNumber.earlynumber}}</div>
								<div class="lj_unit">次</div>
							</div>
						</div>
					</div>
					<div class="twpolice">
						<div class="pol_img">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/twoerpolice2.png" />
							<div class="tower_num">
								<div class="tpl_tit">
									当日报警
								</div>
								<div class="tpl_val_box">
									<div class="tpl_val" style="color: #FF5353;">{{alarmNumber.dayAlarm}}</div>
									<div class="tpl_unit">次</div>
								</div>

							</div>
						</div>
						<div class="police_line"></div>
						<div class="ljbj_box">
							<div class="lj_title">累计报警</div>
							<div class="ljbj_box2">
								<div class="lj_num">{{alarmNumber.alarmNumber}}</div>
								<div class="lj_unit">次</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="equipment_num_box">

				<div class="equ_num">
					<div class="jr_box">
						<div class="jrimg_box">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/sb_img2.png" />
						</div>
						<div class="jrsb_box">
							<div class="sb_num">
								<div class="sb_num1">{{equimentNumber.deviceQuantity}}</div>
								<div class="sb_num2">台</div>
							</div>
							<div class="sb_tit">接入升降机</div>
						</div>
					</div>
				</div>
			</div>
			<div class="trend_box">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
					<div>报警趋势</div>
				</div>
				<div class="trend_tab">
					<div class="tab_con" :class="trendIndex==1?'trend_active':''" @click="trendClick(1)">7日报警数量</div>
					<div class="tab_con" :class="trendIndex==2?'trend_active':''" @click="trendClick(2)">30日报警数量</div>
				</div>
				<div id="trend"></div>
			</div>
			<div class="ranking_box">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
					<div>升降机报警排名</div>
				</div>
				<div class="ranking_list_box">
					<div v-for="(item,index) in projectRankList" class="rank_list_box">

							<div class="no_tit_box">
								<div class="ranking">NO.{{index + 1}}</div>
								<div class="ranking_name">{{item.projectName}}</div>
							</div>
							<el-tooltip class="item" effect="dark" :content="'报警数量：'+item.alarmNumber" placement="top">
							<div class="ranking_bg">
								<div class="ranking_speed" :style="{width: item.percent }"></div>
							</div>
							</el-tooltip>
					</div>
				</div>
			</div>
		</div>
		<div class="tower_content">
			<div class="tower_select_box">
				<el-cascader class='power_cascader'  v-model="elevatorValue" :options="options" :props="props" @change="handleChange">
				</el-cascader>
				<div class="wookjl" @click="springClick(1)">工作循环记录</div>
				<div class="wookjl" @click="springClick(2)">监测历史记录</div>
				<div class="wookjl" @click="springClick(3)">报警历史记录</div>
			</div>
			<div class="sjj_equ_box">
				<div class="tj_bh_box">
					<div class="tjbh_tit">设备编号</div>
					<div class="tjbh_val">{{elevatorData.assetCode}}</div>
          <div class="tjbh_tit" style="margin-left: 50px">数据获取时间</div>
          <div class="tjbh_val">{{elevator.offlineDate}}</div>
				</div>
				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="sjjimg" src="../../../../assets/img/projectAnalyse/sjjimg.png" />
				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="jtimg jtimg1" src="../../../../assets/img/projectAnalyse/sjjleft.png" />
				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="jtimg jtimg2" src="../../../../assets/img/projectAnalyse/sjjleft.png" />
				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="jtimg jtimg3" src="../../../../assets/img/projectAnalyse/sjjright.png" />
				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="jtimg jtimg4" src="../../../../assets/img/projectAnalyse/sjjright.png" />
				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="jtimg jtimg5" src="../../../../assets/img/projectAnalyse/sjjright.png" />
				<div class="sjjxx_box sjjxx_box1">
					<div class="sjjxx">
						<div class="sjjxx_tit">实时重量</div>
						<div class="sjjxx_val">{{elevator.weight?elevator.weight:'0'}}KG</div>
					</div>
				</div>
				<div class="sjjxx_box sjjxx_box2">
					<div class="sjjxx">
						<div class="sjjxx_tit">实时高度</div>
						<div class="sjjxx_val">{{elevator.height?elevator.height:'0'}}m</div>
					</div>
				</div>
				<div class="sjjxx_box sjjxx_box3">
					<div class="sjjxx">
						<div class="sjjxx_tit">实时速度</div>
						<div class="sjjxx_val">{{elevator.speed?elevator.speed:'0'}}m/s</div>
					</div>
				</div>
				<div class="sjjxx_box sjjxx_box4">
					<div class="sjjxx">
						<div class="sjjxx_tit">实时倾斜度</div>
						<div class="sjjxx_val">{{elevator.bias?elevator.bias:'0'}}度</div>
					</div>
				</div>
				<div class="sjjxx_box sjjxx_box5">
					<div class="sjjxx">
						<div class="sjjxx_tit">实时人数</div>
						<div class="sjjxx_val">{{elevator.numberOfPeople?elevator.numberOfPeople:'0'}}</div>
					</div>
				</div>
			</div>
			<div class="sjjkm_box">
				<div class="sjjkm_list">
					<div class="kmimg">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/kmicon1.png" />
					</div>
					<div class="km_val km_val1">
						{{elevator.liftingDirection?elevator.liftingDirection=='1'?'静止':elevator.liftingDirection=='2'?'上升':'下降':'--'}}
					</div>
					<div class="km_tit">方向</div>
				</div>
				<div class="sjjkm_list">
					<div class="kmimg">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/kmicon1.png" />
					</div>
					<div class="km_val km_val2">
						<p>前门：{{elevator.beforeLock?elevator.beforeLock=='0'?'关闭':'开启':'--'}}</p>
						<p>后门：{{elevator.afterLock?elevator.afterLock=='0'?'关闭':'开启':'--'}}</p>
					</div>
					<div class="km_tit">门锁状态</div>
				</div>
				<div class="sjjkm_list">
					<div class="kmimg">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/kmicon1.png" />
					</div>
					<div class="km_val km_val3" :style="yxzt(elevator)=='正常'?'color: #37F6BE;':'color:red;'">{{yxzt(elevator)}}</div>
					<div class="km_tit">运行状态</div>
				</div>
			</div>
			<div class="time_lj_box">
				<div class="moment_box">
					<div class="model_title">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
						<div>实时速度</div>
					</div>
					<div id="moment">

					</div>
				</div>
				<div class="moment_box">
					<div class="model_title">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
						<div>实时载重</div>
					</div>
					<div id="weight">

					</div>
				</div>
			</div>
		</div>
		<div class="tower_right">
			<!--<div class="vide_box">
				<div id="myPlayer"></div>
			</div>-->
			<!--<div class="kj_box">
				<div class="kzdx_aj">
					<div class="box_center">
						<div class="sxzy_box">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="imgbgkz" src="../../../../assets/lrtb_bg.png" />
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="video_left" @mousedown='control("left")' @mouseup='control("stop")' src="../../../../assets/video_left.png" />
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="video_right" @mousedown='control("right")' @mouseup='control("stop")' src="../../../../assets/video_right.png" />
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="video_center" @mousedown='control("stop")' @mouseup='control("stop")' src="../../../../assets/center_stop.png" />
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="video_top" @mousedown='control("up")' @mouseup='control("stop")' src="../../../../assets/video_top.png" />
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="video_bottom" @mousedown='control("down")' @mouseup='control("stop")' src="../../../../assets/video_bottom.png" />
						</div>

					</div>
					<div class="max_min">
						<div @mousedown='control("zoomin")' @mouseup='control("stop")'><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/screen_max.png" />放大</div>
						<div @mousedown='control("zoomout")' @mouseup='control("stop")'><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/screen_min.png" />缩小</div>
					</div>
				</div>

				<div class="slider_box">
					<el-tooltip class="item" effect="dark" content="旋转速度" placement="top">
						<el-slider v-model="sliderVidewo" :min="30" :max="250"></el-slider>
					</el-tooltip>
				</div>
			</div>-->
			<div class="xx_list_details">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    style="margin-left: 0;" src="../../../../assets/img/projectAnalyse/model_title.png" />
					<div>设备信息</div>
				</div>
				<div class="xx_list_box">
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #1B65FA;"></div>
							<div class="xx_tit">设备名称</div>
						</div>
						<div class="xx_val">{{elevatorData.name}}</div>
					</div>
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #5B97F9;"></div>
							<div class="xx_tit">设备编号</div>
						</div>
						<div class="xx_val">{{elevatorData.assetCode}}</div>
					</div>
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #5BC7F9;"></div>
							<div class="xx_tit">额定重量</div>
						</div>
						<div class="xx_val">{{elevatorData.noLoad}}KG</div>
					</div>
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #18BAA0;"></div>
							<div class="xx_tit">基本高度</div>
						</div>
						<div class="xx_val">{{elevatorData.noLoadActual}}M</div>
					</div>
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #BDEB80;"></div>
							<div class="xx_tit">起升速度</div>
						</div>
						<div class="xx_val">{{elevatorData.loadValue}}M/S</div>
					</div>
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #F9C827;"></div>
							<div class="xx_tit">额定载人数</div>
						</div>
						<div class="xx_val">{{elevatorData.loadValueActual}}KG</div>
					</div>
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #FFA62A;"></div>
							<div class="xx_tit">吊笼尺寸</div>
						</div>
						<div class="xx_val">{{elevatorData.lowEnd}}M</div>
					</div>
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #F4664A;"></div>
							<div class="xx_tit">最大起升高度</div>
						</div>
						<div class="xx_val">{{elevatorData.lowEndActual}}M</div>
					</div>
				</div>
			</div>
			<div class="sjxx_box">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    style="margin-left: 0;" src="../../../../assets/img/projectAnalyse/model_title.png" />
					<div>司机信息</div>
				</div>
				<div class="driver_box">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="driver_img" v-src="sjxx.imgCardUrl" />
					<div class="driver_con">
						<div class="driver_list">
							<div style="width: 50%;">姓名：{{sjxx.name?sjxx.name:'--'}}</div>
							<div style="width: 50%;">性别：{{sjxx.sex?sjxx.sex==1?'女':'男':'--'}}</div>

						</div>
						<div class="driver_list">
							<div style="width: 50%;">年龄：{{sjxx.csrq?sjxx.csrq:'--'}}</div>
							<div style="width: 50%;">工作年限：{{sjxx.workAge?sjxx.workAge:'--'}}</div>

						</div>
						<div class="driver_list">
							<div style="width: 100%;">证书名称：{{sjxx.cerName?sjxx.cerName:'--'}}</div>

						</div>
						<div class="driver_list">
							<div style="width: 100%;">证书有效期：{{sjxx.cerUseendDate?sjxx.cerUseendDate:'--'}}</div>

						</div>
						<div class="driver_list">
							<div>身份证号：{{sjxx.idCard?sjxx.idCard:'--'}}</div>
						</div>
					</div>
				</div>
			</div>
			<div class="ssgd_box">
				<div class="model_title">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
						<div>实时高度</div>
				</div>
				<div id="hirght">

				</div>
			</div>
		</div>
		<el-dialog title="工作循环记录" :visible.sync="open1" width="1200px" >
		  <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
		    <el-form-item>
		    <el-cascader class='power_cascader' v-model="formElevator" :options="options" :props="props" @change="handleChange"></el-cascader>
		    </el-form-item>
		    <el-form-item>
		    <el-date-picker class="seledate" style="width: 220px;"  v-model="form.startTime" type="date" value-format="yyyy-MM-dd" :clearable='false' placeholder="开始日期">
		    </el-date-picker>
		    </el-form-item>
		    <el-form-item>
		    <el-date-picker class="seledate" style="width: 220px;"  v-model="form.endTime" type="date" value-format="yyyy-MM-dd" :clearable='false' placeholder="结束日期">
		    </el-date-picker>
		    </el-form-item>
		    <el-form-item style="float: right">
		      <el-button type="primary" icon="el-icon-search" size="mini" @click="getList1()">搜索</el-button>
		    </el-form-item>
		  </el-form>
		  <el-table v-loading="loading" :data="list1" highlight-current-row height='75%'>
		    <el-table-column label="设备名称" align="center" prop="assetName" width="160" />
		    <el-table-column label="设备编号" align="center" prop="assetCode" width="160"/>
		    <el-table-column label="开始时间" align="center" prop="startTime" width="200" />
		    <el-table-column label="结束时间" align="center" prop="endTime" width="200" />
		    <el-table-column label="载重(KG)" align="center" prop="weight" />
		    <el-table-column label="速度(M/S)" align="center" prop="speed" />
		    <el-table-column label="行程高度(M)" align="center" prop="height" width="140"/>
		    <el-table-column label="起点高度(M)" align="center" prop="startHeight" width="140"/>
		    <el-table-column label="终点高度(M)" align="center" prop="endHeight" width="140"/>
		    <el-table-column label="行驶方向" align="center" prop="direction" />
			<el-table-column label="X轴倾斜(°)" align="center" prop="angle" width="140" />
			<el-table-column label="Y轴倾斜(°)" align="center" prop="angleY" width="140" />
			<el-table-column label="载人数量" align="center" prop="numberOfPeople" />
		  </el-table>
		  <div class="p-contianer">
		    <el-pagination
		      :current-page="currentPage1"
		      :page-sizes="pageSizes1"
		      :page-size.sync="pageSize1"
		      :total="total1"
		      class="p-bar"
		      background
		      layout="prev, pager, next, sizes, total, jumper"
		      @size-change="handleSizeChange1"
		      @current-change="handleCurrentChange1"/>
		  </div>
		  <div slot="footer" class="dialog-footer">
		    <el-button @click="cancel1">取 消</el-button>
		  </div>
		</el-dialog>

		<el-dialog title="监测历史记录" :visible.sync="open2" width="1200px" >
		  <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
		    <el-form-item>
		    <el-cascader class='power_cascader' v-model="formElevator" :options="options" :props="props" @change="handleChange"></el-cascader>
		    </el-form-item>
		     <el-form-item>
		    <el-date-picker class="seledate" style="width: 220px;"  v-model="form.startTime" type="date" value-format="yyyy-MM-dd" :clearable='false' placeholder="开始日期">
		    </el-date-picker>
		    <el-form-item>
		    </el-form-item>
		    <el-date-picker class="seledate" style="width: 220px;"  v-model="form.endTime" type="date" value-format="yyyy-MM-dd" :clearable='false' placeholder="结束日期">
		    </el-date-picker>
		    </el-form-item>
		    <el-form-item style="float: right">
		      <el-button type="primary" icon="el-icon-search" size="mini" @click="getList2()">搜索</el-button>
		    </el-form-item>

		  </el-form>
		  <el-table v-loading="loading" :data="list2" highlight-current-row height='75%'>
			<el-table-column label="项目名称" align="center" prop="projectName" width="160"/>
		    <el-table-column label="设备名称" align="center" prop="assetName" />
		    <el-table-column label="设备编号" align="center" prop="assetCode" width="160"/>
		    <el-table-column label="监测时间" align="center" prop="monitorTime" width="200" />
		    <el-table-column label="高度(M)" align="center" prop="height" />
		    <el-table-column label="速度(M/S)" align="center" prop="speed" />
		    <el-table-column label="载重(KG)" align="center" prop="weight" />
			<el-table-column label="载重百分比" align="center" prop="weightPercent" />
			<el-table-column label="X倾角(°)" align="center" prop="xAngle" />
			<el-table-column label="X倾角比" align="center" prop="xAngleRatio" />
			<el-table-column label="Y倾角(°)" align="center" prop="yAngle" />
			<el-table-column label="Y倾角比" align="center" prop="yAngleRatio" />
		    <el-table-column label="起升方向" align="center" prop="liftingDirection" :formatter="formatter"/>
		    <el-table-column label="人数" align="center" prop="numberOfPeople" />
		    <el-table-column label="前门状态" align="center" prop="beforeLock" :formatter="formatter"/>
			<el-table-column label="后门状态" align="center" prop="afterLock" :formatter="formatter"/>
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
		      @current-change="handleCurrentChange2"/>
		  </div>
		  <div slot="footer" class="dialog-footer">
		    <el-button @click="cancel2">取 消</el-button>
		  </div>
		</el-dialog>

		<el-dialog title="报警历史记录" :visible.sync="open3" width="1200px" >
		  <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
		    <el-form-item>
		    <el-cascader class='power_cascader' v-model="formElevator" :options="options" :props="props" @change="handleChange"></el-cascader>
		    <el-select v-model="form.alertType" clearable  placeholder="请选择报警类型">
		    	<el-option
		    	  v-for="item in alertTypeOption"
		    	  :key="item.value"
		    	  :label="item.label"
		    	  :value="item.value">
		    	</el-option>
		    </el-select>
			</el-form-item>
			<el-form-item>
			<el-date-picker class="seledate" style="width: 220px;"  v-model="form.startTime" type="date" value-format="yyyy-MM-dd" :clearable='false' placeholder="开始日期">
		    </el-date-picker>
		    </el-form-item>
		    <el-form-item>
		    <el-date-picker class="seledate" style="width: 220px;"  v-model="form.endTime" type="date" value-format="yyyy-MM-dd" :clearable='false' placeholder="结束日期">
		    </el-date-picker>
		    </el-form-item>
		    <el-form-item style="float: right">
		      <el-button type="primary" icon="el-icon-search" size="mini" @click="getList3()">搜索</el-button>
		    </el-form-item>
		  </el-form>
		  <el-table v-loading="loading" :data="list3" highlight-current-row height='75%'>
		    <el-table-column label="项目名称" align="center" prop="projectName" width="160" />
		    <el-table-column label="设备名称" align="center" prop="assetName" />
		    <el-table-column label="设备编号" align="center" prop="assetCode" width="160"/>
		    <el-table-column label="报警时间" align="center" prop="monitorTime" width="200" />
			<el-table-column label="报警原因" align="center" prop="bjyy" :formatter="formatter" width="120" />
		    <el-table-column label="高度(M)" align="center" prop="height" />
		    <el-table-column label="速度(M/S)" align="center" prop="speed" />
		    <el-table-column label="载重(KG)" align="center" prop="weight" />
		    <el-table-column label="载重百分比" align="center" prop="weightPercent" />
		    <el-table-column label="X倾角(°)" align="center" prop="xAngle" />
		    <el-table-column label="X倾角比" align="center" prop="xAngleRatio" />
		    <el-table-column label="Y倾角(°)" align="center" prop="yAngle" />
		    <el-table-column label="Y倾角比" align="center" prop="yAngleRatio" />
		    <el-table-column label="起升方向" align="center" prop="liftingDirection" :formatter="formatter"/>
		    <el-table-column label="人数" align="center" prop="numberOfPeople" />
		    <el-table-column label="前门状态" align="center" prop="beforeLock" :formatter="formatter"/>
		    <el-table-column label="后门状态" align="center" prop="afterLock" :formatter="formatter"/>
		  </el-table>
		  <div class="p-contianer">
		    <el-pagination
		      :current-page="currentPage3"
		      :page-sizes="pageSizes3"
		      :page-size.sync="pageSize3"
		      :total="total3"
		      class="p-bar"
		      background
		      layout="prev, pager, next, sizes, total, jumper"
		      @size-change="handleSizeChange3"
		      @current-change="handleCurrentChange3"/>
		  </div>
		  <div slot="footer" class="dialog-footer">
		    <el-button @click="cancel3">取 消</el-button>
		  </div>
		</el-dialog>
	</div>
</template>

<script>
	import { mapGetters } from 'vuex'
	import EZUIKit from '@/views/nj/video/videoView/EZUIKitPlayer.js';
	import * as echarts from 'echarts';
	import request from '@/utils/request'
	export default {
		data() {
			return {
				loading: false,
				trendIndex: 1,
				value: '',
				options: [],
				props:{value:'id',label:'name'},
				elevatorValue:[],
				formElevator:[],
				elevatorData:{},
				elevator:{},
				sjxx:{},
				player: null,
				sliderVidewo: 30,
				// 显示搜索条件
				showSearch: true,
				form:{},
				queryForm:{},
				// 物料称重查询参数
				queryParams: {
				  page: this.currentPage,
				  limit: this.pageSize,
				  hydrantCode: undefined,
				  pipeId: undefined
				},
				alertTypeOption:[
					{
						value: '0',
						label: '预警提醒'
					},
					{
						value: '1',
						label: '报警提醒'
					}
				],
				open1: false,
				list1: [],
				// 分页
				currentPage1: 1,
				pageSize1: 10,
				pageSizes1: [10, 20, 30, 40],
				total1: 0,
				open2: false,
				list2: [],
				// 分页
				currentPage2: 1,
				pageSize2: 10,
				pageSizes2: [10, 20, 30, 40],
				total2: 0,
				open3: false,
				list3: [],
				// 分页
				currentPage3: 1,
				pageSize3: 10,
				pageSizes3: [10, 20, 30, 40],
				total3: 0,

				alarmNumber: {},
				equimentNumber: {},
				projectRankList: [],
				timer:null
			}
		},
		computed: {
    ...mapGetters(['pageDataReflushTime']),
  },
		mounted() {
			let that=this
			this.timer=setInterval(function(){
				that.selectElevatorCraneById();
				that.selectDZTJ();
			// },that.pageDataReflushTime)
			},10000)
			request({
				url: 'build/equipmentAssets/selectAllElevator',
				method: 'post',
				data: {},
				headers: {
					'Content-Type': 'application/json;charset=UTF-8'
				}
			}).then(res => {
				this.options = res.data
				this.elevatorValue[0] = this.options[0].id
				this.elevatorValue[1] = this.options[0].children[0].id
				this.formElevator[0] = this.options[0].id
				this.formElevator[1] = this.options[0].children[0].id
				this.elevatorData = this.options[0].children[0]
				this.selectElevatorCraneById();
				this.selectDZTJ();
			}).catch(() => {

			})
			//this.videoPalyer()
			this.trendEcharts()

			this.selectAlarmTrend()
			this.selectAlarmNumber(); //报警信息
			this.selectEquipmentNumber();//接入信息
			this.selectProjectRank()//工地排名报警

			this.remsize()
		},
		methods: {
			selectAlarmNumber(){
			  let params = {
			    monitorType: '3'
			  }
			  request({
			    url: 'build/monitorPointAlert/selectAlarmNumByMonitorType',
			    method: 'post',
			    data: params,
			    headers: {
			      'Content-Type': 'application/json;charset=UTF-8'
			    }
			  }).then(response => {
			    this.alarmNumber = response.data
			  }).catch(() => {

			  })
			},
			selectEquipmentNumber(){
			  let params = {
			    monitorType: '3'
			  }
			  request({
			    url: 'build/monitorPointAlert/selectProjectAndEquiNum',
			    method: 'post',
			    data: params,
			    headers: {
			      'Content-Type': 'application/json;charset=UTF-8'
			    }
			  }).then(response => {
			    this.equimentNumber = response.data
			  }).catch(() => {

			  })
			},
			selectAlarmTrend(){
			  let params = {
			    monitorType: '3',
			    type: this.trendIndex
			  }
			  request({
			    url: 'build/monitorPointAlert/selectAlertTrend',
			    method: 'post',
			    data: params,
			    headers: {
			      'Content-Type': 'application/json;charset=UTF-8'
			    }
			  }).then(response => {
			    console.log(response.data)
			    let resData = response.data
			    let xData = []
			    let yData1 = []
			    let yData2 = []

			    resData.forEach(item => {
			      xData.push(item.monitorDate)
			      yData1.push(item.earlyNumber)
			      yData2.push(item.alertNumber)
			    })

			    // this.trendOption.xAxis.data = xData
			    // this.trendOption.series[0].data = yData1
			    // this.trendOption.series[1].data = yData2
			    this.trendEcharts(xData,yData1,yData2)//报警趋势图表加载
			    // this.trendChart.setOption(this.trendOption, true)
			  }).catch(() => {

			  })
			},
			selectProjectRank(){
			  let params = {
			    monitorType: '3'
			  }
			  request({
			    url: 'build/monitorPointAlert/selectEquipmentNumberRank',
			    method: 'post',
			    data: params,
			    headers: {
			      'Content-Type': 'application/json;charset=UTF-8'
			    }
			  }).then(res => {
			    let data = res.data
			    let baseData = data[0].alarmNumber
			    data.forEach(item => {
			      let percent = (item.alarmNumber / baseData * 100) + '%'
			      item.percent = percent
			    })
			    this.projectRankList = data
			  }).catch(() => {

			  })
			},
			springClick(num){
			  if(num==1){
			    this.form ={};
			    this.formElevator = this.elevatorValue;
			    this.getList1();
			    this.open1=true
			  }else if(num==2){
			    this.form ={};
			    this.formElevator = this.elevatorValue;
			    this.getList2();
			    this.open2=true
			  }else if(num==3){
			    this.form ={};
			    this.formElevator = this.elevatorValue;
			    this.getList3();
			    this.open3=true
			  }
			},
			getList1(){
			  var startTime = this.form.startTime
			  var endTime = this.form.endTime
			  if (startTime != null && startTime != ''){
			    startTime = /\d{4}-\d{1,2}-\d{1,2}/g.exec(startTime)
			    startTime = startTime+" 00:00:00"
			  }
			  if (endTime != null && endTime != ''){
			    endTime = /\d{4}-\d{1,2}-\d{1,2}/g.exec(endTime)
			    endTime = endTime+" 23:59:59"
			  }
			  request({
			    url: 'build/equipmentElevatorEffect/selectPage',
			    method: 'post',
			    data: {page:this.currentPage1,limit:this.pageSize1,assetId:this.formElevator[1],startTime:startTime,endTime:endTime},
			    headers: {
			      'Content-Type': 'application/json;charset=UTF-8'
			    }
			  }).then(res => {
			    this.list1 = res.data.list
			    this.total1 = res.data.totalRow
			  }).catch(() => {

			  })
			},
			getList2(){
			  var startTime = this.form.startTime
			  var endTime = this.form.endTime
			  if (startTime != null && startTime != ''){
			    startTime = /\d{4}-\d{1,2}-\d{1,2}/g.exec(startTime)
			    startTime = startTime+" 00:00:00"
			  }
			  if (endTime != null && endTime != ''){
			    endTime = /\d{4}-\d{1,2}-\d{1,2}/g.exec(endTime)
			    endTime = endTime+" 23:59:59"
			  }
			  request({
			    url: 'build/equipmentElevatorDataAt/selectPage',
			    method: 'post',
			    data: {page:this.currentPage2,limit:this.pageSize2,equipmentId:this.formElevator[1],startTime:startTime,endTime:endTime},
			    headers: {
			      'Content-Type': 'application/json;charset=UTF-8'
			    }
			  }).then(res => {
			    this.list2 = res.data.list
			    this.total2 = res.data.totalRow
			  }).catch(() => {

			  })
			},
			getList3(){
			  var startTime = this.form.startTime
			  var endTime = this.form.endTime
			  if (startTime != null && startTime != ''){
			    startTime = /\d{4}-\d{1,2}-\d{1,2}/g.exec(startTime)
			    startTime = startTime+" 00:00:00"
			  }
			  if (endTime != null && endTime != ''){
			    endTime = /\d{4}-\d{1,2}-\d{1,2}/g.exec(endTime)
			    endTime = endTime+" 23:59:59"
			  }
			  request({
			    url: 'build/equipmentElevatorDataAt/selectPage',
			    method: 'post',
			    data: {page:this.currentPage3,limit:this.pageSize3,equipmentId:this.formElevator[1],alertType:this.form.alertType?this.form.alertType:'3',startDate:startTime,endDate:endTime},
			    headers: {
			      'Content-Type': 'application/json;charset=UTF-8'
			    }
			  }).then(res => {
			    this.list3 = res.data.list
			    this.total3 = res.data.totalRow
			  }).catch(() => {

			  })
			},
			selectDZTJ(){
				request({
					url: 'build/equipmentElevatorEffect/selectDZTJ/'+this.elevatorValue[1],
					method: 'get',
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(res => {
					this.dzObj = res.data;
					this.weightEcharts()
					this.momentEcharts()
					this.hirght()

				}).catch(() => {

				})
			},
			selectElevatorCraneById(){
				request({
					url: 'build/equipmentAssets/selectElevatorById/'+this.elevatorValue[1],
					method: 'get',
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(res => {
					this.elevator = res.data;
					request({
						url: 'build/equipmentTowerEffect/selectSJXX/'+this.elevator.assetCode,
						method: 'get',
						headers: {
							'Content-Type': 'application/json;charset=UTF-8'
						}
					}).then(res2 => {
						if(!res2.data){
							console.log(!res2.data)
							this.sjxx = {}
						}else{
							console.log(!res2.data)
							this.sjxx = res2.data;
						}

					}).catch(() => {

					})
				}).catch(() => {

				})
			},
			formatter(row, column, cellValue, index){
				if(column.property == "liftingDirection"){
					if(cellValue == "1"){
						return "静止";
					}
					if(cellValue == "2"){
						return "向上";
					}
					if(cellValue == "3"){
						return "向下";
					}
				}else if(column.property == "beforeLock" || column.property == "afterLock" ){
					if(cellValue == "0"){
						return "关闭";
					}
					if(cellValue == "1"){
						return "开启";
					}
				}else if(column.property == "bjyy"){
					switch ("1"){
						case row.loadAlarm:
							return "载重报警"
						case row.numberAlarm:
							return "人数报警"
						case row.windSpeedAlarm:
							return "风速报警"
						case row.altitudeAlarm:
							return "高度报警"
						case row.tiltAlarm:
							return "倾斜报警"
						case row.roofFlushingAlarm:
							return "冲顶报警"
						case row.upperLimitAlarm:
							return "上限位报警"
						case row.lowerLimitAlarm:
							return "下限位报警"
						case row.speedWarning:
							return "速度报警"
						case row.bedoorWarn:
							return "前门锁报警"
						case row.afdoorWarn:
							return "后门锁报警"
					}
					switch ("2"){
						case row.loadAlarm:
							return "载重预警"
						case row.numberAlarm:
							return "人数预警"
						case row.windSpeedAlarm:
							return "风速预警"
						case row.altitudeAlarm:
							return "高度预警"
						case row.tiltAlarm:
							return "倾斜预警"
						case row.roofFlushingAlarm:
							return "冲顶预警"
						case row.upperLimitAlarm:
							return "上限位预警"
						case row.lowerLimitAlarm:
							return "下限位预警"
						case row.speedWarning:
							return "速度预警"
						case row.bedoorWarn:
							return "前门锁预警"
						case row.afdoorWarn:
							return "后门锁预警"
					}
				}else{
					return cellValue;
				}
			},
			videoPalyer() {
				this.player = new EZUIKit.EZUIKitPlayer({
					id: 'myPlayer', // 视频容器ID
					accessToken: 'token', //授权过程获取的access_token
					url: '//ezopen://open.ys7.com/33011081992397348766:33011036991117755118/7.live',
					// audio: 1,//是否默认开启声音 1：打开（默认） 0：关闭
					template: "standard", // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
				})
			},
			hirght(){
				let myChart = echarts.init(document.getElementById('hirght'));
				let option = {
					tooltip: {
						trigger: "axis",
						transitionDuration:0,
            formatter:function(data){
              return data[0].name+"时</br>"+data[0].marker + data[0].seriesName + "："+ data[0].value + "M/S"
            }
					},
					color: ["#5AA3E3", "#57CB91"],
					grid: {
						right: "10%",
						left: "18%",
						top: '15%',
						bottom: '15%'
					},
					legend: {
						show: false,
						icon: "circle",
						top: 5,
						data: ['PM2.5', 'PM10'],
						right: 30,
						itemWidth: 10,
						itemHeight: 10,
						textStyle: {
							color: "#000",
							fontSize: 10
						},
					},
					xAxis: {
						data: this.dzObj.dates,
						type: 'category',
						axisLine: {
						  lineStyle: {
						    color: "#5B8FF9",
						  },
						},

						axisLabel: {
							color: '#fffff',
						},
						splitLine: {
							show: true,
							lineStyle: {
								color: '#ffffff',
								opacity: 0.2
							}
						},

					},
					yAxis: {
						nameTextStyle: {
							color: "#000",
						},
						type: 'value',
						splitLine: {
							show: true,
							lineStyle: {
								color: '#000',
								opacity: 0.2
							}
						},
						boundaryGap: true,
						axisLine: {
							show: false,
							color: 'A3E9FC'
						},
						axisLabel: {
							show: true,
							margin: 20,
							textStyle: {
								color: '#000'
							},
              formatter:'{value} M'
						},
						axisTick: {
							show: false
						}
					},
					series: [{
						name: '实时高度',
						type: 'line',
						symbol: 'none',
						smooth: true,
						areaStyle: {
							color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: 'rgba(100, 155, 248, 1)'
								},
								{
									offset: 1,
									color: 'rgba(100, 155, 248, 0.1)'
								}
							])
						},
						data: this.dzObj.gd,
						itemStyle: {
							normal: {

								borderColor: 'rgba(100, 155, 248, 1',
								lineStyle: {
									color: 'rgba(100, 155, 248, 1'
								}
							}
						}
					}, ]
				};
				myChart.setOption(option)
			},
			momentEcharts() {
				let myChart = echarts.init(document.getElementById('moment'));
				let option = {
					tooltip: {
						trigger: "axis",
						transitionDuration:0,
        formatter:function(data){
          return data[0].name+"时</br>"+data[0].marker + data[0].seriesName + "："+ data[0].value + "M/S"
        }
					},
					color: ["#5AA3E3", "#57CB91"],
					grid: {
						right: "10%",
						left: "18%",
						top: '15%',
						bottom: '15%'
					},
					legend: {
						show: false,
						icon: "circle",
						top: 5,
						data: ['PM2.5', 'PM10'],
						right: 30,
						itemWidth: 10,
						itemHeight: 10,
						textStyle: {
							color: "#000",
							fontSize: 10
						},
					},
					xAxis: {
						data: this.dzObj.dates,
						type: 'category',
						axisLine: {
						  lineStyle: {
						    color: "#5B8FF9",
						  },
						},

						axisLabel: {
							color: '#fffff',
						},
						splitLine: {
							show: true,
							lineStyle: {
								color: '#ffffff',
								opacity: 0.2
							}
						},

					},
					yAxis: {
						nameTextStyle: {
							color: "#000",
						},
						type: 'value',
						splitLine: {
							show: true,
							lineStyle: {
								color: '#000',
								opacity: 0.2
							}
						},
						boundaryGap: true,
						axisLine: {
							show: false,
							color: 'A3E9FC'
						},
						axisLabel: {
							show: true,
							margin: 20,
							textStyle: {
								color: '#000'
							},
              formatter:'{value} M/S'
						},
						axisTick: {
							show: false
						}
					},
					series: [{
						name: '实时速度',
						type: 'line',
						symbol: 'none',
						smooth: true,
						areaStyle: {
							color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: 'rgba(100, 155, 248, 1)'
								},
								{
									offset: 1,
									color: 'rgba(100, 155, 248, 0.1)'
								}
							])
						},
						data: this.dzObj.sd,
						itemStyle: {
							normal: {

								borderColor: 'rgba(100, 155, 248, 1',
								lineStyle: {
									color: 'rgba(100, 155, 248, 1'
								}
							}
						}
					}, ]
				};
				myChart.setOption(option)
			},
			weightEcharts() {
				let myChart = echarts.init(document.getElementById('weight'));
				let option = {
					tooltip: {
						trigger: "axis",
						transitionDuration:0,
            formatter:function(data){
						  return data[0].name+"时</br>"+data[0].marker + data[0].seriesName + "："+ data[0].value + "KG"
            }
					},
					color: ["#5AA3E3", "#57CB91"],
					grid: {
						right: "10%",
						left: "18%",
						top: '15%',
						bottom: '15%'
					},
					legend: {
						show: false,
						icon: "circle",
						top: 5,
						data: ['PM2.5', 'PM10'],
						right: 30,
						itemWidth: 10,
						itemHeight: 10,
						textStyle: {
							color: "#000",
							fontSize: 10
						},
					},
					xAxis: {
						data: this.dzObj.dates,
						type: 'category',
						axisLine: {
						  lineStyle: {
						    color: "#5B8FF9",
						  },
						},

						axisLabel: {
							color: '#fffff',
						},
						splitLine: {
							show: true,
							lineStyle: {
								color: '#ffffff',
								opacity: 0.2
							}
						},

					},
					yAxis: {
						nameTextStyle: {
							color: "#000",
						},
						type: 'value',
						splitLine: {
							show: true,
							lineStyle: {
								color: '#000',
								opacity: 0.2
							}
						},
						boundaryGap: true,
						axisLine: {
							show: false,
							color: 'A3E9FC'
						},
						axisLabel: {
							show: true,
							margin: 20,
							textStyle: {
								color: '#000'
							},
              formatter:'{value} KG'
						},
						axisTick: {
							show: false
						}
					},
					series: [{
						name: '实时载重',
						type: 'line',
						symbol: 'none',
						smooth: true,
						areaStyle: {
							color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: 'rgba(100, 155, 248, 1)'
								},
								{
									offset: 1,
									color: 'rgba(100, 155, 248, 0.1)'
								}
							])
						},
						data: this.dzObj.zz,
						itemStyle: {
							normal: {

								borderColor: 'rgba(100, 155, 248, 1',
								lineStyle: {
									color: 'rgba(100, 155, 248, 1'
								}
							}
						}
					}, ]
				};
				myChart.setOption(option)
			},

			handleChange(value) {
        this.elevatorValue = [];
        this.formElevator = [];
        this.elevatorValue[0] = value[0];
        this.formElevator[0] = value[0];
        this.elevatorValue[1] = value[1];
        this.formElevator[1] = value[1];

        this.trendEcharts()
        this.selectAlarmTrend()
        this.selectAlarmNumber(); //报警信息
        this.selectEquipmentNumber();//接入信息
        this.selectProjectRank()//工地排名报警
        this.remsize()
			},
			trendEcharts(xdata,data1,data2) {
				let myChart = echarts.init(document.getElementById('trend'));
				let option = {
					tooltip: {
						trigger: "axis",
						transitionDuration:0
					},
					color: ["#5AA3E3", "#57CB91"],
					grid: {
						right: "8%",
						left: "15%",
						top: '20%',
						bottom: '20%'
					},
					legend: {
						show: true,
						icon: "circle",
						top: 5,

						right: 30,
						itemWidth: 10,
						itemHeight: 10,
						textStyle: {
							color: "#000",
							fontSize: 10
						},
					},
					xAxis: {
						data: xdata,
						type: 'category',
						axisLine: {
						  lineStyle: {
						    color: "#5B8FF9",
						  },
						},

						axisLabel: {
							color: '#fffff',
						},
						splitLine: {
							show: true,
							lineStyle: {
								color: '#ffffff',
								opacity: 0.2
							}
						},

					},
					yAxis: {
						nameTextStyle: {
							color: "#000",
						},
						type: 'value',
						splitLine: {
							show: true,
							lineStyle: {
								color: '#000',
								opacity: 0.2
							}
						},
						boundaryGap: true,
						axisLine: {
							show: false,
							color: 'A3E9FC'
						},
						axisLabel: {
							show: true,
							margin: 20,
							textStyle: {
								color: '#000'
							}
						},
						axisTick: {
							show: false
						}
					},
					series: [{
						name: '预警数量',
						type: 'line',
						symbol: 'none',
						smooth: true,
						areaStyle: {
							color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: 'rgba(100, 155, 248, 0.5)'
								},
								{
									offset: 1,
									color: 'rgba(100, 155, 248, 0.5)'
								}
							])
						},
						data: data1,
						itemStyle: {
							normal: {

								borderColor: 'rgba(100, 155, 248, 0.5)',
								lineStyle: {
									color: 'rgba(100, 155, 248, 0.5)'
								}
							}
						}
					},{
					name: '报警数量',
					type: 'line',
					symbol: 'none',
					smooth: true,
					areaStyle: {
					  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
						offset: 0,
						color: 'rgba(245, 125, 101, 0.5)'
					  },
						{
						  offset: 0.5,
						  color: 'rgba(245, 125, 101, 0.5)'
						}
					  ])
					},
					data: data2,
					itemStyle: {
					  color: "rgba(245, 125, 101, 0.5)",
					  borderColor:  "#F57D65",
					  borderWidth: 2,
					},
				  }, ]
				};
				myChart.setOption(option)
			},
			trendClick(num) {
				this.trendIndex = num
				this.selectAlarmTrend()
			},
			remsize() {
				function setRemFontSize() {
					var remSize = window.innerWidth / 19.2;
					document.querySelector("html").style.fontSize = remSize + "px";
				}
				setRemFontSize();
				window.addEventListener("resize", function() {
					setTimeout(function() {
						setRemFontSize();
					}, 10000)
				});
			},
			yxzt(elevatorValue){
				if(elevatorValue.weightAlarm == '1'){
					return '载重比报警'
				}else if(elevatorValue.weightAlarm == '2'){
					return '载重比预警'
				}else if(elevatorValue.roofFlushingAlarm == '1'){
					return '冲顶报警'
				}else if(elevatorValue.roofFlushingAlarm == '2'){
					return '冲顶预警'
				}else if(elevatorValue.numberAlarm == '1'){
					return '人数报警'
				}else if(elevatorValue.numberAlarm == '2'){
					return '人数预警'
				}else if(elevatorValue.altitudeAlarm == '1'){
					return '高度报警'
				}else if(elevatorValue.altitudeAlarm == '2'){
					return '高度预警'
				}else if(elevatorValue.loadAlarm == '1'){
					return '载重报警'
				}else if(elevatorValue.loadAlarm == '2'){
					return '载重预警'
				}else if(elevatorValue.tiltAlarm == '1'){
					return '倾斜报警'
				}else if(elevatorValue.tiltAlarm == '2'){
					return '倾斜预警'
				}else if(elevatorValue.windSpeedAlarm == '1'){
					return '风速报警'
				}else if(elevatorValue.windSpeedAlarm == '2'){
					return '风速预警'
				}else if(elevatorValue.upperLimitAlarm == '1'){
					return '上限位报警'
				}else if(elevatorValue.upperLimitAlarm == '2'){
					return '上限位预警'
				}else if(elevatorValue.lowerLimitAlarm == '1'){
					return '下限位报警'
				}else if(elevatorValue.lowerLimitAlarm == '2'){
					return '下限位预警'
				}else if(elevatorValue.speedWarning == '1'){
					return '速度报警'
				}else if(elevatorValue.speedWarning == '2'){
					return '速度预警'
				}else if(elevatorValue.bedoorWarn == '1'){
					return '前门锁报警'
				}else if(elevatorValue.bedoorWarn == '2'){
					return '前门锁预警'
				}else if(elevatorValue.afdoorWarn == '1'){
					return '后门锁报警'
				}else if(elevatorValue.afdoorWarn == '2'){
					return '后门锁预警'
				}else{
					return '正常'
				}
			},
			// 更改每页展示数量
			handleSizeChange1(val) {
			  this.pageSize1 = val
			  this.getList1()
			},
			// 更改当前页数
			handleCurrentChange1(val) {
			  this.currentPage1 = val
			  this.getList1()
			},
			// 更改每页展示数量
			handleSizeChange2(val) {
			  this.pageSize2 = val
			  this.getList2()
			},
			// 更改当前页数
			handleCurrentChange2(val) {
			  this.currentPage2 = val
			  this.getList2()
			},
			// 更改每页展示数量
			handleSizeChange3(val) {
			  this.pageSize3 = val
			  this.getList3()
			},
			// 更改当前页数
			handleCurrentChange3(val) {
			  this.currentPage3 = val
			  this.getList3()
			},
			cancel1() {
			  this.open1 = false
			},
			cancel2() {
			  this.open2 = false
			},
			cancel3() {
			  this.open3 = false
			},
		},
		beforeDestroy(){
	    clearInterval(this.timer);　　// 清除定时器
	    this.timer = null;
	  },
	}
</script>

<style scoped>
	.tower_page {
		height: 100%;
		width: 16.63rem;
		display: flex;
		justify-content: space-evenly;
		align-items: center;
	}

	.tower_left {
		width: 4.16rem;
		height: 95.7%;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.4400);
		border-radius: 5px;
	}

	.model_title {
		height: 0.4rem;
		display: flex;
		align-items: center;
	}

	.model_title img {
		width: 0.16rem;
		height: 0.16rem;
		margin-left: 0.2rem;
		margin-right: 0.1rem;
	}

	.model_title div {
		font-size: 0.16rem;
		color: #000000;
	}

	.tower_police_box {
		height: 23.2%;
	}

	.tower_police_info {
		display: flex;
		margin: 0 auto;
		height: calc(100% - 0.4rem);
		justify-content: space-between;
		width: 3.7rem;
	}

	.twpolice {
		height: 100%;
		width: 1.84rem;

		display: flex;
		flex-direction: column;
		justify-content: space-evenly;
	}

	.pol_img {
		display: flex;
		align-items: center;

		justify-content: space-evenly;
	}

	.pol_img img {
		width: 0.9rem;
		height: auto;
	}

	.tower_num {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		width: 0.7rem;
	}

	.tpl_val_box {
		display: flex;
		align-items: center;
		justify-content: space-between;
		width: 100%;
		margin-top: 0.1rem;
	}

	.tpl_val {
		font-size: 0.24rem;
	}

	.tpl_unit {
		color: #404040;
		font-size: 0.14rem;
	}

	.tpl_tit {
		color: #404040;
		font-size: 0.14rem;
		width: 100%;
		text-align: justify;
		text-justify:distribute-all-lines;
		text-align-last: justify;
	}

	.police_line {
		width: 1.56rem;
		height: 3px;
		background: #E5EDFF;
		margin: 0 auto;

	}

	.ljbj_box {
		display: flex;
		align-items: center;
		justify-content: space-evenly;
	}

	.lj_title {
		color: #404040;
		font-size: 0.14rem;
		width: 0.9rem;
		text-align: center;
	}
	.ljbj_box2{
		width: 0.7rem;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
	.lj_num {
		color: #404040;
		font-size: 0.24rem;
	}

	.lj_unit {
		color: #404040;
		font-size: 0.14rem;
	}

	.equipment_num_box {
		height: 16%;
		display: flex;
		align-items: center;
	}

	.equ_num {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 3.7rem;
		margin: 0 auto;
	}

	.jr_box {
		width: 1.84rem;
		display: flex;
		align-items: center;
		justify-content: space-evenly;
	}
	.jrimg_box{
		width: 0.7rem;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.jr_box img {
		width: 0.66rem;
		height: auto;
	}
	.jrsb_box{
		width: 0.7rem;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}
	.sb_num {
		display: flex;
		justify-content: space-between;
		align-items: center;
		width: 100%;

	}

	.sb_num1 {
		color: #3397FE;
		font-size: 0.24rem;
	}

	.sb_num2 {
		color: #3397FE;
		font-size: 0.14rem;
		margin-left: 0.14rem;
	}

	.sb_tit {
		text-align: center;
		color: #404040;
		font-size: 0.14rem;
		margin-top: 5px;
		text-align: justify;
		text-justify:distribute-all-lines;
		text-align-last: justify;
		width: 100%;
	}

	.sb_line {
		width: 2px;
		height: 70px;
		background: #E5EDFF;
	}

	.trend_box {
		height: 30.2%;
		position: relative;
	}

	.trend_tab {
		width: 190px;
		justify-content: space-between;
		display: flex;
		position: absolute;
		top: 5px;
		right: 0.29rem;
	}

	.tab_con {
		width: 90px;
		height: 24px;
		background: #E6F5FF;
		border-radius: 2px;
		border: 1px solid #C8E0F8;
		line-height: 24px;
		color: #649BF8;
		font-size: 12px;
		text-align: center;
	}

	.trend_active {
		color: #FFFFFF;
		background: #649BF8;
	}

	#trend {
		height: calc(100% - 0.4rem);
	}

	.ranking_box {
		height: 32%;
	}

	.rank_list_box {
		width: 3.66rem;
		margin-left: 0.21rem;
		margin-top: 5%;
	}
	.ranking_list_box{
		height: calc(100% - 0.4rem);
		overflow: auto;
	}
	.ranking_box::-webkit-scrollbar {
		display: none;
		/* Chrome Safari */
	}

	.ranking_box {
		scrollbar-width: none;
		/* firefox */
		-ms-overflow-style: none;
		/* IE 10+ */
	}
	.no_tit_box {
		display: flex;
		align-items: center;
	}

	.ranking {
		color: #649BF8;
		font-size: 0.14rem;
		margin-right: 0.14rem;
	}

	.ranking_name {
		color: #404040;
		font-size: 0.12rem;
	}

	.ranking_bg {
		height: 8px;
		border-radius: 12px;
	}

	.ranking_speed {
		height: 8px;
		border-radius: 12px;
	}

	.rank_list_box:nth-child(1) .ranking_bg {
		background: #FFF1EE;
	}

	.rank_list_box:nth-child(2) .ranking_bg {
		background: #FFF4DF;
	}

	.rank_list_box:nth-child(3) .ranking_bg {
		background: #EEFFF7;
	}

	.rank_list_box:nth-child(4) .ranking_bg {
		background: #E6F2FF;
	}

	.rank_list_box:nth-child(5) .ranking_bg {
		background: #E6F2FF;
	}

	.rank_list_box:nth-child(1) .ranking_speed {
		background: #F4664A;
	}

	.rank_list_box:nth-child(2) .ranking_speed {
		background: #FAAD14;
	}

	.rank_list_box:nth-child(3) .ranking_speed {
		background: #57CB91;
	}

	.rank_list_box:nth-child(4) .ranking_speed {
		background: #77B8F8;
	}

	.rank_list_box:nth-child(5) .ranking_speed {
		background: #5B96FA;
	}

	/*中间样式*/

	.tower_content {
		width: 8rem;
		height: 95.7%;
	}

	.tower_select_box {
		display: flex;
		justify-content: space-between;
		align-items: center;
		width: 8rem;
	}

	/deep/.power_cascader input {
		background: #F2F8FF;
		width: 4.36rem;
		border: none;
		font-size: 0.14rem;
	}

	.wookjl {
		background: #5B97F9;
		color: #FFFFFF;
		font-size: 0.14rem;
		width: 1.1rem;
		height: 36px;
		line-height: 36px;
		border-radius: 5px;
		text-align: center;
	}

	.sjj_equ_box {
		height: 56.5%;
		width: 8rem;
		position: relative;
	}


	.sjjimg {
		width: 2.12rem;
		height: auto;
		position: absolute;
		left: 2.95rem;
		top: 6.3%;
	}

	.jtimg {
		position: absolute;
	}

	.jtimg1 {
		top: 27%;
		left: 2.9rem;
	}

	.jtimg2 {
		top: 60%;
		left: 2.9rem;
	}

	.jtimg3 {
		right: 2.81rem;
		top: 17%;
	}

	.jtimg4 {
		right: 2.81rem;
		top: 46%;
	}

	.jtimg5 {
		right: 2.81rem;
		top: 76%;
	}

	.sjjxx_box {
		position: absolute;
		width: 2.7rem;
		height: 24.3%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.4400);
		border-radius: 5px;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.sjjxx_box1 {
		left: 0.13rem;
		top: 16%;
	}

	.sjjxx_box1 .sjjxx {
		background: url(../../../../assets/img/projectAnalyse/sjjbg1.png) no-repeat;
		background-size: 100% 100%;
	}

	.sjjxx_box2 {
		right: 0.04rem;
		top: 6%;
	}

	.sjjxx_box2 .sjjxx {
		background: url(../../../../assets/img/projectAnalyse/sjjbg2.png) no-repeat;
		background-size: 100% 100%;
	}

	.sjjxx_box3 {
		left: 0.13rem;
		top: 50%;
	}

	.sjjxx_box3 .sjjxx {
		background: url(../../../../assets/img/projectAnalyse/sjjbg3.png) no-repeat;
		background-size: 100% 100%;
	}

	.sjjxx_box4 {
		right: 0.04rem;
		top: 35%;
	}

	.sjjxx_box4 .sjjxx {
		background: url(../../../../assets/img/projectAnalyse/sjjbg4.png) no-repeat;
		background-size: 100% 100%;
	}

	.sjjxx_box5 {
		right: 0.04rem;
		top: 65%;
	}

	.sjjxx_box5 .sjjxx {
		background: url(../../../../assets/img/projectAnalyse/sjjbg4.png) no-repeat;
		background-size: 100% 100%;
	}

	.sjjxx {
		width: 2.37rem;
		height: 76%;
		display: flex;
		align-items: center;
	}

	.sjjxx_tit {
		color: #404040;
		font-size: 0.14rem;
		margin-left: 0.2rem;
		margin-right: 0.1rem;
	}

	.sjjxx_val {
		color: #649BF8;
		font-size: 0.19rem;
	}

	.tj_bh_box {
		display: flex;
		align-items: center;
		margin-top: 5%;
	}

	.tjbh_tit {
		color: #404040;
		font-size: 0.14rem;
		margin-right: 0.1rem;
	}

	.tjbh_val {
		color: #3477FF;
		font-size: 0.14rem;
	}

	.ybp_box {
		height: 100%;
		width: 2.4rem;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-between;
	}

	#rotate {
		height: 78.9%;
		width: 2.4rem;
	}

	.hx_box {
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.hx_box img {
		width: 0.43rem;
		height: auto;
	}

	.hx_val {
		color: #649BF8;
		font-size: 0.24rem;
		margin-left: 0.25rem;
		margin-right: 0.25rem;
	}

	.hx_tit {
		color: #404040;
		font-size: 0.14rem;
	}

	.sjjkm_box {
		height: 6.9%;
		width: 8rem;
		display: flex;
		justify-content: space-between;
	}

	.sjjkm_list {
		width: 2.44rem;
		height: 100%;
		background: #EFF5FF;
		border-radius: 4px;
		border: 1px solid #D3E3FE;
		display: flex;
		align-items: center;

	}
	.kmimg{
		display: flex;
		align-items: center;
		justify-content: center;
		height: 100%;
	}
	.km_tit {
		font-size: 0.14rem;
		color: #404040;
	}

	.sjjkm_list div {
		text-align: center;
		width: 33.33%;
	}

	.sjjkm_list img {

		height: 85%;
	}

	.km_val {

		font-size: 0.16rem;
	}

	.km_val1 {
		color: #649BF8;
	}

	.km_val2 {
		color: #649BF8;
	}

	.km_val3 {
		color: #30BF78;
	}

	.time_lj_box {
		height: 26.7%;
		width: 8rem;
		display: flex;
	}

	.moment_box {
		width: 50%;
		height: 100%;
	}

	#moment {
		height: calc(100% - 0.4rem);
		width: 100%;
	}

	#weight {
		height: calc(100% - 0.4rem);
		width: 100%;
	}

	/*中间样式*/

	.tower_right {
		width: 3.5rem;
		height: 95.7%;
	}

	.vide_box {
		height: 24.1%;
	}

	#myPlayer {
		height: 100%;
		width: 100%;
	}

	.kj_box {
		display: flex;
		flex-direction: column;
		justify-content: center;
		height: 27.4%;
	}

	.kzdx_aj {
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.sxzy_box {
		position: relative;
		overflow: hidden;
		display: inline-block;
	}

	.sxzy_box img {
		position: absolute;
		cursor: pointer;
	}

	.sxzy_box .imgbgkz {
		position: relative;
		width: 1.5rem;
		height: auto;
	}

	.video_left {
		top: 42%;
		left: 5%;
	}

	.video_right {
		bottom: 42%;
		right: 5%;
	}

	.video_center {
		top: 38%;
		left: 43%;
	}

	.video_top {
		top: 5%;
		left: 42%;
	}

	.video_bottom {
		bottom: 5%;
		right: 42%;
	}

	.max_min {
		display: flex;
		flex-direction: column;
		clear: both;
		margin-left: 10px;
		float: left;
	}

	.max_min div {
		cursor: pointer;
		width: 80px;
		height: 40px;
		display: flex;
		align-items: center;
		justify-content: center;
		background: url(../../../../assets/screen_bg.png) no-repeat;
		background-size: 100% 100%;
	}

	.max_min div img {
		margin-right: 5px;
	}

	.kz_box {
		height: 28px;
		line-height: 28px;
		font-size: 22px;
		text-align: center;
	}

	.slider_box {
		width: 1.5rem;
		margin: 0 auto;
		clear: both;
	}

	.xx_list_details {
		height: 45.3%;
	}

	.xx_list_box {
		height: calc(100% - 0.4rem);
	}

	.xx_list {
		width: 100%;
		height: 12%;
		border-bottom: 1px solid #E1E1E1;
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-size: 0.14rem;
		color: #404040;
	}

	.xx_bg {
		height: 50%;
		width: 0.06rem;
		border-radius: 4px;
		margin-right: 0.25rem;
	}

	.xx_list_left {
		height: 100%;
		display: flex;
		align-items: center;
	}

	.sjxx_box {
		height: 25%;
	}

	.driver_box {
		height: calc(100% - 0.7rem);
		display: flex;
		margin-top: 17px;
		align-items: center;
	}

	.driver_img {
		width: 0.97rem;
		height: 80%;
	}

	.driver_con {
		width: 2.37rem;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		height: 100%;
		color: #404040;
		font-size: 0.14rem;
		margin-left: 0.2rem;
	}

	.driver_list {
		display: flex;
	}
	.ssgd_box{
		height: 26.7%;
		margin-top: 0.25rem;
	}
	#hirght{
		height: calc(100% - 0.4rem);
	}
	/deep/.el-dialog{
		    height: calc(100% - 20vh);
		    margin-top: 10vh!important;
	}
	/deep/.el-dialog__body{
		    height: 90%;
		    overflow: auto;
	}
</style>
