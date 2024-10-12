<template>

  <div>
    <!-- 头部：搜索、操作 start -->
    <el-form
      v-show="true"
      ref="queryForm"
      :model="queryParams"
      :inline="true"
      label-width="120px"
      style="padding: 20px 20px 0px 20px;background-color: white;border-top: 0px solid #e6e6e6;"
    >
      <el-form-item
        label="创优评优申报批次"
        prop="appraisingBatch"
      >
        <el-select
          v-model="queryParams.appraisingBatch"
          placeholder="请选择"
          clearable
          size="small"
          style="width: 200px"
          @change="appraisingBatchChange"
        >
          <el-option
            v-for="dict in AppraisingBatchAll"
            :key="dict.id"
            :label="dict.appraisingBatch"
            :value="dict.id"
          />
        </el-select>
      </el-form-item>

    </el-form>
    <el-tabs
      v-model="tabCurrentName"
      type="border-card"
      class="d-container-bd--left"
      style="border-top: 1px solid #e6e6e6;"
      :action="action"
    >

      <el-tab-pane
        label="创优评优台账--已通过"
        name="AdoptOkList"
        lazy
      >
        <AdoptOkList
          style="margin-left: -15px;margin-right: -15px;"
          :datalist="data_list_ok"
          :dictData="dictData"
          :total="data_list_ok_num"
        ></AdoptOkList>
      </el-tab-pane>
      <el-tab-pane
        label="创优评优台账--未通过"
        name="AdoptNoList"
      >
        <AdoptOkList
          style="margin-left: -15px;margin-right: -15px;"
          :datalist="data_list_no"
          :dictData="dictData"
          :total="data_list_no_num"
        ></AdoptOkList>
      </el-tab-pane>

    </el-tabs>

  </div>

</template>

<script>
import { mapGetters } from "vuex"; //权限
import Lockr from "lockr"; //分页
import AdoptOkList from "./AdoptList/AdoptOkList";
import { userListAPI } from "@/api/common"; // 直属上级接口  用户接口
import {
  getAppraisingDeclareList,
  getAppraisingDeclareById
} from "../api/appraisingDeclare";
export default {
  /** DEMO列表 */
  name: "appraisingStandingBook",
  components: {
    AdoptOkList
  },
  data() {
    return {
      //默认显示tab
      tabCurrentName: "AdoptOkList",
      // 加载动画
      loading: false,
      /** 列表 */
      tableHeight: document.documentElement.clientHeight - 320, // 表的高度
      //查询参数
      queryParams: {},
      //所有创优评优批次数据
      AppraisingBatchAll: [],
      //传给子组件的值
      action: {
        type: "ok",
        isReadonly: false,
        data: {}
      },
      datalist: [{ declareProject: "申报项目", projectManager: "逄证" }],
      data_list_ok: [],
      data_list_no: [],
      data_list_ok_num:0,
      data_list_no_num:0,
      dictData: {
        userListData: []
      }
    };
  },
  computed: {
    //权限 start
    ...mapGetters(["allAuth"]),
    //权限 end
    titleIcon() {
      return require(`@/assets/img/crm/customer.png`);
    }
  },
  mounted() {
    // this.initData(this.$route)
    /** 控制table的高度 */
    // (window.onresize = () => {
    //   this.updateTableHeight();
    // }),
    //获取列表数据
    this.getList();
    this.InitData();
  },
  watch: {},
  methods: {
    //按钮操作权限
    canHandle(type) {
      return this.allAuth.demo
        ? this.allAuth.demo.wf
          ? this.allAuth.demo.wf[type]
          : false
        : false;
    },
    // /** 重置按钮操作 */
    // resetQuery() {
    //   this.queryParams = {};
    //   this.getList();
    // },
    getList() {},
    InitData() {
      //获取所有批次数据
      this.loading = true;
      getAppraisingDeclareList()
        .then(res => {
          // console.log(res);
          this.AppraisingBatchAll = res.data.list;
          this.queryParams.appraisingBatch = this.AppraisingBatchAll[ 0  ].id;
          // this.loading = false;
          this.GetSubcomponentData(this.queryParams.appraisingBatch);
        })
        .catch(() => {
          this.loading = false;
        });
      // 查询当前项目的用户
      var params = {
        pageType: 0
      };
      userListAPI(params)
        .then(res => {
          this.dictData.userListData = res.data.list;
        })
        .catch(() => {});
      //
    },
    appraisingBatchChange(event) {
      // console.log("进入change事件");
      this.$forceUpdate();
      this.GetSubcomponentData(event);
    },
    //获取子组件需要的数据
    GetSubcomponentData(event) {
      // console.log(event);
      let data_list_ok_temp = [];
      let data_list_no_temp = [];
      getAppraisingDeclareById(event)
        .then(res => {
          // console.log(res);
          // let mainTemp = res.data.appraisingDeclare;
          let reviewLeaderTemp=res.data.appraisingDeclare.reviewLeader
          let reviewTimeTemp=res.data.appraisingDeclare.reviewTime
          let reviewEndTimeTemp=res.data.appraisingDeclare.reviewEndTime
          let sonListTemp = res.data.hiddenDeclareList;

          for (var i = 0; i < sonListTemp.length; i++) {
            let forTemp = sonListTemp[i];
            forTemp["reviewLeader"] = reviewLeaderTemp;
            forTemp["reviewTime"] = reviewTimeTemp;
            forTemp["reviewEndTime"] = reviewEndTimeTemp;
            if (forTemp.whetherAdopt == 1) {
              data_list_ok_temp.push(forTemp);
            } else {
              data_list_no_temp.push(forTemp);
            }
          }
          // this.$nextTick(() => {
            this.data_list_ok = data_list_ok_temp;
            this.data_list_no = data_list_no_temp;
            this.data_list_ok_num=this.data_list_ok.length
             this.data_list_no_num=this.data_list_no.length
          // });
          // console.log("ok", this.data_list_ok);
          // console.log("no", this.data_list_no);
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
        });
    }
  }
};
</script>

<style lang="scss" scoped>
/*//表头*/
/*.table-head-container {*/
/*  //border-bottom: 1px solid #e6e6e6;*/
/*  border-top: 1px solid #e6e6e6;*/
/*  background-color: white;*/
/*}*/

.th-container {
  font-size: 13px;
  height: 50px;
  padding: 0 20px;

  .scene-select {
    width: 180px;
    /deep/ .el-input__inner {
      cursor: pointer;
    }
  }

  .filter-button {
    margin-left: 20px;
    /deep/ i {
      font-size: 14px;
      margin-right: 5px;
    }
  }
}

/** 场景和筛选 */
.condition_title {
  cursor: pointer;
}

.condition_title:hover {
  color: $xr-color-primary;
}

.m-arrow {
  margin: 0 8px;
}

.c-filtrate {
  margin: 0 10px 0 30px;
  width: 12px;
}

/** 勾选操作 */
.selection-bar {
  font-size: 12px;
  height: 50px;
  padding: 0 20px;
  color: #777;

  .selected—title {
    flex-shrink: 0;
    padding-right: 20px;
    color: #333;
    .selected—count {
      color: $xr-color-primary;
    }
  }
}

.selection-items-box {
  overflow-x: auto;
  overflow-y: hidden;
  padding: 0 15px;

  .el-button {
    color: #666;
    background-color: #f9f9f9;
    border-color: #e5e5e5;
    font-size: 12px;
    height: 28px;
    border-radius: 4px;
    /deep/ i {
      font-size: 12px;
      margin-right: 5px;
    }
  }

  .el-button--primary:hover {
    background: $xr-color-primary;
    border-color: $xr-color-primary;
    color: #ffffff;
  }

  .el-button + .el-button {
    margin-left: 15px;
  }
}

//页面头部
.c-container {
  height: 60px;
  position: relative;
  //z-index: 100;
  .title-icon {
    width: 30px;
    height: 30px;
    margin-right: 10px;
  }

  .title {
    float: left;
    width: auto;
    padding-left: 28px;
    font-size: 16px;
    font-weight: 600;
    margin-top: 15px;
  }
  .sc-container {
    width: 300px;
    margin: -16px 0 0 -150px;
    position: absolute;
    left: 50%;
    top: 50%;
  }

  .el-input {
    /deep/ .el-input-group__append {
      background-color: $xr-color-primary;
      border-color: $xr-color-primary;
      color: white;
    }
  }

  .right-container {
    margin-right: -10px;
    float: right;
    margin: 15px 20px 0 0;
    position: relative;

    .dup-check-btn,
    .xr-btn--orange {
      margin-left: 0;
      margin-right: 10px;
    }

    .right-item {
      float: left;
      margin-right: 10px;
      padding: 8px 10px;
      font-size: 13px;
      border-radius: 2px;
    }
  }
}

@import "@/views/styles/table.scss";
</style>
