<template>
  <div class="app-main" :class="flag==true?'scrotrue':'scorfalse'" style="background-color: white;height: 100%;">
    <keep-alive :include="includelist">
    	<router-view ></router-view>
    </keep-alive>
    <slot />
  </div>
</template>

<script>
	import PageHeader from './PageHeader'
	import { mapGetters } from 'vuex'
export default {
  name: 'AppMain',
  
  data(){
  	return {
  		flag:false,
  		includelist:[]
  	}
  },
  components: {
			PageHeader
		},
  computed: {
			...mapGetters(['pageheader'])
		},
  watch:{
   
  $route(to,from){
    if(to.path=='/logo/index'){
    	this.flag=false
    }else if(to.path=='/proj/allData'){
    	this.flag=false   	
    }else if(to.path=='/equipment/environMonitor'){
      this.flag=false  
    }else if(to.path=='/equipment/towerCraneMonitor'){
      this.flag=false  
    }else{
    	this.flag=true 
    }
  },
  pageheader(newdata,olddata){
  	this.includelist=[]
  	for(let i=0;i<newdata.length;i++){
  		this.includelist.push(newdata[i].comname)
  	}
  	console.log(this.includelist)
  }
},
created(){
	// console.log(this.pageheader)
},
}
</script>

<style scoped>
.app-main {
  position: relative;
  height: 100%;
}
.scrotrue{
	overflow: auto;
}
.scorfalse{
	overflow: hidden;	
}
</style>
