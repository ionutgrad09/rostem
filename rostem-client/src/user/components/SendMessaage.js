import React from "react";
import { withStyles } from "@material-ui/core/styles";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import * as constants from "../../constants/constants.js";
import { Typography } from "@material-ui/core";
import Divider from "@material-ui/core/Divider";
import Box from "@material-ui/core/Box";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import DraftsIcon from "@material-ui/icons/Drafts";
import IconButton from "@material-ui/core/IconButton";
import Tooltip from "@material-ui/core/Tooltip";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import WhatshotIcon from "@material-ui/icons/Whatshot";

const styles = theme => ({
  boxRoot: {
    marginTop: 25,
    marginBottom: 25,
    width: 500,
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
    width: 450
  },
  actionButtons: {
    marginTop: 25
  },
  dangerError: {
    color: "red"
  }
});

class SendMessage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
      user: "",
      message: "",
      showMessage: false,
      existUser: false
    };
  }

  async getUserDetails() {
    await constants.axiosRequest
      .get(constants.BASE_URL + "/users/details/" + this.props.friendEmail)
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting user details.");
        } else {
          console.log(res.user);
          this.setState({
            existUser: true,
            user: res.object
          });
        }
      });
  }

  handleClose = () => {
    this.setState({ open: false });
  };

  handleOpen = () => {
    if (this.props.friendEmail !== this.props.userEmail) {
      this.setState({ open: true });
    }
  };

  componentDidMount() {
    this.getUserDetails();
  }

  onMessageChange(e) {
    this.setState({
      showMessage: false,
      message: e.target.value
    });
  }

  async onSendMessage() {
    if (this.state.message !== "") {
      const body = {
        message: this.state.message,
        sentBy: this.props.userEmail,
        receivedBy: this.props.friendEmail
      };
      await constants.axiosRequest
        .post(constants.BASE_URL + "/messages/send", body)
        .then(result => {
          let res = result.data;
          if (res.status === "false") {
            console.log("Error getting user details.");
          } else {
            this.setState({
              message: "",
              showMessage: true
            });
          }
        });
    }
  }

  isTheSameUser() {
    if (this.props.friendEmail === this.props.userEmail) {
      return true;
    }
    return false;
  }

  render() {
    const { classes } = this.props;
    return (
      <div>
        <Tooltip
          title={
            this.isTheSameUser()
              ? "You can't message yourself!"
              : "Message " + this.props.username
          }
        >
          <IconButton aria-label="Message">
            <DraftsIcon onClick={this.handleOpen} />
          </IconButton>
        </Tooltip>
        <Dialog open={this.state.open} onClose={this.handleClose}>
          <DialogContent>
            <div>
              <center>
                <Box bgcolor="primary.main" className={classes.boxRoot}>
                  <div className={classes.root}>
                    <br />
                    <Typography component="h1" variant="h5">
                      {this.state.user.username}
                    </Typography>
                    <br />
                    <Divider />
                    <br />
                    <TextField
                      disabled
                      id="standard-email-input"
                      label="Email"
                      value={this.state.user.email}
                      className={classes.textField}
                      type="email"
                      margin="normal"
                    />

                    <TextField
                      id="standard-textarea"
                      label="Bio"
                      value={this.state.user.bio}
                      multiline
                      className={classes.textField}
                      margin="normal"
                      disabled
                    />
                    <br />
                    <TextField
                      id="standard-textarea"
                      label="Send a message"
                      multiline
                      className={classes.textField}
                      margin="normal"
                      value={this.state.message}
                      onChange={this.onMessageChange.bind(this)}
                    />
                    <div className={classes.actionButtons}>
                      <Button
                        type="submit"
                        variant="contained"
                        color="secondary"
                        onClick={this.onSendMessage.bind(this)}
                      >
                        Send message
                      </Button>
                      <br />
                      <br />
                      <Divider />
                      {this.state.showMessage && (
                        <Typography variant="h6">
                          Message sent successfully!
                        </Typography>
                      )}
                    </div>

                    <div className={classes.root}>
                      <br />
                      {this.state.existUser &&
                      this.state.user.badges.length > 0 ? (
                        <div>
                          <Tooltip title="Badges are earn when you complete a tutorial 100%.">
                            <Typography variant="h5">Badges</Typography>
                          </Tooltip>
                          <br />
                          <Divider />
                          <br />
                          <div>
                            <List dense={false} disablePadding>
                              {this.state.user.badges.map(badge => (
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
                          {this.state.user.username} didn't earn any badge yet!
                        </Typography>
                      )}
                    </div>
                  </div>
                </Box>
              </center>
            </div>
          </DialogContent>
        </Dialog>
      </div>
    );
  }
}

export default withStyles(styles)(SendMessage);
