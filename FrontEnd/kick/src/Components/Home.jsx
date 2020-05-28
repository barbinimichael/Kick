import React, { Component } from "react";
import { Container, Row, Col } from "react-bootstrap";
import API from "../api/api";

import Feed from "./Feed";
import Dashboard from "./Dashboard";

class Home extends Component {
  state = { user: "", feed: [] };

  componentDidMount() {
    let username = "";

    API({
      method: "get",
      url: "api/applicationUsers/self",
    })
      .then((response) => {
        console.log(response);
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
        console.log(response);
        const feed = response.data.content;

        if (feed.length > 0) {
          this.setState({ feed });
        }
      })
      .catch((error) => {
        console.log(error);
      });
  }

  render() {
    return (
      <div>
        <Container fluid>
          <Row>
            <Col lg="3"></Col>
            <Col lg="6">
              <Feed feed={this.state.feed} />
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
