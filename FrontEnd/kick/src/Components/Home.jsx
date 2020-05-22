import React, { Component } from "react";
import { Container, Row, Col } from "react-bootstrap";

import Feed from "./Feed";
import Dashboard from "./Dashboard";

class Home extends Component {
  state = {};

  render() {
    return (
      <div>
        <Container fluid>
          <Row>
            <Col lg="3"></Col>
            <Col lg="6">
              <Feed />
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
