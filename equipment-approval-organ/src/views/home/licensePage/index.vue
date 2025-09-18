<template>
  <div v-if="componentList.length">
    <!-- 基本信息 -->
    <div class="card-title-head m-b-20">
      <div class="flex items-center m-b-10">
        <svg-icon name="icon-park-solid:copy" size="32" color="#165dff" class="m-r-5" />
        <div class="f-s-24 f-w-700 l-h-1_5 c-165DFF letter-spacing-2">{{ licenseDeviceName }} - {{ metaTitle }}</div>
      </div>
      <div class="f-s-14 c-64748b l-h-1_5">
        <span class="m-r-5">许可证编号: {{ licenseNo || "--" }}</span>
      </div>
    </div>
    <!-- 其他信息 -->
    <div class="card-container-box">
      <div class="select-com-box m-b-15">
        <div
          v-for="item in componentList"
          :key="item.name"
          class="flex items-center p-t-b-10 p-l-r-10 c-pointer"
          :class="{ active: item.key === activeItem.key }"
          @click.stop="handleClick(item)"
        >
          <svg-icon :name="item.icon" size="22" class="m-r-5" />
          <span class="f-s-14">{{ item.label }}</span>
          <i :class="{ 'c-dc2626': item.key !== activeItem.key }" v-if="item.tag">*</i>
        </div>
      </div>

      <div class="core-box">
        <keep-alive>
          <component :is="activeItem.component" :disabled="!activeItem.tag" ref="activeComRef" />
        </keep-alive>
      </div>

      <!-- footer -->
      <div class="footer-box" v-show="activeItem.tag">
        <el-button type="primary" round size="large" @click.stop="submitFn()">保存</el-button>
      </div>
    </div>
  </div>
  <el-empty v-else :image-size="200" />
</template>

<script setup>
import comList from "./index";
import { useBasisStore } from "@/pinia/modules/basis";
const basisStore = useBasisStore();
const route = useRoute();
const router = useRouter();
const metaTitle = route.meta.title;
const metaPage = route.meta.page;
const licenseDeviceName = basisStore.licenseBasis?.licenseDeviceName || "";
let licenseNo = ref(basisStore.licenseBasis?.licenseNo || "");

const componentList = computed(() => {
  switch (metaPage) {
    case "copy":
      return comList.copyComList;
    case "file":
      return comList.fileComList;
    case "detail":
      return comList.detailComList;
    default:
      return [];
  }
});
let activeItem = ref(componentList.value[0]);
let activeComRef = ref(null);
const handleClick = (item) => {
  activeItem.value = item;
};
const submitFn = () => {
  let pass = activeComRef.value?.submit();
  if (pass) {
    router.replace({ path: "/home/index-page" });
  }
};
onMounted(() => {
  let key = route.query?.key || "";
  if (key) {
    activeItem.value = componentList.value.find((item) => item.key === key) || activeItem.value;
  }
});
</script>

<style lang="scss" scoped>
.card-title-head {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(22, 93, 255, 0.08);
  padding: 15px 20px;
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-left: 4px solid #165dff;
}
.card-container-box {
  background-color: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(22, 93, 255, 0.08);
  overflow: hidden;
  border: 1px solid rgba(226, 232, 240, 0.8);
  .select-com-box {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 10px;
    padding: 5px 10px;
    > div {
      border-radius: 8px;
      color: #64748b;
      transition: all 0.3s ease-in-out;
      &:hover {
        color: #165dff;
        background-color: #165cff21;
        box-shadow: 0 2px 4px 0px #00000020;
      }
      &.active {
        color: #fff;
        background-color: #165dff;
        box-shadow: 0 2px 4px 0px #00000020;
      }
    }
  }
  .core-box {
    min-height: 500px;
    margin-bottom: 20px;
    padding: 10px 20px;
  }
  .footer-box {
    height: 90px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #f6f6f6;
    box-shadow: 0 -1px 2px 0px #f3f3f3;
    border-top: 2px solid #efefef;
  }
}
</style>
