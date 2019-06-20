import React from "react";
import { lighten, withStyles } from "@material-ui/core/styles";
import Box from "@material-ui/core/Box";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import * as constants from "../../constants/constants";
import LinearProgress from "@material-ui/core/LinearProgress";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import WorkIcon from "@material-ui/icons/Work";
import ListItemText from "@material-ui/core/ListItemText";

const BorderLinearProgress = withStyles({
  root: {
    height: 10,
    backgroundColor: lighten("#ff6c5c", 0.5)
  },
  bar: {
    borderRadius: 20,
    backgroundColor: "#ff6c5c"
  }
})(LinearProgress);

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

class TutorialProgress extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      tutorials: [],
      categoryId: this.props.categoryId
    };
  }

  async getTutorialsProgress() {
    await constants.axiosRequest
      .post(constants.BASE_URL + "/tutorials/progress", {
        email: this.props.userEmail,
        id: this.state.categoryId
      })
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting tutorials");
        } else {
          this.setState({
            tutorials: res.object.objects
          });
        }
      });
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      chapterId: nextProps.categoryId
    });
    this.getTutorialsProgress();
  }

  componentDidMount() {
    this.getTutorialsProgress();
  }

  render() {
    const { classes } = this.props;
    return (
      <div className={classes.root}>
        <Box bgcolor="primary.main" className={classes.wrapperBox} p={1} m={1}>
          <List dense={false} disablePadding>
            {this.state.tutorials.map(tutorial => (
              <div>
                <ListItem>
                  <ListItemIcon>
                    <WorkIcon />
                  </ListItemIcon>
                  <ListItemText
                    primary={
                      tutorial.name + " - " + tutorial.percentage + "% done."
                    }
                    secondary={
                      <BorderLinearProgress
                        className={classes.margin}
                        variant="determinate"
                        color="secondary"
                        value={tutorial.percentage}
                      />
                    }
                  />
                </ListItem>
              </div>
            ))}
          </List>
        </Box>
      </div>
    );
  }
}

export default withStyles(styles)(TutorialProgress);
