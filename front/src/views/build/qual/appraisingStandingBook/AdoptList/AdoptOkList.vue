<template>
  <div>

    <!-- 头部：搜索、操作 end -->
    <!-- <div class="table-head-container">
      <flexbox
        v-if="selectionList.length > 0"
        class="selection-bar"
      >
        <div class="selected—title">已选中 <span class="selected—count">{{ selectionList.length }}</span> 项</div>
        <flexbox class="selection-items-box">
          <el-button
            v-for="(item, index) in selectionHandleItems"
            v-if="canHandle(item.type)"
            :icon="item.icon | wkIconPre"
            :key="index"
            type="primary"
            @click.native="handleTypeDrop(item.type)"
          >{{ item.name }}
          </el-button>
        </flexbox>
      </flexbox>
    </div> -->
<!--    <div-->
<!--      v-empty="!canHandle('list')"-->
<!--      xs-empty-icon="nopermission"-->
<!--      xs-empty-text="暂无权限"-->
<!--      class="crm-container"-->
<!--    >-->
      <el-table
        v-loading="loading"
        id="crm-table"
        :data="this.datalist"
        :height="tableHeight"
        class="n-table--border"
        use-virtual
        stripe
        border
        highlight-current-row
        :cell-class-name="cellClassName"
        style="width: 100%"
        @row-click="handleRowClick"
        @cell-click="pageto"
      >
<!--        <el-table-column-->
<!--          show-overflow-tooltip-->
<!--          type="selection"-->
<!--          align="center"-->
<!--          width="55"-->
<!--        />-->
         <el-table-column type="index" label="序号" align="center" width="80"/>
         <el-table-column prop="declareProject" label="申报项目" align="center" :formatter="fieldFormatter" sortable> </el-table-column>
         <el-table-column prop="projectManager" label="项目经理" align="center" width="150" :formatter="fieldFormatter" sortable> </el-table-column>
         <el-table-column prop="architectureScale" label="建筑规模" align="center" width="150" :formatter="fieldFormatter" sortable> </el-table-column>
         <el-table-column prop="totalScore" label="总计评分" align="center" width="150" :formatter="fieldFormatter" sortable> </el-table-column>
         <el-table-column prop="inspectionConclusion" label="检查结论" align="center" :formatter="fieldFormatter" sortable></el-table-column>
         <el-table-column prop="reviewTime" label="评审开始时间" align="center" width="140" :formatter="fieldFormatter" sortable> </el-table-column>
         <el-table-column prop="reviewEndTime" label="评审结束时间" align="center" width="140" :formatter="fieldFormatter" sortable> </el-table-column>
         <el-table-column prop="reviewLeader" label="评审组长" align="center" width="140" :formatter="fieldFormatter" sortable> </el-table-column>
      </el-table>
<!--      <div class="p-contianer">-->
<!--        <el-pagination-->
<!--          :current-page="currentPage"-->
<!--          :page-size.sync="pageSize"-->
<!--          :total="total"-->
<!--          class="p-bar"-->
<!--          background-->
<!--          layout="prev, pager, next, sizes, total, jumper"-->
<!--          @size-change="handleSizeChange"-->
<!--          @current-change="handleCurrentChange"-->
<!--        />-->
<!--      </div>-->
<!--    </div>-->
      <div class="p-contianer">
        <el-pagination
          :current-page="currentPage"
          :page-size.sync="pageSize"
          :total="totalNum"
          :pager-count="5"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
       <el-dialog class="updataopen" :title="title" :visible.sync="open2" width="1200px" append-to-body>
        <el-form ref="form" :disabled="true" :model="form" :rules="rules" label-width="170px" class="add-form">
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
          <create-sections style="overflow:hidden"  title="银行信息">
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
          <create-sections title="单位楼座信息">
            <!-- <el-button type="primary" icon="el-icon-plus" size="mini" @click="addLz" style="position: absolute;right: 10px;top: 0px;padding: 5px 10px" >添加</el-button> -->
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
              <!-- <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="danger"
                    @click="handleDelete(scope.row)">删除</el-button>
                </template>
              </el-table-column> -->
            </el-table>
          </create-sections>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="cancel">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </div>
    <!-- 相关详情页面 -->
    <!-- <detail
      v-if="showDview"
      :id="projectID"
      :titleName="titleName"
      :dataDetatil="dataDetatil"
      @close="showDview = false"
    /> -->

</template>

<script>
import { mapGetters } from "vuex"; //权限
import Lockr from "lockr"; //分页
 /************************项目详情查看start************************************/
 import { getList, addProjectInfo, updateProjectInfo, getProjectInfoById, delSupplyPoint,addBuilding,selectByProjectId,removeById } from '@/api/build/projectInfo'
  import {selectAreaList} from '@/api/common'
  import Reminder from '@/components/Reminder'
  import XrHeader from '@/components/XrHeader'
  import CreateSections from '@/components/CreateSections'
    import EditImage from '@/components/EditImage'
     import { XhUserCell } from '@/components/CreateCom'
 /************************项目详情查看end************************************/
import {
  // getList,
  delTrueByIds
} from "../../../labour/cermanage/speworktype/speworktype";
import { getProjectInfo } from "../../api/appraisingDeclare";

// import Detail from "../Detail";
export default {
  /** DEMO列表 */
  name: "DemoIndex",
  components: {
    // Detail,
     /************************项目详情查看start************************************/
       Reminder,
      XrHeader,
      XhUserCell,
      EditImage,
      CreateSections
     /************************项目详情查看end************************************/
    },
  props: {
    action: {
      type: Object,
      // 是否只读
      isReadonly: false,
      default: () => {
        return {
          type: "save",
          id: "",
          data: {}
        };
      }
    },
    datalist: {},
    dictData: {},
    total:{}
  },
  data() {
     /************************项目详情查看start************************************/
    var checkPhone = (rule, value, callback) => {
        if (!value) {
          callback();
        }else {
          const phoneReg = /^1[3|4|5|6|7|8][0-9]{9}$/;
          if (phoneReg.test(value)) {
            callback()
          } else {
            return callback(new Error('电话号码格式不正确'))
          }
        }
      };
        var checkXS = (rule, value, callback) => {
        if (!value) {
          callback();
        } else {
          const reg = /^(([^0][0-9]+|0)\.([0-9]{1,2})$)|^(([^0][0-9]+|0)$)|^(([1-9]+)\.([0-9]{1,2})$)|^(([1-9]+)$)/;
          if (reg.test(value)) {
            callback();
          } else {
            return callback(new Error('请输入正确数据类型'));
          }
        }
      };
       /************************项目详情查看end************************************/
    return {
       /************************项目详情查看start************************************/
      open2:false,
      title:"申报项目详情",
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
      lzOpen:false,
      lzform:{},
      lzIndex: 0,
      projectId:null,
      // 表单参数
      form: {
        sort: undefined
      },

      // 表单校验
        rules: {
          projectName: [
            { required: true, message: '必填项', trigger: 'blur' }
          ],
          constructionPermitCode: [
            { required: true, message: '必填项', trigger: 'blur' }
          ],
          companyName: [
            { required: true, message: '必填项', trigger: 'blur' }
          ],
          uniscid: [
            { required: true, message: '必填项', trigger: 'blur' }
          ],
          province: [
            { required: true, message: '必填项', trigger: 'blur' }
          ],
          city: [
            { required: true, message: '必填项', trigger: 'blur' }
          ],
          district: [
            { required: true, message: '必填项', trigger: 'blur' }
          ],
          projectState: [
            { required: true, message: '必填项', trigger: 'blur' }
          ],
          projectAddress: [
            { required: true, message: '必填项', trigger: 'blur' }
          ],
          businessState: [
            { required: true, message: '必填项', trigger: 'blur' }
          ],
          contractCommencementDate: [
            { required: true, message: '必填项', trigger: 'blur' }
          ],
          contractCompletionDate: [
            { required: true, message: '必填项', trigger: 'blur' }
          ],
          engineeringStatus: [
            { required: true, message: '必填项', trigger: 'blur' }
          ],
          contactsTel: [
            {  validator: checkPhone, trigger: 'blur' }
          ],
          bidAmount: [
            { required: false,validator: checkXS, trigger: 'blur'}
          ],
          abovegroundArea: [
            { required: false,validator: checkXS, trigger: 'blur'}
          ],
          totalArea: [
            {required: false, validator: checkXS, trigger: 'blur'}
          ],
          totalCost: [
            { required: false,validator: checkXS, trigger: 'blur'}
          ],
          tenderArea: [
            { required: false,validator: checkXS, trigger: 'blur'}
          ],
          earthworkVolume: [
            { required: false,validator: checkXS, trigger: 'blur'}
          ],
          biddingProfitMargin: [
            { required: false,validator: checkXS, trigger: 'blur'}
          ],
          buildingEavesHeight: [
            { required: false,validator: checkXS, trigger: 'blur'}
          ],
          undergroundArea: [
            { required: false,validator: checkXS, trigger: 'blur'}
          ],
          totalLength: [
            { required: false,validator: checkXS, trigger: 'blur'}
          ]
        },
         systemImage: '',
        showEditImage: false,
        editImage: null,
        editFile: null,
        // 区划数据-省
        provinceOptions: [],
        // 区划数据-市
        cityOptions: [],
        // 区划数据-区
        districtOptions: [],
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
        SingleBuildingType:[],
        projectTypeOptions:[],
 /************************项目详情查看end************************************/
      //详情数据
      dataDetatil:{},
      titleName: "查看",
      //项目名称
      ProjectInfos: [],
      //建设规模
      constructionDict: [],

      businessType: 10000,
      loading: false, // 加载动画
      /** 页面头部 */
      inputContent: "", //查询值
      //更多操作
      /** 选中时操作 */
      selectionHandleItems: [
        {
          name: "删除",
          type: "delete",
          icon: "delete"
        }
      ],
      /** 列表 */
      tableHeight: document.documentElement.clientHeight - 320, // 表的高度
      list: [],
      fieldList: [
        //表头
        {
          prop: "declareProject",
          label: "申报项目",
          // width: "300",
          align: "center"
        },
        {
          prop: "projectManager",
          label: "项目经理",
          width: "150",
          align: "center"
        },
        {
          prop: "architectureScale",
          label: "建筑规模",
          width: "150",
          align: "center"
        },
        // {
        //   prop: "batchId",
        //   label: "评审材料",
        //   width: "300",
        //   align: "center"
        // },
        {
          prop: "totalScore",
          label: "总计评分",
          width: "150",
          align: "center"
        },
        {
          prop: "inspectionConclusion",
          label: "检查结论",
          // width: "300",
          align: "center"
        },
          {
              prop: "reviewTime",
              label: "评审开始时间",
              width: "140",
              align: "center"
          },
        {
          prop: "reviewEndTime",
          label: "评审结束时间",
          width: "140",
          align: "center"
        },
        {
          prop: "reviewLeader",
          label: "评审组长",
          width: "140",
          align: "center"
        }
      ],
      sortData: [], // 字段排序
      currentPage: 1,
      pageSize: 10,
      pageSizes: [10,20,30,40,50,100],
      // total: 0,
      queryParams: {},
      createShow: false,
      /***/
      // action: {type: 'save', data: null},
      /** 勾选行 */
      selectionList: [], // 勾选数据 用于全局导出
      /** 控制详情展示 */
      rowID: "", // 行信息
      showDview: false,
      /** 高级筛选 2021-03-15 暂未使用*/
      filterObj: [], // 筛选确定数据
      totalNum:this.total,
      //证书类型
      // cerCategoryTypeOptions: [],
      //项目id
      projectID:'',
    };
  },

  computed: {
    //权限 start
    ...mapGetters(["allAuth"]),
    //权限 end
    titleIcon() {
      return require(`@/assets/img/crm/customer.png`);
    }
  },
  mounted() {
    /** 控制table的高度 */
    window.onresize = () => {
      this.updateTableHeight();
    };
    //获取列表数据
    // this.getList();
    this.initDatas();
  },
  created() {
    //  证书类型 字典项
    // this.getDicts("b_cerCategoryType").then(response => {
    //   this.cerCategoryTypeOptions = response.data;
    // });
    // console.log(this.total);
     this.geAdoptDicts();
  },
  methods: {
    /************************项目详情查看start************************************/
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
        // 清空市、区区划数据
        this.cityOptions = []
        this.districtOptions = []
        // 获取省级区划
        this.getProvinceList();
      },
      clickView(id){
        console.log("项目id==="+id)
         this.reset();
        this.getProvinceList()
        this.projectId = id;
        this.getlzData();
        getProjectInfoById(id).then(response => {
          // console.log(response)
          this.form = response.data
          this.systemImage = response.data.projectImg
          this.form.id = id
          this.form.isSmartBuild = response.data.isSmartBuild+"";
          this.open2 = true
          this.title = '修改项目信息'
          this.getCityList(response.data.province)
          this.getDistrictList(response.data.city)
          this.form.province = parseInt(response.data.province);
          this.form.city = parseInt(response.data.city);
          this.form.district = parseInt(response.data.district);
        })
      },
       deleteSystemImage() {
        this.systemImage = ''
        this.editedImage = null
      },
      submiteImage(data) {
        this.editedImage = data
        this.systemImage = data.image
      },
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
      //获取楼座信息
      getlzData(){
        selectByProjectId(this.projectId).then(response => {
          this.lzData = response.data
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },
      // 获取省级区划数据
      getProvinceList() {
        const param = {
          level:1
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
          // console.log(this.cityOptions)
        })
      },
      // 获取县级区划数据
      getDistrictList(id) {
        const param = {
          parentId: id
        }
        selectAreaList(param).then(res => {
          this.districtOptions = res.data
          // console.log(this.districtOptions)

        })
      },
      areaChangeProvince(d){
        this.form.city = ''
        this.form.district = ''
        this.getCityList(d)
      },
      areaChangeCity(d){
        this.form.district = ''
        this.getDistrictList(d)
        this.$forceUpdate()
      },
      areaChangeDistrict(d){
        this.$forceUpdate()
      },
      // addLz(){
      //   this.lzform = {};
      //   this.lzOpen = true;
      // },
       engineeringStatusFormat(row, column) {
        return this.selectDictLabel(this.engineeringStatusOptions, row.engineeringStatus)
      },
      projectStateFormat(row, column) {
        return this.selectDictLabel(this.engineeringStatusOptions, row.projectState)
      },
      projectClassifyFormat(row, column) {
        return this.selectDictLabel(this.projectClassifyOptions, row.projectClassify)
      },
      singleBuildingTypeFormat(row, column) {
        return this.selectDictLabel(this.SingleBuildingType, row.type)
      },
      singleBuildingIsFormat(row, column) {
        return this.selectDictLabel(this.yesNoOptions, row.isPublic)
      },

      geAdoptDicts(){
        /************************项目详情查看start************************************/
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
       /************************项目详情查看end************************************/
      },
    /******************************项目详情查看end************************************/
    /**
     *更多操作点击
     */
    handleTypeDrop(command) {
      // console.log(command)
      if (command == "delete") {
        //删除
        this.$confirm("确定删除选中的数据吗？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            const ids = this.selectionList.map(item => item["id"]);
            this.loading = true;
            delTrueByIds(ids)
              .then(res => {
                if (res.code == 0) {
                  this.$message.success("删除成功");
                  // this.getList();
                } else {
                  this.$message.error(res.msg);
                }
                this.loading = false;
              })
              .catch(err => {
                this.loading = false;
              });
          })
          .catch(() => {});
      }
    },
    // 当某一行被点击时会触发该事件
    handleRowClick(row, column, event) {
      if (column.property == "code") {
        this.action.type = "showDetials";
        this.action.id = id;
        this.createShow = true;
      }
    },
    //列格式化  如一些字典等
    fieldFormatter(row, column, cellValue) {
      //console.log(row, column)
      //证件类别
      if (column.property === "reviewLeader") {
        return this.CustomFormat(this.dictData.userListData, row.reviewLeader);
      } else if (column.property === "declareProject") {
        return this.CustomFormat2(this.ProjectInfos, row.declareProject);
      }else if (column.property === "architectureScale") {
         return this.selectDictLabel(this.constructionDict, row.architectureScale)
      }
      return row[column.property] === "" || row[column.property] === null
        ? "--"
        : row[column.property];
    },
    //按钮操作权限
    canHandle(type) {
      return this.allAuth.demo
        ? this.allAuth.demo.wf
          ? this.allAuth.demo.wf[type]
          : false
        : false;
    },

    /** 勾选操作 */
    // 当选择项发生变化时会触发该事件
    // handleSelectionChange(val) {
    //   this.selectionList = val; // 勾选的行
    //   this.updateTableHeight();
    // },

    // 更改当前页数
    handleCurrentChange(val) {
      this.currentPage = val;
      // this.getList();
    },

    // 更改每页展示数量
    handleSizeChange(val) {
      // Lockr.set("crmPageSizes", val);
      this.pageSize = val;
      // this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {};
      // this.getList();
    },

    /**
     * 修改点击
     */
    updateClick(id) {
      this.action.type = "update";
      this.action.id = id;
      this.createShow = true;
    },
    /**
     * 查看点击
     */
    showDetails(id) {
      this.action.type = "showDetials";
      this.action.id = id;
      this.createShow = true;
      this.showDview = true;
    },
    deleteClick(id) {
      this.$confirm("确定删除选中的数据吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          const ids = [id];
          this.loading = true;
          delTrueByIds(ids)
            .then(res => {
              if (res.code == 0) {
                this.$message.success("删除成功");
                // this.getList();
              } else {
                this.$message.error(res.msg);
              }
              this.loading = false;
            })
            .catch(err => {
              this.loading = false;
            });
        })
        .catch(() => {});
    },
    /**
     * 更新表高
     */
    // updateTableHeight() {
    //   var offsetHei = document.documentElement.clientHeight;
    //   var removeHeight = this.selectionList.length > 0 ? 390 : 340;
    //   this.tableHeight = offsetHei - removeHeight;
    // },
    initDatas() {
      //获取字典信息
      this.getDicts("b_construction_scale").then(response => {
        this.constructionDict = response.data;
      });
      //获取所有项目信息
      getProjectInfo()
        .then(res => {
          // console.log("项目信息", res.data.list);
          this.ProjectInfos = res.data.list;
        })
        .catch(() => {});
    },
    //格式化用户名称
    CustomFormat(datas, value) {
      // console.log(datas,value);
      var actions = [];
      Object.keys(datas).some(key => {
        if (datas[key].userId == "" + value) {
          actions.push(datas[key].realname);
          return true;
        }
      });
      return actions.join("");
    },
    //格式化项目名称
    CustomFormat2(datas, value) {
      // console.log(datas,value);
      var actions = [];
      Object.keys(datas).some(key => {
        if (datas[key].id == "" + value) {
          actions.push(datas[key].projectName);
          return true;
        }
      });
      return actions.join("");
    },
    //跳转项目详情页面
    pageto(row, column, cell, event) {
       if(column.label=="申报项目"){
            this.action.type = "showDetials";
            // this.open2=true;
            this.clickView(row.declareProject)
       }
    },
     /**
       * 通过回调控制class
       */
      cellClassName({row, column, rowIndex, columnIndex}) {
        if (column.property == 'declareProject') {
          return 'can-visit--underline can-visit--bold'
        }
      },
      headerCellClassName({row, column, rowIndex, columnIndex}){
        return '';
      },
  }
};
</script>

<style lang="scss" scoped>
//表头
.table-head-container {
  //border-bottom: 1px solid #e6e6e6;
  border-top: 1px solid #e6e6e6;
  background-color: white;
}

.th-container {
  font-size: 13px;
  height: 50px;
  padding: 0 20px;

  .scene-select {
    width: 180px;
    /deep/ .el-input__inner {
      cursor: pointer;
    }
  }

  .filter-button {
    margin-left: 20px;
    /deep/ i {
      font-size: 14px;
      margin-right: 5px;
    }
  }
}

/** 场景和筛选 */
.condition_title {
  cursor: pointer;
}

.condition_title:hover {
  color: $xr-color-primary;
}

.m-arrow {
  margin: 0 8px;
}

.c-filtrate {
  margin: 0 10px 0 30px;
  width: 12px;
}

/** 勾选操作 */
.selection-bar {
  font-size: 12px;
  height: 50px;
  padding: 0 20px;
  color: #777;

  .selected—title {
    flex-shrink: 0;
    padding-right: 20px;
    color: #333;
    .selected—count {
      color: $xr-color-primary;
    }
  }
}

.selection-items-box {
  overflow-x: auto;
  overflow-y: hidden;
  padding: 0 15px;

  .el-button {
    color: #666;
    background-color: #f9f9f9;
    border-color: #e5e5e5;
    font-size: 12px;
    height: 28px;
    border-radius: 4px;
    /deep/ i {
      font-size: 12px;
      margin-right: 5px;
    }
  }

  .el-button--primary:hover {
    background: $xr-color-primary;
    border-color: $xr-color-primary;
    color: #ffffff;
  }

  .el-button + .el-button {
    margin-left: 15px;
  }
}

//页面头部
.c-container {
  height: 60px;
  position: relative;
  //z-index: 100;
  .title-icon {
    width: 30px;
    height: 30px;
    margin-right: 10px;
  }

  .title {
    float: left;
    width: auto;
    padding-left: 28px;
    font-size: 16px;
    font-weight: 600;
    margin-top: 15px;
  }
  .sc-container {
    width: 300px;
    margin: -16px 0 0 -150px;
    position: absolute;
    left: 50%;
    top: 50%;
  }

  .el-input {
    /deep/ .el-input-group__append {
      background-color: $xr-color-primary;
      border-color: $xr-color-primary;
      color: white;
    }
  }

  .right-container {
    margin-right: -10px;
    float: right;
    margin: 15px 20px 0 0;
    position: relative;

    .dup-check-btn,
    .xr-btn--orange {
      margin-left: 0;
      margin-right: 10px;
    }

    .right-item {
      float: left;
      margin-right: 10px;
      padding: 8px 10px;
      font-size: 13px;
      border-radius: 2px;
    }
  }
}
/deep/ .is-always-shadow{
  width: 1200px!important
}

@import "@/views/styles/table.scss";
</style>
<style lang="scss" scoped>
  .main {
    height:100%;
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

  .add-form-input{
    float: left;
    margin-bottom: 14px;

  }
  add-form-divider{
    float: left;
    margin-bottom: 10px;
  }
  .add-form::after{
    content: "";
    display: block;
    clear: both;
  }

  add-form-a .el-form-item{
    width: 50% !important;
  }
  .add-form .el-form-item{
    width: 33%;
  }
  .add-form-w100 .el-form-item{
    width: 100%;
  }

  .link-type,.link-type:focus{color:#337ab7;cursor:pointer}

  .link-type:focus:hover,.link-type:hover{color:#20a0ff}

  .el-table {
    border-top: 1px solid $xr-border-line-color;
  }
  .bm-view {
    width: 100%;
    height: 700px;
  }
  /deep/ .el-dialog{
    height: 700px;
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    margin: auto;
    margin-top: auto!important;
  }

  /deep/ .el-dialog .el-dialog__body{
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
  /deep/.el-form .el-input{
    width: 220px;
  }
  /deep/.el-form .el-select{
    width: 220px;
  }
  /deep/.el-form .el-textarea{
    width: 220px;
  }
  .add-form-w100  .el-input{
    width: 500px;
  }
</style>
