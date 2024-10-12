<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    :showSave="!isDetail"
    @close="close"
    @save="debouncedSaveField(true)">
    <create-sections title="基本信息">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
               class="form" :disabled="isDetail">
        <el-row>
          <el-col :span="24">
            <el-form-item label="配置名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入平台名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="配置类型" prop="type">
              <el-select v-model="form.type" clearable placeholder="请选择配置类型">
                <el-option  label="平台对接" value="0"></el-option>
                <el-option  label="报文TCP" value="1"></el-option>
                <el-option  label="报文UDP" value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="showSecret">
            <el-form-item label="密钥" prop="secret">
              <el-input v-model="form.secret" placeholder="请输入数据密匙"></el-input>
              <el-button size="mini" round @click="setUUID">随机生成</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="showPort">
            <el-form-item label="传输端口（10000 - 60000）" prop="port">
              <el-input-number v-model="form.port" controls-position="right"
                               :min="10000" :max="60000" placeholder="传输端口" ></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="设备标识解析器" prop="resCode">
              <el-input v-model="form.resCode" placeholder="请输入设备标识解析器"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="默认设备解析器" prop="resCode">
              <el-input v-model="form.defEqResolver" placeholder="请输入默认设备解析器"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="IP白名单" prop="ips">
              <el-input v-model="form.ips" placeholder="支持通配符，多个IP使用英文逗号间隔"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="form.remarks" placeholder=""></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!--<el-row>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择是否启用">
                <el-option
                  v-for="item in enableOption"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>-->
      </el-form>
    </create-sections>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import SelectTree from '@/components/SelectTree'
  import CreateSections from '@/components/CreateSections'

  import WkUploadFile from '@/components/CreateCom/WkUploadFile'

  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById} from "../api/platform"

  export default {
    // 新建编辑
    name: 'PlanCreate',
    components: {
      XrCreate,
      CreateSections,
      WkUploadFile,
      SelectTree
    },
    //mixins: [ExamineMixin],//业务审批Copy:审批
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
    },
    data() {
      return {
        loading: false,
        form: {},
        // 数据默认字段
        defaultProps: {
          parent: 'parentId',   // 父级唯一标识
          value: 'id',          // 唯一标识
          label: 'label',       // 标签显示
          children: 'children', // 子级
        },
        pickerOptions: { // 限制收货时间不让选择今天以前的
          disabledDate(time) {
            return time.getTime() < Date.now()
          }
        },
        // 是否启用
        enableOption: [
          {
            label: '启用',
            value: '1'
          },
          {
            label: '停用',
            value: '0'
          }
        ],

        rules: {
          name: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          type:[
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          port:[
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          secret: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          resCode: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
        },
        //
        showSecret:false,
        showPort:false,
        isDetail:false,
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑' : '新建'
      },

    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
      if (this.action.type == "update"||this.action.type == "detail") {
        if (this.action.type == "detail") {

          this.isDetail = true;
        }
        this.getDetail();
      }

    },
    watch: {
      'form.type': function(value){
        if(value == '1'||value == '2'){
          this.showPort = true;
          this.showSecret = false;
        }
        if(value == '0'){
          this.showPort = false;
          this.showSecret = true;
        }
      }
    },
    methods: {
      setUUID(){
        var s = [];
        var hexDigits = '0123456789abcdef';
        for (var i = 0; i < 36; i++) {
          s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = '4';
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
        s[8] = s[13] = s[18] = s[23] = '-';
        this.$set(this.form,'secret',s.join(''))
      },
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
  .el-date-editor, .el-select {
    width: 100%;
  }
</style>
