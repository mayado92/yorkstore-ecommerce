import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'

export default function Catalog() {
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    fetch('http://localhost:8080/api/public/categories')
      .then(res => {
        if (!res.ok) throw new Error("Network response was not ok")
        return res.json()
      })
      .then(data => {
        if (data.status === false) {
          setCategories([]) // Treat it as an empty list, not an error
        } else {
          setCategories(data.content || data || [])
        }
        setLoading(false)
      })
      .catch(err => {
        console.error(err)
        // If fetch completely fails, it's likely CORS or Backend is down
        setError('Connection failed. Please check if Backend is running and CORS is enabled.')
        setLoading(false)
      })
  }, [])

  if (loading) return (
    <div className="flex justify-center items-center h-screen">
      <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-indigo-600"></div>
    </div>
  )

  if (error) return (
    <div className="text-center py-20">
      <h2 className="text-2xl font-bold text-red-600">{error}</h2>
      <p className="mt-4 text-gray-600">
        If the backend is running, tell your teammate to add 
        <code className="bg-gray-200 p-1 mx-1 rounded">@CrossOrigin(origins = "http://localhost:5173")</code> 
        to the Java Controller.
      </p>
    </div>
  )

  return (
    <div className="min-h-screen bg-gray-50 py-12">
      <div className="max-w-7xl mx-auto px-4">
        <h1 className="text-4xl font-bold text-gray-800 mb-8 text-center">Browse Categories</h1>
        
        {categories.length === 0 ? (
          <div className="text-center py-20 bg-white rounded-xl shadow">
            <h2 className="text-2xl font-bold text-gray-400">Store is Empty</h2>
            <p className="mt-2 text-gray-500">No categories found. Please add them via Admin/Postman.</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {categories.map(cat => (
              <Link 
                key={cat.categoryId} 
                to={`/category/${cat.categoryId}`} 
                className="group block bg-white rounded-2xl shadow-xl hover:shadow-2xl transition transform hover:-translate-y-2 overflow-hidden border border-gray-100"
              >
                <div className="h-48 bg-gradient-to-br from-indigo-500 to-purple-600 flex items-center justify-center">
                  <span className="text-white text-4xl font-bold">{cat.categoryName.charAt(0)}</span>
                </div>
                <div className="p-8 text-center">
                  <h3 className="text-2xl font-bold text-gray-800 group-hover:text-indigo-600 transition">
                    {cat.categoryName}
                  </h3>
                  <p className="mt-4 text-indigo-500 font-semibold group-hover:underline">
                    View Products &rarr;
                  </p>
                </div>
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}