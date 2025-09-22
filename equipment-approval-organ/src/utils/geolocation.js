// 前端地理定位工具函数

/**
 * 获取当前位置的经纬度
 * @param {Object} options - 定位选项
 * @param {boolean} options.enableHighAccuracy - 是否启用高精度定位
 * @param {number} options.timeout - 超时时间（毫秒）
 * @param {number} options.maximumAge - 位置缓存时间（毫秒）
 * @returns {Promise} 返回包含经纬度信息的Promise
 */
export const getCurrentPosition = (options = {
  enableHighAccuracy: true,
  timeout: 10000,
  maximumAge: 60000
}) => {
  return new Promise((resolve, reject) => {
    // 检查浏览器是否支持地理定位
    if (!navigator.geolocation) {
      reject(new Error('您的浏览器不支持地理定位功能'));
      return;
    }

    // 调用getCurrentPosition方法获取位置
    navigator.geolocation.getCurrentPosition(
      // 成功回调
      (position) => {
        resolve({
          latitude: position.coords.latitude,    // 纬度
          longitude: position.coords.longitude,  // 经度
          accuracy: position.coords.accuracy,    // 精度（米）
          altitude: position.coords.altitude,    // 海拔高度（如果可用）
          altitudeAccuracy: position.coords.altitudeAccuracy,  // 海拔精度（如果可用）
          heading: position.coords.heading,      // 航向（如果可用）
          speed: position.coords.speed,          // 速度（如果可用）
          timestamp: position.timestamp          // 时间戳
        });
      },
      // 错误回调
      (error) => {
        let errorMessage = '';
        switch (error.code) {
          case error.PERMISSION_DENIED:
            errorMessage = '用户拒绝了地理定位请求';
            break;
          case error.POSITION_UNAVAILABLE:
            errorMessage = '位置信息不可用';
            break;
          case error.TIMEOUT:
            errorMessage = '获取位置超时';
            break;
          case error.UNKNOWN_ERROR:
            errorMessage = '发生未知错误';
            break;
          default:
            errorMessage = '获取位置失败';
        }
        reject(new Error(errorMessage));
      },
      // 定位选项
      options
    );
  });
};

/**
 * 监听位置变化
 * @param {Function} successCallback - 位置更新成功的回调函数
 * @param {Function} errorCallback - 位置更新失败的回调函数
 * @param {Object} options - 定位选项
 * @returns {number} 返回监听器ID，可用于清除监听
 */
export const watchPosition = (successCallback, errorCallback, options = {
  enableHighAccuracy: true,
  timeout: 10000,
  maximumAge: 60000
}) => {
  if (!navigator.geolocation) {
    if (errorCallback) {
      errorCallback(new Error('您的浏览器不支持地理定位功能'));
    }
    return null;
  }

  return navigator.geolocation.watchPosition(
    (position) => {
      if (successCallback) {
        successCallback({
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
          accuracy: position.coords.accuracy,
          altitude: position.coords.altitude,
          altitudeAccuracy: position.coords.altitudeAccuracy,
          heading: position.coords.heading,
          speed: position.coords.speed,
          timestamp: position.timestamp
        });
      }
    },
    (error) => {
      let errorMessage = '';
      switch (error.code) {
        case error.PERMISSION_DENIED:
          errorMessage = '用户拒绝了地理定位请求';
          break;
        case error.POSITION_UNAVAILABLE:
          errorMessage = '位置信息不可用';
          break;
        case error.TIMEOUT:
          errorMessage = '获取位置超时';
          break;
        default:
          errorMessage = '获取位置失败';
      }
      if (errorCallback) {
        errorCallback(new Error(errorMessage));
      }
    },
    options
  );
};

/**
 * 清除位置监听
 * @param {number} watchId - 监听器ID
 */
export const clearWatch = (watchId) => {
  if (navigator.geolocation && watchId !== null) {
    navigator.geolocation.clearWatch(watchId);
  }
};

/**
 * 百度地图逆地理编码（将经纬度转换为地址信息）
 * 注意：使用前需要在页面引入百度地图API脚本，并替换为自己的ak
 * @param {number} latitude - 纬度
 * @param {number} longitude - 经度
 * @param {string} ak - 百度地图API密钥
 * @returns {Promise} 返回包含地址信息的Promise
 */
export const getAddressByBaiduMap = (latitude, longitude, ak) => {
  return new Promise((resolve, reject) => {
    if (!window.BMap) {
      reject(new Error('请先引入百度地图API脚本'));
      return;
    }

    const geocoder = new window.BMap.Geocoder();
    const point = new window.BMap.Point(longitude, latitude);

    geocoder.getLocation(point, (result) => {
      if (result) {
        resolve({
          address: result.address,
          addressComponents: result.addressComponents,
          point: result.point
        });
      } else {
        reject(new Error('逆地理编码失败'));
      }
    });
  });
};

/**
 * 高德地图逆地理编码
 * 注意：使用前需要引入高德地图API，或使用axios等工具发送请求
 * @param {number} latitude - 纬度
 * @param {number} longitude - 经度
 * @param {string} key - 高德地图API密钥
 * @returns {Promise} 返回包含地址信息的Promise
 */
export const getAddressByAmap = async (latitude, longitude, key) => {
  try {
    const axios = await import('axios');
    const url = `https://restapi.amap.com/v3/geocode/regeo?location=${longitude},${latitude}&key=${key}`;
    const response = await axios.default.get(url);
    
    if (response.data.status === '1') {
      return response.data.regeocode;
    } else {
      throw new Error('逆地理编码失败：' + response.data.info);
    }
  } catch (error) {
    throw new Error('获取地址信息失败：' + error.message);
  }
};

/**
 * 获取当前位置并转换为地址信息（使用百度地图）
 * @param {string} ak - 百度地图API密钥
 * @returns {Promise} 返回包含位置和地址信息的Promise
 */
export const getCurrentLocationAndAddress = async (ak) => {
  try {
    const position = await getCurrentPosition();
    const address = await getAddressByBaiduMap(position.latitude, position.longitude, ak);
    
    return {
      ...position,
      addressInfo: address
    };
  } catch (error) {
    throw error;
  }
};

/**
 * 检查定位权限状态
 * @returns {Promise<string>} 返回权限状态：'granted' | 'denied' | 'prompt'
 */
export const checkLocationPermission = () => {
  return new Promise((resolve) => {
    if (!navigator.permissions || !navigator.geolocation) {
      // 不支持permissions API或geolocation的浏览器，返回'prompt'
      resolve('prompt');
      return;
    }

    navigator.permissions.query({ name: 'geolocation' }).then((result) => {
      resolve(result.state);
    }).catch(() => {
      resolve('prompt');
    });
  });
};

/**
 * 位置工具类
 */
export class LocationHelper {
  constructor(options = {}) {
    this.options = {
      enableHighAccuracy: true,
      timeout: 10000,
      maximumAge: 60000,
      ...options
    };
    this.watchId = null;
  }

  // 获取当前位置
  getCurrentPosition() {
    return getCurrentPosition(this.options);
  }

  // 开始监听位置变化
  startWatching(successCallback, errorCallback) {
    this.stopWatching();
    this.watchId = watchPosition(successCallback, errorCallback, this.options);
    return this.watchId;
  }

  // 停止监听位置变化
  stopWatching() {
    if (this.watchId !== null) {
      clearWatch(this.watchId);
      this.watchId = null;
    }
  }

  // 获取地址信息
  async getAddress(latitude, longitude, mapType = 'baidu', key) {
    if (mapType === 'baidu') {
      return getAddressByBaiduMap(latitude, longitude, key);
    } else if (mapType === 'amap') {
      return getAddressByAmap(latitude, longitude, key);
    }
    throw new Error('不支持的地图类型');
  }
}