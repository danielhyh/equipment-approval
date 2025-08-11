<template>
  <el-form-item label="请求头" label-position="top">
    <div v-for="(item, index) in props.header" :key="index" class="flex pb-4">
      <div class="mr-2">
        <el-form-item
          :prop="`${bind}.header.${index}.key`"
          :rules="{
            required: true,
            message: '参数名不能为空',
            trigger: 'blur'
          }"
        >
          <el-input v-model="item.key" style="width: 160px" />
        </el-form-item>
      </div>
      <div class="mr-2">
        <el-form-item>
          <el-select v-model="item.type" style="width: 160px" @change="handleTypeChange(item)">
            <el-option
              v-for="types in BPM_HTTP_REQUEST_PARAM_TYPES"
              :key="types.value"
              :label="types.label"
              :value="types.value"
            />
          </el-select>
        </el-form-item>
      </div>
      <div class="mr-2">
        <el-form-item
          :prop="`${bind}.header.${index}.value`"
          :rules="{
            required: true,
            message: '参数值不能为空',
            trigger: 'blur'
          }"
        >
          <el-input
            v-if="item.type === BpmHttpRequestParamTypeEnum.FIXED_VALUE"
            v-model="item.value"
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item
          :prop="`${bind}.header.${index}.value`"
          :rules="{
            required: true,
            message: '参数值不能为空',
            trigger: 'change'
          }"
        >
          <el-select
            v-if="item.type === BpmHttpRequestParamTypeEnum.FROM_FORM"
            v-model="item.value"
            style="width: 200px"
          >
            <el-option
              v-for="(field, fIdx) in formFieldOptions"
              :key="fIdx"
              :disabled="!field.required"
              :label="field.title"
              :value="field.field"
            />
          </el-select>
        </el-form-item>
      </div>
      <div class="mr-1 flex items-center">
        <Icon :size="18" icon="ep:delete" @click="deleteHttpRequestParam(props.header, index)" />
      </div>
    </div>
    <el-button text type="primary" @click="addHttpRequestParam(props.header)">
      <Icon class="mr-5px" icon="ep:plus" />添加一行
    </el-button>
  </el-form-item>
  <el-form-item label="请求体" label-position="top">
    <div v-for="(item, index) in props.body" :key="index" class="flex pb-4">
      <div class="mr-2">
        <el-form-item
          :prop="`${bind}.body.${index}.key`"
          :rules="{
            required: true,
            message: '参数名不能为空',
            trigger: 'blur'
          }"
        >
          <el-input v-model="item.key" style="width: 160px" />
        </el-form-item>
      </div>
      <div class="mr-2">
        <el-form-item>
          <el-select v-model="item.type" style="width: 160px" @change="handleTypeChange(item)">
            <el-option
              v-for="types in BPM_HTTP_REQUEST_PARAM_TYPES"
              :key="types.value"
              :label="types.label"
              :value="types.value"
            />
          </el-select>
        </el-form-item>
      </div>
      <div class="mr-2">
        <el-form-item
          :prop="`${bind}.body.${index}.value`"
          :rules="{
            required: true,
            message: '参数值不能为空',
            trigger: 'blur'
          }"
        >
          <el-input
            v-if="item.type === BpmHttpRequestParamTypeEnum.FIXED_VALUE"
            v-model="item.value"
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item
          :prop="`${bind}.body.${index}.value`"
          :rules="{
            required: true,
            message: '参数值不能为空',
            trigger: 'change'
          }"
        >
          <el-select
            v-if="item.type === BpmHttpRequestParamTypeEnum.FROM_FORM"
            v-model="item.value"
            style="width: 200px"
          >
            <el-option
              v-for="(field, fIdx) in formFieldOptions"
              :key="fIdx"
              :disabled="!field.required"
              :label="field.title"
              :value="field.field"
            />
          </el-select>
        </el-form-item>
      </div>
      <div class="mr-1 flex items-center">
        <Icon :size="18" icon="ep:delete" @click="deleteHttpRequestParam(props.body, index)" />
      </div>
    </div>
    <el-button text type="primary" @click="addHttpRequestParam(props.body)">
      <Icon class="mr-5px" icon="ep:plus" />添加一行
    </el-button>
  </el-form-item>
</template>
<script lang="ts" setup>
import {
  HttpRequestParam,
  BPM_HTTP_REQUEST_PARAM_TYPES,
  BpmHttpRequestParamTypeEnum
} from '../../consts'
import { useFormFieldsAndStartUser } from '../../node'
defineOptions({
  name: 'HttpRequestParamSetting'
})

const props = defineProps({
  header: {
    type: Array as () => HttpRequestParam[],
    required: false,
    default: () => []
  },
  body: {
    type: Array as () => HttpRequestParam[],
    required: false,
    default: () => []
  },
  bind: {
    type: String,
    required: true
  }
})

// 流程表单字段，发起人字段
const formFieldOptions = useFormFieldsAndStartUser()

/** 监听类型变化，清空值 */
const handleTypeChange = (item: HttpRequestParam) => {
  // 当类型改变时，清空值
  item.value = ''
}

/** 添加请求配置项 */
const addHttpRequestParam = (arr: HttpRequestParam[]) => {
  arr.push({
    key: '',
    type: BpmHttpRequestParamTypeEnum.FIXED_VALUE,
    value: ''
  })
}

/** 删除请求配置项 */
const deleteHttpRequestParam = (arr: HttpRequestParam[], index: number) => {
  arr.splice(index, 1)
}
</script>

<style lang="scss" scoped></style>
