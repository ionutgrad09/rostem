import React from "react";
import Paper from "@material-ui/core/Paper";
import { withStyles } from "@material-ui/core/styles";
import { Typography } from "@material-ui/core";

const styles = theme => ({
  root: {
    width: 1050,
    height: 500
  },
  title: {
    align: "center"
  }
});

class ChapterView extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    const { classes } = this.props;
    return (
      <Paper className={classes.root}>
        <center>
          <Typography variant="h2">{this.props.name}</Typography>
          {this.props.url ? (
            <iframe
              width="500"
              height="400"
              src={this.props.url}
              frameBorder="0"
              allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
              allowFullScreen
            />
          ) : null}
        </center>
      </Paper>
    );
  }
}

export default withStyles(styles)(ChapterView);
