import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  if (!user) return null

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark px-3">
      <Link className="navbar-brand" to="/dashboard">📉 Price Tracker</Link>
      <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
        <span className="navbar-toggler-icon"></span>
      </button>
      <div className="collapse navbar-collapse" id="navMenu">
        <ul className="navbar-nav me-auto">
          <li className="nav-item"><Link className="nav-link" to="/dashboard">Dashboard</Link></li>
          <li className="nav-item"><Link className="nav-link" to="/products">Products</Link></li>
          <li className="nav-item"><Link className="nav-link" to="/add-product">Add Product</Link></li>
          <li className="nav-item"><Link className="nav-link" to="/alerts">Alerts</Link></li>
          {user.role === 'ADMIN' && (
            <li className="nav-item"><Link className="nav-link" to="/admin">Admin</Link></li>
          )}
        </ul>
        <span className="navbar-text text-light me-3">Hi, {user.username}</span>
        <button className="btn btn-outline-light btn-sm" onClick={handleLogout}>Logout</button>
      </div>
    </nav>
  )
}
