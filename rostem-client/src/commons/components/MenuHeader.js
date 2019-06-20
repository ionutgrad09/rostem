import React from "react";
import PropTypes from "prop-types";
import {withStyles} from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import AccountCircle from "@material-ui/icons/AccountCircle";
import MenuItem from "@material-ui/core/MenuItem";
import Menu from "@material-ui/core/Menu";
import {withRouter} from "react-router-dom";
import MenuOutlined from "@material-ui/icons/MenuOutlined";
import * as constants from "../../constants/constants";

const styles = {
  root: {
    flexGrow: 1
  },
  grow: {
    flexGrow: 1
  },
  menuButton: {
    marginLeft: -12,
    marginRight: 20
  }
};

const stemAvatar = require("../../resources/blueStem.png");

class MenuAppBar extends React.Component {
  constructor(props) {
    super(props);
    this.username = this.props.username ? this.props.username : "user";
    this.state = {
      auth: true,
      anchorEl: null
    };
  }

  handleChange = event => {
    this.setState({ auth: event.target.checked });
  };

  handleMenu = event => {
    this.setState({ anchorEl: event.currentTarget });
  };

  handleClose = () => {
    this.setState({ anchorEl: null });
  };

  handleLogout = () => {
    constants.axiosRequest.post(constants.BASE_URL + "/logout");
    this.props.history.replace("/login");
  };

  handleProfile = () => {
    this.props.history.push("/profile");
  };

  handleRostemIconClick = () => {
    this.props.history.push("/categories");
  };

  render() {
    const { classes } = this.props;
    const { auth, anchorEl } = this.state;
    const open = Boolean(anchorEl);

    return (
      <div className={classes.root}>
        <AppBar color="secondary" position="static">
          <Toolbar>
            <img
              src={stemAvatar}
              alt=""
              height="50"
              width="50"
              style={{ paddingRight: 20 }}
              onClick={
                this.props.username !== "ADMINISTRATOR"
                  ? this.handleRostemIconClick.bind(this)
                  : null
              }
            />
            <Typography variant="h6" color="primary" className={classes.grow}>
              {this.props.username}
            </Typography>
            {auth && (
              <div>
                <MenuOutlined
                  aria-owns={open ? "menu-appbar" : undefined}
                  aria-haspopup="true"
                  onClick={this.handleMenu}
                  color="inherit"
                >
                  <AccountCircle />
                </MenuOutlined>
                <Menu
                  id="menu-appbar"
                  anchorEl={anchorEl}
                  anchorOrigin={{
                    vertical: "top",
                    horizontal: "right"
                  }}
                  transformOrigin={{
                    vertical: "top",
                    horizontal: "right"
                  }}
                  open={open}
                  onClose={this.handleClose}
                >
                  {this.props.username !== "ADMINISTRATOR" && (
                    <MenuItem onClick={this.handleProfile.bind(this)}>
                      Profile
                    </MenuItem>
                  )}
                  <MenuItem onClick={this.handleLogout.bind(this)}>
                    Logout
                  </MenuItem>
                </Menu>
              </div>
            )}
          </Toolbar>
        </AppBar>
      </div>
    );
  }
}

MenuAppBar.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withRouter(withStyles(styles)(MenuAppBar));
