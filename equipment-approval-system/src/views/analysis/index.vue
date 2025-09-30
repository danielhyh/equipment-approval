<template>
  <div class="analysis-box">
    <div class="type-box">
      <div
        class="type-item"
        v-for="item in typeList"
        :class="{ active: activeType.key === item.key }"
        :key="item.key"
        @click="changeType(item)"
      >
        <Icon :icon="item.icon" />
        <span class="label">{{ item.label }}</span>
      </div>
    </div>
    <div class="content-box">
      <keep-alive>
        <component :is="activeType.component" :key="activeType.key" />
      </keep-alive>
    </div>
  </div>
</template>

<script setup lang="ts" name="Analysis">
import Overview from './components/overview.vue'
import DeviceOwnership from './components/deviceOwnership.vue'
let typeList = reactive([
  {
    label: '统计概览',
    key: 'overview',
    icon: 'icon-park-outline:market-analysis',
    component: markRaw(Overview)
  },
  {
    label: '设备拥有量统计',
    key: 'deviceOwnership',
    icon: 'bi:device-ssd-fill',
    component: markRaw(DeviceOwnership)
  },
  { label: '年度递增分量及总量', key: 'annualIncrease', icon: 'fa:pie-chart', component: '' },
  {
    label: '阶梯配置统计',
    key: 'ladderConfiguration',
    icon: 'bi:clipboard-data-fill',
    component: ''
  },
  {
    label: '设备分布情况',
    key: 'deviceDistribution',
    icon: 'material-symbols:map-pin-review-rounded',
    component: ''
  }
])
let activeType = ref(typeList[0])
const changeType = (item) => {
  activeType.value = item
}
</script>

<style lang="scss" scoped>
.analysis-box {
  position: relative;
}
.type-box {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
  padding: 5px 20px;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px);
  border-radius: 10px;
  position: sticky;
  top: 20px;
  .type-item {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 10px 15px;
    border-radius: 5px;
    font-size: 14px;
    flex: 1;
    color: #4e4e4e;
    cursor: pointer;
    transition: all 0.3s ease-in-out;
    > span {
      margin-left: 6px;
    }
    &:hover {
      background-color: #007bff;
      color: #fff;
    }
    &.active {
      background-color: #007bff;
      color: #fff;
    }
  }
}
.content-box {
  padding-top: 30px;
}
</style>
