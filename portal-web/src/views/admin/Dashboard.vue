<template>
  <div class="dashboard">
    <div class="welcome-header">
      <h1>数据概览</h1>
      <p>欢迎回来，{{ userInfo?.username }}。这是系统目前的运行状态。</p>
    </div>

    <el-row :gutter="20">
      <el-col :span="6" v-for="item in statCards" :key="item.label">
        <div class="stat-box">
          <div class="stat-icon" :style="{ color: item.color, backgroundColor: item.bg }">
            <el-icon :size="24"><component :is="item.icon" /></el-icon>
          </div>
          <div class="stat-data">
            <div class="stat-value">{{ item.value }}</div>
            <div class="stat-label">{{ item.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 24px">
      <el-col :span="16">
        <div class="admin-card">
          <div class="card-title">最近预约</div>
          <el-table :data="recentReservations" style="width: 100%" size="small">
            <el-table-column prop="userName" label="客户" width="100" />
            <el-table-column label="车辆">
              <template #default="{ row }">
                {{ row.carBrand }} {{ row.carSeries }}
              </template>
            </el-table-column>
            <el-table-column prop="startDate" label="日期" width="120" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag size="small" :type="row.status === 0 ? 'warning' : 'success'">
                  {{ row.status === 0 ? '待处理' : '已确认' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="admin-card">
          <div class="card-title">系统公告</div>
          <div class="notice-list">
            <div class="notice-item">
              <span class="dot"></span>
              <div class="notice-text">
                <p>系统已升级至高端视觉版本</p>
                <small>2026-01-26</small>
              </div>
            </div>
            <div class="notice-item">
              <span class="dot"></span>
              <div class="notice-text">
                <p>春节期间租车业务调整通知</p>
                <small>2026-01-20</small>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Van, User, List, CircleCheck } from '@element-plus/icons-vue'
import { getAllCars } from '@/api/car'
import { getAllReservations } from '@/api/reservation'

const userInfo = ref(null)
const stats = ref({
  totalCars: 0,
  totalUsers: 2,
  totalReservations: 0,
  availableCars: 0
})

const recentReservations = ref([])

const statCards = ref([
  { label: '总车辆', value: 0, icon: Van, color: '#409eff', bg: '#ecf5ff' },
  { label: '注册用户', value: 2, icon: User, color: '#67c23a', bg: '#f0f9eb' },
  { label: '预约订单', value: 0, icon: List, color: '#e6a23c', bg: '#fdf6ec' },
  { label: '空闲车辆', value: 0, icon: CircleCheck, color: '#f56c6c', bg: '#fef0f0' }
])

onMounted(async () => {
  const savedUserInfo = localStorage.getItem('userInfo')
  if (savedUserInfo) userInfo.value = JSON.parse(savedUserInfo)

  try {
    const carsRes = await getAllCars()
    const cars = carsRes.data || []
    stats.value.totalCars = cars.length
    stats.value.availableCars = cars.filter(car => car.status === 0).length

    const resRes = await getAllReservations()
    const reservations = resRes.data || []
    stats.value.totalReservations = reservations.length
    recentReservations.value = reservations.slice(0, 5)

    // 更新卡片数据
    statCards.value[0].value = stats.value.totalCars
    statCards.value[1].value = stats.value.totalUsers
    statCards.value[2].value = stats.value.totalReservations
    statCards.value[3].value = stats.value.availableCars
  } catch (error) {
    console.error('加载统计失败', error)
  }
})
</script>

<style scoped>
.welcome-header {
  margin-bottom: 24px;
}

.welcome-header h1 {
  font-size: 24px;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.welcome-header p {
  color: #999;
  font-size: 14px;
}

.stat-box {
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 16px;
  border: 1px solid #f0f2f5;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #1a1a1a;
  line-height: 1;
}

.stat-label {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.admin-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  border: 1px solid #f0f2f5;
  height: 100%;
}

.card-title {
  font-size: 16px;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 20px;
}

.notice-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.notice-item {
  display: flex;
  gap: 12px;
}

.dot {
  width: 8px;
  height: 8px;
  background: #409eff;
  border-radius: 50%;
  margin-top: 6px;
  flex-shrink: 0;
}

.notice-text p {
  font-size: 14px;
  color: #333;
  margin-bottom: 2px;
}

.notice-text small {
  color: #bbb;
}
</style>
