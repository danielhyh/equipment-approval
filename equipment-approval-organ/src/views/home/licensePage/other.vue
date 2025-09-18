<template>
  <div>
    <!-- 设备使用人员 -->
    <div class="header-title m-t-b-10">
      <svg-icon name="typcn:group" size="20" class="m-r-5" />
      <span>设备使用人员</span>
    </div>
    <el-button type="primary" @click="handleOpenAdd" class="m-b-10" v-if="!isDisabled">添加设备使用人员</el-button>
    <el-table class="table_style" style="width: 100%" :data="formData.equipmentUsers">
      <el-table-column label="身份证号码" prop="IdCard" align="center" />
      <el-table-column label="姓名" prop="name" align="center" />
      <!-- 性别 -->
      <el-table-column label="性别" prop="gender" align="center">
        <template #default="{ row }">
          {{ row.gender === "1" ? "男" : "女" }}
        </template>
      </el-table-column>
      <!-- 出生日期 -->
      <el-table-column label="出生日期" prop="birthDate" align="center" />
      <!-- 职称 -->
      <el-table-column label="职称" prop="title" align="center" />
      <!-- 联系电话 -->
      <el-table-column label="联系电话" prop="phoneNumber" align="center" />
      <!-- 操作 -->
      <el-table-column label="操作" fixed="right" width="100" align="center" v-if="!isDisabled">
        <template #default="scope">
          <el-button type="danger" link size="small" @click="handleDeleteUser(scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 正本悬挂位置 -->
    <div class="header-title m-t-b-10">
      <svg-icon name="icon-park-solid:local-two" size="20" class="m-r-5" />
      <span>正本悬挂位置</span>
    </div>
    <!-- 正本悬挂位置 上传 -->
    <div class="upload-content-box">
      <template v-if="!isDisabled">
        <!-- <upload v-model:filePath="formData.originalHangPosition" :accept="acceptType" :disabled="isDisabled" :maxSize="10" /> -->
      </template>
      <template v-else>
        <!-- 正本悬挂位置 回显 -->
        <el-image
          v-if="handlePreviewList[0]"
          style="width: 70%; margin: 0 auto; display: block"
          :src="handlePreviewList[0]"
          fit="fill"
          preview-teleported
          :preview-src-list="handlePreviewList"
        />
        <el-empty v-else :image-size="150"></el-empty>
      </template>
    </div>
    <!-- 设备使用情况 -->
    <div class="header-title m-t-b-10">
      <svg-icon name="fa-solid:chart-line" size="20" class="m-r-5" />
      <span>设备使用情况</span>
    </div>
    <!-- 弹窗 -->
    <Dialog v-model:visible="dialogVisible" :attr="dialogAttr" @closed="handleClosed">
      <template #header>
        <svg-icon name="icon-park-solid:local-two" size="20" class="m-r-5" />
        <span>添加设备使用人员</span>
      </template>

      <el-form :model="userForm" :rules="userFormRules" label-position="top" class="grid-form-2" ref="userFormRef">
        <el-form-item label="身份证号码" prop="IdCard">
          <el-input v-model="userForm.IdCard" placeholder="请输入身份证号码" clearable @change="handleIdCardBlur"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name" placeholder="请输入姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="userForm.gender" placeholder="请选择性别" clearable>
            <el-option label="男" value="1"></el-option>
            <el-option label="女" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthDate">
          <el-date-picker
            v-model="userForm.birthDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择出生日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-input v-model="userForm.title" placeholder="请输入职称" clearable></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="phoneNumber">
          <el-input v-model="userForm.phoneNumber" placeholder="请输入联系电话" clearable></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="flex justify-center align-center">
          <el-button type="info" @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAddUser">确定</el-button>
        </div>
      </template>
    </Dialog>
  </div>
</template>

<script setup>
import upload from "./components/upload.vue";
let props = defineProps({
  disabled: {
    type: Boolean,
    default: true,
  },
});
let isDisabled = props.disabled;
let formRef = ref(null);
let formData = reactive({
  equipmentUsers: [], // 设备使用人员 JSON格式
  hangingLocation: "", // 正本悬挂位置
});
let rules = ref({});

// 弹窗
let dialogVisible = ref(false);
let dialogAttr = {
  width: "500px",
  "append-to-body": true,
  "destroy-on-close": true,
};
let userFormRef = ref(null);
let userForm = reactive({
  IdCard: "",
  name: "",
  gender: "",
  birthDate: "",
  title: "",
  phoneNumber: "",
});
let userFormRules = {
  IdCard: [
    { required: true, message: "请输入身份证号码", trigger: ["blur"] },
    {
      pattern: /^[1-9]\d{5}(?:18|19|20)\d{2}(?:0[1-9]|10|11|12)(?:0[1-9]|[1-2]\d|30|31)\d{3}[\dXx]$/,
      message: "请输入正确的身份证号码",
      trigger: ["blur"],
    },
  ],
  name: [{ required: true, message: "请输入姓名", trigger: ["blur"] }],
  gender: [{ required: true, message: "请选择性别", trigger: ["change"] }],
  birthDate: [{ required: true, message: "请输入出生日期", trigger: ["blur", "change"] }],
  title: [{ required: true, message: "请输入职称", trigger: ["blur"] }],
  phoneNumber: [
    { required: true, message: "请输入联系电话", trigger: ["blur"] },
    {
      pattern: /^(?:(?:\+|00)86)?1[3-9]\d{9}$/,
      message: "请输入正确的联系电话",
      trigger: ["blur"],
    },
  ],
};
const handleIdCardBlur = (v) => {
  userFormRef.value.validateField("IdCard").then((bool) => {
    // 解析身份证号码
    if (bool) {
      let idCard = userForm.IdCard;
      let birthDate = idCard.substring(6, 14);
      userForm.birthDate = birthDate.replace(/(\d{4})(\d{2})(\d{2})/, "$1-$2-$3");
    }
  });
};
// 添加设备使用人员
const handleOpenAdd = () => {
  dialogVisible.value = true;
};
const handleAddUser = async () => {
  try {
    let bool = await userFormRef.value.validate();
    if (!bool) return;
    formData.equipmentUsers.push(JSON.parse(JSON.stringify(userForm)));
    dialogVisible.value = false;
  } catch (err) {
    console.log(err, "添加设备使用人员校验失败");
  }
};
const handleClosed = () => {
  dialogVisible.value = false;
  Object.keys(userForm).forEach((key) => {
    userForm[key] = "";
  });
};
// 删除设备使用人员
const handleDeleteUser = (index) => {
  formData.equipmentUsers.splice(index, 1);
};

// 上传文件
let acceptType = ".jpg,.jpeg,.png,.webp,.bmp";
// 图片回显
const handlePreviewList = computed(() => {
  if (formData.hangingLocation) {
    return [formData.hangingLocation];
  }
  return [];
});


</script>

<style lang="scss" scoped>
.grid-form {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px 15px;
  &:deep(.el-form-item) {
    margin: 0;
    // --el-disabled-text-color: #171717;
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
  .grid-row {
    grid-column: 1 / 5;
  }
  .grid-row-2-4 {
    grid-column: 2 / 5;
  }
}
.grid-form-2 {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px 15px;
  &:deep(.el-form-item) {
    margin: 0;
    --el-disabled-text-color: #171717;
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
    .el-date-editor {
      width: 100%;
    }
  }
}
.header-title {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 700;
  color: #165dff;
  padding: 5px;
  border-bottom: 2px solid rgba(22, 93, 255, 0.1);
}
</style>
