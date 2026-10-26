import { Routes, Route } from 'react-router-dom'
import Navbar from './components/Navbar'
import Catalog from './pages/Catalog'
import Cart from './pages/Cart'
import Checkout from './pages/Checkout'
import Login from './pages/Login'
import Register from './pages/Register'
import Admin from './pages/Admin'
import CategoryProducts from './pages/CategoryProducts' // NEW IMPORT
import ProductDetails from './pages/ProductDetails'     // NEW IMPORT
import Profile from './pages/Profile' 
import { useEffect } from 'react'

function App() {
  useEffect(() => {
    document.title = "YorkStore - Premium E-Commerce"
  }, [])

  return (
    <>
      <Navbar />
      <div className="min-h-screen bg-gray-50">
        <Routes>
          <Route path="/category/:categoryId" element={<CategoryProducts />} />
          <Route path="/product/:productId" element={<ProductDetails />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/" element={<Catalog />} />
          <Route path="/cart" element={<Cart />} />
          <Route path="/checkout" element={<Checkout />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/admin" element={<Admin />} />
        </Routes>
      </div>
    </>
  )
}

export default App