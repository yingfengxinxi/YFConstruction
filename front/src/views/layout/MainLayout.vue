<template>
  <el-container>
    <el-header class="nav-container">
      <navbar :nav-index="navActiveIndex"/>
    </el-header>
    <router-view />
  </el-container>
</template>

<script>
  import { mapGetters } from 'vuex'
  import { Navbar } from './components'

  export default {
    name: 'AdminLayout',
    components: {
      Navbar,
    },
    //mixins: [socket],
    data() {
      return {
      }
    },

    computed: {
      ...mapGetters(['navActiveIndex','activeIndex'])
    },

    mounted() {
      //路由
    },

    methods: {
      navClick(index) {}
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
    padding:0;
  }

  .el-container {
    overflow: hidden;
    height: 100%;
  }

</style>
