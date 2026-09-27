import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

/**
 * Wraps any page that should only be visible to logged-in users.
 * If there's no user in AuthContext, we bounce to /login instead of
 * rendering the protected page - this is what "Protected React Routes" means.
 */
export default function ProtectedRoute({ children, adminOnly = false }) {
  const { user, loading } = useAuth()

  if (loading) return null // avoid a flash-redirect while we check localStorage

  if (!user) {
    return <Navigate to="/login" replace />
  }

  if (adminOnly && user.role !== 'ADMIN') {
    return <Navigate to="/dashboard" replace />
  }

  return children
}
