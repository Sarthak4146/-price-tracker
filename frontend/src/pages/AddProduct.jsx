import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../services/api'

export default function AddProduct() {
  const [url, setUrl] = useState('')
  const [targetPrice, setTargetPrice] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await api.post('/products', {
        url,
        targetPrice: targetPrice === '' ? null : Number(targetPrice),
      })
      navigate('/products')
    } catch (err) {
      setError(err.response?.data?.error || 'Could not add this product. Check the link and try again.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="row justify-content-center">
      <div className="col-12 col-md-8 col-lg-6">
        <div className="card p-4 shadow-sm border-0" style={{ borderRadius: '14px' }}>
          <h4 className="mb-3">Add a Product to Track</h4>
          <p className="text-muted">Paste an Amazon or Flipkart product page link below.</p>

          {error && <div className="alert alert-danger py-2">{error}</div>}

          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label">Product URL</label>
              <input
                type="url"
                className="form-control"
                placeholder="https://www.amazon.in/dp/..."
                value={url}
                onChange={(e) => setUrl(e.target.value)}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Alert me when price drops to (optional)</label>
              <input
                type="number"
                step="0.01"
                className="form-control"
                placeholder="e.g. 999"
                value={targetPrice}
                onChange={(e) => setTargetPrice(e.target.value)}
              />
            </div>
            <button type="submit" className="btn btn-dark w-100" disabled={loading}>
              {loading ? 'Fetching product details...' : 'Track This Product'}
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}
