<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>Car Rental SystemLogin</h2>
        </div>
      </template>
      
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
        <el-form-item label="Username" prop="userName">
          <el-input
            v-model="loginForm.userName"
            placeholder="Enter your username"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="Password" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="Enter your password"
            show-password
            clearable
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">
            Login
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="register-link">
        No account yet?
        <a href="#" @click.prevent="router.push('/register')">Register now</a>
      </div>

      <div class="tips">
        <p>Admin account: admin / admin123</p>
        <p>Regular user: user1 / 123456</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/modules/auth'

const router = useRouter()
const route = useRoute()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  userName: '',
  password: ''
})

const rules = {
  userName: [
    { required: true, message: 'Enter your username', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Enter your password', trigger: 'blur' },
    { min: 6, message: 'Password must be at least 6 characters', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await login(loginForm)
        const userInfo = res.data
        
        // Save user info to localStorage
        localStorage.setItem('userInfo', JSON.stringify(userInfo))
        
        ElMessage.success('Login successful')
        
        const redirect = route.query.redirect
        if (redirect) {
          router.push(redirect)
        } else if (userInfo.role === 1) {
          router.push('/admin/dashboard')
        } else {
          router.push('/')
        }
      } catch (error) {
        ElMessage.error(error || 'Login failed')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 450px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.card-header {
  text-align: center;
}

.card-header h2 {
  margin: 0;
  color: #303133;
}

.register-link {
  text-align: center;
  margin-top: 16px;
  color: #666;
}

.register-link a {
  color: #409eff;
  text-decoration: none;
}

.tips {
  margin-top: 20px;
  padding: 15px;
  background-color: #f4f4f5;
  border-radius: 4px;
  font-size: 14px;
  color: #606266;
}

.tips p {
  margin: 5px 0;
}
</style>
