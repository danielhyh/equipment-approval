<template>
  <div class="use-info-box">
    <div class="title">
      <el-icon><List /></el-icon>
      <span>申请材料</span>
    </div>
    <div class="file-list-box">
      <template v-if="filesData.length > 0">
        <div class="file-item" v-for="item in filesData" :key="item.id">
          <Icon :icon="getFileIcon(item.fileType)" :size="32" color="#165DFF" />
          <div class="file-name">{{ item.name }}</div>
          <div class="file-des">
            <span>上传时间：{{ item.uploadTime }}</span>
            <em>|</em>
            <span>文件大小：{{ item.size }}</span>
          </div>
          <div class="handler-box">
            <el-button round size="small" type="primary" :icon="View" @click.stop="viewFn(item)"
              >查看</el-button
            >
            <el-button round size="small" type="warning" :icon="Download">下载</el-button>
          </div>
        </div>
      </template>
      <el-empty v-else :image-size="120" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { List, View, Download } from '@element-plus/icons-vue'
import { ApplicationMaterialApi } from '@/api/biz/applicationmaterial'
import { useRoute } from 'vue-router'
const route = useRoute()
const { id } = route.query
// 修正 interface 定义
interface fileItemType {
  name: string
  url: string
  uploadTime: string
  size: string
  id: string | number
  fileType: string
}
// let props = defineProps({
//   files: {
//     type: Array as PropType<fileItemType[]>,
//     default: () => [
//       { name: '申请表', url: '', uploadTime: '2024-09-23', size: '3.2MB', id: 1, fileType: 'pdf' },
//       { name: '申请表', url: '', uploadTime: '2024-09-23', size: '3.2MB', id: 2, fileType: 'pdf' },
//       { name: '申请表', url: '', uploadTime: '2024-09-23', size: '3.2MB', id: 3, fileType: 'pdf' },
//       { name: '申请表', url: '', uploadTime: '2024-09-23', size: '3.2MB', id: 4, fileType: 'docx' },
//       {
//         name: '技术条件配套设备专业技术人员材料.docx',
//         url: '',
//         uploadTime: '2024-09-23',
//         size: '3.2MB',
//         id: 5,
//         fileType: 'docx'
//       },
//       {
//         name: '技术条件配套设备专业技术人员材料.xlsx',
//         url: '',
//         uploadTime: '2024-09-23',
//         size: '3.2MB',
//         id: 6,
//         fileType: 'xlsx'
//       }
//     ]
//   }
// })
// let filesData = computed(() => {
//   return props.files
// })

const getFileIcon = (type: string) => {
  let str = 'svg-icon:'
  switch (type) {
    case 'pdf':
      return str + 'file-pdf-solid-full'
    case 'docx':
      return str + 'file-word-solid-full'
    case 'xlsx':
      return str + 'file-excel-solid-full'
    case 'xls':
      return str + 'file-excel-solid-full'
  }
}
const $emit = defineEmits(['view'])
const viewFn = (item: fileItemType) => {
  console.log('useinfo view fn', item)
}
const infoList = ref([])
const filesData = ref<fileItemType[]>([])
// 获取文件扩展名
const getFileType = (filename) => {
  if (!filename) return ''
  const lastDotIndex = filename.lastIndexOf('.')
  if (lastDotIndex === -1 || lastDotIndex === filename.length - 1) {
    return '' // 没有扩展名或 . 在末尾
  }
  return filename.substring(lastDotIndex + 1).toLowerCase()
}
const getInfoList = async () => {
  const params = { id: id }
  infoList.value = await ApplicationMaterialApi.list(params)
  infoList.value.forEach((item) => {
    let obj: Partial<fileItemType> = {}
    obj.name = item.materialName

    obj.id = item.id
    obj.fileType = getFileType(item.materialName)
    obj.size = bytesToMB(item.fileSize)
    obj.url = item.filePath
    obj.uploadTime = item.uploadTime?.toString()
    filesData.value.push(obj)
  })
}
const bytesToMB = (bytes: number) => {
  if (bytes === 0) return '0 MB'
  const mb = bytes / (1024 * 1024) // 转为 MB
  return mb.toFixed(2) + ' MB' // 保留两位小数
}
defineExpose({
  viewFn
})
onMounted(() => {
  getInfoList()
})
</script>

<style lang="scss" scoped>
.use-info-box {
  padding: 16px;
  .title {
    display: flex;
    align-items: center;
    padding: 5px 0px;
    border-bottom: 2px solid rgba(22, 93, 255, 0.1);
    font-size: 16px;
    font-weight: 600;
    color: #165dff;
    .el-icon {
      font-size: 22px;
      margin-right: 6px;
    }
  }
  .file-list-box {
    padding: 12px 0;
    position: relative;
    .file-item {
      position: relative;
      padding: 15px 164px 15px 54px;
      background-color: rgba(248, 250, 252, 0.8);
      border: 1px solid rgba(226, 232, 240, 0.6);
      border-radius: 8px;
      margin-bottom: 15px;
      transition: all 0.3s ease;
      &:hover {
        box-shadow: 0 4px 12px rgba(22, 93, 255, 0.1);
        border-color: rgba(22, 93, 255, 0.3);
      }
      .el-icon {
        position: absolute;
        top: 50%;
        left: 12px;
        transform: translateY(-50%);
      }
      .handler-box {
        position: absolute;
        top: 50%;
        right: 12px;
        transform: translateY(-50%);
      }
      .file-name {
        font-weight: 600;
        color: #1e293b;
        margin-bottom: 4px;
        font-size: 14px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      .file-des {
        color: #64748b;
        font-size: 12px;
        em {
          font-style: normal;
          margin: 0 6px;
        }
      }
    }
  }
}
</style>
