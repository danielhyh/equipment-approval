<template>
  <el-upload
    class="upload-box"
    ref="uploadRef"
    :limit="1"
    v-model:file-list="fileList"
    :show-file-list="false"
    :accept="acceptType"
    :http-request="fileRequestFn"
    :before-upload="(file) => handleUpload(file)"
    :on-exceed="(file) => handleExceed(file)"
    :on-error="(file) => handleError(file)"
    :on-success="(file) => handleSuccess(file)"
    :disabled="isDisabled"
  >
    <div class="content-style" v-loading="loading">
      <template v-if="!filePath">
        <svg-icon name="icomoon-free:upload" :size="40" color="#3092fb" class="m-b-10" />
        <el-button type="primary" size="small" :disabled="isDisabled">
          <span>{{ filePath ? "重新上传" : "点击上传" }}</span>
        </el-button>
      </template>
      <template v-else>
        <svg-icon name="codicon:pass-filled" :size="40" color="#00c853" class="m-b-10" />
        <span class="f-s-20 c-475569">文件上传成功</span>
        <span class="f-s-14 c-059669" @click="handleClick">点击重新上传</span>
      </template>
    </div>
  </el-upload>
</template>

<script setup name="Upload">
import { createUploadFile } from "@/apis/applyFor";
import { genFileId } from "element-plus";
import { Upload } from "@element-plus/icons-vue";
let loading = ref(false);
let props = defineProps({
  accept: {
    type: String,
    default: "*",
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  maxSize: {
    type: Number,
    default: 10, // 10MB
  },
});
let isDisabled = props.disabled;
let acceptType = props.accept;
let uploadRef = ref(null);
let fileList = ref([]);
let filePath = defineModel("filePath", {
  type: String,
  default: "",
});

// 处理文件上传前置校验
const handleUpload = (file) => {
  // 校验文件大小
  const maxSize = 1024 * 1024 * props.maxSize; // 10MB
  if (file.size > maxSize) {
    ElMessage.error("文件大小不能超过10MB");
    return false;
  }
  // 校验文件类型
  const validTypes = acceptType.split(",");
  const fileSuffix = file.name.slice(file.name.lastIndexOf("."));
  if (!validTypes.includes(fileSuffix)) {
    ElMessage.error(`文件类型必须是 ${validTypes.join("、")}`);
    return false;
  }
  return true;
};
// 覆盖之前文件
const handleExceed = async (files) => {
  let valid = await handleUpload(files[0]);
  if (!valid) return;
  if (uploadRef.value) {
    uploadRef.value.clearFiles();
    const file = files[0];
    file.uid = genFileId();
    uploadRef.value.handleStart(file);
    uploadRef.value.submit();
  }
};
const handleError = (error, index, row) => {
  filePath.value = "";
};
const handleSuccess = (response, index, row) => {
  filePath.value = response.data;
};
// 自定义上传
const fileRequestFn = async (file) => {
  const { file: uploadFile, onSuccess, onError } = file;
  try {
    // 创建FormData对象
    const formData = new FormData();
    formData.append("file", uploadFile);
    loading.value = true;
    let response = await createUploadFile(formData);

    // 调用上传成功回调
    // onSuccess(response);
    ElMessage.success(`文件 ${uploadFile.name}上传成功`);
    return response;
  } catch (error) {
    // 调用上传失败回调
    onError(error);
    ElMessage.error(`文件 ${uploadFile.name}上传失败: ${error.msg || "未知错误"}`);
  } finally {
    loading.value = false;
  }
};
// 校验
const validor = async () => {
  if (!filePath.value) {
    ElMessage.error("请上传文件");
    return false;
  }
  return true;
};
defineExpose({
  validor,
  filePath: filePath.value,
});
</script>

<style lang="scss" scoped>
.upload-box {
  width: 100%;
  &:deep(.el-upload--text) {
    width: 100%;
    .content-style {
      width: 100%;
      height: 150px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      border: 2px dashed #cbc9c9;
      background-color: #f5f5f5;
      border-radius: 10px;
      &:hover {
        border-color: #3092fb;
      }
    }
  }
}
</style>
