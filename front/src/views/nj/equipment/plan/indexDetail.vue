<template>
  <div class="main">
    <el-form ref="form" :model="form" label-width="100px" class="new-dialog-form">
      <el-row style="border-bottom: 1px solid #e6e6e6;padding-bottom: 10px;margin-bottom: 20px;">
        <span>基本信息</span>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="计划名称" prop="planName">
            <el-input v-model="form.planName" placeholder="计划名称" disabled></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="计划类型" prop="planType">
            <el-select v-model="form.planType" placeholder="计划类型" disabled>
              <el-option
                v-for="item in planTypes"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否启用" prop="enable">
            <el-select v-model="form.enable" placeholder="是否启用" disabled>
              <el-option
                v-for="item in enables"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="执行周期" prop="cycle">
            <el-input type="number" v-model="form.cycle" placeholder="执行周期" disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行周期单位" prop="unit">
            <el-select v-model="form.unit" placeholder="执行周期单位" disabled
            >
              <el-option
                v-for="item in units"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="计划等级" prop="level">
            <el-select v-model="form.level" placeholder="计划等级" disabled="">
              <el-option
                v-for="item in levels"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行部门" prop="deptId">
            <select-tree :options="showDeptTreeData" v-model="form.deptId" :props="defaultProps"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="作业名称" prop="workName">
            <el-input v-model="form.workName" placeholder="作业名称" disabled></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="作业指导" prop="workDirect">
            <el-input type="textarea" :rows="2" v-model="form.workDirect" placeholder="作业指导" disabled></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="启动日期" prop="startDate">
            <el-date-picker disabled
              v-model="form.startDate"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="请选择启动日期"
            >
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="下次执行日期" prop="nextDate">
            <el-date-picker
              v-model="form.nextDate"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="" :disabled="true">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row style="padding-bottom: 10px;">
        <span>计划维护设备信息</span>
      </el-row>
      <el-row>
        <AssetListDetailNoCheckbox :plan-id="form.planId" @change="assetChange" :showCheckbox="false"></AssetListDetailNoCheckbox>
      </el-row>
    </el-form>
  </div>
</template>

<script>

  import {depListAPI} from '@/api/common' // 直属上级接口

  import SelectTree from '@/components/SelectTree.vue';

  import AssetListDetailNoCheckbox from './components/assetListDetailNoCheckbox'

  import {
    planGet
  } from '../api/plan'


  export default {
    name: 'Dict',
    components: {
      SelectTree,
      AssetListDetailNoCheckbox
    },
    props: {
      planId: {
        require: true,
        type: Number,
        default: -1
      },
    },
    watch:{
      planId(){
        this.handleUpdate();
        this.getDeptTreeList();
      }
    },
    data() {
      return {
        // 表单参数
        form: {},
        // 数据默认字段
        defaultProps: {
          parent: 'parentId',   // 父级唯一标识
          value: 'id',          // 唯一标识
          label: 'label',       // 标签显示
          children: 'children', // 子级
        },
        showDeptTreeData: [],
        planTypes: [],
        units: [],
        enables: [],
        levels: [],
        assetDatas: [],//选中的设备列表
        // 表单校验
        isAdd:false
      }
    },
    created() {
      this.handleUpdate()
      this.getDicts('b_equipment_plan_type').then(response => {
        this.planTypes = response.data
      })
      this.getDicts('b_equipment_plan_unit').then(response => {
        this.units = response.data
      })
      this.getDicts('b_equipment_enable').then(response => {
        this.enables = response.data
      })
      this.getDicts('b_equipment_plan_level').then(response => {
        this.levels = response.data
		console.log()
      })
      this.getDeptTreeList();
    },
    methods: {
      /** 修改按钮操作 */
      handleUpdate() {
        planGet(this.planId).then(response => {
          this.form = response.data || {};
          this.open = true
          this.title = '修改维护信息'
          this.isAdd = false;
        }).catch(() => {
        })

      },
      // 获取树形列表
      getDeptTreeList() {
        depListAPI({type: 'tree'})
          .then(response => {
            this.showDeptTreeData = response.data || []
          })
          .catch(() => {
          })
      },

      //字典翻译
      planTypeFormat(row, column) {
        return this.selectDictLabel(this.planTypes, row.planType)
      },
      unitFormat(row, column) {
        return this.selectDictLabel(this.units, row.unit)
      },
      enableFormat(row, column) {
        return this.selectDictLabel(this.enables, row.enable)
      },
      assetChange(data) {
        this.assetDatas = data;
      },
    }
  }
</script>


<style lang="scss" scoped>
  .main {
    height: 100%;
  }

  /deep/ .xr-header {
    padding: 15px 30px;
  }

  .main-body {
    background-color: white;
    border-top: 1px solid $xr-border-line-color;
    border-bottom: 1px solid $xr-border-line-color;
  }

  .main-table-header-button {
    float: right;
    margin-right: 20px;
    margin-top: 10px;
  }

  .link-type, .link-type:focus {
    color: #337ab7;
    cursor: pointer
  }

  .link-type:focus:hover, .link-type:hover {
    color: #20a0ff
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content
  .el-select-group__wrap:not(:last-of-type)::after {
    display: none;
  }

  .new-dialog-form /deep/ .el-form-item .el-form-item__content .el-select-group {
    padding-left: 10px;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content
  .el-select-group__title {
    border-bottom: 1px solid #e4e7ed;
    padding: 0 0 7px;
    margin: 0 20px 5px;
  }

  .new-dialog-form
  /deep/
  .el-form-item {
    width: 100%;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__label {
    text-align: right;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content {
    width: calc(100% - 120px);
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-date-editor--date, .el-select, .el-date-editor--datetime {
    width: 100%;
  }

  @import '@/views/styles/table.scss';

</style>
