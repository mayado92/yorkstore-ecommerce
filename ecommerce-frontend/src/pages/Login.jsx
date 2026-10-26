import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Login() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  const handleLogin = async (e) => {
    e.preventDefault()
    try {
      const response = await fetch('http://localhost:8080/api/auth/signin', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
      })
      if (!response.ok) {
        throw new Error('Login failed')
      }
      const data = await response.json()
      
      localStorage.setItem('token', data.token)
      
      const fixedUser = {
        ...data,
        roles: data.roles?.map(r => r.replace('ROLE_', '')) || []
      }
      localStorage.setItem('user', JSON.stringify(fixedUser))
      
      alert('Login successful!')
      navigate('/profile')
    } catch (err) {
      alert('Wrong username or password')
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-600 to-purple-700 flex items-center justify-center px-4">
      <div className="max-w-md w-full bg-white rounded-3xl shadow-2xl p-10">
        <h2 className="text-4xl font-bold text-center mb-8 text-gray-800">Welcome Back</h2>
        
        <form onSubmit={handleLogin} className="space-y-6">
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="w-full p-4 border-2 border-gray-300 rounded-xl focus:border-indigo-600 focus:outline-none text-lg"
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full p-4 border-2 border-gray-300 rounded-xl focus:border-indigo-600 focus:outline-none text-lg"
            required
          />
          
          <button
            type="submit"
            className="w-full bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 text-white py-5 rounded-xl text-xl font-bold transform hover:scale-105 transition"
          >
            Login Now
          </button>
        </form>
        
        <p className="text-center mt-8 text-gray-600">
          Don't have an account? 
          <a href="/register" className="text-indigo-600 font-bold ml-2 hover:underline">Register</a>
        </p>
      </div>
    </div>
  )
}