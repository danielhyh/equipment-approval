<template>
  <div class="licence-container" id="licenceID" ref="licenceIDRef" v-loading="loading">
    <!-- 花边框 -->
    <div class="licence-out-border">
      <!-- 内边框 -->
      <div class="licence-in-border">
        <!-- 国徽 -->
        <img class="licence-img" :src="NEImage" alt="国徽" @error="imageloadError" />

        <div class="licence-title">{{ licenceTitle }}</div>
        <div class="licence-subtitle">{{ licenceSubtitle }}</div>
        <div class="licence-code">许可证编号 {{ licenceCode }}</div>
        <div class="licence-content" :class="{ 'licence-content-b': isBLicenceSub }">
          <!-- 许可证内容 -->
          <div class="licence-content-columns">
            <div class="licence-content-item" v-for="(item, i) in firstColumns" :key="i">
              <div class="label-col">
                <div class="label">{{ item.label }}</div>
                <div class="subLabel" v-if="item.subLabel">{{ item.subLabel }}</div>
              </div>
              <span class="value">{{ item.value }}</span>
            </div>
          </div>
          <div class="licence-content-columns">
            <div class="licence-content-item" v-for="(item, i) in secondColumns" :key="i">
              <div class="label-col">
                <div class="label">{{ item.label }}</div>
                <div class="subLabel" v-if="item.subLabel">{{ item.subLabel }}</div>
              </div>
              <span class="value">{{ item.value }}</span>
            </div>
          </div>
        </div>
        <!-- 二维码 -->
        <div class="licence-qr-code" v-if="!isBLicenceSub"></div>
        <!-- 签发机关 年月日盖章 -->
        <div class="licence-stamp-date">
          <div class="stamp-row">
            <span class="label">签发机关</span>
            <span class="value">{{ stampUit }}</span>
          </div>
          <div class="remark">（盖章）</div>
          <div class="date-row">
            <em></em>
            <span> 年 </span>
            <em></em>
            <span> 月 </span>
            <em></em>
            <span> 日 </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts" name="License">
import jsPDF from 'jspdf'
import domtoimage from 'dom-to-image'
import { ElMessage } from 'element-plus'
import NEImage from '@/assets/lincence/national-emblem-b.png'
import { ALicenceData, BLicenceData } from './license'
import { VuePrintNext } from 'vue-print-next'
// 许可证
let props = defineProps({
  licenceType: { type: String, default: 'A' }, // A:甲类 | B:乙类 控制标题  以及样式
  licenceSubtitle: { type: String, default: 'A' }, // A:正本 | B:副本 控制副标题 以及字段内容 以及样式
  code: { type: String, default: '甲2703200938' },
  licenceData: {
    type: Array,
    default: () => {
      return [
        '国家新能源科技有限公司', // 配置单位名称
        '913301067046373179', // 统一社会信用代码
        '韩歆毅', // 法定代表人
        '磁共振成像设备', // 许可设备名称
        '国有企业', // 所有制性质
        'MRI设备', // 阶梯配置机型
        '北京市海淀区' // 设备配置地址
      ]
    }
  },
  stampUit: { type: String, default: '陕西省卫生健康委员会' }, // 签发单位
  stampDate: { type: String, default: '2023年01月01日' }, // 签发日期
  seal: { type: String, default: '' } // 盖章
})

let licenceTitle = computed(() => {
  if (props.licenceType === 'A') {
    return '甲类大型医用设备配置许可证'
  }
  if (props.licenceType === 'B') {
    return '乙类大型医用设备配置许可证'
  }
})
let licenceSubtitle = computed(() => {
  if (props.licenceSubtitle === 'A') {
    return '（正本）'
  }
  if (props.licenceSubtitle === 'B') {
    return '（副本）'
  }
})
let licenceCode = computed(() => {
  return props.code
})
let licenceContent = computed(() => {
  let data =
    props.licenceSubtitle === 'A'
      ? JSON.parse(JSON.stringify(ALicenceData))
      : JSON.parse(JSON.stringify(BLicenceData))
  // 根据传入的 licenceData 替换默认数据
  if (props.licenceData.length > 0) {
    props.licenceData.forEach((item, index) => {
      data[index].value = item || ''
    })
  }
  return data
})
let firstColumns = computed(() => {
  return licenceContent.value.filter((_, index) => index % 2 === 0)
})
let secondColumns = computed(() => {
  return licenceContent.value.filter((_, index) => index % 2 === 1)
})
let isBLicenceSub = computed(() => {
  // 是否是副本
  return props.licenceSubtitle === 'B'
})

let licenceIDRef = ref<Element | null>(null)
let loading = ref<boolean>(false)
let imageload = ref<boolean>(true)
const imageloadError = () => {
  imageload.value = false
  console.error('Image failed to load')
}

const vaildFn = async () => {
  return new Promise((resolve, reject) => {
    if (!licenceIDRef.value) {
      ElMessage({
        message: '资源加载失败',
        type: 'error',
        duration: 1000,
        showClose: false,
        grouping: false
      })
      reject('error:HTML 资源加载失败！')
    }
    if (!imageload.value) {
      ElMessage({
        message: '图片资源加载失败',
        type: 'error',
        duration: 1000,
        showClose: false,
        grouping: false
      })
      reject('error:图片资源加载失败！')
    }
    resolve('success')
  })
}
const printFn = async () => {
  loading.value = true
  // 打印
  let response = await vaildFn().catch((err) => err)
  if (response !== 'success') {
    return
  }
  new VuePrintNext({
    el: '#licenceID'
  })
  loading.value = false
}
const downloadPdf = async () => {
  return new Promise(async (resolve, reject) => {
    loading.value = true
    try {
      // 下载PDF
      let response = await vaildFn().catch((err) => err)
      if (response !== 'success') {
        return
      }
      const element = licenceIDRef.value as HTMLElement
      element.style.margin = '0'
      let dataUrl = await domtoimage.toPng(element).catch(function (error) {
        console.error(' domtoimage 失败!', error)
      })
      element.style.margin = '0 auto'

      // A4横向尺寸 (297mm x 210mm)
      const pdfWidth = 297
      const pdfHeight = 210

      // 创建PDF实例
      const pdf = new jsPDF({
        orientation: 'landscape',
        unit: 'mm',
        format: [pdfWidth, pdfHeight],
        compress: true
      })

      // // 计算图片在PDF中的尺寸
      // const canvasAspectRatio = pdfWidth / pdfHeight
      // const pdfAspectRatio = pdfWidth / pdfHeight
      // // A4横向尺寸 (297mm x 210mm)
      // let imgWidth: number, imgHeight: number, offsetX: number, offsetY: number
      // if (canvasAspectRatio > pdfAspectRatio) {
      //   // 图片更宽，以宽度为准
      //   imgWidth = pdfWidth
      //   imgHeight = pdfWidth / canvasAspectRatio
      //   offsetX = 0
      //   offsetY = (pdfHeight - imgHeight) / 2
      // } else {
      //   // 图片更高，以高度为准
      //   imgHeight = pdfHeight
      //   imgWidth = pdfHeight * canvasAspectRatio
      //   offsetX = (pdfWidth - imgWidth) / 2
      //   offsetY = 0
      // }
      // console.log(imgWidth, imgHeight, '----------->>>>>')
      pdf.addImage(dataUrl, 'PNG', 0, 0, pdfWidth, pdfHeight)
      pdf.save(`${licenceTitle.value}-${licenceSubtitle.value}.pdf`)
      loading.value = false
      resolve('PDF下载成功')
    } catch (err) {
      loading.value = false
      console.log(err, '----->>>>下载PDF失败')
      reject(err)
    }
  })
}
defineExpose({
  print: printFn,
  download: downloadPdf
})
</script>

<style scoped>
.licence-container {
  width: 297mm;
  height: 210mm;
  background-color: white;
  position: relative;
  margin: 0 auto;
  line-height: 1;
  text-align: center;
}
.licence-out-border {
  width: 248.5mm;
  height: 178.3mm;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-sizing: border-box;
  background-image: url('@/assets/lincence/border.svg');
  background-repeat: no-repeat;
  background-size: 100% 100%;
}
.licence-in-border {
  width: 238.1mm;
  height: 167.6mm;
  background-color: rgba(235, 199, 115, 0.05);
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
.licence-img {
  width: 38.8mm;
  height: 41.6mm;
  position: absolute;
  top: 4mm;
  left: 50%;
  transform: translate(-50%, -50%);
}
.licence-title {
  text-align: center;
  font-size: 28pt;
  color: #ebc773;
  letter-spacing: 2.6mm;
  font-family: '黑体', 'Microsoft Yahei', sans-serif;
  padding-top: 33mm;
}
.licence-subtitle {
  text-align: center;
  font-size: 20pt;
  color: #ebc773;
  letter-spacing: 3.9mm;
  font-family: '华文宋体', '宋体', 'Microsoft Yahei', sans-serif;
  font-weight: bold;
  padding-top: 3mm;
}
.licence-code {
  font-size: 13pt;
  color: #000;
  font-family: '黑体', 'Microsoft Yahei', sans-serif;
  font-weight: bold;
  letter-spacing: 1.1mm;
  padding-top: 4mm;
}
.licence-content {
  text-align: left;
  padding: 7.2mm 20mm 0 20mm;
  display: flex;
  justify-content: space-between;
}
.licence-content-columns {
  /* display: table; */
  /* border-collapse: collapse; */
}
.licence-content-columns .licence-content-item {
  display: table-row;
  width: 100%;
}
/* label-col */
.licence-content-columns .licence-content-item .label-col {
  display: table-cell;
  white-space: nowrap;
}
.licence-content-columns .licence-content-item .label {
  font-size: 13pt;
  line-height: 28pt;
  color: #000;
  font-family: '黑体', 'Microsoft Yahei', sans-serif;
  /* font-weight: bold; */
}
.licence-content-columns .licence-content-item .subLabel {
  font-size: 10pt;
  line-height: 20pt;
  color: #000;
  font-family: '黑体', 'Microsoft Yahei', sans-serif;
}
.licence-content-columns .licence-content-item .value {
  font-size: 12pt;
  line-height: 13pt;
  color: #000;
  font-family: '黑体', 'Microsoft Yahei', sans-serif;
  padding-left: 10px;
  display: table-cell;
}
.licence-content .licence-content-columns:nth-of-type(1) {
  width: 98mm;
}
.licence-content .licence-content-columns:nth-of-type(2) {
  width: 81mm;
}
.licence-content .licence-content-columns .licence-content-item:last-child {
  margin-bottom: 0;
}
.licence-content-b .licence-content-columns .licence-content-item .label {
  line-height: 24pt;
}
.licence-content-b .licence-content-columns .licence-content-item .subLabel {
  line-height: 12pt;
}

.licence-qr-code {
  position: absolute;
  bottom: 12mm;
  left: 5mm;
  width: 22mm;
  height: 22mm;
  background-color: #ebc773;
  display: none;
}

.licence-stamp-date {
  position: absolute;
  bottom: 5mm;
  right: 5mm;
  text-align: right;
}
.licence-stamp-date .stamp-row {
  margin-bottom: 4mm;
}
.licence-stamp-date .stamp-row span {
  display: inline-block;
}
.licence-stamp-date .stamp-row .label {
  font-size: 12pt;
  font-family: '黑体', 'Microsoft Yahei', sans-serif;
  letter-spacing: 3.5mm;
}
.licence-stamp-date .stamp-row .value {
  width: 50mm;
  font-size: 12pt;
  font-family: '黑体', 'Microsoft Yahei', sans-serif;
  text-align: center;
}
.licence-stamp-date .remark {
  font-size: 11pt;
  line-height: 18pt;
  margin-bottom: 4mm;
  margin-right: 0;
  margin-left: auto;
  width: 43mm;
  text-align: center;
  font-family: '华文宋体', '宋体', 'Microsoft Yahei', sans-serif;
}
.licence-stamp-date .date-row {
  font-size: 12pt;
  font-style: normal;
  font-family: '黑体', 'Microsoft Yahei', sans-serif;
}
.licence-stamp-date .date-row em {
  display: inline-block;
  width: 10.2mm;
  text-align: center;
}
@page {
  size: A4 landscape;
  margin: 0;
}
@media print {
  * {
    margin: 0;
    padding: 0;
  }
  .licence-container {
    width: 297mm;
    height: 210mm;
    background-color: white;
    position: relative;
    margin: 0 auto;
    line-height: 1;
    text-align: center;
  }
  .licence-out-border {
    width: 248.5mm;
    height: 178.3mm;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    box-sizing: border-box;
    background-image: url('@/assets/lincence/border.svg');
    background-repeat: no-repeat;
    background-size: 100% 100%;
  }
  .licence-in-border {
    width: 238.1mm;
    height: 167.6mm;
    background-color: rgba(235, 199, 115, 0.05);
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
  .licence-img {
    width: 38.8mm;
    height: 41.6mm;
    position: absolute;
    top: 4mm;
    left: 50%;
    transform: translate(-50%, -50%);
  }
  .licence-title {
    text-align: center;
    font-size: 28pt;
    color: #ebc773;
    letter-spacing: 2.6mm;
    font-family: '黑体', 'Microsoft Yahei', sans-serif;
    padding-top: 33mm;
  }
  .licence-subtitle {
    text-align: center;
    font-size: 20pt;
    color: #ebc773;
    letter-spacing: 3.9mm;
    font-family: '华文宋体', '宋体', 'Microsoft Yahei', sans-serif;
    font-weight: bold;
    padding-top: 3mm;
  }
  .licence-code {
    font-size: 13pt;
    color: #000;
    font-family: '黑体', 'Microsoft Yahei', sans-serif;
    font-weight: bold;
    letter-spacing: 1.1mm;
    padding-top: 4mm;
  }
  .licence-content {
    text-align: left;
    padding: 7.2mm 20mm 0 20mm;
    display: flex;
    justify-content: space-between;
  }
  .licence-content-columns {
    /* display: table; */
    /* border-collapse: collapse; */
  }
  .licence-content-columns .licence-content-item {
    display: table-row;
    width: 100%;
  }
  /* label-col */
  .licence-content-columns .licence-content-item .label-col {
    display: table-cell;
    white-space: nowrap;
  }
  .licence-content-columns .licence-content-item .label {
    font-size: 13pt;
    line-height: 28pt;
    color: #000;
    font-family: '黑体', 'Microsoft Yahei', sans-serif;
    /* font-weight: bold; */
  }
  .licence-content-columns .licence-content-item .subLabel {
    font-size: 10pt;
    line-height: 20pt;
    color: #000;
    font-family: '黑体', 'Microsoft Yahei', sans-serif;
  }
  .licence-content-columns .licence-content-item .value {
    font-size: 12pt;
    line-height: 13pt;
    color: #000;
    font-family: '黑体', 'Microsoft Yahei', sans-serif;
    padding-left: 10px;
    display: table-cell;
  }
  .licence-content .licence-content-columns:nth-of-type(1) {
    width: 98mm;
  }
  .licence-content .licence-content-columns:nth-of-type(2) {
    width: 81mm;
  }
  .licence-content .licence-content-columns .licence-content-item:last-child {
    margin-bottom: 0;
  }
  .licence-content-b .licence-content-columns .licence-content-item .label {
    line-height: 24pt;
  }
  .licence-content-b .licence-content-columns .licence-content-item .subLabel {
    line-height: 12pt;
  }

  .licence-qr-code {
    position: absolute;
    bottom: 12mm;
    left: 5mm;
    width: 22mm;
    height: 22mm;
    background-color: #ebc773;
    display: none;
  }

  .licence-stamp-date {
    position: absolute;
    bottom: 5mm;
    right: 5mm;
    text-align: right;
  }
  .licence-stamp-date .stamp-row {
    margin-bottom: 4mm;
  }
  .licence-stamp-date .stamp-row span {
    display: inline-block;
  }
  .licence-stamp-date .stamp-row .label {
    font-size: 12pt;
    font-family: '黑体', 'Microsoft Yahei', sans-serif;
    letter-spacing: 3.5mm;
  }
  .licence-stamp-date .stamp-row .value {
    width: 50mm;
    font-size: 12pt;
    font-family: '黑体', 'Microsoft Yahei', sans-serif;
    text-align: center;
  }
  .licence-stamp-date .remark {
    font-size: 11pt;
    line-height: 18pt;
    margin-bottom: 4mm;
    margin-right: 0;
    margin-left: auto;
    width: 43mm;
    text-align: center;
    font-family: '华文宋体', '宋体', 'Microsoft Yahei', sans-serif;
  }
  .licence-stamp-date .date-row {
    font-size: 12pt;
    font-style: normal;
    font-family: '黑体', 'Microsoft Yahei', sans-serif;
  }
  .licence-stamp-date .date-row em {
    display: inline-block;
    width: 10.2mm;
    text-align: center;
  }
}
</style>
