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
          <el-button type="success" @click="playkey">开始巡检</el-button>
          <el-button type="warning" @click="pause">暂停</el-button>
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
      viewToken: 'b8643c66ccdc4f26a5b74aa674093358',//自定义关键帧：https://bimface.com/developer-jsdemo#1014
      viewer:'',
      app:'',
      color:'',
      wt:'',
      drawableContainer:''
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
    setButtonText(btnId, text) {
      var dom = document.getElementById(btnId);
      if (dom != null && dom.nodeName == "BUTTON") {
        dom.innerText = text;
      }
    },
    // 加载成功回调函数
    successCallback(viewMetaData) {
      var that=this
      that.color = new Glodon.Web.Graphics.Color("#32D3A6", 0.8);
      if (viewMetaData.viewType == "3DView") {
        var dom4Show = document.getElementById('domId');
        // 设置WebApplication3D的配置项
        var webAppConfig = new Glodon.Bimface.Application.WebApplication3DConfig();
        webAppConfig.domElement = dom4Show;
        // 创建WebApplication3D，用以显示模型
        that.app = new Glodon.Bimface.Application.WebApplication3D(webAppConfig);
        that.app.addView(that.viewToken);
        var viewer =  that.app.getViewer();
        that.viewer = viewer;
        //标签类的设置
        var drawableConfig = new Glodon.Bimface.Plugins.Drawable.DrawableContainerConfig();
        drawableConfig.viewer = viewer;
        //容器
        that.drawableContainer = new Glodon.Bimface.Plugins.Drawable.DrawableContainer(drawableConfig);
        var config = new Glodon.Bimface.Plugins.Walkthrough.WalkthroughConfig();
        config.viewer = viewer;
        that.wt = new Glodon.Bimface.Plugins.Walkthrough.Walkthrough(config);
        // 监听添加view完成的事件
        viewer.addEventListener(Glodon.Bimface.Viewer.Viewer3DEvent.ViewAdded, function() {
          //自适应屏幕大小
          window.onresize = function() {
            viewer.resize(document.documentElement.clientWidth, document.documentElement
                    .clientHeight - 40);
          }
          //add key frames
          var keyFrames = [];
          keyFrames.push({
            id: "21124876-8436-4a7e-8f3f-50f30876baeb",
            position:{
              "x":54338.30618321142,
              "y": 24823.562037428845,
              "z": 19009.247486006847
            },
            target:{
              "x": 138966.22492577098,
              "y": -343576.4725266925,
              "z": 47971.612488661136
            },
            "coordinateSystem":"world",
          });
          keyFrames.push({
            id: "86f4dd8e-8a43-4d4d-8aa5-cf5e3cdf16a1",
            position: {
              "x": 55392.23266330976,
              "y": 19177.558474637684,
              "z": 17950.4500522592
            },
            target:{
              "x": 237925.633560422,
              "y": -311824.03024018207,
              "z": 60086.961114641
            },
            "coordinateSystem":"world",
          });
          keyFrames.push({
            id: "d1d3d7bb-684a-4ce6-8b02-e41657ee83d5",
            position:{
              "x": 60458.92777828335,
              "y": 12369.261934180973,
              "z": 19647.874306033493
            },
            target:{
              "x": 206252.83091484627,
              "y": -336377.9684782245,
              "z": 59147.34131520492
            },
            "coordinateSystem":"world",
          });
          keyFrames.push({
            id: "69e3cae8-3b20-481d-9a42-2fe176ca56ea",
            position:{
              "x": 65011.509756267376,
              "y": -7770.007557460343,
              "z": 19279.4542151404
            },
            target:{
              "x": 378613.74215948314,
              "y": -218801.00165745476,
              "z": 50839.57258962678
            },
            "coordinateSystem":"world",
          });
          keyFrames.push({
            id: "5475b7eb-2589-483b-85f8-83d24543d6d8",
            position:{
              "x": 94393.5467001607,
              "y": -10041.437121392693,
              "z": 19279.445871713728
            },
            target:{
              "x": 428914.31778981985,
              "y": -186042.26938381407,
              "z": 56045.09022472101
            },
            "coordinateSystem":"world",
          });
          that.wt.setKeyFrames(keyFrames);
        });
      }
    },
    // 加载失败回调函数
    failureCallback(error) {
      console.log(error);
    },
    pause() {
      this.wt.pause();
    },
    //播放,
    playkey() {
      //wt.play(time);
      //播放到关键帧的时候促发回调事件
      this.wt.setKeyFrameCallback(this.keyFrameCallback);
      this.wt.setWalkthroughTime(20);
      this.wt.stopCallback(this.stopWalk);
      this.wt.play();
    },
    //结束回调函数
    stopWalk(){
      this.drawableContainer.clear()
    },
    //回调事件
    keyFrameCallback(idx) {
      console.log("Current key frame index is " + idx);
      switch (idx) {
        case 0:
          this.action_0();
          break;
        case 1:
          this.action_1();
          break;
        case 2:
          this.action_2();
          break;
        case 3:
          this.action_3();
          break;
        case 4:
          this.action_4();
          break;
        default:
          break;
      }
    },
    //关键帧0的回调事件
    action_0() {
      console.log("Action 1");
      var position1 = new Object();
      position1 = {
        "x": 59026.898499509276,
        "y": 16027.630284457171,
        "z": 18568.508610052737
      };
      this.addcustomitem(position1)
      this.viewer.enableBlinkComponents(true);
      this.viewer.setBlinkColor( this.color);
      var objectIds = ["1477311584420352.3805100"];
      this.viewer.setBlinkComponentsById(objectIds);
    },
    //关键帧1的回调事件
    action_1() {
      console.log("Action 2");
      var position2 = new Object();
      position2 = {
        "x": 61277.39289979506,
        "y": 8167.548450051833,
        "z": 19070.83564545811
      };
      this.addcustomitem(position2);
      var objectIds = ["1477311584420352.3855400"];
      this.viewer.enableBlinkComponents(true);
      this.viewer.overrideComponentsColorById(objectIds, this.color);
      this.viewer.enableBlinkComponents(false);
    },
    //关键帧2的回调事件
    action_2() {
      // console.log("Action 3");
      var position2 = new Object();
      position2 = {
        "x": 70852.81623884341,
        "y": -4461.765783049294,
        "z": 17585.47922510225
      };
      this.addcustomitem(position2);
      var objectIds = ["1477311240716800.2366702"];
      this.viewer.enableBlinkComponents(true);
      this.viewer.overrideComponentsColorById(objectIds, this.color);
      this.viewer.enableBlinkComponents(false);
    },
    //关键帧3的回调事件
    action_3() {
      console.log("Action 4");
      var position3 = new Object();
      position3 = {
        "x": 73670.83334490952,
        "y": -9370.809278067574,
        "z": 20825.000144211066
      };
      this.addcustomitem(position3);
      var objectIds = ["1477311584420352.3686915", "1477311584420352.4245712", "1477311584420352.3686916"];
      this.viewer.enableBlinkComponents(true);
      this.viewer.overrideComponentsColorById(objectIds, this.color);
      this.viewer.enableBlinkComponents(false);
    },
    //关键帧4的回调事件
    action_4() {
      console.log("Action 5");
      var position4 = new Object();
      position4 = {
        "x": 102627.00303460653,
        "y": -15454.954320614783,
        "z": 18565.96119331755
      };
      this.addcustomitem(position4);
      var objectIds = ["1477311584420352.3821577"];
      this.viewer.enableBlinkComponents(true);
      this.viewer.overrideComponentsColorById(objectIds, this.color);
      this.viewer.enableBlinkComponents(false);
    },
    //自定义二维标签
    addcustomitem(position) {
      console.log("addcustomitem");
      var config = new Glodon.Bimface.Plugins.Drawable.CustomItemConfig();
      var circle = document.createElement('div');
      //自定义样式，支持Html的任意dom元素
      circle.style.width = '200px';
      circle.style.height = '32px';
      circle.style.borderRadius = '20%';
      circle.style.background = '#fff';
      var problemname = '例行安全巡检，未发现隐患';
      circle.innerHTML = '<p style="padding:8px 12px">' + problemname +'</p>';
      config.content = circle;
      config.viewer = this.viewer;
      config.worldPosition = position;
      //生成customItem实例
      var customItem = new Glodon.Bimface.Plugins.Drawable.CustomItem(config);
      // 添加自定义标签
      this.drawableContainer.addItem(customItem);
      //设置Tip的样式
      customItem.setTooltipStyle({
        border: '1px'
      });
    }
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

