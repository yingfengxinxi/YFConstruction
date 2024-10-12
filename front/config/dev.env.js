/*
 * This file is part of Qingyun SmartPark.
 *
 * @description  青云智慧园区
 * @link  https://sdqingyun.cn
 * @contact  https://sdqingyun.cn 7*12 9:00-21:00
 */
'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  BASE_API: '"http://zhgd.sdyingfeng.cn/api/"',
  INDEX_URL: '"build/index"',
  VIDEO_URL: '"http://zhgd.sdyingfeng.cn"' //视频流服务器地址--废弃改为萤石云
})
