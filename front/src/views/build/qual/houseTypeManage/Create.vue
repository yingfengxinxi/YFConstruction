<template>
  <xr-create :loading="loading" :title="title" :confirm-button-text="'保存'" @close="close" @save="debouncedSaveField(!disabledT)">

    <el-form ref="form" :model="form" :disabled="disabledT" :rules="rules" label-position="top" label-width="100px" class="form">
      <el-row>
        <el-col :span="8">
          <el-form-item label="户型模板" prop="houseTemplateName">
            <el-input v-model="form.houseTemplateName" placeholder="请输入户型模板名称"></el-input>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="8">
          <el-form-item label="验收工序" prop="acceptWork">
            <el-select v-model="form.acceptWork" placeholder="请选择验收工序" clearable style="width: 269px" @change="acceptChange">
              <el-option v-for="dict in acceptWorks" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="验收模板" prop="acceptTemplateId">
            <el-select v-model="form.acceptTemplateId" placeholder="请选择验收模板" clearable style="width: 269px">
              <el-option v-for="dict in acceptTemplates" :key="dict.id" :label="dict.templateName" :value="dict.id" />
            </el-select>
          </el-form-item>
        </el-col> -->
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="上传户型图">
            <el-upload v-if="!QYsystemImage" :show-file-list="false" :http-request="fileUploadQY" drag class="upload" action="http" accept="image/png, image/jpeg, image/gif, image/jpg">
              <i class="el-icon-plus uploader-icon" />
            </el-upload>
            <div v-else class="upload-show">
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="QYsystemImage">
              <i class="el-icon-remove icon-delete" v-if="!disabledT" @click="deleteSystemImageQY" />
            </div>

            <edit-image :fixed-number="[20, 20]" :show="QYshowEditImage" :image="QYeditImage" title="上传户型图"
                        preview-width="200px" preview-height="200px" preview-radius="0" width="550px" save-button-title="确定"
                        @save="submiteImageQY" @close="QYshowEditImage=false" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <div class="list_left">
            <el-button size="mini" type="text" icon="el-icon-plus" @click="openAddNewDialog('添加房间')">添加房间 </el-button>
            <el-table :data="diskData" height="300">
              <el-table-column prop="houseName" label="房间名称" show-overflow-tooltip>
                <template slot-scope="scope">
                  <span v-if="scope.row.start !='0' || scope.row.start == undefined">{{scope.row.houseName}}</span>
                  <el-input v-model="tableInput.disk.houseName" v-if="scope.row.start =='0'" style="width:90%;"></el-input>
                </template>
              </el-table-column>

              <el-table-column label="操作" min-width="32%">
                <template slot-scope="scope">
                  <el-button size="mini" type="danger" v-if="scope.row.start !='0' || scope.row.start == undefined||disabledT" @click="handleDelete(scope.$index,'disk')"><i class="iconfont icon-delete"></i>删除</el-button>
                  <el-button size="mini" type="primary" v-if="scope.row.start =='0'||disabledT" @click="handleAdd(scope.$index,'disk')"><i class="iconfont icon-add"></i>保存</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-col>
      </el-row>

      <div class="list_right">
        <el-button size="mini" type="text" icon="el-icon-plus" @click="openAddNewDialog2('添加房间')">工序与模板 </el-button>
        <el-table :data="diskData2" height="200">
          <el-table-column prop="acceptWork" label="验收工序" show-overflow-tooltip>
            <template slot-scope="scope">
              <span v-if="scope.row.start !='0' || scope.row.start == undefined">{{ scope.row.acceptWorkName }}</span>
              <el-select v-model="tableInput2.disk.acceptWork" v-if="scope.row.start =='0'" placeholder="请选择验收工序" clearable style="width: 210px" @change="acceptChange">
                <el-option v-for="dict in acceptWorks" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" :disabled="dict.disabled"/>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="acceptTemplate" label="验收模板" show-overflow-tooltip>
            <template slot-scope="scope">
              <span v-if="scope.row.start !='0' || scope.row.start == undefined " > {{ scope.row.acceptTemplateName }} </span>
              <el-select v-model="tableInput2.disk.acceptTemplate" v-if="scope.row.start =='0'" placeholder="请选择验收模板" clearable style="width: 215px">
                <el-option v-for="dict in acceptTemplates" :key="dict.id" :label="dict.templateName" :value="dict.id" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="32%">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" v-if="scope.row.start !='0' || scope.row.start == undefined||disabledT" @click="handleDelete(scope.$index,'disk')"><i class="iconfont icon-delete"></i>删除</el-button>
              <el-button size="mini" type="primary" v-if="scope.row.start =='0'||disabledT" @click="handleAdd2(scope.$index,'disk')"><i class="iconfont icon-add"></i>确认</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-form>
  </xr-create>
</template>

<script>

import XrCreate from '@/components/XrCreate'
import CreateSections from '@/components/CreateSections'
import EditImage from '@/components/EditImage'

import { guid } from '@/utils'

import { debounce } from 'throttle-debounce'
import { saveOrUpdate, getById, getDetailsByMainId } from "../api/houseTypeManage";
import { queryList } from "../api/acceptTemplateSet";
export default {
  // 新建编辑
  name: 'houseTypeMangeCreate',
  components: {
    XrCreate,
    CreateSections,
    EditImage,
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
    titleName: '',
    houseTypeId: {
      type: String,
      default: ''
    }
  },
  data () {
    return {
      loading: false,
      form: {},
      list: [],
      tableHeight: 320, // 表的高度
      rules: {
        houseName: [
          { required: true, message: '必填项不得为空', trigger: 'blur' }
        ]
      },
      QYshowEditImage: false,
      QYeditImage: null,
      QYsystemImage: '',
      QYeditedImage: null, // 编辑后的图片

      batchId: '',
      //添加房间使用
      diskData: [],
      //table下动态绑定的值
      tableInput: {
        disk: {
          houseName: null,
          start: '0'
        },
      },
      //添加验收工序与验收模板使用
      diskData2: [],
      tableInput2: {
        disk: {
          acceptWork: null,
          acceptTemplate: null,
          start: '0'
        },
      },
      //验收模板
      acceptTemplates: [],
      acceptWorks: [],//验收工序
    }
  },
  computed: {
    title () {
      if (this.action.type === 'show') {
        return this.titleName
      } else {
        return this.action.type === 'update' ? '编辑' + this.titleName : '新建' + this.titleName
      }

    },
    disabledT () {
      // console.log("this.action.type === 'show'",this.action.type === 'show');
      return this.action.type === 'show' ? true : false
    },

  },
  watch: {
    'action.id': {
      handler (newValue, oldValue) {

        if (newValue != undefined && newValue != '' && newValue != null && this.action.type == 'show') {
          this.getDetail(this.action.id);
        }
      },
      immediate: true
    }
  },
  created () {
    this.debouncedSaveField = debounce(300, this.save)

    if (this.action.type == "save") {
      this.batchId = guid();
    } else {
      this.getDetail(this.action.id)
    }
    // this.getAcceptTemplatesList()
    this.getDicts('b_qual_accept_work').then(response => {
      this.acceptWorks = response.data
    })
    queryList().then(res => {
      this.acceptTemplates = res.data.list;
    })
  },

  mounted () {
  },

  beforeDestroy () {
  },

  methods: {
    // test(val){console.log("渲染打印",val)},
    openAddNewDialog (val) {
      this.dialogTitle = val;
      // this.dialogVisible = true;
      let isExist = false; //是否存在输入行
      if (this.diskData.length > 0) {
        for (let i = 0; i < this.diskData.length; i++) {
          if (this.diskData[i].start == '0') {
            isExist = true;
            break;
          }
        }
        if (!isExist) {
          this.diskData.push(this.tableInput.disk);
        } else {
          this.$message.error('请保存编辑行');
        }
      } else {
        this.diskData.push(this.tableInput.disk);
      }
    },
    /**
     * 添加验收工序与验收模板
     */
    openAddNewDialog2 (val) {
      this.dialogTitle = val;
      // this.dialogVisible = true;
      let isExist = false; //是否存在输入行
      if (this.diskData2.length > 0) {
        for (let i = 0; i < this.diskData2.length; i++) {
          if (this.diskData2[i].start == '0') {
            isExist = true;
            break;
          }
        }
        if (!isExist) {
          this.acDisabled()

          this.diskData2.push(this.tableInput2.disk);
        } else {
          this.$message.error('请保存编辑行');
        }
      } else {
        this.diskData2.push(this.tableInput2.disk);
      }
    },
    handleAdd (index, ipTable) {
      switch (ipTable) {
        case 'disk':
          if (this.tableInput.disk.houseName == null || this.tableInput.disk.houseName.trim() <= 0) {
            this.$message.error('请填写房间名称');
            return;
          }
          let tempRow = JSON.parse(JSON.stringify(this.tableInput.disk));
          tempRow.start = '1';
          this.diskData.splice(-1, 0, tempRow);

          this.diskData[this.diskData.length - 1].houseName = null;
          this.diskData[this.diskData.length - 1].start = '0';
          this.diskData.splice(this.diskData.length - 1, 1);
          break;
        //多个case
        default:
      }
    },
    handleAdd2 (index, ipTable) {
      switch (ipTable) {
        case 'disk':
          if (this.tableInput2.disk.acceptWork == null) {
            this.$message.error('请选择验收工序');
            return;
          }
          if (this.tableInput2.disk.acceptTemplate + "" == null) {
            this.$message.error('请选择验收模板');
            return;
          }
          this.tableInput2.disk.acceptWorkName=this.acceptWorkFormatter(this.tableInput2.disk.acceptWork);
          this.tableInput2.disk.acceptTemplateName=this.templateFormatter(this.tableInput2.disk.acceptTemplate);
          let tempRow = JSON.parse(JSON.stringify(this.tableInput2.disk));
          tempRow.start = '1';
          this.diskData2.splice(-1, 0, tempRow);
          this.diskData2[this.diskData2.length - 1].acceptWork = null;
          this.diskData2[this.diskData2.length - 1].acceptTemplate = null;
          this.diskData2[this.diskData2.length - 1].start = '0';
          this.diskData2.splice(this.diskData2.length - 1, 1);
          break;
        //多个case
        default:
      }
      // console.log("工序与模板", this.diskData2);
    },
    //删除
    handleDelete (index, ipTable) {
      switch (ipTable) {
        case 'disk':
          this.diskData2.splice(index, 1);
          this.acDisabled()
          break;
        default:
      }
    },
    /** 附件上传  户型图*/
    fileUploadQY (content) {
      const reader = new FileReader()
      var self = this
      reader.onload = function (e) {
        let result
        if (typeof e.target.result === 'object') {
          // 把Array Buffer转化为blob 如果是base64不需要
          result = window.URL.createObjectURL(new Blob([e.target.result]))
        } else {
          result = e.target.result
        }
        self.QYeditImage = result
        self.QYshowEditImage = true
      }
      reader.readAsDataURL(content.file)
    },
    //删除图片 户型图
    deleteSystemImageQY () {
      this.QYsystemImage = ''
      this.QYeditedImage = null
    },
    // 户型图
    submiteImageQY (data) {
      this.QYeditedImage = data
      this.QYsystemImage = data.image
    },

    /**
     * 获取详情
     * */
    getDetail (id) {
      getById(id)
        .then(res => {
          this.QYsystemImage = res.data.houseTypeImg
          // console.log(this.systemImage)
          this.batchId = res.data.batchId

          this.form = res.data;

          getDetailsByMainId(this.action.id).then(res => {

            this.diskData = res.data.house;
            this.diskData2 = res.data.accept;
          })

        })
        .catch(() => {
        })
    },
    /**
     * 保存
     */
    save (isSubmit) {

      this.$refs['form'].validate(valid => {
        if (!this.QYsystemImage && !this.QYeditedImage) {
          this.$message.error('户型图不能为空')
          return false
        }
        if (this.diskData.length > 0) {
          if (this.diskData[this.diskData.length - 1].start == '0') {
            this.diskData.splice(this.diskData.length - 1, 1);
          }
        }
        if (valid) {
          this.form.batchId = this.batchId
          var param = new FormData()
          param.append('ZHUB', JSON.stringify(this.form))
          param.append('ZIB', JSON.stringify(this.diskData))
          param.append('ACCEPT',JSON.stringify(this.diskData2));
          if (this.QYeditedImage != null) {
            param.append('QYfile', this.QYeditedImage.blob)
          }

          this.loading = true;

          saveOrUpdate(param).then(response => {
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

    //获取验收模板列表
    async  acceptChange (value) {
      this.loading = true
      this.$set(this.form, 'acceptTemplateId', '')
      await queryList({ "acceptWork": value }).then(res => {

        this.acceptTemplates = res.data.list;
        this.loading = false
      })
        .catch(() => {
          this.loading = false
        })
    },
    //模板自定义格式化列表--验收工序
    acceptWorkFormatter (value) {
      let datas = this.acceptWorks;
      let retValue = "";
      Object.keys(datas).some(key => {
        if (datas[key].dictValue == "" + value) {
          retValue = datas[key].dictLabel
          return true;
        }
      });
      return retValue;
    },
    /**
     * 模板自定义格式化列表-- 验收模板
     */
    templateFormatter (value) {
      let datas = this.acceptTemplates;
      let retValue = "";
      Object.keys(datas).some(key => {
        if (datas[key].id +"" == "" + value) {
          retValue = datas[key].templateName
          return true;
        }
      });
      return retValue;
    },

    //验收工序下拉框禁用
    acDisabled(){

      for(let j = 0 ; j < this.acceptWorks.length ; j++){
          let ac = this.acceptWorks[j]
          ac.disabled = false
      }

      for(let i = 0; i < this.diskData2.length; i++){
        let acceptWork = this.diskData2[i].acceptWork

        for(let j = 0 ; j < this.acceptWorks.length ; j++){

          if(this.acceptWorks[j].dictValue == acceptWork){
            this.acceptWorks[j].disabled = true
          }
        }

      }

    }

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
/*#crm-table{*/
/*  height: 480px!important;*/
/*}*/

.uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 300px;
  height: 300px;
  line-height: 300px;
  text-align: center;
}
.upload /deep/ .el-upload-dragger {
  width: 300px;
  height: 300px;
}
.upload-show {
  position: relative;
  display: block;
  width: 300px;
  height: 300px;
  img {
    width: 100%;
    height: 100%;
  }

  .icon-delete {
    position: absolute;
    top: -10px;
    right: -8px;
    color: red;
    font-size: 20px;
    display: none;
  }
}
.upload-show:hover {
  .icon-delete {
    display: block;
  }
}

.list_left {
  float: left;
  width: 100%;
}

.list_right {
  float: left;
  width: 100%;
  margin-left: 1%;
}
</style>
