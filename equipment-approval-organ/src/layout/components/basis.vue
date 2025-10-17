<template>
  <!-- 这里可以添加模板内容 -->
  <div class="basis-container">
    <div class="basis-item">
      <svg-icon name="fa-solid:hospital" />
      <div class="value">{{ deptName || "--" }}</div>
    </div>
    <div class="basis-item">
      <svg-icon name="fa-solid:user" />
      <div class="value">{{ userName || "--" }}</div>
    </div>
    <div class="basis-item">
      <svg-icon name="fa-solid:phone" />
      <div class="value">{{ phone || "--" }}</div>
    </div>
    <div class="basis-item">
      <el-button type="primary" size="small" @click="openBasisDialog">
        <template #icon>
          <svg-icon name="dashicons:info-outline" />
        </template>
        基础数据
      </el-button>
    </div>

    <!-- 基础信息弹窗 -->
    <Dialog v-model:visible="dialogVisible" :attr="dialogSetting">
      <template #header>
        <svg-icon name="fa:info-circle" class="m-r-5" size="20" color="#165dff" />
        <span>机构基本信息</span>
      </template>
      <div class="dialog-content m-b-15">
        <div class="item-msg" v-for="(item, index) in initDesc" :key="index">
          <span class="label">{{ item.label }}</span>
          <span class="value">{{ item.value }}</span>
          <span class="handle">
            <el-button v-if="item.edit" type="primary" size="small" :icon="EditPen" round @click.stop="handleEdit(item.key)">
              编辑
            </el-button>
            <el-button v-else type="info" disabled size="small" :icon="Lock" round>不可编辑</el-button>
          </span>
        </div>
      </div>
      <div class="remark-box p-10">
        <div class="f-s-16 flex items-center m-b-5 p-l-r-10">
          <svg-icon name="fa:info-circle" class="m-r-5" size="16" color="#165dff" />
          <span>修改说明：</span>
        </div>
        <ul class="f-s-12 ul-style-inside p-lr-20 c-64748b">
          <li>点击可修改项目后的"修改"按钮可单独修改对应信息</li>
          <li>修改法定代表人和注册地址需要上传营业执照</li>
          <li>修改信息需要管理员审核，请确保填写信息准确无误</li>
          <li>如有疑问请联系系统管理员（029-987654）</li>
        </ul>
      </div>
      <template #footer>
        <div class="flex items-center justify-center">
          <el-button type="primary" @click="dialogVisible = false">关闭</el-button>
        </div>
      </template>
    </Dialog>
    <!-- 信息编辑弹窗 -->
    <Dialog v-model:visible="editDialog" :attr="dialogSetting">
      <template #header>
        <svg-icon name="fa:edit" class="m-r-5" size="20" color="#165dff" />
        <span>修改信息</span>
      </template>
      <!-- 编辑表单 -->
      <el-form
        :model="formData"
        ref="formRef"
        :rules="rules"
        label-width="120px"
        label-position="top"
        :disabled="loading"
        class="grid-form m-b-15"
      >
        <el-form-item label="法定代表人" prop="legalPerson" class="grid-item-row">
          <el-input
            v-model="formData.legalPerson"
            placeholder="请输入法定代表人"
            clearable
            :disabled="editKey === 'contactPerson' || editKey === 'contactPhone'"
          />
        </el-form-item>
        <el-form-item label="注册地址" prop="detailedAddress" class="grid-item-row">
          <el-input
            v-model="formData.detailedAddress"
            type="textarea"
            placeholder="请输入注册地址"
            clearable
            :disabled="editKey === 'contactPerson' || editKey === 'contactPhone'"
          />
        </el-form-item>
        <el-form-item label="营业执照" prop="businessLicensePic" class="grid-item-row">
          <Upload
            v-model:filePath="formData.businessLicensePic"
            :accept="'.jpg,.jpeg,.png'"
            :disabled="editKey === 'contactPerson' || editKey === 'contactPhone'"
          />
        </el-form-item>
        <!-- 联系人 -->
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="formData.contactPerson" placeholder="请输入联系人" clearable />
        </el-form-item>
        <!-- 联系电话 -->
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="formData.contactPhone" placeholder="请输入联系电话" clearable />
        </el-form-item>
      </el-form>
      <!-- 备注 -->
      <div class="remark-box p-10">
        <div class="f-s-16 flex items-center m-b-5 p-l-r-10">
          <svg-icon name="fa:info-circle" class="m-r-5" size="16" color="#165dff" />
          <span>修改说明：</span>
        </div>
        <ul class="f-s-12 ul-style-inside p-lr-20 c-64748b">
          <li>点击可修改项目后的"修改"按钮可单独修改对应信息</li>
          <li>修改法定代表人和注册地址需要上传营业执照</li>
          <li>修改信息需要管理员审核，请确保填写信息准确无误</li>
          <li>如有疑问请联系系统管理员（029-987654）</li>
        </ul>
      </div>
      <!-- 提交 -->
      <template #footer>
        <div class="flex items-center justify-center">
          <el-button type="primary" :icon="Check" :loading="loading" @click.stop="handleSubmit">提交信息</el-button>
          <el-button type="info" :icon="Close" :disabled="loading" @click="editDialog = false">关闭</el-button>
        </div>
      </template>
    </Dialog>
  </div>
</template>

<script setup name="BasisInfo">
import { Lock, EditPen, Close, Check } from "@element-plus/icons-vue";
import { editUserInfo } from "@/apis/login";
// 这里可以添加 setup 语法糖下的脚本逻辑
import { useUserStore } from "@/pinia/modules/user";
import { useDictStore } from "@/pinia/modules/dict";
let userStore = useUserStore();
let dictStore = useDictStore();
let institutionDict = computed(() => dictStore.getDictTypeList("biz_institution_type"));

const userData = computed(() => userStore.getUser);
const deptName = computed(() => userData.value?.institutionName);
const userName = computed(() => userData.value?.nickname);
const phone = computed(() => userData.value?.contactPhone);

const institutionTypeName = computed(() => {
  return institutionDict.value.find((item) => item.value === userData.value?.institutionType)?.label || "--";
});
// 展示弹窗
let dialogVisible = ref(false);
let dialogSetting = {
  width: "650px",
  "append-to-body": true,
  "destroy-on-close": true,
  "close-on-click-modal": false,
  draggable: true,
};
const initDesc = computed(() => {
  return [
    { label: "机构名称", value: userData.value?.institutionName || "--", edit: false },
    { label: "法定代表人", value: userData.value?.legalPerson || "--", edit: true, key: "legalPerson" },
    { label: "统一社会信用代码", value: userData.value?.unifiedSocialCreditCode || "--", edit: false },
    // 注册地址
    { label: "注册地址", value: userData.value?.detailedAddress || "--", edit: true, key: "detailedAddress" },
    // 所有权性质
    { label: "所有权性质", value: userData.value?.ownershipNature || "--", edit: false },
    // 机构性质
    { label: "机构性质", value: institutionTypeName.value, edit: false },
    // 上级机构
    { label: "上级机构", value: userData.value?.superiorInstitution || "--", edit: false },
    // 卫生机构级别
    { label: "卫生机构级别", value: userData.value?.institutionLevel || "--", edit: false },
    // 所属区域
    { label: "所属区域", value: userData.value?.region || "--", edit: false },
    // 联系人
    { label: "联系人", value: userData.value?.contactPerson || "--", edit: true, key: "contactPerson" },
    // 联系人电话
    { label: "联系人电话", value: userData.value?.contactPhone || "--", edit: true, key: "contactPhone" },
  ];
});
const openBasisDialog = () => {
  dialogVisible.value = true;
};
// 编辑弹窗
let editDialog = ref(false);
let loading = ref(false);
let formRef = ref(null);
let formData = ref({
  legalPerson: "",
  businessLicensePic: "",
  detailedAddress: "",
  contactPerson: "",
  contactPhone: "",
});
let rules = ref({
  legalPerson: [{ required: true, message: "请输入法定代表人", trigger: ["blur"] }],
  businessLicensePic: [{ required: true, message: "请上传营业执照", trigger: ["change"] }],
  detailedAddress: [{ required: true, message: "请输入注册地址", trigger: ["blur"] }],
  contactPerson: [{ required: true, message: "请输入联系人", trigger: ["blur"] }],
  contactPhone: [
    { required: true, message: "请输入联系电话", trigger: ["blur"] },
    {
      pattern: /^1[3456789]\d{9}$/,
      message: "请输入正确的联系电话",
      trigger: ["blur"],
    },
  ],
});
let editKey = ref("");
const handleEdit = (key) => {
  editKey.value = key;
  switch (key) {
    case "detailedAddress":
    case "legalPerson":
      rules.value.businessLicensePic = [{ required: true, message: "请上传营业执照", trigger: ["change"] }];
      break;
    case "contactPerson":
    case "contactPhone":
      rules.value.businessLicensePic = [{ required: false, message: "请上传营业执照", trigger: ["change"] }];
      break;
  }

  Object.keys(formData.value).forEach((key) => {
    formData.value[key] = userData.value[key] || "";
  });
  editDialog.value = true;
};
const handleSubmit = async () => {
  try {
    await formRef.value.validate();
    loading.value = true;
    let params = { ...formData.value, institutionId: userData.value?.institutionId };
    Object.keys(params).forEach((key) => {
      if (params[key] === "") {
        delete params[key];
      }
    });
    await editUserInfo(params);
    await userStore.updateUser();
    ElMessage.success("修改成功");
    editDialog.value = false;
  } catch (err) {
    ElMessage.error("修改失败");
  } finally {
    loading.value = false;
  }
};
</script>

<style lang="scss" scoped>
// 这里可以添加 SCSS 样式
.basis-container {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
  &:deep(.basis-item) {
    flex-shrink: 0;
    display: flex;
    align-items: center;
    > .iconify {
      font-size: 14px !important;
      color: #fff;
      margin-right: 4px;
    }
    .value {
      font-size: 14px;
      color: #fff;
    }
    .el-button {
      font-size: 14px;
      border-radius: 8px;
      box-shadow: 0 0 10px rgba(199, 199, 199, 0.1);
      border: 1px solid rgba(227, 227, 227, 0.3);
      --el-button-bg-color: rgba(64, 158, 255, 0.27);
      &:hover {
        transform: translateY(-1px);
      }
    }
  }
}

.item-msg {
  display: grid;
  grid-template-columns: 150px 1fr 120px;
  border-bottom: 1px solid #efefef;
  > span {
    padding: 14px 10px;
  }
  .label {
    background-color: #e9f5ff69;
    font-weight: bolder;
  }
  .handle {
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
.remark-box {
  border-radius: 10px;
  background-color: #dcfff979;
  position: relative;
  border-left: 4px solid #2298e2e8;
}
.grid-form {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px 15px;
  &:deep(.el-form-item) {
    margin: 0;
  }
  .grid-item-row {
    grid-column: 1/3;
  }
}
</style>
