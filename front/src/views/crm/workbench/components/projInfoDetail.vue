<template>
    <el-dialog 
    title="项目信息"   
    width="1200px" 
    :visible="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    :close-on-click-modal="false"
    class="create-dialog"
    @close="close">
       
        <el-form ref="form" :model="form" label-width="170px" class="add-form">
            <create-sections style="overflow:hidden" title="基本信息">
                <el-row>
                    <el-col :span="16" class="add-form-w100">
                        <el-form-item label="项目名称" prop="projectName" class="add-form-input">
                            <el-input v-model="form.projectName" placeholder="请输入项目名称" disabled />
                        </el-form-item>
                        <el-form-item label="建设单位名称" prop="constructionUnitName" class="add-form-input">
                            <el-input v-model="form.constructionUnitName" placeholder="请输入建设单位名称" disabled />
                        </el-form-item>
                        <el-form-item label="施工许可证编号" prop="constructionPermitCode" class="add-form-input">
                            <el-input v-model="form.constructionPermitCode" placeholder="请输入施工许可证编号" disabled />
                        </el-form-item>
                        <el-form-item label="建设用地规划许可证编号 " prop="landPlanningPermitCode" class="add-form-input">
                            <el-input v-model="form.landPlanningPermitCode" placeholder="请输入施工许可证编号" disabled />
                        </el-form-item>
                        <el-form-item label="建设工程规划许可证编号" prop="projectPlanningLicenseCode" class="add-form-input">
                            <el-input v-model="form.projectPlanningLicenseCode" placeholder="请输入施工许可证编号" disabled />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" style="padding: 10px">
                        <el-upload v-if="!systemImage" :show-file-list="false" disabled drag class="upload"
                            action="http" accept="image/png, image/jpeg, image/gif, image/jpg">
                            <i class="el-icon-plus uploader-icon" />
                        </el-upload>
                        <div v-else class="upload-show">
                            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="systemImage">
                        </div>


                    </el-col>
                </el-row>
                <el-form-item label="省份" prop="province" class="add-form-input">
                    <el-select v-model="form.province" disabled placeholder="请选择" clearable size="small"
                        style="width: 200px" >
                        <el-option v-for="option in provinceOptions" :key="option.id" :label="option.name"
                            :value="option.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="所属市" prop="city" class="add-form-input">
                    <el-select v-model="form.city" disabled placeholder="请选择" clearable size="small"
                        style="width: 200px" >
                        <el-option v-for="option in cityOptions" :key="option.id" :label="option.name"
                            :value="option.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="所属区" prop="district" class="add-form-input">
                    <el-select v-model="form.district" disabled placeholder="请选择" clearable size="small"
                        style="width: 200px" >
                        <el-option v-for="option in districtOptions" :key="option.id" :label="option.name"
                            :value="option.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="项目地址" prop="projectAddress" class="add-form-input">
                    <el-input v-model="form.projectAddress" placeholder="请输入" disabled />
                </el-form-item>
                <el-form-item label="项目状态" prop="projectState" class="add-form-input">
                    <el-select v-model="form.projectState" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in engineeringStatusOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="项目分类" prop="projectClassify" class="add-form-input">
                    <el-select v-model="form.projectClassify" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in projectClassifyOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="工程用途" prop="engineeringPurpose" class="add-form-input">
                    <el-select v-model="form.engineeringPurpose" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in engineeringPurposeOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="建设性质" prop="constructionNature" class="add-form-input">
                    <el-select v-model="form.constructionNature" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in constructionNatureOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="总造价(万元)" prop="totalCost" class="add-form-input">
                    <el-input v-model="form.totalCost" placeholder="请输入" disabled />
                </el-form-item>
                <el-form-item label="总面积(平方米)" prop="totalArea" class="add-form-input">
                    <el-input v-model="form.totalArea" placeholder="请输入" disabled />
                </el-form-item>
                <el-form-item label="总长度(米)" prop="totalLength" class="add-form-input">
                    <el-input v-model="form.totalLength" placeholder="请输入" disabled />
                </el-form-item>
                <el-form-item label="实际开工日期" prop="actualCommencementDate" class="add-form-input">
                    <el-date-picker v-model="form.actualCommencementDate" disabled type="date" placeholder="选择日期"
                        value-format="yyyy-MM-dd HH:mm:ss" />
                </el-form-item>
                <el-form-item label="实际竣工日期" prop="actualCompletionDate" class="add-form-input">
                    <el-date-picker v-model="form.actualCompletionDate" disabled type="date" placeholder="选择日期"
                        value-format="yyyy-MM-dd HH:mm:ss" />
                </el-form-item>
                <el-form-item label="联系人姓名" prop="contactsName" class="add-form-input">
                    <el-input v-model="form.contactsName" placeholder="请输入" disabled />
                </el-form-item>
                <el-form-item label="联系人办公电话" prop="contactsTel" class="add-form-input">
                    <el-input v-model="form.contactsTel" placeholder="请输入" disabled />
                </el-form-item>
                <el-form-item label="立项文号" prop="projectCode" class="add-form-input">
                    <el-input v-model="form.projectCode" placeholder="请输入" disabled />
                </el-form-item>
                <el-form-item label="立项级别" prop="createProjectLevel" class="add-form-input">
                    <el-select v-model="form.createProjectLevel" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in createProjectLevelOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="建筑规模" prop="constructionScale" class="add-form-input">
                    <el-select v-model="form.constructionScale" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in constructionScaleOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="是否智慧工地" prop="isSmartBuild" class="add-form-input">
                    <el-switch v-model="form.isSmartBuild" active-value="1" inactive-value="0" disabled />
                </el-form-item>
                <el-form-item label="项目简介" prop="projectBrief" class="add-form-input" style="width: 100%">
                    <el-input v-model="form.projectBrief" type="textarea" disabled />
                </el-form-item>
                <el-form-item label="备注" class="add-form-input" style="width: 100%">
                    <el-input v-model="form.remarks" type="textarea" disabled />
                </el-form-item>
            </create-sections>
            <create-sections style="overflow:hidden" title="位置信息">
                <el-form-item label="WGS84经度" prop="lng" class="add-form-input">
                    <el-input v-model="form.lng" placeholder="请输入" disabled />
                </el-form-item>
                <el-form-item label="WGS84纬度" prop="lat" class="add-form-input">
                    <el-input v-model="form.lat" placeholder="请输入" disabled />
                </el-form-item>
            </create-sections>
            <create-sections style="overflow:hidden" prop="corporateAccountBank" title="银行信息">
                <el-form-item label="对公账号开户银行" class="add-form-input">
                    <el-select v-model="form.corporateAccountBank" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in bankAccountOptions" :key="dict.dictValue" :label="dict.dictLabel"
                            :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="对公账号号码" prop="corporateAccount" class="add-form-input">
                    <el-input v-model="form.corporateAccount" placeholder="请输入" disabled />
                </el-form-item>
                <el-form-item label="代发工资银行" prop="payBank" class="add-form-input">
                    <el-select v-model="form.payBank" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in bankAccountOptions" :key="dict.dictValue" :label="dict.dictLabel"
                            :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="代发工资银行" prop="payBankCode" class="add-form-input">
                    <el-input v-model="form.payBankCode" placeholder="请输入" disabled />
                </el-form-item>
            </create-sections>
            <create-sections style="overflow:hidden" title="中标信息">
                <el-form-item label="公司简称" prop="companyName" class="add-form-input">
                    <el-input v-model="form.companyName" placeholder="请输入公司简称" disabled />
                </el-form-item>
                <el-form-item label="项目简称" prop="projectAbbreviation" class="add-form-input">
                    <el-input v-model="form.projectAbbreviation" placeholder="请输入项目简称" disabled />
                </el-form-item>
                <el-form-item label="所属国家" prop="country" class="add-form-input">
                    <el-select v-model="form.country" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in countryOptions" :key="dict.dictValue" :label="dict.dictLabel"
                            :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="所属区域" prop="region" class="add-form-input">
                    <el-select v-model="form.region" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in regionOptions" :key="dict.dictValue" :label="dict.dictLabel"
                            :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="工程编号" prop="projectNo" class="add-form-input">
                    <el-input v-model="form.projectNo" placeholder="请输入工程编号" disabled />
                </el-form-item>
                <el-form-item label="使用资质" prop="qualification" class="add-form-input">
                    <el-select v-model="form.qualification" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in qualificationOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="投标面积（m²）" prop="tenderArea" class="add-form-input">
                    <el-input v-model="form.tenderArea" placeholder="请输入投标面积" disabled />
                </el-form-item>
                <el-form-item label="土石方量（m²）" prop="earthworkVolume" class="add-form-input">
                    <el-input v-model="form.earthworkVolume" placeholder="请输入土石方量" disabled />
                </el-form-item>
                <el-form-item label="项目用途" prop="projectPurpose" class="add-form-input">
                    <el-input v-model="form.projectPurpose" placeholder="请输入项目用途" disabled />
                </el-form-item>
                <el-form-item label="项目类型" prop="projectType" class="add-form-input">
                    <el-select v-model="form.projectType" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in projectTypeOptions" :key="dict.dictValue" :label="dict.dictLabel"
                            :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="结构类型" prop="structureType" class="add-form-input">
                    <el-input v-model="form.structureType" placeholder="请输入结构类型" disabled />
                </el-form-item>
                <el-form-item label="中标日期" prop="bidWinningDate" class="add-form-input">
                    <el-date-picker v-model="form.bidWinningDate" disabled type="date" placeholder="选择日期"
                        prop="constructionUnitName" value-format="yyyy-MM-dd HH:mm:ss" />
                </el-form-item>
                <el-form-item label="中标开工日期" prop="bidWinningStartDate" class="add-form-input">
                    <el-date-picker v-model="form.bidWinningStartDate" disabled type="date" placeholder="选择日期"
                        value-format="yyyy-MM-dd HH:mm:ss" />
                </el-form-item>
                <el-form-item label="中标竣工日期" prop="bidWinningCompletionDate" class="add-form-input">
                    <el-date-picker v-model="form.bidWinningCompletionDate" disabled type="date" placeholder="选择日期"
                        value-format="yyyy-MM-dd HH:mm:ss" />
                </el-form-item>
                <el-form-item label="中标金额（万元）" prop="bidAmount" class="add-form-input">
                    <el-input v-model="form.bidAmount" placeholder="请输入中标金额" disabled />
                </el-form-item>
                <el-form-item label="承包模式" prop="contractMode" class="add-form-input">
                    <el-select v-model="form.contractMode" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in contractModeOptions" :key="dict.dictValue" :label="dict.dictLabel"
                            :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="高达精特" prop="tallExquisite" class="add-form-input">
                    <el-select v-model="form.tallExquisite" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in tallExquisiteOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="投标利润率（%）" prop="biddingProfitMargin" class="add-form-input">
                    <el-input v-model="form.biddingProfitMargin" placeholder="请输入投标利润率" disabled />
                </el-form-item>
                <el-form-item label="经营模式" prop="managementModel" class="add-form-input">
                    <el-select v-model="form.managementModel" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in managementModelOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="分包模式" prop="subcontractingMode" class="add-form-input">
                    <el-select v-model="form.subcontractingMode" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in subcontractingModeOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
            </create-sections>
            <create-sections style="overflow:hidden" title="工程信息">
                <el-form-item label="工程状态" prop="engineeringStatus" class="add-form-input">
                    <el-select v-model="form.engineeringStatus" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in engineeringStatusOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="商务状态" prop="businessState" class="add-form-input">
                    <el-select v-model="form.businessState" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in engineeringStatusOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="项目级别" prop="projectLevel" class="add-form-input">
                    <el-select v-model="form.projectLevel" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in projectLevelOptions" :key="dict.dictValue" :label="dict.dictLabel"
                            :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="主要施工范围" prop="mainConstructionScope" class="add-form-input">
                    <el-input v-model="form.mainConstructionScope" placeholder="请输入主要施工范围" disabled />
                </el-form-item>
                <el-form-item label="建筑檐高（m）" prop="buildingEavesHeight" class="add-form-input">
                    <el-input v-model="form.buildingEavesHeight" placeholder="请输入建筑檐高" disabled />
                </el-form-item>
                <el-form-item label="合同开工日期" prop="contractCommencementDate" class="add-form-input">
                    <el-date-picker v-model="form.contractCommencementDate" disabled type="date" placeholder="选择日期"
                        value-format="yyyy-MM-dd HH:mm:ss" />
                </el-form-item>
                <el-form-item label="合同竣工日期" prop="contractCompletionDate" class="add-form-input">
                    <el-date-picker v-model="form.contractCompletionDate" disabled type="date" placeholder="选择日期"
                        value-format="yyyy-MM-dd HH:mm:ss" />
                </el-form-item>
                <el-form-item label="开工令日期" prop="commencementOrderDate" class="add-form-input">
                    <el-date-picker v-model="form.commencementOrderDate" disabled type="date" placeholder="选择日期"
                        value-format="yyyy-MM-dd HH:mm:ss" />
                </el-form-item>
                <el-form-item label="计划竣工日期" prop="plannedCompletionDate" class="add-form-input">
                    <el-date-picker v-model="form.plannedCompletionDate" disabled type="date" placeholder="选择日期"
                        value-format="yyyy-MM-dd HH:mm:ss" />
                </el-form-item>
                <el-form-item label="地上面积（㎡）" prop="abovegroundArea" class="add-form-input">
                    <el-input v-model="form.abovegroundArea" placeholder="请输入地上面积" disabled />
                </el-form-item>
                <el-form-item label="地下面积（㎡）" prop="undergroundArea" class="add-form-input">
                    <el-input v-model="form.undergroundArea" placeholder="请输入地下面积" disabled />
                </el-form-item>
                <el-form-item label="总层数" prop="totalFloors" class="add-form-input">
                    <el-input v-model="form.totalFloors" type="number" placeholder="请输入总层数" disabled />
                </el-form-item>
                <el-form-item label="单位工程数量" prop="unitQuantity" class="add-form-input">
                    <el-input v-model="form.unitQuantity" placeholder="请输入单位工程数量" disabled />
                </el-form-item>
                <el-form-item label="工程类别" prop="engineeringType" class="add-form-input">
                    <el-select v-model="form.engineeringType" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in engineeringTypeOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="隐患提醒天数" prop="hiddenDangerDaysReminder" class="add-form-input">
                    <el-input v-model="form.hiddenDangerDaysReminder" type="number" placeholder="请输入隐患提醒天数"
                        disabled />
                </el-form-item>
                <el-form-item label="隐患禁录天数" prop="hiddenDangerBanDays" class="add-form-input">
                    <el-input v-model="form.hiddenDangerBanDays" type="number" placeholder="请输入隐患禁录天数" disabled />
                </el-form-item>
            </create-sections>
            <create-sections style="overflow:hidden" title="质量安全精品创建">
                <el-form-item label="质量目标" class="add-form-input">
                    <el-select v-model="form.qualityTarget" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in qualityTargetOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="安全目标" class="add-form-input">
                    <el-select v-model="form.safetyTarget" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in safetyTargetOptions" :key="dict.dictValue" :label="dict.dictLabel"
                            :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="创建市优质结构" class="add-form-input">
                    <el-select v-model="form.qualityStructure" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in yesNoOptions" :key="dict.dictValue" :label="dict.dictLabel"
                            :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="观感质量等级目标" class="add-form-input">
                    <el-select v-model="form.qualityLevelTarget" disabled placeholder="请选择" clearable size="small"
                        style="width: 160px">
                        <el-option v-for="dict in qualityLevelTargetOptions" :key="dict.dictValue"
                            :label="dict.dictLabel" :value="dict.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="优良面积（㎡）" class="add-form-input">
                    <el-input v-model="form.excellentArea" placeholder="请输入隐患提醒天数" disabled />
                </el-form-item>
            </create-sections>
            
            <create-sections title="单位楼座信息">
                <el-table :data="lzData" empty-text="" style="width: 100%">
                    <el-table-column label="楼座" align="center" prop="building" />
                    <el-table-column label="单元数" align="center" prop="unitsNumber" />
                    <el-table-column label="地上层数" align="center" prop="abovegroundNumber" />
                    <el-table-column label="地下层数" align="center" prop="undergroundNumber" />
                    <el-table-column :formatter="singleBuildingIsFormat" label="是否公共区域" align="center"
                        prop="isPublic" />
                    <el-table-column :formatter="singleBuildingTypeFormat" label="类型" align="center" prop="type" />
                    <el-table-column label="类型" align="center" prop="sort" />
                    <el-table-column label="备注" align="center" prop="remarks" />
                </el-table>
            </create-sections>
        </el-form>
        
        <span
            slot="footer"
            class="dialog-footer">
            <el-button @click.native="close">取 消</el-button>
            </span>
    </el-dialog>
</template>

<script>

import CreateSections from '@/components/CreateSections'

import { selectAreaList } from '@/api/common'
import { getProjectInfoById, selectByProjectId } from '@/api/build/projectInfo'
    
export default {
  name: 'ProjectDetailsData',
  components: {
      CreateSections,

  },
  props: ['userInfo'],
  data() {
    return {
        loading: false,
      
        form:{},
        projectId: null,
        
        lzData:[],//单位楼座信息

        provinceOptions:[],
        cityOptions:[],
        districtOptions:[],

        systemImage: '',
        title:'',

        qualityTargetOptions: [],
        engineeringStatusOptions: [],
        projectClassifyOptions: [],
        engineeringPurposeOptions: [],
        constructionNatureOptions: [],
        createProjectLevelOptions: [],
        constructionScaleOptions: [],
        bankAccountOptions: [],
        countryOptions: [],
        regionOptions: [],
        qualificationOptions: [],
        contractModeOptions: [],
        tallExquisiteOptions: [],
        managementModelOptions: [],
        subcontractingModeOptions: [],
        projectLevelOptions: [],
        engineeringTypeOptions: [],
        qualityTargetOptions: [],
        safetyTargetOptions: [],
        yesNoOptions: [],
        qualityLevelTargetOptions: [],
        SingleBuildingType: [],
        projectTypeOptions: [],
    }
  },
  
  created(){

    this.getDicts('b_engineering_status').then(response => {
				this.engineeringStatusOptions = response.data
    })
    this.getDicts('b_project_classify').then(response => {
        this.projectClassifyOptions = response.data
    })
    this.getDicts('b_engineering_purpose').then(response => {
        this.engineeringPurposeOptions = response.data
    })
    this.getDicts('b_construction_nature').then(response => {
        this.constructionNatureOptions = response.data
    })
    this.getDicts('b_create_project_level').then(response => {
        this.createProjectLevelOptions = response.data
    })
    this.getDicts('b_construction_scale').then(response => {
        this.constructionScaleOptions = response.data
    })
    this.getDicts('b_bankAccount').then(response => {
        this.bankAccountOptions = response.data
    })
    this.getDicts('b_country').then(response => {
        this.countryOptions = response.data
    })
    this.getDicts('b_region').then(response => {
        this.regionOptions = response.data
    })
    this.getDicts('b_qualification').then(response => {
        this.qualificationOptions = response.data
    })
    this.getDicts('b_contract_mode').then(response => {
        this.contractModeOptions = response.data
    })
    this.getDicts('b_tall_exquisite').then(response => {
        this.tallExquisiteOptions = response.data
    })
    this.getDicts('b_management_model').then(response => {
        this.managementModelOptions = response.data
    })
    this.getDicts('b_subcontracting_mode').then(response => {
        this.subcontractingModeOptions = response.data
    })
    this.getDicts('b_project_level').then(response => {
        this.projectLevelOptions = response.data
    })
    this.getDicts('b_engineering_type').then(response => {
        this.engineeringTypeOptions = response.data
    })
    this.getDicts('b_quality_target').then(response => {
        this.qualityTargetOptions = response.data
    })
    this.getDicts('b_safety_target').then(response => {
        this.safetyTargetOptions = response.data
    })
    this.getDicts('sys_yes_no').then(response => {
        this.yesNoOptions = response.data
    })
    this.getDicts('b_quality_level_target').then(response => {
        this.qualityLevelTargetOptions = response.data
    })
    this.getDicts('b_project_type').then(response => {
        this.projectTypeOptions = response.data
    })
    this.getDicts('b_single_building_type').then(response => {
        this.SingleBuildingType = response.data
    })
  },

  watch:{
    // userInfo: {
    //     handler(newName, oldName) {
    //         this.userInfo = newName
 
    //         // this.projectId = newName.projectId
    //         this.detailsDataClick()
    //     },
    
    // },
    userInfo: function(){
 
        this.detailsDataClick()
    }

  },
  mounted() {
       this.detailsDataClick()
  },
  methods: {

      close() {
          this.$emit('close')
        },

      detailsDataClick() {
  
        this.form = {}
        // 清空市、区区划数据
        this.cityOptions = []
        this.districtOptions = []
        // 获取省级区划
        this.getProvinceList()
        const id = this.userInfo.projectId
        this.projectId = this.userInfo.projectId
        this.getlzData()
        getProjectInfoById(id).then(response => {
           
            this.form = response.data
            this.systemImage = response.data.projectImg
            this.form.id = id
            this.form.isSmartBuild = response.data.isSmartBuild + ''

            this.title = '项目信息'
            this.getCityList(response.data.province)
            this.getDistrictList(response.data.city)
            this.form.province = parseInt(response.data.province)
            this.form.city = parseInt(response.data.city)
            this.form.district = parseInt(response.data.district)
        })
    },

    // 获取省级区划数据
    getProvinceList() {
        const param = {
            level: 1
        }
        selectAreaList(param).then(res => {
            this.provinceOptions = res.data
        })
    },

    // 获取市级区划数据																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																   
    getCityList(id) {
        const param = {
            parentId: id
        }
        selectAreaList(param).then(res => {
            this.cityOptions = res.data
            
        })
    },
    // 获取县级区划数据
    getDistrictList(id) {
        const param = {
            parentId: id
        }
        selectAreaList(param).then(res => {
            this.districtOptions = res.data
         
        })
    },       
    //单位楼座信息
    getlzData() {
        selectByProjectId(this.projectId).then(response => {
            this.lzData = response.data
            this.loading = false
        }).catch(() => {
            this.loading = false
        })
    },

    singleBuildingTypeFormat(row, column) {
        return this.selectDictLabel(this.SingleBuildingType, row.type)
    },
    singleBuildingIsFormat(row, column) {
        return this.selectDictLabel(this.yesNoOptions, row.isPublic)
    },


  }
}
</script>

<style scoped lang="scss">

/*详情页面样式*/
	.main-table-header-button {
		float: right;
		margin-right: 20px;
		margin-top: 10px;
	}

	.add-form-input {
		float: left;
		margin-bottom: 14px;

	}

	add-form-divider {
		float: left;
		margin-bottom: 10px;
	}

	.add-form::after {
		content: "";
		display: block;
		clear: both;
	}

	add-form-a .el-form-item {
		width: 50% !important;
	}

	.add-form .el-form-item {
		width: 33%;
	}

	.add-form-w100 .el-form-item {
		width: 100%;
	}

	.link-type,
	.link-type:focus {
		color: #337ab7;
		cursor: pointer
	}

	.link-type:focus:hover,
	.link-type:hover {
		color: #20a0ff
	}

	.el-table {
		border-top: 1px solid $xr-border-line-color;
	}

	.bm-view {
		width: 100%;
		height: 700px;
	}

	/deep/ .el-dialog {
		height: 700px;
		position: absolute;
		left: 0;
		top: 0;
		right: 0;
		bottom: 0;
		margin: auto;
		margin-top: auto !important;
	}

	/deep/ .el-dialog .el-dialog__body {
		height: 615px;
		overflow: auto;
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
		height: 200px;
	}

	.upload-show {
		position: relative;
		display: block;
		width: 360px;
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

	/deep/.el-form .el-input {
		width: 220px;
	}

	/deep/.el-form .el-select {
		width: 220px;
	}

	/deep/.el-form .el-textarea {
		width: 220px;
	}

	.add-form-w100 .el-input {
		width: 500px;
	}

	/*详情页面样式*/

</style>
