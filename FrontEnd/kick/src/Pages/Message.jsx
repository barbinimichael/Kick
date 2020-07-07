import React, { Component } from "react";

import Page from "../Components/Page";

class Message extends Component {
  state = {};
  render() {
    return <Page middleComponent={<h1>Message</h1>} />;
  }
}

export default Message;
