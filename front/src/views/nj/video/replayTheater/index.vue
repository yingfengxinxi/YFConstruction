<template>
    <div class="employee-dep-management" style="margin: 10px;background-color: white; height: 96.5%">

        <div class="customer-content">
            <div class="system-view-nav">
                <el-tree :props="props" :load="loadNode" lazy @node-click="nodeClick" />
            </div>
            <div class="son-flex">
                <div class="form-param">
                    <el-form v-show="true" ref="queryForm" :model="queryParams" :inline="true" label-width="80px" style="background-color: white; padding: 1% 1%;">

                        <el-form-item label="回放时间">
                            <el-date-picker v-model="executeDates" type="datetimerange" @change="aaa" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss" :default-time="['00:00:00', '23:59:59']">
                            </el-date-picker>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" icon="el-icon-search" size="mini" @click="queryClick(nodeData)">搜索</el-button>
                            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
                        </el-form-item>
                        <el-form-item>
                            <!-- <el-button type="primary" icon="el-icon-download" size="mini" @click="downLoadClick(nodeData)">下载</el-button> -->

                        </el-form-item>
                    </el-form>
                </div>

                <keep-alive>
                    <div class="system-view-content">

                        <!--          <video-->
                        <!--            ref="videoPlayer" :src="src"  style="width: 100%; height: 100%; object-fit: fill" />-->
                        <!-- <video ref="video" muted style="width: 100%; height: 100%; object-fit: fill"></video> -->
                        <div id="video-container"></div>
                    </div>
                </keep-alive>
            </div>
        </div>
    </div>
</template>

<script>
import XrHeader from '@/components/XrHeader'
import {
    queryByParentId, queryTreeListByParentId, queryEquiBytypeId, queryVideoMonitor
} from '@/api/build/videoMonitor'
import request from '@/utils/request'
import Qs from 'qs'
// import EZUIKit from 'ezuikit-js';
import EZUIKit from './EZUIKitPlayer.js';
var player = null;//ezopen播放器实例化
let Hls = require('hls.js');
export default {
    name: '',
    components: {
        XrHeader
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

            // 执行时间范围
            executeDates:[this.formatterDate(new Date(new Date().getTime()-24*60*60*1000)),this.formatterDate(new Date())],            
            queryParams: {},
            //临时记录node节点数据
            nodeData: '',
        }
    },
    created(){
//  	this.executeDates.push(this.formatterDate(new Date(new Date().getTime()-24*60*60*1000)))
//  	this.executeDates.push(this.formatterDate(new Date()))
//	console.log(this.executeDates)
    },
    mounted () {
        // console.log(this.$refs.videoPlayer)
        // this.getStream("http://hls01open.ys7.com/openlive/046c0e07cdb24ac085d00b734ec7ec6d.m3u8");


    },
    watch: {
        //监听页面切换
        '$route' (to, from) {
            //TODO 重新加载数据
            this.queryParams = {}
            this.executeDates = ''
            console.log(this.$route)
        }
    },
    methods: {
    	aaa(e){
    		console.log(this.executeDates)
    		console.log(e)
    	},
    	// 格式化时间字符串
		    formatterDate (date) {
		    	let time=new Date(date)
		      if (date == null || date == "") return "";
		      let Y = time.getFullYear() + "-";
		      let M = time.getMonth() + 1 < 10 ? "0" + (time.getMonth() + 1) + "-" : time.getMonth() + 1 + "-";
		      let D = time.getDate() < 10 ? "0" + time.getDate() + " " : time.getDate() + " ";
		      let h = time.getHours() < 10 ? "0" + time.getHours() + ":" : time.getHours() + ":";
		      let m = time.getMinutes() < 10 ? "0" + time.getMinutes() + ":" : time.getMinutes() + ":";
		      let s = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
		      let all = (Y + M + D+h+m+s)
		      return all;
		    },
        /** 重置按钮操作 */
        resetQuery () {
            this.queryParams = {}
            this.executeDates = [new Date(new Date() - 24 * 60 * 60 * 1000), new Date(new Date() - 0)]
        },
        videoPause () {
            if (this.hls) {
                this.$refs.video.pause();
                this.hls.destroy();
                this.hls = null;
                this.src = '';
            }
        },
        getStream (source) {
            if (Hls.isSupported()) {
                this.hls = new Hls();
                this.hls.loadSource(source);
                this.hls.attachMedia(this.$refs.video);
                this.hls.on(Hls.Events.MANIFEST_PARSED, () => {
                    this.loading = false
                    console.log('加载成功');
                    this.$refs.video.play();
                });
                this.hls.on(Hls.Events.ERROR, (event, data) => {
                    this.loading = false
                    console.log(event, data);
                    // 监听出错事件
                    console.log('加载失败');
                    this.videoPause();
                });
            }
        },
        beforeDestroy () {
            this.videoPause();
        },
        loadNode (node, resolve) {
            if (node.level === 0) {
                // 查询项目列表
                request({
                    url: 'build/projectInfo/queryProjectTreeList',
                    method: 'post',
                    data: {},
                    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
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
                }).catch(err => {
                })
            } else if (node.level === 1) {
                //查询项目资产列表
                request({
                    url: 'build/equipmentAssets/groupList',
                    method: 'post',
                    data: { 'pageType': '0', 'projectId': node.data.id, 'typeId': '30' },
                    headers: {
                        'Content-Type': 'application/json;charset=UTF-8'
                    }
                }).then(res => {
                    res.data.list.forEach(element => {
                        element.name = element.assetName
                        element.id = element.assetId
                        element.leaf = true
                        element.level = 1
                    });
                    resolve(res.data.list)
                }).catch(err => {
                })
            }
        },
        async  nodeClick (node) {
            this.loading = true;
            // 执行时间范围
            if (this.executeDates != null && this.executeDates != '') {
                this.queryParams.startTime = this.executeDates[0]
                this.queryParams.endTime = this.executeDates[1]
            } else {
                this.queryParams.startTime = ''
                this.queryParams.endTime = ''
            }
            this.nodeData = node;
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
                    if (res.data != null) {
                        //开始点播
                        console.log(process.env.YSY_VIDEO_URL)
                        console.log(this.executeDates)
                        let paramJSON = {
                             startTime: this.queryParams.startTime,//录像回放开始时间
                             stopTime: this.queryParams.endTime,//录像回放结束时间
                            //startTime: '2021-12-05 00:00:00', stopTime: '2021-12-05 23:59:59',
                            gbchannel: res.data.deviceChannelId,
                            deviceSerial: res.data.deviceSerial,
                            protocol: 1,
                            quality: 2,
                            type: 2,
                        };
                        request({
                            url: 'build/ysy/playback',
                            method: 'post',
                            data: paramJSON,
                            headers: { 'Content-Type': 'application/json;charset=UTF-8' }
                        }).then(res => {
                            console.log(res);
                            if (res.code == 0) {
                                // this.videoPause();
                                // this.getStream(res.data.url);
                                console.log("判断player是否为空", player != null)
                                if (player != null) {
                                    player.stop()
                                        .then(() => {
                                            console.log("停止")
                                            player.play({ url: res.data.url }).then(ee => {
                                                console.log(ee)
                                            }).catch(err => {
                                                console.log(err)
                                            });
                                        }).catch(err => {
                                            console.log(err)
                                        });
                                } else {
                                    player = new EZUIKit.EZUIKitPlayer({
                                        id: 'video-container', // 视频容器ID
                                        accessToken: 'at.4zfgo42v9a5zpjz48j2fgbvva0c6alt6-7aw0fjxaep-1vlsv8q-m15wzoxmw',//授权过程获取的access_token
                                        url: res.data.url,
                                        // audio: 1,//是否默认开启声音 1：打开（默认） 0：关闭
                                        width: 1500,//视频宽度，默认值为容器容器DOM宽度
                                        height: 550,//视频高度，默认值为容器容器DOM高度
                                        templete: "simple",
                                        header: ["capturePicture", "save", "zoom"],//视频头部可选UI组件，可选值：capturePicture：截图,save：录像保存,zoom：电子放大
                                        footer: ["talk", "broadcast", "hd", "fullScreen"],//视频底部部可选UI组件，可选值：talk：对讲,broadcast：语音播报,hd：高清标清切换,fullScreen：全屏
                                    })
                                }



                            } else {
                                that.$message({
                                    type: 'error',
                                    message: res.msg
                                })
                            }
                            // resolve(res.data)
                        }).catch(err => {
                            console.log(err)
                        })
                    } else {
                        that.$message({
                            type: 'error',
                            message: '当前设备不存在'
                        })
                    }

                }).catch(() => {
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

        async  queryClick (node) {
        	console.log(this.executeDates)
            if (node != '') {
                this.loading = true;
                // 执行时间范围
                if (this.executeDates != null && this.executeDates != '') {
                    this.queryParams.startTime = this.executeDates[0]
                    this.queryParams.endTime = this.executeDates[1]
                } else {
                    this.queryParams.startTime = ''
                    this.queryParams.endTime = ''
                }

                console.log(node);
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
                        if (res.data != null) {
                            //开始点播
                            console.log(process.env.YSY_VIDEO_URL)
                            console.log(this.executeDates)
                            let paramJSON = {
                                startTime: this.queryParams.startTime,//录像回放开始时间
                                stopTime: this.queryParams.endTime,//录像回放结束时间
                                // startTime: '2021-12-05 00:00:00', stopTime: '2021-12-05 23:59:59',
                                gbchannel: res.data.deviceChannelId,
                            };
                            request({
                                url: 'build/ysy/playback',
                                method: 'post',
                                data: paramJSON,
                                headers: { 'Content-Type': 'application/json;charset=UTF-8' }
                            }).then(res => {
                                console.log(res);
                                if (res.code == 0) {
                                    // this.videoPause();
                                    // this.getStream(res.data.url);
                                    console.log("判断player是否为空", player != null)
                                    if (player != null) {
                                        player.stop()
                                            .then(() => {
                                                console.log("停止")
                                                player.play({ url: res.data.url }).then(ee => {
                                                    console.log(ee)
                                                }).catch(err => {
                                                    console.log(err)
                                                });
                                            }).catch(err => {
                                                console.log(err)
                                            });
                                    } else {
                                        player = new EZUIKit.EZUIKitPlayer({
                                            id: 'video-container', // 视频容器ID
                                            accessToken: 'at.4zfgo42v9a5zpjz48j2fgbvva0c6alt6-7aw0fjxaep-1vlsv8q-m15wzoxmw',//授权过程获取的access_token
                                            url: res.data.url,
                                            // audio: 1,//是否默认开启声音 1：打开（默认） 0：关闭
                                            width: 1500,//视频宽度，默认值为容器容器DOM宽度
                                            height: 550,//视频高度，默认值为容器容器DOM高度
                                            templete: "simple",
                                            header: ["capturePicture", "save", "zoom"],//视频头部可选UI组件，可选值：capturePicture：截图,save：录像保存,zoom：电子放大
                                            footer: ["talk", "broadcast", "hd", "fullScreen"],//视频底部部可选UI组件，可选值：talk：对讲,broadcast：语音播报,hd：高清标清切换,fullScreen：全屏
                                        })
                                    }

                                } else {
                                    that.$message({
                                        type: 'error',
                                        message: res.msg
                                    })
                                }
                                // resolve(res.data)
                            }).catch(err => {
                                console.log(err)
                            })
                        } else {
                            that.$message({
                                type: 'error',
                                message: '当前设备不存在'
                            })
                        }

                    }).catch(() => {
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
            } else {
                this.$message({
                    type: 'error',
                    message: '请选择末级节点'
                })
            }

        },
        downLoadClick (node) {
            if (node != '') {
                this.loading = true;
                // 执行时间范围
                if (this.executeDates != null && this.executeDates != '') {
                    this.queryParams.startTime = this.executeDates[0]
                    this.queryParams.endTime = this.executeDates[1]
                } else {
                    this.queryParams.startTime = ''
                    this.queryParams.endTime = ''
                }

                console.log(node);
                if (node.leaf) {
                    let that = this;
                    console.log(node)
                    //查询设备资产与流媒体设备通道关系表，查询设备id和通道id
                    request({
                        url: 'build/equipmentAssets/getDeviceId/' + node.assetId,
                        method: 'post',
                        headers: {
                            'Content-Type': 'application/json;charset=UTF-8'
                        }
                    }).then(res => {
                        this.$axios({
                            method: 'get',
                            url: VIDEOURL + "/api/download/start/" + res.data.deviceId + "/" + res.data.deviceChannelId,
                            params: { "deviceId": res.data.deviceId, "channelId": res.data.deviceChannelId, "startTime": this.queryParams.startTime, "endTime": this.queryParams.endTime, "downloadSpeed": "1" }
                        }).then(function (res) {
                            console.log(res);
                            that.$message({
                                type: 'error',
                                message: res.data
                            })

                        }).catch(function (error) {
                            console.log(error)
                        });
                    }).catch(error => {
                        console.log(error)
                    });

                }

            } else {
                this.$message({
                    type: 'error',
                    message: '请选择末级节点'
                })
            }
        }
    },
    beforeDestroy () {
        console.log("离开")
        player.stop().then(ee => {
            console.log(ee)
        }).catch(err => {
            console.log(err)
        });;
    },

}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.son-flex {
  width: 90%;
}
.son-flex .form-param {
  background-color: white;
  height: 11%;
  width: 100%;
  border: 1px solid rgb(230, 230, 230);
  border-radius: 4px;
  margin-bottom: 10px;
}
/deep/ .el-form-item {
  margin-bottom: 0px;
}
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
  min-width: 200px;
  background: #fff;
  margin-right: 10px;
  padding-top: 20px;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
  overflow-y: scroll;
}

.system-view-content {
  flex: 1;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
  background: #fff;
  display: flex;
  flex-direction: column;
  overflow-x: auto;
  height: 87%;
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
</style>
