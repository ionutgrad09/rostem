import React from "react";
import { withStyles } from "@material-ui/core/styles";
import { Typography } from "@material-ui/core";
import Box from "@material-ui/core/Box";
import Divider from "@material-ui/core/Divider";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";
import axios from "axios";
import * as rostemConstants from "../../constants/constants.js";

const styles = theme => ({
  boxRoot: {
    width: 910,
    height: "auto"
  },
  root: {
    padding: 25
  },
  title: {
    align: "center"
  }
});

class ChapterView extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      checkedTODO: this.props.chapter.todo,
      checkedDONE: this.props.chapter.done
    };
  }

  async setActionTutorial(action) {
    const email = sessionStorage.getItem(rostemConstants.EMAIL);
    const actionType = action;
    const chapterId = this.props.chapter.id;

    await axios
      .post(rostemConstants.BASE_URL + "/chapters/mark", {
        email: email,
        chapterId: chapterId,
        actionType: actionType
      })
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error marking chapters");
        }
      });
  }

  unsetTodo() {
    if (this.state.checkedTODO === true) {
      this.unsetActionTutorial("TODO");
      this.setState({
        checkedTODO: false
      });
    }
  }

  unsetDone() {
    if (this.state.checkedDONE === true) {
      this.unsetActionTutorial("DONE");
      this.setState({
        checkedDONE: false
      });
    }
  }

  async unsetActionTutorial(action) {
    const email = sessionStorage.getItem(rostemConstants.EMAIL);
    const actionType = action;
    const chapterId = this.props.chapter.id;

    await axios
      .post(rostemConstants.BASE_URL + "/chapters/unmark", {
        email: email,
        chapterId: chapterId,
        actionType: actionType
      })
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error unmarking chapters");
        }
      });
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      checkedTODO: nextProps.chapter.todo,
      checkedDONE: nextProps.chapter.done
    });
  }

  handleChangeTODO = () => {
    this.unsetDone();
    if (this.state.checkedTODO === true) {
      this.unsetActionTutorial("TODO");
    } else {
      this.setActionTutorial("TODO");
    }
    this.setState({
      checkedTODO: !this.state.checkedTODO
    });
  };

  handleChangeDONE = () => {
    this.unsetTodo();
    if (this.state.checkedDONE === true) {
      this.unsetActionTutorial("DONE");
    } else {
      this.setActionTutorial("DONE");
    }
    this.setState({
      checkedDONE: !this.state.checkedDONE
    });
  };

  render() {
    const { classes } = this.props;
    return (
      <Box bgcolor="primary.main" className={classes.boxRoot}>
        <div className={classes.root}>
          <center>
            <Typography variant="h4">{this.props.chapter.name}</Typography>
            <br />
            <Divider />
            <br />
          </center>
          <Typography variant="h5">Description: </Typography> <br />
          <Typography variant="h6">{this.props.chapter.description}</Typography>
          <br />
          <center>
            {this.props.chapter.sourceUrl ? (
              <iframe
                width="650"
                height="400"
                src={this.props.chapter.sourceUrl}
                frameBorder="0"
                allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                allowFullScreen
              />
            ) : null}
          </center>
          <br />
          <FormControlLabel
            control={
              <Checkbox
                checked={this.state.checkedTODO}
                onChange={this.handleChangeTODO}
                color="secondary"
              />
            }
            label="To-do"
          />
          <br />
          <FormControlLabel
            control={
              <Checkbox
                checked={this.state.checkedDONE}
                onChange={this.handleChangeDONE}
                color="secondary"
              />
            }
            label="Done"
          />
          <Divider />
        </div>
      </Box>
    );
  }
}

export default withStyles(styles)(ChapterView);
