<template>
  <div class="main">
<!--    <xr-header-->
<!--      icon-class="wk wk-project"-->
<!--      icon-color="#2362FB"-->
<!--      label="项目授权管理"/>-->
    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :scroll-wheel-zoom="true" :inline="true"
               label-width="80px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="公司名称" prop="companyName">
          <el-input
            v-model="queryParams.companyName"
            placeholder="请输入公司名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="项目名称" prop="projectName">
          <el-input
            v-model="queryParams.projectName"
            placeholder="请输入项目名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>

        <el-form-item style="float: right;">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" v-if="isList" :data="typeList"
                highlight-current-row @row-click="handleRowClick" :cell-class-name="cellClassName">
        <el-table-column label="序号" type="index"  align='center' width="70"></el-table-column>        
        <el-table-column label="公司名称" align="center" prop="companyName" width="200px"/>
        <el-table-column label="项目名称" align="center" prop="projectName" width="250px"/>
        <el-table-column label="项目地址" align="center" prop="projectAddress"/>
        <el-table-column label="总造价（万元）" align="center" prop="totalCost"  width="120px"/>
        <el-table-column label="总面积（平方米）" align="center" prop="totalArea" width="120px"/>
        <el-table-column label="总造价（万元）" align="center" prop="totalCost"  width="120px"/>
        <el-table-column label="开工令日期" align="center" prop="commencementOrderDate"  width="120px">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.commencementOrderDate,'{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="计划竣工日期" align="center" prop="plannedCompletionDate" width="120px">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.plannedCompletionDate,'{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="工程状态" align="center" prop="engineeringStatus" :formatter="engineeringStatusFormat"/>
<!--        <el-table-column label="项目状态" align="center" prop="projectState" :formatter="projectStateFormat"/>-->
<!--        <el-table-column label="单位工程数量" align="center" prop="unitQuantity" />-->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-share"
              @click="grantUser(scope.row)"
            >授权管理
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div v-if="isList" class="p-contianer">
        <el-pagination
          :current-page="currentPage"
          :page-sizes="pageSizes"
          :page-size.sync="pageSize"
          :total="total"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div>

      <!-- 添加参数配置对话框 -->
      <el-dialog class="updataopen" :title="title" :visible.sync="open2" width="1200px" append-to-body>
        <el-form ref="form" :disabled="true" :model="form" label-width="160px" class="add-form">
          <el-divider content-position="left" class="add-form-divider">基本信息</el-divider>
          <el-row>
            <el-col :span="16" class="add-form-w100">
              <el-form-item label="项目名称" prop="pointCode" class="add-form-input">
                <el-input v-model="form.projectName" placeholder="请输入项目名称"/>
              </el-form-item>
              <el-form-item label="建设单位名称" prop="pointCode" class="add-form-input">
                <el-input v-model="form.constructionUnitName" placeholder="请输入建设单位名称"/>
              </el-form-item>
              <el-form-item label="施工许可证编号" class="add-form-input">
                <el-input v-model="form.constructionPermitCode" placeholder="请输入施工许可证编号"/>
              </el-form-item>
              <el-form-item label="建设用地规划许可证编号" class="add-form-input">
                <el-input v-model="form.landPlanningPermitCode" placeholder="请输入施工许可证编号"/>
              </el-form-item>
              <el-form-item label="建设工程规划许可证编号" class="add-form-input">
                <el-input v-model="form.projectPlanningLicenseCode" placeholder="请输入施工许可证编号"/>
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
          <el-form-item label="省份" class="add-form-input">
            <el-input v-model="form.province" placeholder="请输入省份"/>
          </el-form-item>
          <el-form-item label="地区" class="add-form-input">
            <el-input v-model="form.city" placeholder="请输入地区"/>
          </el-form-item>
          <el-form-item label="区县" class="add-form-input">
            <el-input v-model="form.district" placeholder="请输入区县"/>
          </el-form-item>
          <el-form-item label="项目地址" class="add-form-input">
            <el-input v-model="form.projectAddress" placeholder="请输入项目地址"/>
          </el-form-item>
          <el-form-item label="项目状态" class="add-form-input">
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
          <el-form-item label="项目分类" class="add-form-input">
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
          <el-form-item label="工程用途" class="add-form-input">
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
          <el-form-item label="建设性质" class="add-form-input">
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
          <el-form-item label="总造价(万元)" class="add-form-input">
            <el-input v-model="form.totalCost" placeholder="请输入项目地址"/>
          </el-form-item>
          <el-form-item label="总面积(平方米)" class="add-form-input">
            <el-input v-model="form.totalArea" placeholder="请输入项目地址"/>
          </el-form-item>

          <el-form-item label="建设用地规划许可证编号" class="add-form-input">
            <el-input v-model="form.constructionPermitCode" placeholder="请输入施工许可证编号"/>
          </el-form-item>
          <el-form-item label="公司名称" class="add-form-input">
            <el-input v-model="form.companyName" placeholder="请输入公司名称"/>
          </el-form-item>
          <el-form-item label="统一社会信用代码" class="add-form-input">
            <el-input v-model="form.uniscid" placeholder="请输入统一社会信用代码"/>
          </el-form-item>
          <el-form-item label="实际开工日期" class="add-form-input">
            <el-date-picker
              v-model="form.actualCommencementDate"
              type="date"
              placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
          </el-form-item>
          <el-form-item label="实际竣工日期" class="add-form-input">
            <el-date-picker
              v-model="form.actualCompletionDate"
              type="date"
              placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
          </el-form-item>
          <el-form-item label="联系人姓名" class="add-form-input">
            <el-input v-model="form.contactsName" placeholder="请输入项目地址"/>
          </el-form-item>
          <el-form-item label="联系人办公电话" class="add-form-input">
            <el-input v-model="form.contactsTel" placeholder="请输入项目地址"/>
          </el-form-item>
          <el-form-item label="立项文号" class="add-form-input">
            <el-input v-model="form.projectCode" placeholder="请输入项目地址"/>
          </el-form-item>
          <el-form-item label="立项级别" class="add-form-input">
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
          <el-form-item label="建筑规模" class="add-form-input">
            <el-select
              v-model="form.constructionScale"
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
          <el-form-item label="是否智慧工地" class="add-form-input">
            <el-switch v-model="form.isSmartBuild" active-value="1" inactive-value="0"/>
          </el-form-item>
          <el-form-item label="项目简介" class="add-form-input">
            <el-input v-model="form.projectBrief" type="textarea"/>
          </el-form-item>
          <el-form-item label="备注" class="add-form-input">
            <el-input v-model="form.remarks" type="textarea"/>
          </el-form-item>
          <el-divider content-position="left" class="add-form-divider add-form-input">位置信息</el-divider>
          <el-form-item label="WGS84经度" class="add-form-input">
            <el-input v-model="form.lng" placeholder="请输入项目地址"/>
          </el-form-item>
          <el-form-item label="WGS84纬度" class="add-form-input">
            <el-input v-model="form.lat" placeholder="请输入项目地址"/>
          </el-form-item>
          <el-divider content-position="left" class="add-form-divider add-form-input">银行信息</el-divider>
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
          <el-form-item label="对公账号号码" class="add-form-input">
            <el-input v-model="form.corporateAccount" placeholder="请输入项目地址"/>
          </el-form-item>
          <el-form-item label="代发工资银行" class="add-form-input">
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
          <el-form-item label="代发工资银行" class="add-form-input">
            <el-input v-model="form.payBankCode" placeholder="请输入项目地址"/>
          </el-form-item>
          <el-divider content-position="left" class="add-form-divider add-form-input">中标信息</el-divider>
          <el-form-item label="公司简称" class="add-form-input">
            <el-input v-model="form.companyName" placeholder="请输入公司简称"/>
          </el-form-item>
          <el-form-item label="项目简称" class="add-form-input">
            <el-input v-model="form.projectAbbreviation" placeholder="请输入项目简称"/>
          </el-form-item>
          <el-form-item label="所属国家" class="add-form-input">
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
          <el-form-item label="所属区域" class="add-form-input">
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
          <el-form-item label="工程编号" class="add-form-input">
            <el-input v-model="form.projectNo" placeholder="请输入工程编号"/>
          </el-form-item>
          <el-form-item label="使用资质" class="add-form-input">
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
          <el-form-item label="投标面积（m²）" class="add-form-input">
            <el-input v-model="form.tenderArea" placeholder="请输入投标面积"/>
          </el-form-item>
          <el-form-item label="土石方量（m²）" class="add-form-input">
            <el-input v-model="form.earthworkVolume" placeholder="请输入土石方量"/>
          </el-form-item>
          <el-form-item label="项目用途" class="add-form-input">
            <el-input v-model="form.projectPurpose" placeholder="请输入项目用途"/>
          </el-form-item>
          <el-form-item label="项目类型" class="add-form-input">
            <el-select
              v-model="form.projectType"
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
          <el-form-item label="结构类型" class="add-form-input">
            <el-input v-model="form.structureType" placeholder="请输入结构类型"/>
          </el-form-item>
          <el-form-item label="中标日期" class="add-form-input">
            <el-date-picker
              v-model="form.bidWinningDate"
              type="date"
              placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
          </el-form-item>
          <el-form-item label="中标开工日期" class="add-form-input">
            <el-date-picker
              v-model="form.bidWinningStartDate"
              type="date"
              placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
          </el-form-item>
          <el-form-item label="中标竣工日期" class="add-form-input">
            <el-date-picker
              v-model="form.bidWinningCompletionDate"
              type="date"
              placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
          </el-form-item>
          <el-form-item label="中标金额（万元）" class="add-form-input">
            <el-input v-model="form.bidAmount" placeholder="请输入中标金额"/>
          </el-form-item>
          <el-form-item label="承包模式" class="add-form-input">
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
          <el-form-item label="高达精特" class="add-form-input">
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
          <el-form-item label="投标利润率（%）" class="add-form-input">
            <el-input v-model="form.biddingProfitMargin" placeholder="请输入投标利润率"/>
          </el-form-item>
          <el-form-item label="经营模式" class="add-form-input">
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
          <el-form-item label="分包模式" class="add-form-input">
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
          <el-divider content-position="left" class="add-form-divider add-form-input">工程信息</el-divider>
          <el-form-item label="工程状态" class="add-form-input">
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
          <el-form-item label="商务状态" class="add-form-input">
            <el-select
              v-model="form.businessStatus"
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
          <el-form-item label="项目级别" class="add-form-input">
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
          <el-form-item label="主要施工范围" class="add-form-input">
            <el-input v-model="form.mainConstructionScope" placeholder="请输入主要施工范围"/>
          </el-form-item>
          <el-form-item label="建筑檐高（m）" class="add-form-input">
            <el-input v-model="form.buildingEavesHeight" placeholder="请输入建筑檐高"/>
          </el-form-item>
          <el-form-item label="合同开工日期" class="add-form-input">
            <el-date-picker
              v-model="form.contractCommencementDate"
              type="date"
              placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
          </el-form-item>
          <el-form-item label="合同竣工日期" class="add-form-input">
            <el-date-picker
              v-model="form.contractCompletionDate"
              type="date"
              placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
          </el-form-item>
          <el-form-item label="开工令日期" class="add-form-input">
            <el-date-picker
              v-model="form.commencementOrderDate"
              type="date"
              placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
          </el-form-item>
          <el-form-item label="计划竣工日期" class="add-form-input">
            <el-date-picker
              v-model="form.plannedCompletionDate"
              type="date"
              placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
          </el-form-item>
          <el-form-item label="地上面积（㎡）" class="add-form-input">
            <el-input v-model="form.abovegroundArea" placeholder="请输入地上面积"/>
          </el-form-item>
          <el-form-item label="地下面积（㎡）" class="add-form-input">
            <el-input v-model="form.undergroundArea" placeholder="请输入地下面积"/>
          </el-form-item>
          <el-form-item label="总层数" class="add-form-input">
            <el-input v-model="form.totalFloors" placeholder="请输入总层数"/>
          </el-form-item>
          <el-form-item label="单位工程数量" class="add-form-input">
            <el-input v-model="form.unitQuantity" placeholder="请输入单位工程数量"/>
          </el-form-item>
          <el-form-item label="工程类别" class="add-form-input">
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
          <el-form-item label="隐患提醒天数" class="add-form-input">
            <el-input v-model="form.hiddenDangerDaysReminder" placeholder="请输入隐患提醒天数"/>
          </el-form-item>
          <el-form-item label="隐患禁录天数" class="add-form-input">
            <el-input v-model="form.hiddenDangerBanDays" placeholder="请输入隐患禁录天数"/>
          </el-form-item>
          <el-divider content-position="left" class="add-form-divider add-form-input">质量安全精品创建</el-divider>
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
            <el-input v-model="form.excellentArea" placeholder="请输入隐患提醒天数"/>
          </el-form-item>
          <el-divider content-position="left" class="add-form-divider add-form-input">单位楼座信息</el-divider>
          <el-table
            :data="SingleBuilding"
            empty-text=""
            style="width: 100%">
            <el-table-column
              label="楼座"
              width="120">
              <template slot-scope="scope">
                <el-input v-model="SingleBuilding[scope.row.index].building" placeholder=""/>
              </template>
            </el-table-column>
            <el-table-column
              label="单元数"
              width="120">
              <template slot-scope="scope">
                <el-input v-model="SingleBuilding[scope.row.index].unitsNumber" placeholder=""/>
              </template>
            </el-table-column>
            <el-table-column
              label="地上楼层数"
              width="120">
              <template slot-scope="scope">
                <el-input v-model="SingleBuilding[scope.row.index].abovegroundNumber" placeholder=""/>
              </template>
            </el-table-column>
            <el-table-column
              label="地下层数"
              width="120">
              <template slot-scope="scope">
                <el-input v-model="SingleBuilding[scope.row.index].undergroundNumber" placeholder=""/>
              </template>
            </el-table-column>
            <el-table-column
              label="是否公共区域"
              width="120">
              <template slot-scope="scope">
                <el-input v-model="SingleBuilding[scope.row.index].isPublic" placeholder=""/>
              </template>
            </el-table-column>
            <el-table-column
              label="类型"
              width="120">
              <template slot-scope="scope">
                <el-input v-model="SingleBuilding[scope.row.index].type" placeholder=""/>
              </template>
            </el-table-column>
            <el-table-column
              label="排序"
              width="120">
              <template slot-scope="scope">
                <el-input v-model="SingleBuilding[scope.row.index].sort" placeholder=""/>
              </template>
            </el-table-column>
            <el-table-column
              label="备注"
              width="180">
              <template slot-scope="scope">
                <el-input v-model="SingleBuilding[scope.row.index].remarks" placeholder=""/>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancel">关 闭</el-button>
        </div>
      </el-dialog>

      <!-- 授权管理 -->
      <el-dialog class="updataopen" :title="grantTitle" :visible.sync="grantOpen" width="1200px" append-to-body>
        <GantUser :projectId="grantProId"/>
        <div slot="footer" class="dialog-footer">
          <el-button @click="grantOpen = false">关 闭</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import {getListAll, getProjectInfoById,} from '@/api/build/projectInfo'
  import EditImage from '@/components/EditImage'
  import Reminder from '@/components/Reminder'
  import XrHeader from '@/components/XrHeader'
  import {XhUserCell} from '@/components/CreateCom'
  import GantUser from './grant_user'

  export default {
    name: 'grantpro',
    components: {
      Reminder,
      XrHeader,
      XhUserCell,
      EditImage,
      GantUser
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
        // 列表是否显示
        isList: true,
        // 地图是否显示
        isMap: false,
        //地图窗口
        windowShow: false,
        windowCenter: {lng: 0, lat: 0},
        windowTitle: "",
        projectJGRQ: undefined,
        projectAddress: undefined,
        projectKGRQ: undefined,
        // 分页
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 30, 40],
        total: 0,
        tabPosition: 'list',
        // 字典表格数据
        typeList: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        open2: false,
        // 统计类型数据字典
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
        // 日期范围
        dateRange: [],
        // 查询参数
        queryParams: {
          page: this.currentPage,
          limit: this.pageSize,
          dictName: undefined,
          dictType: undefined,
          status: undefined
        },
        lzData: [],
        lzIndex: 0,
        SingleBuilding: [],
        // 表单参数
        form: {
          sort: undefined
        },
        // 表单校验
        rules: {
          pointCode: [
            {required: true, message: '采集点编号不能为空', trigger: 'blur'}
          ],
          pointName: [
            {required: true, message: '采集点名字不能为空', trigger: 'blur'}
          ],
          unit: [
            {required: true, message: '单位不能为空', trigger: 'blur'}
          ],
          storageField: [
            {required: true, message: '存储字段不能为空', trigger: 'blur'}
          ],
          statisticalType: [
            {required: true, message: '请选择统计类型', trigger: 'blur'}
          ],
          plantType: [
            {required: true, message: '请选择水厂类型', trigger: 'blur'}
          ],
          sort: [
            {type: 'number', message: '排序必须为数字值'}
          ]
        },
        center: {lng: 0, lat: 0},
        zoom: 3,
        systemImage: '',
        showEditImage: false,
        editImage: null,
        editFile: null,
        //授权
        grantOpen: false,
        grantTitle: '',
        grantProId: '',
      }
    },
    created() {
      this.getList()
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
        this.qualificationOptions = response.data
      })
      this.getDicts('b_safety_target').then(response => {
        this.safetyTargetOptions = response.data
      })
      this.getDicts('sys_yes_no').then(response => {
        this.yesNoOptions = response.data
      })
      this.getDicts('b_quality_level_target').then(response => {
        this.qualificationOptions = response.data
      })
    },
    methods: {
      //授权
      grantUser(row) {
        this.grantProId = row.id;
        this.grantTitle = row.projectName + "-授权";
        this.grantOpen = true;
      },
      handler({BMap, map}) {
        console.log(BMap, map)
        this.center.lng = 120.33152
        this.center.lat = 36.0883
        this.zoom = 12
      },
      /** 查询字典类型列表 */
      getList() {
        this.loading = true
        this.queryParams.page = this.currentPage
        this.queryParams.limit = this.pageSize
        getListAll(this.queryParams).then(response => {
          this.typeList = response.data.list
          this.total = response.data.totalRow
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },
      engineeringStatusFormat(row, column) {
        return this.selectDictLabel(this.engineeringStatusOptions, row.engineeringStatus)
      },
      statisticalTypeFormat(row, column) {
        return this.selectDictLabel(this.statisticalTypeOptions, row.statisticalType)
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.open2 = false;
        this.reset()
      },
      // 表单重置
      reset() {
        this.form = {}
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.currentPage = 1
        this.queryParams.limit = this.pageSize
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = []
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        const id = row.id || this.id
        getProjectInfoById(id).then(response => {
          console.log(response)
          this.form = response.data
          this.form.id = id
          this.form.isSmartBuild = response.data.isSmartBuild + "";
          this.open2 = true
          this.title = '项目信息详情'
        })
      },
      // 当某一行被点击时会触发该事件
      handleRowClick(row, column, event) {
        if (column.property == 'projectName') {
          this.handleUpdate(row)
        }
      },
      /**
       * 通过回调控制class
       */
      cellClassName({row, column, rowIndex, columnIndex}) {
        if (column.property === 'projectName') {
          return 'can-visit--underline can-visit--bold'
        }
      },
      // 更改每页展示数量
      handleSizeChange(val) {
        this.pageSize = val
        this.getList()
      },
      // 更改当前页数
      handleCurrentChange(val) {
        this.currentPage = val
        this.getList()
      },
      openList() {
        console.log(this.tabPosition)
        if (this.tabPosition == 'list') {
          this.isMap = false
          this.isList = true
        }
        if (this.tabPosition == 'map') {
          this.isMap = true
          this.isList = false
        }
      },
      infoWindowOpen(item) {
        this.windowShow = true;
        this.windowCenter = {lng: item.lng, lat: item.lat};
        this.projectAddress = item.projectAddress;
        this.projectKGRQ = item.contractCommencementDate;
        this.projectJGRQ = item.contractCompletionDate;
        this.windowTitle = item.projectName;
      },
      //添加楼座信息
      addlc() {
        this.lzIndex++
        this.SingleBuilding.push({
          index: this.lzIndex,
          building: null,
          unitsNumber: null,
          abovegroundNumber: null,
          undergroundNumber: null,
          isPublic: null,
          type: null,
          sort: null,
          remarks: null
        })
        console.log(this.SingleBuilding);
      },
      fileUpload(content) {
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
          self.editImage = result
          self.editFile = content.file
          self.showEditImage = true
        }
        reader.readAsDataURL(content.file)
      },
      deleteSystemImage() {
        this.systemImage = ''
        this.editedImage = null
      },
      submiteImage(data) {
        this.editedImage = data
        this.systemImage = data.image
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

  .handle-item-content {
    flex: 1;
  }

  .main-body {
    background-color: white;
    /*border-top: 1px solid $xr-border-line-color;*/
    border-bottom: 1px solid $xr-border-line-color;
  }

  .main-table-header-button {
    float: right;
    margin-right: 20px;
    margin-top: 10px;
  }

  .add-form-input {
    float: left;
    margin-bottom: 5px;
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

  .link-type, .link-type:focus {
    color: #337ab7;
    cursor: pointer
  }

  .link-type:focus:hover, .link-type:hover {
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
    height: 600px;
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
</style>
