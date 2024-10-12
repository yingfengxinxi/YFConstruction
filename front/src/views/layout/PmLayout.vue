<template>
  <el-container>
    <el-container>
      <sidebar
        :items="routerItems"
        :create-button-title="permissonProject ? '创建项目' : ''"
        create-button-icon="el-icon-plus"
        @quicklyCreate="quicklyCreate"
        @select="siderbarSelect"/>

      <el-main id="project-container">
        <app-main/>
        <tag-board :visible.sync="tagBoardShow" />
      </el-main>
    </el-container>
    <add-project
      v-if="isCreate"
      @save-success="createSuccess"
      @close="createClose"/>
  </el-container>
</template>

<script>
import { mapGetters } from 'vuex'
import { Navbar, Sidebar, AppMain } from './components'
import AddProject from '@/views/pm/components/AddProject'
import TagBoard from '@/views/pm/tag/TagBoard'

export default {
  name: 'PmLayout',
  components: {
    Navbar,
    Sidebar,
    AppMain,
    AddProject,
    TagBoard
  },
  data() {
    return {
      isCreate: false,
      tagBoardShow: false,
      routerItems:[],
    }
  },
  computed: {
    ...mapGetters(['basePath','project', 'applicationId', 'routers']),
    permissonProject() {
      return (
        this.project &&
        this.project.projectManage &&
        this.project.projectManage.save
      )
    }
  },
  created() {
  },
  mounted() {
    var pmRouters = this.routers[this.applicationId];
    this.routerItems = pmRouters.children;
    this.$router.push(this.getFirstChild(this.routerItems,this.basePath))
  },
  beforeDestroy() {
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
    },
    siderbarSelect(key, keyPath) {
      if(key == '/project/tag-list') {
        this.tagBoardShow = true
      } else {
        this.tagBoardShow = false
      }
    },

    quicklyCreate() {
      this.tagBoardShow = false
      this.isCreate = true
    },
    createSuccess() {
      // null
    },
    createClose() {
      this.isCreate = false
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

#project-container {
  max-height: 100%;
  position: relative;
}

.el-container {
  overflow: hidden;
  height: 100%;;
}
</style>
