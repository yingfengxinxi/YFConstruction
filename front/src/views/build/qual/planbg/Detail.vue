<template>
  <xr-create
    :loading="loading"
    title="质量方案变更详情"
    @close="close"
    :showSave="false">
    <div class="d-container-bd" style="height: 100%">
      <el-tabs
        v-model="tabCurrentName"
        type="border-card"
        class="d-container-bd--left">
        <el-tab-pane
          v-for="(item, index) in tabNames"
          :key="index"
          :label="item.label"
          :name="item.name"
          lazy>
          <create-sections v-if="item.isDetail" title="基本信息">
            <el-form :disabled="true" ref="form" :model="form"  label-position="top" label-width="100px"
                     class="form">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="变更方案" prop="planId">
                    <diy-dict v-model="form.planId" sTb="b_qual_plan"
                              sColKey="id" sColVal="id" sColName="name">
                    </diy-dict>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="变更原因" prop="reason">
                    <el-input v-model="form.reason" placeholder="请输入变更原因"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="内容简述" prop="content">
                    <el-input type="textarea" :rows="2" v-model="form.content" placeholder="请输入内容简述"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="变更人" prop="bgBy">
                    <el-input v-model="form.bgBy" placeholder="请输入变更人"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="变更日期" prop="bgDate">
                    <el-date-picker
                      v-model="form.bgDate"
                      type="date"
                      placeholder="请选择变更日期"
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd">
                    </el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </create-sections>
          <component
            v-else
            :is="item.name"
            :batchId="form.batchId"/>
        </el-tab-pane>
      </el-tabs>
    </div>

  </xr-create>
</template>

<script>
  import FilesList from '@/components/common/FilesList' //附件列表
  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import {debounce} from 'throttle-debounce'

  import {getById} from "../api/planbg";

  import DiyDict from "@/components/common/Dict/diy";

  export default {
    // 新建编辑
    name: 'PlanBgDetail',
    components: {
      XrCreate,
      CreateSections,
      FilesList,
      DiyDict
    },
    //mixins: [ExamineMixin],//业务审批Copy:审批
    props: {
      // 详情信息id
      id: [String, Number],
    },
    data() {
      return {
        loading: false,
        form: {},
        //tab
        tabCurrentName: 'Detail',
        //附件
      }
    },
    computed: {
      //声明有多少Tab
      tabNames() {
        var tempsTabs = [
          { label: '详细资料', name: 'Detail' ,isDetail:true},
          { label: '附件列表', name: 'FilesList' },
        ]
        return tempsTabs
      }
    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      getById(this.id)
        .then(res => {
          this.form = res.data;
        })
        .catch(() => {
        })
    },

    mounted() {
    },

    beforeDestroy() {
    },

    methods: {

      /**
       * 关闭
       */
      close() {
        console.log("close")
        this.$emit('close')
      }
    }
  }
</script>

<style lang="scss" scoped>
  .form
  /deep/
  .el-form-item
  .el-form-item__content {
    width: calc(100% - 10px);
  }

  .form
  /deep/
  .el-form-item__label {
    padding: 0px;
  }
  .form
  /deep/
  .el-date-editor{
    width: 100%;
  }
  @import '@/views/crm/styles/crmdetail.scss';
</style>
