<template>
  <el-card class="card-style" style="width: 100%">
    <template #header>
      <div class="card-header">
        <slot name="header"></slot>
      </div>
    </template>
    <slot></slot>
  </el-card>
</template>

<script setup name="Card"></script>

<style lang="scss" scoped>
.card-style {
  border: 1px solid rgba(226, 232, 240, 0.8);
  box-shadow: 0 8px 32px rgba(22, 93, 255, 0.08);
  border-radius: 16px;
  &:deep(.el-card__header) {
    background-image: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    border-bottom: 2px solid rgba(226, 232, 240, 0.6);
    padding: 15px 20px;
    font-weight: bold;
    .card-header {
      display: flex;
      align-items: center;
    }
  }
}
</style>
