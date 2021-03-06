import React, { Component } from "react";
import { Spinner } from "react-bootstrap";

import Post from "./Post";
import API from "../api/api";

class Feed extends Component {
  state = {
    feed: [],
    liked: [],
    privateProfile: false,
    page: 0,
    totalPages: 1,
  };

  componentDidMount() {
    document.addEventListener("scroll", this.handleScroll);
    this.createFeed();
  }

  componentDidUpdate(props) {
    if (this.props.feedURL !== props.feedURL) {
      this.setState({ feed: [], page: 0, totalPages: 1 }, this.createFeed);
    }
  }

  handleScroll = (e) => {
    if (
      document.getElementById("feed").getBoundingClientRect().bottom <=
        window.innerHeight + 1 &&
      this.state.feed.length > 0 &&
      this.state.totalPages > 1
    ) {
      this.createFeed();
    }
  };

  componentWillUnmount() {
    document.removeEventListener("scroll", this.handleScroll);
  }

  createFeed = () => {
    console.log("Feed state", this.state);

    // check that not requesting if no more pages
    if (this.state.page >= this.state.totalPages) {
      return;
    }

    API({
      method: "get",
      url: this.props.feedURL + `page=${this.state.page}&size=10`,
    })
      .then((response) => {
        let feed = response.data.content;
        this.setState({ feed: this.state.feed.concat(feed) });
        this.setState({ totalPages: response.data.totalPages });
        this.setState({ page: this.state.page + 1 });

        if (response.data === "Not following or public") {
          this.setState({ noPost: true });
        }

        return feed;
      })
      .then((feed) => {
        let requests = feed.map((post) => this.handleWhichUserLiked(post));
        Promise.all(requests).then((values) => {
          let liked = values.map((like) => like.data);
          this.setState({ liked: this.state.liked.concat(liked) });
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
        newIndex = index;
        return newPost;
      } else {
        return post;
      }
    });

    if (isLiked !== "comment") {
      liked[newIndex] = isLiked;
    }

    this.setState({ feed: modified, liked });
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
      <div onScroll={this.handleScroll} id="feed">
        {this.state.feed && this.state.feed[0] !== undefined ? (
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
        {this.state.page < this.state.totalPages ? (
          <div className="center">
            <Spinner animation="border" variant="primary" />
            <Spinner animation="border" variant="secondary" />
            <Spinner animation="border" variant="success" />
            <Spinner animation="border" variant="danger" />
            <Spinner animation="border" variant="warning" />
            <Spinner animation="border" variant="info" />
            <Spinner animation="border" variant="light" />
            <Spinner animation="border" variant="dark" />
          </div>
        ) : (
          <React.Fragment></React.Fragment>
        )}
      </div>
    );
  }
}

export default Feed;
