import React, { useState } from "react";
import { login } from "../api/auth.api";
import { useDispatch } from "react-redux";
import { setUser } from "../../../app/store/slices/userSlice";
import { useNavigate } from "react-router-dom";
import LoginForm from "../components/LoginForm.jsx";
import toast from "react-hot-toast";

const LoginPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const validateInputs = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[a-z]{2,}$/i;
    if (!emailRegex.test(email)) { toast.error("Enter valid email"); return false; }
    if (password.length < 4) { toast.error("Password min 4 chars"); return false; }
    return true;
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    if (!validateInputs()) return;
    setLoading(true);
    try {
      const token = await login(email, password);
      localStorage.setItem("token", token);
      dispatch(setUser({ email, token }));
      toast.success("🎉 Logged in!");
      navigate("/");
    } catch (err) {
      toast.error(err.message || "❌ Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-primary-gradient">
      <LoginForm
        title="Welcome Back 👋"
        fields={[
          { placeholder: "Email", value: email, onChange: (e) => setEmail(e.target.value) },
          { type: "password", placeholder: "Password", value: password, onChange: (e) => setPassword(e.target.value) },
        ]}
        buttonText="Sign In"
        loading={loading}
        onSubmit={handleLogin}
        footer={<span>Don’t have an account? <a href="/register">Sign up</a></span>}
      />
    </div>
  );
};

export default LoginPage;
