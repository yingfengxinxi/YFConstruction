<template>
  <div class="employee-dep-management">
    <!-- <xr-header
      :content.sync="searchInput"
      placeholder="请输入设备名称/编号"
      :show-search="true"
      icon-class="wk wk-s-seas"
      icon-color="#26D4DA"
      label="设备管理"
      @search="getTableList">

    </xr-header> -->
    <div class="system-content">
      <!-- 左边导航栏 -->
      <!--<div-->
        <!--v-loading="treeLoading"-->
        <!--class="system-nav">-->
        <!--<div class="system-nav__title">-->
          <!--类型信息-->
        <!--</div>-->
        <!--<div class="system-nav__content">-->
          <!--<div class="section">-->
            <!--<div class="section__content">-->
              <!--<el-tree-->
                <!--ref="tree"-->
                <!--:data="showTreeTableData"-->
                <!--node-key="id"-->
                <!--highlight-current-->
                <!--default-expand-all-->
                <!--@node-click="changeTreeClick">-->
                <!--<flexbox-->
                  <!--slot-scope="{ node }"-->
                  <!--:class="{ 'is-current': node.isCurrent}"-->
                  <!--class="node-data">-->
                  <!--<i-->
                    <!--v-if="node.level == 1"-->
                    <!--class="wk wk-department"/>-->
                  <!--<span-->
                    <!--v-else-->
                    <!--class="node-data__mark"/>-->

                  <!--<div class="node-data__label text-one-line ">{{ node.label }}</div>-->
                  <!--<i-->
                    <!--v-if="node.childNodes && node.childNodes.length"-->
                    <!--:class="{ 'is-close': !node.expanded }"-->
                    <!--class="wk wk-up-unfold"/>-->
                <!--</flexbox>-->
              <!--</el-tree>-->
            <!--</div>-->
          <!--</div>-->

        <!--</div>-->
      <!--</div>-->
      <!-- 右边内容 -->
      <div class="system-view-table flex-index" v-loading="formLoading">
        <flexbox
          justify="space-between"
          class="table-top">
          <div style="margin-right: 0px;margin-left: auto">
            <!--            <el-button-->
            <!--              type="text"-->
            <!--              icon="el-icon-circle-plus"-->
            <!--              @click="add()">新建-->
            <!--            </el-button>-->
            <el-button
              class="main-table-header-button xr-btn--orange"
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="add()">新建
            </el-button>
          </div>
        </flexbox>

        <div class="flex-box">
          <el-table
            :data="tableData"
            :height="tableHeight"
            style="width: 100%" highlight-current-row @sort-change="sortChange">
            <el-table-column label="序号" type="index" show-overflow-tooltip width="70" align="center">
            </el-table-column>
            <el-table-column
              prop="assetName"
              label="设备名称" sortable="custom">
              <template slot-scope="scope">
                <div class="status-name" @click="detail(scope.row)">
                  <div :style="{'background-color' : getStatusColor(scope.row.status)}"/>
                  {{ scope.row.assetName }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              prop="assetCode"
              label="设备编号"
              width="180" sortable="custom">
            </el-table-column>
            <el-table-column
              prop="locationName"
              label="设备位置"
              width="240" sortable="custom">
            </el-table-column>
            <el-table-column
              prop="deptName"
              label="负责部门"
              width="220" sortable="custom">
            </el-table-column>
            <el-table-column
              prop="typeName"
              label="设备类型"
              width="180" sortable="custom">
            </el-table-column>
            <el-table-column
              prop="sort"
              label="排序"
              width="120" sortable="custom">
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              width="240" align="center">
              <template slot-scope="scope">
                <el-button @click="edit(scope.row)" type="text" size="small" icon="el-icon-edit">编辑</el-button>
                <el-button @click="config(scope.row)" type="text" size="small" icon="el-icon-edit">配置</el-button>
                <el-button @click="del(scope.row)" icon="el-icon-delete-solid" type="text" size="small"
                >删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="p-contianer">
            <div class="status-des">
              <div
                :key="1"
                class="status-des-item">
                <div :style="{'background-color' :'#46CDCF'}"/>
                正常
              </div>
              <div
                :key="0"
                class="status-des-item">
                <div :style="{'background-color' :'#FF6767'}"/>
                报废
              </div>
            </div>
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
        </div>
      </div>
    </div>
    <!-- 新建和编辑 -->
    <el-dialog
      v-loading="formLoading"
      :visible.sync="formDialog"
      :title="dialogTitle"
      :close-on-click-modal="false"
      :modal-append-to-body="true"
      :append-to-body="true"
      :before-close="newHandleClose"
      width="60%"

    >
      <el-form
        ref="equform"
        :inline="true"
        :model="form"
        :rules="rules"
        class="new-dialog-form"
        label-width="120px"
        label-position="left"

      >

        <el-row style="border-bottom: 1px solid #e6e6e6;padding-bottom: 10px;margin-bottom: 20px;">
          <span>基本信息</span>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备位置" prop="locationId">
              <select-tree :options="showLocationTreeData" v-model="form.locationId" :props="defaultProps"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备类型" prop="typeId">
              <select-tree :options="showTreeData" :disabledT='true' v-model="form.typeId" :props="defaultProps"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备编号" prop="assetCode">
              <el-input v-model="form.assetCode" placeholder="请输入设备编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备名称" prop="assetName">
              <el-input v-model="form.assetName" placeholder="请输入设备名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="负责部门" prop="deptId">
              <select-tree :options="showDeptTreeData" v-model="form.deptId" :props="defaultProps"
                           :expandAll="true"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备品牌" prop="brand">
              <el-input v-model="form.brand" placeholder="请输入设备品牌"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备规格" prop="specification">
              <el-input v-model="form.specification" placeholder="请输入设备规格"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备型号" prop="model">
              <el-input v-model="form.model" placeholder="请输入设备型号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{ dict.dictLabel }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序号" prop="sort">
              <el-input v-model="form.sort" type="number" placeholder="请输入排序号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="相对位置横向" prop="planeCoordinateX">
              <el-input v-model="form.planeCoordinateX"  type="number" placeholder="请输入相对位置"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="相对位置纵向" prop="planeCoordinateY">
              <el-input v-model="form.planeCoordinateY"  type="number" placeholder="请输入相对位置"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="备注说明">
              <el-input type="textarea"
                        :rows="2" v-model="form.remarks" placeholder="请输入备注说明"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备照片">
              <div class="img-accessory">
                <div class="img-box">
                  <el-upload
                    ref="imageUpload"
                    :action="crmFileSaveUrl"
                    :headers="httpHeader"
                    :data="{type: 'img', batchId: form.batchId}"
                    :on-preview="handleFilePreview"
                    :before-remove="beforeRemove"
                    :on-success="imgFileUploadSuccess"
                    :file-list="imgFileList"
                    name="file"
                    multiple
                    accept="image/*"
                    list-type="picture-card">
                    <p class="add-img">
                      <span class="el-icon-picture"/>
                      <span>图片上传</span>
                    </p>
                    <i class="el-icon-plus"/>
                  </el-upload>
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="border-bottom: 1px solid #e6e6e6;padding-bottom: 10px;margin-bottom: 20px;">
          <span>数据接收配置</span>
        </el-row>
        <el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="对接平台" prop="platform">
                <el-select v-model="form.platform" placeholder="请选择对接平台">
                  <el-option
                    v-for="item in platformDict"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-col :span="24">
            <el-form-item label="数据解析器" prop="dataResolver">
              <el-input v-model="form.dataResolver" placeholder=""></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="border-bottom: 1px solid #e6e6e6;padding-bottom: 10px;margin-bottom: 20px;">
          <span>其他信息</span>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="重要程度">
              <el-radio-group v-model="form.level">
                <el-radio
                  v-for="dict in levelOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{ dict.dictLabel }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="生产厂家">
              <el-input v-model="form.factory" placeholder="请输入生产厂家"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出厂日期">
              <el-date-picker
                v-model="form.productionDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择出厂日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="供货商">
              <el-input v-model="form.supplier" placeholder="请输入供货商"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供货日期">
              <el-date-picker
                v-model="form.supplyDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择供货日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="验收日期">
              <el-date-picker
                v-model="form.acceptanceDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择验收日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安装日期">
              <el-date-picker
                v-model="form.installDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择安装日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="启动日期">
              <el-date-picker
                v-model="form.startaDte"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择启动日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报废预期">
              <el-date-picker
                v-model="form.scrapPre"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择报废预期日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="保修期限（年）">
              <el-input type="number" v-model="form.serviceTerm" placeholder="请输入保修期限"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="报废日期">
              <el-date-picker
                v-model="form.scrapDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择报废日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保修截止日期">
              <el-date-picker
                v-model="form.serviceDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择保修截止日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备原值（万元）">
              <el-input v-model="form.original" placeholder="请输入设备原值"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估价值（万元）">
              <el-input v-model="form.originalPre" placeholder="请输入评估价值"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="设备所属单位">
              <el-input v-model="form.assetBelong" placeholder="请输入设备所属单位"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span
        slot="footer"
        class="dialog-footer">
        <el-button
          type="primary"
          @click="save">保 存</el-button>
        <el-button @click="newHandleClose">取 消</el-button>
      </span>
    </el-dialog>
    <!--配置-->
    <el-dialog
      v-loading="formLoading"
      :visible.sync="formConfig"
      :title="dialogTitle"
      :close-on-click-modal="false"
      :modal-append-to-body="true"
      :append-to-body="true"
      :before-close="newHandleClose"
      width="60%"

    >
      <el-form
        ref="configform"
        :inline="true"
        :model="form"
        :rules="rules"
        class="new-dialog-form"
        label-width="120px"
        label-position="left"

      >

        <el-row style="border-bottom: 1px solid #e6e6e6;padding-bottom: 10px;margin-bottom: 20px;">
          <span>基本信息</span>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="制造许可编号" prop="manufacturingLicense">
              <el-input v-model="form.manufacturingLicense" placeholder="请输入制造许可编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格型号" prop="model">
              <el-input v-model="form.model" placeholder="请输入规格型号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备备案编号" prop="recordNumber">
              <el-input v-model="form.recordNumber" placeholder="请输入设备备案编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="制造厂家" prop="manufacturer">
              <el-input v-model="form.manufacturer" placeholder="请输入制造厂家"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="出厂日期" prop="manufactureDate">
              <el-date-picker
                v-model="form.manufactureDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择出厂日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出厂编号" prop="manufacturingNo">
              <el-input v-model="form.manufacturingNo" placeholder="请输入出厂编号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactNumber">
              <el-input v-model="form.contactNumber" placeholder="请输入联系电话"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安装单位" prop="installationUnit">
              <el-input v-model="form.installationUnit" placeholder="请输入安装单位"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="检验合格日期" prop="verifyDate">
              <el-date-picker
                v-model="form.verifyDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择检验合格日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="使用登记日期" prop="recordDate">
              <el-date-picker
                v-model="form.recordDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择使用登记日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="border-bottom: 1px solid #e6e6e6;padding-bottom: 10px;margin-bottom: 20px;">
          <span>设备信息</span>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="前向臂高（米）" prop="foreArmLength">
              <el-input v-model="form.foreArmLength" min="0" type="number" max="99.99" placeholder="请输入前向臂高"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="后向臂高（米）" prop="rearArmLength">
              <el-input v-model="form.rearArmLength" min="0" type="number" max="99.99" placeholder="请输入后向臂高"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="额定吊重（吨）" prop="maxLoad">
              <el-input v-model="form.maxLoad" min="0" type="number" max="99.99" placeholder="请输入额定吊重"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大吊重（吨）" prop="maxWeight">
              <el-input v-model="form.maxWeight" min="0" type="number" max="99.99" placeholder="请输入最大吊重"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="额定力矩" prop="ratedTorque">
              <el-input v-model="form.ratedTorque" min="0" type="number" max="99.99" placeholder="请输入额定力矩"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="额定风速（m/s）" prop="ratedWindSpeed">
              <el-input v-model="form.ratedWindSpeed" min="0" type="number" max="36.90" placeholder="请输入额定风速"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备编号" prop="deviceNumber">
              <el-input v-model="form.deviceNumber" placeholder="请输入设备编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="拆除单位" prop="dismantleUnit">
              <el-input v-model="form.dismantleUnit" placeholder="请输入拆除单位"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="塔吊设备类型" prop="customTowerCraneType">
              <el-select v-model="form.customTowerCraneType" placeholder="请选择">
                <el-option label="尖头塔" value="1"></el-option>
                <el-option label="平头塔" value="2"></el-option>
                <el-option label="动臂塔" value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监测机构" prop="detectionUnit">
              <el-input v-model="form.detectionUnit" placeholder="请输入监测机构"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="SIM卡号" prop="simCardNo">
              <el-input v-model="form.simCardNo" placeholder="请输入SIM卡号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="硬件版本号" prop="hardwareVer">
              <el-input v-model="form.hardwareVer" placeholder="请输入硬件版本号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="协议版本号" prop="protocolVer">
              <el-input v-model="form.protocolVer" placeholder="请输入协议版本号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="塔机名称编号" prop="selfCraneNo">
              <el-input v-model="form.selfCraneNo" placeholder="请输入塔机名称编号1"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="塔机类型" prop="craneType">
              <el-select v-model="form.craneType" placeholder="请选择">
                <el-option label="动臂吊" value="1"></el-option>
                <el-option label="塔头平臂吊" value="2"></el-option>
                <el-option label="平头平臂吊" value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="border-bottom: 1px solid #e6e6e6;padding-bottom: 10px;margin-bottom: 20px;">
          <span>配置信息</span>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="配置载重功能">
              <el-select style="width: 100%;" v-model="form.weightSet" placeholder="请选择">
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
              <el-select style="width: 100%;" v-model="form.windSpeedSet" placeholder="请选择">
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
              <el-select style="width: 100%;" v-model="form.rangeSet" placeholder="请选择">
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
              <el-select style="width: 100%;" v-model="form.heightSet" placeholder="请选择">
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
              <el-select style="width: 100%;" v-model="form.angleSet" placeholder="请选择">
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
              <el-select style="width: 100%;" v-model="form.obliguitySet" placeholder="请选择">
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
              <el-select style="width: 100%;" v-model="form.gpsSet" placeholder="请选择">
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
              <el-select style="width: 100%;" v-model="form.idSet" placeholder="请选择">
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
              <el-input v-model="form.ratedWindLevel" min="0" type="number" max="12" placeholder="请输入额定风级"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最小变幅" prop="minRange">
              <el-input v-model="form.minRange" min="0" type="number" max="36.90" placeholder="请输入最小变幅"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="大臂高" prop="armHeight">
              <el-input v-model="form.armHeight" min="0" type="number" max="655" placeholder="请输入大臂高"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大高度" prop="maxHeight">
              <el-input v-model="form.maxHeight" min="0" type="number" max="655" placeholder="请输入最大高度"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="最大角度" prop="maxAngle">
              <el-input v-model="form.maxAngle"  type="number" max="540" placeholder="请输入最大角度"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最小角度" prop="minAngle">
              <el-input v-model="form.minAngle" min="-540" type="number" max="540" placeholder="请输入最小角度"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="额定倾角" prop="ratedObliguity">
              <el-input v-model="form.ratedObliguity" min="0" type="number" max="9.99" placeholder="请输入额定倾角"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="最大幅度允许最大载重" prop="tcMsLoadCapacity">
              <el-input v-model="form.tcMsLoadCapacity" min="0" type="number" max="99" placeholder="请输入最大幅度允许最大载重"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大载重达到最大幅度" prop="tcMlMaxScope">
              <el-input v-model="form.tcMlMaxScope" min="0" type="number" max="655" placeholder="请输入最大载重达到最大幅度"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="载重滤波等级" prop="differInput">
              <el-input v-model="form.differInput" min="0" type="number" max="9" placeholder="请输入载重滤波等级"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="高度报警阈值" prop="settingMinHeight">
              <el-input v-model="form.settingMinHeight" min="0" type="number" max="99" placeholder="请输入高度报警阈值"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="载重零点值" prop="weightZero">
              <el-input v-model="form.weightZero" min="0" type="number" max="99.99" placeholder="请输入载重零点"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风速零点值" prop="windSpeedZero">
              <el-input v-model="form.windSpeedZero" min="0" type="number" max="99" placeholder="请输入高度报警阈值"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="角度零点值" prop="angleZero">
              <el-input v-model="form.angleZero" min="0" type="number" max="99.99" placeholder="请输入角度零点值"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="倾角X零点值" prop="dipxZero">
              <el-input v-model="form.dipxZero" min="0" type="number" max="9.99" placeholder="请输入倾角X零点值"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="倾角Y零点值" prop="dipyZero">
              <el-input v-model="form.dipyZero" min="0" type="number" max="9.99" placeholder="请输入角度零点值"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="动臂吊仰角零点值" prop="elevationAngleZero">
              <el-input v-model="form.elevationAngleZero" min="0" type="number" max="99" placeholder="请输入倾角X零点值"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="经度" prop="lng">
              <el-input v-model="form.lng" placeholder="请输入经度"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度" prop="lat">
              <el-input v-model="form.lat" placeholder="请输入纬度"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span
        slot="footer"
        class="dialog-footer">
        <el-button
          type="primary"
          @click="saveConfig">保 存</el-button>
        <el-button @click="newHandleClose">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 新建和编辑 -->
    <el-dialog
      v-loading="formDetailLoading"
      :visible.sync="formDetailDialog"
      title="设备详情"
      :close-on-click-modal="false"
      :modal-append-to-body="true"
      :append-to-body="true"
      :before-close="newHandleClose"
      width="60%"
    >
      <el-tabs v-model="activeName">
        <el-tab-pane label="设备信息" name="asset">
          <el-form
            ref="form"
            :inline="true"
            :model="form"
            class="new-dialog-form"
            label-width="120px"
            label-position="left">

            <el-row style="border-bottom: 1px solid #e6e6e6;padding-bottom: 10px;margin-bottom: 20px;">
              <span>基本信息</span>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="设备位置" prop="locationId">
                  <select-tree disabled :options="showLocationTreeData" v-model="form.locationId"
                               :props="defaultProps"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备类型" prop="typeId">
                  <select-tree :disabledT="true" :options="showTreeData" v-model="form.typeId" :props="defaultProps"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="设备编号" prop="assetCode">
                  <el-input disabled v-model="form.assetCode" placeholder="请输入设备编号"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备名称" prop="assetName">
                  <el-input disabled v-model="form.assetName" placeholder="请输入设备名称"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="负责部门" prop="deptId">
                  <select-tree disabled :options="showDeptTreeData" v-model="form.deptId" :props="defaultProps"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备品牌" prop="brand">
                  <el-input disabled v-model="form.brand" placeholder="请输入设备品牌"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="设备规格" prop="specification">
                  <el-input disabled v-model="form.specification" placeholder="请输入设备规格"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备型号" prop="model">
                  <el-input disabled v-model="form.model" placeholder="请输入设备型号"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="设备状态" prop="status">
                  <el-radio-group disabled v-model="form.status">
                    <el-radio
                      v-for="dict in statusOptions"
                      :key="dict.dictValue"
                      :label="dict.dictValue"
                    >{{ dict.dictLabel }}
                    </el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="排序号" prop="sort">
                  <el-input disabled v-model="form.sort" type="number" placeholder="请输入排序号"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="相对位置横向" prop="planeCoordinateX">
                  <el-input v-model="form.planeCoordinateX"  type="number" placeholder="请输入相对位置"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="相对位置纵向" prop="planeCoordinateY">
                  <el-input v-model="form.planeCoordinateY"  type="number" placeholder="请输入相对位置"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="备注说明">
                  <el-input disabled type="textarea"
                            :rows="2" v-model="form.remarks" placeholder="请输入备注说明"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备照片">
                  <div class="img-accessory">
                    <div class="img-box">
                      <el-upload disabled
                                 ref="imageUpload"
                                 :action="crmFileSaveUrl"
                                 :headers="httpHeader"
                                 :data="{type: 'img', batchId: form.batchId}"
                                 :on-preview="handleFilePreview"
                                 :before-remove="beforeRemove"
                                 :on-success="imgFileUploadSuccess"
                                 :file-list="imgFileList"
                                 name="file"
                                 multiple
                                 accept="image/*"
                                 list-type="picture-card">
                        <p class="add-img">
                          <span class="el-icon-picture"/>
                          <span>图片上传</span>
                        </p>
                        <i class="el-icon-plus"/>
                      </el-upload>
                    </div>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row style="border-bottom: 1px solid #e6e6e6;padding-bottom: 10px;margin-bottom: 20px;">
              <span>数据接收配置</span>
            </el-row>
            <el-row>

              <el-col :span="24">
                <el-form-item label="数据解析器" prop="dataResolver">
                  <el-input disabled v-model="form.dataResolver" placeholder="支持通配符，多个IP使用英文逗号间隔"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row style="border-bottom: 1px solid #e6e6e6;padding-bottom: 10px;margin-bottom: 20px;">
              <span>其他信息</span>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="重要程度">
                  <el-radio-group disabled v-model="form.level">
                    <el-radio
                      v-for="dict in levelOptions"
                      :key="dict.dictValue"
                      :label="dict.dictValue"
                    >{{ dict.dictLabel }}
                    </el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="生产厂家">
                  <el-input disabled v-model="form.factory" placeholder="请输入生产厂家"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="出厂日期">
                  <el-date-picker disabled
                                  v-model="form.productionDate"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="请选择出厂日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="供货商">
                  <el-input disabled v-model="form.supplier" placeholder="请输入供货商"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="供货日期">
                  <el-date-picker disabled
                                  v-model="form.supplyDate"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="请选择供货日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="验收日期">
                  <el-date-picker disabled
                                  v-model="form.acceptanceDate"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="请选择验收日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="安装日期">
                  <el-date-picker disabled
                                  v-model="form.installDate"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="请选择安装日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="启动日期">
                  <el-date-picker disabled
                                  v-model="form.startaDte"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="请选择启动日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报废预期">
                  <el-date-picker disabled
                                  v-model="form.scrapPre"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="请选择报废预期日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="保修期限（年）">
                  <el-input disabled type="number" v-model="form.serviceTerm" placeholder="请输入保修期限"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="报废日期">
                  <el-date-picker disabled
                                  v-model="form.scrapDate"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="请选择报废日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="保修截止日期">
                  <el-date-picker disabled
                                  v-model="form.serviceDate"
                                  type="date"
                                  value-format="yyyy-MM-dd"
                                  placeholder="请选择保修截止日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="设备原值（万元）">
                  <el-input disabled v-model="form.original" placeholder="请输入设备原值"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="评估价值（万元）">
                  <el-input disabled v-model="form.originalPre" placeholder="请输入评估价值"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="设备所属单位">
                  <el-input disabled v-model="form.assetBelong" placeholder="请输入设备所属单位"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="维护记录" name="set">
          <set-detail :assetId="form.assetId"></set-detail>
        </el-tab-pane>
      </el-tabs>

      <span
        slot="footer"
        class="dialog-footer">
        <el-button @click="newHandleClose">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import XrHeader from '@/components/XrHeader'
  import SelectTree from '@/components/SelectTree.vue';

  import SetDetail from '../set/indexDetail'

  import {
    typeList,
  } from '../api/type'

  import {
    locationList,
  } from '../api/location'

  import {depListAPI} from '@/api/common' // 直属上级接口

  import {
    assetSave,
    assetsList,
    assetDelete,
    assetGet,
    getTowerConfig,
    savTowerConfig
  } from '../api/assets'
  import {getList } from "../api/platform"
  //附件
  import {guid, objDeepCopy, getImageData} from '@/utils'
  import {crmFileDeleteAPI, crmFileSaveUrl} from '@/api/common'
  import {adminFileQueryFileListAPI} from '@/api/admin/file'
  import axios from 'axios'
  import { sortColumn } from '@/utils/index';

  export default {
    /** 系统管理 的 员工部门管理 */
    name: 'towerCraneConfig',
    components: {
      XrHeader,
      SelectTree,
      SetDetail,
    },
    data() {
      return {
        configId:'',
        deviceId:'',
        tableHeight: document.documentElement.clientHeight - 240, // 表的高度
        statusOptions: [
          {value: '0', label: '报废'},
          {value: '1', label: '正常'}
          // { value: '2', label: '正常' }
        ],
        //类型
        treeLoading: false,
        showTreeTableData: [
          {
            id: 0,
            label: "全部类型",
            parentId: -1,
            children: []
          }
        ],
        showTreeData: [],
        currTree: {},
        //列表
        searchInput: '',
        tableLoading: false,
        tableData: [],
        queryParams: {},
        // 分页
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 30, 40],
        total: 0,
        //新增编辑
        dialogTitle: '',
        formLoading: false,
        formDialog: false,
        formConfig:false,
        form: {
          status: '1',
          secret:''
        },
        rules: {
          assetCode: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          assetName: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          locationId: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          typeId: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          brand: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          specification: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          model: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          status: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          sort: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          deptId: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          planeCoordinateX:[
            {required: true, message: '必填项不得为空', trigger: 'change'}
          ],
          planeCoordinateY:[
            {required: true, message: '必填项不得为空', trigger: 'change'}
          ]
        },
        // 数据默认字段
        defaultProps: {
          parent: 'parentId',   // 父级唯一标识
          value: 'id',          // 唯一标识
          label: 'label',       // 标签显示
          children: 'children', // 子级
        },
        showLocationTreeData: [],
        showDeptTreeData: [],
        levelOptions: [],
        statusOptions: [],
        towerCraneConfig:[],
        //附件
        imgFileList: [],
        formDetailLoading: false,
        formDetailDialog: false,
        activeName: 'asset',
        //对接平台
        platformDict:[],
      }
    },
    computed: {
      //附件
      httpHeader() {
        return {
          'Admin-Token': axios.defaults.headers['Admin-Token']
        }
      },
      crmFileSaveUrl() {
        return crmFileSaveUrl
      },
    },
    mounted() {
      var self = this
      /** 控制table的高度 */
      window.onresize = function () {
        self.tableHeight = document.documentElement.clientHeight - 240
      }
      this.getTreeList();
      this.getLocationTreeList();
      this.getDeptTreeList();
      this.getTableList();
      //设备状态
      this.getDicts('b_assets_status').then(response => {
        this.statusOptions = response.data
      })
      //重要程度
      this.getDicts('b_assets_level').then(response => {
        this.levelOptions = response.data
      })
      this.getDicts('b_tower_crane_config').then(response => {
        this.towerCraneConfig = response.data
      })

      //
      getList({status:'1'})
        .then(res => {
          this.platformDict = res.data.list == null?[]:res.data.list;
        })
        .catch(() => {
        })
    },
    methods: {
      /**
       * 头部搜索
       */
      headerSearch(search) {
        this.searchInput = search
      },
      // 获取树形列表
      getTreeList() {
        this.treeLoading = true
        typeList()
          .then(response => {
            this.showTreeTableData[0]['children'] = response.data || []
            this.showTreeData = response.data || []
            //console.log(this.showTreeData)
            this.treeLoading = false
          })
          .catch(() => {
            this.treeLoading = false
          })
      },
      // 获取树形列表
      getLocationTreeList() {
        this.treeLoading = true
        locationList()
          .then(response => {
            this.showLocationTreeData = response.data || []
          })
          .catch(() => {
          })
      },
      // 获取树形列表
      getDeptTreeList() {
        depListAPI({type: 'tree'})
          .then(response => {
            this.showDeptTreeData = response.data || []
            if (this.showDeptTreeData.length > 0) {
              if (!this.showDeptTreeData[0].children) {
                this.showDeptTreeData[0].children = []
              }
            }
          })
          .catch(() => {
          })
      },
      /**
       * 选择类型
       */
      changeTreeClick(data, checked, indeterminate) {
        this.currTree = data;
        this.getTableList();
      },
      //新建
      add() {
        // if (!this.currTree.id || this.currTree.id == 0) {
        //   this.$message({
        //     message: '请选择子级设备类型',
        //     type: 'info'
        //   });
        //   return;
        // }
        this.dialogTitle = "新建";
        this.formDialog = true;
        this.form.typeId = 10;
        if (!this.form.batchId) {
          this.form.batchId = guid();
        }
        this.getUpdateOtherInfo();
      },
      edit(data) {
        this.dialogTitle = "编辑";
        this.formDialog = true;
        assetGet(data.assetId)
          .then(response => {
            this.form = response.data || []
            if(response.data.planeCoordinate){
              var strs= new Array();
              strs = response.data.planeCoordinate.split(",");
              console.log(response.data.planeCoordinate)
              console.log(strs)
              this.form.planeCoordinateX = strs[0];
              this.form.planeCoordinateY = strs[1];
            }
            if (!this.form.batchId) {
              this.form.batchId = guid();
            }
            this.getUpdateOtherInfo();
          })
          .catch(() => {
          })

      },
      config(data) {
        this.dialogTitle = "配置";
        this.formConfig = true;
        this.deviceId = data.assetId;
        getTowerConfig(data.assetId)
          .then(response => {
            this.form = response.data || {}

          })
          .catch(() => {
          })

      },
      // 新建和编辑
      newHandleClose() {
        this.formDetailDialog = false;
        this.formDialog = false;
        this.formConfig = false;
        this.form = {};
        this.imgFileList = [];
        this.resetForm('equform')
        this.resetForm('form')

        this.activeName = 'asset';
      },
      getTableList() {
        var data = {search: this.searchInput}
        data.typeId = 10
        data.page = this.currentPage
        data.limit = this.pageSize
        this.tableLoading = true;
        assetsList(data)
          .then(data => {
            if (data.code == 0) {
              this.tableData = data.data.list;
              this.total = data.data.totalRow
            } else {
              this.$message.error(data.msg);
            }
            this.tableLoading = false;
          })
          .catch(() => {
            this.tableLoading = false;
          })
      },
      getList() {
        var data = {search: this.searchInput}
        data.typeId = 10
        data.page = this.currentPage
        data.limit = this.pageSize
        data.fieldName = this.queryParams.fieldName
        data.sortingType = this.queryParams.sortingType
        this.tableLoading = true;
        assetsList(data)
          .then(data => {
            if (data.code == 0) {
              this.tableData = data.data.list;
              this.total = data.data.totalRow
            } else {
              this.$message.error(data.msg);
            }
            this.tableLoading = false;
          })
          .catch(() => {
            this.tableLoading = false;
          })
      },
      // 更改每页展示数量
      handleSizeChange(val) {
        this.pageSize = val
        this.getTableList()
      },
      // 更改当前页数
      handleCurrentChange(val) {
        this.currentPage = val
        this.getTableList()
      },
      save() {
        this.$refs['equform'].validate((valid) => {
          if (valid) {
            this.form.planeCoordinate =  this.form.planeCoordinateX+','+this.form.planeCoordinateY
            console.log(this.form)
            assetSave(this.form)
              .then(data => {
                if (data.code == 0) {
                  this.$message({
                    message: '保存成功',
                    type: 'success'
                  });
                  this.getTableList();
                  this.newHandleClose();
                } else {
                  this.$message.error(data.msg);
                }
              })
              .catch(() => {
              })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      saveConfig(){
        this.$refs['configform'].validate((valid) => {
          if (valid) {
            console.log(this.form)
            this.form.deviceId =  this.deviceId

            savTowerConfig(this.form)
              .then(data => {
                if (data.code == 0) {
                  this.$message({
                    message: '保存成功',
                    type: 'success'
                  });
                  this.getTableList();
                  this.newHandleClose();
                } else {
                  this.$message.error(data.msg);
                }
              })
              .catch(() => {
              })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      del(data) {
        this.$confirm('删除设备将同步删除维修、维护数据，是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          assetDelete(data)
            .then(data => {
              if (data.code == 0) {
                this.$message({
                  message: '删除成功',
                  type: 'success'
                });
                this.getTableList();
              } else {
                this.$message.error(data.msg);
              }
            })
            .catch(() => {
            })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      detail(data) {
        this.formDetailDialog = true;
        assetGet(data.assetId)
          .then(response => {
            this.form = response.data || []
            if (!this.form.batchId) {
              this.form.batchId = guid();
            }
            this.getUpdateOtherInfo();
          })
          .catch(() => {
          })
      },
      // 获取状态颜色 0 报废 1 正常
      getStatusColor(status) {
        if (status == '0') {
          return '#FF6767'
        } else {
          return '#46CDCF'
        }
        // else if (status == 2) {
        //   return '#CCCCCC'
        // }
      },
      //========= 附件部分 =======
      // 更新图片附件相关信息信息
      getUpdateOtherInfo() {
        adminFileQueryFileListAPI(this.form.batchId).then((res) => {
          this.imgFileList = objDeepCopy(res.data || [])

          for (let index = 0; index < this.imgFileList.length; index++) {
            this.setImageList(this.imgFileList[index], index)
          }
        }).catch(() => {
        })


      },
      /**
       * 获取图片内容
       */
      setImageList(item, index) {
        getImageData(item.url).then((data) => {
          item.url = data.src
          this.imgFileList.splice(index, 1, item)
        }).catch(() => {
        })
      },
      // 上传图片
      imgFileUploadSuccess(response, file, fileList) {
        console.log(fileList)
        this.imgFileList = fileList
      },
      // 查看图片
      handleFilePreview(file) {
        if (file.response || file.fileId) {
          let perviewFile
          if (file.response) {
            perviewFile = file.response.data
          } else {
            perviewFile = file
          }
          this.$bus.emit('preview-image-bus', {
            index: 0,
            data: [perviewFile]
          })
        }
      },
      beforeRemove(file, fileList) {
        if (file.response || file.fileId) {
          let fileId
          if (file.response) {
            fileId = file.response.data.fileId
          } else {
            fileId = file.fileId
          }
          this.$confirm('您确定要删除该文件吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
            .then(() => {
              crmFileDeleteAPI({
                id: fileId
              })
                .then(res => {
                  this.$message.success('操作成功')
                  var removeIndex = this.getFileIndex(
                    this.$refs.imageUpload.uploadFiles,
                    fileId
                  )
                  if (removeIndex != -1) {
                    this.$refs.imageUpload.uploadFiles.splice(removeIndex, 1)
                  }
                  removeIndex = this.getFileIndex(this.imgFileList, fileId)
                  if (removeIndex != -1) {
                    this.imgFileList.splice(removeIndex, 1)
                  }
                })
                .catch(() => {
                })
            })
            .catch(() => {
              this.$message({
                type: 'info',
                message: '已取消操作'
              })
            })
          return false
        } else {
          return true
        }
      },
      // 附件索引
      getFileIndex(files, fileId) {
        var removeIndex = -1
        for (let index = 0; index < files.length; index++) {
          const item = files[index]
          let itemFileId
          if (item.response) {
            itemFileId = item.response.data.fileId
          } else {
            itemFileId = item.fileId
          }
          if (itemFileId == fileId) {
            removeIndex = index
            break
          }
        }
        return removeIndex
      },
      sortChange(column) {
        sortColumn(this,column);
      },

    }
  }
</script>

<style lang="scss" scoped>
  @import '@/views/login/index.scss';

  .verify-picture {
    margin-top: 20px;
  }

  .sms-box {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    flex-wrap: nowrap;
    .el-input {
      width: 210px;
    }
    .el-button {
      flex: 1;
      font-size: 12px;
      color: white;
      background-color: #3e6bea;
      border-radius: $xr-border-radius-base;
      border: 0 none;
      padding: 0;
      margin-left: 20px;

      .btn-content {
        width: 100%;
        height: 42px;
        @include center;
        .icon {
          font-size: 16px;
          margin-right: 5px;
        }
      }
      &:hover,
      &.is-disabled,
      &.is-disabled:hover {
        color: white;
        border-color: #517aec;
        background-color: #517aec;
      }
    }
  }

  .employee-dep-management {
    padding: 10px 15px;
    height: 100%;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
  }

  .system-content {
    position: relative;
    height: 100%;
    flex: 1;
    display: flex;
    overflow: hidden;
  }

  .system-nav {
    width: 280px;
    height: 100%;
    overflow: auto;
    margin-right: 10px;
    background: #fff;
    border: 1px solid $xr-border-line-color;
    border-radius: $xr-border-radius-base;

    &__title {
      padding: 15px;
      font-size: 16px;
      font-weight: 600;
      border-bottom: 1px solid $xr-border-line-color;
    }

    &__content {
      overflow: auto;
      height: calc(100% - 50px);
      overflow-y: overlay;
      overflow-x: overlay;
    }
  }

  // 菜单
  .section {
    position: relative;
    &__title {
      position: relative;
      font-size: 14px;
      font-weight: 600;
      color: #333;
      padding: 15px;
      .add-btn {
        position: absolute;
        right: 10px;
        top: 10px;
      }
    }
  }

  .menu-item {
    position: relative;
    cursor: pointer;
    padding: 12px 15px;
    color: #333;

    &__icon {
      font-size: 14px;
      margin-right: 8px;
      color: #8a94a6;
    }

    &__content {
      font-size: 14px;
    }
  }

  .menu-item:hover,
  .menu-item.is-select {
    background-color: $xr--background-color-base;
  }

  .menu-item:hover::before,
  .menu-item.is-select::before {
    content: ' ';
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    width: 2px;
    background-color: #5383ed;
  }

  .system-view-table {
    background: #fff;
    border: 1px solid $xr-border-line-color;
    border-radius: $xr-border-radius-base;
    position: absolute;
    top: 0;
    /*left: 295px;*/
    bottom: 0;
    right: 0;
  }

  .table-top {
    padding: 0 30px;
    height: 50px;
    border-bottom: 1px solid #e6e6e6;
    &__title {
      font-size: 16px;
      color: #333;
    }

    .el-dropdown {
      margin-left: 7px;
    }
  }

  .all-user-reminder {
    width: auto;
    margin-left: 5px;
    display: inline-block;
  }

  .el-table /deep/ .el-table-column--selection .cell {
    padding-left: 14px;
  }

  // .status {
  //   display: inline-block;
  //   margin-left: 50px;
  // }
  // .status > span {
  //   margin-right: 10px;
  // }

  .status-name {
    div {
      display: inline-block;
      width: 6px;
      height: 6px;
      border-radius: 3px;
    }
    color: $xr-color-primary;
    cursor: pointer;
  }

  /** 树形结构 */
  .el-tree /deep/ .el-tree-node__expand-icon {
    display: none;
  }

  .el-tree /deep/ .el-tree-node__content {
    height: 40px;

    .node-data {
      // .node-img {
      //   width: 15px;
      //   height: 15px;
      //   display: block;
      //   margin-right: 8px;
      //   margin-left: 24px;
      // }
      height: 40px;
      padding: 0 15px;
      position: relative;
      border-radius: $xr-border-radius-base;

      .wk {
        font-size: 14px;
        color: #8a94a6;
        flex-shrink: 0;
      }

      .wk-department {
        margin-right: 8px;
      }

      &__mark {
        display: inline-block;
        width: 6px;
        height: 6px;
        border-radius: 50%;
        background-color: #e6e6e6;
        margin-right: 8px;
        flex-shrink: 0;
      }

      &__label {
        flex: 1;
        color: #333;
        font-size: 14px;
        margin-right: 8px;
      }

      .wk-up-unfold {
        margin-left: 8px;
        transition: transform 0.3s;
      }

      .wk-up-unfold.is-close {
        transform: rotateZ(180deg);
      }
      // .node-label-set {
      //   display: none;
      // }
    }

    .node-data.is-current,
    .node-data:hover {
      background-color: $xr--background-color-base;
    }

    // .node-data:hover .node-label-set {
    //   display: block;
    // }
  }

  // .el-tree /deep/ .el-tree-node.is-current > .el-tree-node__content {
  //   background-color: #ebf3ff;
  //   border-right: 2px solid #46cdcf;
  //   .node-label-set {
  //     display: block;
  //   }
  // }
  .system-nav /deep/ .el-tree-node > .el-tree-node__children {
    overflow: visible;
  }

  .system-nav /deep/ .el-tree > .el-tree-node {
    min-width: 100%;
    display: inline-block !important;
  }

  .system-nav
  /deep/
  .el-tree--highlight-current
  .el-tree-node.is-current
  > .el-tree-node__content {
    background-color: white;
  }

  .system-nav /deep/ .el-tree-node__content:hover {
    background-color: white;
  }

  /* 搜索框图标按钮 */
  .icon-search .el-icon-search {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    width: 40px;
    line-height: 40px;
    text-align: center;
    cursor: pointer;
    font-size: 20px;
    color: #ccc;
  }

  /* 设置flex布局 */
  .flex-index {
    display: flex;
    flex-direction: column;
  }

  /* 设置占位 */
  .flex-box {
    flex: 1;
    // border-bottom: 1px solid $xr-border-line-color;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content
  .el-select-group__wrap:not(:last-of-type)::after {
    display: none;
  }

  .new-dialog-form /deep/ .el-form-item .el-form-item__content .el-select-group {
    padding-left: 10px;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content
  .el-select-group__title {
    border-bottom: 1px solid #e4e7ed;
    padding: 0 0 7px;
    margin: 0 20px 5px;
  }

  .new-dialog-form
  /deep/
  .el-form-item {
    width: 100%;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__label {
    text-align: right;
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content {
    width: calc(100% - 120px);
  }

  .new-dialog-form
  /deep/
  .el-form-item
  .el-date-editor--date {
    width: 100%;
  }

  .status-des {
    font-size: 12px;
    color: #777777;
    margin: 0 5px;
    position: absolute;
    left: 0;
    top: 7px;
    .status-des-item {
      margin: 8px;
      display: inline-block;
      div {
        display: inline-block;
        width: 6px;
        height: 6px;
        border-radius: 3px;
        margin-right: 5px;
      }
    }
  }

  // 提示
  // 提示标志
  .wukong-help_tips {
    color: #999;
    font-size: 14px;
    margin-left: 3px;
    cursor: pointer;
  }

  .wukong-help_tips:hover {
    color: $xr-color-primary;
  }

  .tips {
    font-size: 13px;
    color: #999;
  }

  // 图片 附件
  .img-accessory {
    padding: 0 20px;
    font-size: 12px;
    img {
      width: 16px;
      vertical-align: middle;
    }
    .img-box /deep/ .el-upload {
      width: 80px;
      height: 80px;
      line-height: 90px;
    }
    .img-box /deep/ .el-upload-list {
      .el-upload-list__item {
        width: 80px;
        height: 80px;
      }
    }
    .img-box {
      position: relative;
      margin-top: 40px;
      .add-img {
        position: absolute;
        left: 0;
        top: -30px;
        height: 20px;
        line-height: 20px;
        margin-bottom: 10px;
        color: #2362FB;
      }
    }
    .add-accessory {
      margin-top: 25px;
      margin-bottom: 20px;
      color: #2362FB;

      .wukong-file {
        font-size: 13px;
      }
    }
  }

  /deep/ .el-dialog__body {
    height: 600px;
    overflow: auto;
  }

  //@import '../../admin/styles/table.scss';
</style>

