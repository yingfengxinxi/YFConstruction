<template>
  <el-form ref="expform" :model="expform" :rules="exprules" label-position="top" label-width="100px"
           class="form">
    <el-row>
      <el-col :span="12">
        <el-form-item label="所属基坑工程" prop="proId">
          <el-select v-model="expform.proId" placeholder="请选择">
            <el-option v-for="pro in proList" :key="pro.id" :label="pro.name" :value="pro.id" />
          </el-select>
        </el-form-item>

      </el-col>
    </el-row>
  </el-form>
</template>

<script>

  import {pitSave, pitDetail, pitDelete} from "./api/pit"

  import {getDictList } from "../../api/eqpro"

  export default {
    /**
     * 必须包含
     * */
    name: 'FormworkExpend',
    components: {},
    props: {
    },
    data() {
      return {
        expform: {},
        exprules: {
          proId: [
            {required: true, message: '必填项不得为空', trigger: 'blur'}
          ],
        },
        type:'20',
        proList: [],
      }
    },
    computed: {},
    watch: {
      eqID: function (val) {
        if(val == -1){
          this.expform = {};
        }else {
          this.getDetail();
        }
      }
    },
    created() {
      getDictList(this.type)
        .then(res => {
          console.log(res)
          this.proList = res.data == null ? [] : res.data;
        })
        .catch(() => {
        })
    },

    mounted() {
    },

    beforeDestroy() {
    },

    methods: {
      /**
       * 获取详情:必要
       * */
      expendInit(eqID) {
        if(eqID){
          pitDetail(eqID)
            .then(res => {
              this.expform = res.data == null ? {} : res.data;
            })
            .catch(() => {
            })
        }else{
          this.expform = {};
        }

      },
      /**
       * 校验:必要
       */
      expendCheck() {
        var flag = false;
        this.$refs['expform'].validate(valid => {
          if (valid) {
            flag = true;
          }
        })
        return flag;
      },
      /**
       * 保存:必要
       */
      expendSave(eqID) {
        var flag = false;
        var params = this.expform;
        if(eqID){
          params.equipmentId = eqID;
        }
        params.type = this.type;
        this.$refs['expform'].validate(valid => {
          if (valid) {
            pitSave(params).then(response => {
              if (response.code === 0) {
                // 保存成功
                flag = true;
              }
            }).catch(err => {
            })
          }
          return flag;
        })
      },
      /**
       * 删除:必要
       */
      expendDelete(eqID) {
        if(eqID){
          pitDelete(eqID)
            .then(res => {
            })
            .catch(() => {
            })
        }

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
  .el-date-editor {
    width: 100%;
  }
</style>
