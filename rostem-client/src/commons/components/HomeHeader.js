import React from "react";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { withRouter } from "react-router-dom";
import Button from "@material-ui/core/Button";

const titleStyle = { flex: 1 };
const stemAvatar = require("../../resources/blueStem.png");

class HomePage extends React.Component {
  constructor(props) {
    super(props);
  }

  handleRostemIconClick = () => {
    this.props.history.push("/");
  };

  handleRegisterClick() {
    this.props.history.push("/register");
  }

  handleLoginClick() {
    this.props.history.push("/login");
  }

  render() {
    return (
      <div>
        <AppBar color="secondary" position="static">
          <Toolbar>
            <img
              src={stemAvatar}
              alt=""
              height="50"
              width="50"
              style={{ paddingRight: 20 }}
              onClick={this.handleRostemIconClick.bind(this)}
            />

            <Typography color="primary" variant="h5" style={titleStyle}>
              ROSTEM
            </Typography>
            <Button color="primary" onClick={this.handleLoginClick.bind(this)}>
              Log in
            </Button>
            <Button
              variant="outlined"
              color="primary"
              onClick={this.handleRegisterClick.bind(this)}
            >
              GET STARTED
            </Button>
          </Toolbar>
        </AppBar>
      </div>
    );
  }
}

export default withRouter(HomePage);
