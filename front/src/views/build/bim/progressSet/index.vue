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
        <div class='buttons'>
          <!--          <button type="button" class="button" @click="getComponentProperty">构件属性</button>-->
          <!--          <button type="button" class="button" @click="getFloors">楼层信息</button>-->
          <!--              <el-button class="main-table-header-button xr-btn&#45;&#45;orange" @click="getModelTree">目录树信息</el-button>-->
          <!--              <button type="button" class="button" id="btnOverrideComponentsColorByObjectData"-->
          <!--                      @click="overrideComponentsColorByData">根据条件给构件着色</button>-->
        </div>
        <el-row>
          <el-col :span="20">
            <!-- 定义DOM元素，用于在该DOM元素中显示模型或图纸 -->
            <div class='model' id="domId" style="width:100%; height:720px"></div>
          </el-col>
          <el-col :span="4" style="padding-left: 10px">
            <el-button type="success" @click="completeBIM">进度确认</el-button>
            <el-button type="primary" @click="cancelBIM">取消确认</el-button>
            <div style="width:100%; height:720px;overflow-y: auto">
            <el-tree ref="tree"
                     :data="bimList"
                     :props="defaultProps"
                     node-key="idKey"
                     show-checkbox
                     @node-click="handleNodeClick"></el-tree>
<!--            <el-table-->
<!--                    ref="menuTable"-->
<!--                    :data="bimList"-->
<!--                    style="width: 100%"-->
<!--                    :row-key="rowKey"-->
<!--                    border-->
<!--                    :tree-props="{children: 'items', hasChildren: 'hasChildren'}"-->
<!--                    @selection-change="handleSelectionChange">-->
<!--              <el-table-column-->
<!--                      type="selection"-->
<!--                      width="45">-->
<!--              </el-table-column>-->
<!--              <el-table-column-->
<!--                      prop="actualName"-->
<!--                      label="模型名称"-->
<!--                      width="160">-->
<!--              </el-table-column>-->
<!--            </el-table>-->
            </div>
          </el-col>
        </el-row>
      </div>

    </div>

<!--  </flexbox>-->
</template>
<script>

import XrHeader from '@/components/XrHeader'

import { mapGetters } from 'vuex'

export default {
  name: 'SystemConfig',
  components: {
    XrHeader,
  },
  data() {
    return {
      loading: false,
      // viewToken: '0cc87cae234441e7be3b3ed015cfd344',//TODO id不唯一
      // viewToken: '240e38de6c4342b6aa2727f4f3744616',
      viewToken: '23d28f9737af426fb16f57acc92f1e03',//模型信息--获取明细表
      viewer3D:'',
      app:'',
      viewAdded:false,
      isMapLoaded:false,
      loaderConfig:null,
      ids:[],
      isOverrideComponentsColorByDataActivated : false,
      bimList:[{"id":"0","actualName":"加载中","name":"加载中"}],
      defaultProps: {
        children: 'items',
        label: 'actualName'
      },
      multipleSelection:[],
    }
  },
  mounted() {
    var that=this
    // 初始化显示组件
    var options = new BimfaceSDKLoaderConfig();
    options.viewToken = that.viewToken;
    BimfaceSDKLoader.load(options, that.successCallback, that.failureCallback);
  },
  created() {
  },
  methods: {
    handleSelectionChange(val){
      this.multipleSelection = val;
      console.log("this.multipleSelection==="+this.multipleSelection)
    },
    //行标记
    rowKey(row){
      return row.pid+"_"+row.id;
    },
    handleNodeClick(data) {
      console.log(data);
    },
    handleCheckChange(data, checked, indeterminate) {
      console.log(data, checked, indeterminate);
    },
    setButtonText(btnId, text) {
      var dom = document.getElementById(btnId);
      if (dom != null && dom.nodeName == "BUTTON") {
        dom.innerText = text;
      }
    },
    // 加载成功回调函数
    successCallback(viewMetaData) {
      var that=this
      if (viewMetaData.viewType == "3DView") {
        // ======== 判断是否为3D模型 ========
        // 获取DOM元素
        var dom4Show = document.getElementById('domId');
        var webAppConfig = new Glodon.Bimface.Application.WebApplication3DConfig();
        webAppConfig.domElement = dom4Show;
        // 创建WebApplication
        app = new Glodon.Bimface.Application.WebApplication3D(webAppConfig);
        // 添加待显示的模型
        app.addView(that.viewToken);
        // 从WebApplication获取this.viewer3D对象
        var viewer3D = app.getViewer();
        that.viewer3D = viewer3D;
        // 监听添加view完成的事件
        viewer3D.addEventListener(Glodon.Bimface.Viewer.Viewer3DEvent.ViewAdded, function () {
          //自适应屏幕大小
          window.onresize = function () {
            viewer3D.resize(document.documentElement.clientWidth, document.documentElement
                    .clientHeight - 40);
          }
          // 调用this.viewer3D对象的Method，可以继续扩展功能
          // 渲染3D模型
          viewer3D.render();
          that.getModelTree();
        });
      }
    },
    // 加载失败回调函数
    failureCallback(error) {
      console.log(error);
    },
    getComponentProperty() {
      this.viewer3D.getComponentProperty("-2000011", function (objectdata) {
        alert(JSON.stringify(objectdata));
      });
    },
    getFloors() {
      this.viewer3D.getFloors(function (objectdata) {
        alert(JSON.stringify(objectdata));
      });
    },
    getModelTree() {
      // console.log("==that.getModelTree==");
      var that=this
      this.viewer3D.getModelTree(function (objectdata) {
        // alert(JSON.stringify(objectdata));
        console.log("==="+JSON.stringify(objectdata));
        // that.bimList=that.removeOther(objectdata[0],-1).items;
        that.bimList=that.removeOther(objectdata,-1);
        // console.log("==that.bimList=="+JSON.stringify(that.bimList));
      });
    },
    removeOther(data,pid){
      if(data instanceof Array) {
        for(var i=0;i<data.length;i++){
          data[i] = this.removeOther(data[i],pid)
        }
      }else{
        // console.log("===data start 2==="+JSON.stringify(data))
        if(data.items!=undefined && data.items.length>0){
          data.idKey = pid+"_"+data.id
          // console.log("===data b ==="+JSON.stringify(data))
          if(data.items[0].id == undefined ||data.items[0].type =="family" ){
            delete data.items
            data.elementCount = 0
            // console.log("===data e ==="+JSON.stringify(data))
          }else{
            for(var i=0;i<data.items.length;i++){
              if(data.items[i].id == ""){
                data.items[i].id = new Date().getTime()+""+Math.floor(Math.random() * 100)
              }
              data.items[i] = this.removeOther(data.items[i],data.id)
            }
          }
        }
      }
      return data;
    },
    completeBIM(){
      // console.log("tree=="+this.$refs.tree.getCheckedKeys());
      var list = new Array();
      var selects  = this.$refs.tree.getCheckedKeys()
      for (var i = 0; i < selects.length; i++) {
        var str = selects[i].split("_")[1]
        for(var j = 0; j < this.bimList.length; j++) {
          if(selects[i].split("_")[0] == this.bimList[j].id){
            list.push({"levelName":this.bimList[j].name,"categoryId":str})
          }
        }
      }
      //objectData 有5个字段：categoryId、levelName、specialty、family、famliytype
      var color = new Glodon.Web.Graphics.Color("#90EE90", 0.9);
      //viewer3D.overrideComponentsColorById(["271431", "272632", "267327", "268067"], color);
      this.viewer3D.overrideComponentsColorByObjectData(list, color);
      // this.viewer3D.overrideComponentsOpacityByObjectData(list, 0.8);
      // this.$refs.tree.setCheckedKeys([])
    },
    cancelBIM() {
      console.log("tree"+this.$refs.tree.getCheckedKeys());
      var list = new Array();
      var selects  = this.$refs.tree.getCheckedKeys()
      for (var i = 0; i < selects.length; i++) {
        var str = selects[i].split("_")[1]
        for(var j = 0; j < this.bimList.length; j++) {
          if(selects[i].split("_")[0] == this.bimList[j].id){
            list.push({"levelName":this.bimList[j].name,"categoryId":str})
          }
        }
      }
      this.viewer3D.restoreComponentsColorByObjectData(list);
      this.$refs.tree.setCheckedKeys([])
    },
    overrideComponentsColorByData() {
      if (!this.isOverrideComponentsColorByDataActivated) {
        var color = new Glodon.Web.Graphics.Color("#EE799F", 0.8);
        //设置给构件着色的条件
        this.viewer3D.overrideComponentsColorByObjectData([{
          "levelName": "地坪"
        }], color);
        this.viewer3D.overrideComponentsOpacityByObjectData([{
          "levelName": "地坪"
        }], 0.8);
        this.viewer3D.render();
        this.setButtonText("btnOverrideComponentsColorByObjectData", "恢复颜色");
      } else {
        //恢复颜色
        this.viewer3D.restoreComponentsColorByObjectData([{
          "levelName": "F1"
        }, {
          "floorId": "311"
        }]);
        this.viewer3D.render();
        this.setButtonText("btnOverrideComponentsColorByObjectData", "根据条件给构件着色");
      }
      this.isOverrideComponentsColorByDataActivated = !this.isOverrideComponentsColorByDataActivated;
    },
    // 添加构件嵌套关系
    loadMap() {
      var that=this
      if (!that.viewAdded) {
        return;
      }
      if (!that.isMapLoaded) {
        // 构造地图配置项
        var mapConfig = new Glodon.Bimface.Plugins.TileMap.MapConfig();
        mapConfig.viewer = that.this.viewer3D;
        // 设置模型载入的基点
        mapConfig.basePoint = {
          "x": -85689.088,
          "y": -62066.347
        }
        mapConfig.modelPosition = [121.63259566511255,29.837978723638756];
        mapConfig.modelRotationZ = 0 * Math.PI / 180;
        mapConfig.modelAltitude = 0;
        // 构造地图对象
        var map = new Glodon.Bimface.Plugins.TileMap.Map(mapConfig);
      }
    }
  }
}
</script>

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

