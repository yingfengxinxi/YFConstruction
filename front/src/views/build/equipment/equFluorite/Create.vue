<template>
  <xr-create :loading="loading" :title="title" :confirm-button-text="'保存'" @close="close"
             @save="debouncedSaveField(true)">
    <create-sections title="基本信息">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px" class="form">
        <el-row>
          <el-col :span="12">
            <el-form-item label="NVR序列号" prop="deviceId">
              <el-input v-model="form.deviceId" :disabled="onDisabled" placeholder="请输入NVR序列号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="NVR license" prop="nvrLicense">
              <el-input v-model="form.nvrLicense" :disabled="onDisabled" placeholder="请输入NVR license"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="IPC序列号" prop="deviceChannelId">
              <el-input v-model="form.deviceChannelId" :disabled="onDisabled" placeholder="请输入IPC序列号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="IPC license" prop="ipcLicense">
              <el-input v-model="form.ipcLicense" :disabled="onDisabled" placeholder="请输入IPC license"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="主设备序列号" prop="deviceSerial">
              <el-input v-model="form.deviceSerial" :disabled="true" placeholder="请输入主设备序列号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="AI监测类型" prop="aiMonitorType">
              <el-select style="width: 100%;" @change="monitorTypeChange" multiple v-model="form.aiMonitorType"
                         placeholder="请选择AI监测类型">
                <el-option v-for="item in monitorTypeDict" :key="item.dictValue" :label="item.dictLabel"
                           :value="item.dictValue">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="关联设备编号" prop="assetCode">
              <el-input v-model="form.assetCode" :disabled="true" placeholder="请输入关联设备编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联设备名称" prop="assetName">
              <el-input v-model="form.assetName" :disabled="onDisabled" placeholder="请输入关联设备名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="ezopen播放地址" prop="pointCode">
              <el-input v-model="form.url" :disabled="onDisabled" placeholder="请输入ezopen播放地址"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="flv播放地址" prop="ysyFlvUrl">
              <el-input v-model="form.ysyFlvUrl" :disabled="onDisabled" placeholder="请输入flv播放地址"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="m3u8播放地址" prop="wvpOpenUrl">
              <el-input v-model="form.wvpOpenUrl" :disabled="onDisabled" placeholder="请输入m3u8播放地址"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="4">
            <el-form-item label="通道状态" prop="status">
              <el-switch v-model="form.status" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="是否启用" prop="isEnable">
              <el-switch v-model="form.isEnable" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="是否吊钩" prop="hookCheckBox">
              <el-switch v-model="form.hookCheckBox" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="是否云台控制" prop="ptzCheckBox">
              <el-switch v-model="form.ptzCheckBox" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="是否是AI设备" prop="isAiEquipment">
              <el-switch v-model="form.isAiEquipment" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
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
  import {getById, saveOrUpdate} from "../api/equFluorite"
  import {guid} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {getEqListByMoType} from "../api/monitorPoint"

  export default {
    // 新建编辑
    name: 'PlanBgCreate',
    components: {
      XrCreate,
      CreateSections
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
      dictData: {},
    },
    data() {
      return {
        loading: false,
        form: {},
        onDisabled: false,

        rules: {
          assetName: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
        },
        monitorTypeDict: [],
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑监测点' : '新建监测点'
      },

    },
    watch: {},
    created() {
      this.getDicts('b_video_alert_type').then(res => {
        this.monitorTypeDict = res.data
      })
      this.getDetail();
      this.debouncedSaveField = debounce(300, this.save)
    },
    methods: {

      /**
       * 获取详情
       * */
      getDetail() {
        getById(this.action.id)
          .then(res => {
            let objNum = this.booleanChack(res.data)
            if (res.data.aiMonitorType) {
              res.data.aiMonitorType = res.data.aiMonitorType.split(',')
            }
            this.form = res.data;
          })
          .catch((e) => {
            console.log(e)
          })
      },
      booleanChack(objNum) {
        Object.keys(objNum).forEach(key => {
          if (objNum[key] == '0') {
            objNum[key] = false
          } else if (objNum[key] == '1') {
            objNum[key] = true
          }
        })
        return objNum
      },
      /**
       * 保存
       */
      save(isSubmit) {
        this.$refs['form'].validate(valid => {
          if (valid) {
            this.$confirm("修改后无法恢复，确定保存吗？", '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            })
              .then(() => {
                var params = JSON.parse(JSON.stringify(this.form));
                params.isEnable == true ? params.isEnable = '1' : params.isEnable = '0';
                params.hookCheckBox == true ? params.hookCheckBox = '1' : params.hookCheckBox = '0';
                params.ptzCheckBox == true ? params.ptzCheckBox = '1' : params.ptzCheckBox = '0';
                params.isAiEquipment == true ? params.isAiEquipment = '1' : params.isAiEquipment = '0';
                if (params.aiMonitorType) {
                  params.aiMonitorType = params.aiMonitorType.join(',')
                }
                this.loading = true;
                saveOrUpdate(params).then(response => {
                  if (response.code === 0) {
                    this.$message.success(
                      this.action.type == 'update' ? '修改成功' : '添加成功'
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
              })
              .catch(() => {
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
      },
      monitorTypeChange(value) {
        console.log(value)
        this
      },
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

  /deep/ .el-select__tags {
    white-space: nowrap;
    overflow: hidden;
    max-height: 30px !important;
  }
</style>
