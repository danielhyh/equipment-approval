<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <div class="number">404</div>
      <h1 class="title">页面不存在</h1>
      <p class="description">抱歉，您访问的页面不存在或已被移动</p>
      <el-button type="primary" @click="goBack" class="back-button">
        {{ isBack ? "返回上一页" : "返回首页" }}
      </el-button>
    </div>
  </div>
</template>

<script setup>
const router = useRouter();
// const route = useRoute()
console.log(router);
const goBack = () => {
  // 尝试返回上一页，如果不能则跳转到首页
  if (window.history.length > 1) {
    router.back();
  } else {
    router.push("/home");
  }
};
let isBack = computed(() => {
  return window.history.length > 1;
});
</script>

<style lang="scss" scoped>
.not-found-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f8fafe;
}

.not-found-content {
  text-align: center;
  padding: 40px;
  border-radius: 12px;
  background-color: #ffffff;
  box-shadow: 0 8px 32px rgba(22, 93, 255, 0.08);
  max-width: 500px;
  width: 100%;
}

.number {
  font-size: 120px;
  font-weight: bold;
  color: #3b82f6;
  margin-bottom: 20px;
  line-height: 1;
  opacity: 0.9;
}

.title {
  font-size: 28px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 12px;
}

.description {
  font-size: 16px;
  color: #64748b;
  margin-bottom: 32px;
}

.back-button {
  padding: 10px 24px;
  font-size: 16px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.back-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}
</style>
