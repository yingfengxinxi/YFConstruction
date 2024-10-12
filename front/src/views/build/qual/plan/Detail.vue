<template>
  <xr-create
    :loading="loading"
    title="质量方案详情"
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
                  <el-form-item label="编号" prop="code">
                    <el-input v-model="form.code" placeholder="请输入质量方案编号"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入质量方案名称"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="描述" prop="content">
                    <el-input type="textarea" :rows="2" v-model="form.content" placeholder="请输入质量方案描述"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="编制日期" prop="enactDate">
                    <el-date-picker
                      v-model="form.enactDate"
                      type="date"
                      placeholder="请选择编制日期"
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd">
                    </el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="编制人" prop="enactBy">
                    <el-input v-model="form.enactBy" placeholder="请输入编制人"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </create-sections>
          <component
            v-else
            :is="item.name"
            :batchId="form.batchId"
            :id="form.id"/>
        </el-tab-pane>
      </el-tabs>
    </div>

  </xr-create>
</template>

<script>
  import FilesList from '@/components/common/FilesList' //附件列表

  import BgList from '../planbg/List' //变更列表

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import {debounce} from 'throttle-debounce'

  import {getById} from "../api/plan";

  export default {
    // 新建编辑
    name: 'DemoCreate',
    components: {
      XrCreate,
      CreateSections,
      FilesList,
      BgList
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
          { label: '变更履历列表', name: 'BgList' },
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
