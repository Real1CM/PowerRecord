<template>
  <el-container>
    <el-main>
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column label="Id" prop="id" />
      <el-table-column label="名称" prop="name" />
      <el-table-column label="创建时间" prop="createTime" />
      <el-table-column label="更新时间" prop="updateTime" />
      <el-table-column align="right">
        <template #header>
          <el-button type="primary" @click="dialogFormVisible = true">添加</el-button>
        </template>
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">修改</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    </el-main>
    <el-footer>
      <el-pagination background layout="prev, pager, next" 
      :total="pageInfo.total"
      v-model:page-size="pageInfo.pageSize"
      :page-count="pageInfo.pageCount"
      v-model:current-page="pageInfo.currentPage"
      @current-change="pageChange"
      style="float: right;" />
    </el-footer>
  </el-container>

  <el-dialog v-model="dialogFormVisible" title="添加用户" width="500">
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
        <el-button type="primary" @click="handleAdd"> 确认 </el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="updateFormVisible" title="修改用户" width="500">
    <el-form :model="updateForm">
      <el-form-item label="名称" :label-width="formLabelWidth">
        <el-input v-model="updateForm.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="密码" :label-width="formLabelWidth">
        <el-input v-model="updateForm.password" autocomplete="off" type="password" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="updateFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate"> 确认 </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from 'vue'
import { addUser, deleteUser, getUsers, updateUser } from '@/api/user'
import { ElMessage } from 'element-plus'


const loading = ref(false)
const form = reactive({
  name: '',
  password: ''
})

const updateForm = reactive({
  id: 0,
  name: '',
  password: ''
})


const pageInfo = reactive({
  total: 0,
  pageSize: 10,
  pageCount: 0,
  currentPage: 1
})
const tableData = ref([])
const dialogFormVisible = ref(false)
const updateFormVisible = ref(false)
const formLabelWidth = '140px'

interface User {
  id: number
  name: string
  createTime: string
  updateTime: string
}


onMounted(() => {
  fetchData(1)
})

const pageChange =  (value: number) => {
  fetchData(value)
}

async function fetchData(page: Number) {
  loading.value = true
  const { data } = await getUsers({ page: page })
  loading.value = false
  tableData.value = data.records

  pageInfo.currentPage = data.current
  pageInfo.pageSize = data.size
  pageInfo.total = data.total
  pageInfo.pageCount = data.pages
}

const handleEdit = (index: number, row: User) => {
  updateFormVisible.value = true
  updateForm.name = row.name
  updateForm.id = row.id
}
const handleDelete = async (index: number, row: User) => {
  await deleteUser(row.id).then((value: any) => {
    if (value.status === 'SUCCESS') {
      ElMessage('删除成功')
      fetchData(1)
    }
  })
}

const handleAdd = async () => {
  await addUser(form).then((value: any) => {
    if (value.status === 'SUCCESS') {
      ElMessage('添加成功')
      dialogFormVisible.value = false
      fetchData(1)
    }
    form.name = ''
    form.password = ''
  })
}

const handleUpdate = async () => {
  await updateUser(updateForm).then((value: any) => {
    if (value.status === 'SUCCESS') {
      ElMessage('修改成功')
      updateFormVisible.value = false
      fetchData(1)
    }
    updateForm.name = ''
    updateForm.password = ''
  })
}
</script>
