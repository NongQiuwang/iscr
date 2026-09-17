<template>
  <div class="admin-page">
    <div class="page-header">
      <div class="header-info">
        <h2>Reservations</h2>
        <p>Manage all customer rental reservation requests</p>
      </div>
    </div>

    <div class="admin-card">
      <el-table :data="tableData" v-loading="loading" class="flat-table">
        <el-table-column prop="reservationId" label="No." width="80" />
        <el-table-column prop="userName" label="Customer" width="100" />
        <el-table-column label="Vehicle" min-width="180">
          <template #default="{ row }">
            <span class="car-info-text">{{ row.carBrand }} {{ row.carSeries }}</span>
            <div class="car-model-text">{{ row.carModel }}</div>
          </template>
        </el-table-column>
        <el-table-column label="Rental Period" width="220">
          <template #default="{ row }">
            <div class="date-range">
              <span>{{ row.startDate }}</span>
              <span class="to">to</span>
              <span>{{ row.endDate }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="totalPrice" label="Total" width="120">
          <template #default="{ row }">
            <span class="price-text">¥{{ row.totalPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="Status" width="100">
          <template #default="{ row }">
            <el-tag size="small" :type="getStatusType(row.status)" effect="light">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="180" align="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" link type="success" @click="handleConfirm(row)">Confirm</el-button>
            <el-button v-if="row.status === 1" link type="primary" @click="handleComplete(row)">Complete</el-button>
            <el-button v-if="row.status === 0 || row.status === 1" link type="danger" @click="handleCancel(row)">Cancel</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllReservations, updateReservationStatus, cancelReservation } from '@/api/modules/reservation'

const loading = ref(false)
const tableData = ref([])

onMounted(() => fetchData())

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getAllReservations()
    tableData.value = res.data || []
  } catch (e) {
    ElMessage.error('Failed to load')
  } finally {
    loading.value = false
  }
}

const getStatusType = (s) => ['warning', 'success', 'danger', 'info'][s] || ''
const getStatusText = (s) => ['Pending', 'Confirmed', 'Cancelled', 'Completed'][s] || 'Unknown'

const handleConfirm = async (row) => {
  await updateReservationStatus(row.reservationId, 1)
  ElMessage.success('Reservation confirmed')
  fetchData()
}

const handleComplete = async (row) => {
  await updateReservationStatus(row.reservationId, 3)
  ElMessage.success('Order completed')
  fetchData()
}

const handleCancel = async (row) => {
  try {
    await ElMessageBox.confirm('Cancel this reservation?', 'Notice', { type: 'warning' })
    await cancelReservation(row.reservationId)
    ElMessage.success('Cancelled')
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
