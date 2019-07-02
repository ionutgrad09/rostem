import React from "react";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import * as rostemConstants from "../../../constants/constants";
import { withStyles } from "@material-ui/core/styles";

const styles = theme => ({
  root: {
    height: 200,
    width: 500,
    overflow: "auto"
  }
});

class EditCategory extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showUniqueNameError: false,
      emptyNameError: false,
      open: false,
      name: "",
      description: "",
      id: null
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

  updateCategory() {
    const { name, description } = this.state;
    rostemConstants.axiosRequest
      .put(
        rostemConstants.BASE_URL + "/admin/updateCategory/" + this.state.id,
        {
          name: name,
          description: description
        }
      )
      .then(response => {
        if (response.status === false) {
        } else {
          this.handleClose();
          this.props.onAdd();
        }
      });
  }

  async getCategoryDetails() {
    rostemConstants.axiosRequest
      .get(
        rostemConstants.BASE_URL +
          "/categories/category/" +
          this.props.categoryId
      )
      .then(response => {
        if (response.status === false) {
        } else {
          this.setState({
            name: response.data.object.name,
            description: response.data.object.description,
            id: response.data.object.id
          });
        }
      });
  }

  componentDidMount() {
    this.getCategoryDetails();
  }

  submitCategory(e) {
    if (this.state.name === "") {
      this.setState({ emptyNameError: true });
    } else {
      this.updateCategory();
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
        <Button variant="contained" color="secondary" onClick={this.handleOpen}>
          EDIT
        </Button>
        <Dialog open={this.state.open} onClose={this.handleClose}>
          <DialogTitle variant="h2">Edit category</DialogTitle>
          <DialogContent>
            <div className={classes.root}>
              <TextField
                id="custom-css-standard-input"
                label="Category name"
                type="text"
                value={this.state.name}
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

              {this.state.showUniqueNameError && (
                <p style={{ color: "red" }}>
                  <br />
                  Category name must be unique!
                  <br />
                </p>
              )}

              <TextField
                id="standard-password-input"
                label="Description"
                type="text"
                margin="normal"
                multiline
                rowsMax="4"
                value={this.state.description}
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
              Save
            </Button>
          </DialogActions>
        </Dialog>
      </div>
    );
  }
}

export default withStyles(styles)(EditCategory);
