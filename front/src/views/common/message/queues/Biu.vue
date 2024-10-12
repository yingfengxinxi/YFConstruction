<template>
  <xr-create
    :loading="loading"
    title="发送数据"
    @close="close"
    :showSave="false">

    <el-form ref="form" :model="form" :rules="rules" label-position="top" label-width="100px"
             class="form">
      <create-sections title="Entiy">
        <el-row>
          <el-col :span="12">
            <el-form-item label="接收人" prop="toUsers">
              <el-select v-model="form.toUsers" multiple filterable clearable placeholder="请选择接收人"
                         style="width: 99%">
                <el-option
                  v-for="item in userList"
                  :key="item.userId"
                  :label="item.realname"
                  :value="item.userId">
                </el-option>
              </el-select>
            </el-form-item>

          </el-col>
          <el-col :span="12">
            <el-form-item label="发送人" prop="byUser">
              <el-select v-model="form.byUser" filterable clearable placeholder="请选择发送人" style="width: 99%">
                <el-option
                  v-for="item in userList"
                  :key="item.userId"
                  :label="item.realname"
                  :value="item.userId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="标题(旧版本)" prop="title">
              <el-input type="textarea" :rows="2" v-model="form.title"
                        placeholder="请输入标题，可替换消息类型配置的“标题”中的@title@"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="模板参数" prop="titleParamsStr">
              <el-input type="textarea" :rows="2" v-model="titleParamsStr" placeholder="请输入描述，多个参数用英文逗号隔开"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="内容详情" prop="msgContent">
              <el-input type="textarea" :rows="2" v-model="form.msgContent"
                        placeholder="请输入内容详情"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="传递参数" prop="data">
              <el-input type="textarea" :rows="2" v-model="form.data"
                        placeholder=""></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="text-align: center">
          <el-button type="primary" icon="el-icon-s-promotion" @click="send()">发送</el-button>
        </el-row>
      </create-sections>
      <create-sections title="系统响应">
        <el-row>
          <el-col :span="24">
            <el-form-item label="返回信息">
              <el-input type="textarea" :rows="2" v-model="result" :readonly="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </create-sections>
    </el-form>

  </xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'
  import CreateSections from '@/components/CreateSections'

  import {debounce} from 'throttle-debounce'
  import { userListAPI } from '@/api/common'
  import {sendMsg} from "../api/biu";

  export default {
    // 新建编辑
    name: 'QueuesBiu',
    components: {
      XrCreate,
      CreateSections,
    },
    props: {
      code: {
        type: String,
      }
    },
    data() {
      return {
        radio: '',
        loading: false,
        disabled: false,
        form: {},
        userList: [],
        msgResult: '',
        rules: {
          descr: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
          toUsers: [
            {
              required: true, validator: (rule, value, callback) => {
                if (this.form.toUsers.length < 1) {
                  callback(new Error('必填项不得为空'));
                } else {
                  callback();
                }
              }, trigger: 'blur'
            }
          ],
        },
        titleParamsStr: "",
        result: "",
      }
    },
    computed: {},
    watch: {},
    created() {
      this.debouncedSaveField = debounce(300, this.save)
      this.getUserList();
    },

    mounted() {
    },

    beforeDestroy() {
    },

    methods: {
      getUserList() {
        userListAPI({page:1,limit:9999}).then(response => {
          if (response.code === 0) {
            this.userList = response.data.list;
          } else {
            this.$message.error(
              response.msg
            )
          }
        }).catch(err => {
          console.log(err)
        })
      },
      /**
       * 保存
       */
      send() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            var params = this.form;
            params.code = this.code;
            //处理标题参数
            if(this.titleParamsStr){
              params.titleParams = this.titleParamsStr.split(",");
            }
            this.loading = true;
            sendMsg(params).then(response => {
              this.result = JSON.stringify(response);
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
        console.log("close")
        this.$emit('close')
      }
    }
  }
</script>

<style lang="scss" scoped>

  .form
  /deep/
  .el-form-item__label {
    padding: 0px;
  }

  .form
  /deep/
  .el-date-editor {
    width: 100%;
  }
</style>
