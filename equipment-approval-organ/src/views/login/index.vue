<template>
    <div class="login-container">
        <div class="login-wrapper">
            <div class="login-header">
                <h1>设备审批系统</h1>
                <span class="org-tag">机构端</span>
            </div>
            <el-tabs v-model="activeTab" class="login-tabs">
            <el-tab-pane label="账户登录" name="account">
                <el-form :model="accountForm" :rules="accountRules" ref="accountFormRef">
                    <el-form-item prop="username">
                        <el-input v-model="accountForm.username" placeholder="请输入用户名"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input v-model="accountForm.password" type="password" placeholder="请输入密码" show-password></el-input>
                    </el-form-item>
                    <el-button type="primary" @click="handleAccountLogin">登录</el-button>
                </el-form>
            </el-tab-pane>
            <el-tab-pane label="手机验证码登录" name="phone">
                <el-form :model="phoneForm" :rules="phoneRules" ref="phoneFormRef">
                    <el-form-item prop="phone">
                        <el-input v-model="phoneForm.phone" placeholder="请输入手机号"></el-input>
                    </el-form-item>
                    <el-form-item prop="code">
                        <div class="code-input">
                            <el-input v-model="phoneForm.code" placeholder="请输入验证码"></el-input>
                            <el-button type="primary" :disabled="isCountingDown" @click="sendCode">
                                {{ countdown > 0 ? `${countdown}s后重新获取` : '获取验证码' }}
                            </el-button>
                        </div>
                    </el-form-item>
                    <el-button type="primary" @click="handlePhoneLogin">登录</el-button>
                </el-form>
            </el-tab-pane>
            <svg-icon name="material-symbols:10mp" color='#f3f3f3' />
        </el-tabs>
        <div class="login-footer">
            <p>设备审批系统 &copy; 版权所有</p>
        </div>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';

const activeTab = ref('account');

// 账户登录表单
const accountForm = reactive({
    username: '',
    password: ''
});

const accountRules = {
    username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
};

const accountFormRef = ref(null);

// 手机验证码登录表单
const phoneForm = reactive({
    phone: '',
    code: ''
});

const phoneRules = {
    phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
    ],
    code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
};

const phoneFormRef = ref(null);

const countdown = ref(0);
const isCountingDown = ref(false);

// 发送验证码
const sendCode = () => {
    phoneFormRef.value.validateField('phone', (error) => {
        if (!error) {
            // 这里调用发送验证码的API
            ElMessage.success('验证码已发送');
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
            ElMessage.success('登录成功');
            
            // 模拟登录跳转
            // window.location.href = '/';
        }
    });
};

// 手机验证码登录
const handlePhoneLogin = () => {
    phoneFormRef.value.validate((valid) => {
        if (valid) {
            // 这里调用手机验证码登录的API
            ElMessage.success('登录成功');
            
            // 模拟登录跳转
            // window.location.href = '/';
        }
    });
};
</script>

<style scoped lang="scss">
// 全局样式重置
* {
    box-sizing: border-box;
}

body {
    margin: 0;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-color: #f0f2f5;
    
    .login-wrapper {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        padding: 20px;
    }
}

.login-header {
    text-align: center;
    margin-bottom: 30px;
    
    h1 {
        font-size: 28px;
        color: #333;
        font-weight: 500;
        margin-bottom: 10px;
        letter-spacing: -0.5px;
    }
    
    .org-tag {
        display: inline-block;
        padding: 4px 16px;
        background-color: #1a73e8;
        color: white;
        border-radius: 16px;
        font-size: 14px;
        font-weight: 400;
    }
}

.login-tabs {
    width: 400px;
    padding: 24px;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
    margin-bottom: 20px;
    
    :deep(.el-tabs__nav-wrap) {
        &::after {
            display: none;
        }
    }
    
    :deep(.el-tabs__item) {
        font-size: 16px;
        padding: 0 20px;
        height: 48px;
        line-height: 48px;
        color: #606266;
        transition: color 0.3s;
        
        &.is-active {
            color: #1a73e8;
        }
    }
    
    :deep(.el-tabs__active-bar) {
        height: 3px;
        background-color: #1a73e8;
    }
}

.el-form {
    margin-top: 20px;
}

.el-form-item {
    margin-bottom: 20px;
    
    &.is-error {
        .el-input__inner {
            border-color: #f56c6c;
            
            &:focus {
                border-color: #f56c6c;
                box-shadow: 0 0 0 2px rgba(245, 108, 108, 0.2);
            }
        }
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
        border-color: #dcdfe6;
        transition: border-color 0.3s, box-shadow 0.3s;
        font-size: 14px;
        color: #303133;
        padding: 0 15px;
        
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
}

.code-input {
    display: flex;
    gap: 10px;
    
    .el-input {
        flex: 1;
    }
    
    .el-button {
        width: 120px;
        height: 40px;
        padding: 0;
        font-size: 13px;
    }
}

.el-button {
    width: 100%;
    height: 40px;
    font-size: 14px;
    font-weight: 500;
    border-radius: 4px;
    transition: all 0.3s;
    border: 1px solid transparent;
    
    &.is-disabled {
        opacity: 0.6;
        cursor: not-allowed;
    }
}

:deep(.el-button--primary) {
    background-color: #1a73e8;
    border-color: #1a73e8;
    color: white;
    
    &:hover,
    &:focus {
        background-color: #1557b0;
        border-color: #1557b0;
        transform: translateY(-1px);
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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

.login-footer {
    text-align: center;
    
    p {
        color: #909399;
        font-size: 13px;
        margin: 0;
        line-height: 1.5;
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
        font-size: 24px;
    }
    
    .code-input {
        flex-direction: column;
        
        .el-button {
            width: 100%;
        }
    }
}
</style>