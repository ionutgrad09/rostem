import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import InputBase from "@material-ui/core/InputBase";
import IconButton from "@material-ui/core/IconButton";
import SearchIcon from "@material-ui/icons/Search";
import * as constants from "../../constants/constants.js";

import Box from "@material-ui/core/Box";
import CategoryItem from "../components/CategoryItem.js";
import RecentPosts from "./RecentPosts.js";
import SuggestionsPosts from "./SuggestionsPosts.js";
import { withRouter } from "react-router-dom";

const styles = theme => ({
  root: {
    padding: 15
  },
  categoriesRoot: {
    width: 900
  },
  wrapperBox: {
    display: "flex",
    flexWrap: "wrap",
    justifyContent: "flex-start",
    overflow: "hidden"
  },
  box: {
    display: "flex",
    flexWrap: "wrap",
    justifyContent: "flex-start",
    overflow: "hidden"
  },
  search: {
    marginLeft: 25,
    padding: "4px 4px 4px 4px",
    display: "flex",
    alignItems: "center",
    width: 780
  },
  input: {
    marginLeft: 8,
    flex: 1
  },
  utilitiesBoxes: {
    display: "flex",
    flexWrap: "wrap",
    justifyContent: "flex-start",
    overflow: "hidden"
  }
});

class CategoriesView extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      searchText: "",
      categories: [],
      shownCategories: []
    };
  }

  filterShownCategories(searchKey) {
    const filteredCategories = this.state.shownCategories.filter(function(
      category
    ) {
      return category.name.includes(searchKey);
    });

    this.setState({ shownCategories: filteredCategories });
  }

  onSearchChange(event, value) {
    const searchKey = event.target.value;
    this.setState({
      searchText: searchKey,
      shownCategories: this.state.categories
    });
    if (searchKey !== "") {
      this.filterShownCategories(searchKey);
    }
  }

  async getAllCategories() {
    await constants.axiosRequest
      .get(constants.BASE_URL + "/categories/" + this.props.userEmail)
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error getting categories");
        } else {
          this.setState({
            categories: res.object.objects,
            shownCategories: res.object.objects
          });
        }
      });
  }

  componentDidMount() {
    this.getAllCategories();
  }

  render() {
    const { classes } = this.props;
    return (
      <div className={classes.root}>
        <Box className={classes.wrapperBox} p={1} m={1}>
          <div className={classes.categoriesRoot}>
            <Paper className={classes.search} elevation={1}>
              <IconButton className={classes.iconButton} aria-label="Search">
                <SearchIcon />
              </IconButton>
              <InputBase
                onCclassName={classes.input}
                placeholder="Search category..."
                onChange={this.onSearchChange.bind(this)}
              />
            </Paper>
            <Box className={classes.box} p={1} m={1}>
              {this.state.shownCategories.map(category => (
                <CategoryItem
                  userEmail={this.props.userEmail}
                  id={category.id}
                  categoryName={category.name}
                  description={category.description}
                  isMarkedAsFavorite={category.markedAsFavorite}
                />
              ))}
            </Box>
          </div>
          <Box className={classes.utiliesBoxes}>
            <RecentPosts userEmail={this.props.userEmail} />
            <SuggestionsPosts userEmail={this.props.userEmail} />
          </Box>
        </Box>
      </div>
    );
  }
}

CategoriesView.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withRouter(withStyles(styles)(CategoriesView));
