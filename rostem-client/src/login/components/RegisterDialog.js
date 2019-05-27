import React from "react";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import * as AuthenticationActions from "../actions/AuthenticatonActions";

export default class RegisterDialog extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
      errors: [],
      email: "",
      password: "",
      confirmPassword: "",
      username: "",
      showPassword: false
    };
  }

  handleClickShowPassword = () => {
    this.setState(state => ({ showPassword: !state.showPassword }));
  };

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
    //TODO show message
  }

  submitRegister(e) {
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
      this.showValidationError("confirmPassword", "Passwords does not match!");
    }

    AuthenticationActions.register(
      this.state.email,
      this.state.username,
      this.state.password,
      this.state.confirmPassword
    );
  }

  handleClose = () => {
    this.setState({ open: false });
  };

  handleOpen = () => {
    this.setState({ open: true });
  };

  render() {
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
        <Button variant="outlined" color="primary" onClick={this.handleOpen}>
          GET STARTED
        </Button>
        <Dialog open={this.state.open} onClose={this.handleClose}>
          <DialogTitle variant="h2">Register</DialogTitle>
          <DialogContent>
            <TextField
              id="custom-css-standard-input"
              label="Email"
              type="string"
              fullWidth
              required
              onChange={this.onEmailChange.bind(this)}
            />
            <small className="danger-error">
              {emailError ? emailError : ""}
            </small>

            <TextField
              id="custom-css-standard-input"
              label="Username"
              type="string"
              fullWidth
              required
              onChange={this.onUsernameChange.bind(this)}
            />
            <small className="danger-error">
              {usernameError ? usernameError : ""}
            </small>

            <TextField
              id="standard-password-input"
              label="Password"
              type={this.state.showPassword ? "text" : "password"}
              margin="normal"
              fullWidth
              required
              onChange={this.onPasswordChange.bind(this)}
            />
            <small className="danger-error">
              {passwordError ? passwordError : ""}
            </small>

            <TextField
              id="standard-password-input"
              label="Confirm password"
              type={this.state.showPassword ? "text" : "password"}
              margin="normal"
              fullWidth
              required
              onChange={this.onConfirmPasswordChange.bind(this)}
            />
            <small className="danger-error">
              {confirmPasswordError ? confirmPasswordError : ""}
            </small>
          </DialogContent>
          <DialogActions>
            <Button onClick={this.handleClose.bind(this)} color="secondary">
              Cancel
            </Button>
            <Button onClick={this.submitRegister.bind(this)} color="secondary">
              Register
            </Button>
          </DialogActions>
        </Dialog>
      </div>
    );
  }
}
