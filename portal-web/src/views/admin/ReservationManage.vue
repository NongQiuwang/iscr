<template>
  <div class="admin-page">
    <div class="page-header">
      <div class="header-info">
        <h2>预约订单</h2>
        <p>管理所有客户的租车预约申请</p>
      </div>
    </div>

    <div class="admin-card">
      <el-table :data="tableData" v-loading="loading" class="flat-table">
        <el-table-column prop="reservationId" label="单号" width="80" />
        <el-table-column prop="userName" label="客户" width="100" />
        <el-table-column label="预约车辆" min-width="180">
          <template #default="{ row }">
            <span class="car-info-text">{{ row.carBrand }} {{ row.carSeries }}</span>
            <div class="car-model-text">{{ row.carModel }}</div>
          </template>
        </el-table-column>
        <el-table-column label="租期" width="220">
          <template #default="{ row }">
            <div class="date-range">
              <span>{{ row.startDate }}</span>
              <span class="to">至</span>
              <span>{{ row.endDate }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="totalPrice" label="总金额" width="120">
          <template #default="{ row }">
            <span class="price-text">¥{{ row.totalPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag size="small" :type="getStatusType(row.status)" effect="light">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" link type="success" @click="handleConfirm(row)">确认</el-button>
            <el-button v-if="row.status === 1" link type="primary" @click="handleComplete(row)">完成</el-button>
            <el-button link type="danger" @click="handleCancel(row)">取消</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllReservations, updateReservationStatus, cancelReservation } from '@/api/reservation'

const loading = ref(false)
const tableData = ref([])

onMounted(() => fetchData())

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getAllReservations()
    tableData.value = res.data || []
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const getStatusType = (s) => ['warning', 'success', 'danger', 'info'][s] || ''
const getStatusText = (s) => ['待确认', '已确认', '已取消', '已完成'][s] || '未知'

const handleConfirm = async (row) => {
  await updateReservationStatus(row.reservationId, 1)
  ElMessage.success('已确认预约')
  fetchData()
}

const handleComplete = async (row) => {
  await updateReservationStatus(row.reservationId, 3)
  ElMessage.success('订单已完成')
  fetchData()
}

const handleCancel = async (row) => {
  try {
    await ElMessageBox.confirm('确定取消该预约？', '提示', { type: 'warning' })
    await cancelReservation(row.reservationId)
    ElMessage.success('已取消')
    fetchData()
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
}

.car-info-text {
  font-weight: 600;
  color: #333;
}

.car-model-text {
  font-size: 12px;
  color: #999;
}

.date-range {
  font-size: 13px;
  color: #666;
}

.date-range .to {
  margin: 0 8px;
  color: #ccc;
}

.price-text {
  color: #f56c6c;
  font-weight: 600;
}
</style>
