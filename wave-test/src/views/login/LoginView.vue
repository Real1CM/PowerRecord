<template>
  <div class="login-container">
    <el-form
      ref="loginForm"
      :model="loginFormData"
      :rules="loginRules"
      class="login-form"
      autocomplete="on"
      label-position="left"
    >
      <div class="title-container">
        <h3 class="title">登录</h3>
      </div>

      <el-form-item prop="username">
        <el-input
          ref="username"
          v-model="loginFormData.username"
          placeholder="用户名"
          name="username"
          type="text"
          tabindex="1"
          autocomplete="on"
          :prefix-icon="UserFilled"
        />
      </el-form-item>
      <el-tooltip :visible="capsTooltip" content="大写已开" placement="right">
        <el-form-item prop="password">
          <el-input
            ref="password"
            v-model="loginFormData.password"
            type="password"
            show-password
            placeholder="密码"
            name="password"
            tabindex="2"
            autocomplete="on"
            @keyup="checkCapslock"
            :prefix-icon="Lock"
          />
        </el-form-item>
      </el-tooltip>

      <el-button
        :loading="loading"
        type="primary"
        style="width: 85%"
        @click.enter.prevent="handleLogin"
      >
        登录
      </el-button>

      <el-button type="success" style="width: 10%" @click="dialogFormVisible = true">
        注册
      </el-button>
    </el-form>

    <el-dialog v-model="dialogFormVisible" title="注册用户" width="500">
      <el-form :model="form">
        <el-form-item label="名称" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="密码" :label-width="formLabelWidth">
          <el-input v-model="form.password" autocomplete="off" type="password" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="handleRegister"> 确认 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { isValidUsername } from '@/utils/validate'
import { ElForm, ElFormItem, ElInput, ElButton, ElTooltip, ElMessage } from 'element-plus'
import type { InputInstance, FormInstance } from 'element-plus/lib/components/index.js'
import { onMounted, ref, reactive } from 'vue'
import { UserFilled, Lock } from '@element-plus/icons-vue'
import { login, register } from '@/api/user'
import router from '@/router'
import { userModuleStore } from '@/stores/user'

const dialogFormVisible = ref(false)
const username = ref<null | InputInstance>(null)
const password = ref<null | InputInstance>(null)
const loginForm = ref<null | FormInstance>(null)
const formLabelWidth = '140px'

const validateUserName = (rule: any, value: any, callback: any) => {
  if (!isValidUsername(value)) {
    callback(new Error('用户名不能为空'))
  } else {
    callback()
  }
}

const validatePassword = (rule: any, value: any, callback: any) => {
  if (value.length < 6) {
    callback(new Error('密码长度不能少于6位'))
  } else {
    callback()
  }
}

let loginFormData = reactive({
  username: '',
  password: ''
})

const form = reactive({
  name: '',
  password: ''
})

const loginRules = {
  username: [{ validator: validateUserName, trigger: 'blur' }],
  password: [{ validator: validatePassword, trigger: 'blur' }]
}

let loading = ref(false)
const capsTooltip = ref(false)

onMounted(() => {
  if (loginFormData.username === '') {
    username.value!.focus()
  } else if (loginFormData.password === '') {
    password.value!.focus()
  }
})

const checkCapslock = (e: KeyboardEvent) => {
  const { key } = e
  capsTooltip.value = key !== null && key.length === 1 && key >= 'A' && key <= 'Z'
}

const handleLogin = () => {
  loading.value = true
  loginForm!.value?.validate(async (valid: boolean) => {
    if (valid) {
      loading.value = false
      const data: any = await login({
        name: loginFormData.username,
        password: loginFormData.password
      })
      if (data.status === 'SUCCESS') {
          const userStore = userModuleStore()
          userStore.token = data.data.token
          userStore.username = data.data.name
          userStore.refreshToken = data.data.refreshToken
      }
      if (data.data.token) {
        router.push({
          path: '/'
        })
      }
    } else {
      return false
    }
  })
}

const handleRegister = async () => {
  const data: any = await register(form)
  if (data.status === 'SUCCESS') {
    ElMessage({
      message: '注册成功，请登录',
      duration: 2 * 1000,
      type: 'success'
    })
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  height: 100%;
  width: 100%;
  overflow: hidden;
  background-color: #2d3a4b;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }
  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: #eee;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: #889aa4;
    cursor: pointer;
    user-select: none;
  }
}
</style>
