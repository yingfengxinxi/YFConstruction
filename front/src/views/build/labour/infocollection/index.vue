<template>
  <div class="main">
<!--    <xr-header-->
<!--            icon-class="wk wk-project"-->
<!--            icon-color="#2362FB"-->
<!--            label="信息采集" />-->
    <div class="main-body">
      <el-form ref="form" :model="form" :rules="rules" class="add-form" :inline="true" style="padding-top: 20px;padding-left:30px;">

          <!-- 基本信息  开始 -->
             <create-sections title="基本信息">
                <div class="section-content">

                    <el-row>
                      <el-col :span="8">
                        <el-row>
                          <el-col :span="12">
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


                              <!-- <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="information" src="../../../../../static/img/a.jpg"> -->
                          </el-col>
                          <el-col :span="12">
                            <p align="center" style="height: 200px;line-height: 22px;">
                                上传最新头像照片<br>
                                以提高人脸识别率<br>
                                图片尺寸为640*480左右<br>
                                图片中只有一个人像<br>
                                图片中人像端正<br>
                                图片中人像占图片30%以上，<br>
                                面部区域像素不低于128x128<br>
                                图片大小不超过400K<br>
                                确保同一人员注册照均为本人<br>
                            </p>
                          </el-col>
                        </el-row>

                      </el-col>
                      <el-col :span="16">
                          <div class="infofrom">
                            <el-row>
                              <el-col :span="8">
                                  <el-form-item label="身份证号" prop="idCard" class="add-form-input">
                                   <!-- <el-input v-model="form.idCard" @change="idCardBlur" placeholder="请输入" />-->
                                    <el-input v-model="form.idCard"  placeholder="请输入" />
                                  </el-form-item>
                                  <el-button type="success" style="margin-top:4px" @click="alert">读 卡</el-button>
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
                                <el-form-item label="发证机关" prop="issuingAuthority" class="add-form-input">
                                  <el-input v-model="form.issuingAuthority" placeholder="请输入" />
                                </el-form-item>
                              </el-col>
                            </el-row>
                            <el-row>
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
                            <el-row>
                              <el-col :span="8">
                                <el-form-item  label="婚姻状况" class="add-form-input" prop="maritalStatus" >
                                  <el-select
                                          v-model="form.maritalStatus"
                                          placeholder="请选择"
                                          clearable
                                          size="small">
                                    <el-option
                                            v-for="dict in maritalStatusOptions"
                                            :key="dict.dictValue"
                                            :label="dict.dictLabel"
                                            :value="dict.dictValue" />
                                  </el-select>
                                </el-form-item>
                              </el-col>
                              <el-col :span="8">
                                  <el-form-item label="工龄" prop="workAge" class="add-form-input">
                                    <el-input v-model="form.workAge" placeholder="请输入" />
                                  </el-form-item>
                              </el-col>
                              <el-col :span="8">
                                <el-form-item label="家庭住址" prop="address" class="add-form-input yangshi2" style="width: 100%" >
                                  <el-input  v-model="form.address" :rows="4" style="width: 535px;" placeholder="请输入" class="yangshi" />
                                </el-form-item>
                              </el-col>
                            </el-row>
                        </div>
                      </el-col>
                    </el-row>

                </div>
             </create-sections>

          <!-- 基本信息  结束 -->

            <!-- 所属工种  开始 -->
            <create-sections title="所属工种">
              <div style="color:red;margin-bottom: 30px;">*特殊工种为红色，需要维护证件信息</div>
              <div class="infofrom">
				<el-form-item  label="工人类型" class="add-form-input" prop="workRole" >
				  <el-select
						  v-model="form.workRole"
						  placeholder="请选择"
						  clearable
						  size="small"
						  style="width: 160px">
					<el-option
							v-for="dict in workRoleOptions"
							:key="dict.dictLabel"
							:label="dict.dictLabel"
							:value="dict.dictValue" />
				  </el-select>
				</el-form-item>
				<el-form-item v-if="form.workRole == 10" label="管理岗位" class="add-form-input" prop="manageType" >
				  <el-select
						  v-model="form.manageType"
						  placeholder="请选择"
						  clearable
						  size="small"
						  style="width: 160px">
					<el-option
							v-for="dict in manageTypeOptions"
							:key="dict.dictLabel"
							:label="dict.dictLabel"
							:value="dict.dictValue" />
				  </el-select>
				</el-form-item>
				<!-- 工种改为字典，原先数据表废弃 原数据列表 worktypeList 字段由workTypeId改为workTypeDict-->
                <el-radio-group v-if="form.workRole == 20"
                v-model="form.workTypeDict"
                v-for="worktype in workTypeOptions"
                :key="worktype.dictValue"
                :class="specialWorkTypeList.includes(worktype.dictValue)?'workTypeClass':''"
                style="margin-bottom: 30px;"
                @change="putworkTypeIfspecial(worktype)">
                  <el-radio-button
                  :label="worktype.dictValue" >
                    {{worktype.dictLabel}}
                  </el-radio-button>
                </el-radio-group>
              </div>
            </create-sections>
            <!-- 所属工种  结束 -->

            <!-- 证书信息  开始 -->
            <div v-show="workTypeIfspecial">
              <create-sections title="证书信息">
                <div class="infofrom">
                  <el-form-item label="证书名称" prop="cerName" class="add-form-input">
                  <el-input v-model="cerForm.cerName" placeholder="请输入" />
                  </el-form-item>
                  <el-form-item label="证书编号" prop="cerNumber" class="add-form-input">
                    <el-input v-model="cerForm.cerNumber" placeholder="请输入" />
                  </el-form-item>

                  <el-form-item  label="证书种类" class="add-form-input" prop="cerCategory" >
                    <el-select
                            v-model="cerForm.cerCategory"
                            @change="cerOptions"
                            placeholder="请选择"
                            clearable
                            size="small"
                            style="width: 160px">
                      <el-option
                              v-for="dict in cerCategoryOptions"
                              :key="dict.dictValue"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                    </el-select>
                  </el-form-item>

                  <el-form-item  label="证书类型" class="add-form-input" prop="cerCategoryType" >
                    <el-select
                            v-model="cerForm.cerCategoryType"
                            placeholder="请选择"
                            clearable
                            size="small"
                            style="width: 160px">
                      <el-option
                              v-for="dict in cerCategoryTypeOptions_"
                              :key="dict.dictValue"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                    </el-select>
                  </el-form-item>

                  <el-form-item  label="证书等级" class="add-form-input" prop="cerLevel" >
                    <el-select
                            v-model="cerForm.cerLevel"
                            placeholder="请选择"
                            clearable
                            size="small"
                            style="width: 160px">
                      <el-option
                              v-for="dict in cerLevelOptions"
                              :key="dict.dictValue"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                    </el-select>
                  </el-form-item>

                  <el-form-item label="岗位名称" prop="positionTitle" class="add-form-input">
                    <el-input v-model="cerForm.positionTitle" placeholder="请输入" />
                  </el-form-item>

                  <el-form-item label="第一次发证日期" prop="cerStartDate">
                    <el-date-picker
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd"
                      v-model="cerForm.cerStartDate"
                      type="date"
                      placeholder="选择日期" >
                    </el-date-picker>
                  </el-form-item>

                  <el-form-item label="有效期启" prop="cerUsestartDate">
                    <el-date-picker
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd"
                      v-model="cerForm.cerUsestartDate"
                      type="date"
                      placeholder="选择日期" >
                    </el-date-picker>
                  </el-form-item>

                  <el-form-item label="有效期止" prop="cerUseendDate">
                    <el-date-picker
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd"
                      v-model="cerForm.cerUseendDate"
                      type="date"
                      placeholder="选择日期" >
                    </el-date-picker>
                  </el-form-item>

                  <el-form-item label="发证机关" prop="cerIssuingAuthority" class="add-form-input">
                    <el-input v-model="cerForm.cerIssuingAuthority" placeholder="请输入" />
                  </el-form-item>

                  <el-form-item label="复核日期" prop="cerReviewDate">
                    <el-date-picker
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd"
                      v-model="cerForm.cerReviewDate"
                      type="date"
                      placeholder="选择日期" >
                    </el-date-picker>
                  </el-form-item>

                  <el-form-item label="证书验证" class="add-form-input">
                    <el-button type="primary" >点击证书验证</el-button>
                  </el-form-item>

                </div>
              </create-sections>
            </div>

            <!-- 证书信息  结束 -->

            <!-- 进场信息  开始 -->
            <create-sections title="进场信息">
              <div style="color:red;margin-bottom: 30px;">*第一个先录班组长（是否班组长选【是】），再录其他工人！</div>
              <div class="infofrom">

                <el-form-item  label="是否班组长" class="add-form-input" prop="ifTeamLeader" >
                    <el-select
                            v-model="form.ifTeamLeader"
                            @change="selectIfTeamLeader"
                            placeholder="请选择"
                            clearable
                            size="small"
                            style="width: 160px">
                      <el-option
                              v-for="dict in ynOptions"
                              :key="dict.dictValue"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                    </el-select>
                  </el-form-item>

                  <el-form-item  label="班组名称" class="add-form-input teamNameClass" prop="teamName" >
                    <el-select
                            v-model="form.teamName"
                            @change="selectIfTeamLeader_1"
                            placeholder="请选择"
                            v-bind:disabled="isAble"
                            clearable
                            size="small"
                            style="width: 160px">
                      <el-option
                              v-for="dict in BZList"
                              :key="dict.teamName+dict.id"
                              :label="dict.teamName"
                              :value="dict.id" />
                    </el-select>
                  </el-form-item>

                  <el-form-item  label="分包单位" class="add-form-input subcontractorClass" prop="subcontractor" >
                    <el-select
                            v-model="form.subcontractor"
                            @change="selectIfTeamLeader_2"
                            placeholder="请选择"
                            v-bind:disabled="!isAble"
                            clearable
                            size="small"
                            style="width: 160px">
                      <el-option
                              v-for="dict in FBSList"
                              :key="dict.realName"
                              :label="dict.realName"
                              :value="dict.id" />
                    </el-select>
                  </el-form-item>

                  <el-form-item  label="进场状态" class="add-form-input" prop="enterStatus" >
                    <el-select
                            v-model="form.enterStatus"
                            placeholder="请选择"
                            clearable
                            size="small"
                            style="width: 160px">
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

                  <el-form-item  label="文化程度" class="add-form-input" prop="educationLevel" >
                    <el-select
                            v-model="form.educationLevel"
                            placeholder="请选择"
                            clearable
                            size="small"
                            style="width: 160px">
                      <el-option
                              v-for="dict in educationLevelOptions"
                              :key="dict.dictLabel"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                    </el-select>
                  </el-form-item>

                  <el-form-item  label="计价方式" class="add-form-input" prop="valuationMethod" >
                    <el-select
                            v-model="form.valuationMethod"
                            placeholder="请选择"
                            clearable
                            size="small"
                            style="width: 160px">
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

                  <el-form-item  label="政治面貌" class="add-form-input" prop="politicalStatus" >
                    <el-select
                            v-model="form.politicalStatus"
                            placeholder="请选择"
                            clearable
                            size="small"
                            style="width: 160px">
                      <el-option
                              v-for="dict in politicalStatusOptions"
                              :key="dict.dictLabel"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                    </el-select>
                  </el-form-item>

                  <el-form-item  label="开户银行" class="add-form-input" prop="bankAccount" >
                    <el-select
                            v-model="form.bankAccount"
                            placeholder="请选择"
                            clearable
                            size="small"
                            style="width: 160px">
                      <el-option
                              v-for="dict in bankAccountOptions"
                              :key="dict.dictLabel"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="银行名称" prop="payRollBankName" class="add-form-input">
                    <el-input v-model="form.payRollBankName"  placeholder="请输入" />
                  </el-form-item>
                  <el-form-item label="银行联号" prop="bankLinkNumber" class="add-form-input">
                    <el-input v-model="form.bankLinkNumber"  placeholder="请输入" />
                  </el-form-item>
                  <el-form-item label="银行卡号" prop="bankCardNumber" class="add-form-input">
                    <el-input v-model="form.bankCardNumber"  placeholder="请输入" />
                  </el-form-item>

                  <el-form-item label="联系电话" prop="phone" class="add-form-input">
                    <el-input v-model="form.phone"  placeholder="请输入" />
                  </el-form-item>
                  <el-form-item label="邮政编码" prop="postalCode" class="add-form-input">
                    <el-input v-model="form.postalCode"  placeholder="请输入" />
                  </el-form-item>
				  <el-form-item label="发卡时间" prop="issueCardDate">
				    <el-date-picker
				      format="yyyy-MM-dd"
				      value-format="yyyy-MM-dd"
				      v-model="form.issueCardDate"
				      type="date"
				      placeholder="选择日期">
				    </el-date-picker>
				  </el-form-item>
                  <el-form-item label="考勤卡号" prop="attendanceNumber" class="add-form-input">
                    <el-input v-model="form.attendanceNumber" placeholder="请输入" />
                  </el-form-item>
                  <el-form-item  label="是否购买工伤或意外伤害保险" class="add-form-input label_2" prop="hasBuyInsurance" >
                    <el-select
                            v-model="form.hasBuyInsurance"
                            placeholder="请选择"
                            clearable
                            size="small"
                            style="width: 160px">
                      <el-option
                              v-for="dict in qgYesNoOptions"
                              :key="dict.dictValue"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                    </el-select>
                  </el-form-item>
				  <el-form-item label="加入公会时间" prop="joinedTime">
				    <el-date-picker
				      format="yyyy-MM-dd"
				      value-format="yyyy-MM-dd"
				      v-model="form.joinedTime"
				      type="date"
				      placeholder="选择日期">
				    </el-date-picker>
				  </el-form-item>
                  <el-form-item label="特长" prop="Specialty" class="add-form-input">
                    <el-input v-model="form.Specialty" placeholder="请输入" />
                  </el-form-item>
                  <el-form-item  label="是否有重大病史" class="add-form-input label_1" prop="hasBuyInsurance" >
                    <el-select
                            v-model="form.hasBadMedicalHistory"
                            placeholder="请选择"
                            clearable
                            size="small"
                            style="width: 160px">
                      <el-option
                              v-for="dict in qgYesNoOptions"
                              :key="dict.dictValue"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="紧急联系人姓名" prop="urgentLinkMan" class="add-form-input label_1">
                    <el-input v-model="form.urgentLinkMan" placeholder="请输入" />
                  </el-form-item>
                  <el-form-item label="紧急联系方式" prop="urgentLinkManPhone" class="add-form-input">
                    <el-input v-model="form.urgentLinkManPhone" placeholder="请输入" />
                  </el-form-item>

                  <el-form-item  label="劳动合同" class="add-form-input" prop="laborContract" >
                    <el-select
                            v-model="form.laborContract"
                            @change="selectlaborContract"
                            placeholder="请选择"
                            clearable
                            size="small"
                            style="width: 160px">
                      <el-option
                              v-for="dict in laborContractOptions"
                              :key="dict.dictValue"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                    </el-select>
                  </el-form-item>

                  <el-form-item  label="上传政府平台" class="add-form-input" prop="uploadGovernmentPlatform" >
                    <el-select
                            v-model="form.uploadGovernmentPlatform"
                            placeholder="请选择"
                            clearable
                            size="small"
                            style="width: 160px">
                      <el-option
                              v-for="dict in ynOptions"
                              :key="dict.dictValue"
                              :label="dict.dictLabel"
                              :value="dict.dictValue" />
                    </el-select>
                  </el-form-item>


              </div>
             </create-sections>
            <!-- 进场信息  结束 -->

            <!-- 合同信息  开始 -->
            <div v-show="HTIf">
              <create-sections title="合同信息">
                <div class="infofrom">

                  <el-form-item label="签订日期" prop="signingDate">
                      <el-date-picker
                        format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd"
                        v-model="form.signingDate"
                        type="date"
                        placeholder="选择日期" >
                      </el-date-picker>
                    </el-form-item>

                    <el-form-item label="生效日期" prop="effectiveDate">
                      <el-date-picker
                        format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd"
                        v-model="form.effectiveDate"
                        type="date"
                        placeholder="选择日期" >
                      </el-date-picker>
                    </el-form-item>

                    <el-form-item label="失效日期" prop="expirationDate">
                      <el-date-picker
                        format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd"
                        v-model="form.expirationDate"
                        type="date"
                        placeholder="选择日期" >
                      </el-date-picker>
                    </el-form-item>
					<el-form-item style="width: 100%;" label="合同附件">
					  <wk-upload-file
					    ref="wkUploadFile2"
					    name="file"
					    multiple
					    accept="*"
						:size = "1"
					    :autoUpload="false"
					    :batchId="attachments"
					  ></wk-upload-file>
					</el-form-item>

                </div>
               </create-sections>
            </div>
            <!-- 合同信息  结束 -->



          <div style="text-align:center;">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="reset">重 置</el-button>
        </div>
      </el-form>

    </div>
  </div>
</template>

<script>

  import EditImage from '@/components/EditImage'
  import XrHeader from '@/components/XrHeader'
  import request from '@/utils/request'
  import SelectCol from "@/views/admin/components/selectCol";
  import { validatePhone,validateIdNo,isNum,guid,validateZipCode } from '@/utils'
import WkUploadFile from '@/components/CreateCom/WkUploadFile'
  import {crmFileSaveAPI} from '@/api/common'
  import CreateSections from '@/components/CreateSections'

  export default {
    name: 'infocollection',
    components: {
      EditImage,
      XrHeader,
      SelectCol,
      CreateSections,
	  WkUploadFile
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
        // 证书表单参数
        cerForm: {},
        // 工种数据
        worktypeList: [],
		workTypeOptions: [],
		// 特殊工种
		specialWorkTypeList:['130','270','170','080','180','030'],
        // 分包商数据信息
        FBSList: [],
        // 班组数据信息
        BZList: [],
        //是否特殊工种 true的时候显示证书信息
        workTypeIfspecial:false,
        //是否签订劳动合同 true的时候显示证书信息
        iflaborContract:false,
        //性别
        sexOptions: [],
        //民族
        nationOptions:[],
        //职业健康
        occupationalHealthOptions:[],
        //是否
        ynOptions: [],
        //文化程度
        educationLevelOptions:[],
        //计价方式
        valuationMethodOptions: [],
        //政治面貌
        politicalStatusOptions:[],
        //开户银行
        bankAccountOptions: [],

        //证书种类
        cerCategoryOptions:[],
        //证书类型
        cerCategoryTypeOptions:[],
        //证书类型_
        cerCategoryTypeOptions_:[],
        //证书等级
        cerLevelOptions:[],
        // 劳动合同
        laborContractOptions:[],
        // 进场状态
        enterStatusOptions:[],
        // 工人类型
        workRoleOptions:[],
        // 管理岗位
        manageTypeOptions:[],
        // 全国建筑工人管理是否字典
        qgYesNoOptions:[],
        // 婚姻状况
        maritalStatusOptions:[],

		// 合同附件
		attachments:'',

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
          workAge: [
            { required: true, message: '工龄不能为空', trigger: 'blur' }
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
          workRole: [
            { required: true, message: '工人类型不能为空', trigger: 'blur' }
          ],
          manageType: [
            { required: true, message: '管理岗位不能为空', trigger: 'blur' }
          ],
          dailyWage:[
            { required: true, message: '每日工资不能为空', trigger: 'blur' }
          ],

          ifTeamLeader:[
            { required: true, message: '是否班组长不能为空', trigger: 'blur' }
          ],
          educationLevel:[
            { required: true, message: '文化程度不能为空', trigger: 'blur' }
          ],
          politicalStatus:[
            { required: true, message: '政治面貌不能为空', trigger: 'blur' }
          ],

          // signingDate:[
          //   { required: true, message: '签订日期不能为空', trigger: 'blur' }
          // ],
          // effectiveDate:[
          //   { required: true, message: '生效日期不能为空', trigger: 'blur' }
          // ],
          // expirationDate:[
          //   { required: true, message: '失效日期不能为空', trigger: 'blur' }
          // ],

          phone: [
            {  required: true, message: '联系电话不能为空',trigger: 'blur' },
            {  validator: validatePhone,trigger: 'blur'}
          ],
          urgentLinkManPhone: [
            {  validator: validatePhone,trigger: 'blur'}
          ],
          bankLinkNumber: [
            {  validator: isNum,trigger: 'blur'}
          ],
          attendanceNumber: [
            {  validator: isNum,trigger: 'blur'}
          ],
          bankCardNumber: [
            {  validator: isNum,trigger: 'blur'}
          ],
          postalCode: [
            {  validator: validateZipCode,trigger: 'blur'}
          ],
          // postalCode:[
          //   {  required: false, validator: isNum,trigger: 'blur' }
          // ],
          // bankCardNumber:[
          //   {  required: false, validator: isNum,trigger: 'blur' }
          // ],
          // attendanceNumber:[
          //   {  required: false, validator: isNum,trigger: 'blur' }
          // ],

        },
        pinyin:'',
        addpin:'',
        showEditImage: false,
        editImage: null,
        editFile: null,
        systemName: '',
        systemImage: '',
        editedImage: null, // 编辑后的图片

        isAble:false,

        HTIf:false,//是否签订合同，显示合同信息
        batchId: "",
      }
    },
    created() {
//    this.batchId = guid();
//    this.attachments = guid();

      

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
      //  文化程度字典项
      this.getDicts('b_educationLevel').then(response => {
        this.educationLevelOptions = response.data
      })
      //  计价方式字典项
      this.getDicts('b_valuation_method').then(response => {
        this.valuationMethodOptions = response.data
      })
      //  政治面貌 字典项
      this.getDicts('b_politicalStatus').then(response => {
        this.politicalStatusOptions = response.data
      })
      //  开户银行 字典项
      this.getDicts('b_bankAccount').then(response => {
        this.bankAccountOptions = response.data
      })

      //  证书种类 字典项
      this.getDicts('b_cerCategory').then(response => {
        this.cerCategoryOptions = response.data
      })
      //  证书类型 字典项
      this.getDicts('b_cerCategoryType').then(response => {
        this.cerCategoryTypeOptions = response.data
      })
      //  证书等级 字典项
      this.getDicts('b_cerLevel').then(response => {
        this.cerLevelOptions = response.data
      })
      //  进场状态
      this.getDicts('b_enterStatus').then(response => {
        this.enterStatusOptions = response.data
      })
      //  工人类型
      this.getDicts('b_work_role').then(response => {
        this.workRoleOptions = response.data
      })
      //  管理岗位
      this.getDicts('b_mange_type').then(response => {
        this.manageTypeOptions = response.data
      })
      //  全国建筑工人管理是否
      this.getDicts('qg_yes_no').then(response => {
        this.qgYesNoOptions = response.data
      })
      //  婚姻状况
      this.getDicts('b_marital_status').then(response => {
        this.maritalStatusOptions = response.data
      })
	  // 工种数据
	  // this.getList()
	  // 工种字典
      this.getDicts('b_labour_work_type').then(response => {
        this.workTypeOptions = response.data
      })

    },
    watch: {

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
      /** 查询所工种数据列表 */
      getList() {
        request({
          url: 'build/labourWorktype/selcetIdsAndWorktype',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(response => {
          this.worktypeList = response.data
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },
      /** 查询所有分包商id和name */
      getListByFBS() {
        request({
          url: 'build/labourSubcontractor/selcetIdsAndName',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(response => {
          this.FBSList = response.data
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },
      /** 查询所有班组名称 */
      getListByBZ() {
        request({
          url: 'build/labourTeam/selcetIdsAndName',
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(response => {
          this.BZList = response.data
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },
      //是否显示证书信息
      putworkTypeIfspecial(data){
        //this.workTypeIfspecial = 'Y' == data.workTypeIfspecial ? true :false
		this.workTypeIfspecial = this.specialWorkTypeList.includes(data.dictValue) // 工种改用字典值
        this.cerForm = {}
        this.resetForm('cerForm')
      },
      //是否显示劳动合同信息
      selectlaborContract(data){
        this.HTIf = '1' == data ? true :false
      },
      //不是班组长，显示班组名称,禁用分包单位；是班组长，禁用班组名称，显示分包单位
      selectIfTeamLeader(data){
          if(data == "Y"){
            this.form.teamName = ""//禁用班组名称,置空
            this.isAble = true
            // this.BZList = []
            this.getListByFBS()
          }else{
            this.form.subcontractor = ""//禁用分包单位,置空
            this.isAble = false
            // this.FBSList = []
            this.getListByBZ()
          }
      },
      selectIfTeamLeader_1(){
          this.$forceUpdate()//强制刷新
      },
      selectIfTeamLeader_2(){
          this.$forceUpdate()//强制刷新
      },
      // 表单重置
      reset() {
        this.workTypeIfspecial = false
        this.systemImage = ''
        this.batchId = ""
        this.editedImage = null
        this.form = {}
        this.cerForm = {}
        this.resetForm('form')
        this.resetForm('cerForm')
      },
      alert(){
        this.$message.error('请连接读卡设备')
      },
      /** 提交按钮 */
      submitForm: function() {

				 this.batchId = guid();
      		this.attachments = this.batchId;
        this.$refs['form'].validate(valid => {

          var param = new FormData()

          if (!this.editedImage) {
            this.$message.error('身份证头像不能为空')
          }

          var data = {type: 'img', batchId: this.batchId, file: this.editedImage.blob};
          crmFileSaveAPI(data).then(response => {

                if (response.code === 0) {
                  // param.append(
                  //   'file',
                  //   this.editedImage.blob,
                  //   this.editedImage.file.name
                  // )
                  // console.log(response.data.url)
                  this.form.imgCardUrl = response.data.url
                  this.form.batchId = this.batchId

				  // 保存数据
				  this.saveInfo()
                }
              });


        })
      },
	  // 保存信息
	  saveInfo() {
		  this.$refs['form'].validate(valid => {
		  	console.log(valid)
		  	console.log(this.form)
			  var param = new FormData()
			  this.form.projectId = null

			  //如果为异步上传需要主动调用提交
			  this.form.attachments = this.attachments;
			  var fileFlag = this.$refs.wkUploadFile2.submitAsync();
			  if (!fileFlag) {
			  this.$message({
			  	type: 'error',
			  	message: '退场方案附件提交异常'
			  })
			  return;
			  }

			  var s1 = JSON.stringify(this.form)
			  param.append(
				'form',
				s1
			  )

			  var s2 = JSON.stringify(this.cerForm)
			  param.append(
				'cerForm',
				s2
			  )

			  if (valid) {
				request({
					url: 'build/labourInfocollection/add',
					method: 'post',
					data: param,
					headers: {
					  'Content-Type': 'application/json;charset=UTF-8'
					}
				  }).then(response => {
					if (response.code === 0) {
					  this.msgSuccess('新增成功')
					  this.reset()
					}
				  })
			  }else{
				this.$message.error('身份证头像上传失败')
			  }
		  })
	  },
      //根据证书种类显示证书类型
      cerOptions:function(value){

        var actions = []
        var datas = this.cerCategoryTypeOptions

        Object.keys(datas).some((key) => {
          var a = datas[key].dictValue.split('-')[0]
          if (a == ('' + value)) {
            actions.push(datas[key])
          }
        })

        this.cerCategoryTypeOptions_ = actions
      },
	  // 身份证号失去焦点时触发
	  idCardBlur(value) {

		  var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
		  if((!reg.test(value)) || value =='') {
			  return;
	      }

		  request({
		    url: 'build/labourInfocollection/queryByIdCard/' + value,
		    method: 'post',
		    headers: {
		      'Content-Type': 'application/json;charset=UTF-8'
		    }
		  }).then(response => {

          //根据是否是班组长显示班组名称或者分包单位
          this.selectIfTeamLeader(response.data.ifTeamLeader)

          this.systemImage = response.data.imgCardUrl
          // console.log(this.systemImage)
          this.batchId = response.data.batchId

          this.form = response.data

          //是否签订劳动合同
          this.HTIf = '1' == response.data.laborContract ? true :false

		  //var datas = this.worktypeList
          var datas = this.workTypeOptions

          Object.keys(datas).some((key) => {
            var a = datas[key].id
            if (a == ('' + response.data.workTypeDict)) {
              this.putworkTypeIfspecial(datas[key])
			  
			  // 工种改用字典值
			  if( this.specialWorkTypeList.includes(response.data.workTypeDict)){
			       this.getSpeZHSHU(response.data.id)
			  }

              /* //如果是特殊工种，根据采集信息主键显示证书信息
              if('Y' == datas[key].workTypeIfspecial){
                   this.getSpeZHSHU(response.data.id)
              } */

              return true
            }
          })

          this.loading = false
        }).catch(() => {
          this.loading = false
        })
	  },
      //获取特殊工种证书信息
      getSpeZHSHU(date){

        request({
          url: 'build/labourSpeworktype/queryByInfoId/'+date,
          method: 'post',
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        }).then(response => {
           this.cerOptions(response.data.cerCategory)

          this.cerForm = response.data

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
    height: 100%;
    overflow-y: auto;
  }

  .main-table-header-button {
    float: right;
    margin-right: 20px;
    margin-top: 10px;
  }

.section + .section {
  margin-top: 0px;
}

.section-title {
  color: #333;
  font-weight: bold;
  font-size: 14px;
  margin-bottom: 20px;
}

.section-content {
  margin-bottom: 20px;
  .name {
    color: #333;
    font-size: 14px;
    margin-bottom: 10px;
  }

  .el-input {
    width: 300px;
  }
  .yangshi {
    width:100%!important;
  }
}
  .yangshi2 /deep/.el-form-item__content{
    width:calc(84% - 90px);
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
	/deep/ .special .el-form-item__label{
		width: 100px!important;
	}
  .upload{
    float: left;
  }
.information{
 display: inline-block;
  width: 200px;
  height: 200px;
  margin-left: 30px
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
.el-date-editor.el-input, .el-date-editor.el-input__inner {
    width: 163px;
}

// .img-accessory .img-box[data-v-4b1eeb44] .el-upload {
//     width: 200px;
//     height: 200px;
//     line-height: 180px;
// }

/deep/ .section-content .el-input{
  width: 160px;
}
/deep/.el-form-item__label{
  width: 90px!important;
}
/deep/ .label_1 .el-form-item__label{
  width: 120px!important;
}
/deep/ .label_2 .el-form-item__label{
  width: 200px!important;
}
/deep/.el-date-editor.el-input, .el-date-editor.el-input__inner{
  width: 160px;
}

</style>
