import { useAuth } from '../context/AuthContext'

/**
 * Simple admin-only screen (ProtectedRoute with adminOnly=true guards this
 * on the frontend; SecurityConfig's hasRole("ADMIN") on /api/admin/** would
 * guard any admin-only backend endpoints you add later).
 *
 * Kept intentionally simple - it's a settings/preferences placeholder you
 * can extend with real admin features (manage users, view all products, etc.)
 * once the core app is working.
 */
export default function AdminPanel() {
  const { user } = useAuth()

  return (
    <div>
      <h3 className="mb-4">Admin Panel</h3>

      <div className="card p-4 shadow-sm border-0 mb-3" style={{ borderRadius: '14px' }}>
        <h5>Account</h5>
        <p className="mb-1"><strong>Username:</strong> {user.username}</p>
        <p className="mb-1"><strong>Email:</strong> {user.email}</p>
        <p className="mb-0"><strong>Role:</strong> {user.role}</p>
      </div>

      <div className="card p-4 shadow-sm border-0" style={{ borderRadius: '14px' }}>
        <h5>App Settings</h5>
        <p className="text-muted mb-0">
          This panel is a starting point. Ideas to extend it: a "check interval"
          control that updates <code>price-check.interval-ms</code>, a list of
          all users (new endpoint needed), or global scraping health status.
        </p>
      </div>
    </div>
  )
}
