<template>
  <div class="employee-dep-management" style="margin: 10px;background-color: white;">
    <!--    <xr-header-->
    <!--      icon-class="wk wk-manage"-->
    <!--      icon-color="#2EC2F9"-->
    <!--      label="视频查看"/>-->
    <div class="customer-content">
      <div class="system-view-nav" style="height:760px;">
        <el-tree :props="props" :load="loadNode" lazy @node-click="nodeClick" />
      </div>
      <div class="customer-right">
        <keep-alive>
          <div class="system-view-content" style="height:700px;" v-loading="loading">
            <!--          <video-->
            <!--            ref="videoPlayer" :src="src"  style="width: 100%; height: 100%; object-fit: fill" />-->
            <video ref="video" muted style="width: 100%; height: 100%; object-fit: fill"></video>
          </div>
        </keep-alive>
        <div class="control-button" style="">
          <el-button type="primary" icon="el-icon-caret-top" @mousedown.native="ptzCamera(0, 2, 0)" @mouseup.native="ptzCamera(0, 0, 0)"></el-button>
          <el-button type="primary" icon="el-icon-caret-bottom" @mousedown.native="ptzCamera(2, 0, 0)" @mouseup.native="ptzCamera(0, 0, 0)"></el-button>
          <el-button type="primary" icon="el-icon-caret-left" @mousedown.native="ptzCamera(0, 1, 0)" @mouseup.native="ptzCamera(0, 0, 0)"></el-button>
          <el-button type="primary" icon="el-icon-caret-right" @mousedown.native="ptzCamera(1, 0, 0)" @mouseup.native="ptzCamera(0, 0, 0)"></el-button>
          <el-button type="primary" icon="el-icon-refresh" @click="control('refresh')">重置</el-button>
        </div>
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
  name: 'videoReplayTheater',
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
      loading:false,
      hls: '',
      //摄像头移动速度
      controSpeed: 30,
      //临时记录node节点数据
      nodeData: ''
    }
  },
  mounted () {
    // console.log(this.$refs.videoPlayer)
    // this.getStream("http://hls01open.ys7.com/openlive/046c0e07cdb24ac085d00b734ec7ec6d.m3u8");
  },
  methods: {
    ptzCamera (leftRight, upDown, zoom) {
      console.log("进入控制", this.nodeData)
      if (this.nodeData != '' && this.nodeData.leaf == true) {

        console.log('云台控制：' + leftRight + ' : ' + upDown + " : " + zoom);
        getDeviceId(this.nodeData.assetId)
          .then(res => {
            if (res.data != null) {
              this.$axios({
                method: 'post',
                url: process.env.VIDEO_URL+'/api/ptz/control/' + res.data.deviceId + '/' + res.data.deviceChannelId + '?cmdCode=' + (zoom * 16 + upDown * 4 + leftRight) + '&horizonSpeed=' + this.controSpeed + '&verticalSpeed=' + this.controSpeed + '&zoomSpeed=' + this.controSpeed
              }).then(function (res) { console.log("云台控制回执-->", res) });
            } else {
              this.$message({ type: 'error', message: '当前位置暂无视频设备' })
            }
          })
          .catch(() => { this.loading = false })
      } else {
        this.$message({ type: 'error', message: '请选择位置树末级节点' })
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
      console.log(node);
      if (node.leaf) {
        this.nodeData = node;
        let param = {};
        param.portType = 'httpPort';
        param.app = node.locationId
        param.stream = node.assetCode
        await queryVideoMonitor(param).then(res => {
          console.log(res);

          if (res.data != null) {
            this.src = 'http://' + res.data.ip + ':' + res.data.httpPort + '/' + res.data.app + '/' + res.data.stream + '/hls.m3u8'
          } else {
            this.$message({
              type: 'error',
              message: '请检查网络连接是否正常或检查摄像头是否开启'
            })
          }
        })
      } else {
        this.src = 'http://hls01open.ys7.com/openlive/046c0e07cdb24ac085d00b734ec7ec6d.m3u8'
        
      }
      this.videoPause();
      this.getStream(this.src);
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
.customer-right {
  flex: 1;
  position: relative;
  display: flex;
  overflow: hidden;
  height: 760px;
  flex-flow: column;
}
.control-button {
  position: relative;
  border: 1px solid $xr-border-line-color;
  height: 50px;
  margin-top: 10px;
  padding: 10px 30%;
  /deep/ .el-button {
    margin-left: 30px !important;
  }
}
.system-view-nav {
  min-width: 200px;
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
  overflow-x: auto;
  overflow-y: auto;
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
