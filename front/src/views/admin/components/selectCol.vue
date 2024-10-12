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
import { custom } from '@/api/admin/dict/type'

export default {
  name: 'SelectCol',
  props: {
    value: [Number, String],
    sTb: String,
    sColKey: String,
    sColVal: String,
    sColName: String,
  },
  data() {
    return {
      // 类型数据字典
      typeOptions: []
    }
  },
  created() {
    custom({sTb: this.sTb,sColKey: this.sColKey,sColVal: this.sColVal,sColName: this.sColName}).then(response => {
          this.typeOptions = response.data
      })
  }
}
</script>

<style scoped>

</style>
