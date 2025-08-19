<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="80px"
    >
      <el-form-item label="状态" prop="appStatus">
        <el-select v-model="queryParams.appStatus" placeholder="Select" style="width: 150px">
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
        <el-form-item label="设备类型" prop="licenseDeviceName">
          <el-select v-model="queryParams.licenseDeviceName" placeholder="Select" style="width: 240px">
            <el-option
              v-for="item in deviceOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
      </el-form-item>
      <el-form-item label="" prop="deptOrDeviceName">
        <el-input v-model="queryParams.deptOrDeviceName" placeholder="搜索申请单位或设备名称..."/>
      </el-form-item>

        <el-form-item>
          <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
          <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
          <el-button
            type="primary"
            plain
            @click="openForm('create')"
            v-hasPermi="['biz:application:create']"
          >
            <Icon icon="ep:plus" class="mr-5px" /> 新增
          </el-button>
          <el-button
            type="success"
            plain
            @click="handleExport"
            :loading="exportLoading"
            v-hasPermi="['biz:application:export']"
          >
            <Icon icon="ep:download" class="mr-5px" /> 导出
          </el-button>
          <el-button
              type="danger"
              plain
              :disabled="isEmpty(checkedIds)"
              @click="handleDeleteBatch"
              v-hasPermi="['biz:application:delete']"
          >
            <Icon icon="ep:delete" class="mr-5px" /> 批量删除
          </el-button>
        </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table
        row-key="id"
        v-loading="loading"
        :data="list"
        :stripe="true"
        :show-overflow-tooltip="true"
        @selection-change="handleRowCheckboxChange"
    >
    <el-table-column type="selection" width="55" />
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column label="申请单位" align="center" prop="institutionName" />
      <el-table-column label="设备名称" align="center" prop="licenseDeviceName" />
      <el-table-column label="阶梯配置机型" align="center" prop="ladderConfigModel" />
      <el-table-column
        label="申请时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="状态" align="center" prop="appStatus">
        <template #default="scope">
          {{ getStatusLabel(scope.row.appStatus) }}
        </template>
      </el-table-column>
      <el-table-column label="剩余时间" align="center" :formatter="dateFormatter" prop="deadline" />
      <el-table-column label="操作" align="center" min-width="120px">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"

          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"

          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <ApplicationForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { ApplicationApi, Application } from '@/api/biz/application'
import ApplicationForm from './ApplicationForm.vue'

/** 申请 列表 */
defineOptions({ name: 'Application' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const statusOptions = [
  { value: 1, label: '待初审' },
  { value: 2, label: '初审通过' },
  { value: 3, label: '初审不通过' },
  { value: 4, label: '待专家审核' },
  { value: 5, label: '专家审核通过' },
  { value: 6, label: '专家审核不通过' },
  { value: 7, label: '副本待审批' },
  { value: 8, label: '验收资料待审批' },
  { value: 9, label: '已完成' }
]
const deviceOptions = [
  {
    value: "正电子发射型磁共振成像系统",
    label: "正电子发射型磁共振成像系统"
  },
  {
    value: "X线正电子发射断层扫描仪",
    label: "X线正电子发射断层扫描仪"
  },
  {
    value: "腹腔内窥镜手术系统",
    label: "腹腔内窥镜手术系统"
  },
  {
    value: "常规放射治疗类设备",
    label: "常规放射治疗类设备"
  },
  {
    value: "首次配置的大型医疗器械",
    label: "首次配置的大型医疗器械"
  }
]

const getStatusLabel =  (value: number) => {
  const statusMap = {
    1: '待初审',
    2: '初审通过',
    3: '初审不通过',
    4: '待专家审核',
    5: '专家审核通过',
    6: '专家审核不通过',
    7: '副本待审批',
    8: '验收资料待审批',
    9: '已完成'
  }
  return statusMap[value] || '未知状态';
}
const loading = ref(true) // 列表的加载中
const list = ref<Application[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  appStatus: undefined,
  licenseDeviceName: undefined,
  deptOrDeviceName: undefined,
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ApplicationApi.getApplicationPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ApplicationApi.deleteApplication(id)
    message.success(t('common.delSuccess'))
    // currentRow.value = {}
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除申请 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await ApplicationApi.deleteApplicationList(checkedIds.value);
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: Application[]) => {
  checkedIds.value = records.map((item) => item.id);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ApplicationApi.exportApplication(queryParams)
    download.excel(data, '申请.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
