import React from "react";
import { withStyles } from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import { withRouter } from "react-router-dom";
import * as constants from "../../constants/constants.js";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import NavigateNextIcon from "@material-ui/icons/NavigateNext";
import PriorityHighRounded from "@material-ui/icons/PriorityHighRounded";
import Tooltip from "@material-ui/core/Tooltip";
import CheckBoxRounded from "@material-ui/icons/CheckBoxRounded";
import ListSubheader from "@material-ui/core/ListSubheader";
import ListItemText from "@material-ui/core/ListItemText";
import Collapse from "@material-ui/core/Collapse";
import InboxIcon from "@material-ui/icons/MoveToInbox";
import DraftsIcon from "@material-ui/icons/Drafts";
import SendIcon from "@material-ui/icons/Send";
import ExpandLess from "@material-ui/icons/ExpandLess";
import ExpandMore from "@material-ui/icons/ExpandMore";
import StarBorder from "@material-ui/icons/StarBorder";
import WorkIcon from "@material-ui/icons/Work";

const styles = theme => ({
  root: {
    width: "100%",
    maxWidth: 500,
    backgroundColor: theme.palette.background.paper
  },
  nested: {
    paddingLeft: theme.spacing(4)
  },
  box: {
    display: "flex",
    flexWrap: "wrap",
    justifyContent: "flex-start",
    overflow: "hidden"
  },
  tutorials: {
    marginTop: 25
  },
  details: {
    display: "inline-flex"
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

class SimpleTutorial extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      chapters: [],
      open: false
    };
  }

  async getAllChapters() {
    await constants.axiosRequest
      .post(constants.BASE_URL + "/chapters/action", {
        email: this.props.userEmail,
        tutorialId: this.props.tutorial.id
      })
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

  componentDidMount() {
    this.getAllChapters();
  }

  handleClick(e) {
    this.getAllChapters();
    this.setState({
      open: !this.state.open
    });
  }

  render() {
    const { classes } = this.props;
    return (
      <div className={classes.root}>
        <ListItem button onClick={this.handleClick.bind(this)}>
          <ListItemIcon>
            <WorkIcon />
          </ListItemIcon>
          <ListItemText
            primary={this.props.tutorial.name}
            secondary={this.props.tutorial.description}
          />
          {this.state.open ? <ExpandLess /> : <ExpandMore />}
        </ListItem>
        <Collapse in={this.state.open} timeout="auto" unmountOnExit>
          <List componenet="div" dense={true}>
            {this.state.chapters.length > 0 ? (
              this.state.chapters.map(chapter => {
                return (
                  <ListItem
                    className={classes.details}
                    button
                    divider
                    onClick={e => this.props.clickChapter(chapter)}
                  >
                    {chapter.todo && (
                      <Tooltip title="TODO" aria-label="TODO">
                        <PriorityHighRounded color="secondary" />
                      </Tooltip>
                    )}
                    {chapter.done && (
                      <Tooltip title="DONE" aria-label="DONE">
                        <CheckBoxRounded color="secondary" />
                      </Tooltip>
                    )}
                    <ListItemText
                      primary={chapter.name}
                      style={{ marginLeft: 15 }}
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
        </Collapse>
      </div>
    );
  }
}

export default withRouter(withStyles(styles)(SimpleTutorial));
