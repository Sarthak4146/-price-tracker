import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import api from '../services/api'

export default function Dashboard() {
  const [summary, setSummary] = useState(null)
  const [recentProducts, setRecentProducts] = useState([])
  const [loading, setLoading] = useState(true)

  // useEffect hook: runs once when the component mounts (empty [] dependency
  // array), fetching data from the backend as soon as the page loads.
  useEffect(() => {
    async function loadDashboard() {
      try {
        const [summaryRes, productsRes] = await Promise.all([
          api.get('/dashboard/summary'),
          api.get('/products'),
        ])
        setSummary(summaryRes.data)
        setRecentProducts(productsRes.data.slice(0, 5))
      } catch (err) {
        console.error('Failed to load dashboard', err)
      } finally {
        setLoading(false)
      }
    }
    loadDashboard()
  }, [])

  if (loading) return <p className="text-center mt-5">Loading dashboard...</p>

  return (
    <div>
      <h3 className="mb-4">Dashboard</h3>

      {/* Stat cards - Bootstrap grid + Flexbox handles the responsive layout */}
      <div className="row g-3 mb-4">
        <StatCard title="Products Tracked" value={summary.productsTracked} icon="📦" />
        <StatCard title="Active Alerts" value={summary.activeAlerts} icon="🔔" />
        <StatCard title="Price Changes (24h)" value={summary.priceChangesLast24h} icon="📈" />
      </div>

      <div className="d-flex justify-content-between align-items-center mb-3">
        <h5 className="mb-0">Recently Tracked Products</h5>
        <Link to="/add-product" className="btn btn-dark btn-sm">+ Add Product</Link>
      </div>

      {recentProducts.length === 0 ? (
        <p className="text-muted">No products tracked yet. Paste a link to get started!</p>
      ) : (
        <div className="row g-3">
          {recentProducts.map((p) => (
            <div className="col-12 col-sm-6 col-lg-4" key={p.id}>
              <div className="card product-card p-3 h-100">
                <div className="d-flex justify-content-between">
                  <strong className="text-truncate" style={{ maxWidth: '70%' }}>{p.name}</strong>
                  <span className="badge bg-secondary">{p.platform}</span>
                </div>
                <p className="mb-1 mt-2">₹{p.currentPrice ?? '—'}</p>
                <Link to={`/products/${p.id}/history`} className="btn btn-sm btn-outline-dark mt-auto">
                  View History
                </Link>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

function StatCard({ title, value, icon }) {
  return (
    <div className="col-12 col-sm-4">
      <div className="card card-stat p-3 text-center h-100">
        <div style={{ fontSize: '1.8rem' }}>{icon}</div>
        <h3 className="mt-1 mb-0">{value}</h3>
        <small className="text-muted">{title}</small>
      </div>
    </div>
  )
}
