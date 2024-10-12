<template>
<!--  <flexbox-->
<!--    class="main"-->
<!--    direction="column"-->
<!--    align="stretch">-->
<!--    <xr-header-->
<!--      icon-class="wk wk-enterprise"-->
<!--      icon-color="#2362FB"-->
<!--      label="BIM" />-->
      <!-- 基本信息 -->
      <div class='main'>
            <!-- 定义DOM元素，用于在该DOM元素中显示模型或图纸 -->

            <div class='model' id="domId" style="width:80%; height:100%"></div>
            <div class="right-bar">
              <el-table :data="monitorTypeDict" highlight-current-row @current-change="selectListData">
                <el-table-column label="监测项名称" align="center" prop="dictLabel" />
              </el-table>
            </div>

<!--          <el-col :span="4" style="padding-left: 10px">-->
<!--            <el-button type="success" @click="sectionPlaneAnimation">进度确认</el-button>-->
<!--          </el-col>-->
      </div>


<!--  </flexbox>-->
</template>
<script>

import XrHeader from '@/components/XrHeader'
import * as echarts from 'echarts';
import request from '@/utils/request'
import { mapGetters } from 'vuex'
export default {
  name: 'equDataShow',
  components: {
    XrHeader,
    mapGetters
  },
  data() {
    return {
      loading: false,
      viewToken: '58cf133c5133456f9f7892c4016e8d80',//自定义引线标签：https://bimface.com/developer-jsdemo#817
      viewer3D: '',
      app: '',
      dataS: {},
      drawableContainer: '',
      // 监测项类型字典项
      monitorTypeDict: [],
      dataList: [],
      RumTime: null,
      typeId: ''
    }
  },
  computed: {
    ...mapGetters(['pageDataReflushTime']),
  },
  mounted() {
    var that = this
    // 初始化显示组件
    var options = new BimfaceSDKLoaderConfig();
    options.viewToken = that.viewToken;
    BimfaceSDKLoader.load(options, that.successCallback, that.failureCallback);
  },
  created() {
    this.getDicts('b_equipment_monitor_type').then(res => {
      console.log(res.data);
      this.monitorTypeDict = res.data
    })
    console.log( this.pageDataReflushTime)
  },
  beforeDestroy() {
    console.log("12313131111");
    if (this.RumTime) {
      clearInterval(this.RumTime)
    }
  },
  methods: {
    selectListData(val) {
      var worldPositions = {}
      var that = this
      clearInterval(this.RumTime)
      this.drawableContainer.clear()
      this.typeId = val.dictValue
      request({
        url: 'build/equipmentMonitorPoint/getMonitorList/' + val.dictValue,
        method: 'get'
      }).then(response => {
        this.dataList = response.data
        console.log(this.dataList)
        for (var i = 0; i < this.dataList.length; i++) {
          console.log(this.dataList[i].controlsState)
          if (this.dataList[i].controlsState != '3') {
            var m = this.dataList[i].controlsCoordinates
            if (m != null && m != '') {
              m = this.dataList[i].controlsCoordinates.split(',')
              if (m.length > 2) {
                worldPositions = { 'x': m[0], 'y': m[1], 'z': m[2] }
              }
            }
            console.log(worldPositions)
            var divId = 'dataShow' + i
            var config = new Glodon.Bimface.Plugins.Drawable.CustomItemConfig()
            // 引线折点的相对位置
            config.offset = { x: 27, y: -47 }
            // 引线标签的内容
            var content = document.createElement('div')
            if (this.dataList[i].controlsState == '1' || this.dataList[i].controlsState == '0') {
              content.innerHTML = `<div class="leadTips" id="${divId}"><div>
                 <img   src="https://static.bimface.com/attach/24ce9654e88a4218908f46279e5c4b04_line.png" height="35" width="49"/>
                 </div><div class="leadTextRed"><div class="solid"></div>${this.dataList[i].pointName}:<span style="color:${this.dataList[i].controlsState == '1'?'red' : '#000'}" >
                  ${this.dataList[i].controlsValue}(${this.dataList[i].unit})</span></div></div>`
            } else if (this.dataList[i].controlsState == '2') {
              content.innerHTML = `<div class="leadTips" id="${divId}"><div >
                 <img     src="https://static.bimface.com/attach/24ce9654e88a4218908f46279e5c4b04_line.png" height="35" width="49"/>
                 </div><div class="leadTextRed">离线</div></div>`
            }
            config.content = content
            // 引线标签关联的构件
            config.objectId = this.dataList[i].controlsId
            // 引线标签的世界坐标
            config.worldPosition = worldPositions
            // 引线标签是否可拖拽
            config.draggable = false
            // 引线标签的视图
            config.viewer = this.viewer3D
            var label = new Glodon.Bimface.Plugins.Drawable.CustomItem(config)
            this.drawableContainer.addItem(label)
          }
        }
        // 新建定时任务刷新数据内容
        this.RumTime = setInterval(function() {
          console.log(that.typeId)
          if (that.typeId != null && that.typeId != '') {
            request({
              url: 'build/equipmentMonitorPoint/getMonitorList/' + that.typeId,
              method: 'get'
            }).then(response => {
              that.dataList = response.data
              for (var i = 0; i < that.dataList.length; i++) {
                var divIdsb = 'dataShow' + i
                if (that.dataList[i].controlsState == '1' || that.dataList[i].controlsState == '0') {
                  document.getElementById(divIdsb).innerHTML = `<div>
                 <img       src="https://static.bimface.com/attach/24ce9654e88a4218908f46279e5c4b04_line.png" height="35" width="49"/>
                 </div><div class="leadTextRed"><div class="solid"></div>${that.dataList[i].pointName}:<span style="color:${that.dataList[i].controlsState == '1'?'red' : '#000'}" >
                  ${that.dataList[i].controlsValue}(${that.dataList[i].unit})</span></div>`
                } else if (that.dataList[i].controlsState == '2') {
                  document.getElementById(divIdsb).innerHTML = `<div style="width:49px;height:35px;">
                 <img     src="https://static.bimface.com/attach/24ce9654e88a4218908f46279e5c4b04_line.png" height="35" width="49"/>
                 </div><div class="leadTextRed">离线</div>`
                }
              }
            })

          } else {
            clearInterval(this.RumTime)
          }
        }, this.pageDataReflushTime)
      })
    },
    // 获取id与经纬度
    hqidjwd(componentData) {
      if (componentData && componentData.objectId) {
        this.dataS.componentId = componentData.objectId
        this.dataS.worldPosition = componentData.worldPosition
        console.log(this.dataS)
      } else {
        alert('123321')
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
        // 渲染场景
        viewer3D.render();
        //引线标签的顶点
        var position = new Object();
        position = { "x": 33894.07796927153, "y": 41552.17955049469, "z": -2279.999944983153 };

        // 初始化DrawableContainer
        var drawableConfig = new Glodon.Bimface.Plugins.Drawable.DrawableContainerConfig();
        drawableConfig.viewer = viewer3D;
        that.drawableContainer = new Glodon.Bimface.Plugins.Drawable.DrawableContainer(drawableConfig);

        //设置背景颜色
        viewer3D.setBackgroundColor(new Glodon.Web.Graphics.Color(105, 105, 105, 1));
      });

      // 视图增加点击事件
/*      that.viewer3D.addEventListener(Glodon.Bimface.Application.WebApplication3DEvent.ComponentsSelectionChanged, function(componentData) {
        console.log(componentData)
        if (componentData && componentData.objectId) {
          that.dataS.componentId = componentData.objectId
          that.dataS.worldPosition = componentData.worldPosition
          console.log(that.dataS)
        } else {
          alert('123321')
        }
      })*/
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


/deep/.leadTips {
  display: flex;
  transform: translateY(-40px);

}

/deep/.leadTips img {
  display: inline-block;
  width: 49px;
  height: 40px
}

/deep/.leadText {
  display: block;
  background: #4a90e2;
  color: #fff;

}
/deep/.right-bar {
  position: absolute;
  right: 0;
  top: 0;
  width: 20%;
  height: 100%;
  border-left: 1px solid #d8d8d8;
  vertical-align: top;
  overflow: auto;
}
 /deep/.leadTextRed{
   height: 28px;
   line-height: 28px;
   color: #000;
   background: rgba(255,255,255,0.77);
   font-size: 12px;
   padding: 0 10px;
   white-space:nowrap;
   display: flex;
   align-items: center;
 }
  /deep/.solid{
    width: 8px;
    height: 8px;
    background: #3e6bea;
    border-radius: 50%;
    margin-right: 8px;
  }
</style>

