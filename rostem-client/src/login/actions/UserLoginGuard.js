import React from "react";
import { withRouter } from "react-router";
import axios from "axios";
import * as rostemConstants from "../../constants/constants.js";

export default function requireUserAuth(Component) {
  class AuthenticatedComponent extends React.Component {
    state = {
      loggedIn: false
    };

    componentWillMount() {
      this.checkAuth();
    }

    checkAuth() {
      console.log(global.email);
      console.log(global.role);
      if (global.email === "") {
        this.props.history.replace("/login");
      } else if (global.role === "ROLE_ADMIN") {
        this.props.history.push("/forbidden");
      } else {
        this.setState({
          loggedIn: true
        });
      }
    }

    render() {
      return this.state.loggedIn ? <Component {...this.props} /> : null;
    }
  }

  return withRouter(AuthenticatedComponent);
}
