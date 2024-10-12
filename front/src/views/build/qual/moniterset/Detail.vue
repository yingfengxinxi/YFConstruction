<template>
  <xr-create
    :loading="loading"
    :title="title"
    @close="close"
    :showSave="false">

        <el-form ref="form" :disabled="true" :model="form" label-position="top" label-width="100px"
            class="form">
            <el-row>
              <el-col :span="12">
                <el-form-item label="监测项编码" prop="code">
                  <el-input v-model="form.code" placeholder="请输入监测项编码"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="监测项名称" prop="name">
                  <el-input v-model="form.name" placeholder="请输入监测项名称"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="单位" prop="unit">
                  <el-input v-model="form.unit" placeholder="请输单位"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="公式" prop="publicity">
                  <el-input v-model="form.publicity" placeholder="请输入公式"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="经度" prop="longitude">
                  <el-input v-model="form.longitude" placeholder="请输经度"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="纬度" prop="latitude">
                  <el-input v-model="form.latitude" placeholder="请输纬度"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="描述" prop="description">
                  <el-input type="textarea" v-model="form.description" placeholder="请输描述"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="排序" prop="sort">
                  <el-input v-model="form.sort" placeholder="请输排序"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="关联设备名称" prop="assDeviceName">
                  <el-input v-model="form.assDeviceName" placeholder="请输关联设备名称"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="报警上限阀值" prop="alarmMax">
                  <el-input v-model="form.alarmMax" placeholder="请输报警上限阀值"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报警下限阀值" prop="alarmMin">
                  <el-input v-model="form.alarmMin" placeholder="请输报警下限阀值"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

          </el-form>

  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import {debounce} from 'throttle-debounce'

  import {getById} from "../api/moniterset";

  export default {
    // 新建编辑
    name: 'DemoCreate',
    components: {
      XrCreate,
      CreateSections,
    },
    props: {
      // 详情信息id
      id: Number,
      titleName:''
    },
    data() {
      return {
        loading: false,
        form: {},
      }
    },
    computed: {
      title() {
        return this.titleName 
      }
    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      getById(this.id)
        .then(res => {
          this.form = res.data;
        })
        .catch(() => {
        })
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
        // console.log("close")
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
