import localeMessageBox from '@/components/message-box/locale/en-US';
import localeLogin from '@/views/login/locale/en-US';
import localeSettings from './en-US/settings';

export default {
  'menu.swim': 'Swim Club',
  'menu.swim.dashboard': 'Overview',
  'menu.swim.users': 'Users',
  'menu.swim.coaches': 'Coaches',
  'menu.swim.courses': 'Courses',
  'menu.swim.schedules': 'Schedules',
  'menu.swim.reservations': 'Reservations',
  'menu.swim.orders': 'Orders',
  'menu.swim.evaluations': 'Evaluations',
  'menu.swim.feedbacks': 'Feedback',
  'menu.swim.params': 'System Params',
  'menu.swim.notices': 'Notices',
  'menu.swim.files': 'Uploads',
  'menu.swim.coachSchedules': 'My Schedules',
  'menu.swim.coachReservations': 'My Reservations',
  'navbar.docs': 'Docs',
  'navbar.action.locale': 'Switch to English',
  ...localeSettings,
  ...localeMessageBox,
  ...localeLogin,
};
