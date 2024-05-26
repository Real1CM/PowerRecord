<template>
  <el-container>
    <el-main>
      <el-form :model="form" label-width="auto" style="max-width: 800px; margin: auto;" >
        <el-form-item label="选择设备">
          <el-select v-model="form.facilityId" placeholder="请选择设备">
            <el-option
            v-for="item in options"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
          </el-select>
        </el-form-item>

        <el-form-item label="上传您的数据文件 ">
          <el-upload
            ref="uploadRef"
            :headers="setHeaders"
            :data="form"
            action="/api/wave/data/compare"
            :on-success="onSuccess"
            accept=".csv"
            class="upload-item"
            drag
            :auto-upload="false"
          >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">拖动上传 或 <em>手动上传</em></div>
            <template #tip>
              <div class="el-upload__tip">csv 文件需小于500kb</div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item style="float: right;">
          <el-button type="primary" @click="onSubmit">校验数据</el-button>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script lang="ts" setup>
import { listFacility } from '@/api/facility';
import { userModuleStore } from '@/stores/user';
import { ElMessageBox, type UploadFile, type UploadFiles, type UploadInstance } from 'element-plus';
import { onMounted, reactive, ref } from 'vue'



const uploadRef = ref<UploadInstance>()
const options:any = ref([])
const userModel = userModuleStore()

onMounted(() => {
    fetchOptions()
})


async function fetchOptions() {
    const { data } = await listFacility()
    options.value = data
}


const form = reactive({
  facilityId:null
})


const setHeaders = {
        "token": userModel.token,
        "username": userModel.username,
    }

const onSubmit = async () => {
    uploadRef.value!.submit()
}

const onSuccess = (response: any, uploadFile: UploadFile, uploadFiles: UploadFiles) => {
    if(response.status === 'SUCCESS') {
        ElMessageBox.alert('波形数据比对成功')
    } else{
      ElMessageBox.alert(response.errorMessage)
    }
}
</script>

<style lang="scss" scoped>
.upload-item {
  width: 800px;
}
</style>
