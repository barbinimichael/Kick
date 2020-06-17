import React, { Component } from "react";
import API from "../api/api";
import Post from "./Post";

class Feed extends Component {
  render() {
    this.props.feed.map((post, index) => console.log(this.props.liked[index]));
    return (
      <div>
        {this.props.feed.map((post, index) => (
          <Post
            key={post.id}
            id={post.id}
            post={post}
            liked={this.props.liked[index]}
            handleUserCommented={this.props.handleUserCommented}
            handleUserLiked={this.props.handleUserLiked}
            handleUserUnLiked={this.props.handleUserUnLiked}
          ></Post>
        ))}
      </div>
    );
  }
}

export default Feed;
