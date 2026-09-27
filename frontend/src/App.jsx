import { Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import Navbar from './components/Navbar'
import ProtectedRoute from './components/ProtectedRoute'

import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import AddProduct from './pages/AddProduct'
import ProductList from './pages/ProductList'
import PriceHistoryPage from './pages/PriceHistoryPage'
import Alerts from './pages/Alerts'
import AdminPanel from './pages/AdminPanel'

function App() {
  return (
    <AuthProvider>
      <Navbar />
      <div className="container-fluid py-4 px-3 px-md-4">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route path="/dashboard" element={
            <ProtectedRoute><Dashboard /></ProtectedRoute>
          } />
          <Route path="/products" element={
            <ProtectedRoute><ProductList /></ProtectedRoute>
          } />
          <Route path="/add-product" element={
            <ProtectedRoute><AddProduct /></ProtectedRoute>
          } />
          <Route path="/products/:id/history" element={
            <ProtectedRoute><PriceHistoryPage /></ProtectedRoute>
          } />
          <Route path="/alerts" element={
            <ProtectedRoute><Alerts /></ProtectedRoute>
          } />
          <Route path="/admin" element={
            <ProtectedRoute adminOnly={true}><AdminPanel /></ProtectedRoute>
          } />

          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </div>
    </AuthProvider>
  )
}

export default App
