<template>
  <div :style="{ height: `${property.height}px` }" class="title-bar">
    <el-image v-if="property.bgImgUrl" :src="property.bgImgUrl" class="w-full" fit="cover" />
    <div class="absolute left-0 top-0 w-full h-full flex flex-col justify-center">
      <!-- 标题 -->
      <div
        v-if="property.title"
        :style="{
          fontSize: `${property.titleSize}px`,
          fontWeight: property.titleWeight,
          color: property.titleColor,
          textAlign: property.textAlign,
          marginLeft: `${property.marginLeft}px`,
          marginBottom: '4px'
        }"
      >
        {{ property.title }}
      </div>
      <!-- 副标题 -->
      <div
        v-if="property.description"
        :style="{
          fontSize: `${property.descriptionSize}px`,
          fontWeight: property.descriptionWeight,
          color: property.descriptionColor,
          textAlign: property.textAlign,
          marginLeft: `${property.marginLeft}px`
        }"
      >
        {{ property.description }}
      </div>
    </div>
    <!-- 更多 -->
    <div
      v-show="property.more.show"
      :style="{
        color: property.descriptionColor
      }"
      class="more"
    >
      <span v-if="property.more.type !== 'icon'"> {{ property.more.text }} </span>
      <Icon v-if="property.more.type !== 'text'" icon="ep:arrow-right" />
    </div>
  </div>
</template>
<script lang="ts" setup>
import { TitleBarProperty } from './config'

/** 标题栏 */
defineOptions({ name: 'TitleBar' })

defineProps<{ property: TitleBarProperty }>()
</script>
<style lang="scss" scoped>
.title-bar {
  position: relative;
  width: 100%;
  min-height: 20px;
  box-sizing: border-box;

  /* 更多 */
  .more {
    position: absolute;
    top: 0;
    right: 8px;
    bottom: 0;
    display: flex;
    margin: auto;
    font-size: 10px;
    color: #969799;
    align-items: center;
    justify-content: center;
  }
}
</style>
