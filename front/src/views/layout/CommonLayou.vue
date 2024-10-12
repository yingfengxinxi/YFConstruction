<template>
	<el-container>
		<el-container>
			<sidebar :items="routerItems"  class="sidebar-container" />

			<el-main id="manager-main-container">
				<!-- <app-main/>-->
				<page-header></page-header>
				<div class="content" style="padding-top: 0px">
					<app-main/>
				</div>
			</el-main>

		</el-container>
	</el-container>
</template>

<script>
	import { mapGetters } from 'vuex'
	import { Navbar, Sidebar, AppMain } from './components'
	import PageHeader from './components/PageHeader'

	export default {
		name: 'CommonLayout',
		components: {
			Navbar,
			Sidebar,
			AppMain,
			PageHeader
		},
		data() {
			return {
				routerItems: [],
				activeclass: {}
			}
		},

		computed: {
			...mapGetters(['basePath', 'navActiveIndex', 'applicationId', 'routers', 'pagea'])
		},
		created(){
			//路由
			// this.routerItems = this.routers[this.applicationId].children;
			// console.log("this.routers==="+JSON.stringify(this.routers))
			this.routerItems = []
			for (let key in this.routers) {
				let addFlag = true;
				for(let i=0;i<this.routerItems.length;i++){
					if(this.routers[key].sort<this.routerItems[i].sort){
						this.routerItems.splice(i,0,this.routers[key])
						addFlag = false;
						break;
					}
				}
				if(addFlag){
					this.routerItems.push(this.routers[key]);
				}
				// console.log("this.routerItems==="+JSON.stringify(this.routerItems))
			}
			// this.$router.push(this.getFirstChild(this.routerItems, this.basePath))
			this.$router.push("/logo/index")
//			头部标签页第一个数据添加
			this.activeclass = {
				close: true,
				title: "首页",
				comname: "/logo/index",
				name: "home"
			}
// 			this.activeclass = {
// 				close: true,
// 				title: this.routerItems[0].meta.title,
// 				comname:this.routerItems[0].path,
// 				name: this.routerItems[0].name
// 			}
			if(JSON.stringify(this.$store.state.app.pageheader).indexOf(JSON.stringify(this.activeclass)) == -1) {
				this.$store.state.app.pageheader.push(this.activeclass); // 进行动态的操作
			}
//			头部标签页第一个数据添加截至

		},
		mounted() {
		},
		beforeDestroy(){
			this.$store.state.app.pageheader=[]
		},
		watch:{
		  $route(to,from){
		  }
		},
		methods: {
			navClick(index) {},
			getFirstChild(children, path) {
				if(children && children.length) {
				  for(var i = 0;i<children.length;i++){
				    var one = children[i];
            if(!one.hidden){
              var childPath = one.path;
              if(childPath.indexOf("/") == 0) { //以/开头
                path = childPath;
              } else {
                path = path + "/" + childPath;
              }
              path = this.getFirstChild(one.children, path);
              break;
            }
          }
				}
				return path;
			},
		}
	}
</script>

<style lang="scss" scoped>
.content{
	height: calc(100% - 40px);
	/*height: calc(100% - 66px);*/
}
	.aside-container {
		position: relative;
		background-color: #2d3037;
		box-sizing: border-box;
	}

	.nav-container {
		padding: 0;
		box-shadow: 0px 1px 2px #dbdbdb;
		z-index: 100;
		min-width: 1200px;
	}

	#manager-main-container {
		max-height: 100%;
		padding: 0px;
	}

	.el-container {
		overflow: hidden;
		height: 100%;
	}
</style>
