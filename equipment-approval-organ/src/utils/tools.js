// 获取 assets image 下的图片
export const getImageUrl = (url) => {
  if (!url) return;
  return new URL(`../assets/images/${url}`, import.meta.url).href;
};
// 获取 assets doc 下文件
export const getDocUrl = (url) => {
  if (!url) return;
  return new URL(`../assets/doc/${url}`, import.meta.url).href;
};
export const getLicenseUrl = (url) => {
  if (!url) return;
  return new URL(`../assets/license/${url}`, import.meta.url).href;
}
// 判断是否是 JSON 字符串
export function isJsonString(str) {
  if(typeof str !== 'string') return false;
  try {
    JSON.parse(str);
    return true;
  } catch (e) {
    return false;
  }
}
// 判断对象是否为空
export function isEmptyObject(obj) {
  return Object.keys(obj).length === 0;
}

// 时间格式化
export function formatDate(date, format = "yyyy-MM-dd hh:mm:ss") {
  if (!date) return "-";
  let d = new Date(date);
  let year = d.getFullYear();
  let month = d.getMonth() + 1;
  let day = d.getDate();
  let hour = d.getHours();
  let minute = d.getMinutes();
  let second = d.getSeconds();
  // 补位
  month = month < 10 ? "0" + month : month;
  day = day < 10 ? "0" + day : day;
  hour = hour < 10 ? "0" + hour : hour;
  minute = minute < 10 ? "0" + minute : minute;
  second = second < 10 ? "0" + second : second;
  return format.replace("yyyy", year).replace("MM", month).replace("dd", day).replace("hh", hour).replace("mm", minute).replace("ss", second);
}
// 时间格式化 YYYY-MM-DD 转 YYYY 年 MM 月 DD 日
export const dayTimeFormate = (time) => {
  if (!time) return ''
  return time.replace(/(\d{4})-(\d{2})-(\d{2})/, '$1 年 $2 月 $3 日')
}

export function textToSpeech(text, callback) {
  if (!speechSynthesis && window.speechSynthesis.getVoices().length === 0) {
    console.log("浏览器不支持语音合成");
    return;
  }
  if (text.trim().length === 0) return;
  const speech = new SpeechSynthesisUtterance(text);
  speech.lang = "zh-CN"; // 语言
  speech.rate = 1; // 语速
  speech.pitch = 1; // 音调
  speech.volume = 1; // 音量
  speech.voice = window.speechSynthesis
    .getVoices()
    .find((voice) => voice.name === "Microsoft Yaoyao - Chinese (Simplified, PRC)"); // 音色
  // 状态监听
  speech.addEventListener("start", () => {
    console.log("开始播放");
  });
  speech.addEventListener("end", () => {
    console.log("播放结束");
    if (callback) callback();
  });
  speech.addEventListener("pause", () => {
    console.log("暂停播放");
  });
  speech.addEventListener("resume", () => {
    console.log("状态: 恢复朗读");
  });
  // speech.addEventListener("error", (event) => {
  //   console.error("错误:", event.error);
  // });
  //   voice 语音
  window.speechSynthesis.speak(speech);
}

/**
 * 从URL中获取指定参数的值
 * @param {string} key - 参数名
 * @param {string} url - URL字符串，默认为当前页面URL
 * @returns {string|null} 参数值
 */
export function getUrlValue(key, url = window.location.href) {
  try {
    console.log('原始URL:', url);
    
    // 处理Hash路由的情况 - 参数可能在 # 之后
    let searchParams = new URLSearchParams();
    
    // 检查是否是Hash路由（包含 #）
    if (url.includes('#')) {
      // 获取 # 后面的部分
      const hashPart = url.split('#')[1];
      if (hashPart && hashPart.includes('?')) {
        // 提取 ? 后面的查询参数
        const queryString = hashPart.split('?')[1];
        searchParams = new URLSearchParams(queryString);
        console.log('从Hash中提取的查询字符串:', queryString);
      }
    } else {
      // 普通路由，使用标准方法解析
      const urlObj = new URL(url);
      searchParams = urlObj.searchParams;
    }
    
    const value = searchParams.get(key);
    console.log(`参数 ${key} 的值:`, value);
    return value;
  } catch (error) {
    console.error('解析URL失败:', error);
    return null;
  }
}
