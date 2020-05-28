import React, { Component } from "react";
import API from "../api/api";
import Post from "./Post";

class Feed extends Component {
  render() {
    return (
      <div>
        {this.props.feed.map((post, index) => (
          <Post id={post.id} post={post} liked={this.props.liked[index]}></Post>
        ))}
      </div>
    );
  }
}

export default Feed;
