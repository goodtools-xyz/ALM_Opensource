<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h1>ALM系统</h1>
        <p>应用生命周期管理</p>
      </div>
      
      <el-form :model="loginForm" label-width="80px" class="login-form">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="login-button">登录</el-button>
        </el-form-item>
      </el-form>

      <div class="role-select">
        <span>选择角色：</span>
        <el-radio-group v-model="loginForm.role">
          <el-radio label="admin">管理员</el-radio>
          <el-radio label="user">普通用户</el-radio>
        </el-radio-group>
      </div>

      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const loginForm = reactive({
  username: '',
  password: '',
  remember: false,
  role: 'admin'
})

const errorMessage = ref('')

const handleLogin = () => {
  if (!loginForm.username || !loginForm.password) {
    errorMessage.value = '请输入用户名和密码'
    return
  }
  
  // 模拟登录验证
  if (loginForm.username === 'admin' && loginForm.password === 'admin') {
    // 保存用户信息到localStorage
    localStorage.setItem('userInfo', JSON.stringify({
      username: loginForm.username,
      role: loginForm.role
    }))
    router.push('/hardware')
  } else if (loginForm.username === 'user' && loginForm.password === 'user') {
    localStorage.setItem('userInfo', JSON.stringify({
      username: loginForm.username,
      role: loginForm.role
    }))
    router.push('/hardware')
  } else {
    errorMessage.value = '用户名或密码错误（演示：admin/admin 或 user/user）'
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  background: white;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h1 {
  font-size: 28px;
  color: #1f2937;
  margin-bottom: 5px;
}

.login-header p {
  color: #6b7280;
  font-size: 14px;
}

.login-form {
  margin-bottom: 20px;
}

.login-button {
  width: 100%;
  height: 40px;
  font-size: 16px;
}

.role-select {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
  padding: 15px;
  background: #f9fafb;
  border-radius: 8px;
}

.role-select span {
  color: #6b7280;
}

.error-message {
  margin-top: 15px;
  padding: 10px;
  background: #fee2e2;
  color: #dc2626;
  border-radius: 5px;
  text-align: center;
}
</style>