import { createContext, useContext, useEffect, useState } from 'react'
import api from '../services/api'

const AuthContext = createContext(null)

/**
 * Holds the logged-in user's info in memory (React state) and mirrors the
 * JWT + user info to localStorage so a page refresh doesn't log you out.
 *
 * localStorage vs sessionStorage (concept for the syllabus):
 *  - localStorage persists until explicitly cleared (survives browser close)
 *  - sessionStorage clears when the tab closes
 * We use localStorage here so "remember me" style behaviour is the default.
 */
export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const storedUser = localStorage.getItem('user')
    const token = localStorage.getItem('token')
    if (storedUser && token) {
      setUser(JSON.parse(storedUser))
    }
    setLoading(false)
  }, [])

  const login = async (username, password) => {
    const res = await api.post('/auth/login', { username, password })
    persistSession(res.data)
    return res.data
  }

  const register = async (username, email, password) => {
    const res = await api.post('/auth/register', { username, email, password })
    persistSession(res.data)
    return res.data
  }

  const persistSession = (data) => {
    localStorage.setItem('token', data.token)
    const userInfo = { username: data.username, email: data.email, role: data.role }
    localStorage.setItem('user', JSON.stringify(userInfo))
    setUser(userInfo)
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, login, register, logout, loading }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
