import React from "react";
import Typography from "@material-ui/core/Typography";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Box from "@material-ui/core/Box";
import FormHelperText from "@material-ui/core/FormHelperText";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import FormControl from "@material-ui/core/FormControl";
import FormLabel from "@material-ui/core/FormLabel";
import Radio from "@material-ui/core/Radio";
import RadioGroup from "@material-ui/core/RadioGroup";
import { Divider } from "@material-ui/core";
import CategoryStatistics from "./CategoryStatistics";
import UserStatistics from "./UserStatistics";
import ChapterStatistics from "./ChapterStatistics";

const styles = theme => ({
  root: {
    padding: 15
  },
  wrapperBox: {
    display: "flex",
    flexWrap: "wrap",
    justifyContent: "flex-start",
    overflow: "hidden",
    listStyle: "none"
  },
  radioGrup: {
    marginTop: 75,
    marginLeft: 25,
    marginRight: 25,
    width: 400,
    height: 400
  },
  formControl: {
    marginTop: 25,
    justifyContent: "center"
  },
  group: {
    marginLeft: 15
  }
});

class AdminStatistics extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: "user"
    };
  }

  handleChange(event) {
    this.setState({
      value: event.target.value
    });
  }

  render() {
    const { classes } = this.props;
    return (
      <div className={classes.root}>
        <Box className={classes.wrapperBox} p={1} m={1}>
          <Box className={classes.radioGrup} bgcolor="primary.main">
            <center className={classes.formControl}>
              <FormLabel component="legend">SELECT STATISTICS</FormLabel>
              <br />
              <Divider />
              <br />
              <RadioGroup
                aria-label="position"
                name="position"
                onChange={this.handleChange.bind(this)}
                value={this.state.value}
                className={classes.group}
              >
                <FormControlLabel
                  value="user"
                  control={<Radio />}
                  label="Top Users"
                  labelPlacement="top"
                />
                <br />
                <FormControlLabel
                  value="category"
                  control={<Radio />}
                  label="Top Categories"
                  labelPlacement="top"
                />
                <br />
                <FormControlLabel
                  value="chapter"
                  control={<Radio />}
                  label="Top Chapters"
                  labelPlacement="top"
                />
              </RadioGroup>
            </center>
          </Box>
          <div>
            {this.state.value === "user" && <UserStatistics />}
            {this.state.value === "category" && <CategoryStatistics />}
            {this.state.value === "chapter" && <ChapterStatistics />}
          </div>
        </Box>
      </div>
    );
  }
}

export default withStyles(styles)(AdminStatistics);
