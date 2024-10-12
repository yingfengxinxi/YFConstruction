<template>
	<div class="employee-dep-management" style="margin: 10px;background-color: white; height: 96.5%">
		<!--    <xr-header-->
		<!--      icon-class="wk wk-manage"-->
		<!--      icon-color="#2EC2F9"-->
		<!--      label="视频查看"/>-->
		<div class="customer-content" v-loading="initLoading" element-loading-text="切换中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 1)">
			<div class="system-view-nav">
				<div class="tree_box">
					<el-tree :props="props" :load="loadNode" lazy @node-click="nodeClick" />
				</div>
				<div class="lately_box">
					<div class="kz_box">
						{{kzName}}
					</div>
					<div class="split_screen">
						 <el-tooltip class="item" effect="dark" content="全屏" placement="top">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    @click="changeVideo(1)"  src="../../../../assets/video_split1.png" />
						</el-tooltip>
						<el-tooltip class="item" effect="dark" content="四分屏" placement="top">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    @click="changeVideo(4)"  src="../../../../assets/video_split4.png" />
						</el-tooltip>
						<el-tooltip class="item" effect="dark" content="九分屏" placement="top">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    @click="changeVideo(9)"  src="../../../../assets/video_split9.png" />
						</el-tooltip>
						<!-- <el-tooltip class="item" effect="dark" content="视频执法" placement="top">
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    @click="videoEnforce()"  src="../../../../assets/video_split.png" />
						</el-tooltip> -->
					</div>
					<div class="box_center">
						<div class="sxzy_box">
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
					<div class="slider_box">
						<el-tooltip class="item" effect="dark" content="旋转速度" placement="top">
						<el-slider v-model="sliderVidewo" :min="30" :max="250"></el-slider>
						</el-tooltip>
					</div>
				</div>
			</div>

			<div class="system-view-content" style="height:100%;">
				<!-- <div class="screen">
					<div @click="changeVideo(1)" :class="activeNum==1?'activevideo':''">一份屏</div>
					<div @click="changeVideo(4)" :class="activeNum==4?'activevideo':''">四分屏</div>
					<div @click="changeVideo(9)" :class="activeNum==9?'activevideo':''">九分屏</div>
				</div> -->
				<!--          <video-->
				<!--            ref="videoPlayer" :src="src"  style="width: 100%; height: 100%; object-fit: fill" />-->
				<!-- <video ref="video" muted style="width: 100%; height: 100%; object-fit: fill"></video> -->
				<div class="video_con" v-loading="fullscreenLoading" element-loading-text="切换中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 1)">
					<div class="video_box" :style="{width:widthHeight,height:widthHeight}" :class="clickIndex==index?'clickActive':''" v-for="(item,index) in num" :key="index">
						<span class="mask" @click="clickVideo(index)" v-show="hiddenIndex!=index"></span>
						<div :id="'myPlayer'+index" style="wid th: 100%;height: 100%;"></div>
					</div>
				</div>
			</div>
		</div>

		<!-- 监控执法新增 -->
		<el-dialog :title="title" :visible.sync="addOpen" width="900px" append-to-body>
			<el-form ref="form" :model="form" :rules="rules" label-width="100px" class="add-form">
				<el-row>
					<el-col :span="12">
						<el-form-item label="项目名称" prop="projectId" class="add-form-input">
							<el-select v-model="form.projectId" placeholder="请选择" clearable size="small" style="width: 330px">
								<el-option v-for="dict in projectOptions" :key="dict.id" :label="dict.name" :value="dict.id" />
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="发现时间" prop="findDate" class="add-form-input">
							<el-date-picker v-model="form.findDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 330px" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="发现人" prop="findPeople" class="add-form-input">
							<el-input v-model="form.findPeople" placeholder="请输入发现人" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="发现问题" prop="findProblem" class="add-form-input">
							<el-input v-model="form.findProblem" placeholder="请输入问题" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="处罚措施" prop="punishMeasures" class="add-form-input">
							<el-input v-model="form.punishMeasures" placeholder="请输入处罚措施" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="处罚条例" prop="punishReg" class="add-form-input">
							<el-input v-model="form.punishReg" placeholder="请输入处罚条例" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="整改期限" prop="rectTerm" class="add-form-input">
							<el-input v-model="form.rectTerm" placeholder="请输入整改期限" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="整改要求" prop="rectReq" class="add-form-input">
							<el-input v-model="form.rectReq" placeholder="请输入整改要求" />
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="执法依据附件">
							<wk-upload-file ref="wkUploadFile" name="file" multiple accept="*" :autoUpload="false" :batchId="batchId"></wk-upload-file>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm">确 定</el-button>
				<el-button @click="addCancel">取 消</el-button>
			</div>
		</el-dialog>

	</div>
</template>

<script>
import XrHeader from '@/components/XrHeader'
import {
	queryByParentId,
	queryTreeListByParentId,
	queryEquiBytypeId,
	queryVideoMonitor,
	getVideoToken
} from '@/api/build/videoMonitor'
import request from '@/utils/request'
// let Hls = require('hls.js');
//import EZUIKit from 'ezuikit-js';

import EZUIKit from './EZUIKitPlayer.js';
export default {
	name: 'videoView',
	components: {
		XrHeader
	},
	data () {
		return {
			props: {
				label: 'label',
				children: 'hasChildren',
				isLeaf: 'leaf'
			},
			src: '',
			hls: '',
			deviceId: '', //设备id
			channelId: '', //通道id
			player: [],
			num: 4,
			widthHeight: '50%',
			videoIndex: 0,
			activeNum: 4,
			// videoToken: '',
			selected: false,
			clickIndex: 0,
			kzName: '',
			record: [],
			arrList: [], //储存搜索
			replayCount: {}, //重新播放计数，十五次失败时候不再重新播放
			fullscreenLoading: false,
			sliderVidewo: 30,
			hiddenIndex: 0,
			nodes: [],
			isHook: false,
			node_had: '',
			resolve_had: '',
			initLoading: false,//页面切换遮罩
						//监控执法新增
			title: '',
			addOpen: false,
			form: {},
			rules: {
				projectId: [
					{ required: true, message: '请选择项目', trigger: 'blur' }
				],
				findDate: [
					{ required: true, message: '发现时间不能为空', trigger: 'blur' }
				],
				findPeople: [
					{ required: true, message: '发现人不能为空', trigger: 'blur' }
				],
				findProblem: [
					{ required: true, message: '发现问题不能为空', trigger: 'blur' }
				],
				punishMeasures: [
					{ required: true, message: '处罚措施不能为空', trigger: 'blur' }
				],
				punishReg: [
					{ required: true, message: '处罚条例不能为空', trigger: 'blur' }
				],
				rectTerm: [
					{ required: true, message: '整改期限不能为空', trigger: 'blur' }
				],
				rectReq: [
					{ required: true, message: '整改要求不能为空', trigger: 'blur' }
				],
			},

			projectOptions: [],
			batchId: '',
			videoId:''
		}
	},
	watch: {
		$route: {
			handler (newRouter, old) {
				// if (old != undefined) {
				// 	console.log(newRouter.name == old.name)
				// 	console.log(this.$route.query.isHook!=true)
				// 	console.log(newRouter.name == old.name&&this.$route.query.isHook!=true)
				// 	if (newRouter.name == old.name&&this.$route.query.isHook!=true) {
				// 		this.isHook = false;
				// 		this.requestNewData()
				// 	}
				// }
        // 最开始初始化时先判断是否是吊钩进入
        var pathNew = newRouter.path
        //根据路由判断当前页面是视频监控查看或吊钩可视化
        if ( pathNew.indexOf("hookBlindVisual") != -1) {
          this.isHook = true;
        } else if (pathNew.indexOf("videoMonitorCheck") != -1) {
          this.isHook = false;
        }
				//先判断页面之前有没有被打开过，如果打开过则重置
				if (old != undefined) {
					this.initLoading = true;
					this.requestNewData()//重置树节点
					this.videoPlayerInit()//清空视频组件及其他信息
				}

				//根据路由判断当前页面是视频监控查看或吊钩可视化
				if ( pathNew.indexOf("hookBlindVisual") != -1) {
					this.isHook = true;
					if (this.$route.query.assetId) {
						this.num = 1;
						this.widthHeight = '100%';
						if (this.$route.query.isHook) {
							this.isHook = this.$route.query.isHook
						}
						this.initLoad();
					}
				} else if (pathNew.indexOf("videoMonitorCheck") != -1) {
					this.isHook = false;
				}
			},
			immediate: true
		}
	},
	mounted () {
		let that = this;
		window.addEventListener('message', function (e) {
			if (e.data.type === 'handleError') {
				let domNodeId = e.data.id;

				let index = domNodeId.substring(domNodeId.length - 1, domNodeId.length)
				if (that.replayCount[domNodeId] != null && that.replayCount[domNodeId] != undefined) {
					that.replayCount[domNodeId] = that.replayCount[domNodeId] + 1;
					if (that.replayCount[domNodeId] > 5) {
						return;
					}
				} else {
					that.replayCount[domNodeId] = 1;
				}
				console.log(domNodeId + "播放失败，第 " + that.replayCount[domNodeId] + " 次重试")
				that.player[index].play().then(e => {
					console.log("重播成功")
				});
			}
		});
		// getVideoToken().then(res => {
		// 	this.videoToken = res.data.accessToken
		// }).catch(() => {

		// })

		// this.getStream("http://hls01open.ys7.com/openlive/046c0e07cdb24ac085d00b734ec7ec6d.m3u8");
	},
	created () {
		request({
			url: 'build/projectInfo/queryProjectList',
			method: 'post',
			data: {},
			headers: {
				'Content-Type': 'application/json;charset=UTF-8'
			}
		}).then(response => {
			this.projectOptions = response.data
		})

	},
	methods: {
			submitForm () {
			this.$refs['form'].validate(valid => {
				if (valid) {
					request({
						url: 'build/jgEnforceRecord/save',
						method: 'post',
						data: this.form,
						headers: {
							'Content-Type': 'application/json;charset=UTF-8'
						}
					}).then(response => {
						if (response.code === 0) {
							if (this.form.id != undefined) {
								this.msgSuccess('整改成功')
								this.open = false
								this.rectDislable = false;
							} else {
								this.msgSuccess('新增成功')
								this.addOpen = false
							}
						}
					})
				}
			})
		},

		// 表单重置
		reset () {
			this.form = {}
			this.resetForm('form')
		},
		addCancel () {
			this.addOpen = false
			this.reset()
		},
		videoEnforce () {
			this.addOpen = true;
			this.title = '视频监控执法新建';
			this.reset()
			this.batchId = guid();
			this.form.findDate = new Date()
		},
		// 视频组件重置
		async videoPlayerInit () {
			let that = this;
			for (let i = this.player.length - 1; i >= 0; i--) {
				// await this.videoStop(i)
				that.player[i].stop().then(() => {
					that.player[i].destroy().then(() => {
						that.player.pop()
					})
				}).catch(() => {
				})
			}
			that.record = []//清空视频设备信息
			that.selected = false;//取消已选中的内容
			that.kzName = '';//重置已选中的设备名称
			that.initLoading = false;
		},
		control (type) {
			if (this.record[this.clickIndex]) {
				request({
					url: 'build/ysy/ptzControl',
					method: 'post',
					data: {
						deviceId: this.record[this.clickIndex].deviceChannelId,
						channelId: this.record[this.clickIndex].deviceChannelId,
						type: type,
						speed: '150'
					},
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(res => {

				})

			} else {
				if (type == 'stop') {
					this.$message({
						message: '请选择播放中的视频画面',
						type: 'warning'
					});
				}
			}
		},
		videoPause () {
			if (this.hls) {
				this.$refs.video.pause();
				this.hls.destroy();
				this.hls = null;
				this.src = '';
			}
		},
		clickVideo (index) {
			this.hiddenIndex = index
			if (this.clickIndex == index) {
				this.clickIndex = null
				this.selected = false
			} else {
				this.clickIndex = index
				this.selected = true;
				this.kzName = this.record != undefined && this.record[this.clickIndex] != undefined && this.record[this.clickIndex].assetName != undefined ? this.record[this.clickIndex].assetName : "";
			}
		},
		videoStop (i) {
			let that = this
			return new Promise((resolve) => {
				that.player[i].stop().then(() => {
					that.player[i].destroy().then(() => {
						that.player.pop()
						resolve()
					})
				}).catch(() => { })
			});
		},
		videoStop2 () {
			let that = this
			let index = 0
			let all = this.player.length
			return new Promise((resolve) => {
				for (let i = this.player.length - 1; i >= 0; i--) {
					that.player[i].stop().then(() => {
						that.player[i].destroy().then(() => {
							setTimeout(function () {
								index++
							}, i * 100)
						})
					}).catch(() => {
					})
				}
				countIndex()
				function countIndex () {
					setTimeout(function () {
						if (index < all) {
							countIndex()
						} else {
							that.player = []
							resolve()
						}
					}, 1000);
				}
			});
		},
		async changeVideo (index) {
			this.activeNum = index
			if (index == this.num) {
				return
			}
			this.replayCount = {};
			this.fullscreenLoading = true;
			if (index == 1) {
				this.widthHeight = '100%'
				if (this.player.length > 0) {
					let videoArr = []
					for (let i = 0; i < this.player.length; i++) {
						videoArr.push(this.player[i].opt.url)
					}
					//					for (let i = this.player.length - 1; i >= 0; i--) {
					//						await this.videoStop(i)
					//					}
					await this.videoStop2()
					this.num = index
					let arrLength
					if (videoArr.length > index) {
						arrLength = index
					} else {
						arrLength = videoArr.length
					}
					for (let i = 0; i < arrLength; i++) {
						getVideoToken().then(res => {

							this.player.push(
								new EZUIKit.EZUIKitPlayer({
									id: 'myPlayer' + i, // 视频容器ID
									accessToken: res.data.accessToken, //授权过程获取的access_token
									url: videoArr[i],
									// audio: 1,//是否默认开启声音 1：打开（默认） 0：关闭
									template: "security", // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
									header: ["capturePicture", "save"], //视频头部可选UI组件，可选值：capturePicture：截图,save：录像保存,zoom：电子放大
									footer: ["broadcast", "fullScreen"], //视频底部部可选UI组件，可选值：talk：对讲,broadcast：语音播报,hd：高清标清切换,fullScreen：全屏
								})
							)
						}).catch(() => {

						})

					}
					this.fullscreenLoading = false
				} else {
					this.num = index
					this.fullscreenLoading = false
				}
			} else if (index == 4) {
				this.widthHeight = '50%'
				if (this.player.length > 0) {
					let videoArr = []
					for (let i = 0; i < this.player.length; i++) {
						videoArr.push(this.player[i].opt.url)
					}
					//					for (let i = this.player.length - 1; i >= 0; i--) {
					//						await this.videoStop(i)
					//					}
					await this.videoStop2()
					this.num = index
					let arrLength
					if (videoArr.length > index) {
						arrLength = index
					} else {
						arrLength = videoArr.length
					}
					for (let i = 0; i < arrLength; i++) {
						getVideoToken().then(res => {
							this.player.push(
								new EZUIKit.EZUIKitPlayer({
									id: 'myPlayer' + i, // 视频容器ID
									accessToken: res.data.accessToken, //授权过程获取的access_token
									url: videoArr[i],
									// audio: 1,//是否默认开启声音 1：打开（默认） 0：关闭
									template: "security", // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
									header: ["capturePicture", "save"], //视频头部可选UI组件，可选值：capturePicture：截图,save：录像保存,zoom：电子放大
									footer: ["broadcast", "fullScreen"], //视频底部部可选UI组件，可选值：talk：对讲,broadcast：语音播报,hd：高清标清切换,fullScreen：全屏
								})
							)
						}).catch(() => {

						})

					}
					this.fullscreenLoading = false
				} else {
					this.num = index
					this.fullscreenLoading = false
				}

			} else if (index == 9) {
				this.widthHeight = '33.33%'
				if (this.player.length > 0) {
					let videoArr = []
					for (let i = 0; i < this.player.length; i++) {
						videoArr.push(this.player[i].opt.url)
					}
					//					for (let i = this.player.length - 1; i >= 0; i--) {
					//						await this.videoStop(i)
					//					}
					await this.videoStop2()
					this.num = index
					let arrLength
					if (videoArr.length > index) {
						arrLength = index
					} else {
						arrLength = videoArr.length
					}
					for (let i = 0; i < arrLength; i++) {
						getVideoToken().then(res => {
							this.player.push(
								new EZUIKit.EZUIKitPlayer({
									id: 'myPlayer' + i, // 视频容器ID
									accessToken: res.data.accessToken, //授权过程获取的access_token
									url: videoArr[i],
									// audio: 1,//是否默认开启声音 1：打开（默认） 0：关闭
									template: "security", // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
									header: ["capturePicture", "save"], //视频头部可选UI组件，可选值：capturePicture：截图,save：录像保存,zoom：电子放大
									footer: ["broadcast", "fullScreen"], //视频底部部可选UI组件，可选值：talk：对讲,broadcast：语音播报,hd：高清标清切换,fullScreen：全屏
								})
							)
						}).catch(() => {

						})

					}
					this.fullscreenLoading = false
				} else {
					this.num = index
					this.fullscreenLoading = false
				}
			}
		},
		//当请求新数据时，两行代码就可以了。
		requestNewData () {
			this.node_had.childNodes = [];//把存起来的node的子节点清空，不然会界面会出现重复树！
			this.loadNode(this.node_had, this.resolve_had);//再次执行懒加载的方法
		},
		loadNode (node, resolve) {
			if (node.level === 0) {
				this.node_had = node;//这里是关键！在data里面定义一个变量，将node.level == 0的node存起来
				this.resolve_had = resolve;//同上，把node.level == 0的resolve也存起来
				// 查询项目列表
				if (this.isHook) {
					let obj1 = { 'hookCheckBox': '1' }
					queryTreeListByParentId(0, obj1)
						.then(res => {
							res.data.forEach(element => {
								if (element.leaf == 1) {
									element.leaf = true;
								}
							});
							resolve(res.data)
						})
						.catch(() => {
							this.loading = false
						})
				} else {
					queryTreeListByParentId(0)
						.then(res => {
							res.data.forEach(element => {
								if (element.leaf == 1) {
									element.leaf = true;
								}
							});
							resolve(res.data)
						})
						.catch(() => {
							this.loading = false
						})
				}

			} else if (node.level >0) {
				queryTreeListByParentId(node.data.id)
					.then(res => {
						if (res.data.length == 0) {
							let obj = {};
							obj.locationId = node.data.id;
							obj.pageType = 0;
							if (this.isHook) {
								obj.hookCheckBox = '1';
							}
							queryEquiBytypeId(obj).then(res => {
								res.data.list.forEach(element => {
									element.label = element.assetName;
									element.leaf = true;
									element.level = 1
								});
								resolve(res.data.list)
								this.nodes.push(res.data.list)
							})
						} else {
							res.data.forEach(element => {
								if (element.leaf == 1) {
									element.leaf = true;
								}
							});
							resolve(res.data)
						}
					})
					.catch(() => {
						this.loading = false
					})
			}
		},
		async nodeClick (node) {
			let deviceAndAssets = '';
			let that = this
			this.loading = true;
			if (node.leaf && node.level == 1) {
				let that = this;

				if(node.assetId==that.assetId){
					return false
				}else{
					//查询设备资产与流媒体设备通道关系表，查询设备id和通道id
				that.assetId=node.assetId
				setTimeout(function(){
					that.assetId=''
				},1000)
				request({
					url: 'build/equipmentAssets/getDeviceId/' + node.assetId,
					method: 'post',
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(res => {
					if (res.data != null) {
						deviceAndAssets = res.data
						//开始点播
						let paramJSON = {
							gbchannel: res.data.deviceChannelId,
							deviceSerial: res.data.deviceSerial, //直播源，均采用英文符号，限制50个
							protocol: 1, //流播放协议，1-ezopen、2-hls、3-rtmp、4-flv，默认为1
							quality: 2, //视频清晰度，1-高清（主码流）、2-流畅（子码流）
							type: "1", //1-预览，2-本地录像回放，3-云存储录像回放，非必选，默认为1
						};
						// request({
						// 	url: 'build/ysy/playback',
						// 	method: 'post',
						// 	data: paramJSON,
						// 	headers: {
						// 		'Content-Type': 'application/json;charset=UTF-8'
						// 	}
						// }).then(res => {
						if (res.code == 0) {
							if (this.player.length < this.num) {
								getVideoToken().then(res => {
									this.player.push(
										new EZUIKit.EZUIKitPlayer({
											id: 'myPlayer' + this.player.length, // 视频容器ID
											accessToken: res.data.accessToken, //授权过程获取的access_token
											url: deviceAndAssets.url,
											// audio: 1,//是否默认开启声音 1：打开（默认） 0：关闭
											template: "security", // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
											header: ["capturePicture", "save"], //视频头部可选UI组件，可选值：capturePicture：截图,save：录像保存,zoom：电子放大
											footer: ["broadcast", "fullScreen"], //视频底部部可选UI组件，可选值：talk：对讲,broadcast：语音播报,hd：高清标清切换,fullScreen：全屏
										})
									)
								}).catch(() => {

								})

								node.deviceChannelId = deviceAndAssets.deviceChannelId
								this.record.push(node)
								this.kzName = this.record[this.clickIndex].assetName
							} else {
								if (this.selected) {
									this.player[this.clickIndex].stop().then(function () {
										that.player[that.clickIndex].play({
											url: res.data.url
										})
									})
									this.$set(this.record, that.clickIndex, node)
									this.kzName = this.record[this.clickIndex].assetName
								} else {
									this.player[this.videoIndex].stop().then(function () {
										that.player[that.videoIndex].play({
											url: res.data.url
										})
										if (that.videoIndex < that.num - 1) {
											that.videoIndex += 1
										} else {
											that.videoIndex = 0
										}

									})
									this.$set(this.record, that.videoIndex, node)
									this.kzName = this.record[this.videoIndex].assetName
								}
							}

						} else {
							that.$message({
								type: 'error',
								message: res.msg
							})
						}
						// resolve(res.data)
						// }).catch(err => {

						// })
					} else {
						that.$message({
							type: 'error',
							message: '当前设备不存在'
						})
					}

				}).catch(() => {
					this.loading = false
				})
				}

			} else {
				if (node.leaf) {
					this.$message({
						type: 'warning',
						message: '当前项目下无视频设备'
					})
				}

			}

		},
		initLoad () {
			var assetId = this.$route.query.assetId;
			let assetName = this.$route.query.name
			var node = {};
			this.nodes.forEach(function (item) {
				if (item.assetId == assetId) {
					node = item
				}
			})
			if (assetId) {
				let deviceAndAssets = '';
				this.loading = true;
				let that = this;

				//查询设备资产与流媒体设备通道关系表，查询设备id和通道id
				request({
					url: 'build/equipmentAssets/getDeviceId/' + assetId,
					method: 'post',
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(res => {
					if (res.data != null) {
						deviceAndAssets = res.data
						if (res.code == 0) {
							if (this.player.length < this.num) {
								node.deviceChannelId = res.data.deviceChannelId
								node.assetName = assetName;
								this.record.push(node)
								this.kzName = this.record[this.clickIndex].assetName

								getVideoToken().then(res => {
									this.player.push(
										new EZUIKit.EZUIKitPlayer({
											id: 'myPlayer' + this.player.length, // 视频容器ID
											accessToken: res.data.accessToken, //授权过程获取的access_token
											url: deviceAndAssets.url,
											// audio: 1,//是否默认开启声音 1：打开（默认） 0：关闭
											template: "security", // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
											header: ["capturePicture", "save"], //视频头部可选UI组件，可选值：capturePicture：截图,save：录像保存,zoom：电子放大
											footer: ["broadcast", "fullScreen"], //视频底部部可选UI组件，可选值：talk：对讲,broadcast：语音播报,hd：高清标清切换,fullScreen：全屏
										})
									)
								}).catch(() => {

								})

							} else {
								if (this.selected) {
									this.player[this.clickIndex].stop().then(function () {
										that.player[that.clickIndex].play({
											url: res.data.url
										})
									})
									node.assetName = assetName;
									node.deviceChannelId = res.data.deviceChannelId
									this.$set(this.record, that.videoIndex, node)
									this.kzName = this.record[this.clickIndex].assetName
								} else {
									this.player[this.videoIndex].stop().then(function () {
										that.player[that.videoIndex].play({
											url: res.data.url
										})
										if (that.videoIndex < that.num - 1) {
											that.videoIndex += 1
										} else {
											that.videoIndex = 0
										}

									})
									node.assetName = assetName;
									node.deviceChannelId = res.data.deviceChannelId
									this.$set(this.record, that.videoIndex, node)
									this.kzName = this.record[this.videoIndex].assetName
								}
							}

						} else {
							that.$message({
								type: 'error',
								message: res.msg
							})
						}
					} else {
						that.$message({
							type: 'error',
							message: '当前设备不存在'
						})
					}

				}).catch(err => {
					console.log(err)
					this.loading = false
				})
			}
		}
	},
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.system-customer {
  height: 100%;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  padding: 0 15px;
}

.customer-content {
  flex: 1;
  position: relative;
  display: flex;
  overflow: hidden;
  height: 100%;
}

.system-view-nav {
  min-width: 300px;
  background: #fff;
  margin-right: 10px;
  padding-top: 20px;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
}

.system-view-content {
  flex: 1;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
  background: #fff;
  display: flex;
  flex-direction: column;
  overflow-x: hidden;
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
  content: " ";
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  width: 2px;
  background-color: #5383ed;
}

.screen {
  display: flex;
  height: 60px;
  align-items: center;
  justify-content: center;
}

.screen div {
  cursor: pointer;
  margin: 0 30px;
  height: 30px;
  width: 80px;
  line-height: 30px;
  border-radius: 10px;
  border: 2px solid rgb(239, 239, 239);
  text-align: center;
}

.screen .activevideo {
  background: rgb(64, 158, 255);
  border: 2px solid rgb(64, 158, 255);
  color: #ffffff;
}

.video_con {
  height: calc(100% - 0px);
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
}

.video_box {
  position: relative;
  border: 1px solid #cccccc;
}

.video_box > div {
  overflow: hidden !important;
  background: #000000;
}

.mask {
  display: block;
  height: calc(100% - 96px);
  width: 100%;
  position: absolute;
  top: 48px;
  z-index: 9;
  cursor: pointer;
}

.clickActive {
  border: 1px solid red;
}

.sxzy_box {
  height: 170px;
  width: 170px;
  background: url(../../../../assets/lrtb_bg.png) no-repeat;
  background-size: 100% 100%;
  margin-top: 10px;
  position: relative;
  float: left;
}

.sxzy_box img {
  position: absolute;
  cursor: pointer;
}

.video_left {
  top: 74px;
  left: 9px;
}

.video_right {
  top: 74px;
  right: 9px;
}

.video_center {
  top: 74px;
  left: 76px;
}

.video_top {
  top: 9px;
  left: 74px;
}

.video_bottom {
  bottom: 9px;
  left: 74px;
}

.max_min {
  display: flex;
  justify-content: center;
  clear: both;
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

.lately_box {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  height: 300px;
  margin-right: 20px;
}

.box_center {
  display: flex;
  align-items: center;
}

.split_screen {
  width: 30px;
  position: absolute;
  right: 5px;
  top: 50px;
  img {
    width: 26px;
    height: 26px;
    margin-bottom: 10px;
  }
}

.slider_box {
  width: 150px;
  margin: 0 auto;
  clear: both;
}

.tree_box {
  height: calc(100% - 300px);
  overflow-y: scroll;
}
</style>
