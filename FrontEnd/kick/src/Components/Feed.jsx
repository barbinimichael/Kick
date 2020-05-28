import React, { Component } from "react";

import Post from "./Post";

class Feed extends Component {
  render() {
    return (
      <div>
        {this.props.feed.map((post) => (
          <Post key={post.id} post={post}></Post>
        ))}
      </div>
    );
  }
}

export default Feed;
