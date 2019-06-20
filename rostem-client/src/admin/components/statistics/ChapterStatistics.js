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

class ChapterStatistics extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      topChapters: []
    };
  }

  async getTopChapters() {
    await axios
      .get(rostemConstants.BASE_URL + "/admin/statistics/chapters")
      .then(res =>
        this.setState({
          topChapters: res.data.object.objects
        })
      )
      .catch(function(error) {
        console.log(error);
      });
  }

  componentDidMount() {
    this.getTopChapters();
  }

  render() {
    const { classes } = this.props;
    return (
      <div className={classes.root}>
        <Box bgcolor="primary.main" className={classes.wrapperBox} p={1} m={1}>
          <Table className={classes.table}>
            <TableHead>
              <TableRow>
                <TableCell>Chapter</TableCell>
                <TableCell align="right">Comments</TableCell>
                <TableCell align="right">Likes</TableCell>
                <TableCell align="right">Marked as ToDo</TableCell>
                <TableCell align="right">Marked as Done</TableCell>
                <TableCell align="right">TOTAL SCORE</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {this.state.topChapters.map(chapter => (
                <TableRow key={chapter.name}>
                  <TableCell component="th" scope="row">
                    {chapter.name}
                  </TableCell>
                  <TableCell align="right">{chapter.noComments}</TableCell>
                  <TableCell align="right">{chapter.noLikes}</TableCell>
                  <TableCell align="right">{chapter.noTodo}</TableCell>
                  <TableCell align="right">{chapter.noDone}</TableCell>
                  <TableCell align="right">
                    {chapter.noComments +
                      chapter.noLikes +
                      chapter.noTodo +
                      chapter.noDone}
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

export default withStyles(styles)(ChapterStatistics);
