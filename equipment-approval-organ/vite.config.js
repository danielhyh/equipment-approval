import { defineConfig, loadEnv } from "vite";
import vue from "@vitejs/plugin-vue";
// 引入 element-plus  element-plus-icon 自动导入
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import Icons from "unplugin-icons/vite";
import IconsResolver from "unplugin-icons/resolver";
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";
// 引入 资源压缩插件
import viteCompression from "vite-plugin-compression";
// 引入 svg 精灵图 配置
import { createSvgIconsPlugin } from "vite-plugin-svg-icons";
// 引入 @vue/compat 插件（如果需要）
// import vueCompat from "@vue/compat";

import path from "path";

// https://vite.dev/config/
export default defineConfig(({ command, mode }) => {
  let modeEnv = loadEnv(mode, process.cwd());
  console.log(modeEnv, "环境");
  return {
    base: "/",
    resolve: {
      alias: {
        // vue: "@vue/compat", // 别名配置，确保使用兼容性版本的 Vue
        "@": path.resolve(__dirname, "./src"),
        "@views": path.resolve(__dirname, "./src/views"),
      },
    },
    define: {
      "process.env": {},
    },
    server: {
      host: "0.0.0.0",
      port: "8080",
      open: false, // 浏览器自动打开
      https: false,
      ssr: false,
      // proxy: {
      //   '/wx': {
      //     target: 'http://wwlocal.qq.com/cgi-bin/',
      //     changeOrigin: true,//是否跨域
      //     rewrite: (path) => path.replace(/^\/wx/, '')//重写路径
      //   },
      // }
    },
    build: {
      target: "es2015",
      outDir: "dist",
      assetsDir: "/assets",
      minify: "terser",
      terserOptions: {
        compress: {
          drop_console: false,
          drop_debugger: true,
        },
      },
      sourcemap: false,
      chunkSizeWarningLimit: 4096,
      reportCompressedSize: false,
      rollupOptions: {
        input: {
          //在构建过程中，你只需指定多个 .html 文件作为入口点即可：
          main: path.resolve(__dirname, "index.html"), //默认index页面
          //   large: path.resolve(__dirname, 'large.html'),//large //http://localhost:3000/large.html
          // blogs: resolve(__dirname, 'blogs.html'),//large //http://localhost:3000/blogs.html
        },
        output: {
          //配置打包后的js css 存储目录
          chunkFileNames: "assets/js/[name]-[hash].js",
          entryFileNames: "assets/js/[name]-[hash].js",
          assetFileNames: "assets/[ext]/[name]-[hash].[ext]",
          compact: true,
          manualChunks: {
            vue: ["vue", "vue-router", "pinia", "axios"],
            // echarts: ['echarts'],
          },
        },
      },
    },
    css: {
      preprocessorOptions: {
        scss: {
          api: "modern-compiler", // 关闭scss api 过期提示
          silenceDeprecations: ["legacy-js-api"],
        },
      },
    },
    plugins: [
      vue(),
      // vueCompat(), // 引入 @vue/compat 插件
      AutoImport({
        // imports: ["vue", "vue-router", "pinia", "axios"],
        resolvers: [
          ElementPlusResolver(),
          IconsResolver({
            prefix: "Icon",
          }),
        ],
      }),
      Components({
        resolvers: [
          ElementPlusResolver(),
          IconsResolver({
            enabledCollections: ["ep"], // 'ep' 是 Element Plus 图标集的集合名
          }), // 使用方式 <IEpLocation/>
        ],
      }),
      Icons({
        autoInstall: true,
      }),
      viteCompression({
        verbose: true, // 是否在控制台输出压缩结果
        disable: false, // 是否禁用
        threshold: 10240, // 体积大于 threshold 才会被压缩,单位 b
        algorithm: "gzip", // 压缩算法,可选 [ 'gzip' , 'brotliCompress' ,'deflate' , 'deflateRaw']  gzip 压缩率高，但是体积大
        ext: ".gz", // 生成的压缩包后缀
      }),
      createSvgIconsPlugin({
        iconDirs: [path.resolve(process.cwd(), "src/icons/svg")], // SVG 存放路径
        symbolId: "icon-[name]",
      }),
    ],
  };
});
