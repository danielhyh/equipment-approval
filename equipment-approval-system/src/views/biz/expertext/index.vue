<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button type="primary" plain @click="openForm('create')">
          <Icon icon="ep:plus" class="mr-5px" /> 新增
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
      <el-table-column label="专家姓名" align="center" prop="name" />
      <el-table-column label="性别" align="center" prop="gender">
        <template #default="scope">
          {{ getDictLabel(DICT_TYPE.SYSTEM_USER_SEX, scope.row.gender) }}
        </template>
      </el-table-column>
      <el-table-column label="工作单位" align="center" prop="unit" />
      <el-table-column label="职务" align="center" prop="position" />
      <el-table-column label="职称" align="center" prop="title" />
      <el-table-column label="专业领域" align="center" prop="specialty" />
      <el-table-column label="联系电话" align="center" prop="phone" />
      <el-table-column label="已参与评审数量" align="center" prop="reviewNums" />
      <el-table-column label="操作" align="center" min-width="120px">
        <template #default="scope">
<!--          <el-button link type="primary" @click="openForm('update', scope.row.id)">-->
<!--            编辑-->
<!--          </el-button>-->
          <el-button link type="primary" @click="openViewForm(scope.row)"> 查看 </el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)"> 删除 </el-button>
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
  <ExpertExtForm ref="formRef" @success="getList" />
  <Dialog v-model="dialogVisible" title="专家详情" width="1200px">
    <el-form :model="expertDetail" label-width="100px" label-position="top">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="专家姓名">
            <el-input v-model="expertDetail.name" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别">
            <el-input
              :value="expertDetail.gender === 1 ? '男' : expertDetail.gender === 2 ? '女' : ''"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工作单位">
            <el-input v-model="expertDetail.unit" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职务">
            <el-input v-model="expertDetail.position" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职称">
            <el-input v-model="expertDetail.title" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="专业领域">
            <el-input v-model="expertDetail.specialty" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话">
            <el-input v-model="expertDetail.phone" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="参与评审项目">
            <el-table :data="reviewRecords" style="width: 100%" :show-overflow-tooltip="true">
              <el-table-column prop="expertReviewTime" label="评审时间" align="center" />
              <el-table-column prop="appStatus" label="申请状态" align="center">
                <template #default="scope">
                  <span
                    class="status-label"
                         :class="statusMap(scope.row.appStatus).colorType">
                    {{ statusMap(scope.row.appStatus).label }}
                  </span>

                </template>
              </el-table-column>
              <el-table-column prop="licenseDeviceName" label="设备名称" align="center" />
              <el-table-column prop="institutionName" label="机构名称" align="center" />
              <el-table-column prop="appNo" label="申请编号" align="center" />
            </el-table>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </Dialog>
</template>

<script setup lang="ts">
import { ExpertExtApi, ExpertExt } from '@/api/biz/expertext'
import { DICT_TYPE, type DictDataType, getDictLabel, getDictOptions } from '@/utils/dict'
import ExpertExtForm from './ExpertExtForm.vue'
import { computed } from 'vue'

/** 专家扩展信息 列表 */
defineOptions({ name: 'ExpertExt' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ExpertExt[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  userId: undefined,
  name: undefined,
  gender: undefined,
  age: undefined,
  unit: undefined,
  title: undefined,
  specialty: undefined,
  phone: undefined,
  email: undefined,
  qualificationCert: undefined,
  status: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ExpertExtApi.getExpertExtPage(queryParams)
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
    await ExpertExtApi.deleteExpertExt(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}
const dialogVisible = ref(false)
const expertDetail = ref({})
const reviewRecords = ref([])
const openViewForm = async (val) => {
  dialogVisible.value = true
  expertDetail.value = { ...expertDetail.value, ...val } // 接收 scope.row 数据
  reviewRecords.value = await ExpertExtApi.expertReviewRecord(val.id)
}
const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: ExpertExt[]) => {
  checkedIds.value = records.map((item) => item.id)
}
let dictStatusList = computed<DictDataType[]>(() => getDictOptions('biz_app_status'))
const statusMap = (status: number) => {
  return (
    dictStatusList.value.find((item) => item.value === String(status)) || {
      label: '状态异常',
      colorType: 'danger'
    }
  )
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
<style lang="scss" scoped>
.status-label {
  --color: rgb(37, 84, 237);
  --color-rab: 37, 84, 237;
  --color-opacity: 0.1;
  display: inline-block;
  padding: 2px 14px;
  border-radius: 12px;
  font-size: 12px;
  line-height: 18px;
  color: var(--color);
  border: 1px solid var(--color);
  background-color: rgba(var(--color-rab), var(--color-opacity));
  user-select: none;
  &.info {
    --color: rgb(249, 130, 11);
    --color-rab: 249, 130, 11;
  }
  &.primary {
    --color: #409eff;
    --color-rab: 64, 158, 255;
  }
  &.success {
    --color: #67c23a;
    --color-rab: 103, 194, 58;
  }
  &.danger {
    --color: #f56c6c;
    --color-rab: 245, 108, 108;
  }
  &.warning {
    --color: #e6a23c;
    --color-rab: 230, 162, 60;
  }
  &.primary {
    --color: #409eff;
    --color-rab: 64, 158, 255;
  }
}
</style>
