<template>
  <div>
    <div class="p-b-border m-b-20" v-loading="loading">
      <div class="head-style flex items-center c-165DFF m-b-10">
        <svg-icon name="typcn:group" size="20" class="m-r-5" />
        <span class="f-w-700 f-s-16">设备使用人员</span>
      </div>
      <div style="text-align: right">
        <el-button type="primary" size="small" @click="handleOpenAdd" class="m-b-10">添加设备使用人员</el-button>
      </div>
      <el-table class="table_style" style="width: 100%" :data="personList">
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
        <el-table-column label="操作" fixed="right" width="100" align="center">
          <template #default="scope">
            <el-button type="danger" link size="small" @click="handleDeleteUser(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="p-b-border m-b-20">
      <div class="head-style flex items-center c-165DFF m-b-10">
        <svg-icon name="mdi:address-marker" size="20" />
        <span class="f-w-700 f-s-16">正本悬挂位置</span>
      </div>
      <div class="m-b-5 f-s-14 f-w-700 c-64748b">上传正本悬挂位置照片</div>
      <Upload v-model:filePath="originaImage" accept=".jpg,.jpeg,.png,.webp" />
    </div>

    <div class="p-b-border m-b-20">
      <div class="head-style flex items-center c-165DFF m-b-10">
        <svg-icon name="fa-solid:chart-line" size="20" class="m-r-5" />
        <span class="f-w-700 f-s-16">设备使用情况</span>
      </div>
      <div class="m-b-5 f-s-14 f-w-700 c-64748b">使用情况说明</div>
      <el-input v-model="deviceUse" placeholder="请输入使用情况说明" type="textarea" :autosize="{ minRows: 5 }"></el-input>
    </div>

    <div class="m-b-20">
      <div class="head-style flex items-center c-165DFF m-b-10">
        <svg-icon name="fa-solid:tools" size="20" class="m-r-5" />
        <span class="f-w-700 f-s-16">检查保养情况</span>
      </div>
      <div class="m-b-5 f-s-14 f-w-700 c-64748b">检查保养情况</div>
      <el-input v-model="checkMaintain" placeholder="请输入检查保养情况" type="textarea" :autosize="{ minRows: 5 }"></el-input>
    </div>

    <!-- 弹窗 -->
    <Dialog v-model:visible="dialogVisible" :attr="dialogAttr" @closed="handleClosed">
      <template #header>
        <svg-icon name="icon-park-solid:local-two" size="20" class="m-r-5" color="#165DFF" />
        <span class="f-w-700">添加设备使用人员</span>
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
import { formatDate } from "@/utils/tools";
import { createLicenseOther } from "@/apis/home";
import { useBasisStore } from "@/pinia/modules/basis";
import Upload from "../../../components/upload/upload.vue";
const basisStore = useBasisStore();
const licenseBasis = computed(() => basisStore.getLicenseBasis);
let applicationId = computed(() => licenseBasis.value.applicationId);

let loading = defineModel("loading", false);
// 1-正本悬挂位置，2-设备使用情况，3-检查保养记录，4-使用人员变更  infoType
// 使用人员
let personList = ref([]);
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
// --- 添加设备使用人员
const handleOpenAdd = () => {
  dialogVisible.value = true;
};
const handleAddUser = async () => {
  try {
    let bool = await userFormRef.value.validate();
    if (!bool) return;
    personList.value.push(JSON.parse(JSON.stringify(userForm)));
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
// --- 删除设备使用人员
const handleDeleteUser = (index) => {
  personList.value.splice(index, 1);
};
// 正本悬挂
let originaImage = ref("");
// 设备使用情况
let deviceUse = ref("");
// 检查保养记录
let checkMaintain = ref("");

const validor = () => {
  return new Promise((resolve, reject) => {
    let valid1 = personList.value.length === 0;
    let valid2 = originaImage.value === "";
    let valid3 = deviceUse.value === "";
    let valid4 = checkMaintain.value === "";
    if (valid1 && valid2 && valid3 && valid4) {
      ElMessage.error("请填写信息");
      reject(false);
    } else {
      resolve(true);
    }
  });
};
const submit = async () => {
  try {
    await validor();
    loading.value = true;
    let params = [
      {
        applicationId: applicationId.value,
        infoType: 1,
        infoContent: originaImage.value,
        submitTime: formatDate(new Date().getTime()),
      },
      {
        applicationId: applicationId.value,
        infoType: 2,
        infoContent: deviceUse.value,
        submitTime: formatDate(new Date().getTime()),
      },
      {
        applicationId: applicationId.value,
        infoType: 3,
        infoContent: checkMaintain.value,
        submitTime: formatDate(new Date().getTime()),
      },
      {
        applicationId: applicationId.value,
        infoType: 4,
        infoContent: JSON.stringify(personList.value),
        submitTime: formatDate(new Date().getTime()),
      },
    ];
    await createLicenseOther(params);
    ElMessage.success("提交成功");
    return true;
  } catch (err) {
    return false;
  } finally {
    loading.value = false;
  }
};

defineExpose({
  submit,
});
</script>

<style lang="scss" scoped>
.p-b-border {
  padding-bottom: 20px;
  border-bottom: 2px solid #e2e8f0;
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
</style>
