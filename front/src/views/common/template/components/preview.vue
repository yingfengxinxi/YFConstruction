<template>

  <el-container
    v-loading="loading"
    :class="{ 'is-perview': isPreview }"
    class="wk-print-container">
    <el-header>
      <template v-if="isPreview">
        <el-button type="primary" @click="handleClick('download')">{{ `${perviewTypeName} 下载` }}</el-button>
        <el-button type="primary" @click="handleClick('print')">打印</el-button>
        <el-button @click="handleClick('goback')">取消</el-button>
      </template>
      <template v-else>
        <!--<span class="select-label">选择模版</span>
        <el-select
          v-model="templateId"
          style="width: 170px;"
          class="handle-item-content"
          @change="getDetail">
          <el-option
            v-for="item in templateOptions"
            :key="item.templateId"
            :label="item.templateName"
            :value="item.templateId"/>
        </el-select>-->
        <span class="select-label">选择格式</span>
        <el-select
          v-model="perviewType"
          style="width: 80px;"
          class="handle-item-content">
          <el-option
            v-for="item in previewOptions"
            :key="item"
            :label="item"
            :value="item"/>
        </el-select>
        <el-button class="preview-button" type="primary" @click="printPreviewClick">打印/预览</el-button>
        <el-button
          class="back-button"
          type="normal"
          icon="el-icon-back"
          @click="back" >返回</el-button>
      </template>
    </el-header>
    <el-main>
      <div class="main-container">
        <div class="main-content">
          <iframe
            v-show="isPreview"
            id="wkPrint"
            :height="iframeHeight"
            :src="iframeUrl"
            style="width: 100%;margin-top: 54px;"
            scrolling="no"
            frameborder="0"/>
        </div>
        <div v-show="!isPreview" class="main-tips">您可以在当前页面选择文件格式，点击打印/预览按钮展示文件具体内容。</div>
        <!-- <div v-show="!isPreview" class="main-content">
          <tinymce
            ref="editor" :toolbar="[]" v-model="content" :init="{
              statusbar: false,
              plugins: 'print autoresize',
              extended_valid_elements: 'span[class|title|wktag|style|contenteditable]',
              content_style: ' body {padding: 60px !important;width: 595px; margin: 0 auto;} p { margin: 5px 0;}',
          }"/>
        </div> -->
      </div>
    </el-main>
  </el-container>

</template>

<script>
  import {
    printPrintAPI,
    printRecordDetailAPI,
    printSaveRecordAPI,
		printPreviewByFM
  } from '../api'
  import {crmFileDownByPathAPI, downloadFileAPI} from '@/api/common'
  import Tinymce from '@/components/Tinymce'

  import {downloadFileWithBuffer} from '@/utils'

  export default {
    name: 'Print',
    components: {
      Tinymce,
    },
    watch: {
    },
    data() {
      return {
        loading: false,
        isPreview: false, // 是编辑 还是打印预览
        templateOptions: [],
        previewOptions: ['word', 'pdf'],
        perviewType: 'word',
        content: '',
        historyData: null,

        // 预览
        iframeUrl: '',
        iframeHeight: document.documentElement.clientHeight - 170,
        srcData: null,
        //参数
        printParams:{}
      }
    },
    computed: {
      perviewTypeName() {
        return {
          word: 'Word',
          pdf: 'PDF'
        }[this.perviewType]
      }
    },
    created() {
      this.printParams = this.$route.query.printParams;
      window.addEventListener('resize', this.changeIframeHeight)
    },
    beforeRouteUpdate(to, from, next) {
      this.content = ''
      next()
    },
    beforeDestroy() {
      window.removeEventListener('resize', this.changeIframeHeight)
    },
    methods: {
      changeIframeHeight() {
        this.iframeHeight = document.documentElement.clientHeight - 170
      },

      /**
       * 打印
       */
      compactPrint() {
        this.$refs.editor.editor.execCommand('mcePrint')
        this.savePrintRecord()
      },
      /**
       * 打印预览
       */
      printPreviewClick() {
        this.loading = true
		const url = this.printParams.url
        printPreviewByFM(url, this.printParams)
          .then(res => {
            this.loading = false
            const data = res.data
            const iframeUrl = `/utilsPrint/preview.pdf?type=1&key=${data}`
            downloadFileAPI(iframeUrl).then(res => {
              this.iframeUrl = window.URL.createObjectURL(res.data)
            }).catch(() => {
            })

            this.srcData = data
            this.isPreview = true
          })
          .catch(() => {
            this.loading = false
          })
      },

      /**
       *保存打印记录
       */
      savePrintRecord() {
        if (this.templateId && this.id) {
          printSaveRecordAPI({
            templateId: this.templateId,
            typeId: this.id,
            recordContent: this.content
          })
            .then(res => {
            })
            .catch(() => {
            })
        }
      },
      back() {
        this.$router.go(-1)
      },
      hidenView() {
        this.$emit('close')
      },

      /** 预览页面逻辑 */

      handleClick(type) {
        if (type == 'goback') {
          this.isPreview = false
        } else if (type == 'print') {
          document.getElementById('wkPrint').contentWindow.print() // 调用浏览器的打印功能打印指定区域
          this.savePrintRecord()
        } else if (type == 'download') {
          if (this.srcData) {
            var ext = this.perviewType
            const path = this.srcData
            var data = {
              key: path,
              type: ext === 'word' ? 2 : 1
            }
            ext = ext === 'word' ? 'doc' : 'pdf'
            crmFileDownByPathAPI(data).then(res => {
              const blob = new Blob([res.data], {
                type: ''
              })
              downloadFileWithBuffer(blob, `文档.${ext}`)
            }).catch(() => {
            })
          }
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .wk-print-container {
    .el-header {
      background-color: white;
      box-shadow: 0px 1px 2px #dbdbdb;
      line-height: 60px;
      padding: 0 70px;
      z-index: 1;
      position: relative;
      border-top: solid #e8e8e8 2px;
      .select-label {
        margin-right: 15px;
      }

      .el-select + .select-label {
        margin-left: 30px;
      }
    }

    .preview-button {
      position: absolute;
      right: 140px;
      top: 16px;
    }
    .back-button {
      position: absolute;
      right: 40px;
      top: 16px;
    }
    .el-main {
      padding: 0;
    }

    &.is-perview {
      .el-header {
        text-align: right;
      }
    }
  }

  .main-container {
    height: 100%;
    overflow: auto;

    .main-tips {
      text-align: center;
      font-size: 13px;
      margin: 20px 0;
      color: #BFBFBF;
    }

    .main-content {
      width: 90%;
      margin: 0 auto;
    }
  }

</style>
