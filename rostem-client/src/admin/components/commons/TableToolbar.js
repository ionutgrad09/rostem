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
import EditCategory from "../posts/EditCategory.js";
import EditTutorial from "../posts/EditTutorial.js";
import EditChapter from "../posts/EditChapter.js";

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

  editChapter() {
    const chapterId = this.props.itemsSelected[0];
    this.props.history.push("/admin/editChapter/" + chapterId);
  }
  render() {
    const {
      isCategory,
      isChapter,
      isTutorial,
      tableName,
      onDelete,
      onAdd,
      itemsSelected,
      classes
    } = this.props;

    return (
      <Toolbar
        className={classNames(classes.root, {
          [classes.highlight]: itemsSelected.length > 0
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
          {itemsSelected.length > 0 ? (
            <Typography color="inherit" variant="subtitle1">
              {itemsSelected.length} selected
            </Typography>
          ) : (
            <Typography variant="h6" id="tableTitle">
              {tableName}
            </Typography>
          )}
        </div>
        <div className={classes.spacer} />
        <div>
          {itemsSelected.length == 1 && (
            <div>
              {isCategory === "true" && (
                <EditCategory categoryId={itemsSelected[0]} onAdd={onAdd} />
              )}
              {isTutorial === "true" && (
                <EditTutorial tutorialId={itemsSelected[0]} onAdd={onAdd} />
              )}
              {isChapter === "true" && (
                <Button
                  variant="contained"
                  color="secondary"
                  onClick={this.editChapter.bind(this)}
                >
                  EDIT
                </Button>
              )}
            </div>
          )}
        </div>
        <div className={classes.actions}>
          {itemsSelected.length > 0 && (
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
