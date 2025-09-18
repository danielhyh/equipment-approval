<template>
  <el-dialog
    v-model="visible"
    v-bind="attr"
    modal-class="custom-modal-style"
    class="custom-dialog-style"
    @close="close"
    @closed="closed"
  >
    <template #header>
      <slot name="header"></slot>
    </template>
    <slot></slot>
    <template #footer>
      <slot name="footer"></slot>
    </template>
  </el-dialog>
</template>

<script setup>
let props = defineProps({
  attr: {
    type: Object,
    default: () => ({
      title: "对话框",
      width: "50%",
      "append-to-body": true,
      "destroy-on-close": true,
      "close-on-click-modal": false,
      draggable:true
    }),
  },
});
let visible = defineModel("visible", {
  type: Boolean,
  default: false,
});
let $emit = defineEmits(["closed", "close"]);
const close = () => {
  $emit("close");
};
const closed = () => {
  $emit("closed");
};
</script>

<style lang="scss" scoped></style>
