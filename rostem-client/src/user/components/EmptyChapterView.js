import React from "react";
import Paper from "@material-ui/core/Paper";
import { withStyles } from "@material-ui/core/styles";

const styles = theme => ({
  root: {
    width: 1050,
    height: 500,
    align: "center"
  }
});

class EmptyChapterView extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    const { classes } = this.props;
    return <Paper className={classes.root}>EMPTYYY</Paper>;
  }
}

export default withStyles(styles)(EmptyChapterView);
