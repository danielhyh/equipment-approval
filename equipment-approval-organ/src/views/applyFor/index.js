import { getDocUrl } from "@/utils/tools";
// 申请单位执业许可证
const applyUnitLicense = getDocUrl("申请单位执业许可证.jpg");
// 事业单位法人证
const unitLegal = getDocUrl("事业单位法人证.jpg");
// 乙类大型医用设备配置许可申请表-空表.docx
const largeMedical = getDocUrl("乙类大型医用设备配置许可申请表-空表.docx");
// 乙类大型医用设备配置许可申请表-样表.docx
const largeMedicalSample = getDocUrl("乙类大型医用设备配置许可申请表-样表.docx");
// 营业执照.jpg
const businessLicense = getDocUrl("营业执照.jpg");
// 与申请配置大型医用设备相应的技术条件、配套设备和专业技术人员资质、能力材料.docx
const technicalConditions = getDocUrl("与申请配置大型医用设备相应的技术条件、配套设备和专业技术人员资质、能力材料.docx");
// 中国（陕西）自由贸易试验区社会办医配置乙类大型医用设备备案登记表-空表.docx
const register = getDocUrl("中国（陕西）自由贸易试验区社会办医配置乙类大型医用设备备案登记表-空表.docx");
// 中国（陕西）自由贸易试验区社会办医配置乙类大型医用设备备案登记表-样表.docx
const registerSample = getDocUrl("中国（陕西）自由贸易试验区社会办医配置乙类大型医用设备备案登记表-样表.docx");
export default {
  // 核发
  issue: {
    title: "乙类大型医用设备配置许可证核发",
    notice: {
      // 办理信息
      processInfo: [
        { label: "到场次数", value: "0次" },
        { label: "法定办结时限", value: "20个工作日" },
        { label: "承诺办结时限", value: "20个工作日" },
        { label: "办理深度", value: "全省" },
        { label: "办理深度", value: "全程网办" },
      ],
      // 基础信息
      basisInfo: [
        { label: "事项名称", value: "乙类大型医用设备配置许可证核发" },
        { label: "目录名称", value: "乙类大型医用设备配置许可证核发" },
        { label: "实施机关", value: "省卫生健康委员会" },
        { label: "承诺办结时限", value: "20 工作日" },
        { label: "实施机构（科）室", value: "省卫健委财务处" },
        { label: "承诺办结时限说明", value: "无" },
        { label: "咨询电话", value: "029-987654" },
      ],
      // 审批条件
      condition: {
        list: [
          "符合乙类大型医用设备配置规划；",
          "具有执业许可证，并设置相应的诊疗科目；或具备符合相关规定要求的从事医疗服务的其他法人资质；",
          "与功能定位、临床服务需求相适应，具有与申请的大型医用设备相适应的技术条件、配套设施和具备相应资质、能力的专业技术人员；",
          "医疗质量安全保障制度健全；",
        ],
        remark: "办理该项业务，需满足以下申请条件，才能进行业务的办理，请自检是否满足审批条件。",
      },
      // 收取材料
      material: {
        dept: {
          shby: {
            label: "社会办医",
            list: [
              {
                text: "乙类大型医用设备配置许可申请表", // 材料名称
                required: true, // 是否必填
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件7份", //纸质材料份数
                standard: "无", // 受理标准
                source: "陕西省卫生健康委财务处", // 来源渠道
                notice: "无", // 填表须知
                sample: largeMedicalSample, // 样表
                empty: largeMedical, // 空表
                view: "", // 查看模板
              },
              {
                text: "乙类大型医用设备配置许可告知承诺书",
                required: true,
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件7份", //纸质材料份数
                standard: "无", // 受理标准
                source: "陕西省卫生健康委财务处", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
              {
                text: "社会办医选择不实行告知承诺制申请书",
                required: true,
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件7份", //纸质材料份数
                standard: "无", // 受理标准
                source: "陕西省卫生健康委财务处", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
              {
                text: "营业执照",
                required: true,
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份", //纸质材料份数
                standard: "无", // 受理标准
                source: "自备", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
              {
                text: "申请单位执业许可证",
                required: true,
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份或复印件1份", //纸质材料份数
                standard: "无", // 受理标准
                source: "自备", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
              {
                text: "与申请配置大型医用设备相应的技术条件、配套设备和专业技术人员资质、能力材料",
                required: true,
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份或复印件1份", //纸质材料份数
                standard: "无", // 受理标准
                source: "陕西省卫生健康委财务处", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
            ],
          },
          cjzj: {
            label: "筹建或在建",
            list: [
              {
                text: "申请单位执业许可证", // 材料名称
                required: true, // 是否必填
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件1份", //纸质材料份数
                standard: "无", // 受理标准
                source: "由申请人自备", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
              {
                text: "营业执照", // 材料名称
                required: true, // 是否必填
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件1份", //纸质材料份数
                standard: "无", // 受理标准
                source: "自备", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
              {
                text: "与申请配置大型医用设备相应的技术条件、配套设备和专业技术人员资质、能力材料", // 材料名称
                required: true, // 是否必填
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件7份", //纸质材料份数
                standard: "无", // 受理标准
                source: "无", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
              {
                text: "乙类大型医用设备配置许可申请表", // 材料名称
                required: true, // 是否必填
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件7份", //纸质材料份数
                standard: "无", // 受理标准
                source: "无", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
            ],
          },
          zmqnshby: {
            label: "自贸区内社会办医",
            list: [
              {
                text: "乙类大型医用设备配置许可申请表", // 材料名称
                required: true, // 是否必填
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件1份", //纸质材料份数
                standard: "无", // 受理标准
                source: "自备", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
              {
                text: "中国（陕西）自由贸易试验区社会办医配置乙类大型医用设备备案登记表", // 材料名称
                required: true, // 是否必填
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件1份", //纸质材料份数
                standard: "无", // 受理标准
                source: "自备", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
              {
                text: "申请单位执业许可证", // 材料名称
                required: true, // 是否必填
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件1份", //纸质材料份数
                standard: "无", // 受理标准
                source: "自备", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
              {
                text: "营业执照", // 材料名称
                required: true, // 是否必填
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件1份", //纸质材料份数
                standard: "无", // 受理标准
                source: "自备", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
              {
                text: "与申请配置大型医用设备相应的技术条件、配套设备和专业技术人员资质、能力材料", // 材料名称
                required: true, // 是否必填
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件1份", //纸质材料份数
                standard: "无", // 受理标准
                source: "自备", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
            ],
          },
          bwcjhzj: {
            label: "不为筹建或在建",
            list: [
              // 申请单位执业许可证
              {
                text: "申请单位执业许可证", // 材料名称
                required: true, // 是否必填
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件1份", //纸质材料份数
                standard: "无", // 受理标准
                source: "自备", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
              // 营业执照
              {
                text: "营业执照", // 材料名称
                required: true, // 是否必填
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件1份", //纸质材料份数
                standard: "无", // 受理标准
                source: "自备", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
              // 与申请配置大型医用设备相应的技术条件、配套设备和专业技术人员资质、能力材料
              {
                text: "与申请配置大型医用设备相应的技术条件、配套设备和专业技术人员资质、能力材料", // 材料名称
                required: true, // 是否必填
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件7份", //纸质材料份数
                standard: "无", // 受理标准
                source: "无", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
              // 乙类大型医用设备配置许可申请表
              {
                text: "乙类大型医用设备配置许可申请表", // 材料名称
                required: true, // 是否必填
                type: "原件和复印件", //材料类型
                form: "纸质、电子", // 材料形式
                paper: "原件1份,复印件7份", //纸质材料份数
                standard: "无", // 受理标准
                source: "无", // 来源渠道
                notice: "无", // 填表须知
                sample: "", // 样表
                empty: "", // 空表
                view: "", // 查看模板
              },
            ],
          },
        },
        remark: "办理该项业务，需提供以下申请材料，才能进行业务的办理，请自检是否具备该材料。",
      },
    },
  },
  //   补办
  reissue: {
    title: "乙类大型医用设备配置许可证补办（申请单位为社会办医）",
  },
  // 变更
  change: {
    title: "乙类大型医用设备配置许可证变更（申请单位为社会办医）",
  },
};
