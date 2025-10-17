<template>
  <div v-loading="loading">
    <div class="page-content">
      <div class="qrcode-content-box" id="QRCODEIMAGE" ref="qrcodeRef" style="overflow: hidden">
        <Qrcode tag="canvas" :text="qrcodeText" :width="230" />
      </div>
      <p class="text-style">许可证编号: {{ licenseCode }}</p>
      <p class="text-style">扫描二维码可快速验证证书信息</p>
      <div class="page-btn-box">
        <el-button type="primary" size="small" @click="handlerDownlod">下载</el-button>
        <el-button type="primary" size="small" @click="handlPrint">打印</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { VuePrintNext } from 'vue-print-next'
import domtoimage from 'dom-to-image'
import { useApplicationDataStore } from '@/store/applicationData'

const route = useRoute()
const licenseCode = route.query.licenseCode

const appData = useApplicationDataStore()
const basicInfoStore = computed(() => appData.getApplicationData)

let originalId = inject('originalId', '')
let duplicateId = inject('duplicateId', '')

let loading = ref(false)
let qrcodeText = computed(() => {
  let text = 'http://hospital.fangliyun.com/#/mobile/qrcode?'
  if (originalId) {
    text += `o=${originalId}&`
  }
  if (duplicateId) {
    text += `d=${duplicateId}`
  }
  return text
})

const qrcodeRef = ref(null)

const handlerDownlod = async () => {
  if (qrcodeRef.value) {
    // 获取 canvas 元素
    const canvas = qrcodeRef.value
    if (canvas) {
      // 克隆 demo
      loading.value = true
      let dataUrl = await domtoimage.toPng(canvas).catch(function (error) {
        console.error(' domtoimage 失败!', error)
      })
      // 创建一个隐藏的 a 标签用于下载
      const downloadLink = document.createElement('a')
      downloadLink.href = dataUrl
      // 设置下载文件名
      downloadLink.download = 'qrcode.png'
      // 将 a 标签添加到文档中
      document.body.appendChild(downloadLink)

      // 触发点击事件进行下载
      downloadLink.click()
      // 移除 a 标签
      document.body.removeChild(downloadLink)
      loading.value = false
    }
  }
}

const handlPrint = () => {
  if (qrcodeRef.value) {
    loading.value = true
    new VuePrintNext({
      el: '#QRCODEIMAGE',
      standard: 'html5',
      orientation: 'landscape'
    })
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.page-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  padding-top: 40px;
}

.qrcode-content-box {
  background-color: #fff;
  padding: 10px;
  box-shadow: 0 0 10px 3px rgba(0, 0, 0, 0.3);
  border-radius: 20px;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}
.page-btn-box {
  margin-top: 10px;
}
.text-style {
  font-size: 14px;
  color: #666;
  margin-top: 10px;
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
