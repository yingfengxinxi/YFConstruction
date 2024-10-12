<template>
  <div class="main">
    <div class="main-body">
      <el-row  style="background-color: #fff">
        <el-col :span="4"  style="">
          <el-card class="box-card" style="margin: 10px;height:calc(100vh - 170px);">
            <div slot="header" class="clearfix">
              <span style="font-weight: bold;font-size: 16px">试题类别</span>
            </div>
            <el-tree
              :data="catalogData"
              node-key="id"
              default-expand-all
              :expand-on-click-node="false"
              :highlight-current="true"
              :props="defaultProps"
              @node-click="checkCatalog">
            </el-tree>
          </el-card>
        </el-col>
        <el-col :span="20" >
          <el-card class="box-card" style="margin: 10px;height:calc(100vh - 475px);">
            <div style="height: 45px;">
              <span style="padding-left:5px;line-height: 40px;">试题列表</span>
              <el-button
                class="xr-btn--orange"
                type="primary"
                icon="el-icon-plus"
                size="mini"
                style="position: absolute;top: 35px;right: 120px;z-index: 999"
                @click="handleAdd" >新建</el-button>
				        <el-button class="export-btn" style="float:right;margin-top: 4px;margin-right: 20px;" @click="excelImport">导入</el-button>

            </div>

            <el-table v-loading="loading"
                      ref="questTable"
                      :data="questionList"
                      :height="tableHeight"
                      :row-class-name="tableRowClassName"
                      @row-click="handleQuestCurrentDataChange"
                      @sort-change="sortChange"
                      highlight-current-row >
                <el-table-column label="序号" type="index" align="center"  width="90"></el-table-column>
                <el-table-column label="试题" align="center" prop="questContent" show-overflow-tooltip sortable="custom"  />
                <el-table-column label="试题类别" align="center" prop="typeId" width="120px" :formatter="typeIdFormatter" sortable="custom" />
                <el-table-column label="试题类型" align="center" prop="questType" width="120px" :formatter="questTypeFormatter" sortable="custom" />
                <el-table-column label="录入时间" align="center" prop="createTime" width="180px" sortable="custom" />
                <el-table-column label="录入人" align="center" prop="realname" width="120px" sortable="custom" />

                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220px">
                    <template slot-scope="scope">
                      <el-button type="text" icon="el-icon-edit" size="small" @click="handleClick('edit', scope)">编辑</el-button>
                      <el-button type="text" icon="el-icon-delete" size="small" @click="handleClick('delete', scope)">删除</el-button>
                      <el-button type="text" icon="el-icon-document" size="small" @click="handleClick('show', scope)">查看</el-button>
                    </template>
                </el-table-column>

            </el-table>
            <div class="p-contianer" >
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

          </el-card>


          <el-card class="box-card" style="margin: 5px;height:295px;position:relative;">
            <div style="height: 55px;">
              <span style="padding-left:5px;line-height: 40px;">试题选项信息</span>
              <el-button
                v-show="questionList.length > 0"
                class="xr-btn--orange"
                type="primary"
                icon="el-icon-plus"
                size="mini"
                style="position: absolute;top: 30px;right: 35px;"
                @click="handleAddOption" >新建</el-button>
            </div>

            <el-table v-loading="loading" :data="questionOptionList" height="200px" >
                <el-table-column label="序号" type="index" align="center"  width="90"></el-table-column>
                <el-table-column label="选项标记" align="center" prop="answerFlag" width="120px" />
                <el-table-column label="试题选项" align="center" prop="options" show-overflow-tooltip/>
                <el-table-column label="答案标记" align="center" prop="optionsFlag" width="120px" :formatter="optionsFlagFormatter"/>

                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220px">
                    <template slot-scope="scope">
                      <el-button type="text" icon="el-icon-edit" size="small" @click="handleOptionClick('edit', scope)">编辑</el-button>
                      <el-button type="text" icon="el-icon-delete" size="small" @click="handleOptionClick('delete', scope)">删除</el-button>
                      <el-button type="text" icon="el-icon-document" size="small" @click="handleOptionClick('show', scope)">查看</el-button>
                    </template>
                </el-table-column>
            </el-table>
          </el-card>

        </el-col>
      </el-row>
    </div>

    <Create
      v-if="createShow"
      :questTypeId="questTypeId"
      :questTypeName = "questTypeName"
      :showType = "showType"
      :menuData = "menuData"
      width="500px"
      @close="createShow = false"
      @save-success="getList"
    />

<!-- 试题选项 -->
    <Option-create
      v-if="createOptionShow"
      :questId="questId"
      :questContent = "questContent"
      :showQuestType = "showQuestType"
      :menuQuestData = "menuQuestData"
      width="500px"
      @close="createOptionShow = false"
      @save-success="getQuestOptionList(questId)"
    />

    <!-- 批量导入 -->
		<bulk-import-user
			:show="bulkImportShow"
      :param="param"
			@close="bulkImportClose"
			/>

  </div>
</template>

<script>

    import {selectTree , selectQuestionIdAndName ,typeIdFormatter}from '@/views/build/wifi/api/questType'//左侧树
    import {getQuestionList, delQuestionById, selectQuestionById }from '@/views/build/wifi/api/question'//右侧试题表
    import {getQuestOptionList, delQuestOptionById, selectQuestOptionById }from '@/views/build/wifi/api/questOption'//右侧试题选项表
		import {sortColumn } from '@/utils'
    import XrHeader from '@/components/XrHeader'
    import Create from './conponents/Create'
    import OptionCreate from './conponents/OptionCreate'

    import BulkImportUser from '@/components/importModel'


  export default {
    name: 'wifiQuestion',
    components: {
      Create,
      OptionCreate,
      BulkImportUser
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        tableHeight:document.documentElement.clientHeight - 580,

        // 分页
        currentPage: 1,
        pageSize: 10,
        pageSizes: [10, 20, 30, 40],
        total: 0,

        catalogData: [],//左侧问题类别树
        defaultProps: {
            value: 'id',          // 唯一标识
            label: 'name',       // 标签显示
            children: 'children', // 子级
        },

        currentIndex:0,//选中当前行的index

        //右侧 试题表--开始
        createShow: false,
        questTypeId:0,//当前选中的试题类别的id，在新建试题的时候使用
        questTypeName:'',//当前选中的试题类别的name，在新建试题的时候使用

        menuData:{},//试题修改、查看的时候传递的页面展示数据
        showType:'',//试题新增（createShow）、修改（updateShow）、查看（detailsShow）的时候传递的类型

        id: 0,

        questionList:[],//右侧 试题表 数据

        questTypeList:[],//题型(1:单选2:多选3：填空4：问答)
        typeList:[],//试题类型

        //右侧 试题表--结束


        //试题选项--开始
        questionOptionList:[],//右侧 试题选项表 数据
        questId:'',//当前选中的试题的id，新建试题选项的时候使用
        questContent:'',//试题内容
        createOptionShow:false,//试题选项弹出框是否显示
        showQuestType:'',//试题选项新增（createOptionShow）、修改（updateOptionShow）、查看（detailsOptionShow）的时候传递的类型
        menuQuestData:{},//试题选项修改、查看的时候传递的页面展示数据

        optionsFlagList:[],//答案标记
        //试题选项--结束

        bulkImportShow:false,//导入
        param:{},
        queryParams:{}
      }
    },

    watch: {
      questionList: function() {
        this.$nextTick(function() {

          let index = this.currentIndex

          if(this.showType == 'createShow'){
            index = this.questionList.length-1
            this.questId = this.questionList[index].id
            this.questContent = this.questionList[index].questContent

            this.getQuestOptionList(this.questId)
          }

          if(this.showType == 'deleteShow'){
            index = 0
            this.questId = this.questionList[index].id
            this.questContent = this.questionList[index].questContent

            this.getQuestOptionList(this.questId)
          }

          this.$refs.questTable.setCurrentRow(this.questionList[index])
        })
      }
    },

    created() {
        //  题型 字典项
        this.getDicts('b_wifi_quest_type').then(response => {
          this.questTypeList = response.data
        })
       //  答案标记 字典项
        this.getDicts('b_wifi_option_flag').then(response => {
          this.optionsFlagList = response.data
        })

        //试题类型
        this.typeListFunction()


        //左侧树
        this.selectTree();

    },
    methods: {
				sortChange(column) {
		      sortColumn(this,column);
		    },
        //查询试题类型id和name
        typeListFunction(){
            selectQuestionIdAndName().then(res=>{
                this.typeList = res.data
            })
        },

        //左侧树
        selectTree(){
            selectTree().then(response => {

                this.catalogData = response.data

                if(response.data.length > 0){
                    this.questTypeId = response.data[0].id
                    this.questTypeName = response.data[0].name;
                    //右侧table数据
                    this.getList();
                }

                this.loading = false
            }).catch(() => {
                this.loading = false
            })
        },

        //左侧树点击查询
        checkCatalog(data,node,e){
            this.questTypeId = data.id;
            this.questTypeName = data.name;
            this.getList();

        },

        //右侧table数据
        getList(){
            this.queryParams.page = this.currentPage;
            this.queryParams.limit = this.pageSize;
            this.queryParams.typeId = this.questTypeId;

            //查询试题表数据
            getQuestionList( this.queryParams).then(res => {
                this.questionList = res.data.list

                if (res.data.totalRow && Math.ceil(res.data.totalRow / this.pageSize) < this.currentPage && this.currentPage > 1) {
                  this.currentPage = this.currentPage - 1
                  this.getList()
                } else {
                  this.total = res.data.totalRow
                  this.loading = false
                }
                this.$nextTick(() => {
                  document.querySelector('.el-table__body-wrapper').scrollTop = 1
                })

                if(this.questionList.length > 0){

                    this.questId = this.questionList[this.currentIndex].id
                    this.questContent = this.questionList[this.currentIndex].questContent
                    //根据试题id查询试题选项表数据
                    this.getQuestOptionList(this.questionList[this.currentIndex].id)

                }else{
                  this.getQuestOptionList("0")
                }

                this.loading = false

            }).catch(() => {
                this.loading = false
            })


        },

        close(){
            this.createShow = false;

        },


        //新建试题
        handleAdd(){
            if(this.questTypeId == '0'){
                this.$message({
                    message: '请选择试题类别',
                    type: 'warning'
                });
            }else {
                this.showType = 'createShow'
                this.createShow = true;
            }
        },

        tableRowClassName({row, rowIndex}) {
          row.row_index = rowIndex;
        },

        //选中试题
        handleQuestCurrentDataChange(currentRow,col,e){

          if(currentRow != null){
            this.currentIndex = currentRow.row_index
            this.questId = currentRow.id
            this.questContent = currentRow.questContent
            this.getQuestOptionList(this.questId)
          }

        },

        //试题表  修改、删除、查看
        async handleClick(event, scope) {
            //此处需同步调用
            await selectQuestionById(scope.row.id)
                .then(res => {
                    this.menuData = res.data;
                    // let a  = this.typeIdFormatter(this.menuData)

                    this.menuData.questTypeName = this.questTypeName
                })

            if (event == 'edit') {//修改
                this.showType = 'updateShow'
                this.createShow = true;
            } else if (event == 'delete') { //删除
                this.$confirm('您确定要删除吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    })
                    .then(() => {
                        delQuestionById(this.menuData.id)
                            .then(res => {
                                this.loading = false
                                if (res.code == 0) {
                                    this.msgSuccess('删除成功')
                                    this.showType = 'deleteShow'
                                    this.getList();

                                } else {
                                    this.$message({
                                        type: 'warn',
                                        message: res.msg
                                    })
                                }
                            })
                            .catch(() => {
                                this.loading = false
                            })
                    })
                    .catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消删除'
                        })
                    })
            } else if(event == 'show'){//查看
                this.showType = 'detailsShow'
                this.createShow = true;
            }
        },

        //试题类型
        typeIdFormatter(row, column) {
            return typeIdFormatter(row.typeId, this.typeList)
        },

        //题型
        questTypeFormatter(row, column) {
            return this.selectDictLabel(this.questTypeList, row.questType)
        },

        //答案标记
        optionsFlagFormatter(row, column){
            return this.selectDictLabel(this.optionsFlagList, row.optionsFlag)
        },

        //根据试题id查询试题选项表数据
        getQuestOptionList(questionId){
          getQuestOptionList(questionId).then(res=>{
              this.questionOptionList = res.data
              this.loading = false
          }).catch(() => {
              this.loading = false
          })
        },

        //新建试题选项
        handleAddOption(){
          if(this.questId == '0'){
                this.$message({
                    message: '请选择试题',
                    type: 'warning'
                });
            }else {
                this.showQuestType = 'createOptionShow'
                this.createOptionShow = true;
            }
        },

        //试题选项表  修改、删除、查看
        async handleOptionClick(event, scope){
            //此处需同步调用
            await selectQuestOptionById(scope.row.id)
                .then(res => {
                    this.menuQuestData = res.data;
                    // let a  = this.typeIdFormatter(this.menuData)

                    // this.menuQuestData.questTypeName = this.questTypeName
                })

            if (event == 'edit') {//修改
                this.showQuestType = 'updateOptionShow'
                this.createOptionShow = true;
            } else if (event == 'delete') { //删除
                this.$confirm('您确定要删除吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    })
                    .then(() => {
                        delQuestOptionById(this.menuQuestData.id)
                            .then(res => {
                                this.loading = false
                                if (res.code == 0) {
                                    this.msgSuccess('删除成功')
                                    this.getQuestOptionList(this.questId);

                                } else {
                                    this.$message({
                                        type: 'warn',
                                        message: res.msg
                                    })
                                }
                            })
                            .catch(() => {
                                this.loading = false
                            })
                    })
                    .catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消删除'
                        })
                    })
            } else if(event == 'show'){//查看
                this.showQuestType = 'detailsOptionShow'
                this.createOptionShow = true;
            }
        },


        //导入
        excelImport(){
          let lists = [
            "题型(1:单选，2:多选，3:填空，4:问答)(*)",
            "试题内容(*)",
            "试题详解(*)",
            "选项(A、B...)及答案标记(1:是;0:否)-1",
            "试题选项-1",
            "选项(A、B...)及答案标记(1:是;0:否)-2",
            "试题选项-2",
            "选项(A、B...)及答案标记(1:是;0:否)-3",
            "试题选项-3",
            "选项(A、B...)及答案标记(1:是;0:否)-4",
            "试题选项-4"
          ]
          //示例数据
          let dataLists = [
            "1",
            "示例数据1",
            "示例数据2",
            "A:1",
            "测试A1",
            "B:0",
            "测试B1",
            "C:0",
            "测试C1",
            "D:1",
            "测试D1"
          ]

          this.param = {
            title:"wifi教育题库导入模板(*)为必填项 另:【选项及答案标记】和【试题选项】任意一项有数据，另一项必填",
            excelTitle:"wifi教育题库导入模板",
            colNames:lists,
            sampleData:dataLists,
            url:'build/wifiQuestions/excelImport',
            questTypeId:this.questTypeId
          }
          this.bulkImportShow = true
        },

        // 关闭导入组件
        bulkImportClose() {
          this.bulkImportShow=false
          this.getList();
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

    },

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
    /*border-bottom: 1px solid $xr-border-line-color;*/
  }
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }
  .el-aside {
    color: #333;
  }
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
  .custom-tree-but{
    display: inline-block;
    position: relative;
    right: 10px;
    top: 0px;
  }
  .custom-tree-button{
    padding: 3px 10px;
  }
  .xl-but{
    background-color: #CCCCCC;
    color: #000000;
  }
</style>
