import React, { Component } from "react";

import Page from "../Components/Page";
import Post from "../Components/Post";
import API from "../api/api";

class PostPage extends Component {
  state = { post: [], liked: false };

  componentDidMount() {
    this.getPost();
  }

  componentDidUpdate(props) {
    if (this.props !== props) {
      this.getPost();
    }
  }

  getPost = () => {
    API({
      method: "get",
      url: `/api/posts/${this.props.match.params.postId}`,
    })
      .then((response) => {
        this.setState({ post: response.data });

        API({
          method: "get",
          url: `api/posts/${response.data.id}/liked`,
        }).then((liked) => {
          this.setState({ liked: liked.data });
        });
      })
      .catch((error) => {
        console.log(error);
      });
  };

  handleUserLiked = (postId) => {
    API({
      method: "post",
      url: `api/posts/${postId}/likePosts`,
    }).then((response) => {
      this.setState({ post: response.data });
      this.setState({ liked: true });
    });
  };

  handleUserUnLiked = (postId) => {
    API({
      method: "delete",
      url: `api/posts/${postId}/likePosts`,
    }).then((response) => {
      this.setState({ post: response.data });
      this.setState({ liked: false });
    });
  };

  handleUserCommented = (postId, comment) => {
    API({
      method: "post",
      url: `api/posts/${postId}/commentPosts`,
      data: comment,
    }).then((response) => {
      this.setState({ post: response.data });
    });
  };

  render() {
    console.log("Post page", this.state.post);
    console.log("Post page likes", this.state.liked);

    let post = this.state.post;
    let liked = this.state.liked;

    return this.state.post.length !== 0 ? (
      <Page
        middleComponent={
          <Post
            key={post.id}
            id={post.id}
            post={post}
            liked={liked}
            handleUserCommented={this.handleUserCommented}
            handleUserLiked={this.handleUserLiked}
            handleUserUnLiked={this.handleUserUnLiked}
          />
        }
      />
    ) : (
      <React.Fragment></React.Fragment>
    );
  }
}

export default PostPage;
