import React from "react";
import { withStyles } from "@material-ui/core/styles";
import { Typography } from "@material-ui/core";
import Divider from "@material-ui/core/Divider";
import axios from "axios";
import * as rostemConstants from "../../constants/constants.js";
import Box from "@material-ui/core/Box";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";

const styles = theme => ({
  boxRoot: {
    marginTop: 25,
    marginBottom: 25,
    width: 700,
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
    width: 650
  },
  actionButtons: {
    marginTop: 25
  },
  dangerError: {
    color: "red"
  }
});

class UserAccount extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      errors: [],
      email: "",
      username: "",
      bio: "",
      photo: null,
      creationDate: null
    };
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

  async getUserDetails() {
    const email = JSON.parse(sessionStorage.getItem(rostemConstants.USER))
      .email;
    await axios
      .get(rostemConstants.BASE_URL + "/users/details/" + email)
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting done chapters");
        } else {
          this.setState({
            email: res.object.email,
            username: res.object.username,
            bio: res.object.bio,
            photo: res.object.photo,
            creationDate: res.object.registrationDate.substr(
              0,
              res.object.registrationDate.indexOf(".")
            )
          });
        }
      });
  }

  componentDidMount() {
    this.getUserDetails();
  }

  async updateDetails() {
    const email = JSON.parse(sessionStorage.getItem(rostemConstants.USER))
      .email;
    const data = new FormData();
    await axios
      .put(rostemConstants.BASE_URL + "/users/update", {
        email: this.state.email,
        newUsername: this.state.username,
        newBio: this.state.bio
      })
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting done chapters");
        } else {
          this.getUserDetails();
        }
      });
  }

  onUpdateClick(e) {
    if (this.state.username === "") {
      this.showValidationError("username", "Username can't be empty!");
    } else {
      this.updateDetails();
    }
  }

  onUpdatePhoto = event => {
    // const blobFile = new Blob(event.target.files[0]);
    // const arrayFile = FileReader.readAsArrayBuffer(blobFile);
    // console.log(arrayFile);
    // this.setState({
    //   photo: arrayFile
    // });
  };

  render() {
    const { classes } = this.props;
    let usernameError = null;

    for (let err of this.state.errors) {
      if (err.element === "username") {
        usernameError = err.message;
      }
    }
    return (
      <div>
        <center>
          <Box bgcolor="primary.main" className={classes.boxRoot}>
            <div className={classes.root}>
              <br />
              <Typography component="h1" variant="h5">
                My Profile
              </Typography>
              <br />
              <Divider />
              <br />

              <TextField
                id="standard-username-input"
                label="Username"
                className={classes.textField}
                type="text"
                margin="normal"
                value={this.state.username}
                onChange={this.onUsernameChange.bind(this)}
                placeholder={usernameError ? usernameError : ""}
                error={usernameError ? true : false}
              />

              <TextField
                id="standard-textarea"
                label="Bio"
                value={this.state.bio}
                multiline
                className={classes.textField}
                margin="normal"
                onChange={this.onBioChange.bind(this)}
              />

              <TextField
                disabled
                id="standard-email-input"
                label="Email"
                value={this.state.email}
                className={classes.textField}
                type="email"
                margin="normal"
              />

              <TextField
                disabled
                id="standard-textarea"
                label="Registration date"
                value={this.state.creationDate}
                className={classes.textField}
                margin="normal"
              />

              <div className={classes.actionButtons}>
                <Button
                  type="submit"
                  variant="contained"
                  color="secondary"
                  onClick={this.onUpdateClick.bind(this)}
                >
                  Save changes
                </Button>
              </div>
            </div>
          </Box>
        </center>
      </div>
    );
  }
}

export default withStyles(styles)(UserAccount);
