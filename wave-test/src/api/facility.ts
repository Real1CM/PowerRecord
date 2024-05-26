import request from '@/utils/request'

export const listFacility = () =>
  request({
    url: `/facility/list`,
    method: 'get'
  })



export const getFacility = (pageNumber: number) =>
  request({
    url: `/facility/page/${pageNumber}`,
    method: 'get'
  })

export const addFacility = (data: any) =>
  request({
    url: '/facility/add',
    method: 'post',
    data
  })

export const updateFacility = (data: any) =>
  request({
    url: `/facility/update`,
    method: 'post',
    data
  })

export const deleteFacility = (facilityId: Number) =>
  request({
    url: `/facility/${facilityId}`,
    method: 'delete'
  })

export const bindWaveData = (data: any) =>
  request({
    url: `/facility/bind`,
    method: 'post',
    data
  })

export const unBindWaveData = (params: any) =>
  request({
    url: `/facility/unbind`,
    method: 'post',
    params
  })
