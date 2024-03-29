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
  { id: "email", numeric: false, disablePadding: true, label: "Email " },
  { id: "username", numeric: false, disablePadding: false, label: "Username" },
  { id: "bio", numeric: false, disablePadding: false, label: "Bio" },
  {
    id: "registration",
    numeric: false,
    disablePadding: false,
    label: "Registration date"
  },
  { id: "badges", numeric: false, disablePadding: false, label: "No. Badges" }
];

class UsersTable extends React.Component {
  state = {
    order: "asc",
    orderBy: "email",
    selected: [],
    data: [],
    page: 0,
    rowsPerPage: 8
  };

  async getAllUsers() {
    await axios.get(rostemConstants.BASE_URL + "/admin/users").then(result => {
      let res = result.data;
      if (res.status === "false") {
        console.log("Error getting users");
      } else {
        this.setState({ data: res.object.objects });
      }
    });
  }

  componentDidMount() {
    this.getAllUsers();
  }

  async handleDeleteUsers() {
    const { selected } = this.state;
    await axios
      .delete(rostemConstants.BASE_URL + "/admin/users", {
        data: { emails: selected }
      })
      .catch(function(error) {
        console.log(error);
      });

    this.setState({ selected: [] });
    this.getAllUsers();
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
      this.setState(state => ({ selected: state.data.map(n => n.email) }));
      return;
    }
    this.setState({ selected: [] });
  };

  handleClick = (event, email) => {
    const { selected } = this.state;
    const selectedIndex = selected.indexOf(email);
    let newSelected = [];

    if (selectedIndex === -1) {
      newSelected = newSelected.concat(selected, email);
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

  isSelected = email => this.state.selected.indexOf(email) !== -1;

  render() {
    const { classes } = this.props;
    const { data, order, orderBy, selected, rowsPerPage, page } = this.state;
    const emptyRows =
      rowsPerPage - Math.min(rowsPerPage, data.length - page * rowsPerPage);

    return (
      <center>
        <Paper className={classes.root}>
          <TableToolbar
            shouldShowAddButton="false"
            onDelete={this.handleDeleteUsers.bind(this)}
            tableName="Users"
            itemsSelected={selected}
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
                    const isSelected = this.isSelected(n.email);
                    return (
                      <TableRow
                        hover
                        onClick={event => this.handleClick(event, n.email)}
                        role="checkbox"
                        aria-checked={isSelected}
                        tabIndex={-1}
                        key={n.email}
                        selected={isSelected}
                      >
                        <TableCell padding="checkbox">
                          <Checkbox checked={isSelected} />
                        </TableCell>
                        <TableCell component="th" scope="row" padding="none">
                          {n.email}
                        </TableCell>
                        <TableCell align="justify">{n.username}</TableCell>
                        <TableCell align="justify">{n.bio}</TableCell>

                        <TableCell align="justify">
                          {n.registrationDate}
                        </TableCell>
                        <TableCell align="justify">
                          {n.badges !== null ? n.badges.length : 0}
                        </TableCell>
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

UsersTable.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(styles)(UsersTable);
