import { defineStore } from "pinia";

export const userModuleStore = defineStore('userModule',{
    state: () => {
        return {
            token: '',
            username: '',
            refreshToken: '',
        }
    },
    actions: {
        setToken(token: string) {
            this.token = token
        },
        setUsername(username: string) {
            this.username = username
        },
        setRefreshToken(refreshToken: string) {
            this.refreshToken = refreshToken
        }
    },
    getters: {
        getToken: (state) => {
            return state.token
        },
        getUsername: (state) => {
            return state.username
        },
        getRefreshToken: (state) => {
            return state.refreshToken
        }
    }
})