<template>
  <div class="steps-container">
    <!-- 步骤指示器 -->
    <div class="steps-indicator">
      <template v-for="(step, index) in steps" :key="step.id || index">
        <!-- 步骤节点 -->
        <div
          class="step-node"
          :class="{
            'step-node-active': index === currentStep,
            'step-node-end': index <= maxStep,
            'step-disabled': disabledFn(index),
          }"
          @click="handleChangeStep(index)"
        >
          <div class="step-number">
            {{ index + 1 }}
          </div>
          <div class="step-label">{{ step.title }}</div>
          <div class="step-status">{{ step.status }}</div>
        </div>
        <!-- 连接线 (最后一个步骤不需要连接线) -->
        <div
          v-if="index < steps.length - 1"
          class="step-connector"
          :class="{
            active: index <= maxStep,
            'step-half': index === maxStep,
          }"
        ></div>
      </template>
    </div>
  </div>
</template>

<script setup name="Steps">

// 定义组件props
const props = defineProps({
  // 步骤数据数组
  steps: {
    type: Array,
    required: false,
    default: () => [
      {
        title: "申报须知",
        status: "10%",
      },
      {
        title: "申报信息",
        status: "30%",
      },
      {
        title: "注意事项",
        status: "50%",
      },
      {
        title: "材料上传",
        status: "80%",
      },
      {
        title: "评审状态",
        status: "90%",
      },
      {
        title: "申报结束",
        status: "100%",
      },
    ],
  },
  handleStep: {
    //可操作的步骤索引
    type: Number,
    default: 1,
  },
});

let currentStep = defineModel("currentStep", {
  type: Number,
  default: 0,
});
const disabledFn = (index) => {
  return index > props.handleStep;
};
let maxStep = computed(() => {
  return props.handleStep;
});
const handleChangeStep = (index) => {
  if (index <= props.handleStep) {
    currentStep.value = index;
  }
};
</script>

<style lang="scss" scoped>
.steps-container {
  width: 100%;
  background-color: #ffffff;
  padding: 16px;
  border-radius: 14px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  user-select: none;
}

.steps-indicator {
  display: flex;
  align-items: center;
  width: 100%;
  position: relative;
}

.step-node {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  z-index: 1;
  flex: 1;
  cursor: pointer;
  .step-number {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    background-color: #e2e8f0;
    color: #64748b;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: bold;
    font-size: 16px;
    margin-bottom: 8px;
    transition: all 0.3s ease;
  }
}
.step-node-end {
  .step-number {
    background-image: radial-gradient(circle at 20% 10%, #5b8cffc0 0%, #0044e3 100%);
    color: #ffffff;
    box-shadow: 0 4px 12px rgba(22, 93, 255, 0.3);
  }
}
.step-node-active {
  .step-number {
    background-image: radial-gradient(circle at 20% 10%, #ff9971 0%, #9d5303 100%);
    color: #ffffff;
    box-shadow: 0 4px 12px rgba(254, 181, 45, 0.797);
  }
  .step-label,
  .step-status {
    color: #1e293b;
    font-weight: bold;
  }
}
.step-disabled {
  cursor: default;
}

.step-label {
  font-size: 14px;
  font-weight: 500;
  color: #1e293b;
  margin-bottom: 4px;
}

.step-status {
  font-size: 12px;
  color: #64748b;
}

.step-connector {
  flex: 1;
  height: 2px;
  background-color: #e2e8f0;
  border-radius: 4px;
  overflow: hidden;
  margin: 0 2px;
  position: relative;
  &::after {
    content: "";
    display: block;
    height: 100%;
    width: 0px;
    border-radius: 4px;
    background-image: linear-gradient(to right, #165dff, #165dff);
    transition: all 0.3s ease-in-out;
  }
}

.step-connector.active {
  &::after {
    width: 100%;
  }
}
.step-connector.step-half {
  &::after {
    width: 50%;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .steps-container {
    padding: 12px 16px;
  }

  .step-number {
    width: 30px;
    height: 30px;
    font-size: 14px;
  }

  .step-label {
    font-size: 12px;
  }

  .step-status {
    font-size: 10px;
  }
}
</style>
