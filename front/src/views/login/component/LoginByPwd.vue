<template>
  <div class="login-by-pwd">
    <el-form class="login-from">
      <el-form-item>
        <el-input
        	class="aa"
          ref="username"
          v-model.trim="form.username"
          :class="{error: !validateRes.username}"
          placeholder="请输入用户名"
          autofocus
          type="text"
          @focus="focusKey = 'username'"
          @blur="checkFromItem('username', form.username)"
          @keyup.enter.native="debouncedHandleLogin"
        >
          <!--<span
            slot="prefix"
            :class="{
              full: Boolean(form.username),
              focus: focusKey === 'username'
            }"
            class="form-icon wk wk-user" />-->
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-input
        	class="aa"
          ref="password"
          v-model.trim="form.password"
          :maxlength="20"
          :class="{error: !validateRes.password}"
          placeholder="请输入密码"
          type="password"
          @focus="focusKey = 'password'"
          @keyup.enter.native="debouncedHandleLogin"
          @blur="checkForm"
        />
      </el-form-item>
    </el-form>

    <div class="cell login-action">
    	<div class="tips_msg"><a href='../njv/' target="_blank" >数据大屏</a></div>
      <div class="cell-box">
        <el-checkbox
          v-model="rememberMe">
          记住账号
        </el-checkbox>
      </div>
      <!--<div class="empty">
        &nbsp;
      </div>-->
    </div>

    <div
      :class="{ok: !Boolean(errorInfo)}"
      class="error-info">
      <div
        v-if="errorInfo"
        class="box">
        <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'
          src="~@/assets/login/error.png"
          alt=""
          class="icon">
        <span>{{ errorInfo }}</span>
      </div>
    </div>

    <div class="control">
      <div
        class="btn"
        @click="debouncedHandleLogin">
        登&nbsp;录
      </div>
    </div>
  </div>
</template>

<script>
import { querySystemStatusAPI } from '@/api/login'
import { Loading } from 'element-ui'

import Mixins from './Mixins'
import { debounce } from 'throttle-debounce'

export default {
  name: 'LoginByPwd',
  mixins: [Mixins],
  data() {
    const pwdReg = /^(?=.*[a-zA-Z])(?=.*\d).{6,20}$/
    return {
      redirect: undefined,
      rememberMe: true,
      form: {
        username: '',
        password: ''
      },
      errorInfo: null,
      validateRes: {
        username: true,
        password: true
      },
      rules: {
        username: [{ required: true, msg: '用户名不能为空' }],
        password: [
          { required: true, msg: '密码不能为空' },
          { reg: pwdReg, msg: '密码由6-20位字母、数字组成' }
        ]
      },
      tenantId: ''
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.$refs.username.focus()
    })
  },
  created() {
    this.getInit()
    this.debouncedHandleLogin = debounce(300, this.handleLogin)
    const username = localStorage.getItem('account')
    let jsonUser=JSON.parse(username)
    if (username) {
      this.form = {
        username:username,
        password: ''
      }
    } else if (this.$route.query.username) {
      this.form = {
        username: this.$route.query.username,
        password: this.$route.query.password || ''
      }
    }
  },
  methods: {
    /**
     * 登录
     */
    handleLogin() {//TODO 登录成功之后，只是获取了Token和用户信息，权限等信息路由的前置处理的
      const flag = this.checkForm()
      if (!flag) return
      if (this.rememberMe) {
        localStorage.setItem('account', this.form.username)
      } else {
        localStorage.removeItem('account')
      }

      const loading = Loading.service({
        target: document.querySelector('.login-main-content')
      })
      this.$store
        .dispatch('Login', this.form)
        .then(res => {
          //this.$router.push({ path: this.redirect || '/' })
          //console.log(this.redirect)
          this.$router.push({ path: this.redirect || '/' })
        })
        .catch(() => {
          loading.close()
        })
    },

    /**
     * 校验登录表单
     */
    checkForm() {
      this.clearError()
      const arr = ['username', 'password']
      for (let i = 0; i < arr.length; i++) {
        const res = this.checkFromItem(arr[i], this.form[arr[i]] || null)
        if (!res) return false
      }
      return true
    },

    getInit() {
      querySystemStatusAPI().then(res => {
        res.data == 0 && this.$router.push('/welcome')
      }).catch(() => {})
    },

    clearError() {
      this.errorInfo = null
      this.validateRes = {
        username: true,
        password: true
      }
    }
  }
}
</script>

<style scoped lang="scss">
@import '../index';

.login-by-pwd {
	width:305px;
  .forget-pwd {
    height: 50px;
    color: #999;
    line-height: 50px;
    cursor: pointer;
    padding: 0 5px;
    display: block;
    &:hover {
      color: #3e6bea;
    }
    // @media screen and (max-width: 1550px) {
    //   height: 50px;
    //   font-size: 14px;
    //   line-height: 50px;
    //   padding: 0 5px;
    // }
  }
}

.login-action {

  display: flex;
  justify-content: space-between;
  	margin-top: 15px;
  .login-by-code {
    font-size: 14px;
    color: #3e6bea;
    cursor: pointer;
    &:hover {
      text-decoration: underline;
    }
  }

  // @media screen and (min-width: 1550px) {
    /deep/ .el-checkbox {
      .el-checkbox__inner {
        width: 14px;
        height: 14px;
        &::after {
          top: 2px;
          left: 5px;
        }
      }
      .el-checkbox__label {
        font-size: 14px;
      }
    }
  // }

  // @media screen and (max-width: 1550px) {
  //   margin-top: -5px;
  //   .login-by-code {
  //     font-size: 14px;
  //   }
  // }
}

.control {
  .others {
    font-size: 14px;
    .el-dropdown {
      font-size: 14px;
    }
    .register {
      cursor: pointer;
      &:hover {
        text-decoration: underline;
      }
    }
  }
  // @media screen and (max-width: 1550px) {
  //   .others {
  //     font-size: 14px;
  //     .el-dropdown {
  //       font-size: 14px;
  //     }
  //   }
  // }
}

.center-tips {
  font-size: 12px;
  color: #999;
  margin-top: 10px;
  .el-icon-warning {
    color: #f9a74e;
    font-size: 14px;
  }
}
.aa{
	width: 305px;
	height: 45px!important;
	border-radius: 23px!important;
	background: #F1F5FA!important;
}
/deep/.el-input__inner{
	height: 45px!important;
	border-radius: 23px!important;
}
.cell-box{

	float: right;
    text-align: right;
}
.tips_msg{
		color: #4887FF;
    font-size: 14px;
    cursor: pointer;
}
</style>
