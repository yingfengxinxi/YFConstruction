<template>
  <el-form ref="expform" :model="expform" :rules="exprules" label-position="top" label-width="100px"
           class="form">
    <el-row>
      <el-col :span="12">
        <el-form-item label="制造许可编号" prop="manufacturingLicense">
          <el-input v-model="expform.manufacturingLicense" placeholder="请输入制造许可编号"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="设备备案编号" prop="recordNumber">
          <el-input v-model="expform.recordNumber" placeholder="请输入设备备案编号"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="制造厂家" prop="manufacturer">
          <el-input v-model="expform.manufacturer" placeholder="请输入制造厂家"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="出厂编号" prop="manufacturingNo">
          <el-input v-model="expform.manufacturingNo" placeholder="请输入出厂编号"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="检验合格日期" prop="verifyDate">
          <el-date-picker
            v-model="expform.verifyDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择检验合格日期">
          </el-date-picker>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="使用登记日期" prop="recordDate">
          <el-date-picker
            v-model="expform.recordDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择使用登记日期">
          </el-date-picker>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="联系电话" prop="contactNumber">
          <el-input v-model="expform.contactNumber" placeholder="请输入联系电话"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="安装单位" prop="installationUnit">
          <el-input v-model="expform.installationUnit" placeholder="请输入安装单位"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
		<el-row>
		  <el-col :span="12">
		    <el-form-item label="高度预警值" prop="noLoad">
		      <el-input v-model="expform.heightWarningValue" min="0" type="number" placeholder="请输入高度预警值"></el-input>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="高度报警值" prop="heightAlarmValue">
		      <el-input v-model="expform.heightAlarmValue" min="0" type="number" placeholder="请输入高度报警值"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>
		<el-row>
		  <el-col :span="12">
		    <el-form-item label="载重预警值" prop="weightWarningValue">
		      <el-input v-model="expform.weightWarningValue" min="0" type="number" placeholder="请输入载重预警值"></el-input>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="载重报警值" prop="weightAlarmValue">
		      <el-input v-model="expform.weightAlarmValue" min="0" type="number" placeholder="请输入载重报警值"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>
		<el-row>
		  <el-col :span="12">
		    <el-form-item label="速度预警值" prop="speedWarningValue">
		      <el-input v-model="expform.speedWarningValue" min="0" type="number" placeholder="请输入速度预警值"></el-input>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="速度报警值" prop="speedAlarmValue">
		      <el-input v-model="expform.speedAlarmValue" min="0" type="number" placeholder="请输入速度报警值"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>

    <el-row>
      <el-col :span="12">
        <el-form-item label="额定重量（KG）" prop="noLoad">
          <el-input v-model="expform.noLoad" min="0" type="number" max="99.99" placeholder="请输入额定重量"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="基本高度（M）" prop="noLoadActual">
          <el-input v-model="expform.noLoadActual" min="0" type="number" max="99.99" placeholder="请输入基本高度"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
		<el-row>
		  <el-col :span="12">
		    <el-form-item label="起升速度（M/S）" prop="loadValue">
		      <el-input v-model="expform.loadValue" min="0" type="number" max="99.99" placeholder="请输入起升速度"></el-input>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="额定载人数（人）" prop="loadValueActual">
		      <el-input v-model="expform.loadValueActual" min="0" type="number" max="99.99" placeholder="请输入额定载人数"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>
		<el-row>
		  <el-col :span="12">
		    <el-form-item label="吊笼尺寸（M）" prop="lowEnd">
		      <el-input v-model="expform.lowEnd" placeholder="请输入吊笼尺寸"></el-input>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="最大起升高度（M）" prop="lowEndActual">
		      <el-input v-model="expform.lowEndActual" min="0" type="number" max="99.99" placeholder="请输入最大起升高度"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>
		<!--<el-row>-->
		  <!--<el-col :span="12">-->
		    <!--<el-form-item label="高度顶端值（米）" prop="topEnd">-->
		      <!--<el-input v-model="expform.topEnd" min="0" type="number" max="99.99" placeholder="请输入高度低端值"></el-input>-->
		    <!--</el-form-item>-->
		  <!--</el-col>-->
		  <!--<el-col :span="12">-->
		    <!--<el-form-item label="高度顶端实际值（米）" prop="topEndActual">-->
		      <!--<el-input v-model="expform.topEndActual" min="0" type="number" max="99.99" placeholder="请输入高度低端实际值"></el-input>-->
		    <!--</el-form-item>-->
		  <!--</el-col>-->
		<!--</el-row>-->
    <el-row>
      <el-col :span="12">
        <el-form-item label="设备编号" prop="deviceNumber">
          <el-input v-model="expform.deviceNumber" placeholder="请输入设备编号"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="拆除单位" prop="dismantleUnit">
          <el-input v-model="expform.dismantleUnit" placeholder="请输入拆除单位"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="监测机构" prop="detectionUnit">
          <el-input v-model="expform.detectionUnit" placeholder="请输入监测机构"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="产权单位" prop="rentCompany">
          <el-input v-model="expform.rentCompany" placeholder="请输入产权单位"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="产权编号" prop="titleNumber">
          <el-input v-model="expform.titleNumber" placeholder="请输入产权编号"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="安装单位代码" prop="installCompanyCode">
          <el-input v-model="expform.installCompanyCode" placeholder="请输入安装单位代码"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="检测证书编号" prop="checkCertificateNo">
          <el-input v-model="expform.checkCertificateNo" placeholder="请输入检测证书编号"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="设备自编号(检到位)" prop="projectDeviceNo">
          <el-input v-model="expform.projectDeviceNo" placeholder="请输入设备自编号(检到位)"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="使用登记编号" prop="useRegistrationNumber">
          <el-input v-model="expform.useRegistrationNumber" placeholder="请输入使用登记编号"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script>

  import {elevatorSave, elevatorDetail, elevatorDelete} from "./api/elevator"

  export default {
    /**
     * 必须包含
     * */
    name: 'ElevatorExpend',
    components: {},
    props: {
    },
    data() {
      return {
        expform: {},
        exprules: {
          manufacturingLicense: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
        },
      }
    },
    computed: {},
    watch: {
      eqID: function (val) {
        if(val == -1){
          this.expform = {};
        }else {
          this.getDetail();
        }
      }
    },
    created() {
    },

    mounted() {
    },

    beforeDestroy() {
    },

    methods: {
      /**
       * 获取详情:必要
       * */
      expendInit(eqID) {
        if(eqID){
          elevatorDetail(eqID)
            .then(res => {
              this.expform = res.data == null ? {} : res.data;
            })
            .catch(() => {
            })
        }else{
          this.expform = {};
        }

      },
      /**
       * 校验:必要
       */
      expendCheck() {
        var flag = false;
        this.$refs['expform'].validate(valid => {
          if (valid) {
            flag = true;
          }
        })
        return flag;
      },
      /**
       * 保存:必要
       */
      expendSave(eqID) {
        var flag = false;
        var params = this.expform;
        if(eqID){
          params.equipmentId = eqID;
        }
        this.$refs['expform'].validate(valid => {
          if (valid) {
            elevatorSave(params).then(response => {
              if (response.code === 0) {
                // 保存成功
                flag = true;
              }
            }).catch(err => {
            })
          }
          return flag;
        })
      },
      /**
       * 删除:必要
       */
      expendDelete(eqID) {
        if(eqID){
          elevatorDelete(eqID)
            .then(res => {
            })
            .catch(() => {
            })
        }

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
  .el-date-editor {
    width: 100%;
  }
</style>
