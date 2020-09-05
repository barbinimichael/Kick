import React, { Component } from "react";

import { Container, Row, Col } from "react-bootstrap";

class Page extends Component {
  render() {
    return (
      <div>
        <Container fluid>
          <Row>
            <Col lg="3" className="Sidebar sticky-top"></Col>
            {this.props.leftComponent ? (
              this.props.leftComponent
            ) : (
              <React.Fragment />
            )}
            <Col lg="6">
              {this.props.middleComponent ? (
                this.props.middleComponent
              ) : (
                <React.Fragment />
              )}
            </Col>
            <Col lg="3" className="Sidebar sticky-top">
              {this.props.rightComponent ? (
                this.props.rightComponent
              ) : (
                <React.Fragment />
              )}
            </Col>
          </Row>
        </Container>
      </div>
    );
  }
}

export default Page;
