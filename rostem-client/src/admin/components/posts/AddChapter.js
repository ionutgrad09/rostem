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

export default class AddChapter extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showUniqueNameError: false,
      showUniqueNameErrorMessage: "",
      open: false,
      errors: [],
      name: "",
      description: "",
      url: "",
      tutorials: [],
      tutorial: ""
    };
  }

  onNameChange(e) {
    this.setState({ name: e.target.value });
    this.clearValidationError("name");
  }

  onDescriptionChange(e) {
    this.setState({ description: e.target.value });
  }

  onUrlChange(e) {
    this.setState({ url: e.target.value });
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
    const { url, name, description, tutorial } = this.state;
    axios
      .post(rostemConstants.BASE_URL + "/admin/createChapter", {
        name: name,
        description: description,
        tutorialId: tutorial,
        url: url
      })
      .then(response => {
        if (response.status === false) {
        } else {
          this.handleClose();
          this.props.onAdd();
        }
      })
      .catch(error => {
        this.setState({
          showUniqueNameError: true,
          showUniqueNameErrorMessage: error.response.data.exception
        });
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
        <Fab
          size="small"
          color="secondary"
          aria-label="Add"
          onClick={this.handleOpen}
        >
          <AddIcon />
        </Fab>
        <Dialog open={this.state.open} onClose={this.handleClose}>
          <DialogTitle variant="h2">New chapter</DialogTitle>
          <DialogContent>
            <TextField
              id="custom-css-standard-input"
              label="Chapter name"
              type="text"
              fullWidth
              required
              onChange={this.onNameChange.bind(this)}
            />
            <small className="danger-error">{nameError ? nameError : ""}</small>
            <small className="danger-error">
              {this.state.showUniqueNameError
                ? this.state.showUniqueNameErrorMessage
                : ""}
            </small>
            <TextField
              id="custom-css-standard-input"
              label="Description"
              type="text"
              margin="normal"
              fullWidth
              onChange={this.onDescriptionChange.bind(this)}
            />
            <TextField
              id="custom-css-standard-input"
              label="Source (URL)"
              type="text"
              margin="normal"
              fullWidth
              onChange={this.onUrlChange.bind(this)}
            />
            <small className="danger-error">{urlError ? urlError : ""}</small>
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
          </DialogContent>

          <DialogActions>
            <Button onClick={this.handleClose.bind(this)} color="secondary">
              Cancel
            </Button>
            <Button onClick={this.submitChapter.bind(this)} color="secondary">
              Add
            </Button>
          </DialogActions>
        </Dialog>
      </div>
    );
  }
}