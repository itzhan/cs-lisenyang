import http from './http'

export function getCourses(params) {
  return http.get('/public/courses', { params })
}

export function getCoaches(params) {
  return http.get('/public/coaches', { params })
}

export function getSchedules(params) {
  return http.get('/public/schedules', { params })
}

export function getNotices(params) {
  return http.get('/public/notices', { params })
}
