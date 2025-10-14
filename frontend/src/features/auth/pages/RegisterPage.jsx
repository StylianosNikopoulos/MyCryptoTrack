import React, { useState } from "react";
import { register } from "../api/auth.api";
import { useDispatch } from "react-redux";
import { setUser } from "../../../app/store/slices/userSlice";
import { useNavigate } from "react-router-dom";
import LoginForm from "../components/LoginForm.jsx";

const RegisterPage = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const token = await register({ username, email, password });
      localStorage.setItem("token", token);
      dispatch(setUser({ username, email, token }));
      navigate("/");
    } catch (err) {
      alert(`❌ Register failed`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-800 to-gray-900">
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
        footer={
          <>
            Already have an account?{" "}
            <a href="/login" className="text-indigo-400 hover:underline">
              Log in
            </a>
          </>
        }
      />
    </div>
  );
};

export default RegisterPage;
