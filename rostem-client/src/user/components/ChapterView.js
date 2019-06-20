import React from "react";
import { withStyles } from "@material-ui/core/styles";
import { Typography } from "@material-ui/core";
import Box from "@material-ui/core/Box";
import Divider from "@material-ui/core/Divider";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";
import * as constants from "../../constants/constants.js";
import ThumbUp from "@material-ui/icons/ThumbUp";
import Fab from "@material-ui/core/Fab";
import Tooltip from "@material-ui/core/Tooltip";
import CommentsView from "./CommentsView.js";
import htmlToDraft from "html-to-draftjs";
import { ContentState } from "draft-js";
import ReactHtmlParser, {
  processNodes,
  convertNodeToElement,
  htmlparser2
} from "react-html-parser";

const styles = theme => ({
  boxRoot: {
    width: 1000,
    minWidth: 300,
    height: "auto"
  },
  root: {
    padding: 25
  },
  title: {
    align: "center"
  },
  description: {
    display: "inline-flex",
    alignItems: "flex-start"
  },
  content: {
    width: 900,
    wordWrap: "break-all"
  }
});

const isValidUrl = string => {
  try {
    new URL(string);
    return true;
  } catch (_) {
    return false;
  }
};

class ChapterView extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      checkedTODO: this.props.chapter.todo,
      checkedDONE: this.props.chapter.done,
      isLiked: this.props.chapter.liked,
      chapter: this.props.chapter
    };
  }

  async setActionTutorial(action) {
    const actionType = action;
    const chapterId = this.props.chapter.id;

    await constants.axiosRequest
      .post(constants.BASE_URL + "/chapters/mark", {
        email: this.props.userEmail,
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

  async unlikeChapter() {
    const email = this.props.userEmail;
    const chapterId = this.props.chapter.id;

    if (this.state.isLiked === true) {
      await constants.axiosRequest
        .post(constants.BASE_URL + "/chapters/dislike", {
          email: email,
          chapterId: chapterId
        })
        .then(result => {
          let res = result.data;
          if (res.status === "false") {
            console.log("Error marking chapter as disliked.");
          } else {
            this.setState({
              isLiked: !this.state.isLiked
            });
          }
        });
    } else {
      await constants.axiosRequest
        .post(constants.BASE_URL + "/chapters/like", {
          email: email,
          chapterId: chapterId
        })
        .then(result => {
          let res = result.data;
          if (res.status === "false") {
            console.log("Error marking chapter as liked.");
          } else {
            this.setState({
              isLiked: !this.state.isLiked
            });
          }
        });
    }
  }

  async unsetActionTutorial(action) {
    const email = this.props.userEmail;
    const actionType = action;
    const chapterId = this.props.chapter.id;

    await constants.axiosRequest
      .post(constants.BASE_URL + "/chapters/unmark", {
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
      checkedDONE: nextProps.chapter.done,
      isLiked: nextProps.chapter.done,
      chapter: nextProps.chapter
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
            <Typography variant="h5">{this.props.chapter.name}</Typography>
            <br />
            <Divider />
          </center>
          <br />

          <Typography className={classes.description} variant="h6">
            <center>
              <div className={classes.content}>
                {ReactHtmlParser(this.props.chapter.content)}
              </div>
            </center>
          </Typography>
          <br />

          <center>
            <br />
            <br />
            {!this.state.isLiked ? (
              <Tooltip title="Thumb UP" aria-label="Thumb UP">
                <Fab
                  color="primary"
                  className={classes.absolute}
                  onClick={this.unlikeChapter.bind(this)}
                >
                  <ThumbUp />
                </Fab>
              </Tooltip>
            ) : (
              <Tooltip
                title="Thanks for the vote"
                aria-label="Thanks for the vote"
              >
                <Fab
                  color="secondary"
                  className={classes.absolute}
                  onClick={this.unlikeChapter.bind(this)}
                >
                  <ThumbUp />
                </Fab>
              </Tooltip>
            )}
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
          <br />
          <CommentsView
            userEmail={this.props.userEmail}
            chapter={this.state.chapter}
          />
        </div>
      </Box>
    );
  }
}

export default withStyles(styles)(ChapterView);
