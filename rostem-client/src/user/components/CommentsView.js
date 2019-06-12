import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import { withRouter } from "react-router-dom";
import axios from "axios";
import * as rostemConstants from "../../constants/constants.js";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import ListItemAvatar from "@material-ui/core/ListItemAvatar";
import SendMessage from "./SendMessaage";

const styles = theme => ({
  details: {
    display: "inline-flex",
    alignItems: "flex-start"
  },
  root: {
    width: "100%",
    maxWidth: 880
  }
});

class CommentsView extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      comments: [],
      userComment: ""
    };
  }

  async getAllComments() {
    const chapterId = this.props.chapter.id;
    await axios
      .get(rostemConstants.BASE_URL + "/chapters/comments/" + chapterId)
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting comments");
        } else {
          this.setState({
            comments: res.object.objects
          });
          console.log(this.state.comments);
        }
      });
  }

  onCommentChange(e) {
    this.setState({ userComment: e.target.value });
  }

  async addComment() {
    const body = {
      chapterId: this.props.chapter.id,
      content: this.state.userComment,
      email: JSON.parse(sessionStorage.getItem(rostemConstants.USER)).email
    };
    await axios
      .post(rostemConstants.BASE_URL + "/chapters/comments", body)
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting comments");
        } else {
          this.getAllComments();
        }
      });
    this.setState({
      userComment: ""
    });
  }

  componentDidUpdate(prevProps) {
    if (prevProps.chapter.id !== this.props.chapter.id) {
      this.getAllComments();
    }
  }

  componentDidMount() {
    console.log("Did mount for" + this.props.chapter.id);
    this.getAllComments();
  }

  render() {
    const { description, categoryName, classes } = this.props;
    return (
      <div className={classes.divRoot}>
        <Typography variant="h5">
          <i>Comments</i>
        </Typography>
        <List dense={true} className={classes.root}>
          {this.state.comments.length > 0 ? (
            this.state.comments.map(comment => {
              return (
                <ListItem className={classes.details} divider>
                  <ListItemAvatar>
                    <SendMessage
                      friendEmail={comment.email}
                      username={comment.username}
                    />
                  </ListItemAvatar>
                  <ListItemText
                    secondary={
                      <div>
                        <i>
                          <b>{comment.username}</b>
                        </i>{" "}
                        at{" "}
                        {comment.creationDate.substr(
                          0,
                          comment.creationDate.indexOf(".")
                        )}
                      </div>
                    }
                    primary={comment.content}
                  />
                </ListItem>
              );
            })
          ) : (
            <center>
              <div>No comments for this chapter!</div>
            </center>
          )}
        </List>
        <br />
        <center>
          <TextField
            id="outlined-multiline-flexible"
            label="Add a comment.."
            multiline
            rowsMax="5"
            className={classes.textField}
            margin="normal"
            style={{ width: "80%" }}
            variant="filled"
            value={this.state.userComment}
            onChange={this.onCommentChange.bind(this)}
          />
          <br />
          <Button
            variant="contained"
            color="secondary"
            className={classes.button}
            onClick={this.addComment.bind(this)}
          >
            ADD
          </Button>
        </center>
      </div>
    );
  }
}

CommentsView.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withRouter(withStyles(styles)(CommentsView));
