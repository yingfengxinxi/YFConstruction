<template>
  <create-sections title="塔吊历史数据信息">
    <el-table
      :data="lzData"
      empty-text=""
      style="width: 100%"
    >
      <el-table-column
        label="监测时间"
        align="center"
        prop="monitorTime"
        width="160"
      />
      <el-table-column
        label="幅度"
        align="center"
        prop="fd"
      />
      <el-table-column
        label="高度"
        align="center"
        prop="gd"
      />
      <el-table-column
        label="转角"
        align="center"
        prop="zj"
      />
      <el-table-column
        label="吊重"
        align="center"
        prop="dz"
      />
      <el-table-column
        label="力矩"
        align="center"
        prop="lj"
      />
      <el-table-column
        label="风速"
        align="center"
        prop="fs"
      />
      <el-table-column
        label="倾角X"
        align="center"
        prop="qjx"
      />
      <el-table-column
        label="倾角Y"
        align="center"
        prop="qjy"
      />
    </el-table>
  </create-sections>
</template>

<script>
import CreateSections from '@/components/CreateSections'

export default {
  name: "historyDataTable",
  components: {
    CreateSections
  },
  props:{
    lzData:Array
  },
  data() {
    return {

    }
  }
};
</script>
<style scoped>
</style>
