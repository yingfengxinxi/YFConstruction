<!--百度地图-->
<template>
<xr-create
    :loading="loading"
    :title="titleMap"
    :showSave="false"
    @close="close">
    
    <baidu-map class="map" :center="{lng: action.longitude, lat: action.latitude}" :zoom="13">
        <bm-marker :position="{lng: action.longitude, lat: action.latitude}" @click="infoWindowOpen()">
        </bm-marker>
        <bm-info-window :show="windowShow" @close="closeClick" :position="{lng: action.longitude, lat: action.latitude}" style="width:100px;height:60px;">
            <div>
                {{'经度： ' + action.longitude }}
            </div>
            <div>
                {{'纬度： ' + action.latitude }} 
            </div>
        </bm-info-window>

        <!-- 控件将定位到地图的右上角 -->
        <bm-scale anchor="BMAP_ANCHOR_TOP_RIGHT"></bm-scale>
        <!-- 右上角平移缩放组件 -->
        <bm-navigation anchor="BMAP_ANCHOR_TOP_RIGHT"></bm-navigation>
        <!-- 右下角缩略图 -->
        <bm-overview-map anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :isOpen="true"></bm-overview-map>
        <!-- 左上角 【地图】【混合】 -->
        <bm-map-type :map-types="['BMAP_NORMAL_MAP', 'BMAP_HYBRID_MAP']"
                        anchor="BMAP_ANCHOR_TOP_LEFT"></bm-map-type>
        
    </baidu-map>

</xr-create>
</template>

<script>

  import XrCreate from '@/components/XrCreate'

  export default {
    name: 'JBaiduMap',
    components: {
      XrCreate,
    },

      props: {
        action:{}
    },
    data () {
      return {
        windowShow:true,
        windowTitle:"信息",
        titleMap:'填报地点',
        loading: false,
      }
    },
    methods: {
        infoWindowOpen(){
            this.windowShow = true;
        },
        closeClick(){
          this.windowShow = false;
        },

        close() {
        this.$emit('close')
      },

      // 百度地图
    //   handler ({
    //     BMap,
    //     map
    //   }) {
    //       console.log(this.action)
    //     map.enableScrollWheelZoom(true) // 开启鼠标滚轮缩放
    //     // this.pt = new BMap.Point(116.117, 39.909)
    //     // 中心点
    //     this.center.lng = this.action.longitude
    //     this.center.lat = this.action.latitude
    //     this.zoom = 11.5
    //   }

    }
  }
</script>

<style scoped>

  .mapBox1 {
    line-height: 550px;
  }
  .map {
    opacity: 0.9;
    margin-right: 10px;
    height: 100%;
  }
  /* .button {
    z-index: 2;
    position: absolute;
    top: 30%;
    left: 88%;
  } */
</style>
