<template>
  <flexbox
    class="main"
    direction="column"
    align="stretch">
    <!--<xr-header
      icon-class="wk wk-all"
      icon-color="#FD964A"
      label="应用管理" />-->
    <div
      v-loading="loading"
      class="body">
      <div
        v-for="(bigItem, bigIndex) in allList"
        :key="bigIndex"
        class="section">
        <flexbox class="section-header">
          {{ bigItem.name }}
        </flexbox>
        <flexbox
          wrap="wrap"
          class="section-body">
          <flexbox
            v-for="(item, index) in bigItem.sublist"
            :key="index"
            class="section-item">
            <i :class="item.icon" />
            <span class="item-name">{{ item.menuName }}</span>
            <el-dropdown
              class="more-menu"
              @command="handleMoreCommand($event, item)">
              <i class="el-icon-more"/>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="item.status ? 'disable' : 'enable'">{{ item.status ? '停用' : '启用' }}</el-dropdown-item>
                <el-dropdown-item command="edit">编辑</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </flexbox>
        </flexbox>
      </div>
    </div>
  </flexbox>
</template>

<script>
import {
  adminConfigsetIndexAPI,
  adminConfigsetUpdateAPI
} from '@/api/admin/application'


import XrHeader from '@/components/XrHeader'
import {
  mapGetters
} from 'vuex'

export default {
  /** 系统管理 的 应用管理 */
  name: 'SystemModule',
  components: {
    XrHeader
  },
  mixins: [],
  data() {
    return {
      loading: false,
      allList: [
        {
          name: '已启用应用',
          type: 1,
          status: 1,
          sublist: []
        },
        {
          name: '已停用应用',
          type: 1,
          status: 0,
          sublist: []
        }
      ]
    }
  },
  computed: {
    ...mapGetters(['manage'])
  },
  mounted() {
    this.getDetail()
  },
  methods: {
    editMenu(item){
      this.$router.push({
        path: this.$route.fullPath + '/' +item.menuId,
      })
    },
    /**
     * 详情
     */
    getDetail() {
      this.loading = true
      adminConfigsetIndexAPI()
        .then(res => {
          //console.log(res)
          this.loading = false
          var appList = res.data;
          for (let i = 0; i < appList.length; i++) {
            var app = appList[i];
            if (app.status == 1){
              this.allList[0].sublist.push(app);
            }else{
              this.allList[1].sublist.push(app);
            }
          }
          console.log(this.allList)
        })
        .catch(() => {
          this.loading = false
        })
    },

    /**
     * 更多操作
     */
    handleMoreCommand(command, item) {
      if(command == 'edit'){
        this.editMenu(item);
        return;
      }
      this.getConfirmMessage(command, item, () => {
        this.loading = true
        adminConfigsetUpdateAPI({
          menuId: item.menuId,
          status: command == 'disable' ? 0 : 1
        })
          .then(res => {
            /*this.$message({
              type: 'success',
              message: '设置成功'
            })*/
            this.loading = false
            window.location.reload()
          })
          .catch(() => {
            this.loading = false
          })
      })
    },

    /**
     * 操作提示
     */
    getConfirmMessage(command, item, result) {
      if (command == 'enable') {
        result()
      } else {
        this.$confirm(`停用${item.menuName}后，企业所有员工将无法使用此功能。确定要停用吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            result()
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消操作'
            })
          })
      }
    },

  }
}
</script>

<style lang="scss" scoped>
.main {
  height: 100%;
}

.body {
  flex: 1;
  overflow-y: auto;
  padding-top: 20px;
  background-color: white;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
}

.section-header {
  padding: 3px 20px;
  font-size: 12px;
  color: #999;
}

.section-body {
  padding: 20px;
  .section-item {
    width: auto;
    min-width: 240px;
    position: relative;
    padding: 12px 35px 12px 20px;
    border: 1px solid #ebeef5;
    border-radius: $xr-border-radius-base;
    margin-right: 15px;
    margin-bottom: 10px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    vertical-align: middle;
    .item-icon {
      width: 40px;
      height: 40px;
      margin-right: 15px;
    }
    .item-name {
      font-size: 14px;
      color: #333333;
    }
  }
}

.el-icon-more {
  color: #cdcdcd;
  transform: rotate(90deg);
  cursor: pointer;
}

.more-menu {
  position: absolute;
  top: 8px;
  right: 8px;
}

.detail-button {
  position: absolute;
  top: 0px;
  right: 8px;
  color: #999;
  font-size: 12px;
}

.more-mark {
  color: #b3884f;
  font-size: 12px;
  background: linear-gradient(to right, #f9f3ed, #f8e1be);
  padding: 5px;
  border-radius: 2px;
  transform: scale(0.8);
  position: absolute;
  top: 6px;
  right: 4px;
}
</style>
