<template>
  <xr-create :loading="loading" :title="title" :confirm-button-text="'保存'" @close="close" @save="debouncedSaveField(true)">
    <create-sections title="基本信息">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px" class="form">
        <el-row>
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templateName">
              <el-input v-model="form.templateName" placeholder="请输入模板名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="验收工序" prop="acceptWork">
              <el-select v-model="form.acceptWork" placeholder="请选择验收工序" clearable style="width: 400px">
                <el-option v-for="dict in acceptWorks" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
    </create-sections>
    <create-sections title="模板内容明细">
      <hidden-quality-items :action="action" ref="hiddenQualityItems"></hidden-quality-items>
    </create-sections>
  </xr-create>
</template>

<script>

import XrCreate from '@/components/XrCreate'
import CreateSections from '@/components/CreateSections'
import { guid } from '@/utils'
import { debounce } from 'throttle-debounce'
import HiddenQualityItems from "./components/HiddenQualityItems";

import { saveOrUpdate, queryById } from "../api/acceptTemplateSet";
export default {
  // 新建编辑
  name: 'acceptTemplateSetCreate',
  components: {
    XrCreate,
    CreateSections,
    HiddenQualityItems
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
    titleName: '',
  },
  data () {
    return {
      loading: false,
      form: {
      },
      batchId: '',

      rules: {
        templateName: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        acceptWork: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ]
      },
      //验收工序字典项
      acceptWorks: []
    }
  },
  computed: {
    title () {
      if (this.action.type === 'show') {
        return this.titleName
      } else {
        return this.action.type === 'update' ? '编辑' + this.titleName : '新建' + this.titleName
      }
    },

  },
  watch: {},
  created () {
    this.debouncedSaveField = debounce(300, this.save)
    //考虑附件ID
    if (this.action.type == "update") {
      this.getDetail();
    }
    this.getDicts("b_qual_accept_work").then(response => {
      this.acceptWorks = response.data;
    });
  },

  mounted () {
  },

  beforeDestroy () {
  },

  methods: {
    /**
     * 获取详情
     * */
    getDetail () {
      queryById(this.action.id)
        .then(res => {
          // this.form = res.data;
          console.log(res)
           this.form = res.data.templateForm;
          // // 绑定隐患
          // this.hiddenTroubleList = res.data.templateList;
          // 将数据传到子控件
          this.$refs.hiddenQualityItems.fieldFormAll =  res.data.templateList;
          // 生成页对象
          var pageAllItems = [];
          res.data.templateList.forEach(item => {
            pageAllItems.push({});
          });
          // 将数据传到子控件
          this.$refs.hiddenQualityItems.pageAllItems = pageAllItems;
        })
        .catch(() => {
        })
    },
    /**
     * 保存
     */
    save (isSubmit) {
      
      this.$refs['form'].validate(valid => {
          if (!this.$refs.hiddenQualityItems.startCheck()) {
             this.loading = false;
            this.$message.error( "请完善模板内容信息"  )
            return;
          }
        if (valid) {
         
        var fieldFormAll = this.$refs.hiddenQualityItems.fieldFormAll;
          if (fieldFormAll.length <= 0) {
             this.loading = false;
            this.$message({ type: "error", message: "请添加模板内容" });
            return;
          }
          var params = {
            templateForm: this.form,
            templateList: fieldFormAll
          };
          console.log(params);
          // return ;
           this.loading = true;
          saveOrUpdate(params).then(response => {
            if (response.code === 0) {
              this.$message.success(
                this.action.type == 'update' ? '编辑成功' : '添加成功'
              )
              this.close()
              // 保存成功
              this.$emit('save-success', {
                type: 'receivables'
              })
            } else {
              this.$message.error(
                response.msg
              )
            }
            this.loading = false;
          }).catch(err => {
            this.loading = false;
          })

        }
      })
    },
    /**
     * 关闭
     */
    close () {
      console.log("close")
      this.$emit('close')
    }
  }
}
</script>

<style lang="scss" scoped>
.form /deep/ .el-form-item .el-form-item__content {
  width: calc(100% - 10px);
}

.form /deep/ .el-form-item__label {
  padding: 0px;
}
.form /deep/ .el-date-editor {
  width: 100%;
}
</style>
