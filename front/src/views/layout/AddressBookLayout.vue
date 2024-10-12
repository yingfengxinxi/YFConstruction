<template>
  <el-container>
    <!--<el-header class="nav-container">
      <navbar
        nav-index="/addressBook"
        @nav-items-click="navClick"/>
    </el-header>-->
    <el-container>
      <sidebar
        :items="routerItems"
        class="sidebar-container" />

      <el-main
        id="addressBook-main-container"
        style="padding: 0;">
        <app-main/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import {  AppMain, Sidebar } from './components'
import { mapGetters } from 'vuex'

export default {
  name: 'AddressBookLayout',

  components: {
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
    var addressBookRouters = this.routers[this.applicationId];
    this.routerItems = addressBookRouters.children ;
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
  .el-container {
    min-height: 0;
    height: 100%;
  }

  .nav-container {
    padding: 0;
    box-shadow: 0px 1px 2px #dbdbdb;
    z-index: 100;
    min-width: 1200px;
  }
</style>
