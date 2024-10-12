<template>
  <!-- <div class="main">
    <div class="main-body"> -->
      <el-form ref="form" :model="form" :rules="rules" class="add-form" :inline="true" style="padding-top: 20px;padding-left:30px;">

          <div class="section">
            <!-- 基本信息  开始 -->
            <create-sections title="基本信息">
              <div class="section-content">
                  
                  <el-row>
                    <el-col :span="6">
                      <el-row>
                        <el-col :span="24">
                            <el-upload
                              v-if="!systemImage"
                              :show-file-list="false"
                              :http-request="fileUpload"
                              drag
                              class="upload"
                              action="http"
                              accept="image/png, image/jpeg, image/gif, image/jpg">
                              <i class="el-icon-plus uploader-icon"/>
                            </el-upload>
                            <div
                              v-else
                              class="upload-show">
                              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="systemImage">
                              <i
                                class="el-icon-remove icon-delete"
                                @click="deleteSystemImage"/>
                            </div>
                      
                            <edit-image
                              :fixed-number="[20, 20]"
                              :show="showEditImage"
                              :image="editImage"
                              :file="editFile"
                              title="编辑身份证头像"
                              preview-width="200px"
                              preview-height="200px"
                              preview-radius="0"
                              width="550px"
                              save-button-title="确定"
                              @save="submiteImage"
                              @close="showEditImage=false"/>
                        </el-col>
                      </el-row>
                      <!-- <el-row>
                        <el-col :span="24">
                          <p align="center" style="width:200px;height: 200px;line-height: 22px;">
                              上传最新头像照片
                              以提高人脸识别率,
                              图片尺寸为640*480左右,
                              图片中只有一个人像,
                              图片中人像端正,
                              图片中人像占图片30%以上,
                              面部区域像素不低于128x128,
                              图片大小不超过400K,
                              确保同一人员注册照均为本人
                          </p>
                        </el-col>
                      </el-row> -->
                        
                    </el-col>
                    <el-col :span="18">
                        <div class="infofrom">
                          <el-row>
                            <el-col :span="8">
                                <el-form-item label="身份证号" prop="idCard" class="add-form-input">
                                  <el-input v-model="form.idCard" placeholder="请输入" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="姓名" prop="name" class="add-form-input">
                                  <el-input v-model="form.name" placeholder="请输入" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item  label="民族" class="add-form-input" prop="nation" >
                                  <el-select
                                          v-model="form.nation"
                                          placeholder="请选择"
                                          clearable
                                          size="small">
                                    <el-option
                                            v-for="dict in nationOptions"
                                            :key="dict.dictValue"
                                            :label="dict.dictLabel"
                                            :value="dict.dictValue" />
                                  </el-select>
                                </el-form-item>
                            </el-col>
                          </el-row>
                          <el-row>
                            <el-col :span="8">
                              <el-form-item  label="性别" class="add-form-input" prop="sex" >
                                  <el-select
                                          v-model="form.sex"
                                          placeholder="请选择"
                                          clearable
                                          size="small">
                                    <el-option
                                            v-for="dict in sexOptions"
                                            :key="dict.dictValue"
                                            :label="dict.dictLabel"
                                            :value="dict.dictValue" />
                                  </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                              <el-form-item label="出生日期" prop="birthday">
                                  <el-date-picker
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    v-model="form.birthday"
                                    type="date"
                                    placeholder="选择日期">
                                  </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                              <el-form-item label="家庭住址" prop="address" class="add-form-input">
                                  <el-input v-model="form.address" placeholder="请输入" />
                                </el-form-item> 
                            </el-col>
                          </el-row>
                          <el-row>
                            <el-col :span="8">
                              <el-form-item label="发证机关" prop="issuingAuthority" class="add-form-input">
                                  <el-input v-model="form.issuingAuthority" placeholder="请输入" />
                                </el-form-item> 
                            </el-col>
                            <el-col :span="8">
                              <el-form-item label="有效期启" prop="dateStart">
                                  <el-date-picker
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    v-model="form.dateStart"
                                    type="date"
                                    placeholder="选择日期" >
                                  </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                              <el-form-item label="有效期止" prop="dateEnd">
                                  <el-date-picker
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    v-model="form.dateEnd"
                                    type="date"
                                    placeholder="选择日期" >
                                  </el-date-picker>
                                </el-form-item>
                            </el-col>
                          </el-row>
                          <el-row>
                            <el-col :span="8">
                              <el-form-item  label="职业健康" class="add-form-input" prop="occupationalHealth" >
                                <el-select
                                        v-model="form.occupationalHealth"
                                        placeholder="请选择"
                                        clearable
                                        size="small">
                                  <el-option
                                          v-for="dict in occupationalHealthOptions"
                                          :key="dict.dictValue"
                                          :label="dict.dictLabel"
                                          :value="dict.dictValue" />
                                </el-select>
                              </el-form-item>
                            </el-col>
                          </el-row>
                          
                      </div>
                    </el-col>
                  </el-row>
                  
              </div>
            </create-sections>
            <!-- 基本信息  结束 -->

            <!-- 进场信息  开始 -->
            <create-sections title="进场信息">
              <div class="infofrom">

                  <el-form-item  label="进场状态" class="add-form-input" prop="enterStatus" >
                    <el-select
                            v-model="form.enterStatus"
                            placeholder="请选择"
                            clearable
                            size="small">
                      <el-option
                              v-for="dict in enterStatusOptions"
                              :key="dict.dictLabel"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                    </el-select>
                  </el-form-item>

                  <el-form-item label="进场日期" prop="enterDate">
                    <el-date-picker
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd"
                      v-model="form.enterDate"
                      type="date"
                      placeholder="选择日期">
                    </el-date-picker>
                  </el-form-item>

                  <el-form-item label="联系电话" prop="phone" class="add-form-input">
                    <el-input v-model="form.phone"  placeholder="请输入" />
                  </el-form-item> 

                  <el-form-item  label="上传政府平台" class="add-form-input" prop="uploadGovernmentPlatform" >
                    <el-select
                            v-model="form.uploadGovernmentPlatform"
                            placeholder="请选择"
                            clearable
                            size="small">
                      <el-option
                              v-for="dict in ynOptions"
                              :key="dict.dictValue"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                    </el-select>
                  </el-form-item>

                  <el-form-item  label="计价方式" class="add-form-input" prop="valuationMethod" >
                    <el-select
                            v-model="form.valuationMethod"
                            placeholder="请选择"
                            clearable
                            size="small">
                      <el-option
                              v-for="dict in valuationMethodOptions"
                              :key="dict.dictLabel"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                    </el-select>
                  </el-form-item>

                  <el-form-item label="每日工资" prop="dailyWage" class="add-form-input">
                    <el-input v-model.number="form.dailyWage" type="number" placeholder="请输入" />
                  </el-form-item> 

                  <el-form-item label="考勤卡号" prop="attendanceNumber" class="add-form-input">
                    <el-input v-model="form.attendanceNumber" placeholder="请输入" />
                  </el-form-item> 

                  <el-form-item label="备注" prop="note" class="add-form-input">
                    <el-input v-model="form.note" placeholder="请输入" />
                  </el-form-item> 
    
              </div>
            </create-sections>
            <!-- 进场信息  结束 -->
            
          </div>

          <!-- <div style="text-align:center;">
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div> -->
      </el-form>

    <!-- </div>
  </div> -->
</template>

<script>

  import EditImage from '@/components/EditImage'
  import XrHeader from '@/components/XrHeader'
  import request from '@/utils/request'
  import SelectCol from "@/views/admin/components/selectCol";
  import { validatePhone,validateIdNo,isNum,guid } from '@/utils'
  import CreateSections from '@/components/CreateSections'

  export default {
    name: 'worktype',
    components: {
      EditImage,
      XrHeader,
      SelectCol,
      CreateSections
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 表单参数
        form: {},

        //性别
        sexOptions: [],
        //民族
        nationOptions:[],
        //职业健康
        occupationalHealthOptions:[],
        //是否
        ynOptions: [],
        //计价方式
        valuationMethodOptions: [],

        // 进场状态
        enterStatusOptions:[],

        // 表单校验
        rules: {
          idCard: [
            { required: true, message: '身份证号不能为空', trigger: 'blur' },
            {validator: validateIdNo,trigger: 'blur'}
          ],
          name: [
            { required: true, message: '姓名不能为空', trigger: 'blur' }
          ],
          sex: [
            { required: true, message: '性别不能为空', trigger: 'blur' }
          ],
          nation: [
            { required: true, message: '民族不能为空', trigger: 'blur' }
          ],
          address: [
            { required: true, message: '家庭住址不能为空', trigger: 'blur' }
          ],
          birthday: [
            { required: true, message: '出生日期不能为空', trigger: 'blur' }
          ],
          issuingAuthority: [
            { required: true, message: '发证机关不能为空', trigger: 'blur' }
          ],
          dateStart: [
            { required: true, message: '有效期启不能为空', trigger: 'blur' }
          ],
          dateEnd: [
            { required: true, message: '有效期止不能为空', trigger: 'blur' }
          ],
          dailyWage:[
            { required: true, message: '每日工资不能为空', trigger: 'blur' }
          ],

          phone: [
            {  required: true, message: '联系电话不能为空', trigger: 'blur' }
          ]
          
        },
        pinyin:'',
        addpin:'',
        showEditImage: false,
        editImage: null,
        editFile: null,
        systemName: '',
        systemImage: '',
        editedImage: null, // 编辑后的图片

        batchId:"",
        show:"",

      }
    },
    //接收参数
    props:["UID"],
    created() {

      //  性别字典项
      this.getDicts('sys_user_sex').then(response => {
        this.sexOptions = response.data
      })
      //  民族字典项
      this.getDicts('b_nation').then(response => {
        this.nationOptions = response.data
      })
       // 职业健康 
      this.getDicts('b_occupational_health').then(response => {
        this.occupationalHealthOptions = response.data
      })
      //  是否字典项
      this.getDicts('sys_yes_no').then(response => {
        this.ynOptions = response.data
      })
      //  进场 劳动合同 字典项
      this.getDicts('b_laborContract').then(response => {
        this.laborContractOptions = response.data
      })
      
      //  计价方式字典项
      this.getDicts('b_valuation_method').then(response => {
        this.valuationMethodOptions = response.data
      })

      //  进场状态
      this.getDicts('b_enterStatus').then(response => {
        this.enterStatusOptions = response.data
      })

      if(this.UID != null && this.UID != ''){
        this.getInfo()
      }

    },
    watch: {
        'UID': function (val, oldVal) {
          if(this.UID != null && this.UID != ''){
            this.getInfo()
          }
		  },
		},
    methods: {
      /** 附件上传 */
      fileUpload(content) {
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
          self.editImage = result
          self.editFile = content.file
          self.showEditImage = true
        }
        reader.readAsDataURL(content.file)
      },
      //删除图片
      deleteSystemImage() {
        this.systemImage = ''
        this.editedImage = null
      },   
      submiteImage(data) {
        this.editedImage = data
        this.systemImage = data.image
      },

      // 表单重置
      reset() {
        this.systemImage = ''
        this.editedImage = null
        this.form = {}
        this.resetForm('form')
      },
      /** 提交按钮 */
      submitForm: function() {

        let flag=false
        this.$refs['form'].validate(valid => {
          
          var param = new FormData()

          this.form.ifOddjob = "1" //是零工
          console.log(this.form)

          var s1 = JSON.stringify(this.form)
          param.append(
            'form',
            s1
          )
          this.show=param
          
          if (valid) {
            flag = true 
          }else{
          	flag = false
          }
        })
         return flag
      },

      //获取当前id的数据信息
      getInfo(){
        request({
          url: 'build/labourInfocollection/queryById/'+this.UID,
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(response => {
          this.systemImage = response.data.imgCardUrl

          this.batchId = response.data.batchId

          this.form = response.data
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },

    }
  }
</script>


<style lang="scss" scoped>
  .main {
    height:100%;
  }
  /deep/ .xr-header {
    padding: 15px 30px;
  }

  .main-body {
    background-color: white;
    /*border-top: 1px solid $xr-border-line-color;*/
    border-bottom: 1px solid $xr-border-line-color;
    height: 90%;
    overflow-y: auto;
  }

  .main-table-header-button {
    float: right;
    margin-right: 20px;
    margin-top: 10px;
  }

.section + .section {
  margin-top: 50px;
}

.section-title {
  color: #333;
  font-weight: bold;
  font-size: 14px;
  margin-bottom: 30px;
}

.section-content {
  margin-bottom: 30px;
  .name {
    color: #333;
    font-size: 14px;
    margin-bottom: 10px;
  }

  .el-input {
    width: 300px;
  }
}

  .uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 200px;
  height: 200px;
  line-height: 200px;
  text-align: center;
}
.upload /deep/ .el-upload-dragger {
  width: 200px;
  height: 200px;
}
.upload-show {
  position: relative;
  display: block;
  width: 200px;
  height: 200px;
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


  .add-form::after{
    content: "";
    display: block;
    clear: both;
  }

  .link-type,.link-type:focus{color:#337ab7;cursor:pointer}

  .link-type:focus:hover,.link-type:hover{color:#20a0ff}

  .el-table {
    border-top: 1px solid $xr-border-line-color;
  }
	// /deep/ .special .el-form-item__label{
	// 	width: 100px!important;
	// }
  .upload{
    float: left;
  }
.information{
  display: inline-block;
  width: 200px;
  height: 200px;
  margin-left: 30px;
  margin-top: 60px;
}
.infofrom{
  clear: both;
}
/deep/ .workTypeClass .el-radio-button__inner{
  color:red;
}
.upload-show{
	display: inline-block;
}

/deep/ .section-content .el-input{
  width: 160px;
}
/deep/.el-form-item__label{
  width: 90px!important;
}
/deep/.el-date-editor.el-input, .el-date-editor.el-input__inner{
  width: 160px;
}
/deep/.el-input__inner{
  width: 160px;
}

</style>
