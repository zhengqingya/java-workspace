import axios, { AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const service = axios.create({
  baseURL: "http://localhost:80",
  // 请求超时时间：50s
  timeout: 50000,
  headers: { 'Content-Type': 'application/json;charset=utf-8' },
})

// 请求拦截器
service.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    if (!config.headers) {
      throw new Error(`Expected 'config' and 'config.headers' not to be undefined`)
    }
    // 租户ID
    config.headers['TENANT_ID'] = '1'
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data
    const { code, msg } = res
    if (code === 200) {
      return res.data
    } else {
      ElMessage({
        message: msg || '系统出错',
        type: 'error',
        duration: 5 * 1000,
      })
    }
  },
  (error) => {
    const { msg } = error.response.data
    ElMessage({
      message: '网络异常，请稍后再试!',
      type: 'error',
      duration: 5 * 1000,
    })
    return Promise.reject(new Error(msg || 'Error'))
  },
)

// 导出实例
export default service
