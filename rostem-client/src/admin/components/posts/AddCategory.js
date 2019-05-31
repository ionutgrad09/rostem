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

export default class AddCategory extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showUniqueNameError: false,
      showUniqueNameErrorMessage: "",
      open: false,
      errors: [],
      name: "",
      description: ""
    };
  }

  onNameChange(e) {
    this.setState({ name: e.target.value });
    this.clearValidationError("name");
  }

  onDescriptionChange(e) {
    this.setState({ description: e.target.value });
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

  addCategory() {
    const { name, description } = this.state;
    axios
      .post(rostemConstants.BASE_URL + "/admin/createCategory", {
        name: name,
        description: description
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

  submitCategory(e) {
    if (this.state.name === "") {
      this.setState({ showUniqueNameError: false });
      this.showValidationError("name", "Category name can't be empty!");
    } else {
      this.addCategory();
    }
  }

  handleClose = () => {
    this.setState({ open: false, showUniqueNameError: false, errors: [] });
  };

  handleOpen = () => {
    this.setState({ open: true });
  };

  render() {
    let nameError = null;

    for (let err of this.state.errors) {
      if (err.element === "name") {
        nameError = err.message;
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
          <DialogTitle variant="h2">New category</DialogTitle>
          <DialogContent>
            <TextField
              id="custom-css-standard-input"
              label="Category name"
              type="text"
              fullWidth
              required
              onChange={this.onNameChange.bind(this)}
            />
            <small className="danger-error">{nameError ? nameError : ""}</small>
            <TextField
              id="standard-password-input"
              label="Description"
              type="text"
              margin="normal"
              fullWidth
              onChange={this.onDescriptionChange.bind(this)}
            />
            <small className="danger-error">
              {this.state.showUniqueNameError
                ? this.state.showUniqueNameErrorMessage
                : ""}
            </small>
          </DialogContent>

          <DialogActions>
            <Button onClick={this.handleClose.bind(this)} color="secondary">
              Cancel
            </Button>
            <Button onClick={this.submitCategory.bind(this)} color="secondary">
              Add
            </Button>
          </DialogActions>
        </Dialog>
      </div>
    );
  }
}
