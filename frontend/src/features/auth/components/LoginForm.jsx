import React from "react";
import { motion } from "framer-motion";
import Input from "../../../shared/components/Input.jsx";
import Button from "../../../shared/components/Button.jsx";

const LoginForm = ({
  title,
  fields,
  buttonText,
  onSubmit,
  loading,
  footer,
}) => {
  return (
    <motion.form
      onSubmit={onSubmit}
      className="bg-gray-900 text-white p-8 rounded-2xl shadow-2xl flex flex-col gap-6 w-full max-w-md mx-auto"
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.4 }}
    >
      <h2 className="text-3xl font-bold text-center text-indigo-400">
        {title}
      </h2>

      {fields.map((field, i) => (
        <div key={i}>
          <Input
            type={field.type || "text"}
            value={field.value}
            onChange={field.onChange}
            placeholder={field.placeholder}
          />
        </div>
      ))}

      <Button
        type="submit"
        disabled={loading}
        className="bg-indigo-600 hover:bg-indigo-700 text-white font-semibold rounded-xl py-2 transition"
      >
        {loading ? "Processing..." : buttonText}
      </Button>

      {footer && (
        <p className="text-sm text-gray-400 text-center mt-2">{footer}</p>
      )}
    </motion.form>
  );
};

export default LoginForm;
