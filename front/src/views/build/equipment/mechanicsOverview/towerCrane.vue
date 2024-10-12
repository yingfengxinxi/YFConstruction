<template>
  <div class="dom">
    <div class="dom_l">
      <div class="dom_l_top">
        <div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="/static/img/Symbol.png">塔吊基本信息</div>
        <div class="tower_img_dom">
          <div class="tower_img_dom_3">
            <div class="tower_img_dom_3_c">
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'     src="../../../../../static/img/towerCraneMonitor/tjimg.png" height="100%" width="100%" class="img2">
            </div>
          </div>
          <div class="tower_img_dom_3">
            <div class="tower_img_dom_3_c tower_img_dom_3_d">
              <div class="tower_middle_10">
                <div class="tower_text_1">
                  <div id="triangle-up"></div>
                </div>
                <div class="tower_text_3">
                  <span>塔机名称</span>
                </div>
                <div class="tower_text_6">
                  <el-select v-model="assetId" @change="getInfo" placeholder="请选择">
                    <el-option
                      v-for="item in towerList"
                      :key="item.assetId"
                      :label="item.assetName"
                      :value="item.assetId">
                    </el-option>
                  </el-select>
                </div>
              </div>
              <div  class="tower_middle_10">
                <div class="tower_text_1">
                  <div id="triangle-up-2"></div>
                </div>
                <div class="tower_text_3">
                  <span>塔机编号</span>
                </div>
                <div class="tower_text_6">
                  <span style="padding-left: 8px">{{tower.assetCode}}</span>
                </div>
              </div>
              <div  class="tower_middle_20">
                <div class="tower_qbc_dom">
                  <div style="position: relative;top: 50%;transform: translateY(-50%);">
                    <div class="tower_dom_text_l"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'     src="../../../../../static/img/towerCraneMonitor/qbcico.png"/></div>
                    <div class="tower_dom_text_m">{{tower.foreArmLength?tower.foreArmLength:'0'}}</div>
                    <div class="tower_dom_text_r">前臂长</div>
                  </div>
                </div>
              </div>
              <div  class="tower_middle_20">
                <div class="tower_qbc_dom lsbj">
                  <div style="position: relative;top: 50%;transform: translateY(-50%);">
                    <div class="tower_dom_text_l"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'     src="../../../../../static/img/towerCraneMonitor/hbcico.png"/></div>
                    <div class="tower_dom_text_m">{{tower.rearArmLength?tower.rearArmLength:'0'}}</div>
                    <div class="tower_dom_text_r">后臂长</div>
                  </div>
                </div>
              </div>
              <div  class="tower_middle_20">
                <div class="tower_qbc_dom csbj">
                  <div style="position: relative;top: 50%;transform: translateY(-50%);">
                    <div class="tower_dom_text_l"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'     src="../../../../../static/img/towerCraneMonitor/tbgico.png"/></div>
                    <div class="tower_dom_text_m">{{tower.armHeight?tower.armHeight:'0'}}</div>
                    <div class="tower_dom_text_r">塔臂高</div>
                  </div>
                </div>
              </div>
              <div  class="tower_middle_20">
                <div class="tower_qbc_dom hsbj">
                  <div style="position: relative;top: 50%;transform: translateY(-50%);">
                    <div class="tower_dom_text_l"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'     src="../../../../../static/img/towerCraneMonitor/blico.png"/></div>
                    <div class="tower_dom_text_m">{{tower.multiple?tower.multiple:'0'}}</div>
                    <div class="tower_dom_text_r">倍率</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="tower_img_dom_3" style="padding: 0">
            <div class="tower_jd_dom">
              <div  id="tjxzfx" style="height: 100%;width: 100%">

              </div>
            </div>
            <div class="tower_jd_dom">
                <div class="tower_jd_dom_3">
                  <div class="tower_jd_text_2">转角 {{tower.angle?tower.angle:''}}度</div>
                  <div class="tower_jd_text_3">数据获取时间:</div>
                  <div class="tower_jd_text_4" style="color: #649BF8">{{tower.monitorTime?tower.monitorTime:'-- / --'}}</div>
                </div>
                <div class="tower_jd_dom_35">
                  <div class="tower_jd_bor">
                    <div style="position: relative;top: 50%;transform: translateY(-50%);">
                      <div  class="tower_jd_bor_img"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    width="16px" src="../../../../../static/img/towerCraneMonitor/fxico.png"/></div>
                      <div  class="tower_jd_bor_imgt">风级</div>
                      <div  class="tower_jd_bor_imgr" style="border: 0">{{tower.windLevel?tower.windLevel:'0'}}</div>
                    </div>
                  </div>
                </div>
                <div class="tower_jd_dom_35">
                  <div  class="tower_jd_bor">
                    <div style="position: relative;top: 50%;transform: translateY(-50%);">
                      <div  class="tower_jd_bor_img"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    width="16px" src="../../../../../static/img/towerCraneMonitor/qjico.png"/></div>
                      <div  class="tower_jd_bor_imgt2">倾角</div>
                      <!--<div  class="tower_jd_bor_imgt3">X 0度</div>-->
                      <div  class="tower_jd_bor_imgr">{{tower.obliguity?tower.obliguity:'0'}}度</div>
                    </div>
                  </div>
                </div>
            </div>
          </div>
        </div>
        <div  class="tower_data_dom">
          <div  class="tower_data_dom2">
            <div class="tower_data_6">
              <div>
                <div class="tower_data_img"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/gdimg.png"/></div>
                <div  class="tower_data_text">
                  <div class="tower_data_text1">{{tower.height?tower.height:0}}M</div>
                  <div class="tower_data_text2">高度</div>
                </div>
              </div>
              <div class="tower_data_jdt">
                <el-progress  :show-text="false" :stroke-width="12" color="#A89FFF" :percentage="baifbjs(tower.height,tower.maxHeight)"></el-progress>
              </div>
            </div>
            <div class="tower_data_6">
              <div>
                <div class="tower_data_img"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/fdimg.png"/></div>
                <div  class="tower_data_text">
                  <div class="tower_data_text1">{{tower.amplitude?tower.amplitude:0}}M</div>
                  <div class="tower_data_text2">幅度</div>
                </div>
              </div>
              <div class="tower_data_jdt">
                <el-progress  :show-text="false" :stroke-width="12" color="#649BF8" :percentage="baifbjs(tower.amplitude,tower.maxRange)"></el-progress>
              </div>
            </div>
            <div class="tower_data_6">
              <div>
                <div class="tower_data_img"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/hzimg.png"/></div>
                <div  class="tower_data_text">
                  <div class="tower_data_text1">{{tower.angle?tower.angle:0}}度</div>
                  <div class="tower_data_text2">回转</div>
                </div>
              </div>
              <div class="tower_data_jdt">
                <el-progress  :show-text="false" :stroke-width="12" color="#77B8F8" :percentage="baifbjs(tower.angle,tower.maxAngle)"></el-progress>
              </div>
            </div>
            <div class="tower_data_6">
              <div>
                <div class="tower_data_img"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/xwzimg.png"/></div>
                <div  class="tower_data_text">
                  <div class="tower_data_text1">{{tower.weight?tower.weight:0}}T</div>
                  <div class="tower_data_text2">吊重</div>
                </div>
              </div>
              <div class="tower_data_jdt">
                <el-progress  :show-text="false" :stroke-width="12" color="#57CB91" :percentage="baifbjs(tower.weight,tower.maxWeight)"></el-progress>
              </div>
            </div>
            <div class="tower_data_6">
              <div>
                <div class="tower_data_img"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/fsimg.png"/></div>
                <div  class="tower_data_text">
                  <div class="tower_data_text1">{{tower.windspeed?tower.windspeed:0}}m/s</div>
                  <div class="tower_data_text2">风速</div>
                </div>
              </div>
              <div class="tower_data_jdt">
                <el-progress  :show-text="false" :stroke-width="12" color="#FAAD14" :percentage="baifbjs(tower.windspeed,tower.ratedWindSpeed)"></el-progress>
              </div>
            </div>
            <div class="tower_data_6">
              <div>
                <div class="tower_data_img"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/towerCraneMonitor/qximg.png"/></div>
                <div  class="tower_data_text">
                  <div class="tower_data_text1">{{tower.obliguity?tower.obliguity:0}}度</div>
                  <div class="tower_data_text2">倾斜</div>
                </div>
              </div>
              <div class="tower_data_jdt">
                <el-progress  :show-text="false" :stroke-width="12" color="#F4664A" :percentage="baifbjs(tower.obliguity,4)"></el-progress>
              </div>
            </div>
          </div>
        </div>


      </div>
      <div class="dom_l_bottom">
        <div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">实时统计</div>
        <div id="sstj">

        </div>
      </div>
    </div>
    <div class="dom_r">
      <div class="yxzt">
        <div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">运行转态</div>
        <div class="yxzt_dom">
          <p>
            <span class="gzsc">工作时长：{{yxzt.gzsc?(yxzt.gzsc/60).toFixed(2):'0'}}min</span>
            <span class="jrdz">今日吊重：{{yxzt.jrdz?yxzt.jrdz:'0'}}T</span>
          </p>
          <p>
            <span class="gzsc">今日吊次：{{yxzt.jrdc?yxzt.jrdc:'0'}}次</span>
            <span class="jrdz">今日报警：{{yxzt.bjs?yxzt.bjs:'0'}}次</span>
          </p>
          <p>
            <span class="gzsc">今日功效：{{yxzt.jrdz/yxzt.gzsc?(yxzt.jrdz/yxzt.gzsc).toFixed(2):0}}T/min</span>
          </p>
        </div>
      </div>
      <div class="sjxx">
        <div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">司机信息</div>
        <div class="sjxx_dom">
          <div class="sjxx_img">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="sjxx?sjxx.imgCardUrl:''"/>
          </div>
          <div class="sjxx_text">
            <p>姓名：{{sjxx.name?sjxx.name:'--'}}</p>
            <p>性别：{{sjxx.sex?sjxx.sex==1?'女':'男':'--'}}</p>
            <p>年龄：{{sjxx.csrq?sjxx.csrq:'--'}}</p>
            <p>电话：{{sjxx.phone?sjxx.phone:'--'}}</p>
            <p>身份证号：{{sjxx.idCard?sjxx.idCard:'--'}}</p>
          </div>
        </div>
      </div>
      <div class="sbxx">
        <div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">设备信息</div>
        <div class="sbxx_dom">
          <p>
            <span class="sbxx_text_l">设备编号：</span>
            <span  class="sbxx_text_r">{{tower.assetCode?tower.assetCode:'--'}}</span>
          </p>
          <p>
            <span class="sbxx_text_l">型号：</span>
            <span  class="sbxx_text_r">{{tower.model?tower.model:'--'}}</span>
          </p>
          <p>
            <span class="sbxx_text_l">设备产权单位：</span>
            <span  class="sbxx_text_r">{{tower.propertyUnit?tower.propertyUnit:'--'}}</span>
          </p>
          <p>
            <span class="sbxx_text_l">安装单位：</span>
            <span  class="sbxx_text_r">{{tower.installationUnit?tower.installationUnit:'--'}}</span>
          </p>
          <p>
            <span class="sbxx_text_l">制作厂家：</span>
            <span  class="sbxx_text_r">{{tower.manufacturer?tower.manufacturer:'--'}}</span>
          </p>
          <p>
            <span class="sbxx_text_l">检测机构：</span>
            <span  class="sbxx_text_r">{{tower.detectionUnit?tower.detectionUnit:'--'}}</span>
          </p>
          <p>
            <span class="sbxx_text_l">监测合格日期：</span>
            <span  class="sbxx_text_r">{{tower.verifyDate?tower.verifyDate:'--'}}</span>
          </p>
          <p>
            <span class="sbxx_text_l">使用登记日期：</span>
            <span  class="sbxx_text_r">{{tower.recordDate?tower.recordDate:'--'}}</span>
          </p>
          <p>
            <span class="sbxx_text_l">额定力矩：</span>
            <span  class="sbxx_text_r">{{tower.ratedTorque?tower.ratedTorque:'--'}}M·T</span>
          </p>
          <p>
            <span class="sbxx_text_l">最大吊重：</span>
            <span  class="sbxx_text_r">{{tower.maxWeight?tower.maxWeight:'--'}}T</span>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import  * as echarts from "echarts";
import { selectTowerCraneById,getList,selectDZTJ,selectJRGX,selectSJXX} from './api/towerCrane'
export default {
  name: "towerCrane",
  data() {
    return {
      timer:null, //定时器名称
      assetId:'',
      tower:{},
      rotation:0,
      towerList:[],
      dzObj:{},
      yxzt:{},
      sjxx:{},
    };
  },
  created() {

  },
  mounted() {
    this.tjxzfx();
    this.sstj();
    this.getTowerList();
  },
  methods: {
    getTowerList(){
      getList().then(res => {
        this.towerList = res.data;
        this.loading = false
        this.init();
      }).catch(() => {
        this.loading = false
      })
    },
    init() {
      this.assetId = this.$route.query.assetId;
      if(!this.assetId){
        this.assetId = this.towerList[0].assetId
      }
      selectTowerCraneById(this.assetId).then(res => {
        this.tower = res.data;
        if(this.tower.rotation){
          this.rotation = this.tower.rotation
        }
        this.tjxzfx();
        selectSJXX(this.tower.assetCode).then(res2 => {
          if(res2.data != null){
            this.sjxx = res2.data;
            var date = new Date();
            this.sjxx.csrq = date .getFullYear()-parseInt(this.sjxx.birthday.substr(0,4))
          }
        }).catch(() => {
        })
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
      selectDZTJ(this.assetId).then(res => {
        this.dzObj = res.data;
        this.sstj()
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
      selectJRGX(this.assetId).then(res => {
        this.yxzt = res.data;
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    getInfo(val){
      this.loading = true
      this.assetId = val;
      if(this.assetId){
        selectTowerCraneById(this.assetId).then(res => {
          this.tower = res.data;
          if(this.tower.rotation){
            this.rotation = this.tower.rotation
          }
          this.tjxzfx();
          selectSJXX(this.tower.assetCode).then(res2 => {
            this.sjxx = res2.data;
            var date = new Date();
            this.sjxx.csrq = date .getFullYear()-parseInt(this.sjxx.birthday.substr(0,4))
          }).catch(() => {
          })
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
        selectDZTJ(this.assetId).then(res => {
          this.dzObj = res.data;
          this.sstj()
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
        selectJRGX(this.assetId).then(res => {
          this.yxzt = res.data;
          console.log(this.yxzt)
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      }
    },
    tjxzfx(){
      var myChart = echarts.init(document.getElementById('tjxzfx'));
      var option = {
        series: [{
          type: 'gauge',
          radius: '80%',
          z: 100,
          min: 0, //最小刻度
          max: 360, //最大刻度
          splitNumber: 10, //刻度数量
          startAngle: 90,
          endAngle: -269.9,
          axisLine: {
            show: true,
            lineStyle: {
              width: 1,
              color: [
                [1, 'rgba(0,0,0,0)']
              ]
            }
          }, //仪表盘轴线
          pointer: {
            show: true,
            width: 3,
            length: "70%",

          },
          itemStyle: {
            color: '#367BEE',
            fontSize:14
          },
          axisTick: {
            show: true,
            splitNumber: 7,
            lineStyle: {
              color: '#367BEE',
              width: 1,
            },
            length: -8
          }, //刻度样式
          splitLine: {
            show: true,
            length: -20,
            lineStyle: {
              color: '#367BEE'
            }
          }, //分隔线样式
          axisLabel: {
            show: true,
            distance: 25,
            color: '#367BEE'
          }, //刻度标签。
          detail: {
            show: false,
          },
          data: [{
            value: this.rotation
          }]
        }, ]
      };
      myChart.setOption(option);
    },
    sstj(){
      var myChart = echarts.init(document.getElementById('sstj'));
      let label = [233, 233, 200, 180, 199, 233, 210, 180];
      let value = [233, 233, 200, 180, 199, 233, 210, 180];
      var option = {

        grid: {
          top: 30,
          left:10,
          bottom:20,
          right:20,
          containLabel: true,
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            lineStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: 'rgba(255,255,255,0)', // 0% 处的颜色
                  },
                  {
                    offset: 0.5,
                    color: 'rgba(255,255,255,1)', // 100% 处的颜色
                  },
                  {
                    offset: 1,
                    color: 'rgba(255,255,255,0)', // 100% 处的颜色
                  },
                ],
                global: false, // 缺省为 false
              },
            },
          },
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            axisLabel: {
              formatter: '{value}',
              fontSize: 14,
              margin: 20,
              textStyle: {
                color: '#404040',
              },
            },
            axisLine: {
              lineStyle: {
                color: '#243753',
              },
            },

            axisTick: {
              show: false,
            },
            data: this.dzObj.dates,
          },
        ],
        yAxis: [
          {
            boundaryGap: false,
            type: 'value',
            axisLabel: {
              textStyle: {
                color: '#999999',
              },
            },
            nameTextStyle: {
              color: '#fff',
              fontSize: 12,
              lineHeight: 40,
            },
            splitLine: {
              lineStyle: {
                color: '#243753',
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: '#283352',
              },
            },
            axisTick: {
              show: false,
            },
          },
        ],
        series: [
          {
            name: '吊重',
            type: 'line',
            smooth: true,
            showSymbol: true,
            symbolSize: 8,
            zlevel: 3,
            itemStyle: {
              color: '#19a3df',
              borderColor: '#a3c8d8',
            },
            lineStyle: {
              normal: {
                width: 6,
                color: '#19a3df',
              },
            },
            areaStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(
                  0,
                  0,
                  0,
                  1,
                  [
                    {
                      offset: 0,
                      color: 'rgba(88,255,255,0.2)',
                    },
                    {
                      offset: 0.8,
                      color: 'rgba(88,255,255,0)',
                    },
                  ],
                  false
                ),
              },
            },
            data: this.dzObj.dz,
          },
        ],
      };
      myChart.setOption(option);
    },
    baifbjs(a,b){
      if(a == undefined || b==undefined){
        return 0;
      }
      if( typeof(a /b) =='number' &&(a /b) != Infinity&&(a /b) != NaN){
        if((a/b) >100){
          return 100
        }else {
          return (a/b)*100
        }
      }else{
        return 0;
      }
    }
  }
}
</script>

<style scoped lang="scss">
.dom{
  width: 100%;
  height: 100%;
  font-size: 0.14rem;
  padding: 10px;
}
  .dom_l{
    display: inline-block;
    height: 100%;
    margin-right: 20px;
    width: 68.4%;
  }
  .dom_r{
    display: inline-block;
    height: 100%;
    width: calc(31.6% - 20px);
    box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.44);
    border-radius: 5px;
    float: right;
  }
  .dom_l_top{
    width: 100%;
    height: 65%;
    box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.44);
    border-radius: 5px;
    margin-bottom: 20px;
    padding: 15px 10px;
  }
  .dom_l_bottom{
    width: 100%;
    height: calc(34% - 10px);
    box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.44);
    border-radius: 5px;
    padding: 10px;
  }
  #sstj{
    width: 100%;
    height: calc(100% - 20px);;
  }
.subject {
  height: 15px;
  font-size: 0.16rem;
  font-weight: 500;
  color: #404040;
  display: flex;
  align-items: center;
  img {
    margin-right: 0.1rem;
    height: 0.16rem;
    width: 0.16rem;
  }
}
.tower_img_dom{
  width: 100%;
  height: calc(70% - 30px);
}
  .tower_img_dom_3{
    float: left;
    display: inline-block;
    width: 32.5%;
    padding: 40px 0 0 0;
    height: 100%;
  }
  .tower_img_dom_3_c{
    padding: 0 40px ;
    width: 100%;
    height: 100%;
    text-align: center;
  }
  .tower_img_dom_3_d{
    border-right: 2px solid #E1F0FF;
  }
  .tower_middle_10{
    height: 10%;
    padding-bottom: 15px;
    padding-left: 10px;
    text-align: left;
  }
.tower_middle_20{
  height: 20%;
  padding: 7px 10px;
}
.tower_qbc_dom{
  height: 100%;
  border: 1px solid rgba(100, 155, 248, 0.2);
  border-radius: 5px;
  line-height: 100%;
  background-color: rgba(52, 119, 255, 0.08);
  .tower_dom_text_m{
    color: #649BF8;
    font-size: 0.2rem;
    font-weight: 600;
  }
}
.lsbj{
  border-color: rgba(48, 191, 120, 0.22);;
  background-color: rgba(48, 191, 120, 0.08);;
  .tower_dom_text_m{
    color: #57CB91;
    font-size: 20px;
    font-weight: 600;
  }
}
.csbj{
  border-color: rgba(250, 173, 20, 0.2);
  background-color: rgba(250, 173, 20, 0.12);
  .tower_dom_text_m{
    color: #FF8F13;
    font-size: 20px;
    font-weight: 600;
  }
}
.hsbj{
  border-color: rgba(255, 52, 52, 0.08);
  background-color: rgba(100, 155, 248, 0.2);
  .tower_dom_text_m{
    color: #F4664A;
    font-size: 20px;
    font-weight: 600;
  }
}
  .tower_dom_text_l{
    display: inline-block;
    width: calc(20% - 4px);
    margin: auto 0px;
  }
  .tower_dom_text_m{
    display: inline-block;
    width: calc(40% - 4px);
    margin: auto 0px;
  }
  .tower_dom_text_r{
    display: inline-block;
    width: calc(40% - 4px);
    margin: auto 0px;
  }
#triangle-up {
  width: 0;
  height: 0;
  border-left: 6px solid transparent;
  border-right: 6px solid transparent;
  border-bottom: 12px solid #30BF78;
}
#triangle-up-2 {
  width: 0;
  height: 0;
  border-left: 6px solid transparent;
  border-right: 6px solid transparent;
  border-bottom: 12px solid #649BF8;
}
  .tower_text_1{
    display: inline-block;
    width: calc(10% - 4px);
  }
  .tower_text_3{
    display: inline-block;
    width: calc(30% - 4px);
  }
.tower_text_6{
  display: inline-block;
  width: calc(60% - 4px);
  color: #3477FF;
}
  .tower_jd_dom{
    height: 50%;
    padding: 0 40px;
    width: 100%;
  }
  .tower_jd_dom_3{
    height: 28%;
    width: 100%;
    padding: 7px 10px;
  }
.tower_jd_dom_35{
  height: 36%;
  width: 100%;
  padding: 7px 10px;
}
  .tower_jd_text_2{
    width:100%;
  }
.tower_jd_text_3{
  display: inline-block;
/*  width:calc(38% - 7px);*/
}
.tower_jd_text_4{
  display: inline-block;
/*  width:calc(62% - 7px);*/
}
.tower_jd_bor{
  width: 100%;
  height: 100%;
  border: 1px solid #D3E3FE;
  border-radius: 4px;
  background-color: #EFF5FF;
  padding: 0 10px;
}
  .tower_jd_bor_img{
    width: 30px;
    display: inline-block;
  }
  .tower_jd_bor_imgt{
    width:calc(95% - 80px);
    display: inline-block;
  }
  .tower_jd_bor_imgt2{
    width:calc(47% - 40px);
    display: inline-block;
  }
  .tower_jd_bor_imgt3{
    width:calc(47% - 40px);
    display: inline-block;
  }
.tower_jd_bor_imgr{
  width:50px;
  display: inline-block;
  text-align: right;
  border-left: 1px solid #B3CBF4;
}
  .tower_data_dom{
    width: 100%;
    height: calc(30% - 10px);
    padding: 0.1rem;
    margin-top: 0.2rem;
  }
  .tower_data_dom2{
    border: 1px solid #D3E3FE;
    height: 100%;
  }
  .tower_data_6{
    display: inline-block;
    width: 16%;
    height: 100%;
    padding: 0.15rem;
  }
  .tower_data_img{
    display: inline-block;
    width: 47%;
  }
.tower_data_text{
  display: inline-block;
  width: 47%;
  margin: auto 0;
  text-align: center;
}
  .tower_data_text1{
    font-size: 0.18rem;
    font-weight: 600;
    margin: 3px 0;
  }
  .tower_data_jdt{
    margin-top: 0.18rem;
  }
  .yxzt{
    height: calc(20% - 15px);
    margin: 15px;
    border-bottom: 1px solid #D3E3FE;
  }
  .yxzt_dom{
    width: 100%;
    padding: 0.25rem;
    p{
      margin-top: 0.15rem;
      .gzsc{
        display: inline-block;
        width: 45%;
      }
      .jrdz{
        display: inline-block;
        width: 50%;
      }
    }
  }
  .sjxx{
    height: calc(25% - 15px);
    margin: 15px;
    border-bottom: 1px solid #D3E3FE;
  }
  .sbxx{
    height:calc(55% - 30px);
    margin: 15px;
  }
  .sjxx_dom{
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .sjxx_img{
    display: inline-block;
    width: 35%;
    height: 100%;
    img{
			width: 100%;
			height: auto;
    }
  }
  .sjxx_text{
    display: inline-block;
    width: 58%;
    p{
      margin-bottom: 0.1rem;
    }
  }
  .sbxx_dom{
    padding: 0.25rem;
    p{
      margin-top: 0.15rem;
      .sbxx_text_l{
        display: inline-block;
        width: 30%;
      }
      .sbxx_text_r{
        display: inline-block;
        width: 65%;
        text-align: right;
      }
    }
  }
  /deep/ .el-input--suffix{
    line-height:30px;
    height:30px;
    input{
      height: 20px;
    }
  }
</style>
