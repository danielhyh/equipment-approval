<template>
  <el-dialog
    class="login-dialog-style"
    v-model="visible"
    top="20vh"
    width="760px"
    :destroy-on-close="true"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :close-icon="CircleCloseFilled"
    @closed="closedDialogFn"
  >
    <div class="print--container-box">
      <!-- 手机号 | 账号 登录 -->
      <div class="login-content" v-if="currentStep === 1 || currentStep === 2">
        <div class="title-select">
          <div class="title-item" :class="{ active: currentStep === 2 }" @click="currentStep = 2">
            <!-- 账号登录 -->
            密码登录
          </div>

          <span>/</span>
          <div class="title-item" :class="{ active: currentStep === 1 }" @click="currentStep = 1">
            <!-- 手机号登录 -->
            手机验证码登录
          </div>
        </div>
        <div class="sub-title">{{ currentStep === 1 ? "使用已注册手机号登录" : "未注册手机号将会在登录时自动创建账号" }}</div>
        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          label-width="0"
          size="large"
          :validate-on-rule-change="false"
          :disabled="loading"
        >
          <el-form-item prop="account" v-if="currentStep === 2">
            <el-input v-model="loginForm.account" placeholder="请输入账号" clearable />
          </el-form-item>
          <el-form-item prop="password" v-if="currentStep === 2 && (phoneValidate === 2 || phoneValidate === 3)">
            <el-input v-model="loginForm.password" type="password" show-password placeholder="请输入密码" clearable />
          </el-form-item>
          <el-form-item prop="phone" v-if="currentStep === 1">
            <el-input v-model="loginForm.phone" placeholder="请输入手机号" clearable />
          </el-form-item>
          <el-form-item prop="smsCode" v-if="currentStep === 1 || phoneValidate === 3">
            <el-input v-model="loginForm.smsCode" placeholder="请输入验证码">
              <template #append>
                <div
                  class="sms-code-btn"
                  :class="{ disabled: startTime }"
                  @click="getSmsCode(loginForm.phone || loginForm.account)"
                >
                  {{ startTime ? currentCount + "s" : "获取验证码" }}
                </div>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item>
            <div class="login-btn-box" style="width: 100%">
              <el-button type="primary" color="#1648E1" style="width: 100%" :loading="loading" @click.stop="loginFormSubmit">
                {{
                  currentStep === 2
                    ? phoneValidate === 1
                      ? "账号验证"
                      : phoneValidate === 2
                      ? "立即登录"
                      : "立即注册并登录"
                    : "立即登录"
                }}
              </el-button>
            </div>
            <div class="privacy-agreement-row">
              <div class="left" :class="{ warning: checkWarning }">
                <el-checkbox v-model="checkAgreement">
                  <template #default>
                    已阅读并同意 <span @click.stop="goToUserFn">用户服务协议</span>和
                    <span @click.stop="goToPrivacy">隐私声明</span></template
                  >
                </el-checkbox>
              </div>
              <div class="right forget-word" v-if="currentStep === 2" @click.stop="forgetPasswordFn">忘记密码</div>
            </div>
          </el-form-item>
          <div class="other-row">
            <div class="line-row">
              <div class="line"></div>
              <div class="text">其他登录方式</div>
              <div class="line"></div>
            </div>
            <div class="wechat-login" @click.stop="handleWeChatFn">
              <svg-icon name="wechat-fill"></svg-icon>
              <span>微信</span>
            </div>
          </div>
        </el-form>
      </div>
      <!-- 忘记密码 -->
      <div class="login-content forget-password" v-if="currentStep === 3">
        <div class="title-select">
          <div class="title-item active">忘记密码</div>
        </div>
        <el-form ref="forgetFormRef" :model="forgetForm" :rules="forgetRules" label-width="0" size="large">
          <el-form-item prop="phone">
            <el-input v-model="forgetForm.phone" placeholder="请输入手机号" clearable />
          </el-form-item>
          <el-form-item prop="smsCode">
            <el-input v-model="forgetForm.smsCode" placeholder="请输入验证码">
              <template #append>
                <div class="sms-code-btn" :class="{ disabled: startTime }" @click="getSmsCode(forgetForm.phone)">
                  {{ startTime ? currentCount + "s" : "获取验证码" }}
                </div>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="forgetForm.password" type="password" show-password placeholder="请输入密码" clearable />
          </el-form-item>
          <el-form-item>
            <div class="login-btn-box" style="width: 100%">
              <el-button type="primary" color="#1648E1" style="width: 100%" @click="resetPasswordFn">重置密码</el-button>
            </div>
          </el-form-item>
          <div class="other-row">
            <div class="back-login" @click.stop="goToAccount(2)">
              <IEpArrowLeft />
              <span>返回安全登录</span>
            </div>
          </div>
        </el-form>
      </div>
      <!-- 微信扫码 -->
      <div class="login-content wechat" v-if="currentStep === 4">
        <div class="title-select">
          <div class="title-item active">微信扫码登录</div>
        </div>
        <div class="sub-title">未注册微信号将会在登录时自动创建账号</div>
        <div class="wechat-qrcode">
          <div class="qrcode-img" v-loading="loading">
            <img :src="wechatCode" alt="微信二维码" @load="codeLoadFn" @click.stop="getWeChatCode" />
          </div>
        </div>
        <div class="other-tip">
          <div class="text">
            扫码表示已阅读并同意 <span @click.stop="goToUserFn">用户服务协议</span> 和
            <span @click.stop="goToPrivacy">隐私声明</span>
          </div>
        </div>
        <div class="other-row">
          <div class="back-login" @click.stop="goToAccount(1)">
            <IEpArrowLeft />
            <span>返回安全登录</span>
          </div>
        </div>
      </div>
      <!-- 账号登录绑定手机号 -->
      <div class="login-content bind-phone bind-account-phone" v-if="currentStep === 5">
        <div class="title-select">
          <div class="title-item active">绑定手机号</div>
        </div>
        <div class="sub-title">
          当前账号，未绑定手机号。<br />
          应国家法律要求，登录前需要验证手机号。如果被验证手机号未绑定，将自动绑定到当前帐号。
        </div>
        <el-form ref="bindPhoneFormRef" :model="accoutBindPhoneForm" :rules="bindPhoneRules" label-width="0" size="large">
          <el-form-item prop="phone">
            <el-input v-model="accoutBindPhoneForm.phone" placeholder="请输入手机号" clearable />
          </el-form-item>
          <el-form-item prop="smsCode">
            <el-input v-model="accoutBindPhoneForm.smsCode" placeholder="请输入验证码">
              <template #append>
                <div class="sms-code-btn" :class="{ disabled: startTime }" @click="getSmsCode(accoutBindPhoneForm.phone)">
                  {{ startTime ? currentCount + "s" : "获取验证码" }}
                </div>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <div class="login-btn-box" style="width: 100%">
              <el-button type="primary" color="#1648E1" style="width: 100%" @click.stop="bindPhoneFormSubmit"
                >立即绑定/登录</el-button
              >
            </div>
          </el-form-item>
        </el-form>
      </div>
      <!-- 微信扫码登录绑定手机号 -->
      <div class="login-content bind-phone bind-wechat-phone" v-if="currentStep === 6">
        <div class="title-select">
          <div class="title-item active">绑定手机号</div>
        </div>
        <div class="sub-title">
          当前账号，未绑定手机号。<br />
          应国家法律要求，登录前需要验证手机号。如果被验证手机号未绑定，将自动绑定到当前帐号。
        </div>
        <el-form
          ref="wechatBindPhoneFormRef"
          :model="wechatBindPhoneForm"
          :rules="wechatBindPhoneRules"
          label-width="0"
          size="large"
        >
          <el-form-item prop="phone">
            <el-input v-model="wechatBindPhoneForm.phone" placeholder="请输入手机号" clearable />
          </el-form-item>
          <el-form-item prop="smsCode">
            <el-input v-model="wechatBindPhoneForm.smsCode" placeholder="请输入验证码">
              <template #append>
                <div class="sms-code-btn" :class="{ disabled: startTime }" @click="getSmsCode(wechatBindPhoneForm.phone)">
                  {{ startTime ? currentCount + "s" : "获取验证码" }}
                </div>
              </template>
            </el-input>
          </el-form-item>
          <!-- <el-form-item prop="password">
            <el-input v-model="wechatBindPhoneForm.password" type="password" show-password placeholder="请输入密码" clearable />
          </el-form-item> -->
          <el-form-item>
            <div class="login-btn-box" style="width: 100%">
              <el-button type="primary" color="#1648E1" style="width: 100%" @click.stop="wechatBindPhoneFormSubmit">
                立即绑定/登录
              </el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <!-- 手机号登录设置密码 -->
      <div class="login-content bind-phone-password" v-if="currentStep === 7">
        <div class="title-select">
          <div class="title-item active">设置新密码</div>
        </div>
        <div class="sub-title">
          首次登录需要设置新密码。<br />
          请设置一个至少 6 位的密码。
        </div>
        <el-form
          ref="bindPhonePasswordFormRef"
          :model="bindPhonePasswordForm"
          :rules="bindPhonePasswordRules"
          label-width="0"
          size="large"
        >
          <el-form-item prop="password">
            <el-input v-model="bindPhonePasswordForm.password" type="password" show-password placeholder="请输入密码" clearable />
          </el-form-item>
          <el-form-item>
            <div class="login-btn-box" style="width: 100%">
              <el-button type="primary" color="#1648E1" style="width: 100%" @click.stop="bindPhonePasswordFormSubmit">
                立即设置/登录
              </el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import {
  accountLogin,
  mobileLogin,
  checkUserExist,
  sendSmsCode,
  sendLoginCode,
  getWechatQRCode,
  wechatLogin,
  wechatLoginEnroll,
} from "@/apis/login.js";
import { CircleCloseFilled } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { ref, computed, watch } from "vue";
import { useUserStore } from "@/pinia/modules/user.js";
const userStore = useUserStore();
// 双向绑定 模型
let visible = defineModel("visible", { default: false });
// 全局加载
let loading = ref(false);
// 当前 步骤
// 1 手机号 登录
// 2 账号 登录
// 3 忘记密码
// 4 微信扫码
// 5 账号登录绑定手机号
// 6 微信扫码登录绑定手机号
// 7 手机号登录设置密码
let currentStep = ref(2); // 当前步骤
watch(currentStep, (v) => {
  resetDialog();
});
// 一、二、手机号 | 账号 登录
let loginForm = ref({
  account: "",
  password: "",
  phone: "",
  smsCode: "",
});
const loginFormRef = ref(null);
// 账号登录 手机号校验是否通过 1:初始化 ，2：校验通过，3：校验未通过
let phoneValidate = ref(1);
// 获取验证码
let defaultCountDown = 60; // 默认倒计时 60s
let countDown = ref(60);
let currentCount = ref(0);
let startTime = ref(null);
let timeOutId = null;
const updateCountDown = () => {
  let now = new Date().valueOf();
  if (countDown.value > 0) {
    let time = Math.ceil((startTime.value + defaultCountDown * 1000 - now) / 1000);
    currentCount.value = time;
    countDown.value--;
    timeOutId = setTimeout(updateCountDown, 1000);
  } else {
    countDown.value = 60;
    startTime.value = null;
    timeOutId = null;
  }
};
const getSmsCode = async (phone) => {
  // 正则表达式
  let regix = /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1589]))\d{8}$/;
  if (!phone) {
    ElMessage.warning("请输入手机号！");
    return;
  }
  if (!regix.test(phone)) {
    ElMessage.warning("请输入正确手机号！");
    return;
  }
  if (startTime.value) {
    return;
  }
  startTime.value = new Date().valueOf();
  let params = { username: phone };
  let requestFn = null;
  if ((currentStep.value === 2 && phoneValidate.value === 3) || currentStep.value === 6) {
    // 发送注册验证码
    requestFn = sendSmsCode(params);
  } else {
    // 发送登录验证码
    requestFn = sendLoginCode(params);
  }
  requestFn
    .then((response) => {
      ElMessage.success("验证码已发送！");

      updateCountDown();
    })
    .catch((err) => {
      startTime.value = null;
    });
};
// 是否同意协议
let checkAgreement = ref(false);
let checkWarning = ref(false);
// 手机号 | 账号 登录 表单校验规则
let accountLoginRules = ref({
  account: [
    { required: true, message: "请输入账号", trigger: "blur" },
    {
      pattern: /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1589]))\d{8}$/,
      message: "请输入正确的账号",
      trigger: "blur",
    },
  ],
  password: [
    { required: true, message: "请输入密码完成登录", trigger: "blur" },
    { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" },
  ],
});
let PhoneLoginRules = ref({
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    {
      pattern: /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1589]))\d{8}$/,
      message: "请输入正确的手机号",
      trigger: "blur",
    },
  ],
  smsCode: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    { min: 6, max: 6, message: "请输入6位验证码", trigger: "blur" },
  ],
});
const loginRules = computed(() => {
  if (currentStep.value === 1) {
    return PhoneLoginRules.value;
  }
  if (currentStep.value === 2 && phoneValidate.value === 1) {
    accountLoginRules.value.password[0].required = false;
    return accountLoginRules.value;
  }
  if (currentStep.value === 2 && phoneValidate.value === 2) {
    accountLoginRules.value.password[0].required = true;
    return accountLoginRules.value;
  }
  if (currentStep.value === 2 && phoneValidate.value === 3) {
    accountLoginRules.value.password[0].required = true;
    return { ...accountLoginRules.value, smsCode: PhoneLoginRules.value.smsCode };
  }
});
// 手机号 | 账号 登录 表单校验
const loginFormSubmit = () => {
  if (!checkAgreement.value) {
    checkWarning.value = true;
    setTimeout(() => {
      checkWarning.value = false;
    }, 1000);
    return ElMessage.warning("请阅读并同意 用户服务协议和隐私声明");
  }
  loginFormRef.value.validate((valid) => {
    if (valid) {
      // 账号登录  先验证手机号是否存在
      if (currentStep.value === 2 && phoneValidate.value === 1) {
        checkUserExistFn();
        return;
      }
      if (currentStep.value === 2 && phoneValidate.value === 2) {
        accountLoginFn(false);
        return;
      }
      if (currentStep.value === 2 && phoneValidate.value === 3) {
        accountLoginFn(true);
        return;
      }
      // 手机号登录
      if (currentStep.value === 1) {
        mobileLoginFn();
        return;
      }
    }
  });
};
// 账号验证 函数请求
const checkUserExistFn = () => {
  loading.value = true;
  checkUserExist(loginForm.value.account)
    .then((res) => {
      if (res.code === 200 && res.data) {
        phoneValidate.value = 2;
      } else {
        phoneValidate.value = 3;
      }
    })
    .finally(() => {
      loading.value = false;
    });
};
//  账号(注册 | 登录) 函数请求
const accountLoginFn = (register) => {
  let params = {
    username: loginForm.value.account || loginForm.value.phone,
    password: loginForm.value.password,
  };
  if (register) {
    params.code = loginForm.value.smsCode;
  }
  loading.value = true;
  accountLogin(params)
    .then((res) => {
      let { token } = res.data;
      userStore.setToken(token);
      // todo  没有用户信息 后续 将用户信息 存储到 pinia 中
      userStore.setUser(res.data);
      visible.value = false;
      ElMessage.success("登录成功！");
    })
    .finally(() => {
      loading.value = false;
    });
};
// 手机号登录
const mobileLoginFn = () => {
  let params = {
    username: loginForm.value.phone,
    code: loginForm.value.smsCode,
  };
  loading.value = true;
  mobileLogin(params)
    .then((res) => {
      let { token } = res.data;
      userStore.setToken(token);
      // todo  没有用户信息 后续 将用户信息 存储到 pinia 中
      userStore.setUser(res.data);
      visible.value = false;
      ElMessage.success("登录成功！");
    })
    .finally(() => {
      loading.value = false;
    });
};
// 三、 忘记密码
const forgetFormRef = ref(null);
const forgetPasswordFn = () => {
  currentStep.value = 3;
  console.log("----- 忘记密码 ----");
};
let forgetForm = ref({
  phone: "",
  smsCode: "",
  password: "",
});
const forgetRules = {
  phone: [
    { required: true, message: "手机号不能为空", trigger: "blur" },
    {
      pattern: /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1589]))\d{8}$/,
      message: "请输入正确的手机号",
      trigger: "blur",
    },
  ],
  smsCode: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    { min: 6, max: 6, message: "请输入6位验证码", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" },
  ],
};
// 重置密码
const resetPasswordFn = () => {
  console.log("----- 重置密码 ----");
};

// 四、 二维码登录
let wechatCode = ref("");
let weChatPrefix = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
let currentTicket = "";
let loopTime = null;
const getWeChatCode = () => {
  // 获取二维码
  loading.value = true;
  currentTicket = "";
  getWechatQRCode()
    .then((res) => {
      currentTicket = res.data.ticket;
      wechatCode.value = weChatPrefix + currentTicket;
    })
    .catch((err) => {
      currentTicket = "";
      wechatCode.value = "";
    })
    .finally(() => {
      loading.value = false;
      if (loopTime) {
        clearInterval(loopTime);
        loopTime = null;
      }
    });
};
const codeLoadFn = () => {
  // 图片加载完成 // 轮询扫码登录状态
  if (loopTime) {
    clearInterval(loopTime);
    loopTime = null;
    return;
  }
  loopTime = setInterval(() => queryWeChatCode(), 2000);
};
// 查询扫码状态
const queryWeChatCode = () => {
  if (!currentTicket) {
    ElMessage.warning("未获取到微信二维码");
    return;
  }
  // 查询扫码状态
  wechatLogin(currentTicket).then((res) => {
    let { data } = res;
    if (data === null) {
      return;
    }
    if (data?.enroll) {
      clearInterval(loopTime);
      currentStep.value = 6;
      return;
    }
    if (!!!data?.enroll && !!data?.token) {
      clearInterval(loopTime);
      userStore.setToken(data.token);
      // todo  没有用户信息 后续 将用户信息 存储到 pinia 中
      // userStore.setUser(res.data);
      visible.value = false;
      ElMessage.success("登录成功！");
      return;
    }
  });
};
// 进入微信扫码登录
const handleWeChatFn = () => {
  currentStep.value = 4;
  console.log("----- 微信扫码登录 ----");
  // 获取二维码
  getWeChatCode();
};
// 五、 账号登录绑定手机号
const bindPhoneFormRef = ref(null);
let accoutBindPhoneForm = ref({
  phone: "",
  smsCode: "",
});
let bindPhoneRules = {
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    {
      pattern: /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1589]))\d{8}$/,
      message: "请输入正确的手机号",
      trigger: "blur",
    },
  ],
  smsCode: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    { min: 6, max: 6, message: "请输入6位验证码", trigger: "blur" },
  ],
};
const bindPhoneFormSubmit = () => {
  console.log("----- 账号登录绑定手机号 ----");
};

// 六、 微信绑定手机号与密码
const wechatBindPhoneFormRef = ref(null);
let wechatBindPhoneForm = ref({
  phone: "",
  smsCode: "",
  // password: "",
});
let wechatBindPhoneRules = computed(() => {
  return {
    phone: forgetRules.phone,
    smsCode: forgetRules.smsCode,
  };
});
const wechatBindPhoneFormSubmit = () => {
  console.log("----- 微信扫码登录绑定手机号 ----");
  let params = {
    username: wechatBindPhoneForm.value.phone,
    code: wechatBindPhoneForm.value.smsCode,
  };
  loading.value = true;
  wechatLoginEnroll(params, currentTicket)
    .then((res) => {
      let {
        data: { token },
        msg,
      } = res;
      userStore.setToken(token || msg);
      // todo  没有用户信息 后续 将用户信息 存储到 pinia 中
      // userStore.setUser(res.data);
      visible.value = false;
      ElMessage.success("登录成功！");
    })
    .finally(() => {
      loading.value = false;
    });
};
// 七、 手机号登录设置密码
const bindPhonePasswordFormRef = ref(null);
let bindPhonePasswordForm = ref({
  password: "",
});
const bindPhonePasswordRules = {
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" },
  ],
};
const bindPhonePasswordFormSubmit = () => {
  console.log("----- 手机号登录设置密码 ----");
};
// 返回账密登录 || 手机号登录
const goToAccount = (v) => {
  currentStep.value = v;
};
// 用户隐私协议
const goToUserFn = () => {
  console.log("----- 用户隐私协议 ----");
};
// 隐私声明
const goToPrivacy = () => {
  console.log("----- 隐私声明 ----");
};
// 重置数据
const resetDialog = () => {
  loading.value = false;
  phoneValidate.value = 1;
  // 手机号 账号登录 重置数据
  Object.keys(loginForm.value).forEach((key) => {
    loginForm.value[key] = "";
  });
  if (timeOutId) {
    // 切换方式 是否 重置
    clearTimeout(timeOutId);
    timeOutId = null;
    startTime.value = null;
    countDown.value = 60;
  }
  // 微信登录
  Object.keys(wechatBindPhoneForm.value).forEach((key) => {
    wechatBindPhoneForm.value[key] = "";
  });
  // currentTicket = "";
  if (loopTime) {
    clearInterval(loopTime);
    loopTime = null;
  }
  if (wechatCode.value) {
    wechatCode.value = "";
  }
};
// 弹窗关闭  重置数据
const closedDialogFn = () => {
  currentStep.value = 2;
  resetDialog();
};
</script>

<style lang="scss">
// 抖动动画
@keyframes shake {
  0%,
  100% {
    transform: translateX(0);
  }
  10%,
  30%,
  50%,
  70%,
  90% {
    transform: translateX(-10px);
  }
  20%,
  40%,
  60%,
  80% {
    transform: translateX(10px);
  }
}
.login-dialog-style {
  &.el-dialog {
    border-radius: 24px;
    height: 520px;
    padding: 0 !important;
    background-image: url("../../assets/images/login_bj.jpg");
    background-size: auto 100%;
    background-position: left top;
    box-shadow: 2px 2px 4px 0px #2336ffb8 inset;
    .el-dialog__header.show-close {
      width: 100%;
      box-sizing: border-box;
      position: absolute;
      top: 0;
      left: 0;
      z-index: 2;
      .el-dialog__headerbtn {
        .el-dialog__close {
          font-size: 28px;
          color: #9ca9d9;
          &:hover {
            color: #6e79a3;
          }
          &:active {
            transform-origin: center center;
            transform: scale(0.9);
          }
        }
      }
    }
    .el-dialog__body {
      display: flex;
      justify-content: flex-end;
      height: 100%;
    }
    .print--container-box {
      width: 440px;
      border-radius: 24px;
      height: 100%;
      background-image: radial-gradient(circle at 45% top, rgba(216, 231, 253, 0.8) 0px, rgba(255, 255, 255, 0.95) 100%);
      backdrop-filter: blur(10px);
      padding: 60px 50px 20px;
      box-sizing: border-box;
    }
    .login-content {
      .title-select {
        display: flex;
        align-items: center;
        .title-item {
          cursor: pointer;
          font-size: 20px;
          color: #7e8dd7;
          font-family: "miSans_bold";
          &.active {
            color: #000;
          }
        }
        span {
          color: #7e8dd7;
          font-size: 20px;
          margin: 0 10px;
        }
      }
      .sub-title {
        font-size: 12px;
        color: rgb(0, 0, 0, 0.6);
        margin-bottom: 30px;
      }
      .el-form {
        .el-form-item--large {
          margin-bottom: 0;
          & + .el-form-item--large {
            margin-top: 16px;
          }
          .el-form-item__content {
            line-height: 42px;
            .el-input--large {
              --el-input-height: 42px;
              --el-input-border-radius: 8px;
              --el-border-radius-base: 8px;
              .el-input__wrapper {
                box-shadow: 0 0 0 1px #dcdcdc inset;
                &.is-focus {
                  box-shadow: 0 0 0 1px var(--el-input-focus-border-color) inset;
                }
              }
              .sms-code-btn {
                min-width: 40px;
                font-size: 14px;
                color: #406eef;
                user-select: none;
                text-align: center;
                cursor: pointer;
                &.disabled {
                  pointer-events: none;
                }
              }
            }
          }
          .el-form-item__error {
            padding-top: 2px;
          }
        }
        .el-form-item.is-error {
          .el-form-item__content {
            .el-input {
              .el-input__wrapper {
                box-shadow: 0 0 0 1px var(--el-color-danger) inset !important;
              }
            }
          }
        }
        .login-btn-box {
          margin-top: 10px;
          .el-button--large {
            --el-border-radius-base: 8px;
            --el-button-size: 40px;
          }
        }
        .privacy-agreement-row {
          width: 100%;
          display: flex;
          justify-content: space-between;
          & > div {
            font-size: 12px;
            height: 40px;
            color: #5a5f66;
          }
          .left {
            display: flex;
            align-items: center;
            line-height: 40px;
            .el-checkbox.el-checkbox--large {
              margin-right: 5px;
              height: auto;
              .el-checkbox__label {
                font-size: 12px;
                padding-left: 4px;
                span {
                  color: #0052ee;
                  cursor: default;
                }
              }
              .el-checkbox__input.is-checked + .el-checkbox__label {
                color: #5a5f66;
              }
            }
            &.warning {
              animation: shake 0.6s ease-in-out;
            }
          }
          .forget-word {
            cursor: pointer;
            color: #0052ee;
          }
        }
        .other-row {
          display: flex;
          flex-direction: column;
          align-items: center;
          position: absolute;
          width: calc(100% - 100px);
          left: 0;
          right: 0;
          margin: 0 auto;
          bottom: 30px;
          .line-row {
            width: 100%;
            display: flex;
            align-items: center;
            justify-content: space-between;
            .line {
              flex: 1;
              height: 1px;
              background-image: linear-gradient(
                to right,
                #dcdcdc 0px,
                #dcdcdc 2px,
                transparent 2px,
                transparent 4px,
                #dcdcdc 4px,
                #dcdcdc 6px,
                transparent 6px,
                transparent 8px,
                #dcdcdc 8px,
                #dcdcdc 10px,
                transparent 10px,
                transparent 12px,
                #dcdfe6 12px,
                #dcdfe6 94%,
                transparent 94%,
                transparent calc(94% + 2px),
                #dcdcdc calc(94% + 2px),
                #dcdcdc calc(94% + 4px),
                transparent calc(94% + 4px),
                transparent 100%
              );
              &:nth-last-of-type(1) {
                background-image: linear-gradient(
                  to left,
                  #dcdcdc 0px,
                  #dcdcdc 2px,
                  transparent 2px,
                  transparent 4px,
                  #dcdcdc 4px,
                  #dcdcdc 6px,
                  transparent 6px,
                  transparent 8px,
                  #dcdcdc 8px,
                  #dcdcdc 10px,
                  transparent 10px,
                  transparent 12px,
                  #dcdfe6 12px,
                  #dcdfe6 94%,
                  transparent 94%,
                  transparent calc(94% + 2px),
                  #dcdcdc calc(94% + 2px),
                  #dcdcdc calc(94% + 4px),
                  transparent calc(94% + 4px),
                  transparent 100%
                );
              }
            }
            .text {
              white-space: nowrap;
              font-size: 12px;
              color: #5a5f66;
              margin: 0 10px;
              cursor: default;
            }
          }
          .wechat-login {
            margin-top: 20px;
            display: flex;
            align-items: center;
            line-height: 1;
            cursor: pointer;
            .svg-icon {
              font-size: 30px;
              color: #2aae67;
            }
            span {
              font-size: 14px;
              color: #2aae67;
            }
          }
          .back-login {
            display: flex;
            align-items: center;
            cursor: pointer;
            svg {
              color: #0052ee;
            }
            span {
              margin-left: 2px;
              color: #0052ee;
            }
          }
        }
      }
    }
    .forget-password {
      .title-select {
        margin-bottom: 50px;
      }
    }
    .wechat {
      .title-select {
        justify-content: center;
      }
      .sub-title {
        margin-bottom: 10px;
        text-align: center;
      }
      .wechat-qrcode {
        .qrcode-img {
          width: 220px;
          height: 220px;
          border-radius: 8px;
          overflow: hidden;
          margin: 20px auto;
          background-color: #fff;
          img {
            width: 100%;
            height: 100%;
            display: block;
            cursor: pointer;
          }
        }
      }
      .other-tip {
        text-align: center;
        .text {
          font-size: 12px;
          color: #5a5f66;
          cursor: default;
          span {
            color: #0052ee;
            cursor: pointer;
          }
        }
      }
      .other-row {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-top: 50px;
        .back-login {
          display: flex;
          align-items: center;
          cursor: pointer;
          svg {
            color: #0052ee;
          }
          span {
            margin-left: 2px;
            color: #0052ee;
          }
        }
      }
    }
  }
}
</style>
