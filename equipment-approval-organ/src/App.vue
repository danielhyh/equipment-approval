<template>
  <div class="web-container">
    <el-config-provider :locale="language">
      <router-view></router-view>
    </el-config-provider>
  </div>
</template>

<script setup>
import zhCn from "element-plus/dist/locale/zh-cn.mjs";
import { onMounted, computed } from "vue";

const language = computed(() => zhCn);

onMounted(() => {
  function isJsonString(str) {
    try {
      JSON.parse(str);
      return true;
    } catch (e) {
      return false;
    }
  }
});
// import gsap from "gsap"
// const target = ref(null);
// const startAnimation = () => {
//   // 测试动画一
//   // gsap.fromTo(
//   //   target.value,
//   //   { opacity: 0, x: -100, y: 0, rotationY: 0 },
//   //   { opacity: 1, x: 0, y: 50, rotationY: 360, duration: 2, yoyo: true, repeat: -1 }
//   // )
//   // 测试动画二
//   gsap.to(target.value, {
//     duration: 10,
//     x: -400,
//     opacity: 0,
//     onUpdate: () => {
//       const currentX = gsap.getProperty(target.value, "x");
//       const y = 50 * Math.sin((2 * Math.PI * currentX)/100);
//       gsap.to(target.value, { y: y });
//     },
//     ease: "linear",
//     yoyo: true,
//     repeat: -1,
//   });
// };
// onMounted(() => {
//   startAnimation();
// });
</script>

<style lang="scss" scoped>
.web-container {
  position: relative;
  height: 100vh;
  overflow: hidden;
}
</style>
