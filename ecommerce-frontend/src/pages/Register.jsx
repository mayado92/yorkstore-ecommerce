import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Register() {
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()

  const handleRegister = async (e) => {
    e.preventDefault()
    setError('')

    try {
      const response = await fetch('http://localhost:8080/api/auth/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          username,
          email,
          password,
          roles: ["user"]   // ← THIS LINE WAS MISSING!
        })
      })

      if (response.ok) {
        alert('🎉 Registered successfully! Now login.')
        navigate('/login')
      } else {
        const data = await response.json()
        setError(data.message || 'Registration failed')
      }
    } catch (err) {
      setError('Server error - is your Spring Boot running?')
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-600 to-indigo-700 flex items-center justify-center px-4">
      <div className="max-w-md w-full bg-white rounded-3xl shadow-2xl p-10">
        <h2 className="text-4xl font-bold text-center mb-8 text-gray-800">Create Account</h2>
        
        {error && <p className="text-red-600 text-center mb-4 font-bold">{error}</p>}
        
        <form onSubmit={handleRegister} className="space-y-6">
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="w-full p-4 border-2 border-gray-300 rounded-xl focus:border-purple-600 focus:outline-none text-lg"
            required
          />
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full p-4 border-2 border-gray-300 rounded-xl focus:border-purple-600 focus:outline-none text-lg"
            required
          />
          <input
            type="password"
            placeholder="Password (min 6 chars)"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full p-4 border-2 border-gray-300 rounded-xl focus:border-purple-600 focus:outline-none text-lg"
            required
          />
          
          <button 
            type="submit" 
            className="w-full bg-gradient-to-r from-purple-600 to-indigo-600 hover:from-purple-700 hover:to-indigo-700 text-white py-5 rounded-xl text-xl font-bold transform hover:scale-105 transition"
          >
            Register Now
          </button>
        </form>
        
        <p className="text-center mt-8 text-gray-600">
          Already have an account? 
          <a href="/login" className="text-purple-600 font-bold ml-2 hover:underline">Login</a>
        </p>
      </div>
    </div>
  )
}