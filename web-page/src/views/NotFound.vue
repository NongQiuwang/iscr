<template>
  <div class="not-found">
    <h1>404</h1>
    <p>The page does not exist or has been moved</p>
    <el-button type="primary" @click="$router.push('/')">Back to Home</el-button>
  </div>
</template>

<script setup>
</script>

<style scoped>
.not-found {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
}

.not-found h1 {
  font-size: 72px;
  color: #1a1a1a;
}

.not-found p {
  color: #666;
}
</style>
