<template>
  <el-select :value="value" @input="$emit('input', $event)" size="small">
    <el-option
      v-for="item in typeOptions"
      :key="item.dictId"
      :label="item.dictName"
      :value="item.dictType"
    />
  </el-select>
</template>

<script>
import { custom } from '../api'

export default {
  name: 'DiyDict',
  props: {
    value: [Number, String],
    sTb: String,
    sColKey: String,
    sColVal: String,
    sColName: String,
    sOrderBy: String,
    sWhere: String,
  },
  data() {
    return {
      // 类型数据字典
      typeOptions: []
    }
  },
  created() {
    var data = {sTb: this.sTb,sColKey: this.sColKey,sColVal: this.sColVal,sColName: this.sColName};
    data.sOrderBy = this.sOrderBy;
    data.sWhere = this.sWhere;
    custom(data).then(response => {
          this.typeOptions = response.data
      })
  }
}
</script>

<style scoped>
  .el-select{
    width: 100%;
  }
</style>
