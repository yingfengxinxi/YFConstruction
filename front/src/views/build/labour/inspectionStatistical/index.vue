<template>
	<div>
		<el-row>
			<el-col :span="12">
				<el-card class="box-card" style="margin: 10px">
					<div id="proportion" style="width: 100%;height:340px;"></div>
				</el-card>
			</el-col>

			<el-col :span="12">
				<el-card class="box-card" style="margin: 10px;">
					<div id="stage" style="height:340px;"></div>
				</el-card>
			</el-col>

		</el-row>
    <el-row>
      <el-col :span="24">
        <el-table
          id="crm-table"
          :data="tableData"
          :height="tableHeight"
          class="n-table--border"
          use-virtual
          stripe
          border
          highlight-current-row>
          <el-table-column label="序号" type="index" align='center' width="60"></el-table-column>
          <!-- <el-table-column :formatter="fieldFormatter" label="分项类型" align="center" prop="itemType" width="100" /> -->
          <el-table-column label="监督员" align="center" prop="realname" />
          <el-table-column label="监督工程数量" align="center" prop="projectNum" />
          <el-table-column label="监督工程总面积(㎡)" align="center" prop="totalArea" />
          <el-table-column label="已检查次数" align="center" prop="checkNum" />
          <el-table-column label="总检查次数" align="center" prop="totalNum" />
          <el-table-column label="最近检查日期" align="center" prop="checkTime" />
        </el-table>
      </el-col>
    </el-row>
	</div>

</template>

<script>
	import * as echarts from 'echarts';
  import request from '@/utils/request'
	export default {
		data() {
			return {
        tableHeight: 300,
        tableData:[]
			}
		},
		mounted() {
      this.getData();
      this.getSupeList()
		},
		methods: {
      getData(){
        request({
          url: 'build/bLabourInspection/inspectionStatistical',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(response => {
          console.log(response.data)
          this.proportion(response.data.inspectionStatistical)
          this.stage(response.data.inspectionStatisticalHistogram)
        }).catch(() => {

        })
      },
      getSupeList() {
        request({
          url: 'build/supervisionEnterprise/selectSupeList',
          method: 'post',
          data: [],
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(res => {
            this.tableData = res.data
          })
          .catch(() => {
          })
      },
			stage(data) {
        let xAxisData = []
        let seriesData = []
        let seriesData2 = []
        data.forEach(item => {
          xAxisData.push(item.dictLabel)
          seriesData.push(item.sqsl)
          seriesData2.push(item.bjsl)
        })
				let myChart = echarts.init(document.getElementById('stage'));
				let option = {
					title: {
						text: '检查阶段',
					},
					grid: {
						bottom: '3%',
						containLabel: true
					},
					tooltip: {
						trigger: 'axis'
					},
					xAxis: {
						type: 'category',
						axisLine: {
							lineStyle: {
								color: "#5B8FF9",
							},
						},
            axisLabel: {
              color: '#fffff',
              formatter: function (params) {
                var newParamsName = '' // 最终拼接成的字符串
                var paramsNameNumber = params.length // 实际标签的个数
                var provideNumber = 4 // 每行能显示的字的个数
                var rowNumber = Math.ceil(paramsNameNumber / provideNumber) // 换行的话，需要显示几行，向上取整
                var rowNumber = 2
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
                      tempStr = '....'
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
						data:xAxisData
					},
					yAxis: {
						type: 'value',
						axisLine: {
							show: false
						}
					},
					series: [{
						data: seriesData,
						type: 'bar',
           name:'申请数量'
					},{
            data: seriesData2,
            type: 'bar',
            name:'完结数量'
          }]
				}
				myChart.setOption(option)
			},
			proportion(data) {
				var data = [{
					name: '总项目数量',
					value: data.xmzs
				}, {
					name: '提交申请项目数量',
					value: data.sqzs
				}, {
					name: '已结束项目数量',
					value: data.wjzs
				}]
				let myChart = echarts.init(document.getElementById('proportion'));
				let option = {
					title: {
						text: "节点报检占比",
					},
					tooltip: {
						trigger: 'item',
						formatter: '{a}:{d}<br/>{b}:{c}'
					},
					legend: {

						left: 'center',
						bottom: '10'
					},
					series: [{
						name: '占比',
						type: 'pie',
						radius: '50%',
						// center: ['50%', '60%'],
						data: data,
					}]
				}
				myChart.setOption(option)

			}
		},

	}
</script>

<style scoped>

</style>
