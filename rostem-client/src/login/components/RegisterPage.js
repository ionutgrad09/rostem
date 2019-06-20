import React from "react";
import { withStyles } from "@material-ui/core/styles";
import { Typography } from "@material-ui/core";
import Divider from "@material-ui/core/Divider";
import axios from "axios";
import * as rostemConstants from "../../constants/constants.js";
import Box from "@material-ui/core/Box";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Link from "@material-ui/core/Link";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import HomeHeader from "../../commons/components/HomeHeader";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";

const styles = theme => ({
  boxRoot: {
    marginTop: 25,
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

class RegisterPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      errors: [],
      email: "",
      password: "",
      confirmPassword: "",
      username: "",
      bio: ""
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

  onConfirmPasswordChange(e) {
    this.setState({ confirmPassword: e.target.value });
    this.clearValidationError("confirmPassword");
  }

  onUsernameChange(e) {
    this.setState({ username: e.target.value });
    this.clearValidationError("username");
  }

  onBioChange(e) {
    this.setState({ bio: e.target.value });
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

  success() {
    this.setState({ open: true });
  }

  register(email, username, password, bio) {
    const transport = axios.create({
      withCredentials: true
    });
    transport
      .post(rostemConstants.BASE_URL + "/register", {
        email,
        password,
        username,
        bio
      })
      .then(res => {
        this.success();
      });
  }

  existsError() {
    return (
      this.state.username === "" ||
      this.state.email === "" ||
      this.state.password === "" ||
      this.state.confirmPassword !== this.state.password
    );
  }

  submitRegister(e) {
    if (this.existsError()) {
      if (this.state.username === "") {
        this.showValidationError("username", "Username can't be empty!");
      }
      if (this.state.email === "") {
        this.showValidationError("email", "Email can't be empty!");
      }
      if (this.state.password === "") {
        this.showValidationError("password", "Password can't be empty!");
      }
      if (this.state.confirmPassword !== this.state.password) {
        this.showValidationError(
          "confirmPassword",
          "Passwords does not match!"
        );
      }
    } else {
      this.register(
        this.state.email,
        this.state.username,
        this.state.password,
        this.state.bio
      );
    }
  }

  handleClose = () => {
    this.setState({ open: false });
  };

  navigateToLogin() {
    this.props.history.push("/login");
  }

  render() {
    const { classes } = this.props;
    let usernameError = null,
      passwordError = null,
      confirmPasswordError = null,
      emailError = null;

    for (let err of this.state.errors) {
      if (err.element === "email") {
        emailError = err.message;
      }
      if (err.element === "password") {
        passwordError = err.message;
      }
      if (err.element === "username") {
        usernameError = err.message;
      }
      if (err.element === "confirmPassword") {
        confirmPasswordError = err.message;
      }
    }
    return (
      <div>
        <HomeHeader />
        <Dialog open={this.state.open} onClose={this.handleClose}>
          <DialogContent>
            <center>
              <Box bgcolor="primary.main" className={classes.boxRoot}>
                <div className={classes.root}>
                  <Avatar>
                    <LockOutlinedIcon />
                  </Avatar>
                  <br />
                  <Typography component="h1" variant="h5">
                    Successfully completed registration
                  </Typography>
                  <br />
                  <Typography component="h1" variant="h5">
                    You need to activate the account using your email address.
                  </Typography>

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
          </DialogContent>
        </Dialog>
        <center>
          <Box bgcolor="primary.main" className={classes.boxRoot}>
            <div className={classes.root}>
              <Avatar>
                <LockOutlinedIcon />
              </Avatar>
              <br />
              <Typography component="h1" variant="h5">
                Register
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

              <TextField
                id="standard-password-input"
                label="Confirm password"
                className={classes.textField}
                type="password"
                margin="normal"
                required="true"
                onChange={this.onConfirmPasswordChange.bind(this)}
                placeholder={confirmPasswordError ? confirmPasswordError : ""}
                error={confirmPasswordError ? true : false}
              />

              <TextField
                id="standard-username-input"
                label="Username"
                className={classes.textField}
                type="text"
                margin="normal"
                required="true"
                onChange={this.onUsernameChange.bind(this)}
                placeholder={usernameError ? usernameError : ""}
                error={usernameError ? true : false}
              />

              <TextField
                id="standard-textarea"
                label="Bio"
                multiline
                className={classes.textField}
                margin="normal"
              />

              <div className={classes.actionButtons}>
                <Button
                  type="submit"
                  variant="contained"
                  color="secondary"
                  onClick={this.submitRegister.bind(this)}
                >
                  Register
                </Button>
              </div>
              <br />
              <Link color="secondary" href="/login" variant="body2">
                Already got an account? Log In.
              </Link>
            </div>
          </Box>
        </center>
      </div>
    );
  }
}

export default withStyles(styles)(RegisterPage);
