<template>
  <div class="licence-container" id="licenceID" ref="licenceIDRef" @click="printFn">
    <!-- 花边框 -->
    <div class="licence-out-border">
      <!-- 内边框 -->
      <div class="licence-in-border">
        <!-- 国徽 -->
        <img class="licence-img" :src="NEImage" alt="国徽" @error="imageloadError" />

        <div class="licence-title">甲类大型医用设备配置许可证</div>
        <div class="licence-subtitle">（正本）</div>
        <div class="licence-code">许可证编号 甲2702200743</div>
        <div class="licence-content">
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
        <div class="licence-qr-code"></div>
        <!-- 签发机关 年月日盖章 -->
        <div class="licence-stamp-date">
          <div class="stamp-row">
            <span class="label">签发机关</span>
            <span class="value">国家医疗机构保障局 </span>
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

<script setup lang="ts" name="Licence">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import NEImage from '@/assets/lincence/national-emblem-b.png'

let props = defineProps({
  licenceType: { type: String, default: 'A' }, // A:甲类 | B:乙类 控制标题  以及样式
  licenceSubtitle: { type: String, default: 'A' }, // A:正本 | B:副本 控制副标题 以及字段内容 以及样式
  licenceCode: { type: String, default: '甲2703200938' },
  licenceData: {
    type: Array,
    default: () => {
      return []
    }
  },
  stampUit: { type: String, default: '国家医疗机构保障局' }, // 签发单位
  stampDate: { type: String, default: '2023年01月01日' }
})
let imageload = ref<boolean>(true)
const imageloadError = () => {
  imageload.value = false
  console.error('Image failed to load')
}

let licenceIDRef = ref<Element | null>(null)

type contentType = {
  label: string
  subLabel?: string
  value?: string
}
let licenceContent = reactive<contentType[]>([])
licenceContent = [
  {
    label: '配置单位名称',
    value: '国家新能源科技有限公司'
  },
  {
    label: '统一社会信用代码',
    subLabel: '（或组织机构代码）',
    value: '913301067046373179'
  },
  {
    label: '法定代表人',
    subLabel: '（或主要负责人）',
    value: '韩歆毅'
  },
  {
    label: '许可设备名称',
    value: '磁共振成像设备'
  },
  {
    label: '所有制性质',
    value: '国有企业'
  },
  {
    label: '阶梯配置机型',
    value: 'MRI设备'
  },
  {
    label: '设备配置地址',
    value: '北京市海淀区长安路249号-1号楼-2单元-101室'
  }
]

let firstColumns = computed(() => {
  return licenceContent.filter((_, index) => index % 2 === 0)
})
let secondColumns = computed(() => {
  return licenceContent.filter((_, index) => index % 2 === 1)
})

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
  // 打印
  let response = await vaildFn().catch((err) => err)
  if (response !== 'success') {
    return
  }
  console.log(response, '-----')
}
defineExpose({
  print: printFn
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

@print {
  * {
    margin: 0;
    padding: 0;
  }
}
</style>
