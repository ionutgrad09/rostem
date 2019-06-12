import React from "react";
import { withStyles } from "@material-ui/core/styles";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import axios from "axios";
import * as rostemConstants from "../../constants/constants.js";
import Avatar from "@material-ui/core/Avatar";
import { Typography } from "@material-ui/core";
import Divider from "@material-ui/core/Divider";
import Box from "@material-ui/core/Box";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Fab from "@material-ui/core/Fab";
import AddIcon from "@material-ui/icons/Add";

const stemAvatar = require("../../resources/blueStem.png");

const styles = theme => ({
  title: {
    align: "center"
  },
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
  },
  speedDial: {
    position: "absolute",
    bottom: theme.spacing(2),
    right: theme.spacing(3)
  }
});

class SimpleSendMessage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
      to: "",
      message: "",
      showMessage: false
    };
  }

  handleClose = () => {
    this.setState({ open: false });
  };

  handleOpen = () => {
    this.setState({ open: true });
  };

  onMessageChange(e) {
    this.setState({
      showMessage: false,
      message: e.target.value
    });
  }

  onToChange(e) {
    this.setState({
      showMessage: false,
      to: e.target.value
    });
  }

  async onSendMessage() {
    if (this.state.message !== "") {
      const email = JSON.parse(sessionStorage.getItem(rostemConstants.USER))
        .email;
      const body = {
        message: this.state.message,
        sentBy: email,
        receivedBy: this.state.to
      };
      await axios
        .post(rostemConstants.BASE_URL + "/messages/send", body)
        .then(result => {
          let res = result.data;
          if (res.status === "false") {
            console.log("Error sending message.");
          } else {
            this.setState({
              message: "",
              showMessage: true,
              to: ""
            });
          }
        });
    }
  }

  render() {
    const { classes } = this.props;
    return (
      <div className={classes.speedDial}>
        <Fab
          color="primary"
          aria-label="Sent new message"
          onClick={this.handleOpen}
        >
          <AddIcon />
        </Fab>

        <Dialog open={this.state.open} onClose={this.handleClose}>
          <DialogContent>
            <div>
              <center>
                <Box bgcolor="primary.main" className={classes.boxRoot}>
                  <div className={classes.root}>
                    <br />
                    <Typography component="h1" variant="h5">
                      Send a new message
                    </Typography>
                    <br />
                    <Divider />
                    <br />
                    <br />
                    <TextField
                      id="standard-textarea"
                      label="To"
                      className={classes.textField}
                      margin="normal"
                      value={this.state.to}
                      onChange={this.onToChange.bind(this)}
                    />

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

export default withStyles(styles)(SimpleSendMessage);
