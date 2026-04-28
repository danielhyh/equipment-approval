<template>
  <div>
    <div class="title-row" style="margin-bottom: 10px">
      <div class="left">
        <div class="title">
          <Icon icon="mdi:message-text-clock" color="#4245df" :size="18" style="margin-right: 5px" />
          配置分布情况
        </div>
      </div>
    </div>
    <el-table
      :data="tableData"
      style="width: 100%"
      border
      stripe
      size="small"
      v-loading="loading"
      :style="{ 'border-radius': '10px' }"
      :header-cell-style="{ background: '#007bff', color: '#fff', textAlign: 'center' }"
    >
      <el-table-column prop="deviceName" label="设备品目" align="center" fixed="left" min-width="200" />
      <el-table-column prop="total" label="已执行数量" align="center" min-width="100" />
      <!-- 按办医主体分 -->
      <el-table-column label="按办医主体分" align="center">
        <el-table-column prop="publicCount" label="公立" align="center" min-width="70" />
        <el-table-column prop="privateCount" label="民营" align="center" min-width="70" />
      </el-table-column>
      <!-- 按管理级次分（公立医院） -->
      <el-table-column label="按管理级次分（公立医院）" align="center">
        <el-table-column prop="centralCount" label="中央级" align="center" min-width="70" />
        <el-table-column prop="provinceCount" label="省级" align="center" min-width="70" />
        <el-table-column prop="cityCount" label="市级" align="center" min-width="70" />
        <el-table-column prop="countyCount" label="县级" align="center" min-width="70" />
      </el-table-column>
      <!-- 按医院性质分 -->
      <el-table-column label="按医院性质分" align="center">
        <el-table-column prop="generalCount" label="综合" align="center" min-width="70" />
        <el-table-column prop="specialistCount" label="专科" align="center" min-width="70" />
      </el-table-column>
      <!-- 按医院等级分 -->
      <el-table-column label="按医院等级分" align="center">
        <el-table-column prop="level3Count" label="三级" align="center" min-width="70" />
        <el-table-column prop="level2Count" label="二级" align="center" min-width="70" />
        <el-table-column prop="unratedCount" label="未定级" align="center" min-width="80" />
      </el-table-column>
      <!-- 验收情况 -->
      <el-table-column label="验收情况" align="center">
        <el-table-column prop="acceptedCount" label="已验收" align="center" min-width="70" />
        <el-table-column prop="notAcceptedCount" label="未验收" align="center" min-width="70" />
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { AnalysisApi } from '@/api/biz/analysis'

let loading = ref(false)
let tableData = ref([])

const getData = async () => {
  try {
    loading.value = true
    const response = await AnalysisApi.getConfigDistributionV2()
    tableData.value = response || []
  } catch (err) {
    console.log(err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getData()
})
</script>

<style lang="scss" scoped>
.title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .left {
    .title {
      display: flex;
      align-items: center;
      line-height: 20px;
      font-size: 16px;
      font-weight: bold;
      color: #333;
      border-left: 4px solid #4245df;
      padding-left: 10px;
    }
  }
}
</style>