import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import HomePage from "./commons/components/HomeHeader";
import Presentation from "./commons/components/Cover";

class App extends Component {
  render() {
    return (
      <div>
        <HomePage />
        <Presentation />
      </div>
    );
  }
}

export default withRouter(App);
