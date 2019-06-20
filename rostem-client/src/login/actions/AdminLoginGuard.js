import React from "react";
import { withRouter } from "react-router";
import * as constants from "../../constants/constants.js";

export default function requireAdminAuth(Component) {
  class AuthenticatedComponent extends React.Component {
    constructor(props) {
      super(props);
      this.state = {
        showComponent: false
      };
    }

    componentDidMount() {
      this.getUser();
    }

    async getUser() {
      await constants.axiosRequest
        .post(constants.BASE_URL + "/login/getdetails")
        .then(response => {
          if (response.data.object.role !== "ROLE_ADMIN") {
            this.props.history.push("/forbidden");
          } else {
            this.setState({
              showComponent: true
            });
          }
        })
        .catch(error => {
          this.props.history.replace("/login");
        });
    }

    render() {
      return this.state.showComponent && <Component {...this.props} />;
    }
  }

  return withRouter(AuthenticatedComponent);
}
