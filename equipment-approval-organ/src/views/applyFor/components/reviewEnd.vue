<template>
  <div>
    <div class="p-t-b-20 flex items-center justify-center">
      <svg-icon name="codicon:pass-filled" color="#008000" size="50" />
    </div>
    <h1 class="flex items-center justify-center c-333 f-s-20 letter-spacing-2 m-b-5">申报完成</h1>
    <p class="text-align-center f-s-14 w-500 m-auto">恭喜!您的申请流程已全部完成。</p>
    <p class="text-align-center f-s-14 w-500 m-auto">请根据评审结果进行后续操作。</p>
    <el-divider />
    <div class="flex items-center justify-center">
      <el-button type="primary" size="default" @click="handleClick">
        <template #icon>
          <svg-icon name="fa7-solid:home-lg-alt" />
        </template>
        返回首页
      </el-button>
    </div>
  </div>
</template>

<script setup>
const router = useRouter();
const handleClick = () => {
  router.replace({ path: "/home/index-page" });
};
</script>

<style lang="scss" scoped>
.w-500 {
  max-width: 500px;
}
.m-auto {
  margin: 0 auto;
}
.text-align-center {
  text-align: center;
}
</style>
