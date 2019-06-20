import React from "react";
import { withStyles } from "@material-ui/core/styles";
import { Typography } from "@material-ui/core";
import Divider from "@material-ui/core/Divider";
import * as constants from "../../constants/constants.js";
import Box from "@material-ui/core/Box";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Link from "@material-ui/core/Link";
import Grid from "@material-ui/core/Grid";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import HomeHeader from "../../commons/components/HomeHeader";

const styles = theme => ({
  boxRoot: {
    marginTop: 100,
    marginBottom: 25,
    width: 550,
    height: "auto"
  },
  root: {
    padding: 25
  },
  title: {
    align: "center"
  },
  textField: {
    marginTop: 15,
    width: 500
  },
  actionButtons: {
    marginTop: 25
  },
  dangerError: {
    color: "red"
  }
});

class ActivateAccountView extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      activated: false
    };
  }

  async activateAccount() {
    const key = this.props.match.params.key;
    console.log(key);
    await constants.axiosRequest
      .get(constants.BASE_URL + "/activate/" + key)
      .then(result => {
        let res = result.data;
        if (res.status !== "false") {
          this.setState({
            activated: true
          });
        }
      });
  }

  componentDidMount() {
    this.activateAccount();
  }

  navigateToLogin() {
    this.props.history.push("/login");
  }

  render() {
    const { classes } = this.props;

    return (
      <div>
        <HomeHeader />
        <center>
          <Box bgcolor="primary.main" className={classes.boxRoot}>
            <div className={classes.root}>
              <Avatar>
                <LockOutlinedIcon />
              </Avatar>
              <br />
              {this.state.activated ? (
                <div>
                  <Typography component="h1" variant="h5">
                    Your account have been activated.
                  </Typography>
                  <br />
                  <Typography component="h1" variant="h5">
                    Enjoy ROSTEM!
                  </Typography>
                </div>
              ) : (
                <Typography component="h1" variant="h5">
                  Invalid activation key.
                </Typography>
              )}
              <br />
              <Divider />
              <br />

              <div className={classes.actionButtons}>
                <Button
                  type="submit"
                  variant="contained"
                  color="secondary"
                  onClick={this.navigateToLogin.bind(this)}
                >
                  Log in
                </Button>
              </div>
            </div>
          </Box>
        </center>
      </div>
    );
  }
}

export default withStyles(styles)(ActivateAccountView);
