<template>
	<div class="employee-dep-management" style="margin: 10px;background-color: white; height: 96.5%">
		<!--    <xr-header-->
		<!--      icon-class="wk wk-manage"-->
		<!--      icon-color="#2EC2F9"-->
		<!--      label="视频查看"/>-->
		<div class="customer-content">
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
						<el-tooltip class="item" effect="dark" content="视频执法" placement="top">											
							<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    @click="videoEnforce()"  src="../../../../assets/video_split.png" />
						</el-tooltip>
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
				<div class="video_con" v-loading="fullscreenLoading" element-loading-text="切换中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 1)">
					<div class="video_box" :style="{width:widthHeight,height:widthHeight}" :class="clickIndex==index?'clickActive':''" v-for="(item,index) in num" :key="index">
						<span class="mask" @click="clickVideo(index)" v-show="hiddenIndex!=index"></span>
						<video :id="'myPlayer'+index" style="width: 100%;height: 100%;object-fit: fill;" muted controls ></video>
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
let Hls = require('hls.js');
import { guid } from '@/utils'
import WkUploadFile from "@/components/CreateCom/WkUploadFile";
export default {
	name: 'videoView',
	components: {
		XrHeader,
		WkUploadFile
	},
	data () {
		return {
			props: {
				label: 'name',
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
			videoToken: '',
			selected: false,
			clickIndex: 0,
			kzName: '',
			record: [],
			arrList: [],//储存搜索
			fullscreenLoading: false,
			hiddenIndex: 0,
			sliderVidewo: 30,
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
		}
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
	mounted () {
		getVideoToken().then(res => {
			this.videoToken = res.data.accessToken
		}).catch(() => {

		})
		// this.getStream("http://hls01open.ys7.com/openlive/046c0e07cdb24ac085d00b734ec7ec6d.m3u8");
	},
	// watch: {
	// 	clickIndex (newName, oldName) {
	// 		console.log("clickIndex----old值", oldName, "clickIndex----new值", newName)
	// 	},
	// 	videoIndex (newName, oldName) {
	// 		console.log("videoIndex----old值", oldName, "videoIndex----new值", newName)
	// 	}
	// },
	methods: {
		videoEnforce () {
			this.addOpen = true;
			this.title = '视频监控执法新建';
			this.reset()
			this.batchId = guid();
			this.form.findDate = new Date()
		},
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
		control (type) {
			if(this.record[this.clickIndex]){
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
					console.log(res)
				})
				
			}else{
				if(type=='stop'){
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
				}).catch(() => {
				})
			});
		},

		async changeVideo (index) {
			this.activeNum = index
			if (index == this.num) {
				return
			}

			this.fullscreenLoading = true;
			if (index == 1) {
				this.widthHeight = '100%'
				if (this.player.length > 0) {
					let videoArr = []
					for (let i = 0; i < this.player.length; i++) {
						// videoArr.push(this.player[i].opt.url)
						videoArr.push(this.player[i].url)
					}
					for (let i = this.player.length - 1; i >= 0; i--) {
						// await this.videoStop(i)
						await this.videoHlsDestroy(this.player[i], document.getElementById('myPlayer' + i));
						if (i >= index) {
							this.player[i] = null;
							this.record[i] = ''
						}
					}
					this.num = index
					let arrLength
					if (videoArr.length > index) {
						arrLength = index
					} else {
						arrLength = videoArr.length
					}
					for (let i = 0; i < arrLength; i++) {
						let video = document.getElementById('myPlayer' + i)
						this.hlsCreate(video, videoArr[i], i, true);
						// this.player.push(
						// 	new EZUIKit.EZUIKitPlayer({
						// 		id: 'myPlayer' + i, // 视频容器ID
						// 		accessToken: this.videoToken, //授权过程获取的access_token
						// 		url: videoArr[i],
						// 		// audio: 1,//是否默认开启声音 1：打开（默认） 0：关闭
						// 		template: "security", // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
						// 		header: ["capturePicture", "save"], //视频头部可选UI组件，可选值：capturePicture：截图,save：录像保存,zoom：电子放大
						// 		footer: ["broadcast", "fullScreen"], //视频底部部可选UI组件，可选值：talk：对讲,broadcast：语音播报,hd：高清标清切换,fullScreen：全屏
						// 	})
						// )
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
						// videoArr.push(this.player[i].opt.url)
						if (this.player[i] != null) {
							videoArr.push(this.player[i].url)
						}
					}
					for (let i = this.player.length - 1; i >= 0; i--) {
						// await this.videoStop(i)
						await this.videoHlsDestroy(this.player[i], document.getElementById('myPlayer' + i));
						if (i >= index) {
							this.player[i] = null;
							this.record[i] = ''
						}
					}
					this.num = index
					let arrLength
					if (videoArr.length > index) {
						arrLength = index
					} else {
						arrLength = videoArr.length
					}
					for (let i = 0; i < arrLength; i++) {
						let video = document.getElementById('myPlayer' + i)
						this.hlsCreate(video, videoArr[i], i, true);
						// this.player.push(
						// 	new EZUIKit.EZUIKitPlayer({
						// 		id: 'myPlayer' + i, // 视频容器ID
						// 		accessToken: this.videoToken, //授权过程获取的access_token
						// 		url: videoArr[i],
						// 		// audio: 1,//是否默认开启声音 1：打开（默认） 0：关闭
						// 		template: "security", // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
						// 		header: ["capturePicture", "save"], //视频头部可选UI组件，可选值：capturePicture：截图,save：录像保存,zoom：电子放大
						// 		footer: ["broadcast", "fullScreen"], //视频底部部可选UI组件，可选值：talk：对讲,broadcast：语音播报,hd：高清标清切换,fullScreen：全屏
						// 	})
						// )
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
						// videoArr.push(this.player[i].opt.url)
						videoArr.push(this.player[i].url)
					}
					for (let i = this.player.length - 1; i >= 0; i--) {
						// await this.videoStop(i)
						await this.videoHlsDestroy(this.player[i], document.getElementById('myPlayer' + i));
					}
					this.num = index
					let arrLength
					if (videoArr.length > index) {
						arrLength = index
					} else {
						arrLength = videoArr.length
					}
					for (let i = 0; i < arrLength; i++) {
						let video = document.getElementById('myPlayer' + i)
						this.hlsCreate(video, videoArr[i], i, true);
						// this.player.push(
						// 	new EZUIKit.EZUIKitPlayer({
						// 		id: 'myPlayer' + i, // 视频容器ID
						// 		accessToken: this.videoToken, //授权过程获取的access_token
						// 		url: videoArr[i],
						// 		// audio: 1,//是否默认开启声音 1：打开（默认） 0：关闭
						// 		template: "security", // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
						// 		header: ["capturePicture", "save"], //视频头部可选UI组件，可选值：capturePicture：截图,save：录像保存,zoom：电子放大
						// 		footer: ["broadcast", "fullScreen"], //视频底部部可选UI组件，可选值：talk：对讲,broadcast：语音播报,hd：高清标清切换,fullScreen：全屏
						// 	})
						// )
					}
					this.fullscreenLoading = false

				} else {
					this.num = index
					this.fullscreenLoading = false

				}
			}
		},

		loadNode (node, resolve) {
			if (node.level === 0) {
				// 查询项目列表
				request({
					url: 'build/projectInfo/queryProjectTreeList',
					method: 'post',
					data: {},
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(res => {
					res.data.forEach(element => {
						if (element.leaf == 1) {
							element.leaf = true;
						} else {
							element.leaf = false;
						}
						element.level = 0
					});
					resolve(res.data)
				}).catch(err => { })
				//查询设备列表
				// this.$axios({ method: 'get', url: process.env.VIDEO_URL +"/api/device/query/devices", params: { "page": "1", "count": "9999" } }).then(function (res) {     resolve(res.data.list) }).catch(function (error) {  });
			} else if (node.level === 1) {
				//查询项目资产列表
				request({
					url: 'build/equipmentAssets/groupList2',
					method: 'post',
					data: {
						'pageType': '0',
						'projectId': node.data.id,
						'typeId': '30'
					},
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(res => {
					console.log(res)
					res.data.forEach(element => {
						element.name = element.assetName
						element.id = element.assetId
						element.leaf = true
						element.level = 1
					});
					resolve(res.data)
				}).catch(err => { })
				//查询设备下级通道
				// this.$axios({ method: 'get', url: process.env.VIDEO_URL +"/api/device/query/devices/"+node.data.deviceId+"/channels", params: { "page": "1", "count": "9999", "deviceId": node.data.deviceId } }).then(function (res) {   if(res.data.list.length!=0){  res.data.list.forEach(element => {  element.leaf=true });  resolve(res.data.list) }  }).catch(function (error) {  });
			} else if (node.level === 2) {

			} else {

			}
		},
		async nodeClick (node) {
			let that = this
			this.loading = true;
			if (node.leaf && node.level == 1) {
				let that = this;

				//查询设备资产与流媒体设备通道关系表，查询设备id和通道id
				request({
					url: 'build/equipmentAssets/getDeviceId/' + node.assetId,
					method: 'post',
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(res => {
					console.log(res)
					if (res.code == 0) {
						if (res.data != null) {
							//开始点播
							console.log("请求成功")
							console.log("判断payer的数量是否小于num")
							if (this.player.length < this.num) {
								console.log("确定payer<num")
								// console.log("videoDom-----",this.$refs)
								// console.log("videoDom-----",this.$refs['myPlayer' + i])
								// console.log("videoDom-----",this.$refs['myPlayer' + i][0])
								this.$nextTick(function () {
									// this.videoHlsDestroy(that.player[this.clickIndex], video);
									let video = document.getElementById('myPlayer' + this.clickIndex)
									console.log("video-----", video)
									this.hlsCreate(video, res.data.wvpOpenUrl, this.clickIndex, false);
									this.record.push(node)
									this.kzName = this.record[this.clickIndex].assetName

								})

							} else {
								console.log("确定payer不小于num")
								console.log("判断是否选中")
								if (this.selected) {
									console.log("已选中")

									this.$nextTick(function () {
										let video = document.getElementById('myPlayer' + this.clickIndex)
										this.videoHlsDestroy(that.player[this.clickIndex], video);
										this.hlsCreate(video, res.data.wvpOpenUrl, this.clickIndex, true);

										this.$set(this.record, that.clickIndex, node)
										this.kzName = this.record[this.clickIndex].assetName
									})


								} else {
									console.log("未选中")
									let video = document.getElementById('myPlayer' + this.clickIndex)
									this.videoHlsDestroy(that.player[this.clickIndex], video);
									this.hlsCreate(video, res.data.wvpOpenUrl, this.clickIndex, true);
									if (that.videoIndex < that.num - 1) {
										that.videoIndex += 1
									} else {
										that.videoIndex = 0
									}
									this.$set(this.record, that.videoIndex, node)
									this.kzName = this.record[this.videoIndex].assetName
								}
							}

						} else {
							that.$message({
								type: 'error',
								message: '当前设备不存在'
							})
						}
					} else {
						that.$message({
							type: 'error',
							message: res.msg
						})
					}
				}).catch(err => {
					console.log(err)
					this.loading = false
				})
			} else {
				if (node.leaf) {
					this.$message({
						type: 'warning',
						message: '当前项目下无视频设备'
					})
				}

			}

		},
		//video暂停、hls销毁
		videoHlsDestroy (hls, video) {
			console.log("videoHlsDestroy打印hls", hls)
			console.log("videoHlsDestroy打印video", video)
			if (video != null && video != undefined) { video.pause(); }
			if (hls != null && hls != undefined) {
				hls.destroy();
				hls = null;

			}
		},
		/**
		 * hls对象创建
		 * videoDom video标签根据id获取的dom元素
		 * url 		视频播放的url
		 * index    hls创建的索引
		 * isReplace 是否是第一次创建，或点击名称替换视频
		 * 
		 */
		hlsCreate (videoDom, url, index, isReplace) {
			if (Hls.isSupported()) {
				if (url != null && url != undefined) {
					let hls = new Hls()
					hls.attachMedia(videoDom);
					hls.loadSource(url)
					hls.on(Hls.Events.MANIFEST_PARSED, function (event, data) {
						console.log("success回调打印", event, data)
						videoDom.play().then(function (event) {
							console.log(event)
						}).catch(function (error) {
							console.log(event)
						});

					});
					hls.on(Hls.Events.ERROR, function (event, data) {
						console.log("error回调打印==", event, data);

					});
					if (isReplace) {
						this.player.splice(index, 1, hls);
					} else {
						this.player.push(hls);
					}
				}
			}
		},
	}
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
  height: calc(100% - 1px);
  width: 100%;
  position: absolute;
//   top: 48px;
  z-index: 9;
  cursor: pointer;
}
.clickActive {
  border: 3px solid red;
}

.sxzy_box {
  height: 170px;
  width: 170px;
  background: url(../../../../assets/lrtb_bg.png) no-repeat;
  background-size: 100% 100%;
  margin-left: 10px;
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
  height: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
}
.box_center {
  display: flex;
  align-items: center;
}
.split_screen {
  width: 30px;
  position: absolute;
  right: 10px;
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