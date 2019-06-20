import React from "react";
import {withStyles} from "@material-ui/core/styles";
import {Typography} from "@material-ui/core";
import Divider from "@material-ui/core/Divider";
import ListItemText from "@material-ui/core/ListItemText";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import * as constants from "../../constants/constants.js";
import Box from "@material-ui/core/Box";
import SendIcon from "@material-ui/icons/Send";
import ListItemIcon from "@material-ui/core/ListItemIcon";

const styles = theme => ({
  root: {
    width: 600,
    height: 500,
    overflow: "auto"
  },
  title: {
    align: "center"
  },
  empty: {
    marginTop: 150
  },
  boxRoot: {
    marginTop: 25,
    marginBottom: 25,
    width: 500,
    height: "auto"
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

class UserSentMessages extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
      message: "",
      sentMessages: [],
      showMessage: false
    };
  }

  async getSentMessages() {
    const email = this.props.userEmail;
    await constants.axiosRequest
      .get(constants.BASE_URL + "/messages/sent/" + email)
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting sent messages");
        } else {
          this.setState({
            sentMessages: res.object.objects
          });
        }
      });
  }

  componentDidMount() {
    this.getSentMessages();
  }

  handleClose = () => {
    this.setState({ showMessage: false, message: "", open: false });
  };

  handleOpen = () => {
    this.setState({ open: true });
  };

  async onSendMessage(message) {
    if (this.state.message !== "") {
      const body = {
        message: this.state.message,
        sentBy: message.sentBy,
        receivedBy: message.receivedBy
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
            this.getSentMessages();
          }
        });
    }
  }

  onMessageChange(e) {
    this.setState({
      showMessage: false,
      message: e.target.value
    });
  }

  render() {
    const { classes } = this.props;
    return (
      <Box bgcolor="primary.main" className={classes.root}>
        <center>
          <Typography variant="h6">SENT</Typography>
        </center>
        <Divider />
        <div>
          <List dense={false}>
            {this.state.sentMessages.length > 0 ? (
              this.state.sentMessages.map(message => {
                return (
                  <div>
                    <ListItem className={classes.details} divider>
                      <ListItemIcon>
                        <SendIcon />
                      </ListItemIcon>
                      <ListItemText
                        primary={message.message}
                        secondary={
                          "To " +
                          message.receivedBy +
                          " at " +
                          message.creationDate.substr(
                            0,
                            message.creationDate.indexOf(".")
                          )
                        }
                      />
                    </ListItem>
                  </div>
                );
              })
            ) : (
              <div className={classes.empty}>
                <center>
                  <Typography variant="h4">No sent messages yet!</Typography>
                </center>
              </div>
            )}
          </List>
        </div>
      </Box>
    );
  }
}

export default withStyles(styles)(UserSentMessages);
