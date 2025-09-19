<template>
  <div class="msg-box">
    <el-button type="primary" size="small" @click.stop="openDialogFn">
      <template #icon>
        <svg-icon name="tabler:bell-filled" />
      </template>
    </el-button>

    <Dialog v-model:visible="dialogVisible">
      <template #header>
        <svg-icon name="tabler:bell-filled" size="18" color="#409eff" class="m-r-5" />
        <span>消息通知</span>
      </template>
      <div class="h-400-scroll" v-loading="loading">
        <template v-if="msgList.length > 0">
          <div v-for="item in msgList" :key="item.id" class="msg-item">
            <div class="f-s-12">{{ formatDate(item.publishTime) }}</div>
            <div class="f-s-14 f-w-700 m-t-b-5 c-333">{{ item.title }}</div>
            <div class="f-s-12 l-h-1_5">{{ item.content }}</div>
            <el-divider style="margin-top: 15px; margin-bottom: 15px" />
            <div flex>
              <el-button type="primary" size="small" @click.stop="markReadFn(item.id)"> 标记已读 </el-button>
            </div>
          </div>
        </template>
        <template v-else>
          <el-empty description="暂无消息" :image-size="150" />
        </template>
      </div>
      <template #footer>
        <div class="flex items-center justify-center">
          <el-button type="info" :icon="Close" @click.stop="closeDialogFn"> 关闭 </el-button>
          <el-button type="primary" :icon="Check" @click.stop="allCheckFn" :disabled="loading"> 全部标记已读 </el-button>
        </div>
      </template>
    </Dialog>
  </div>
</template>

<script setup>
import { Close, Check } from "@element-plus/icons-vue";
import { getUnreadMsg, markReadMsg } from "@/apis/login";
import { formatDate } from "@/utils/tools";
let loading = ref(false);
let dialogVisible = ref(false);
let msgList = ref([
  {
    id: 39,
    title: "内窥镜手术器械控制系统申请进度更新",
    content: "您提交的内窥镜手术器械控制系统配置许可证申请专家审核已通过。, 审核意见：333。",
    publishTime: 1758266963000,
    status: "已发布",
    viewCount: 0,
  },
]);

const openDialogFn = () => {
  dialogVisible.value = true;
  getUnreadMsgFn();
};
const closeDialogFn = () => {
  dialogVisible.value = false;
};
const markReadFn = async (id) => {
  try {
    loading.value = true;
    await markReadMsg(id);
    getUnreadMsgFn();
  } catch (err) {
    console.log(err);
    loading.value = false;
  }
};
const allCheckFn = async () => {
  if (msgList.value.length === 0) return;
  try {
    loading.value = true;
    let requestList = msgList.value.map((item) => markReadMsg(item.id));
    await Promise.all(requestList);
    getUnreadMsgFn();
  } catch (error) {
    console.log(error);
    loading.value = false;
  }
};
const getUnreadMsgFn = async () => {
  try {
    loading.value = true;
    const res = await getUnreadMsg();
    msgList.value = res.data;
  } catch (err) {
    console.log(err);
  } finally {
    loading.value = false;
  }
};
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
.h-400-scroll {
  height: 400px;
  overflow-y: auto;
}
.msg-item {
  border: 2px solid rgba(148, 163, 184, 0.2);
  border-radius: 12px;
  padding: 15px;
  transition: all 0.3s ease;
  position: relative;
  background-color: rgba(255, 255, 255, 0.6);
  &:hover {
    box-shadow: 0 8px 32px rgba(59, 130, 246, 0.15);
    border-color: #3b82f6;
  }
  & + .msg-item {
    margin-top: 14px;
  }
}
</style>
