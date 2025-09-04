// 图片添加水印
export function imgAddWatermark(watermarkText, imgUrl) {
  console.log("success loading img", watermarkText);
  return new Promise((resolve, reject) => {
    const img = new Image();
    img.crossOrigin = "anonymous"; // 解决跨域问题
    let text = watermarkText;
    img.onload = function () {
      // 绘制原来图片到canvas上
      let responseImageUrl = addWatermark(img, {
        text: text,
        position: { x: "80%", y: "10%" },
        fontStyle: "25px Microsoft YaHei",
        color: "rgba(0,0,0,0.5)",
      });
      resolve(responseImageUrl);
    };
    img.onerror = function () {
      reject("error");
    };

    img.src = imgUrl;
  });
}
// 添加水印函数
function addWatermark(img, options = { text: "测试", position: { x: 30, y: 10 }, fontStyle: "20px Microsoft YaHei", color }) {
  const canvas = document.createElement("canvas");
  const ctx = canvas.getContext("2d");
  canvas.width = img.width;
  canvas.height = img.height;
  ctx.drawImage(img, 0, 0, img.width, img.height);

  // 添加水印文字
  ctx.save();
  ctx.font = options.fontStyle;
  ctx.fillStyle = options.color;
  ctx.textBaseline = "top"; // 文本基线对齐方式
  // 在指定位置绘制文本
  let x = options.position.x;
  let y = options.position.y;
  if (typeof options.position.x === "string" && options.position.x.includes("%")) {
    let _x = options.position.x.replace("%", "");
    x = (img.width * Math.round(_x * 100)) / 10000;
  }
  if (typeof options.position.y === "string" && options.position.y.includes("%")) {
    let _y = options.position.y.replace("%", "");
    y = (img.height * Math.round(_y * 100)) / 10000;
  }
  ctx.translate(img.width - x, img.height - y);
  ctx.rotate((-45 * Math.PI) / 180);
  ctx.fillText(options.text, 0, 0);
  ctx.restore();

  if (canvas.toDataURL("image/webp").startsWith("data:image/webp")) {
    console.log("支持 WEBP");
    return canvas.toDataURL("'image/webp'", 0.9);
  } else {
    return canvas.toDataURL("image/jpeg", 0.9);
  }
}

// 图片添加 文字 作为占位图 用于生成海报
export function imgAddText(textOption = { title, compile, publish }, imgUrl) {
  return new Promise((resolve, reject) => {
    const img = new Image();
    img.crossOrigin = "anonymous"; // 解决跨域问题
    let _text = textOption;
    img.onload = function () {
      // 绘制原来图片到canvas上
      let responseImageUrl = addText(img, _text);

      resolve(responseImageUrl);
    };
    img.onerror = function () {
      reject("error");
    };

    img.src = imgUrl;
  });
}
function addText(img, textOption) {
  const canvas = document.createElement("canvas");
  const ctx = canvas.getContext("2d");
  canvas.width = img.width;
  canvas.height = img.height;
  ctx.drawImage(img, 0, 0, img.width, img.height);
  let center_x = canvas.width / 2;

  // 在指定位置绘制文本
  let x = center_x;
  let y = 200;
  let titleText = textOption.title;
  let titleTextLength = titleText.length;
  let loopCount = Math.ceil(titleTextLength / 9);
  switch (loopCount) {
    case 1:
      y = 300;
      break;
    case 2:
      y = 230;
      break;
    case 3:
      y = 200;
      break;
    default:
      y = 200;
      break;
  }
  for (let i = 0; i < titleTextLength; i += 9) {
    let ly = y + (i / 9) * 100 * 1.2;
    titleLoop(ctx, titleText.slice(i, i + 9), x, ly);
  }
  // 标题文字处理循环
  function titleLoop(ctx, text, x, ly) {
    ctx.save();
    ctx.font = "100px miSans";
    ctx.fillStyle = "rgb(255,255,255)";
    ctx.textBaseline = "top"; // 文本基线对齐方式
    // 在指定位置绘制文本
    ctx.textAlign = "center";
    ctx.fillText(text, x, ly);
    ctx.restore();
  }
  // 添加编译文字
  ctx.save();
  ctx.font = "36px miSans";
  ctx.fillStyle = "rgb(255, 255, 255)";
  ctx.textBaseline = "top"; // 文本基线对齐方式
  // 在指定位置绘制文本
  x = center_x;
  y = 546;
  ctx.textAlign = "center";
  ctx.fillText(textOption.compile + "  /  著", x, y);
  ctx.restore();
  // 添加发布文字
  ctx.save();
  ctx.font = " 18px miSans";
  ctx.fillStyle = "rgb(255, 255, 255)";
  ctx.textBaseline = "top"; // 文本基线对齐方式
  // 在指定位置绘制文本
  x = center_x;
  y = 1090;
  ctx.textAlign = "center";
  ctx.fillText(textOption.publish, x, y);
  ctx.restore();

  return canvas.toDataURL("image/jpeg", 0.9);
}

// 创建水印画布
export function createWatermarkDom(el, example) {
  let canvas = document.createElement("canvas");
  canvas.style.width = "100%";
  canvas.style.height = "100%";
  canvas.style.position = "absolute";
  canvas.style.zIndex = 3;
  el.appendChild(canvas);
  example.watermark = canvas;
}
// 创建水印
export function createWatermark(canvas, test = ["测试", new Date().toLocaleDateString(), "12345678901"]) {
  // console.log("xxxxx-------", test);
  // scale 用来处理canvas 模糊
  var scale = window.devicePixelRatio;
  canvas.width = Math.floor(canvas.clientWidth * scale);
  canvas.height = Math.floor(canvas.clientHeight * scale);

  const ctx = canvas.getContext("2d");
  ctx.scale(scale, scale);
  ctx.clearRect(0, 0, canvas.width, canvas.height); //清除画布

  let wmConfig = {
    font: "fangsong",
    textArray: test,
    density: 2,
  };
  let fontSize = 12;
  let imgWidth = canvas.clientWidth;
  let imgHeight = canvas.clientHeight;

  ctx.font = `${fontSize}px ${wmConfig.font}`;
  ctx.lineWidth = 2;
  ctx.fillStyle = "rgba(0, 0, 0, 0.5)";
  ctx.textAlign = "center";
  ctx.textBaseline = "top";
  // //文字坐标
  const maxPx = imgWidth;
  const maxYPx = imgHeight;
  const stepXPx = Math.floor(maxPx / wmConfig.density);
  const stepYPx = Math.floor(maxYPx / wmConfig.density);
  let arrayX = [0]; //初始水印位置 canvas坐标 0 0 点
  let arrayY = [0];
  while (arrayX[arrayX.length - 1] < maxPx) {
    arrayX.push(arrayX[arrayX.length - 1] + stepXPx);
  }
  while (arrayY[arrayY.length - 1] < maxYPx) {
    arrayY.push(arrayY[arrayY.length - 1] + stepYPx);
  }
  for (let i = 0; i < arrayX.length; i++) {
    for (let j = 0; j < arrayY.length; j++) {
      ctx.save();
      ctx.translate(60, 40); ///画布旋转原点 移到 图片中心
      ctx.rotate((-20 * Math.PI) / 180);

      wmConfig.textArray.forEach((el, index) => {
        let offsetY = fontSize * index;
        ctx.fillText(el, arrayX[i], arrayY[j] + offsetY);
      });
      ctx.restore();
    }
  }
}
