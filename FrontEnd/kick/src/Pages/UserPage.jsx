import React, { Component } from "react";

class UserPage extends Component {
  state = {};
  render() {
    return (
      <div>
        <h1>{this.props.match.params.username}</h1>
      </div>
    );
  }
}

export default UserPage;
