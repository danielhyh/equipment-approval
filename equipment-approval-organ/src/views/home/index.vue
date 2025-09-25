<template>
  <div class="home-container">
    <Card>
      <template #header>
        <svg-icon name="bxs:file" size="24" style="margin-right: 2px" color="#237efd" />
        <span>证件办理</span>
      </template>
      <div class="card-container">
        <!-- 通过v-for循环渲染卡片 -->
        <div v-for="card in cardList" :key="card.id" min-height="200px" :class="['card-item']">
          <div class="card-header">
            <div :class="['card-icon', `card-icon-${card.iconType}`]">
              <svg-icon :name="card.iconName" size="24" color="#fff" />
            </div>
            <h3 class="card-title">{{ card.title }}</h3>
          </div>
          <p class="card-description">
            {{ card.description }}
          </p>
          <div class="card-actions">
            <button class="btn-primary" @click="handleOnlineApply(card)">
              <svg-icon name="svg-icon:print" size="16" class="mr-1" />
              在线办理
            </button>
            <button class="btn-secondary" @click="handleGuide(card)">
              <svg-icon name="svg-icon:book" size="16" class="mr-1" />
              办事指南
            </button>
          </div>
        </div>
      </div>
      <!-- 办理列表 -->
      <div class="todo-list" v-if="toDoList.length">
        <h3>办理列表</h3>
        <el-table class="table_style m-b-20" :data="toDoList" style="width: 100%" size="small">
          <!-- 序号列 -->
          <el-table-column type="index" label="序号" width="50" fixed="left" align="center" />
          <!-- 申请编号 -->
          <el-table-column prop="appNo" label="申请编号" width="180" align="center">
            <template #default="scope">
              {{ scope.row.appNo || "-" }}
            </template>
          </el-table-column>
          <!-- 申请类型 -->
          <el-table-column prop="appType" label="申请类型" align="center" fixed="left">
            <template #default="scope">
              {{ applyTypeDict.find((item) => item.value === scope.row.appType + "")?.label || "-" }}
            </template>
          </el-table-column>

          <!-- 许可设备名称 -->
          <el-table-column prop="licenseDeviceName" label="许可设备名称" align="center" />
          <!-- 阶梯配置机型 -->
          <el-table-column prop="ladderConfigModel" label="阶梯配置机型" width="180" align="center" />
          <!-- 创建日期 -->
          <el-table-column prop="createTime" label="创建日期" width="180" align="center" />
          <!-- 进度状态 -->
          <el-table-column prop="appStatus" label="进度状态" align="center">
            <template #default="scope">
              {{ statusDict.find((item) => item.value === scope.row.appStatus + "")?.label || "-" }}
            </template>
          </el-table-column>

          <el-table-column prop="guideProcess" label="操作" width="180" fixed="right" align="center">
            <template #default="scope">
              <el-button type="primary" size="small" @click.stop="handleDetail(scope.row)">详情</el-button>
              <el-button type="warning" size="small" v-if="scope.row.appStatus === 1" @click.stop="handleEdit(scope.row)">
                修改
              </el-button>
            </template>
          </el-table-column>
          <template #empty>
            <el-empty description="暂无数据" :image-size="80"></el-empty>
          </template>
        </el-table>
        <Pagination
          :total="paramsValue.total"
          v-model:pageNum="paramsValue.pageNum"
          v-model:pageSize="paramsValue.pageSize"
          :background="true"
          @changePageOrPageSize="getToDoListFn"
        />
      </div>
    </Card>

    <!-- 证书列表 -->
    <Card style="margin-top: 20px">
      <template #header>
        <svg-icon name="fa7-solid:certificate" size="24" style="margin-right: 2px" color="#237efd" />
        <span>证书列表</span>
      </template>
      <LicenseList />
    </Card>
  </div>
</template>

<script setup>
import LicenseList from "./licenseList.vue";
import { useBasisStore } from "@/pinia/modules/basis";
import { useDictStore } from "@/pinia/modules/dict";
import { getApplyList } from "@/apis/home";
import { formatDate } from "@/utils/tools";
import { getApplyReviewedList } from "@/apis/applyFor";

const dictStore = useDictStore();
const basisStore = useBasisStore();

const router = useRouter();
// 创建卡片数据数组
const cardList = computed(() => basisStore.getModelList);
// 处理按钮点击事件的函数
const handleOnlineApply = async (card) => {
  if (card.id === "issue") {
    router.push({
      path: "/deputy/apply-for",
      query: { type: card.id },
    });
    return;
  }
  const { data } = await getApplyReviewedList();
  if (data && data?.length) {
    router.push({
      path: "/deputy/apply-for",
      query: { type: card.id },
    });
    return;
  }
  ElMessage.error("您暂无可操作数据");
};

const handleGuide = (card) => {
  // 实际应用中可能需要打开对应的指南文档
  if (card.id) {
    router.push({
      path: "/deputy/guide",
      query: { type: card.id },
    });
  }
};

// 代办列表
const toDoList = ref([]);
let paramsValue = reactive({
  total: 0,
  pageNum: 1,
  pageSize: 10,
});
// 申请类型
const applyTypeDict = dictStore.getDictTypeList("biz_app_type");
// 申请状态
const statusDict = dictStore.getDictTypeList("biz_app_status");
const getToDoListFn = () => {
  let params = {
    pageSize: paramsValue.pageSize,
    pageNo: paramsValue.pageNum,
  };
  getApplyList(params)
    .then((res) => {
      let {
        data: { list, total },
      } = res;
      paramsValue.total = total;
      toDoList.value = (list || []).map((item) => {
        return {
          ...item,
          createTime: formatDate(item.createTime),
        };
      });
    })
    .catch((err) => {
      toDoList.value = [];
    });
};
// 办理列表 详情
const handleDetail = (row) => {
  let { appNo, appType, id, appStatus, createTime, licenseDeviceName, ladderConfigModel } = row;
  let applyType = null;
  switch (appType) {
    case 1:
      applyType = "issue";
      break;
    case 2:
      applyType = "reissue";
      break;
    case 3:
      applyType = "change";
      break;
    case 4:
      applyType = "basis";
      break;
    default:
      applyType = null;
  }
  if (!applyType) {
    ElMessage.error("申请类型不存在");
    return;
  }
  router.push({
    path: `/deputy/apply-for`,
    query: { id, type: applyType, handle: "detail" },
  });
};
// 办理列表 编辑
const handleEdit = (row) => {
  let { appNo, appType, id, appStatus, createTime, licenseDeviceName, ladderConfigModel } = row;
  let applyType = null;
  switch (appType) {
    case 1:
      applyType = "issue";
      break;
    case 2:
      applyType = "reissue";
      break;
    case 3:
      applyType = "change";
      break;
    case 4:
      applyType = "basis";
      break;
    default:
      applyType = null;
  }
  if (!applyType) {
    ElMessage.error("申请类型不存在");
    return;
  }
  router.push({
    path: `/deputy/apply-for`,
    query: { id, type: applyType },
  });
};
// 查询
onMounted(() => {
  getToDoListFn();
});
</script>

<style lang="scss" scoped>
.home-container {
  .card-container {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 20px;
    margin-bottom: 32px;
  }
  .card-item {
    background: white;
    border-radius: 8px;
    padding: 24px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
    border: 1px solid #e5e7eb;
    transition: all 0.3s ease;

    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      border: 1px solid #3b82f6;
      transform: translateY(-2px);
    }
    .card-header {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      margin-bottom: 16px;

      .card-icon {
        width: 48px;
        height: 48px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white !important;
        margin-bottom: 10px;
        &.card-icon-green {
          background-color: #10b981;
        }

        &.card-icon-orange {
          background-color: #f59e0b;
        }

        &.card-icon-blue {
          background-color: #3b82f6;
        }
      }

      .card-title {
        font-size: 18px;
        font-weight: 600;
        color: #1f2937;
        margin: 0;
      }
    }

    .card-description {
      font-size: 14px;
      color: #4b5563;
      line-height: 1.5;
      margin-bottom: 20px;
    }

    .card-actions {
      display: flex;
      gap: 12px;
      justify-content: center;
      .btn-primary {
        background-color: #3b82f6;
        color: white;
        border: none;
        padding: 8px 16px;
        border-radius: 4px;
        cursor: pointer;
        font-size: 14px;
        display: flex;
        align-items: center;
        transition: background-color 0.3s ease;

        &:hover {
          background-color: #2563eb;
        }
      }

      .btn-secondary {
        background-color: #f3f4f6;
        color: #4b5563;
        border: 1px solid #e5e7eb;
        padding: 8px 16px;
        border-radius: 4px;
        cursor: pointer;
        font-size: 14px;
        display: flex;
        align-items: center;
        transition: all 0.3s ease;

        &:hover {
          background-color: #e5e7eb;
        }
      }
    }
  }
  .todo-list {
    > h3 {
      font-weight: bold;
      font-size: 18px;
      color: #2563eb;
      margin-bottom: 14px;
    }
  }
}
</style>
