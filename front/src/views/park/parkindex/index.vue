<template>
	<div class="imgbox">
		<img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    class="indeximg" src="../../../../static/img/indeximg.png">
	</div>
</template>

<script>
</script>

<style scoped>
	.imgbox{
		padding: 10px 10px 0 10px;
		width: 100%;
		height: 100%;
	}
	.indeximg{
		height: 100%;
		width: 100%;
	}
</style>