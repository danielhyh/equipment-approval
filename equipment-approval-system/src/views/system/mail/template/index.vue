<template>
  <doc-alert title="邮件配置" url="https://doc.iocoder.cn/mail" />

  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="150px"
    >
      <el-form-item label="模板编码" prop="code">
        <el-input
          v-model="queryParams.code"
          class="!w-240px"
          clearable
          placeholder="请输入模板编码"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模板名称" prop="name">
        <el-input
          v-model="queryParams.name"
          class="!w-240px"
          clearable
          placeholder="请输入模板名称"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邮箱账号" prop="accountId">
        <el-select
          v-model="queryParams.accountId"
          class="!w-240px"
          clearable
          placeholder="请选择邮箱账号"
        >
          <el-option
            v-for="account in accountList"
            :key="account.id"
            :label="account.mail"
            :value="account.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="开启状态" prop="status">
        <el-select
          v-model="queryParams.status"
          class="!w-240px"
          clearable
          placeholder="请选择开启状态"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          class="!w-240px"
          end-placeholder="结束日期"
          start-placeholder="开始日期"
          style="width: 240px"
          type="daterange"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon class="mr-5px" icon="ep:search" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon class="mr-5px" icon="ep:refresh" /> 重置</el-button>
        <el-button
          v-hasPermi="['system:mail-template:create']"
          plain
          type="primary"
          @click="openForm('create')"
        >
          <Icon class="mr-5px" icon="ep:plus" />新增
        </el-button>
        <el-button
          v-hasPermi="['system:mail-template:delete']"
          :disabled="checkedIds.length === 0"
          plain
          type="danger"
          @click="handleDeleteBatch"
        >
          <Icon class="mr-5px" icon="ep:delete" />批量删除
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" @selection-change="handleRowCheckboxChange">
      <el-table-column type="selection" width="55" />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="模板编码"
        prop="code"
        width="120"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="模板名称"
        prop="name"
        width="120"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="模板标题"
        prop="title"
        width="150"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="模板内容"
        prop="content"
        width="200"
      />
      <el-table-column align="center" label="邮箱账号" prop="accountId" width="200">
        <template #default="scope">
          {{ getAccountMail(scope.row.accountId) }}
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="发送人名称"
        prop="nickname"
        width="120"
      />
      <el-table-column align="center" label="开启状态" prop="status" width="80">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="创建时间"
        prop="createTime"
        width="180"
      />
      <el-table-column align="center" fixed="right" label="操作" width="210">
        <template #default="scope">
          <el-button
            v-hasPermi="['system:mail-template:update']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            修改
          </el-button>
          <el-button
            v-hasPermi="['system:mail-template:send-mail']"
            link
            type="primary"
            @click="openSendForm(scope.row.id)"
          >
            测试
          </el-button>
          <el-button
            v-hasPermi="['system:mail-template:delete']"
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
  <MailTemplateForm ref="formRef" @success="getList" />
  <!-- 表单弹窗：测试发送 -->
  <MailTemplateSendForm ref="sendFormRef" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as MailTemplateApi from '@/api/system/mail/template'
import * as MailAccountApi from '@/api/system/mail/account'
import MailTemplateForm from './MailTemplateForm.vue'
import MailTemplateSendForm from './MailTemplateSendForm.vue'

defineOptions({ name: 'SystemMailTemplate' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(false) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryFormRef = ref() // 搜索的表单
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  code: '',
  name: '',
  accountId: undefined,
  status: undefined,
  createTime: []
})
const accountList = ref<MailAccountApi.MailAccountVO[]>([]) // 邮箱账号列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await MailTemplateApi.getMailTemplatePage(queryParams)
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

/** 发送邮件按钮 */
const sendFormRef = ref()
const openSendForm = (id: number) => {
  sendFormRef.value.open(id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await MailTemplateApi.deleteMailTemplate(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除按钮操作 */
const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (rows: MailTemplateApi.MailTemplateVO[]) => {
  checkedIds.value = rows.map((row) => row.id!)
}

const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起批量删除
    await MailTemplateApi.deleteMailTemplateList(checkedIds.value)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 获取邮箱账号名称 */
const getAccountMail = (accountId: number) => {
  const account = accountList.value.find((account) => account.id === accountId)
  return account?.mail || ''
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 加载邮箱账号列表
  accountList.value = await MailAccountApi.getSimpleMailAccountList()
})
</script>
