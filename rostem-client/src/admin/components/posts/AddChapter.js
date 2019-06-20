import React from "react";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import * as rostemConstants from "../../../constants/constants";
import axios from "axios";
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
import { EditorState, convertFromRaw, convertToRaw } from "draft-js";
import draftToHtml from "draftjs-to-html";

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

class AddChapter extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showUniqueNameError: false,
      showUniqueNameErrorMessage: "",
      errors: [],
      name: "",
      tutorials: [],
      tutorial: "",
      editorState: EditorState.createEmpty()
    };
    this.onChange = editorState => this.setState({ editorState });
  }

  onNameChange(e) {
    this.setState({ name: e.target.value });
    this.clearValidationError("name");
  }

  onTutorialChange(e) {
    this.setState({ tutorial: e.target.value });
    this.clearValidationError("tutorial");
  }

  showValidationError(element, message) {
    this.setState(prevState => ({
      errors: [...prevState.errors, { element, message }]
    }));
  }

  clearValidationError(element) {
    this.setState(prevState => {
      let newArr = [];
      for (let err of prevState.errors) {
        if (element !== err.element) {
          newArr.push(err);
        }
      }
      return { errors: newArr };
    });
  }

  async getAllTutorials() {
    await axios.get(rostemConstants.BASE_URL + "/tutorials").then(result => {
      let res = result.data;
      if (res.status === "false") {
        console.log("Error getting categories");
      } else {
        this.setState({ tutorials: res.object.objects });
      }
    });
  }

  componentDidMount() {
    this.getAllTutorials();
  }

  addChapter() {
    const { name, tutorial } = this.state;
    const content = draftToHtml(
      convertToRaw(this.state.editorState.getCurrentContent())
    );
    axios
      .post(rostemConstants.BASE_URL + "/admin/createChapter", {
        name: name,
        content: content,
        tutorialId: tutorial
      })
      .then(result => {
        this.props.history.push("/admin");
      });
  }

  submitChapter(e) {
    let shouldAdd = true;
    if (this.state.name === "") {
      this.setState({ showUniqueNameError: false });
      this.showValidationError("name", "Chapter's name can't be empty!");
      shouldAdd = false;
    }
    if (this.state.url === "") {
      this.showValidationError(
        "url",
        "You must provide a source for the chapter!"
      );
      shouldAdd = false;
    }
    if (this.state.tutorial === "") {
      this.showValidationError(
        "tutorial",
        "The chapter must belong to a tutorial!"
      );
      shouldAdd = false;
    }
    if (shouldAdd === true) {
      this.addChapter();
    }
  }

  handleClose = () => {
    this.setState({ open: false, showUniqueNameError: false, errors: [] });
  };

  handleOpen = () => {
    this.setState({ open: true });
  };

  render() {
    const { classes } = this.props;
    let nameError = null,
      tutorialError = null,
      urlError = null;
    const { tutorials } = this.state;
    for (let err of this.state.errors) {
      if (err.element === "name") {
        nameError = err.message;
      }
      if (err.element === "tutorial") {
        tutorialError = err.message;
      }
      if (err.element === "url") {
        urlError = err.message;
      }
    }

    return (
      <div>
        <MenuAppBar username="ADMINISTRATOR" />
        <center>
          <Box bgcolor="primary.main" className={classes.boxRoot}>
            <div className={classes.root}>
              <br />
              <Typography component="h1" variant="h5">
                Add a new chapter
              </Typography>
              <br />
              <Divider />
              <br />
              <TextField
                id="custom-css-standard-input"
                label="Chapter name"
                type="text"
                fullWidth
                required
                onChange={this.onNameChange.bind(this)}
              />
              <small className="danger-error">
                {nameError ? nameError : ""}
              </small>

              <FormControl fullWidth>
                <InputLabel>Tutorial</InputLabel>
                <Select
                  value={this.state.tutorial}
                  onChange={this.onTutorialChange.bind(this)}
                >
                  {tutorials.map(c => (
                    <MenuItem value={c.id}>{c.name}</MenuItem>
                  ))}
                </Select>
              </FormControl>
              <small className="danger-error">
                {tutorialError ? tutorialError : ""}
              </small>
              <br />
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
                  onClick={this.addChapter.bind(this)}
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

export default withRouter(withStyles(styles)(AddChapter));
