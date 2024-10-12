<template>
  <div
    class="login-wrapper">
    <div class="top-nav">
    </div>
    <div class="container">
     	<div class="left">
        <!--<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'   
          class="img_login"
          src="~@/assets/login/icon-portrait.png"
          alt="">
        <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'   
          src="~@/assets/login/login_left.png"
          alt=""
          class="main-pic">-->
      </div>
      <div class="right">

        <div class="login-main-content">
          <!--<div class="logo-box">
            {{ titleMap[activeCom] }}
          </div>-->
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="tit_img" src="~@/assets/login/tit_img.png" />
					<div class="tips_msg">你好，欢迎登录</div>					
          <component
            :is="activeCom"/>

          <!--<div class="use-tip">
            <div>
              <span class="text">建议使用</span>
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="~@/assets/login/chrome.png" alt="" class="icon">
              <span class="text">Chrome、</span>
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="~@/assets/login/firefox.png" alt="" class="icon">
              <span class="text">火狐、</span>
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="~@/assets/login/safari.png" alt="" class="icon">
              <span class="text">Safari浏览器</span>
            </div>
            <div>
              <span class="text">或</span>
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="~@/assets/login/360.png" alt="" class="icon">
              <span class="text">360、</span>
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="~@/assets/login/sougou.png" alt="" class="icon">
              <span class="text">搜狗、</span>
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="~@/assets/login/qq.png" alt="" class="icon">
              <span class="text">QQ等浏览器的</span>
              <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="~@/assets/login/fast.png" alt="" class="icon">
              <span class="text">极速模式</span>
            </div>
          </div>-->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LoginByPwd from './component/LoginByPwd'

export default {
  name: 'Login',
  components: {
    LoginByPwd
  },
  data() {
    return {
      activeCom: 'LoginByPwd',
      titleMap: {
        LoginByPwd: '欢迎登录'
      }
    }
  },
  watch: {},
  created() {
  },
  methods: {}
}
</script>

<style scoped lang="scss">
@import 'index';
.img_login{
  position: relative;
  left: 20px;
  top: 20px;
  width: 100px;
  opacity: 0.7;
}
.login-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  background: url('~@/assets/login/login_bg3.png') center no-repeat;
  background-size: cover;
  display: flex;
  flex-direction: column;
  overflow: auto;

  .top-nav {
    width: 100%;
    padding: 23px 2.8% 0;
    display: flex;
    img {
      width: 204px;
      height: 50px;
    }
    // @media screen and (max-width: 1550px) {
    //   padding: 23px 2.8% 0;
    //   img {
    //     width: 118px;
    //     height: 36px;
    //   }
    // }
  }

  .container {
    width: 935px;
    height: 460px;
    background-color: #fff;
     background: url('~@/assets/login/container_bg.png') center no-repeat;
    position: relative;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
    display: flex;
    align-items: center;
    overflow: auto;
    margin-top: -30px;
    border-radius: 10px;
    .left {
      width: 58%;
      height: 100%;
      position: relative;
      .title {
        font-size: 38px;
        font-weight: normal;
        color: white;
        letter-spacing: 3px;
        margin-top: 10px;
        margin-left: 34px;
        margin-bottom: 20px;
        @media screen and (max-width: 1550px) {
          font-size: 32px;
          margin-top: 0;
          margin-bottom: 15px;
        }
      }
      .mini-title {
        font-size: 28px;
        font-weight: normal;
        color: white;
        margin-bottom: 12px;
        margin-left: 34px;
        @media screen and (max-width: 1550px) {
          font-size: 18px;
          margin-bottom: 6px;
        }
      }
      .desc {
        font-size: 16px;
        color: white;
        line-height: 1.5;
        margin-left: 34px;
        @media screen and (max-width: 1550px) {
          font-size: 14px;
        }
      }
      .register-btn {
        width: 120px;
        height: 48px;
        font-size: 18px;
        text-align: center;
        line-height: 48px;
        color: #394af3;
        background-color: white;
        border-radius: $xr-border-radius-base;
        cursor: pointer;
        margin: 15px 0 15px 34px;
        @media screen and (max-width: 1550px) {
          width: 100px;
          height: 40px;
          font-size: 16px;
          line-height: 40px;
          border-radius: 4px;
          margin: 10px 0 10px 50px;
        }
      }
      .main-pic {
        width: 400px;
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%,-50%);
        @media screen and (max-width: 1550px) {
          width: 350px;
          // margin-top: 10px;
          // margin-left: -20px;
          // margin-bottom: 20px;
        }
      }
      .download-group {
        width: 100%;
        overflow: hidden;
        .download-item {
          float: left;
          border-radius: 22px;
          background-color: #2b5db9;
          text-align: center;
          cursor: pointer;
          margin-right: 20px;
          padding: 8px 16px;
          .icon {
            width: 30px;
            height: 30px;
            vertical-align: middle;
            margin-right: 10px;
          }
          .text {
            font-size: 14px;
            color: white;
            vertical-align: middle;
          }
          @media screen and (max-width: 1550px) {
            border-radius: 18px;
            margin-right: 15px;
            padding: 7px 12px;
            .icon {
              width: 22px;
              height: 22px;
              vertical-align: middle;
              margin-right: 8px;
            }
            .text {
              font-size: 14px;
              color: white;
              vertical-align: middle;
            }
          }
        }
      }
    }
    .right {
      width: 42%;
      /*margin-left: 80px;*/
      .login-main-content {
        position: relative;
        width: 100%;
        height: 460px;
        // height: 510px;
        border-radius: 6px;
        overflow: hidden;
        .logo-box {
          position: relative;
          width: 100%;
          font-weight: bold;
          font-size: 20px;
          color: #4c4c4c;
          letter-spacing: 2px;
          padding: 0 40px;
          margin: 15px 0 20px;
          &:before {
            position: absolute;
            top: 50%;
            left: 24px;
            transform: translateY(-50%);
            content: '';
            width: 4px;
            height: 16px;
            background-color: #3e6bea;
            border-radius: 2px;
            display: block;
          }
        }
        .use-tip {
          position: absolute;
          left: 0;
          bottom: 18px;
          width: 100%;
          text-align: center;
          color: #999;
          font-size: 12px;
          div + div {
            margin-top: 3px;
          }
          .text {
            vertical-align: middle;
          }
          .icon {
            width: 15px;
            display: inline-block;
            vertical-align: middle;
          }
        }
      }

      // @media screen and (max-width: 1550px) {
        // width: 480px;
        // margin-left: 80px;
        // padding-top: 10px;
        // .login-main-content {
          // height: 450px;
          // .logo-box {
            // font-size: 20px;
            // padding: 0 40px;
            // margin: 15px 0 20px;
            // &:before {
            //   height: 16px;
            //   left: 24px;
            // }
          // }
          // .use-tip {
          //   font-size: 12px;
          //   .icon {
          //     width: 15px;
          //   }
          // }
        // }
      // }
    }

    /*@media screen and (max-width: 1550px) {*/
      /*margin-top: 1vh;*/
    /*}*/
  }
}

.download-popover {
  .popover-box {
    width: 126px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    .qrcode {
      width: 126px;
      height: 126px;
    }
    .down-text {
      width: 126px;
      font-size: 12px;
      text-align: center;
      color: #666;
      margin-top: 5px;
    }
  }
}

.create-wrapper {
  .container {
    .left {
      .activity-list {
        width: 700px;
        display: flex;
        flex-wrap: wrap;
        margin-left: 34px;
        margin-top: 26px;
        .activity-list-item {
          width: 350px;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          margin: 15px 0;
          .icon {
            font-size: 26px;
            color: white;
            margin-right: 10px;
            display: flex;
          }
          .box {
            .activity-title {
              font-size: 18px;
              color: white;
              margin-bottom: 5px;
            }
            .text {
              color: white;
              font-size: 14px;
            }
          }
        }
      }
      @media screen and (max-width: 1550px) {
        .main-pic {
          width: 450px;
          margin-bottom: 5px;
        }
        .activity-list {
          width: 500px;
          margin-top: 5px;
          margin-left: 40px;
          .activity-list-item {
            width: 250px;
            margin: 6px 0;
            .icon {
              font-size: 20px;
            }
            .box {
              .activity-title {
                font-size: 16px;
              }
              .text {
                font-size: 12px;
              }
            }
          }
        }
      }
    }
    .right {
      .login-main-content {
        height: 400px;
        // @media screen and (max-width: 1550px) {
        //   height: 490px;
        // }
      }
    }
  }
}
.tit_img{
	margin-top: 63px;
}
.tips_msg{
	color: #4887FF;
	font-size: 14px;
	margin-top: 30px;
	margin-bottom: 34px;
}
</style>
