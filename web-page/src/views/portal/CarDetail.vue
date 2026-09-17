<template>
  <div class="car-detail-page" v-loading="loading">
    <nav class="navbar">
      <div class="nav-container">
        <div class="logo" @click="$router.push('/')">ANTIGRAVITY <span>RENTAL</span></div>
        <div class="nav-links">
          <a href="#" @click.prevent="$router.push('/')">Back to Home</a>
        </div>
      </div>
    </nav>

    <main v-if="car" class="detail-content">
      <el-row :gutter="40">
        <el-col :md="12">
          <div class="image-box">
            <img :src="getCarImage(car.imagePath)" alt="Vehicle Image" />
          </div>
        </el-col>
        <el-col :md="12">
          <div class="detail-info">
            <h1>{{ car.brand }} {{ car.series }}</h1>
            <p>{{ car.year }} · {{ car.model }}</p>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="Category">{{ car.category }}</el-descriptions-item>
              <el-descriptions-item label="Displacement">{{ car.displacement }}</el-descriptions-item>
              <el-descriptions-item label="Transmission">{{ car.transmission }}</el-descriptions-item>
              <el-descriptions-item label="Seats">{{ car.seatCount }}  seats</el-descriptions-item>
              <el-descriptions-item label="Fuel">{{ car.fuelType }} {{ car.fuelLabel }}</el-descriptions-item>
              <el-descriptions-item label="Status">{{ car.status === 0 ? 'Available' : 'Unavailable' }}</el-descriptions-item>
            </el-descriptions>

            <div class="price-box">
              <span class="label">Daily Rate</span>
              <span class="price">¥{{ car.dailyRent }}</span>
            </div>

            <el-button class="reserve-btn" :disabled="car.status !== 0" @click="handleReserve">
              {{ car.status === 0 ? 'Reserve Now' : 'Unavailable' }}
            </el-button>
          </div>
        </el-col>
      </el-row>
    </main>

    <el-dialog v-model="reservationVisible" title="Reservation Details" width="450px" center>
      <el-form :model="reservationForm" :rules="reservationRules" ref="formRef" label-position="top">
        <el-form-item label="Pickup Date" prop="startDate">
          <el-date-picker v-model="reservationForm.startDate" type="date" placeholder="Select pickup date" style="width: 100%" value-format="YYYY-MM-DD" :disabled-date="disabledDate" />
        </el-form-item>
        <el-form-item label="Return Date" prop="endDate">
          <el-date-picker v-model="reservationForm.endDate" type="date" placeholder="Select return date" style="width: 100%" value-format="YYYY-MM-DD" :disabled-date="disabledDate" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reservationVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitReservation">Submit Reservation</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCarById } from '@/api/modules/car'
import { createReservation } from '@/api/modules/reservation'

const route = useRoute()
const router = useRouter()
const car = ref(null)
const loading = ref(false)
const reservationVisible = ref(false)
const formRef = ref(null)

const reservationForm = reactive({
  startDate: '',
  endDate: ''
})

const reservationRules = {
  startDate: [{ required: true, message: 'Please select a pickup date', trigger: 'change' }],
  endDate: [{ required: true, message: 'Please select a return date', trigger: 'change' }]
}

onMounted(() => fetchCar())

const fetchCar = async () => {
  loading.value = true
  try {
    const res = await getCarById(route.params.id)
    car.value = res.data
  } catch (error) {
    ElMessage.error('Vehicle not found or failed to load')
    router.push('/')
  } finally {
    loading.value = false
  }
}

const getCarImage = (imagePath) => imagePath
  ? `http://localhost:8080${imagePath}`
  : 'https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&q=80&w=800'

const disabledDate = (time) => time.getTime() < Date.now() - 86400000

const handleReserve = () => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || 'null')
  if (!userInfo) {
    ElMessage.warning('Please log in before making a reservation')
    router.push({ path: '/login', query: { redirect: route.fullPath } })
    return
  }
  reservationVisible.value = true
}

const submitReservation = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return

    try {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      await createReservation({
        userId: userInfo.userId,
        carId: car.value.carId,
        startDate: reservationForm.startDate,
        endDate: reservationForm.endDate
      })
      ElMessage.success('Reservation submitted')
      reservationVisible.value = false
      router.push('/profile')
    } catch (error) {
      ElMessage.error(error || 'Reservation failed')
    }
  })
}
</script>

<style scoped>
.car-detail-page {
  min-height: 100vh;
}

.navbar {
  position: sticky;
  top: 0;
  z-index: 10;
  background: rgba(255, 255, 255, 0.9);
  padding: 15px 0;
}

.nav-container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-weight: 800;
  cursor: pointer;
}

.logo span {
  color: #b38b52;
}

.nav-links a {
  color: #333;
  text-decoration: none;
}

.detail-content {
  max-width: 1100px;
  margin: 60px auto;
  padding: 0 20px;
}

.image-box {
  height: 440px;
  background: #f5f5f5;
}

.image-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.detail-info h1 {
  font-size: 34px;
  margin-bottom: 6px;
}

.detail-info p {
  color: #666;
  margin-bottom: 24px;
}

.price-box {
  margin: 30px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.price-box .label {
  color: #666;
}

.price-box .price {
  color: #b38b52;
  font-size: 34px;
  font-weight: 800;
}

.reserve-btn {
  background: #1a1a1a;
  color: #fff;
  border: none;
  width: 100%;
  height: 48px;
}
</style>
