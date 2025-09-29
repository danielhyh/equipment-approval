<template>
  <div>
    <!-- 办件统计汇总 -->
    <div class="title-row">
      <div class="left">
        <div class="title">办件统计汇总</div>
      </div>
      <div class="right">
        <el-button type="primary" size="small" :icon="Filter">筛选</el-button>
      </div>
    </div>
    <div class="content-row">
      <div class="item-box" v-for="item in handingTotal" :key="item.key">
        <div class="value">{{ item.value }}</div>
        <div class="label">{{ item.label }}</div>
      </div>
    </div>
    <!-- 许可证统计汇总 -->
    <div class="title-row m-t-10">
      <div class="left">
        <div class="title">许可证统计汇总</div>
      </div>
    </div>
    <div class="content-row">
      <div class="item-box" v-for="item in licenseTotal" :key="item.key">
        <div class="value">{{ item.value }}</div>
        <div class="label">{{ item.label }}</div>
      </div>
    </div>
    <!-- 历史数据统计汇总 -->
    <div class="title-row m-t-10">
      <div class="left">
        <div class="title">历史数据统计汇总</div>
      </div>
    </div>
    <div class="content-row">
      <div class="item-box" v-for="item in historyTotal" :key="item.key">
        <div class="value">{{ item.value }}</div>
        <div class="label">{{ item.label }}</div>
      </div>
    </div>
    <!-- 专家统计汇总 -->
    <div class="title-row m-t-10">
      <div class="left">
        <div class="title">专家统计汇总</div>
      </div>
    </div>
    <div class="content-row">
      <div class="item-box" v-for="item in expertTotal" :key="item.key">
        <div class="value">{{ item.value }}</div>
        <div class="label">{{ item.label }}</div>
      </div>
    </div>
    <!-- 公告统计汇总 -->
    <div class="title-row m-t-10">
      <div class="left">
        <div class="title">公告统计汇总</div>
      </div>
    </div>
    <div class="content-row">
      <div class="item-box" v-for="item in noticeTotal" :key="item.key">
        <div class="value">{{ item.value }}</div>
        <div class="label">{{ item.label }}</div>
      </div>
    </div>
    <!-- 设备生产企业汇总 -->
    <div class="title-row m-t-10">
      <div class="left">
        <div class="title">设备生产企业汇总</div>
      </div>
    </div>
    <div class="content-row">
      <div class="item-box" v-for="item in deviceTotal" :key="item.key">
        <div class="value">{{ item.value }}</div>
        <div class="label">{{ item.label }}</div>
      </div>
    </div>
    <!-- 医疗机构汇总 -->
    <div class="title-row m-t-10">
      <div class="left">
        <div class="title">医疗机构汇总</div>
      </div>
    </div>
    <div class="content-row">
      <div class="item-box" v-for="item in hospitalTotal" :key="item.key">
        <div class="value">{{ item.value }}</div>
        <div class="label">{{ item.label }}</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Filter } from '@element-plus/icons-vue'

interface itemsType {
  label: string
  value: number | string
  key: string
}
// 办件统计汇总
let handingTotal = reactive<itemsType[]>([
  { label: '办件总量', value: 0, key: 'total_count' },
  //证书申请
  { label: '证书申请', value: 0, key: 'cert_apply_count' },
  // 证书补办
  { label: '证书补办', value: 0, key: 'cert_renew_count' },
  // 证书变更
  { label: '证书变更', value: 0, key: 'cert_change_count' },
  // 基本信息变更
  { label: '基本信息变更', value: 0, key: 'info_change_count' }
])
// 许可证统计汇总
let licenseTotal = reactive<itemsType[]>([
  { label: '许可证总量', value: 0, key: 'license_count' },
  // 正电子发射型磁共振
  { label: '正电子发射型磁共振', value: 0, key: 'mr_count' },
  // X线正电子发射断层扫描仪
  { label: 'X线正电子发射断层扫描仪', value: 0, key: 'xray_count' },
  // 腹腔内窥镜手术系统
  { label: '腹腔内窥镜手术系统', value: 0, key: 'endoscope_count' },
  // 常规放射治疗类设备
  { label: '常规放射治疗类设备', value: 0, key: 'radiation_count' },
  // 首次配置的大型医疗器械
  { label: '首次配置的大型医疗器械', value: 0, key: 'first_config_count' }
])
// 历史数据统计汇总
let historyTotal = reactive<itemsType[]>([
  { label: '历史数据总量', value: 0, key: 'history_count' },
  // x线正电子发射断层扫描仪
  { label: 'x线正电子发射断层扫描仪', value: 0, key: 'history_xray_count' },
  // 内窥镜手术器械控制系统
  { label: '内窥镜手术器械控制系统', value: 0, key: 'history_endoscope_count' },
  // 64排以上X线计算机断层扫描仪
  { label: '64排以上X线计算机断层扫描仪', value: 0, key: 'history_xray_computer_count' },
  // 1.5%T及以上磁共振成像系统
  { label: '1.5%T及以上磁共振成像系统', value: 0, key: 'history_mr_count' },
  // 直线加速器
  { label: '直线加速器', value: 0, key: 'history_linear_accelerator_count' },
  // 伽马射线立体定向放射治疗系统
  { label: '伽马射线立体定向放射治疗系统', value: 0, key: 'history_gamma_ray_count' }
])
// 专家统计汇总
let expertTotal = reactive<itemsType[]>([
  // 专家总数
  { label: '专家总数', value: 0, key: 'expert_count' },
  // 放射影像
  { label: '放射影像', value: 0, key: 'history_radiation_count' },
  // 放射治疗
  { label: '放射治疗', value: 0, key: 'history_radiation_therapy_count' },
  // 核医学
  { label: '核医学', value: 0, key: 'history_nuclear_medicine_count' },
  // 卫生管理
  { label: '卫生管理', value: 0, key: 'history_waste_management_count' },
  // 直线加速器
  { label: '直线加速器', value: 0, key: 'history_linear_accelerator_count' },
  // 医学设备与安全防护
  { label: '医学设备与安全防护', value: 0, key: 'history_medical_equipment_count' },
  // 医学智能工程
  { label: '医学智能工程', value: 0, key: 'history_medical_intelligent_count' }
])
// 公告统计汇总
let noticeTotal = reactive<itemsType[]>([
  { label: '公告总量', value: 0, key: 'notice_count' },
  // 已发布
  { label: '已发布', value: 0, key: 'notice_published_count' },
  // 未发布
  { label: '未发布', value: 0, key: 'notice_unpublished_count' },
  // 总浏览量
  { label: '总浏览量', value: 0, key: 'notice_views_count' }
])
// 设备生产企业汇总
let deviceTotal = reactive<itemsType[]>([
  { label: '设备生产企业总量', value: 0, key: 'device_count' },
  //  正电子发射型核磁共振成像系统生产企业
  { label: '正电子发射型核磁共振成像系统生产企业', value: 0, key: 'mr_count' },
  //  X线正电子发射断层扫描仪生产企业
  { label: 'X线正电子发射断层扫描仪生产企业', value: 0, key: 'xray_count' },
  //  腹腔内窥镜手术系统生产企业
  { label: '腹腔内窥镜手术系统生产企业', value: 0, key: 'endoscope_count' },
  //  常规放射治疗类设备生产企业
  { label: '常规放射治疗类设备生产企业', value: 0, key: 'radiation_count' },
  //  大型医疗器械生产企业
  { label: '大型医疗器械生产企业', value: 0, key: 'first_config_count' }
])
// 医疗机构汇总
let hospitalTotal = reactive<itemsType[]>([
  { label: '医疗机构总量', value: 0, key: 'hospital_count' },
  // 拥有正电子发射型核磁共振成像系统医疗机构
  { label: '拥有正电子发射型核磁共振成像系统医疗机构', value: 0, key: 'mr_hospital_count' },
  //  拥有X线正电子发射断层扫描仪医疗机构
  { label: '拥有X线正电子发射断层扫描仪医疗机构', value: 0, key: 'xray_hospital_count' },
  //  拥有腹腔内窥镜手术系统医疗机构
  { label: '拥有腹腔内窥镜手术系统医疗机构', value: 0, key: 'endoscope_hospital_count' },
  //  拥有常规放射治疗类设备医疗机构
  { label: '拥有常规放射治疗类设备医疗机构', value: 0, key: 'radiation_hospital_count' },
  //  首次配置的大型医疗器械医疗机构
  { label: '首次配置的大型医疗器械医疗机构', value: 0, key: 'first_config_hospital_count' }
])
</script>

<style lang="scss" scoped>
.m-t-10 {
  margin-top: 10px;
}
.title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .left {
    .title {
      line-height: 20px;
      font-size: 16px;
      font-weight: bold;
      color: #333;
      border-left: 4px solid #4245df;
      padding-left: 10px;
    }
  }
}
.content-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 15px;
  padding: 10px 0;
}
.item-box {
  padding: 10px 15px;
  border-radius: 10px;
  background-color: #fff;
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease-in-out;
  flex: 1;
  cursor: default;
  .value {
    line-height: 24px;
    font-size: 18px;
    font-weight: bolder;
    color: #066de3;
  }
  .label {
    font-size: 14px;
    font-weight: bold;
    color: #333;
  }
  &:hover {
    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.3);
  }
}
</style>
