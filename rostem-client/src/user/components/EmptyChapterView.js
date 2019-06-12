import React from "react";
import Paper from "@material-ui/core/Paper";
import { withStyles } from "@material-ui/core/styles";
import { Typography } from "@material-ui/core";

const styles = theme => ({
  root: {
    width: 910,
    height: 500,
    align: "center"
  },
  typo: {
    marginTop: 200
  }
});

class EmptyChapterView extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    const { classes } = this.props;
    return (
      <Paper className={classes.root}>
        <center>
          <Typography className={classes.typo} variant="h3">
            <i>Select a chapter... </i>
          </Typography>
        </center>
      </Paper>
    );
  }
}

export default withStyles(styles)(EmptyChapterView);
