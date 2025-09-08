<template>
  <!-- 修复：只监听change事件，避免重复触发 -->
  <div class="pagination-box">
    <el-pagination
      :style="alignStyle"
      v-model:current-page="modelPage"
      v-model:page-size="modelSize"
      :pager-count="pagerCount"
      :size="size"
      :hide-on-single-page="hideOnSinglePage"
      :page-sizes="pageSizes"
      :total="validTotal"
      :layout="layout"
      @change="handleChange"
      :disabled="disabled"
      :background="background"
      :prev-text="prevText"
      :next-text="nextText"
    />
  </div>
</template>

<script setup>
// 修复：正确使用defineOptions语法
const props = defineProps({
  align: {
    type: String,
    default: "center",
  },
  size: {
    type: String,
    default: "small",
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  background: {
    type: Boolean,
    default: false,
  },
  pagerCount: {
    type: Number,
    default: 5,
  },
  pageSizes: {
    type: Array,
    default: () => [10, 20, 30, 40],
  },
  layout: {
    type: String,
    default: "total,  prev, pager, next, sizes",
  },
  total: {
    type: Number,
    default: 10,
  },
  hideOnSinglePage: {
    type: Boolean,
    default: true,
  },
  prevText: {
    type: String,
    default: "",
  },
  nextText: {
    type: String,
    default: "",
  },
});

let alignStyle = computed(() => {
  return {
    "justify-content": props.align,
  };
});
// 修复：使用defineModel并设置合理的默认值
let modelPage = defineModel("pageNum", {
  type: Number,
  default: 1,
});
let modelSize = defineModel("pageSize", {
  type: Number,
  default: 10,
});

// 修复：添加对total的验证处理
const validTotal = computed(() => {
  return props.total < 0 ? 0 : props.total;
});

let emit = defineEmits(["changePageOrPageSize", "update:pageNum", "update:pageSize"]);

// 修复：简化事件处理，避免重复触发
const handleChange = (currentPage, pageSize) => {
  // 确保页码在有效范围内
  if (validTotal.value === 0) {
    modelPage.value = 1;
    return;
  }

  // 发出统一的事件，包含所有分页信息
  emit("changePageOrPageSize", {
    pageNum: currentPage,
    pageSize,
    total: validTotal.value,
  });
};

// 添加watch监听器，确保页码不会超出总页数
watch([() => validTotal.value, modelSize], () => {
  if (validTotal.value === 0) {
    modelPage.value = 1;
    return;
  }

  const totalPages = Math.ceil(validTotal.value / modelSize.value);
  if (modelPage.value > totalPages && totalPages > 0) {
    modelPage.value = totalPages;
  }
});
</script>

<style lang="scss" scoped></style>
