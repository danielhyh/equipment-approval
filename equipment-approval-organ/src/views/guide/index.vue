<template>
  <div>
    <div class="page-header">
      <h1 class="page-title flex items-center m-r-5">
        <svg-icon name="f7:doc-text-fill" size="24" color="#165DFF" />
        {{ title }}(申请单位为{{ deptName }})
      </h1>
      <p class="page-subtitle">办事指南详细信息</p>
    </div>
    <Card style="margin-top: 24px">
      <template #header>
        <svg-icon name="fa:info-circle" size="20" color="#165DFF" />
        <span style="margin-left: 8px; font-size: 18px">办事信息</span>
      </template>
      <div class="card-guid-msg">
        <div v-for="(item, index) in processInfo" :key="index">
          <div class="f-s-12 l-h-1_5 m-b-5 c-64748b">{{ item.label }}</div>
          <div class="f-s-14 l-h-1_5 f-w-700 c-333" :class="{ 'c-165dffI': index === 0 || index === processInfo.length - 1 }">
            {{ item.value }}
          </div>
        </div>
        <el-button type="primary" @click="goApplyFor">
          <template #icon>
            <svg-icon name="fluent:book-24-filled" color="#fff" />
          </template>
          在线办理
        </el-button>
      </div>
    </Card>

    <Card style="margin-top: 24px">
      <template #header>
        <svg-icon name="fa-solid:list-alt" size="20" color="#165DFF" />
        <span style="margin-left: 8px; font-size: 18px">基本信息</span>
      </template>
      <el-descriptions class="custom-desriptions" direction="horizontal" :column="1" size="large" label-width="150px" border>
        <el-descriptions-item v-for="(item, index) in basisInfo" :key="index" :label="item.label">
          {{ item.value }}
        </el-descriptions-item>
      </el-descriptions>
      <div class="p-10 b-solid-1-0891b2 b-r-10 bg-7cdaf141">
        <div class="flex items-center c-0891b2 l-h-1_5 m-b-15">
          <svg-icon name="flowbite:lightbulb-solid" size="20" color="#0891b2" />
          <span class="f-w-700 c-0891b2 f-s-14 m-l-r-6">温馨提示</span>
        </div>
        <ul class="ul-style-inside p-l-r-10 f-s-14 l-h-1_5 c-475569">
          <li>本事项支持全程网上办理，无需到现场</li>
          <li>请确保申请材料齐全且符合要求</li>
          <li>如有疑问，请拨打咨询电话或<em class="c-165DFF m-l-r-6 f-w-700 c-pointer" title="029-987654">在线咨询</em></li>
        </ul>
      </div>
    </Card>

    <Card style="margin-top: 24px">
      <template #header>
        <svg-icon name="fa:info-circle" size="20" color="#165DFF" />
        <span style="margin-left: 8px; font-size: 18px">申请材料</span>
      </template>
      <div class="p-10 b-solid-1-0891b2 b-r-10 bg-7cdaf141 m-b-30">
        <div class="flex items-center c-0891b2 l-h-1_5 m-b-15">
          <svg-icon name="flowbite:lightbulb-solid" size="20" color="#0891b2" />
          <span class="f-w-700 c-0891b2 f-s-14 m-l-r-6">温馨提示</span>
        </div>
        <ul class="ul-style-inside p-l-r-10 f-s-14 l-h-1_5 c-475569 m-b-20">
          <li><em class="c-dc2626 m-r-5 f-w-700">红色标记</em>为必要材料，必须提交</li>
          <li>请按照样表要求填写相关信息</li>
          <li>所有材料均支持电子版上传</li>
          <li>建议提前下载并填写相关表格</li>
        </ul>
      </div>
      <el-table class="table_style" :data="material" size="small">
        <!-- 序号 -->
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="text" label="材料名称" />
        <el-table-column prop="type" label="材料类型" width="120" align="center" />
        <el-table-column prop="form" label="材料形式" width="110" align="center" />
        <el-table-column prop="paper" label="纸质材料份数" width="160" align="center" />
        <el-table-column prop="required" label="材料必要性" width="100" align="center">
          <template #default="{ row }">
            <span class="f-w-700" :class="{ 'c-dc2626': row.required === true }">
              {{ row.required === true ? "必要" : "非必要" }}</span
            >
          </template>
        </el-table-column>
        <el-table-column prop="standard" label="受理标准" align="center" width="100" />
        <el-table-column prop="source" label="来源渠道" align="center" width="100" />
        <el-table-column prop="notice" label="填表须知" align="center" width="60" />
        <el-table-column label="样/空表下载" align="center" width="140">
          <template #default="{ row }">
            <el-button v-if="row.empty" type="primary" :icon="Download" size="small" @click="downloadFn(row.empty)">
              下载空表
            </el-button>
            <el-button v-if="row.sample" type="primary" :icon="Download" size="small" @click="downloadFn(row.sample)">
              下载样表
            </el-button>
            <el-button v-if="row.view" type="primary" :icon="View" size="small" @click="openView(row.view)">查看模板</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-10 b-solid-1-0891b2 b-r-10 bg-7cdaf141 m-t-30">
        <div class="flex items-center c-0891b2 l-h-1_5 m-b-5">
          <svg-icon name="bi:exclamation-triangle-fill" size="20" color="#0891b2" />
          <span class="f-w-700 c-0891b2 f-s-14 m-l-r-6">重要提示</span>
        </div>
        <ul class="ul-style-inside p-l-r-10 f-s-14 l-h-1_5 c-475569">
          <li>所有材料均需按要求准备齐全后方可提交申请</li>
          <li>材料不齐全或不符合要求的，将被退回补正</li>
          <li>虚假材料将承担相应法律责任</li>
          <li>如需帮助，请联系咨询电话：<em class="c-165DFF m-l-r-6 f-w-700 c-pointer" title="029-987654">029-987654</em></li>
        </ul>
      </div>
    </Card>
  </div>
</template>

<script setup>
import { Download, View } from "@element-plus/icons-vue";
import { useDictStore } from "@/pinia/modules/dict.js";
import { useUserStore } from "@/pinia/modules/user.js";
import { getApplyReviewedList } from "@/apis/applyFor";
import applyForMsg from "../applyFor/index.js";
const route = useRoute();
const router = useRouter();
const dictStore = useDictStore();
const userStore = useUserStore();

const institutionDict = computed(() => dictStore.getDictTypeList("biz_institution_type"));
const type = route.query.type;
let dept = computed(() => {
  let userInfo = userStore.getUser;
  let deptMsg = institutionDict.value.find((item) => item.value === userInfo?.institutionType);
  return deptMsg || { cssClass: "shby", label: "社会办医", value: "1" };
});
// 部门名称
let deptName = computed(() => applyForMsg.dept[dept.value.cssClass]);
// 标题
const title = computed(() => applyForMsg[type].title);

const entity = computed(() => applyForMsg[type]);
// 办理信息
const processInfo = computed(() => entity.value.processInfo);
// 基本信息
const basisInfo = computed(() => entity.value.basisInfo);
// 申请材料
const material = computed(() => {
  return entity.value.material.dept[dept.value.cssClass].list;
});

const goApplyFor = async () => {
  if (type === "issue") {
    router.replace({
      path: "/deputy/apply-for",
      query: {
        type: type,
      },
    });
    return;
  }
  const { data } = await getApplyReviewedList();
  if (data && data?.length) {
    router.replace({
      path: "/deputy/apply-for",
      query: {
        type: type,
      },
    });
    return;
  }
  ElMessage.error("您暂无可操作数据");
};
// 下载文件
const downloadFn = (url) => {
  window.open(url);
};
// 查看文件
const openView = (url) => {
  window.open(url);
};
</script>

<style lang="scss" scoped>
.page-header {
  padding: 16px 20px;
  background-color: #fff;
  color: #165dff;
  box-shadow: 0 8px 32px rgba(22, 93, 255, 0.08);
  border-left: 4px solid #165dff;
  border-radius: 16px;
  margin-bottom: 24px;
  .page-title {
    font-size: 24px;
    font-weight: bold;
    color: #165dff;
    margin: 0 0 8px 0;
  }

  .page-subtitle {
    font-size: 14px;
    color: #64748b;
    margin: 0;
  }
}
.card-guid-msg {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  padding-right: 150px;
  gap: 10px 20px;
  position: relative;
  .el-button {
    position: absolute;
    right: 0;
    top: 50%;
    transform: translateY(-50%);
    width: 150px;
    height: 40px;
    border-radius: 8px;
    background-image: linear-gradient(135deg, #165dff, #3b82f6);
    box-shadow: 0 4px 16px rgba(22, 93, 255, 0.3);
    font-size: 14px;
    &:hover {
      transform: translateY(calc(-50% - 2px));
      box-shadow: 0 4px 16px rgba(22, 92, 255, 0.768);
    }
  }
}
.custom-desriptions {
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 0 1px 1px rgba(0, 0, 0, 0.06);
  margin-bottom: 20px;
  &:deep(tr) {
    transition: all 0.3s ease-in-out;
    &:hover {
      transform: translateX(5px);
      background-color: #f5f5f5;
    }
  }
}
.b-solid-1-0891b2 {
  border: 1px solid #70d4ed;
}
.bg-7cdaf141 {
  background-color: #7cdaf141;
}
.t-indent-8px {
  text-indent: 8px;
}
.table_style {
  &:deep(.el-table__cell) {
    .el-button {
      margin: 0;
      & + .el-button {
        margin-top: 5px;
      }
    }
  }
}
</style>
