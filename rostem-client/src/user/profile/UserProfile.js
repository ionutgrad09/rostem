import React from "react";
import Paper from "@material-ui/core/Paper";
import { withStyles } from "@material-ui/core/styles";
import { Typography } from "@material-ui/core";
import { withRouter } from "react-router-dom";

const styles = theme => ({
  root: {
    width: 910,
    height: 200,
    align: "center"
  },
  typo: {
    marginTop: 50
  }
});

class UserProfile extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    const { classes } = this.props;
    return (
      <Paper className={classes.root}>
        <center>
          <Typography className={classes.typo} variant="h3">
            <i>HELLO TO YOUR PROFILE </i>
          </Typography>
        </center>
      </Paper>
    );
  }
}

export default withRouter(withStyles(styles)(UserProfile));
