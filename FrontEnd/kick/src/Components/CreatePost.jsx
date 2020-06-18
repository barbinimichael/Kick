import React, { Component } from "react";
import { Container, Row, Col, Form, Button } from "react-bootstrap";
import Image from "react-bootstrap/Image";

class CreatePost extends Component {
  state = { image: "" };

  handleFileSubmit = (event) => {
    event.preventDefault();
    console.log("files", event.target.files);
    this.setState({ image: URL.createObjectURL(event.target.files[0]) });
  };

  render() {
    return (
      <div>
        <Container fluid>
          <Row>
            <Col lg="3"></Col>
            <Col lg="6">
              <Form>
                <Col>
                  <Row xs="6" md="3" lg="1">
                    <Form.Group controlId="formImage">
                      <Image src={this.state.image} roundedCircle fluid />
                    </Form.Group>
                  </Row>
                  <Row>
                    <Form.Group controlId="formFile">
                      <Form.File
                        id="formFileImageUpload"
                        label="Upload a local image"
                        onChange={this.handleFileSubmit}
                        type="file"
                        accept=".jpg, .png, .jpeg"
                      />
                    </Form.Group>
                  </Row>
                  <Row>
                    <Form.Group controlId="formCaption">
                      <Form.Label>Caption</Form.Label>
                      <Form.Control
                        type="caption"
                        placeholder="Enter a caption"
                      />
                    </Form.Group>
                  </Row>
                  <Row>
                    <Form.Group controlId="formLocation">
                      <Form.Label>Location</Form.Label>
                      <Row>
                        <Col>
                          <Form.Control placeholder="City" />
                        </Col>
                        <Col>
                          <Form.Control placeholder="Country" />
                        </Col>
                      </Row>
                    </Form.Group>
                  </Row>
                  <Row>
                    <Button variant="primary" type="submit">
                      Post
                    </Button>
                  </Row>
                </Col>
              </Form>
            </Col>
            <Col lg="3"></Col>
          </Row>
        </Container>
      </div>
    );
  }
}

export default CreatePost;
