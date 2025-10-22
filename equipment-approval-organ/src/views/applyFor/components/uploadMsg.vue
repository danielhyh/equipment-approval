<template>
  <div>
    <h3 class="c-165DFF m-b-15 f-w-700">{{ headTitle }}</h3>
    <div class="p-10 b-solid-1-0891b2 b-r-10 m-b-10">
      <div class="flex items-center c-0891b2 l-h-1_5 m-b-10">
        <svg-icon name="flowbite:lightbulb-solid" size="24" color="#0891b2" />
        <span class="f-w-700">温馨提示</span>
      </div>
      <p class="l-h-1_5 f-s-14 t-indent-8px c-475569">
        <em class="c-dc2626">*</em> 为必要材料,您必须提交才能申报,△ 为容缺后补材料,您可以在网上预览理后在窗口提交,○
        为非必要材料,根据您实际情况提交
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
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="{ row, $index }">
          <!-- 审批状态 >= 3 时显示预览和下载按钮 -->
          <template v-if="appStatus >= 3 && row.filePath">
            <el-button type="primary" size="small" @click="viewFn(row)" class="m-r-5">
              <el-icon><View /></el-icon>
              <span>预览</span>
            </el-button>
            <el-button type="success" size="small" @click="downLoadFn(row)">
              <el-icon><Download /></el-icon>
              <span>下载</span>
            </el-button>
          </template>
          <!-- 审批状态 < 3 时显示上传按钮 -->
          <template v-else>
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
            >
              <el-button type="primary" size="small">
                <el-icon><Upload /></el-icon>
                <span>{{ row.fileName ? "重新上传" : "上传" }}</span>
              </el-button>
            </el-upload>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <!-- 预览对话框 -->
    <el-dialog
        v-model="previewVisible"
        :title="previewTitle"
        :width="previewWidth"
        :fullscreen="isFullscreen"
        destroy-on-close
    >
      <div class="preview-container" v-loading="previewLoading">
        <!-- 图片预览 -->
        <div v-if="previewType === 'image'" class="image-preview">
          <el-image :src="previewUrl" fit="contain" :preview-src-list="[previewUrl]" />
        </div>

        <!-- PDF预览 -->
        <div v-else-if="previewType === 'pdf'" class="pdf-preview">
          <iframe :src="previewUrl" frameborder="0"></iframe>
        </div>

        <!-- 不支持的格式 -->
        <div v-else class="unsupported-preview">
          <el-result icon="warning" title="暂不支持该格式的在线预览" sub-title="请下载后查看">
            <template #extra>
              <el-button type="primary" @click="downLoadFn(currentFile)">立即下载</el-button>
            </template>
          </el-result>
        </div>
      </div>

      <template #footer>
        <el-button @click="previewVisible = false">关闭</el-button>
        <el-button type="primary" @click="toggleFullscreen">
          {{ isFullscreen ? '退出全屏' : '全屏显示' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import applyForMsg from "../index";
import { createUploadFile, createApplyMaterial, getApplyMaterial } from "@/apis/applyFor";
import { genFileId } from "element-plus";
import { Upload, Document, View, Download } from "@element-plus/icons-vue";
import { nextTick } from "vue";

let loading = ref(false);
let formAllData = inject("formAllData");
let disabled = inject("disabled", () => false);
let route = useRoute();
let type = route.query.type;
let deptMsg = inject("deptMsg");
let headTitle = inject("headTitle");
let entity = computed(() => applyForMsg[type]);
let dept = deptMsg.cssClass;

// 获取审批状态
let appStatus = computed(() => formAllData.value?.appStatus || 0);

// 收取材料列表
let material = computed(() => entity.value.material.dept[dept].list);

let materialTable = ref([]);

// 标记是否已经提交过
let hasSubmitted = ref(false);

// 预览相关状态
const previewVisible = ref(false);
const previewLoading = ref(false);
const previewType = ref('');
const previewUrl = ref('');
const previewTitle = ref('');
const previewWidth = ref('80%');
const isFullscreen = ref(false);
const currentFile = ref(null);

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
      hasNewUpload: false,
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
        item.hasNewUpload = false;
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
  const maxSize = 1024 * 1024 * 10; // 10MB
  if (file.size > maxSize) {
    ElMessage.error("文件大小不能超过10MB");
    return false;
  }
  const validTypes = row.accept.split(",");
  const fileSuffix = file.name.slice(file.name.lastIndexOf("."));
  if (!validTypes.includes(fileSuffix)) {
    ElMessage.error(`文件类型必须是${validTypes.join("、")}`);
    return false;
  }

  row.fileName = file.name;
  row.fileSize = file.size;
  row.hasNewUpload = true;
  hasSubmitted.value = false;

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
  row.hasNewUpload = false;
};

const handleSuccess = (response, index, row) => {
  row.filePath = response.data;
  row.hasNewUpload = true;
  hasSubmitted.value = false;
};

// 自定义上传
const fileRequestFn = async (file) => {
  const { file: uploadFile, onSuccess, onError } = file;
  try {
    const formData = new FormData();
    formData.append("file", uploadFile);
    let response = await createUploadFile(formData);
    ElMessage.success(`文件${uploadFile.name}上传成功`);
    return response;
  } catch (error) {
    onError(error);
    ElMessage.error(`文件${uploadFile.name}上传失败: ${error.msg || "未知错误"}`);
  }
};

// 获取文件类型
const getFileType = (filename) => {
  if (!filename) return '';
  const lastDotIndex = filename.lastIndexOf('.');
  if (lastDotIndex === -1 || lastDotIndex === filename.length - 1) {
    return '';
  }
  return filename.substring(lastDotIndex + 1).toLowerCase();
};

// 切换全屏
const toggleFullscreen = () => {
  isFullscreen.value = !isFullscreen.value;
};

// 预览文件
const viewFn = async (item) => {
  console.log('preview file', item);
  currentFile.value = item;
  previewTitle.value = item.fileName || '文件预览';
  previewVisible.value = true;
  previewLoading.value = true;
  previewType.value = '';

  try {
    const fileType = getFileType(item.fileName);

    // 图片预览
    if (['png', 'jpg', 'jpeg', 'gif', 'bmp', 'webp'].includes(fileType)) {
      previewType.value = 'image';
      previewUrl.value = item.filePath || '';
      previewWidth.value = '60%';
      previewLoading.value = false;
    }
    // PDF预览
    else if (fileType === 'pdf') {
      previewType.value = 'pdf';
      previewUrl.value = item.filePath || '';
      previewWidth.value = '90%';
      previewLoading.value = false;
    }
    // Word和Excel不支持在线预览
    else if (['docx', 'doc', 'xlsx', 'xls'].includes(fileType)) {
      previewType.value = 'unsupported';
      previewWidth.value = '50%';
      previewLoading.value = false;
    }
    // 其他不支持的格式
    else {
      previewType.value = 'unsupported';
      previewWidth.value = '50%';
      previewLoading.value = false;
    }
  } catch (error) {
    console.error('预览失败:', error);
    ElMessage.error('预览失败,请稍后重试');
    previewLoading.value = false;
    previewType.value = 'unsupported';
  }
};

// 下载文件
const downLoadFn = (item) => {
  if (item?.filePath) {
    window.open(item.filePath, '_blank');
  }
};

// 校验
const validor = async () => {
  const requiredFields = materialTable.value.filter((item) => item.required === true);
  const unUploadedRequired = requiredFields.filter((item) => !item.filePath);
  if (unUploadedRequired.length > 0) {
    ElMessage.error(`请上传必要材料:${unUploadedRequired.map((item) => item.text).join("、")}`);
    return false;
  }
  return true;
};

// 提交
const submit = async () => {
  const isValid = await validor();
  if (!isValid) return false;

  if (hasSubmitted.value && !materialTable.value.some(item => item.hasNewUpload)) {
    ElMessage.success("资料已提交");
    return {};
  }

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

    hasSubmitted.value = true;
    materialTable.value.forEach(item => {
      item.hasNewUpload = false;
    });

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
.m-r-5 {
  margin-right: 5px;
}
.upload-btn {
  display: inline-block;
}

.preview-container {
  max-height: 70vh;
  min-height: 400px;
  overflow: auto;

  .image-preview {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 400px;

    :deep(.el-image) {
      max-width: 100%;
      max-height: 70vh;
    }
  }

  .pdf-preview {
    width: 100%;
    height: 70vh;

    iframe {
      width: 100%;
      height: 100%;
    }
  }

  .unsupported-preview {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 400px;
  }
}
</style>