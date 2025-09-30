<template>
  <div>
    <div class="title-row">
      <div class="left">
        <div class="title">
          <Icon
            icon="material-symbols:map-pin-review-rounded"
            color="#4245df"
            :size="18"
            style="margin-right: 5px"
          />
          区域分布情况
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts"></script>

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
