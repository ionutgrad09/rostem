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

export default class AddTutorial extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showUniqueNameError: false,
      showUniqueNameErrorMessage: "",
      open: false,
      errors: [],
      name: "",
      description: "",
      categories: [],
      category: ""
    };
  }

  onNameChange(e) {
    this.setState({ name: e.target.value });
    this.clearValidationError("name");
  }

  onDescriptionChange(e) {
    this.setState({ description: e.target.value });
  }

  onCategoryChange(e) {
    this.setState({ category: e.target.value });
    this.clearValidationError("category");
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

  async getAllCategories() {
    await axios.get(rostemConstants.BASE_URL + "/categories").then(result => {
      let res = result.data;
      if (res.status === "false") {
        console.log("Error getting categories");
      } else {
        console.log(res.object.objects);
        this.setState({ categories: res.object.objects });
      }
    });
  }

  componentDidMount() {
    this.getAllCategories();
  }

  addTutorial() {
    const { name, description, category } = this.state;
    axios
      .post(rostemConstants.BASE_URL + "/admin/addTutorial", {
        name: name,
        description: description,
        categoryId: category
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

  submitTutorial(e) {
    let shouldAdd = true;
    if (this.state.name === "") {
      this.setState({ showUniqueNameError: false });
      this.showValidationError("name", "Tutorial's name can't be empty!");
      shouldAdd = false;
    }
    if (this.state.category === "") {
      this.showValidationError(
        "category",
        "The tutorial must belong to a category!"
      );
      shouldAdd = false;
    }
    if (shouldAdd === true) {
      this.addTutorial();
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
      categoryError = null;
    const { categories } = this.state;
    for (let err of this.state.errors) {
      if (err.element === "name") {
        nameError = err.message;
      } else if (err.element === "category") {
        categoryError = err.message;
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
          <DialogTitle variant="h2">New tutorial</DialogTitle>
          <DialogContent>
            <TextField
              id="custom-css-standard-input"
              label="Tutorial name"
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
              id="standard-password-input"
              label="Description"
              type="text"
              margin="normal"
              fullWidth
              onChange={this.onDescriptionChange.bind(this)}
            />
            <FormControl fullWidth>
              <InputLabel>Category</InputLabel>
              <Select
                value={this.state.category}
                onChange={this.onCategoryChange.bind(this)}
              >
                {categories.map(c => (
                  <MenuItem value={c.id}>{c.name}</MenuItem>
                ))}
              </Select>
            </FormControl>
            <small className="danger-error">
              {categoryError ? categoryError : ""}
            </small>
          </DialogContent>

          <DialogActions>
            <Button onClick={this.handleClose.bind(this)} color="secondary">
              Cancel
            </Button>
            <Button onClick={this.submitTutorial.bind(this)} color="secondary">
              Add
            </Button>
          </DialogActions>
        </Dialog>
      </div>
    );
  }
}
