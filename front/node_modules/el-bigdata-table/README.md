# el-bigdata-table

用来实现el-table展示大数据
流畅渲染万级数据并不会影响到el-table的原有功能

[点击查看在线demo](https://code-farmer-i.github.io/el-bigdata-table/dist/)

## Install
```shell
npm install el-bigdata-table -S
```

## webpack.base.conf.js
因代码中使用了es6的语法 所以需要添加babel配置
``` javascript
'use strict'
const path = require('path')
const utils = require('./utils')
const config = require('../config')
const vueLoaderConfig = require('./vue-loader.conf')
// 此处为添加的配置
const fs = require('fs')

function resolve (dir) {
  return path.join(__dirname, '..', dir)
}

//此处为添加的配置
let dirsName = fs.readdirSync(resolve('node_modules')).filter(dirName => /el-bigdata-table/.test(dirName))
const includesDirs = dirsName.map(dir => resolve(`node_modules/${dir}/src`))

module.exports = {
  ... //省略的代码
  module: {
    rules: [
      // 省略代码...
      // 此处有添加babel-loader配置 ‘...includesDirs‘
      {
        test: /.js$/,
        loader: 'babel-loader',
        include: [resolve('src'), resolve('test'), resolve('node_modules/webpack-dev-server/client'), ...includesDirs]
      },
      {
        test: /.(png|jpe?g|gif|svg)(?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          name: utils.assetsPath('img/[name].[hash:7].[ext]')
        }
      },
```

## Quick Start
``` javascript
// main.js
import 'el-bigdata-table'
```

### 基础用法
```html
<template>
  <!-- 使用 useVirtual 属性开启虚拟滚动 使用虚拟滚动时，必须要固定表格高度和行高 -->
  <el-table
    :data="tableData"
    height="400"
    useVirtual
  >
    <el-table-column
      type="index"
      width="100"
      fixed
    ></el-table-column>
    <el-table-column
      prop="date"
      label="日期"
      width="180">
    </el-table-column>
    <el-table-column
      prop="name"
      label="姓名"
      width="180">
    </el-table-column>
    <el-table-column
      prop="address"
      label="地址">
    </el-table-column>
  </el-table>
</template>

<script>
  export default {
    data() {
      return {
        tableData: Array.from({length: 10000}, () => ({
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1516 弄'
        }))
      }
    }
  }
</script>
```

## API

### 新增 Props:

属性  |  说明  |  类型  |  默认值
:-------: | -------  |  :-------:  |  :-------:
use-virtual  |  是否开启虚拟滚动  |  Boolean  |  false
row-height  |  行高(必须要设置正确的行高，否则会导致表格计算不正确)  |  Number  |  48
excess-rows  |  表格可视区域上方与下方额外渲染的行数，行数越多表格接替渲染效果越好，但越耗性能  |  Number  |  5
use-row-key  |  大部分场景下可以不使用rowKey来最大化复用dom，极大的提升渲染效率  |  Boolean  | false

作者wx: ckang1229

