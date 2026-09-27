import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts'

/**
 * Renders the price history trend line.
 * ResponsiveContainer makes this chart resize with its parent - part of
 * the "responsive design" requirement, handled here instead of media queries
 * since Recharts needs a measured pixel container to draw into.
 */
export default function PriceChart({ data }) {
  const chartData = data.map((point) => ({
    date: new Date(point.checkedAt).toLocaleDateString('en-IN', { day: '2-digit', month: 'short' }),
    price: Number(point.price),
  }))

  if (chartData.length === 0) {
    return <p className="text-muted">No price history yet. Check back after the next scheduled price check.</p>
  }

  return (
    <div style={{ width: '100%', height: 320 }}>
      <ResponsiveContainer>
        <LineChart data={chartData} margin={{ top: 10, right: 20, left: 0, bottom: 0 }}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="date" />
          <YAxis domain={['auto', 'auto']} />
          <Tooltip formatter={(value) => [`₹${value}`, 'Price']} />
          <Line type="monotone" dataKey="price" stroke="#0d6efd" strokeWidth={2} dot={{ r: 3 }} />
        </LineChart>
      </ResponsiveContainer>
    </div>
  )
}
