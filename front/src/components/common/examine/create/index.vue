<template>
  <create-sections
    v-if="isOpenExamine"
    title="审核信息">
    <template slot="header">
      <div
        v-if="examineInfoAll.examineType===1 || examineInfoAll.examineType===2"
        class="examine-type">{{ examineInfoAll.examineType===1 ? '固定审批流' : '授权审批人' }}</div>
      <el-tooltip
        v-if="examineInfoAll && examineInfoAll.remarks"
        :content="examineInfoAll.remarks"
        effect="dark"
        placement="top">
        <i class="wk wk-help wk-help-tips" style="height: 13px;"/>
      </el-tooltip>
    </template>
    <create-examine-info
      :businessType="businessType"
      @value-change="examineValueChange" />
  </create-sections>
</template>

<script>

  import CreateSections from '@/components/CreateSections'
  import CreateExamineInfo from '../components/CreateExamineInfo'

  export default {
    // 新建编辑
    name: 'ExamineCreate',

    components: {
      CreateSections,
      CreateExamineInfo,
    },

    props: {
      // 业务类型
      businessType: {
        type: Number,
        default: -1
      },
    },

    data() {
      return {
        loading: false,
        // 审批类型、审批信息
        examineInfoAll: {}
      }
    },

    computed: {
      // 合同 回款 下展示审批人信息
      isOpenExamine() {
        if (this.examineInfoAll) {
          // 初始状态是空对象默认展示，请求之后，根据status判断
          return Object.keys(this.examineInfoAll).length > 0 ? this.examineInfoAll.status == 1 : true
        }
        return false
      },
    },

    watch: {},

    created() {

    },

    mounted() {},

    beforeDestroy() {},

    methods: {
      /**
       * 审批信息值更新
       */
      examineValueChange(data) {
        this.examineInfoAll = this.checkLagel(data);
        this.$emit('value-change', data)
      },
      checkLagel(data){
        if (data.examineType === 2) {
          if(data.value.length > 0 && data.value[0].userId){
            data.checkLagel = true;
            data.checkUserId = data.value[0].userId;
          }else{
            data.checkLagel = false;
          }
        }else{
          data.checkLagel = true;
        }
        return data;
      },
    }
  }
</script>

<style lang="scss" scoped>
  .wk-form {
    /deep/ .el-form-item.is-product {
      flex: 0 0 100%;
    }
  }
</style>
