<template>
  <div class="main">
    <div class="main-body">
      <el-row style="background-color: #fff">
        <el-col :span="4" style="">
          <el-card class="box-card" style="margin: 10px;height:calc(100vh - 170px);">
            <div slot="header" class="clearfix">
              <span style="font-weight: bold;font-size: 16px">危险源</span>
            </div>
            <el-tree
              :data="catalogData"
              :props="defaultProps"
              :expand-on-click-node="false"
              :highlight-current="true"
              :render-content="renderContent"
              node-key="id"
              default-expand-all/>
          </el-card>
        </el-col>
        <el-col :span="20" >
          <el-card class="box-card" style="margin: 10px;height:calc(100vh - 220px);">
            <el-card v-for="item in dataList" :key="item.id" :body-style="{ padding: '0px' }" style="width: 180px;display: inline-block;margin: 8px">
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'   
                v-src="getImg(item.imgs)"
                :preview-src-list="getIms(item.imgs)"
                style="width: 140px; height: 140px;margin: 18px"/>
              <div style="padding: 0px 14px 14px;">
<!--                <el-tooltip :content="item.problemDescription" class="item" effect="dark" placement="top-start">-->
                  <div class="bottom clearfix">
                    <time class="time" v-bind:title="item.problemDescription">{{ item.problemDescription }}</time>
                    <el-button type="text" class="button" @click="details(item)" >详情</el-button>
                  </div>
<!--                </el-tooltip>-->
              </div>
            </el-card>
          </el-card>
          <div class="p-contianer" style="margin-bottom: 0px">
            <el-pagination
                    :current-page="currentPage"
                    :page-size.sync="pageSize"
                    :total="total"
                    class="p-bar"
                    background
                    layout="prev, pager, next, sizes, total, jumper"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"/>
          </div>
        </el-col>
      </el-row>
    </div>

    <Details
      v-if="detailsShow"
      :id="id"
      @close="detailsShow = false"
      @save-success="getList"
    />
  </div>
</template>

<script>
import { getTree, getList } from './api'
import XrHeader from '@/components/XrHeader'
import Details from './Details'
import { mapGetters } from 'vuex'

export default {
  name: 'dangerPhoto',
  components: {
    XrHeader,
    Details
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      detailsShow: false,
      tableHeight: document.documentElement.clientHeight - 280, // 表的高度
      // 是否显示弹出层
      open: false,
      title: '',
      // 分页
      currentPage: 1,
      pageSize: 10,
      pageSizes: [10, 20, 30, 40],
      total: 0,
      catalogData: [{id:0,dangerName:'全部目录'}],
      defaultProps: {
        children: 'children',
        label: 'dangerName'
      },
      activeName: 'all',
      catalogId: 0,
      isAdd: true,
      id: 0,
      catalogForm: {},
      dataList: [],
      queryParams: {},
      dangerSourceId: null,
      url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
      srcList: [
        'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
        'https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg'
      ]
    }
  },

  created() {
    this.init()
    this.getList()
  },
  computed: {
    ...mapGetters(['imageCache'])
  },
  methods: {
    init() {
      console.log(this.imageCache)
      this.loading = true
      getTree().then(response => {
        console.log('tree', response)
        this.catalogData = this.catalogData.concat(response.data);
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    getList() {
      this.loading = true
      this.queryParams.page = this.currentPage
      this.queryParams.limit = this.pageSize
      this.queryParams.dangerSourceId = this.dangerSourceId
      getList(this.queryParams).then(response => {
        console.log('list', response)
        this.dataList = response.data.list
        this.total = response.data.totalRow
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    getImg(imgData) {
      var url = ''
      if (imgData[0] != undefined) {
        url = imgData[0].url
      }
      return url
    },
    getIms(imgs) {
      var imgList = []
      imgs.forEach(function(element) {
        imgList.push('/api/' + element.url)
      })
      console.log(imgList)
      return imgList
    },
    close() {
      this.detailsShow = false
    },
    details(data) {
      console.log(data)
      this.id = data.id
      this.detailsShow = true
    },
    // 更改每页展示数量
    handleSizeChange(val) {
      this.pageSize = val
      this.getList()
    },
    // 更改当前页数
    handleCurrentChange(val) {
      this.currentPage = val
      this.getList()
    },
    shouc(data) {
      console.log(data)
      if (data.collect == '1') {
        collect(data.id).then(response => {
          if (response.code === 0) {
            this.getList()
          }
        }).catch(() => {})
      } else {
        deleteCollect(data.id).then(response => {
          if (response.code === 0) {
            this.getList()
          }
        }).catch(() => {})
      }
    },
    nodeClick(data) {
      this.dangerSourceId = data.dangerSourceId
      this.getList()
    },
    renderContent(h, { node, data, store }) {
      return (
        <span on-click={ () => this.nodeClick(data) } style='display:block;width:100%;position: relative'>
            <span style='width: 180px;display: block;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;' title={node.label}>{node.label}</span>
        </span>)
    }
  }
}
</script>


<style lang="scss" scoped>
  .main {
    height:100%;
  }
  /deep/ .xr-header {
    padding: 15px 30px;
  }

  .main-body {
    background-color: white;
    /*border-top: 1px solid $xr-border-line-color;*/
    border-bottom: 1px solid $xr-border-line-color;
  }
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }
  .el-aside {
    color: #333;
  }
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
  .custom-tree-but{
    display: inline-block;
    position: relative;
    right: 10px;
    top: 0px;
  }
  .custom-tree-button{
    padding: 3px 10px;
  }
  .xl-but{
    background-color: #CCCCCC;
    color: #000000;
  }
  .time {
    display: inline-block;
    width: 100px;
    height: 13px;
    font-size: 13px;
    color: #999;
    white-space:nowrap;//不换行
    overflow: hidden;//超出隐藏
    text-overflow: ellipsis;//变成...
    line-height: 13px;
  }

  .bottom {
    /*margin-top: 13px;*/
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }
</style>
