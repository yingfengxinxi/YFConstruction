<template>
  <xr-create
    :loading="loading"
    :title="title"
    :confirm-button-text="'保存'"
    @close="close"
    @save="debouncedSaveField(true)">
    <create-sections title="基本信息">
      <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
               class="form">
        <el-row>
          <el-col :span="12">
            <el-form-item label="应用类别" prop="classType">
              <el-select v-model="form.classType" placeholder="请选择应用类别">
                <el-option
                  v-for="item in dictData.classTypeDict"
                  :key="item.dictValue"
                  :label="item.dictLabel"
                  :value="item.dictValue">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
		  <el-col :span="12">
		    <el-form-item label="分项类型" prop="itemType">
		      <el-select v-model="form.itemType" placeholder="请选择分项类型">
		        <el-option
		          v-for="item in dictData.itemTypeDict"
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
		    <el-form-item label="分项名称" prop="itemName">
		      <el-input v-model="form.itemName" placeholder="请输入分项名称"
		      ></el-input>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="分项描述" prop="itemDes">
		      <el-input v-model="form.itemDes" placeholder="请输入分项描述"
		      ></el-input>
		    </el-form-item>
		  </el-col>
		</el-row>
		
        <el-row>
		  <el-col :span="12">
		    <el-form-item label="最小分值" prop="minData">
		      <el-input type="number" v-model="form.minData" placeholder="请输入最小分值"></el-input>
		    </el-form-item>
		  </el-col>
		  <el-col :span="12">
		    <el-form-item label="最大分值" prop="maxData">
		      <el-input type="number" v-model="form.maxData" placeholder="请输入最大分值"></el-input>
		    </el-form-item>
		  </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
			<el-form-item label="等级" prop="levels">
			   <el-checkbox-group v-model="levelList">
				  <el-checkbox v-for="level in dictData.evalLevelList" :label="level.id" :key="level.id">{{level.levelName}}</el-checkbox>
			   </el-checkbox-group>
			</el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="form.remarks" placeholder="请输入备注"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="sort">
              <el-input v-model="form.sort" placeholder="请输入备注"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
    </create-sections>
  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'

  import {debounce} from 'throttle-debounce'

  import {saveOrUpdate, getById} from "../api/items"

  export default {
    // 新建编辑
    name: 'PlanCreate',
    components: {
      XrCreate,
      CreateSections,
    },
    //mixins: [ExamineMixin],//业务审批Copy:审批
    props: {
      action: {
        type: Object,
        default: () => {
          return {
            type: 'save',
            id: '',
            data: {}
          }
        }
      },
	  // 字典数据
	  dictData: {},
    },
    data() {
      return {
        loading: false,
		
		
		levelList: [],
        form: {
        },
		// 数据默认字段
		defaultProps: {
		  parent: 'parentId',   // 父级唯一标识
		  value: 'id',          // 唯一标识
		  label: 'label',       // 标签显示
		  children: 'children', // 子级
		},

        rules: {
          classType: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          itemType: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          itemName: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
          levels: [
            {required: true, message: '必填项不得为空', trigger: 'change'},
          ],
        },
      }
    },
    computed: {
      title() {
        return this.action.type === 'update' ? '编辑工地等级' : '新建工地等级'
      },

    },
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      //考虑附件ID
      if (this.action.type == "update") {
        this.getDetail();
      }
    },
    methods: {
      /**
       * 获取详情
       * */
      getDetail() {
        getById(this.action.id)
          .then(res => {
			let item = res.data;
			this.levelList = item.levels.split(",")
            this.form = res.data
          })
          .catch(() => {
          })
      },
      /**
       * 保存
       */
      save(isSubmit) {
		let levels = ''
		let levelList = this.levelList
		for (let i = 0; i<levelList.length; i++) {
			if (i === levelList.length-1) {
				levels += levelList[i]
			} else {
				levels += levelList[i] + ","
			}
		}
		this.form.levels = levels
		
        this.$refs['form'].validate(valid => {
          if (valid) {

            var params = this.form;
            this.loading = true;
            saveOrUpdate(params).then(response => {
              if (response.code === 0) {
                this.$message.success(
                  this.action.type == 'update' ? '编辑成功' : '添加成功'
                )
                this.close()
                // 保存成功
                this.$emit('save-success', {
                  type: 'receivables'
                })
              } else {
                this.$message.error(
                  response.msg
                )
              }
              this.loading = false;
            }).catch(err => {
              this.loading = false;
            })

          }
        })
      },
      /**
       * 关闭
       */
      close() {
        this.$emit('close')
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
  .el-date-editor, .el-select{
    width: 100%;
  }
</style>
