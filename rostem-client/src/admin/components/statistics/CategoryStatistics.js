import React from "react";
import { withStyles } from "@material-ui/core/styles";
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

class CategoryStatistics extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      topCategories: []
    };
  }

  async getTopCategories() {
    await axios
      .get(rostemConstants.BASE_URL + "/admin/statistics/categories")
      .then(res =>
        this.setState({
          topCategories: res.data.object.objects
        })
      )
      .catch(function(error) {
        console.log(error);
      });
  }

  componentDidMount() {
    this.getTopCategories();
  }

  render() {
    const { classes } = this.props;
    return (
      <div className={classes.root}>
        <Box bgcolor="primary.main" className={classes.wrapperBox} p={1} m={1}>
          <Table className={classes.table}>
            <TableHead>
              <TableRow>
                <TableCell>Category name</TableCell>
                <TableCell>Number of Tutorials</TableCell>
                <TableCell align="right">
                  How many users marked this category as favorite
                </TableCell>
                <TableCell align="right">Percentage (users)</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {this.state.topCategories.map(category => (
                <TableRow key={category.name}>
                  <TableCell component="th" scope="row">
                    {category.name}
                  </TableCell>
                  <TableCell align="center">{category.noTutorials}</TableCell>
                  <TableCell align="center">{category.noFavorites}</TableCell>
                  <TableCell align="right">{category.percentage} %</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Box>
      </div>
    );
  }
}

export default withStyles(styles)(CategoryStatistics);
