<template>
  <div>
    <el-popover
      v-model="showPopover"
      placement="right-end"
      width="800"
      popper-class="no-padding-popover"
      trigger="click">
      <crm-relative
        v-if="showRelative"
        ref="crmrelative"
        :radio="false"
        :selected-data="selectedData"
        :show-types="showTypes"
        @close="crmrelativeClose"
        @changeCheckout="checkInfos"/>
      <span
        slot="reference"
        class="add-btn" @click="showRelative = true">
        <i class="wk wk-l-plus" />
        <span class="label">关联业务</span>
      </span>
    </el-popover>
    <div class="related-business">
      <div
        v-for="(items, key) in selectedData"
        :key="key">
        <related-business-cell
          v-for="(item, itemIndex) in items"
          :data="item"
          :cell-index="itemIndex"
          :type="key"
          :key="itemIndex"
          :cursor-pointer="false"
          @unbind="delRelevance"/>
      </div>
    </div>
  </div>
</template>
<script type="text/javascript">
import CrmRelative from '@/components/CreateCom/CrmRelative'
import RelatedBusinessCell from '@/views/oa/components/RelatedBusinessCell'
import { objDeepCopy } from '@/utils'
import request from '@/utils/request'

export default {
  name: 'RelatedBusiness', // 关联业务
  components: {
    CrmRelative,
    RelatedBusinessCell
  },
  props: {
    /** 已选信息 */
    selectedInfos: {
      type: Object,
      default: () => {
        return {}
      }
    },
    checkTypes:{
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      showTypes: ['business', 'contract'],
      // 业务弹窗
      showRelative: false,
      showPopover: false,
      // 编辑时勾选
      selectedData: {}
    }
  },
  computed: {},
  watch: {
    selectedInfos: function(data) {
      this.selectedData = data
    }
  },
  mounted() {},
  methods: {
    delRelevance(field, index) {
      this.$confirm('确认取消关联?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        customClass: 'is-particulars'
      })
        .then(() => {
          this.selectedData[field].splice(index, 1)
          this.selectedData = objDeepCopy(this.selectedData)
          this.submitValueChange()
        })
        .catch(() => {
          this.$message.info('已取消操作')
        })
    },
    getTypeName(type) {
      if (type == 'customer') {
        return '客户'
      } else if (type == 'contacts') {
        return '联系人'
      } else if (type == 'business') {
        return '商机'
      } else if (type == 'contract') {
        return '合同'
      }
    },
    crmrelativeClose() {
      this.showPopover = false
    },
    checkInfos(val) {
      for (let index = 0; index < this.checkTypes.length; index++) {
        const checkType = this.checkTypes[index]
        if(checkType == 'business'){
          //商机check：若商机已签订合同，则不允许被选择，只能选择商机对应的合同
          // alert(this.index+"--"+JSON.stringify(val.data))
          if(val.data && val.data.business){
            for (let index2 = 0; index2 < val.data.business.length; index2++) {
              let busId = val.data.business[index2].businessId
              request({
                url: 'crmBusiness/queryContract',
                method: 'post',
                data: {businessId:busId},
                headers: {
                  'Content-Type': 'application/json;charset=UTF-8'
                }
              }).then(res => {
                if(res.data.list.length > 0){
                  this.$message.error("商机“"+val.data.business[index2].businessName+"”已签订合同，请选择商机对应的合同")
                  this.showPopover = true
                  return true
                }
              })
            }
          }
        }
      }
      this.showPopover = false
      this.selectedData = val.data
      this.submitValueChange()
    },
    submitValueChange() {
      this.$emit('value-change', {
        index: this.index,
        value: this.selectedData
      })
    }
  }
}
</script>
<style lang="scss" scoped>

.related-business {
  margin-top: 10px;
}

// 添加btm
$btn-b-color: #f8faff;
$btn-color: #333333;
$btn-b-hover-color: #eff4ff;
.add-btn {
  margin-top: 8px;
  font-size: 12px;
  color: $btn-color;
  background-color: $btn-b-color;
  border-radius: $xr-border-radius-base;
  display: inline-block;
  padding: 3px 10px;
  cursor: pointer;

  .wk-l-plus {
    font-size: 12px;
  }
}

.add-btn:hover {
  color: $xr-color-primary;
  background-color: $btn-b-hover-color;
}
</style>
