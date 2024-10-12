<template>
	<div class="main">
		<div class="main-body">
			<div>
				<div class="right-container" style="float: right; margin-top: 5px;">
						<el-button v-if="canHandle('add')" class="xr-btn--orange" icon="el-icon-plus" type="primary" @click="createClick">新建
						</el-button>
					</div>
				<el-card class="box-card" style="margin: 10px;">		
					<div class="swiper_box">
						<div class="swiper_kunag">
							<swiper class="swiper gallery-top" :options="swiperOptionTop" ref="swiperTop">
								<swiper-slide class="slide-1" v-for='item in dataList' :key='item.id'>
									<div class="swiper_title">{{item.content}}</div>
									<div class="swiper_name">{{item.informer}}</div>
									<div class="imgbox">
										<img :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`' v-src="getImg(item.imgs)"/>										
										<div class="swiper_line"></div>
										<div class="swiper_circular"></div>
										<div class="swiper_time">{{item.reportTime}}</div>
									</div>
								</swiper-slide>	
							</swiper>
						</div>
						<img class="leftimg" src="../../../../assets/img/swiperleft.png"/>
						<img class="rightimg" src="../../../../assets/img/swiperright.png"/>
						<img class="swiperlineimg" src="../../../../assets/img/swiperline.png"/>
					</div>
					<div class="card_box">
						<el-card class='content_card' v-for="item in dataList" :key="item.id" :body-style="{ padding: '0px' }" >
							<img class="con_img" :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`' v-src="getImg(item.imgs)" style="width: 170px; height: 170px;" />
							<div style="padding: 0px 14px 14px;">
								<div class="bottom clearfix">
									<span class="time" v-bind:title="item.content">{{ item.content }}</span>
									<el-button type="text" class="button" @click="details(item)">详情</el-button>
									<el-button type="text" style="padding-right: 10px;" class="button" @click="handleDelete(item.id)">删除</el-button>
								</div>
							</div>
						</el-card>
						
					</div>
				</el-card>
			</div>

		</div>

		<!-- 新建 -->
		<create v-if="createShow" :businessType="businessType" :action="action" @close="createShow = false" @save-success="getList" />
	</div>
</template>

<script>
	import { swiper, swiperSlide } from "vue-awesome-swiper";
	import "swiper/dist/css/swiper.css";
	import { getList, delById } from '../api/imageDisplay'
	import XrHeader from '@/components/XrHeader'
	import Create from './Create.vue'
	import { mapGetters } from 'vuex'

	export default {
		name: 'imageDisplay',
		components: {
			XrHeader,
			Create,
			swiper,
			swiperSlide
		},
		data() {
			return {
				// 遮罩层
				businessType: 10000,
				loading: true,
				createShow: false,
				action: {
					type: 'save',
					data: null
				},
				tableHeight: document.documentElement.clientHeight - 280, // 表的高度
				// 是否显示弹出层
				open: false,
				title: '',
				// 分页
				currentPage: 1,
				pageSize: 30,
				pageSizes: [10, 20, 30, 40],
				id: 0,
				dataList: [],
				queryParams: {},
				dangerSourceId: null,
				swiperOptionTop: {					
			  		slidesPerView: 3,
		       	 	spaceBetween: 30,
		        	freeMode: true,
		        	navigation: {
			          nextEl: ".leftimg",
			          prevEl: ".rightimg",
			        },
				},
			}
		},
		created() {
			this.getList()
		},
		computed: {
			...mapGetters(['imageCache'])
		},
		methods: {
			getList() {
				this.loading = true
				this.queryParams.dangerSourceId = this.dangerSourceId
				getList(this.queryParams).then(response => {
					this.dataList = response.data
					console.log(this.dataList)
					this.loading = false
				}).catch(() => {
					this.loading = false
				})
			},
			getImg(imgData) {
				var url = ''
				if(imgData[0] != undefined) {
					url = imgData[0].url
				}
				return url
			},
			getIms(imgs) {
				var imgList = []
				imgs.forEach(function(element) {
					imgList.push('/api/' + element.url)
				})
				return imgList
			},
			close() {
				this.createShow = false
			},
			details(data) {
				this.action.type = 'details';
				this.action.id = data.id
				this.createShow = true
			},
			// 更改每页展示数量
			handleSizeChange(val) {
				this.pageSize = val
				this.getList()
			},
			// 更改当前页数
			handleCurrentChange(val) {
				this.currentPage = val
				this.getList()
			},
			//按钮操作权限
			canHandle(type) {
				return true;
			},
			/**
			 * 新建点击
			 */
			createClick() {
				this.action.type = 'save';
				this.createShow = true
			},
			/** 删除按钮操作 */
			handleDelete(id) {
				this.$confirm('确定删除选中的数据吗？', '警告', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function() {
					return delById(id)
				}).then(() => {
					this.getList()
					this.msgSuccess('删除成功')
				}).catch(function() {})
			},
		}
	}
</script>

<style lang="scss" scoped>
	.main {
		height: 100%;
	}
	
	/deep/ .xr-header {
		padding: 15px 30px;
	}
	
	.main-body {
		background-color: white;
		/*border-top: 1px solid $xr-border-line-color;*/
		border-bottom: 1px solid $xr-border-line-color;
	}
	
	.el-header {
		background-color: #B3C0D1;
		color: #333;
		line-height: 60px;
	}
	
	.el-aside {
		color: #333;
	}
	
	.custom-tree-node {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-size: 14px;
		padding-right: 8px;
	}
	
	.custom-tree-but {
		display: inline-block;
		position: relative;
		right: 10px;
		top: 0px;
	}
	
	.custom-tree-button {
		padding: 3px 10px;
	}
	
	.xl-but {
		background-color: #CCCCCC;
		color: #000000;
	}
	
	.time {
		display: inline-block;
		width: 100px;
		height: 13px;
		font-size: 13px;
		color: #999;
		white-space: nowrap; //不换行
		overflow: hidden; //超出隐藏
		text-overflow: ellipsis; //变成...
		line-height: 13px;
	}
	
	.bottom {
		/*margin-top: 13px;*/
		line-height: 12px;
	}
	
	.button {
		padding: 0;
		float: right;
	}
	
	.clearfix:before,
	.clearfix:after {
		display: table;
		content: "";
	}
	
	.clearfix:after {
		clear: both
	}
	.right-container{
		position: absolute;
		top: 0;
		left: 0;
	}
	.swiper_box{
		width: 90%;
		height: 400px;
		background: #EFEEEE;
		margin: 0 auto;
		position: relative;
	}
	.swiper_kunag{
		width: 80%;
		height: 100%;
		margin: 0 auto;
		position: relative;
	}
	.swiperlineimg{
		width: 90%;
		position: absolute;
		bottom: 19%;
		left: 50%;
		transform: translateX(-50%);
	}
	.gallery-top{
		height: 100%;
	}
	.imgbox{
		width: 90%;
		margin: 0 auto;
		height: 50%;
		border-radius: 10px;
		background: #fff;
		box-shadow: 0px 1px 14px 0px rgba(107,107,107,0.58);
		display: flex;
		align-items: center;
		justify-content: center;
		margin-top: 6%;
		position: relative;
	}
	.imgbox img{
		width: 95%;
		height: 95%;
		
	}
	.swiper_title{
		color: #000000;
		font-size: 18px;
		width: 90%;
		margin: 0 auto;
		margin-top: 6%;
	}
	.swiper_name{
		color: #999999;
		font-size: 16px;
		width: 90%;
		margin: 0 auto;
		margin-top: 6%;
	}
	.swiper_line{
		width: 3px;
		height: 26px;
		background: #FFFFFF;
		position: absolute;
		bottom: -26px;
	}
	.swiper_circular{
		width: 10px;
		height: 10px;
		background: #FFFFFF;
		border-radius: 50%;
		position: absolute;
		bottom: -36px;
	}
	.swiper_time{
		width: 151px;
		height: 35px;
		background: linear-gradient(180deg, #0B1644, rgba(17,39,109,0.7));
		box-shadow: 0px 1px 32px 0px rgba(107,107,107,0.22);
		border-radius: 18px;
		position: absolute;
		bottom: -85px;
		color: #FFFFFF;
		text-align: center;
		line-height: 35px;
	}
	.content_card{
		display: inline-block;
		width: 20%;
		margin:10px 2.5%;
	}
	.con_img{
		display: block;
		margin: 18px auto;
	}
	.leftimg{
		position: absolute;
		left: 5%;
		top: 40%;
		cursor: pointer;
		
	}
	.rightimg{
		position: absolute;
		right: 5%;
		top: 40%;
		cursor: pointer;
	}
</style>