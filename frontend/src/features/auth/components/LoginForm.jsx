import React from "react";

const LoginForm = ({ title, fields, buttonText, loading, onSubmit, footer }) => {
  return (
    <form className="form-card animate-fadeInUp" onSubmit={onSubmit}>
      <h2 style={{ textAlign: "center", color: "#f9fafb", fontSize: "2rem", fontWeight: "700" }}>
        {title}
      </h2>

      <div style={{ display: "flex", flexDirection: "column", gap: "1rem" }}>
        {fields.map((field, idx) => (
          <input
            key={idx}
            type={field.type || "text"}
            placeholder={field.placeholder}
            value={field.value}
            onChange={field.onChange}
            className="input-field"
          />
        ))}
      </div>

      <button type="submit" className="btn-primary" disabled={loading}>
        {loading ? "Loading..." : buttonText}
      </button>

      <div className="form-footer">{footer}</div>
    </form>
  );
};

export default LoginForm;
