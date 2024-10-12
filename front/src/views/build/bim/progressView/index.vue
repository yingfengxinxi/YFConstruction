<template>
<!--  <flexbox-->
<!--    class="main"-->
<!--    direction="column"-->
<!--    align="stretch">-->
<!--    <xr-header-->
<!--      icon-class="wk wk-enterprise"-->
<!--      icon-color="#2362FB"-->
<!--      label="BIM" />-->
    <div
      v-loading="loading"
      class="body">
      <!-- 基本信息 -->
      <div class='main'>
        <div class='buttons'>
          <!--          <button type="button" class="button" @click="getComponentProperty">构件属性</button>-->
          <!--          <button type="button" class="button" @click="getFloors">楼层信息</button>-->
          <!--              <el-button class="main-table-header-button xr-btn&#45;&#45;orange" @click="getModelTree">目录树信息</el-button>-->
          <!--              <button type="button" class="button" id="btnOverrideComponentsColorByObjectData"-->
          <!--                      @click="overrideComponentsColorByData">根据条件给构件着色</button>-->
        </div>
        <el-row>
          <el-col :span="24">
            <!-- 定义DOM元素，用于在该DOM元素中显示模型或图纸 -->
            <div class='model' id="domId" style="width:100%; height:720px"></div>
          </el-col>
        </el-row>
        <el-row style="text-align: right;padding: 10px">
          <el-button type="success" @click="sectionPlaneAnimation">施工进度展示</el-button>
        </el-row>
      </div>

    </div>

<!--  </flexbox>-->
</template>
<script>

import XrHeader from '@/components/XrHeader'

import { mapGetters } from 'vuex'

export default {
  name: 'SystemConfig',
  components: {
    XrHeader,
  },
  data() {
    return {
      loading: false,
      viewToken: '2363294e86ea42be8eb5fe2a461ad34c',//模型生长：https://bimface.com/developer-jsdemo#1108
      viewer3D:'',
      app:'',
      sectionPlane : null,
      originPt : { x: 0, y: 0, z: 7600 },
      planeDirection : { x: 0, y: 0, z: 1 },
      sectionPlaneOffset : 500,
      isSectionPlaneAnimationActivated : false,
      animationId : null,
    }
  },
  mounted() {
    var that=this
    // 初始化显示组件
    var options = new BimfaceSDKLoaderConfig();
    options.viewToken = that.viewToken;
    BimfaceSDKLoader.load(options, that.successCallback, that.failureCallback);
  },
  created() {
  },
  methods: {
    createSectionPlane(){
      if (!this.viewAdded || this.sectionPlane) {
        return;
      }
      var sectionPlaneConfig = new Glodon.Bimface.Plugins.Section.SectionPlaneConfig();
      sectionPlaneConfig.viewer = this.viewer3D;
      this.sectionPlane = new Glodon.Bimface.Plugins.Section.SectionPlane(sectionPlaneConfig);
      this.sectionPlane.setPlane(Glodon.Bimface.Plugins.Section.SectionPlanePlane.Z);
      this.sectionPlane.setDirection(Glodon.Bimface.Plugins.Section.SectionPlaneDirection.Forward);
      this.sectionPlane.setPositionByPlane(this.originPt, this.planeDirection, this.sectionPlaneOffset);
      this.sectionPlane.setObjectsByObjectData([
        {
          categoryId: "-2000011",
          family: "基本墙"
        },
        {
          categoryId: "-2001330"
        }
      ]);
      this.sectionPlane.hidePlane();
      },
    sectionPlaneAnimation(){
      if (!this.viewAdded || !this.sectionPlane) {
        return;
      }
      if (!this.isSectionPlaneAnimationActivated) {
        this.animate();
        this.setButtonText('btnSctPlaneAnimation', '停止剖切动画');
      } else {
        this.cancelAnimationFrame(this.animationId);
        this.sectionPlaneOffset = 500;
        this.sectionPlane.setPositionByPlane(originPt, planeDirection, sectionPlaneOffset);
        this.viewer3D.render();
        this.setButtonText('btnSctPlaneAnimation', '开启剖切动画');
      }
      this.isSectionPlaneAnimationActivated = !this.isSectionPlaneAnimationActivated;
    },
    animate() {
      this.animationId = requestAnimationFrame(this.animate);
      this.sectionPlaneOffset += 50;
      if (this.sectionPlaneOffset > 3500) {
        this.sectionPlaneOffset = 500;
      }
      this.sectionPlane.setPositionByPlane(this.originPt, this.planeDirection, this.sectionPlaneOffset);
      this.viewer3D.render();
    },
    setButtonText(btnId, text) {
      var dom = document.getElementById(btnId);
      if (dom != null && dom.nodeName == "BUTTON") {
        dom.innerText = text;
      }
    },
    // 加载成功回调函数
    successCallback(viewMetaData) {
      var that=this
      var dom4Show = document.getElementById('domId');
      // 设置WebApplication3D的配置项
      var webAppConfig = new Glodon.Bimface.Application.WebApplication3DConfig();
      webAppConfig.domElement = dom4Show;
      // 创建WebApplication3D，用以显示模型
      app = new Glodon.Bimface.Application.WebApplication3D(webAppConfig);
      app.addView(that.viewToken);
      var viewer3D = app.getViewer();
      that.viewer3D = viewer3D;
      // 增加加载完成监听事件
      viewer3D.addEventListener(Glodon.Bimface.Viewer.Viewer3DEvent.ViewAdded, function () {
        that.viewAdded = true;
        //自适应屏幕大小
        window.onresize = function () {
          viewer3D.resize(document.documentElement.clientWidth, document.documentElement.clientHeight - 40);
        }
        viewer3D.setCameraStatus({
          "name": "persp",
          "position": { "x": -30219.946765163415, "y": -45491.65416486451, "z": 29660.465140231066 },
          "target": { "x": 207832.38340026487, "y": 170233.69288006236, "z": -126343.72635548069 },
          "up": { "x": 0.3236896665312058, "y": 0.2933266544203183, "z": 0.8995468156730366 },
          "fov": 45,
          "zoom": 1,
          "version": 1,
          "coordinateSystem": "world"
        });
        viewer3D.hideComponentsByObjectData([
          {
            levelName: "Roof"
          },
          {
            levelName: "Parapet"
          },
          {
            levelName: "03 - Floor",
            categoryId: "-2000038"
          },
          {
            categoryId: "-2000023"
          }
        ]);
        that.createSectionPlane();
        // 渲染场景
        viewer3D.render();
      });
    },
    // 加载失败回调函数
    failureCallback(error) {
      console.log(error);
    },
  }
}
</script>

<style lang="scss" scoped>
.main {
  padding: 0px;
  height: 100%;
}

.body {
  background-color: white;
  border-bottom: 1px solid $xr-border-line-color;
  flex: 1;
  /*overflow-y: auto;*/
  padding: 10px;
  background-color: white;
  border-radius: $xr-border-radius-base;
  position: relative;
}

</style>

