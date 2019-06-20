import React from "react";
import {withStyles} from "@material-ui/core/styles";
import Box from "@material-ui/core/Box";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import * as rostemConstants from "../../../constants/constants";
import axios from "axios";

const styles = theme => ({
  wrapperBox: {
    marginLeft: 25,
    width: 900,
    height: 525,
    overflow: "auto"
  },
  table: {
    marginRight: 15,
    maxWidth: 850
  }
});

class UserStatistics extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      topUsers: []
    };
  }

  async getTopUsers() {
    await axios
      .get(rostemConstants.BASE_URL + "/admin/statistics/users")
      .then(res =>
        this.setState({
          topUsers: res.data.object.objects
        })
      )
      .catch(function(error) {
        console.log(error);
      });
  }

  componentDidMount() {
    this.getTopUsers();
  }

  render() {
    const { classes } = this.props;
    return (
      <div className={classes.root}>
        <Box bgcolor="primary.main" className={classes.wrapperBox} p={1} m={1}>
          <Table className={classes.table}>
            <TableHead>
              <TableRow>
                <TableCell>User</TableCell>
                <TableCell align="right">Comments</TableCell>
                <TableCell align="right">Todo Chapters</TableCell>
                <TableCell align="right">Done Chapters</TableCell>
                <TableCell align="right">Liked Chapters</TableCell>
                <TableCell align="right">Favorite Categories</TableCell>
                <TableCell align="right">TOTAL SCORE</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {this.state.topUsers.map(user => (
                <TableRow key={user.email}>
                  <TableCell component="th" scope="row">
                    {user.email}
                  </TableCell>
                  <TableCell align="right">{user.noComments}</TableCell>
                  <TableCell align="right">{user.todoChapters}</TableCell>
                  <TableCell align="right">{user.doneChapters}</TableCell>
                  <TableCell align="right">{user.likedChapters}</TableCell>
                  <TableCell align="right">{user.favoriteCategories}</TableCell>
                  <TableCell align="right">
                    {user.noComments +
                      user.todoChapters +
                      user.doneChapters +
                      user.likedChapters +
                      user.favoriteCategories}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Box>
      </div>
    );
  }
}

export default withStyles(styles)(UserStatistics);
