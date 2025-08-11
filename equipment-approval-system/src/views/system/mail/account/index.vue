<template>
  <doc-alert title="邮件配置" url="https://doc.iocoder.cn/mail" />

  <ContentWrap>
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="68px"
    >
      <el-form-item label="邮箱" prop="mail">
        <el-input
          v-model="queryParams.mail"
          class="!w-240px"
          clearable
          placeholder="请输入邮箱"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名" prop="username">
        <el-input
          v-model="queryParams.username"
          class="!w-240px"
          clearable
          placeholder="请输入用户名"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
          end-placeholder="结束日期"
          start-placeholder="开始日期"
          type="daterange"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon class="mr-5px" icon="ep:search" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon class="mr-5px" icon="ep:refresh" /> 重置</el-button>
        <el-button
          v-hasPermi="['system:mail-account:create']"
          plain
          type="primary"
          @click="openForm('create')"
        >
          <Icon class="mr-5px" icon="ep:plus" /> 新增</el-button
        >
        <el-button
          v-hasPermi="['system:mail-account:delete']"
          :disabled="checkedIds.length === 0"
          plain
          type="danger"
          @click="handleDeleteBatch"
        >
          <Icon class="mr-5px" icon="ep:delete" /> 批量删除
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" @selection-change="handleRowCheckboxChange">
      <el-table-column type="selection" width="55" />
      <el-table-column align="center" label="编号" prop="id" />
      <el-table-column align="center" label="邮箱" prop="mail" />
      <el-table-column align="center" label="用户名" prop="username" />
      <el-table-column align="center" label="SMTP 服务器域名" prop="host" />
      <el-table-column align="center" label="SMTP 服务器端口" prop="port" />
      <el-table-column align="center" label="是否开启 SSL" prop="sslEnable">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.sslEnable" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否开启 STARTTLS" prop="starttlsEnable">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.starttlsEnable" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="创建时间"
        prop="createTime"
        width="180"
      />
      <el-table-column align="center" label="操作">
        <template #default="scope">
          <el-button
            v-hasPermi="['system:mail-account:update']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            编辑
          </el-button>
          <el-button
            v-hasPermi="['system:mail-account:delete']"
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
      v-model:limit="queryParams.pageSize"
      v-model:page="queryParams.pageNo"
      :total="total"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <MailAccountForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as MailAccountApi from '@/api/system/mail/account'
import MailAccountForm from './MailAccountForm.vue'

defineOptions({ name: 'SystemMailAccount' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const loading = ref(false) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryFormRef = ref() // 搜索的表单
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  mail: '',
  username: '',
  createTime: []
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await MailAccountApi.getMailAccountPage(queryParams)
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
    await MailAccountApi.deleteMailAccount(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除按钮操作 */
const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (rows: MailAccountApi.MailAccountVO[]) => {
  checkedIds.value = rows.map((row) => row.id)
}

const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起批量删除
    await MailAccountApi.deleteMailAccountList(checkedIds.value)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
