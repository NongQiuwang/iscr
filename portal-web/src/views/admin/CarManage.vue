<template>
  <div class="admin-page">
    <div class="page-header">
      <div class="header-info">
        <h2>车辆管理</h2>
        <p>管理系统中的所有租赁车辆信息</p>
      </div>
      <el-button type="primary" @click="handleAdd">新增车辆</el-button>
    </div>

    <div class="admin-card">
      <!-- 极简搜索栏 -->
      <div class="search-section">
        <el-input v-model="searchForm.brand" placeholder="品牌" class="search-item" clearable @change="fetchData" />
        <el-input v-model="searchForm.series" placeholder="车系" class="search-item" clearable @change="fetchData" />
        <el-button @click="resetSearch">重置</el-button>
      </div>

      <!-- 扁平化表格 -->
      <el-table :data="tableData" v-loading="loading" class="flat-table">
        <el-table-column prop="carId" label="ID" width="70" />
        <el-table-column label="车辆信息" min-width="200">
          <template #default="{ row }">
            <div class="car-cell">
              <el-image :src="getImageUrl(row.imagePath)" class="car-thumb" fit="cover" />
              <div class="car-text">
                <div class="car-title">{{ row.brand }} {{ row.series }}</div>
                <div class="car-sub">{{ row.year }} · {{ row.model }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="类别" width="120" />
        <el-table-column prop="dailyRent" label="日租金" width="120">
          <template #default="{ row }">
            <span class="price-text">¥{{ row.dailyRent }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag size="small" :type="row.status === 0 ? 'success' : row.status === 1 ? 'warning' : 'info'" effect="light">
              {{ row.status === 0 ? '空闲' : row.status === 1 ? '已租' : '维护' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 简洁表单弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" class="clean-dialog">
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="品牌" prop="brand">
              <el-input v-model="form.brand" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车系" prop="series">
              <el-input v-model="form.series" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年款" prop="year">
              <el-input v-model="form.year" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="型号" prop="model">
              <el-input v-model="form.model" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="日租金" prop="dailyRent">
              <el-input-number v-model="form.dailyRent" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="form.status" style="width: 100%">
                <el-option label="空闲" :value="0" />
                <el-option label="已租" :value="1" />
                <el-option label="维护" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="车辆图片">
          <el-upload
            class="clean-upload"
            :action="uploadAction"
            :on-success="handleUploadSuccess"
            :show-file-list="false"
          >
            <div v-if="form.imagePath" class="upload-preview">
              <img :src="getImageUrl(form.imagePath)" />
              <div class="upload-mask">更换图片</div>
            </div>
            <div v-else class="upload-placeholder">
              <el-icon><Plus /></el-icon>
              <span>上传图片</span>
            </div>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getAllCars, addCar, updateCar, deleteCar } from '@/api/car'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增车辆')
const formRef = ref(null)
const isEdit = ref(false)
const uploadAction = 'http://localhost:8080/iscr/api/upload/car-image'

const searchForm = reactive({ brand: '', series: '' })
const form = reactive({
  carId: null, brand: '', series: '', year: '', model: '',
  dailyRent: 0, status: 0, imagePath: ''
})

const rules = {
  brand: [{ required: true, message: '必填', trigger: 'blur' }],
  series: [{ required: true, message: '必填', trigger: 'blur' }],
  dailyRent: [{ required: true, message: '必填', trigger: 'blur' }]
}

onMounted(() => fetchData())

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getAllCars()
    tableData.value = res.data || []
  } catch (error) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchForm.brand = ''
  searchForm.series = ''
  fetchData()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增车辆'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑车辆'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该车辆信息？', '提示', { type: 'warning', confirmButtonText: '删除' })
    await deleteCar(row.carId)
    ElMessage.success('已删除')
    fetchData()
  } catch (e) {}
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) await updateCar(form.carId, form)
        else await addCar(form)
        ElMessage.success('保存成功')
        dialogVisible.value = false
        fetchData()
      } catch (e) {
        ElMessage.error('保存失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const resetForm = () => {
  Object.assign(form, { carId: null, brand: '', series: '', year: '', model: '', dailyRent: 0, status: 0, imagePath: '' })
}

const handleUploadSuccess = (res) => {
  if (res.code === 0) {
    form.imagePath = res.data
    ElMessage.success('上传成功')
  }
}

const getImageUrl = (path) => {
  if (path) return `http://localhost:8080${path}`
  return 'https://via.placeholder.com/100x60?text=No+Image'
}
</script>

<style scoped>
.admin-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.header-info h2 {
  font-size: 20px;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.header-info p {
  color: #999;
  font-size: 13px;
}

.admin-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #f0f2f5;
}

.search-section {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.search-item {
  width: 180px;
}

.flat-table :deep(.el-table__header) th {
  background-color: #f9fafb;
  color: #666;
  font-weight: 600;
  font-size: 13px;
  border-bottom: 1px solid #f0f2f5;
}

.car-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.car-thumb {
  width: 60px;
  height: 40px;
  border-radius: 4px;
  background: #f5f7fa;
}

.car-title {
  font-weight: 600;
  color: #333;
  font-size: 14px;
}

.car-sub {
  font-size: 12px;
  color: #999;
}

.price-text {
  color: #f56c6c;
  font-weight: 600;
}

/* Upload Style */
.clean-upload {
  width: 120px;
  height: 80px;
  border: 1px dashed #dcdfe6;
  border-radius: 8px;
  cursor: pointer;
  overflow: hidden;
  transition: border-color 0.3s;
}

.clean-upload:hover {
  border-color: #409eff;
}

.upload-placeholder {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #999;
  font-size: 12px;
}

.upload-preview {
  position: relative;
  height: 100%;
}

.upload-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.4);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.upload-preview:hover .upload-mask {
  opacity: 1;
}
</style>
