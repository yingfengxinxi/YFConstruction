<template>

    <div ref="workbench" class="crm-workbench">
        <div class="content" style="width: 100%;height: 880px">
            <img   :onerror='`this.src="${require("@/assets/noimgsrc.png")}"`'    src="/static/img/homebg.png" style="width: 100%;height: 880px">
        </div>
    </div>
</template>

<script>
    export default {
        name: "BuildIndex"
    }
</script>

<style scoped>

    .crm-workbench {
        position: fixed;
        top: 60px;
        right: 50px;
        bottom: 10px;
        left: 50px;
        margin: 10px;
        border-radius: 4px;
    }

    .content {
        width: 100%;
        display: flex;
        justify-content: space-between;
        flex-wrap: wrap;
        height: 30%;
    }
</style>