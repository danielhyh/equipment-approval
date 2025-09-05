<template>
  <svg v-if="islocalName" :class="svgClass" v-bind="$attrs" :style="{ color: color, fontSize: size + 'px' }">
    <use :href="iconName"></use>
  </svg>
  <Icon v-else :icon="props.name" :style="{ color: color, fontSize: size + 'px' }" />
</template>
​
<script setup>
import { computed } from "vue";
import { Icon } from "@iconify/vue"; // 在线使用 占用http请求
const props = defineProps({
  name: {
    type: String,
    required: true,
    default: "svg-icon:coll",
  },
  color: {
    type: String,
    default: "",
  },
  size: {
    type: [Number, String],
    default: 24,
  },
});
const islocalName = computed(() => {
  return props.name.startsWith("svg-icon:");
});
const iconName = computed(() => (islocalName ? `#icon-${props.name.split(":")[1]}` : props.name));
const svgClass = computed(() => {
  if (props.name) return `svg-icon icon-${props.name}`;
  return "svg-icon";
});
</script>
​
<style scoped>
.svg-icon {
  width: 1em;
  height: 1em;
  fill: currentColor;
  vertical-align: middle;
}
</style>
