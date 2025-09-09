import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import Cadastro from './pages/Cadastro'
import Login from './pages/Login'

function App() {
  return (
    <Router>
      <Routes>
        <Route path='/' element={<Cadastro />} />
        <Route path='/login' element={<Login />} />
      </Routes>
    </Router>
  )
}

export default App
