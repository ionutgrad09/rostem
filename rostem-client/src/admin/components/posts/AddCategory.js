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
import { withStyles } from "@material-ui/core/styles";

const styles = theme => ({
  root: {
    height: 200,
    width: 500,
    overflow: "auto"
  }
});

class AddCategory extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showUniqueNameError: false,
      emptyNameError: false,
      open: false,
      name: "",
      description: ""
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
      });
  }

  submitCategory(e) {
    if (this.state.name === "") {
      this.setState({ emptyNameError: true });
    } else {
      this.addCategory();
    }
  }

  handleClose = () => {
    this.setState({
      open: false,
      emptyNameError: false,
      showUniqueNameError: false
    });
  };

  handleOpen = () => {
    this.setState({ open: true });
  };

  render() {
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
          <DialogTitle variant="h2">New category</DialogTitle>
          <DialogContent>
            <div className={classes.root}>
              <TextField
                id="custom-css-standard-input"
                label="Category name"
                type="text"
                fullWidth
                required
                error={this.state.emptyNameError ? true : false}
                placeholder={
                  this.state.emptyNameError
                    ? "Category name can't be blank"
                    : ""
                }
                onChange={this.onNameChange.bind(this)}
              />
              <br />
              {this.state.showUniqueNameError && (
                <p style={{ color: "red" }}>Category name must be unique!</p>
              )}
              <br />
              <TextField
                id="standard-password-input"
                label="Description"
                type="text"
                margin="normal"
                multiline
                rowsMax="4"
                fullWidth
                onChange={this.onDescriptionChange.bind(this)}
              />
            </div>
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

export default withStyles(styles)(AddCategory);
