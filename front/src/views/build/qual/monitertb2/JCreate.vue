<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">
    
      <el-form ref="form" :model="form" :disabled="disabledT" :rules="rules" label-position="top" label-width="100px"
               class="form">
        <el-row>
          <el-col :span="8">
            <el-form-item label="填报编号" prop="code">
              <el-input v-model="form.code" placeholder="请输入编号"></el-input>
            </el-form-item>
          </el-col>
           <el-col :span="8">
            <el-form-item label="填报日期" prop="dataDate">          
              <el-date-picker
                      v-model="form.dataDate"
                      type="date" placeholder="选择日期" value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="取样人员" prop="samplerName">
              <el-input v-model="form.samplerName" placeholder="请输入取样人员"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="见证人员" prop="witnessName">
              <el-input v-model="form.witnessName" placeholder="请输入见证人员"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="经度" prop="longitude">
              <el-input v-model="form.longitude" type="number" placeholder="请输入经度"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="取样人员照片" >
              <el-upload
                v-if="!QYsystemImage"
                :show-file-list="false"
                :http-request="fileUploadQY"
                drag
                class="upload"
                action="http"
                accept="image/png, image/jpeg, image/gif, image/jpg">
                <i class="el-icon-plus uploader-icon"/>
              </el-upload>
              <div
                v-else
                class="upload-show">
                <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="QYsystemImage">
                <i
                  class="el-icon-remove icon-delete"
                  @click="deleteSystemImageQY"/>
              </div>
        
              <edit-image
                :fixed-number="[20, 20]"
                :show="QYshowEditImage"
                :image="QYeditImage"
       
                title="取样人员照片"
                preview-width="200px"
                preview-height="200px"
                preview-radius="0"
                width="550px"
                save-button-title="确定"
                @save="submiteImageQY"
                @close="QYshowEditImage=false"/>

            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="见证人员照片" >
              <el-upload
                v-if="!JZsystemImage"
                :show-file-list="false"
                :http-request="fileUploadJZ"
                drag
                class="upload"
                action="http"
                accept="image/png, image/jpeg, image/gif, image/jpg">
                <i class="el-icon-plus uploader-icon"/>
              </el-upload>
              <div
                v-else
                class="upload-show">
                <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="JZsystemImage">
                <i
                  class="el-icon-remove icon-delete"
                  @click="deleteSystemImageJZ"/>
              </div>
        
              <edit-image
                :fixed-number="[20, 20]"
                :show="JZshowEditImage"
                :image="JZeditImage"
       
                title="见证人员照片"
                preview-width="200px"
                preview-height="200px"
                preview-radius="0"
                width="550px"
                save-button-title="确定"
                @save="submiteImageJZ"
                @close="JZshowEditImage=false"/>

            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="纬度" prop="latitude">
              <el-input v-model="form.latitude" type="number" placeholder="请输入纬度"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-table
        v-loading="loading"
        id="crm-table"
        :data="list"
        :height="tableHeight"
        class="n-table--border"
        use-virtual
        stripe
        border
        highlight-current-row
        style="width: 100%">
          <el-table-column :show-overflow-tooltip="true" label="监测项编号" align="center" prop="code" />
          <el-table-column :show-overflow-tooltip="true" label="监测项名称" align="center" prop="name" />

          <el-table-column label="监测采集值" align="center" prop="sValue">
            <template  slot-scope="scope">

              <el-input size="small" type="number" v-model="scope.row.sValue"  
              placeholder="请输入监测采集值" ></el-input>
              
            </template>
          </el-table-column>

          <el-table-column :show-overflow-tooltip="true" label="单位" align="center" prop="unit" />
          <el-table-column :show-overflow-tooltip="true" label="报警上限阀值" align="center" prop="alarmMax" />
          <el-table-column :show-overflow-tooltip="true" label="报警下限阀值" align="center" prop="alarmMin" />

          <el-table-column label="说明" align="center" prop="note">
            <template  slot-scope="scope">

              <el-input size="small" v-model="scope.row.note"  
              placeholder="请输入说明" ></el-input>
              
            </template>
          </el-table-column>

      </el-table>

      </el-form>
   

  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'
  import EditImage from '@/components/EditImage'

  import {guid, validatePhone, regexIsNumber} from '@/utils'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById, getDetailsByZHId} from "../api/moniterTB";
  import {getListNoPage} from "../api/moniterset";

  export default {
    // 新建编辑
    name: 'monitersetCreate',
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
            monitType:'',
            data: {}
          }
        }
      },
      titleName:''
    },
    data() {
      return {
        loading: false,
        form: {
          dataDate:this.initDataSel(),
        },
        list: [],
        tableHeight: 320, // 表的高度
        rules: {
          code: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          dataDate: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          phone: [
            {required: true, message: '必填项不得为空', trigger: 'blur'},
            { validator: validatePhone, trigger: 'blur'}
          ],
          samplerName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          witnessName: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],

        },

        QYshowEditImage: false,
        QYeditImage: null,
        QYsystemImage:'',
        QYeditedImage: null, // 编辑后的图片

        JZshowEditImage: false,
        JZeditImage: null,
        JZsystemImage:'',
        JZeditedImage: null, // 编辑后的图片

        batchId:''
      }
    },
    computed: {
      title() {
        if(this.action.type === 'show'){
            return this.titleName
        }else{
            return this.action.type === 'update' ? '编辑'+this.titleName : '新建'+this.titleName
        }
        
      },
      disabledT() {
        return this.action.type === 'show' ? true : false
      },

    },
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)

      if (this.action.type == "save") {
        this.batchId = guid();
        this.getList()
      }else{
         this.getDetail()
      }
    },

    mounted() {
    },

    beforeDestroy() {
    },

    methods: {
      /** 附件上传  取样*/
      fileUploadQY(content) {
        const reader = new FileReader()
        var self = this
        reader.onload = function(e) {
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
      //删除图片 取样
      deleteSystemImageQY() {
        this.QYsystemImage = ''
        this.QYeditedImage = null
      },   
      // 取样
      submiteImageQY(data) {
        this.QYeditedImage = data
        this.QYsystemImage = data.image
      },

      /** 附件上传  见证*/
      fileUploadJZ(content) {
        const reader = new FileReader()
        var self = this
        reader.onload = function(e) {
          let result
          if (typeof e.target.result === 'object') {
            // 把Array Buffer转化为blob 如果是base64不需要
            result = window.URL.createObjectURL(new Blob([e.target.result]))
          } else {
            result = e.target.result
          }
          self.JZeditImage = result
          self.JZshowEditImage = true
        }
        reader.readAsDataURL(content.file)
      },
      //删除图片 见证
      deleteSystemImageJZ() {
        this.JZsystemImage = ''
        this.JZeditedImage = null
      },   
      // 见证
      submiteImageJZ(data) {
        this.JZeditedImage = data
        this.JZsystemImage = data.image
      },

      /**
       * 获取详情
       * */
      getDetail() {
        getById(this.action.id,this.action.monitType)
          .then(res => {
            this.QYsystemImage = res.data.samplerImgUrl
            this.JZsystemImage = res.data.witnessImgUrl
            // console.log(this.systemImage)
            this.batchId = res.data.batchId

            this.form = res.data;
          
            getDetailsByZHId(this.action.id,this.action.monitType).then(res =>{
              this.list = res.data
            })

          })
          .catch(() => {
          })
      },
      /**
       * 保存
       */
      save(isSubmit) {
        
        this.$refs['form'].validate(valid => {
          
          if (!this.QYsystemImage && !this.QYeditedImage) {
            this.$message.error('取样人员照片不能为空')
            return false
          }

          if (!this.JZsystemImage && !this.JZeditedImage) {
            this.$message.error('见证人员照片不能为空')
            return false
          }

          if (valid) {

            this.form.batchId = this.batchId

            var param = new FormData()
            var s1 = JSON.stringify(this.form)
            var s2 = JSON.stringify(this.list)

            param.append(
              'ZHUB',
              s1
            ) 
            param.append(
              'ZIB',
              s2
            ) 

            if(this.QYeditedImage  != null){
              param.append(
                'QYfile',
                this.QYeditedImage.blob
              )
            }
            
            if(this.JZeditedImage != null){
              param.append(
                'JZfile',
                this.JZeditedImage.blob
              )
            }
            

            this.loading = true;

            saveOrUpdate(param,this.action.monitType).then(response => {
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
      close() {
        // console.log("close")
        this.$emit('close')
      },

       /**
       * 通过回调控制class
       */
      cellClassName({row, column, rowIndex, columnIndex}) {
        if (column.property === 'code') {
          return 'can-visit--underline can-visit--bold'
        }
      },

      headerCellClassName({row, column, rowIndex, columnIndex}) {
        /*if (column.property === 'code') {
          return 'header-can-visit-backgroud'
        }*/
        return ''
      },
      //列格式化  如一些字典等
      fieldFormatter(row, column, cellValue) {
        //console.log(row, column)
        return row[column.property] === '' || row[column.property] === null ? '--' : row[column.property]
      },

      //获取监测项
      getList(){
        let queryParams = {}
        queryParams.type = this.action.monitType

        getListNoPage(queryParams)
          .then(res => {
            this.list = res.data;
            this.loading = false
          })
          .catch(() => {
            this.loading = false
          })
      },
      //选择时间
      initDataSel(){
          //获取当前时间
          var now   = new Date()
          var monthn = now.getMonth()+1>9?now.getMonth()+1:"0"+(now.getMonth()+1)
          var yearn  = now.getFullYear()
          var dayn = now.getDate()>9?now.getDate():"0"+(now.getDate())
          var selectMonth = yearn+"-"+monthn +"-"+dayn
          
          return selectMonth
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
  .el-date-editor{
    width: 100%;
  }
  /*#crm-table{*/
  /*  height: 480px!important;*/
  /*}*/

  .uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
.upload /deep/ .el-upload-dragger {
  width: 100px;
  height: 100px;
}
.upload-show {
  position: relative;
  display: block;
  width: 100px;
  height: 100px;
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
</style>
