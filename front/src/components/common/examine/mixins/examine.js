/**
 * 业务审批混肴
 *
 * */
export default {
  data() {
    return {
      //流程
      examineInfo: {}
    }
  },
  props: {
    businessType: {
      type: Number,
      required: true,
      default: -1
    },
  },
  computed: {
    // 流程审批核实
    isOpenExamine() {
      if (this.examineInfo) {
        // 初始状态是空对象默认展示，请求之后，根据status判断
        return Object.keys(this.examineInfo).length > 0 ? this.examineInfo.status == 1 : true
      }
      return false
    },
    // 确认名称
    sureBtnTitle() {
      if (this.isOpenExamine) {
        return '提交审核'
      }
      return '保存'
    }
  },
  methods: {
    handleExamineInfo(isSubmit,form){
      //启用了流程则校验 start
      if (!this.examineInfo.checkLagel) {
        this.$message.error("请完善审批信息");
        return false;
      }
      var data = {};
      data.checkUserId = this.examineInfo.checkUserId;
      data.isSubmit = isSubmit;
      data.businessType = this.businessType;
      form.wfVO = data;
      return form;
      //启用了流程则校验 end
    },
    /**
     * 审批信息值更新
     */
    examineValueChange(data) {
      this.examineInfo = data
    },
  }

}
