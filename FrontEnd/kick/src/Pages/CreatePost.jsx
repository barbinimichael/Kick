import React, { Component } from "react";
import { Container, Row, Col, Form, Button } from "react-bootstrap";
import Image from "react-bootstrap/Image";
import API from "../api/api";
import history from "../Components/History";

class CreatePost extends Component {
  state = { image: "", caption: "", city: "", country: "" };

  handleFileSubmit = (event) => {
    event.preventDefault();
    this.setState({ image: URL.createObjectURL(event.target.files[0]) });
  };

  onChange = (e) => this.setState({ [e.target.name]: e.target.value });

  onPostSubmit = (e) => {
    e.preventDefault();

    let data = {
      caption: this.state.caption,
      imageURL: "inDevelopment.com",
      city: this.state.city,
      country: this.state.country,
      postDate: new Date(),
    };

    API({
      method: "post",
      url: "api/posts",
      data: data,
    })
      .then((response) => {
        console.log("response", response);
        history.push("/");
        window.location.reload(true);
      })
      .catch((error) => {
        console.log(error);
        this.props.handleLogout();
      });
  };

  render() {
    let { caption, city, country } = this.state;

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
                        type="text"
                        placeholder="Enter a caption"
                        name="caption"
                        value={caption}
                        onChange={this.onChange}
                      />
                    </Form.Group>
                  </Row>
                  <Row>
                    <Form.Group controlId="formLocation">
                      <Form.Label>Location</Form.Label>
                      <Row>
                        <Col>
                          <Form.Control
                            type="text"
                            placeholder="City"
                            name="city"
                            value={city}
                            onChange={this.onChange}
                          />
                        </Col>
                        <Col>
                          <Form.Control
                            type="text"
                            placeholder="Country"
                            name="country"
                            value={country}
                            onChange={this.onChange}
                          />
                        </Col>
                      </Row>
                    </Form.Group>
                  </Row>
                  <Row>
                    <Button
                      variant="primary"
                      type="submit"
                      onClick={this.onPostSubmit}
                    >
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
