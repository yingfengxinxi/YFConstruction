<template>
  <div>
    <baidu-map :center="center" :zoom="zoom" class="bm-view" @ready="handler">
      <bm-marker v-for="item in typeList" :key="item.id" :position="{lng: item.lng, lat: item.lat}"
                 :icon="{url: 'static/img/bj3.png', size: {width: 60, height: 55}}" @click="infoWindowOpen(item)"/>
      <bm-info-window :show="windowShow" :title="windowTitle" :position="windowCenter">
        <div style="margin-bottom: 5px;">
          {{ '所属项目：' + showData.projectName}}
        </div>
        <div style="margin-bottom: 5px;">
          {{ '姓名：' + showData.name}}
        </div>
        <div style="margin-bottom: 5px;">
          {{ '联系电话： ' + showData.phone }}
        </div>
        <div style="margin-bottom: 5px;">
          {{ '状态：'}}{{showData.status=='1'?'在线':'离线'}}
        </div>
        <div style="text-align: center;">
          <el-button v-if="showData.status=='1'" type="primary" icon="el-icon-phone-outline" round
                     @click="rtcOnline(showData)">呼叫
          </el-button>
          <el-button v-if="showData.status!='1'" type="info" icon="el-icon-phone" round>呼叫</el-button>
        </div>
      </bm-info-window>
    </baidu-map>
    <el-dialog
      title="通话链接"
      :visible.sync="showRTC"
      :modal="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false" width="1200px">
      <el-form>
        <el-row>
          <el-col :span="12" style="padding-right: 10px">
            <div id='local_stream' class="local-stream">
            </div>
          </el-col>
          <el-col :span="12" style="padding-left: 10px">
            <div id="remote_stream" v-loading="waitConnect" class="remote_stream" :modal-append-to-body="false">
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" style="padding-top: 10px;text-align: center;">
            <el-button @click="localChangeVideo(false)" v-if="localVideo" type="primary" icon="el-icon-video-camera-solid">视频</el-button>
            <el-button @click="localChangeVideo(true)" v-if="!localVideo" type="info" icon="el-icon-video-camera-solid">视频</el-button>
            <el-button @click="localChangeAudio(false)" v-if="localAudio" type="primary" icon="el-icon-phone-outline">音频</el-button>
            <el-button @click="localChangeAudio(true)" v-if="!localAudio" type="info" icon="el-icon-phone-outline">音频</el-button>
            <el-button type="info" icon="el-icon-error" @click="closeRtc">断开</el-button>
          </el-col>
          <el-col :span="12" style="padding-top: 10px;text-align: center;">
            <el-button @click="remoteChangeVideo(false)" v-if="remoteVideo" type="primary" icon="el-icon-video-camera-solid">视频</el-button>
            <el-button @click="remoteChangeVideo(true)" v-if="!remoteVideo" type="info" icon="el-icon-video-camera-solid">视频</el-button>
            <el-button @click="remoteChangeAudio(false)" v-if="remoteAudio" type="primary" icon="el-icon-phone-outline">音频</el-button>
            <el-button @click="remoteChangeAudio(true)" v-if="!remoteAudio" type="info" icon="el-icon-phone-outline">音频</el-button>
          </el-col>
        </el-row>
      </el-form>
      <div style="padding:10px 10px;">

      </div>

    </el-dialog>

  </div>
</template>

<script>
  //导入sdk
  import TRTC from "trtc-js-sdk";

  import {sendTRtc} from './api/rtc'

  import {
    adminUsersReadAPI
  } from '@/api/user/personCenter'

  export default {
    // 新建编辑
    name: 'BdMap',
    components: {
    },
    props: {},
    data() {
      return {
        center: {lng: 117.323065, lat: 34.806747},
        zoom: 10,
        typeList: [
          {
            userId: 14856,
            projectName: '幸福小区项目',
            name: '李文强',
            phone: '13061460760',
            status: '1',
            lng: '117.168413',
            lat: '35.093619'
          },
          {
            userId: 122333,
            projectName: '幸福小区项目',
            name: '吕复臣',
            phone: '13061460761',
            status: '0',
            lng: '117.585213',
            lat: '34.775795'
          },
        ],//标点数据 name phone status
        // 地图窗口
        windowShow: false,
        windowTitle: '人员信息',
        windowCenter: {lng: 0, lat: 0},
        showData: {},
        //rtc
        showRTC: false,
        client: '',//客户端服务
        remoteStream: '',//远方播放流
        localStream: '',//本地流
        waitConnect: true,
        localAudio: true,
        localVideo: true,
        remoteAudio: false,
        remoteVideo: false,
      }
    },
    methods: {
      //音频
      localChangeAudio(flag) {
        this.localAudio = flag;
        if (flag) {
          this.localStream.unmuteAudio();
        }else{
          this.localStream.muteAudio();
        }
      },
      remoteChangeAudio(flag) {
        if(this.waitConnect){
          return;
        }
        this.remoteAudio = flag;
        if (flag) {
          this.remoteStream.unmuteAudio();
        }else{
          this.remoteStream.muteAudio();
        }
      },
      //视频
      localChangeVideo(flag) {
        this.localVideo = flag;
        if (flag) {
          this.localStream.unmuteVideo();
        }else{
          this.localStream.muteVideo();
        }
      },
      remoteChangeVideo(flag) {
        if(this.waitConnect){
          return;
        }
        this.remoteVideo = flag;
        if (flag) {
          this.remoteStream.unmuteVideo();
        }else{
          this.remoteStream.muteVideo();
        }
      },
      closeRtc() {//退出房间
        this.showRTC = false;
        this.leaveRoom(this.client);
      },
      rtcOnline(user) {
        adminUsersReadAPI().then(res => {
          console.log(res.data)
          var currUser = res.data;
          this.showRTC = true;
          //todo 后期关联用户表，房间号使用userId，原则上一个用户同时只能打开一个房间
          var userId = currUser.realname + "(" + currUser.username + ")"
          this.createClient(userId, currUser.userId);
          //参数 name 发起者姓名 id:被发起者userId
          sendTRtc({name: currUser.realname, userId: user.userId, roomId: currUser.userId});
        })
          .catch(() => {
          })

      },
      //===========rtc=============
      //创建链接
      createClient(userId, roomId) {
        console.log(userId, roomId);
        //获取签名
        const config = this.genTestUserSig(userId)
        const sdkAppId = config.sdkAppId
        const userSig = config.userSig
        this.client = TRTC.createClient({
          mode: 'rtc',
          sdkAppId,
          userId,
          userSig
        });
        //注册远程监听，要放在加入房间前--这里用了发布订阅模式
        this.subscribeStream(this.client)
        //初始化后才能加入房间
        this.joinRoom(this.client, roomId)
      },
      //加入房间
      joinRoom(client, roomId) {
        client.join({roomId})
          .catch(error => {
            console.error('进房失败 ' + error);
          })
          .then(() => {
            console.log('进房成功');
            //创建本地流
            this.createStream(this.userId)
            //播放远端流
            this.playStream(this.client)
          });
      },

      //创建本地音视频流
      createStream(userId) {
        const localStream = TRTC.createStream({userId, audio: true, video: true});
        this.localStream = localStream

        localStream
          .initialize()
          .catch(error => {
            console.error('初始化本地流失败 ' + error);
          })
          .then(() => {
            console.log('初始化本地流成功');
            // 创建好后才能播放 本地流播放 local_stream 是div的id
            localStream.play('local_stream');
            //创建好后才能发布
            this.publishStream(localStream, this.client)
          });
      },

      //发布本地音视频流
      publishStream(localStream, client) {
        client
          .publish(localStream)
          .catch(error => {
            console.error('本地流发布失败 ' + error);
          })
          .then(() => {
            console.log('本地流发布成功');
          });
      },

      //订阅远端流--加入房间之前
      subscribeStream(client) {
        var that = this;
        client.on('stream-added', event => {
          that.remoteStream = event.stream;
          console.log('远端流增加: ' + that.remoteStream.getId());
          //订阅远端流
          client.subscribe(that.remoteStream);
        });
      },

      //播放远端流
      playStream(client) {
        client.on('stream-subscribed', event => {
          const remoteStream = event.stream;
          console.log('远端流订阅成功：' + remoteStream.getId());
          remoteStream.play('remote_stream');
          this.waitConnect = false;
          this.remoteAudio = true;
          this.remoteVideo = true;
          /*var rDID = '-' + remoteStream.getId();
          //做了dom操作 需要使用$nextTick(),否则找不到创建的标签无法进行播放
          this.$nextTick(() => {
            //播放

          })*/
        });
      },

      //退出音视频
      leaveRoom(client) {
        client
          .leave()
          .then(() => {
            console.log('退房成功')
            // 停止本地流，关闭本地流内部的音视频播放器
            this.localStream.stop();
            // 关闭本地流，释放摄像头和麦克风访问权限
            this.localStream.close();
            this.localStream = null;
            this.client = null
            // 退房成功，可再次调用client.join重新进房开启新的通话。
          })
          .catch(error => {
            console.error('退房失败 ' + error);
            // 错误不可恢复，需要刷新页面。
          });
      },


      //获取用户签名--前端测试用
      genTestUserSig(userID) {
        /**
         * 腾讯云 SDKAppId，需要替换为您自己账号下的 SDKAppId。
         *
         * 进入腾讯云实时音视频[控制台](https://console.cloud.tencent.com/rav ) 创建应用，即可看到 SDKAppId，
         * 它是腾讯云用于区分客户的唯一标识。
         */
        const SDKAPPID = 1400653844;
        /**
         * 签名过期时间，建议不要设置的过短
         * <p>
         * 时间单位：秒
         * 默认时间：7 x 24 x 60 x 60 = 604800 = 7 天
         */
        const EXPIRETIME = 604800;
        /**
         * 计算签名用的加密密钥，获取步骤如下：
         *
         * step1. 进入腾讯云实时音视频[控制台](https://console.cloud.tencent.com/rav )，如果还没有应用就创建一个，
         * step2. 单击“应用配置”进入基础配置页面，并进一步找到“帐号体系集成”部分。
         * step3. 点击“查看密钥”按钮，就可以看到计算 UserSig 使用的加密的密钥了，请将其拷贝并复制到如下的变量中
         *
         * 注意：该方案仅适用于调试Demo，正式上线前请将 UserSig 计算代码和密钥迁移到您的后台服务器上，以避免加密密钥泄露导致的流量盗用。
         * 文档：https://cloud.tencent.com/document/product/647/17275#Server
         */
        const SECRETKEY = "0ca9ff67e42827142cc051de0f145300118370b54246b2d84a3e8e1c640b78f3";

        // a soft reminder to guide developer to configure sdkAppId/secretKey
        if (SDKAPPID === "" || SECRETKEY === "") {
          alert(
            "请先配置好您的账号信息： SDKAPPID 及 SECRETKEY " +
            "\r\n\r\nPlease configure your SDKAPPID/SECRETKEY in js/debug/GenerateTestUserSig.js"
          );
        }
        const generator = new LibGenerateTestUserSig(SDKAPPID, SECRETKEY, EXPIRETIME);
        const userSig = generator.genTestUserSig(userID);
        return {
          sdkAppId: SDKAPPID,
          userSig: userSig
        };
      },
      //==========地图=============
      infoWindowOpen(item) {//展示标记窗口
        this.windowShow = true
        this.windowCenter = {lng: item.lng, lat: item.lat}
        this.showData = item;
      },
      handler({BMap, map}) {
        // console.log(BMap, map)
      },
    }
  }
</script>

<style lang="scss" scoped>
  //本地流
  .local-stream {
    width: 540px;
    height: 650px;
  }
  //远端流
  .remote_stream {
    width: 540px;
    height: 650px;
  }

  .bm-view {
    width: 100%;
    height: 700px;
  }
</style>
