import { Link } from 'react-router-dom'

export default function ProductCard({ product, onDelete, onRefresh }) {
  return (
    <div className="col-12 col-sm-6 col-lg-4">
      <div className="card product-card p-3 h-100">
        <div className="d-flex justify-content-between align-items-start">
          <strong className="text-truncate" style={{ maxWidth: '70%' }}>{product.name}</strong>
          <span className="badge bg-secondary">{product.platform}</span>
        </div>

        <p className="fs-5 fw-bold mt-2 mb-0">₹{product.currentPrice ?? '—'}</p>

        {product.targetPrice && (
          <small className="text-muted">Target: ₹{product.targetPrice}</small>
        )}

        <div className="d-flex gap-3 mt-2 small text-muted">
          <span>Min: ₹{product.minPrice ?? '—'}</span>
          <span>Max: ₹{product.maxPrice ?? '—'}</span>
          <span>Avg: ₹{product.avgPrice ? product.avgPrice.toFixed(0) : '—'}</span>
        </div>

        <div className="d-flex gap-2 mt-3">
          <Link to={`/products/${product.id}/history`} className="btn btn-sm btn-outline-dark flex-fill">
            History
          </Link>
          <button className="btn btn-sm btn-outline-secondary" onClick={() => onRefresh(product.id)}>
            ⟳
          </button>
          <button className="btn btn-sm btn-outline-danger" onClick={() => onDelete(product.id)}>
            ✕
          </button>
        </div>
      </div>
    </div>
  )
}
