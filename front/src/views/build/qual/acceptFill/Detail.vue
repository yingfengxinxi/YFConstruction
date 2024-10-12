<template>
  <xr-create :loading="loading" 
  :title="title" 
  :confirm-button-text="'保存'" 
  @close="close" 
  @save="debouncedSaveField(true)">
    <create-sections title="基本信息">
      <el-form ref="form" :model="form" :disabled="disabledDiy" :rules="rules" label-position="top" label-width="100px" class="form">
        <el-row>
          <el-col :span="12">
            <el-form-item label="楼座" prop="buildingId">
              <el-select v-model="form.buildingId" placeholder="请选择楼座" clearable style="width: 400px" @change="getBuildingDataList">
                <el-option v-for="dict in buildList" :key="dict.buildingId" :label="dict.buildingName" :value="dict.buildingId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房号" prop="houseId">
              <select-tree v-model="form.houseId" ref="treeChild" :data="houseList" :defaultKey="sonTreeDefaultKey" clearable collapseTags expandClickNode @getValue="setTreeMenu"></select-tree>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="验收工序" prop="acceptWork">
              <el-select v-model="form.acceptWork" placeholder="请选择验收工序" clearable style="width: 400px">
                <el-option v-for="dict in acceptWorks" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="验收模板" prop="templateNameId">
              <el-select v-model="form.templateNameId" placeholder="请选择验收模板" clearable style="width: 400px">
                <el-option v-for="dict in templateList" :key="dict.id" :label="dict.templateName" :value="dict.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="验收时间" prop="acceptTime">
              <el-date-picker v-model="form.acceptTime" type="date" placeholder="请选择验收时间" format="yyyy-MM-dd" value-format="yyyy-MM-dd"> </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="验收人" prop="acceptBy">
              <el-select
                  v-model="form.acceptBy"
                  placeholder="请选择"
                  clearable
                  multiple
                  style="width: 400px">
                  <el-option
                      v-for="dict in acceptByList"
                      :key="dict.id"
                      :label="dict.personName"
                      :value="dict.id" />
                </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="验收结论" prop="acceptConclusion">
              <el-input v-model="form.acceptConclusion" placeholder="请输入验收结论"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否通过" prop="isAdopt">
              <el-select v-model="form.isAdopt" placeholder="请选择是否通过" clearable style="width: 400px">
                <el-option v-for="dict in isAdopts" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="经度" prop="locationLon">
              <el-input v-model="form.locationLon" placeholder="请输入经度"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度" prop="locationLat">
              <el-input v-model="form.locationLat" placeholder="请输入纬度"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      <wk-upload-file :ref="'wkUploadFile'" name="file" multiple accept="*" :disabled="disabledDiy" :autoUpload="false" :batchId="form.batchId"></wk-upload-file>
      </el-form>

    </create-sections>
    <create-sections title="模板内容明细">
      <el-table ref="menuTable" :data="templateMainSetList"
       style="width: 100%" 
       :height="tableHeight" 
       border 
       v-loading="loading" 
       highlight-current-row >
        <el-table-column type="index" align="center" label="序号" width="70"></el-table-column>
        <el-table-column prop="acceptType" align="center" label="验收类型" :formatter="acceptTypeFormatter"></el-table-column>
        <el-table-column prop="acceptPro" align="center" label="验收项目" width="140"></el-table-column>
        <el-table-column prop="acceptContent" align="center" label="验收内容" width="140"></el-table-column>
        <el-table-column prop="acceptResult" align="center" label="验收结果"></el-table-column>
        <el-table-column prop="isAdopt" align="center" label="是否通过" :formatter="isAdoptFormatter"></el-table-column>

        <el-table-column fixed="right" align="center" label="操作" width="180">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-edit" size="small" :disabled="true" @click="handleClick('edit', scope)">填报</el-button>
            <!-- <el-button type="text" icon="el-icon-delete" size="small" :disabled="true" @click="handleClick('delete', scope)">删除</el-button> -->
            <el-button type="text" icon="el-icon-document" size="small" @click="handleClick('show', scope)">查看</el-button>
            
          </template>
        </el-table-column>
		  </el-table>
    </create-sections>

    <!-- 验收项目明细 查看-->
		<CreateMenu v-if="showCreate" 
					:form="currentMenu" 
					:actionType="actionType"

					@hiden-view="showCreate=false" />

  </xr-create>
</template>

<script>

import XrCreate from '@/components/XrCreate'
import CreateSections from '@/components/CreateSections'
import WkUploadFile from "@/components/CreateCom/WkUploadFile";

import { guid } from '@/utils'
import { debounce } from 'throttle-debounce'
import { XhStrucUserCell } from '@/components/CreateCom'

// import HiddenAcceptItems from "./components/HiddenAcceptItems";
import SelectTree from "./components/selectTree";

import {getIdAndNameList} from "../api/checkPersonnel"
import CreateMenu from "./components/CreateMenu"

// import { saveOrUpdate, queryById } from "../api/acceptTemplateSet";
import { TempLateSetList, buildList, queryTreeList, getAcceptTemplateById, queryById } from "../api/acceptFill";
export default {
  // 新建编辑
  name: 'acceptTemplateSetCreate',
  components: {
    XrCreate,
    CreateSections,
    WkUploadFile,
    XhStrucUserCell,
    SelectTree,
    // HiddenAcceptItems,
    CreateMenu
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
    },
    titleName:''
  },
  data () {
    return {
      loading: false,
      tableHeight:document.documentElement.clientHeight - 700, // 表的高度

      form: {
        acceptTime: new Date(new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate() +
          " 00:00:00"),
      },
      batchId: '',

      rules: {
        buildingId: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        houseId: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        acceptWork: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        templateNameId: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        acceptTime: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        acceptBy: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        acceptConclusion: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
        isAdopt: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ],
      },
      //字典项
      acceptWorks: [],//验收工序字典项
      buildList: [],//楼座列表
      houseList: [],//房号树
      templateList: [],//模板列表
      isAdopts: [{ 'dictValue': '0', 'dictLabel': '否' }, { 'dictValue': '1', 'dictLabel': '是' }],//是否通过
      //给子级树组件默认赋值
      sonTreeDefaultKey: [],
      //控制表单是否可编辑
      disabledDiy: false,

      acceptByList:[],
      HouseTypeId:-1,//
      showCreate:false,//验收项目明细 新建、修改、查看弹出的页面
      actionType:'',
      currentMenu:{},

      //字典项
      acceptTypes: [],//验收类型
      templateMainSetList: [],//获取验收明细模板内容

    }
  },
  computed: {
     title () {
      if (this.action.type === 'show') {
        return '查看'+this.titleName
      } else {
        return this.action.type === 'update' ? '编辑' + this.titleName : '新建' + this.titleName
      }
    },
  },
  watch: {},
  created () {
    this.debouncedSaveField = debounce(300, this.save)
    //考虑附件ID
    if (this.action.type == "show") {
      this.getDetail();
      this.disabledDiy = true
    }
    //获取字典项
    this.getDicts("b_qual_accept_work").then(response => {
      this.acceptWorks = response.data;
    });
    //验收类型
    this.getDicts("b_qual_accept_type").then(response => {
        this.acceptTypes = response.data;
    });
    buildList(0).then(response => {
      this.buildList = response.data;
    })
    TempLateSetList().then(response => {
      this.templateList = response.data.list;
    })

    //验收人员list
    this.getIdAndNameList()

  },

  mounted () {
  },

  beforeDestroy () {
  },

  methods: {
    /**
     * 获取详情
     * */
    async  getDetail () {
      this.loading = true;
      await queryById(this.action.id)
        .then(res => {
          this.form = res.data.templateForm;
          //给房号树选择器默认赋值
          this.getBuildingDataList(res.data.templateForm.buildingId);

          res.data.templateList.forEach(item => {
            if (item.batchId == null) {
             
              item.batchId = guid()
            } 
          });

          this.templateMainSetList = res.data.templateList;
          this.loading = false;
        })
        .catch(() => {
        })
    },
    /**
     * 保存
     */
    save (isSubmit) {
      
    },
    /**
     * 关闭
     */
    close () {
      this.$emit('close')
    },
    /**
    * 获取楼栋信息
    */
    async   getBuildingDataList (value) {
      // console.log("获取楼栋信息的id====", value);
      this.loading = true
      this.$refs.treeChild.clean();
      this.form.templateNameId = '';
      let newDataList = [];

      await queryTreeList(value).then(res => {
        for (let i = 0; i < this.buildList.length; i++) {
          if (value == this.buildList[i].buildingId) {
            this.buildList[i].disabled = true
            newDataList.push(this.buildList[i]);
            break;
          }
        }
        newDataList[0].children = res.data;
        this.sonTreeDefaultKey.push(this.form.houseId)
        this.loading = false
      }).catch(err => {
        this.loading = false
        // console.log("获取楼栋信息的error", err);
        this.$message({
          type: "error",
          message: "系统异常,请稍后重试"
        });
      })
      this.houseList = newDataList;
      // await console.log("房号列表项子组件传递的值===", this.houseList);
    },

    /**
     * 下拉树获取子组件值
     */
    async setTreeMenu (key, data) {

      if (data != null) {
        this.form.houseId = key
        let HouseTypeId = data.distribHouseTypeId;
        if (HouseTypeId != undefined && HouseTypeId.length != null && HouseTypeId.length != 0) {
          if (HouseTypeId != this.form.templateNameId) {
            this.loading = true;
            await getAcceptTemplateById(HouseTypeId).then(res => {
              this.$set(this.form, 'templateNameId', res.data.acceptTemplateId);
              this.$forceUpdate();
              this.loading = false;
            })
          }
        }
      }

    },


    handleClick(event, scope){

      if(event == 'show'){//查看
          
          this.actionType = 'show'
          this.currentMenu = scope.row

          this.showCreate=true
					
        }
        
    },

    //查询验收人员id和name的list
    getIdAndNameList(){
      getIdAndNameList().then(res=>{
        this.acceptByList = res.data

      })
    },

    acceptTypeFormatter(row, column){
      return this.selectDictLabel(this.acceptTypes, row.acceptType)
    },

    //是否通过
    isAdoptFormatter (row) {
      let value = row.isAdopt
      let datas = this.isAdopts;
      let retValue = "";

      Object.keys(datas).some(key => {
        if (datas[key].dictValue == "" + value) {
          retValue = datas[key].dictLabel
          return true;
        }
      });

      return retValue;
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
</style>
