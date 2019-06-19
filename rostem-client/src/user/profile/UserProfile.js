import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import MenuAppBar from "../../commons/components/MenuHeader";
import { withRouter } from "react-router-dom";
import * as constants from "../../constants/constants.js";
import UserLearningProfile from "./UserLearningProfile";
import UserAccount from "./UserAccount";
import UserMessages from "./UserMessages";

const styles = {
  root: {
    flexGrow: 1
  }
};

class UserProfile extends React.Component {
  state = {
    value: 0,
    userEmail: "",
    showComponents: false
  };

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

  handleChange = (event, value) => {
    this.setState({ value });
  };

  render() {
    const { classes } = this.props;

    return (
      <div>
        <MenuAppBar username="ROSTEM" />
        <Paper square className={classes.root}>
          <Tabs
            value={this.state.value}
            onChange={this.handleChange}
            indicatorColor="secondary"
            textColor="secondary"
            centered
          >
            <Tab label="Account" />
            <Tab label="Learning" />
            <Tab label="Messages" />
          </Tabs>
        </Paper>
        {this.state.showComponents && (
          <div>
            {this.state.value === 0 && (
              <UserAccount userEmail={this.state.userEmail} />
            )}
            {this.state.value === 1 && (
              <UserLearningProfile userEmail={this.state.userEmail} />
            )}
            {this.state.value === 2 && (
              <UserMessages userEmail={this.state.userEmail} />
            )}
          </div>
        )}
      </div>
    );
  }
}

UserProfile.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withRouter(withStyles(styles)(UserProfile));
