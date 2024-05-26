<template>
  <el-container>
    <el-header>
      <div class="user-info">
        <div class="left-info">
          <el-text tag="b" type="primary" size="large">后台管理</el-text>
        </div>
        <div class="right-info">
          <el-space>
            <el-avatar>{{ username }}</el-avatar>
            <el-button type="danger" round @click="logout">登出</el-button>
          </el-space>
        </div>
      </div>
    </el-header>
  </el-container>
  <el-container>
    <el-aside width="200px">
      <el-menu
        active-text-color="#ffd04b"
        background-color="#545c64"
        class="el-menu-vertical-demo"
        :default-active="activeIndex"
        text-color="#fff"
        router
      >
        <el-menu-item index="/home">
          <el-icon><House /></el-icon>
          <span>波形数据</span>
        </el-menu-item>
        <el-menu-item index="/wave/compare">
          <el-icon><Aim /></el-icon>
          <span>波形对比</span>
        </el-menu-item>
        <el-menu-item index="/users/page">
          <el-icon><Document /></el-icon>
          <span>用户列表</span>
        </el-menu-item>
        <el-menu-item index="/facility/page">
          <el-icon><Setting /></el-icon>
          <span>设备列表</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main>
      <RouterView />
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import router from '@/router';
import { userModuleStore } from '@/stores/user';
import { House, Document, Setting, Aim } from '@element-plus/icons-vue'
import { ElContainer, ElAside, ElMenu, ElMenuItem, ElIcon } from 'element-plus'
import { ref, type Ref } from 'vue'
import { RouterView, useRouter } from 'vue-router'

const { currentRoute } = useRouter()
const activeIndex: Ref<string> = ref<string>(currentRoute.value.path)
const username = userModuleStore().username

const logout = async () => {
  localStorage.removeItem('username')
  localStorage.removeItem('token')
  localStorage.removeItem('refresh-token')
  router.push('/login')
}
</script>

<style lang="scss" scoped>
.el-aside {
  height: 100vh;
  background-color: #545c64;
}
.el-header {
  -webkit-box-shadow: #c1c1c1 0px 0px 5px;
  -moz-box-shadow: #c1c1c1 0px 0px 5px;
  box-shadow: #c1c1c1 0px 0px 5px;
  background: #f9f9f9;
}

.user-info {
  height: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  text-align: center;
}
</style>
