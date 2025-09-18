<template>
  <div v-loading="loading">
    <!-- 设备使用人员 -->
    <div class="header-title m-t-b-10">
      <svg-icon name="typcn:group" size="20" class="m-r-5" />
      <span>设备使用人员</span>
    </div>
    <el-table class="table_style" style="width: 100%" :data="formData.equipmentUsers">
      <el-table-column label="身份证号码" prop="IdCard" align="center" />
      <el-table-column label="姓名" prop="name" align="center" />
      <!-- 性别 -->
      <el-table-column label="性别" prop="gender" align="center">
        <template #default="{ row }">
          {{ row.gender === "1" ? "男" : "女" }}
        </template>
      </el-table-column>
      <!-- 出生日期 -->
      <el-table-column label="出生日期" prop="birthDate" align="center" />
      <!-- 职称 -->
      <el-table-column label="职称" prop="title" align="center" />
      <!-- 联系电话 -->
      <el-table-column label="联系电话" prop="phoneNumber" align="center" />
    </el-table>
    <!-- 正本悬挂位置 -->
    <div class="header-title m-t-b-10">
      <svg-icon name="icon-park-solid:local-two" size="20" class="m-r-5" />
      <span>正本悬挂位置</span>
    </div>
    <!-- 正本悬挂位置 上传 -->
    <div class="upload-content-box">
      <!-- 正本悬挂位置 回显 -->
      <el-image
        v-if="handlePreviewList[0]"
        style="width: 70%; margin: 0 auto; display: block"
        :src="handlePreviewList[0]"
        fit="fill"
        preview-teleported
        :preview-src-list="handlePreviewList"
      />
      <el-empty v-else :image-size="80"></el-empty>
    </div>
    <!-- 设备使用情况 -->
    <div class="header-title m-t-b-10">
      <svg-icon name="fa-solid:chart-line" size="20" class="m-r-5" />
      <span>设备使用情况</span>
    </div>
    <div>
      <template v-if="formData.deviceMsgList.length">
        <div v-for="item in formData.deviceMsgList" :key="item.createTime" class="msg-item">
          <div class="time">{{ item.createTime }}</div>
          <div class="remark">{{ item.remark }}</div>
        </div>
      </template>
      <el-empty v-else :image-size="80"></el-empty>
    </div>
    <!-- 检查保养情况 -->
    <div class="header-title m-t-b-10">
      <svg-icon name="fa-solid:tools" size="20" class="m-r-5" />
      <span>检查保养情况</span>
    </div>
    <div>
      <template v-if="formData.checkMaintain.length">
        <div></div>
      </template>
      <el-empty v-else :image-size="80"></el-empty>
    </div>
  </div>
</template>

<script setup>
import { formatDate } from "@/utils/tools";
import { getLicenseOtherList } from "@/apis/home";
import { useBasisStore } from "@/pinia/modules/basis";
const basisStore = useBasisStore();
let licenseBasis = computed(() => basisStore.getLicenseBasis);
let applicationId = computed(() => licenseBasis.value.applicationId);
let loading = ref(false);
let formData = reactive({
  equipmentUsers: [], // 设备使用人员 JSON格式
  hangingLocation: "", // 正本悬挂位置
  deviceMsgList: [],
  checkMaintain: [],
});
// 图片回显
const handlePreviewList = computed(() => {
  if (formData.hangingLocation) {
    return [formData.hangingLocation];
  }
  return [];
});

// 1-正本悬挂位置，2-设备使用情况，3-检查保养记录，4-使用人员变更  infoType int
const init = async () => {
  if (!applicationId.value) return;
  try {
    const response = await getLicenseOtherList({ applicationId: applicationId.value });
    let originaImageList = response.data
      .filter((item) => item.infoType === 1)
      .sort((pre, cur) => cur.createTime - pre.createTime);
    formData.hangingLocation = originaImageList[0]?.infoContent || "";

    let personList = response.data.filter((item) => item.infoType === 4).sort((pre, cur) => cur.createTime - pre.createTime);
    formData.equipmentUsers = personList[0].infoContent ? JSON.parse(personList[0].infoContent) : [];

    let checkMaintainList = response.data
      .filter((item) => item.infoType === 3)
      .sort((pre, cur) => cur.createTime - pre.createTime);
    formData.checkMaintain = checkMaintainList.map((item) => ({
      createTime: formatDate(item.createTime),
      remark: item.infoContent,
    }));

    let deviceMsgListArr = response.data
      .filter((item) => item.infoType === 2)
      .sort((pre, cur) => cur.createTime - pre.createTime);
    formData.deviceMsgList = deviceMsgListArr.map((item) => ({
      createTime: formatDate(item.createTime),
      remark: item.infoContent,
    }));
  } catch (err) {
    console.log(err);
  }
};
onMounted(() => {
  init();
});
</script>

<style lang="scss" scoped>
.grid-form {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px 15px;
  &:deep(.el-form-item) {
    margin: 0;
    // --el-disabled-text-color: #171717;
    .el-form-item__label {
      font-size: 14px;
      color: #666;
      font-weight: 700;
    }
    .el-select {
      width: 100%;
      .el-select__wrapper {
        line-height: 32px;
        min-height: 40px;
      }
    }
    .el-input {
      --el-input-height: 40px;
    }
  }
  .grid-row {
    grid-column: 1 / 5;
  }
  .grid-row-2-4 {
    grid-column: 2 / 5;
  }
}
.grid-form-2 {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px 15px;
  &:deep(.el-form-item) {
    margin: 0;
    --el-disabled-text-color: #171717;
    .el-form-item__label {
      font-size: 14px;
      color: #666;
      font-weight: 700;
    }
    .el-select {
      width: 100%;
      .el-select__wrapper {
        line-height: 32px;
        min-height: 40px;
      }
    }
    .el-input {
      --el-input-height: 40px;
    }
    .el-date-editor {
      width: 100%;
    }
  }
}
.header-title {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 700;
  color: #165dff;
  padding: 5px;
  border-bottom: 2px solid rgba(22, 93, 255, 0.1);
}
.msg-item {
  border-radius: 10px;
  padding: 14px 20px;
  
}
</style>
