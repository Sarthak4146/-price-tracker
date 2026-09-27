import axios from 'axios'

// In Docker Compose this gets overridden via VITE_API_URL; locally it just
// hits your Spring Boot dev server directly.
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

const api = axios.create({
  baseURL: API_BASE_URL,
})

// Interceptor: attaches the JWT (from localStorage) to every outgoing
// request automatically, so individual components never have to think
// about it. This is the "Authorization: Bearer <token>" header in action.
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Interceptor: if the server ever says 401 (token invalid/expired),
// log the user out automatically and send them back to login.
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default api
