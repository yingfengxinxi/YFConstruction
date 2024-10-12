<template>
  <div class="content_box">
    <div class="con_left">
      <div class="left_top">
        <div class="cqfl" >
          <div style="margin-top:0.2rem;">项目总工程师</div>
          <div  id="type1" style="height:60%; width: 100%;"></div>
          <div>出勤率97%</div>
        </div>
        <div class="cqfl" >
          <div style="margin-top:0.2rem;">项目经理</div>
          <div  id="type2" style="height:60%; width: 100%;"></div>
          <div>出勤率100%</div>
        </div>
        <div class="cqfl">
          <div style="margin-top:0.2rem;">工程经理</div>
          <div  id="type3" style="height:60%; width: 100%;"></div>
          <div>出勤率95%</div>
        </div>
        <div class="cqfl">
          <div style="margin-top:0.2rem;">施工员</div>
          <div  id="type4" style="height:60%; width: 100%;"></div>
          <div>出勤率89%</div>
        </div>
        <div class="cqfl">
          <div style="margin-top:0.2rem;">质量员</div>
          <div  id="type5" style="height:60%; width: 100%;"></div>
          <div>出勤率87%</div>
        </div>
        <div class="cqfl">
          <div style="margin-top:0.2rem;">安全员</div>
          <div  id="type6" style="height:60%; width: 100%;"></div>
          <div>出勤率93%</div>
        </div>
        <div class="cqfl">
          <div style="margin-top:0.2rem;">材料员</div>
          <div  id="type7" style="height:60%; width: 100%;"></div>
          <div>出勤率92%</div>
        </div>
      </div>

      <div class="left_btton">
        <div class="qql_police">
          <div class="model_title">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png"/>
            <div>缺勤率报警</div>
          </div>
          <div class="police_list">
            <div class="police_list_title">
              <div style="width: 10%;">序号</div>
              <div style="width: 50%; ">项目名称</div>
              <div style="width: 20%;">人员</div>
              <div style="width: 20%;">缺勤率</div>
              <!-- <div style="width: 20%;">处理时间</div> -->
            </div>
            <div class="prolice_list_conbox">
              <div class="police_list_con" v-for="item in datalist2">
                <div style="width: 10%">
                  <span style="width: 18px;width: 18px;height: 18px;background: #649BF8;border-radius: 4px;display: block;margin: 0 auto" >
                    <p style="color: #FFFFFF"> {{item.xh}}</p>
                  </span>
                </div>
                <div style="width: 50%;line-height: 0.4rem;">{{item.projectName}}</div>
                <div style="width: 20%;line-height: 0.4rem;">{{item.perName}}</div>
                <div style="width: 20%;line-height: 0.4rem;">{{item.qql}}</div>
                <!-- <div style="width: 20%;">{{item.handleTime}}</div> -->
              </div>
            </div>
          </div>
        </div>
        <div class="bot_right">
          <div class="br_t">
            <div class="model_title">
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png"/>
              <div>缺勤率报警</div>
            </div>
            <div id="qql" style="height:80%; width: 100%;">

            </div>
          </div>
          <div class="br_b">
            <div class="model_title">
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png"/>
              <div>近30天出勤率</div>
            </div>
            <div id="cql" style="height:80%; width: 100%;">

            </div>
          </div>
        </div>
      </div>

    </div>
    <div class="con_right">
      <div class="model_title">
        <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png"/>
        <div>实时动态</div>
      </div>
      <div class="model_title_right">
        <span class="ssdt_children_under_span_red"></span> 进场
        <span class="ssdt_children_under_span_blue" style="margin-left: 0.2rem"></span>离场
      </div>
      <div class="ssdt">
        <!-- 因静态地址图片加载有问题故先注掉等待后续正式数据拼接后解除-->
<!--        <div class="ssdt_left" v-for="item in datalist">
          <div class="ssdt_children_left">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="ssdt_children_left_img" src="../../../../assets/img/projectAnalyse/tx1.png"/>
          </div>
          <div class="ssdt_children_regit">
            <span class="ssdt_children_regit_span">{{item.projectName}}</span>
            <span class="ssdt_children_regit_span" >姓名:{{item.perName}}</span>
            <span class="ssdt_children_regit_span" style="font-weight: bold">{{item.job}}</span>
          </div>
          <div class="ssdt_children_under">
            <span :class="item.jcc==1?'ssdt_children_under_span_red':'ssdt_children_under_span_blue'"></span>
            <span> {{item.timeDate}}</span>
          </div>
        </div>-->
        <!-- 演示用正式版删除并解除上方注释后在进行对接-->
        <div class="ssdt_left" >
          <div class="ssdt_children_left">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="ssdt_children_left_img" src="../../../../assets/img/projectAnalyse/tx1.png"/>
          </div>
          <div class="ssdt_children_regit">
            <span class="ssdt_children_regit_span">地鑫新河花园项目</span>
            <span class="ssdt_children_regit_span" >姓名:柳传志</span>
            <span class="ssdt_children_regit_span" style="font-weight: bold">质量员</span>
          </div>
          <div class="ssdt_children_under">
            <span class="ssdt_children_under_span_red"></span>
            <span> 2021-11-29 08:22:20</span>
          </div>
        </div>
        <div class="ssdt_left" >
          <div class="ssdt_children_left">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="ssdt_children_left_img" src="../../../../assets/img/projectAnalyse/tx2.png"/>
          </div>
          <div class="ssdt_children_regit">
            <span class="ssdt_children_regit_span">幸福小区项目</span>
            <span class="ssdt_children_regit_span" >姓名:钱兴天</span>
            <span class="ssdt_children_regit_span" style="font-weight: bold">工程经理</span>
          </div>
          <div class="ssdt_children_under">
            <span class="ssdt_children_under_span_red"></span>
            <span> 2021-11-29 07:39:51</span>
          </div>
        </div>
        <div class="ssdt_left" >
          <div class="ssdt_children_left">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="ssdt_children_left_img" src="../../../../assets/img/projectAnalyse/tx3.png"/>
          </div>
          <div class="ssdt_children_regit">
            <span class="ssdt_children_regit_span">德百玫瑰公馆A区项目</span>
            <span class="ssdt_children_regit_span" >姓名:周明超</span>
            <span class="ssdt_children_regit_span" style="font-weight: bold">项目经理</span>
          </div>
          <div class="ssdt_children_under">
            <span class="ssdt_children_under_span_red"></span>
            <span> 2021-11-29 07:21:33</span>
          </div>
        </div>
        <div class="ssdt_left" >
          <div class="ssdt_children_left">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="ssdt_children_left_img" src="../../../../assets/img/projectAnalyse/tx4.png"/>
          </div>
          <div class="ssdt_children_regit">
            <span class="ssdt_children_regit_span">富力城项目二期</span>
            <span class="ssdt_children_regit_span" >姓名:张森林</span>
            <span class="ssdt_children_regit_span" style="font-weight: bold">项目总工程师</span>
          </div>
          <div class="ssdt_children_under">
            <span class="ssdt_children_under_span_blue"></span>
            <span> 2021-11-25 15:24:12</span>
          </div>
        </div>
        <div class="ssdt_left" >
          <div class="ssdt_children_left">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="ssdt_children_left_img" src="../../../../assets/img/projectAnalyse/tx5.png"/>
          </div>
          <div class="ssdt_children_regit">
            <span class="ssdt_children_regit_span">丰润花园项目</span>
            <span class="ssdt_children_regit_span" >姓名:王先奇</span>
            <span class="ssdt_children_regit_span" style="font-weight: bold">项目总工程师</span>
          </div>
          <div class="ssdt_children_under">
            <span class="ssdt_children_under_span_red"></span>
            <span> 2021-11-29 08:11:10</span>
          </div>
        </div>
        <div class="ssdt_left" >
          <div class="ssdt_children_left">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="ssdt_children_left_img" src="../../../../assets/img/projectAnalyse/tx6.png"/>
          </div>
          <div class="ssdt_children_regit">
            <span class="ssdt_children_regit_span">丰润花园项目</span>
            <span class="ssdt_children_regit_span" >姓名:刘浩</span>
            <span class="ssdt_children_regit_span" style="font-weight: bold">施工员</span>
          </div>
          <div class="ssdt_children_under">
            <span class="ssdt_children_under_span_blue"></span>
            <span> 2021-11-23 19:31:20</span>
          </div>
        </div>
        <div class="ssdt_left" >
          <div class="ssdt_children_left">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="ssdt_children_left_img" src="../../../../assets/img/projectAnalyse/tx7.png"/>
          </div>
          <div class="ssdt_children_regit">
            <span class="ssdt_children_regit_span">丰润花园项目</span>
            <span class="ssdt_children_regit_span" >姓名:张思源</span>
            <span class="ssdt_children_regit_span" style="font-weight: bold">材料员</span>
          </div>
          <div class="ssdt_children_under">
            <span class="ssdt_children_under_span_red"></span>
            <span> 2021-11-29 08:44:50</span>
          </div>
        </div>
        <div class="ssdt_left" >
          <div class="ssdt_children_left">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="ssdt_children_left_img" src="../../../../assets/img/projectAnalyse/tx8.png"/>
          </div>
          <div class="ssdt_children_regit">
            <span class="ssdt_children_regit_span">幸福小区项目</span>
            <span class="ssdt_children_regit_span" >姓名:王德伟</span>
            <span class="ssdt_children_regit_span" style="font-weight: bold">质量员</span>
          </div>
          <div class="ssdt_children_under">
            <span class="ssdt_children_under_span_red"></span>
            <span> 2021-11-29 08:58:02</span>
          </div>
        </div>
        <div class="ssdt_left" >
          <div class="ssdt_children_left">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="ssdt_children_left_img" src="../../../../assets/img/projectAnalyse/tx9.png"/>
          </div>
          <div class="ssdt_children_regit">
            <span class="ssdt_children_regit_span">德百玫瑰公馆A区项目</span>
            <span class="ssdt_children_regit_span" >姓名:付强</span>
            <span class="ssdt_children_regit_span" style="font-weight: bold">安全员</span>
          </div>
          <div class="ssdt_children_under">
            <span class="ssdt_children_under_span_red"></span>
            <span> 2021-11-29 08:44:21</span>
          </div>
        </div>
        <div class="ssdt_left" >
          <div class="ssdt_children_left">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="ssdt_children_left_img" src="../../../../assets/img/projectAnalyse/tx10.png"/>
          </div>
          <div class="ssdt_children_regit">
            <span class="ssdt_children_regit_span">地鑫新河花园项目</span>
            <span class="ssdt_children_regit_span" >姓名:孙达兴</span>
            <span class="ssdt_children_regit_span" style="font-weight: bold">材料员</span>
          </div>
          <div class="ssdt_children_under">
            <span class="ssdt_children_under_span_blue"></span>
            <span> 2021-11-24 18:10:02</span>
          </div>
        </div>
        <div class="ssdt_left" >
          <div class="ssdt_children_left">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="ssdt_children_left_img" src="../../../../assets/img/projectAnalyse/tx11.png"/>
          </div>
          <div class="ssdt_children_regit">
            <span class="ssdt_children_regit_span">富力城项目二期</span>
            <span class="ssdt_children_regit_span" >姓名:朱孟贵</span>
            <span class="ssdt_children_regit_span" style="font-weight: bold">质量员</span>
          </div>
          <div class="ssdt_children_under">
            <span class="ssdt_children_under_span_blue"></span>
            <span> 2021-11-25 20:58:02</span>
          </div>
        </div>
        <div class="ssdt_left" >
          <div class="ssdt_children_left">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="ssdt_children_left_img" src="../../../../assets/img/projectAnalyse/tx12.png"/>
          </div>
          <div class="ssdt_children_regit">
            <span class="ssdt_children_regit_span">宏业大厦项目</span>
            <span class="ssdt_children_regit_span" >姓名:李子明</span>
            <span class="ssdt_children_regit_span" style="font-weight: bold">项目经理</span>
          </div>
          <div class="ssdt_children_under">
            <span class="ssdt_children_under_span_red"></span>
            <span> 2021-11-29 07:11:02</span>
          </div>
        </div>
        <!-- 演示用正式版删除并解除上方注释后在进行对接-->



      </div>
    </div>
  </div>
</template>

<script>
  import * as echarts from 'echarts';
  export default {
    data(){
       return{
        datalist:[
          {
            projectName: '富力城二期',
            perName: '杨树林',
            job: '项目总工',
            jcc: '1',
            image:'tx1.png',
            timeDate:'2021-09-12 18:19:20'
          },
          {
            projectName: '富力城二期',
            perName: '杨树林',
            job: '项目总工',
            jcc: '2',
            image:'tx1.png',
            timeDate:'2021-09-12 18:19:20'
          }
        ],
        datalist2:[
          {
            projectName: '丰润花园项目',
            perName: '刘浩',
            xh: '1',
            qql:'10%'
          },
          {
            projectName: '宏业大厦项目',
            perName: '刘子涵',
            xh: '2',
            qql:'9%'
          },
          {
            projectName: '地鑫新河花园项目',
            perName: '孙达兴',
            xh: '3',
            qql:'9%'
          },
          {
            projectName: '富力城项目二期',
            perName: '朱孟贵',
            xh: '4',
            qql:'7%'
          },
          {
            projectName: '德百玫瑰公馆A区项目',
            perName: '吴兴业',
            xh: '5',
            qql:'5%'
          },
          {
            projectName: '富力城项目二期',
            perName: '张森林',
            xh: '6',
            qql:'5%'
          },
          {
            projectName: '宏业大厦项目',
            perName: '王思明',
            xh: '7',
            qql:'5%'
          },
          {
            projectName: '德百玫瑰公馆A区项目',
            perName: '付强',
            xh: '8',
            qql:'4%'
          },
          {
            projectName: '幸福小区项目',
            perName: '王德伟',
            xh: '9',
            qql:'4%'
          },
          {
            projectName: '丰润花园项目',
            perName: '张思源',
            xh: '10',
            qql:'3%'
          },
          {
            projectName: '丰润花园项目',
            perName: '王先奇',
            xh: '11',
            qql:'2%'
          },
          {
            projectName: '德百玫瑰公馆A区项目',
            perName: '周明超',
            xh: '12',
            qql:'1%'
          }
        ]
      }
    },
    mounted(){
      this.remsize()
      this.qql()
      this.cql ()
      this.type1()
      this.type2()
      this.type3()
      this.type4()
      this.type5()
      this.type6()
      this.type7()
    },
    created() {

    },
    methods:{
      remsize() {
        function setRemFontSize() {
          var remSize = window.innerWidth / 19.2;
          document.querySelector("html").style.fontSize = remSize + "px";
        }
        setRemFontSize();
        window.addEventListener("resize", function() {
          setTimeout(function() {
            setRemFontSize();
          }, 200)
        });
      },
      // 缺勤率图表
      qql ()  {
        let xAxisData = ['丰润花园项目','宏业大厦项目','地鑫新河花园项目','富力城项目二期','幸福小区项目','德百玫瑰公馆A区项目']
        let seriesData = ['6','12','5','14','8','11']
        var myChart = echarts.init(document.getElementById('qql'));
        let option = {
          tooltip: {
            trigger: "axis",
            transitionDuration: 0, //防止页面抖动
          },
          legend: {
            textStyle: {
              color: "#000",
              fontSize: 10
            },
            itemWidth: 10,
            itemHeight: 10,

          },
          grid: {
            right: "7%",
            left: "7%",
            top: '15%',
            bottom: '20%'
          },
          xAxis: {

            data: xAxisData,
            type: 'category',
            axisLine: {
              show: false,
              color: '#A3E9FC'
            },

            axisLabel: {
              color: '#000',
              formatter: function (params) {
                var newParamsName = '' // 最终拼接成的字符串
                var paramsNameNumber = params.length // 实际标签的个数
                var provideNumber = 4 // 每行能显示的字的个数
                var rowNumber = Math.ceil(paramsNameNumber / provideNumber) // 换行的话，需要显示几行，向上取整
                /**
                 * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
                 */
                // 条件等同于rowNumber>1
                if (paramsNameNumber > provideNumber) {
                  /** 循环每一行,p表示行 */
                  for (var p = 0; p < rowNumber; p++) {
                    var tempStr = '' // 表示每一次截取的字符串
                    var start = p * provideNumber // 开始截取的位置
                    var end = start + provideNumber // 结束截取的位置
                    // 此处特殊处理最后一行的索引值
                    if (p === rowNumber - 1) {
                      // 最后一次不换行
                      tempStr = params.substring(start, paramsNameNumber)
                    } else {
                      // 每一次拼接字符串并换行
                      tempStr = params.substring(start, end) + '\n'
                    }
                    newParamsName += tempStr // 最终拼成的字符串
                  }
                } else {
                  // 将旧标签的值赋给新标签
                  newParamsName = params
                }
                return newParamsName
              }

            },
            splitLine: {
              show: false,
            },

          },
          yAxis: {
            nameTextStyle: {
              color: "#000",
            },
            type: 'value',
            scale: true,
            splitLine: {
              show: true,
              lineStyle: {
                color: '#000',
                opacity: 0.2
              }
            },
            boundaryGap: true,
            axisLine: {
              show: false,
              color: 'A3E9FC'
            },
            axisLabel: {
              show: true,

              textStyle: {
                color: '#000'
              }
            },
            axisTick: {
              show: false
            }
          },
          series: [{
            type: 'bar',
           barWidth: '10',
            itemStyle: {
              normal: {
                color: '#5B97F9',
              }
            },
            data: seriesData
          },]
        };
        myChart.setOption(option)
      },
      cql () { //24小时空气质量数据
        let xAxisData = ['丰润花园项目','宏业大厦项目','地鑫新河花园项目','富力城项目二期','幸福小区项目','德百玫瑰公馆A区项目']
        let seriesData = ['92','86','94','89','96','85']
/*        for (let i = 0; i < data.length; i++) {
          xAxisData.push(data[i].hourNumber + '时')
          PM2Count.push(data[i].PM2Count)
          PM10Count.push(data[i].PM10Count)
        }*/
        let myChart = echarts.init(document.getElementById('cql'));
        let option = {
          tooltip: {
            trigger: "axis",
            transitionDuration: 0, //防止页面抖动
          },

          grid: {
            right: "7%",
            left: "7%",
            top: '15%',
            bottom: '20%'
          },
          legend: {
            icon: "circle",
            data: ['PM2.5'],
            itemWidth: 10,
            itemHeight: 10,
            textStyle: {
              color: "#000",
              fontSize: 10
            },
          },
          xAxis: {
            data: xAxisData,
            type: 'category',
            axisLine: {
              show: false,
              color: '#A3E9FC'
            },

            axisLabel: {
              color: '#000',
              formatter: function (params) {
                var newParamsName = '' // 最终拼接成的字符串
                var paramsNameNumber = params.length // 实际标签的个数
                var provideNumber = 4 // 每行能显示的字的个数
                var rowNumber = Math.ceil(paramsNameNumber / provideNumber) // 换行的话，需要显示几行，向上取整
                /**
                 * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
                 */
                // 条件等同于rowNumber>1
                if (paramsNameNumber > provideNumber) {
                  /** 循环每一行,p表示行 */
                  for (var p = 0; p < rowNumber; p++) {
                    var tempStr = '' // 表示每一次截取的字符串
                    var start = p * provideNumber // 开始截取的位置
                    var end = start + provideNumber // 结束截取的位置
                    // 此处特殊处理最后一行的索引值
                    if (p === rowNumber - 1) {
                      // 最后一次不换行
                      tempStr = params.substring(start, paramsNameNumber)
                    } else {
                      // 每一次拼接字符串并换行
                      tempStr = params.substring(start, end) + '\n'
                    }
                    newParamsName += tempStr // 最终拼成的字符串
                  }
                } else {
                  // 将旧标签的值赋给新标签
                  newParamsName = params
                }
                return newParamsName
              }

            },
            splitLine: {
              show: false,
            },

          },
          yAxis: {
            nameTextStyle: {
              color: "#000",
            },
            type: 'value',
            splitLine: {
              show: true,
              lineStyle: {
                color: '#000',
                opacity: 0.2
              }
            },
            boundaryGap: true,
            axisLine: {
              show: false,
              color: 'A3E9FC'
            },
            axisLabel: {
              show: true,
              margin: 10,
              textStyle: {
                color: '#000'
              }
            },
            axisTick: {
              show: false
            }
          },
          series: [{
            type: 'line',
            smooth: true,
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: '#30BF78'
              },
                {
                  offset: 0.8,
                  color: 'rgba(255, 255, 255, 0)'
                }
              ])
            },
            data: seriesData,
            lineStyle: {
              normal: {
                color: "#30BF78",
              },
            },
            itemStyle: {
              color: "#30BF78",
              borderColor: "#30BF78",
              borderWidth: 2,
            },
          }]
        };
        myChart.setOption(option)
      },
      type1(){//报警类型占比
        let myChart = echarts.init(document.getElementById('type1'));
        let option = {
          title: {
            text: '3',
            left:'center',
            top:'center'
          },
          tooltip: {
            trigger: 'item'
          },
          color:['#5B96FA','#A89FFF', '#F57D65', '#FAAD14', '#FA70FF', '#58C4EB', '#57CB91', '#FDEB62', '#FF5353','#77B8F8'],
          series: [
            {
              name: '考勤数据',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              label: {
                normal: {
                  show: true,
                  position: 'outside',
                  formatter: '{c}',
                },
                emphasis: {    //文本样式
                  show: true,    //展示
                  textStyle: {    //文本样式
                    fontSize: '10',
                    fontWeight: '100',
                  }
                }
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '40',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: true,
                normal:{
                  length:1
                }
              },
              data: [
                { value: 2, name: '出勤',itemStyle: {color: '#649BF8'} },
                { value: 1, name: '缺勤',itemStyle: {color: '#BBD1F8'} }
              ]
            }
          ]
        };
        myChart.setOption(option)
      },
      type2(){//报警类型占比
        let myChart = echarts.init(document.getElementById('type2'));
        let option = {
          title: {
            text: '3',
            left:'center',
            top:'center'
          },
          tooltip: {
            trigger: 'item'
          },
          color:['#5B96FA','#A89FFF', '#F57D65', '#FAAD14', '#FA70FF', '#58C4EB', '#57CB91', '#FDEB62', '#FF5353','#77B8F8'],
          series: [
            {
              name: '考勤数据',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              label: {
                normal: {
                  show: true,
                  position: 'outside',
                  formatter: '{c}',
                },
                emphasis: {    //文本样式
                  show: true,    //展示
                  textStyle: {    //文本样式
                    fontSize: '10',
                    fontWeight: '100',
                  }
                }
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '40',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: true,
                normal:{
                  length:1
                }
              },
              data: [
                { value: 3, name: '出勤',itemStyle: {color: '#30BF78'} },
                { value: 0, name: '缺勤',itemStyle: {color: '#A0E9C5'} }
              ]
            }
          ]
        };
        myChart.setOption(option)
      },
      type3(){//报警类型占比
        let myChart = echarts.init(document.getElementById('type3'));
        let option = {
          title: {
            text: '2',
            left:'center',
            top:'center'
          },
          tooltip: {
            trigger: 'item'
          },
          color:['#5B96FA','#A89FFF', '#F57D65', '#FAAD14', '#FA70FF', '#58C4EB', '#57CB91', '#FDEB62', '#FF5353','#77B8F8'],
          series: [
            {
              name: '考勤数据',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              label: {
                normal: {
                  show: true,
                  position: 'outside',
                  formatter: '{c}',
                },
                emphasis: {    //文本样式
                  show: true,    //展示
                  textStyle: {    //文本样式
                    fontSize: '10',
                    fontWeight: '100',
                  }
                }
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '40',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: true,
                normal:{
                  length:1
                }
              },
              data: [
                { value: 1, name: '出勤',itemStyle: {color: '#F86464'} },
                { value: 1, name: '缺勤',itemStyle: {color: '#FFD0D0'} }
              ]
            }
          ]
        };
        myChart.setOption(option)
      },
      type4(){//报警类型占比
        let myChart = echarts.init(document.getElementById('type4'));
        let option = {
          title: {
            text: '2',
            left:'center',
            top:'center'
          },
          tooltip: {
            trigger: 'item'
          },
          color:['#5B96FA','#A89FFF', '#F57D65', '#FAAD14', '#FA70FF', '#58C4EB', '#57CB91', '#FDEB62', '#FF5353','#77B8F8'],
          series: [
            {
              name: '考勤数据',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              label: {
                normal: {
                  show: true,
                  position: 'outside',
                  formatter: '{c}',
                },
                emphasis: {    //文本样式
                  show: true,    //展示
                  textStyle: {    //文本样式
                    fontSize: '10',
                    fontWeight: '100',
                  }
                }
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '40',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: true,
                normal:{
                  length:1
                }
              },
              data: [
                { value: 0, name: '出勤',itemStyle: {color: '#FAAD14'} },
                { value: 2, name: '缺勤',itemStyle: {color: '#FFE0A3'} }
              ]
            }
          ]
        };
        myChart.setOption(option)
      },
      type5(){//报警类型占比
        let myChart = echarts.init(document.getElementById('type5'));
        let option = {
          title: {
            text: '3',
            left:'center',
            top:'center'
          },
          tooltip: {
            trigger: 'item'
          },
          color:['#5B96FA','#A89FFF', '#F57D65', '#FAAD14', '#FA70FF', '#58C4EB', '#57CB91', '#FDEB62', '#FF5353','#77B8F8'],
          series: [
            {
              name: '考勤数据',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              label: {
                normal: {
                  show: true,
                  position: 'outside',
                  formatter: '{c}',
                },
                emphasis: {    //文本样式
                  show: true,    //展示
                  textStyle: {    //文本样式
                    fontSize: '10',
                    fontWeight: '100',
                  }
                }
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '40',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: true,
                normal:{
                  length:1
                }
              },
              data: [
                { value: 2, name: '出勤',itemStyle: {color: '#A89FFF'}  },
                { value: 1, name: '缺勤',itemStyle: {color: '#D6D2FF'}  }
              ]
            }
          ]
        };
        myChart.setOption(option)
      },
      type6(){//报警类型占比
        let myChart = echarts.init(document.getElementById('type6'));
        let option = {
          title: {
            text: '3',
            left:'center',
            top:'center'
          },
          tooltip: {
            trigger: 'item'
          },
          color:['#5B96FA','#A89FFF', '#F57D65', '#FAAD14', '#FA70FF', '#58C4EB', '#57CB91', '#FDEB62', '#FF5353','#77B8F8'],
          series: [
            {
              name: '考勤数据',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              label: {
                normal: {
                  show: true,
                  position: 'outside',
                  formatter: '{c}',
                },
                emphasis: {    //文本样式
                  show: true,    //展示
                  textStyle: {    //文本样式
                    fontSize: '10',
                    fontWeight: '100',
                  }
                }
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '40',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: true,
                normal:{
                  length:1
                }
              },
              data: [
                { value: 3, name: '出勤',itemStyle: {color: '#FF6100'}  },
                { value: 0, name: '缺勤',itemStyle: {color: '#FFCFB2'}  }
              ]
            }
          ]
        };
        myChart.setOption(option)
      },
      type7(){//报警类型占比
        let myChart = echarts.init(document.getElementById('type7'));
        let option = {
          title: {
            text: '2',
            left:'center',
            top:'center'
          },
          tooltip: {
            trigger: 'item'
          },
          color:['#5B96FA','#A89FFF', '#F57D65', '#FAAD14', '#FA70FF', '#58C4EB', '#57CB91', '#FDEB62', '#FF5353','#77B8F8'],
          series: [
            {
              name: '考勤数据',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              label: {
                normal: {
                  show: true,
                  position: 'outside',
                  formatter: '{c}',
                },
                emphasis: {    //文本样式
                  show: true,    //展示
                  textStyle: {    //文本样式
                    fontSize: '10',
                    fontWeight: '100',
                  }
                }
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '40',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: true,
                normal:{
                  length:1
                }
              },
              data: [
                { value: 1, name: '出勤',itemStyle: {color: '#73CDF6'} },
                { value: 1, name: '缺勤',itemStyle: {color: '#BDEAFF'} }
              ]
            }
          ]
        };
        myChart.setOption(option)
      },
    }
  }

</script>

<style scoped>
.content_box{
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-evenly;
}
  .con_left{
    width: 12.17rem;
    margin-top: 0.23rem;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    height: 94.85%;
  }
  .con_right{
    width: 3.93rem;
    height: 94.85%;
    background: #FFFFFF;
    box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
    border-radius: 5px;
    margin-top: 0.23rem;
    position: relative;
  }
  .left_top{
    width: 100%;
    height: 26.05%;
    background: #FFFFFF;
    box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
    border-radius: 5px;
  }
  .left_btton{
    width: 12.17rem;
    height: 71.76%;
    display: flex;
    justify-content: space-between;
  }
  .qql_police{
    width: 4.46rem;
    height: 100%;
    background: #FFFFFF;
    box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
    border-radius: 5px;
  }
  .bot_right{
    width: 7.51rem;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
  .br_t{
     height: 48.4%;
    width: 100%;
    background: #FFFFFF;
    box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
    border-radius: 5px;
   }
.br_b{
  height: 48.4%;
  width: 100%;
  background: #FFFFFF;
  box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
  border-radius: 5px;
}
.model_title{
  height: 0.4rem;
  display: flex;
  align-items: center;
}
.model_title img{
  width: 0.16rem;
  height: 0.16rem;
  margin-left: 0.2rem;
  margin-right: 0.1rem;
}
.model_title div{
  font-size: 0.16rem;
  color: #000000;
}
  .ssdt{
    height: calc(100% - 0.4rem);
    overflow-y: auto;
  }
  .ssdt::-webkit-scrollbar {
  display: none;
  /* Chrome Safari */
}

.ssdt {
  scrollbar-width: none;
  /* firefox */
  -ms-overflow-style: none;
  /* IE 10+ */
}
  .cqfl{
    width: 1.738rem;
    height: 100%;
    float:left;
    align-items:center;
    text-align: center;
  }
  .ssdt_left{
    width: 1.7rem;
    height: 14.6%;
    margin-left: 0.2rem;
    display:inline-block;
    margin-top: 0.1rem;
  }
  .ssdt_children_left{
    width: 45%;
    height: 78%;
    float:left;
  }
  .ssdt_children_regit{
    width: 53%;
    height: 78%;
    float:right;

  }
  .ssdt_children_under{
    width: 100%;
    height: 20%;
    clear: both;
    text-align: center;
  }
  .ssdt_children_regit_span{
    width: 100%;
    height: 0.17rem;
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #404040;
    line-height: 0.17rem;
    margin-top: 0.08rem;
    float:right;
    white-space: nowrap;/*设置不换行*/
    overflow: hidden; /*设置隐藏*/
    text-overflow: ellipsis; /*设置隐藏部分为省略号*/
  }
  .ssdt_children_left_img{
    width: 100%;
    height:100%;
  }
  .ssdt_children_under_span_red{
     width:0.08rem;
     background-color :red;
     border-radius: 50%;
     height:0.08rem;
     display: inline-block;
   }
  .ssdt_children_under_span_blue{
    width:0.08rem;
    background-color :blue;
    border-radius: 50%;
    height:0.08rem;
    display: inline-block;
  }
  .model_title_right{
    position:absolute;
    right:0.4rem;
    top:0.1rem;
  }
  .police_list_box{
    width: 8.06rem;
    height:31.1%;
    background: #FFFFFF;
    box-shadow: 0px 0px 10px 0px rgba(145,180,241,0.29);
    border-radius: 5px;
    position: relative;
  }
  .police_list{
    height: 90%;
    width: 100%;
    margin-top:0.2rem;
  }
  .police_list_title{
    width: 4.46rem;
    margin: 0 auto;
    height: 0.37rem;
    border-bottom: 1px solid #286DFF;
    line-height: 0.37rem;
    color: #286DFF;
    font-size: 0.14rem;
    display: flex;
  }
  .police_list_title div{
    text-align: center;
  }
  .prolice_list_conbox{
    width: 4.46rem;
    margin: 0 auto;
    height: 90%;
    overflow-y: auto;
  }

  .prolice_list_conbox::-webkit-scrollbar {
    display: none;
    /* Chrome Safari */
  }
  .prolice_list_conbox {
    scrollbar-width: none;
    /* firefox */
    -ms-overflow-style: none;
    /* IE 10+ */
  }
  .police_list_con{
    display: flex;
    align-items: center;
  }
  .police_list_con div{
    text-align: center;
    color: #666666;
    font-size: 0.14rem;
    height: 0.4rem;
    text-align: center;
    align-items: center;
    display: flex;
    justify-content: center;
  }
.police_list_con:nth-of-type(even){
  background: #F2F8FF;
}
</style>
