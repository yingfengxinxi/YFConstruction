<!--  -->
<template>
  <xr-create :loading="loading" :title="title" :confirm-button-text="'保存'" @close="close" @save="debouncedSaveField(true)">

    <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px" class="form">
      <el-row>
        <el-col :span="12">
          <el-form-item label="选择楼栋" prop="buildingId">
            <el-select v-model="form.buildingId" placeholder="请选择需要分配的楼栋" clearable size="small" style="width: 200px" @change="getBuildingOptions">
              <el-option v-for="dict in buildingNameList" :key="dict.buildingId" :label="dict.buildingName" :value="dict.buildingId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="选择户型" prop="houseTypeId">
            <el-select v-model="form.houseTypeId" placeholder="请选择需要分配的户型" clearable size="small" style="width: 200px">
              <el-option v-for="dict in houseTemplateNameList" :key="dict.id" :label="dict.houseTemplateName" :value="dict.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <tree-select :parentKey="parentId+''" :MainData="buildingNameList" @BuildingCheckList="BuildingCheckList" style="width: 100%;height: 600px;" />
        </el-col>
      </el-row>
    </el-form>

  </xr-create>
</template>

<script>
import XrCreate from '@/components/XrCreate'
import {debounce} from 'throttle-debounce'

import TreeSelect from './components/TreeSelect2'
import { getList } from "../api/houseTypeManage";
import { queryByParentId,SaveDistrib } from '@/views/build/qual/api/buildManage'
export default {
  name: "",
  /**组件注册 */
  components: {
    XrCreate,
    TreeSelect
  },
  props: {
    action: {
      type: Object,
      default: () => {
        return {
          type: 'save',
          id: '',
          monitType: '',
          data: {}
        }
      }
    },
    titleName: ''
  },

  data () {
    return {
      loading: false,
      rules: {
        buildingId: [{ required: true, message: '必填项不得为空', trigger: 'blur' }],
        houseTypeId: [{ required: true, message: '必填项不得为空', trigger: 'blur' }]
      },
      form: {},
      //楼栋数据列表
      buildingNameList: [],
      //户型数据列表
      houseTemplateNameList: [],
      //记录父级id，向子组件传值
      parentId: '',
      //选择分配户型的楼栋列表
      checkBuildingList: []
    }
  },
  //生命周期 - 创建完成（访问当前this实例）
  created () {
    //获取全部数据
    this.initData();
    this.debouncedSaveField = debounce(300, this.save)
  },
  computed: {
    title () {
      if (this.action.type === 'distrib') {
        return '分配'+this.titleName
      } else {
        return this.action.type === 'update' ? '编辑' + this.titleName : '新建' + this.titleName
      }

    },
  },
  //监听事件
  watch: {},
  //生命周期 - 挂载完成（访问DOM元素）
  mounted () {

  },
  methods: {

    /**
     * 楼栋选择click事件
     */
    getBuildingOptions (e) {
      this.parentId = e;
    },

    /**
     * 树形列表选择回调事件
     */
    BuildingCheckList (data) {
      // console.log("树形列表选择回调参数",data)
      this.checkBuildingList = data;

    },

    /**
     * 数据初始化事件
     */
    initData () {
      this.loading = true;
      //获取所有楼栋
      queryByParentId(0).then(res => {
        // console.log(res)
        this.buildingNameList = res.data;
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
      //获取所有户型
      getList(this.queryParams).then(res => {
        this.houseTemplateNameList = res.data.list;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },

    /**
     * 保存
     */
    save (isSubmit) {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.loading = true;
           var params = new FormData()
          params.append('houseTypeId',JSON.stringify(this.form.houseTypeId))
          params.append('ZI',JSON.stringify(this.checkBuildingList))
          // console.log(params);
          SaveDistrib(params).then(response => {
            if (response.code === 0) {
              this.$message.success( '分配成功' )
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
<style scoped>
/* @import url(); 引入css类 */
</style>