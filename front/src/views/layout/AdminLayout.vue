<template>
  <el-container>
    <!--<el-header class="nav-container">
      <navbar nav-index="/manage"/>
    </el-header>-->
    <el-container>
      <sidebar
        :items="routerItems"
        class="sidebar-container" />

      <el-main id="manager-main-container">
        <app-main/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapGetters } from 'vuex'
import { Navbar, Sidebar, AppMain } from './components'

export default {
  name: 'AdminLayout',
  components: {
    Navbar,
    Sidebar,
    AppMain
  },
  data() {
    return {
      routerItems: []
    }
  },

  computed: {
    ...mapGetters(['basePath','manage','applicationId', 'routers'])
  },

  mounted() {
    console.log('88888888888888888888888');
    
    var manageRouters = this.routers[this.applicationId];
    this.routerItems = manageRouters.children;
    //自动选择第一个
    this.$router.push(this.getFirstChild(this.routerItems,this.basePath))
  },
		beforeDestroy(){
			this.$store.state.app.pageheader=[]
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

#manager-main-container {
  max-height: 100%;
  padding: 0px;
}

.el-container {
  overflow: hidden;
  height: 100%;
}

</style>
