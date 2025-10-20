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
            <span>上传时间:{{ item.uploadTime }}</span>
            <em>|</em>
            <span>文件大小:{{ item.size }}</span>
          </div>
          <div class="handler-box">
            <el-button round size="small" type="primary" :icon="View" @click.stop="viewFn(item)">
              预览
            </el-button>
            <el-button
              round
              size="small"
              type="warning"
              :icon="Download"
              @click.stop="downLoadFn(item)"
            >
              下载
            </el-button>
          </div>
        </div>
      </template>
      <el-empty v-else :image-size="120" />
    </div>

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

        <!-- Word预览 -->
        <div v-else-if="previewType === 'word'" class="word-preview">
          <div v-html="wordContent"></div>
        </div>

        <!-- Excel预览 -->
        <div v-else-if="previewType === 'excel'" class="excel-preview">
          <el-tabs v-model="activeSheet" v-if="excelSheets.length > 0">
            <el-tab-pane
              v-for="sheet in excelSheets"
              :key="sheet.name"
              :label="sheet.name"
              :name="sheet.name"
            >
              <div class="excel-table-wrapper">
                <table class="excel-table">
                  <tbody>
                  <tr v-for="(row, rowIndex) in sheet.data" :key="rowIndex">
                    <td v-for="(cell, colIndex) in row" :key="colIndex">
                      {{ cell }}
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </el-tab-pane>
          </el-tabs>
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

<script setup lang="ts">
import { ref, computed } from 'vue'
import { List, View, Download } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { ApplicationMaterialApi } from '@/api/biz/applicationmaterial'
import { useRoute } from 'vue-router'
import * as mammoth from 'mammoth'
import * as XLSX from 'xlsx'

const route = useRoute()
const { id } = route.query

interface fileItemType {
  name: string
  url: string
  uploadTime: string
  size: string
  id: string | number
  fileType: string
}

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
    case 'png':
    case 'jpg':
    case 'jpeg':
      return 'lets-icons:img-box-fill'
  }
}

const infoList = ref([])
const filesData = ref<fileItemType[]>([])

// 预览相关状态
const previewVisible = ref(false)
const previewLoading = ref(false)
const previewType = ref('')
const previewUrl = ref('')
const previewTitle = ref('')
const previewWidth = ref('80%')
const isFullscreen = ref(false)
const currentFile = ref<fileItemType | null>(null)
const wordContent = ref('')
const excelSheets = ref<Array<{ name: string; data: any[][] }>>([])
const activeSheet = ref('')

const toggleFullscreen = () => {
  isFullscreen.value = !isFullscreen.value
}

const viewFn = async (item: fileItemType) => {
  console.log('useinfo view fn', item)
  currentFile.value = item
  previewTitle.value = item.name
  previewVisible.value = true
  previewLoading.value = true
  previewType.value = ''
  wordContent.value = ''
  excelSheets.value = []

  try {
    const fileType = item.fileType?.toLowerCase()

    // 图片预览
    if (['png', 'jpg', 'jpeg', 'gif', 'bmp', 'webp'].includes(fileType)) {
      previewType.value = 'image'
      previewUrl.value = item.url
      previewWidth.value = '60%'
      previewLoading.value = false
    }
    // PDF预览
    else if (fileType === 'pdf') {
      previewType.value = 'pdf'
      previewUrl.value = item.url
      previewWidth.value = '90%'
      previewLoading.value = false
    }
    // Word预览
    else if (fileType === 'docx' || fileType === 'doc') {
      previewType.value = 'word'
      previewWidth.value = '80%'
      await previewWord(item.url)
    }
    // Excel预览
    else if (fileType === 'xlsx' || fileType === 'xls') {
      previewType.value = 'excel'
      previewWidth.value = '90%'
      await previewExcel(item.url)
    }
    // 不支持的格式
    else {
      previewType.value = 'unsupported'
      previewWidth.value = '50%'
      previewLoading.value = false
    }
  } catch (error) {
    console.error('预览失败:', error)
    ElMessage.error('预览失败,请稍后重试')
    previewLoading.value = false
    previewType.value = 'unsupported'
  }
}

const previewWord = async (url: string) => {
  try {
    const response = await fetch(url)
    const arrayBuffer = await response.arrayBuffer()
    const result = await mammoth.convertToHtml({ arrayBuffer })
    wordContent.value = result.value
    previewLoading.value = false
  } catch (error) {
    console.error('Word预览失败:', error)
    ElMessage.error('Word文档预览失败')
    previewLoading.value = false
    previewType.value = 'unsupported'
  }
}

const previewExcel = async (url: string) => {
  try {
    const response = await fetch(url)
    const arrayBuffer = await response.arrayBuffer()
    const workbook = XLSX.read(arrayBuffer, { type: 'array' })

    excelSheets.value = workbook.SheetNames.map((sheetName) => {
      const worksheet = workbook.Sheets[sheetName]
      const data = XLSX.utils.sheet_to_json(worksheet, { header: 1, defval: '' })
      return {
        name: sheetName,
        data: data as any[][]
      }
    })

    if (excelSheets.value.length > 0) {
      activeSheet.value = excelSheets.value[0].name
    }
    previewLoading.value = false
  } catch (error) {
    console.error('Excel预览失败:', error)
    ElMessage.error('Excel文档预览失败')
    previewLoading.value = false
    previewType.value = 'unsupported'
  }
}

const downLoadFn = (item: fileItemType) => {
  window.open(item.url, '_blank')
}

const getFileType = (filename) => {
  if (!filename) return ''
  const lastDotIndex = filename.lastIndexOf('.')
  if (lastDotIndex === -1 || lastDotIndex === filename.length - 1) {
    return ''
  }
  return filename.substring(lastDotIndex + 1).toLowerCase()
}

const bytesToMB = (bytes: number) => {
  if (bytes === 0) return '0 MB'
  const mb = bytes / (1024 * 1024)
  return mb.toFixed(2) + ' MB'
}

const getInfoList = async () => {
  const params = { id: id }
  infoList.value = await ApplicationMaterialApi.list(params)
  infoList.value.forEach((item) => {
    let obj: Partial<fileItemType> = {}
    obj.name = item.materialName
    obj.id = item.id
    obj.fileType = getFileType(item.filePath)
    obj.size = bytesToMB(item.fileSize)
    obj.url = item.filePath
    obj.uploadTime = new Date(item.uploadTime).toLocaleString()
    filesData.value.push(obj as fileItemType)
  })
}

onMounted(() => {
  getInfoList()
})

defineExpose({
  viewFn
})
</script>

<style lang="scss" scoped>
.use-info-box {
  padding: 16px;

  .title {
    display: flex;
    padding: 5px 0;
    font-size: 16px;
    font-weight: 600;
    color: #165dff;
    border-bottom: 2px solid rgb(22 93 255 / 10%);
    align-items: center;

    .el-icon {
      margin-right: 6px;
      font-size: 22px;
    }
  }

  .file-list-box {
    position: relative;
    padding: 12px 0;

    .file-item {
      position: relative;
      padding: 15px 164px 15px 54px;
      margin-bottom: 15px;
      background-color: rgb(248 250 252 / 80%);
      border: 1px solid rgb(226 232 240 / 60%);
      border-radius: 8px;
      transition: all 0.3s ease;

      &:hover {
        border-color: rgb(22 93 255 / 30%);
        box-shadow: 0 4px 12px rgb(22 93 255 / 10%);
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
        margin-bottom: 4px;
        overflow: hidden;
        font-size: 14px;
        font-weight: 600;
        color: #1e293b;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .file-des {
        font-size: 12px;
        color: #64748b;

        em {
          margin: 0 6px;
          font-style: normal;
        }
      }
    }
  }
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

  .word-preview {
    padding: 20px;
    background: #fff;
    border: 1px solid #e5e7eb;
    border-radius: 4px;

    :deep(img) {
      height: auto;
      max-width: 100%;
    }

    :deep(table) {
      width: 100%;
      margin: 10px 0;
      border-collapse: collapse;

      td,
      th {
        padding: 8px;
        border: 1px solid #ddd;
      }
    }
  }

  .excel-preview {
    .excel-table-wrapper {
      max-height: 60vh;
      overflow: auto;
    }

    .excel-table {
      width: 100%;
      font-size: 13px;
      border-collapse: collapse;

      td {
        min-width: 80px;
        padding: 8px 12px;
        white-space: nowrap;
        border: 1px solid #e5e7eb;
      }

      tr:nth-child(even) {
        background-color: #f9fafb;
      }

      tr:hover {
        background-color: #f3f4f6;
      }
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
