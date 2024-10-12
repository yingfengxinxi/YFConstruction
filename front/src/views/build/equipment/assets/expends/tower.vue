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
        <el-form-item label="力矩预警值" prop="torqueWarningValue">
          <el-input v-model="expform.torqueWarningValue" min="0" type="number"  placeholder="请输入力矩预警值"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="力矩报警值" prop="torqueAlarmValue">
          <el-input v-model="expform.torqueAlarmValue" min="0" type="number"  placeholder="请输入力矩报警值"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
		<el-row>
		  <el-col :span="12">
		    <el-form-item label="吊重预警值" prop="weightWarningValue">
		      <el-input v-model="expform.weightWarningValue" min="0" type="number"  placeholder="请输入吊重预警值"></el-input>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="吊重报警值" prop="weightAlarmValue">
		      <el-input v-model="expform.weightAlarmValue" min="0" type="number"  placeholder="请输入吊重报警值"></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="前向臂高（米）" prop="foreArmLength">
          <el-input v-model="expform.foreArmLength" min="0" type="number" max="99.99" placeholder="请输入前向臂高"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="后向臂高（米）" prop="rearArmLength">
          <el-input v-model="expform.rearArmLength" min="0" type="number" max="99.99" placeholder="请输入后向臂高"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="额定吊重（吨）" prop="maxLoad">
          <el-input v-model="expform.maxLoad" min="0" type="number" max="99.99" placeholder="请输入额定吊重"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="最大吊重（吨）" prop="maxWeight">
          <el-input v-model="expform.maxWeight" min="0" type="number" max="99.99" placeholder="请输入最大吊重"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="额定力矩" prop="ratedTorque">
          <el-input v-model="expform.ratedTorque" min="0" type="number" max="99.99" placeholder="请输入额定力矩"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="额定风速（m/s）" prop="ratedWindSpeed">
          <el-input v-model="expform.ratedWindSpeed" min="0" type="number" max="36.90" placeholder="请输入额定风速"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
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
        <el-form-item label="塔吊设备类型" prop="customTowerCraneType">
          <el-select v-model="expform.customTowerCraneType" placeholder="请选择">
            <el-option label="尖头塔" value="1"></el-option>
            <el-option label="平头塔" value="2"></el-option>
            <el-option label="动臂塔" value="3"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="监测机构" prop="detectionUnit">
          <el-input v-model="expform.detectionUnit" placeholder="请输入监测机构"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="SIM卡号" prop="simCardNo">
          <el-input v-model="expform.simCardNo" placeholder="请输入SIM卡号"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="硬件版本号" prop="hardwareVer">
          <el-input v-model="expform.hardwareVer" placeholder="请输入硬件版本号"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="协议版本号" prop="protocolVer">
          <el-input v-model="expform.protocolVer" placeholder="请输入协议版本号"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="塔机名称编号" prop="selfCraneNo">
          <el-input v-model="expform.selfCraneNo" placeholder="请输入塔机名称编号1"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="塔机类型" prop="craneType">
          <el-select v-model="expform.craneType" placeholder="请选择">
            <el-option label="动臂吊" value="1"></el-option>
            <el-option label="塔头平臂吊" value="2"></el-option>
            <el-option label="平头平臂吊" value="3"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="配置载重功能">
          <el-select style="width: 100%;" v-model="expform.weightSet" placeholder="请选择">
            <el-option
              v-for="item in towerCraneConfig"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="配置风速功能">
          <el-select style="width: 100%;" v-model="expform.windSpeedSet" placeholder="请选择">
            <el-option
              v-for="item in towerCraneConfig"
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
        <el-form-item label="配置幅度功能">
          <el-select style="width: 100%;" v-model="expform.rangeSet" placeholder="请选择">
            <el-option
              v-for="item in towerCraneConfig"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="配置高度功能">
          <el-select style="width: 100%;" v-model="expform.heightSet" placeholder="请选择">
            <el-option
              v-for="item in towerCraneConfig"
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
        <el-form-item label="配置角度功能">
          <el-select style="width: 100%;" v-model="expform.angleSet" placeholder="请选择">
            <el-option
              v-for="item in towerCraneConfig"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="配置倾角功能">
          <el-select style="width: 100%;" v-model="expform.obliguitySet" placeholder="请选择">
            <el-option
              v-for="item in towerCraneConfig"
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
        <el-form-item label="配置gps功能">
          <el-select style="width: 100%;" v-model="expform.gpsSet" placeholder="请选择">
            <el-option
              v-for="item in towerCraneConfig"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="item.dictValue">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="配置人员识别功能">
          <el-select style="width: 100%;" v-model="expform.idSet" placeholder="请选择">
            <el-option
              v-for="item in towerCraneConfig"
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
        <el-form-item label="额定风级" prop="ratedWindLevel">
          <el-input v-model="expform.ratedWindLevel" min="0" type="number" max="12" placeholder="请输入额定风级"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="最小变幅" prop="minRange">
          <el-input v-model="expform.minRange" min="0" type="number" max="36.90" placeholder="请输入最小变幅"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="大臂高" prop="armHeight">
          <el-input v-model="expform.armHeight" min="0" type="number" max="655" placeholder="请输入大臂高"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="最大高度" prop="maxHeight">
          <el-input v-model="expform.maxHeight" min="0" type="number" max="655" placeholder="请输入最大高度"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="最大角度" prop="maxAngle">
          <el-input v-model="expform.maxAngle" type="number" max="540" placeholder="请输入最大角度"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="最小角度" prop="minAngle">
          <el-input v-model="expform.minAngle" min="-540" type="number" max="540" placeholder="请输入最小角度"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="额定倾角" prop="ratedObliguity">
          <el-input v-model="expform.ratedObliguity" min="0" type="number" max="9.99" placeholder="请输入额定倾角"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="最大幅度允许最大载重" prop="tcMsLoadCapacity">
          <el-input v-model="expform.tcMsLoadCapacity" min="0" type="number" max="99"
                    placeholder="请输入最大幅度允许最大载重"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="最大载重达到最大幅度" prop="tcMlMaxScope">
          <el-input v-model="expform.tcMlMaxScope" min="0" type="number" max="655"
                    placeholder="请输入最大载重达到最大幅度"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="载重滤波等级" prop="differInput">
          <el-input v-model="expform.differInput" min="0" type="number" max="9" placeholder="请输入载重滤波等级"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="高度报警阈值" prop="settingMinHeight">
          <el-input v-model="expform.settingMinHeight" min="0" type="number" max="99"
                    placeholder="请输入高度报警阈值"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="载重零点值" prop="weightZero">
          <el-input v-model="expform.weightZero" min="0" type="number" max="99.99" placeholder="请输入载重零点"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="风速零点值" prop="windSpeedZero">
          <el-input v-model="expform.windSpeedZero" min="0" type="number" max="99" placeholder="请输入高度报警阈值"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="角度零点值" prop="angleZero">
          <el-input v-model="expform.angleZero" min="0" type="number" max="99.99" placeholder="请输入角度零点值"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="倾角X零点值" prop="dipxZero">
          <el-input v-model="expform.dipxZero" min="0" type="number" max="9.99" placeholder="请输入倾角X零点值"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="倾角Y零点值" prop="dipyZero">
          <el-input v-model="expform.dipyZero" min="0" type="number" max="9.99" placeholder="请输入角度零点值"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="动臂吊仰角零点值" prop="elevationAngleZero">
          <el-input v-model="expform.elevationAngleZero" min="0" type="number" max="99"
                    placeholder="请输入倾角X零点值"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script>

  import {towerSave, towerDetail, towerDelete} from "./api/tower"

  export default {
    /**
     * 必须包含
     * */
    name: 'ToweExpend',
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
        towerCraneConfig: [],
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
      this.getDicts('b_tower_crane_config').then(response => {
        this.towerCraneConfig = response.data
      })
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
          towerDetail(eqID)
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
            towerSave(params).then(response => {
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
          towerDelete(eqID)
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
