// 获取 assets image 下的图片
export const getImageUrl = (url) => {
  if (!url) return;
  return new URL(`../assets/images/${url}`, import.meta.url).href;
};

// 判断是否是 JSON 字符串
export function isJsonString(str) {
  try {
    JSON.parse(str);
    return true;
  } catch (e) {
    return false;
  }
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
