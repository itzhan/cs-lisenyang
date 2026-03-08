import http from './http'

export function login(data) {
  return http.post('/auth/login', data)
}

export function register(data) {
  return http.post('/auth/register', data)
}

export function getProfile() {
  return http.get('/user/profile')
}

export function updateProfile(data) {
  return http.put('/user/profile', data)
}

export function getReservations(params) {
  return http.get('/user/reservations', { params })
}

export function createReservation(data) {
  return http.post('/user/reservations', data)
}

export function cancelReservation(id, reason) {
  return http.put(`/user/reservations/${id}/cancel`, null, { params: { reason } })
}

export function submitEvaluation(data) {
  return http.post('/user/evaluations', data)
}

export function getOrders(params) {
  return http.get('/user/orders', { params })
}

export function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  return http.post('/admin/files/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}
