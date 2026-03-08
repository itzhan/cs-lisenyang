import localeMessageBox from '@/components/message-box/locale/zh-CN';
import localeLogin from '@/views/login/locale/zh-CN';
import localeSettings from './zh-CN/settings';

export default {
  'menu.swim': '翔泳社管理',
  'menu.swim.dashboard': '运营概览',
  'menu.swim.users': '用户管理',
  'menu.swim.coaches': '教练管理',
  'menu.swim.courses': '课程管理',
  'menu.swim.schedules': '排课管理',
  'menu.swim.reservations': '预约管理',
  'menu.swim.orders': '订单管理',
  'menu.swim.evaluations': '评价管理',
  'menu.swim.feedbacks': '反馈管理',
  'menu.swim.params': '系统参数',
  'menu.swim.notices': '公告管理',
  'menu.swim.files': '文件上传',
  'menu.swim.coachSchedules': '我的排课',
  'menu.swim.coachReservations': '我的预约',
  'navbar.docs': '文档中心',
  'navbar.action.locale': '切换为中文',
  ...localeSettings,
  ...localeMessageBox,
  ...localeLogin,
};
