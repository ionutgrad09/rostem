import React from "react";
import { withRouter } from "react-router";
import * as constants from "../../constants/constants.js";
import UserPage from "../../user/UserPage.js";
import AdminPanel from "../../admin/components/AdminPanel";

export default function alreadyLoggedIn(Component) {
  class AuthenticatedComponent extends React.Component {
    constructor(props) {
      super(props);
      this.state = {
        showComponent: false,
        user: false,
        admin: false,
        firstPage: false
      };
    }

    componentDidMount() {
      this.getUser();
    }

    async getUser() {
      await constants.axiosRequest
        .post(constants.BASE_URL + "/login/getdetails")
        .then(response => {
          if (response.data.object.role === "ROLE_USER") {
            this.setState({
              user: true,
              showComponent: true
            });
          } else if (response.data.object.role === "ROLE_ADMIN") {
            this.setState({
              admin: true,
              showComponent: true
            });
          }
        })
        .catch(error => {
          this.setState({
            firstPage: true,
            showComponent: true
          });
        });
    }

    render() {
      return (
        <div>
          {this.state.showComponent && (
            <div>
              {this.state.user && <UserPage />}
              {this.state.admin && <AdminPanel />}
              {this.state.firstPage && <Component {...this.props} />}
            </div>
          )}
        </div>
      );
    }
  }

  return withRouter(AuthenticatedComponent);
}
