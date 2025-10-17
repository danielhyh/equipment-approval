<template>
  <div class="license-container">
    <!-- 头部信息 -->
    <div class="license-header">
      <div class="header-top">
        <h1 class="license-title">乙类大型医用设备配置许可证</h1>
        <p class="issuing-authority">陕西省卫生健康委员会制发</p>
      </div>
    </div>

    <!-- 证书内容区域 -->
    <div class="license-content" v-loading="loading">
      <div class="boder-shadow">
        <div class="change-box" v-if="false">
          <div class="change-item" :class="{ active: current === 'origin' }" @click="changeCurrent('origin')">正本信息</div>
          <div class="change-item" :class="{ active: current === 'copy' }" @click="changeCurrent('copy')">副本信息</div>
        </div>
        <!-- 证书编号 -->
        <div class="certificate-number">
          <span class="number-text">{{ currentMsg[0]?.value }}</span>
          <span class="status-badge" :class="{ error: !pass }">
            {{ current === "origin" ? "正本" : "副本" }}-{{ pass ? "证书有效" : "证书资源未获取" }}
          </span>
        </div>

        <!-- 证书详情列表 -->
        <div class="detail-list">
          <div class="detail-item" v-for="item in currentMsg" :key="item.key">
            <svg-icon :name="item.icon" color="#145ef0c9" class="icon" />
            <span class="label">{{ item.label }}</span>
            <span class="value">{{ item.value }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部信息 -->
    <div class="license-footer">
      <p class="footer-text">陕西省卫生健康委员会</p>
      <p class="footer-text">大型医用设备配置许可证查验系统</p>
      <p class="copyright">© 2025 版权所有</p>
    </div>
  </div>
</template>

<script setup name="MobileLicense">
import { ElMessage } from "element-plus";
import allMsg from "./index";
import { getOriginMsg, getCopyMsg } from "@/apis/mobile";
const route = useRoute();

let originId = computed(() => route.query.o);
let copyId = computed(() => route.query.d);
let isCopy = computed(() => route.query.c === "1");
let loading = ref(false);
let currentMsg = ref([]);
let current = ref("origin");
let pass = ref(false);
// 适应移动设备的函数 - 基于rem的响应式字体系统
function adjustForMobile() {
  // 设置根元素的字体大小，作为rem的基准
  const baseFontSize = Math.min(window.innerWidth / 37.5, 16);
  document.documentElement.style.fontSize = `${baseFontSize}px`;

  // 监听窗口大小变化，实时调整根元素字体大小
  window.addEventListener("resize", () => {
    const newBaseFontSize = Math.min(window.innerWidth / 37.5, 16);
    document.documentElement.style.fontSize = `${newBaseFontSize}px`;
  });
}
// 初始化
const initFn = async () => {
  try {
    loading.value = true;
    let responseData = {};
    if (current.value === "origin") {
      currentMsg.value = JSON.parse(JSON.stringify(allMsg.originList));
      let { data } = await getOriginMsg(originId.value);
      responseData = Object.assign(responseData, data || {});
    } else {
      let { data } = await getOriginMsg(originId.value);
      let res = await getCopyMsg(copyId.value);
      responseData = Object.assign(responseData, res.data || {}, data || {});
      currentMsg.value = JSON.parse(JSON.stringify(allMsg.copyList));
    }
    currentMsg.value.forEach((item) => {
      if (item.key) {
        item.value = responseData[item.key] || "-";
      }
    });
    pass.value = true;
  } catch (e) {
    pass.value = false;
    ElMessage.error("初始化失败");
  } finally {
    loading.value = false;
  }
};
const changeCurrent = (val) => {
  if(val === current.value) return;
  if ((originId.value && val === "origin") || (copyId.value && val === "copy")) {
    current.value = val;
    initFn();
    return;
  }
  ElMessage.error("证书资源不存在");
};
onMounted(() => {
  // 确保在移动设备上正确显示
  adjustForMobile();
  if(isCopy.value){
    current.value = "copy";
  }
  initFn();
});
</script>

<style lang="scss" scoped>
/* 基础样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  height: 100%;
  overflow: hidden;
}

/* 统一字体设置 - 移动端优化版（使用rem单位） */
.license-container {
  /* 基础字体大小系统 - 使用rem单位，便于响应式适配 */
  --font-size-base: 1.4125rem;
  --font-size-small: 1.1875rem;
  --font-size-medium: 1.4375rem;
  --font-size-large: 1.5625rem;
  --font-size-xlarge: 1.8125rem;

  /* 颜色系统 */
  --text-color-primary: #333;
  --text-color-secondary: #666;
  --text-color-tertiary: #999;

  /* 字体家族 - 针对不同系统和设备的优化字体栈 */
  --font-family: 
    /* iOS / macOS */ -apple-system, BlinkMacSystemFont, "SF Pro Display", "SF Pro Text",
    /* Windows */ "Segoe UI", "Microsoft YaHei UI", "Microsoft YaHei", "SimHei", /* Android */ "Roboto", "Noto Sans SC",
    "WenQuanYi Micro Hei", /* 通用回退 */ "Helvetica Neue", Arial, sans-serif;
}

body {
  font-family: var(--font-family);
  background-color: #f5f5f5;
  color: var(--text-color-primary);
  line-height: 1.8;
  font-size: var(--font-size-base);
  /* 文本渲染优化 - 提升字体清晰度 */
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-rendering: optimizeLegibility;
  /* 防止文本被选中（提升用户体验） */
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

.license-container {
  width: 100%;
  background-color: #fff;
  height: 100vh;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: relative;
  /* 防止在iOS上的页面缩放 */
  -webkit-text-size-adjust: 100%;
  -ms-text-size-adjust: 100%;
  text-size-adjust: 100%;
  font-family: var(--font-family);
  font-size: var(--font-size-base);
  /* 优化文本渲染 */
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* 防止内容溢出 */
  overflow-x: hidden;
  word-wrap: break-word;
  /* 使用flex布局实现固定头部和底部 */
  display: flex;
  flex-direction: column;
}

.change-box {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0.8rem;
  height: 4rem;
  margin-bottom: 1.6rem;
  .change-item {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 0.8rem;
    background-color: #f5f5f5;
    color: #007bff;
    font-size: 1.6rem;
    font-weight: bolder;
    transition: all 0.3s ease;
    &.active {
      background-color: #007bff;
      color: #fff;
    }
  }
}
/* 头部样式 - 固定在顶部 */
.license-header {
  background: linear-gradient(135deg, #007bff, #00bcd4);
  padding: 2rem 1rem;
  color: white;
  text-align: center;
  /* 固定头部高度 */
  flex-shrink: 0;
}

.header-top {
  position: relative;
}

.logo {
  width: 3rem;
  height: 3rem;
  background-color: white;
  border-radius: 50%;
  margin: 0 auto 1rem;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23007bff'%3E%3Cpath d='M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z'/%3E%3C/svg%3E");
  background-size: 60%;
  background-position: center;
  background-repeat: no-repeat;
}

.license-title {
  font-size: var(--font-size-xlarge);
  font-weight: 600;
  margin-bottom: 0.8rem;
  letter-spacing: 0.5px;
}

.issuing-authority {
  font-size: var(--font-size-base);
  opacity: 0.9;
}

/* 证书内容样式 - 可滚动区域 */
.license-content {
  padding: 2rem 1rem;
  flex: 1;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch; /* 平滑滚动效果 */
}
.boder-shadow {
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 2em 1rem;
  border-radius: 2rem;
  position: relative;
  overflow: hidden;
  &::before {
    content: "";
    display: block;
    width: 100%;
    height: 0.5rem;
    background-image: linear-gradient(to right, #007bff, #00bcd4);
    position: absolute;
    top: 0px;
    left: 0px;
  }
}
.certificate-number {
  text-align: center;
  padding-bottom: 2rem;
  margin-bottom: 1rem;
  border-bottom: 0.1rem solid #e2e2e2;
  position: relative;
}

.number-text {
  font-size: var(--font-size-xlarge);
  font-weight: bold;
  color: #007bff;
  display: block;
  margin-bottom: 0.8rem;
  letter-spacing: 1px;
}

.status-badge {
  display: inline-block;
  padding: 0.4rem 1rem;
  background-color: #28a745;
  color: white;
  border-radius: 20px;
  font-size: var(--font-size-small);
  font-weight: 500;
  &.error {
    background-color: #a73728;
  }
}

/* 详情列表样式 */
.detail-list {
  margin-bottom: 2rem;
}

.detail-item {
  display: flex;
  align-items: center;
  padding: 1rem 0.5rem;
  border-bottom: 1px solid #eee;
  /* 适配不同屏幕尺寸的布局 */
  flex-wrap: wrap;
}

.detail-item .icon {
  font-size: 1.8rem !important;
  margin-right: 0.5rem;
  flex-shrink: 0;
}

.detail-item .label {
  flex: 0 0 35%;
  margin-right: 0.9rem;
  font-weight: 500;
  color: var(--text-color-secondary);
  font-size: var(--font-size-base);
}

.detail-item .value {
  flex: 1;
  color: var(--text-color-primary);
  font-size: var(--font-size-medium);
  font-weight: 500;
  word-break: break-word;
}

/* 底部样式 - 固定在底部 */
.license-footer {
  text-align: center;
  padding: 1.5rem 1rem;
  border-top: 1px solid #eee;
  box-shadow: 0 -4px 8px 4px rgba(0, 0, 0, 0.05);
  /* 固定底部高度 */
  flex-shrink: 0;
}

.footer-text {
  font-size: var(--font-size-base);
  color: var(--text-color-secondary);
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.copyright {
  font-size: var(--font-size-small);
  color: var(--text-color-tertiary);
  margin-top: 0.8rem;
}

/* 媒体查询 - 适配不同屏幕尺寸（使用rem单位） */
@media screen and (max-width: 375px) {
  .license-container {
    --font-size-base: 1.1rem;
    --font-size-small: 1.125rem;
    --font-size-medium: 1.375rem;
    --font-size-large: 1.5rem;
    --font-size-xlarge: 1.75rem;
  }

  .license-title {
    font-size: var(--font-size-large);
  }

  .number-text {
    font-size: var(--font-size-large);
  }

  .detail-item .label {
    flex: 0 0 25%;
  }
}

@media screen and (max-width: 320px) {
  .license-container {
    --font-size-base: 1.0875rem;
    --font-size-small: 1.0425rem;
    --font-size-medium: 1.3125rem;
    --font-size-large: 1.125rem;
    --font-size-xlarge: 1.375rem;
  }

  .license-header {
    padding: 1.5rem 0.8rem;
  }

  .license-content {
    padding: 1.5rem 0.8rem;
  }

  .detail-item {
    padding: 0.8rem 0;
  }

  .detail-item .label {
    flex: 0 0 100%;
    margin-bottom: 0.5rem;
  }

  .detail-item .value {
    flex: 0 0 calc(100% - 1.3rem);
  }
}

/* 触摸反馈样式 */
.detail-item:active {
  background-color: #f8f9fa;
  /* iOS上的触摸反馈 */
  -webkit-tap-highlight-color: transparent;
}

/* 打印样式 */
@media print {
  .license-container {
    box-shadow: none;
    max-width: 100%;
  }
}
</style>
