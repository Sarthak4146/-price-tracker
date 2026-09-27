import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import api from '../services/api'
import PriceChart from '../components/PriceChart'

export default function PriceHistoryPage() {
  const { id } = useParams()
  const [product, setProduct] = useState(null)
  const [history, setHistory] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    async function load() {
      try {
        const [productRes, historyRes] = await Promise.all([
          api.get(`/products/${id}`),
          api.get(`/products/${id}/history`),
        ])
        setProduct(productRes.data)
        setHistory(historyRes.data)
      } catch (err) {
        console.error(err)
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [id])

  if (loading) return <p className="text-center mt-5">Loading...</p>
  if (!product) return <p className="text-center mt-5">Product not found.</p>

  return (
    <div>
      <Link to="/products" className="btn btn-sm btn-outline-secondary mb-3">← Back to Products</Link>

      <div className="card p-4 shadow-sm border-0 mb-4" style={{ borderRadius: '14px' }}>
        <div className="d-flex justify-content-between flex-wrap gap-2">
          <div>
            <h4 className="mb-1">{product.name}</h4>
            <span className="badge bg-secondary">{product.platform}</span>
          </div>
          <h3 className="mb-0">₹{product.currentPrice ?? '—'}</h3>
        </div>
      </div>

      {/* Price Analysis: min / max / avg - the "Price Analysis" requirement */}
      <div className="row g-3 mb-4">
        <AnalysisCard label="Minimum Price" value={product.minPrice} />
        <AnalysisCard label="Maximum Price" value={product.maxPrice} />
        <AnalysisCard label="Average Price" value={product.avgPrice ? product.avgPrice.toFixed(2) : null} />
      </div>

      <div className="card p-4 shadow-sm border-0" style={{ borderRadius: '14px' }}>
        <h5 className="mb-3">Price History</h5>
        <PriceChart data={history} />
      </div>
    </div>
  )
}

function AnalysisCard({ label, value }) {
  return (
    <div className="col-12 col-sm-4">
      <div className="card card-stat p-3 text-center h-100">
        <small className="text-muted">{label}</small>
        <h4 className="mt-1 mb-0">{value != null ? `₹${value}` : '—'}</h4>
      </div>
    </div>
  )
}
