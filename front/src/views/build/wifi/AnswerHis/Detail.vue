<template>
  <xr-create
    :loading="loading"
    :title="title"
    @close="close"
    :showSave="false"
  >
    <div class="container">
      <el-container style="height: calc(100vh - 180px); border: 1px solid #eee">
        <el-aside
          width="300px"
          style="background-color: #F5F6FA"
					class="elasd"
        >

          <el-form
            ref="form"
            v-model="form"
            size="mini"
            class='dtrform'
            label-position="left"
          >
            <el-form-item
              label="答题人："
              label-width="90px"
              class='form_label'
            >
              <span class="span_color">{{ form.answerName }}</span>
            </el-form-item>
            <el-form-item
              label="手机号："
              label-width="90px"
              class='form_label'
            >
              <span class="span_color">{{ form.answerPhone }}</span>
               </el-form-item>
              <el-form-item
                label="总分："
                label-width="90px"
                class='form_label'
              >
                <span class="span_color">{{ form.paperScore }}</span>
              </el-form-item>
              <el-form-item
                label="得分："
                label-width="90px"
                class='form_label'
              >
                <span class="span_color">{{ form.questScore }}</span>
              </el-form-item>
              <el-form-item
                label="工种："
                label-width="90px"
                class='form_label'
              >
                <span class="span_color">{{ CustomFormat(form.workTypeId) }}</span>
              </el-form-item>
              <el-form-item
                label="通过情况："
                label-width="90px"
                class='form_label'
              >
                <span class="span_color">{{form.questScore=='0'?'通过':'未通过'}}</span>
              </el-form-item>
          </el-form>
          <div class="stdh">
           		<div></div>试题导航
          </div>
          <div class="note">
            <div class="note_div">
            	<div style="background: #D8D8D8;"></div>
            	全部
            </div>
            <div class="note_div">
            	<div style="background: #30BF78;"></div>
            	正确
            </div>
            <div class="note_div">
            	<div style="background: #F4664A;"></div>
            	错误
            </div>
          </div>
          <ul class="sole">
            <li
              v-for="(item,index) in checkboxData"
              :key="index"
              :class="item.color"
              @click="toTegional(index)"
            >
              {{ index+1 }}
            </li>
          </ul>
        </el-aside>
        <el-container>


          <el-main>
            <div
              ref="rightContent"
              style="height: calc(100vh - 160px);"
            >
              <div
                v-for="(item,index) in checkboxData"
                :key="index"
              >
                <el-card
                  :id="'a'+index"
                  class="box-card"
                >
                  <div style="width: 100%; padding:20px 0">
                    <div style="padding-top: 20px">
                      <div
                        class="protitle"
                      >
                      <div class="wifidd">{{item.QuestionType}}</div>
                  
                       <div>{{ index + 1 }}.{{ item.title }}({{ item.questScore }}分)</div> 
                      </div>
                      <p
                        v-if="item.type!=='3'&&isReady"
                        style="display: inline;margin-left: 20px"
                      >
                        正确答案：{{ item.trueOption }}
                      </p>
                      <p
                        v-if="item.type!=='3'&&isReady"
                        style="display: inline;margin-left: 10px"
                      >
                        您的答案：{{ item.userChoice }}
                      </p>

                    </div>
                  </div>
                  <el-radio-group
                    v-if="item.type==='1'"
                    v-model="item.userChoice"
                    @change="onChange(item)"
                  >
                    <ul>
                      <li
                        v-for="(item1,index1) in item.problem"
                        :key="index1"
                        style="margin:10px 0 10px 20px"
                      >
                        <el-radio
                          :value="item1.name"
                          :label="item1.value"
                          :disabled="isReady"
                        >
                          {{ item1.value }}. {{ item1.name }}
                        </el-radio>
                      </li>
                    </ul>
                  </el-radio-group>
                  <el-checkbox-group
                    v-else-if="item.type==='2'"
                    v-model="item.userChoice"
                    @change="onChange(item)"
                  >
                    <ul>
                      <li
                        v-for="(item1,index1) in item.problem"
                        :key="index1"
                        style="margin:10px 0 10px 20px"
                      >
                        <el-checkbox
                          :value="item1.name"
                          :label="item1.value"
                          :disabled="isReady"
                        >
                          {{ item1.value }}. {{ item1.name }}
                        </el-checkbox>
                      </li>
                    </ul>
                  </el-checkbox-group>

                </el-card>
              </div>
            </div>
          </el-main>
        </el-container>
      </el-container>
    </div>
  </xr-create>
</template>
<script>
import XrCreate from "@/components/XrCreate";
import { getTestPaperDetail } from "../api/AnswerHis";
export default {
  name: "wifiAnswerHis",
  components: {
    XrCreate,
  },
  props: {
    action: {
      type: Object,
      default: () => {
        return {
          type: 'save',
          id: '',
          data: {}
        }
      }
    },
  },
  data() {
    return {
      loading: false,
      title: '详情',
      queryParams: {},
      form: '',
      checkboxData: [],
      isReady: true,


      isSubmit: false,
      src:
        'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg',
      examValue: '',
      count: 0,
      successCount: 0,
      finished: 0,
      order: ['A', 'B', 'C', 'D', 'E', 'F'],
      list: [],
      chengeList: [],
      hr: 0,
      min: 0,
      sec: 0,
      allScore: 0,
      trueScore: 0,
      countTime: "", // 测评时间
    };
  },
  computed: {
    
    unRate: function () {
      if (this.successCount === 0) {
        return 0
      }
      return Math.round(this.successCount / this.count * 100)

    },
    rate: function () {
      if (this.finished === 0) {
        return 0
      }
      return Math.round(this.finished / this.count * 100)

    },
  },
  created() {
    this.getMenus();
    // this.getStyle();      
    // this.timer();
  },
  mounted() {
    // console.log("父传子数据",this.action);
    if (window.sessionStorage.getItem('finished') !== null) {
      this.finished = window.sessionStorage.getItem('finished');
    }
    if (window.sessionStorage.getItem('isReady') !== null) {
      this.isReady = true;
      this.isSubmit = true;
    }
  },
  destroyed() {
    clearTimeout(this.setTime);
    clearTimeout(this.setTimer);
    window.sessionStorage.removeItem('finished');
    window.sessionStorage.removeItem('timed');
    window.sessionStorage.removeItem('chengeList');
    window.sessionStorage.removeItem('isReady');
  },
  methods: {
    /**
     * 关闭
     */
    close() {
      this.$emit("close");
    },
    // 获取信息
    getMenus() {
      // console.log("试卷id===", this.action.id);
      getTestPaperDetail(this.action.id)
        .then(res => {
          // console.log("试卷详情===", res);
          this.form = res.data.TestPaper;
          var dataList = res.data.checkboxData
          dataList.forEach(function (element) {
            if (parseInt(element.type) == 1) {
              element.QuestionType = "单选题";
            } else if (parseInt(element.type) == 2) {
              element.QuestionType = "多选题";
            }
            if (parseInt(element.duicuo) == 1) {
              element.color = 'greenClass'
            } else {
              element.color = 'redClass'
            }
            element.problem.forEach(function (ew) {
              if (parseInt(ew.zt) == 1) {

                if (parseInt(element.type) == 1) {
                  element.userChoice = ew.value
                } else {
                  element.userChoice = ew.value.split(',')
                }
              }



            });
          });
          // console.log("处理结束之后的===", dataList);
          this.checkboxData = dataList;

        })
        .catch(() => {
        })

    },
     /**
             * 模板自定义格式化列表
             */
        CustomFormat(value) {
          let datas=this.action.data;
            // console.log("模板自定义格式化====",datas,value);
            var actions = [];
            Object.keys(datas).some(key => {
                /* if (datas[key].id == "" + value) {
                    actions.push(datas[key].workType);
                    return true;
                } */
				if (datas[key].dictValue == "" + value) {
					actions.push(datas[key].dictLabel);
					return true;
				}
            });
            return actions.join("");
        },

    toTegional(val) {
      let id = '#a' + val;
      document.querySelector(id).scrollIntoView(true);
    },

  },
}
</script>

<style  lang="scss" scoped>
.container {
  height: calc(100vh - 180px);
}
.showImg {
  width: 125px;
  height: 150px;
  display: block;
  margin-left: 87px;
  margin-top: 20px;
  background-color: #dadcdc;
}
.box-card{
	border: none!important;
	box-shadow: none!important;
}
/deep/.el-card__body{
	border-bottom: 1px dotted #E3E3E3;
}
/deep/.xr-create__body{
	overflow: hidden;
}
.sole {
  height: auto;
  list-style-type: none;
  margin-top: 18px;
}

.sole li {
  width: 28px;
  height: 28px;
  border-radius: 16px;
  float: left;
  text-align: center;
  line-height: 28px;
  font-size: 14px;
  color: #666666;
  background-color: #fcfcfc;
  margin-right: 13px;
  margin-bottom: 14px;
}
.sole .greenClass{
	background: #30BF78;
	color: #FFFFFF;
}
.sole .redClass{
	background: #F4664A;
	color: #FFFFFF;
}
.note {
  width: 285px;
	display: flex;
	justify-content: space-around;
	margin-top: 18px;
}
.note_div{
	display: flex;
	align-items: center;
	color: #404040;
	font-size: 14px;
}
.note_div div{
	width: 8px;
	height: 8px;
	border-radius: 5px;
	margin-right: 5px;
}
.textStyle {
  width: 50px;
  float: left;
  margin-left: 5px;
  margin-top: 10px;
}
.elasd{
	padding-left: 15px;
}
.dtrform{
	width:285px;
	padding:10px 0px 10px 20px;
	background: #5B97F9;
}
.span_color{
	color: #FFFFFF;
	font-size: 14px;
}
/deep/.el-form-item__label{
	font-size: 14px;
	color: #FFFFFF;
}
/deep/ .el-form-item--mini.el-form-item{
	margin-bottom: 12px;
}
.stdh{
	display: flex;
	align-items: center;
	font-weight: 500;
	font-size: 14px;
	color: #404040;
	margin-top: 10px;
	width: 260px;
	padding-bottom: 8px;
	border-bottom: 1px solid #DFEBFF;
}
.wifidd{
	height: 20px;
	width: 54px;
	background: url('../../../../../static/img/wifidd.png') no-repeat;
	flex-shrink: 0;
	color: #FFFFFF;
	margin-right: 10px;
	padding-left: 10px;
	line-height: 20px;
	font-size: 12px;
}
.stdh div{
	width: 4px;
	height: 12px;
	background: #649BF8;
	border-radius: 2px;
	margin-right: 5px;
	margin-right: 8px;
}
p {
  word-break: keep-all;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
/deep/ .el-card__body {
  padding: 0 0 0 !important;
}
/deep/ .el-checkbox__input.is-disabled.is-checked .el-checkbox__inner {
  background-color: #30BF78 !important;
  border-color: #30BF78 !important;
}

/deep/ .is-checked .el-radio__inner {
  background: #30BF78 !important;
}
.protitle {
  word-wrap: break-word;
  margin-bottom: 10px;
  display: flex;
}
/deep/ .xr-create__header {
  height: 46px;
  padding: 0 10px;
  flex-shrink: 0;
}
/deep/.xr-create__footer{
	box-shadow: 0px -3px 10px 0px #E2E9F0;
}
/deep/.el-button--default{
	background: #5B97F9;
	color: #FFFFFF;
}
</style>

