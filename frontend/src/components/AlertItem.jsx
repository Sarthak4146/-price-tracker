export default function AlertItem({ alert, onMarkRead }) {
  return (
    <div className={`card p-3 mb-2 border-0 shadow-sm ${alert.read ? 'bg-light' : ''}`} style={{ borderRadius: '10px' }}>
      <div className="d-flex justify-content-between align-items-center flex-wrap gap-2">
        <div>
          <p className="mb-1">{alert.message}</p>
          <small className="text-muted">{new Date(alert.createdAt).toLocaleString()}</small>
        </div>
        {!alert.read && (
          <button className="btn btn-sm btn-outline-dark" onClick={() => onMarkRead(alert.id)}>
            Mark as read
          </button>
        )}
      </div>
    </div>
  )
}
