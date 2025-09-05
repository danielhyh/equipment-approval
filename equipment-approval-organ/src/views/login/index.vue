<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-header">
        <div class="logo-container">
          <svg-icon name="svg-icon:logo-shaanxi" size="80" />
        </div>
        <h1>陕西省大型医用设备在线审批归档系统</h1>
        <p class="subtitle">机构端</p>
      </div>
      <div class="login-select-box">
        <div
          class="form-btn-box"
          v-for="item in loginTypeList"
          :key="item.value"
          @click="changeLoginType(item)"
          :class="{ active: activeTab === item.value }"
        >
          <svg-icon :name="item.icon" size="14" class="btn-icon" />
          <span class="value">{{ item.label }}</span>
        </div>
      </div>

      <div class="login-form-container">
        <!-- 手机验证码登录 -->
        <el-form v-if="activeTab === 'phone'" :model="phoneForm" :rules="phoneRules" ref="phoneFormRef" label-position="top">
          <el-form-item prop="phone" label="手机号">
            <el-input v-model="phoneForm.phone" placeholder="请输入手机号" :maxlength="11" clearable>
              <template #prefix>
                <svg-icon name="svg-icon:user-circle" size="16" class="input-icon" />
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="code" label="验证码">
            <div class="code-input">
              <el-input v-model="phoneForm.code" placeholder="请输入验证码" :maxlength="6" clearable>
                <template #prefix>
                  <svg-icon name="svg-icon:lock" size="16" class="input-icon" />
                </template>
              </el-input>
              <el-button type="primary" :disabled="isCountingDown" @click="sendCode" class="code-button">
                {{ countdown > 0 ? `${countdown}s后重新获取` : "获取验证码" }}
              </el-button>
            </div>
          </el-form-item>
          <el-button type="primary" @click="handlePhoneLogin" class="login-button">
            <span>登录</span>
            <svg-icon name="svg-icon:arrow-right" size="16" class="login-arrow" />
          </el-button>
        </el-form>

        <!-- 账户密码登录 -->
        <el-form v-else :model="accountForm" :rules="accountRules" ref="accountFormRef" label-position="top">
          <el-form-item prop="username" label="用户名">
            <el-input v-model="accountForm.username" placeholder="请输入用户名" clearable>
              <template #prefix>
                <svg-icon name="svg-icon:user-circle" size="16" class="input-icon" />
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password" label="密码">
            <el-input v-model="accountForm.password" type="password" placeholder="请输入密码" show-password clearable>
              <template #prefix>
                <svg-icon name="svg-icon:lock" size="16" class="input-icon" />
              </template>
            </el-input>
          </el-form-item>
          <el-button type="primary" @click="handleAccountLogin" class="login-button">
            <span>登录</span>
            <svg-icon name="svg-icon:arrow-right" size="16" color="#fff" class="login-arrow" />
          </el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { ElMessage } from "element-plus";
import router from "@/router";
import { useUserStore } from "@/pinia/modules/user";
const userStore = useUserStore();

const activeTab = ref("phone");
// 登录类型列表
const loginTypeList = reactive([
  { label: "手机验证码", value: "phone", icon: "ic:outline-phone-iphone" },
  { label: "账户密码", value: "account", icon: "solar:key-bold" },
]);
const changeLoginType = (v) => {
  activeTab.value = v.value;
};

// 账户登录表单
const accountForm = reactive({
  username: "",
  password: "",
});

const accountRules = {
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
};

const accountFormRef = ref(null);

// 手机验证码登录表单
const phoneForm = reactive({
  phone: "",
  code: "",
});

const phoneRules = {
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    // 手机号正则校验
    { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号", trigger: "blur" },
  ],
  code: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    { pattern: /^\d{6}$/, message: "请输入正确的验证码", trigger: "blur" },
  ],
};

const phoneFormRef = ref(null);

const countdown = ref(0);
const isCountingDown = ref(false);

// 发送验证码
const sendCode = () => {
  phoneFormRef.value.validateField("phone", (pass) => {
    if (pass) {
      // 这里调用发送验证码的API
      ElMessage.success("验证码已发送");
      countdown.value = 60;
      isCountingDown.value = true;

      const timer = setInterval(() => {
        countdown.value--;
        if (countdown.value <= 0) {
          clearInterval(timer);
          isCountingDown.value = false;
        }
      }, 1000);
    }
  });
};

// 账户登录
const handleAccountLogin = () => {
  accountFormRef.value.validate((valid) => {
    if (valid) {
      // 这里调用账户登录的API
      // 假设登录成功后返回的token
      const token = "mock-token-for-account-login"; // 实际项目中应该从API返回中获取

      // 存储token
      userStore.setToken(token);

      ElMessage.success("登录成功");

      checkRedirect();
    }
  });
};

// 手机验证码登录
const handlePhoneLogin = () => {
  phoneFormRef.value.validate((valid) => {
    if (valid) {
      // 这里调用手机验证码登录的API
      // 假设登录成功后返回的token
      const token = "mock-token-for-phone-login"; // 实际项目中应该从API返回中获取

      // 存储token
      userStore.setToken(token);

      ElMessage.success("登录成功");

      checkRedirect();
    }
  });
};

// 检查重定向
const checkRedirect = () => {
  // 检查是否有重定向路径
  const redirect = router.currentRoute.value.query.redirect;
  // 使用router进行页面跳转
  if (redirect) {
    router.push(decodeURIComponent(redirect));
  } else {
    router.push("/");
  }
};
</script>

<style scoped lang="scss">
// 全局样式重置
* {
  box-sizing: border-box;
}

body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
}

.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #41d1ff 0%, #bd34fe 100%);
  background-size: 100% 100%;
  position: relative;
  overflow: hidden;

  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: linear-gradient(to right, rgba(255, 255, 255, 0.1) 1px, transparent 1px),
      linear-gradient(to bottom, rgba(255, 255, 255, 0.1) 1px, transparent 1px);
    background-size: 90px 90px;
    pointer-events: none;
  }

  .login-wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 450px;
    padding: 40px 50px;
    position: relative;
    z-index: 1;
    background-color: #fff;
    border-radius: 18px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3), inset 0 0 10px rgba(0, 0, 0, 0.1);
  }
}

.login-header {
  text-align: center;

  .logo-container {
    margin-bottom: 16px;
    .svg-icon {
      border-radius: 12px;
      box-shadow: 0 8px 10px rgba(0, 0, 0, 0.15);
    }
  }

  h1 {
    font-size: 20px;
    color: #333;
    font-weight: bolder;
    font-family: "微软雅黑";
    margin-bottom: 8px;
    letter-spacing: 0;
    line-height: 1.2;
  }

  .subtitle {
    color: #1a73e8;
    font-size: 14px;
    margin: 0;
  }
}

.login-select-box {
  display: flex;
  border-radius: 6px;
  background-color: #f5f7fa;
  padding: 4px;
  margin: 20px 0;
  width: 360px;
  overflow: hidden;
}

.form-btn-box {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 10px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  color: #606266;
  font-size: 14px;
  font-weight: 500;
  background-color: transparent;
  position: relative;
  border: none;
  outline: none;

  &.active {
    background-color: #1a73e8;
    color: white;
    box-shadow: 0 2px 8px rgba(26, 115, 232, 0.2);
  }

  .btn-icon {
    transition: all 0.3s;
  }

  &.active .btn-icon {
    color: white;
  }
}

.login-form-container {
  width: 100%;
  padding: 0;
}

.el-form {
  width: 100%;
}

.el-form-item {
  margin-bottom: 24px;

  &.is-error {
    .el-input__inner {
      border-color: #f56c6c;

      &:focus {
        border-color: #f56c6c;
        box-shadow: 0 0 0 2px rgba(245, 108, 108, 0.2);
      }
    }
  }
  &:deep(.el-form-item__label) {
    font-weight: bolder;
  }
}

.el-form-item__error {
  font-size: 12px;
  padding-top: 4px;
  color: #f56c6c;
}

.el-input {
  height: 40px;

  .el-input__inner {
    height: 40px;
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    transition: border-color 0.3s, box-shadow 0.3s;
    font-size: 14px;
    color: #303133;
    padding-left: 40px;
    padding-right: 12px;

    &:focus {
      border-color: #1a73e8;
      box-shadow: 0 0 0 2px rgba(26, 115, 232, 0.2);
      outline: none;
    }

    &:hover {
      border-color: #c0c4cc;
    }

    &::placeholder {
      color: #c0c4cc;
    }
  }

  .el-input__prefix {
    padding: 0 0 0 12px;
  }
}

.input-icon {
  color: #c0c4cc;
}

.code-input {
  display: flex;
  gap: 10px;

  .el-input {
    flex: 1;
  }
}

.code-button {
  width: 120px !important;
  height: 40px !important;
  padding: 0 !important;
  font-size: 12px !important;
  border-radius: 6px !important;
}

.el-button {
  width: 100%;
  height: 40px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 4px;
  transition: all 0.3s;
  border: 1px solid transparent;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;

  &.is-disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
}

.login-button {
  position: relative;
  overflow: hidden;
}

.login-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(26, 115, 232, 0.3);
}

.login-arrow {
  transition: transform 0.3s;
}

.login-button:hover .login-arrow {
  transform: translateX(4px);
}

:deep(.el-button--primary) {
  background-color: #1a73e8;
  border-color: #1a73e8;
  color: white;

  &:hover,
  &:focus {
    background-color: #1557b0;
    border-color: #1557b0;
  }

  &:active {
    transform: translateY(0);
  }

  &.is-disabled {
    background-color: #e4e7ed;
    border-color: #e4e7ed;
    color: #c0c4cc;
    transform: none;
    box-shadow: none;
  }
}

:deep(.el-button--default) {
  background-color: #fff;
  border-color: #dcdfe6;
  color: #606266;
  &:hover,
  &:focus {
    color: #1a73e8;
    border-color: #c6e2ff;
    background-color: #ecf5ff;
  }
}

// 响应式设计
@media (max-width: 480px) {
  .login-tabs {
    width: 100%;
    max-width: 360px;
    padding: 20px;
  }

  .login-header h1 {
    font-size: 16px;
  }

  .code-input {
    flex-direction: column;

    .el-button {
      width: 100% !important;
    }
  }
}
</style>
