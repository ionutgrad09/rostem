import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import App from "./App";
import AdminPanel from "./admin/components/AdminPanel";
import { BrowserRouter as Router, Route } from "react-router-dom";
import { createMuiTheme, MuiThemeProvider } from "@material-ui/core/styles";
import UserPage from "./user/UserPage";
import TutorialsView from "./user/components/TutorialsView";
import UserProfile from "./user/profile/UserProfile";
import RegisterPage from "./login/components/RegisterPage";
import LoginPage from "./login/components/LoginPage";
import requireAdminAuth from "./login/actions/AdminLoginGuard";
import requireUserAuth from "./login/actions/UserLoginGuard";
import Forbidden from "./commons/components/Forbidden";
import alreadyLoggedIn from "./login/actions/AlreadyLoginGuard";
import ActivateAccountView from "./login/components/ActivateAccountView";
import AddChapter from "./admin/components/posts/AddChapter";

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
      <Route exact path="/admin" component={requireAdminAuth(AdminPanel)} />
      <Route exact path="/categories" component={requireUserAuth(UserPage)} />
      <Route
        exact
        path="/admin/addChapter"
        component={requireAdminAuth(AddChapter)}
      />
      <Route
        path="/categories/:categoryName"
        component={requireUserAuth(TutorialsView)}
      />
      <Route exact path="/profile" component={requireUserAuth(UserProfile)} />
      <Route exact path="/register" component={alreadyLoggedIn(RegisterPage)} />
      <Route exact path="/login" component={alreadyLoggedIn(LoginPage)} />
      <Route
        path="/activate/:key"
        component={alreadyLoggedIn(ActivateAccountView)}
      />
      <Route exact path="/badRequest" component={Forbidden} />
    </MuiThemeProvider>
  </Router>
);

ReactDOM.render(routing, document.getElementById("root"));
