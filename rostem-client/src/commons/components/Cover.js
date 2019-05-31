import React from "react";
import Grid from "@material-ui/core/Grid";
import { Typography } from "@material-ui/core";

const gridStyle = {
  position: "absolute",
  left: "50%",
  top: "50%",
  transform: "translate(-50%, -50%)"
};
const style = {
  fontWeight: "fontWeightMedium",
  textAlign: "center"
};

function Presentation(props) {
  return (
    <Grid
      container
      spacing={0}
      alignItems="center"
      direction="column"
      justify="center"
      style={gridStyle}
    >
      <Grid item xs={8}>
        <Typography style={style} variant="h1" color="primary">
          LEARN ANYTHING.
        </Typography>
        <Typography style={style} variant="h4" color="primary">
          Free e-learning platform.
        </Typography>
      </Grid>
    </Grid>
  );
}

export default Presentation;
