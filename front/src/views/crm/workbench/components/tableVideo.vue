<template>
	<div class="tab">
		<div class="tab-con">
			<!--<div v-show="0==index">
				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/videobg.jpg"/>
			</div>
			<div v-show="1==index">
				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="proDate.projectImg"/>
				
			</div>
			<div v-show="2==index">
				<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="../../../../../static/img/videobg2.jpg"/>
				
			</div>-->
			<el-carousel trigger="click" height="100%">
		      <el-carousel-item v-for="item in fileList" :key="item.url">
		        <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-src="item.url"/>
		      </el-carousel-item>
		    </el-carousel>
		</div>
	</div>
</template>

<script>
	import request from '@/utils/request'
	export default{
		name: 'tavlevideo',
		data(){
			return {
				index:1,
				proDate:{},
				fileList:[]
			}
		},
		mounted(){
			this.getProDate()
			this.remsize()
		},
		methods:{
			getProDate(){
				let projectId=JSON.parse(localStorage.getItem('loginUserInfo')).projectId
				
				request({
				    url: 'build/projectInfo/getProjectInfoById/'+projectId,
				    method: 'post',
				    data: {},
				    headers: {
				      'Content-Type': 'application/json;charset=UTF-8'
				    }
				 }).then((res)=>{
				  	this.proDate=res.data
				  	
				  	request({
					    url: 'adminFile/queryFileList/'+this.proDate.imgBatchId,
					    method: 'post',
					    data: {},
					    headers: {
					      'Content-Type': 'application/json;charset=UTF-8'
					    }
					 }).then((res)=>{
					  	console.log(res)
					  	this.fileList=res.data
					  })
				  	
				  })
			},
			tabbar(index){
				this.index=index
			},
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
	}
</script>

<style scoped lang="scss">
.tab{
	width: 100%;
	height: 100%;
	position: relative;
}
.tab-bar{
	position: absolute;
	z-index: 10;
	transform: translateX(-50%);
	left: 50%;
	top: 26px;
	width: 3.5rem;
	display: flex;
	justify-content: space-between;
}
.tab-bar span{
	display:inline-block;
	width: 1.05rem;
	height: 0.23rem;
	background: #F5F6F9;
	border-radius: 2px;	
	line-height: 0.23rem;
	text-align: center;
	font-size: 0.14rem;
	cursor: pointer;
}
.tab-bar .active{
	background: #27BEFF;	
	color: #FFFFFF;
}
.tab-con div{
	height: 100%;
	width: 100%;
	position: absolute;
	img{
		height: 100%;
		width: 100%;
	}
}
</style>