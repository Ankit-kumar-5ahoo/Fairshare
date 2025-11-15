import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import './App.css'; 

// --- Import all our components ---
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import ProtectedRoute from './components/ProtectedRoute';
import GroupPage from './pages/GroupPage';
import GroupSettingsPage from './pages/GroupSettingsPage';
// --- 1. IMPORT THE NEW CHECKLIST PAGE ---
import ChecklistPage from './pages/ChecklistPage';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {/* --- Public Routes --- */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          {/* --- Protected Routes --- */}
          {/* This route now renders the Navbar + Page */}
          <Route element={<ProtectedRoute />}>
            <Route path="/" element={<DashboardPage />} />
            <Route path="/group/:groupId" element={<GroupPage />} /> 
            <Route path="/group/:groupId/settings" element={<GroupSettingsPage />} />

            {/* --- 2. ADD THE NEW CHECKLIST ROUTE --- */}
            <Route path="/checklist" element={<ChecklistPage />} />

          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;