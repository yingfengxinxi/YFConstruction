<template>
	<div class="tower_page">
		<div class="tower_left">
			<div class="tower_police_box">
				<div class="model_title">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
					<div>设备报警</div>
				</div>
				<div class="tower_police_info">
					<div class="twpolice">
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
					<!--<div class="jr_box">-->
						<!--<div class="jrimg_box">-->
							<!--<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/sb_img1.png" />-->

						<!--</div>-->
						<!--<div class="jrsb_box">-->
							<!--<div class="sb_num">-->
								<!--<div class="sb_num1">{{equimentNumber.projectnumber}}</div>-->
								<!--<div class="sb_num2">家</div>-->
							<!--</div>-->
							<!--<div class="sb_tit">接入工地</div>-->
						<!--</div>-->
					<!--</div>-->
					<!--<div class="sb_line"></div>-->
					<div class="jr_box">
						<div class="jrimg_box">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/sb_img2.png" />
						</div>
						<div class="jrsb_box">
							<div class="sb_num">
								<div class="sb_num1">{{equimentNumber.deviceQuantity}}</div>
								<div class="sb_num2">台</div>
							</div>
							<div class="sb_tit">接入塔吊</div>
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
					<div>塔机报警排名</div>
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
				<el-cascader class='power_cascader' v-model="towerValue" :options="options" :props="props" @change="handleChange"></el-cascader>
				<div class="wookjl" @click="springClick(1)">工作循环记录</div>
				<div class="wookjl" @click="springClick(2)">监测历史记录</div>
				<div class="wookjl" @click="springClick(3)">报警历史记录</div>
			</div>
			<div class="equipment_box">
				<div class="tower_img_box">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/tjimg.png" />
					<div class="tj_bh_box">
						<div class="tjbh_tit">设备编号</div>
						<div class="tjbh_val">{{towerData.assetCode}}</div>
					</div>
          <div class="tj_bh_box">
            <div class="tjbh_tit">数据获取时间</div>
            <div class="tjbh_val">{{tower.offlineDate}}</div>
          </div>
				</div>
				<div class="tj_details_box">
					<div class="tj_details_list">
						<div class="tjdetails_val">{{yxzt.gzsc?(yxzt.gzsc/60).toFixed(2):'0'}}min</div>
						<div class="tjdetails_tit">工作时长</div>
					</div>
					<div class="tj_details_list">
						<div class="tjdetails_val">{{yxzt.jrdc?yxzt.jrdc:'0'}}次</div>
						<div class="tjdetails_tit">今日吊次</div>
					</div>
					<div class="tj_details_list">
						<div class="tjdetails_val">{{yxzt.jrdz?yxzt.jrdz:'0'}}T</div>
						<div class="tjdetails_tit">今日吊重</div>
					</div>
					<div class="tj_details_list">
						<div class="tjdetails_val">{{yxzt.jrdz/yxzt.gzsc?(yxzt.jrdz/yxzt.gzsc).toFixed(2):0}}T/min</div>
						<div class="tjdetails_tit">今日功效</div>
					</div>
					<div class="tj_details_list">
						<div class="tjdetails_val">{{yxzt.bjs?yxzt.bjs:'0'}}次</div>
						<div class="tjdetails_tit">今日报警</div>
					</div>
				</div>
				<div class="ybp_box">
					<div id="rotate">

					</div>
					<div class="hx_box">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/hximg.png" />
						<div class="hx_val">{{tower.rotation?tower.rotation:0}}度</div>
						<div class="hx_tit">回转</div>
					</div>
				</div>
			</div>
			<div class="tj_work_details">
				<div class="tj_work_box">
					<div class="tj_work_list">
						<div class="work_img">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/work1.png" />
						</div>
						<div class="work_val">
							{{tower.weight?tower.weight:0}}T
						</div>
						<div class="work_tit">
							吊重
						</div>
					</div>
					<div class="tj_work_list">
						<div class="work_img">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/work2.png" />
						</div>
						<div class="work_val">
							{{tower.amplitude?tower.amplitude:0}}M
						</div>
						<div class="work_tit">
							幅度
						</div>
					</div>
					<div class="tj_work_list">
						<div class="work_img">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/work3.png" />
						</div>
						<div class="work_val">
							{{tower.obliguity?tower.obliguity:'0'}}度
						</div>
						<div class="work_tit">
							倾度
						</div>
					</div>
					<div class="tj_work_list">
						<div class="work_img">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/work4.png" />
						</div>
						<div class="work_val">
							{{tower.windspeed?tower.windspeed:0}}m/s
						</div>
						<div class="work_tit">
							风速
						</div>
					</div>
					<div class="tj_work_list">
						<div class="work_img">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/work5.png" />
						</div>
						<div class="work_val">
							{{parseFloat((tower.amplitude?tower.amplitude:0)*(tower.weight?tower.weight:0)).toFixed(2)}} M·T
						</div>
						<div class="work_tit">
							力矩
						</div>
					</div>
					<div class="tj_work_list">
						<div class="work_img">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/work6.png" />
						</div>
						<div class="work_val">
							{{tower.height?tower.height:0}}M
						</div>
						<div class="work_tit">
							高度
						</div>
					</div>
				</div>
			</div>
			<div class="time_lj_box">
				<div class="moment_box">
					<div class="model_title">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
						<div>实时力矩</div>
					</div>
					<div id="moment">

					</div>
				</div>
				<div class="moment_box">
					<div class="model_title">
						<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png" />
						<div>实时吊重</div>
					</div>
					<div id="weight">

					</div>
				</div>
			</div>
		</div>
		<div class="tower_right">
			<div class="vide_box">
				<div id="myPlayer"></div>
			</div>
			<div class="kj_box">
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
			</div>
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
						<div class="xx_val">{{towerData.name}}</div>
					</div>
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #5B97F9;"></div>
							<div class="xx_tit">设备编号</div>
						</div>
						<div class="xx_val">{{towerData.assetCode}}</div>
					</div>
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #5BC7F9;"></div>
							<div class="xx_tit">最大吊重</div>
						</div>
						<div class="xx_val">{{towerData.maxWeight}}T</div>
					</div>
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #18BAA0;"></div>
							<div class="xx_tit">最大力矩</div>
						</div>
						<div class="xx_val">{{towerData.ratedTorque}}T·M</div>
					</div>
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #BDEB80;"></div>
							<div class="xx_tit">前臂长</div>
						</div>
						<div class="xx_val">{{towerData.foreArmLength}}米</div>
					</div>
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #F9C827;"></div>
							<div class="xx_tit">后臂长</div>
						</div>
						<div class="xx_val">{{towerData.rearArmLength}}米</div>
					</div>
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #FFA62A;"></div>
							<div class="xx_tit">塔帽高</div>
						</div>
						<div class="xx_val">{{towerData.capHeight}}米</div>
					</div>
					<div class="xx_list">
						<div class="xx_list_left">
							<div class="xx_bg" style="background: #F4664A;"></div>
							<div class="xx_tit">塔臂高</div>
						</div>
						<div class="xx_val">{{towerData.armHeight}}米</div>
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
		</div>
		<el-dialog title="工作循环记录" :visible.sync="open1" width="1200px">
			<el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
				<el-form-item>
					<el-cascader class='power_cascader' v-model="formTower" :options="options" :props="props" @change="handleChange"></el-cascader>
				</el-form-item>
				<el-form-item>
					<el-date-picker class="seledate" style="width: 220px;" v-model="form.startTime" type="date" value-format="yyyy-MM-dd" :clearable='false' placeholder="开始日期">
					</el-date-picker>
					</el-form-item>
					<el-form-item>
						<el-date-picker class="seledate" style="width: 220px;" v-model="form.endTime" type="date" value-format="yyyy-MM-dd" :clearable='false' placeholder="结束日期">
						</el-date-picker>
					</el-form-item>
					<el-form-item style="float: right">
						<el-button type="primary" icon="el-icon-search" size="mini" @click="getList1()">搜索</el-button>
					</el-form-item>
			</el-form>
			<el-table v-loading="loading" :data="list1" highlight-current-row height='75%'>
				<el-table-column label="项目名称" align="center" prop="projectName"  width="160" />
				<el-table-column label="塔机名称" align="center" prop="assetName" />
				<el-table-column label="塔机编号" align="center" prop="assetCode"  width="160" />
				<el-table-column label="开始时间" align="center" prop="startTime" width="200" />
				<el-table-column label="结束时间" align="center" prop="endTime" width="200" />
				<el-table-column label="本次循环最大钓重" align="center" prop="weight"  width="160"/>
				<el-table-column label="本次循环最大力矩" align="center" prop="maxPercent"  width="160"/>
				<el-table-column label="最大高度(M)" align="center" prop="maxHeight"  width="140"/>
				<el-table-column label="最小高度(M)" align="center" prop="minHeight" width="140"/>
				<el-table-column label="最大幅度(M)" align="center" prop="maxRadius" width="140"/>
				<el-table-column label="最小幅度(M)" align="center" prop="minRadius" width="140"/>
				<el-table-column label="起吊点角度(°)" align="center" prop="startAngle" width="140"/>
				<el-table-column label="卸掉点角度(°)" align="center" prop="endAngle" width="140"/>
				<el-table-column label="起掉点高度(°)" align="center" prop="liftingPointHeight" width="140" />
				<el-table-column label="卸掉点高度(°)" align="center" prop="liftingPointHeightUnload" width="140"/>
				<el-table-column label="起掉点幅度(M)" align="center" prop="liftingPointRadius" width="140" />
				<el-table-column label="卸掉点幅度(M)" align="center" prop="liftingPointRadiusUnload" width="140" />
			</el-table>
			<div class="p-contianer">
				<el-pagination :current-page="currentPage1" :page-sizes="pageSizes1" :page-size.sync="pageSize1" :total="total1" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange1" @current-change="handleCurrentChange1" />
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="cancel1">取 消</el-button>
			</div>
		</el-dialog>

		<el-dialog title="监测历史记录" :visible.sync="open2" width="1200px">
			<el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
				<el-form-item>
					<el-cascader class='power_cascader' v-model="formTower" :options="options" :props="props" @change="handleChange"></el-cascader>
				</el-form-item>
				<el-form-item>
					<el-date-picker class="seledate" style="width: 220px;" v-model="form.startTime" type="date" value-format="yyyy-MM-dd" :clearable='false' placeholder="开始日期">
					</el-date-picker>
				</el-form-item>
				<el-form-item>
					<el-date-picker class="seledate" style="width: 220px;" v-model="form.endTime" type="date" value-format="yyyy-MM-dd" :clearable='false' placeholder="结束日期">
					</el-date-picker>
				</el-form-item>
				<el-form-item style="float: right">
					<el-button type="primary" icon="el-icon-search" size="mini" @click="getList2()">搜索</el-button>
				</el-form-item>
			</el-form>
			<el-table v-loading="loading" :data="list2" highlight-current-row height='75%'>
				<el-table-column label="项目名称" align="center" prop="projectName"  width="160" />
				<el-table-column label="塔机名称" align="center" prop="assetName" />
				<el-table-column label="塔机编号" align="center" prop="assetCode"  width="160"/>
				<el-table-column label="监测时间" align="center" prop="monitorTime" width="200" />
				<el-table-column label="幅度(M)" align="center" prop="amplitude" />
				<el-table-column label="高度(M)" align="center" prop="height" />
				<el-table-column label="转角(°)" align="center" prop="rotation" />
				<el-table-column label="吊重(T)" align="center" prop="weight" />
				<el-table-column label="风速(M/S)" align="center" prop="windspeed" />
				<el-table-column label="倾角(°)" align="center" prop="obliguity" />
				<el-table-column label="力矩百分比" align="center" prop="moment" />
				<el-table-column label="吊重百分比" align="center" prop="weightPercentage" />
				<el-table-column label="风速百分比" align="center" prop="windPercentage" />
				<el-table-column label="倾角百分比" align="center" prop="obliguityPercentage" />
			</el-table>
			<div class="p-contianer">
				<el-pagination :current-page="currentPage2" :page-sizes="pageSizes2" :page-size.sync="pageSize2" :total="total2" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange2" @current-change="handleCurrentChange2" />
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="cancel2">取 消</el-button>
			</div>
		</el-dialog>

		<el-dialog title="报警历史记录" :visible.sync="open3" width="1200px">
			<el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="110px" style="padding-top: 20px;padding-left:10px;">
				<el-form-item>
					<el-cascader class='power_cascader' v-model="formTower" :options="options" :props="props" @change="handleChange"></el-cascader>
					<el-select v-model="form.alertType" clearable placeholder="请选择报警类型">
						<el-option v-for="item in alertTypeOption" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item>
					<el-date-picker class="seledate" style="width: 220px;" v-model="form.startTime" type="date" value-format="yyyy-MM-dd" :clearable='false' placeholder="开始日期">
					</el-date-picker>
				</el-form-item>
				<el-form-item>
					<el-date-picker class="seledate" style="width: 220px;" v-model="form.endTime" type="date" value-format="yyyy-MM-dd" :clearable='false' placeholder="结束日期">
					</el-date-picker>
				</el-form-item>
				<el-form-item style="float: right">
					<el-button type="primary" icon="el-icon-search" size="mini" @click="getList3()">搜索</el-button>
				</el-form-item>
			</el-form>
			<el-table v-loading="loading" :data="list3" highlight-current-row height='75%'>
				<el-table-column label="项目名称" align="center" prop="projectName" width="160" />
				<el-table-column label="塔机名称" align="center" prop="assetName" />
				<el-table-column label="塔机编号" align="center" prop="assetCode"  width="160"/>
				<el-table-column label="报警时间" align="center" prop="createTime" width="200" />
				<el-table-column label="报警内容" align="center" prop="content" :formatter="formatter" width="300" />
				<el-table-column label="报警数据" align="center" prop="monitorData"  />
				<el-table-column label="报警类型" align="center" prop="alertType" :formatter="formatter" />
				<el-table-column label="处理状态" align="center" prop="handleState" :formatter="formatter" />
				<!--<el-table-column label="吊重(T)" align="center" prop="weight" />-->
				<!--<el-table-column label="风速(M/S)" align="center" prop="windspeed" />-->
				<!--<el-table-column label="倾角(°)" align="center" prop="obliguity" />-->
				<!--<el-table-column label="力矩百分比" align="center" prop="moment" />-->
				<!--<el-table-column label="吊重百分比" align="center" prop="weightPercentage" />-->
				<!--<el-table-column label="风速百分比" align="center" prop="windPercentage" />-->
				<!--<el-table-column label="倾角百分比" align="center" prop="obliguityPercentage" />-->
				<!--<el-table-column label="制动状态" width="400" align="center" prop="brakingStatus" :formatter="formatter" />-->

			</el-table>
			<div class="p-contianer">
				<el-pagination :current-page="currentPage3" :page-sizes="pageSizes3" :page-size.sync="pageSize3" :total="total3" class="p-bar" background layout="prev, pager, next, sizes, total, jumper" @size-change="handleSizeChange3" @current-change="handleCurrentChange3" />
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
				alarmNumber: {},
				equimentNumber: {},
				projectRankList: [],
				options: [],
				towerValue: [],
				formTower: [],
				props: {
					value: 'id',
					label: 'name'
				},
				towerData: {},
				yxzt: {},
				tower: {},
				sjxx: {},
				dzObj: {},
				videoToken: '',
				videoData: {},
				trendOption: {},
				trendChart: {},
				player: null,
				sliderVidewo: 150,
				// 显示搜索条件
				showSearch: true,
				form: {},
				queryForm: {},
				// 物料称重查询参数
				queryParams: {
					page: this.currentPage,
					limit: this.pageSize,
					hydrantCode: undefined,
					pipeId: undefined
				},
				alertTypeOption: [{
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
				timer: null
			}
		},
		computed: {
			...mapGetters(['pageDataReflushTime']),
		},
		mounted() {
			let that = this
			this.timer = setInterval(function() {
				that.selectJRGX();
				that.selectDZTJ();
				that.selectTowerCraneById();
			}, that.pageDataReflushTime)
			this.selectAlarmTrend()
			request({
				url: 'build/equipmentAssets/selectAllTower',
				method: 'post',
				data: {},
				headers: {
					'Content-Type': 'application/json;charset=UTF-8'
				}
			}).then(res => {
				this.options = res.data
				this.towerValue[0] = this.options[0].id
				this.towerValue[1] = this.options[0].children[0].id
				this.formTower[0] = this.options[0].id
				this.formTower[1] = this.options[0].children[0].id
				this.towerData = this.options[0].children[0]
				this.selectJRGX();
				this.selectDZTJ();
				this.selectTowerCraneById();
				this.getVideo();
			}).catch(() => {

			})

			this.selectAlarmNumber(); //报警信息
			this.selectEquipmentNumber(); //接入信息

			this.selectProjectRank() //工地排名报警

			this.remsize()
		},
		methods: {
			control(type) { //按下
				request({
					url: 'build/ysy/ptzControl',
					method: 'post',
					data: {
						deviceId: this.videoData.deviceChannelId,
						channelId: this.videoData.deviceChannelId,
						type: type,
						speed: this.sliderVidewo
					},
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(res => {
					if(res.data.code == 412 && type == 'stop') {
						this.$message.error(res.data.msg);
					}
				}).catch(err => {})

			},
			// ========================分割线动态实装开始===================================
			selectAlarmNumber() {
				let params = {
					monitorType: '2'
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
			selectEquipmentNumber() {
				let params = {
					monitorType: '2'
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
			selectAlarmTrend() {
				let params = {
					monitorType: '2',
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
					this.trendEcharts(xData, yData1, yData2) //报警趋势图表加载
					// this.trendChart.setOption(this.trendOption, true)
				}).catch(() => {

				})
			},
			selectProjectRank() {
				let params = {
					monitorType: '2'
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
			selectJRGX() {
				request({
					url: 'build/equipmentTowerEffect/selectJRGX/' + this.towerValue[1],
					method: 'get'
				}).then(res => {
					this.yxzt = res.data;
				})
			},
			selectDZTJ() {
				request({
					url: 'build/equipmentTowerEffect/selectDZTJ/' + this.towerValue[1],
					method: 'get'
				}).then(res => {
					this.dzObj = res.data;
					this.momentEcharts()
					this.weightEcharts()
				})
			},
			selectTowerCraneById() {
				request({
					url: 'build/equipmentAssets/selectTowerCraneById/' + this.towerValue[1],
					method: 'get'
				}).then(res => {
					this.tower = res.data;
					request({
						url: 'build/equipmentTowerEffect/selectSJXX/' + this.tower.assetCode,
						method: 'get'
					}).then(res2 => {
						if(!res2.data) {
							this.sjxx = {}
						} else {
							this.sjxx = res2.data;
						}
					})
					this.rotateEcharts()
				})
			},
			getVideo() {
				request({
					url: 'build/ysy/getAccessToken',
					method: 'post',
					data: {},
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(res => {
					this.videoToken = res.data.accessToken
					request({
						url: 'build/videoMonitorData/selectVideoByEquipmentId/' + this.towerValue[1],
						method: 'get'
					}).then(res2 => {
						this.videoData = res2.data
						this.videoPalyer()
					})
				}).catch(() => {

				})
			},
			handleChange(value) {
        this.towerValue = [];
        this.formTower = [];
				var than = this;
				this.towerValue[0] = value[0]
				this.towerValue[1] = value[1]
				this.formTower[0] = value[0]
				this.formTower[1] = value[1]
        console.log("切换",this.towerValue)
        console.log("切换",this.formTower)
				this.options.forEach(function(v, i) {
					if(v.id == value[0]) {
						v.children.forEach(function(v2, i2) {
							if(v2.id == value[1]) {
								than.towerData = v2
							}
						})
					}

				})
				// this.towerData = this.options[0].children[0]
				this.selectJRGX();
				this.selectDZTJ();
				this.selectTowerCraneById();
        if( than.player){
          than.player.stop().then(() => {
            than.player.destroy().then(() => {
              than.player=null
              than.getVideo();

            })
          }).catch(()=>{
          })
        }else{
          than.getVideo();
        }
        this.$forceUpdate();
			},
			springClick(num) {
				if(num == 1) {
					this.form = {};
					this.formTower = this.towerValue;
					this.getList1();
					this.open1 = true
				} else if(num == 2) {
					this.form = {};
					this.formTower = this.towerValue;
					this.getList2();
					this.open2 = true
				} else if(num == 3) {
					this.form = {};
					this.formTower = this.towerValue;
					this.getList3();
					this.open3 = true
				}
			},
			getList1() {
				var startTime = this.form.startTime
				var endTime = this.form.endTime
				if(startTime != null && startTime != '') {
					startTime = /\d{4}-\d{1,2}-\d{1,2}/g.exec(startTime)
					startTime = startTime + " 00:00:00"
				}
				if(endTime != null && endTime != '') {
					endTime = /\d{4}-\d{1,2}-\d{1,2}/g.exec(endTime)
					endTime = endTime + " 23:59:59"
				}
				request({
					url: 'build/equipmentTowerEffect/selectPage',
					method: 'post',
					data: {
						page: this.currentPage1,
						limit: this.pageSize1,
						assetId: this.formTower[1],
						startTime: startTime,
						endTime: endTime
					},
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(res => {
					this.list1 = res.data.list
					this.total1 = res.data.totalRow
				}).catch(() => {

				})
			},
			getList2() {
				var startTime = this.form.startTime
				var endTime = this.form.endTime
				if(startTime != null && startTime != '') {
					startTime = /\d{4}-\d{1,2}-\d{1,2}/g.exec(startTime)
					startTime = startTime + " 00:00:00"
				}
				if(endTime != null && endTime != '') {
					endTime = /\d{4}-\d{1,2}-\d{1,2}/g.exec(endTime)
					endTime = endTime + " 23:59:59"
				}
				request({
					url: 'build/equipmentTowerDataAt/selectPage',
					method: 'post',
					data: {
						page: this.currentPage2,
						limit: this.pageSize2,
						equipmentId: this.formTower[1],
						startTime: startTime,
						endTime: endTime
					},
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(res => {
					this.list2 = res.data.list
					this.total2 = res.data.totalRow
				}).catch(() => {})
			},
			getList3() {
				var startTime = this.form.startTime
				var endTime = this.form.endTime
				if(startTime != null && startTime != '') {
					startTime = /\d{4}-\d{1,2}-\d{1,2}/g.exec(startTime)
					startTime = startTime + " 00:00:00"
				}
				if(endTime != null && endTime != '') {
					endTime = /\d{4}-\d{1,2}-\d{1,2}/g.exec(endTime)
					endTime = endTime + " 23:59:59"
				}
				request({
					url: 'build/monitorPointAlert/selectPage',
					method: 'post',
					data: {
						page: this.currentPage3,
						limit: this.pageSize3,
						equipmentId: this.formTower[1],
						// alertType: this.form.alertType ? this.form.alertType : '3',
            monitorType:'2',
						startDate: startTime,
						endDate: endTime
					},
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(res => {
					this.list3 = res.data.list
					this.total3 = res.data.totalRow
				}).catch(() => {

				})
			},
			formatter(row, column, cellValue, index) {
				if(column.property == "bjyy") {
					switch("1") {
						case row.weightAlarm:
							return "吊重报警"
						case row.momentAlarm:
							return "力矩报警"
						case row.obliguityAlarm:
							return "倾角报警"
						case row.windSpeedAlarm:
							return "风速报警"
						case row.towerCollisionAlarm:
							return "碰撞报警"
						case row.minRangeAlarm:
							return "幅度报警"
						case row.heightAlarm:
							return "高度报警"
						case row.posAngleAlarm:
							return "回转报警"
					}
					switch("2") {
						case row.weightAlarm:
							return "吊重预警"
						case row.momentAlarm:
							return "力矩预警"
						case row.obliguityAlarm:
							return "倾角预警"
						case row.windSpeedAlarm:
							return "风速预警"
						case row.towerCollisionAlarm:
							return "碰撞预警"
						case row.minRangeAlarm:
							return "幅度预警"
						case row.heightAlarm:
							return "高度预警"
						case row.posAngleAlarm:
							return "回转预警"
					}
				} else if(column.property == "brakingStatus" && cellValue != null) {
					console.log(cellValue)
					var str = "";
					if(cellValue.substring(7, 8) == '1') {
						str += "上：闭合 "
					} else {
						str += "上：断开 "
					}
					if(cellValue.substring(6, 7) == '1') {
						str += "下：闭合 "
					} else {
						str += "下：断开 "
					}
					if(cellValue.substring(5, 6) == '1') {
						str += "前：闭合 "
					} else {
						str += "前：断开 "
					}
					if(cellValue.substring(4, 5) == '1') {
						str += "后：闭合 "
					} else {
						str += "后：断开 "
					}
					if(cellValue.substring(3, 4) == '1') {
						str += "左：闭合 "
					} else {
						str += "左：断开 "
					}
					if(cellValue.substring(2, 3) == '1') {
						str += "右：闭合 "
					} else {
						str += "右：断开 "
					}

					return str
				}else if(column.property == "alertType" && cellValue != null){
				  console.log(cellValue)
          if(cellValue == '1') {
            str = "报警"
          }else {
            str = "预警"
          }
          return str;
        }else if(column.property == "handleState" && cellValue != null){
          console.log(cellValue)
          if(cellValue == '1') {
            str = "已处理"
          }else {
            str = "未处理"
          }
          return str;
        } else {
					return cellValue;
				}
			},
			// ========================分割线动态实装结束===================================
			videoPalyer() {
				this.player = new EZUIKit.EZUIKitPlayer({
					id: 'myPlayer', // 视频容器ID
					accessToken: this.videoToken, //授权过程获取的access_token
					url: this.videoData.url,
					// audio: 1,//是否默认开启声音 1：打开（默认） 0：关闭
					template: "standard", // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
				})
			},
			momentEcharts() {
				let myChart = echarts.init(document.getElementById('moment'));
				let option = {
					tooltip: {
						trigger: "axis",
						transitionDuration: 0,
            formatter:function(data){
              return data[0].name+"时</br>"+data[0].marker + data[0].seriesName + "："+ data[0].value + "T·M"
            }
					},
					color: ["#5AA3E3", "#57CB91"],
					grid: {
						right: "10%",
						left: "15%",
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
              formatter:'{value} T·M'
						},
						axisTick: {
							show: false
						}
					},
					series: [{
						name: '实时力矩',
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
						data: this.dzObj.lj,
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

						transitionDuration: 0,
            formatter:function(data){
              return data[0].name+"时</br>"+data[0].marker + data[0].seriesName + "："+ data[0].value + "T"
            }
					},
					color: ["#5AA3E3", "#57CB91"],
					grid: {
						right: "10%",
						left: "15%",
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
							show: true,
							color: '#A3E9FC'
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
              formatter:'{value} T'
						},
						axisTick: {
							show: false
						}
					},
					series: [{
						name: '实时吊重',
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
						data: this.dzObj.dz,
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
			rotateEcharts() {
				let myChart = echarts.init(document.getElementById('rotate'));
				let option = {
					series: [{
						type: 'gauge',
						center: ['50%', '50%'],
						radius: '90%',
						z: 100,
						min: 0, //最小刻度
						max: 360, //最大刻度
						splitNumber: 10, //刻度数量
						startAngle: 269.999,
						endAngle: -90,
						axisLine: {
							show: true,
							lineStyle: {
								width: 1,
								color: [
									[1, 'rgba(0,0,0,0)']
								]
							}
						}, //仪表盘轴线
						pointer: {
							show: true,
							width: 3,
							length: "70%",

						},
						itemStyle: {
							color: '#5B97F9'
						},
						axisTick: {
							show: true,
							splitNumber: 7,
							lineStyle: {
								color: '#367BEE',
								width: 1,
							},
							length: -8
						}, //刻度样式
						splitLine: {
							show: true,
							length: -15,
							lineStyle: {
								color: '#404040'
							}
						}, //分隔线样式
						axisLabel: {
							show: true,
							distance: 25,
							color: '#404040'
						}, //刻度标签。
						detail: {
							show: false,
						},
						data: [{
							value: this.tower.rotation
						}]
					}, ]
				};
				myChart.setOption(option)
			},
			trendEcharts(xdata, data1, data2) {
				let myChart = echarts.init(document.getElementById('trend'));
				let option = {
					tooltip: {
						trigger: "axis",
						transitionDuration: 0
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
              formatter:'{value}'
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
					}, {
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
							borderColor: "#F57D65",
							borderWidth: 2,
						},
					}, ]
				};
				myChart.setOption(option)
				this.trendOption = option
				this.trendChart = myChart
				//this.selectAlarmTrend()//报警趋势数据
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
					}, 200)
				});
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
		beforeDestroy() {
			clearInterval(this.timer);　　 // 清除定时器
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
		text-justify: distribute-all-lines;
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

	.ljbj_box2 {
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

	.jrimg_box {
		width: 0.7rem;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.jr_box img {
		width: 0.66rem;
		height: auto;
	}

	.jrsb_box {
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
		text-justify: distribute-all-lines;
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

	.ranking_list_box {
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

	.rank_list_box:nth-child(1n) .ranking_bg {
		background: #FFF1EE;
	}

	.rank_list_box:nth-child(2n) .ranking_bg {
		background: #FFF4DF;
	}

	.rank_list_box:nth-child(3n) .ranking_bg {
		background: #EEFFF7;
	}

	.rank_list_box:nth-child(4n) .ranking_bg {
		background: #E6F2FF;
	}

	.rank_list_box:nth-child(5n) .ranking_bg {
		background: #E6F2FF;
	}

	.rank_list_box:nth-child(1n) .ranking_speed {
		background: #F4664A;
	}

	.rank_list_box:nth-child(2n) .ranking_speed {
		background: #FAAD14;
	}

	.rank_list_box:nth-child(3n) .ranking_speed {
		background: #57CB91;
	}

	.rank_list_box:nth-child(4n) .ranking_speed {
		background: #77B8F8;
	}

	.rank_list_box:nth-child(5n) .ranking_speed {
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

	.equipment_box {
		height: 40.6%;
		display: flex;
		margin-top: 2%;
		width: 8rem;
		justify-content: space-between;
	}

	.tower_img_box {
		height: 100%;
		width: 2.61rem;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}

	.tower_img_box img {
		width: 2.61rem;
		height: auto;
	}

	.tj_bh_box {
		display: flex;
		align-items: center;
		justify-content: center;
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

	.tj_details_box {
		height: 100%;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}

	.tj_details_list {
		height: 17.1%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.4400);
		border-radius: 5px;
		width: 2.44rem;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.tjdetails_val {
		color: #649BF8;
		font-size: 0.24rem;
		margin-left: 0.2rem;
	}

	.tjdetails_tit {
		color: #404040;
		margin-right: 0.2rem;
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

	.tj_work_details {
		height: 24.6%;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.tj_work_box {
		height: 58.8%;
		width: 7.44rem;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
	}

	.tj_work_list {
		width: 2.44rem;
		height: 40.8%;
		background: #EFF5FF;
		border-radius: 4px;
		border: 1px solid #D3E3FE;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.work_img {
		width: 25%;
		text-align: center;
		display: flex;
		align-items: center;
		justify-content: center;
		height: 100%;
	}

	.work_img img {
		height: 87%;
	}

	.work_val {
		width: 50%;
		color: #649BF8;
		font-size: 0.2rem;
		text-align: center;
	}

	.work_tit {
		width: 25%;
		color: #404040;
		font-size: 0.14rem;
		text-align: center;
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
		height: 25.4%;
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
		height: 22.3%;
	}

	.xx_list_box {
		height: calc(100% - 0.4rem);
	}

	.xx_list {
		width: 50%;
		height: 24%;
		float: left;
		border-bottom: 1px solid #E1E1E1;
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-size: 0.14rem;
		color: #404040;
	}

	.xx_list:nth-of-type(even) .xx_bg {
		margin-left: 0.1rem;
	}

	.xx_bg {
		height: 50%;
		width: 0.06rem;
		border-radius: 4px;
		margin-right: 0.15rem;
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
		height: calc(100% - 0.4rem);
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

	/deep/.el-dialog {
		height: calc(100% - 20vh);
		margin-top: 10vh!important;
	}

	/deep/.el-dialog__body {
		height: 90%;
		overflow: auto;
	}
</style>
