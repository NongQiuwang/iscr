export const API = {
  auth: {
    login: '/api/auth/login',
    register: '/api/auth/register',
    logout: '/api/auth/logout'
  },
  cars: {
    all: '/api/cars/all',
    list: '/api/cars/list',
    detail: (id) => `/api/cars/${id}`,
    create: '/api/cars',
    update: (id) => `/api/cars/${id}`,
    delete: (id) => `/api/cars/${id}`,
    batchDelete: '/api/cars/batch'
  },
  reservations: {
    all: '/api/reservations/all',
    list: '/api/reservations/list',
    detail: (id) => `/api/reservations/${id}`,
    byUser: (userId) => `/api/reservations/user/${userId}`,
    create: '/api/reservations',
    update: (id) => `/api/reservations/${id}`,
    updateStatus: (id) => `/api/reservations/${id}/status`,
    cancel: (id) => `/api/reservations/${id}`
  },
  users: {
    all: '/api/users/all',
    detail: (id) => `/api/users/${id}`,
    update: (id) => `/api/users/${id}`,
    delete: (id) => `/api/users/${id}`
  },
  upload: {
    carImage: '/api/upload/car-image'
  }
}
