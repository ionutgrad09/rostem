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
import { withStyles } from "@material-ui/core/styles";

const styles = theme => ({
  root: {
    height: 250,
    width: 500,
    overflow: "auto"
  }
});

class AddTutorial extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showUniqueNameError: false,
      emptyNameError: false,
      emptyCategoryError: false,
      open: false,
      errors: [],
      name: "",
      description: "",
      categories: [],
      category: ""
    };
  }

  onNameChange(e) {
    this.setState({
      name: e.target.value,
      emptyNameError: false,
      showUniqueNameError: false
    });
  }

  onDescriptionChange(e) {
    this.setState({ description: e.target.value });
  }

  onCategoryChange(e) {
    this.setState({ category: e.target.value, emptyCategoryError: false });
  }

  async getAllCategories() {
    await axios.get(rostemConstants.BASE_URL + "/categories").then(result => {
      let res = result.data;
      if (res.status === "false") {
        console.log("Error getting categories");
      } else {
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
      .post(rostemConstants.BASE_URL + "/admin/createTutorial", {
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
        this.setState({ showUniqueNameError: true });
      });
  }

  submitTutorial(e) {
    let shouldAdd = true;
    if (this.state.name === "") {
      this.setState({ emptyNameError: true });
      shouldAdd = false;
    }
    if (this.state.category === "") {
      this.setState({ emptyCategoryError: true });
      shouldAdd = false;
    }
    if (shouldAdd === true) {
      this.addTutorial();
    }
  }

  handleClose = () => {
    this.setState({
      open: false,
      emptyNameError: false,
      emptyCategoryError: false,
      showUniqueNameError: false,
      errors: []
    });
  };

  handleOpen = () => {
    this.setState({ open: true });
  };

  render() {
    const { categories } = this.state;
    const { classes } = this.props;
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
            <div className={classes.root}>
              <TextField
                id="custom-css-standard-input"
                label="Tutorial name"
                type="text"
                fullWidth
                required
                onChange={this.onNameChange.bind(this)}
                error={this.state.emptyNameError ? true : false}
                placeholder={
                  this.state.emptyNameError
                    ? "Tutorial name can't be blank"
                    : ""
                }
              />

              {this.state.showUniqueNameError && (
                <p style={{ color: "red" }}>
                  <br />
                  Tutorial name must be unique!
                  <br />
                </p>
              )}
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

              {this.state.emptyCategoryError && (
                <p style={{ color: "red" }}>
                  <br />
                  Select a category!
                  <br />
                </p>
              )}
            </div>
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

export default withStyles(styles)(AddTutorial);
