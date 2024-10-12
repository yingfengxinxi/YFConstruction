<template>

   
      <el-tabs
        v-model="tabCurrentName"
        type="border-card"
		@tab-click="handleClick"
        class="d-container-bd--left">
        <el-tab-pane label="AI应用总览" name="applicationOverview">
             <applicationOverview v-if="nowPage == '0'" style="height: 100%;"></applicationOverview>
         </el-tab-pane>
          <el-tab-pane label="AI设备" name="equipmentAI">
             <equipmentAI v-if="nowPage == '1'"></equipmentAI>
         </el-tab-pane>
          
      </el-tabs>
   


</template>

<script>

  import {mapGetters} from 'vuex'//权限
  import Lockr from 'lockr'//分页
  import equipmentAI from './equipmentAI/equipmentAI.vue'
  import applicationOverview from './applicationOverview/applicationOverview'

  export default {
    /** DEMO列表 */
    name: 'cermanage',
    components: {
      equipmentAI,
      applicationOverview,
    },
    data() {
      return {
        //tab
        tabCurrentName: 'applicationOverview',
        loading: false, // 加载动画
        /** 列表 */
        tableHeight: document.documentElement.clientHeight - 320, // 表的高度
		nowPage: '0',

      }
    },
    computed: {
      //权限 start
      ...mapGetters(['allAuth']),
      //权限 end
      titleIcon() {
        return require(`@/assets/img/crm/customer.png`)
      },
    },
    mounted() {
      // this.initData(this.$route)
      /** 控制table的高度 */
      window.onresize = () => {
        this.updateTableHeight()
      }
    },
    watch:{
      
    },
    methods: {   
      //按钮操作权限
      canHandle(type) {
        return this.allAuth.demo ? this.allAuth.demo.wf ? this.allAuth.demo.wf[type] : false : false;
      },
	  handleClick(tab, event) {
		   this.nowPage = tab.index
	  },

    }
  }
</script>

<style lang="scss" scoped>
.d-container-bd--left{
	height: 100%;
}
  //表头
  .table-head-container {
    //border-bottom: 1px solid #e6e6e6;
    border-top: 1px solid #e6e6e6;
    background-color: white;
  }

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

  @import '@/views/styles/table.scss';
  /deep/.el-tabs__content{
  	height: calc(100% - 40px);
  }
  /deep/#pane-applicationOverview{
  	height: 100%;
  }
</style>
