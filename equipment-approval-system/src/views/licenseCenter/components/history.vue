<template>
  <div class="history-page">
    <div class="title-row">
      <Icon icon="lucide:history" />
      <span>操作记录</span>
    </div>
    <div class="history-list">
      <template v-if="historyList.length">
        <div v-for="item in historyList" :key="item?.time" class="history-item">
          <div class="containr-b-x">
            <div class="time-row">{{ item?.time }}</div>
            <div class="content-row">{{ item?.content }}</div>
            <div class="other-msg-row">
              <div class="operator">操作人: {{ item.operator }}</div>
              <div class="line"></div>
              <div class="remark">备注: {{ item.remark }}</div>
            </div>
          </div>
          <div class="step-line"></div>
        </div>
      </template>
      <el-empty v-else :image-size="200" description="暂无数据" />
    </div>
  </div>
</template>

<script setup lang="ts" name="HistoryProcess">
let historyList = ref([
  {
    time: '2023-08-01 10:00:00',
    content: '副本证书发放',
    operator: '张三',
    remark: '操作类型文本'
  },
  {
    time: '2023-08-02 10:00:00',
    content: '副本证书发放',
    operator: '张三',
    remark: '操作类型文本'
  }
])
</script>

<style lang="scss" scoped>
.history-page {
  padding: 16px;
  .title-row {
    display: flex;
    align-items: center;
    padding: 5px 0px;
    border-bottom: 2px solid rgba(22, 93, 255, 0.1);
    font-size: 16px;
    font-weight: 600;
    color: #165dff;
    margin-bottom: 12px;
    &:deep(.el-icon) {
      font-size: 22px !important;
      span {
        font-size: 22px !important;
      }

      margin-right: 6px;
    }
  }
  .history-list {
    .history-item {
      padding: 14px 10px 14px 40px;
      position: relative;
      .containr-b-x {
        border-radius: 10px;
        border: 1px solid #d3d3d3;
        padding: 8px 14px;
        background-color: #f7f7f7;
        .time-row {
          font-size: 18px;
          color: #165dff;
          font-weight: bold;
        }
        .content-row {
          font-size: 16px;
          line-height: 24px;
          color: #333;
        }
        .other-msg-row {
          font-size: 14px;
          line-height: 20px;
          color: #666;
          display: flex;
          align-items: center;
          .operator {
            flex-shrink: 0;
          }
          .line {
            width: 1px;
            height: 14px;
            background-color: #666;
            margin: 0 10px;
            flex-shrink: 0;
          }
        }
      }
      .step-line {
        position: absolute;
        top: 0;
        left: 20px;
        width: 2px;
        transform: translateX(-50%);
        height: 100%;
        background-color: #1a8dec;
      }
      &::after {
        content: '';
        position: absolute;
        top: 50%;
        left: 20px;
        transform: translate(-50%, -50%);
        width: 14px;
        height: 14px;
        background-color: #1ab7ec;
        border: 2px solid #fff;
        border-radius: 50%;
        box-shadow: 0 0 0 2px #65b7fbd9;
      }
    }
  }
}
</style>
