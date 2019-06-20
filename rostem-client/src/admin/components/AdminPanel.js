import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import AdminUsers from "./users/AdminUsers";
import AdminCategories from "./posts/AdminCategories";
import AdminChapters from "./posts/AdminChapters";
import AdminTutorials from "./posts/AdminTutorials";
import AdminStatistics from "./statistics/AdminStatistics";
import MenuAppBar from "../../commons/components/MenuHeader";

const styles = {
  root: {
    flexGrow: 1
  }
};

class AdminPanel extends React.Component {
  state = {
    value: 0
  };

  handleChange = (event, value) => {
    this.setState({ value });
  };

  render() {
    const { classes } = this.props;

    return (
      <div>
        <MenuAppBar username="ADMINISTRATOR" />
        <Paper square className={classes.root}>
          <Tabs
            value={this.state.value}
            onChange={this.handleChange}
            indicatorColor="secondary"
            textColor="secondary"
            centered
          >
            <Tab label="Users" />
            <Tab label="Categories" />
            <Tab label="Tutorials" />
            <Tab label="Chapters" />
            <Tab label="Statistics" />
          </Tabs>
        </Paper>
        {this.state.value === 3 && <AdminUsers />}
        {this.state.value === 1 && <AdminCategories />}
        {this.state.value === 2 && <AdminTutorials />}
        {this.state.value === 0 && <AdminChapters />}
        {this.state.value === 4 && <AdminStatistics />}
      </div>
    );
  }
}

AdminPanel.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(styles)(AdminPanel);
