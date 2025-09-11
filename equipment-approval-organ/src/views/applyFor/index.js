import { getDocUrl } from "@/utils/tools";
// 申请单位执业许可证
const applyUnitLicense = getDocUrl("申请单位执业许可证.jpg");
// 事业单位法人证
const unitLegal = getDocUrl("事业单位法人证.jpg");
// 营业执照.jpg
const businessLicense = getDocUrl("营业执照.jpg");
// 乙类大型医用设备配置许可申请表-空表.docx
const largeMedical = getDocUrl("乙类大型医用设备配置许可申请表-空表.docx");
// 乙类大型医用设备配置许可申请表-样表.docx
const largeMedicalSample = getDocUrl("乙类大型医用设备配置许可申请表-样表.docx");
// 乙类大型医用设备配置许可告知承诺书
const largeMedicalCommitment = getDocUrl("乙类大型医用设备配置许可告知承诺书.docx");
// 社会办医选择不实行告知承诺制申请书
const notImplementApplication = getDocUrl("社会办医选择不实行告知承诺制申请书.docx");
// 与申请配置大型医用设备相应的技术条件、配套设备和专业技术人员资质、能力材料.docx
const technicalConditions = getDocUrl("与申请配置大型医用设备相应的技术条件、配套设备和专业技术人员资质、能力材料.docx");
// 空表 中国（陕西）自由贸易试验区社会办医配置乙类大型医用设备备案登记表-空表.docx
const register = getDocUrl("中国（陕西）自由贸易试验区社会办医配置乙类大型医用设备备案登记表-空表.docx");
// 样表 中国（陕西）自由贸易试验区社会办医配置乙类大型医用设备备案登记表-样表.docx
const registerSample = getDocUrl("中国（陕西）自由贸易试验区社会办医配置乙类大型医用设备备案登记表-样表.docx");
export default {
  dept: {
    shby: "社会办医",
    cjzj: "筹建或在建",
    zmqnshby: "自贸区内社会办医",
    bwcjhzj: "不为筹建或在建",
  },
  // 核发
  issue: {
    title: "乙类大型医用设备配置许可证核发",
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
              accept: ".doc,.docx",
              fileType: 1,
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
              accept: ".doc,.docx",
              fileType: 2,
              type: "原件和复印件", //材料类型
              form: "纸质、电子", // 材料形式
              paper: "原件1份,复印件7份", //纸质材料份数
              standard: "无", // 受理标准
              source: "陕西省卫生健康委财务处", // 来源渠道
              notice: "无", // 填表须知
              sample: "", // 样表
              empty: largeMedicalCommitment, // 空表
              view: "", // 查看模板
            },
            {
              text: "社会办医选择不实行告知承诺制申请书",
              required: true,
              accept: ".doc,.docx",
              fileType: 2,
              type: "原件和复印件", //材料类型
              form: "纸质、电子", // 材料形式
              paper: "原件1份,复印件7份", //纸质材料份数
              standard: "无", // 受理标准
              source: "陕西省卫生健康委财务处", // 来源渠道
              notice: "无", // 填表须知
              sample: "", // 样表
              empty: notImplementApplication, // 空表
              view: "", // 查看模板
            },
            {
              text: "营业执照",
              required: true,
              accept: ".jpg,.png,.jpeg",
              fileType: 3,
              type: "原件和复印件", //材料类型
              form: "纸质、电子", // 材料形式
              paper: "原件1份", //纸质材料份数
              standard: "无", // 受理标准
              source: "自备", // 来源渠道
              notice: "无", // 填表须知
              sample: "", // 样表
              empty: "", // 空表
              view: businessLicense, // 查看模板
            },
            {
              text: "申请单位执业许可证",
              required: true,
              accept: ".jpg,.png,.jpeg",
              fileType: 4,
              type: "原件和复印件", //材料类型
              form: "纸质、电子", // 材料形式
              paper: "原件1份或复印件1份", //纸质材料份数
              standard: "无", // 受理标准
              source: "自备", // 来源渠道
              notice: "无", // 填表须知
              sample: "", // 样表
              empty: "", // 空表
              view: applyUnitLicense, // 查看模板
            },
            {
              text: "与申请配置大型医用设备相应的技术条件、配套设备和专业技术人员资质、能力材料",
              required: true,
              accept: ".doc,.docx",
              fileType: 5,
              type: "原件和复印件", //材料类型
              form: "纸质、电子", // 材料形式
              paper: "原件1份或复印件1份", //纸质材料份数
              standard: "无", // 受理标准
              source: "陕西省卫生健康委财务处", // 来源渠道
              notice: "无", // 填表须知
              sample: "", // 样表
              empty: technicalConditions, // 空表
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
              accept: ".jpg,.png,.jpeg",
              fileType: 4,
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
              accept: ".jpg,.png,.jpeg",
              fileType: 3,
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
              accept: ".doc,.docx",
              fileType: 5,
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
              accept: ".doc,.docx",
              fileType: 1,
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
              accept: ".doc,.docx",
              fileType: 1,
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
              accept: ".doc,.docx",
              fileType: 1,
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
              accept: ".jpg,.png,.jpeg",
              fileType: 4,
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
              accept: ".jpg,.png,.jpeg",
              fileType: 3,
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
              accept: ".doc,.docx",
              fileType: 5,
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
              accept: ".jpg,.png,.jpeg",
              fileType: 4,
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
              accept: ".jpg,.png,.jpeg",
              fileType: 3,
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
              accept: ".doc,.docx",
              fileType: 5,
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
              accept: ".doc,.docx",
              fileType: 1,
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
    // 注意事项 不同设备关联不同事项 todo 暂定使用 表单信息中选中的许可设备 字典值 选择不同的注意事项
    note: {
      // 内窥镜手术器械控制系统
      1: {
        deviceName: "内窥镜手术器械控制系统", // 设备名称
        // 注意事项列表
        list: [
          {
            title: "（一）功能定位及临床服务需求",
            content: `用于全身各系统肿瘤治疗。三级甲等医院可以配置科研型LA（开展调强放疗业务3年以上）。三级乙等医院、二级甲等医院及肿瘤专科医院可配置临床研究型LA和临床实用型LA（开展三维适形放疗业务5年以上）。综合性医院床位数500张以上，年门、急诊量30万以上；肿瘤专科医院床位数300张以上，年门、急诊量10万以上。`,
          },
          {
            title: "（二）技术条件",
            content: [
              "具有独立的放射治疗专业科室设置，且肿瘤外科、肿瘤内科、病理科和医学影像等相关学科设置均在3年以上。肿瘤科为省或市级以上实力较强学科",
              "放疗科具有引领和指导我省、市肿瘤放疗相关专业疾病诊疗及继续教育培训作用。",
              "具有立体定向、三维适形放疗和调强放射治疗的技术和经验。",
              "具有图像引导放射治疗计划设计与执行的能力。",
            ],
          },
          {
            title: "（三）配套设施",
            content: [
              "具备大孔径CT或MR模拟定位机和治疗计划系统;",
              "具备相应的物理质控和剂量验证系统及剂量测量设备;",
              "具备符合要求的场地、机房和辐射防护设施等。",
              "具备CT、MR或PET/CT等设备；",
              "具备完善的影像传输系统和医疗信息化管理系统;",
            ],
          },
          {
            title: "（四）专业技术人员资质和能力",
            content: [
              "放射治疗医师。至少有2名初级和1名中级以上职称的专科医师，具备医师执业资格，取得大型医用设备上岗证或在三甲医院进修学习放疗1年以上。配置科研型LA，至少有1名从事放疗专业5年以上并取得本专业技术高级职称医师及2名中级职称医师；配置临床研究型和临床实用型LA，至少配备1名从事放疗专业3年以上的副高级职称医师及2名中级职称医师。",
              "放射治疗技术人员。至少有2名初级及1名中级及以上专业技术职务任职资格的技术人员，并持有相应专业的资格证书，取得大型医用设备上岗证或在三甲医院进修学习放疗半年以上。配置科研型LA，至少配备1名从事放疗工作5年以上的具有正高级职称放射物理师及2名中级职称技术人员；配置临床研究型和临床实用型LA，至少配备1名从事放疗专业3年以上的副高级职称放射物理师及2名中级职称技术人员。",
            ],
          },
          {
            title: "（五）质量保障",
            content: [
              "具有放射治疗技术质量控制和质量保障体系。",
              "具有相应的辐射防护管理制度。",
              "具有相关安全事件的应急机制及处理能力；具有放射治疗不良反应和疗效评价机制。",
            ],
          },
          {
            title: "（六）其他",
            content: [
              "新建（筹建）医疗机构、社会办医疗机构应当具备以上（二）（三）（四）（五）规定的条件，重点考核人员资质和能力等保障医疗质量安全的相关指标。",
              "新建（筹建）医疗机构相关人员应当具有相应专业技术资质和从业经验，承诺实现技术条件、配套设施、质量保障等相关标准和条件。",
              "公立医疗机构经济运行状况良好，设备配置资金来源有保障且符合有关政策规定。",
            ],
          },
        ],
        // 注意事项 描述
        remark: `分为科研型LA（指具有开展容积调强（旋转调强）的放疗设备）、临床研究型LA（指开展图像引导及调强放疗的设备）和临床实用型LA（指开展常规放疗、三维适形放疗、立体定向放疗的设备）。`,
      },
    },
  },
  //   补办
  reissue: {
    title: "乙类大型医用设备配置许可证补办",
  },
  // 变更
  change: {
    title: "乙类大型医用设备配置许可证变更",
  },
};
