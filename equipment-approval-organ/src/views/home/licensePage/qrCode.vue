<template>
  <div v-loading="loading">
    <div v-if="qrcodeInit" class="flex justify-center items-center m-b-30" style="flex-direction: column">
      <div class="flex justify-center items-center" id="QRCODEIMAGE" style="overflow: hidden" ref="qrcodeRef">
        <Qrcode tag="canvas" :text="qrcodeText" :width="230" />
      </div>
      <div class="flex justify-center m-t-10">
        <el-button type="primary" size="small" @click="handlerDownlod">下载</el-button>
        <el-button type="primary" size="small" @click="handlPrint">打印</el-button>
      </div>
    </div>
    <el-empty v-else description="暂无二维码" />
    <div class="remark-box p-10">
      <div class="f-s-16 flex items-center m-b-5 p-l-r-10">
        <svg-icon name="fa:info-circle" class="m-r-5" size="16" color="#165dff" />
        <span>二维码信息</span>
      </div>
      <div class="f-s-14 p-lr-20 c-64748b">
        <div v-for="(item, index) in listMsg" :key="index" class="m-b-5">
          <span class="f-w-700">{{ item.label }} ：</span>
          <span>{{ item.value || "--" }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useBasisStore } from "@/pinia/modules/basis";
import { getLicenseOrigin } from "@/apis/home";
import { VuePrintNext } from "vue-print-next";
import domtoimage from "dom-to-image";
let basisStore = useBasisStore();
let licenseBasis = computed(() => basisStore.getLicenseBasis);
let loading = ref(false);
let listMsg = reactive([
  { label: "证书编号", value: "", key: "licenseNo" },
  { label: "许可设备名称", value: "", key: "licenseDeviceName" },
  { label: "配置单位", value: "", key: "configUnitName" },
  { label: "生成时间", value: "", key: "issueDate" },
  { label: "二维码用途", value: "用于快速查询证书详细信息和验证证书真伪", key: "" },
  { label: "扫码说明", value: "使用手机扫描二维码可快速访问证书在线验证页面", key: "" },
]);
let qrcodeText = computed(() => {
  let text = window.location.origin + "/#/mobile/qrcode?";
  if (licenseBasis.value.originalId) {
    text += `o=${licenseBasis.value.originalId}&`;
  }
  if (licenseBasis.value.duplicateId) {
    text += `d=${licenseBasis.value.duplicateId}`;
  }
  return text;
});
let qrcodeInit = ref(false);
const init = async () => {
  try {
    loading.value = true;
    let res = await getLicenseOrigin(licenseBasis.value.originalId);
    let msg = { ...res.data, ...licenseBasis.value };
    listMsg.forEach((item) => {
      if (item.key) item.value = msg[item.key] || "--";
    });
    qrcodeInit.value = true;
  } catch (err) {
    ElMessage.error("获取二维码信息失败");
  } finally {
    loading.value = false;
  }
};
const qrcodeRef = ref(null);

const handlerDownlod = async () => {
  if (qrcodeRef.value) {
    // 获取 canvas 元素
    const canvas = qrcodeRef.value;
    console.log(canvas);
    if (canvas) {
      // 克隆 demo
      loading.value = true;
      let dataUrl = await domtoimage.toPng(canvas).catch(function (error) {
        console.error(" domtoimage 失败!", error);
      });
      // 创建一个隐藏的 a 标签用于下载
      const downloadLink = document.createElement("a");
      downloadLink.href = dataUrl;
      // 设置下载文件名
      downloadLink.download = "qrcode.png";
      // 将 a 标签添加到文档中
      document.body.appendChild(downloadLink);

      // 触发点击事件进行下载
      downloadLink.click();
      // 移除 a 标签
      document.body.removeChild(downloadLink);
      loading.value = false;
    }
  }
};

const handlPrint = () => {
  if (qrcodeRef.value) {
    loading.value = true;
    new VuePrintNext({
      el: "#QRCODEIMAGE",
      standard: "html5",
      orientation: "landscape",
    });
    loading.value = false;
  }
};

onMounted(() => {
  init();
});
</script>

<style lang="scss" scoped>
.remark-box {
  border-radius: 10px;
  background-color: #dcfff979;
  position: relative;
  border: 1px solid #2298e2e8;
  border-left: 4px solid #2298e2e8;
}
#QRCODEIMAGE {
  padding: 10px;
  box-shadow: 0 0 10px 0 #cfcfcf;
  border-radius: 10px;
  background-color: #fff;
}
@media print {
  #QRCODEIMAGE {
    overflow: hidden;
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    margin: auto;
  }
}
</style>
