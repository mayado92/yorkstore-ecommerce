import { Link, useNavigate } from 'react-router-dom'

export default function Navbar() {
  const token = localStorage.getItem('token')
  const navigate = useNavigate()

  const logout = () => {
    localStorage.removeItem('token')
    navigate('/login')
  }

  return (
    <nav className="bg-gradient-to-r from-indigo-600 to-purple-700 text-white shadow-xl sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <Link to="/" className="text-3xl font-bold tracking-tight hover:scale-105 transition">
            YorkStore
          </Link>
          
          <div className="hidden md:flex items-center space-x-10 text-lg">
            <Link to="/" className="hover:text-yellow-300 transition font-medium">Catalog</Link>
            <Link to="/cart" className="hover:text-yellow-300 transition font-medium flex items-center">
              Cart
              {token && <span className="ml-2 bg-yellow-400 text-black text-xs px-2 py-1 rounded-full">3</span>}
            </Link>
            
            {token ? (
              <div className="flex items-center space-x-4">
                <span className="text-sm">Hello, User</span>
                <button 
                  onClick={logout}
                  className="bg-red-600 hover:bg-red-700 px-6 py-2 rounded-full font-semibold transition"
                >
                  Logout
                </button>
              </div>
            ) : (
              <>
                <Link to="/login" className="hover:text-yellow-300 transition font-medium">Login</Link>
                <Link 
                  to="/register" 
                  className="bg-yellow-400 text-black px-6 py-2 rounded-full font-bold hover:bg-yellow-500 transition"
                >
                  Sign Up
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  )
}