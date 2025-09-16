<template>
  <el-dialog v-model="visible">
    <slot></slot>
  </el-dialog>
</template>

<script setup>
let visible = defineModel("visible", {
  type: Boolean,
  default: false,
});
</script>

<style lang="scss" scoped></style>
