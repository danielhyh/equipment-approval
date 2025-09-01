<template>
  <div class="page-content">
    <div class="title-row">
      <Icon icon="svg-icon:user-graduatel" />
      <span>专家审核</span>
    </div>
    <div class="content-row">
      <!-- 审核结果 -->
      <div class="result-row">
        <el-tag :type="statusValue.colorType" round>
          <Icon :icon="statusIcon" :size="20" />
          <span class="txt">{{ statusValue.label }}</span>
        </el-tag>
      </div>
      <!-- 审核内容 -->
      <div class="remark-row">{{ getReviewDetails.expertReviewOpinion }}</div>
      <!-- 审核人 -->
      <div class="approver-row">
        <div class="col">
          <span>审核时间：</span>
          <span>{{ getReviewDetails.expertReviewTime || '--' }}</span>
        </div>
        <div class="line"></div>
        <div class="col">
          <span>审核人：</span>
          <span>{{ getReviewDetails.expertList || '--' }}</span>
        </div>
      </div>
    </div>

    <div class="title-row">
      <Icon icon="mingcute:certificate-fill" />
      <span>许可证信息</span>
    </div>

    <div class="license__form_box">
      <el-form disabled :model="getReviewDetails" label-position="top">
        <el-form-item label="许可证编号" prop="licenseCode">
          <el-input v-model="getReviewDetails.licenseNo" placeholder="请输入许可证编号" />
        </el-form-item>
        <el-form-item label="生成日期" prop="generateTime">
          <el-input v-model="getReviewDetails.licenseGenerateDate" placeholder="请输入生成日期" />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { getDictOptions } from '@/utils/dict'
import type { DictDataType } from '@/utils/dict'
import { useApplicationDataStore } from '@/store/applicationData'

const useAppData = useApplicationDataStore()
const getReviewDetails = computed(() => {
  return useAppData.getReviewDetails
})

let statusOptions = computed<DictDataType[]>(() => getDictOptions('biz_review_result'))
let statusValue = computed(() => {
  return (
    statusOptions.value.find(
      (item) => item.value === getReviewDetails.value.expertReviewResult
    ) || {
      label: '未知',
      value: '3',
      colorType: 'primary'
    }
  )
})
let statusIcon = computed(() => {
  switch (statusValue.value.value) {
    case '0':
      return 'mingcute:close-line'
    case '1':
      return 'mingcute:check-line'
    case '2':
      return 'ri:draft-line'
    default:
      return 'mingcute:close-line'
  }
})
</script>

<style lang="scss" scoped>
.page-content {
  padding: 16px;
  .title-row {
    display: flex;
    align-items: center;
    padding: 5px 0px;
    border-bottom: 2px solid rgba(22, 93, 255, 0.1);
    font-size: 16px;
    font-weight: 600;
    color: #165dff;
    &:deep(.el-icon) {
      font-size: 22px !important;
      span {
        font-size: 22px !important;
      }

      margin-right: 6px;
    }
  }
  .content-row {
    .result-row {
      margin: 12px 0px;
      .el-tag {
        user-select: none;
        .el-tag__content {
          display: inline-flex;
          align-items: center;
          .el-icon {
            vertical-align: bottom;
            margin-right: 4px;
          }
          .txt {
            font-size: 14px;
          }
        }
      }
    }
    .remark-row {
      margin-bottom: 12px;
      padding: 6px 12px;
      background-color: #f8f8f8bd;
      border-radius: 6px;
      border: 1px solid #e4e7ed;
      min-height: 120px;
      font-size: 14px;
      line-height: 24px;
    }
    .approver-row {
      display: flex;
      align-items: center;
      margin-bottom: 12px;
      .col {
        display: flex;
        align-items: center;
        span {
          font-size: 14px;
          color: #606266;
        }
      }
      .line {
        width: 1px;
        height: 14px;
        background-color: #e4e7ed;
        margin: 0px 12px;
      }
    }
  }
  .license__form_box {
    margin-top: 14px;
    &:deep(.el-form) {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      grid-gap: 20px;
      .el-form-item {
        margin: 0;
        .el-input,
        .el-textarea {
          .el-input__inner,
          .el-textarea__inner {
            --el-disabled-text-color: #000;
          }
          .el-input__inner::placeholder {
            -webkit-text-fill-color: #999;
          }
        }
        .el-form-item__label {
          font-weight: bold;
        }
      }
    }
  }
}
</style>
