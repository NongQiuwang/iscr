<template>
  <div class="user-reservations">
    <nav class="navbar navbar-scrolled">
      <div class="nav-container">
        <div class="logo" @click="router.push('/')">ANTIGRAVITY <span>RENTAL</span></div>
        <div class="nav-links">
          <a href="#" @click.prevent="router.push('/')">Back to Home</a>
          <div class="auth-section">
            <el-dropdown trigger="click">
              <span class="user-trigger">
                {{ userInfo?.username }} <el-icon><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleLogout">Sign Out</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
    </nav>

    <main class="main-content fade-in-up">
      <div class="page-header">
        <h1 class="page-title">My Reservations</h1>
        <p class="page-subtitle">Manage your rental trips and reservation records</p>
      </div>

      <div class="reservations-container" v-loading="loading">
        <el-empty v-if="!loading && list.length === 0" description="You have no reservations yet" />
        
        <div v-else class="reservation-list">
          <div v-for="item in list" :key="item.reservationId" class="reservation-card glass-effect">
            <div class="card-body">
              <div class="car-info">
                <div class="car-details">
                  <h3>{{ item.carBrand }} {{ item.carSeries }}</h3>
                  <p class="car-model">{{ item.carModel }}</p>
                </div>
                <div class="reservation-status">
                  <el-tag :type="getStatusType(item.status)" effect="plain">
                    {{ getStatusText(item.status) }}
                  </el-tag>
                </div>
              </div>
              
              <div class="reservation-details">
                <div class="detail-item">
                  <span class="label">Pickup Date</span>
                  <span class="value">{{ item.startDate }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">Return Date</span>
                  <span class="value">{{ item.endDate }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">Total</span>
                  <span class="value price">¥{{ item.totalPrice }}</span>
                </div>
              </div>
            </div>
            
            <div class="card-footer">
              <el-button 
                v-if="item.status === 0" 
                link 
                type="danger" 
                @click="handleCancel(item)"
              >
                Cancel Reservation
              </el-button>
              <span v-else class="status-note">
                {{ item.status === 2 ? 'Order cancelled' : 'Order in progress' }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </main>

    <footer class="footer">
      <div class="footer-content">
        <p>&copy; 2026 ANTIGRAVITY RENTAL. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'
import { getReservationsByUserId, cancelReservation } from '@/api/modules/reservation'

const router = useRouter()
const loading = ref(false)
const list = ref([])
const userInfo = ref(null)

onMounted(() => {
  const savedUserInfo = localStorage.getItem('userInfo')
  if (savedUserInfo) {
    userInfo.value = JSON.parse(savedUserInfo)
    fetchData()
  } else {
    router.push('/login')
  }
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getReservationsByUserId(userInfo.value.userId)
    list.value = res.data || []
  } catch (error) {
    ElMessage.error('Failed to load reservations')
  } finally {
    loading.value = false
  }
}

const getStatusType = (s) => ['warning', 'success', 'danger', 'info'][s] || ''
const getStatusText = (s) => ['Pending', 'Confirmed', 'Cancelled', 'Completed'][s] || 'Unknown'

const handleCancel = async (item) => {
  try {
    await ElMessageBox.confirm('Cancel this reservation?', 'Notice', {
      confirmButtonText: 'OKCancel',
      cancelButtonText: 'Keep it',
      type: 'warning'
    })
    
    await cancelReservation(item.reservationId)
    ElMessage.success('Reservation cancelled')
    fetchData()
  } catch (e) {}
}

const handleLogout = () => {
  localStorage.removeItem('userInfo')
  router.push('/login')
}
</script>

<style scoped>
.user-reservations {
  min-height: 100vh;
  background-color: #fcfcfc;
  display: flex;
  flex-direction: column;
}

.navbar {
  position: sticky;
  top: 0;
  z-index: 1000;
  padding: 15px 0;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  box-shadow: var(--shadow-sm);
}

.nav-container {
  max-width: 1000px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.logo {
  font-size: 18px;
  font-weight: 800;
  letter-spacing: 1px;
  cursor: pointer;
}

.logo span {
  color: var(--accent-color);
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 30px;
}

.nav-links a {
  text-decoration: none;
  color: #666;
  font-size: 14px;
  font-weight: 500;
}

.user-trigger {
  cursor: pointer;
  font-weight: 600;
  font-size: 14px;
}

.main-content {
  flex: 1;
  max-width: 800px;
  margin: 0 auto;
  padding: 60px 20px;
  width: 100%;
}

.page-header {
  margin-bottom: 40px;
  text-align: center;
}

.page-title {
  font-size: 32px;
  font-weight: 800;
  margin-bottom: 10px;
}

.page-subtitle {
  color: #999;
  font-size: 15px;
}

.reservation-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.reservation-card {
  border-radius: 16px;
  overflow: hidden;
  transition: var(--transition-base);
  border: 1px solid #eee;
}

.reservation-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.card-body {
  padding: 25px;
}

.car-info {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 25px;
}

.car-details h3 {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 4px;
}

.car-model {
  color: #999;
  font-size: 14px;
}

.reservation-details {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  padding-top: 20px;
  border-top: 1px solid #f5f5f5;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.detail-item .label {
  font-size: 12px;
  color: #bbb;
  text-transform: uppercase;
}

.detail-item .value {
  font-weight: 600;
  color: #333;
}

.detail-item .price {
  color: var(--accent-color);
  font-size: 18px;
}

.card-footer {
  background: rgba(0, 0, 0, 0.02);
  padding: 15px 25px;
  display: flex;
  justify-content: flex-end;
  border-top: 1px solid #f5f5f5;
}

.status-note {
  font-size: 13px;
  color: #bbb;
}

.footer {
  padding: 40px 0;
  text-align: center;
  color: #ccc;
  font-size: 12px;
}
</style>
