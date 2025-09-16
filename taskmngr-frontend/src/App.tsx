import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import Cadastro from './pages/Cadastro'
import Login from './pages/Login'
import Home from './pages/Home'
import Equipes from './pages/Equipes'
import Calendario from './pages/Calendario'
import Tarefas from './pages/Tarefas'
import Dashboard from './pages/Dashboard'

function App() {
  return (
    <Router>
      <Routes>
        <Route path='/' element={<Cadastro />} />
        <Route path='/login' element={<Login />} />
        <Route path='/home' element={<Home />}/>
        <Route path='/equipes' element={<Equipes />}/>
        <Route path='/calendario' element={<Calendario />}/>
        <Route path='/tarefas' element={<Tarefas />}/>
        <Route path='/dashboard' element={<Dashboard />}/>
      </Routes>
    </Router>
  )
}

export default App
