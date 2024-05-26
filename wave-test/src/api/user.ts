import request from '@/utils/request'

export const getUsers = (params: any) =>
  request({
    url: '/user/manage/page',
    method: 'get',
    params
  })

  export const addUser = (data: any) =>
  request({
    url: '/user/manage/add',
    method: 'post',
    data
  })


export const updateUser = (data: any) =>
  request({
    url: `/user/manage/update`,
    method: 'post',
    data
  })

export const deleteUser = (userId: Number) =>
  request({
    url: `/user/manage/${userId}`,
    method: 'delete'
  })

export const login = (data: any) =>
  request({
    url: '/user/login',
    method: 'post',
    data
  })


export const register = (data: any) =>
  request({
    url: '/user/register',
    method: 'put',
    data
  })
