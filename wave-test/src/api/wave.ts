import request from '@/utils/request'

export const listWaveData = (params:any) =>
  request({
    url: '/wave/data/list',
    method: 'get',
    params
  })

export const getWaveData = (page: number) =>
  request({
    url: `/wave/data/page/${page}`,
    method: 'get',
  })

  export const addWaveData = (data: any) =>
  request({
    url: '/wave/data/add',
    method: 'post',
    data
  })


export const updateWaveData = (data: any) =>
  request({
    url: `/wave/data/update`,
    method: 'post',
    data
  })

export const deleteWaveData = (waveDataId: Number) =>
  request({
    url: `/wave/data/${waveDataId}`,
    method: 'delete'
  })

  export const compareWaveData = (data:any) =>
  request({
    url: `/wave/data/compare`,
    method: 'post',
    data
  })

