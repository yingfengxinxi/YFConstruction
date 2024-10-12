<template>
  <div class="content_box">
    <div class="con_left">
      <div class="left_top">
        <div class="cqfl" >
          <div style="margin-top:0.2rem;">项目总工程师</div>
          <div  id="type1" style="height:60%; width: 100%;"></div>
          <div>出勤率{{xmzgcscql}}%</div>
        </div>
        <div class="cqfl" >
          <div style="margin-top:0.2rem;">项目经理</div>
          <div  id="type2" style="height:60%; width: 100%;"></div>
          <div>出勤率{{xmjlcql}}%</div>
        </div>
        <div class="cqfl">
          <div style="margin-top:0.2rem;">工程经理</div>
          <div  id="type3" style="height:60%; width: 100%;"></div>
          <div>出勤率{{gcjlcql}}%</div>
        </div>
        <div class="cqfl">
          <div style="margin-top:0.2rem;">施工员</div>
          <div  id="type4" style="height:60%; width: 100%;"></div>
          <div>出勤率{{sgycql}}%</div>
        </div>
        <div class="cqfl">
          <div style="margin-top:0.2rem;">质量员</div>
          <div  id="type5" style="height:60%; width: 100%;"></div>
          <div>出勤率{{zlycql}}%</div>
        </div>
        <div class="cqfl">
          <div style="margin-top:0.2rem;">安全员</div>
          <div  id="type6" style="height:60%; width: 100%;"></div>
          <div>出勤率{{aqycql}}%</div>
        </div>
        <div class="cqfl">
          <div style="margin-top:0.2rem;">材料员</div>
          <div  id="type7" style="height:60%; width: 100%;"></div>
          <div>出勤率{{clycql}}%</div>
        </div>
      </div>

      <div class="left_btton">
        <div class="qql_police">
          <div class="model_title">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png"/>
            <div>人员缺勤率</div>
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
              <div class="police_list_con" v-for="(item,index) in datalist2">
                <div style="width: 10%">
                  <span style="width: 18px;width: 18px;height: 18px;background: #649BF8;border-radius: 4px;display: block;margin: 0 auto" >
                    <p style="color: #FFFFFF">{{index+1}}</p>
                  </span>
                </div>
                <div style="width: 50%;line-height: 0.4rem;">{{item.infocollectionCard}}</div>
                <div style="width: 20%;line-height: 0.4rem;">{{item.infocollectionName}}</div>
                <div style="width: 20%;line-height: 0.4rem;">{{item.attendanceNumber}}</div>
                <!-- <div style="width: 20%;">{{item.handleTime}}</div> -->
              </div>
            </div>
          </div>
        </div>
        <div class="bot_right">
          <div class="br_t">
            <div class="model_title">
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../assets/img/projectAnalyse/model_title.png"/>
              <div>项目缺勤率排名</div>
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
        <div class="ssdt_left" v-for="item in datalist">
          <div class="ssdt_children_left">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="ssdt_children_left_img" v-src="item.bloodPressure"/>
          </div>
          <div class="ssdt_children_regit">
            <span class="ssdt_children_regit_span">{{item.bloodOxygen}}</span>
            <span class="ssdt_children_regit_span" >姓名:{{item.alcoholicity}}</span>
            <span class="ssdt_children_regit_span" style="font-weight: bold">{{item.dataGeneration}}</span>
          </div>
          <div class="ssdt_children_under">
            <span :class="item.clockDirection==0?'ssdt_children_under_span_red':'ssdt_children_under_span_blue'"></span>
            <span> {{item.clockTime}}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import * as echarts from 'echarts';
  import request from '@/utils/request'
  export default {
    data(){
       return{
        datalist:[],
        datalist2:[],
         xmzgcscql: '',
         xmjlcql: '',
         gcjlcql: '',
         sgycql: '',
         zlycql: '',
         aqycql: '',
         clycql: ''
      }
    },
    mounted(){
      this.remsize()
      this.qql()
      this.cql ()
      this.dataToObtain()
      this.getList();
      this.getList2();
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
        request({
          url: 'build/bLabourAttendanceRecord/projectAttendanceRate',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
        let resultData = res.data
        let xAxisData = []
        let seriesData = []
        resultData.forEach(item => {
          xAxisData.push(item.projectName)
          seriesData.push(item.averageQql)
        })
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
      })
      },
      cql () { //24小时空气质量数据
        request({
          url: 'build/bLabourAttendanceRecord/projectAttendanceRateGo',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          let resultData = res.data
          let xAxisData = []
          let seriesData = []
          resultData.forEach(item => {
            xAxisData.push(item.projectName)
            seriesData.push(item.averageQql)
          })
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
       })
      },
      type1(cqs,cql,qqs,zs){//报警类型占比
        let myChart = echarts.init(document.getElementById('type1'));
        this.xmzgcscql = cql
        let option = {
          title: {
            text: zs,
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
                { value: cqs, name: '出勤',itemStyle: {color: '#649BF8'} },
                { value: qqs, name: '缺勤',itemStyle: {color: '#BBD1F8'} }
              ]
            }
          ]
        };
        myChart.setOption(option)
      },
      type2(cqs,cql,qqs,zs){//报警类型占比
        let myChart = echarts.init(document.getElementById('type2'));
        this.xmjlcql = cql
        let option = {
          title: {
            text: zs,
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
                { value: cqs, name: '出勤',itemStyle: {color: '#30BF78'} },
                { value: qqs, name: '缺勤',itemStyle: {color: '#A0E9C5'} }
              ]
            }
          ]
        };
        myChart.setOption(option)
      },
      type3(cqs,cql,qqs,zs){//报警类型占比
        let myChart = echarts.init(document.getElementById('type3'));
        this.gcjlcql = cql
        let option = {
          title: {
            text: zs,
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
                { value: cqs, name: '出勤',itemStyle: {color: '#F86464'} },
                { value: qqs, name: '缺勤',itemStyle: {color: '#FFD0D0'} }
              ]
            }
          ]
        };
        myChart.setOption(option)
      },
      type4(cqs,cql,qqs,zs){//报警类型占比
        let myChart = echarts.init(document.getElementById('type4'));
        this.sgycql = cql
        let option = {
          title: {
            text: zs,
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
                { value: cqs, name: '出勤',itemStyle: {color: '#FAAD14'} },
                { value: qqs, name: '缺勤',itemStyle: {color: '#FFE0A3'} }
              ]
            }
          ]
        };
        myChart.setOption(option)
      },
      type5(cqs,cql,qqs,zs){//报警类型占比
        let myChart = echarts.init(document.getElementById('type5'));
        this.zlycql = cql
        let option = {
          title: {
            text: zs,
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
                { value: cqs, name: '出勤',itemStyle: {color: '#A89FFF'}  },
                { value: qqs, name: '缺勤',itemStyle: {color: '#D6D2FF'}  }
              ]
            }
          ]
        };
        myChart.setOption(option)
      },
      type6(cqs,cql,qqs,zs){//报警类型占比
        let myChart = echarts.init(document.getElementById('type6'));
        this.aqycql = cql
        let option = {
          title: {
            text: zs,
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
                { value: cqs, name: '出勤',itemStyle: {color: '#FF6100'}  },
                { value: qqs, name: '缺勤',itemStyle: {color: '#FFCFB2'}  }
              ]
            }
          ]
        };
        myChart.setOption(option)
      },
      type7(cqs,cql,qqs,zs){//报警类型占比
        let myChart = echarts.init(document.getElementById('type7'));
        this.clycql = cql
        let option = {
          title: {
            text: zs,
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
                { value: cqs, name: '出勤',itemStyle: {color: '#73CDF6'} },
                { value: qqs, name: '缺勤',itemStyle: {color: '#BDEAFF'} }
              ]
            }
          ]
        };
        myChart.setOption(option)
      },
      dataToObtain(){
        request({
          url: 'build/bLabourAttendanceRecord/selectPositionAttendance',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          //console.log("111111111111222222222222"+ JSON.stringify(res.data.xmjlzs))
          var dataPosition =res.data
          this.type1(dataPosition.xmzgcscqs,dataPosition.xmzgcscql,dataPosition.xmzgcsqqs,dataPosition.xmzgcszs)
          this.type2(dataPosition.xmjlcqs,dataPosition.xmjlcql,dataPosition.xmjlqqs,dataPosition.xmjlzs)
          this.type3(dataPosition.gcjlcqs,dataPosition.gcjlcql,dataPosition.gcjlqqs,dataPosition.gcjlzs)
          this.type4(dataPosition.sgycqs,dataPosition.sgycql,dataPosition.sgyqqs,dataPosition.sgyzs)
          this.type5(dataPosition.zlycqs,dataPosition.zlycql,dataPosition.zlyqqs,dataPosition.zlyzs)
          this.type6(dataPosition.aqycqs,dataPosition.aqycql,dataPosition.aqyqqs,dataPosition.aqyzs)
          this.type7(dataPosition.clycqs,dataPosition.clycql,dataPosition.clyqqs,dataPosition.clyzs)

        })
      },
      getList(){
        request({
          url: 'build/bLabourAttendanceRecord/selectDynamic',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          //console.log("打印:"+ JSON.stringify(res.data))
          this.datalist =res.data
        })

      },
      getList2(){
        request({
          url: 'build/bLabourAttendanceRecord/AttendanceRate',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
          //console.log("打印:"+ JSON.stringify(res.data))
          this.datalist2 =res.data
        })

      }
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
