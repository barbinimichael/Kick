import React, { Component } from "react";

import Post from "./Post";

class Feed extends Component {
  state = {
    users: [
      { id: 1, username: "mbarbzzz" },
      { id: 2, username: "bob" },
      { id: 3, username: "will" },
      { id: 4, username: "jon" },
    ],
  };
  render() {
    return (
      <div>
        {this.state.users.map((user) => (
          <Post key={user.id} user={user}></Post>
        ))}
      </div>
    );
  }
}

export default Feed;
