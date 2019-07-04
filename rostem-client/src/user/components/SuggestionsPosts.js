import React from "react";
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
import Breadcrumbs from "@material-ui/core/Breadcrumbs";

const styles = theme => ({
  root: {
    marginTop: 25,
    width: 500,
    height: 320,
    overflow: "auto"
  },
  title: {
    align: "center"
  },
  emptyMessage: {
    marginTop: 50
  }
});

class SuggestionsPosts extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      latestChapters: []
    };
  }

  async getRecentChapters() {
    const pageCount = 8;
    const email = this.props.userEmail;

    await axios
      .get(rostemConstants.BASE_URL + "/chapters/random/" + email)
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

  render() {
    const { classes } = this.props;
    return (
      <Box bgcolor="primary.main" className={classes.root}>
        <center>
          <Typography variant="h6">Posts you may like...</Typography>
        </center>
        <Divider />
        <div>
          <List dense={false}>
            {this.state.latestChapters.length > 0 ? (
              this.state.latestChapters.map(chapter => {
                return (
                  <ListItem className={classes.details} divider>
                    <ListItemText
                      primary={
                        <Breadcrumbs separator="›" aria-label="Breadcrumb">
                          <Typography color="textPrimary">
                            {chapter.categoryName}
                          </Typography>
                          <Typography color="textPrimary">
                            {chapter.tutorialName}
                          </Typography>
                          <Typography color="textPrimary">
                            {chapter.name}
                          </Typography>
                        </Breadcrumbs>
                      }
                      secondary={
                        "Creation Date: " +
                        chapter.creationDate.substr(
                          0,
                          chapter.creationDate.indexOf(" ")
                        )
                      }
                    />
                    <ChapterWrapper
                      userEmail={this.props.userEmail}
                      chapter={chapter}
                      updatePosts={this.getRecentChapters.bind(this)}
                    />
                  </ListItem>
                );
              })
            ) : (
              <div className={classes.emptyMessage}>
                <center>
                  <Typography variant="h5">
                    No suggestions for you right now..
                  </Typography>
                </center>
              </div>
            )}
          </List>
        </div>
      </Box>
    );
  }
}

export default withStyles(styles)(SuggestionsPosts);
