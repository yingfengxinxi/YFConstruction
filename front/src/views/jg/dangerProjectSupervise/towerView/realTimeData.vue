<template>
  <div class="main_div">
    <div class="spaceSon" style="margin-top: 0.34rem;">
      <div class="words">幅度</div>
      <div class="numbers">15m</div>
      <div class="prop1">
        <div class="prop1_1"><span class="spot"></span></div>
      </div>
    </div>
    <div class="spaceSon" style="margin-top: 0.34rem;">
      <div class="words">高度</div>
      <div class="numbers">25m</div>
      <div class="prop2">
        <div class="prop2_1"><span class="spot"></span></div>
      </div>
    </div>
    <div class="spaceSon" style="margin-top: 0.34rem;">
      <div class="words">转角</div>
      <div class="numbers">23°</div>
      <div class="prop3">
        <div class="prop3_1"><span class="spot"></span></div>
      </div>
    </div>
    <div class="spaceSon">
      <div class="words">吊重</div>
      <div class="numbers">0.8T</div>
      <div class="prop4">
        <div class="prop4_1"><span class="spot"></span></div>
      </div>
    </div>
    <div class="spaceSon">
      <div class="words">力矩</div>
      <div class="numbers">12N·m</div>
      <div class="prop5">
        <div class="prop5_1"><span class="spot"></span></div>
      </div>
    </div>
    <div class="spaceSon">
      <div class="words">风速</div>
      <div class="numbers">2.7m/s</div>
      <div class="prop6">
        <div class="prop6_1"><span class="spot"></span></div>
      </div>
    </div>
    <div class="spaceSon">
      <div class="words">倾角X</div>
      <div class="numbers">0°</div>
      <div class="prop7">
        <div class="prop7_1"><span class="spot"></span></div>
      </div>
    </div>
    <div class="spaceSon">
      <div class="words">倾角Y</div>
      <div class="numbers">0°</div>
      <div class="prop8">
        <div class="prop8_1"><span class="spot"></span></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "RealTimeData",
  components: {},
  props:{
    lzData:Array
  },
  data() {
    return {};
  },
  mounted(){
  	this.remsize()
  },
 	methods:{
 		remsize(){
    	function setRemFontSize() {
        var remSize = window.innerWidth / 19.2;
        document.querySelector("html").style.fontSize = remSize + "px";
      }
      setRemFontSize();
      window.addEventListener("resize", function() {
        setTimeout(function() {
          setRemFontSize();
        }, 200)
      });
    },
 	}
};
</script>
<style lang="scss" scoped>
.main_div {
  width: 8.50rem;
  // border: 1px solid red;
  overflow: hidden;
}
.spaceSon {
  float: left;
  width: 2.12rem;
  height: 0.41rem;
  // border: 1px solid red;
  margin-top: 0.74rem;
  margin-right: 0.35rem;
  margin-left: 0.35rem;
}
.words {
  float: left;
  display: block;
  height: 0.20rem;
  font-size: 14px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #404040;
  line-height: 0.2rem;
}
.numbers {
  float: left;
  display: block;
  width: 0.41rem;
  height: 0.20rem;
  font-size: 14px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #666666;
  line-height: 0.20rem;
}
.prop1 {
  clear: both;
  width: 2.12rem;
  height: 0.11rem;
  background: #e6f2ff;
  border-radius: 12px;
  border: 1px solid #cfe1ff;
  margin-top: 0.25rem;
  .prop1_1 {
    width: 1.56rem;
    height: 0.09rem;
    background: #5b96fa;
    border-radius: 12px;
  }
}

.prop2 {
  clear: both;
  width: 2.12rem;
  height: 0.10rem;
  background: #e6f2ff;
  border-radius: 12px;
  border: 1px solid #d5e5ff;
  margin-top: 0.25rem;
  .prop2_1 {
    width: 156px;
    height: 0.10rem;
    background: #77b8f8;
    border-radius: 12px;
  }
}

.prop3 {
  clear: both;
  width: 2.12rem;
  height: 0.10rem;
  background: #eefff7;
  border-radius: 12px;
  border: 1px solid #bff8dc;
  margin-top: 0.25rem;
  .prop3_1 {
    width: 1.56rem;
    height: 0.10rem;
    background: #57cb91;
    border-radius: 12px;
  }
}

.prop4 {
  clear: both;
  width: 2.12rem;
  height: 0.11rem;
  background: #fff4df;
  border-radius: 12px;
  border: 1px solid #ffe8ba;
  margin-top: 0.25rem;
  .prop4_1 {
    width: 1.56rem;
    height: 0.11rem;
    background: #faad14;
    border-radius: 12px;
  }
}

.prop5 {
  clear: both;
  width: 2.12rem;
  height: 0.10rem;
  background: #fff1ee;
  border-radius: 12px;
  border: 1px solid #ffdad3;
  margin-top: 0.25rem;
  .prop5_1 {
    width: 1.56rem;
    height: 0.10rem;
    background: #f4664a;
    border-radius: 12px;
  }
}

.prop6 {
  clear: both;
  width: 2.12rem;
  height: 0.10rem;
  background: #ffecf9;
  border-radius: 12px;
  border: 1px solid #ffddf5;
  margin-top: 0.25rem;
  .prop6_1 {
    width: 1.56rem;
    height: 0.10rem;
    background: #ff9fe3;
    border-radius: 12px;
  }
}

.prop7 {
  clear: both;
  width: 2.12rem;
  height: 0.11rem;
  background: #f2f0ff;
  border-radius: 12px;
  border: 1px solid #e4e1ff;
  margin-top: 0.25rem;
  .prop7_1 {
    width: 1.56rem;
    height: 0.11rem;
    background: #a89fff;
    border-radius: 12px;
  }
}

.prop8 {
  clear: both;
  width: 2.12rem;
  height: 0.10rem;
  background: #fff1ee;
  border-radius: 12px;
  border: 1px solid #ffe0da;
  margin-top: 0.25rem;
  .prop8_1 {
    width: 1.56rem;
    height: 0.10rem;
    background: #ff9f9f;
    border-radius: 12px;
  }
}

.spot {
  width: 6px;
  height: 6px;
  background: #ffffff;
  border-radius: 50%;
  transform: scale(0.5, 0.5);
  margin-right: 1px;
  margin-top: 1.5px;
  float: right;
}
</style>
