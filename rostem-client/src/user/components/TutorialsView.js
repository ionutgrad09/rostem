import React from "react";
import Typography from "@material-ui/core/Typography";
import MenuAppBar from "../../commons/components/MenuHeader";
import ListItemAvatar from "@material-ui/core/ListItemAvatar";
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import ListItemText from "@material-ui/core/ListItemText";
import IconButton from "@material-ui/core/IconButton";
import Grid from "@material-ui/core/Grid";
import { withStyles } from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import { withRouter } from "react-router-dom";
import axios from "axios";
import * as rostemConstants from "../../constants/constants.js";
import Paper from "@material-ui/core/Paper";
import InputBase from "@material-ui/core/InputBase";
import SearchIcon from "@material-ui/icons/Search";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import InboxIcon from "@material-ui/icons/MoveToInbox";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import StarBorder from "@material-ui/icons/StarBorder";
import Divider from "@material-ui/core/Divider";
import { unstable_Box as Box } from "@material-ui/core/Box";
import NavigateNextIcon from "@material-ui/icons/NavigateNext";
import EmptyChapterView from "./EmptyChapterView";
import ChapterView from "./ChapterView";

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
    maxWidth: 400
  },
  tutorials: {
    marginTop: 25
  },
  details: {
    display: "inline-flex"
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
      selectedChapterName: "",
      selectedChapterId: "",
      selectedChapterDescription: "",
      selectedChapterUrl: "",
      showChapter: false
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

  async getAllTutorials() {
    const id = this.props.match.params.id;
    await axios
      .get(rostemConstants.BASE_URL + "/tutorials/" + id)
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

  async getAllChapters(id) {
    await axios
      .get(rostemConstants.BASE_URL + "/chapters/" + id)
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting chapters");
        } else {
          this.setState({
            chapters: res.object.objects
          });
        }
      });
  }

  handleChapterClick(id) {
    const selectedChapter = this.state.chapters.find(function(chapter) {
      return chapter.id === id;
    });
    this.setState({
      showChapter: true,
      selectedChapterId: selectedChapter.id,
      selectedChapterName: selectedChapter.name,
      selectedChapterDescription: selectedChapter.description,
      selectedChapterUrl: selectedChapter.url
    });
  }

  handleChange = id => (event, expanded) => {
    this.getAllChapters(id);
    this.setState({
      expanded: expanded ? id : false
    });
  };

  componentDidMount() {
    this.getAllTutorials();
  }

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
            <div className={classes.tutorials}>
              <Grid item>
                <div>
                  <List dense={false}>
                    {this.state.shownTutorials.map(tutorial => (
                      <div>
                        <ExpansionPanel
                          expanded={expanded === tutorial.id}
                          onChange={this.handleChange(tutorial.id)}
                        >
                          <ExpansionPanelSummary
                            expandIcon={<ExpandMoreIcon />}
                          >
                            <Typography className={classes.heading}>
                              {tutorial.name}
                            </Typography>
                            <Typography className={classes.secondaryHeading}>
                              {tutorial.description}
                            </Typography>
                          </ExpansionPanelSummary>
                          <ExpansionPanelDetails>
                            <List dense={true}>
                              {this.state.chapters.length > 0 ? (
                                this.state.chapters.map(chapter => {
                                  return (
                                    <ListItem
                                      className={classes.details}
                                      button
                                      divider
                                      onClick={e =>
                                        this.handleChapterClick(chapter.id)
                                      }
                                    >
                                      <ListItemText
                                        primary={chapter.name}
                                        secondary={chapter.description}
                                      />
                                      <ListItemIcon button>
                                        <NavigateNextIcon />
                                      </ListItemIcon>
                                    </ListItem>
                                  );
                                })
                              ) : (
                                <div>No chapters for this tutorial!</div>
                              )}
                            </List>
                          </ExpansionPanelDetails>
                        </ExpansionPanel>
                        <Divider />
                      </div>
                    ))}
                  </List>
                </div>
              </Grid>
            </div>
          </div>
          {this.state.showChapter ? (
            <ChapterView
              name={this.state.selectedChapterName}
              description={this.state.selectedChapterDescription}
              url={this.state.selectedChapterUrl}
              id={this.state.selectedChapterId}
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