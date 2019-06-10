import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import App from "./App";
import AdminPanel from "./admin/components/AdminPanel";
import { Route, BrowserRouter as Router } from "react-router-dom";
import { MuiThemeProvider, createMuiTheme } from "@material-ui/core/styles";
import UserPage from "./user/UserPage";
import TutorialsView from "./user/components/TutorialsView";
import UserProfile from "./user/profile/UserProfile";
import RegisterPage from "./login/components/RegisterPage";
import LoginPage from "./login/components/LoginPage";

const theme = createMuiTheme({
  palette: {
    primary: {
      main: "#FFFFFF"
    },
    secondary: {
      main: "#041643"
    }
  }
});

const routing = (
  <Router>
    <MuiThemeProvider theme={theme}>
      <Route exact path="/" component={App} />
      <Route path="/admin" component={AdminPanel} />
      <Route exact path="/categories" component={UserPage} />
      <Route path="/categories/:categoryName" component={TutorialsView} />
      <Route exact path="/profile" component={UserProfile} />
      <Route exact path="/register" component={RegisterPage} />
      <Route exact path="/login" component={LoginPage} />
    </MuiThemeProvider>
  </Router>
);

ReactDOM.render(routing, document.getElementById("root"));
