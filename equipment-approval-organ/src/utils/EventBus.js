const EventBus = {
  events: {},
  $on(event, callback) {
    if (typeof callback !== 'function') {
      throw new Error('EventBus.$on requires a function as callback');
    }
    if (!this.events[event]) {
      this.events[event] = [];
    }
    this.events[event].push(callback);
  },
  $emit(event, ...args) {
    const callbacks = this.events[event];
    if (callbacks) {
      // 使用 slice() 避免遍历时数组修改的影响
      callbacks.slice().forEach(callback => {
        try {
          callback(...args);
        } catch (e) {
          console.error(`EventBus ${event} handler error:`, e);
        }
      });
    }
  },
  $off(event, callback) {
    const callbacks = this.events[event];
    if (!callbacks) return;

    if (callback) {
      this.events[event] = callbacks.filter(cb => cb !== callback);
    } else {
      // 无参数时清除所有监听器
      delete this.events[event];
    }
  }
};

export default EventBus;
