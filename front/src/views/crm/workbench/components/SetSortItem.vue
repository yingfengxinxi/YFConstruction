<template>
  <div class="data-statistics card">
    <flexbox class="card-title">
      <div class="card-title-left">
        <span
          :class="icon"
          :style="{ color: iconColor }"
          class="icon" />
        <span class="text">{{ title }}</span>
        <el-button
                v-if="this.editable"
                icon="el-icon-edit"
                size="mini"
                type="text"
                @click.native="edit()"></el-button>
      </div>
      <slot name="header" />
    </flexbox>
    <div class="content">
      <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    :src="img" >
    </div>
  </div>
</template>

<script>

  export default {
  name: 'SetSortItem',
  props: {
    title: String,
    icon: String,
    iconColor: String,
    img: String,
    editable: {
      type: Boolean,
      default: false
    },
    modelId: {
      type: Number,
      default: 0
    },
  },
  data() {
    return {}
  },
  methods: {
    edit(){
      this.$prompt('请输入仪表盘名称', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: this.title,
        inputPattern: /^.{1,20}$/,
        inputErrorMessage: '名称必填，长度小于20'
      }).then(({ value }) => {
        this.$emit('updateModelName',{modelId:this.modelId,modelName:value});
      })

    }
  }
}
</script>

<style scoped lang="scss">
.card {
  background-color: white;
  border-radius: $xr-border-radius-base;
  padding: 10px;
  border: 1px solid #e6e6e6;
  &:hover {
    box-shadow: 0 0 16px 0 rgba(0, 0, 0, 0.08);
    cursor: move;
  }
  &-title {
    position: relative;
    &-left {
      .icon {
        color: #4983ef;
        font-size: 13px;
      }
      .text {
        font-size: 14px;
        font-weight: bold;
        margin-left: 5px;
      }
    }
  }
}

.content {
  text-align: center;
  height: 80%;
  padding-top: 8px;
  overflow: hidden;

  img {
    max-width: 100%;
    height: 100%;
  }
}
</style>
