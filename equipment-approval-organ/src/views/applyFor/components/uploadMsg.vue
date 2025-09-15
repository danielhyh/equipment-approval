<template>
  <div>
    <h3 class="c-165DFF m-b-15 f-w-700">{{ headTitle }}</h3>
    <div class="p-10 b-solid-1-0891b2 b-r-10 m-b-10">
      <div class="flex items-center c-0891b2 l-h-1_5 m-b-10">
        <svg-icon name="flowbite:lightbulb-solid" size="24" color="#0891b2" />
        <span class="f-w-700">温馨提示</span>
      </div>
      <p class="l-h-1_5 f-s-14 t-indent-8px c-475569">
        <em class="c-dc2626">*</em> 为必要材料，您必须提交才能申报，△ 为容缺后补材料，您可以在网上预受理后在窗口提交，○
        为非必要材料，根据您实际情况提交
      </p>
    </div>

    <h4 class="c-165DFF m-b-15">申报材料</h4>

    <el-table :data="materialTable" style="width: 100%" class="table_style" v-loading="loading">
      <!-- 序号 -->
      <el-table-column type="index" width="60" label="序号" align="center" fixed="left">
        <template #default="{ $index }">
          {{ $index + 1 }}
        </template>
      </el-table-column>

      <!-- 材料名称 -->
      <el-table-column prop="text" label="材料名称" min-width="300" align="left" fixed="left">
        <template #default="{ row }">
          <div class="flex items-center">
            <span>{{ row.text }}</span>
            <span v-if="row.required" class="c-dc2626">*</span>
            <span v-else-if="row.required === 2" class="c-2563eb m-r-5">△</span>
            <span v-else-if="row.required === 3" class="c-16a34a m-r-5">○</span>
          </div>
        </template>
      </el-table-column>

      <!-- 文件名称 -->
      <el-table-column prop="fileName" label="文件名称" min-width="300" align="center">
        <template #default="{ row }">
          <div v-if="row.fileName" class="flex items-center justify-content-center">
            <el-icon :size="16" class="m-r-5 c-0891b2"><Document /></el-icon>
            <span>{{ row.fileName }}</span>
          </div>
          <span v-else class="c-9ca3af">未上传</span>
        </template>
      </el-table-column>

      <!-- 操作 -->
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template #default="{ row, $index }">
          <el-upload
            :ref="(el) => setUploadRef(el, $index)"
            class="upload-btn"
            :show-file-list="false"
            :before-upload="(file) => handleUpload(file, row)"
            :limit="1"
            :accept="row.accept || '*'"
            :http-request="fileRequestFn"
            :on-exceed="(file) => handleExceed(file, $index, row)"
            :on-error="(file) => handleError(file, $index, row)"
            :on-success="(file) => handleSuccess(file, $index, row)"
            :disabled="disabled"
          >
            <el-button type="primary" size="small" :disabled="disabled">
              <el-icon><Upload /></el-icon>
              <span>{{ row.fileName ? "重新上传" : "上传" }}</span>
            </el-button>
          </el-upload>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import applyForMsg from "../index";
import { createUploadFile, createApplyMaterial, getApplyMaterial } from "@/apis/applyFor";
import { genFileId } from "element-plus";
import { Upload, Delete, Document } from "@element-plus/icons-vue";
import { nextTick } from "vue";
let loading = ref(false);
let formAllData = inject("formAllData");
let disabled = inject("disabled", () => false);
let route = useRoute();
let type = route.query.type;
let deptMsg = inject("deptMsg");
let headTitle = inject("headTitle");
let entity = computed(() => applyForMsg[type]);
let dept = deptMsg.cssClass; // 部门 前端标记 机构性质
// 收取材料列表
let material = computed(() => entity.value.material.dept[dept].list);

let materialTable = ref([]);
const initTable = () => {
  if (!(material.value && Array.isArray(material.value))) {
    ElMessage.warning("基础材料数据获取异常");
    return;
  }
  materialTable.value = material.value.map((item) => {
    return {
      text: item.text,
      required: item.required,
      accept: item.accept,
      materialType: item.fileType,
      fileName: "",
      fileSize: "",
      filePath: null,
    };
  });
  if (formAllData.value?.id) {
    initTableDataFn();
  }
};
const initTableDataFn = async () => {
  try {
    if (!formAllData.value.id) return;
    loading.value = true;
    let response = await getApplyMaterial(formAllData.value.id);
    materialTable.value.forEach((item, index) => {
      let res = response.data.find((i) => i.materialName === item.text);
      if (res) {
        item.fileName = res.materialName;
        item.fileSize = res.fileSize;
        item.filePath = res.filePath;
      }
    });
  } catch (err) {
    console.log(err);
  } finally {
    loading.value = false;
  }
};

// 用于存储所有upload实例的映射
const uploadRefs = ref(new Map());
// 设置upload引用
const setUploadRef = (el, index) => {
  if (el) {
    // 使用行的唯一标识作为key存储实例
    const rowKey = `row_${index}`;
    uploadRefs.value.set(rowKey, el);
  }
};
// 获取指定行的upload实例
const getUploadInstance = (index) => {
  const rowKey = `row_${index}`;
  return uploadRefs.value.get(rowKey);
};
// 重置所有上传组件
const resetAllUploads = async () => {
  await nextTick();
  uploadRefs.value.forEach((instance) => {
    if (instance && instance.clearFiles) {
      instance.clearFiles();
    }
  });
};
// 处理文件上传前置校验
const handleUpload = (file, row) => {
  // 校验文件大小
  const maxSize = 1024 * 1024 * 10; // 10MB
  if (file.size > maxSize) {
    ElMessage.error("文件大小不能超过10MB");
    return false;
  }
  // 校验文件类型
  const validTypes = row.accept.split(",");
  const fileSuffix = file.name.slice(file.name.lastIndexOf("."));
  if (!validTypes.includes(fileSuffix)) {
    ElMessage.error(`文件类型必须是${validTypes.join("、")}`);
    return false;
  }
  // 这里可以添加文件大小、类型等校验逻辑
  row.fileName = file.name;
  row.fileSize = file.size;
  return true;
};
// 覆盖之前文件
const handleExceed = async (files, index, row) => {
  let valid = await handleUpload(files[0], row);
  if (!valid) return;
  const uploadInstance = getUploadInstance(index);
  if (uploadInstance) {
    uploadInstance.clearFiles();
    const file = files[0];
    file.uid = genFileId();
    uploadInstance.handleStart(file);
    uploadInstance.submit();
  }
};

const handleError = (error, index, row) => {
  row.fileName = "";
  row.fileSize = "";
  row.filePath = "";
};
const handleSuccess = (response, index, row) => {
  row.filePath = response.data;
};
// 自定义上传
const fileRequestFn = async (file) => {
  const { file: uploadFile, onSuccess, onError } = file;
  try {
    // 创建FormData对象
    const formData = new FormData();
    formData.append("file", uploadFile);
    let response = await createUploadFile(formData);
    // 调用上传成功回调
    // onSuccess(response);
    ElMessage.success(`文件${uploadFile.name}上传成功`);
    return response;
  } catch (error) {
    // 调用上传失败回调
    onError(error);
    ElMessage.error(`文件${uploadFile.name}上传失败: ${error.msg || "未知错误"}`);
  }
};

// 校验
const validor = async () => {
  // 检查是否所有必填项都已上传
  const requiredFields = materialTable.value.filter((item) => item.required === true);
  const unUploadedRequired = requiredFields.filter((item) => !item.filePath);
  if (unUploadedRequired.length > 0) {
    ElMessage.error(`请上传必要材料：${unUploadedRequired.map((item) => item.text).join("、")}`);
    return false;
  }
  return true;
};
// 提交
const submit = async () => {
  const isValid = await validor();
  if (!isValid) return false;
  let params = materialTable.value.map((item) => ({
    applicationId: formAllData.value?.id || formAllData.value?.appNoId,
    materialType: item.materialType,
    materialName: item.text,
    filePath: item.filePath,
    fileSize: item.fileSize + "",
  }));
  try {
    loading.value = true;
    await createApplyMaterial(params);
    ElMessage.success("提交成功");
    return {};
  } catch (err) {
    ElMessage.error("提交失败");
    return false;
  } finally {
    loading.value = false;
  }
};
defineExpose({
  validor,
  submit,
});
onMounted(() => {
  initTable();
});
</script>

<style lang="scss" scoped>
.flex {
  display: flex;
}
.items-center {
  align-items: center;
}
.justify-content-center {
  justify-content: center;
}
.c-0891b2 {
  color: #0891b2;
}
.b-solid-1-0891b2 {
  border: 1px solid #70d4ed;
}
.t-indent-8px {
  text-indent: 8px;
}
</style>
