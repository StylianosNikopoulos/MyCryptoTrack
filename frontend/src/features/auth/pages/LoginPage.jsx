import React, { useState } from "react";
import { login } from "../api/auth.api";
import { useDispatch } from "react-redux";
import { setUser } from "../../../app/store/slices/userSlice";
import { useNavigate } from "react-router-dom";
import LoginForm from "../components/LoginForm.jsx";

const LoginPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const token = await login(email, password);
      localStorage.setItem("token", token);
      dispatch(setUser({ email, token }));
      navigate("/");
    } catch (err) {
      alert(`❌ Login failed`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-800 to-black">
      <LoginForm
        title="Welcome Back 👋"
        fields={[
          { placeholder: "Email", value: email, onChange: (e) => setEmail(e.target.value) },
          { type: "password", placeholder: "Password", value: password, onChange: (e) => setPassword(e.target.value) },
        ]}
        buttonText="Sign In"
        loading={loading}
        onSubmit={handleLogin}
        footer={
          <>
            Don’t have an account?{" "}
            <a href="/register" className="text-indigo-400 hover:underline">
              Sign up
            </a>
          </>
        }
      />
    </div>
  );
};

export default LoginPage;
