import React from "react";
import MenuAppBar from "../commons/components/MenuHeader";
import CategoriesView from "./components/CategoriesView.js";
import { withRouter } from "react-router-dom";

class UserPage extends React.Component {
  render() {
    return (
      <div>
        <MenuAppBar username="ROSTEM" />
        <CategoriesView />
      </div>
    );
  }
}

export default withRouter(UserPage);
