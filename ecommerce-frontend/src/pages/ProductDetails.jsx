import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'

export default function ProductDetails() {
  const { productId } = useParams()
  const navigate = useNavigate()
  const [product, setProduct] = useState(null)
  
  useEffect(() => {
  
    fetch(`http://localhost:8080/api/public/products`) 
      .then(res => res.json())
      .then(data => {
        // Finding the product manually from the list if direct ID fetch isn't there
        const found = data.content.find(p => p.productId.toString() === productId)
        setProduct(found)
      })
  }, [productId])

  const addToCart = () => {
    const token = localStorage.getItem('token')
    if (!token) { alert('Please login first!'); return }
    fetch(`http://localhost:8080/api/public/product/${productId}/quantity/1`, {
      method: 'POST', headers: { 'Authorization': 'Bearer ' + token }
    }).then(() => alert('Added to cart!'))
  }

  if (!product) return <div className="text-center py-20">Loading Details...</div>

  return (
    <div className="min-h-screen bg-gray-50 py-12">
      <div className="max-w-5xl mx-auto px-4">
        <button onClick={() => navigate(-1)} className="mb-6 text-indigo-600 font-bold hover:underline">
          &larr; Back
        </button>
        
        <div className="bg-white rounded-2xl shadow-xl overflow-hidden flex flex-col md:flex-row">
          <div className="md:w-1/2">
            <img src={product.image || 'https://via.placeholder.com/500'} alt="Product" className="w-full h-full object-cover"/>
          </div>
          <div className="p-10 md:w-1/2 flex flex-col justify-center">
            <h1 className="text-4xl font-bold mb-4">{product.productName}</h1>
            <p className="text-gray-600 text-lg mb-6">{product.description}</p>
            
            <div className="mb-6">
              <p className="text-sm text-gray-500 uppercase tracking-wide">Price</p>
              <div className="flex items-center gap-4">
                <span className="text-4xl font-bold text-indigo-600">${product.specialPrice}</span>
                {product.discount > 0 && (
                   <span className="bg-red-100 text-red-600 px-3 py-1 rounded-full text-sm font-bold">
                     {product.discount}% OFF
                   </span>
                )}
              </div>
            </div>

            <div className="mb-8">
              <p className="text-sm text-gray-500 uppercase tracking-wide mb-2">Availability</p>
              {/* Inventory Logic */}
              <p className={`font-bold ${product.quantity > 0 ? 'text-green-600' : 'text-red-600'}`}>
                {product.quantity > 0 ? `In Stock (${product.quantity} remaining)` : 'Out of Stock'}
              </p>
            </div>

            <button 
              onClick={addToCart}
              disabled={product.quantity <= 0}
              className="w-full bg-indigo-600 text-white py-4 rounded-xl font-bold text-xl hover:bg-indigo-700 transition disabled:bg-gray-400">
              {product.quantity > 0 ? 'Add to Cart' : 'Sold Out'}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}