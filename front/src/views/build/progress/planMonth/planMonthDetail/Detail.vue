<template>
  <slide-view
    :body-style="{padding: 0, height: '100%'}"
    xs-empty-icon="nopermission"
    xs-empty-text="暂无权限"
    @afterEnter="getDetial"
    @close="hideView">
    <div
      v-loading="loading"
      ref="crmDetailMain"
      class="detail-main">
      <flexbox
        direction="column"
        align="stretch"
        class="d-container">
        <div class="container">
          <flexbox
            class="t-section"
            align="stretch">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'   
              :src="crmIcon"
              class="t-section__hd">
            <div class="t-section__bd">
              <div class="type-name">DEMO</div>
              <p class="name"><el-tooltip
                :disabled="!name"
                :content="name"
                effect="dark"
                placement="top-start">
                <span>{{ name }}</span>
              </el-tooltip><slot name="name" /></p>
            </div>
            <div class="t-section__ft">
              <el-button
                v-if="canHandle('edit')"
                class="head-handle-button xr-btn--green"
                icon="wk wk-circle-edit"
                type="primary"
                @click.native="handleTypeClick('edit')">编辑</el-button>
              <el-dropdown
                trigger="click"
                @command="handleTypeClick">
                <el-button
                  icon="el-icon-more"
                  class="t-more" />
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item
                    v-for="(item, index) in moreTypes"
                    :key="index"
                    v-if="canHandle(item.type)"
                    :icon="item.icon | wkIconPre"
                    :command="item.type">{{ item.name }}</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </flexbox>
          <flexbox
            class="h-section"
            align="stretch">
            <flexbox-item
              v-for="(item, index) in headDetails"
              v-show="item.title"
              :key="index"
              class="h-item"
              span="200">
              <div class="h-title">{{ item.title }}</div>
              <div class="h-value text-one-line"><i
                v-if="item.showIcon"
                :style="item.style"
                :class="item.icon" />{{ item.value }}</div>
            </flexbox-item>
          </flexbox>
          <slot />
        </div>
        <!-- 流程审批start -->
        <examine-info
          v-if="detailData.examineRecordId"
          :id="id"
          :record-id="detailData.examineRecordId"
          :owner-user-id="detailData.ownerUserId"
          class="examine-info"
          @on-handle="examineHandle" />
        <!-- 流程审批 end -->
        <div class="d-container-bd">
          <el-tabs
            v-model="tabCurrentName"
            type="border-card"
            class="d-container-bd--left">
            <el-tab-pane
              v-for="(item, index) in tabNames"
              :key="index"
              :label="item.label"
              :name="item.name"
              style="overflow: auto;"
              lazy>
              <el-form v-if="item.isDetail" :disabled="true" ref="form" :model="detailData" label-position="top" label-width="100px"
                      class="form">
                <create-sections title="基本信息">

                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="任务名称" prop="name">
                        <el-input v-model="detailData.name" placeholder="请输入"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="关联楼座" prop="relatedBuildingId">
                        <el-select
                        
                          v-model="detailData.relatedBuildingId"
                          placeholder="请选择"
                          clearable
                          size="small"
                          style="display:block">
                          <el-option
                              v-for="dict in loftList"
                              :key="dict.id"
                              :label="dict.building"
                              :value="dict.id" />
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="是否里程碑" prop="ifMilestone">
                        <el-select
                          v-model="detailData.ifMilestone"
                          placeholder="请选择"
                          clearable
                          size="small"
                          style="display:block">
                          <el-option
                              v-for="dict in ifMilestoneOptions"
                              :key="dict.dictValue"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="计划起日"  prop="planStartDate">
                        <el-date-picker
                          class="date"
                          v-model="detailData.planStartDate"
                          type="date"
                          placeholder="请选择"
                          align="right"
                          value-format="yyyy-MM-dd">
                        </el-date-picker>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="计划至日"  prop="planEndDate">
                        <el-date-picker
                          class="date"
                          v-model="detailData.planEndDate"
                          type="date"
                          placeholder="请选择"
                          align="right"
                          value-format="yyyy-MM-dd">
                        </el-date-picker>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="计划天数"  prop="planGqiNumber">
                        <el-input type="number" v-model="detailData.planGqiNumber" placeholder="请输入"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="计划完成比例%"  prop="planFinishPersent">
                        <el-input type="number" v-model="detailData.planFinishPersent" placeholder="请输入"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="实际起日" prop="actualStartDate">
                        <el-date-picker
                          v-model="detailData.actualStartDate"
                          type="date"
                          placeholder="请选择"
                          format="yyyy-MM-dd"
                          value-format="yyyy-MM-dd">
                        </el-date-picker>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="实际至日" prop="actualEndDate">
                        <el-date-picker
                          class="date"
                          v-model="detailData.actualEndDate"
                          type="date"
                          placeholder="请选择"
                          align="right"
                          value-format="yyyy-MM-dd">
                        </el-date-picker>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="实际天数" prop="actualGqiNumber">
                        <el-input type="number" v-model="detailData.actualGqiNumber" placeholder="请输入"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="实际完成比例%" prop="actualFinishPersent">
                        <el-input type="number" v-model="detailData.actualFinishPersent" placeholder="请输入"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="延误天数" prop="delayDays">
                        <el-input type="number" v-model="detailData.delayDays" placeholder="请输入"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="延期责任" prop="delayDuty">
                      
                        <el-select
                          v-model="detailData.delayDuty"
                          placeholder="请选择"
                          clearable
                          size="small"
                          style="display:block">
                          <el-option
                              v-for="dict in  delayDutyOptions"
                              :key="dict.dictValue"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="责任单位" prop="responsibleParty">
                        <el-input v-model="detailData.responsibleParty" placeholder="请输入"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="延期原因" prop="delayReason">
                        <el-select
                          v-model="detailData.delayReason"
                          placeholder="请选择"
                          clearable
                          size="small"
                          style="display:block">
                          <el-option
                              v-for="dict in  delayReasonOptions"
                              :key="dict.dictValue"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-row>
                    <el-col :span="24">
                      <el-form-item label="填报说明" prop="fillInDescription">
                        <el-input type="textarea" :rows="2" v-model="detailData.fillInDescription" placeholder="请输入"></el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="24">
                      <el-form-item label="延期原因描述" prop="delayReason">
                        <el-input type="textarea" :rows="2" v-model="detailData.delayReason" placeholder="请输入"></el-input>
                      </el-form-item>
                    </el-col>
                    
                  </el-row>
                  <el-row>
                    <el-col :span="24">
                      <el-form-item label="采取措施" prop="measures">
                        <el-input type="textarea" :rows="2" v-model="detailData.measures" placeholder="请输入"></el-input>
                      </el-form-item>
                    </el-col>
                    
                  </el-row>

                </create-sections>
              </el-form>
              <component
                v-else
                :is="item.name"
                :batchId="detailData.batchId"/>
            </el-tab-pane>
          </el-tabs>
          
        </div>
      </flexbox>
    </div>

    <Update
      v-if="isCreate"
      :businessType="businessType"
      :id="id"
      @save-success="editSaveSuccess"
      @hiden-view="isCreate=false" />
  </slide-view>
</template>

<script>
import {mapGetters} from 'vuex'//权限

import SlideView from '@/components/SlideView'
import Update from './Update' // 修改页面

import FilesList from '@/components/common/FilesList' //附件列表

import CreateSections from '@/components/CreateSections'

import {selectById} from '@/views/build/progress/api/planMonth'
import {selectLoft } from '@/views/build/progress/api/planCreate'

//流程审批
import ExamineInfo from '@/components/common/examine/examine/form'

export default {
  // 详情
  name: 'DemoDetail',
  components: {
    SlideView,
    Update,

    FilesList,
    ExamineInfo,
    CreateSections
  },
  props: {
    // 详情信息id
    id: [String, Number],
    businessType: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      // 展示加载loading
      loading: false,
      moreTypes: [
        {type: 'import', name: '导入', icon: 'import'},
        {type: 'export', name: '导出', icon: 'export'},
      ],//更多操作
      // 名称
      name: '',
      headDetails: [
        { title: '内容', value: '' },
        { title: '备注', value: '' },
      ],
      tabCurrentName: 'Detail',
      // 编辑操作
      isCreate: false,
      detailData:{},

      loftList:[],
      ifMilestoneOptions:[],//是否里程碑字典项
      delayDutyOptions:[],//延期责任
      delayReasonOptions:[],//延期原因

    }
  },
  computed: {
    //权限 start
    ...mapGetters(['allAuth']),
    crmIcon() {
      return require(`@/assets/img/crm/expense.png`)
    },
    //声明有多少Tab
    tabNames() {
      var tempsTabs = [
        {label: '详细资料', name: 'Detail', isDetail: true},
        {label: '附件列表', name: 'FilesList'},
      ]
      return tempsTabs
    }
  },
  created(){
      //  是否里程碑字典项
			this.getDicts('sys_yes_no').then(response => {
				this.ifMilestoneOptions = response.data
      })

      // 延期责任
      this.getDicts('b_progress_delay_duty').then(response => {
				this.delayDutyOptions = response.data
      })
      //延期原因
      this.getDicts('b_progress_delay_reason').then(response => {
				this.delayReasonOptions = response.data
      })

  },

  mounted() {},
  methods: {
    /**
     * 审核操作完成后的钩子
     */
    examineHandle() {
      this.$emit('handle', { type: 'examine' })
    },
    /**
     * 详情
     */
    getDetial() {
      this.loading = true
      selectById(this.id)
        .then(res => {
          this.loading = false
          this.name = res.data.number
          this.detailData = res.data
          //   // 负责人
          this.headDetails[0].value = res.data.fillInDescription
          this.headDetails[1].value = res.data.note

          this.form = res.data;
        })
        .catch(() => {
          this.loading = false
          this.hideView()
        })
    },
    handleTypeClick(type){
      if(type == 'edit'){//编辑
        this.isCreate = true;
      }
    },
    //按钮操作权限
    canHandle(type) {
      return this.allAuth.demo.wf[type];
    },
    editSaveSuccess(){
      this.getDetial();
      this.$emit('handle');
    },
    /**
     * 关闭
     */
    hideView() {
      this.$emit('close')
    },

    //查询 关联楼层下拉 
			selectLoft(){
				selectLoft().then(res=>{
					
					this.loftList = res.data

					
				})
      },
      
  }
}
</script>

<style lang="scss" scoped>
  //头部
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

  .form{
    margin-left: 10px;
  }
  .form
  /deep/
  .el-form-item
  .el-form-item__content {
    width: calc(100% - 10px);
  }
  .form
  /deep/
  .el-form-item__label{
    padding: 0px;
  }
  .el-date-editor.el-input, .el-date-editor.el-input__inner{
    width: calc(100% - 10px);
  }

@import '@/views/crm/styles/crmdetail.scss';
</style>
