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
        let requests = feed.map((post) => this.handleLiked(post));
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

  handleLiked = (post) => {
    return API({
      method: "get",
      url: `api/posts/${post.id}/liked`,
    });
  };

  render() {
    console.log(this.state);
    return (
      <div>
        <Container fluid>
          <Row>
            <Col lg="3"></Col>
            <Col lg="6">
              <Feed feed={this.state.feed} liked={this.state.liked} />
            </Col>
            <Col lg="3" className="Sidebar sticky-top">
              <Dashboard />
            </Col>
          </Row>
          >
        </Container>
      </div>
    );
  }
}

export default Home;
