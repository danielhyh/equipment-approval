<template>
  <!-- 这里可以添加模板内容 -->
  <header>
    <div class="header-left">
      <Logo />
      <BasisInfo />
    </div>

    <div class="header-right">
      <MsgBox />
      <div class="header-right-item">
        <el-button type="primary" size="default" @click.stop="logoutFn">
          <template #icon>
            <svg-icon name="streamline:logout-1-remix" />
          </template>
          退出登录
        </el-button>
      </div>
    </div>
  </header>
</template>
<script setup name="HeaderTop">
import Logo from "./logo.vue";
import BasisInfo from "./basis.vue";
import MsgBox from "./msgBox.vue";
import { ElMessageBox } from "element-plus";
import { useUserStore } from "@/pinia/modules/user";
import { loginOutSystem, getSsoLoginUrl } from "@/apis/login";
const userStore = useUserStore();
let router = useRouter();

// 检查是否启用SSO
const ENABLE_SSO = import.meta.env.VITE_ENABLE_SSO === 'true';

const logoutFn = () => {
  ElMessageBox.confirm("确定退出登录吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      //await loginOutSystem();
      // 确认退出登录，清除token
      userStore.loginOut();
      console.log(ENABLE_SSO)
      // 判断是否启用SSO
      if (ENABLE_SSO) {
        try {
          // 获取SSO登录地址并跳转
          const response = await getSsoLoginUrl();
          console.log(response);
          if (response) {
            ElMessage.success("退出登录成功");
            // 跳转到SSO登录页面
            window.location.href = response;
          } else {
            // 如果没有获取到SSO地址，回退到普通登录页
            console.error('SSO登录地址为空，回退到普通登录');
            ElMessage.success("退出登录成功");
            router.push({ name: "Login" });
          }
        } catch (error) {
          console.error('获取SSO登录地址失败:', error);
          // SSO失败时，回退到普通登录页
          ElMessage.success("退出登录成功");
          router.push({ name: "Login" });
        }
      } else {
        // 未启用SSO，跳转到普通登录页
        ElMessage.success("退出登录成功");
        router.push({ name: "Login" });
      }
    })
    .catch(() => {
      // 取消退出登录
    });
};
</script>

<style lang="scss" scoped>
/* 这里可以添加样式代码 */
header {
  min-height: var(--nav-height);
  padding: 0 20px;
  outline: 1px solid rgba(154, 208, 255, 0.5);
  backdrop-filter: blur(10px);
  background-image: linear-gradient(135deg, #165dff 0%, #06b6d4 100%);
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  overflow: hidden;
  cursor: default;
  .header-left {
    display: flex;
    align-items: center;
    gap: 12px;
  }
  .header-right {
    display: flex;
    align-items: center;
    gap: 12px;
    .header-right-item {
      &:deep(.el-button) {
        box-shadow: 0 0 10px rgba(199, 199, 199, 0.1);
        border: 1px solid rgba(227, 227, 227, 0.3);
        border-radius: 10px !important;
        --el-button-bg-color: rgba(64, 158, 255, 0.27);
        &:hover {
          transform: translateY(-1px);
        }
      }
    }
  }
}
</style>
