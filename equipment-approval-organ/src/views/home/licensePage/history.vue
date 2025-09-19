<template>
  <div>
    <template v-if="historyList.length">
      <div v-for="item in historyList" :key="item.id" class="msg-item">
        <div class="time">{{ item.createTime }}</div>
        <div class="handler">{{ item.actionDesc }}</div>
        <div class="remark">{{ item.remark }}</div>
        <div class="user">操作员：{{ item.operatorName || "--" }}</div>
      </div>
    </template>
    <el-empty v-else :image-size="80"></el-empty>
  </div>
</template>

<script setup>
import { useBasisStore } from "@/pinia/modules/basis";
import { getLicenseHistoryList } from "@/apis/home";
const basisStore = useBasisStore();
const licenseBasis = computed(() => basisStore.getLicenseBasis);
const applicationId = computed(() => licenseBasis.value.applicationId);

let historyList = ref([]);
let loading = ref(false);
const getHistoryList = async () => {
  if (!applicationId.value) return;
  try {
    loading.value = true;
    const response = await getLicenseHistoryList({ id: applicationId.value });
    historyList.value = response.data;
  } catch (error) {
    console.log(error);
  } finally {
    loading.value = false;
  }
};
onMounted(() => {
  getHistoryList();
});
</script>

<style lang="scss" scoped>
.msg-item {
  background: rgba(248, 250, 252, 0.8);
  border: 1px solid rgba(226, 232, 240, 0.6);
  border-radius: 8px;
  padding: 15px;
  padding-left: 20px;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  &:hover {
    box-shadow: 0 4px 12px rgba(22, 93, 255, 0.1);
    border-color: rgba(22, 93, 255, 0.3);
  }
  & + .msg-item {
    margin-top: 10px;
  }
  &::before {
    content: "";
    display: block;
    height: 100%;
    width: 4px;
    position: absolute;
    top: 0;
    left: 0;
    background-image: linear-gradient(to bottom, #165dff, #8658f3);
  }
  .time {
    font-size: 14px;
    color: #2667ea;
    font-weight: 700;
    margin-bottom: 5px;
  }
  .handler {
    font-size: 14px;
    margin-bottom: 5px;
    color: #2667ea;
  }
  .remark {
    font-size: 12px;
    margin-bottom: 5px;
  }
  .user {
    font-size: 12px;
  }
}
</style>
