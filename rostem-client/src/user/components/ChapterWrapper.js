import React from "react";
import { withStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import ChapterView from "./ChapterView";

const styles = theme => ({
  root: {
    width: 910,
    height: 600
  },
  title: {
    align: "center"
  }
});

class RecentPosts extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
      latestChapters: []
    };
  }

  handleClose = () => {
    this.props.updatePosts();
    this.setState({ open: false });
  };

  handleOpen = () => {
    this.setState({ open: true });
  };

  render() {
    const { classes } = this.props;
    return (
      <div>
        <Button
          variant="contained"
          color="secondary"
          className={classes.button}
          onClick={this.handleOpen}
        >
          EXPLORE
        </Button>
        <Dialog
          fullWidth={true}
          fullHeight={true}
          open={this.state.open}
          onClose={this.handleClose}
          maxWidth={"md"}
          maxHeight={"md"}
        >
          <DialogContent>
            <ChapterView
              userEmail={this.props.userEmail}
              chapter={this.props.chapter}
            />
          </DialogContent>
        </Dialog>
      </div>
    );
  }
}

export default withStyles(styles)(RecentPosts);
