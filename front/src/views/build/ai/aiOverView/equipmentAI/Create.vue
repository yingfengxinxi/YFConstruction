<template>
  <xr-create :loading="loading" :title="title" :confirm-button-text="'保存'" :showSave="this.action.type === 'showDetials'?false:true" @close="close" @save="debouncedSaveField(true)">
    <el-form ref="form" :disabled="disabledForm" :model="form" :rules="rules" label-position="top" label-width="100px" class="form">
      <el-row>
        <el-col :span="12">
          <el-form-item label="设备名称" class="add-form-input subcontractorClass" prop="id">
            <el-select v-model="form.id" @change="getChange" placeholder="请选择" clearable size="small">
              <el-option v-for="dict in assetList" :key="dict.id" :label="dict.assetName" :value="dict.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="设备编号" class="add-form-input subcontractorClass" prop="assetCode">
            <el-input v-model="form.assetCode" disabled placeholder="请输入设备编号"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="设备序列号" class="add-form-input subcontractorClass" prop="deviceChannelId">
            <el-input v-model="form.deviceChannelId" disabled placeholder="请输入设备序列号"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="AI监测类型" prop="aiMonitorType">
            <el-select style="width: 100%;" multiple v-model="form.aiMonitorType" placeholder="请选择AI监测类型">
              <el-option v-for="item in eventTypeDict" :key="item.dictValue" :label="item.dictLabel" :value="item.dictValue">
              </el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item label="ai监测类型" class="add-form-input subcontractorClass" prop="aiEventType">
            <xh-project :allowCheckAll="false" style="width: 100%;" :infoRequest="req" :openStatus="addOpen" :selectedData="selectedData" :idKeyName="'dictValue'" :labelKeyName="'dictLabel'" :titleName="'AI监测类型'" @changeCheckout="strcUserChange" />
          </el-form-item> -->
        </el-col>
      </el-row>
    </el-form>
  </xr-create>
</template>

<script>

import XrCreate from '@/components/XrCreate'
import XhProject from '@/components/CreateCom/XhProject'


import { debounce } from 'throttle-debounce'

import { getAssetList, getDictList, saveOrUpdate, getById } from "./equipmentAI.js";


export default {
  // 新建编辑
  name: 'monitersetCreate',
  components: {
    XrCreate
  },

  props: {
    action: {
      type: Object,
      default: () => {
        return {
          type: '',
          id: '',
          data: {}
        }
      }
    }
  },
  data () {
    return {
      loading: false,
      form: {
      },
      disabledForm: false,
      rules: {
        id: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
      },
      assetList: [],
	  eventTypeDict: [],

    }
  },
  computed: {
    title () {
      if (this.action.type === 'showDetials') {
        return 'AI设备信息'
      } else {
        return this.action.type === 'update' ? '编辑AI设备信息' : '添加AI设备信息'
      }
    },

  },
  watch: {},
  created () {
    this.getDicts('b_ai_event_type').then(res => {
      this.eventTypeDict = res.data
    })
    if (this.action.type == 'update') {
      this.getDetail();
    }
    this.getAssetLists();
    this.debouncedSaveField = debounce(300, this.save)

  },
  methods: {
    getAssetLists () {
      getAssetList(this.queryParams).then(res => {
        this.assetList = res.data.list;
      })
        .catch((e) => {
          console.log(e)
        })
    },
    getChange (e) {
      this.assetList.forEach(item => {
        if (item.id == e) {
          this.form.assetCode = item.assetCode;
          this.form.deviceChannelId = item.deviceChannelId
        }
      });
    },
    /**
     * 获取详情
     * */
    getDetail () {
      getById(this.action.id)
        .then(res => {
         if(res.data.aiMonitorType){
            res.data.aiMonitorType = res.data.aiMonitorType.split(',')
         }
          this.action.id = res.data.id
          this.form = res.data;

          this.loading = false

        })
        .catch((e) => {
          this.loading = false
          console.log(e)
        })
    },
    /**
     * 保存
     */
    save (isSubmit) {
      this.$refs['form'].validate(valid => {
        if (valid) {
          var params = JSON.parse(JSON.stringify(this.form));
          params.aiMonitorType = params.aiMonitorType.join(',')
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
      this.$emit('close')
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
/deep/ .el-select {
  width: 100%;
}
/deep/ .el-select__tags {
  white-space: nowrap;
  overflow: hidden;
  max-height: 30px !important;
}
xr-create{
  width: 100%;
  height: auto;
}
/deep/ .crm-create-card-container{
  width: 900px;
    height: auto;
    margin-top: 100px;
}
</style>
