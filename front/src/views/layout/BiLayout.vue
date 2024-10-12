<template>
  <el-container>
    <!--<el-header class="nav-container">
      <navbar
        nav-index="/bi"
        @nav-items-click="navClick"/>
    </el-header>-->
    <el-container>
      <sidebar
        :items="routerItems"
        class="sidebar-container"/>
      <el-main id="crm-main-container">
        <app-main/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapGetters } from 'vuex'
import { Navbar, Sidebar, AppMain } from './components'

export default {
  name: 'BiLayout',
  components: {
    Navbar,
    Sidebar,
    AppMain
  },
  data() {
    return {
      routerItems: [],
    }
  },
  computed: {
    ...mapGetters(['basePath','applicationId', 'routers'])
  },
  mounted() {
    var biRouters = this.routers[this.applicationId];
    this.routerItems = biRouters.children;
    //自动选择第一个
    this.$router.push(this.getFirstChild(this.routerItems,this.basePath))
  },
  methods: {
    navClick(index) {},
    getFirstChild(children,path){
      if(children && children.length){
        var one  = children[0];
        var childPath = one.path;
        if(childPath.indexOf("/") == 0){//以/开头
          path = childPath;
        }else{
          path = path + "/" +childPath;
        }
        path = this.getFirstChild(one.children,path);
      }
      return path;
    }
  }
}
</script>

<style lang="scss" scoped>
@import './styles/common.scss';
.aside-container {
  position: relative;
  background-color: #2d3037;
  box-sizing: border-box;
}

.nav-container {
  padding: 0;
  box-shadow: 0px 1px 2px #dbdbdb;
  z-index: 100;
  min-width: 1200px;
}

.el-container {
  overflow: hidden;
  height: 100%;
}
</style>
