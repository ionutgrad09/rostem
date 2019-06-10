import React from "react";
import { Typography, withStyles } from "@material-ui/core";

const styles = {
  footer: {
    padding: 15,
    backgroundColor: "secondary"
  }
};

class Footer extends React.Component {
  render() {
    const { classes } = this.props;
    return (
      <footer className={classes.footer}>
        <Typography
          variant="subtitle1"
          align="center"
          color="primary"
          component="p"
        >
          @ROSTEM
        </Typography>
      </footer>
    );
  }
}

export default withStyles(styles)(Footer);
