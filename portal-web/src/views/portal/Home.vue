<template>
  <div class="portal-home">
    <!-- 极简透明导航栏 -->
    <nav class="navbar" :class="{ 'navbar-scrolled': isScrolled }">
      <div class="nav-container">
        <div class="logo">ANTIGRAVITY <span>RENTAL</span></div>
        <div class="nav-links">
          <a href="#cars">精选车型</a>
          <a href="#">关于我们</a>
          <div class="auth-section">
            <template v-if="!userInfo">
              <el-button class="btn-login" @click="gotoLogin">登录</el-button>
            </template>
            <template v-else>
              <el-dropdown trigger="click">
                <span class="user-trigger">
                  {{ userInfo.username }} <el-icon><ArrowDown /></el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="router.push('/profile')">我的预约</el-dropdown-item>
                    <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </div>
        </div>
      </div>
    </nav>

    <!-- Hero Section: 高端英雄区域 -->
    <section class="hero">
      <div class="hero-overlay"></div>
      <div class="hero-content fade-in-up">
        <h1 class="hero-title">开启您的<span>非凡</span>旅程</h1>
        <p class="hero-subtitle">探索顶级豪华座驾，体验极致驾驶乐趣。我们为您提供不仅仅是租赁，更是身份的象征。</p>
        <div class="hero-actions">
          <el-button class="btn-primary" @click="scrollToCars">立即探索</el-button>
        </div>
      </div>
    </section>

    <!-- 汽车列表区域 -->
    <main id="cars" class="main-content">
      <div class="section-header fade-in-up">
        <h2 class="section-title">精选车型</h2>
        <div class="title-line"></div>
        <div class="filter-bar">
          <el-input
            v-model="searchForm.brand"
            placeholder="搜索您心仪的品牌..."
            class="search-input"
            @change="fetchCars"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </div>

      <el-row :gutter="30" v-loading="loading">
        <el-col :xs="24" :sm="12" :md="8" v-for="(car, index) in carList" :key="car.carId">
          <div class="car-card-wrapper fade-in-up" :style="{ animationDelay: index * 0.1 + 's' }">
            <el-card :body-style="{ padding: '0px' }" class="car-card" @click="showCarDetail(car)">
              <div class="car-image">
                <img :src="getCarImage(car.imagePath)" alt="汽车图片" />
                <div class="image-overlay">
                  <span>查看详情</span>
                </div>
              </div>
              <div class="car-info">
                <div class="car-header">
                  <h3>{{ car.brand }} {{ car.series }}</h3>
                  <el-tag effect="plain" class="tag-category">{{ car.category }}</el-tag>
                </div>
                <p class="car-model">{{ car.year }} · {{ car.model }}</p>
                <div class="car-footer">
                  <div class="car-price">
                    <span class="currency">¥</span>
                    <span class="amount">{{ car.dailyRent }}</span>
                    <span class="unit">/日</span>
                  </div>
                  <el-tag :type="car.status === 0 ? 'success' : 'info'" class="status-tag">
                    {{ car.status === 0 ? '可预约' : '已租出' }}
                  </el-tag>
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>

      <el-empty v-if="!loading && carList.length === 0" description="暂无符合条件的车型" />
    </main>

    <!-- 汽车详情弹窗: 毛玻璃效果 -->
    <el-dialog 
      v-model="detailVisible" 
      :title="null" 
      width="800px" 
      class="custom-dialog"
      destroy-on-close
    >
      <div v-if="currentCar" class="car-detail-container">
        <div class="detail-header">
          <h2>{{ currentCar.brand }} {{ currentCar.series }}</h2>
          <p>{{ currentCar.model }}</p>
        </div>
        <el-row :gutter="40">
          <el-col :md="12">
            <div class="detail-image-box">
              <img :src="getCarImage(currentCar.imagePath)" alt="汽车图片" />
            </div>
          </el-col>
          <el-col :md="12">
            <div class="detail-info-grid">
              <div class="info-item">
                <span class="label">变速箱</span>
                <span class="value">{{ currentCar.transmission }}</span>
              </div>
              <div class="info-item">
                <span class="label">燃料</span>
                <span class="value">{{ currentCar.fuelType }} ({{ currentCar.fuelLabel }})</span>
              </div>
              <div class="info-item">
                <span class="label">排量</span>
                <span class="value">{{ currentCar.displacement }}</span>
              </div>
              <div class="info-item">
                <span class="label">座位数</span>
                <span class="value">{{ currentCar.seatCount }}座</span>
              </div>
            </div>
            <div class="detail-price-section">
              <div class="price-display">
                <span class="label">日租金</span>
                <span class="value">¥{{ currentCar.dailyRent }}</span>
              </div>
              <el-button 
                class="btn-reserve" 
                :disabled="currentCar.status !== 0"
                @click="handleReservation"
              >
                {{ currentCar.status === 0 ? '立即预约' : '暂不可租' }}
              </el-button>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-dialog>

    <!-- 预约弹窗 -->
    <el-dialog v-model="reservationVisible" title="预约详情" width="450px" center>
      <el-form :model="reservationForm" :rules="reservationRules" ref="reservationFormRef" label-position="top">
        <el-form-item label="取车日期" prop="startDate">
          <el-date-picker
            v-model="reservationForm.startDate"
            type="date"
            placeholder="选择取车日期"
            style="width: 100%"
            :disabled-date="disabledDate"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="还车日期" prop="endDate">
          <el-date-picker
            v-model="reservationForm.endDate"
            type="date"
            placeholder="选择还车日期"
            style="width: 100%"
            :disabled-date="disabledDate"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="reservationVisible = false">取消</el-button>
          <el-button type="primary" class="btn-confirm" @click="submitReservation">提交预约</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <p>&copy; 2026 ANTIGRAVITY RENTAL. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, ArrowDown } from '@element-plus/icons-vue'
import { getAllCars } from '@/api/car'
import { createReservation } from '@/api/reservation'

const router = useRouter()
const loading = ref(false)
const carList = ref([])
const detailVisible = ref(false)
const reservationVisible = ref(false)
const currentCar = ref(null)
const userInfo = ref(null)
const reservationFormRef = ref(null)
const isScrolled = ref(false)

const searchForm = reactive({
  brand: ''
})

const reservationForm = reactive({
  startDate: '',
  endDate: ''
})

const reservationRules = {
  startDate: [{ required: true, message: '请选择取车日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择还车日期', trigger: 'change' }]
}

const handleScroll = () => {
  isScrolled.value = window.scrollY > 50
}

onMounted(() => {
  const savedUserInfo = localStorage.getItem('userInfo')
  if (savedUserInfo) {
    userInfo.value = JSON.parse(savedUserInfo)
  }
  fetchCars()
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

const fetchCars = async () => {
  loading.value = true
  try {
    const res = await getAllCars()
    carList.value = res.data || []
  } catch (error) {
    ElMessage.error('获取车辆列表失败')
  } finally {
    loading.value = false
  }
}

const getCarImage = (imagePath) => {
  if (imagePath) return `http://localhost:8080${imagePath}`
  return 'https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&q=80&w=800'
}

const showCarDetail = (car) => {
  currentCar.value = car
  detailVisible.value = true
}

const scrollToCars = () => {
  document.getElementById('cars').scrollIntoView({ behavior: 'smooth' })
}

const handleReservation = () => {
  if (!userInfo.value) {
    ElMessage.warning('请先登录以进行预约')
    router.push('/login')
    return
  }
  detailVisible.value = false
  reservationVisible.value = true
  reservationForm.startDate = ''
  reservationForm.endDate = ''
}

const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7
}

const submitReservation = async () => {
  if (!reservationFormRef.value) return
  await reservationFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const data = {
          userId: userInfo.value.userId,
          carId: currentCar.value.carId,
          startDate: reservationForm.startDate,
          endDate: reservationForm.endDate
        }
        await createReservation(data)
        ElMessage.success('预约申请已提交')
        reservationVisible.value = false
        fetchCars()
      } catch (error) {
        ElMessage.error(error || '预约失败')
      }
    }
  })
}

const gotoLogin = () => router.push('/login')
const handleLogout = () => {
  localStorage.removeItem('userInfo')
  userInfo.value = null
  ElMessage.success('已安全退出')
}
</script>

<style scoped>
.portal-home {
  background-color: #fff;
}

/* 导航栏样式 */
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  padding: 20px 0;
  transition: var(--transition-base);
}

.navbar-scrolled {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  padding: 12px 0;
  box-shadow: var(--shadow-sm);
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.logo {
  font-size: 20px;
  font-weight: 800;
  letter-spacing: 2px;
  color: #1a1a1a;
}

.logo span {
  color: var(--accent-color);
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 40px;
}

.nav-links a {
  text-decoration: none;
  color: #333;
  font-weight: 500;
  font-size: 14px;
  transition: color 0.3s;
}

.nav-links a:hover {
  color: var(--accent-color);
}

.btn-login {
  background: #1a1a1a;
  color: #fff;
  border: none;
  padding: 10px 25px;
  border-radius: 0;
  font-weight: 600;
}

.user-trigger {
  cursor: pointer;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 5px;
}

/* Hero Section */
.hero {
  height: 100vh;
  background-image: url('https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?auto=format&fit=crop&q=80&w=1920');
  background-size: cover;
  background-position: center;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: #fff;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
}

.hero-content {
  position: relative;
  z-index: 1;
  max-width: 800px;
  padding: 0 20px;
}

.hero-title {
  font-size: 64px;
  font-weight: 800;
  margin-bottom: 20px;
  letter-spacing: -1px;
}

.hero-title span {
  color: var(--accent-color);
}

.hero-subtitle {
  font-size: 18px;
  line-height: 1.6;
  margin-bottom: 40px;
  opacity: 0.9;
}

.btn-primary {
  background: var(--accent-color);
  border: none;
  color: #fff;
  padding: 15px 40px;
  font-size: 16px;
  font-weight: 700;
  border-radius: 0;
  transition: var(--transition-base);
}

.btn-primary:hover {
  background: #b38b52;
  transform: translateY(-2px);
}

/* Main Content */
.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 100px 20px;
}

.section-header {
  text-align: center;
  margin-bottom: 60px;
}

.section-title {
  font-size: 36px;
  font-weight: 800;
  margin-bottom: 15px;
}

.title-line {
  width: 60px;
  height: 3px;
  background: var(--accent-color);
  margin: 0 auto 30px;
}

.search-input {
  max-width: 400px;
  margin: 0 auto;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 0;
  box-shadow: var(--shadow-sm);
  padding: 10px 15px;
}

/* Car Card */
.car-card-wrapper {
  margin-bottom: 30px;
}

.car-card {
  border: none;
  border-radius: 0;
  transition: var(--transition-base);
  position: relative;
  overflow: hidden;
}

.car-card:hover {
  transform: translateY(-10px);
  box-shadow: var(--shadow-md);
}

.car-image {
  height: 240px;
  position: relative;
  overflow: hidden;
}

.car-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s;
}

.car-card:hover .car-image img {
  transform: scale(1.1);
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.car-card:hover .image-overlay {
  opacity: 1;
}

.image-overlay span {
  color: #fff;
  border: 1px solid #fff;
  padding: 8px 20px;
  font-size: 14px;
}

.car-info {
  padding: 25px;
}

.car-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.car-info h3 {
  font-size: 20px;
  font-weight: 700;
  margin: 0;
}

.tag-category {
  font-size: 10px;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.car-model {
  color: var(--secondary-text);
  font-size: 14px;
  margin-bottom: 20px;
}

.car-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid #eee;
  padding-top: 20px;
}

.car-price .currency {
  font-size: 16px;
  color: var(--accent-color);
  font-weight: 600;
}

.car-price .amount {
  font-size: 28px;
  font-weight: 800;
  color: #1a1a1a;
  margin: 0 4px;
}

.car-price .unit {
  font-size: 12px;
  color: #999;
}

/* Dialog & Detail */
.custom-dialog :deep(.el-dialog__body) {
  padding: 0;
}

.car-detail-container {
  padding: 40px;
}

.detail-header {
  margin-bottom: 30px;
}

.detail-header h2 {
  font-size: 32px;
  font-weight: 800;
  margin-bottom: 5px;
}

.detail-header p {
  color: var(--secondary-text);
}

.detail-image-box {
  width: 100%;
  height: 300px;
  background: #f5f5f5;
}

.detail-image-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.detail-info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 40px;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-item .label {
  font-size: 12px;
  color: #999;
  text-transform: uppercase;
  margin-bottom: 5px;
}

.info-item .value {
  font-weight: 600;
  color: #1a1a1a;
}

.detail-price-section {
  background: #f9f9f9;
  padding: 25px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price-display .label {
  display: block;
  font-size: 12px;
  color: #666;
}

.price-display .value {
  font-size: 32px;
  font-weight: 800;
  color: var(--accent-color);
}

.btn-reserve {
  background: #1a1a1a;
  color: #fff;
  border: none;
  padding: 15px 30px;
  font-weight: 700;
  border-radius: 0;
}

.btn-reserve:hover {
  background: #333;
}

/* Footer */
.footer {
  padding: 60px 0;
  background: #1a1a1a;
  color: #666;
  text-align: center;
  font-size: 14px;
}
</style>
