import React from "react";
import Typography from "@material-ui/core/Typography";
import MenuAppBar from "../../commons/components/MenuHeader";
import IconButton from "@material-ui/core/IconButton";
import Grid from "@material-ui/core/Grid";
import { withStyles } from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import { withRouter } from "react-router-dom";
import * as constants from "../../constants/constants.js";
import Paper from "@material-ui/core/Paper";
import InputBase from "@material-ui/core/InputBase";
import SearchIcon from "@material-ui/icons/Search";
import Box from "@material-ui/core/Box";
import EmptyChapterView from "./EmptyChapterView";
import ChapterView from "./ChapterView";
import SimpleTutorial from "./SimpleTutorial";
import { Divider } from "@material-ui/core";

const styles = theme => ({
  box: {
    display: "flex",
    flexWrap: "wrap",
    justifyContent: "flex-start",
    overflow: "hidden"
  },
  root: {
    flexGrow: 1,
    padding: 25,
    maxWidth: 400,
    fontSize: "10px"
  },
  tutorials: {
    marginTop: 0
  },
  details: {
    display: "inline-flex"
  },
  categoryTitle: {
    marginTop: 10
  },
  expansionPanelDetailsRoot: {
    display: "list-item"
  },
  heading: {
    fontSize: theme.typography.pxToRem(15),
    flexBasis: "33.33%",
    flexShrink: 0
  },
  secondaryHeading: {
    fontSize: theme.typography.pxToRem(15),
    color: theme.palette.text.secondary
  }
});

class TutorialsView extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      expanded: null,
      searchText: "",
      tutorials: [],
      shownTutorials: [],
      chapters: [],
      selectedChapter: null,
      showChapter: false,
      userEmail: ""
    };
  }

  filterShownTutorials(searchKey) {
    const filteredTutorials = this.state.shownTutorials.filter(function(
      tutorial
    ) {
      return tutorial.name.includes(searchKey);
    });

    this.setState({ shownTutorials: filteredTutorials });
  }

  onSearchChange(event, value) {
    const searchKey = event.target.value;
    this.setState({
      searchText: searchKey,
      shownTutorials: this.state.tutorials
    });
    if (searchKey !== "") {
      this.filterShownTutorials(searchKey);
    }
  }

  async getUser() {
    await constants.axiosRequest
      .post(constants.BASE_URL + "/login/getdetails")
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting user");
        } else {
          this.setState({
            userEmail: res.object.user.email
          });
        }
      });
  }

  async getAllTutorials() {
    const categoryName = this.props.match.params.categoryName;
    await constants.axiosRequest
      .get(constants.BASE_URL + "/tutorials/" + categoryName)
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting tutorials");
        } else {
          this.setState({
            tutorials: res.object.objects,
            shownTutorials: res.object.objects
          });
        }
      });
  }

  componentDidMount() {
    this.getUser();
    this.getAllTutorials();
  }

  handleChapterClick(ch) {
    this.setState({
      showChapter: true,
      selectedChapter: ch
    });
  }

  handleChange = id => (event, expanded) => {
    this.getAllChapters(id);
    this.setState({
      expanded: expanded ? id : false
    });
  };

  render() {
    const { classes } = this.props;
    const { expanded } = this.state;
    return (
      <div>
        <MenuAppBar username="ROSTEM" />
        <Box className={classes.box} p={1} m={1}>
          <div className={classes.root}>
            <Paper className={classes.search} elevation={1}>
              <IconButton className={classes.iconButton} aria-label="Search">
                <SearchIcon />
              </IconButton>
              <InputBase
                onCclassName={classes.input}
                placeholder="Search tutorial..."
                onChange={this.onSearchChange.bind(this)}
              />
            </Paper>

            <Paper square className={classes.categoryTitle} elevation={1}>
              <center>
                <br />
                <Typography variant="h4">
                  {this.props.match.params.categoryName}
                </Typography>
                <br />
              </center>
            </Paper>
            <div className={classes.tutorials}>
              <Grid item>
                {this.state.shownTutorials.length > 0 ? (
                  <div>
                    <List dense={false} disablePadding>
                      {this.state.shownTutorials.map(tutorial => (
                        <SimpleTutorial
                          userEmail={this.state.userEmail}
                          tutorial={tutorial}
                          clickChapter={this.handleChapterClick.bind(this)}
                        />
                      ))}
                    </List>
                  </div>
                ) : (
                  <Paper square>
                    <center>
                      <br />
                      <br />
                      <Typography variant="h5">No tutorials yet..</Typography>
                      <br />
                      <br />
                    </center>
                  </Paper>
                )}
              </Grid>
            </div>
          </div>
          {this.state.showChapter ? (
            <ChapterView
              userEmail={this.state.userEmail}
              chapter={this.state.selectedChapter}
            />
          ) : (
            <EmptyChapterView />
          )}
        </Box>
      </div>
    );
  }
}

export default withRouter(withStyles(styles)(TutorialsView));
