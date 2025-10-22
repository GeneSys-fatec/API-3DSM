import React from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext'; 

const RotaProtegida: React.FC = () => {
  const { isAuthenticated, loading } = useAuth(); 
  const location = useLocation();

  if (loading) return <div>Verificando sessão...</div>;

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return <Outlet />;
}

export default RotaProtegida;