<template>
  <!-- 列表 -->
  <ContentWrap>
    <el-table
      v-loading="loading"
      :data="list"
      :show-overflow-tooltip="true"
      :stripe="true"
      row-key="id"
    >
      <el-table-column align="center" label="编号" prop="id" />
      <el-table-column align="center" label="名字" prop="name" />
      <el-table-column align="center" label="班主任" prop="teacher" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="创建时间"
        prop="createTime"
        width="180px"
      />
    </el-table>
  </ContentWrap>
</template>
<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import { Demo03StudentApi } from '@/api/infra/demo/demo03/inner'

const props = defineProps<{
  studentId?: number // 学生编号（主表的关联字段）
}>()
const loading = ref(false) // 列表的加载中
const list = ref([]) // 列表的数据

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await Demo03StudentApi.getDemo03GradeByStudentId(props.studentId)
    if (!data) {
      return
    }
    list.value.push(data)
  } finally {
    loading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
