import React, { Component } from "react";
import { Container, Row, Col } from "react-bootstrap";

import Feed from "../Components/Feed";
import Dashboard from "../Components/Dashboard";

class Home extends Component {
  render() {
    return (
      <div>
        <Container fluid>
          <Row>
            <Col lg="3"></Col>
            <Col lg="6">
              <Feed feedURL="api/posts/feed?" />
            </Col>
            <Col lg="3" className="Sidebar sticky-top">
              <Dashboard meUser={this.props.meUser} />
            </Col>
          </Row>
        </Container>
      </div>
    );
  }
}

export default Home;
