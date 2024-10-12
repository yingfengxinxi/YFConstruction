<template>
  <xr-create :loading="loading" :title="title" :confirm-button-text="'保存'" @close="hidenView" @save="debouncedSaveField(true)" :showSave="!disabledT">

    <create-sections title="基本信息" style="margin-bottom: 20px;">
      <el-form v-loading="loading" class="form" ref="menuForm" :rules="selectVale == '2' ? memuRules2 : memuRules" :model="menuForm" :disabled="disabledT" label-width="115px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="教育模板名称" prop="name">
              <el-input v-model="menuForm.name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="适用工种" class='special' prop="workTypeId">
              <el-select v-model="menuForm.workTypeId" placeholder="请选择" clearable style="width: 100%" @change="workTypeChange">
                <el-option v-for="dict in workTypeOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="教育模式" prop="educationModel">
              <el-select v-model="menuForm.educationModel" placeholder="请选择" clearable style="width: 100%" @change="dataload" >
                <el-option v-for="dict in educationModelOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用" prop="ifStartUse">
              <el-select v-model="menuForm.ifStartUse" placeholder="请选择" clearable style="width: 100%" >
                <el-option v-for="dict in ifStartUseOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="课程视频" prop="courseVideos">
            <el-select v-model="menuForm.courseVideos" multiple placeholder="请选择" style="width: 100%" >
              <el-option v-for="item in courseVideos" :key="item.id" :label="item.courseVideoName" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-row>
      </el-form>
    </create-sections>
  </xr-create>
</template>
<script type="text/javascript">
import XrCreate from '@/components/XrCreate'
import CreateSections from '@/components/CreateSections'
import { guid } from '@/utils'

import { debounce } from 'throttle-debounce'

import { saveVideoSet, updateById, saveDetail, getLibraryListByWorkType } from '@/views/build/wifi/api/videoSet'
import { getWTList } from '@/api/build/infocollection/infocoll'


export default {
  name: 'CreateMenu', // 所有新建效果的view
  components: {//声明组件
    XrCreate,
    CreateSections,
  },
  filters: {//过滤器
  },
  props: {//属性
    menuForm: {},
    actionType: '',
  },
  data () {
    return {

      loading: true,
      selectVale: '0',
      tableHeight: document.documentElement.clientHeight - 450, // 表的高度

      // 自定义字段验证规则
      memuRules: {
        name: [
          { required: true, message: '必填项不能为空', trigger: 'blur' }
        ],
        ifStartUse: [
          { required: true, message: '必填项不能为空', trigger: 'blur' }
        ],
        workTypeId: [
          { required: true, message: '必填项不能为空', trigger: 'blur' }
        ],
        educationModel: [
          { required: true, message: '必填项不能为空', trigger: 'blur' }
        ],
        courseVideos: [
          { required: true, message: '必填项不能为空', trigger: 'blur' }
        ]
      },
      memuRules2: {
        name: [
          { required: true, message: '必填项不能为空', trigger: 'blur' }
        ],
        ifStartUse: [
          { required: true, message: '必填项不能为空', trigger: 'blur' }
        ],
        workTypeId: [
          { required: true, message: '必填项不能为空', trigger: 'blur' }
        ],
        educationModel: [
          { required: true, message: '必填项不能为空', trigger: 'blur' }
        ]
      },
      worktypeList: [],// 工种数据
			workTypeOptions: [],//工种字典
      ifStartUseOptions: [],//是否启用
      educationModelOptions: [],//教育模式

      courseVideos: [],//课程视频
    }
  },
  created () {
    this.debouncedSaveField = debounce(300, this.saveVideoSet)

    //工种数据
    //this.getWorktypeList()
	// 工种字典
	this.getDicts('b_labour_work_type').then(response => {
	  this.workTypeOptions = response.data
	})

    //  是否启用 字典项
    this.getDicts('sys_yes_no').then(response => {
      this.ifStartUseOptions = response.data
    })
    //教育模式
    this.getDicts('b_wifi_education_model').then(response => {
      this.educationModelOptions = response.data
    })

    if (this.actionType === 'update' || this.actionType === 'show') {
      let paramObj = {}
      paramObj.pageType = 1;
      paramObj.workTypeId = this.menuForm.workTypeId;
      this.selectVale = this.menuForm.educationModel;
      getLibraryListByWorkType(paramObj).then(res => {
        this.loading = false
        // console.log(res);
        this.courseVideos = res.data.list
        let str = this.menuForm.courseLibraryIds.split(",")
        for (let i = 0; i < str.length; i++) {
          for (let j = 0; j < this.courseVideos.length; j++) {
            if (str[i] == this.courseVideos[j].id + "") {
              this.menuForm.courseVideos.push(this.courseVideos[j].id);
              break;
            }
          }
        }
      })
        .catch(() => {
          this.loading = false
        })
    }else if (this.actionType === 'create') {
      this.loading = false
      this.courseVideos = []
    }
  },
  computed: {//监听属性
    title () {
      return this.actionType === 'update' ? '编辑教育视频配置' : (this.actionType === 'show' ? '查看教育视频配置' : '新建教育视频配置')
    },
    disabledT () {
      return this.actionType === 'show' ? true : false
    },

  },
  mounted () {//渲染完成后

  },
  destroyed () {//销毁后
  },
  methods: {//声明方法
    workTypeChange (value) {
      // console.log(value)
      this.loading = true
      let paramObj = {}
      paramObj.pageType = 1;
      paramObj.workTypeId = value;
      getLibraryListByWorkType(paramObj).then(res => {
        this.loading = false
        // console.log(res);
        this.courseVideos = res.data.list
      })
        .catch(() => {
          this.loading = false
        })
    },
    saveVideoSet () {
      // console.log(this.$refs.menuForm);
      this.$refs.menuForm.validate(valid => {
        if (valid) {
          this.loading = true;
          this.saveOrUpdate()
        } else {
          this.$message.error('请完善必填信息')
          return false
        }
      })
    },

    saveOrUpdate () {
      this.menuForm.batchIds = [];
      this.menuForm.courseLibraryIds = "";

      this.courseVideos.forEach(element => {
        this.menuForm.courseVideos.forEach(eee => {
          if (eee == element.id) {
            this.menuForm.batchIds.push(element.batchId);
            if (this.menuForm.courseLibraryIds == "") {
              this.menuForm.courseLibraryIds = element.id;
            } else {
              this.menuForm.courseLibraryIds = this.menuForm.courseLibraryIds + "," + element.id;
            }
          }
        })
      });

      if (this.actionType == 'create') {
        saveVideoSet(this.menuForm)
          .then(res => {
            this.msgSuccess('添加成功')
            this.$emit('save');
            this.hidenView();
            this.loading = false;
          })
          .catch(() => {
            this.loading = false
          })

      } else {
        // console.log("更新提交", this.menuForm);
        updateById(this.menuForm)
          .then(res => {
            this.msgSuccess('修改成功')
            this.$emit('save');
            this.hidenView();
            this.loading = false;
          })
          .catch(() => {
            this.loading = false
          })
      }
    },
    hidenView () {
      this.$emit('hiden-view')
    },

    /** 查询所工种数据列表 */
    getWorktypeList () {
      getWTList().then(response => {
        this.worktypeList = response.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    dataload(value) {
      var than = this
      than.selectVale = value
      this.$refs.menuForm.clearValidate()
      console.log("shuju:"+than.selectVale)
    }

  }
}
</script>
