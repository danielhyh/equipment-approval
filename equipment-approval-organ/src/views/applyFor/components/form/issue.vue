<template>
  <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px" label-position="top" class="grid-form">
    <!-- 组织机构名称（配置单位名称）* -->
    <el-form-item label="组织机构名称（配置单位名称）*" prop="orgName" disabled>
      <el-input v-model="formData.orgName" placeholder="请输入组织机构名称（配置单位名称）" disabled />
    </el-form-item>
    <!-- 法定代表人（或主要负责人）* -->
    <el-form-item label="法定代表人（或主要负责人）*" prop="legalPerson">
      <el-input v-model="formData.legalPerson" placeholder="请输入法定代表人（或主要负责人）" disabled />
    </el-form-item>
    <!-- 统一社会信用代码* -->
    <el-form-item label="统一社会信用代码*" prop="creditCode">
      <el-input v-model="formData.creditCode" placeholder="请输入统一社会信用代码" disabled />
    </el-form-item>
    <!-- 所有制性质* -->
    <el-form-item label="所有制性质*" prop="ownershipType">
      <el-input v-model="formData.ownershipType" placeholder="请输入所有制性质" disabled />
    </el-form-item>
    <!-- 许可设备名称* -->
    <el-form-item label="许可设备名称*" prop="deviceType" class="grid-item-l-2">
      <el-select v-model="formData.deviceType" placeholder="请选择许可设备名称" clearable>
        <el-option
          v-for="item in deviceOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
          :disabled="item.disabled"
        />
      </el-select>
    </el-form-item>
    <!-- 阶梯配置机型* -->
    <el-form-item label="阶梯配置机型*" prop="modelType" class="grid-item-r-2">
      <el-select v-model="formData.modelType" placeholder="请输入阶梯配置机型" clearable>
        <el-option v-for="item in modelOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
    </el-form-item>
    <!-- 设备配置地址* -->
    <el-form-item label="设备配置地址*" prop="deviceAddress" class="grid-item-row">
      <template #label="{ label }">
        <span>{{ label }}</span>
      </template>
      <el-input v-model="formData.deviceAddress" placeholder="请输入设备配置地址" disabled />
    </el-form-item>
    <!-- 联系人* -->
    <el-form-item label="联系人*" prop="contactName" class="grid-item-l-2">
      <el-input v-model="formData.contactName" placeholder="请输入联系人" disabled />
    </el-form-item>
    <!-- 联系人电话* -->
    <el-form-item label="联系人电话*" prop="contactPhone" class="grid-item-r-2">
      <el-input v-model="formData.contactPhone" placeholder="请输入联系人电话" disabled />
    </el-form-item>
  </el-form>
</template>

<script setup>
let formRef = ref(null);
let formData = reactive({
  orgName: "组织机构名称", // 组织机构名称（配置单位名称）
  legalPerson: "法定代表人", // 法定代表人（或主要负责人）
  creditCode: "统一社会信用代码", // 统一社会信用代码
  ownershipType: "所有制性质", // 所有制性质
  deviceType: "1", // 许可设备名称
  modelType: "", // 阶梯配置机型
  deviceAddress: "设备配置地址", // 设备配置地址
  contactName: "联系人", // 联系人
  contactPhone: "联系人电话", // 联系人电话
});
let rules = reactive({
  deviceType: [{ required: true, message: "请选择许可设备名称", trigger: "blur" }],
  modelType: [{ required: true, message: "请输入阶梯配置机型", trigger: "blur" }],
});
// 许可设备
let deviceOptions = [
  {
    label: "伽玛射线立体定向放射治疗系统",
    value: "6",
    disabled: true,
  },
  {
    label: "直线加速器",
    value: "5",
    disabled: true,
  },
  {
    label: "1.5T 及以上磁共振成像系统",
    value: "4",
    disabled: true,
  },
  {
    label: "64 排及以上X线计算机断层扫描仪",
    value: "3",
    disabled: true,
  },
  {
    label: "X线正电子发射断层扫描仪",
    value: "2",
    disabled: true,
  },
  {
    label: "内窥镜手术器械控制系统",
    value: "1",
    disabled: false,
  },
];
// 阶梯配置机型
let modelOptions = [
  {
    label: "高端型",
    value: "4",
  },
  {
    label: "基础型",
    value: "3",
  },
  {
    label: "临床型",
    value: "2",
  },
  {
    label: "科研型",
    value: "1",
  },
];

const validor = () => {
  return new Promise((resolve, reject) => {
    formRef.value.validate((valid) => {
      if (valid) {
        resolve(true);
      } else {
        reject(false);
      }
    });
  });
};
const submit = () => {
  return new Promise(async (resolve, reject) => {
    try {
      await validor();
      resolve(JSON.parse(JSON.stringify(formData)));
    } catch (err) {
      reject(err);
    }
  });
};
defineExpose({
  validor,
  submit,
});
</script>

<style lang="scss" scoped>
.grid-form {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px 20px;
  &:deep(.el-form-item) {
    margin: 0;
    --el-text-color-placeholder: #555555;
    .el-form-item__label {
      font-size: 14px;
      color: #666;
      font-weight: 700;
    }
    .el-select {
      width: 100%;
      .el-select__wrapper {
        line-height: 32px;
        min-height: 40px;
      }
    }
    .el-input {
      --el-input-height: 40px;
    }
  }
  .grid-item-l-2 {
    grid-column: 1/3;
  }
  .grid-item-r-2 {
    grid-column: 3/5;
  }
  .grid-item-row {
    grid-column: 1/5;
  }
}
</style>
