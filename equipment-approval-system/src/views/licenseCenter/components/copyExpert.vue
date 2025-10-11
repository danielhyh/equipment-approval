<template>
  <div class="review-page" v-loading="loading">
    <div class="page-b-p">
      <div class="title">
        <Icon icon="svg-icon:user-graduatel" :size="24" color="#165DFF" />
        <span>专家审核</span>
      </div>

      <el-form
        :model="formValue"
        :rules="rules"
        ref="formRef"
        label-width="120px"
        class="demo-ruleForm"
        label-position="top"
        :disabled="isDisabled"
      >
        <el-form-item label="审核结果" prop="reviewResult">
          <el-radio-group v-model="formValue.reviewResult">
            <el-radio :value="1">通过</el-radio>
            <el-radio :value="0">不通过</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注" prop="reviewOpinion">
          <el-input
            v-model="formValue.reviewOpinion"
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 6 }"
          />
        </el-form-item>
      </el-form>

      <div class="title">
        <Icon icon="svg-icon:user-graduatel" :size="24" color="#165DFF" />
        <span>关联专家</span>
      </div>

      <div class="contact-row">
        <div class="search-row">
          <el-input
            v-model.trim="searchExpertForm.keyword"
            placeholder="请输入专家姓名或医院"
            @change="getExpertList"
            clearable
          />
          <el-select
            v-model="searchExpertForm.specialty"
            @change="getExpertList"
            placeholder="请选择专业类别"
            clearable
          >
            <el-option
              v-for="(item, index) in specialtyList"
              :key="index"
              :label="item"
              :value="item"
            />
          </el-select>
        </div>
        <el-table
          :data="expertList"
          row-key="id"
          style="width: 100%"
          max-height="320px"
          ref="multipleTableRef"
          @row-click="handleRowClick"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" :selectable="selectableFn" align="center" />
          <el-table-column prop="name" label="专家姓名" />
          <el-table-column prop="gender" label="性别">
            <template #default="scope">
              {{ getDictLabel(DICT_TYPE.SYSTEM_USER_SEX, scope.row.gender) }}
            </template>
          </el-table-column>
          <el-table-column prop="unit" label="医院名称" />
          <el-table-column prop="specialty" label="专业类别" />
          <el-table-column prop="remark" label="备注" />
        </el-table>

        <div class="check-list-box">
          <div class="check-list-title">
            <span>已选择专家</span>
            <span>({{ selectMultiple.length }}/{{ maxLimit }})</span>
          </div>

          <div class="check-list-content">
            <div v-for="item in selectMultiple" :key="item.id" class="check-list-item">
              <div class="name">{{ item.name }}</div>
              <div class="info">
                <span>{{ item.hospital }}</span> <span>{{ item.typeName }}</span>
              </div>
              <Icon
                v-show="!isDisabled"
                icon="ep:close"
                :size="18"
                color="#fb3939"
                @click="removeItem(item)"
              />
            </div>
          </div>
        </div>
      </div>

      <div class="upload-contact-box">
        <label>专家审批附件：</label>
        <UploadFile
          v-model:model-value="fileList"
          :file-type="['doc', 'xls', 'pdf', 'png', 'jpg', 'jpeg']"
          :drag="true"
          :file-size="10"
          :auto-upload="true"
          :disabled="isDisabled"
        />
        <el-empty v-if="fileList.length === 0 && isDisabled" :image-size="80" />
      </div>

      <div style="display: flex; align-items: center; justify-content: center" v-if="!isDisabled">
        <el-button type="info" @click.stop="goBack">取消</el-button>
        <el-button type="primary" @click="submitFn">提交</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts" name="Expert">
import { UploadFile } from '@/components/UploadFile/index'
import { ElMessage } from 'element-plus'
import type { FormInstance, TableInstance } from 'element-plus'
import { useRoute } from 'vue-router'
import { ExpertExtApi } from '@/api/biz/expertext'
import { DICT_TYPE, getDictLabel } from '@/utils/dict'
import { LicenseApi } from '@/api/biz/license/index'
const route = useRoute()
const router = useRouter()
const { duplicateId } = route.query
let loading = ref(false)

const goBack = () => {
  router.back()
}
let props = defineProps({
  disabled: {
    type: Boolean,
    default: false
  }
})
let isDisabled = computed(() => {
  return props.disabled
})
let formValue = ref({
  id: Number(duplicateId),
  reviewResult: '',
  reviewOpinion: '',
  expertAttachments: '', //上传附件的地址 多个以逗号隔开
  expertIds: '' //选中的专家的id
})
const updateFormValue = async () => {
  try {
    loading.value = true
    let response = await LicenseApi.auditCopyLicenseDetail({ id: Number(duplicateId) })
    formValue.value.reviewResult = response.reviewResult
    formValue.value.reviewOpinion = response.reviewOpinion
    formValue.value.expertAttachments = response.expertAttachments
    formValue.value.expertIds =
      response.expertList && Array.isArray(response.expertList)
        ? response.expertList.map((item: any) => item.id).join(',')
        : ''
    // 文件回显处理
    if (response.expertAttachments) {
      fileList.value = response.expertAttachments.split(',')
    }
    // 列表回显处理
    if (expertList.value.length) {
      let expertIdArr = formValue.value.expertIds.split(',') || []
      expertIdArr.forEach((eg) => {
        let item = expertList.value.find((item) => item.id === Number(eg))
        if (item) {
          selectMultiple.value.push(item)
        }
      })
    }
  } catch (e) {
    ElMessage.error('获取设备验收信息失败')
  } finally {
    loading.value = false
  }
}

let formRef = ref<FormInstance | null>(null)
let rules = reactive({
  reviewResult: [{ required: true, message: '请选择审核结果', trigger: 'blur' }],
  reviewOpinion: [{ required: false, message: '请输入审核备注', trigger: 'blur' }]
})

interface listDataType {
  id: number
  gender: string
  name: string
  hospital: string
  typeName: string
  type: number
  remark: string
}
let expertList = ref<any[]>([])
let multipleTableRef = ref<TableInstance | null>(null)
let selectMultiple = ref<listDataType[]>([])
let maxLimit = 3
const selectableFn = (row) => {
  if (isDisabled.value) {
    return false
  }

  let includesBool = selectMultiple.value.findIndex((eg) => eg.id === row.id)
  if (includesBool !== -1) {
    return true
  }
  if (selectMultiple.value.length >= maxLimit) {
    return false
  }
  return true
}
const handleSelectionChange = (val: listDataType[]) => {
  if (!multipleTableRef.value) return

  if (val.length > maxLimit) {
    ElMessage.warning('最多选择3个专家')
    multipleTableRef.value.clearSelection()
    return
  }
  let expertIdArr: number[] = []
  val.forEach((item: listDataType) => expertIdArr.push(item.id))
  formValue.value.expertIds = expertIdArr.join(',')
  selectMultiple.value = val
}
const handleRowClick = (row) => {
  if (multipleTableRef.value && selectableFn(row)) {
    multipleTableRef.value.toggleRowSelection(row)
  }
}
const removeItem = (item: listDataType) => {
  if (isDisabled.value) {
    return
  }

  handleRowClick(item)
}

// 附件
let fileList = ref<string[]>([])
const submitFn = async () => {
  if (!formRef.value) {
    ElMessage.error('表单加载错误')
    return
  }
  try {
    await formRef.value.validate()
    //调用接口
    formValue.value.expertAttachments = fileList.value.join(',')
    await LicenseApi.auditCopyLicense(formValue.value)
    ElMessage.success('提交成功')
    goBack()
    return { success: true }
  } catch (err) {
    // ElMessage.error('请填写完整信息')
    // console.log(err)
  }
}
const specialtyList = ref([])
const getExpertList = async () => {
  let params = {
    keywords: searchExpertForm.value.keyword,
    specialty: searchExpertForm.value.specialty
  }
  expertList.value = await ExpertExtApi.getList(params)
}
const getSpecialtyList = async () => {
  let res = await ExpertExtApi.getSpecialty()
  specialtyList.value = res.filter((eg) => eg)
}
const searchExpertForm = ref({
  keyword: undefined,
  specialty: undefined
})
onMounted(async () => {
  await getExpertList()
  getSpecialtyList()
  updateFormValue()
})

defineExpose({
  submitFn
})
</script>

<style lang="scss" scoped>
.review-page {
  padding: 0 10px;
  .page-b-p {
    background-color: rgba(248, 250, 252, 0.8);
    border: 1px solid rgba(226, 232, 240, 0.6);
    border-radius: 12px;
    padding: 10px 14px;
    margin-top: 20px;
  }
  .title {
    display: flex;
    align-items: center;
    font-size: 16px;
    font-weight: 600;
    color: #165dff;
    padding-bottom: 8px;
    border-bottom: 2px solid rgba(22, 93, 255, 0.1);
    span {
      margin-left: 6px;
    }
  }
  &:deep(.demo-ruleForm) {
    padding: 20px 0;
    .el-form-item__label {
      color: #333;
      font-weight: bold;
    }
    .el-input,
    .el-textarea {
      .el-input__inner,
      .el-textarea__inner {
        --el-disabled-text-color: #000;
      }
      .el-input__inner::placeholder {
        -webkit-text-fill-color: #999;
      }
    }
    .el-radio {
      .is-checked {
        .el-radio__inner {
          border-color: #999;
          background-color: #fff;
          &::after {
            background-color: #5b5b5b;
          }
        }
      }

      .el-radio__label {
        color: #000;
      }
    }
    .row-col {
      display: grid;
      grid-template-columns: 1fr 1fr;
      grid-gap: 20px;
    }
  }
  .contact-row {
    padding: 20px 0;
    .search-row {
      display: grid;
      grid-template-columns: repeat(8, 1fr);
      gap: 20px;
      align-items: center;
      margin-bottom: 20px;
      .el-input {
        grid-column: 1/7;
      }
      .el-select {
        grid-column: 7/9;
      }
    }

    .check-list-box {
      padding: 14px;
      background-color: #fff;
      border: 1px solid rgba(226, 232, 240, 0.6);
      border-radius: 12px;
      margin-top: 14px;
      .check-list-title {
        display: flex;
        align-items: center;
        font-size: 16px;
        font-weight: 600;
        color: #165dff;
        padding: 10px 0;
        span {
          margin-right: 6px;
        }
      }
      .check-list-content {
        .check-list-item {
          position: relative;
          padding: 10px;
          border: 1px solid #d6d6d6;
          background-color: rgb(249, 250, 250);
          border-radius: 12px;
          & + .check-list-item {
            margin-top: 10px;
          }
          .name {
            font-size: 14px;
            font-weight: 600;
            color: #333;
          }
          .info {
            font-size: 12px;
            color: #333;
            span + span {
              margin-left: 10px;
            }
          }
          .el-icon {
            position: absolute;
            top: 50%;
            right: 12px;
            transform: translateY(-50%);
            cursor: pointer;
          }
        }
      }
    }
  }
  .upload-contact-box {
    & > label {
      font-size: 14px;
      line-height: 18px;
      color: #333;
      font-weight: 600;
    }
    &:deep(.upload-file) {
      .upload-file-tip {
        & > div {
          font-size: 12px !important;
        }
      }
    }
  }
}
</style>
