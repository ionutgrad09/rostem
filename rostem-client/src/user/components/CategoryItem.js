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
import { CardActions } from "@material-ui/core";
import FavoriteIcon from "@material-ui/icons/Favorite";
import IconButton from "@material-ui/core/IconButton";
import axios from "axios";
import * as rostemConstants from "../../constants/constants.js";

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
    this.state = {
      isMarkedAsFavorite: this.props.isMarkedAsFavorite
    };
  }

  goToCategory() {
    this.props.history.push("/categories/" + this.props.categoryName);
  }

  async handleAddToFavorite() {
    const email = sessionStorage.getItem(rostemConstants.EMAIL);
    const { id } = this.props;
    await axios
      .post(rostemConstants.BASE_URL + "/categories/favorites", {
        id: id,
        email: email
      })
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error unmarking chapters");
        } else {
          this.setState({
            isMarkedAsFavorite: !this.state.isMarkedAsFavorite
          });
        }
      });
  }

  async handleDeleteFavorite() {
    const email = sessionStorage.getItem(rostemConstants.EMAIL);
    const { id } = this.props;
    await axios
      .delete(rostemConstants.BASE_URL + "/categories/favorites", {
        data: {
          id: id,
          email: email
        }
      })
      .then(result => {
        let res = result.data;
        if (res.status === "false") {
          console.log("Error deleting favorite category");
        } else {
          this.setState({
            isMarkedAsFavorite: !this.state.isMarkedAsFavorite
          });
        }
      });
  }

  handleFavoriteAction() {
    this.state.isMarkedAsFavorite
      ? this.handleDeleteFavorite()
      : this.handleAddToFavorite();
  }

  render() {
    console.log(this.state);
    const { id, description, categoryName, classes } = this.props;
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
        <CardActions disableSpacing>
          {this.state.isMarkedAsFavorite === true ? (
            <IconButton
              aria-label="Add to favorites"
              onClick={this.handleFavoriteAction.bind(this)}
              color="secondary"
            >
              <FavoriteIcon />
            </IconButton>
          ) : (
            <IconButton
              aria-label="Add to favorites"
              onClick={this.handleFavoriteAction.bind(this)}
            >
              <FavoriteIcon />
            </IconButton>
          )}
        </CardActions>
      </Card>
    );
  }
}

CategoryItem.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withRouter(withStyles(styles)(CategoryItem));
