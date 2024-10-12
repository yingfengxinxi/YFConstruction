<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">
    <create-sections title="基本信息">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
               class="form">
        <el-row>
          <el-col :span="24">
            <el-form-item label="等级名称" prop="levelName">
              <el-input v-model="form.levelName" placeholder="请输入等级名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="最小分值关系" prop="minRel">
              <el-select v-model="form.minRel" placeholder="请选择最小分值关系">
                <el-option
                  v-for="item in dictData.minRelationDict"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
		  <el-col :span="12">
		    <el-form-item label="最小分值" prop="minData">
		      <el-input type="number" v-model="form.minData" placeholder="请输入最小分值"></el-input>
		    </el-form-item>
		  </el-col>
        </el-row>
		<el-row>
		  <el-col :span="12">
		    <el-form-item label="最大分值关系" prop="maxRel">
		      <el-select v-model="form.maxRel" placeholder="请选择最大分值关系">
		        <el-option
		          v-for="item in dictData.maxRelationDict"
		          :key="item.dictValue"
		          :label="item.dictLabel"
		          :value="item.dictValue">
		        </el-option>
		      </el-select>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="最大分值" prop="maxData">
		      <el-input type="number" v-model="form.maxData" placeholder="请输入最大分值"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="form.remarks" placeholder="请输入备注"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
    </create-sections>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById} from "../api/level"

  export default {
    // 新建编辑
    name: 'PlanCreate',
    components: {
      XrCreate,
      CreateSections,
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
	  // 字典数据
	  dictData: {},
    },
    data() {
      return {
        loading: false,
        form: {
        },
		// 数据默认字段
		defaultProps: {
		  parent: 'parentId',   // 父级唯一标识
		  value: 'id',          // 唯一标识
		  label: 'label',       // 标签显示
		  children: 'children', // 子级
		},

        rules: {
          levelName: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          minRel: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          minData: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
        },
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑等级' : '新建等级'
      },

    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
      if (this.action.type == "update") {
        this.getDetail();
      }
    },
    methods: {
      /**
       * 获取详情
       * */
      getDetail() {
        getById(this.action.id)
          .then(res => {
            this.form = res.data
          })
          .catch(() => {
          })
      },
      /**
       * 保存
       */
      save(isSubmit) {
        this.$refs['form'].validate(valid => {
          if (valid) {
            

            var params = this.form;
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
      close() {
        this.$emit('close')
      },
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
  .el-date-editor, .el-select{
    width: 100%;
  }
</style>
