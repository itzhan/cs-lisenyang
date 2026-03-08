import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: '',
    username: '',
    role: '',
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
  },
  actions: {
    load() {
      if (this.token) return
      const token = localStorage.getItem('token')
      if (token) {
        this.token = token
        this.username = localStorage.getItem('username') || ''
        this.role = localStorage.getItem('role') || ''
      }
    },
    setAuth(token, username, role) {
      this.token = token
      this.username = username
      this.role = role
      localStorage.setItem('token', token)
      localStorage.setItem('username', username)
      localStorage.setItem('role', role)
    },
    logout() {
      this.token = ''
      this.username = ''
      this.role = ''
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      localStorage.removeItem('role')
    },
  },
})
