<template>
  <el-container>
    <el-main>
      <el-table show-overflow-tooltip :data="tableData" style="width: 100%" border :loading="loading">
        <el-table-column fixed label="Id" prop="id" />
        <el-table-column label="名称" prop="name" width="100" />
        <el-table-column label="坐标轴数据" prop="xyValue" width="800" />
        <el-table-column label="单位长度" prop="unitValue" width="100" />
        <el-table-column label="周期" prop="period" width="100" />
        <el-table-column label="创建时间" prop="createTime" width="200" />
        <el-table-column label="更新时间" prop="updateTime" width="200" />
        <el-table-column fixed="right" width="180">
          <template #header>
            <el-button type="primary" @click="dialogFormVisible = true">添加</el-button>
          </template>
          <template #default="scope">
            <el-button size="small" @click="openDialog(scope.$index, scope.row)">修改</el-button>
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
  <el-dialog v-model="dialogFormVisible" :title="isUpdate ? '更新数据' : '添加数据'" width="500">
    <el-form :model="form">
      <el-form-item label="名称" :label-width="formLabelWidth">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="坐标轴数据" :label-width="formLabelWidth">
        <el-input v-model="form.xyValue" autocomplete="off" type="textarea" />
      </el-form-item>
      <el-form-item label="单位长度" :label-width="formLabelWidth">
        <el-input-number v-model="form.unitValue" :min="1" />
      </el-form-item>
      <el-form-item label="周期" :label-width="formLabelWidth">
        <el-input-number v-model="form.period" :min="1" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handlerSubmit"> 确认 </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { addWaveData, deleteWaveData, getWaveData, updateWaveData } from '@/api/wave'
import { ElMessage } from 'element-plus';
import { onMounted, reactive, ref } from 'vue'

const loading = ref(false)
const tableData = ref([])
const dialogFormVisible = ref(false)
const formLabelWidth = '140px'
const isUpdate = ref(false)

interface WaveData {
  id: number
  name: string
  xyValue: string
  unitValue: number
  period: number
  createTime: string
  updateTime: string
}

const form = reactive({
  id: 0,
  name: '',
  xyValue: '',
  unitValue: 0,
  period: 0
})


const pageInfo = reactive({
  total: 0,
  pageSize: 10,
  pageCount: 0,
  currentPage: 1
})


onMounted(() => {
  fetchData(1)
})

async function fetchData(page: number) {
  loading.value = true
  const { data } = await getWaveData(page)
  tableData.value = data.records
  loading.value = false

  pageInfo.currentPage = data.current
  pageInfo.pageSize = data.size
  pageInfo.total = data.total
  pageInfo.pageCount = data.pages
}

const pageChange =  (value: number) => {
  fetchData(value)
}


const openDialog = (index: number, row: WaveData) => {
  isUpdate.value = true
  dialogFormVisible.value = true
  form.id = row.id
  form.name = row.name
  form.period = row.period
  form.unitValue = row.unitValue
  form.xyValue = row.xyValue
}

const handleDelete = async (index: number, row: WaveData) => {
  const data: any = await deleteWaveData(row.id)
  if(data.status === 'SUCCESS') {
    ElMessage({
      message: '删除成功',
      duration: 1 * 1000,
      type: 'warning',
      onClose: () => fetchData(1)
    })
  }
}

const handlerSubmit = async () => {

  if(isUpdate.value) {
    const data: any = await updateWaveData(form)
    if(data.status === 'SUCCESS') {
      dialogFormVisible.value = false
      ElMessage({
        message: '修改成功',
        duration: 1 * 1000,
        type: 'success',
        onClose: () => fetchData(1)
      })
    }
  } else {

  const data: any =  await addWaveData(form)
  if(data.status === 'SUCCESS') {
    dialogFormVisible.value = false
    ElMessage({
      message: '添加成功',
      duration: 1 * 1000,
      type: 'success',
      onClose: () => fetchData(1)
    })
  }
}
}
</script>
