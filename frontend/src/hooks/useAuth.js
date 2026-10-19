import { useAuthStore } from '../store/authStore';
import { authApi } from '../api/authApi';

export const useAuth = () => {
  const { setUser, setTokens, logout, setLoading, setError } = useAuthStore();

  const login = async (email, password) => {
    setLoading(true);
    setError(null);
    try {
      const response = await authApi.login(email, password);
      const { accessToken, refreshToken, ...user } = response.data;
      setTokens(accessToken, refreshToken);
      setUser(user);
      return { success: true };
    } catch (error) {
      const message = error.response?.data?.message || 'Login failed';
      setError(message);
      return { success: false, message };
    } finally {
      setLoading(false);
    }
  };

  const register = async (data) => {
    setLoading(true);
    setError(null);
    try {
      const response = await authApi.register(data);
      const { accessToken, refreshToken, ...user } = response.data;
      setTokens(accessToken, refreshToken);
      setUser(user);
      return { success: true };
    } catch (error) {
      const message = error.response?.data?.message || 'Registration failed';
      setError(message);
      return { success: false, message };
    } finally {
      setLoading(false);
    }
  };

  return { login, register, logout };
};
