<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">

    <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
             class="form">
      <create-sections title="基本信息">
        <el-row>
          <el-col :span="12">
            <el-form-item label="描述" prop="descr">
              <el-input type="textarea" :rows="2" v-model="form.descr" placeholder="请输入描述"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标题" prop="descr">
              <el-input type="textarea" :rows="2" v-model="form.sendContent"
                        placeholder="请输入发送内容，@userName@代表提交用户，@title@代表数据项标题"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="Code标识（不可重复）" prop="code">
              <el-tooltip class="item" effect="dark" content="此code是【消息中心】的生产者和消费者之间的传递标记，同时也是WebSocket客户端监听的事件名称，请谨慎操作！若发生改动，
需要同步修改生产和消费的实现。" placement="top-start">
                <el-input v-model="form.code" placeholder="请输入标识" :disabled="disabled"></el-input>
              </el-tooltip>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据隔离级别" prop="keepLevelArr">
              <el-checkbox-group v-model="keepLevelArr" @change="reCheckBox()">
                <el-checkbox :label="'10'">租户级</el-checkbox>
                <el-checkbox :label="'20'">项目级</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否记录消息" prop="insertLog">
              <el-radio-group v-model="form.insertLog">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </create-sections>
      <create-sections title="客户端配置">
        <el-row>
          <el-col :span="12">
            <el-form-item label="点击是否跳转" prop="clickOpen">
              <el-radio-group v-model="form.clickOpen">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型展示图标" prop="icon">
              <icon-change :icon.sync="form.icon"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="showClientConfig">
          <el-col :span="24">
            <el-form-item label="打开页面" prop="url">
              <el-input v-model="form.url" placeholder="请输入打开页面" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </create-sections>
      <create-sections title="MQ通道配置">
        <el-row>
          <el-col :span="12">
            <el-form-item label="启用MQ通道" prop="ifSendMq">
              <el-radio-group v-model="form.ifSendMq">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="showMqConfig">
          <el-col :span="24">
            <el-form-item label="连接交换机" prop="exchangeName">
              <diy-dict v-model="form.exchangeName" sTb="wm_exchanges"
                        sColKey="id" sColVal="name" sColName="descr">
              </diy-dict>
            </el-form-item>
          </el-col>
          <!--<el-col :span="12">
            <el-form-item label="通道标识" prop="channelName">
              <el-input v-model="form.channelName" readonly placeholder="请输入通道标识"></el-input>
            </el-form-item>
          </el-col>-->
        </el-row>
        <el-row v-if="showMqConfig">
          <el-col :span="12">
            <el-form-item label="持久化" prop="durability">
              <el-radio-group v-model="form.durability" :disabled="true">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="自动删除" prop="autoDelete">
              <el-radio-group v-model="form.autoDelete" :disabled="true">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="showMqConfig">
          <el-col :span="24">
            <el-form-item label="扩展参数" prop="arguments">
              <el-input type="textarea" :rows="2" v-model="form.arguments" placeholder="JSON"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </create-sections>
      <create-sections title="即时消息配置">
        <el-row>
          <el-col :span="24">
            <el-form-item label="是否启用Web通道" prop="ifSendWeb">
              <el-radio-group v-model="form.ifSendWeb">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="showWebConfig">
          <el-col :span="24">
            <el-form-item label="Web端推送引擎" prop="webSendEngineArr">
              <el-checkbox-group v-model="webSendEngineArr" @change="reCheckBox()">
                <el-checkbox :label="'10'">SocketIO</el-checkbox>
                <el-checkbox :label="'20'">WebSocket</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="showWebConfig">
          <el-col :span="12">
            <el-form-item label="推送目标类型" prop="targetConfigType">
              <el-radio-group v-model="form.targetConfigType">
                <el-radio label="0" title="接收调用方传入的目标列表进行推送">系统指定</el-radio>
                <el-radio label="1" title="由租户配置推送目标">租户配置</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="showWebConfig">
          <el-col :span="24" v-if="showWebConfig">
            <el-form-item label="推送端" prop="targetsArr">
              <el-checkbox-group v-model="targetsArr" @change="reCheckBox()">
                <el-checkbox :label="'PCM'">PC管理端（PCM）</el-checkbox>
                <el-checkbox :label="'PCSC'">管理端大屏（PCSC）</el-checkbox>
                <el-checkbox :label="'PCSCP'">项目端大屏（PCSCP）</el-checkbox>
                <el-checkbox :label="'APPM'">移动APP端（APPM）</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="同步推送移动设备" prop="ifSendMobile">
              <el-radio-group v-model="form.ifSendMobile">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="showMobileConfig">
          <el-col :span="24">
            <el-form-item label="移动设备消息引擎" prop="mobileSendEngineArr">
              <el-checkbox-group v-model="mobileSendEngineArr">
                <el-checkbox :label="'UNIPUSH'">UniPush（UNIPUSH）</el-checkbox>
                <el-checkbox :label="'DTALK'">钉钉应用提醒（DTALK）</el-checkbox>
                <el-checkbox :label="'SMS'">短信（SMS）</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>

      </create-sections>
    </el-form>

  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import DiyDict from "@/components/common/Dict/diy";
  import IconChange from "@/components/common/IconChange";

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById} from "../api/queues";

  import {XhStrucUserCell} from '@/components/CreateCom'

  export default {
    // 新建编辑
    name: 'QueuesCreate',
    components: {
      XrCreate,
      CreateSections,
      DiyDict,
      XhStrucUserCell,
      IconChange
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
        radio: '',
        loading: false,
        disabled: false,
        form: {},
        defaultValue: {
          type: 'fanout',
          durability: "1",
          autoDelete: "0",
          clickOpen: '0',
          ifSendWeb: "0",
          ifSendMq: "0",
          insertLog: '1',
        },
        targetsArr: [],
        webSendEngineArr: [],
        mobileSendEngineArr: [],
        keepLevelArr: [],
        showClientConfig: false,
        showMqConfig: false,
        showWebConfig: false,
        showMobileConfig: false,
        showMobileConfig: false,
        rules: {
          descr: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          name: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          code: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          clickOpen: [{required: true, message: '必填项不得为空', trigger: 'blur'}],
          icon: [{required: true, message: '请选择图标', trigger: 'blur'}],
          clickOpenType: [{required: true, message: '必填项不得为空', trigger: 'blur'}],
          /*keepLevelArr: [
            {
              required: true, validator: (rule, value, callback) => {
                if (this.keepLevelArr.length < 1) {
                  callback(new Error('必填项不得为空'));
                } else {
                  callback();
                }
              }, trigger: 'blur'
            }
          ],*/
          ifSendMq: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          exchangeName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          durability: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          autoDelete: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          channelName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          ifSendWeb: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          webSendEngineArr: [
            {
              required: true, validator: (rule, value, callback) => {
                if (this.webSendEngineArr.length < 1) {
                  callback(new Error('必填项不得为空'));
                } else {
                  callback();
                }
              }, trigger: 'blur'
            }
          ],
          ifSendMobile: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          mobileSendEngineArr: [
            {
              required: true, validator: (rule, value, callback) => {
                if (this.mobileSendEngineArr.length < 1) {
                  callback(new Error('必填项不得为空'));
                } else {
                  callback();
                }
              }, trigger: 'blur'
            }
          ],
          targetConfigType: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          targetsArr: [
            {
              required: true, validator: (rule, value, callback) => {
                if (this.targetsArr.length < 1) {
                  callback(new Error('必填项不得为空'));
                } else {
                  callback();
                }
              }, trigger: 'blur'
            }
          ],
        },
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑' : '新建'
      },
    },
    watch: {
      'form.clickOpen'() {
        if (this.form.clickOpen == '1') {
          this.showClientConfig = true;
        } else {
          this.showClientConfig = false;
        }
      },
      'form.ifSendMq'() {
        if (this.form.ifSendMq == '1') {
          this.showMqConfig = true;
        } else {
          this.showMqConfig = false;
        }
      },
      'form.ifSendWeb'() {
        if (this.form.ifSendWeb == '1') {
          this.showWebConfig = true;
        } else {
          this.showWebConfig = false;
        }
      },
      'form.ifSendMobile'() {
        if (this.form.ifSendMobile == '1') {
          this.showMobileConfig = true;
        } else {
          this.showMobileConfig = false;
        }
      },
      'form.targetType'() {
        if (this.form.ifSendMobile == '1') {
          this.showMobileConfig = true;
        } else {
          this.showMobileConfig = false;
        }
      }
    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)

      this.form = this.defaultValue;
      if (this.action.type == "update") {
        this.disabled = true;
        this.getDetail();
      }
    },

    mounted() {
    },

    beforeDestroy() {
    },

    methods: {
      reCheckBox() {
        this.$forceUpdate()
      },
      /**
       * 获取详情
       * */
      getDetail() {
        getById(this.action.id)
          .then(res => {
            this.form = res.data;
            this.targetsArr = res.data.targets.split(",") || [];
            this.webSendEngineArr = res.data.webSendEngine.split(",") || [];
            this.mobileSendEngineArr = res.data.mobileSendEngine ? res.data.mobileSendEngine.split(",") || [] : [];
            this.keepLevelArr = res.data.keepLevel ? res.data.keepLevel.split(",") || [] : [];
            console.log(this.form)
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
            //处理复选
            params.targets = this.targetsArr.join(",");
            params.webSendEngine = this.webSendEngineArr.join(",");
            params.mobileSendEngine = this.mobileSendEngineArr.join(",");
            params.keepLevel = this.keepLevelArr.join(",");
            this.loading = true;
            saveOrUpdate(params).then(response => {
              if (response.code === 0) {
                this.$message.success(
                  this.action.type == 'update' ? '编辑成功' : '添加成功'
                )
                this.close()
                // 保存成功b
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
  .el-form-item__label {
    padding: 0px;
  }

  .form
  /deep/
  .el-date-editor {
    width: 100%;
  }
</style>
