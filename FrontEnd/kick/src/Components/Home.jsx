import React, { Component } from "react";
import { Container, Row, Col } from "react-bootstrap";
import API from "../api/api";

import Feed from "./Feed";
import Dashboard from "./Dashboard";

class Home extends Component {
  state = { user: [], feed: [], liked: [] };

  componentDidMount() {
    API({
      method: "get",
      url: "api/applicationUsers/self",
    })
      .then((response) => {
        this.setState({ user: response.data });
      })
      .catch((error) => {
        console.log(error);
      });

    API({
      method: "get",
      url: "api/posts/feed",
    })
      .then((response) => {
        let feed = response.data.content;
        this.setState({ feed });
        return feed;
      })
      .then((feed) => {
        let requests = feed.map((post) => this.handleWhichUserLiked(post));
        Promise.all(requests).then((values) => {
          console.log("REQUESTS:", values);
          let liked = values.map((like) => like.data);
          console.log("LIKED", liked);
          this.setState({ liked });
        });
      })
      .catch((error) => {
        console.log(error);
      });
  }

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

  render() {
    console.log("state", this.state);
    return (
      <div>
        <Container fluid>
          <Row>
            <Col lg="3"></Col>
            <Col lg="6">
              <Feed
                feed={this.state.feed}
                liked={this.state.liked}
                handleUserCommented={this.handleUserCommented}
                handleUserLiked={this.handleUserLiked}
                handleUserUnLiked={this.handleUserUnLiked}
              />
            </Col>
            <Col lg="3" className="Sidebar sticky-top">
              <Dashboard />
            </Col>
          </Row>
        </Container>
      </div>
    );
  }
}

export default Home;
