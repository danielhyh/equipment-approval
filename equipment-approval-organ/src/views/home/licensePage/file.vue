<template>
  <div>
    <div class="p-10 b-solid-1-0891b2 b-r-10 m-b-20">
      <div class="flex items-center c-0891b2 l-h-1_5 m-b-10">
        <svg-icon name="flowbite:lightbulb-solid" size="18" color="#0891b2" />
        <span class="f-w-700 f-s-14">温馨提示</span>
      </div>
      <p class="l-h-1_5 f-s-12 t-indent-8px c-475569">1.带*号为必上传材料，请根据实际情况进行上传</p>
      <p class="l-h-1_5 f-s-12 t-indent-8px c-475569">2.支持 PDF、DOC、DOCX、JPG、PNG、ZIP、RAR 等格式</p>
      <p class="l-h-1_5 f-s-12 t-indent-8px c-475569">3.单个文件大小不超过 20MB</p>
      <p class="l-h-1_5 f-s-12 t-indent-8px c-475569">4.部分材料提供模板下载，请按模板要求填写后上传</p>
    </div>
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
          <div v-if="row.fileName" class="flex items-center justify-center">
            <el-icon :size="16" class="m-r-5 c-0891b2"><Document /></el-icon>
            <span>{{ row.fileName }}</span>
          </div>
          <span v-else class="c-9ca3af">未上传</span>
        </template>
      </el-table-column>

      <!-- 操作 -->
      <el-table-column label="操作" width="220" align="center" fixed="right">
        <template #default="{ row, $index }">
          <!-- 已上传文件显示预览和下载按钮 -->
          <template v-if="row.filePath">
            <el-button type="primary" size="small" @click="previewFile(row)" class="m-r-5">
              <el-icon><View /></el-icon>
              <span>预览</span>
            </el-button>
            <el-button type="success" size="small" @click="downloadFile(row)" class="m-r-5">
              <el-icon><Download /></el-icon>
              <span>下载</span>
            </el-button>
            <!-- 未禁用时显示删除按钮 -->
            <el-button
                v-if="!isDisabled"
                type="danger"
                size="small"
                @click="deleteFile(row)"
            >
              <el-icon><Delete /></el-icon>
              <span>删除</span>
            </el-button>
          </template>
          <!-- 未上传文件显示上传按钮 -->
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
                :disabled="isDisabled"
            >
              <el-button type="primary" size="small" :disabled="isDisabled">
                <el-icon><Upload /></el-icon>
                <span>上传</span>
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
              <el-button type="primary" @click="downloadFile(currentFile)">立即下载</el-button>
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
import { createUploadFile } from "@/apis/applyFor";
import { createFilesMaterial, getFilesMaterialList } from "@/apis/home";
import { genFileId } from "element-plus";
import { Upload, Document, View, Download, Delete } from "@element-plus/icons-vue";
import { useBasisStore } from "@/pinia/modules/basis";
import { onMounted, nextTick } from "vue";

const basisStore = useBasisStore();
let licenseBasis = computed(() => basisStore.getLicenseBasis);

let props = defineProps({
  disabled: {
    type: Boolean,
    default: false,
  },
});

let isDisabled = computed(() => props.disabled);
let loading = ref(false);

// 预览相关状态
let previewVisible = ref(false);
let previewLoading = ref(false);
let previewType = ref('');
let previewUrl = ref('');
let previewTitle = ref('');
let previewWidth = ref('80%');
let isFullscreen = ref(false);
let currentFile = ref(null);

let materialTable = ref([
  {
    text: "采购合同 ",
    required: true,
    accept: ".pdf,.doc,.docx,.jpg,.png",
    materialType: 1,
    fileName: "",
    fileSize: "",
    filePath: null,
  },
  {
    text: "中标通知书",
    required: true,
    accept: ".pdf,.doc,.docx,.jpg,.png",
    materialType: 2,
    fileName: "",
    fileSize: "",
    filePath: null,
  },
  {
    text: "采购发票",
    required: true,
    accept: ".pdf,.jpg,.png",
    materialType: 3,
    fileName: "",
    fileSize: "",
    filePath: null,
  },
  {
    text: "验收表格",
    required: true,
    accept: ".pdf,.doc,.docx",
    materialType: 4,
    fileName: "",
    fileSize: "",
    filePath: null,
  },
  {
    text: "医疗器械注册证",
    required: true,
    accept: ".pdf,.jpg,.png",
    materialType: 5,
    fileName: "",
    fileSize: "",
    filePath: null,
  },
  {
    text: "承诺事项落实材料",
    required: true,
    accept: ".pdf,.doc,.docx",
    materialType: 6,
    fileName: "",
    fileSize: "",
    filePath: null,
  },
  {
    text: "乙类大型医用设备配置信息登记表",
    required: true,
    accept: ".pdf,.doc,.docx",
    materialType: 7,
    fileName: "",
    fileSize: "",
    filePath: null,
  },
  {
    text: "《乙类大型医用设备配置许可证》副本电子版",
    required: true,
    accept: ".pdf,.jpg,.png",
    materialType: 8,
    fileName: "",
    fileSize: "",
    filePath: null,
  },
]);

const initTableDataFn = async () => {
  try {
    if (!licenseBasis.value?.applicationId) return;
    loading.value = true;
    let response = await getFilesMaterialList({ applicationId: licenseBasis.value?.applicationId });
    materialTable.value.forEach((item, index) => {
      let res = response.data.find((i) => i.materialType === item.materialType);
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
  const maxSize = 1024 * 1024 * 20; // 20MB
  if (file.size > maxSize) {
    ElMessage.error("文件大小不能超过20MB");
    return false;
  }
  // 校验文件类型
  const validTypes = row.accept.split(",");
  const fileSuffix = file.name.slice(file.name.lastIndexOf("."));
  if (!validTypes.includes(fileSuffix)) {
    ElMessage.error(`文件类型必须是${validTypes.join("、")}`);
    return false;
  }
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
const previewFile = async (row) => {
  if (!row.filePath) {
    ElMessage.warning("文件路径不存在");
    return;
  }

  currentFile.value = row;
  previewTitle.value = row.fileName || '文件预览';
  previewVisible.value = true;
  previewLoading.value = true;
  previewType.value = '';

  try {
    const fileName = row.fileName || '';
    const fileExt = fileName.slice(fileName.lastIndexOf('.')).toLowerCase();

    // 图片预览
    if (['.png', '.jpg', '.jpeg', '.gif', '.bmp', '.webp'].includes(fileExt)) {
      previewType.value = 'image';
      previewUrl.value = row.filePath;
      previewWidth.value = '60%';
      previewLoading.value = false;
    }
    // PDF预览
    else if (fileExt === '.pdf') {
      previewType.value = 'pdf';
      previewUrl.value = row.filePath;
      previewWidth.value = '90%';
      previewLoading.value = false;
    }
    // Word和Excel不支持在线预览
    else if (['.docx', '.doc', '.xlsx', '.xls'].includes(fileExt)) {
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
const downloadFile = (row) => {
  if (!row.filePath) {
    ElMessage.warning("文件路径不存在");
    return;
  }

  window.open(row.filePath, '_blank');
};

// 删除文件
const deleteFile = (row) => {
  ElMessageBox.confirm(
      `确定要删除文件"${row.fileName}"吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(() => {
    row.fileName = "";
    row.fileSize = "";
    row.filePath = null;
    ElMessage.success("删除成功");
  }).catch(() => {
    ElMessage.info("已取消删除");
  });
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
    applicationId: licenseBasis.value?.applicationId || licenseBasis.value?.applicationId,
    materialType: item.materialType,
    materialName: item.fileName,
    filePath: item.filePath,
    fileSize: item.fileSize + "",
    uploadTime: new Date().toLocaleString(),
  }));
  try {
    loading.value = true;
    await createFilesMaterial(params);
    ElMessage.success("提交成功");
    return true;
  } catch (err) {
    ElMessage.error("提交失败");
    return false;
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  initTableDataFn();
});

defineExpose({
  validor,
  submit,
});
</script>

<style lang="scss" scoped>
.b-solid-1-0891b2 {
  border: 1px solid #70d4ed;
}
.t-indent-8px {
  text-indent: 8px;
}
.m-r-5 {
  margin-right: 5px;
}
.flex {
  display: flex;
}
.items-center {
  align-items: center;
}
.justify-center {
  justify-content: center;
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