import { createApp } from "vue";
import "./style.css";
import App from "./App.vue";

const app = createApp(App);

// element-plus
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
app.use(ElementPlus);

// 配置全局api
import api from "@/api";
app.config.globalProperties.$api = api;

app.mount("#app");
