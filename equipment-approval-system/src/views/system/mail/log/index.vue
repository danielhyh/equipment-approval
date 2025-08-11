<template>
  <doc-alert title="邮件配置" url="https://doc.iocoder.cn/mail" />

  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="100px"
    >
      <el-form-item label="接收邮箱" prop="toMail">
        <el-input
          v-model="queryParams.toMail"
          class="!w-240px"
          clearable
          placeholder="请输入接收邮箱"
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
      <el-form-item label="模板编号" prop="templateId">
        <el-input
          v-model="queryParams.templateId"
          class="!w-240px"
          clearable
          placeholder="请输入模板编号"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发送状态" prop="sendStatus">
        <el-select
          v-model="queryParams.sendStatus"
          class="!w-240px"
          clearable
          placeholder="请选择发送状态"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_MAIL_SEND_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用户编号" prop="userId">
        <el-input
          v-model="queryParams.userId"
          class="!w-240px"
          clearable
          placeholder="请输入用户编号"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户类型" prop="userType">
        <el-select
          v-model="queryParams.userType"
          class="!w-240px"
          clearable
          placeholder="请选择用户类型"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.USER_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="发送时间" prop="sendTime">
        <el-date-picker
          v-model="queryParams.sendTime"
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
          v-hasPermi="['system:mail-log:export']"
          :loading="exportLoading"
          plain
          type="success"
          @click="handleExport"
        >
          <Icon class="mr-5px" icon="ep:download" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column align="center" label="编号" prop="id" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="发送时间"
        prop="sendTime"
        width="180"
      />
      <el-table-column align="center" label="接收邮箱" prop="toMail" width="200">
        <template #default="scope">
          <div>{{ scope.row.toMail }}</div>
          <div v-if="scope.row.userType && scope.row.userId">
            <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType" />
            {{ '(' + scope.row.userId + ')' }}
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="邮件标题" prop="templateTitle" width="200" />
      <el-table-column align="center" label="发送状态" width="120">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_MAIL_SEND_STATUS" :value="scope.row.sendStatus" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="邮箱账号" width="200">
        <template #default="scope">
          {{ getAccountMail(scope.row.accountId) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="模板编号" prop="templateId" />
      <el-table-column align="center" class-name="fixed-width" fixed="right" label="操作">
        <template #default="scope">
          <el-button
            v-hasPermi="['system:mail-log:query']"
            link
            type="primary"
            @click="openDetail(scope.row)"
          >
            详情
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

  <!-- 表单弹窗：详情 -->
  <MailLogDetail ref="detailRef" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter, formatDate } from '@/utils/formatTime'
import download from '@/utils/download'
import * as MailAccountApi from '@/api/system/mail/account'
import * as MailLogApi from '@/api/system/mail/log'
import MailLogDetail from './MailLogDetail.vue'

defineOptions({ name: 'SystemMailLog' })

const message = useMessage() // 消息弹窗

const loading = ref(false) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryFormRef = ref() // 搜索的表单
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  toMail: '',
  accountId: null,
  templateId: null,
  sendStatus: null,
  userId: null,
  userType: null,
  sendTime: []
})
const exportLoading = ref(false) // 导出的加载中
const accountList = ref([]) // 邮箱账号列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await MailLogApi.getMailLogPage(queryParams)
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

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await MailLogApi.exportMailLog(queryParams)
    download.excel(data, '邮件日志.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 详情操作 */
const detailRef = ref()
const openDetail = (data: MailLogApi.MailLogVO) => {
  detailRef.value.open(data)
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
