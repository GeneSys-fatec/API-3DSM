import { logout } from '@/components/features/auth/authService';
import React, { createContext, useState, useContext, ReactNode, useCallback, useEffect } from 'react';

interface AuthState {
  userName: string | null;
  isAuthenticated: boolean;
}

interface AuthContextType extends AuthState {
  loginUser: (userName: string) => void;
  handleLogout: (navigate: (path: string) => void) => void;
  loading: boolean;
}

const initialAuthState: AuthState = {
  userName: null,
  isAuthenticated: false,
};

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth deve ser usado dentro de AuthProvider');
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [authState, setAuthState] = useState<AuthState>(initialAuthState);
  const [loading, setLoading] = useState(true);

  const loginUser = useCallback((userName: string) => {
    setAuthState({ userName, isAuthenticated: true });
  }, []);

  const handleLogout = useCallback(async (navigate: (path: string) => void) => {
    try {
      await logout(); 
    } catch (error) {
      console.error("Erro no logout:", error);
    } finally {
      setAuthState(initialAuthState);
      navigate("/login");
    }
  }, []);

  useEffect(() => {
    const checkSession = async () => {
      try {
        const res = await fetch("http://localhost:8080/auth/session", {
          method: "GET",
          credentials: "include", 
        });
        
        if (res.ok) {
          const data = await res.json();
          if (data.userName) {
            setAuthState({ userName: data.userName, isAuthenticated: true });
          }
        } else {
          setAuthState(initialAuthState);
        }
      } catch {
        setAuthState(initialAuthState);
      } finally {
        setLoading(false); 
      }
    };

    checkSession();
  }, []);

  return (
    <AuthContext.Provider value={{ ...authState, loginUser, handleLogout, loading }}>
      {children}
    </AuthContext.Provider>
  );
}
