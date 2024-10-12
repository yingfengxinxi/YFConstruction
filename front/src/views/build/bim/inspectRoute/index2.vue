<template>
<!--  <flexbox-->
<!--    class="main"-->
<!--    direction="column"-->
<!--    align="stretch">-->
<!--    <xr-header-->
<!--      icon-class="wk wk-enterprise"-->
<!--      icon-color="#2362FB"-->
<!--      label="BIM" />-->
    <div
      v-loading="loading"
      class="body">
      <!-- 基本信息 -->
      <div class='main'>
        <el-row>
          <el-col :span="24">
            <!-- 定义DOM元素，用于在该DOM元素中显示模型或图纸 -->
<!--            <div class='model' id="domId" style="width:100%; height:720px"></div>-->
<!--            <iframe src="http://101.200.143.182:8889/bim.html" style="width:100%; height:720px"></iframe>-->
            <iframe src="static/inspectRoute.html" style="border:0px; width:100%; height:720px"></iframe>
          </el-col>
        </el-row>
      </div>

    </div>

</template>

<style lang="scss" scoped>
.main {
  padding: 0px;
  height: 100%;
}

.body {
  background-color: white;
  border-bottom: 1px solid $xr-border-line-color;
  flex: 1;
  /*overflow-y: auto;*/
  padding: 10px;
  background-color: white;
  border-radius: $xr-border-radius-base;
  position: relative;
}

</style>

