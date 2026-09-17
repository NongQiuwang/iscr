import axios from 'axios'

// Create axios instance
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/iscr',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Request interceptor
request.interceptors.request.use(
  config => {
    // Read user info from localStorage
    const userInfo = localStorage.getItem('userInfo')
    if (userInfo) {
      try {
        const user = JSON.parse(userInfo)
        if (user.userId) {
          config.headers.userId = user.userId
        }
      } catch (e) {
        localStorage.removeItem('userInfo')
      }
    }
    return config
  },
  error => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// Response interceptor
request.interceptors.response.use(
  response => {
    const res = response.data
    // Check the status code returned by the backend
    if (res.code === 0) {
      return res
    }

    if (res.code === 401) {
      localStorage.removeItem('userInfo')
      window.location.href = '/login'
    }

    console.error('Business error:', res.message)
    return Promise.reject(new Error(res.message || 'Request failed'))
  },
  error => {
    console.error('Response error:', error)
    return Promise.reject(error)
  }
)

export default request
