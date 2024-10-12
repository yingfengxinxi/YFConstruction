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
<!--          <el-col :span="4" style="padding-left: 10px">-->
<!--            <el-button type="success" @click="sectionPlaneAnimation">进度确认</el-button>-->
<!--          </el-col>-->
        </el-row>
      </div>

    </div>

<!--  </flexbox>-->
</template>
<script>

import XrHeader from '@/components/XrHeader'
import * as echarts from 'echarts';

export default {
  name: 'SystemConfig',
  components: {
    XrHeader,
  },
  data() {
    return {
      loading: false,
      viewToken: '2a618f105f4f4038aae6e7b2d04b276e',//自定义引线标签：https://bimface.com/developer-jsdemo#817
      viewer3D:'',
      app:'',
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
        // 渲染场景
        viewer3D.render();
        //引线标签的顶点
        var position = new Object();
        position = { "x": 33894.07796927153, "y": 41552.17955049469, "z": -2279.999944983153 };

        // 初始化DrawableContainer
        var drawableConfig = new Glodon.Bimface.Plugins.Drawable.DrawableContainerConfig();
        drawableConfig.viewer = viewer3D;
        var drawableContainer = new Glodon.Bimface.Plugins.Drawable.DrawableContainer(drawableConfig);

        // 创建自定义元素，可以是一个dom element，也可以是个字符串
        var config = new Glodon.Bimface.Plugins.Drawable.CustomItemConfig();
        var content = document.createElement('div');
        content.innerHTML = '<div class="leadTips" style="display:flex;"><div style="width:49px;height:35px;">' +
                '<img   src="https://static.bimface.com/attach/24ce9654e88a4218908f46279e5c4b04_line.png" height="35" width="49"/>' +
                '</div><div class="leadText" id="canvasDiv" style="height:320px;width:580px"></div></div>'
        config.content = content;
        config.viewer = viewer3D;
        config.worldPosition = position;
        //生成customItem实例
        var customItem = new Glodon.Bimface.Plugins.Drawable.CustomItem(config);
        // 添加自定义标签
        drawableContainer.addItem(customItem);
        //用icharts画出图标
        that.drawChart2();
        //设置背景颜色
        viewer3D.setBackgroundColor(new Glodon.Web.Graphics.Color(105, 105, 105, 1));
      });
    },
    // 加载失败回调函数
    failureCallback(error) {
      console.log(error);
    },
    drawChart2(){
      let myChart = echarts.init(document.getElementById('canvasDiv'));
      let option = {
        backgroundColor: '#333',
        tooltip: {
          trigger: "axis",
        },
        color: ["#5DC2F9", "#F5CB3B"],
        grid: {
          right: "10%",
          left: "10%",
          top: '10%',
          bottom: '10%'
        },
        // legend: {
        //   icon: "circle",
        //   bottom: '6%',
        //   left: 20,
        //   textStyle: {
        //     color: "#ffffff",
        //   },
        // },
        xAxis: {
          type: 'category',
          axisLine: {
            show: true,
            color: '#A3E9FC'
          },
          axisLabel: {
            color: '#fffff',
            width: 100,
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: '#ffffff',
              opacity: 0.2
            }
          },
          data: ['02:33', '03:33', '04:33', '05:33', '05:33', '05:33'],
        },
        yAxis: {
          type: 'value',
          splitLine: {
            show: true,
            lineStyle: {
              color: '#ffffff',
              opacity: 0.2
            }
          },
          boundaryGap: true,
          axisLine: {
            show: true,
            color: 'A3E9FC'
          },
          axisLabel: {
            show: true,
            margin: 20,
            textStyle: {
              color: '#ffffff'
            }
          },
          axisTick: {
            show: false
          }
        },
        series: [{
          name: "水位监测值",
          type: "line",
          markLine: { //最大值和最小值
            data: [{
              name: '水位上限',
              yAxis: 300,
              label: {
                show: 'true',
                position: 'end',
                formatter: '水位上限',
                color: '#7BF7AB'
              },
              lineStyle: {
                normal: {
                  width: 2,
                  color: "#7BF7AB",
                }
              }

            },
              // {
              //   name: '水位下限20',
              //   yAxis: 44,
              //   label: {
              //     show: 'true',
              //     position: 'end',
              //     formatter: '水位下限20',
              //     color: '#E8556D'
              //   },
              //   lineStyle: {
              //     normal: {
              //       width: 2,
              //       color: "#E8556D",
              //     }
              //   }
              //
              // },
            ]
          },
          smooth: true, //平滑曲线显示
          showAllSymbol: true, //显示所有图形。
          symbol: "circle", //标记的图形为实心圆
          symbolSize: 10, //标记的大小
          itemStyle: {
            //折线拐点标志的样式
            color: "#64C9F0"
          },
          lineStyle: {
            color: "#64C9F0"
          },
          data: [122, 208, 348, 450, 580]
        }, ]
      };
      myChart.setOption(option)
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


.leadTips {
  display: flex;
  justify-content: start;
  width: auto;
  transform: translateY(-40px)
}

.leadTips img {
  display: inline-block;
  width: 49px;
  height: 40px
}

.leadText {
  display: block;
  background: #4a90e2;
  color: #fff;
  padding: 10px
}
</style>

