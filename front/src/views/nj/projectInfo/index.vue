<template>
  <div class="main">
    <div class="main-body">
      <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :scroll-wheel-zoom="true" :inline="true" label-width="80px" style="padding-top: 20px;padding-left:10px;">
        <el-form-item label="施工单位" prop="companyName">
          <el-input
            v-model="queryParams.companyName"
            placeholder="请输入施工单位名称"
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
        <el-form-item label="项目状态" prop="projectState">
          <el-select
            v-model="queryParams.projectState"
            placeholder="请选择"
            clearable
            size="small"
            style="width: 200px"
          >
            <el-option
              v-for="dict in engineeringStatusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>

        <el-form-item style="margin-left: 20px">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
        <el-form-item style="margin-left: 50px">
          <el-radio-group v-model="tabPosition" @change="openList">
            <el-radio-button label="list">列表</el-radio-button>
            <el-radio-button label="map" >地图</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item style="float: right;">
          <el-button
            class=" xr-btn--orange"
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd" >新建</el-button>
          <el-button type="primary" @click="bulkImportClick">项目导入</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" v-if="isList" :data="typeList" :height="tableHeight" @sort-change="sortChange">
        <el-table-column label="序号" type="index"  align='left' width="70"></el-table-column>
        <el-table-column label="项目名称" align="left" prop="projectName" width="120px" sortable="custom"/>
        <el-table-column label="项目简称" align="left" prop="projectAbbreviation" width="120px" sortable="custom"/>
        <el-table-column label="建设单位名称" align="left" prop="constructionUnitName" width="180px" sortable="custom"/>
        <el-table-column label="施工单位名称" align="left" prop="companyName" width="180px" sortable="custom"/>
        <!--        <el-table-column label="开户银行" align="center" prop="corporateAccountBank" />-->
        <!--        <el-table-column label="对公账户" align="center" prop="corporateAccount" />-->
        <el-table-column label="项目地址" align="left" prop="projectAddress" width="120px" sortable="custom" />
<!--        <el-table-column :formatter="projectClassifyFormat" label="项目分类" align="center" prop="projectClassify" width="120px"/>-->
        <el-table-column label="总造价（万元）" align="left" prop="totalCost" width="140px" sortable="custom"/>
        <el-table-column label="总面积（平方米）" align="left" prop="totalArea" width="160px" sortable="custom"/>
        <el-table-column label="联系人" align="left" prop="contactsName" width="120px" sortable="custom"/>
        <el-table-column label="联系人电话" align="left" prop="contactsTel" width="120px" sortable="custom"/>
        <el-table-column label="计划竣工日期" align="left" prop="plannedCompletionDate" width="140px" sortable="custom">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.plannedCompletionDate,'{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
		<el-table-column label="排序" align="left" prop="sort" width="120px" sortable="custom"/>
<!--        <el-table-column :formatter="engineeringStatusFormat" label="工程状态" align="center" prop="engineeringStatus"/>-->
        <el-table-column :formatter="projectStateFormat" label="项目状态" align="left" prop="projectState" width="100px"/>
<!--        <el-table-column label="单位工程数量" align="center" prop="unitQuantity" />-->
        <el-table-column label="操作" align="left" class-name="small-padding fixed-width" width="120px">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate2(scope.row)"
            >修改</el-button>
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
      <baidu-map v-if="isMap" :center="center" :zoom="zoom" class="bm-view" @ready="handler">
        <bm-marker v-for="item in typeList" :key="item.id" :position="{lng: item.lng, lat: item.lat}" :icon="{url: 'static/img/bj1.png', size: {width: 60, height: 55}}" @click="infoWindowOpen(item)" />
        <bm-info-window :show="windowShow" :title="windowTitle" :position="windowCenter" >
          <div>
            {{ '地址： ' + projectAddress }}
          </div>
          <div v-if="projectKGRQ">
            {{ '开工日期： ' + projectKGRQ }}    {{ '竣工日期： ' + projectJGRQ }}
          </div>
        </bm-info-window>
      </baidu-map>

      <!-- 添加参数配置对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="900px"   append-to-body>
        <el-form ref="form" :model="form" :rules="new_rules" label-width="140px" label-position="right" >
          <el-row>
            <el-col :span="12">
              <el-form-item label="建设单位名称" prop="constructionUnitName" class="add-form-input">
                <el-input v-model="form.constructionUnitName" placeholder="请输入建设单位名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="施工单位名称" prop="companyName" class="add-form-input">
                <el-input v-model="form.companyName" placeholder="请输入施工单位名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="项目名称" prop="projectName" class="add-form-input">
                <el-input v-model="form.projectName" placeholder="请输入项目名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="项目简称" prop="projectAbbreviation" class="add-form-input">
                <el-input v-model="form.projectAbbreviation" placeholder="请输入项目简称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="项目状态" prop="projectState" class="add-form-input">
                 <el-select v-model="form.projectState"  placeholder="请选择状态" clearable size="small"  style="width: 200px" @change="areaChangeProvince">
                 <el-option v-for="dict in engineeringStatusOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="施工许可证编号" prop="constructionPermitCode" class="add-form-input">
                <el-input v-model="form.constructionPermitCode" placeholder="请输入施工许可证编号" />
              </el-form-item>
            </el-col>
             <el-col :span="12">
              <el-form-item label="工程造价(万元)" prop="totalCost" class="add-form-input">
                <el-input v-model="form.totalCost" placeholder="请输入工程造价" />
              </el-form-item>
            </el-col>
             <el-col :span="12">
              <el-form-item label="工程面积(平)" prop="totalArea" class="add-form-input">
                <el-input v-model="form.totalArea" placeholder="请输入工程面积" />
              </el-form-item>
            </el-col>
             <el-col :span="12">
              <el-form-item label="联系人" prop="contactsName" class="add-form-input">
                <el-input v-model="form.contactsName" placeholder="请输入联系人" />
              </el-form-item>
            </el-col>
             <el-col :span="12">
              <el-form-item label="联系电话" prop="contactsTel" class="add-form-input">
                <el-input v-model="form.contactsTel" placeholder="请输入联系电话" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="开工令日期" prop="commencementOrderDate" class="add-form-input">
                <el-date-picker
                  v-model="form.commencementOrderDate"
                  type="date"
                  placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="实际开工日期" prop="actualCommencementDate" class="add-form-input">
                <el-date-picker
                  v-model="form.actualCommencementDate"
                  type="date"
                  placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="计划竣工日期" prop="plannedCompletionDate" class="add-form-input">
                <el-date-picker
                  v-model="form.plannedCompletionDate"
                  type="date"
                  placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="项目地址" prop="projectAddress" class="add-form-input">
                <el-input type="textarea" :row="1" v-model="form.projectAddress" placeholder="请输入项目地址" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="排序" prop="sort" class="add-form-input">
				        <el-input v-model.number="form.sort" type="number" placeholder="请输入" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <create-sections style="overflow:hidden" title="施工平面图">
                <el-row>
                  <el-col :span="12" style="padding: 10px">
                    <el-upload
                      v-if="!systemAddPlaneImage"
                      :show-file-list="false"
                      :http-request="fileAddPlaneUpload"
                      drag
                      class="upload"
                      action="http"
                      accept="image/png, image/jpeg, image/gif, image/jpg">
                      <i class="el-icon-plus uploader-icon"/>
                    </el-upload>
                    <div
                      v-else
                      class="upload-show">
                      <img  v-src="systemAddPlaneImage">
                      <i
                        class="el-icon-remove icon-delete"
                        @click="deleteSystemAddPlaneImage"/>
                    </div>

                    <edit-image
                      :fixed-number="[16, 9]"
                      :show="showEditAddPlaneImage"
                      :image="editAddPlaneImage"
                      :file="editAddPlaneFile"
                      title="编辑项目图片"
                      preview-width="160px"
                      preview-height="90px"
                      preview-radius="0"

                      width="550px"
                      save-button-title="确定"
                      @save="submiteAddPlaneImage"
                      @close="showEditAddPlaneImage=false"/>
                  </el-col>
                  <el-col :span="12" style="padding: 10px">
                    <el-row>
                      <el-col :span="24">
                        <el-form-item label="经度" prop="lng" class="add-form-input">
                          <el-input v-model="form.lng" placeholder="请输入经度" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="24">
                        <el-form-item label="纬度" prop="lat" class="add-form-input">
                          <el-input v-model="form.lat" placeholder="请输入纬度" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="24">
                        <el-form-item label="横向坐标" prop="constructionPermitCode" class="add-form-input">
                          <el-input v-model="form.planeCoordinateX" type="number"  placeholder="请输入横向坐标" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="24">
                        <el-form-item label="纵向坐标" prop="companyName" class="add-form-input">
                          <el-input v-model="form.planeCoordinateY" type="number" placeholder="请输入纵向坐标" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </create-sections>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm2">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
      <!-- 修改对话框 -->
      <el-dialog :title="title" :visible.sync="open2" class="updataopen" width="1200px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="170px" class="add-form">
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
                  <img style="margin: 0 auto;display: block;height: 100%;width:auto;"  :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="systemImage">
                  <i
                    class="el-icon-remove icon-delete"
                    @click="deleteSystemImage"/>
                </div>

                <edit-image
                  :fixed-number="[16, 9]"
                  :show="showEditImage"
                  :image="editImage"
                  :file="editFile"
                  title="编辑项目图片"
                  preview-width="160px"
                  preview-height="90px"
                  preview-radius="0"

                  width="550px"
                  save-button-title="确定"
                  @save="submiteImage"
                  @close="showEditImage=false"/>
              </el-col>
            </el-row>
            <el-form-item label="省份" prop="province" class="add-form-input">
              <el-select
                v-model="form.province"
                placeholder="请选择"
                clearable
                size="small"
                style="width: 200px"
                @change="areaChangeProvince"
              >
                <el-option
                  v-for="option in provinceOptions"
                  :key="option.id"
                  :label="option.name"
                  :value="option.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="所属市" prop="city" class="add-form-input">
              <el-select
                v-model="form.city"
                placeholder="请选择"
                clearable
                size="small"
                style="width: 200px"
                @change="areaChangeCity"
              >
                <el-option
                  v-for="option in cityOptions"
                  :key="option.id"
                  :label="option.name"
                  :value="option.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="所属区" prop="district" class="add-form-input">
              <el-select
                v-model="form.district"
                placeholder="请选择"
                clearable
                size="small"
                style="width: 200px"
                @change="areaChangeDistrict"
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

            <el-form-item label="项目简介/工程概况" prop="projectBrief" class="add-form-input" style="width: 100%">
              <el-input v-model="form.projectBrief" type="textarea" style="width: 100%"/>
            </el-form-item>
            <el-form-item label="备注" class="add-form-input" style="width: 100%">
              <el-input v-model="form.remarks" type="textarea" style="width: 100%"/>
            </el-form-item>
          </create-sections>

          <create-sections style="overflow:hidden" title="位置信息">
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
          <create-sections style="overflow:hidden" title="中标信息">
            <el-form-item label="施工单位简称" prop="companyName" class="add-form-input">
              <el-input v-model="form.companyName" placeholder="请输入施工单位简称" />
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
                placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
            </el-form-item>
            <el-form-item label="中标竣工日期" prop="bidWinningCompletionDate" class="add-form-input">
              <el-date-picker
                v-model="form.bidWinningCompletionDate"
                type="date"
                placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"/>
            </el-form-item>
            <el-form-item label="中标金额（万元）" prop="bidAmount" class="add-form-input">
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
          <create-sections style="overflow:hidden" title="工程信息">
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
            <el-form-item label="联系人" prop="contactsName" class="add-form-input">
              <el-input v-model="form.contactsName" placeholder="请输入联系人" />
            </el-form-item>
            <el-form-item label="联系电话" prop="contactsTel" class="add-form-input">
              <el-input v-model="form.contactsTel" placeholder="请输入联系电话" />
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
          <create-sections style="overflow:hidden" title="质量安全精品创建">
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
          <create-sections style="overflow:hidden" title="项目管理体系">
            <el-form-item label="项目经理" prop="projectManagerList" class="add-form-input">
              <xh-user-cell :radio="false" v-model="form.projectManagerList" @value-change="projectManagerCha"/>
            </el-form-item>
            <el-form-item label="项目执行经理" class="add-form-input">
              <xh-user-cell :radio="false" :value="projectExecutiveManager" @value-change="projectExecutiveManagerCha"/>
            </el-form-item>
            <el-form-item label="项目副经理" class="add-form-input">
              <xh-user-cell :radio="false" :value="deputyProjectManager" @value-change="deputyProjectManagerCha"/>
            </el-form-item>
            <el-form-item label="商务经理" class="add-form-input">
              <xh-user-cell :radio="false" :value="businessManager" @value-change="businessManagerCha"/>
            </el-form-item>
            <el-form-item label="项目总工" class="add-form-input">
              <xh-user-cell :radio="false" :value="projectDepartmentChief" @value-change="projectDepartmentChiefCha"/>
            </el-form-item>
            <el-form-item label="专项质量员" class="add-form-input">
              <xh-user-cell :radio="false" :value="specialQualityOfficer" @value-change="specialQualityOfficerCha"/>
            </el-form-item>
            <el-form-item label="专职安全员" class="add-form-input">
              <xh-user-cell :radio="false" :value="fullTimeSafetyOfficer" @value-change="fullTimeSafetyOfficerCha"/>
            </el-form-item>
            <el-form-item label="信息化" class="add-form-input">
              <xh-user-cell :radio="false" :value="informationize" @value-change="informationizeCha"/>
            </el-form-item>
            <el-form-item label="材料员" class="add-form-input">
              <xh-user-cell :radio="false" :value="materialman" @value-change="materialmanCha"/>
            </el-form-item>
            <el-form-item label="预算员" class="add-form-input">
              <xh-user-cell :radio="false" :value="budgeter" @value-change="budgeterCha"/>
            </el-form-item>
            <el-form-item label="施工员" class="add-form-input">
              <xh-user-cell :radio="false" :value="constructionWorkers" @value-change="constructionWorkersCha"/>
            </el-form-item>
            <el-form-item label="成本会计" class="add-form-input">
              <xh-user-cell :radio="false" :value="costAccounting" @value-change="costAccountingCha"/>
            </el-form-item>
            <el-form-item label="劳务管理员" class="add-form-input">
              <xh-user-cell :radio="false" :value="laborAdministrator" @value-change="laborAdministratorCha"/>
            </el-form-item>
            <el-form-item label="劳务分管领导" class="add-form-input">
              <xh-user-cell :radio="false" :value="laborLeader" @value-change="laborLeaderCha"/>
            </el-form-item>
            <el-form-item label="劳务分管领导电话" prop="laborLeaderTel" class="add-form-input">
              <el-input v-model="form.laborLeaderTel" placeholder="请输入劳务分管领导电话" />
            </el-form-item>
            <el-form-item label="项目经理电话" prop="projectManagerTel" class="add-form-input">
              <el-input v-model="form.projectManagerTel" placeholder="请输入项目经理电话" />
            </el-form-item>
          </create-sections>
          <create-sections style="overflow:hidden" title="相关单位">
            <el-row>

                <el-form-item label="监理单位" prop="supervisorCompany" class="add-form-input">
                  <el-input v-model="form.supervisorCompany" placeholder="请输入监理单位" />
                </el-form-item>

                <el-form-item label="设计单位" prop="designCompany" class="add-form-input">
                  <el-input v-model="form.designCompany" placeholder="请输入设计单位" />
                </el-form-item>

                <el-form-item label="勘察单位" prop="investigationCompany" class="add-form-input">
                  <el-input v-model="form.investigationCompany" placeholder="请输入勘察单位" />
                </el-form-item>

            </el-row>

            <el-row>

              <el-form-item label="工程所在地人社部投诉电话" prop="societyComplaintCall" class="add-form-input">
                <el-input v-model="form.societyComplaintCall" placeholder="请输入" />
              </el-form-item>

              <el-form-item label="甲方人设投诉电话" prop="abuildComplaintCall" class="add-form-input">
                <el-input v-model="form.abuildComplaintCall" placeholder="请输入" />
              </el-form-item>

            </el-row>

            <el-row>

              <el-form-item label="工程所在地住建部投诉电话" prop="buildComplaintCall" class="add-form-input">
                <el-input v-model="form.buildComplaintCall" placeholder="请输入" />
              </el-form-item>

              <el-form-item label="甲方住建部投诉电话" prop="asocietyComplaintCall" class="add-form-input">
                <el-input v-model="form.asocietyComplaintCall" placeholder="请输入" />
              </el-form-item>
            </el-row>
            <el-row>
            <el-form-item label="劳务分包" prop="laborSubcontract" class="add-form-input">
              <el-input v-model="form.laborSubcontract"  type="textarea" placeholder="请输入"/>
            </el-form-item>
            <el-form-item label="专业分包" prop="majorSubcontract" class="add-form-input" >
              <el-input v-model="form.majorSubcontract" type="textarea" placeholder="请输入"/>
            </el-form-item>
            </el-row>
          </create-sections>
          <create-sections title="单位楼座信息">
            <el-button type="primary" icon="el-icon-plus" size="mini" style="position: absolute;right: 10px;top: 0px;padding: 5px 10px" @click="addLz" >添加</el-button>
            <el-table
              :data="lzData"
              empty-text=""
              style="width: 100%">
              <el-table-column label="楼座" align="center" prop="building"/>
              <el-table-column label="单元数" align="center" prop="unitsNumber"/>
              <el-table-column label="地上层数" align="center" prop="abovegroundNumber"/>
              <el-table-column label="地下层数" align="center" prop="undergroundNumber"/>
              <el-table-column :formatter="singleBuildingIsFormat" label="是否公共区域" align="center" prop="isPublic" />
              <el-table-column :formatter="singleBuildingTypeFormat" label="类型" align="center" prop="type"/>
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
      <el-dialog :visible.sync="lzOpen" title="新增楼座" width="900px" append-to-body>
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
                <el-input v-model="lzform.abovegroundNumber" type="number" placeholder="请输入地上层数" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="地下层数" prop="undergroundNumber" class="add-form-input">
                <el-input v-model="lzform.undergroundNumber" type="number" placeholder="请输入地下层数" />
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
    <!-- 批量导入 -->
    <bulk-import-user
      :show="bulkImportShow"
      :param="param"
      @close="bulkImportShow=false"
    />
  </div>
</template>

<script>
import { getList, addProjectInfo, updateProjectInfo, getProjectInfoById, delSupplyPoint, addBuilding, selectByProjectId, removeById } from '@/api/build/projectInfo'
import EditImage from '@/components/EditImage'
import Reminder from '@/components/Reminder'
import XrHeader from '@/components/XrHeader'
import CreateSections from '@/components/CreateSections'
import request from '@/utils/request'
import { XhUserCell } from '@/components/CreateCom'
import { selectAreaList } from '@/api/common'
import { crmFileSaveAPI } from '@/api/common'
import { validatePhone, validateIdNo, isNum, guid,sortColumn } from '@/utils'

import BulkImportUser from '@/components/importModel'

export default {
  name: 'projectInfo',
  components: {
    Reminder,
    XrHeader,
    XhUserCell,
    EditImage,
    CreateSections,
    BulkImportUser,

  },
  data() {
    var checkXS = (rule, value, callback) => {
      if (!value) {
        callback()
      } else {
        const reg = /^(([^0][0-9]+|0)\.([0-9]{1,2})$)|^(([^0][0-9]+|0)$)|^(([1-9]+)\.([0-9]{1,6})$)|^(([1-9]+)$)/
        if (reg.test(value)) {
          callback()
        } else {
          return callback(new Error('请输入正确数据类型(仅数字)'))
        }
      }
    }
     var checkLngAndLat = (rule, value, callback) => {
      if (!value) {
        callback()
      } else {
        const reg = /^(([^0][0-9]+|0)\.([0-9]{1,2})$)|^(([^0][0-9]+|0)$)|^(([1-9]+)\.([0-9]{1,6})$)|^(([1-9]+)$)/
        if (reg.test(value)) {
          callback()
        } else {
          return callback(new Error('请输入数字,最多保留6位小数)'))
        }
      }
    }
    var checkPhone = (rule, value, callback) => {
      if (!value) {
        callback()
      } else {
        const phoneReg = /^1[3|4|5|6|7|8][0-9]{9}$/
        if (phoneReg.test(value)) {
          callback()
        } else {
          return callback(new Error('电话号码格式不正确'))
        }
      }
    }
    var checkxmjl = (rule, value, callback) => {
      if (value != '[]'&&value != '') {
        callback()
      }else{
        return callback(new Error('请选择项目经理'))
      }
    }
    return {
    	tableHeight: document.documentElement.clientHeight - 310, // 表的高度   //搜索有两行时 -310
      param:'',//导入组件默认传参
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
      // 地图窗口
      windowShow: false,
      // 批量导入
      bulkImportShow: false,
      windowCenter: { lng: 0, lat: 0 },
      windowTitle: '',
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
      SingleBuildingType: [],
      projectTypeOptions: [],
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
      lzOpen: false,
      lzform: {},
      lzIndex: 0,
      projectId: null,
      // 表单参数
      form: {
        sort: undefined,
      },
      // 表单校验
      new_rules:{
        companyName: [
          { required: true, message: '必填项', trigger: 'blur' }
        ],
         projectName: [
          { required: true, message: '必填项', trigger: 'blur' }
        ],
        projectState: [
          { required: true, message: '必填项', trigger: 'blur' }
        ],
         constructionPermitCode: [
          { required: true, message: '必填项', trigger: 'blur' }
        ],
         totalCost: [
          { required: true, message: '必填项', trigger: 'blur' },
          { required: true, validator: checkLngAndLat, trigger: 'blur' }
        ],
         totalArea: [
          { required: true, message: '必填项', trigger: 'blur' },
          { required: true, validator: checkLngAndLat, trigger: 'blur' }
        ],
         actualCommencementDate: [
          { required: true, message: '必填项', trigger: 'blur' }
        ],
         plannedCompletionDate: [
          { required: true, message: '必填项', trigger: 'blur' }
        ],
         projectAddress: [
          { required: true, message: '必填项', trigger: 'blur' }
        ],
          lng: [
          { required: false, validator: checkLngAndLat, trigger: 'blur' }
        ],
          lat: [
          { required: false, validator: checkLngAndLat, trigger: 'blur' }
        ],
      },
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
          { validator: checkPhone, trigger: 'blur' }
        ],
        bidAmount: [
          { required: false, validator: checkXS, trigger: 'blur' }
        ],
        abovegroundArea: [
          { required: false, validator: checkXS, trigger: 'blur' }
        ],
        totalArea: [
          { required: false, validator: checkXS, trigger: 'blur' }
        ],
        totalCost: [
          { required: false, validator: checkXS, trigger: 'blur' }
        ],
        tenderArea: [
          { required: false, validator: checkXS, trigger: 'blur' }
        ],
        earthworkVolume: [
          { required: false, validator: checkXS, trigger: 'blur' }
        ],
        biddingProfitMargin: [
          { required: false, validator: checkXS, trigger: 'blur' }
        ],
        buildingEavesHeight: [
          { required: false, validator: checkXS, trigger: 'blur' }
        ],
        undergroundArea: [
          { required: false, validator: checkXS, trigger: 'blur' }
        ],
        totalLength: [
          { required: false, validator: checkXS, trigger: 'blur' }
        ],
        projectManager: [
          { required: true, validator: checkxmjl, trigger: 'blur' }
        ],
        laborLeaderTel: [
          { validator: checkPhone, trigger: 'blur' }
        ],
        projectManagerTel: [
          { validator: checkPhone, trigger: 'blur' }
        ],
        plannedCompletionDate: [
          { required: true, message: '必填项', trigger: 'blur' }
        ],

        supervisorCompany: [
          { required: true, message: '必填项', trigger: 'blur' }
        ],
        designCompany: [
          { required: true, message: '必填项', trigger: 'blur' }
        ],
        investigationCompany: [
          { required: true, message: '必填项', trigger: 'blur' }
        ],
        societyComplaintCall: [
          { validator: checkPhone, trigger: 'blur' }
        ],
        abuildComplaintCall: [
          { validator: checkPhone, trigger: 'blur' }
        ],
        buildComplaintCall: [
          { validator: checkPhone, trigger: 'blur' }
        ],
        asocietyComplaintCall: [
          { validator: checkPhone, trigger: 'blur' }
        ],

      },
      center: { lng: 0, lat: 0 },
      zoom: 3,
      systemImage: '',
      showEditImage: false,
      editImage: null,
      editFile: null,
      systemAddPlaneImage: '',
      showEditAddPlaneImage: false,
      editAddPlaneImage: null,
      editAddPlaneFile: null,
      projectManager: [],
      projectExecutiveManager: [],
      deputyProjectManager: [],
      businessManager: [],
      projectDepartmentChief: [],
      specialQualityOfficer: [],
      fullTimeSafetyOfficer: [],
      informationize: [],
      materialman: [],
      budgeter: [],
      constructionWorkers: [],
      costAccounting: [],
      laborAdministrator: [],
      laborLeader: []
    }
  },
  created() {
    this.getList()
    this.getDicts('b_new_engineering_status').then(response => {
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
  methods: {
  	sortChange(column) {
      sortColumn(this,column);
    },
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
    handler({ BMap, map }) {
      // console.log(BMap, map)
      this.center.lng = 116.806899
      this.center.lat = 37.657961
      this.zoom = 14
    },
    /** 查询字典类型列表 */
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize
      getList(this.queryParams).then(response => {
        this.typeList = response.data.list

        this.total = response.data.totalRow
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    getlzData() {
      selectByProjectId(this.projectId).then(response => {
        this.lzData = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    statisticalTypeFormat(row, column) {
      return this.selectDictLabel(this.statisticalTypeOptions, row.statisticalType)
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.open2 = false
      this.reset()
    },
    cancellz() {
      this.lzOpen = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.$nextTick(()=>{
        this.$refs['form'].clearValidate()//DOM加载完成之后重置表单校验
      })
      this.form = {}
      this.projectManager = []
      this.projectExecutiveManager= []
      this.deputyProjectManager = []
      this.businessManager =[]
      this.projectDepartmentChief = []
      this.specialQualityOfficer = []
      this.fullTimeSafetyOfficer= []
      this.informationize= []
      this.materialman= []
      this.budgeter= []
      this.constructionWorkers = []
      this.costAccounting= []
      this.laborAdministrator= []
      this.laborLeader= []
      this.resetForm('form')
      // 清空市、区区划数据
      this.cityOptions = []
      this.districtOptions = []
      // 获取省级区划
      this.getProvinceList()
      this.systemAddPlaneImage=  ''
      this.showEditAddPlaneImage=  false
      this.editAddPlaneImage=  null
      this.editAddPlaneFile=  null
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
      this.$forceUpdate()
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {

      this.open = true
      this.title = '新建项目信息'
      this.reset()
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.getProvinceList()
      const id = row.id || this.id
      this.projectId = row.id
      this.getlzData()
      getProjectInfoById(id).then(response => {
        this.form = response.data
        this.systemAddPlaneImage = response.data.planeImg
        if(response.data.planeCoordinate){
          var strs= new Array();
          strs = response.data.planeCoordinate.split(",");
          console.log(response.data.planeCoordinate)
          console.log(strs)
          this.form.planeCoordinateX = strs[0];
          this.form.planeCoordinateY = strs[1];
        }
        this.form.id = id
        this.form.isSmartBuild = response.data.isSmartBuild + ''
        this.open2 = true
        this.title = '修改项目信息'
        this.getCityList(response.data.province)
        this.getDistrictList(response.data.city)
        this.form.province = parseInt(response.data.province)
        this.form.city = parseInt(response.data.city)
        this.form.district = parseInt(response.data.district)

		//this.projectManager = JSON.parse(response.data.projectManager)==null?[]:JSON.parse(response.data.projectManager);


		this.projectExecutiveManager= JSON.parse(response.data.projectExecutiveManager)==null?[]:JSON.parse(response.data.projectExecutiveManager);
        this.deputyProjectManager = JSON.parse(response.data.deputyProjectManager)==null?[]:JSON.parse(response.data.deputyProjectManager);
        this.businessManager = JSON.parse(response.data.businessManager)==null?[]:JSON.parse(response.data.businessManager);
        this.projectDepartmentChief = JSON.parse(response.data.projectDepartmentChief)==null?[]:JSON.parse(response.data.projectDepartmentChief);
        this.specialQualityOfficer = JSON.parse(response.data.specialQualityOfficer)==null?[]:JSON.parse(response.data.specialQualityOfficer);
        this.fullTimeSafetyOfficer= JSON.parse(response.data.fullTimeSafetyOfficer)==null?[]:JSON.parse(response.data.fullTimeSafetyOfficer);
        this.informationize= JSON.parse(response.data.informationize)==null?[]:JSON.parse(response.data.informationize);
        this.materialman= JSON.parse(response.data.materialman)==null?[]:JSON.parse(response.data.materialman);
        this.budgeter= JSON.parse(response.data.budgeter)==null?[]:JSON.parse(response.data.budgeter);
        this.constructionWorkers = JSON.parse(response.data.constructionWorkers)==null?[]:JSON.parse(response.data.constructionWorkers);
        this.costAccounting= JSON.parse(response.data.costAccounting)==null?[]:JSON.parse(response.data.costAccounting);
        this.laborAdministrator= JSON.parse(response.data.laborAdministrator)==null?[]:JSON.parse(response.data.laborAdministrator);
        this.laborLeader= JSON.parse(response.data.laborLeader)==null?[]:JSON.parse(response.data.laborLeader);
      })
    },
     handleUpdate2(row) {
      const id = row.id || this.id
      this.projectId = row.id

      getProjectInfoById(id).then(response => {
        this.form = response.data
        this.systemAddPlaneImage = response.data.planeImg
        if(response.data.planeCoordinate){
          var strs= new Array();
          strs = response.data.planeCoordinate.split(",");
          console.log(response.data.planeCoordinate)
          console.log(strs)
          this.form.planeCoordinateX = strs[0];
          this.form.planeCoordinateY = strs[1];
        }
        this.form.id = id
        this.open = true
        this.title = '修改项目信息'
      })
    },
     /** 提交按钮 */
    submitForm2: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            console.log(this.editedAddPlaneImage)
            if (this.editedAddPlaneImage != null) {
              var batchId = guid()
              var data = { type: 'img', batchId: batchId, file: this.editedAddPlaneImage.blob }
              crmFileSaveAPI(data).then(response => {
                if (response.code === 0) {
                  this.form.planeImg = response.data.url
                  if(this.form.planeCoordinateX!=''&&this.form.planeCoordinateX!=undefined){
                    if(this.form.planeCoordinateY!=''&&this.form.planeCoordinateY!=undefined){
                      this.form.planeCoordinate = this.form.planeCoordinateX+","+this.form.planeCoordinateY
                    }else{
                      this.form.planeCoordinate = this.form.planeCoordinateX+","+''
                    }
                  }else if(this.form.planeCoordinateY!=''&&this.form.planeCoordinateY!=undefined){
                    this.form.planeCoordinate = ''+","+this.form.planeCoordinateY
                  }
                  updateProjectInfo(this.form).then(response => {
                    if (response.code === 0) {
                      this.msgSuccess('修改成功')
                      this.open2 = false
                      this.getList()
                    }
                  })
                }
              })
            } else {
              if(this.form.planeCoordinateX!=''&&this.form.planeCoordinateX!=undefined){
                if(this.form.planeCoordinateY!=''&&this.form.planeCoordinateY!=undefined){
                  this.form.planeCoordinate = this.form.planeCoordinateX+","+this.form.planeCoordinateY
                }else{
                  this.form.planeCoordinate = this.form.planeCoordinateX+","+''
                }
              }else if(this.form.planeCoordinateY!=''&&this.form.planeCoordinateY!=undefined){
                this.form.planeCoordinate = ''+","+this.form.planeCoordinateY
              }
              updateProjectInfo(this.form).then(response => {
                if (response.code === 0) {
                  this.msgSuccess('修改成功')
                  this.open = false
                  this.getList()
                }
              })
            }
          } else {
            if (this.editedAddPlaneImage != null) {
              var batchId = guid()
              var data = { type: 'img', batchId: batchId, file: this.editedAddPlaneImage.blob }
              crmFileSaveAPI(data).then(response => {
                if (response.code === 0) {
                  this.form.planeImg = response.data.url
                  if(this.form.planeCoordinateX!=''&&this.form.planeCoordinateX!=undefined){
                    if(this.form.planeCoordinateY!=''&&this.form.planeCoordinateY!=undefined){
                      this.form.planeCoordinate = this.form.planeCoordinateX+","+this.form.planeCoordinateY
                    }else{
                      this.form.planeCoordinate = this.form.planeCoordinateX+","+''
                    }
                  }else if(this.form.planeCoordinateY!=''&&this.form.planeCoordinateY!=undefined){
                    this.form.planeCoordinate = ''+","+this.form.planeCoordinateY
                  }
                  addProjectInfo(this.form).then(response => {
                    if (response.code === 0) {
                      this.msgSuccess('新增成功')
                      this.open = false
                      this.getList()
                    }
                  })
                }
              })
            }else {
              if(this.form.planeCoordinateX!=''&&this.form.planeCoordinateX!=undefined){
                if(this.form.planeCoordinateY!=''&&this.form.planeCoordinateY!=undefined){
                  this.form.planeCoordinate = this.form.planeCoordinateX+","+this.form.planeCoordinateY
                }else{
                  this.form.planeCoordinate = this.form.planeCoordinateX+","+''
                }
              }else if(this.form.planeCoordinateY!=''&&this.form.planeCoordinateY!=undefined){
                this.form.planeCoordinate = ''+","+this.form.planeCoordinateY
              }
              addProjectInfo(this.form).then(response => {
                if (response.code === 0) {
                  this.msgSuccess('新增成功')
                  this.open = false
                  this.getList()
                }
              })
            }

          }
        }
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      alert(111)
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            console.log(this.editedAddPlaneImage)
            if (this.editedAddPlaneImage != null) {
              var batchId = guid()
              var data = { type: 'img', batchId: batchId, file: this.editedAddPlaneImage.blob }
              crmFileSaveAPI(data).then(response => {
                if (response.code === 0) {
                  this.form.planeImg = response.data.url
                  if(this.form.planeCoordinateX!=''&&this.form.planeCoordinateX!=undefined){
                    if(this.form.planeCoordinateY!=''&&this.form.planeCoordinateY!=undefined){
                      this.form.planeCoordinate = this.form.planeCoordinateX+","+this.form.planeCoordinateY
                    }else{
                      this.form.planeCoordinate = this.form.planeCoordinateX+","+''
                    }
                  }else if(this.form.planeCoordinateY!=''&&this.form.planeCoordinateY!=undefined){
                    this.form.planeCoordinate = ''+","+this.form.planeCoordinateY
                  }
                  updateProjectInfo(this.form).then(response => {
                    if (response.code === 0) {
                      this.msgSuccess('修改成功')
                      this.open = false
                      this.getList()
                    }
                  })
                }
              })
            } else {
              if(this.form.planeCoordinateX!=''&&this.form.planeCoordinateX!=undefined){
                if(this.form.planeCoordinateY!=''&&this.form.planeCoordinateY!=undefined){
                  this.form.planeCoordinate = this.form.planeCoordinateX+","+this.form.planeCoordinateY
                }else{
                  this.form.planeCoordinate = this.form.planeCoordinateX+","+''
                }
              }else if(this.form.planeCoordinateY!=''&&this.form.planeCoordinateY!=undefined){
                this.form.planeCoordinate = ''+","+this.form.planeCoordinateY
              }
              updateProjectInfo(this.form).then(response => {
                if (response.code === 0) {
                  this.msgSuccess('修改成功')
                  this.open = false
                  this.getList()
                }
              })
            }
          } else {
            if (this.editedAddPlaneImage != null) {
              var batchId = guid()
              var data = { type: 'img', batchId: batchId, file: this.editedAddPlaneImage.blob }
              crmFileSaveAPI(data).then(response => {
                if (response.code === 0) {
                  this.form.planeImg = response.data.url
                  if(this.form.planeCoordinateX!=''&&this.form.planeCoordinateX!=undefined){
                    if(this.form.planeCoordinateY!=''&&this.form.planeCoordinateY!=undefined){
                      this.form.planeCoordinate = this.form.planeCoordinateX+","+this.form.planeCoordinateY
                    }else{
                      this.form.planeCoordinate = this.form.planeCoordinateX+","+''
                    }
                  }else if(this.form.planeCoordinateY!=''&&this.form.planeCoordinateY!=undefined){
                    this.form.planeCoordinate = ''+","+this.form.planeCoordinateY
                  }
                  addProjectInfo(this.form).then(response => {
                    if (response.code === 0) {
                      this.msgSuccess('新增成功')
                      this.open = false
                      this.getList()
                    }
                  })
                }
              })
            }else {
              this.msgSuccess('请上传项目平面图')
            }

          }
        }
      })
    },
    // 添加楼座
    submitLzForm: function() {
      this.lzform.parentId = this.projectId
      addBuilding(this.lzform).then(response => {
        if (response.code === 0) {
          this.msgSuccess('添加成功')
          this.lzOpen = false
          this.getlzData()
        }
      })
    },
    // 删除楼座
    handleDelete(data) {

      removeById(data.id).then(response => {
        this.getlzData()
      }).catch(() => {

      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/dict/type/export', {
        ...this.queryParams
      }, `type_${new Date().getTime()}.xlsx`)
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
      this.windowShow = true
      this.windowCenter = { lng: item.lng, lat: item.lat }
      this.projectAddress = item.projectAddress
      this.projectKGRQ = item.contractCommencementDate
      this.projectJGRQ = item.contractCompletionDate
      this.windowTitle = item.projectName
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
    deleteSystemImage() {
      this.systemImage = ''
      this.editedImage = null
    },
    submiteImage(data) {
      this.editedImage = data
      this.systemImage = data.image
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
    fileAddPlaneUpload(content) {
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
        self.editAddPlaneImage = result
        self.editAddPlaneFile = content.file
        self.showEditAddPlaneImage = true
      }
      reader.readAsDataURL(content.file)
    },
    deleteSystemAddPlaneImage() {
      this.systemAddPlaneImage = ''
      this.editedAddPlaneImage = null
    },
    submiteAddPlaneImage(data) {
      this.editedAddPlaneImage = data
      this.systemAddPlaneImage = data.image
    },
    areaChangeProvince(d) {
      this.form.city = ''
      this.form.district = ''
      this.getCityList(d)
    },
    areaChangeCity(d) {
      this.form.district = ''
      this.getDistrictList(d)
      this.$forceUpdate()
    },
    areaChangeDistrict(d) {
      this.$forceUpdate()
    },
    addLz() {
      this.lzform = {}
      this.lzOpen = true
    },
    projectManagerCha(data) {
	  this.form.projectManagerList = data.value
      this.form.projectManager = this.form.projectManagerList
		.map(item => item.userId).join(',')
    },
    projectExecutiveManagerCha(data) {
      this.form.projectExecutiveManager = JSON.stringify(data.value);
    },
    deputyProjectManagerCha(data) {
      this.form.deputyProjectManager = JSON.stringify(data.value);
    },
    businessManagerCha(data) {
      this.form.businessManager = JSON.stringify(data.value);
    },
    projectDepartmentChiefCha(data) {
      this.form.projectDepartmentChief = JSON.stringify(data.value);
    },
    specialQualityOfficerCha(data) {
      this.form.specialQualityOfficer = JSON.stringify(data.value);
    },
    fullTimeSafetyOfficerCha(data) {
      this.form.fullTimeSafetyOfficer = JSON.stringify(data.value);
    },
    informationizeCha(data) {
      this.form.informationize = JSON.stringify(data.value);
    },
    materialmanCha(data) {
      this.form.materialman = JSON.stringify(data.value);
    },
    budgeterCha(data) {
      this.form.budgeter = JSON.stringify(data.value);
    },
    constructionWorkersCha(data) {
      this.form.constructionWorkers = JSON.stringify(data.value);
    },
    costAccountingCha(data) {
      this.form.costAccounting = JSON.stringify(data.value);
    },
    laborAdministratorCha(data) {
      this.form.laborAdministrator = JSON.stringify(data.value);
    },
    laborLeaderCha(data) {
      this.form.laborLeader = JSON.stringify(data.value);
    },
    /**
     * 批量导入
     */
    bulkImportClick() {
      let lists = [
        "施工单位名称(*)",
        "项目名称(*)",
        "施工许可证编号(*)",
        "工程造价(万元)(*)",
        "工程面积(平)(*)",
        "实际开工日期(*)",
        "计划竣工时间(*)",
        "项目地址",
        "经度",
        "纬度"
      ]
      //示例数据
      let dataLists = [
        "中建八局四公司",
        "【中建】36号地铁一期",
        "330825202004290101",
        "10200",
        "20560",
        "2021-04-05",
        "2022-04-05",
        "山东省德州市宁津县",
        "35.000000",
        "126.000000"
      ]

      this.param = {
        title:"项目导入模板(*)为必填项 ",
        excelTitle:"项目导入模板",
        colNames:lists,
        sampleData:dataLists,
        url:'build/projectInfo/excelImport'
      }
      this.bulkImportShow = true
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
    height: 500px;
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    margin: auto;
    margin-top: auto!important;
  }

  /deep/ .el-dialog .el-dialog__body{
    height: 400px;
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
  /deep/.el-form .vux-flexbox{
    width: 220px;
  }
  .add-form-w100  .el-input{
    width: 500px;
  }
</style>
