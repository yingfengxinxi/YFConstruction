<template>
  <div class="employee-dep-management" style="margin: 10px;background-color: white;">
<!--    <xr-header-->
<!--      icon-class="wk wk-manage"-->
<!--      icon-color="#2EC2F9"-->
<!--      label="视频查看"/>-->
    <div class="customer-content">
      <div class="system-view-nav">
        <el-tree
          :props="props"
          :load="loadNode"
          lazy
          @node-click="nodeClick"/>
      </div>
      <keep-alive>
        <div class="system-view-content" style="height:760px;">
<!--          <video-->
<!--            ref="videoPlayer" :src="src"  style="width: 100%; height: 100%; object-fit: fill" />-->
          <video  ref="video" muted style="width: 100%; height: 100%; object-fit: fill" ></video>
        </div>
      </keep-alive>
    </div>
  </div>
</template>

<script>
import XrHeader from '@/components/XrHeader'
import {
  queryByParentId
} from '@/api/build/videoMonitor'
let Hls = require('hls.js');
export default {
  name: 'videoMonitorCheck',
  components: {
    XrHeader
  },
  data() {
    return {
      props: {
        label: 'deviceName',
        children: 'zones'
        // isLeaf: 'hasChildren'
      },
      src: '',
      hls: ''
    }
  },
  mounted() {
    // console.log(this.$refs.videoPlayer)
    this.getStream("http://127.0.0.1:80/1/1/hls.m3u8");
  },
  methods: {
    videoPause() {
      if (this.hls) {
        this.$refs.video.pause();
        this.hls.destroy();
        this.hls = null;
      }
    },
    getStream(source) {
      if (Hls.isSupported()) {
        this.hls = new Hls();
        this.hls.loadSource(source);
        this.hls.attachMedia(this.$refs.video);
        this.hls.on(Hls.Events.MANIFEST_PARSED, () => {
          console.log('加载成功');
          this.$refs.video.play();
        });
        this.hls.on(Hls.Events.ERROR, (event, data) => {
          console.log(event, data);
          // 监听出错事件
          console.log('加载失败');
        });
      }
    },
    beforeDestroy() {
      this.videoPause();
    },
    loadNode(node, resolve) {

      if (node.level === 0) {
        queryByParentId(0)
          .then(res => {
            resolve(res.data)
          })
          .catch(() => {
            this.loading = false
          })
      } else {
        queryByParentId(node.data.id)
          .then(res => {
            resolve(res.data)
          })
          .catch(() => {
            this.loading = false
          })
      }
    },
    nodeClick(node) {
      // var i =  Math.floor(Math.random()*100)
      // console.log(JSON.stringify(node))
      if(node.id%2==1){
        // console.log("aaa"+i)
        this.src = 'http://hls01open.ys7.com/openlive/046c0e07cdb24ac085d00b734ec7ec6d.m3u8'
      } else {
        // console.log("bbb"+1)
        this.src = 'http://hls01open.ys7.com/openlive/beb61431b833401bab19c55cd1faa5ba.m3u8'
      }
      this.videoPause();
      this.getStream(this.src);
      // var than = this;
      // setTimeout(function () {
      //   than.$refs.videoPlayer.play();
      // }, 1000);
      // console.log(node)
    },
    // toPlay(){
    //   this.$refs.videoPlayer.paused === true ? this.$refs.videoPlayer.play() : this.$refs.videoPlayer.pause();
    // },
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
    content: ' ';
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    width: 2px;
    background-color: #5383ed;
  }
</style>
