<template>
  <div v-loading="loading" class="rc-cont">
    <flexbox
      class="rc-head"
      direction="row-reverse">
      <!--<el-button
        :disabled="!canEdit"
        class="rc-head-item"
        type="primary"
        @click.native="addFile">上传附件</el-button>-->
      <input
        id="file"
        type="file"
        class="rc-head-file"
        accept="*/*"
        multiple
        @change="uploadFile">
    </flexbox>
    <el-table
      :data="list"
      :height="tableHeight"
      class="file-table"
      stripe
      style="width: 100%;border: 1px solid #E6E6E6;"
      @row-click="handleRowClick">
      <el-table-column
        v-for="(item, index) in fieldList"
        :key="index"
        :prop="item.prop"
        :label="item.label"
        show-overflow-tooltip/>
      <el-table-column
        label="操作"
        width="200">
        <template slot-scope="scope">
          <flexbox>
            <el-button
              type="text"
              @click.native="handleFile('preview', scope)">预览
            </el-button>
            <el-button
              type="text"
              @click.native="handleFile('download', scope)">下载
            </el-button>
            <el-button
              :disabled="scope.row.readOnly == 1 || !canEdit"
              type="text"
              @click.native="handleFile('edit', scope)">重命名
            </el-button>
            <el-button
              :disabled="scope.row.readOnly == 1 || !canEdit"
              type="text"
              @click.native="handleFile('delete', scope)">删除
            </el-button>
          </flexbox>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      :append-to-body="true"
      :close-on-click-modal="false"
      :visible.sync="editDialog"
      title="编辑"
      width="30%">
      <el-form :model="editForm">
        <el-form-item
          label="新名称"
          label-width="100">
          <el-input
            v-model="editForm.name"
            autocomplete="off"/>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer">
        <el-button @click="editDialog = false">取 消</el-button>
        <el-button
          type="primary"
          @click="confirmEdit">确 定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script type="text/javascript">
  import {crmFileDeleteAPI, crmFileUpdateAPI, downloadFileAPI} from '@/api/common'

  import {adminFileQueryFileListAPI} from '@/api/admin/file'

  import {fileSize, canPreviewFile, wkPreviewFile, downloadFileWithBuffer} from '@/utils'
  import {debounce} from 'throttle-debounce'

  export default {
    name: 'RelativeFiles', // 相关附件  可能再很多地方展示 放到客户管理目录下
    components: {},
    props: {
      /** 模块ID */
      batchId: String,
    },
    data() {
      return {
        loading: false,
        list: [],
        fieldList: [],
        tableHeight: 'calc(100% - 15px)',
        /** 重命名 弹窗 */
        editDialog: false,
        /** 编辑信息 */
        editForm: {name: '', data: {}}
      }
    },
    inject: ['rootTabs'],
    computed: {
      //是否可以修改
      canEdit() {
        if (this.crmType == "expense") {//费用
          //判断详情状态
          var detail = this.detail;
          if (detail.hasExamine != '0' && detail.checkStatus != "2" && detail.checkStatus != "4" && detail.checkStatus != "5") {
            return false;
          }
        }
        return false;
      }
    },
    watch: {
      batchId(val) {
        this.list = []
        this.getDetail()
      },

      'rootTabs.currentName'(val) {
        this.getDetail(false)
      }
    },
    mounted() {
      this.fieldList.push({prop: 'name', width: '300', label: '附件名称'})
      this.fieldList.push({prop: 'size', width: '100', label: '附件大小'})
      this.fieldList.push({
        prop: 'createUserName',
        width: '100',
        label: '上传人'
      })
      this.fieldList.push({
        prop: 'createTime',
        width: '200',
        label: '上传时间'
      })
      this.getDetail()

      this.debouncedGetDetail = debounce(300, () => {
        this.$bus.emit('crm-tab-num-update')
        this.getDetail()
      })
    },
    methods: {
      //TODO 附件类型初始化
      getDetail(loading = true) {
        this.loading = loading
        adminFileQueryFileListAPI(this.batchId)
          .then(res => {
            this.loading = false
            this.list = res.data.map(item => {
              item.size = fileSize(item.size)
              return item
            })
          })
          .catch(() => {
            this.loading = false
          })
      },
      addFile() {
        document.getElementById('file').click()
      },
      /** 图片选择出发 */
      uploadFile(event) {
        var files = event.target.files
        for (let index = 0; index < files.length; index++) {
          const file = files[index]
          var params = {}
          params.batchId = this.detail.batchId
          params.file = file
          this.$wkUploadFile.upload({
            file: file,
            params: {
              batchId: this.detail.batchId
            }
          }).then(completeData => {
            this.debouncedGetDetail()
          }).catch(() => {
          })
        }

        event.target.value = ''
      },
      // 当某一行被点击时会触发该事件
      handleRowClick(row, column, event) {
      },

      showPreviewBtn(item) {
        return canPreviewFile(item.row.name)
      },

      /**
       * 编辑删除cell
       */
      handleFile(type, item) {
        if (type === 'preview') {
          if (canPreviewFile(item.row.name)) {
            wkPreviewFile(item.row.url, item.row.name)
          } else {
            this.$bus.emit('preview-image-bus', {
              index: item.$index,
              data: this.list
            })
          }
        } else if (type === 'delete') {
          this.$confirm('您确定要删除该文件吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
            .then(() => {
              crmFileDeleteAPI({
                id: item.row.fileId
              })
                .then(res => {
                  this.list.splice(item.$index, 1)
                  this.$bus.emit('crm-tab-num-update')
                  this.$message.success('操作成功')
                })
                .catch(() => {
                })
            })
            .catch(() => {
            })
        } else if (type === 'download') {
          downloadFileAPI(item.row.url).then(res => {
            const blob = new Blob([res.data], {
              type: ''
            })
            downloadFileWithBuffer(blob, item.row.name)
          }).catch(() => {
          })
        } else {
          this.editForm.data = item
          this.editForm.name = item.row.name
          this.editDialog = true
        }
      },
      confirmEdit() {
        if (this.editForm.name) {
          crmFileUpdateAPI({
            fileId: this.editForm.data.row.fileId,
            name: this.editForm.name
          })
            .then(res => {
              this.$message.success('编辑成功')
              this.editDialog = false
              var item = this.list[this.editForm.data.$index]
              item.name = this.editForm.name
            })
            .catch(() => {
            })
        }
      }
    }
  }
</script>
<style lang="scss" scoped>
  @import '@/views/crm/styles/relativecrm.scss';

  .h-item {
    font-size: 13px;
    color: #409eff;
    margin: 0 5px;
    cursor: pointer;
  }

  .rc-head-file {
    position: absolute;
    top: 0;
    right: 0;
    height: 98px;
    width: 98px;
    opacity: 0;
    z-index: -1;
    cursor: pointer;
  }
</style>

