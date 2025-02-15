import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import { Button, message } from "ant-design-vue";

createApp(App).use(Button).use(router).mount("#app");
