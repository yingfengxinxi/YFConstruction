<!--百度地图-->
<template>
  <baidu-map class="map" :center="{lng: 120.369557, lat: 36.094406}" :zoom="12" :scroll-wheel-zoom="true" >
      <bm-point-collection :points="pointsBlue" shape="BMAP_POINT_SHAPE_CIRCLE" color="red" size="BMAP_POINT_SIZE_BIGGER" @mouseover="overHandler" @mouseout="outHandler" @click="clickHandler"></bm-point-collection>

      <!-- 项目信息详情 对话框 -->
        <project-details-data
              v-if="open2"
              :userInfo='userInfo'
              @close="open2 = false"
        ></project-details-data>
    <bm-info-window :position="latLng" title="项目信息" :show="infoWindow.show" >
      <p >项目名称：{{projectName}}</p>
      <p >项目地址：{{projectAddress}}</p>
<!--      <button @click="clear">Clear</button>-->
    </bm-info-window>

  </baidu-map>

</template>

<script>
  import ProjectDetailsData from '../../../crm/workbench/components/projInfoDetail'

  export default {
    name: 'bMap',
    components: {
      ProjectDetailsData
    },

    props:['projectList'],

    data () {
      return {

        loading: false,
        open2: false,

        userInfo:{},
        projectName: '',
        projectAddress: '',
        infoWindow: {
          show: false,
          contents: '1232131231231231231'
        },
        pointsBlue: [],
        latLng: {lng: 120.369557, lat: 36.094406},
        mapStyle: {
          styleJson: [
            {
              "featureType": "all",
              "elementType": "geometry",
              "stylers": {
                  "hue": "#007fff",
                  "saturation": 89,
              }
            },
            // {
            //   "featureType": "water",
            //   "elementType": "all",
            //   "stylers": {
            //       "color": "#ffffff"
            //   }
            // }
          ]
        },

      }
    },

    watch: {

      projectList:function(newName){
        let pointsl = [];

        for (var i = 0; i < newName.length; i++) {
          let position = {lng: Number(newName[i].lng), lat: Number(newName[i].lat),projectId:newName[i].id
            ,projectName:newName[i].projectName,projectAddress:newName[i].projectAddress}
          pointsl.push(position)
        }

        this.pointsBlue = pointsl

      }

    },

    created() {

    },
    methods: {

      clickHandler (e) {
        // console.log(e.point)

        this.userInfo.projectId = e.point.projectId
        this.open2 = true

      },
      overHandler (e) {
        console.log("over===================")
        this.latLng.lng =  e.point.lng
        this.latLng.lat =  e.point.lat
        this.projectName = e.point.projectName
        this.projectAddress = e.point.projectAddress
        this.infoWindow.show = true
      },
      outHandler (e) {
       console.log("out===================")
        this.infoWindow.show = false
      },



    }

  }
</script>

<style scoped lang="scss">

  .mapBox1 {
    line-height: 550px;
  }
  .map {
    opacity: 0.9;
    height: 100%;
  }

  /deep/ .el-dialog {
		height: 700px;
		position: absolute;
		left: 0;
		top: 0;
		right: 0;
		bottom: 0;
		margin: auto;
		margin-top: auto !important;
	}

	/deep/ .el-dialog .el-dialog__body {
		height: 615px;
		overflow: auto;
	}

</style>
