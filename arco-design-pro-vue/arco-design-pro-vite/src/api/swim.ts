import axios from 'axios';

export interface PageResult<T> {
  total: number;
  records: T[];
}

export interface SysUser {
  id?: number;
  username?: string;
  realName?: string;
  phone?: string;
  email?: string;
  status?: number;
  avatar?: string;
}

export interface CoachProfile {
  id?: number;
  userId?: number;
  qualification?: string;
  specialty?: string;
  intro?: string;
  avatarUrl?: string;
  status?: number;
}

export interface Course {
  id?: number;
  courseName?: string;
  courseType?: string;
  level?: string;
  durationMin?: number;
  price?: number;
  capacity?: number;
  coverUrl?: string;
  description?: string;
  status?: number;
}

export interface CourseSchedule {
  id?: number;
  courseId?: number;
  coachId?: number;
  startTime?: string;
  endTime?: string;
  courseDate?: string;
  location?: string;
  maxCapacity?: number;
  currentCount?: number;
  status?: number;
}

export interface Reservation {
  id?: number;
  scheduleId?: number;
  userId?: number;
  reserveTime?: string;
  status?: number;
  cancelTime?: string;
  cancelReason?: string;
}

export interface OrderInfo {
  id?: number;
  orderNo?: string;
  userId?: number;
  reservationId?: number;
  amount?: number;
  payStatus?: number;
  payType?: number;
  payTime?: string;
  refundStatus?: number;
  refundTime?: string;
}

export interface Evaluation {
  id?: number;
  userId?: number;
  courseId?: number;
  reservationId?: number;
  score?: number;
  content?: string;
  status?: number;
  createTime?: string;
}

export interface Feedback {
  id?: number;
  userId?: number;
  type?: string;
  content?: string;
  status?: number;
  replyContent?: string;
  replyTime?: string;
}

export interface SysParam {
  id?: number;
  paramKey?: string;
  paramValue?: string;
  remark?: string;
  status?: number;
}

export interface Notice {
  id?: number;
  title?: string;
  content?: string;
  publishTime?: string;
  status?: number;
}

export interface FileResource {
  id?: number;
  fileName?: string;
  fileUrl?: string;
  filePath?: string;
  bizType?: string;
  bizId?: number;
  contentType?: string;
  fileSize?: number;
}

export interface TrendPoint {
  date: string;
  value: number;
}

export interface StatusPoint {
  status: number;
  value: number;
}

export interface DashboardStats {
  orderTrend: TrendPoint[];
  reservationTrend: TrendPoint[];
  payStatus: StatusPoint[];
  reservationStatus: StatusPoint[];
}

export function listUsers(params: {
  page: number;
  size: number;
  keyword?: string;
}) {
  return axios.get<PageResult<SysUser>>('/api/admin/users', {
    params,
  });
}
export function createUser(
  data: SysUser & { password?: string; roleKey?: string }
) {
  return axios.post<SysUser>('/api/admin/users', data);
}
export function updateUser(id: number, data: Partial<SysUser>) {
  return axios.put(`/api/admin/users/${id}`, data);
}
export function deleteUser(id: number) {
  return axios.delete(`/api/admin/users/${id}`);
}

export function listCoaches(params: {
  page: number;
  size: number;
  keyword?: string;
}) {
  return axios.get<PageResult<CoachProfile>>('/api/admin/coaches', {
    params,
  });
}
export function createCoach(data: CoachProfile) {
  return axios.post<CoachProfile>('/api/admin/coaches', data);
}
export function updateCoach(id: number, data: CoachProfile) {
  return axios.put(`/api/admin/coaches/${id}`, data);
}
export function deleteCoach(id: number) {
  return axios.delete(`/api/admin/coaches/${id}`);
}

export function listCourses(params: {
  page: number;
  size: number;
  keyword?: string;
}) {
  return axios.get<PageResult<Course>>('/api/admin/courses', {
    params,
  });
}
export function createCourse(data: Course) {
  return axios.post<Course>('/api/admin/courses', data);
}
export function updateCourse(id: number, data: Course) {
  return axios.put(`/api/admin/courses/${id}`, data);
}
export function deleteCourse(id: number) {
  return axios.delete(`/api/admin/courses/${id}`);
}

export function listSchedules(params: { page: number; size: number }) {
  return axios.get<PageResult<CourseSchedule>>('/api/admin/schedules', {
    params,
  });
}
export function createSchedule(data: CourseSchedule) {
  return axios.post<CourseSchedule>('/api/admin/schedules', data);
}
export function updateSchedule(id: number, data: CourseSchedule) {
  return axios.put(`/api/admin/schedules/${id}`, data);
}
export function deleteSchedule(id: number) {
  return axios.delete(`/api/admin/schedules/${id}`);
}
export function getScheduleCoaches(id: number) {
  return axios.get(`/api/admin/schedules/${id}/coaches`);
}
export function updateScheduleCoaches(id: number, coachIds: number[]) {
  return axios.put(`/api/admin/schedules/${id}/coaches`, { coachIds });
}

export function listReservations(params: { page: number; size: number }) {
  return axios.get<PageResult<Reservation>>('/api/admin/reservations', {
    params,
  });
}
export function updateReservationStatus(id: number, status: number) {
  return axios.put(`/api/admin/reservations/${id}/status`, null, {
    params: { status },
  });
}

export function listOrders(params: { page: number; size: number }) {
  return axios.get<PageResult<OrderInfo>>('/api/admin/orders', {
    params,
  });
}
export function updateOrderPayStatus(id: number, payStatus: number) {
  return axios.put(`/api/admin/orders/${id}/pay-status`, null, {
    params: { payStatus },
  });
}
export function refundOrder(id: number) {
  return axios.put(`/api/admin/orders/${id}/refund`);
}

export function listEvaluations(params: { page: number; size: number }) {
  return axios.get<PageResult<Evaluation>>('/api/admin/evaluations', {
    params,
  });
}
export function deleteEvaluation(id: number) {
  return axios.delete(`/api/admin/evaluations/${id}`);
}

export function listFeedbacks(params: { page: number; size: number }) {
  return axios.get<PageResult<Feedback>>('/api/admin/feedbacks', {
    params,
  });
}
export function replyFeedback(id: number, replyContent: string) {
  return axios.put(`/api/admin/feedbacks/${id}/reply`, { replyContent });
}

export function listParams(params: { page: number; size: number }) {
  return axios.get<PageResult<SysParam>>('/api/admin/params', {
    params,
  });
}
export function createParam(data: SysParam) {
  return axios.post<SysParam>('/api/admin/params', data);
}
export function updateParam(id: number, data: SysParam) {
  return axios.put(`/api/admin/params/${id}`, data);
}
export function deleteParam(id: number) {
  return axios.delete(`/api/admin/params/${id}`);
}

export function listNotices(params: { page: number; size: number }) {
  return axios.get<PageResult<Notice>>('/api/admin/notices', {
    params,
  });
}
export function createNotice(data: Notice) {
  return axios.post<Notice>('/api/admin/notices', data);
}
export function updateNotice(id: number, data: Notice) {
  return axios.put(`/api/admin/notices/${id}`, data);
}
export function deleteNotice(id: number) {
  return axios.delete(`/api/admin/notices/${id}`);
}

export function getStatsOverview() {
  return axios.get('/api/admin/stats/overview');
}

export function getDashboardStats() {
  return axios.get<DashboardStats>('/api/admin/stats/dashboard');
}

export function uploadFile(formData: FormData) {
  return axios.post<FileResource>('/api/admin/files/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
}

export function listCoachSchedules(params: { page: number; size: number }) {
  return axios.get<PageResult<CourseSchedule>>('/api/coach/schedules', {
    params,
  });
}
export function listCoachReservations(params: { page: number; size: number }) {
  return axios.get<PageResult<Reservation>>('/api/coach/reservations', {
    params,
  });
}
export function updateCoachReservationStatus(id: number, status: number) {
  return axios.put(`/api/coach/reservations/${id}/status`, null, {
    params: { status },
  });
}
