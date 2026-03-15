import axios from 'axios'
import { Message } from '@arco-design/web-vue'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

http.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 0) {
      return res
    }
    Message.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      localStorage.removeItem('role')
      Message.error('登录已过期，请重新登录')
      window.location.hash = ''
      window.location.href = '/login'
    } else if (error.response?.status === 403) {
      Message.error('您没有权限访问该功能，请确认登录状态')
    } else {
      Message.error(error.response?.data?.message || '网络异常，请稍后重试')
    }
    return Promise.reject(error)
  },
)

export default http
