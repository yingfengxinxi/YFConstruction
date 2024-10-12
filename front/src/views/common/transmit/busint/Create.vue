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
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
<!--        <el-row>
          <el-col :span="24">
            <el-form-item label="类型" prop="type">
              <el-input v-model="form.type" placeholder="请输入名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row> -->
		<el-row>
		  <el-col :span="24">
		    <el-form-item label="描述" prop="describes">
		      <el-input type="textarea" :rows="2" v-model="form.describes" placeholder="请输入描述"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>
		<el-row>
		  <el-col :span="24">
		    <el-form-item label="响应实例" prop="resdemo">
		      <el-input v-model="form.resdemo" placeholder="请输入响应实例JSON字符串"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>
		<el-row>
		  <el-col :span="24">
		    <el-form-item label="执行器" prop="executor">
		      <el-input  v-model="form.executor" placeholder="请输入执行接口全包名"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>
		<el-row>
		  <el-col :span="24">
		    <el-form-item label="方法" prop="function">
		      <el-input v-model="form.function" placeholder="请输入service方法名"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>
		<el-row>
		  <el-col :span="12">
		    <el-form-item label="状态" prop="status">
		      <el-switch
		        style="display: block"
		        v-model="form.status"
		        active-color="#13ce66"
		        active-text="启用"
		        inactive-text="停用"
				active-value="1"
				inactive-value="0">
		      </el-switch>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="是否安装" prop="hasIns">
		      <el-switch
		        style="display: block"
		        v-model="form.hasIns"
		        active-color="#13ce66"
		        active-text="是"
		        inactive-text="否"
				active-value="1"
				inactive-value="0">
		      </el-switch>
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
  import {saveOrUpdate, getById} from "../api/busint";
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
       
        },
        batchId:'',

        rules: {
          name: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          describe: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          function: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          executor: [
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
