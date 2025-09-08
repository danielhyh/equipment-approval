<template>
  <!-- 这里可以添加模板内容 -->
  <div class="basis-container">
    <div class="basis-item">
      <svg-icon name="fa-solid:hospital" />
      <div class="value">陕西省人民医院</div>
    </div>
    <div class="basis-item">
      <svg-icon name="fa-solid:user" />
      <div class="value">张三</div>
    </div>
    <div class="basis-item">
      <svg-icon name="fa-solid:phone" />
      <div class="value">13800000000</div>
    </div>
    <div class="basis-item">
      <el-button type="primary" size="small">
        <template #icon>
          <svg-icon name="dashicons:info-outline" />
        </template>
        基础数据
      </el-button>
    </div>
  </div>
</template>

<script setup name="BasisInfo">
// 这里可以添加 setup 语法糖下的脚本逻辑
import { useUserStore } from "@/pinia/modules/user";
let userStore = useUserStore();

const userData = computed(() => userStore.getUser);

const deptName = computed(() => userData.value.hospital);
const userName = computed(() => userData.value.name);
const phone = computed(() => userData.value.phone);
</script>

<style lang="scss" scoped>
// 这里可以添加 SCSS 样式
.basis-container {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
  &:deep(.basis-item) {
    flex-shrink: 0;
    display: flex;
    align-items: center;
    > .iconify {
      font-size: 14px !important;
      color: #fff;
      margin-right: 4px;
    }
    .value {
      font-size: 14px;
      color: #fff;
    }
    .el-button {
      font-size: 14px;
      border-radius: 8px;
      box-shadow: 0 0 10px rgba(199, 199, 199, 0.1);
      border: 1px solid rgba(227, 227, 227, 0.3);
      --el-button-bg-color: rgba(64, 158, 255, 0.27);
      &:hover {
        transform: translateY(-1px);
      }
    }
  }
}
</style>
