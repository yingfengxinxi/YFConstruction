<template>
	<div class="pageheader">

		<div ref="boxwidth" class="ulbox">
			<ul ref="ulwidth">
				<li @click.stop="switchs(item)" v-for="(item,index) in pageheader"
					:class="activeclass==item.comname?(item.comname=='/logo/index'?'active-mini':'active'):''"
					:style="item.comname=='/logo/index'?'width:88px;':''">
					<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="pageicon" src="../../../../static/img/pageicon.png" v-if="item.comname=='/logo/index'"/>
					<span class="contentspan" :style="item.comname=='/logo/index'?'width:38px;':''">{{item.title}}</span>
					<div></div><span class="remove" @click.stop="remove(index,item)" v-if="index>0"></span>
				</li>
				<!--<li @click.stop="switchs(item)" :class="activeclass==item.comname?'active':''" v-for="(item,index) in pageheader"><span class="contentspan">{{item.title}}</span><span class="remove" @click.stop="remove(index,item)" v-if="index>0"></span></li>-->
			</ul>			
		</div>
		<div  class="activeshow">
			<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-show="leftshow" class="lefticon" @click="goleft" src="../../../../static/img/lefticon.png"/>
			<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    v-show="rightshow" class="righticon" @click="goright" src="../../../../static/img/righticon.png"/>
		</div>
		<el-dropdown class='closeshow' trigger="click" @command="handleCommand">
		  <span class="el-dropdown-link">
		   <!-- 下拉菜单<i class="el-icon-arrow-down el-icon--right"></i>-->
		   <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="closeallimg" src="../../../../static/img/closall.png"/>
		  </span>
		  <el-dropdown-menu slot="dropdown">
		    <el-dropdown-item command="all">关闭全部</el-dropdown-item>
		    <el-dropdown-item command="other">关闭其他</el-dropdown-item>
		    <el-dropdown-item command="right">关闭右侧</el-dropdown-item>
		  </el-dropdown-menu>
		</el-dropdown>
	</div>
</template>

<script>
	import { mapGetters } from 'vuex'
	export default {
		name: 'PageHeader',
		components: {},
		data() {
			return {
				routerItems: [],
				pageheader: [],
				activeclass: '',
				boxwidth:'',//外盒子宽度
				num:0,//可以放几个
				movewidth:'',//移动宽度
				routerdata:{},
				closeshow:false,
				leftshow:false,
				rightshow:false,
				indexpage:1,//一共有几页
				stay:0,//下标
			}
		},
		computed: {
			...mapGetters(['basePath', 'navActiveIndex', 'applicationId', 'routers', ]),
		},
		created(){			
			this.pageheader = this.$store.state.app.pageheader
			this.$store.state.app.activeclass=this.$store.state.app.pageheader[0].comname
			this.activeclass=this.$store.state.app.activeclass
		},
		mounted() {
			this.boxwidth=this.$refs.boxwidth.offsetWidth
			this.num=Math.floor(this.$refs.boxwidth.offsetWidth/153)+1
			this.$refs.boxwidth.style.width=(this.num*153-65)+'px'
			this.movewidth=this.num*153-65
		},
		watch: {
			//监听页面切换
			'$route'(to, from) {
				    let num='' 
					this.activeclass=this.$store.state.app.activeclass
					for(let i=0 ; i<this.$store.state.app.pageheader.length;i++){
							if(this.$store.state.app.pageheader[i].comname==this.activeclass){
								this.stay=Math.floor(i/this.num)
								this.$refs.ulwidth.style.left=-this.stay*this.movewidth+'px'
							}
					}
			},

			pageheader:function(to , from ){
				this.indexpage=Math.ceil(this.pageheader.length/this.num)//计算出一共几页
//				if(this.pageheader.length<(this.num+1)){				
//					this.leftshow=false	
//					this.rightshow=false										
//				}
				if(this.pageheader.length>(this.num)){
					if(this.stay==0){
						this.rightshow=true
						this.leftshow=false
					}else if(this.stay>0&&this.stay<this.indexpage-1){
						this.rightshow=true
						this.leftshow=true	
					}else if(this.indexpage==this.stay+1){
						this.rightshow=false
						this.leftshow=true						
					}
				}else {
					this.rightshow=false
						this.leftshow=false
				}
			},
			stay:function(){
				if(this.pageheader.length>(this.num)){
					if(this.stay==0){
						this.rightshow=true
						this.leftshow=false
					}else if(this.stay>0&&this.stay<this.indexpage-1){
						this.rightshow=true
						this.leftshow=true	
					}else if(this.indexpage==this.stay+1){
						this.rightshow=false
						this.leftshow=true						
					}
				}else {
					this.rightshow=false
						this.leftshow=false
				}
				
			}	

		},
		methods: {
			//点击头部
			switchs(item) {
				this.$store.state.app.activeclass=item.comname
				this.$router.push({name:item.name})
				console.log(item)
			},
			//点击关闭
			remove(index,item) {
				this.pageheader.splice(index, 1);
				//this.$store.state.app.pageheader.splice(index,1)
				if(this.$store.state.app.activeclass!=item.comname){					
				}else{
					this.$store.state.app.activeclass = this.pageheader[index - 1].comname				
					// this.$router.push(this.basePath + '/' + this.$store.state.app.pageheader[index - 1].comname)
					this.$router.push({name:this.$store.state.app.pageheader[index - 1].name})
					
				}
			},
			//点击向右箭头
			goright(){				
				if(this.indexpage>this.stay+1){
					this.stay+=1
					this.$refs.ulwidth.style.left=-this.stay*this.movewidth+'px'
				}
			},
			//点击向左箭头
			goleft(){
				if(this.stay>0){
					this.stay-=1
					this.$refs.ulwidth.style.left=-this.stay*this.movewidth+'px'
				}
			},
			//关闭全部
			handleCommand(command){
				if(command=='all'){
					this.pageheader.splice(1, this.pageheader.length);				
					this.$store.state.app.activeclass = this.pageheader[0].comname				
					// this.$router.push(this.basePath + '/' + this.$store.state.app.pageheader[0].comname)
					this.$router.push({name:this.$store.state.app.pageheader[0].name})
					this.stay=0
				}else if(command=='other'){
					let index
					if(this.$store.state.app.pageheader.length>1){
						for(let i=0 ; i<this.$store.state.app.pageheader.length;i++){
							if(this.$store.state.app.pageheader[i].comname==this.activeclass){
								index=i
							}
						}
						console.log(index)
						this.pageheader.splice(index+1, this.pageheader.length);	
						this.pageheader.splice(1, index-1);	
						this.$refs.ulwidth.style.left=0
					}
					this.stay=0
				}else if(command=='right'){
					let index				
					if(this.$store.state.app.pageheader.length>1){
						for(let i=0 ; i<this.$store.state.app.pageheader.length;i++){
							if(this.$store.state.app.pageheader[i].comname==this.activeclass){
								index=i
							}
						}
						this.pageheader.splice(index+1, this.pageheader.length);
						this.stay=Math.floor(index/this.num)
					}	
				}
			}
		}
	}
</script>

<style scoped>
	.pageheader {
		height: 39px;
		padding:0 10px 0 0;
		background: rgb(245, 246, 249);
	}
	.pageheader :after{
	    content: " ";
	    display: block;
	    clear:both;
	    height: 0;
	    visibility: hidden;
	}

	.ulbox{
		float: left;
		width: calc(100% - 90px);
		margin-left: 5px;
		position: relative;	
		height: 39px;
		overflow: hidden;
	}
	.pageheader ul{
		/*width: 2940px;*/
		white-space: nowrap;
		position: absolute;
		bottom: -1px;
		height: 35px;
	}
	.pageheader ul li {
		/*float: left;*/
		padding-left: 20px;
		line-height: 35px;
		cursor: pointer;
		width: 153px;
		height: 35px;
		font-size: 14px;
		display: inline-block;
	}
	
	.pageheader ul li .remove {
		cursor: pointer;
		float: right;
		margin-right: 21px;
		display: block;
		height: 35px;
		width: 9px;
		background: url("../../../../static/img/headerclose.png") no-repeat center;
	}
	.pageheader ul li div{
		float: right;
		width: 1px;
		height: 22px;
		background: #ACACAC;
		margin-top: 6px;
	}
	.content {
		padding: 20px;
		width: 100%;
		clear: both;
	}
	
	.active {
		/*background: #FFFFFF!important;*/
		background: url("../../../../static/img/hederbg.png") no-repeat center;	
	}
	.active-mini {
		 /*background: #FFFFFF!important;*/
		 background: url("../../../../static/img/hederbg-mini.png") no-repeat center;
	}
	.active div{
		background:none!important;
	}
	.pageicon{
		float: left;
		margin-top: 10px;
	}
	.lefticon{
		margin-left: 10px;
		margin-top: 9px;
		cursor: pointer;		
	}
	.righticon{
		margin-top: 9px;
		cursor: pointer;
		margin-left: 10px;
	}
	.contentspan{
		display: inline-block;
		width: 100px;
		overflow: hidden;
		text-overflow:ellipsis;
		white-space: nowrap;
		text-align: center;
		font-weight: 600;
	}
	.activeshow{
		float: left;
	}
	.closeshow{
		cursor: pointer;
		float: right;
		margin-top: 12px;
	}
</style>