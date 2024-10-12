<template>
  <div>
  <!-- 修改对话框 -->
  <el-dialog class="updataopen" :title="title" :visible.sync="open2" width="1200px" append-to-body>
    <el-form ref="form"  :model="form" :rules="rules" label-width="170px" class="add-form">
      <create-sections style="overflow:hidden" title="基本信息">
        <el-row>
          <el-col :span="16" class="add-form-w100">
            <el-form-item label="项目名称" prop="projectName" class="add-form-input">
              <el-input v-model="form.projectName" placeholder="请输入项目名称" />
            </el-form-item>
            <el-form-item label="建设单位名称" prop="constructionUnitName" class="add-form-input">
              <el-input v-model="form.constructionUnitName" placeholder="请输入建设单位名称" />
            </el-form-item>
            <el-form-item label="施工许可证编号" prop="constructionPermitCode" class="add-form-input">
              <el-input v-model="form.constructionPermitCode" placeholder="请输入施工许可证编号" />
            </el-form-item>
            <el-form-item label="建设用地规划许可证编号 " prop="landPlanningPermitCode" class="add-form-input">
              <el-input v-model="form.landPlanningPermitCode" placeholder="请输入施工许可证编号" />
            </el-form-item>
            <el-form-item label="建设工程规划许可证编号" prop="projectPlanningLicenseCode" class="add-form-input">
              <el-input v-model="form.projectPlanningLicenseCode" placeholder="请输入施工许可证编号" />
            </el-form-item>
          </el-col>
          <el-col :span="8" style="padding: 10px">
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
              :fixedNumber="[16, 9]"
              :show="showEditImage"
              :image="editImage"
              :file="editFile"
              title="编辑项目图片"
              previewWidth="160px"
              previewHeight="90px"
              preview-radius="0"

              width="550px"
              save-button-title="确定"
              @save="submiteImage"
              @close="showEditImage=false"/>
          </el-col>
        </el-row>
        <el-form-item label="省份" prop="province"  class="add-form-input">
          <el-select
            v-model="form.province"
            @change="areaChangeProvince"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 200px"
          >
            <el-option
              v-for="option in provinceOptions"
              :key="option.id"
              :label="option.name"
              :value="option.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属市"  prop="city" class="add-form-input">
          <el-select
            v-model="form.city"
            @change="areaChangeCity"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 200px"
          >
            <el-option
              v-for="option in cityOptions"
              :key="option.id"
              :label="option.name"
              :value="option.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属区"  prop="district" class="add-form-input">
          <el-select
            v-model="form.district"
            @change="areaChangeDistrict"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 200px"
          >
            <el-option
              v-for="option in districtOptions"
              :key="option.id"
              :label="option.name"
              :value="option.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="项目地址" prop="projectAddress" class="add-form-input">
          <el-input v-model="form.projectAddress" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="项目状态" prop="projectState" class="add-form-input">
          <el-select
            v-model="form.projectState"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in engineeringStatusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="项目分类" prop="projectClassify" class="add-form-input">
          <el-select
            v-model="form.projectClassify"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in projectClassifyOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="工程用途" prop="engineeringPurpose" class="add-form-input">
          <el-select
            v-model="form.engineeringPurpose"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in engineeringPurposeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="建设性质" prop="constructionNature" class="add-form-input">
          <el-select
            v-model="form.constructionNature"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in constructionNatureOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="总造价(万元)" prop="totalCost" class="add-form-input">
          <el-input v-model="form.totalCost" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="总面积(平方米)" prop="totalArea" class="add-form-input">
          <el-input v-model="form.totalArea" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="总长度(米)" prop="totalLength" class="add-form-input">
          <el-input v-model="form.totalLength" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="实际开工日期" prop="actualCommencementDate" class="add-form-input">
          <el-date-picker
            v-model="form.actualCommencementDate"
            type="date"
            placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
        </el-form-item>
        <el-form-item label="实际竣工日期" prop="actualCompletionDate" class="add-form-input">
          <el-date-picker
            v-model="form.actualCompletionDate"
            type="date"
            placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
        </el-form-item>
        <el-form-item label="联系人姓名" prop="contactsName" class="add-form-input">
          <el-input v-model="form.contactsName" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="联系人办公电话" prop="contactsTel" class="add-form-input">
          <el-input v-model="form.contactsTel" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="立项文号" prop="projectCode" class="add-form-input">
          <el-input v-model="form.projectCode" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="立项级别" prop="createProjectLevel" class="add-form-input">
          <el-select
            v-model="form.createProjectLevel"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in createProjectLevelOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="建筑规模" prop="constructionScale" class="add-form-input">
          <el-select
            v-model="form.constructionScale"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in constructionScaleOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否智慧工地" prop="isSmartBuild" class="add-form-input">
          <el-switch v-model="form.isSmartBuild" active-value="1" inactive-value="0"/>
        </el-form-item>
        <el-form-item label="项目简介" prop="projectBrief" class="add-form-input" style="width: 100%">
          <el-input v-model="form.projectBrief" type="textarea"/>
        </el-form-item>
        <el-form-item label="备注" class="add-form-input"  style="width: 100%">
          <el-input v-model="form.remarks" type="textarea"/>
        </el-form-item>
      </create-sections>
      <create-sections style="overflow:hidden"  title="位置信息">
        <el-form-item label="WGS84经度" prop="lng" class="add-form-input">
          <el-input v-model="form.lng" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="WGS84纬度" prop="lat" class="add-form-input">
          <el-input v-model="form.lat" placeholder="请输入" />
        </el-form-item>
      </create-sections>
      <create-sections style="overflow:hidden" prop="corporateAccountBank" title="银行信息">
        <el-form-item label="对公账号开户银行" class="add-form-input">
          <el-select
            v-model="form.corporateAccountBank"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in bankAccountOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="对公账号号码" prop="corporateAccount" class="add-form-input">
          <el-input v-model="form.corporateAccount" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="代发工资银行" prop="payBank" class="add-form-input">
          <el-select
            v-model="form.payBank"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in bankAccountOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="代发工资银行" prop="payBankCode" class="add-form-input">
          <el-input v-model="form.payBankCode" placeholder="请输入" />
        </el-form-item>
      </create-sections>
      <create-sections style="overflow:hidden"  title="中标信息">
        <el-form-item label="公司简称" prop="companyName" class="add-form-input">
          <el-input v-model="form.companyName" placeholder="请输入公司简称" />
        </el-form-item>
        <el-form-item label="项目简称" prop="projectAbbreviation" class="add-form-input">
          <el-input v-model="form.projectAbbreviation" placeholder="请输入项目简称" />
        </el-form-item>
        <el-form-item label="所属国家" prop="country" class="add-form-input">
          <el-select
            v-model="form.country"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in countryOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属区域" prop="region" class="add-form-input">
          <el-select
            v-model="form.region"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in regionOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="工程编号" prop="projectNo" class="add-form-input">
          <el-input v-model="form.projectNo" placeholder="请输入工程编号" />
        </el-form-item>
        <el-form-item label="使用资质" prop="qualification" class="add-form-input">
          <el-select
            v-model="form.qualification"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in qualificationOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="投标面积（m²）" prop="tenderArea" class="add-form-input">
          <el-input v-model="form.tenderArea" placeholder="请输入投标面积" />
        </el-form-item>
        <el-form-item label="土石方量（m²）" prop="earthworkVolume" class="add-form-input">
          <el-input v-model="form.earthworkVolume" placeholder="请输入土石方量" />
        </el-form-item>
        <el-form-item label="项目用途" prop="projectPurpose" class="add-form-input">
          <el-input v-model="form.projectPurpose" placeholder="请输入项目用途" />
        </el-form-item>
        <el-form-item label="项目类型" prop="projectType" class="add-form-input">
          <el-select
            v-model="form.projectType"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in projectTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="结构类型" prop="structureType" class="add-form-input">
          <el-input v-model="form.structureType" placeholder="请输入结构类型" />
        </el-form-item>
        <el-form-item label="中标日期" prop="bidWinningDate" class="add-form-input">
          <el-date-picker
            v-model="form.bidWinningDate"
            type="date"
            placeholder="选择日期" prop="constructionUnitName" value-format="yyyy-MM-dd HH:mm:ss"/>
        </el-form-item>
        <el-form-item label="中标开工日期" prop="bidWinningStartDate" class="add-form-input">
          <el-date-picker
            v-model="form.bidWinningStartDate"
            type="date"
            placeholder="选择日期"  value-format="yyyy-MM-dd HH:mm:ss"/>
        </el-form-item>
        <el-form-item label="中标竣工日期" prop="bidWinningCompletionDate" class="add-form-input">
          <el-date-picker
            v-model="form.bidWinningCompletionDate"
            type="date"
            placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
        </el-form-item>
        <el-form-item label="中标金额（万元）"  prop="bidAmount" class="add-form-input">
          <el-input v-model="form.bidAmount" placeholder="请输入中标金额" />
        </el-form-item>
        <el-form-item label="承包模式" prop="contractMode" class="add-form-input">
          <el-select
            v-model="form.contractMode"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in contractModeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="高达精特" prop="tallExquisite" class="add-form-input">
          <el-select
            v-model="form.tallExquisite"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in tallExquisiteOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="投标利润率（%）" prop="biddingProfitMargin" class="add-form-input">
          <el-input v-model="form.biddingProfitMargin" placeholder="请输入投标利润率" />
        </el-form-item>
        <el-form-item label="经营模式" prop="managementModel" class="add-form-input">
          <el-select
            v-model="form.managementModel"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in managementModelOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分包模式" prop="subcontractingMode" class="add-form-input">
          <el-select
            v-model="form.subcontractingMode"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in subcontractingModeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
      </create-sections>
      <create-sections style="overflow:hidden"  title="工程信息">
        <el-form-item label="工程状态" prop="engineeringStatus" class="add-form-input">
          <el-select
            v-model="form.engineeringStatus"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in engineeringStatusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="商务状态" prop="businessState" class="add-form-input">
          <el-select
            v-model="form.businessState"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in engineeringStatusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="项目级别" prop="projectLevel" class="add-form-input">
          <el-select
            v-model="form.projectLevel"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in projectLevelOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="主要施工范围" prop="mainConstructionScope" class="add-form-input">
          <el-input v-model="form.mainConstructionScope" placeholder="请输入主要施工范围" />
        </el-form-item>
        <el-form-item label="建筑檐高（m）" prop="buildingEavesHeight" class="add-form-input">
          <el-input v-model="form.buildingEavesHeight" placeholder="请输入建筑檐高" />
        </el-form-item>
        <el-form-item label="合同开工日期" prop="contractCommencementDate" class="add-form-input">
          <el-date-picker
            v-model="form.contractCommencementDate"
            type="date"
            placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
        </el-form-item>
        <el-form-item label="合同竣工日期" prop="contractCompletionDate" class="add-form-input">
          <el-date-picker
            v-model="form.contractCompletionDate"
            type="date"
            placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
        </el-form-item>
        <el-form-item label="开工令日期" prop="commencementOrderDate" class="add-form-input">
          <el-date-picker
            v-model="form.commencementOrderDate"
            type="date"
            placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
        </el-form-item>
        <el-form-item label="计划竣工日期" prop="plannedCompletionDate" class="add-form-input">
          <el-date-picker
            v-model="form.plannedCompletionDate"
            type="date"
            placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
        </el-form-item>
        <el-form-item label="地上面积（㎡）" prop="abovegroundArea" class="add-form-input">
          <el-input v-model="form.abovegroundArea" placeholder="请输入地上面积" />
        </el-form-item>
        <el-form-item label="地下面积（㎡）" prop="undergroundArea" class="add-form-input">
          <el-input v-model="form.undergroundArea" placeholder="请输入地下面积" />
        </el-form-item>
        <el-form-item label="总层数" prop="totalFloors" class="add-form-input">
          <el-input v-model="form.totalFloors" type="number" placeholder="请输入总层数" />
        </el-form-item>
        <el-form-item label="单位工程数量" prop="unitQuantity" class="add-form-input">
          <el-input v-model="form.unitQuantity" placeholder="请输入单位工程数量" />
        </el-form-item>
        <el-form-item label="工程类别" prop="engineeringType" class="add-form-input">
          <el-select
            v-model="form.engineeringType"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in engineeringTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="隐患提醒天数" prop="hiddenDangerDaysReminder" class="add-form-input">
          <el-input v-model="form.hiddenDangerDaysReminder" type="number" placeholder="请输入隐患提醒天数" />
        </el-form-item>
        <el-form-item label="隐患禁录天数" prop="hiddenDangerBanDays" class="add-form-input">
          <el-input v-model="form.hiddenDangerBanDays" type="number" placeholder="请输入隐患禁录天数" />
        </el-form-item>
      </create-sections>
      <create-sections style="overflow:hidden"  title="质量安全精品创建">
        <el-form-item label="质量目标" class="add-form-input">
          <el-select
            v-model="form.qualityTarget"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in qualityTargetOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="安全目标" class="add-form-input">
          <el-select
            v-model="form.safetyTarget"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in safetyTargetOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="创建市优质结构" class="add-form-input">
          <el-select
            v-model="form.qualityStructure"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in yesNoOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="观感质量等级目标" class="add-form-input">
          <el-select
            v-model="form.qualityLevelTarget"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 160px"
          >
            <el-option
              v-for="dict in qualityLevelTargetOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="优良面积（㎡）" class="add-form-input">
          <el-input v-model="form.excellentArea" placeholder="请输入隐患提醒天数" />
        </el-form-item>
      </create-sections>
      <!--<el-divider content-position="left" class="add-form-divider add-form-input">项目管理体系</el-divider>-->
      <!--<el-form-item label="项目经理" class="add-form-input">-->
      <!--<xh-user-cell :radio="false" :value="form.projectManager" class="handle-item-content add-form-input" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="项目执行经理" class="add-form-input">-->
      <!--<xh-user-cell :radio="false" :value="form.projectExecutiveManager" class="handle-item-content add-form-input" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="项目副经理" class="add-form-input">-->
      <!--<xh-user-cell :radio="false" :value="form.deputyProjectManager" class="handle-item-content add-form-input" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="商务经理" class="add-form-input">-->
      <!--<xh-user-cell :radio="false" :value="form.businessManager" class="handle-item-content add-form-input" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="项目总工" class="add-form-input">-->
      <!--<xh-user-cell :radio="false" :value="form.projectDepartmentChief" class="handle-item-content add-form-input" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="专项质量员" class="add-form-input">-->
      <!--<xh-user-cell :radio="false" :value="form.specialQualityOfficer" class="handle-item-content add-form-input" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="专职安全员" class="add-form-input">-->
      <!--<xh-user-cell :radio="false" :value="form.fullTimeSafetyOfficer" class="handle-item-content add-form-input" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="信息化" class="add-form-input">-->
      <!--<xh-user-cell :radio="false" :value="form.informationize" class="handle-item-content add-form-input" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="材料员" class="add-form-input">-->
      <!--<xh-user-cell :radio="false" :value="form.materialman" class="handle-item-content add-form-input" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="预算员" class="add-form-input">-->
      <!--<xh-user-cell :radio="false" :value="form.budgeter" class="handle-item-content add-form-input" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="施工员" class="add-form-input">-->
      <!--<xh-user-cell :radio="false" :value="form.constructionWorkers" class="handle-item-content add-form-input" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="成本会计" class="add-form-input">-->
      <!--<xh-user-cell :radio="false" :value="form.costAccounting" class="handle-item-content add-form-input" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="劳务管理员" class="add-form-input">-->
      <!--<xh-user-cell :radio="false" :value="form.laborAdministrator" class="handle-item-content add-form-input" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="劳务分管领导" class="add-form-input">-->
      <!--<xh-user-cell :radio="false" :value="form.laborLeader" class="handle-item-content add-form-input" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="劳务分管领导电话" class="add-form-input">-->
      <!--<el-input v-model="form.laborLeaderTel" placeholder="请输入劳务分管领导电话" />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="项目经理电话" class="add-form-input">-->
      <!--<el-input v-model="form.projectManagerTel" placeholder="请输入项目经理电话" />-->
      <!--</el-form-item>-->
      <create-sections title="单位楼座信息">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="addLz" style="position: absolute;right: 10px;top: 0px;padding: 5px 10px" >添加</el-button>
        <el-table
          :data="lzData"
          empty-text=""
          style="width: 100%">
          <el-table-column label="楼座" align="center" prop="building"/>
          <el-table-column label="单元数" align="center" prop="unitsNumber"/>
          <el-table-column label="地上层数" align="center" prop="abovegroundNumber"/>
          <el-table-column label="地下层数" align="center" prop="undergroundNumber"/>
          <el-table-column label="是否公共区域" align="center" prop="isPublic" :formatter="singleBuildingIsFormat" />
          <el-table-column label="类型" align="center" prop="type" :formatter="singleBuildingTypeFormat"/>
          <el-table-column label="类型" align="center" prop="sort"/>
          <el-table-column label="备注" align="center" prop="remarks"/>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </create-sections>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
  <!-- 添加楼座对话框 -->
  <el-dialog  title="新增楼座" :visible.sync="lzOpen" width="900px" append-to-body>
    <el-form ref="lzform" :model="lzform" :rules="rules" label-width="140px" label-position="right" >
      <el-row>
        <el-col :span="12">
          <el-form-item label="楼座" prop="building" class="add-form-input">
            <el-input v-model="lzform.building" placeholder="请输入楼座" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单元数" prop="unitsNumber" class="add-form-input">
            <el-input v-model="lzform.unitsNumber" type="number" placeholder="请输入单元数" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="地上层数" prop="abovegroundNumber" class="add-form-input">
            <el-input v-model="lzform.abovegroundNumber"  type="number"  placeholder="请输入地上层数" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="地下层数" prop="undergroundNumber" class="add-form-input">
            <el-input v-model="lzform.undergroundNumber" type="number"   placeholder="请输入地下层数" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否公共区域" prop="isPublic" class="add-form-input">
            <el-select
              v-model="lzform.isPublic"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 160px"
            >
              <el-option
                v-for="dict in yesNoOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="类型" prop="type" class="add-form-input">
            <el-select
              v-model="lzform.type"
              placeholder="请选择"
              clearable
              size="small"
              style="width: 160px"
            >
              <el-option
                v-for="dict in SingleBuildingType"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="排序" prop="sort" class="add-form-input">
            <el-input v-model="lzform.sort" type="number" placeholder="请输入排序" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注" class="add-form-input">
            <el-input v-model="lzform.remarks" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitLzForm">确 定</el-button>
      <el-button @click="cancellz">取 消</el-button>
    </div>
  </el-dialog>
  </div>
</template>

<script>
import {
  userImportTemplateURL,
  attendanceExcelImportAPI,
  userErrorExcelDownAPI
} from '@/api/admin/employeeDep'

import { downloadExcelWithResData, verifyFileTypeWithFileName } from '@/utils'
import { downloadFileAPI } from '@/api/common'
import { downloadFileWithBuffer } from '@/utils'

export default {
  // 批量导入员工股
  name: 'DetailsData',
  components: {},
  props: {
    show: {
      type: Boolean,
      default: false
    },
    // CRM类型
    crmType: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      showDialog: false,
      file: { name: '' },
      stepsActive: 1,
      stepList: [
        {
          icon: 'wk wk-upload',
          title: '上传文件',
          status: 'wait'
        },
        {
          icon: 'wk wk-data-import',
          title: '导入数据',
          status: 'wait'
        },
        {
          icon: 'wk wk-success',
          title: '导入完成',
          status: 'wait'
        }
      ],
      resultData: null
    }
  },
  computed: {
    sureTitle() {
      return {
        1: '立即导入',
        2: '',
        3: '确定'
      }[this.stepsActive]
    },

    showCancel() {
      return this.stepsActive != 2
    }
  },
  watch: {
    show: function(val) {
      this.showDialog = val
      this.resetData()
    }
  },
  mounted() {},
  methods: {

  }
}
</script>

<style scoped lang="scss">
.el-steps {
  margin-bottom: 15px;

  /deep/ .el-step__title {
    font-size: 14px;
  }

  /deep/ .el-step.is-simple .el-step__arrow::before,
  /deep/ .el-step.is-simple .el-step__arrow::after {
    height: 10px;
    width: 2px;
  }

  /deep/ .el-step.is-simple .el-step__arrow::after {
    transform: rotate(45deg) translateY(3px);
  }
  /deep/ .el-step.is-simple .el-step__arrow::before {
    transform: rotate(-45deg) translateY(-2px);
  }
}

.sections {
  font-size: 14px;
  min-height: 215px;
  .download {
    cursor: pointer;
    color: #2362fb;
    margin-bottom: 15px;
  }

  /deep/ .el-loading-spinner {
    top: 45%;
    .el-icon-loading {
      font-size: 40px;
      color: #999;
    }

    .el-loading-text {
      color: #333;
    }
  }
}

.content {
  padding: 10px 0;
}

.content-tips {
  font-size: 12px;
  color: #999;
  line-height: 15px;
}

#importInputFile {
  display: none;
}

.file-select {
  .el-input {
    width: 400px;
  }
  button {
    margin-left: 20px;
  }
}

.is-hidden {
  visibility: hidden;
}

// 结果信息
.result-info {
  text-align: center;
  padding-top: 30px;

  &__icon {
    font-size: 40px;
    color: $xr-color-primary;
  }

  &__des {
    margin-top: 15px;
    color: #333;
    font-size: 14px;
  }

  &__detail {
    margin-top: 15px;
    font-size: 12px;
    color: #666;
    &--all {
      color: #333;
      font-weight: 600;
    }

    &--suc {
      color: $xr-color-primary;
      font-weight: 600;
    }

    &--err {
      color: #f94e4e;
      font-weight: 600;
    }
  }

  &__btn--err {
    margin-top: 10px;
  }
}
</style>
