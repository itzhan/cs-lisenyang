import axios from 'axios';
import type { RouteRecordNormalized } from 'vue-router';
import { UserState } from '@/store/modules/user/types';

export interface LoginData {
  username: string;
  password: string;
}

export interface LoginRes {
  token: string;
  username: string;
  role: string;
}
export function login(data: LoginData) {
  return axios.post<LoginRes>('/api/auth/login', data);
}

export function logout() {
  return Promise.resolve();
}

export function getUserInfo() {
  return axios.get<UserState>('/api/user/profile');
}

export function getMenuList() {
  return axios.post<RouteRecordNormalized[]>('/api/user/menu');
}
