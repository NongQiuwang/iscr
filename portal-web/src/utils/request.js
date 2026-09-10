import axios from 'axios'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8080/iscr', // 后端API地址
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 从localStorage获取用户信息
    const userInfo = localStorage.getItem('userInfo')
    if (userInfo) {
      const user = JSON.parse(userInfo)
      // 可以在这里添加token等认证信息
      config.headers['userId'] = user.userId
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    // 根据后端返回的状态码判断
    if (res.code === 0) {
      return res
    } else {
      console.error('业务错误:', res.message)
      return Promise.reject(res.message || '请求失败')
    }
  },
  error => {
    console.error('响应错误:', error)
    return Promise.reject(error)
  }
)

export default request
