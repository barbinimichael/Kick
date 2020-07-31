import React, { Component } from "react";
import Post from "./Post";
import API from "../api/api";

class Feed extends Component {
  state = { feed: [], liked: [], privateProfile: false };

  componentDidUpdate(props) {
    if (this.props !== props) {
      this.createFeed();
    }
  }

  createFeed = () => {
    API({
      method: "get",
      url: this.props.feedURL,
    })
      .then((response) => {
        let feed = response.data.content;
        this.setState({ feed });

        if (response.data === "Not following or public") {
          this.setState({ noPost: true });
        }

        return feed;
      })
      .then((feed) => {
        let requests = feed.map((post) => this.handleWhichUserLiked(post));
        Promise.all(requests).then((values) => {
          let liked = values.map((like) => like.data);
          this.setState({ liked });
        });
      })
      .catch((error) => {
        console.log(error);
      });
  };

  handleWhichUserLiked = (post) => {
    return API({
      method: "get",
      url: `api/posts/${post.id}/liked`,
    });
  };

  handlePostUpdated = (newPost, postId, isLiked) => {
    // replace the corresponding post with the new one
    let feed = this.state.feed;
    let liked = this.state.liked;
    let newIndex = 0;

    let modified = feed.map((post, index) => {
      if (post.id === postId) {
        console.log("mathicng postId", postId);
        console.log("matching index", index);
        newIndex = index;
        return newPost;
      } else {
        return post;
      }
    });

    if (isLiked !== "comment") {
      liked[newIndex] = isLiked;
    }

    this.setState({ feed: modified });
    this.setState({ liked });
  };

  handleUserLiked = (postId) => {
    API({
      method: "post",
      url: `api/posts/${postId}/likePosts`,
    }).then((response) => {
      this.handlePostUpdated(response.data, postId, true);
    });
  };

  handleUserUnLiked = (postId) => {
    API({
      method: "delete",
      url: `api/posts/${postId}/likePosts`,
    }).then((response) => {
      this.handlePostUpdated(response.data, postId, false);
    });
  };

  handleUserCommented = (postId, comment) => {
    API({
      method: "post",
      url: `api/posts/${postId}/commentPosts`,
      data: comment,
    }).then((response) => {
      // replace the corresponding post with the new one
      this.handlePostUpdated(response.data, postId, "comment");
    });
  };

  handleUserDeleted = (postId) => {
    API({
      method: "delete",
      url: `api/posts/${postId}`,
    }).then(() => {
      window.location.reload(true);
    });
  };

  render() {
    return (
      <div>
        {this.state.feed && this.state.feed.length > 0 ? (
          this.state.feed.map((post, index) => (
            <Post
              key={post.id}
              id={post.id}
              post={post}
              liked={this.state.liked[index]}
              handleUserCommented={this.handleUserCommented}
              handleUserLiked={this.handleUserLiked}
              handleUserUnLiked={this.handleUserUnLiked}
              handleUserDeleted={this.handleUserDeleted}
              myPost={this.props.myPosts}
            ></Post>
          ))
        ) : this.state.feed ? (
          <h1 className="center">No Posts</h1>
        ) : (
          <h1 className="center">Private profile</h1>
        )}
      </div>
    );
  }
}

export default Feed;
