<template>
  <div class="layout-app-main" id="AppMain">
    <div class="layout-app-header">
      <div class="left">
        <svg-icon name="solar:hospital-bold" :size="30" color="#93c5fd" />
        <span class="title-text">{{ pageTitle }}</span>
      </div>
      <div class="right">
        <el-button type="primary" size="default" :icon="Back" @click.stop="backHome"> 返回首页 </el-button>
      </div>
    </div>
    <div class="layout-app-content">
      <router-view></router-view>
    </div>
    <FooterBottom class="layout-app-footer" />
  </div>
</template>

<script setup>
import { Back } from "@element-plus/icons-vue";
import FooterBottom from "./components/footer.vue";
import { useBasisStore } from "@/pinia/modules/basis";
import config from "@/config";
const basisStore = useBasisStore();
const router = useRouter();
const route = useRoute();
let metaTitle = computed(() => route.meta.title);
let type = computed(() => route.query.type);
basisStore.setPageTitle(type.value);

const pageTitle = computed(() => {
  let title = basisStore.getPageTitle;
  return metaTitle.value + " " + title + " - " + config.systemName;
});
const backHome = () => {
  router.replace("/home/index-page");
};
</script>

<style lang="scss" scoped>
.layout-app-main {
  height: 100%;
  overflow: auto;
  position: relative;
  background-color: #f8fafe;
  .layout-app-header {
    position: sticky;
    top: 0;
    left: 0;
    right: 0;
    z-index: 99;
    min-height: var(--nav-height);
    padding: 0 20px;
    outline: 1px solid rgba(154, 208, 255, 0.5);
    backdrop-filter: blur(10px);
    background-image: linear-gradient(135deg, #165dff 0%, #06b6d4 100%);
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    overflow: hidden;
    cursor: default;
    .left {
      display: flex;
      align-items: center;
      justify-content: center;
      flex-wrap: wrap;
      overflow: hidden;
      cursor: default;
      .title-text {
        font-size: 18px;
        font-weight: bold;
        color: #fff;
        margin-left: 12px;
      }
    }
    .right {
      &:deep(.el-button) {
        box-shadow: 0 0 10px rgba(199, 199, 199, 0.1);
        border: 1px solid rgba(227, 227, 227, 0.3);
        border-radius: 10px !important;
        --el-button-bg-color: rgba(64, 158, 255, 0.27);
        &:hover {
          transform: translateY(-1px);
        }
      }
    }
  }
  .layout-app-content {
    min-height: calc(100% - var(--footer-height) - var(--nav-height));
    width: var(--content-width);
    margin: 0 auto;
    padding: 20px;
    background-color: #f8fafe;
  }
}
</style>
