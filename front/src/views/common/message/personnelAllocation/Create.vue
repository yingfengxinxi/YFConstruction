<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">

    <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
             class="form">
      <el-row>
        <el-col :span="12">
          <el-form-item label="推送类型" prop="targetType">
            <el-radio-group v-model="form.targetType">
              <el-radio label="0" title="组织机构内所有用户">全部</el-radio>
              <el-radio label="1" title="配置消息接收的人员或部门">指定</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row v-if="showTargets">
        <el-col :span="24">
          <el-form-item label="推送目标" prop="targets">
            <xh-struc-user-cell
              :max="9"
              :value="null"
              :users="form.userList"
              :strucs="form.deptList"
              style="width: 100%;"
              @value-change="strcUserChange"/>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import {XhStrucUserCell} from '@/components/CreateCom'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getByCode} from "../api/assignuser";

  export default {
    // 新建编辑
    name: 'QueuesCreate',
    components: {
      XrCreate,
      XhStrucUserCell
    },
    props: {
      code: {
        type: String,
        default: '-1',
      }
    },
    data() {
      return {
        loading: false,
        disabled: false,
        form: {
          deptList: [],
          userList: [],
        },
        rules: {
          targetType: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          targets: [
            {
              required: true, message: '必填项不得为空', validator: (rule, value, callback) => {
                if (this.form.deptList.length < 1 && this.form.userList.length < 1) {
                  callback(new Error('必填项不得为空'));
                } else {
                  callback();
                }
              }, trigger: 'blur'
            }
          ],
        },
        showTargets: false,
      }
    },
    computed: {
      title() {
        return '推送配置'
      },

    },
    watch: {
      'form.targetType'() {
        if (this.form.targetType == '1') {
          this.showTargets = true;
        } else {
          this.showTargets = false;
        }
      },
    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)

      this.getDetail();
    },

    mounted() {
    },

    beforeDestroy() {
    },

    methods: {
      // 关闭按钮
      strcUserChange(data) {
        this.form.deptList = data.value.strucs;
        this.form.userList = data.value.users;
      },
      /**
       * 获取详情
       * */
      getDetail() {
        getByCode(this.code)
          .then(res => {
            const data = res.data
            this.form = data;
          })
          .catch(() => {
          })
      },
      /**
       * 保存
       */
      save() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            var params = this.form;
            params.code = this.code;
            this.loading = true;
            saveOrUpdate(params).then(response => {
              if (response.code === 0) {
                this.$message.success('配置成功')
                this.close()
                // 保存成功
                this.$emit('save-success')
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
  .el-date-editor {
    width: 100%;
  }

  /deep/ .crm-create-card-container {
    height: 70%;
  }
</style>
