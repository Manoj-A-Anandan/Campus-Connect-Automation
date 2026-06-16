import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';
import '../styles/dashboard.css';

export default function DashboardPage() {
  const navigate = useNavigate();
  const user = authService.getCurrentUser();

  const handleLogout = () => {
    authService.logout();
    navigate('/login');
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <h1>Campus Connect</h1>
        <div className="user-info">
          <span>Welcome, {user?.fullName}!</span>
          <button onClick={handleLogout} className="logout-btn">Logout</button>
        </div>
      </header>

      <main className="dashboard-main">
        <h2>Dashboard</h2>
        <div className="user-details">
          <p><strong>Email:</strong> {user?.email}</p>
          <p><strong>Role:</strong> {user?.role}</p>
          <p><strong>User ID:</strong> {user?.userId}</p>
        </div>
      </main>
    </div>
  );
}
