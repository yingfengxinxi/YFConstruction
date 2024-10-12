<template>
	<div class="content_box">
		<div class="foun_left">
			<div class="sheb_list_box">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">{{assetName}}</div>
				<div class="list_box">
					<div class="list_tit">
						<div style="width: 30%">监测点名称</div>
						<div style="width:20%">采集值</div>
						<div style="width:20%">单位</div>
						<div style="width:40%">监控时间</div>
					</div>
					<div class="list_con">
						<div class="sheblist" v-for="item in equipmentlist">
							<div style="width: 30%">{{item.pointName}}</div>
							<div style="width:20%">{{item.value}}</div>
							<div style="width:20%">{{item.unit}}</div>
							<div style="width:40%">{{item.monitorTime}}</div>
						</div>
					</div>
				</div>
			</div>
			<div class="sheb_echarts">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">{{assetName}}沉降</div>
				<div id="shebInfo">

				</div>
			</div>
		</div>
		<div class="foun_center">
			<div class="swiper_box">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="syright" src="./img/sb_right.png"/>
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="syleft" src="./img/sb_left.png"/>
				<div class="swiper_btn">
					<swiper  :options="swiperOption" ref="mySwiper">
						<swiper-slide v-for="(data,index) in swiperList" :key="index+321">
							<ul class="speed_con_box">
								<li v-for="item in data" :class="assetId==item.id?'xzsheb':'wxzsheb'" @click="xzClick(item)">
									<div class="jkname">{{item.name}}</div>
									<div class="jktyoe">
										<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.status==1" src='./img/shebicon1.png'/>
										<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-if="item.status==0" src="./img/shebicon2.png"/>
										<div>{{filter(item.status)}}</div>
									</div>
								</li>
							</ul>
						</swiper-slide>
					</swiper>
				</div>
			</div>
			<div class="lenged_box">
				<div>
					<div class="lenged" style="background: #57CB91;"></div>
					正常值
				</div>
				<div>
					<div class="lenged" style="background: #FDEB62;"></div>
					超出预警值
				</div>
				<div>
					<div class="lenged" style="background: #FF5353;"></div>
					超出报警值
				</div>
			</div>
			<!--<div class="jk_box">
				
			</div>-->
			<div id="container"></div>
		</div>
		<div class="foun_right">
			<div class="statistics_box">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">一周预警报警数据统计</div>
				<div id="statistics">
					
				</div>
			</div>
			<div class="cumulative_box">
				<div class="subject"><img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/Symbol.png">累计报警监测类型占比统计</div>
				<div id="cumulative">
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import * as THREE from 'three'
	import {
		AmbientLight,
		DirectionalLight,
		PerspectiveCamera,
		Scene,
		WebGLRenderer,
	} from 'three'
	import { OBJLoader } from 'three/examples/jsm/loaders/OBJLoader'
	import { MTLLoader } from 'three/examples/jsm/loaders/MTLLoader'
	import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls'
	import { CSS2DRenderer, CSS2DObject } from 'three/examples/jsm/renderers/CSS2DRenderer'
	import {getlistByPro,getList,getDayDataForPit,getCountBarByPro,getCountPieByPro} from '@/views/build/equipment/api/foundationView'
	import * as echarts from 'echarts';
	import { swiper, swiperSlide } from "vue-awesome-swiper";
	import "swiper/dist/css/swiper.css";
	import request from '@/utils/request'
	export default {
		data() {
			return {
				//swiper 初始化
			      swiperOption: {
					navigation:{
						prevEl:'.syleft',
						nextEl:'.syright'
					}
			      },
				swiperList: [],
				assetId:null,
				assetName:'',
				equipmentlist:[],
				wclist:[],
				//c4d-------
				renderer: null,
				scene: null,
				camera:null,
				mtlLoader:null,
				objLoader:null,
				orbitControls:null,
				c4dUrl:''
				//c4d-------	
			}
		},
		components: {
			swiper,
			swiperSlide
		},
		computed:{
			filter(){
				return function(data){
					for (let i=0;i<this.wclist.length;i++) {
						if(data==this.wclist[i].dictValue){
							return this.wclist[i].dictLabel
						}
					}
				}
			}
		},
		created(){
			request({
					url: 'adminDict/data/type/b_equipment_pro_status',
					method: 'get',
					data: {},
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				}).then(response => {
					this.wclist = response.data
					
				})
				getList(20).then(res=>{
					if(this.$route.params.assetId){
						this.assetId=this.$route.params.assetId
						for(let i=0;i<res.data.length;i++){
							if(this.assetId==res.data[i].id){
								this.assetName=res.data[i].name
								this.c4dUrl=res.data[i].c4dUrl
							}
						}
					}else{						
						this.assetId=res.data[0].id
						this.assetName=res.data[0].name
						this.c4dUrl=res.data[0].c4dUrl
					}
					console.log(this.c4dUrl)
					this.swiperList=res.data
					this.swiper()
					this.getequipmentList(this.assetId)
					this.getSettlement(this.assetId)
					this.getMonitorData(this.assetId)
					this.getWarningData(this.assetId)
				}).catch(res=>{
					
				})

		},
		mounted() {		
			this.remsize()
		},
		methods: {
			//C4D------------------------------
			destroyC4d(){
				
				 if(!this.scene || !this.renderer){
					return
				 }else{
				 	let that=this
				 	this.orbitControls.dispose()
				 	this.scene.remove.apply(that.scene, that.scene.children);
				 	this.renderer.dispose()					 	
				 	this.scene = null
				 	this.renderer = null
				 	document.getElementById("container").innerHTML = ""
				 }
			},
			init(el, obj,objposition) {
				let that = this
				// 获取容器尺寸，
				// 如果是window对象，请使用window.innerWidth, window.innerHeight
				const width = el.offsetWidth
				const height = el.offsetHeight

				// 创建场景
				that.scene = new Scene()
				// 将加载完成的模型对象，添加到场景中
				that.scene.add(obj)

				// 创建环境光
				const ambientLight = new AmbientLight(0x666666)
				ambientLight.position.set(100, -100, -200)
				that.scene.add(ambientLight)

				// 创建平行光
				const light = new DirectionalLight(0xcccccc, 1)
				light.position.set(2000, 1000, 1000)
				that.scene.add(light)

				// 创建摄像机
				that.camera = new PerspectiveCamera(45, width / height, 1, 80000)
				that.camera.position.set(1000, 1000, 1000)

				// 创建renderer
				that.renderer = new WebGLRenderer({
					// 消除锯齿
					antialias: true
				})
				// 设置渲染区域尺寸
				that.renderer.setSize(width, height)
				// 设置背景颜色
				that.renderer.setClearColor(0x000000, 0)
				// 将场景Canvas DOM元素，添加至父元素中
				el.appendChild(that.renderer.domElement)

				// 创建场景鼠标控制实例，
				// 可以对页面上的模型对象进行旋转/缩放等操作
				this.orbitControls = new OrbitControls(that.camera, el)
				this.orbitControls.addEventListener('change', render)
				
				
				//添加标识
				var planeGeometry = new THREE.PlaneGeometry(1,1,1);
 				var planeMaterial = new THREE.MeshStandardMaterial({color:0xaaaaaa});
				var plane = new THREE.Mesh(planeGeometry, planeMaterial);
				that.scene.add( plane );
				
				let moonDiv={}
				let moonLabel={}
				for(let i=0;i<objposition.length;i++){

					if(objposition[i].c4dPosition){
						let c4dx=objposition[i].c4dPosition.split( "," )[0]*1
						let c4dy=objposition[i].c4dPosition.split( "," )[1]*1
						let c4dz=objposition[i].c4dPosition.split( "," )[2]*1
						moonDiv['div_'+i]=document.createElement( 'div' );
						moonDiv['div_'+i].className = 'labelss';
						moonDiv['div_'+i].innerHTML = `<div style='position:absolute;width:1px;height:70px;background:#58C4EB;top:-70px'></div>
						<div style='width:10px;height:10px;background:rgb(98, 222, 135);border-radius:50%;position:absolute;top:-5px;left:-5px;'></div>
						<div style='position:absolute;width:150px;height:60px;top:-130px;border:1px solid #58C4EB;left:-75px;background:rgba(0,0,0,0.5);border-radius:10px;padding:10px';>
							<div style='font-weight:800;margin-bottom:5px'>${objposition[i].pointName}</div>
							<div>采集值:${objposition[i].value}${objposition[i].unit}</br></div>					
						</div>`;
						moonLabel['lab_'+i]=new CSS2DObject( moonDiv['div_'+i] )
						moonLabel['lab_'+i].position.set(c4dx,c4dy,c4dz);
						plane.add( moonLabel['lab_'+i] );
					}
					
				}
//				const moonDiv = document.createElement( 'div' );
//				moonDiv.className = 'label';
//				moonDiv.textContent = '基坑1号';
//				const moonLabel = new CSS2DObject( moonDiv );
//				moonLabel.position.set( -100, 100, 100 );
//				plane.add( moonLabel );
//				
//				const moonDiv2 = document.createElement( 'div' );
//				moonDiv2.className = 'label';
//				moonDiv2.textContent = '基坑2号';
//				const moonLabel2 = new CSS2DObject( moonDiv2 );
//				moonLabel2.position.set( 100, 100, 100);
//				plane.add( moonLabel2 );
//				
//				const moonDiv3 = document.createElement( 'div' );
//				moonDiv3.className = 'label';
//				moonDiv3.textContent = '基坑3号';
//				const moonLabel3 = new CSS2DObject( moonDiv3 );
//				moonLabel3.position.set( -100, 100, -100);
//				plane.add( moonLabel3 );
//				
//				const moonDiv4 = document.createElement( 'div' );
//				moonDiv4.className = 'label';
//				moonDiv4.textContent = '基坑4号';
//				const moonLabel4 = new CSS2DObject( moonDiv4 );
//				moonLabel4.position.set( 100, 100, -100);
//				plane.add( moonLabel4 );
				
				// 创建labelRenderer div3D为当前场景挂载的div
				  let labelRenderer = new CSS2DRenderer()
				  labelRenderer.setSize(el.clientWidth, el.clientHeight)
				  labelRenderer.domElement.style.position = 'absolute'
				  labelRenderer.domElement.style.top = '0px'
				  el.appendChild(labelRenderer.domElement)
					labelRenderer.render(that.scene, that.camera)
					const controls = new OrbitControls( that.camera, labelRenderer.domElement );
			
				
				
				
				// 执行渲染，指定场景和相机作为参数
				function render() {
					that.renderer.render(that.scene, that.camera)
					labelRenderer.render(that.scene, that.camera)
				}
				render()
				
				 
			},
			loadResource() {
				let that=this
				return new Promise((resolve, reject) => {
					that.objLoader = new OBJLoader()
					that.mtlLoader = new MTLLoader()
					// 加载贴图文件
					that.mtlLoader.load(window.location.pathname+that.c4dUrl+'.mtl', mtl => {
						// 加载对象前，先设置贴图数据
						that.objLoader.setMaterials(mtl)
						// 加载对象文件
						that.objLoader.load(window.location.pathname+that.c4dUrl+'.obj', res => {
							resolve(res)
						}, undefined, reject)
					}, undefined, reject)
				})
			},

			threeinit(objposition) {
				let container = document.getElementById("container");
				this.loadResource().then(res => {
					this.init(container, res,objposition)
				})
			},
			//C4D------------------------------
			xzClick(data){
				this.destroyC4d()
				this.c4dUrl=data.c4dUrl
				this.assetId=data.id
				this.assetName=data.name
				this.getequipmentList(this.assetId)
				this.getSettlement(this.assetId)
				this.getMonitorData(this.assetId)
				this.getWarningData(this.assetId)
			},
			getequipmentList(id){//房山一号深基坑
				getlistByPro(id).then(res=>{
					this.equipmentlist=res.data
					this.threeinit(this.equipmentlist)
				}).catch(res=>{
					
				})
			},
			getSettlement(id){
				getDayDataForPit(id).then(res=>{
					this.shebInfo(res.data)
				}).catch(res=>{
					
				})
			},
			getMonitorData(id){
				getCountBarByPro(id).then(res=>{
					this.getStatistics(res.data)
				}).catch(res=>{
					
				})
			},
			getWarningData(id){
				getCountPieByPro(id).then(res=>{
					this.getcumulative(res.data)
				}).catch(res=>{
					
				})
			},
			getcumulative(data){
				
				let myChart = echarts.init(document.getElementById('cumulative'));
				let option = {
						tooltip: {
							trigger: 'item'
						},
						color: ['#5B96FA', '#A89FFF', '#F57D65', '#FAAD14', '#FA70FF', '#58C4EB', '#57CB91', '#FDEB62', '#FF5353', '#77B8F8'],
						legend: {
							icon: "circle",
							left: 'center',
							bottom: '0',
							itemWidth: 10,
							itemHeight: 10,
							textStyle: {
								color: "#000000",
								fontSize: 10
							},
						},
						series: [{
							type: "pie",
							radius: ["30%", "50%"],//饼图大小  如无圆心 radius：'55%'
							center: ["50%", "40%"],//位置
							hoverAnimation: true,//鼠标移入动画
							z: 10,
							itemStyle: {//饼图样式。
								normal: {
									borderWidth: 5,//边框
									borderColor: "#D7EBFF"
								}
							},
							label: {
								show: true,
								color: '#000'
							},
							data:data,
					}]
					}
				myChart.setOption(option)
			},
			getStatistics(data){
				let bjdata=[]
				let yjdata=[]
				let xdata=[]
				if(!data){
					xdata=[]
					bjdata=[]
					yjdata=[]
				}else{
					for (let i=0;i<data.list.length;i++) {
						if(data.list[i].name=='报警'){ 
							bjdata=data.list[i].data
						}
						if(data.list[i].name=='预警'){
							yjdata=data.list[i].data
						}
					}
					xdata=data.xdata
				}
					
				let myChart = echarts.init(document.getElementById('statistics'));
				let option = {
					tooltip: {
						trigger: 'axis',
						transitionDuration: 0
					},
					grid: {
						top: '15%',
						left: '13%',
						right: '10%',
						bottom: '20%'
					},
					legend: {
						bottom: '0'
					},
					xAxis: [{
						type: 'category',
						axisLine: {
							show: false,
							color: '#A582EA'
						},

						axisLabel: {
							color: '#000',
							width: 100
						},
						splitLine: {
							show: false
						},
						boundaryGap: false,
						data:xdata // this.$moment(data.times).format("HH-mm") ,

					}],

					yAxis: [{
						type: 'value',
						min: 0,
						// max: 140,
						splitNumber: 4,
						splitLine: {
							show: true,
							lineStyle: {
								color: '#000000',
								opacity: 0.15
							}
						},
						boundaryGap: true,
						axisLine: {
							show: false
						},
						axisLabel: {
							show: true,
							margin: 20,
							textStyle: {
								color: '#000'
							}
						},
						axisTick: {
							show: false
						}
					}],
					series: [{
						name: '报警数量',
						type: 'bar',
						barWidth: '8',
						itemStyle: {
							normal: {
								color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: 'rgba(119, 167, 248, 1)'
								}, {
									offset: 1,
									color: 'rgba(119, 167, 248, 1)'
								}]),
								barBorderRadius: 11,
							}
						},
						data:bjdata
					}, {
						name: '预警数量',
						type: 'bar',
						barWidth: '8',
						itemStyle: {
							normal: {
								color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: 'rgba(192, 220, 253, 1)'
								}, {
									offset: 1,
									color: 'rgba(192, 220, 253, 1)'
								}]),
								barBorderRadius: 11,
							}

						},
						data: yjdata
					},]
				}
				myChart.setOption(option)
			},
			
			swiper() {
				var list = []
				//一次显示几个个
				for(var i = 0; i < this.swiperList.length; i += 3) {
					list.push(this.swiperList.slice(i, i + 3));
				}
				this.swiperList = []
				for(var j = 0; j < list.length; j++) {
					this.swiperList.push(list[j])
				}
			},
			shebInfo(data) {
				let seriesdata=[]
				let xdata=[]
				if(data){
					for (let i=0;i<data.list.length;i++) {
						seriesdata.push({						
							name: data.list[i].name,
							type: 'line',
							smooth: true,
							data: data.list[i].value,
							symbol: 'circle',
							symbolSize: 0,
						})
					}
					xdata=data.xdata
				}else{
					xdata=[]
					seriesdata=[]
				}
				let myChart = echarts.init(document.getElementById('shebInfo'));
				let option = {
					tooltip: {
						trigger: 'axis',
						transitionDuration: 0
					},
					grid: {
						top: '15%',
						left: '10%',
						right: '10%',
						bottom: '30%'
					},
					legend: {
						bottom: '0'
					},
					color: ["#97EBFE", "#FDE652", "#8CFA73", "#5C62F9", "#FC4C84", "#326CFF"],
					xAxis: [{
						type: 'category',
						axisLine: {
							show: false,
							color: '#A582EA'
						},

						axisLabel: {
							color: '#000',
							width: 100
						},
						splitLine: {
							show: false
						},
						boundaryGap: false,
						data:xdata

					}],

					yAxis: [{
						type: 'value',
						min: 0,
						// max: 140,
						splitNumber: 4,
						splitLine: {
							show: true,
							lineStyle: {
								color: '#000000',
								opacity: 0.15
							}
						},
						boundaryGap: true,
						axisLine: {
							show: false
						},
						axisLabel: {
							show: true,
							textStyle: {
								color: '#000'
							}
						},
						axisTick: {
							show: false
						}
					}],
					series:seriesdata
				}
				myChart.setOption(option)
			},
			remsize() {
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
	}
</script>

<style scoped lang="scss">
	.subject {
		height: 0.16rem;
		font-size: 0.16rem;
		font-weight: 500;
		color: #404040;
		display: flex;
		align-items: center;
		margin-left: 0.08rem;
		margin-top: 0.12rem;
		img {
			margin-right: 0.1rem;
			height: 0.16rem;
			width: 0.16rem;
		}
	}
	
	.content_box {
		height: 100%;
		display: flex;
		align-items: center;
		justify-content: space-evenly;
	}
	
	.foun_left {
		height: 95.5%;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}
	
	.sheb_list_box {
		height: 47.4%;
		width: 4.47rem;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 0.05rem;
	}
	
	.list_box {
		width: 4.25rem;
		height: 90%;
		margin: 0 auto;
	}
	
	.list_tit {
		display: flex;
		color: #286DFF;
		text-align: center;
		font-size: 0.12rem;
		border-bottom: 1px solid #286DFF;
		padding-bottom: 5px;
		margin-top: 0.2rem;
	}
	
	.list_con {
		width: 4.25rem;
		height: 90%;
	}
	
	.list_con {
		overflow: auto;
		scrollbar-width: none;
		/* firefox */
		-ms-overflow-style: none;
		/* IE 10+ */
	}
	
	.list_con::-webkit-scrollbar {
		display: none;
		/* Chrome Safari */
	}
	
	.sheblist {
		display: flex;
		text-align: center;
		align-items: center;
		font-size: 0.12rem;
		color: #404040;
		height: 42px;
		border-bottom: 1px solid rgba(121, 121, 121, 0.15);
	}
	
	.sheb_echarts {
		width: 4.47rem;
		height: 50%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 0.05rem;
	}
	
	#shebInfo {
		width: 100%;
		height: 92%;
	}
	
	.foun_center {
		width: 6.94rem;
		height: 95.5%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.44);
		border-radius: 0.05rem;
	}
	
	.swiper_box {
		width: 100%;
		height: 13.3%;
		margin-top: 0.34rem;
		position: relative;
	}
	
	.swiper_btn {
		width: 6.32rem;
		height: 100%;
		margin: 0 auto;
		overflow: hidden;
		
	}
	.syright{
		position: absolute;
		width: 0.12rem;
		height: 0.19rem;
		top: 50%;
		transform: translateY(-50%);
		right: 0.1rem;	
		cursor: pointer;
		
	}
	.syleft{
		position: absolute;
		width: 0.12rem;
		height: 0.19rem;
		top: 50%;
		transform: translateY(-50%);
		left: 0.1rem;
		cursor: pointer;
	}
	.speed_con_box{
		height: 100%;
	}
	.speed_con_box li {
		width: 2.02rem;
		height: 100%;
		float: left;
		margin-right: 0.13rem;
		display: flex;
		flex-direction: column;
		justify-content: center;
		cursor: pointer;
	}
	.speed_con_box li:last-child {
		margin-right: 0;
	}
	.xzsheb{
		background: url(./img/shebxz.png) no-repeat;
		background-size: 100% 100%;
		color: #FFFFFF;
		font-size: 0.16rem;
	}
	.wxzsheb{
		background: url(./img/shebwxz.png) no-repeat;
		background-size: 100% 100%;
		color: #404040;
		font-size: 0.16rem;
	}
	/deep/.swiper-container{
		height: 100%;
	}
	.jkname{
		margin-bottom: 0.15rem;
		text-align: center;
	}
	.jktyoe{
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.jktyoe img{
		width: 0.28rem;
		height: 0.28rem;
		margin-right: 0.1rem;
	}
	.lenged_box{
		width: 4rem;
		display: flex;
		justify-content: space-between;
		color: #666666;
		font-size: 0.12rem;
		margin: 0 auto;
		margin-top: 0.6rem;
	}
	.lenged_box div{
		display: flex;
		align-items: center;
	}
	.lenged{
		width:0.08rem;
		height:0.08rem;
		border-radius: 50%;
		margin-right: 0.15rem;
		
	}
	.jk_box{
		width: 5.21rem;
		margin: 0 auto;
		height: 58.6%;
		margin-top: 0.36rem;
		background: url(./img/gzm_bg.png) no-repeat;
		background-size: 100% 100%;
	}
	.foun_right{
		width: 4.58rem;
		height: 95.5%;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}
	.statistics_box{
		height: 45.6%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 0.05rem;
		overflow: hidden;
	}
	#statistics{
		height: 90%;
	}
	.cumulative_box{
		height: 51.8%;
		background: #FFFFFF;
		box-shadow: 0px 0px 10px 0px rgba(145, 180, 241, 0.29);
		border-radius: 0.05rem;
		overflow: hidden;
	}
	#cumulative{
		height: 90%;
	}
	#container{
		width: 5.21rem;
		margin: 0 auto;
		height: 58.6%;
		position: relative;
		margin-top: 0.36rem;
		/*background: url(./img/gzm_bg.png) no-repeat;
		background-size: 100% 100%;*/
	}
	/deep/ .labelss{
		background: red;
		color: #FFFFFF;
	}
</style>