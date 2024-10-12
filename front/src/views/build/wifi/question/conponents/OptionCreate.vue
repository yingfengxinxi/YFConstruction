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

    <el-form ref="form" :model="form" :disabled="this.showQuestType == 'detailsOptionShow' ? true : false" :rules="rules" label-width="100px" >
      <el-row>
        <el-col :span="12">
          <el-form-item label="试题" prop="questContent">
            <el-input :disabled="true" v-model="form.questContent" ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="选项标记" prop="answerFlag">
            <el-input v-model="form.answerFlag" ></el-input>
          </el-form-item>
        </el-col>
        
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="试题选项" prop="options">
            <el-input v-model="form.options" 
                      placeholder="请输入试题选项"
                      :row="2"
                      type="textarea"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="答案标记" prop="optionsFlag">
            <el-select
              v-model="form.optionsFlag"
              placeholder="请选择答案标记">
              <el-option
                v-for="item in optionsFlagList"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue"/>
            </el-select>
            
          </el-form-item>
        </el-col>
      </el-row>
      
    </el-form>
    <span
      slot="footer"
      class="dialog-footer">
      <el-button @click.native="close">取 消</el-button>
      <el-button type="primary" v-show="this.showQuestType == 'detailsOptionShow'?false:true" @click.native="save">确 定</el-button>
    </span>
  </el-dialog>

</template>

<script>

  import {saveQuestOption, updateQuestOptionById }from '@/views/build/wifi/api/questOption'//试题选项表
  
    export default {
      name: "CreateOption",
      components: {
 
      },
      props: {
        questId: {//试题类型id
          type: String,
          require: true
        },
        questContent: {//试题name
          type: String,
          require: true
        },
        showQuestType: {//新增（createOptionShow）、修改（updateOptionShow）、查看（detailsOptionShow）
          type: String,
          require: true
        },
        menuQuestData: {//修改、查看 时的数据展示
          type: Object,
          default: {}
        },
      },
      data() {
        return {
          loading: false,
          title: "新增试题选项",
          form: {
            questId:'',
            questContent:''
          },
          optionsFlagList:[],//答案标记（0:错误，1:正确）

          rules: {
            answerFlag: [
              { required: true, message: '必填项', trigger: 'change' }
            ],
            options: [
              { required: true, message: '必填项', trigger: 'change' }
            ],
            optionsFlag: [
              { required: true, message: '必填项', trigger: 'change' }
            ],
            
          },
        }
      },
      created() {
        
         //  题型 字典项
        this.getDicts('b_wifi_option_flag').then(response => {
          this.optionsFlagList = response.data
        })
       
       if(this.showQuestType != 'createOptionShow'){
         this.form = this.menuQuestData
       }else{
         this.form.questId = this.questId;
       }
       
       this.form.questContent = this.questContent
       this.title = this.showQuestType == 'createOptionShow' ? '新增试题选项' : (this.showQuestType == 'updateOptionShow' ? '修改试题选项' : '查看试题选项')

      },
      methods: {
        /**
         * 保存
         */
        save() {
          this.$refs.form.validate(valid => {
          if (valid) {
            this.loading = true;
          
              if(this.showQuestType == 'createOptionShow'){//新增

                saveQuestOption(this.form).then(response => {
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

              }if(this.showQuestType == 'updateOptionShow'){//修改
                updateQuestOptionById(this.form).then(response => {
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
