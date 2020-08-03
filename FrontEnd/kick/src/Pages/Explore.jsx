import React, { Component } from "react";

import Page from "../Components/Page";
import Feed from "../Components/Feed";

class Explore extends Component {
  state = {};
  render() {
    return (
      <Page
        middleComponent={
          <React.Fragment>
            <h1>Explore</h1>
            <Feed feedURL="/api/posts/explore?" />
          </React.Fragment>
        }
      />
    );
  }
}

export default Explore;
