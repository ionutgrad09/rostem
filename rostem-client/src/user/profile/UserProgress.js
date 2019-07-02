import React from "react";
import { withStyles } from "@material-ui/core/styles";
import Box from "@material-ui/core/Box";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import FormLabel from "@material-ui/core/FormLabel";
import Radio from "@material-ui/core/Radio";
import RadioGroup from "@material-ui/core/RadioGroup";
import { Divider } from "@material-ui/core";
import * as constants from "../../constants/constants";
import TutorialProgress from "./TutorialProgress";

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
    marginTop: 25,
    marginLeft: 25,
    marginRight: 25,
    width: 400,
    height: 500,
    overflow: "auto"
  },
  formControl: {
    marginTop: 25
  },
  group: {
    marginLeft: 15
  }
});

class UserProgress extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: null,
      categories: [],
      tutorials: []
    };
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
            categories: res.object.objects
          });
        }
      });
  }

  componentDidMount() {
    this.getAllCategories();
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
              <FormLabel component="legend">Categories</FormLabel>
            </center>
            <br />
            <Divider />
            <br />

            <RadioGroup
              onChange={this.handleChange.bind(this)}
              value={this.state.value}
              className={classes.group}
            >
              {this.state.categories.map(category => (
                <FormControlLabel
                  value={category.name}
                  control={<Radio />}
                  label={category.name}
                  labelPlacement="end"
                />
              ))}
            </RadioGroup>
          </Box>
          <div>
            <TutorialProgress
              userEmail={this.props.userEmail}
              categoryName={this.state.value}
            />
          </div>
        </Box>
      </div>
    );
  }
}

export default withStyles(styles)(UserProgress);
