<template>
  <div class="home">
    <!-- Hero -->
    <section class="hero">
      <div class="container hero-content">
        <div class="hero-text">
          <h1 class="hero-title">畅游水域，乐享健康</h1>
          <p class="hero-desc">翔泳社为您提供专业游泳培训课程、资深教练团队与便捷的在线预约服务，开启您的水上运动之旅。</p>
          <div class="hero-actions">
            <a-button type="primary" size="large" @click="$router.push('/courses')">
              浏览课程
            </a-button>
            <a-button size="large" @click="$router.push('/coaches')">
              了解教练
            </a-button>
          </div>
        </div>
        <div class="hero-visual">
          <div class="hero-card">
            <div class="hero-stat">
              <span class="stat-num">50+</span>
              <span class="stat-label">精品课程</span>
            </div>
            <div class="hero-stat">
              <span class="stat-num">20+</span>
              <span class="stat-label">专业教练</span>
            </div>
            <div class="hero-stat">
              <span class="stat-num">1000+</span>
              <span class="stat-label">服务学员</span>
            </div>
          </div>
        </div>
      </div>
      <div class="hero-wave">
        <svg viewBox="0 0 1440 100" preserveAspectRatio="none">
          <path d="M0,40 C360,100 1080,0 1440,60 L1440,100 L0,100 Z" fill="var(--color-bg)" />
        </svg>
      </div>
    </section>

    <!-- 热门课程 -->
    <section class="page-section">
      <div class="container">
        <h2 class="section-title">热门课程</h2>
        <p class="section-subtitle">精心设计的课程体系，满足不同水平的学员需求</p>
        <div class="card-grid" v-if="courses.length">
          <div
            class="card-item course-card"
            v-for="c in courses"
            :key="c.id"
            @click="$router.push(`/courses/${c.id}`)"
          >
            <div class="card-cover">
              <img :src="c.coverUrl || 'https://images.unsplash.com/photo-1530549387789-4c1017266635?w=400&h=300&fit=crop&q=80'" alt="课程封面" />
            </div>
            <div class="card-body">
              <div class="card-title">{{ c.courseName }}</div>
              <div class="card-desc">{{ c.description || '暂无描述' }}</div>
            </div>
            <div class="card-footer">
              <span class="price">¥{{ c.price }}</span>
              <a-tag color="arcoblue" size="small">{{ c.level || '全级别' }}</a-tag>
            </div>
          </div>
        </div>
        <div class="empty-state" v-else-if="!loading">
          <div class="empty-icon"><Inbox :size="48" :stroke-width="1.5" /></div>
          <p>暂无课程数据</p>
        </div>
        <div style="text-align: center; margin-top: 32px" v-if="courses.length">
          <a-button type="outline" @click="$router.push('/courses')">查看全部课程</a-button>
        </div>
      </div>
    </section>

    <!-- 教练团队 -->
    <section class="page-section coach-section">
      <div class="container">
        <h2 class="section-title">教练团队</h2>
        <p class="section-subtitle">经验丰富的专业教练，为您保驾护航</p>
        <div class="coach-grid" v-if="coaches.length">
          <div class="coach-card" v-for="coach in coaches" :key="coach.id">
            <a-avatar :size="72" :style="{ backgroundColor: avatarColors[coach.id % avatarColors.length] }">
              {{ coach.name?.charAt(0) || '教' }}
            </a-avatar>
            <div class="coach-name">{{ coach.name }}</div>
            <div class="coach-specialty">{{ coach.specialty || '游泳教练' }}</div>
            <div class="coach-exp" v-if="coach.experience">{{ coach.experience }}年教学经验</div>
          </div>
        </div>
        <div style="text-align: center; margin-top: 32px" v-if="coaches.length">
          <a-button type="outline" @click="$router.push('/coaches')">查看全部教练</a-button>
        </div>
      </div>
    </section>

    <!-- 公告 -->
    <section class="page-section">
      <div class="container">
        <h2 class="section-title">最新公告</h2>
        <p class="section-subtitle">了解翔泳社最新动态与通知</p>
        <div class="notice-list" v-if="notices.length">
          <div class="notice-item" v-for="n in notices" :key="n.id">
            <div class="notice-dot"></div>
            <div class="notice-content">
              <div class="notice-title">{{ n.title }}</div>
              <div class="notice-time">{{ n.createTime }}</div>
            </div>
          </div>
        </div>
        <div class="empty-state" v-else-if="!loading">
          <div class="empty-icon"><Inbox :size="48" :stroke-width="1.5" /></div>
          <p>暂无公告</p>
        </div>
        <div style="text-align: center; margin-top: 32px" v-if="notices.length">
          <a-button type="outline" @click="$router.push('/notices')">查看全部公告</a-button>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCourses, getCoaches, getNotices } from '../api/public'
import { Inbox } from 'lucide-vue-next'

const courses = ref([])
const coaches = ref([])
const notices = ref([])
const loading = ref(true)

const avatarColors = ['#1890FF', '#00B42A', '#E6A23C', '#F53F3F', '#722ED1', '#0FC6C2']

onMounted(async () => {
  try {
    const [courseRes, coachRes, noticeRes] = await Promise.allSettled([
      getCourses({ page: 1, size: 6 }),
      getCoaches({ page: 1, size: 6 }),
      getNotices({ page: 1, size: 5 }),
    ])
    if (courseRes.status === 'fulfilled') courses.value = courseRes.value.data?.records || []
    if (coachRes.status === 'fulfilled') coaches.value = coachRes.value.data?.records || []
    if (noticeRes.status === 'fulfilled') notices.value = noticeRes.value.data?.records || []
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.hero {
  position: relative;
  background: linear-gradient(135deg, #e6f4ff 0%, #f0f7ff 50%, #e8f0fe 100%);
  padding: 80px 0 100px;
  overflow: hidden;
}

.hero-content {
  display: flex;
  align-items: center;
  gap: 60px;
}

.hero-text {
  flex: 1;
}

.hero-title {
  font-size: 42px;
  font-weight: 700;
  line-height: 1.2;
  color: var(--color-text);
  margin-bottom: 16px;
}

.hero-desc {
  font-size: 16px;
  line-height: 1.8;
  color: var(--color-text-secondary);
  margin-bottom: 32px;
  max-width: 480px;
}

.hero-actions {
  display: flex;
  gap: 12px;
}

.hero-visual {
  flex-shrink: 0;
}

.hero-card {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 32px 40px;
  box-shadow: var(--shadow-lg);
  display: flex;
  gap: 40px;
}

.hero-stat {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-num {
  font-size: 32px;
  font-weight: 700;
  color: var(--color-primary);
}

.stat-label {
  font-size: 13px;
  color: var(--color-text-tertiary);
  margin-top: 4px;
}

.hero-wave {
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  line-height: 0;
}

.hero-wave svg {
  width: 100%;
  height: 60px;
}

.course-card {
  cursor: pointer;
}

.coach-section {
  background: var(--color-surface);
}

.coach-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 24px;
}

.coach-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 28px 16px;
  border-radius: var(--radius-md);
  background: var(--color-bg);
  transition: transform 0.25s, box-shadow 0.25s;
}

.coach-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.coach-name {
  margin-top: 12px;
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text);
}

.coach-specialty {
  margin-top: 4px;
  font-size: 13px;
  color: var(--color-primary);
}

.coach-exp {
  margin-top: 4px;
  font-size: 12px;
  color: var(--color-text-tertiary);
}

.notice-list {
  max-width: 700px;
  margin: 0 auto;
}

.notice-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px 0;
  border-bottom: 1px solid var(--color-border-light);
}

.notice-item:last-child {
  border-bottom: none;
}

.notice-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--color-primary);
  margin-top: 6px;
  flex-shrink: 0;
}

.notice-content {
  flex: 1;
}

.notice-title {
  font-size: 15px;
  font-weight: 500;
  color: var(--color-text);
}

.notice-time {
  font-size: 12px;
  color: var(--color-text-tertiary);
  margin-top: 4px;
}

@media (max-width: 768px) {
  .hero {
    padding: 48px 0 80px;
  }
  .hero-content {
    flex-direction: column;
    gap: 32px;
  }
  .hero-title {
    font-size: 28px;
  }
  .hero-card {
    padding: 20px 24px;
    gap: 24px;
  }
  .stat-num {
    font-size: 24px;
  }
  .coach-grid {
    grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  }
}
</style>
