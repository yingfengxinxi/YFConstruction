<template>
  <div id="app">
    <router-view class="router-view" v-if="isRouterAlive"/>
    <vue-picture-viewer
      v-if="showPreviewImg"
      :img-data="previewImgs"
      :select-index="previewIndex"
      @close-viewer="showPreviewImg=false"/>
    <xr-import
      v-if="showFixImport"
      :process-status="crmImportStatus"
      @click.native="fixImportClick"/>
    <c-r-m-import
      :show.sync="showCRMImport"
      :crm-type="crmType"
      :props="crmProps"
      :cache-show.sync="cacheShow"
      :cache-done="cacheDone"
      @status="crmImportChange"
      @close="crmImportClose"/>
  </div>
</template>

<script>
/** 常用图片预览创建组件 */
import VuePictureViewer from '@/components/VuePictureViewer/index'
import XrImport from '@/components/XrImport'
import XrImportMixins from '@/components/XrImport/XrImportMixins'
import CRMImport from '@/components/CRMImport'
import { mapGetters } from 'vuex'
import cache from '@/utils/cache'
import disableDevtool from 'disable-devtool';

export default {
  name: 'App',
  components: {
    VuePictureViewer,
    XrImport,
    CRMImport
  },
  mixins: [XrImportMixins],
  data() {
    return {
      showPreviewImg: false,
      previewIndex: 0,
      previewImgs: [],
      isRouterAlive: true  // 控制 router-view 是否显示达到刷新效果
    }
  },
  computed: {
    ...mapGetters(['activeIndex', 'userInfo'])
  },
  watch: {
    $route(to, from) {
      this.showPreviewImg = false // 切换页面隐藏图片预览
    }
  },
  created(){
    // console.log("process.env.DEV_TOOL_FORBID")
    // console.log(process.env.DEV_TOOL_FORBID)
  	if(process.env.NODE_ENV == 'production' && process.env.DEV_TOOL_FORBID){
  		disableDevtool()
	}
  },
  mounted() {
    this.addBus()
    this.addDocumentVisibilityChange()
    this.setMinHeight()
  },
  provide () {  // 在祖先组件中通过 provide 提供变量
    return {
      reload: this.reload  //  声明一个变量
    }
  },
  methods: {
    // provide中声明的变量
    reload () {
      // 通过 this.isRouterAlive 控制 router-view 达到刷新效果
      this.isRouterAlive = false
      this.$nextTick(function () {
        this.isRouterAlive = true
      })
    },
    addDocumentVisibilityChange() {
      // 网页当前状态判断
      // hidden,
      var state, visibilityChange
      if (typeof document.hidden !== 'undefined') {
        // hidden = 'hidden'
        visibilityChange = 'visibilitychange'
        state = 'visibilityState'
      } else if (typeof document.mozHidden !== 'undefined') {
        // hidden = 'mozHidden'
        visibilityChange = 'mozvisibilitychange'
        state = 'mozVisibilityState'
      } else if (typeof document.msHidden !== 'undefined') {
        // hidden = 'msHidden'
        visibilityChange = 'msvisibilitychange'
        state = 'msVisibilityState'
      } else if (typeof document.webkitHidden !== 'undefined') {
        // hidden = 'webkitHidden'
        visibilityChange = 'webkitvisibilitychange'
        state = 'webkitVisibilityState'
      }
      // 添加监听器，在title里显示状态变化
      document.addEventListener(visibilityChange, () => {
        if (document[state] == 'visible') {
          cache.updateAxiosHeaders()
        }
        this.$bus.emit('document-visibility', document[state])
      }, false)
    },

    addBus() {
      var self = this
      this.$bus.on('preview-image-bus', function(data) {
        self.previewIndex = data.index
        self.previewImgs = data.data
        self.showPreviewImg = true
      })
    },

    setMinHeight() {
      this.$nextTick(() => {
        const dpr = window.devicePixelRatio || 1
        const clientWidth = document.body.clientWidth
        const dom = document.getElementById('app')
        if (dpr !== 1 && clientWidth > 1600) {
          dom.style.minHeight = '800px'
        } else if (dpr === 1 && clientWidth > 1600) {
          dom.style.minWidth = '1650px'
        } else {
          // dom.style.minWidth = '1200px'
          dom.style.minHeight = '605px'
        }
      })
    }
  }
}
</script>

<style>
#app {
  width: 100%;
  position: relative;
  height: 100%;
  min-width: 1200px;
  min-height: 605px;
}
</style>
