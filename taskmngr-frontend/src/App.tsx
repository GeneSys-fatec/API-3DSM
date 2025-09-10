import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import Cadastro from './pages/Cadastro'
import Login from './pages/Login'
import Home from './pages/Home'
import Equipes from './pages/Equipes'
import Calendario from './pages/Calendario'

function App() {
  return (
    <Router>
      <Routes>
        <Route path='/' element={<Cadastro />} />
        <Route path='/login' element={<Login />} />
        <Route path='/home' element={<Home />}/>
        <Route path='/equipes' element={<Equipes />}/>
        <Route path='/calendario' element={<Calendario />}/>
      </Routes>
    </Router>
  )
}

export default App
