import React from "react";
import { withStyles } from "@material-ui/core/styles";
import { Typography } from "@material-ui/core";
import Divider from "@material-ui/core/Divider";
import * as constants from "../../constants/constants.js";
import Box from "@material-ui/core/Box";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import WhatshotIcon from "@material-ui/icons/Whatshot";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import Tooltip from "@material-ui/core/Tooltip";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import Paper from "@material-ui/core/Paper";
import CircularProgress from "@material-ui/core/CircularProgress";

const styles = theme => ({
  boxRoot: {
    marginTop: 25,
    marginBottom: 25,
    width: 700,
    height: "auto"
  },
  badgesClass: {
    maxHeight: 200,
    height: "auto"
  },
  root: {
    padding: 25
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
  },
  details: {
    display: "inline-flex"
  },
  tabsStyle: {
    flexGrow: 1,
    justifyContent: "space-evenly"
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
      creationDate: null,
      badges: [],
      tabValue: 0,
      showCircularBar: false
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

  handleTabChange = (event, value) => {
    console.log(value);
    this.setState({ tabValue: value });
  };

  async getUserDetails() {
    const email = this.props.userEmail;
    await constants.axiosRequest
      .get(constants.BASE_URL + "/users/details/" + email)
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting done chapters");
        } else {
          this.setState({
            email: res.object.email,
            username: res.object.username,
            bio: res.object.bio,
            showCircularBar: false,
            creationDate: res.object.registrationDate.substr(
              0,
              res.object.registrationDate.indexOf(".")
            ),
            badges: res.object.badges
          });
        }
      });
  }

  componentDidMount() {
    this.getUserDetails();
  }

  async updateDetails() {
    this.setState({ showCircularBar: true });
    await constants.axiosRequest
      .put(constants.BASE_URL + "/users/update", {
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
            <Paper square className={classes.tabsStyle}>
              <Tabs
                value={this.state.tabValue}
                onChange={this.handleTabChange}
                indicatorColor="secondary"
                textColor="secondary"
                centered
                variant="fullWidth"
              >
                <Tab label="Profile" />
                <Tab label="Badges" />
              </Tabs>
            </Paper>
            <div>
              {this.state.tabValue === 0 && (
                <div className={classes.root}>
                  <br />
                  <Typography component="h1" variant="h5">
                    Profile
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
                  {this.state.showCircularBar && (
                    <CircularProgress
                      className={classes.progress}
                      color="secondary"
                    />
                  )}
                </div>
              )}{" "}
              {this.state.tabValue === 1 && (
                <div>
                  <br />
                  <div className={classes.root}>
                    <Tooltip title="You can earn badges by completing a tutorial 100%.">
                      <Typography variant="h5">Badges</Typography>
                    </Tooltip>
                    <br />
                    <Divider />
                    <br />
                    {this.state.badges.length > 0 ? (
                      <div>
                        <div>
                          <List dense={false} disablePadding>
                            {this.state.badges.map(badge => (
                              <ListItem className={classes.details}>
                                <ListItemIcon>
                                  <WhatshotIcon />
                                </ListItemIcon>
                                <ListItemText primary={badge} />
                              </ListItem>
                            ))}
                          </List>
                        </div>
                      </div>
                    ) : (
                      <Typography variant="h5">
                        You didn't earn any badge yet!
                      </Typography>
                    )}
                  </div>
                </div>
              )}
            </div>
          </Box>
        </center>
      </div>
    );
  }
}

export default withStyles(styles)(UserAccount);
