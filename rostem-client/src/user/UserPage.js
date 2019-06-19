import React from "react";
import MenuAppBar from "../commons/components/MenuHeader";
import CategoriesView from "./components/CategoriesView.js";
import { withRouter } from "react-router-dom";
import * as constants from "../constants/constants";

class UserPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showComponents: false,
      userEmail: ""
    };
  }

  async getUser() {
    await constants.axiosRequest
      .post(constants.BASE_URL + "/login/getdetails")
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting user");
        } else {
          this.setState({
            userEmail: res.object.user.email,
            showComponents: true
          });
        }
      });
  }

  componentDidMount() {
    this.getUser();
  }

  render() {
    return (
      <div>
        <MenuAppBar username="ROSTEM" />
        {this.state.showComponents && (
          <CategoriesView userEmail={this.state.userEmail} />
        )}
      </div>
    );
  }
}

export default withRouter(UserPage);
