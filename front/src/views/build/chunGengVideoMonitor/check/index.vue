<template>
  <div class="employee-dep-management" style="margin: 10px;background-color: white; height: 96.5%">
    <!--    <xr-header-->
    <!--      icon-class="wk wk-manage"-->
    <!--      icon-color="#2EC2F9"-->
    <!--      label="视频查看"/>-->
    <div class="customer-content">
      <div class="system-view-nav">
        <el-tree :props="props" :load="loadNode" lazy @node-click="nodeClick" />
      </div>
      <keep-alive>
        <div class="system-view-content" style="height:100%;">
          <!--          <video-->
          <!--            ref="videoPlayer" :src="src"  style="width: 100%; height: 100%; object-fit: fill" />-->
          <video ref="video" muted style="width: 100%; height: 100%; object-fit: fill"></video>
        </div>
      </keep-alive>
    </div>
  </div>
</template>

<script>
  import XrHeader from '@/components/XrHeader'
  import {
    queryByParentId, queryTreeListByParentId, queryEquiBytypeId, queryVideoMonitor
  } from '@/api/build/videoMonitor'
  import request from '@/utils/request'
  let Hls = require('hls.js');
  export default {
    name: 'videoView',
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


        deviceId: '', //设备id
        channelId: '' //通道id
      }
    },
    mounted () {
      // console.log(this.$refs.videoPlayer)
      // this.getStream("http://hls01open.ys7.com/openlive/046c0e07cdb24ac085d00b734ec7ec6d.m3u8");
    },
    methods: {
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
        console.log("node===", node)
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
          //查询设备列表
          // this.$axios({ method: 'get', url: process.env.VIDEO_URL +"/api/device/query/devices", params: { "page": "1", "count": "9999" } }).then(function (res) {  console.log(res);   resolve(res.data.list) }).catch(function (error) { console.log(error) });
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
          //查询设备下级通道
          // this.$axios({ method: 'get', url: process.env.VIDEO_URL +"/api/device/query/devices/"+node.data.deviceId+"/channels", params: { "page": "1", "count": "9999", "deviceId": node.data.deviceId } }).then(function (res) {  console.log(res); if(res.data.list.length!=0){  res.data.list.forEach(element => {  element.leaf=true });  resolve(res.data.list) }  }).catch(function (error) { console.log(error) });
        } else if (node.level === 2) {

        } else {

        }
      },
      async  nodeClick (node) {
        this.loading = true;
        console.log(node);
        console.log(node.level);
        console.log(node.leaf && node.level == 1);
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
              this.$axios({
                method: 'get',
                url: process.env.VIDEO_URL + "/api/play/start/" + res.data.deviceId + "/" + res.data.deviceChannelId + "",
                params: { "deviceId": res.data.deviceId, "channelId": res.data.deviceChannelId }
              }).then(function (res) {
                console.log(res);
                if (res.data != "" && res.data != null) {
                  if (res.data.code == -1) {
                    that.$message({
                      type: 'error',
                      message: res.data.msg
                    })
                  } else {
                    this.videoPause();
                    this.getStream(this.src);
                  }
                } else {
                  that.$message({
                    type: 'error',
                    message: '当前设备不存在或已离线'
                  })
                }


              }).catch(function (error) {
                console.log(error)
              });
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
