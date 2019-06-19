import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Box from "@material-ui/core/Box";
import UserSentMessages from "./UserSentMessages";
import UserReceivedMessages from "./UserReceivedMessages";
import SimpleSendMessage from "./SimpleSendMessage";

const styles = theme => ({
  root: {
    padding: 15
  },
  wrapperBox: {
    display: "flex",
    flexWrap: "wrap",
    justifyContent: "space-evenly",
    overflow: "hidden",
    listStyle: "none"
  }
});

class UserMessages extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { classes } = this.props;
    return (
      <div className={classes.root}>
        <Box className={classes.wrapperBox} p={1} m={1}>
          <UserSentMessages userEmail={this.props.userEmail} />
          <UserReceivedMessages userEmail={this.props.userEmail} />
          <SimpleSendMessage userEmail={this.props.userEmail} />
        </Box>
      </div>
    );
  }
}

UserMessages.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(styles)(UserMessages);
