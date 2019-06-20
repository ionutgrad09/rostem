import React from "react";
import {withStyles} from "@material-ui/core/styles";
import {Typography} from "@material-ui/core";
import Divider from "@material-ui/core/Divider";
import ListItemText from "@material-ui/core/ListItemText";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import * as constants from "../../constants/constants.js";
import ChapterWrapper from "../components/ChapterWrapper.js";
import Box from "@material-ui/core/Box";

const styles = theme => ({
  root: {
    width: 450,
    height: 500,
    overflow: "auto"
  },
  title: {
    align: "center"
  },
  empty: {
    marginTop: 150
  }
});

class UserDoneChapters extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      doneChapters: []
    };
  }

  async getDoneChapters() {
    const email = this.props.userEmail;
    await constants.axiosRequest
      .get(constants.BASE_URL + "/chapters/done/" + email)
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting done chapters");
        } else {
          this.setState({
            doneChapters: res.object.objects
          });
        }
      });
  }

  componentDidMount() {
    this.getDoneChapters();
  }

  render() {
    const { classes } = this.props;
    return (
      <Box bgcolor="primary.main" className={classes.root}>
        <center>
          <Typography variant="h6">DONE</Typography>
        </center>
        <Divider />
        <div>
          <List dense={false}>
            {this.state.doneChapters.length > 0 ? (
              this.state.doneChapters.map(chapter => {
                return (
                  <ListItem className={classes.details} divider>
                    <ListItemText primary={chapter.name} />
                    <ChapterWrapper
                      chapter={chapter}
                      updatePosts={this.getDoneChapters.bind(this)}
                    />
                  </ListItem>
                );
              })
            ) : (
              <div className={classes.empty}>
                <center>
                  <Typography variant="h4">No DONE chapters yet!</Typography>
                </center>
              </div>
            )}
          </List>
        </div>
      </Box>
    );
  }
}

export default withStyles(styles)(UserDoneChapters);
