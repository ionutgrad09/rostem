import React from "react";
import classNames from "classnames";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import IconButton from "@material-ui/core/IconButton";
import Tooltip from "@material-ui/core/Tooltip";
import DeleteIcon from "@material-ui/icons/Delete";
import { lighten } from "@material-ui/core/styles/colorManipulator";
import AddCategory from "../posts/AddCategory.js";
import AddTutorial from "../posts/AddTutorial.js";
import AddChapter from "../posts/AddChapter.js";
import Button from "@material-ui/core/Button";
import Fab from "@material-ui/core/Fab";
import AddIcon from "@material-ui/icons/Add";
import { withRouter } from "react-router";
const toolbarStyles = theme => ({
  root: {
    paddingRight: theme.spacing.unit
  },
  highlight:
    theme.palette.type === "light"
      ? {
          color: theme.palette.secondary.main,
          backgroundColor: lighten(theme.palette.secondary.light, 0.85)
        }
      : {
          color: theme.palette.text.primary,
          backgroundColor: theme.palette.secondary.dark
        },
  spacer: {
    flex: "1 1 100%"
  },
  actions: {
    color: theme.palette.text.secondary
  },
  title: {
    flex: "0 0 auto"
  }
});

class TableToolbar extends React.Component {
  handleAddChapter() {
    this.props.history.push("/admin/addChapter");
  }
  render() {
    const {
      isCategory,
      isChapter,
      isTutorial,
      tableName,
      onDelete,
      onAdd,
      numSelected,
      classes
    } = this.props;

    return (
      <Toolbar
        className={classNames(classes.root, {
          [classes.highlight]: numSelected > 0
        })}
      >
        <div className={classes.actions}>
          {isCategory === "true" && <AddCategory onAdd={onAdd} />}
          {isTutorial === "true" && <AddTutorial onAdd={onAdd} />}
          {isChapter === "true" && (
            <Fab
              size="small"
              color="secondary"
              aria-label="Add"
              onClick={this.handleAddChapter.bind(this)}
            >
              <AddIcon />
            </Fab>
          )}
        </div>
        <div className={classes.spacer} />
        <div className={classes.title}>
          {numSelected > 0 ? (
            <Typography color="inherit" variant="subtitle1">
              {numSelected} selected
            </Typography>
          ) : (
            <Typography variant="h6" id="tableTitle">
              {tableName}
            </Typography>
          )}
        </div>
        <div className={classes.spacer} />
        <div>
          {numSelected == 1 && (
            <Button
              variant="contained"
              color="secondary"
              className={classes.button}
            >
              EDIT
            </Button>
          )}
        </div>
        <div className={classes.actions}>
          {numSelected > 0 && (
            <Tooltip title="Delete">
              <IconButton onClick={onDelete} aria-label="Delete">
                <DeleteIcon />
              </IconButton>
            </Tooltip>
          )}
        </div>
      </Toolbar>
    );
  }
}

TableToolbar.propTypes = {
  classes: PropTypes.object.isRequired,
  numSelected: PropTypes.number.isRequired
};

export default withRouter(withStyles(toolbarStyles)(TableToolbar));
