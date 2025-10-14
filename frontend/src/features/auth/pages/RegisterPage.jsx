import React, { useState } from "react";
import { register } from "../api/auth.api";
import { useDispatch } from "react-redux";
import { setUser } from "../../../app/store/slices/userSlice";
import { useNavigate } from "react-router-dom";
import LoginForm from "../components/LoginForm.jsx";
import toast from "react-hot-toast";

const RegisterPage = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const validateInputs = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[a-z]{2,}$/i;
    if (!username.trim()) { toast.error("Please enter a username."); return false; }
    if (!emailRegex.test(email)) { toast.error("Enter a valid email"); return false; }
    if (password.length < 4) { toast.error("Password must be at least 4 characters long"); return false; }
    return true;
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    if (!validateInputs()) return;

    setLoading(true);
    try {
      const token = await register({ username, email, password });
      localStorage.setItem("token", token);
      dispatch(setUser({ username, email, token }));
      toast.success("🎉 Account created successfully!");
      navigate("/");
    } catch (err) {
      toast.error(err.message || "❌ Registration failed. Try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-primary-gradient">
      <LoginForm
        title="Create Account ✨"
        fields={[
          { placeholder: "Username", value: username, onChange: (e) => setUsername(e.target.value) },
          { placeholder: "Email", value: email, onChange: (e) => setEmail(e.target.value) },
          { type: "password", placeholder: "Password", value: password, onChange: (e) => setPassword(e.target.value) },
        ]}
        buttonText="Sign Up"
        loading={loading}
        onSubmit={handleRegister}
        footer={<span>Already have an account? <a href="/login">Log in</a></span>}
      />
    </div>
  );
};

export default RegisterPage;
