import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TablePagination from "@material-ui/core/TablePagination";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import Checkbox from "@material-ui/core/Checkbox";
import * as UsersFunctions from "../commons/UtilsFunctions";
import TableHeader from "../commons/TableHeader";
import TableToolbar from "../commons/TableToolbar";
import * as rostemConstants from "../../../constants/constants";
import axios from "axios";

const styles = theme => ({
  root: {
    maxWidth: 1220,
    marginTop: theme.spacing.unit * 4
  },
  table: {
    maxWidth: 1200,
    minWidth: 400
  },
  tableWrapper: {
    overflowX: "auto"
  }
});

const rows = [
  { id: "name", numeric: false, disablePadding: true, label: "Name " },
  {
    id: "description",
    numeric: false,
    disablePadding: false,
    label: "Description"
  },
  {
    id: "tutorial",
    numeric: false,
    disablePadding: false,
    label: "Tutorial"
  }
];

class ChaptersTable extends React.Component {
  state = {
    order: "asc",
    orderBy: "name",
    selected: [],
    data: [],
    page: 0,
    rowsPerPage: 8
  };

  async getAllChapters() {
    await axios.get(rostemConstants.BASE_URL + "/chapters").then(result => {
      let res = result.data;
      if (res.status === "false") {
        console.log("Error getting tutorials");
      } else {
        this.setState({ data: res.object.objects });
      }
    });
  }

  componentDidMount() {
    this.getAllChapters();
  }

  async handleDeleteChapter() {
    const { selected } = this.state;
    await axios
      .delete(rostemConstants.BASE_URL + "/admin/deleteChapters", {
        data: { ids: selected }
      })
      .catch(function(error) {
        console.log(error);
      });

    this.setState({ selected: [] });
    this.getAllChapters();
  }

  async handleAddChapter() {
    this.getAllChapters();
  }

  handleRequestSort = (event, property) => {
    const orderBy = property;
    let order = "desc";

    if (this.state.orderBy === property && this.state.order === "desc") {
      order = "asc";
    }

    this.setState({ order, orderBy });
  };

  handleSelectAllClick = event => {
    if (event.target.checked) {
      this.setState(state => ({ selected: state.data.map(n => n.id) }));
      return;
    }
    this.setState({ selected: [] });
  };

  handleClick = (event, id) => {
    const { selected } = this.state;
    const selectedIndex = selected.indexOf(id);
    let newSelected = [];

    if (selectedIndex === -1) {
      newSelected = newSelected.concat(selected, id);
    } else if (selectedIndex === 0) {
      newSelected = newSelected.concat(selected.slice(1));
    } else if (selectedIndex === selected.length - 1) {
      newSelected = newSelected.concat(selected.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelected = newSelected.concat(
        selected.slice(0, selectedIndex),
        selected.slice(selectedIndex + 1)
      );
    }

    this.setState({ selected: newSelected });
  };

  handleChangePage = (event, page) => {
    this.setState({ page });
  };

  handleChangeRowsPerPage = event => {
    this.setState({ rowsPerPage: event.target.value });
  };

  isSelected = id => this.state.selected.indexOf(id) !== -1;

  render() {
    const { classes } = this.props;
    const { data, order, orderBy, selected, rowsPerPage, page } = this.state;
    const emptyRows =
      rowsPerPage - Math.min(rowsPerPage, data.length - page * rowsPerPage);

    return (
      <center>
        <Paper className={classes.root}>
          <TableToolbar
            isChapter="true"
            isTutorial="false"
            isCategory="false"
            onAdd={this.handleAddChapter.bind(this)}
            onDelete={this.handleDeleteChapter.bind(this)}
            tableName="Chapters"
            numSelected={selected.length}
          />
          <div className={classes.tableWrapper}>
            <Table className={classes.table} aria-labelledby="tableTitle">
              <TableHeader
                rows={rows}
                numSelected={selected.length}
                order={order}
                orderBy={orderBy}
                onSelectAllClick={this.handleSelectAllClick}
                onRequestSort={this.handleRequestSort}
                rowCount={data.length}
              />
              <TableBody>
                {UsersFunctions.stableSort(
                  data,
                  UsersFunctions.getSorting(order, orderBy)
                )
                  .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                  .map(n => {
                    const isSelected = this.isSelected(n.id);
                    return (
                      <TableRow
                        hover
                        onClick={event => this.handleClick(event, n.id)}
                        role="checkbox"
                        aria-checked={isSelected}
                        tabIndex={-1}
                        key={n.id}
                        selected={isSelected}
                      >
                        <TableCell padding="checkbox">
                          <Checkbox checked={isSelected} />
                        </TableCell>
                        <TableCell component="th" scope="row" padding="none">
                          {n.name}
                        </TableCell>
                        <TableCell align="justify">{n.description}</TableCell>
                        <TableCell align="justify">{n.tutorialName}</TableCell>
                      </TableRow>
                    );
                  })}
                {emptyRows > 0 && (
                  <TableRow style={{ height: 49 * emptyRows }}>
                    <TableCell colSpan={4} />
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </div>
          <TablePagination
            rowsPerPageOptions={[5, 8, 12]}
            component="div"
            count={data.length}
            rowsPerPage={rowsPerPage}
            page={page}
            backIconButtonProps={{
              "aria-label": "Previous Page"
            }}
            nextIconButtonProps={{
              "aria-label": "Next Page"
            }}
            onChangePage={this.handleChangePage}
            onChangeRowsPerPage={this.handleChangeRowsPerPage}
          />
        </Paper>
      </center>
    );
  }
}

ChaptersTable.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(styles)(ChaptersTable);
