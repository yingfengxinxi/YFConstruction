<template>
  <xr-create
    :loading="loading"
    title="申报项目详情"
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
                  <el-form-item label="申报项目" prop="declareProject">
                    <el-select v-model="form.declareProject" placeholder="请选择" clearable style="display:block">
                      <el-option v-for="dict in ProjectInfos" :key="dict.id" :label="dict.projectName" :value="dict.id" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="项目经理" prop="projectManager">
                    <el-input v-model="form.projectManager" placeholder="项目经理"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
               <el-row>
                <el-col :span="12">
                  <el-form-item label="建筑规模" prop="architectureScale">
                     <el-select v-model="form.architectureScale" placeholder="请选择" clearable  style="display:block">
                <el-option v-for="dict in constructionDict" :key="dict.dictValue" :label="dict.dictLabel"  :value="dict.dictValue"  />
              </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="总计评分" prop="totalScore">
                    <el-input v-model="form.totalScore" placeholder="总计评分"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="检查结论" prop="InspectionConclusion">
                    <el-input type="textarea" :rows="2" v-model="form.inspectionConclusion" placeholder="请输入检查结论"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                 <el-col :span="12">
                  <el-form-item label="通过时间" prop="reviewEndTime">
                    <el-input v-model="form.reviewEndTime" placeholder="请输入通过时间"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="是否通过" prop="whetherAdopt">
                    <el-select v-model="form.whetherAdopt" placeholder="请选择" clearable style="display:block">
                       <el-option  v-for="dict in IsAdopt" :key="dict.dataValue" :label="dict.dataLable" :value="dict.dataValue" />
                    </el-select>
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
      dataDetatil:{}
    },
    data() {
      return {
        loading: false,
        form: {},
        //tab
        tabCurrentName: 'Detail',
        //是否通过
      IsAdopt: [
        { dataLable: "是", dataValue: "1" },
        { dataLable: "否", dataValue: "2" }
      ],
      //项目列表
      ProjectInfos: [],
      //建设规模
      constructionDict: [],
      }
    },
    computed: {
      //声明有多少Tab
      tabNames() {
        var tempsTabs = [
          { label: '详细资料', name: 'Detail' ,isDetail:true},
          { label: '附件列表', name: 'FilesList' },
          // { label: '变更履历列表', name: 'BgList' },
        ]
        return tempsTabs
      }
    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      this.form=this.dataDetatil.form
       this.ProjectInfos=this.dataDetatil.ProjectInfos
        this.constructionDict=this.dataDetatil.constructionDict
      // getById(this.id)
      //   .then(res => {
      //     console.log(res.data);
      //     this.form = res.data;
      //   })
      //   .catch(() => {
      //   })
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
