<template>
  <el-container>
    <el-main>
      <el-table :data="tableData" border style="width: 100%" :loading="loading">
        <el-table-column prop="id" label="Id" width="180" />
        <el-table-column prop="name" label="设备名称" width="180" />
        <el-table-column prop="macAddress" label="设备地址" width="180" />
        <el-table-column prop="createTime" label="添加时间" />
        <el-table-column prop="updateTime" label="更新时间" />
        <el-table-column align="right">
          <template #header>
            <el-button type="primary" @click="dialogFormVisible = true">添加</el-button>
          </template>
          <template #default="scope">
            <el-button size="small" @click="cleanBinded(scope.$index, scope.row)"
              >解绑数据</el-button
            >
            <el-button size="small" @click="handleBind(scope.$index, scope.row)"
              >绑定数据</el-button
            >
            <el-button size="small" @click="handleEdit(scope.$index, scope.row)">修改</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-main>
    <el-footer> 
      <el-pagination background layout="prev, pager, next" style="float: right;"
      :total="pageInfo.total"
      v-model:page-size="pageInfo.pageSize"
      :page-count="pageInfo.pageCount"
      v-model:current-page="pageInfo.currentPage"
      @current-change="pageChange"
      />
    </el-footer>
  </el-container>

 

  <el-dialog v-model="dialogFormVisible" title="添加设备" width="500">
    <el-form :model="form">
      <el-form-item label="名称" :label-width="formLabelWidth">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="设备地址" :label-width="formLabelWidth">
        <el-input v-model="form.macAddress" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAdd"> 确认 </el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="updateFormVisible" title="修改设备" width="500">
    <el-form :model="updateForm">
      <el-form-item label="名称" :label-width="formLabelWidth">
        <el-input v-model="updateForm.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="设备地址" :label-width="formLabelWidth">
        <el-input v-model="updateForm.macAddress" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="updateFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate"> 确认 </el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="bindFormVisible" title="绑定数据" width="500">
    <el-form>
      <el-form-item label="波形数据">
        <el-select
          v-model="waveData"
          multiple
          collapse-tags
          collapse-tags-tooltip
          placeholder="请选择"
          style="width: 240px"
        >
          <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="bindFormVisible = false">取消</el-button>
        <el-button type="primary" @click="addBind"> 确认 </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import {
  addFacility,
  bindWaveData,
  deleteFacility,
  getFacility,
  unBindWaveData,
  updateFacility
} from '@/api/facility'
import { listWaveData } from '@/api/wave'
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

const loading = ref(false)
const tableData = ref([])
const options: any = ref([])
const waveData: any = ref([])
const bindFacilityId: any = ref(0)

const form = reactive({
  name: '',
  macAddress: ''
})

const updateForm = reactive({
  id: 0,
  name: '',
  macAddress: ''
})

const dialogFormVisible = ref(false)
const updateFormVisible = ref(false)
const bindFormVisible = ref(false)
const formLabelWidth = '140px'

interface Faciltity {
  id: number
  name: string
  macAddress: string
  createTime: string
  updateTime: string
}

const pageInfo = reactive({
  total: 0,
  pageSize: 10,
  pageCount: 0,
  currentPage: 1
})

onMounted(() => {
  fetchData(1)
})

async function fetchData(pageNumber: number) {
  loading.value = true
  const { data } = await getFacility(pageNumber)
  loading.value = false
  tableData.value = data.records

  pageInfo.currentPage = data.current
  pageInfo.pageSize = data.size
  pageInfo.total = data.total
  pageInfo.pageCount = data.pages
}

const cleanBinded = async (index: number, row: Faciltity) => {
  await unBindWaveData({ facilityId: row.id }).then((value: any) => {
    if (value.status === 'SUCCESS') {
      ElMessage('解绑成功')
    }
  })
}

const handleBind = async (index: number, row: Faciltity) => {
  const { data } = await listWaveData({ facilityId: row.id })
  options.value = data
  bindFormVisible.value = true
  bindFacilityId.value = row.id
}

const addBind = async () => {
  await bindWaveData({
    waveDataIds: waveData.value,
    facilityId: bindFacilityId.value
  }).then((value: any) => {
    bindFormVisible.value = false
    if (value.status === 'SUCCESS') {
      ElMessage('绑定成功')
    }
  })
}

const handleEdit = (index: number, row: Faciltity) => {
  updateFormVisible.value = true
  updateForm.name = row.name
  updateForm.macAddress = row.macAddress
  updateForm.id = row.id
}
const handleDelete = async (index: number, row: Faciltity) => {
  await deleteFacility(row.id).then((value: any) => {
    if (value.status === 'SUCCESS') {
      ElMessage('删除成功')
      fetchData(1)
    }
  })
}

const handleAdd = async () => {
  await addFacility(form).then((value: any) => {
    if (value.status === 'SUCCESS') {
      ElMessage('添加成功')
      dialogFormVisible.value = false
      fetchData(1)
    }
    form.name = ''
    form.macAddress = ''
  })
}

const handleUpdate = async () => {
  await updateFacility(updateForm).then((value: any) => {
    if (value.status === 'SUCCESS') {
      ElMessage('修改成功')
      updateFormVisible.value = false
      fetchData(1)
    }
    updateForm.name = ''
    updateForm.macAddress = ''
  })
}

const pageChange =  (value: number) => {
  fetchData(value)
}
</script>
reactive,
