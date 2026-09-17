<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <h2>Create Account</h2>
        </div>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="Username" prop="userName">
          <el-input v-model="form.userName" placeholder="Enter your username" clearable />
        </el-form-item>
        <el-form-item label="Password" prop="password">
          <el-input v-model="form.password" type="password" placeholder="Enter your password" show-password />
        </el-form-item>
        <el-form-item label="ConfirmPassword" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="Re-enter your password" show-password />
        </el-form-item>
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="form.phone" placeholder="Enter your phone number" clearable />
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="form.email" placeholder="Enter your email" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="submit-btn" :loading="loading" @click="handleSubmit">
            Register
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-link">
        Already have an account?
        <a href="#" @click.prevent="$router.push('/login')">Back to login</a>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '@/api/modules/auth'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  userName: '',
  password: '',
  confirmPassword: '',
  phone: '',
  email: ''
})

const rules = {
  userName: [{ required: true, message: 'Enter your username', trigger: 'blur' }, { min: 2, max: 20, message: 'Must be 2-20 characters', trigger: 'blur' }],
  password: [{ required: true, message: 'Enter your password', trigger: 'blur' }, { min: 6, max: 64, message: 'Must be 6-64 characters', trigger: 'blur' }],
  confirmPassword: [{
    validator: (_, value, callback) => {
      if (!value) callback(new Error('Re-enter your password'))
      else if (value !== form.password) callback(new Error('Passwords do not match'))
      else callback()
    },
    trigger: 'blur'
  }],
  phone: [{ required: true, message: 'Enter your phone number', trigger: 'blur' }, { pattern: /^1[3-9]\d{9}$/, message: 'Invalid phone format', trigger: 'blur' }],
  email: [{ required: true, message: 'Enter your email', trigger: 'blur' }, { type: 'email', message: 'Invalid email format', trigger: 'blur' }]
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      const { userName, password, phone, email } = form
      await register({ userName, password, phone, email })
      ElMessage.success('Registration successful, please log in')
      router.push('/login')
    } catch (error) {
      ElMessage.error(error || 'Registration failed')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-card {
  width: 450px;
}

.card-header {
  text-align: center;
}

.card-header h2 {
  margin: 0;
}

.submit-btn {
  width: 100%;
}

.login-link {
  text-align: center;
  margin-top: 16px;
  color: #666;
}

.login-link a {
  color: #409eff;
  text-decoration: none;
}
</style>
