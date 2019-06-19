import React from "react";
import Paper from "@material-ui/core/Paper";
import { withStyles } from "@material-ui/core/styles";
import { Typography } from "@material-ui/core";
import Divider from "@material-ui/core/Divider";
import ListItemText from "@material-ui/core/ListItemText";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import * as constants from "../../constants/constants.js";
import Box from "@material-ui/core/Box";
import Button from "@material-ui/core/Button";
import { withRouter } from "react-router-dom";

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

class UserFavoriteCategories extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      favoriteCategories: []
    };
  }

  async getFavoriteCategories() {
    const email = this.props.userEmail;
    await constants.axiosRequest
      .get(constants.BASE_URL + "/categories/favorites/" + email)
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting favorites categories");
        } else {
          this.setState({
            favoriteCategories: res.object.objects
          });
        }
      });
  }

  componentDidMount() {
    this.getFavoriteCategories();
  }

  navigateToCategory(category) {
    this.props.history.push("categories/" + category);
  }

  render() {
    const { classes } = this.props;
    return (
      <Box bgcolor="primary.main" className={classes.root}>
        <center>
          <Typography variant="h6">Favorite Categories</Typography>
        </center>
        <Divider />
        <div>
          <List dense={false}>
            {this.state.favoriteCategories.length > 0 ? (
              this.state.favoriteCategories.map(category => {
                return (
                  <ListItem className={classes.details} divider>
                    <ListItemText
                      primary={category.name}
                      secondary={category.description}
                    />
                    <Button
                      variant="contained"
                      color="secondary"
                      className={classes.button}
                      onClick={() => {
                        console.log(category.name);
                        this.navigateToCategory(category.name);
                      }}
                    >
                      NAVIGATE
                    </Button>
                  </ListItem>
                );
              })
            ) : (
              <div className={classes.empty}>
                <center>
                  <Typography variant="h4">
                    No FAVORITE categories yet!
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

export default withRouter(withStyles(styles)(UserFavoriteCategories));
