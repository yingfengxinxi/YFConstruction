<template>
  <div class="body">
    <div class="nat_dom_l ">
    	<div :class="type == 6?'nat_but nat_but_xz':'nat_but'" @click="equipmentSwitch(6)">
        <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="nat_but_img" :src="type == 6?require('./img/zlimg.png'):require('./img/zlimg1.png')">
        <p class="nat_but_text">总览</p>
      </div>
      <div :class="type == 1?'nat_but nat_but_xz':'nat_but'" @click="equipmentSwitch(1)">
        <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="nat_but_img" :src="type == 1?require('./img/tdanb.png'):require('./img/tdanl.png')">
        <p class="nat_but_text">塔吊</p>
      </div>
      <div :class="type == 2?'nat_but nat_but_xz':'nat_but'" @click="equipmentSwitch(2)">
        <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="nat_but_img" :src="type == 2?require('./img/sjjanb.png'):require('./img/sjjanl.png')">
        <p class="nat_but_text">升降机</p>
      </div>
      <div :class="type == 3?'nat_but nat_but_xz':'nat_but'" @click="equipmentSwitch(3)">
        <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="nat_but_img" :src="type == 3?require('./img/dganb.png'):require('./img/dganl.png')">
        <p class="nat_but_text">吊钩可视化</p>
      </div>
      <div :class="type == 4?'nat_but nat_but_xz':'nat_but'" @click="equipmentSwitch(4)">
        <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="nat_but_img" :src="type == 4?require('./img/sjkanb.png'):require('./img/sjkanl.png')">
        <p class="nat_but_text">深基坑</p>
      </div>
      <div :class="type == 5?'nat_but nat_but_xz':'nat_but'" @click="equipmentSwitch(5)">
        <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="nat_but_img" :src="type == 5?require('./img/gzmanb.png'):require('./img/gzmanl.png')">
        <p class="nat_but_text">高支模</p>
      </div>
    </div>
    <div class="main">
      <TowerCraneOverview v-if="type == 1"/>
      <ElevatorOverview v-if="type == 2"/>
      <HookOverview v-if="type == 3"/>
      <PitOverview v-if="type == 4"/>
      <HightOverview v-if="type == 5"/>
      <allOverview v-if="type == 6"/>
    </div>
  </div>
</template>

<script>
  import TowerCrane from './towerCrane'
  import TowerCraneOverview from './towerCraneOverview'
  import ElevatorOverview from './elevatorOverview'
  import PitOverview from './pitOverview'
  import HightOverview from './hightOverview'
	import HookOverview from './hookOverview'
	import allOverview from './allOverview'
  export default {
    name: "equView",
    components: {
      TowerCrane,
      TowerCraneOverview,
      ElevatorOverview,
      PitOverview,
      HightOverview,
      HookOverview,
      allOverview
    },
    data() {
      return {
        type: '6',

      }
    },
    created() {

    },
    mounted() {

    },
    methods: {
      equipmentSwitch(val) {
        this.type = val
      }
    }
  }
</script>

<style scoped lang="scss">
  .body {
    width: 100%;
    height: 100%;
    margin: 0;
    padding: 10px;
  }

  .nat_dom_l {
    display: inline-block;
    width: 69px;
    float: left;
    margin-right: 20px;
    height: 500px;
    margin-top: 10px;
  }

  .nat_but {
    margin: 0px 0px 20px 0px;
    text-align: center;
    width: 100%;
    background: #FDFDFF;
    box-shadow: 0px 12px 18px 0px rgba(156, 177, 222, 0.47);
    border-radius: 8px;
    padding-bottom:19%;
    p {
      color: #649BF8;
    }
  }

  .nat_but_img {
    width: 24px;
    height: 24px;
    margin: 14px 22px 0px;
  }

  .nat_but_text {
    height: 17px;
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    line-height: 17px;
  }

  .nat_but_xz {
    background-color: #649BF8;
    color: #ffffff;
    p {
      color: white;
    }
  }

  .main {
    display: inline-block;
    width: calc(100% - 90px);
    float: right;
    height: 100%;
  }


</style>
