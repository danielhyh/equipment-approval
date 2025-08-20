<template>
  <div class="review-page">
    <div class="page-b-p">
      <div class="title">
        <Icon icon="svg-icon:user-graduatel" :size="24" color="#165DFF" />
        <span>初步审核</span>
      </div>

      <el-form
        :model="formValue"
        :rules="rules"
        ref="formRef"
        label-width="120px"
        class="demo-ruleForm"
        label-position="top"
      >
        <el-form-item label="审核结果" prop="reivew">
          <el-radio-group v-model="formValue.reivew">
            <el-radio value="1">通过</el-radio>
            <el-radio value="2">不通过</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注" prop="remark">
          <el-input
            v-model="formValue.remark"
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 6 }"
          />
        </el-form-item>
        <div class="row-col">
          <el-form-item label="许可证编号" prop="licenseCode">
            <el-input v-model="formValue.licenseCode" show-word-limit maxlength="11" />
          </el-form-item>
          <el-form-item label="生成日期" prop="createDate">
            <el-date-picker
              v-model="formValue.createDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择日期"
            />
          </el-form-item>
        </div>
      </el-form>

      <div class="title">
        <Icon icon="svg-icon:user-graduatel" :size="24" color="#165DFF" />
        <span>关联专家</span>
      </div>
      <div class="contact-row">
        <div class="search-row">
          <el-input
            v-model.trim="searchValue.keyword"
            placeholder="请输入专家姓名或医院"
            clearable
          />
          <el-select v-model="searchValue.type" placeholder="请选择专业类别" clearable>
            <el-option label="心血管" value="1" />
            <el-option label="神经外科" value="2" />
            <el-option label="骨科" value="3" />
          </el-select>
        </div>
        <el-table
          :data="expertList"
          row-key="id"
          style="width: 100%"
          ref="multipleTableRef"
          @row-click="handleRowClick"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" :selectable="selectableFn" align="center" />
          <el-table-column prop="name" label="专家姓名" />
          <el-table-column prop="gender" label="性别" />
          <el-table-column prop="hospital" label="医院名称" />
          <el-table-column prop="typeName" label="专业类别" />
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
              <Icon icon="ep:close" :size="18" color="#fb3939" @click="removeItem(item)" />
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
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts" name="Expert">
import { UploadFile } from '@/components/UploadFile/index'
import { ElMessage } from 'element-plus'
import type { FormInstance, TableInstance } from 'element-plus'
interface formType {
  reivew: string | number | null
  remark: string
  licenseCode: string
  createDate: string
}
let formValue = ref<formType>({
  reivew: null,
  remark: '',
  licenseCode: '',
  createDate: ''
})

let formRef = ref<FormInstance | null>(null)
let rules = reactive({
  reivew: [{ required: true, message: '请选择审核结果', trigger: 'blur' }],
  remark: [{ required: false, message: '请输入审核备注', trigger: 'blur' }],
  licenseCode: [
    { required: false, message: '请输入专家证书编号', trigger: 'blur' },
    { min: 11, max: 11, message: '请输入11位专家证书编号', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: any) => {
        // 第一个字符是 甲或者乙 剩余字符都是0-9数字 正则表达式
        if (!/^[甲乙][0-9]{10}$/.test(value)) {
          callback(new Error('请输入正确的专家证书编号'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  createDate: [{ required: true, message: '请输入专家证书编号', trigger: 'blur' }]
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
// 搜索
let searchValue = ref({
  keyword: '',
  type: ''
})
let expertList = ref([
  {
    id: 1,
    gender: '男',
    name: '张三',
    hospital: '陕西省第一人民医院',
    typeName: '心血管',
    type: 1,
    remark: ''
  },
  {
    id: 2,
    gender: '男',
    name: '李四',
    hospital: '陕西省第二人民医院',
    typeName: '神经外科',
    type: 2,
    remark: ''
  },
  {
    id: 3,
    gender: '男',
    name: '王五',
    hospital: '陕西省第三人民医院',
    typeName: '骨科',
    type: 3,
    remark: ''
  },
  {
    id: 4,
    gender: '男',
    name: '赵六',
    hospital: '陕西省第四人民医院',
    typeName: '心血管',
    type: 1,
    remark: ''
  },
  {
    id: 5,
    gender: '男',
    name: '孙七',
    hospital: '陕西省第五人民医院',
    typeName: '神经外科',
    type: 2,
    remark: ''
  },
  {
    id: 6,
    gender: '男',
    name: '周八',
    hospital: '陕西省第六人民医院',
    typeName: '骨科',
    type: 3,
    remark: ''
  }
])
let multipleTableRef = ref<TableInstance | null>(null)
let selectMultiple = ref<listDataType[]>([])
let maxLimit = 3
const selectableFn = (row) => {
  let includesBool = selectMultiple.value.findIndex((eg) => eg.id === row.id)
  if (includesBool !== -1) {
    return true
  }
  if (selectMultiple.value.length >= maxLimit) {
    return false
  }
  return true
}
const handleSelectionChange = (val: any) => {
  if (!multipleTableRef.value) return

  if (val.length > maxLimit) {
    ElMessage.warning('最多选择3个专家')
    multipleTableRef.value.clearSelection()
    return
  }
  selectMultiple.value = val
}
const handleRowClick = (row, col, event) => {
  if (multipleTableRef.value && selectableFn(row)) {
    multipleTableRef.value.toggleRowSelection(row)
  }
}

const removeItem = (item: listDataType) => {
  handleRowClick(item, null, null)
}

// 附件
let fileList = ref([])
const submitFn = async () => {
  if (!formRef.value) {
    ElMessage.error('表单加载错误')
    return
  }
  try {
    let valid = await formRef.value.validate()
    if (!valid) {
      ElMessage.error('请填写完整信息')
      return
    }
  } catch (err) {}
}

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
    .el-form-item {
      .el-form-item__label {
        color: #333;
        font-weight: 600;
      }
      .el-date-editor {
        width: 100%;
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
