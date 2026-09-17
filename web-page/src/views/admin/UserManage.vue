<template>
  <div class="admin-page">
    <div class="page-header">
      <div class="header-info">
        <h2>Users</h2>
        <p>View and manage registered users</p>
      </div>
      <div class="header-actions">
        <el-input v-model="keyword" placeholder="Search username / email / phone" clearable class="search-input" />
        <el-button type="primary" @click="openCreate">Add User</el-button>
      </div>
    </div>

    <div class="admin-card">
      <el-table :data="filteredData" v-loading="loading" class="flat-table">
        <el-table-column prop="uId" label="ID" width="70" />
        <el-table-column label="User Info" min-width="220">
          <template #default="{ row }">
            <div class="user-cell">
              <el-avatar :size="32" class="user-avatar">{{ (row.userName || '?').charAt(0).toUpperCase() }}</el-avatar>
              <div class="user-text">
                <div class="user-name">{{ row.userName }}</div>
                <div class="user-email">{{ row.email }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="Phone" width="140" />
        <el-table-column prop="role" label="Role" width="110">
          <template #default="{ row }">
            <el-tag size="small" :type="row.role === 1 ? 'danger' : 'info'" effect="plain">
              {{ row.role === 1 ? 'Admin' : 'Regular User' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="150" align="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">Edit</el-button>
            <el-button link type="danger" @click="handleDelete(row)" :disabled="row.role === 1">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="createVisible" title="Add User" width="460px">
      <el-form :model="createForm" :rules="createRules" ref="createRef" label-width="80px">
        <el-form-item label="Username" prop="userName">
          <el-input v-model="createForm.userName" placeholder="2-20 characters" clearable />
        </el-form-item>
        <el-form-item label="Password" prop="password">
          <el-input v-model="createForm.password" type="password" placeholder="6-64 characters" show-password />
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="createForm.email" placeholder="Enter your email" clearable />
        </el-form-item>
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="createForm.phone" placeholder="11-digit phone number" clearable />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createVisible = false">Cancel</el-button>
        <el-button type="primary" :loading="submitting" @click="submitCreate">OK</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="editVisible" title="Edit User" width="460px">
      <el-form :model="editForm" :rules="editRules" ref="editRef" label-width="80px">
        <el-form-item label="Username">
          <el-input v-model="editForm.userName" disabled />
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="editForm.email" placeholder="Enter your email" clearable />
        </el-form-item>
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="editForm.phone" placeholder="11-digit phone number" clearable />
        </el-form-item>
        <el-form-item label="Role" prop="role">
          <el-select v-model="editForm.role" placeholder="Select role">
            <el-option label="Regular User" :value="0" />
            <el-option label="Admin" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="ResetPassword" prop="newPassword">
          <el-input v-model="editForm.newPassword" type="password" placeholder="Leave blank to keep unchanged" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">Cancel</el-button>
        <el-button type="primary" :loading="submitting" @click="submitEdit">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllUsers, deleteUser, updateUserInfo } from '@/api/modules/user'
import { register } from '@/api/modules/auth'

const loading = ref(false)
const tableData = ref([])
const keyword = ref('')

const emailPattern = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/
const phonePattern = /^1[3-9]\d{9}$/

const filteredData = computed(() => {
  const k = keyword.value.trim().toLowerCase()
  if (!k) return tableData.value
  return tableData.value.filter((u) =>
    [u.userName, u.email, u.phone].some((v) => v && String(v).toLowerCase().includes(k))
  )
})

onMounted(() => fetchData())

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getAllUsers()
    tableData.value = res.data || []
  } catch (e) {
    ElMessage.error('Failed to load users')
  } finally {
    loading.value = false
  }
}

const createVisible = ref(false)
const submitting = ref(false)
const createRef = ref(null)
const createForm = reactive({ userName: '', password: '', email: '', phone: '' })

const createRules = {
  userName: [
    { required: true, message: 'Enter your username', trigger: 'blur' },
    { min: 2, max: 20, message: 'Must be 2-20 characters', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Enter your password', trigger: 'blur' },
    { min: 6, max: 64, message: 'Must be 6-64 characters', trigger: 'blur' }
  ],
  email: [
    { required: true, message: 'Enter your email', trigger: 'blur' },
    { pattern: emailPattern, message: 'Invalid email format', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: 'Enter your phone number', trigger: 'blur' },
    { pattern: phonePattern, message: 'Invalid phone format', trigger: 'blur' }
  ]
}

const openCreate = () => {
  Object.assign(createForm, { userName: '', password: '', email: '', phone: '' })
  createVisible.value = true
}

const submitCreate = () => {
  createRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      await register({ ...createForm })
      ElMessage.success('Added successfully')
      createVisible.value = false
      fetchData()
    } catch (e) {
      ElMessage.error(e.message || 'Failed to add')
    } finally {
      submitting.value = false
    }
  })
}

const editVisible = ref(false)
const editRef = ref(null)
const editForm = reactive({ uId: null, userName: '', email: '', phone: '', role: 0, newPassword: '' })

const editRules = {
  email: [{ pattern: emailPattern, message: 'Invalid email format', trigger: 'blur' }],
  phone: [{ pattern: phonePattern, message: 'Invalid phone format', trigger: 'blur' }],
  newPassword: [{ min: 6, max: 64, message: 'PasswordMust be 6-64 characters', trigger: 'blur' }]
}

const openEdit = (row) => {
  Object.assign(editForm, {
    uId: row.uId,
    userName: row.userName,
    email: row.email || '',
    phone: row.phone || '',
    role: row.role ?? 0,
    newPassword: ''
  })
  editVisible.value = true
}

const submitEdit = () => {
  editRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      const payload = { email: editForm.email, phone: editForm.phone, role: editForm.role }
      if (editForm.newPassword) payload.password = editForm.newPassword
      await updateUserInfo(editForm.uId, payload)
      ElMessage.success('Saved')
      editVisible.value = false
      fetchData()
    } catch (e) {
      ElMessage.error(e.message || 'Save failed')
    } finally {
      submitting.value = false
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`Delete user "${row.userName}"? This action is irreversible.`, 'Warning', {
      type: 'error',
      confirmButtonText: 'Delete',
      confirmButtonClass: 'el-button--danger'
    })
    await deleteUser(row.uId)
    ElMessage.success('User deleted')
    fetchData()
  } catch (e) {}
}
</script>

<style scoped>
.admin-page { display: flex; flex-direction: column; gap: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; }
.page-header h2 { font-size: 20px; font-weight: 700; color: #1a1a1a; margin-bottom: 4px; }
.page-header p { color: #999; font-size: 13px; }
.header-actions { display: flex; gap: 10px; align-items: center; }
.search-input { width: 240px; }
.admin-card { background: #fff; border-radius: 12px; padding: 24px; border: 1px solid #f0f2f5; }
.flat-table :deep(.el-table__header) th { background-color: #f9fafb; color: #666; font-weight: 600; font-size: 13px; }
.user-cell { display: flex; align-items: center; gap: 12px; }
.user-avatar { background: #409eff; color: #fff; font-weight: 600; }
.user-name { font-weight: 600; color: #333; }
.user-email { font-size: 12px; color: #999; }
</style>
