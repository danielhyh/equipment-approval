import { createPinia } from "pinia";

export function createPiniaStore(App) {
  const pinia = createPinia();
  App.use(pinia);
}
