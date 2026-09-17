<template>
  <div class="about-page">
    <nav class="navbar">
      <div class="nav-container">
        <div class="logo" @click="$router.push('/')">ANTIGRAVITY <span>RENTAL</span></div>
        <div class="nav-links">
          <a href="#" @click.prevent="$router.push('/')">Back to Home</a>
        </div>
      </div>
    </nav>

    <section class="about-hero">
      <h1>About Us</h1>
      <p>Reimagining the car rental experience digitally</p>
    </section>

    <main class="about-content">
      <div class="about-section">
        <h2>Who We Are</h2>
        <p>ANTIGRAVITY RENTAL is a car rental platform for modern mobility. Through a unified information system it connects users, vehicles and orders, providing a complete service from car selection and reservation to review and management.</p>
      </div>
      <div class="about-section">
        <h2>Our Services</h2>
        <div class="feature-grid">
          <div class="feature-card">
            <h3>Real-time Selection</h3>
            <p>Vehicle status is synced in real time, helping users quickly find available cars.</p>
          </div>
          <div class="feature-card">
            <h3>Online Reservation</h3>
            <p>Submit pickup and return times online; the system calculates the price and detects conflicts automatically.</p>
          </div>
          <div class="feature-card">
            <h3>Order Tracking</h3>
            <p>Users can view reservation status; administrators can approve and dispatch vehicles.</p>
          </div>
        </div>
      </div>
    </main>

    <footer class="footer">
      <p>&copy; 2026 ANTIGRAVITY RENTAL. All rights reserved.</p>
    </footer>
  </div>
</template>

<script setup>
</script>

<style scoped>
.about-page {
  min-height: 100vh;
  background: #fff;
  display: flex;
  flex-direction: column;
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

.about-hero {
  background: linear-gradient(135deg, #1a1a1a, #333);
  color: #fff;
  text-align: center;
  padding: 80px 20px;
}

.about-hero h1 {
  font-size: 42px;
  margin-bottom: 10px;
}

.about-content {
  max-width: 900px;
  margin: 0 auto;
  padding: 60px 20px;
}

.about-section {
  margin-bottom: 50px;
}

.about-section h2 {
  font-size: 28px;
  margin-bottom: 16px;
}

.about-section p {
  color: #666;
  line-height: 1.8;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.feature-card {
  border: 1px solid #eee;
  padding: 24px;
}

.feature-card h3 {
  margin-bottom: 10px;
}

.footer {
  margin-top: auto;
  background: #1a1a1a;
  color: #999;
  text-align: center;
  padding: 40px 0;
}
</style>
