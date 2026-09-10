<template>
  <div class="admin-page">
    <div class="page-header">
      <div class="header-info">
        <h2>用户列表</h2>
        <p>查看并管理系统注册用户</p>
      </div>
    </div>

    <div class="admin-card">
      <el-table :data="tableData" v-loading="loading" class="flat-table">
        <el-table-column prop="uId" label="ID" width="80" />
        <el-table-column label="用户信息">
          <template #default="{ row }">
            <div class="user-cell">
              <el-avatar :size="32" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
              <div class="user-text">
                <div class="user-name">{{ row.userName }}</div>
                <div class="user-email">{{ row.email }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="150" />
        <el-table-column prop="role" label="权限" width="120">
          <template #default="{ row }">
            <el-tag size="small" :type="row.role === 1 ? 'danger' : 'info'" effect="plain">
              {{ row.role === 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="right">
          <template #default="{ row }">
            <el-button link type="danger" @click="handleDelete(row)" :disabled="row.role === 1">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const tableData = ref([
  { uId: 1, userName: 'admin', email: 'admin@example.com', phone: '13800138000', role: 1 },
  { uId: 2, userName: 'user1', email: 'user1@example.com', phone: '13800138001', role: 0 }
])

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定注销该用户？此操作不可逆。', '警告', {
      type: 'error',
      confirmButtonText: '确定注销',
      confirmButtonClass: 'el-button--danger'
    })
    ElMessage.success('用户已注销')
  } catch (e) {}
}
</script>

<style scoped>
.admin-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.page-header h2 {
  font-size: 20px;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.page-header p {
  color: #999;
  font-size: 13px;
}

.admin-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #f0f2f5;
}

.flat-table :deep(.el-table__header) th {
  background-color: #f9fafb;
  color: #666;
  font-weight: 600;
  font-size: 13px;
}

.user-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-name {
  font-weight: 600;
  color: #333;
}

.user-email {
  font-size: 12px;
  color: #999;
}
</style>
