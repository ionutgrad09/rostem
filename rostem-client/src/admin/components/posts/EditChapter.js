import React from "react";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import * as rostemConstants from "../../../constants/constants";
import Fab from "@material-ui/core/Fab";
import AddIcon from "@material-ui/icons/Add";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import ReactDOM from "react-dom";
import { withStyles } from "@material-ui/core/styles";
import MenuAppBar from "../../../commons/components/MenuHeader";
import Box from "@material-ui/core/Box";
import { withRouter } from "react-router";
import Divider from "@material-ui/core/Divider";
import { Typography } from "@material-ui/core";
import { Editor } from "react-draft-wysiwyg";
import "react-draft-wysiwyg/dist/react-draft-wysiwyg.css";
import {
  ContentState,
  EditorState,
  convertFromRaw,
  convertToRaw
} from "draft-js";
import draftToHtml from "draftjs-to-html";
import htmlToDraft from "html-to-draftjs";

const styles = {
  editorStyle: {
    height: "auto",
    border: "1px solid black",
    padding: 10
  },
  boxRoot: {
    marginTop: 25,
    marginBottom: 25,
    width: 900,
    minWidth: 300,
    height: "auto"
  },
  root: {
    padding: 25
  },
  title: {
    align: "center"
  },
  textField: {
    marginTop: 15,
    width: 500
  },
  actionButtons: {
    marginTop: 25
  },
  dangerError: {
    color: "red"
  }
};

class EditChapter extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showUniqueNameError: false,
      emptyNameError: false,
      emptyTutorialError: false,
      name: "",
      tutorials: [],
      tutorial: "",
      editorState: null
    };
    this.onChange = editorState => this.setState({ editorState });
  }

  onNameChange(e) {
    this.setState({
      name: e.target.value,
      showUniqueNameError: false,
      emptyNameError: false
    });
  }

  onTutorialChange(e) {
    this.setState({ tutorial: e.target.value, emptyTutorialError: false });
  }

  async getAllTutorials() {
    await rostemConstants.axiosRequest
      .get(rostemConstants.BASE_URL + "/tutorials")
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting tutorials");
        } else {
          this.setState({ tutorials: res.object.objects });
        }
      });
  }

  async getChapterDetails() {
    await rostemConstants.axiosRequest
      .post(
        rostemConstants.BASE_URL +
          "/chapters/chapter/" +
          this.props.match.params.chapterId
      )
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting tutorials");
        } else {
          this.setState({
            name: res.object.name,
            editorState: EditorState.createWithContent(
              ContentState.createFromBlockArray(htmlToDraft(res.object.content))
            )
          });
        }
      });
  }

  componentWillMount() {
    this.getChapterDetails();
    this.getAllTutorials();
  }

  async updateChapter() {
    const { name, tutorial } = this.state;
    const content = draftToHtml(
      convertToRaw(this.state.editorState.getCurrentContent())
    );
    rostemConstants.axiosRequest
      .put(
        rostemConstants.BASE_URL +
          "/admin/updateChapter/" +
          this.props.match.params.chapterId,
        {
          name: name,
          content: content,
          tutorialId: tutorial
        }
      )
      .then(result => {
        this.props.history.push("/admin");
      });
  }

  submitChapter(e) {
    let shouldUpdate = true;
    if (this.state.name === "") {
      shouldUpdate = false;
      this.setState({ emptyNameError: true });
    }
    if (this.state.tutorial === "") {
      shouldUpdate = false;
      this.setState({ emptyTutorialError: true });
    }
    if (shouldUpdate === true) {
      this.updateChapter();
    }
  }

  render() {
    const { classes } = this.props;
    const { tutorials } = this.state;

    return (
      <div>
        <MenuAppBar username="ADMINISTRATOR" />
        <center>
          <Box bgcolor="primary.main" className={classes.boxRoot}>
            <div className={classes.root}>
              <br />
              <Typography component="h1" variant="h5">
                Update chapter
              </Typography>
              <br />
              <Divider />
              <br />
              <TextField
                id="custom-css-standard-input"
                label="New name"
                type="text"
                fullWidth
                required
                value={this.state.name}
                error={this.state.emptyNameError ? true : false}
                placeholder={
                  this.state.emptyNameError ? "Chapter name can't be blank" : ""
                }
                onChange={this.onNameChange.bind(this)}
              />
              {this.state.showUniqueNameError && (
                <p style={{ color: "red" }}>
                  <br />
                  Chapter name must be unique!
                  <br />
                </p>
              )}

              <FormControl fullWidth>
                <InputLabel>New tutorial</InputLabel>
                <Select
                  value={this.state.tutorial}
                  onChange={this.onTutorialChange.bind(this)}
                >
                  {tutorials.map(c => (
                    <MenuItem value={c.id}>{c.name}</MenuItem>
                  ))}
                </Select>
              </FormControl>
              {this.state.emptyTutorialError && (
                <p style={{ color: "red" }}>
                  <br />
                  Select a tutorial!
                  <br />
                </p>
              )}
              <br />
              <Divider />
              <br />
              <div className={classes.editorStyle}>
                <h2> Content </h2>
                <Editor
                  className={classes.draftEditor}
                  editorState={this.state.editorState}
                  toolbarClassName="toolbarClassName"
                  wrapperClassName="wrapperClassName"
                  editorClassName="editorClassName"
                  onEditorStateChange={this.onChange}
                />
              </div>
              <div className={classes.actionButtons}>
                <Button
                  type="submit"
                  variant="contained"
                  color="secondary"
                  onClick={this.submitChapter.bind(this)}
                >
                  Save
                </Button>
              </div>
              <br />
            </div>
          </Box>
        </center>
      </div>
    );
  }
}

export default withRouter(withStyles(styles)(EditChapter));
