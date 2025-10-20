<template>
  <div class="notification-container" v-loading="loading">
    <!-- 页面标题栏 -->
    <div class="page-header">
      <div class="page-header-left">
        <div class="page-title">通知公告</div>
        <div class="page-subtitle">系统通知与公告管理</div>
      </div>
      <div class="page-header-right">
        <el-button type="primary" :icon="Plus" @click="handleCreate"> 发布公告 </el-button>
      </div>
    </div>

    <!-- 数据卡片 -->
    <div class="data-card">
      <!-- 筛选栏 -->
      <div class="filter-bar">
        <div class="filter-left">
          <el-select
            v-model="queryParams.status"
            placeholder="全部状态"
            clearable
            style="width: 150px"
            @change="handleQuery"
          >
            <el-option label="全部状态" value="" />
            <el-option label="已发布" value="已发布" />
            <el-option label="草稿" value="未发布" />
          </el-select>
        </div>
        <div class="filter-right">
          <el-input
            v-model="queryParams.keyword"
            placeholder="搜索公告标题..."
            :prefix-icon="Search"
            clearable
            style="width: 260px"
            @keyup.enter="handleQuery"
            @clear="handleQuery"
          />
          <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        </div>
      </div>

      <!-- 表格 -->
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column type="index" label="序号" width="70" align="center" />

        <el-table-column prop="title" label="公告标题" min-width="300" show-overflow-tooltip>
          <template #default="{ row }">
            <span class="title-link" @click="handleView(row)">{{ row.title }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="publishTime" label="发布时间" width="180" align="center" />

        <el-table-column prop="status" label="状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === '已发布'" type="success" effect="plain">
              <el-icon><CircleCheck /></el-icon>
              已发布
            </el-tag>
            <el-tag v-else type="warning" effect="plain">
              <el-icon><Edit /></el-icon>
              草稿
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="viewCount" label="浏览数" width="120" align="center">
          <template #default="{ row }">
            <span class="view-count">{{ row.viewCount || 0 }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="240" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" :icon="View" @click="handleView(row)">
              查看详情
            </el-button>

            <template v-if="row.status === 'published'">
              <el-button
                link
                type="danger"
                size="small"
                :icon="RefreshLeft"
                @click="handleRevoke(row)"
              >
                撤回
              </el-button>
            </template>

            <template v-else>
              <el-button link type="primary" size="small" :icon="Edit" @click="handleEdit(row)">
                修改
              </el-button>
              <el-button
                link
                type="success"
                size="small"
                :icon="Promotion"
                @click="handlePublish(row)"
              >
                发布
              </el-button>
            </template>

            <el-button link type="danger" size="small" :icon="Delete" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-box">
        <el-pagination
          v-model:current-page="queryParams.pageNo"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleQuery"
          @current-change="handleQuery"
        />
      </div>
    </div>

    <!-- 查看详情弹窗 -->
    <el-dialog
      v-model="detailVisible"
      title="通知详情"
      width="900px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <div class="notification-detail">
        <div class="detail-meta">
          <div class="meta-item">
            <span class="meta-label">标题：</span>
            <span class="meta-value">{{ currentNotification.title }}</span>
          </div>
          <div class="meta-item">
            <span class="meta-label">发布时间：</span>
            <span class="meta-value">{{ currentNotification.publishTime }}</span>
          </div>
          <div class="meta-item">
            <span class="meta-label">状态：</span>
            <span class="meta-value">
              <el-tag v-if="currentNotification.status === 'published'" type="success">
                已发布
              </el-tag>
              <el-tag v-else type="warning">草稿</el-tag>
            </span>
          </div>
          <div class="meta-item">
            <span class="meta-label">浏览数：</span>
            <span class="meta-value">{{ currentNotification.views || 0 }}</span>
          </div>
        </div>
        <div class="detail-content" v-html="currentNotification.content"></div>
      </div>

      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 发布/编辑弹窗 -->
    <el-dialog
      v-model="formVisible"
      :title="formTitle"
      width="1000px"
      :close-on-click-modal="false"
      destroy-on-close
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
        label-position="top"
      >
        <el-form-item label="公告标题" prop="title">
          <el-input
            v-model="formData.title"
            placeholder="请输入公告标题"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="发布状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio label="已发布">直接发布</el-radio>
            <el-radio label="未发布">存为草稿</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="公告内容" prop="content">
          <div class="editor-wrapper">
            <Toolbar
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              mode="default"
              class="editor-toolbar"
            />
            <Editor
              v-model="formData.content"
              :defaultConfig="editorConfig"
              mode="default"
              class="editor-content"
              @on-created="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading"> 确定 </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onBeforeUnmount, shallowRef } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  Plus,
  Search,
  View,
  Edit,
  Delete,
  Promotion,
  RefreshLeft,
  CircleCheck
} from '@element-plus/icons-vue'
import { NotificationApi, type NotificationVO, type NotificationForm } from '@/api/biz/notification'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import type { IDomEditor, IEditorConfig, IToolbarConfig } from '@wangeditor/editor'

// 加载状态
const loading = ref(false)
const submitLoading = ref(false)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  status: '',
  keyword: ''
})

// 表格数据
const tableData = ref<NotificationVO[]>([])
const total = ref(0)

// 详情弹窗
const detailVisible = ref(false)
const currentNotification = ref<NotificationVO>({
  id: '',
  title: '',
  content: '',
  publishTime: '',
  status: 'draft',
  views: 0
})

// 表单相关
const formVisible = ref(false)
const formTitle = ref('发布公告')
const formRef = ref<FormInstance>()
const formData = reactive<NotificationForm>({
  id: undefined,
  title: '',
  content: '',
  status: '已发布'
})

const formRules = reactive<FormRules>({
  title: [
    { required: true, message: '请输入公告标题', trigger: 'blur' },
    { min: 5, max: 200, message: '标题长度在 5 到 200 个字符', trigger: 'blur' }
  ],
  content: [{ required: true, message: '请输入公告内容', trigger: 'blur' }],
  status: [{ required: true, message: '请选择发布状态', trigger: 'change' }]
})

// 富文本编辑器
const editorRef = shallowRef<IDomEditor>()

const toolbarConfig: Partial<IToolbarConfig> = {
  excludeKeys: ['group-video', 'fullScreen']
}

const editorConfig: Partial<IEditorConfig> = {
  placeholder: '请输入公告内容...',
  MENU_CONF: {}
}

const handleCreated = (editor: IDomEditor) => {
  editorRef.value = editor
}

// 获取列表数据
const getList = async () => {
  loading.value = true
  try {
    const res = await NotificationApi.getNotificationList(queryParams)
    tableData.value = res.list || []
    total.value = res.total || 0
  } catch (error) {
    console.error('获取通知列表失败:', error)
    ElMessage.error('获取通知列表失败')
  } finally {
    loading.value = false
  }
}

// 查询
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

// 查看详情
const handleView = async (row: NotificationVO) => {
  try {
    const res = await NotificationApi.getNotificationById(row.id)
    currentNotification.value = res
    detailVisible.value = true

    // 增加浏览量
    if (res.status === 'published') {
      await NotificationApi.incrementViews(row.id)
    }
  } catch (error) {
    console.error('获取通知详情失败:', error)
    ElMessage.error('获取通知详情失败')
  }
}

// 新建
const handleCreate = () => {
  formTitle.value = '发布公告'
  formVisible.value = true
}

// 编辑
const handleEdit = async (row: NotificationVO) => {
  try {
    const res = await NotificationApi.getNotificationById(row.id)
    formTitle.value = '修改公告'
    formData.id = res.id
    formData.title = res.title
    formData.content = res.content
    formData.status = res.status
    formVisible.value = true
  } catch (error) {
    console.error('获取通知详情失败:', error)
    ElMessage.error('获取通知详情失败')
  }
}

// 发布草稿
const handlePublish = async (row: NotificationVO) => {
  try {
    await ElMessageBox.confirm('确定要发布这条草稿吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const data = {id: row.id, status: '已发布'}
    await NotificationApi.publishDraft(data)
    ElMessage.success('发布成功')
    getList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('发布失败:', error)
      ElMessage.error('发布失败')
    }
  }
}

// 撤回
const handleRevoke = async (row: NotificationVO) => {
  try {
    await ElMessageBox.confirm('确定要撤回这条公告吗？撤回后公告将不再对外显示。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const data = {id: row.id, status: '已撤回'}
    await NotificationApi.revokeNotification(data)
    ElMessage.success('撤回成功')
    getList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('撤回失败:', error)
      ElMessage.error('撤回失败')
    }
  }
}

// 删除
const handleDelete = async (row: NotificationVO) => {
  try {
    await ElMessageBox.confirm('确定要删除这条公告吗？此操作不可恢复。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'error'
    })

    await NotificationApi.deleteNotification(row.id)
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitLoading.value = true
    try {
      if (formData.id) {
        await NotificationApi.updateNotification(formData)
        ElMessage.success('修改成功')
      } else {
        await NotificationApi.createNotification(formData)
        ElMessage.success(formData.status === '已发布' ? '发布成功' : '保存草稿成功')
      }

      formVisible.value = false
      getList()
    } catch (error) {
      console.error('提交失败:', error)
      ElMessage.error('提交失败')
    } finally {
      submitLoading.value = false
    }
  })
}

// 重置表单
const resetForm = () => {
  formData.id = undefined
  formData.title = ''
  formData.content = ''
  formData.status = ''
  formRef.value?.resetFields()
}

// 组件销毁前，销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor) {
    editor.destroy()
  }
})

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
// 响应式
@media (width <= 768px) {
  .notification-container {
    padding: 12px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;

    .page-header-right {
      width: 100%;

      .el-button {
        width: 100%;
      }
    }
  }

  .filter-bar {
    flex-direction: column;
    gap: 12px;

    .filter-left,
    .filter-right {
      width: 100%;
      flex-direction: column;

      .el-select,
      .el-input {
        width: 100% !important;
      }
    }
  }

  .detail-meta {
    grid-template-columns: 1fr !important;
  }
}

.notification-container {
  min-height: 100%;
  padding: 20px;
  background: var(--app-content-bg-color);
}

.page-header {
  display: flex;
  padding: 20px;
  margin-bottom: 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgb(0 0 0 / 6%);
  justify-content: space-between;
  align-items: center;

  .page-header-left {
    .page-title {
      margin-bottom: 6px;
      font-size: 22px;
      font-weight: 600;
      color: #165dff;
    }

    .page-subtitle {
      font-size: 13px;
      color: #64748b;
    }
  }
}

.data-card {
  overflow: hidden;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgb(0 0 0 / 6%);
}

.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #e5e7eb;

  .filter-left {
    display: flex;
    gap: 12px;
  }

  .filter-right {
    display: flex;
    gap: 12px;
  }
}

.title-link {
  font-weight: 500;
  color: #165dff;
  cursor: pointer;

  &:hover {
    text-decoration: underline;
  }
}

.view-count {
  font-weight: 600;
  color: #475569;
}

.pagination-box {
  display: flex;
  padding: 20px;
  justify-content: center;
}

// 详情样式
.notification-detail {
  .detail-meta {
    display: grid;
    padding: 16px;
    margin-bottom: 20px;
    background: #f8fafc;
    border-radius: 8px;
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;

    .meta-item {
      display: flex;
      align-items: center;
      gap: 8px;

      .meta-label {
        font-size: 13px;
        font-weight: 600;
        color: #475569;
      }

      .meta-value {
        font-size: 13px;
        color: #1e293b;
      }
    }
  }

  .detail-content {
    max-height: 500px;
    min-height: 300px;
    padding: 16px;
    overflow-y: auto;
    font-size: 14px;
    line-height: 1.8;
    color: #1e293b;
    border: 1px solid #e5e7eb;
    border-radius: 8px;

    :deep(h4) {
      margin: 20px 0 10px;
      font-size: 16px;
      font-weight: 600;
      color: #1e293b;
    }

    :deep(p) {
      margin: 10px 0;
    }

    :deep(img) {
      height: auto;
      max-width: 100%;
    }
  }
}

// 富文本编辑器样式
.editor-wrapper {
  overflow: hidden;
  border: 1px solid #e5e7eb;
  border-radius: 8px;

  .editor-toolbar {
    border-bottom: 1px solid #e5e7eb;
  }

  .editor-content {
    height: 400px;
    overflow-y: auto;

    :deep(.w-e-text-container) {
      background-color: #fff;
    }

    :deep(.w-e-text-placeholder) {
      top: 10px;
      left: 10px;
    }
  }
}
</style>
