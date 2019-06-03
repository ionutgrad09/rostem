import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import Typography from "@material-ui/core/Typography";
import image from "../../resources/stemlogo.jpg";
import { withRouter } from "react-router-dom";
const styles = {
  card: {
    maxWidth: 345,
    width: 250,
    marginTop: 15,
    marginLeft: 15
  },
  media: {
    height: 140
  }
};

class CategoryItem extends React.Component {
  constructor(props) {
    super(props);
    this.goToCategory = this.goToCategory.bind(this);
  }

  goToCategory() {
    this.props.history.push("/categories/" + this.props.categoryName);
  }

  render() {
    const { description, categoryName, classes } = this.props;
    return (
      <Card className={classes.card}>
        <CardActionArea onClick={this.goToCategory.bind(this)}>
          <CardMedia
            className={classes.media}
            image={image}
            title={categoryName}
          />
          <CardContent>
            <Typography gutterBottom variant="h5" component="h2">
              {categoryName}
            </Typography>
            <Typography component="p">{description}</Typography>
          </CardContent>
        </CardActionArea>
      </Card>
    );
  }
}

CategoryItem.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withRouter(withStyles(styles)(CategoryItem));
