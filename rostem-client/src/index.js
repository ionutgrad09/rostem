import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import App from "./App";
import AdminPanel from "./admin/components/AdminPanel";
import { Route, BrowserRouter as Router, Redirect } from "react-router-dom";
import { MuiThemeProvider, createMuiTheme } from "@material-ui/core/styles";
import UserPage from "./user/UserPage";
import TutorialsView from "./user/components/TutorialsView";
import * as constants from "./constants/constants";
import UserProfile from "./user/profile/UserProfile";
import RegisterPage from "./login/components/RegisterPage";
import LoginPage from "./login/components/LoginPage";
import requireAdminAuth from "./login/actions/AdminLoginGuard";
import requireUserAuth from "./login/actions/UserLoginGuard";
import Forbidden from "./commons/components/Forbidden";
import alreadyLoggedIn from "./login/actions/AlreadyLoginGuard";

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
      <Route exact path="/" component={alreadyLoggedIn(App)} />
      <Route path="/admin" component={AdminPanel} />
      <Route exact path="/categories" component={requireUserAuth(UserPage)} />
      <Route
        path="/categories/:categoryName"
        component={requireUserAuth(TutorialsView)}
      />
      <Route exact path="/profile" component={requireUserAuth(UserProfile)} />
      <Route exact path="/register" component={RegisterPage} />
      <Route exact path="/login" component={LoginPage} />
      <Route exact path="/forbidden" component={Forbidden} />
    </MuiThemeProvider>
  </Router>
);

ReactDOM.render(routing, document.getElementById("root"));
