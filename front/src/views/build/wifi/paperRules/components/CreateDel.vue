<template>

    <el-dialog
    :title="title"
    :visible="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    :close-on-click-modal="false"
    class="create-dialog"
    @close="close"
    >

    <el-form ref="form" :model="form" :disabled="this.showType == 'detailsPaperRulesDelShow' ? true : false" :rules="rules" label-width="100px" >
      <el-row>
        <el-col :span="12">
          <el-form-item label="分值" prop="questScore">
            <el-input type="number" v-model="form.questScore" ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数量" prop="questCount">
            <el-input type="number" v-model="form.questCount" ></el-input>
          </el-form-item>
        </el-col>
        
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="题型" prop="questType">
            <el-select
              v-model="form.questType"
              placeholder="请选择">
              <el-option
                v-for="item in questTypeList"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue"/>
            </el-select>
            
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分类" prop="typeId">
            <!-- <el-select
              v-model="form.typeId"
              placeholder="请选择">
              <el-option
                v-for="item in typeList"
                :key="item.id"
                :label="item.name"
                :value="item.id"/>
            </el-select> -->

              <select-tree :options="catalogData" 
                            v-model="form.typeId"
                            :props="defaultProps" 
                            :expandAll="true"/>
        
          </el-form-item>
        </el-col>
      </el-row>
      
    </el-form>

  <span
      slot="footer"
      class="dialog-footer">
      <el-button @click.native="close">取 消</el-button>
      <el-button type="primary" v-show="this.showType == 'detailsPaperRulesDelShow'?false:true" @click.native="save">确 定</el-button>
    </span>
  </el-dialog>

</template>

<script>

  import {savePaperRulesDel, updatePaperRulesDelById }from '@/views/build/wifi/api/paperRulesDetails'//组卷规则详细表
  import {selectQuestionIdAndName , selectTree}from '@/views/build/wifi/api/questType'
  import SelectTree from '@/components/SelectTree'

    export default {
      name: "Create",
      components: {
        SelectTree
      },
      props: {
        questTypeId: {//试题类型id
          type: String,
          require: true
        },

        showType: {//新增（createPaperRulesDelShow）、修改（updatePaperRulesDelShow）、查看（detailsPaperRulesDelShow）
          type: String,
          require: true
        },
        paperRulesDelData: {//修改、查看 时的数据展示
          type: Object,
          default: {}
        },
      },
      data() {
        return {
          loading: false,
          title: "组卷规则新增",
          form: {
            typeId:''
          },
          questTypeList:[],//题型(1:单选2:多选3：填空4：问答)
          typeList:[],//试题分类

          rules: {
            questType: [
              { required: true, message: '必填项', trigger: 'change' }
            ],
            questScore: [
              { required: true, message: '必填项', trigger: 'change' }
            ],
            questCount: [
              { required: true, message: '必填项', trigger: 'change' }
            ],
            typeId: [
              { required: true, message: '必填项', trigger: 'change' }
            ],
            
          },

          catalogData: [],//试题分类数
          defaultProps: {
              parent: 'pid',
              value: 'id',          // 唯一标识
              label: 'name',       // 标签显示
              children: 'children', // 子级
          },

        }
      },
      created() {
        
         //  题型 字典项
        this.getDicts('b_wifi_quest_type').then(response => {
          this.questTypeList = response.data
        })
        //试题类型
        this.selectTree()

       this.title = this.showType == 'createPaperRulesDelShow' ? '组卷规则新增' : (this.showType == 'updatePaperRulesDelShow' ? '组卷规则修改' : '组卷规则查看')

      },
      methods: {

        //查询试题类型id和name
        typeListFunction(){
            selectQuestionIdAndName().then(res=>{
                this.typeList = res.data
            })
        },

        //试题分类树
        selectTree(){
            selectTree().then(response => {
                
                this.catalogData = response.data

                if(this.showType != 'createPaperRulesDelShow'){
                  this.form = this.paperRulesDelData
                }

                this.loading = false
            }).catch(() => {
                this.loading = false
            })
        },

        /**
         * 保存
         */
        save() {
           this.$refs.form.validate(valid => {
          if (valid) {
            this.loading = true;

              if(this.showType == 'createPaperRulesDelShow'){//新增
                this.form.paperRulesId = this.paperRulesDelData.paperRulesId;

                savePaperRulesDel(this.form).then(response => {
                  if (response.code === 0) {
                    this.msgSuccess('添加成功')
                    this.loading = false;
                    this.close();
                    this.form = {};
                    this.$emit('save-success', {
                      type: 'receivables'
                    })
                  }
                }).catch(() => {

                })

              }if(this.showType == 'updatePaperRulesDelShow'){//修改
                updatePaperRulesDelById(this.form).then(response => {
                  if (response.code === 0) {
                    this.msgSuccess('修改成功')
                    this.loading = false;
                    this.close();
                    this.form = {};
                    this.$emit('save-success', {
                      type: 'receivables'
                    })
                  }
                }).catch(() => {

                })

              }
          } else {
            this.$message.error('请完善必填信息')
            return false
          }
        })

        },
        /**
         * 关闭
         */
        close() {
          this.$emit('close')
        }
      }
    }
</script>

<style scoped>
  .el-select{
    width: 100%;
  }
</style>
