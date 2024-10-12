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
                            <el-date-picker v-model="executeDates" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss" :default-time="['00:00:00', '23:59:59']">
                            </el-date-picker>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" icon="el-icon-search" size="mini" @click="queryClick(nodeData)">搜索</el-button>
                            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" icon="el-icon-download" size="mini" @click="downLoadClick(nodeData)">下载</el-button>

                        </el-form-item>
                    </el-form>
                </div>

                <keep-alive>
                    <div class="system-view-content">

                        <!--          <video-->
                        <!--            ref="videoPlayer" :src="src"  style="width: 100%; height: 100%; object-fit: fill" />-->
                        <video ref="video" muted style="width: 100%; height: 100%; object-fit: fill"></video>
                    </div>
                </keep-alive>
            </div>
        </div>
    </div>
</template>

<script>
import XrHeader from '@/components/XrHeader'
import {
    queryByParentId, queryTreeListByParentId, queryEquiBytypeId, queryVideoMonitor, getDeviceId
} from '@/api/build/videoMonitor'
let Hls = require('hls.js');
export default {
    name: 'replayTheater',
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

            // 执行时间范围
            executeDates: [new Date(new Date() - 24 * 60 * 60 * 1000), new Date(new Date() - 0)],
            queryParams: {},
            //临时记录node节点数据
            nodeData: ''
        }
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
            } else {
                queryTreeListByParentId(node.data.id)
                    .then(res => {
                        if (res.data.length == 0) {
                            let obj = {};
                            obj.locationId = node.data.id;
                            obj.pageType = 0;
                            queryEquiBytypeId(obj).then(res => {
                                res.data.list.forEach(element => {
                                    element.label = element.assetName;
                                    element.leaf = true;
                                });
                                resolve(res.data.list)
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
            console.log(node);
            if (node.leaf) {
                let that = this;
                //查询录像 
                console.log(node)
                getDeviceId(node.assetId).then(res => {
                    if (res.data != null) {
                        this.$axios({
                            method: 'get',
                            url: process.env.VIDEO_URL + "/api/playback/start/" + res.data.deviceId + "/" + res.data.deviceChannelId + "",
                            params: { "deviceId": res.data.deviceId, "channelId": res.data.deviceChannelId, "startTime": this.queryParams.startTime, "endTime": this.queryParams.endTime }
                        }).then(function (res) {
                            console.log(res);
                            that.loading = false;
                            that.$message({
                                type: 'error',
                                message: res.data
                            })

                        }).catch(function (error) {
                            that.loading = false;
                            console.log(error)
                        });
                    } else {
                        that.$message({
                            type: 'error',
                            message: '当前位置暂无视频设备'
                        })
                    }

                }).catch(() => {
                    this.loading = false
                })

            }
            this.videoPause();
            this.getStream(this.src);
        },


        async  queryClick (node) {
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
                    getDeviceId(node.assetId).then(res => {
                        if (res.data != null) {
                            this.$axios({
                                method: 'get',
                                url: process.env.VIDEO_URL + "/api/playback/start/" + res.data.deviceId + "/" + res.data.deviceChannelId + "",
                                params: { "deviceId": res.data.deviceId, "channelId": res.data.deviceChannelId, "startTime": this.queryParams.startTime, "endTime": this.queryParams.endTime }
                            }).then(function (res) {
                                console.log(res);
                                that.loading = false;
                                that.$message({
                                    type: 'error',
                                    message: res.data
                                })

                            }).catch(function (error) {
                                that.loading = false;
                                console.log(error)
                            });
                        } else {
                            that.$message({
                                type: 'error',
                                message: '当前位置暂无视频设备'
                            })
                        }

                    }).catch(() => {
                        this.loading = false
                    })
                }
                this.videoPause();
                this.getStream(this.src);
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
                    this.$axios({
                        method: 'get',
                        url: process.env.VIDEO_URL + "/api/download/start/" + node.deviceId + "/" + node.channelId,
                        params: { "deviceId": node.deviceId, "channelId": node.channelId, "startTime": this.queryParams.startTime, "endTime": this.queryParams.endTime, "downloadSpeed": "1" }
                    }).then(function (res) {
                        console.log(res);
                        that.$message({
                            type: 'error',
                            message: res.data
                        })

                    }).catch(function (error) {
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
    }
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
  user-select:none;
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
