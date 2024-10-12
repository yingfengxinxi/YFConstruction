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
            <el-form-item label="描述" prop="descr">
              <el-input v-model="form.descr" placeholder="请输入描述"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="名称（MQ）" prop="name">
              <el-tooltip class="item" effect="dark" content="交换名称是【MQ】中的唯一标识，用于识别交换机" placement="top-start">
                <el-input v-model="form.name" placeholder="请输入名称" :disabled="disabled"></el-input>
              </el-tooltip>

            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="类型" prop="type">
              <el-radio-group v-model="form.type">
                <el-radio label="fanout">扇形</el-radio>
                <el-radio label="direct">直连</el-radio>
                <el-radio label="topic">通配</el-radio>
                <el-radio label="headers">头部</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="持久化" prop="durability">
              <el-radio-group v-model="form.durability">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="自动删除" prop="autoDelete">
              <el-radio-group v-model="form.autoDelete">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内部使用" prop="internal">
              <el-radio-group v-model="form.internal">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="扩展参数" prop="arguments">
              <el-input type="textarea" :rows="2" v-model="form.arguments" placeholder="JSON"></el-input>
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

  import {saveOrUpdate, getById} from "../api/exchanges";

  export default {
    // 新建编辑
    name: 'ExchangesCreate',
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
      }
    },
    data() {
      return {
        loading: false,
        disabled:false,
        form: {
        },
        defaultValue:{
          type:'fanout',
          durability:"1",
          autoDelete:"0",
          internal:"0",
          isSys:"0",
        },
        batchId:'',

        rules: {
          descr: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          name: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          type: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          durability: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          autoDelete: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          internal: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          isSys: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
        },
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑' : '新建'
      },

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
      if (this.action.type == "update") {
        this.disabled = true;
        this.getDetail();
      }else {
        this.form = this.defaultValue;
      }
    },

    mounted() {
    },

    beforeDestroy() {
    },

    methods: {
      /**
       * 获取详情
       * */
      getDetail() {
        getById(this.action.id)
          .then(res => {
            this.form = res.data;
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
            this.loading = true;
            saveOrUpdate(params).then(response => {
              if (response.code === 0) {
                this.$message.success(
                  this.action.type == 'update' ? '编辑成功' : '添加成功'
                )
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
</style>
