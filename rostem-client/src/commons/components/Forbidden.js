import React from "react";
import { Typography } from "@material-ui/core";
import { withRouter } from "react-router";
import { Editor } from "react-draft-wysiwyg";
import ReactDOM from "react-dom";
import TextField from "@material-ui/core/TextField";
import Paper from "@material-ui/core/Paper";
import "react-draft-wysiwyg/dist/react-draft-wysiwyg.css";
import { EditorState, convertFromRaw, convertToRaw } from "draft-js";
import draftToHtml from "draftjs-to-html";

class Forbidden extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
      <center>
        <Paper>
          <h1>404 NOT FOUND</h1>
        </Paper>
      </center>
    );
  }
}

export default withRouter(Forbidden);
