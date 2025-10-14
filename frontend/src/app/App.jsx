import React from "react";
import { BrowserRouter } from "react-router-dom";
import RoutesConfig from "./routes.jsx";
import Navbar from "../shared/components/Navbar.jsx";

const App = () => (
  <BrowserRouter>
    <Navbar />
    <RoutesConfig />
  </BrowserRouter>
);

export default App;
