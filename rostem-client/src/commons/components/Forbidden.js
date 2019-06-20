import React from "react";
import {Typography} from "@material-ui/core";
import {withRouter} from "react-router";

function Forbidden(props) {
  return (
    <center>
      <Typography variant="h1">FORBIDDEN</Typography>
    </center>
  );
}

export default withRouter(Forbidden);
