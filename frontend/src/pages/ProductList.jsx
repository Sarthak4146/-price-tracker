import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import api from '../services/api'
import ProductCard from '../components/ProductCard'

export default function ProductList() {
  const [products, setProducts] = useState([])
  const [search, setSearch] = useState('')
  const [loading, setLoading] = useState(true)

  const loadProducts = async (keyword = '') => {
    setLoading(true)
    try {
      const res = await api.get('/products', { params: keyword ? { search: keyword } : {} })
      setProducts(res.data)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadProducts()
  }, [])

  // Search & Filter feature: re-queries the backend as the user types
  const handleSearchChange = (e) => {
    const value = e.target.value
    setSearch(value)
    loadProducts(value)
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Stop tracking this product?')) return
    await api.delete(`/products/${id}`)
    setProducts((prev) => prev.filter((p) => p.id !== id))
  }

  const handleRefresh = async (id) => {
    const res = await api.post(`/products/${id}/refresh`)
    setProducts((prev) => prev.map((p) => (p.id === id ? res.data : p)))
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
        <h3 className="mb-0">Tracked Products</h3>
        <Link to="/add-product" className="btn btn-dark btn-sm">+ Add Product</Link>
      </div>

      <input
        type="text"
        className="form-control mb-4"
        placeholder="Search by product name..."
        value={search}
        onChange={handleSearchChange}
      />

      {loading ? (
        <p className="text-center">Loading...</p>
      ) : products.length === 0 ? (
        <p className="text-muted">No products found.</p>
      ) : (
        <div className="row g-3">
          {products.map((p) => (
            <ProductCard key={p.id} product={p} onDelete={handleDelete} onRefresh={handleRefresh} />
          ))}
        </div>
      )}
    </div>
  )
}
