<template>
  <div class="medical-agency-page" v-loading="loading">
    <div class="statistics-row">
      <div class="left">
        <Icon icon="ph:farm-fill" :size="20" />
        <span class="title">医疗机构管理</span>
      </div>
      <div class="right">
        <el-button type="primary" :icon="CirclePlusFilled" @click.stop="addFn">
          新增医疗机构
        </el-button>
      </div>
    </div>
    <div class="seach-row">
      <el-form v-model="paramsValue" inline label-suffix=":">
        <!-- 机构级别 -->
        <el-form-item label="机构级别">
          <el-select
            v-model="paramsValue.orgLevel"
            placeholder="请选择 机构级别"
            style="width: 180px"
          >
            <el-option
              v-for="item in orgLevelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <!-- 所有制性质 -->
        <el-form-item label="所有制性质">
          <el-select
            v-model="paramsValue.ownershipType"
            placeholder="请选择 所有制性质"
            style="width: 180px"
          >
            <el-option
              v-for="item in ownershipTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <!-- 所属区域 -->
        <el-form-item label="所属区域">
          <el-select v-model="paramsValue.area" placeholder="请选择 所属区域" style="width: 180px">
            <el-option
              v-for="item in areaOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <!-- 医疗机构名称 -->
        <el-form-item>
          <el-input
            v-model="paramsValue.keyword"
            placeholder="请输入 医疗机构名称"
            style="width: 240px"
          />
        </el-form-item>
        <!-- 搜索按钮 重置按钮 -->
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="searchFn">搜索</el-button>
          <el-button type="info" :icon="RefreshRight" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-container">
      <el-table :data="tableData" row-key="id" style="min-height: 570px">
        <el-table-column label="序号" type="index" width="54" align="center" :index="changeIndex" />
        <!-- 机构名称 -->
        <el-table-column label="机构名称" prop="orgName" align="center" />
        <!-- 法人代表 -->
        <el-table-column label="法人代表" prop="legalPerson" align="center" />
        <!-- 社会统一信用代码 -->
        <el-table-column label="社会统一信用代码" prop="creditCode" align="center" />
        <!-- 机构类别 -->
        <el-table-column label="机构类别" prop="orgLevel" align="center">
          <template #default="scope">
            {{
              scope.row.orgLevel
                ? orgLevelOptions.find((item) => item.value === scope.row.orgLevel)?.label
                : ''
            }}
          </template>
        </el-table-column>
        <!-- 所有制性质 -->
        <el-table-column label="所有制性质" prop="ownershipType" align="center">
          <template #default="scope">
            {{
              scope.row.ownershipType
                ? ownershipTypeOptions.find((item) => item.value === scope.row.ownershipType)?.label
                : ''
            }}
          </template>
        </el-table-column>
        <!-- 所属区域 -->
        <el-table-column label="所属区域" prop="area" align="center">
          <template #default="scope">
            {{
              scope.row.area ? areaOptions.find((item) => item.value === scope.row.area)?.label : ''
            }}
          </template>
        </el-table-column>
        <!-- 设备数量 -->
        <el-table-column label="设备数量" prop="deviceCount" align="center" />
        <!-- 操作 -->
        <el-table-column label="操作" width="220" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="editFn(scope.row)">编辑</el-button>
            <el-button type="primary" size="small" @click="viewFn(scope.row)">查看</el-button>
            <el-button type="danger" size="small" @click="delFn(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <Pagination
        class="pagination-container"
        :total="paramsValue.total"
        v-model:page="paramsValue.pageNum"
        v-model:limit="paramsValue.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- 弹窗 -->
    <Dialog v-model="dialogVisible" v-bind="dialogBind">
      <addMedicalAgency ref="dialogComponentRef" v-bind="dialogComponentProps" />
      <template #footer v-if="dialogComponentProps.type !== 'view'">
        <el-button type="primary"> 提交 </el-button>
        <el-button type="info" @click="dialogVisible = false"> 取消 </el-button>
      </template>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { CirclePlusFilled, Search, RefreshRight } from '@element-plus/icons-vue'
import addMedicalAgency from './components/addMedicalAgency.vue'
import { getDictOptions } from '@/utils/dict'
import type { DictDataType } from '@/utils/dict'
import { ElMessageBox } from 'element-plus'

// 机构级别
const orgLevelOptions = computed<DictDataType[]>(() => getDictOptions('biz_institution_level'))

// 所有制性质
const ownershipTypeOptions = computed<DictDataType[]>(() => getDictOptions('biz_ownership_nature'))
// 所属区域
const areaOptions = computed<DictDataType[]>(() => getDictOptions('biz_area_list'))

let loading = ref(false)
interface ParamsType {
  pageNum: number
  pageSize: number
  total: number
  keyword: string
  // 机构类别
  orgLevel: string
  // 所有制性质
  ownershipType: string
  // 所属区域
  area: string
}
let paramsValue = reactive<ParamsType>({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  keyword: '',
  orgLevel: '',
  ownershipType: '',
  area: ''
})
const changeIndex = (index: number) => {
  return paramsValue.pageSize * (paramsValue.pageNum - 1) + index + 1
}
interface tableDataType {
  id?: number | string
  // 机构名称
  orgName: string
  // 法定代表
  legalPerson: string
  // 社会统一信用代码
  creditCode: string
  // 机构类型
  orgLevel: string
  // 所有制性质
  ownershipType: string
  // 所属区域
  area: string
  // 设备数量
  deviceCount: number | string
  // 上级机构
  parentOrg?: string
  // 详情地址
  detailAddress?: string
  // 联系人
  contactPerson?: string
  // 联系电话
  contactPhone?: string
  // 营业执照
  businessLicense?: string | string[]
}
let tableData = ref<tableDataType[]>([])
const getList = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    tableData.value = [
      {
        id: 1,
        orgName: '医疗机构1',
        legalPerson: '张三',
        creditCode: '123456789012345678',
        orgLevel: '1',
        ownershipType: '1',
        area: '1',
        deviceCount: 10
      }
    ]
    paramsValue.total = tableData.value.length
  }, 1000)
}
const resetSearch = () => {
  paramsValue = Object.assign(paramsValue, {
    pageNum: 1,
    keyword: '',
    orgLevel: '',
    ownershipType: '',
    area: ''
  })
  getList()
}
const searchFn = () => {
  paramsValue.pageNum = 1
  getList()
}

// 弹窗
let dialogVisible = ref(false)
let dialogBind = reactive({
  title: '查看-',
  width: '940px',
  maxHeight: '500px',
  scroll: true,
  fullscreen: true,
  center: true
})
let dialogComponentProps = reactive({
  row: {},
  type: 'view'
})
let dialogComponentRef = ref(null)

const addFn = () => {
  dialogBind.title = '新增医疗机构'
  dialogComponentProps.row = {}
  dialogComponentProps.type = 'add'
  dialogVisible.value = true
}
const editFn = (row) => {
  dialogBind.title = '医疗机构-编辑'
  dialogComponentProps.row = row
  dialogComponentProps.type = 'edit'
  dialogVisible.value = true
}
const viewFn = (row) => {
  dialogBind.title = '医疗机构-查看'
  dialogComponentProps.row = row
  dialogComponentProps.type = 'view'
  dialogVisible.value = true
}
const delFn = (row) => {
  ElMessageBox.confirm('确定删除 ' + row.companyName + ' 吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      // 发送删除请求
    })
    .then(() => {
      // 删除成功后刷新列表
      getList()
    })
}

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.medical-agency-page {
  border-radius: 10px;
  background-color: #fff;
  padding: 10px;
  .statistics-row {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
    padding-bottom: 10px;
    border-bottom: 1px solid #e5e5e5;
    .left {
      display: flex;
      align-items: center;
      font-size: 20px;
      font-weight: bold;
      color: #1081f2;
      .title {
        margin-left: 4px;
      }
    }
    .right {
      display: flex;
      align-items: center;
    }
    .statistics-item {
      display: flex;
      align-items: center;
      padding: 5px 14px;
      border-radius: 6px;
      box-sizing: border-box;
    }
    .statistics-item + .statistics-item {
      margin-left: 10px;
    }
  }
  .seach-row {
    .el-form {
      .el-form-item {
        margin-bottom: 10px;
        margin-right: 10px;
      }
    }
  }
  .table-container {
    &:deep(.el-table) {
      .el-table__cell {
        .el-tag__content {
          display: flex;
          align-items: center;
          .el-icon {
            margin-right: 4px;
          }
        }
        .license-type-tag {
          color: var(--color);
          background-color: rgba(var(--color), 0.3);
        }
      }
    }
    .pagination-container {
      float: none;
      justify-content: flex-end;
    }
    &::after {
      content: '';
      clear: both;
    }
  }
}
</style>
