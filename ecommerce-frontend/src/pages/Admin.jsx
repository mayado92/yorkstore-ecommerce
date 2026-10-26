import { useState, useEffect } from 'react'

export default function Admin() {
  const [products, setProducts] = useState([])
  const token = localStorage.getItem('token')

  useEffect(() => {
    if (!token) return
    fetch('http://localhost:8080/api/admin/products', {
      headers: { 'Authorization': 'Bearer ' + token }
    })
      .then(r => r.json())
      .then(setProducts)
  }, [token])

  if (!token) {
    return <div className="text-center py-20 text-3xl">Access Denied</div>
  }

  return (
    <div className="min-h-screen bg-gray-100 py-12">
      <div className="max-w-7xl mx-auto px-4">
        <h1 className="text-5xl font-bold mb-10 text-indigo-800">Admin Dashboard</h1>
        
        <div className="bg-white rounded-2xl shadow-2xl p-8">
          <h2 className="text-3xl font-bold mb-6">Inventory Management</h2>
          <button className="mb-6 bg-green-600 hover:bg-green-700 text-white px-8 py-4 rounded-xl text-xl font-bold">
            + Add New Product
          </button>
          
          <div className="grid gap-6">
            {products.map(p => (
              <div key={p.productId} className="border-2 border-gray-200 rounded-xl p-6 hover:border-indigo-500 transition">
                <div className="flex justify-between items-center">
                  <div>
                    <h3 className="text-2xl font-bold">{p.productName}</h3>
                    <p className="text-gray-600">Stock: {p.stock} | Price: ${p.specialPrice}</p>
                  </div>
                  <div className="flex gap-4">
                    <button className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg">Edit</button>
                    <button className="bg-red-600 hover:bg-red-700 text-white px-6 py-3 rounded-lg">Delete</button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}