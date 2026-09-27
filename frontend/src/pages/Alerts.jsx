import { useEffect, useState } from 'react'
import api from '../services/api'
import AlertItem from '../components/AlertItem'

export default function Alerts() {
  const [alerts, setAlerts] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadAlerts()
  }, [])

  const loadAlerts = async () => {
    setLoading(true)
    try {
      const res = await api.get('/alerts')
      setAlerts(res.data)
    } finally {
      setLoading(false)
    }
  }

  const handleMarkRead = async (id) => {
    await api.put(`/alerts/${id}/read`)
    setAlerts((prev) => prev.map((a) => (a.id === id ? { ...a, read: true } : a)))
  }

  return (
    <div>
      <h3 className="mb-4">Price Drop Alerts</h3>

      {loading ? (
        <p className="text-center">Loading...</p>
      ) : alerts.length === 0 ? (
        <p className="text-muted">No alerts yet. You'll see one here as soon as a tracked price drops to your target.</p>
      ) : (
        alerts.map((a) => <AlertItem key={a.id} alert={a} onMarkRead={handleMarkRead} />)
      )}
    </div>
  )
}
