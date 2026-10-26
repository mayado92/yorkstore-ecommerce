import { useState, useEffect } from 'react'
import { useParams, Link, useNavigate } from 'react-router-dom'

export default function CategoryProducts() {
  const { categoryId } = useParams()
  const navigate = useNavigate()
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)
  
  // Sorting and Pagination State
  const [sortBy, setSortBy] = useState('price') // 'price' or 'productName'
  const [sortOrder, setSortOrder] = useState('asc')
  const [pageNumber, setPageNumber] = useState(0)
  const pageSize = 5 // Per specs: "product cards in multiple pages"

  useEffect(() => {
    setLoading(true)
    // Fetch with query params for sort and pagination
    fetch(`http://localhost:8080/api/public/categories/${categoryId}/products?pageNumber=${pageNumber}&pageSize=${pageSize}&sortBy=${sortBy}&sortOrder=${sortOrder}`)
      .then(res => res.json())
      .then(data => {
        setProducts(data.content || [])
        setLoading(false)
      })
      .catch(err => setLoading(false))
  }, [categoryId, pageNumber, sortBy, sortOrder])

  const addToCart = (productId) => {
    const token = localStorage.getItem('token')
    if (!token) { alert('Please login first!'); return }
    fetch(`http://localhost:8080/api/public/product/${productId}/quantity/1`, {
      method: 'POST', headers: { 'Authorization': 'Bearer ' + token }
    }).then(() => alert('Added to cart!'))
  }

  return (
    <div className="min-h-screen bg-gray-50 py-12">
      <div className="max-w-7xl mx-auto px-4">
        {/* Back Button */}
        <button onClick={() => navigate('/')} className="mb-6 text-indigo-600 font-bold hover:underline">
          &larr; Back to Categories
        </button>

        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold">Products</h1>
          
          {/* Sorting Controls */}
          <div className="flex gap-4">
            <select className="p-2 border rounded" onChange={e => setSortBy(e.target.value)} value={sortBy}>
              <option value="price">Sort by Price</option>
              <option value="productName">Sort by Name</option>
            </select>
            <select className="p-2 border rounded" onChange={e => setSortOrder(e.target.value)} value={sortOrder}>
              <option value="asc">Ascending</option>
              <option value="desc">Descending</option>
            </select>
          </div>
        </div>

        {loading ? <p>Loading...</p> : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
            {products.map(product => (
              <div key={product.productId} className="bg-white rounded-xl shadow-lg overflow-hidden">
                 {/* Link to Product Detail */}
                <Link to={`/product/${product.productId}`}>
                  <img src={product.image || 'https://via.placeholder.com/300'} alt={product.productName} className="w-full h-48 object-cover hover:opacity-90 transition"/>
                </Link>
                <div className="p-6">
                  <Link to={`/product/${product.productId}`}>
                    <h3 className="font-bold text-xl mb-2 hover:text-indigo-600">{product.productName}</h3>
                  </Link>
                  <p className="text-2xl font-bold text-purple-600">${product.specialPrice}</p>
                  <button onClick={() => addToCart(product.productId)} className="mt-4 w-full bg-indigo-600 text-white py-2 rounded hover:bg-indigo-700">
                    Add to Cart
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Pagination Controls */}
        <div className="flex justify-center mt-12 gap-4">
          <button disabled={pageNumber === 0} onClick={() => setPageNumber(p => p - 1)} className="px-4 py-2 bg-gray-200 rounded disabled:opacity-50">Previous</button>
          <span className="py-2">Page {pageNumber + 1}</span>
          <button disabled={products.length < pageSize} onClick={() => setPageNumber(p => p + 1)} className="px-4 py-2 bg-gray-200 rounded disabled:opacity-50">Next</button>
        </div>
      </div>
    </div>
  )
}