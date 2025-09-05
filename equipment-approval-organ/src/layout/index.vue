<template>
  <div class="layout-app-main" id="AppMain">
    <HeaderTop class="layout-app-header"/>
    <div class="layout-app-content">
      <router-view></router-view>
    </div>
    <FooterBottom class="layout-app-footer"/>
  </div>
</template>

<script setup>
import HeaderTop from "./components/header.vue";
import FooterBottom from "./components/footer.vue";
</script>

<style lang="scss" scoped>
/* 这里可以添加组件样式 */
.layout-app-main {
  height: 100%;
  overflow: auto;
  position: relative;
  padding-top: var(--nav-height);
  .layout-app-header {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 99;
  }
  .layout-app-content {
    min-height: calc(100% - var(--footer-height));
    width: var(--content-width);
    margin: 0 auto;
    padding: 20px;
    background-color: #fff;
  }
}
</style>
