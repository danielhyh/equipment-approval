<template>
  <div class="license-page">
    <!-- 类型 tag 切换 -->
    <div class="license-type">
      <div
        v-for="item in listType"
        :key="item.key"
        :title="item.label"
        class="license-type-item"
        :class="{ active: item.key === paramsValue.type }"
        @click.stop="handleChangeType(item.key)"
      >
        <span>{{ item.label }}</span>
        <i v-if="item.value" class="el-icon-check">{{ item.value }}</i>
      </div>
    </div>

    <!-- 列表 -->
    <el-table class="table_style" :data="licenseListData" size="small" :loading="loading" style="width: 100%">
      <!-- 序号 -->
      <el-table-column type="index" label="序号" :index="indexFn" width="50" align="center" fixed="left" />
      <!-- 许可证编号	证书类型	许可设备名称	阶梯配置机型	设备配置地址	发证日期	配置状态	生产企业	具体型号	装机日期	操作 -->
      <el-table-column prop="licenseNo" label="许可证编号" align="center" fixed="left" />
      <el-table-column prop="appType" label="证书类型" align="center">
        <template #default="{ row }">
          {{ applyTypeDict.find((item) => item.value === row.appType + "")?.label || "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="licenseDeviceName" label="许可设备名称" align="center" />
      <el-table-column prop="ladderConfigModel" label="阶梯配置机型" align="center" />
      <el-table-column prop="equipmentConfigAddress" label="设备配置地址" align="center" />
      <el-table-column prop="issueDate" label="发证日期" align="center" />
      <!-- <el-table-column prop="configStatus" label="配置状态" align="center" /> -->
      <el-table-column prop="productionEnterprise" label="生产企业" align="center" />
      <el-table-column prop="specificModel" label="具体型号" align="center" />
      <el-table-column prop="installationDate" label="装机日期" align="center" />
      <el-table-column label="操作" align="center" width="230" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" v-if="row.hasDuplicate === 'N'" @click="handleCopy(row)">副本提交</el-button>
          <el-button
            type="success"
            size="small"
            v-if="row.hasDuplicate === 'Y' && row.hasAcceptanceMaterial === 'N'"
            @click="handleFile(row)"
          >
            验收资料提交
          </el-button>
          <el-button type="warning" size="small" v-if="row.hasDuplicate === 'Y'" @click="handleOther(row)"
            >补充其他信息</el-button
          >
          <el-button type="primary" size="small" v-if="row.hasDuplicate === 'Y'" @click="handleDetail(row)">详细信息</el-button>
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
      @changePageOrPageSize="getLicenseListFn"
    />
    <!-- 补充其他信息弹窗 -->
    <Dialog v-model:visible="otherVisible">
      <template #header>
        <svg-icon name="zondicons:add-solid" size="18" class="m-r-5" color="#165DFF" />
        <span class="f-w-700 c-333">补充其他信息</span>
      </template>
      <otherDialog ref="otherDialogRef" v-model:loading="otherloading" />
      <template #footer>
        <div class="flex items-center justify-center">
          <el-button type="info" :disabled="otherloading" @click="otherVisible = false">取消</el-button>
          <el-button type="primary" :loading="otherloading" @click="handleOtherSubmit">提交</el-button>
        </div>
      </template>
    </Dialog>
  </div>
</template>

<script setup name="LicenseList">
import { getLicenseList } from "@/apis/home";
import { useDictStore } from "@/pinia/modules/dict";
import { useBasisStore } from "@/pinia/modules/basis";
import otherDialog from "./licensePage/otherDialog.vue";
const router = useRouter();
const dictStore = useDictStore();
let paramsValue = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  type: "all",
});
let licenseListData = ref([]);
let loading = ref(false);
let listType = reactive([
  { label: "全部证书", value: 0, key: "all" },
  { label: "正电子发射型磁共振成像系统", value: 0, key: "1" },
  { label: "X线正电子发射断层扫描仪", value: 0, key: "2" },
  { label: "腹腔内窥镜手术系统", value: 0, key: "3" },
  { label: "常规放射治疗类设备", value: 0, key: "4" },
  { label: "首次配置的大型医疗器械", value: 0, key: "5" },
]);
// 设备类型
let equipmentTypeDict = computed(() => dictStore.getDictTypeList("biz_main_equipment_type"));
watch(
  () => equipmentTypeDict.value,
  (v) => {
    if (v.length === 0) return;
    listType = v.map((item) => {
      return {
        label: item.label,
        value: 0,
        key: item.value,
      };
    });
    listType.unshift({ label: "全部证书", value: 0, key: "all" });
  },
  { immediate: true }
);

let applyTypeDict = computed(() => dictStore.getDictTypeList("biz_app_type"));
// 自定义序号
const indexFn = (index) => {
  // pageSize pageNum
  return (paramsValue.pageNum - 1) * paramsValue.pageSize + index + 1;
};
const getLicenseListFn = async () => {
  loading.value = true;
  let params = { pageSize: paramsValue.pageSize, pageNum: paramsValue.pageNum, type: paramsValue.type };
  if (params.type === "all") {
    delete params.type;
  }
  getLicenseList(params)
    .then((res) => {
      let {
        data: { list, total },
      } = res;
      licenseListData.value = list || [];
      paramsValue.total = total;
    })
    .catch((err) => {
      licenseListData.value = [];
      paramsValue.total = licenseListData.value.length;
    })
    .finally(() => {
      loading.value = false;
    });
};
const handleChangeType = (v) => {
  paramsValue.type = v;
  paramsValue.pageNum = 1;
  getLicenseListFn();
};

const basisStore = useBasisStore();
// 副本提交
const handleCopy = (row) => {
  basisStore.setLicenseBasis(row);
  router.push({ name: "LicenseCopy", query: { key: "copyMsg" } });
};
// 其他信息补充
let otherVisible = ref(false);
let otherloading = ref(false);
let otherDialogRef = ref(null);
const handleOther = (row) => {
  basisStore.setLicenseBasis(row);
  otherVisible.value = true;
};
const handleOtherSubmit = async () => {
  let isPass = await otherDialogRef.value?.submit()
  if(isPass){
    otherVisible.value = false;
  }
};
// 验收资料提交
const handleFile = (row) => {
  basisStore.setLicenseBasis(row);
  router.push({ name: "LicenseFile", query: { key: "fileMsg" } });
};
// 详细信息
const handleDetail = (row) => {
  basisStore.setLicenseBasis(row);
  router.push({ name: "LicenseDetail" });
};

onMounted(() => {
  getLicenseListFn();
});
</script>

<style lang="scss" scoped>
.license-page {
  .license-type {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
    gap: 10px;
    margin-bottom: 20px;
    padding: 10px;
    border-radius: 8px;
    box-shadow: 0 0px 4px rgba(0, 0, 0, 0.05), inset 0 0 2px rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
    .license-type-item {
      text-align: center;
      padding: 8px 14px;
      border-radius: 4px;
      color: #333;
      font-size: 12px;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.3s ease;
      position: relative;
      &:hover,
      &.active {
        background-color: #1b7cf3;
        color: #fff;
      }
      span {
        display: block;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      i {
        display: block;
        min-width: 20px;
        height: 20px;
        line-height: 16px;
        padding: 2px 8px;
        font-size: 12px;
        color: #fff;
        border-radius: 10px;
        background: #fe9e0e;
        position: absolute;
        top: 0%;
        right: -5px;
        transform: translateY(-50%);
        z-index: 2;
        box-shadow: 0 0 6px rgba(0, 0, 0, 0.3);
      }
    }
  }
  &:deep(.el-table) {
    margin-bottom: 20px;
  }
}
</style>
