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

class LoginPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      errors: [],
      email: "",
      password: "",
      showError: false
    };
  }

  onEmailChange(e) {
    this.setState({ email: e.target.value });
    this.clearValidationError("email");
  }

  onPasswordChange(e) {
    this.setState({ password: e.target.value });
    this.clearValidationError("password");
  }

  showValidationError(element, message) {
    this.setState(prevState => ({
      errors: [...prevState.errors, { element, message }]
    }));
  }

  clearValidationError(element) {
    this.setState(prevState => {
      let newArr = [];
      for (let err of prevState.errors) {
        if (element !== err.element) {
          newArr.push(err);
        }
      }
      return { errors: newArr };
    });
  }

  existsError() {
    return this.state.email === "" || this.state.password === "";
  }

  handleAdmin() {
    this.props.history.replace("/admin");
  }

  handleUser() {
    this.props.history.replace("/categories");
  }

  handleLoginResponse(response) {
    if (response.status === false) {
      console.log("error");
    } else {
      if (response.data.exception) {
        this.setState({ showError: true });
      } else {
        if (response.data.object.role === "ROLE_ADMIN") {
          this.handleAdmin();
        } else if (response.data.object.role === "ROLE_USER") {
          this.handleUser();
        } else {
          console.log("cant redirect");
        }
      }
    }
  }

  login(username, password) {
    const headers = {
      "Content-Type": "application/x-www-form-urlencoded"
    };
    const body = "username=" + username + "&password=" + password;
    constants.axiosRequest
      .post(constants.BASE_URL + "/login?" + body, { headers: headers })
      .then(response => {
        this.handleLoginResponse(response);
      });
  }

  submitLogin(e) {
    if (this.existsError()) {
      if (this.state.email === "") {
        this.showValidationError("email", "Email can't be empty!");
      }
      if (this.state.password === "") {
        this.showValidationError("password", "Password can't be empty!");
      }
    } else {
      this.login(this.state.email, this.state.password);
    }
  }

  render() {
    const { classes } = this.props;
    let passwordError = null,
      emailError = null;

    for (let err of this.state.errors) {
      if (err.element === "email") {
        emailError = err.message;
      }
      if (err.element === "password") {
        passwordError = err.message;
      }
    }
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
              <Typography component="h1" variant="h5">
                Log In
              </Typography>
              <br />
              <Divider />
              <br />
              <TextField
                id="standard-email-input"
                label="Email"
                className={classes.textField}
                type="email"
                margin="normal"
                required="true"
                onChange={this.onEmailChange.bind(this)}
                placeholder={emailError ? emailError : ""}
                error={emailError ? true : false}
              />

              <TextField
                id="standard-password-input"
                label="Password"
                className={classes.textField}
                type="password"
                margin="normal"
                required="true"
                onChange={this.onPasswordChange.bind(this)}
                placeholder={passwordError ? passwordError : ""}
                error={passwordError ? true : false}
              />
              <br />
              {this.state.showError && (
                <p style={{ color: "red" }}>Bad credentials!</p>
              )}
              <br />

              <div className={classes.actionButtons}>
                <Button
                  type="submit"
                  variant="contained"
                  color="secondary"
                  onClick={this.submitLogin.bind(this)}
                >
                  Log In
                </Button>
                <Grid container justify="flex-end">
                  <Grid item>
                    <Link href="#" variant="body2">
                      Already have an account? Sign in
                    </Link>
                  </Grid>
                </Grid>
              </div>
              <Link color="secondary" href="/register" variant="body2">
                Don't have an account yet? Register.
              </Link>
            </div>
          </Box>
        </center>
      </div>
    );
  }
}

export default withStyles(styles)(LoginPage);
