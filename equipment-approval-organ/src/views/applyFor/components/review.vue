<template>
  <div>
    <!-- <h3 class="c-165DFF m-b-15 f-w-700">{{ pageTitle }}(申请单位为{{ deptName }})</h3> -->
    <!-- 评审状态 -->
     <!--  -->
    <div class="m-b-30 flex items-center justify-center">
        <svg-icon  :icon="reviewIcon.icon" :color="reviewIcon.color"  size="50"/>
    </div>
    <h1 class="flex items-center justify-center c-333">{{ reviewStatusLabel }}</h1>


  </div>
</template>

<script setup>
import {useDictStore} from "@/pinia/modules/dict.js";
let formAllData =  inject('formAllData')
let applyMsg = ref({
    appStatus:'1'
})
const dictStore = useDictStore();
const reviewStatusDict = computed(()=>dictStore.getDictTypeList("biz_app_status"))
const reviewStatusLabel = computed(()=>{
    return reviewStatusDict.value.find(item=>item.value === applyMsg.value?.appStatus)?.label || "未知"
})
const reviewIcon = computed(()=>{
    switch(applyMsg.value?.appStatus){
        case "1":
        case "4":
        case "7":
        case "8": // 待审核
            return {
                icon:"fa:warning",
                color:"#165DFF",
                text:"正在审核中，请耐心等待"
            }
        case "2": // 通过 
        case "5":
            return {icon:"codicon:pass-filled",color:"#008000",text:"审核通过",timeLabel:"未通过时间："}
        case "3": // 拒绝 
        case "6":
            return {icon:"vaadin:close-circle",color:"#FF0000",text:"很遗憾，您的申请未通过"}
        case "9": // 结束
            return {icon:"el-icon:close",color:"#FF0000",text:"您的申请已被取消"}
    }
})

</script>

<style lang="scss" scoped></style>
