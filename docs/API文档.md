# 翔泳社游泳馆管理系统 API 文档

## 概述

- **Base URL**: `http://localhost:8080`
- **认证**: JWT（`Authorization: Bearer <token>`）
- **返回结构**: `{ code: 0, message: "ok", data: ... }`
- **分页返回**: `{ code: 0, data: { total: N, records: [...] } }`
- **分页参数**: `page`（从1开始）, `size`（默认10）

## 错误码

| code | 说明 |
|------|------|
| 0 | 成功 |
| 400 | 参数校验失败 |
| 401 | 未认证 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 用户 | user01 | user123 |
| 教练 | coach01 | coach123 |

---

## 一、认证相关

### 1.1 登录

**POST** `/api/auth/login`

**权限**: 无需认证

**请求体**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "admin",
    "role": "ADMIN"
  }
}
```

---

### 1.2 注册

**POST** `/api/auth/register`

**权限**: 无需认证

**请求体**:
```json
{
  "username": "user01",
  "password": "user123",
  "realName": "张三",
  "phone": "13800138000",
  "email": "user@example.com"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |
| realName | string | 否 | 真实姓名 |
| phone | string | 否 | 手机号 |
| email | string | 否 | 邮箱 |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

## 二、公开接口（无需认证）

### 2.1 课程列表

**GET** `/api/public/courses`

**权限**: 无需认证

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 10,
    "records": [
      {
        "id": 1,
        "courseName": "初级蛙泳",
        "courseType": "蛙泳",
        "level": "初级",
        "durationMin": 60,
        "price": 199.00,
        "capacity": 10,
        "coverUrl": "/uploads/xxx.jpg",
        "description": "课程描述",
        "status": 1,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

### 2.2 教练列表

**GET** `/api/public/coaches`

**权限**: 无需认证

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 5,
    "records": [
      {
        "id": 1,
        "userId": 3,
        "qualification": "国家一级游泳教练",
        "specialty": "蛙泳、自由泳",
        "intro": "教练简介",
        "avatarUrl": "/uploads/avatar.jpg",
        "status": 1,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

### 2.3 排课列表

**GET** `/api/public/schedules`

**权限**: 无需认证

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 20,
    "records": [
      {
        "id": 1,
        "courseId": 1,
        "coachId": 1,
        "startTime": "2025-03-01T09:00:00",
        "endTime": "2025-03-01T10:00:00",
        "courseDate": "2025-03-01",
        "location": "1号泳道",
        "maxCapacity": 10,
        "currentCount": 5,
        "status": 1,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

### 2.4 公告列表

**GET** `/api/public/notices`

**权限**: 无需认证

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 5,
    "records": [
      {
        "id": 1,
        "title": "开馆通知",
        "content": "公告内容",
        "publishTime": "2025-01-01T00:00:00",
        "status": 1,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

## 三、用户端接口

### 3.1 用户个人信息

**GET** `/api/user/profile`

**权限**: 需登录（USER）

**响应**:
```json
{
  "code": 0,
  "data": {
    "id": 2,
    "username": "user01",
    "realName": "张三",
    "phone": "13800138000",
    "email": "user@example.com",
    "avatar": null,
    "gender": null,
    "status": 1,
    "lastLoginTime": null,
    "createTime": "2025-01-01T00:00:00",
    "updateTime": "2025-01-01T00:00:00"
  }
}
```

---

### 3.2 更新用户个人信息

**PUT** `/api/user/profile`

**权限**: 需登录（USER）

**请求体**:
```json
{
  "realName": "张三",
  "phone": "13800138000",
  "email": "user@example.com",
  "avatar": "/uploads/avatar.jpg",
  "gender": 1
}
```

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

### 3.3 用户预约列表

**GET** `/api/user/reservations`

**权限**: 需登录（USER）

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 5,
    "records": [
      {
        "id": 1,
        "scheduleId": 1,
        "userId": 2,
        "reserveTime": "2025-01-01T00:00:00",
        "status": 1,
        "cancelTime": null,
        "cancelReason": null,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

### 3.4 创建预约

**POST** `/api/user/reservations`

**权限**: 需登录（USER）

**请求体**:
```json
{
  "scheduleId": 1
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| scheduleId | long | 是 | 排课ID |

**响应**:
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "scheduleId": 1,
    "userId": 2,
    "reserveTime": "2025-01-01T00:00:00",
    "status": 1,
    "createTime": "2025-01-01T00:00:00",
    "updateTime": "2025-01-01T00:00:00"
  }
}
```

---

### 3.5 取消预约

**PUT** `/api/user/reservations/{id}/cancel`

**权限**: 需登录（USER）

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 预约ID |

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| reason | string | 否 | 取消原因 |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

### 3.6 用户订单列表

**GET** `/api/user/orders`

**权限**: 需登录（USER）

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 3,
    "records": [
      {
        "id": 1,
        "orderNo": "ORD202501010001",
        "userId": 2,
        "reservationId": 1,
        "amount": 199.00,
        "payStatus": 1,
        "payType": 1,
        "payTime": "2025-01-01T00:00:00",
        "refundStatus": 0,
        "refundTime": null,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

### 3.7 创建评价

**POST** `/api/user/evaluations`

**权限**: 需登录（USER）

**请求体**:
```json
{
  "courseId": 1,
  "reservationId": 1,
  "score": 5,
  "content": "课程很棒，教练很专业！"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| courseId | long | 是 | 课程ID |
| reservationId | long | 是 | 预约ID |
| score | integer | 是 | 评分(1-5) |
| content | string | 否 | 评价内容 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "userId": 2,
    "courseId": 1,
    "reservationId": 1,
    "score": 5,
    "content": "课程很棒，教练很专业！",
    "status": 1,
    "createTime": "2025-01-01T00:00:00",
    "updateTime": "2025-01-01T00:00:00"
  }
}
```

---

### 3.8 提交反馈

**POST** `/api/user/feedbacks`

**权限**: 需登录（USER）

**请求体**:
```json
{
  "type": "建议",
  "content": "希望增加更多泳道"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| type | string | 否 | 反馈类型 |
| content | string | 否 | 反馈内容 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "userId": 2,
    "type": "建议",
    "content": "希望增加更多泳道",
    "status": 0,
    "replyContent": null,
    "replyTime": null,
    "createTime": "2025-01-01T00:00:00",
    "updateTime": "2025-01-01T00:00:00"
  }
}
```

---

## 四、教练端接口

### 4.1 教练排课列表

**GET** `/api/coach/schedules`

**权限**: 需登录（COACH）

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 10,
    "records": [
      {
        "id": 1,
        "courseId": 1,
        "coachId": 1,
        "startTime": "2025-03-01T09:00:00",
        "endTime": "2025-03-01T10:00:00",
        "courseDate": "2025-03-01",
        "location": "1号泳道",
        "maxCapacity": 10,
        "currentCount": 5,
        "status": 1,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

### 4.2 教练预约列表

**GET** `/api/coach/reservations`

**权限**: 需登录（COACH）

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 15,
    "records": [
      {
        "id": 1,
        "scheduleId": 1,
        "userId": 2,
        "reserveTime": "2025-01-01T00:00:00",
        "status": 1,
        "cancelTime": null,
        "cancelReason": null,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

### 4.3 更新预约状态

**PUT** `/api/coach/reservations/{id}/status`

**权限**: 需登录（COACH）

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 预约ID |

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | integer | 是 | 预约状态 |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

## 五、管理端接口

### 5.1 统计概览

**GET** `/api/admin/stats/overview`

**权限**: ADMIN 或 COACH

**响应**:
```json
{
  "code": 0,
  "data": {
    "orderCount": 100,
    "paidAmount": 19900.00,
    "refundAmount": 500.00
  }
}
```

---

### 5.2 统计仪表盘

**GET** `/api/admin/stats/dashboard`

**权限**: ADMIN 或 COACH

**响应**:
```json
{
  "code": 0,
  "data": {
    "orderTrend": [
      { "date": "2025-02-26", "value": 5 },
      { "date": "2025-02-27", "value": 8 }
    ],
    "reservationTrend": [
      { "date": "2025-02-26", "value": 10 },
      { "date": "2025-02-27", "value": 12 }
    ],
    "payStatus": [...],
    "reservationStatus": [...]
  }
}
```

---

### 5.3 文件上传

**POST** `/api/admin/files/upload`

**权限**: ADMIN

**请求类型**: `multipart/form-data`

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| file | file | 是 | 上传的文件 |
| bizType | string | 否 | 业务类型 |
| bizId | long | 否 | 业务ID |

**响应**:
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "bizType": "course",
    "bizId": 1,
    "fileName": "cover.jpg",
    "filePath": "/path/to/file",
    "fileUrl": "/uploads/2025-03-01/xxx.jpg",
    "contentType": "image/jpeg",
    "fileSize": 102400,
    "uploaderId": 1,
    "createTime": "2025-01-01T00:00:00"
  }
}
```

---

### 5.4 用户管理

#### 5.4.1 用户列表

**GET** `/api/admin/users`

**权限**: ADMIN

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |
| keyword | string | 否 | 关键词（用户名/真实姓名） |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 50,
    "records": [
      {
        "id": 2,
        "username": "user01",
        "realName": "张三",
        "phone": "13800138000",
        "email": "user@example.com",
        "status": 1,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

#### 5.4.2 创建用户

**POST** `/api/admin/users`

**权限**: ADMIN

**请求体**:
```json
{
  "username": "user02",
  "password": "user123",
  "realName": "李四",
  "phone": "13900139000",
  "email": "user02@example.com",
  "roleKey": "USER"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |
| realName | string | 否 | 真实姓名 |
| phone | string | 否 | 手机号 |
| email | string | 否 | 邮箱 |
| roleKey | string | 否 | 角色KEY，默认USER |

**响应**:
```json
{
  "code": 0,
  "data": {
    "id": 5,
    "username": "user02",
    "realName": "李四",
    "phone": "13900139000",
    "email": "user02@example.com",
    "status": 1,
    "createTime": "2025-01-01T00:00:00",
    "updateTime": "2025-01-01T00:00:00"
  }
}
```

---

#### 5.4.3 更新用户

**PUT** `/api/admin/users/{id}`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 用户ID |

**请求体**:
```json
{
  "realName": "李四",
  "phone": "13900139000",
  "email": "user02@example.com",
  "status": 1
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| realName | string | 否 | 真实姓名 |
| phone | string | 否 | 手机号 |
| email | string | 否 | 邮箱 |
| status | integer | 否 | 状态 |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

#### 5.4.4 删除用户

**DELETE** `/api/admin/users/{id}`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 用户ID |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

### 5.5 课程管理

#### 5.5.1 课程列表

**GET** `/api/admin/courses`

**权限**: ADMIN

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |
| keyword | string | 否 | 关键词（课程名称） |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 10,
    "records": [
      {
        "id": 1,
        "courseName": "初级蛙泳",
        "courseType": "蛙泳",
        "level": "初级",
        "durationMin": 60,
        "price": 199.00,
        "capacity": 10,
        "coverUrl": "/uploads/xxx.jpg",
        "description": "课程描述",
        "status": 1,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

#### 5.5.2 创建课程

**POST** `/api/admin/courses`

**权限**: ADMIN

**请求体**:
```json
{
  "courseName": "初级蛙泳",
  "courseType": "蛙泳",
  "level": "初级",
  "durationMin": 60,
  "price": 199.00,
  "capacity": 10,
  "coverUrl": "/uploads/xxx.jpg",
  "description": "课程描述",
  "status": 1
}
```

**响应**:
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "courseName": "初级蛙泳",
    "courseType": "蛙泳",
    "level": "初级",
    "durationMin": 60,
    "price": 199.00,
    "capacity": 10,
    "coverUrl": "/uploads/xxx.jpg",
    "description": "课程描述",
    "status": 1,
    "createTime": "2025-01-01T00:00:00",
    "updateTime": "2025-01-01T00:00:00"
  }
}
```

---

#### 5.5.3 更新课程

**PUT** `/api/admin/courses/{id}`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 课程ID |

**请求体**: 同创建课程

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

#### 5.5.4 删除课程

**DELETE** `/api/admin/courses/{id}`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 课程ID |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

### 5.6 教练管理

#### 5.6.1 教练列表

**GET** `/api/admin/coaches`

**权限**: ADMIN

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |
| keyword | string | 否 | 关键词（资质/专长/简介） |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 5,
    "records": [
      {
        "id": 1,
        "userId": 3,
        "qualification": "国家一级游泳教练",
        "specialty": "蛙泳、自由泳",
        "intro": "教练简介",
        "avatarUrl": "/uploads/avatar.jpg",
        "status": 1,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

#### 5.6.2 创建教练

**POST** `/api/admin/coaches`

**权限**: ADMIN

**请求体**:
```json
{
  "userId": 3,
  "qualification": "国家一级游泳教练",
  "specialty": "蛙泳、自由泳",
  "intro": "教练简介",
  "avatarUrl": "/uploads/avatar.jpg",
  "status": 1
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | long | 是 | 关联用户ID |

**响应**:
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "userId": 3,
    "qualification": "国家一级游泳教练",
    "specialty": "蛙泳、自由泳",
    "intro": "教练简介",
    "avatarUrl": "/uploads/avatar.jpg",
    "status": 1,
    "createTime": "2025-01-01T00:00:00",
    "updateTime": "2025-01-01T00:00:00"
  }
}
```

---

#### 5.6.3 更新教练

**PUT** `/api/admin/coaches/{id}`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 教练ID |

**请求体**: 同创建教练

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

#### 5.6.4 删除教练

**DELETE** `/api/admin/coaches/{id}`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 教练ID |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

### 5.7 排课管理

#### 5.7.1 排课列表

**GET** `/api/admin/schedules`

**权限**: ADMIN

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 20,
    "records": [
      {
        "id": 1,
        "courseId": 1,
        "coachId": 1,
        "startTime": "2025-03-01T09:00:00",
        "endTime": "2025-03-01T10:00:00",
        "courseDate": "2025-03-01",
        "location": "1号泳道",
        "maxCapacity": 10,
        "currentCount": 5,
        "status": 1,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

#### 5.7.2 创建排课

**POST** `/api/admin/schedules`

**权限**: ADMIN

**请求体**:
```json
{
  "courseId": 1,
  "coachId": 1,
  "startTime": "2025-03-01T09:00:00",
  "endTime": "2025-03-01T10:00:00",
  "courseDate": "2025-03-01",
  "location": "1号泳道",
  "maxCapacity": 10,
  "currentCount": 0,
  "status": 1
}
```

**响应**:
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "courseId": 1,
    "coachId": 1,
    "startTime": "2025-03-01T09:00:00",
    "endTime": "2025-03-01T10:00:00",
    "courseDate": "2025-03-01",
    "location": "1号泳道",
    "maxCapacity": 10,
    "currentCount": 0,
    "status": 1,
    "createTime": "2025-01-01T00:00:00",
    "updateTime": "2025-01-01T00:00:00"
  }
}
```

---

#### 5.7.3 更新排课

**PUT** `/api/admin/schedules/{id}`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 排课ID |

**请求体**: 同创建排课

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

#### 5.7.4 删除排课

**DELETE** `/api/admin/schedules/{id}`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 排课ID |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

#### 5.7.5 获取排课关联教练

**GET** `/api/admin/schedules/{id}/coaches`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 排课ID |

**响应**:
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "scheduleId": 1,
      "coachId": 1,
      "createTime": "2025-01-01T00:00:00"
    }
  ]
}
```

---

#### 5.7.6 绑定排课教练

**PUT** `/api/admin/schedules/{id}/coaches`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 排课ID |

**请求体**:
```json
{
  "coachIds": [1, 2]
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| coachIds | array | 否 | 教练ID列表 |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

### 5.8 预约管理

#### 5.8.1 预约列表

**GET** `/api/admin/reservations`

**权限**: ADMIN

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 50,
    "records": [
      {
        "id": 1,
        "scheduleId": 1,
        "userId": 2,
        "reserveTime": "2025-01-01T00:00:00",
        "status": 1,
        "cancelTime": null,
        "cancelReason": null,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

#### 5.8.2 更新预约状态

**PUT** `/api/admin/reservations/{id}/status`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 预约ID |

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | integer | 是 | 预约状态 |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

### 5.9 订单管理

#### 5.9.1 订单列表

**GET** `/api/admin/orders`

**权限**: ADMIN

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 100,
    "records": [
      {
        "id": 1,
        "orderNo": "ORD202501010001",
        "userId": 2,
        "reservationId": 1,
        "amount": 199.00,
        "payStatus": 1,
        "payType": 1,
        "payTime": "2025-01-01T00:00:00",
        "refundStatus": 0,
        "refundTime": null,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

#### 5.9.2 更新支付状态

**PUT** `/api/admin/orders/{id}/pay-status`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 订单ID |

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| payStatus | integer | 是 | 支付状态 |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

#### 5.9.3 退款

**PUT** `/api/admin/orders/{id}/refund`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 订单ID |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

### 5.10 评价管理

#### 5.10.1 评价列表

**GET** `/api/admin/evaluations`

**权限**: ADMIN

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 30,
    "records": [
      {
        "id": 1,
        "userId": 2,
        "courseId": 1,
        "reservationId": 1,
        "score": 5,
        "content": "课程很棒！",
        "status": 1,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

#### 5.10.2 删除评价

**DELETE** `/api/admin/evaluations/{id}`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 评价ID |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

### 5.11 反馈管理

#### 5.11.1 反馈列表

**GET** `/api/admin/feedbacks`

**权限**: ADMIN

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 20,
    "records": [
      {
        "id": 1,
        "userId": 2,
        "type": "建议",
        "content": "希望增加更多泳道",
        "status": 0,
        "replyContent": null,
        "replyTime": null,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

#### 5.11.2 回复反馈

**PUT** `/api/admin/feedbacks/{id}/reply`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 反馈ID |

**请求体**:
```json
{
  "replyContent": "感谢您的建议，我们会认真考虑！"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| replyContent | string | 是 | 回复内容 |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

### 5.12 公告管理

#### 5.12.1 公告列表

**GET** `/api/admin/notices`

**权限**: ADMIN

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 5,
    "records": [
      {
        "id": 1,
        "title": "开馆通知",
        "content": "公告内容",
        "publishTime": "2025-01-01T00:00:00",
        "status": 1,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

#### 5.12.2 创建公告

**POST** `/api/admin/notices`

**权限**: ADMIN

**请求体**:
```json
{
  "title": "开馆通知",
  "content": "公告内容",
  "publishTime": "2025-01-01T00:00:00",
  "status": 1
}
```

**响应**:
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "title": "开馆通知",
    "content": "公告内容",
    "publishTime": "2025-01-01T00:00:00",
    "status": 1,
    "createTime": "2025-01-01T00:00:00",
    "updateTime": "2025-01-01T00:00:00"
  }
}
```

---

#### 5.12.3 更新公告

**PUT** `/api/admin/notices/{id}`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 公告ID |

**请求体**: 同创建公告

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

#### 5.12.4 删除公告

**DELETE** `/api/admin/notices/{id}`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 公告ID |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

### 5.13 系统参数管理

#### 5.13.1 参数列表

**GET** `/api/admin/params`

**权限**: ADMIN

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | long | 否 | 页码，默认1 |
| size | long | 否 | 每页条数，默认10 |

**响应**:
```json
{
  "code": 0,
  "data": {
    "total": 10,
    "records": [
      {
        "id": 1,
        "paramKey": "site_name",
        "paramValue": "翔泳社游泳馆",
        "remark": "站点名称",
        "status": 1,
        "createTime": "2025-01-01T00:00:00",
        "updateTime": "2025-01-01T00:00:00"
      }
    ]
  }
}
```

---

#### 5.13.2 创建参数

**POST** `/api/admin/params`

**权限**: ADMIN

**请求体**:
```json
{
  "paramKey": "site_name",
  "paramValue": "翔泳社游泳馆",
  "remark": "站点名称",
  "status": 1
}
```

**响应**:
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "paramKey": "site_name",
    "paramValue": "翔泳社游泳馆",
    "remark": "站点名称",
    "status": 1,
    "createTime": "2025-01-01T00:00:00",
    "updateTime": "2025-01-01T00:00:00"
  }
}
```

---

#### 5.13.3 更新参数

**PUT** `/api/admin/params/{id}`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 参数ID |

**请求体**: 同创建参数

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

#### 5.13.4 删除参数

**DELETE** `/api/admin/params/{id}`

**权限**: ADMIN

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | long | 参数ID |

**响应**:
```json
{
  "code": 0,
  "message": "ok",
  "data": null
}
```

---

## 附录：状态码说明

### 预约状态 (reservation.status)
- 0: 待确认
- 1: 已确认
- 2: 已取消
- 3: 已完成

### 支付状态 (payStatus)
- 0: 未支付
- 1: 已支付
- 2: 已退款

### 退款状态 (refundStatus)
- 0: 未退款
- 1: 已退款

### 反馈状态 (feedback.status)
- 0: 待回复
- 1: 已回复

### 用户/角色状态 (status)
- 0: 禁用
- 1: 启用
