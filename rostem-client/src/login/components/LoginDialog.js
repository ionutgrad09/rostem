import React from "react";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import * as AuthenticationActions from "../actions/AuthenticatonActions";
import { withRouter } from "react-router-dom";

class LoginDialog extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
      errors: [],
      email: "",
      password: "",
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
    this.props.history.push("/admin");
  }

  handleLoginResponse(response) {
    if (response.status === false) {
      console.log("error");
    } else {
      if (response.data.exception) {
        console.log("Error: " + response.data.exception);
      } else {
        if ((response.data.role = "ROLE_ADMIN")) {
          this.success();
        } else {
          console.log("USER");
        }
      }
    }
  }

  submitLogin(e) {
    let doRequest = true;
    if (this.state.email === "") {
      doRequest = false;
      this.showValidationError("email", "Email can't be empty!");
    }
    if (this.state.password === "") {
      doRequest = false;
      this.showValidationError("password", "Password can't be empty!");
    }

    if (doRequest) {
      AuthenticationActions.login(this.state.email, this.state.password).then(
        response => this.handleLoginResponse(response)
      );
    }
  }

  handleClose = () => {
    this.setState({ open: false });
  };

  handleOpen = () => {
    this.setState({ open: true });
  };

  render() {
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
        <Button color="primary" onClick={this.handleOpen}>
          Log in
        </Button>
        <Dialog open={this.state.open} onClose={this.handleClose}>
          <DialogTitle variant="h2">Log in</DialogTitle>
          <DialogContent>
            <TextField
              id="custom-css-standard-input"
              label="Email"
              type="email"
              fullWidth
              required
              onChange={this.onEmailChange.bind(this)}
            />
            <small className="danger-error">
              {emailError ? emailError : ""}
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
          </DialogContent>
          <DialogActions>
            <Button onClick={this.handleClose.bind(this)} color="secondary">
              Cancel
            </Button>
            <Button onClick={this.submitLogin.bind(this)} color="secondary">
              Log in
            </Button>
          </DialogActions>
        </Dialog>
      </div>
    );
  }
}

export default withRouter(LoginDialog);
