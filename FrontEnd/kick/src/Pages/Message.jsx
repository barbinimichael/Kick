import React, { Component } from "react";

import Page from "../Components/Page";
import UnderConstruction from "../Components/UnderConstruction";

class Message extends Component {
  state = {};
  render() {
    return (
      <Page
        middleComponent={
          <React.Fragment>
            <h1>Message</h1>
            <div className="center">
              <UnderConstruction description="Messages"></UnderConstruction>
            </div>
          </React.Fragment>
        }
      />
    );
  }
}

export default Message;
