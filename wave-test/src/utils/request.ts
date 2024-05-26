import router from '@/router'
import { userModuleStore } from '@/stores/user'
import axios from 'axios'
import {ElMessage } from 'element-plus'

const service = axios.create({
  baseURL: '/api',
  timeout: 50000,
})

const userStore = userModuleStore()

service.interceptors.request.use(
  (config) => {
    
    const token = userStore.token
  
    if (token) {
      config.headers['token'] = token
    }
    const username = userStore.username
    if(username) {
      config.headers['username'] = username
    }

    return config
  },
  (error) => {
    Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.status !== 'SUCCESS') {
      ElMessage({
        message: res.errorMessage || 'Error',
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return response.data
    }
  },
  async (error) => {
    if(error.response.status === 401 && userStore.refreshToken) {
      const data: any = await axios({
        headers: {
          "username": userStore.username,
          "refresh-token": userStore.refreshToken
        },
        url: '/api/user/refresh/token',
        method: 'post'
      })
      if(data.status === 'SUCCESS') {
          userStore.token = data.data.token
          userStore.username = data.data.name
          userStore.refreshToken = data.data.refreshToken
        return service(error.config)
      }
    }
    
    if(error.response.status === 401) {
      ElMessage({
        message: '请重新登录后操作',
        type: 'error',
        duration: 5 * 1000
      })
      router.push('/login')
    } else if(error.response.status === 500) {
      ElMessage({
        message: '服务器异常，请稍后重试',
        type: 'error',
        duration: 5 * 1000
      })
    } else{
      ElMessage({
        message: error.errorMessage,
        type: 'error',
        duration: 5 * 1000
      })
    }
    
    
    
    return Promise.reject(error)
  }
)

export default service
