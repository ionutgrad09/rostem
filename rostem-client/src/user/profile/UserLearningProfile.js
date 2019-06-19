import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Box from "@material-ui/core/Box";
import UserTodoChapters from "./UserTodoChapters";
import UserDoneChapters from "./UserDoneChapters";
import UserFavoriteCategories from "./UserFavoriteCategories";

const styles = theme => ({
  root: {
    padding: 15
  },
  wrapperBox: {
    display: "flex",
    flexWrap: "wrap",
    justifyContent: "space-evenly",
    overflow: "hidden",
    listStyle: "none"
  }
});

class UserLearningProfile extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { classes } = this.props;
    return (
      <div className={classes.root}>
        <Box className={classes.wrapperBox} p={1} m={1}>
          <UserFavoriteCategories userEmail={this.props.userEmail} />
          <UserDoneChapters userEmail={this.props.userEmail} />
          <UserTodoChapters userEmail={this.props.userEmail} />
        </Box>
      </div>
    );
  }
}

UserLearningProfile.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(styles)(UserLearningProfile);
