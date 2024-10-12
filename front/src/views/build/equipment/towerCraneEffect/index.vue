<template>

  <div style="padding: 10px;height: 100%">
    <el-row :gutter="10" style="margin-bottom: 10px;height:calc(33% - 10px)">
      <el-col :span="7" style="height: 100%">
        <el-card class="box-card" style="height: 100%">
          <div slot="header" class="clearfix">
            <span>塔吊工效</span>
            <!--<el-date-picker v-model="tdgxData" type="date" placeholder="选择日期" style="float: right; padding: 3px 0;width: 180px"></el-date-picker>-->
            <el-date-picker
              v-model="tdgxData"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期" value-format="yyyy-MM-dd" style="float: right; padding: 3px 0;width: 250px;border:0" @change="initTdgx">
            </el-date-picker>
          </div>
          <div>
            <table>
              <tbody>
              <tr class="table_tr" v-for="item in tjgx">
                <td class="table_th">{{item.name}}</td>
                <td class="table_td">{{item.value}}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
      <el-col :span="17">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>群塔吊装效率分析</span>
            <el-date-picker
              v-model="qtdzData"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期" value-format="yyyy-MM-dd" style="float: right; padding: 3px 0;width: 250px;border:0" @change="initqtdzxlfx">
            </el-date-picker>
            <el-radio-group v-model="tabPosition" style="float: right;border:0;margin-right: 20px;margin-top:5px" @change="initqtdzxlfx">
              <el-radio-button label="sc"  >工作时长</el-radio-button>
              <el-radio-button label="cs"  >吊装次数</el-radio-button>
            </el-radio-group>
          </div>
          <div id="qtdzxlfx" style="width:100%;height:100%"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="10" style="margin-bottom: 10px;height:calc(33% - 10px)">
      <el-col :span="7">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>吊装排行榜</span>
            <el-date-picker
              v-model="dzphbData"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期" value-format="yyyy-MM-dd" style="float: right; padding: 3px 0;width: 250px;border:0" @change="initDzphb">
            </el-date-picker>
          </div>
          <div>
            <table>
              <tbody>
              <tr class="table_tr" v-for="item in tzphb">
                <td class="table_th_phb">{{item.name}}</td>
                <td class="table_td_phb">{{item.value}}次</td>
              </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
      <el-col :span="17">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>吊装循环次数</span>
            <el-date-picker
              v-model="xhdzcsData"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期" value-format="yyyy-MM-dd" style="float: right; padding: 3px 0;width: 250px;border:0" @change="initxhdzcs">
            </el-date-picker>
          </div>
          <div id="xhdzcs" style="width:100%;height:100%"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="10"  style="height:calc(33% - 10px)">
      <el-col :span="7">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>近四周塔吊吊装次数</span>

          </div>
          <div id="hsztddzcs" style="width:100%;height:100%"></div>
        </el-card>
      </el-col>
      <el-col :span="17">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>吊装重量（T）、载重百分比（%）</span>
            <el-date-picker
              v-model="dzzlData"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期" value-format="yyyy-MM-dd" style="float: right; padding: 3px 0;width: 250px;border:0" @change="initDzphb">
            </el-date-picker>
          </div>
          <div id="dtzlztbfb" style="width:100%;height:100%"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>

</template>

<script type="text/javascript">

  import * as echarts from 'echarts';
  import request from '@/utils/request'
  export default{
    name: 'towerCraneEffect',
    components: {

    },
    data() {
      return {
        loading: false, // 加载动画
        tdgxData:[],
        qtdzData:[],
        dzphbData:[],
        xhdzcsData:[],
        dzzlData:[],

        tabPosition:'sc',
        pieOption: null,
        titleName:'',
        tjgx:[{name:'工作塔吊数',value:"0台"},{name:'平均吊次（每台）',value:"0次"},{name:'平均吊重（每台）',value:"0T"},{name:'平均有效时长（每台）',value:"0min"},{name:'平均工效（每台）',value:"0T/min"}],
        tzphb:[{name:' ',value:"0"}],
        qtdzxlfx:[['2021-11-10','2021-11-11','2021-11-12','2021-11-13','2021-11-14','2021-11-15','2021-11-16','2021-11-17'],[30,22,18,35,42,27,18,10]],
        xhdzcs:[['2021-11-10','2021-11-11','2021-11-12','2021-11-13','2021-11-14','2021-11-15','2021-11-16','2021-11-17'],[30,22,18,35,42,27,18,30]],
        dtzlztbfb:[['2021-11-10','2021-11-11','2021-11-12','2021-11-13','2021-11-14','2021-11-15','2021-11-16','2021-11-17'],[0.8,0.5,0.6,0.3,0.4,0.7,0.5,0.3],[0.6,0.4,0.3,0.3,0.6,0.6,0.4,0.4]],
        hsztddzcs:[
        ['product', '1#塔机', '2#塔机', '3#塔机'],
        ['第一周', 28, 32, 15],
        ['第二周', 32, 33, 25],
        ['第三周', 68, 54, 30],
        ['第四周', 40, 33, 15]
      ]
      }
    },
    watch:{
      //监听页面切换
      '$route'(to, from) {
        //TODO 重新加载数据
        this.initData(to)
      }
    },
    created(){

    },
    mounted(){

      this.tdgxData.push(this.formatDate(new Date()));
      this.tdgxData.push(this.formatDate(new Date()));
      this.qtdzData.push(this.formatDate(new Date(new Date().getTime()-168*60*60*1000)))
      this.qtdzData.push(this.formatDate(new Date()))
      this.xhdzcsData.push(this.formatDate(new Date(new Date().getTime()-168*60*60*1000)))
      this.xhdzcsData.push(this.formatDate(new Date()))
      this.dzzlData.push(this.formatDate(new Date(new Date().getTime()-168*60*60*1000)))
      this.dzzlData.push(this.formatDate(new Date()))
      this.initData(this.$route);
      this.initTdgx();
      this.initqtdzxlfx()
      this.initDzphb();
      this.initxhdzcs()
      this.initdtzlztbfb()
      this.inithsztddzcs()

    },
    methods: {
      //初始化数据
      initData(to) {
        this.initWarningList()//整改超时预警数据

      },
      initTdgx(){

        var params = {'sDate':this.tdgxData[0],'eDate':this.tdgxData[1]};
        request({
          url: 'build/equipmentTowerCrane/getTdgx',
          method: 'post',
          data: params,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(response => {

          this.tjgx = [];
          var pjdc = (response.data.frequency/response.data.number).toFixed(2);
          var pjdz = (response.data.weight/response.data.number).toFixed(2);
          var pjyxsc = (response.data.time/response.data.number).toFixed(2);
          var pjgx = (response.data.weight/response.data.time).toFixed(2);
          if(response.data.number == null || response.data.number ==0){
            response.data.number = 0
            pjdc = 0;
            pjdz = 0;
            pjyxsc = 0;
          }
          if(response.data.frequency == null){
            response.data.frequency = 0
          }
          if(response.data.time == null  || response.data.time ==0){
            response.data.time = 0
            pjgx = 0;
          }
          if(response.data.weight == null){
            response.data.weight = 0
          }
          console.log(response.data)
          this.tjgx.push({name:'工作塔吊数',value:response.data.number+"台"})
          this.tjgx.push({name:'平均吊次（每台）',value:pjdc+"次"})
          this.tjgx.push({name:'平均吊重（每台）',value:pjdz+"T"})
          this.tjgx.push({name:'平均有效时长（每台）',value:pjyxsc+"T"})
          this.tjgx.push({name:'平均工效（每台）',value:pjgx+"T/min"})
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },
      initqtdzxlfx(){
        var params = {'sDate':this.qtdzData[0],'eDate':this.qtdzData[1]};
        request({
          url: 'build/equipmentTowerCrane/getQtdzxlfx',
          method: 'post',
          data: params,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(response => {
          this.qtdzxlfx= [];
          this.qtdzxlfx.push(response.data.dateList);
          this.qtdzxlfx.push(response.data.valueList1);
          this.qtdzxlfx.push(response.data.valueList2);
          var list = [];
          if(this.tabPosition=='sc'){
            this.qtdzxlfx[1] = response.data.valueList2;
            this.qtdzxlfx[2] = response.data.valueList1;
          }

          var chartDom = document.getElementById('qtdzxlfx');
          var myChart = echarts.init(chartDom,'light');
          var option;

          option = {
            grid: { top: '10px',bottom:'40px',left:'40px',right:'10px' },
            xAxis: {
              type: 'category',
              data: this.qtdzxlfx[0]
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              data: this.qtdzxlfx[1],
              type: 'line',
              smooth: true
            }
            ]
          };
          option && myChart.setOption(option,true);
        }).catch(() => {
          this.loading = false
        })

      },
      initDzphb(){
        var params = {'sDate':this.dzphbData[0],'eDate':this.dzphbData[1]};
        request({
          url: 'build/equipmentTowerCrane/getTzphb',
          method: 'post',
          data: params,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(response => {
          this.tzphb = response.data
        }).catch(() => {
          this.loading = false
        })
      },
      initxhdzcs(){
        var params = {'sDate':this.xhdzcsData[0],'eDate':this.xhdzcsData[1]};
        request({
          url: 'build/equipmentTowerCrane/getXhdzcs',
          method: 'post',
          data: params,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(response => {
          var chartDom = document.getElementById('xhdzcs');
          var myChart = echarts.init(chartDom,'light');
          var option;

          option = {
            grid: { top: '10px',bottom:'40px',left:'40px',right:'10px' },
            xAxis: {
              type: 'category',
              data: response.data.dateList
            },
            yAxis: {
              type: 'value'
            },
            series: [
              {
                data: response.data.valueList,
                type: 'line',
                smooth: true
              }
            ]
          };
          option && myChart.setOption(option,true);
        }).catch(() => {
          this.loading = false
        })

      },

      initdtzlztbfb(){
        var params = {'sDate':this.dzzlData[0],'eDate':this.dzzlData[1]};
        request({
          url: 'build/equipmentTowerCrane/getDtzlztbfb',
          method: 'post',
          data: params,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(response => {
          var chartDom = document.getElementById('dtzlztbfb');
          var myChart = echarts.init(chartDom,'light');
          var option;
          option = {
            grid: { top: '10px',bottom:'40px',left:'40px',right:'10px' },
            xAxis: {
              type: 'category',
              data: response.data.dateList
            },
            yAxis: {
              type: 'value'
            },
            tooltip: {
              trigger: 'axis'
            },
            series: [
              {
                data: response.data.valueList1,
                type: 'bar',
                name:'吊装重量'
              },
              {
                data: response.data.valueList1,
                type: 'line',
                name:'载重百分比',
                smooth: true
              }
            ]
          };
          option && myChart.setOption(option,true);
        }).catch(() => {
          this.loading = false
        })
      },
      inithsztddzcs(){
        var params = {'sDate':this.xhdzcsData[0],'eDate':this.xhdzcsData[1]};
        request({
          url: 'build/equipmentTowerCrane/getHsztddzcs',
          method: 'post',
          data: params,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(response => {
          var ber = [];
          for(var i = 0;i<response.data[0].length-1;i++){
            ber.push({ type: 'bar' })
          }

          var chartDom = document.getElementById('hsztddzcs');
          var myChart = echarts.init(chartDom,'light');
          var option;

          option = {
            grid: { top: '30px',bottom:'40px',left:'40px',right:'10px' },
            legend: {},
            tooltip: {},
            dataset: {
              source: response.data
            },
            xAxis: { type: 'category' },
            yAxis: {},
            // Declare several bar series, each will be mapped
            // to a column of dataset.source by default.
            series: ber
          };
          option && myChart.setOption(option,true);
        }).catch(() => {
          this.loading = false
        })

      },

      //初始化整改超时预警饼状图数据
      initWarningList(){

      },
      formatDate (date) {
        var myyear = date.getFullYear();
        var mymonth = date.getMonth() + 1;
        var myweekday = date.getDate();

        if (mymonth < 10) {
          mymonth = "0" + mymonth;
        }
        if (myweekday < 10) {
          myweekday = "0" + myweekday;
        }
        return (myyear + "-" + mymonth + "-" + myweekday);
      },
    },



  }






</script>
<style lang="scss" scoped>
  .el-col{
    height: 100% !important;
  }
  .el-card{
    height: 100% !important;
  }
  /deep/.el-card__header{
    padding: 0 !important;
  }
  /deep/.el-date-editor--date .el-input__inner{
    border: 0;
  }
  /deep/.clearfix{
    line-height: 40px;
    padding-left: 20px;
  }
  /deep/.el-card__body{
    height: calc(100% - 46px)
  }
  .table_tr{

  }
  .table_th{
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 10px;
  }
  .table_td{
    font-size: 16px;
    float: right;
    margin-bottom: 10px;
  }
  .table_th_phb{
    font-size: 20px;
    font-weight: 600;
    margin-bottom: 20px;
  }
  .table_td_phb{
    font-size: 20px;
    float: right;
    margin-bottom: 20px;
  }
</style>
