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

    <el-form ref="form" :model="form" :disabled="this.showType == 'detailsShow' ? true : false" :rules="rules" label-width="100px" >
      <el-row>
        <el-col :span="12">
          <el-form-item label="试题类型" prop="questTypeName">
            <el-input :disabled="true" v-model="form.questTypeName" ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="题型" prop="questType">
            <el-select
              v-model="form.questType"
              placeholder="请选择题型">
              <el-option
                v-for="item in questTypeList"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue"/>
            </el-select>
            
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="试题内容" prop="questContent">
            <el-input v-model="form.questContent" 
                      placeholder="请输入试题内容"
                      :row="2"
                      type="textarea"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="试题详解" prop="questExplanation">
            <el-input v-model="form.questExplanation" 
                      placeholder="请输入试题详解"
                      :row="2"
                      type="textarea"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            <el-input v-model="form.remarks" 
                        placeholder="请输入备注" 
                        :row="2"
                        type="textarea"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
    </el-form>

  <span
      slot="footer"
      class="dialog-footer">
      <el-button @click.native="close">取 消</el-button>
      <el-button type="primary" v-show="this.showType == 'detailsShow'?false:true" @click.native="save">确 定</el-button>
    </span>
  </el-dialog>

</template>

<script>

  import {saveQuestion, updateQuestionById }from '@/views/build/wifi/api/question'//试题表

  import {debounce} from 'throttle-debounce'
  
    export default {
      name: "Create",
      components: {
 
      },
      props: {
        questTypeId: {//试题类型id
          type: String,
          require: true
        },
        questTypeName: {//试题类型name
          type: String,
          require: true
        },
        showType: {//新增（createShow）、修改（updateShow）、查看（detailsShow）
          type: String,
          require: true
        },
        menuData: {//修改、查看 时的数据展示
          type: Object,
          default: {}
        },
      },
      data() {
        return {
          loading: false,
          title: "新增试题",
          form: {
            questTypeName:''
          },
          questTypeList:[],//题型(1:单选2:多选3：填空4：问答)

          rules: {
            questType: [
              { required: true, message: '必填项', trigger: 'change' }
            ],
            questContent: [
              { required: true, message: '必填项', trigger: 'change' }
            ],
            questExplanation: [
              { required: true, message: '必填项', trigger: 'change' }
            ],
            
          },
        }
      },
      created() {
        
         //  题型 字典项
        this.getDicts('b_wifi_quest_type').then(response => {
          this.questTypeList = response.data
        })

        this.debouncedSaveField = debounce(300, this.save)
       
       if(this.showType != 'createShow'){
         this.form = this.menuData
       }else{
         this.form.questTypeName = this.questTypeName
       }

       this.title = this.showType == 'createShow' ? '新增试题' : (this.showType == 'updateShow' ? '修改试题' : '查看试题')

      },
      methods: {
        /**
         * 保存
         */
        save() {
          this.$refs.form.validate(valid => {
          if (valid) {
            this.loading = true;

              if(this.showType == 'createShow'){//新增
                this.form.typeId = this.questTypeId;

                saveQuestion(this.form).then(response => {
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

              }if(this.showType == 'updateShow'){//修改
                updateQuestionById(this.form).then(response => {
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
