<template>
  <div class="msg-box">
    <el-button type="primary" size="small" @click.stop="openDialogFn">
      <template #icon>
        <svg-icon name="tabler:bell-filled" />
      </template>
    </el-button>
  </div>
</template>

<script setup>
const openDialogFn = () => {};
</script>

<style lang="scss" scoped>
.msg-box {
  &:deep(.el-button) {
    box-shadow: 0 0 10px rgba(199, 199, 199, 0.1);
    border: 1px solid rgba(227, 227, 227, 0.3);
    border-radius: 10px !important;
    --el-button-bg-color: rgba(64, 158, 255, 0.27);
    height: 32px;
    padding: 5px;
    .el-icon {
      font-size: 24px !important;
    }
    &:hover {
      transform: translateY(-1px);
    }
  }
}
</style>
