<template>
  <div v-if="!item.hidden" class="menu-wrapper" @click.stop="pageheader">
    <template
      v-if="
        hasOneShowingChild(item.children, item) &&
          (!onlyOneChild.children || onlyOneChild.noShowingChildren) &&
          !item.alwaysShow
      "
    >
      <el-tooltip
        :disabled="!collapse"
        :content="onlyOneChild.meta.title"
        effect="dark"
        placement="right"
      >
        <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
          <el-menu-item
            :index="resolvePath(onlyOneChild.path)"
            :class="{
              'is-select': activeMenu == resolvePath(onlyOneChild.path),
            }"
          >
            <item
              :icon="onlyOneChild.meta.icon || (item.meta && item.meta.icon)"
              :title="onlyOneChild.meta.title"
              :num="onlyOneChild.meta.num"
              :count="onlyOneChild.count"
              :collapse="collapse"
            />
          </el-menu-item>
        </app-link>
      </el-tooltip>
    </template>

    <el-submenu
      v-else
      ref="subMenu"
      :index="resolvePath(item.path)"
      :class="{ 'is-select': activeMenu == resolvePath(item.path) }"
      popper-append-to-body
    >
      <template slot="title">
        <item
          v-if="item.meta"
          :icon="item.meta && item.meta.icon"
          :title="item.meta.title"
          :num="item.meta.num"
        />
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.name"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        :active-menu="activeMenu"
        class="nest-menu"
      />
    </el-submenu>
  </div>
</template>

<script>
import path from 'path'
import { isExternal } from '@/utils/validate'
import Item from './Item'
import AppLink from './Link'
import FixiOSBug from './FixiOSBug'

export default {
  name: 'SidebarItem',
  components: { Item, AppLink },
  mixins: [FixiOSBug],
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    basePath: {
      type: String,
      default: ''
    },
    activeMenu: String,
    collapse: Boolean
  },
  data() {
    // To fix https://github.com/PanJiaChen/vue-admin-template/issues/237
    // TODO: refactor with render function
    this.onlyOneChild = null
    return {
      activeclass: {}
    }
  },
  methods: {
    pageheader() {
      console.log(this.item,"111111111111111")
      if (this.item.menuType == '1' || this.item.component == '') {
        // 目录
      } else {
        this.activeclass = {
          close: true,
          title: this.item.meta.title,
          comname: this.item.path,
          name: this.item.name
        }
        if (
          JSON.stringify(this.$store.state.app.pageheader).indexOf(
            JSON.stringify(this.activeclass)
          ) == -1
        ) {
          this.$store.state.app.pageheader.push(this.activeclass) // 进行动态的操作
        }
        this.$store.state.app.activeclass = this.item.path
      }
    },
    hasOneShowingChild(children = [], parent) {
      const showingChildren = children.filter((item) => {
        if (item.hidden) {
          return false
        } else {
          // Temp set(will be used if only has one showing child)
          this.onlyOneChild = item
          return true
        }
      })

      // When there is only one child router, the child router is displayed by default
      if (showingChildren.length === 1) {
        return true
      }

      // Show parent if there are no child router to display
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ...parent, path: '', noShowingChildren: true }
        return true
      }

      return false
    },
    resolvePath(routePath) {
      if (isExternal(routePath)) {
        return routePath
      }

      if (isExternal(this.basePath)) {
        return this.basePath
      }
      return path.resolve(this.basePath, routePath)
    }
  }
}
</script>
<style lang="scss" scoped>
@import "./variables.scss";

.menu-wrapper {
  /deep/ .el-submenu__title {
    height: auto;
    line-height: normal;
  }
  /deep/ .el-submenu__title:hover {
    background-color: $menuBg !important;
    .menu-item-content {
      background-color: $menuBg !important;
      color: #409eff;
       i {
        color: #409eff !important;
      }
    }
  }

  /*/deep/ .el-submenu.is-active {
    .el-submenu__title {
      span,
      i:first-child {
        color: white;
      }
    }
  }*/
}

.el-menu-item {
  height: auto;
  line-height: normal;
  padding: 0 14px;
  // background-color: #001529 !important;
  // color: #bebec0;
}

.el-menu-item:not(.is-select) {
  // color: #ffffff;
}

// element自带的有问题 is-active 换成 is-select
.el-menu-item.is-select {
  .menu-item-content {
    background-color: RGBA(64,158,255,0.1) !important;
    color:  #409eff!important;
    /deep/ i {
      color: #409eff !important;
    }
  }
}

/deep/.el-menu-item:hover {
  background-color: rgba($color: #fff, $alpha: 0.1) !important;
  .menu-item-content {
    background-color: rgba($color: #fff, $alpha: 0.1) ;
    color: #409eff;
    .wk {
      color: #409eff;
    }
  }
}
</style>
