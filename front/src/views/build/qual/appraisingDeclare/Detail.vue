<template>
  <slide-view
    :body-style="{padding: 0, height: '100%'}"
    xs-empty-icon="nopermission"
    xs-empty-text="暂无权限"
    @afterEnter="getDetail"
    @close="close"
  >

    <div
      v-loading="loading"
      ref="crmDetailMain"
      class="detail-main"
    >
      <flexbox
        direction="column"
        align="stretch"
        class="d-container"
      >
        <div class="container">
          <flexbox
            class="t-section"
            align="stretch"
          >
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'   
              :src="crmIcon"
              class="t-section__hd"
            >
            <div class="t-section__bd">
              <div class="type-name">DEMO</div>
              <p class="name">
                <el-tooltip
                  :disabled="!name"
                  :content="name"
                  effect="dark"
                  placement="top-start"
                >
                  <span>{{ name }}</span>
                </el-tooltip>
                <slot name="name" />
              </p>
            </div>
            <div class="t-section__ft">
              <el-button
                v-if="canHandle('edit')"
                class="head-handle-button xr-btn--green"
                icon="wk wk-circle-edit"
                type="primary"
                @click.native="handleTypeClick('edit')"
              >编辑</el-button>
              <!-- <el-dropdown
                trigger="click"
                @command="handleTypeClick"
              >
                <el-button
                  icon="el-icon-more"
                  class="t-more"
                />
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item
                    v-for="(item, index) in moreTypes"
                    :key="index"
                    v-if="canHandle(item.type)"
                    :icon="item.icon | wkIconPre"
                    :command="item.type"
                  >{{ item.name }}</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown> -->
            </div>
          </flexbox>
          <flexbox
            class="h-section"
            align="stretch"
          >
            <flexbox-item
              v-for="(item, index) in headDetails"
              v-show="item.title"
              :key="index"
              class="h-item"
              span="200"
            >
              <div class="h-title">{{ item.title }}</div>
              <div class="h-value text-one-line"><i
                  v-if="item.showIcon"
                  :style="item.style"
                  :class="item.icon"
                />{{ item.value }}</div>
            </flexbox-item>
          </flexbox>
          <slot />
        </div>
        <!-- 流程审批start -->
        <examine-info
          v-if="checkForm.examineRecordId"
          :id="id"
          :record-id="checkForm.examineRecordId"
          :owner-user-id="checkForm.ownerUserId"
          class="examine-info"
          @on-handle="examineHandle"
        />
        <!-- 流程审批 end -->
        <div class="d-container-bd">
          <el-tabs
            v-model="tabCurrentName"
            type="border-card"
            class="d-container-bd--left"
          >
            <el-tab-pane
              v-for="(item, index) in tabNames"
              :key="index"
              :label="item.label"
              :name="item.name"
              style="height:90%;overflow-y:auto;overflow-x:hidden;"
            >
              <create-sections
                v-if="item.isDetail"
                title="基本信息"
              >
                <el-form
                  :disabled="true"
                  ref="form"
                  :model="checkForm"
                  label-position="top"
                  label-width="100px"
                  class="form"
                >
                  <el-row>
                    <el-col :span="12">
                      <el-form-item
                        label="创优评优批次"
                        prop="appraisingBatch"
                      >
                        <el-input
                          v-model="checkForm.appraisingBatch"
                          placeholder="请输入创优评优批次"
                        ></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="6">
                      <el-form-item
                        label="评审开始时间"
                        prop="reviewTime"
                      >
                        <el-date-picker
                          v-model="checkForm.reviewTime"
                          type="date"
                          placeholder="请选择开始时间"
                          value-format="yyyy-MM-dd"
                        >
                        </el-date-picker>
                      </el-form-item>
                    </el-col>
                    <el-col :span="6">
            <el-form-item
              label="评审结束时间"
              prop="reviewEndTime"
            >
              <el-date-picker
                v-model="checkForm.reviewEndTime"
                type="date"
                placeholder="请选择开始时间"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12">
                      <el-form-item
                        label="评审组长"
                        prop="reviewLeader"
                      >
                        <el-select
                          v-model="checkForm.reviewLeader"
                          placeholder="请选择"
                          clearable
                          :disabled="action.isReadonly"
                          style="display:block"
                        >
                          <el-option
                            v-for="dict in dictData.userListData"
                            :key="dict.userId"
                            :label="dict.realname"
                            :value="dict.userId"
                          />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item
                        label="评审成员"
                        prop="reviewMember"
                      >
                        <xh-struc-user-cell
                          :users="checkForm.dep.ownerUserList"
                          style="width: 100%;"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="24">
                      <el-form-item
                        label="评审标准"
                        prop="reviewStandard"
                      >
                        <el-input
                          type="textarea"
                          :rows="2"
                          v-model="checkForm.reviewStandard"
                          placeholder="请输入评审标准"
                        ></el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
              <el-row>
        </el-row>
                  <create-sections title="申报明细">
                    <hidden-declare-items
                      :action="action"
                      :detaildata="detaildata"
                      ref="hiddenDeclareItems"
                    ></hidden-declare-items>
                  </create-sections>
                </el-form>
              </create-sections>

              <component
                v-else
                :is="item.name"
                :batchId="checkForm.mainBatchId"
                :id="checkForm.id"
              />
            </el-tab-pane>
          </el-tabs>
        </div>
      </flexbox>
    </div>
    <Create
      v-if="isCreate"
      :businessType="businessType"
      :action="{type: 'update',id:id}"
      @save-success="editSaveSuccess"
      @close="isCreate=false"
    />
  </slide-view>
</template>

<script>
import { mapGetters } from "vuex"; //权限
import SlideView from "@/components/SlideView";
//流程审批
import ExamineInfo from "@/components/common/examine/examine/form";

import FilesList from "@/components/common/FilesList"; //附件列表
import XrCreate from "@/components/XrCreate";
import CreateSections from "@/components/CreateSections";
import { debounce } from "throttle-debounce";
import { userListAPI } from "@/api/common"; // 直属上级接口  用户接口
import { XhStrucUserCell } from "@/components/CreateCom";
import WkUploadFile from "@/components/CreateCom/WkUploadFile";

import HiddenDeclareItems from "./HiddenDeclareItems_Detail";
import { getAppraisingDeclareById } from "../api/appraisingDeclare";
export default {
  // 新建编辑
  name: "DemoCreate",
  components: {
    XrCreate,
    SlideView,
    CreateSections,
    WkUploadFile,
    FilesList,
    HiddenDeclareItems,
    XhStrucUserCell,
    ExamineInfo
  },
  //mixins: [ExamineMixin],//业务审批Copy:审批
  props: {
    // 详情信息id
    id: [String, Number],
    action: { type: Object, isReadonly: false },
    businessType: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      //表单数据
      checkForm: {
        dep: {
          ownerUserList: []
        }
      },
      //tab
      tabCurrentName: "Detail",
      // 申报信息
      hiddenDeclareList: [],
      // 字典数据
      dictData: {},
      //传给子组件的值
      detaildata: {},
      headDetails: [{ title: "内容", value: "" }, { title: "备注", value: "" }],
      //更多操作
      // moreTypes: [
      //   { type: "import", name: "导入", icon: "import" },
      //   { type: "export", name: "导出", icon: "export" }
      // ],
      // 名称
      name: "",
      isCreate:false
    };
  },
  computed: {
    //权限 start
    ...mapGetters(["allAuth"]),
    crmIcon() {
      return require(`@/assets/img/crm/expense.png`);
    },
    //声明有多少Tab
    tabNames() {
      var tempsTabs = [
        { label: "详细资料", name: "Detail", isDetail: true },
        { label: "附件列表", name: "FilesList" }
        // { label: "变更履历列表", name: "BgList" }
      ];
      return tempsTabs;
    }
  },
  watch: {},
  created() {
    this.debouncedSaveField = debounce(300, this.save);
    this.InitData();
    this.getDetail();
  },

  mounted() {},

  beforeDestroy() {},

  methods: {
    /**
     * 审核操作完成后的钩子
     */
    examineHandle() {
      this.$emit("handle", { type: "examine" });
    },
    /**
     * 获取详情数据
     * */
    getDetail() {
      getAppraisingDeclareById(this.id)
        .then(res => {
          console.log(res);
          // 设置表单数据
          var appraisingDeclare = res.data.appraisingDeclare;
          // 转换用户对象
          //评审成员信息
          var ownerUserList = appraisingDeclare.ownerUserList;
          var dep = {
            ownerUserList: ownerUserList
          };
          appraisingDeclare.dep = dep;

          // 绑定基本信息
          this.checkForm = appraisingDeclare;

          // 绑定隐患
          this.hiddenDeclareList = res.data.hiddenDeclareList;

          // 生成附件batchId列表
          var batchIdAll = [];
          //   var receiptBatchIdAll = [];
          this.hiddenDeclareList.forEach(item => {
            batchIdAll.push(item.batchId);
          });

          // 将数据传到子控件
          // this.$refs.hiddenDeclareItems.fieldFormAll = this.hiddenDeclareList;
          // this.$refs.hiddenDeclareItems.batchIdAll = batchIdAll;
          //   this.$refs.hiddenDeclareItems.receiptBatchIdAll = receiptBatchIdAll;

          // 生成页对象
          var pageAllItems = [];
          this.hiddenDeclareList.forEach(item => {
            pageAllItems.push({});
          });
          // 将数据传到子控件
          // this.$refs.hiddenDeclareItems.pageAllItems = pageAllItems;

          this.detaildata.fieldFormAll = this.hiddenDeclareList;
          this.detaildata.batchIdAll = batchIdAll;
          this.detaildata.pageAllItems = pageAllItems;
        })
        .catch(() => {});
    },
    InitData() {
      // 查询当前项目的用户
      var params = {
        pageType: 0
      };
      userListAPI(params)
        .then(res => {
          this.dictData.userListData = res.data.list;
        })
        .catch(() => {});
    },
    handleTypeClick(type) {
      if (type == "edit") {
        //编辑
        this.isCreate = true;
      }
    },
    //按钮操作权限
    canHandle(type) {
      return this.allAuth.demo.wf[type];
    },
    editSaveSuccess() {
      this.getDetial();
      this.$emit("handle");
    },
    /**
     * 关闭
     */
    close() {
      console.log("close");
      this.$emit("close");
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  position: relative;
  background-color: white;
  border-bottom: 1px solid $xr-border-line-color;
}

.t-section {
  position: relative;
  padding: 30px 20px 5px;
  min-height: 60px;
  &__hd {
    display: block;
    width: 40px;
    height: 40px;
    margin-right: 15px;
  }
  &__bd {
    flex: 1;
    .type-name {
      color: #999;
      font-size: 12px;
      margin-bottom: 5px;
    }

    .name {
      color: #333;
      font-size: 16px;
      font-weight: 600;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 1;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }
  }

  &__ft {
    flex-shrink: 0;
    margin-left: 20px;
  }

  .t-more {
    margin-left: 15px;
  }

  .t-close {
    display: block;
    width: 40px;
    height: 40px;
    margin-left: 20px;
    padding: 10px;
    cursor: pointer;
  }
}

.h-section {
  position: relative;
  padding: 8px 20px 15px;
  .h-item {
    .h-title {
      font-size: 12px;
      color: #666;
    }
    .h-value {
      min-height: 14px;
      margin-top: 8px;
      font-size: 13px;
      color: #333;
      padding-bottom: 2px;
    }
  }
}

.el-button + .el-button {
  margin-left: 15px;
}

.head-handle-button {
  /deep/ i {
    font-size: 13px;
    margin-right: 5px;
  }
}

.form {
  margin-left: 10px;
}
.form /deep/ .el-form-item .el-form-item__content {
  width: calc(100% - 10px);
}
.form /deep/ .el-form-item__label {
  padding: 0px;
}
.el-date-editor.el-input,
.el-date-editor.el-input__inner {
  width: calc(100% - 10px);
}

.form /deep/ .el-date-editor {
  width: 100%;
}
@import "@/views/crm/styles/crmdetail.scss";
</style>
