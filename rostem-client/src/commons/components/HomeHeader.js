import React from "react";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import LoginDialog from "../../login/components/LoginDialog";
import RegisterDialog from "../../login/components/RegisterDialog";
import { withRouter } from "react-router-dom";

const titleStyle = { flex: 1 };
const stemAvatar = require("../../resources/blueStem.png");

class HomePage extends React.Component {
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
            />

            <Typography color="primary" variant="h5" style={titleStyle}>
              ROSTEM
            </Typography>
            <LoginDialog />
            <RegisterDialog />
          </Toolbar>
        </AppBar>
      </div>
    );
  }
}

export default withRouter(HomePage);
