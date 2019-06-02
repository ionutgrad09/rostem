import React from "react";
import Paper from "@material-ui/core/Paper";
import { withStyles } from "@material-ui/core/styles";
import { Typography } from "@material-ui/core";
import Divider from "@material-ui/core/Divider";
import ListItemText from "@material-ui/core/ListItemText";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import axios from "axios";
import * as rostemConstants from "../../constants/constants.js";
import ChapterWrapper from "./ChapterWrapper.js";
import Box from "@material-ui/core/Box";

const styles = theme => ({
  root: {
    width: 500,
    height: 415,
    overflow: "auto"
  },
  title: {
    align: "center"
  }
});

class RecentPosts extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      latestChapters: []
    };
  }

  async getRecentChapters() {
    const pageCount = 5;
    const email = sessionStorage.getItem(rostemConstants.EMAIL);
    const body = {
      counter: pageCount,
      email: email
    };
    await axios
      .post(rostemConstants.BASE_URL + "/chapters/latest", body)
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting tutorials");
        } else {
          this.setState({
            latestChapters: res.object.objects
          });
        }
      });
  }

  componentDidMount() {
    this.getRecentChapters();
  }

  handleChapterClick(id) {
    const selectedChapter = this.state.latestChapters.find(function(chapter) {
      return chapter.id === id;
    });
  }

  render() {
    const { classes } = this.props;
    return (
      <Box bgcolor="primary.main" className={classes.root}>
        <center>
          <Typography variant="h6">Recent Posts</Typography>
        </center>
        <Divider />
        <div>
          <List dense={false}>
            {this.state.latestChapters.length > 0 ? (
              this.state.latestChapters.map(chapter => {
                return (
                  <ListItem className={classes.details} divider>
                    <ListItemText
                      primary={chapter.name}
                      secondary={
                        "Creation Date: " +
                        chapter.creationDate.substr(
                          0,
                          chapter.creationDate.indexOf(" ")
                        )
                      }
                    />
                    <ChapterWrapper
                      chapter={chapter}
                      updatePosts={this.getRecentChapters.bind(this)}
                    />
                  </ListItem>
                );
              })
            ) : (
              <div>
                <center>No recent posts!</center>
              </div>
            )}
          </List>
        </div>
      </Box>
    );
  }
}

export default withStyles(styles)(RecentPosts);
