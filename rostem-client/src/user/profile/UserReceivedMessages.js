import React from "react";
import { withStyles } from "@material-ui/core/styles";
import { Typography } from "@material-ui/core";
import Divider from "@material-ui/core/Divider";
import ListItemText from "@material-ui/core/ListItemText";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import * as constants from "../../constants/constants.js";
import Box from "@material-ui/core/Box";
import SendIcon from "@material-ui/icons/Send";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import IconButton from "@material-ui/core/IconButton";
import Tooltip from "@material-ui/core/Tooltip";

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
  }
});

class UserReceivedMessages extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      receivedMessages: []
    };
  }

  async getReceivedMessages() {
    const email = this.props.userEmail;
    await constants.axiosRequest
      .get(constants.BASE_URL + "/messages/received/" + email)
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting received messages");
        } else {
          this.setState({
            receivedMessages: res.object.objects
          });
        }
      });
  }

  componentDidMount() {
    this.getReceivedMessages();
  }

  render() {
    const { classes } = this.props;
    return (
      <Box bgcolor="primary.main" className={classes.root}>
        <center>
          <Typography variant="h6">RECEIVED</Typography>
        </center>
        <Divider />
        <div>
          <List dense={false}>
            {this.state.receivedMessages.length > 0 ? (
              this.state.receivedMessages.map(message => {
                return (
                  <ListItem className={classes.details} divider>
                    <ListItemIcon>
                      <SendIcon />
                    </ListItemIcon>
                    <ListItemText
                      primary={message.message}
                      secondary={
                        "From " +
                        message.sentBy +
                        " at " +
                        message.creationDate.substr(
                          0,
                          message.creationDate.indexOf(".")
                        )
                      }
                    />
                  </ListItem>
                );
              })
            ) : (
              <div className={classes.empty}>
                <center>
                  <Typography variant="h4">
                    No received messages yet!
                  </Typography>
                </center>
              </div>
            )}
          </List>
        </div>
      </Box>
    );
  }
}

export default withStyles(styles)(UserReceivedMessages);
