import React, { Component } from "react";
import { Row, Col, Form, Button, Image } from "react-bootstrap";
import { v4 as uuidv4 } from "uuid";

import API from "../api/api";
import firebase from "../api/database";

import Page from "../Components/Page";
import history from "../Components/History";

class CreatePost extends Component {
  state = { image: "", imageURL: "", caption: "", city: "", country: "" };
  storage = firebase.storage();

  handleFileSubmit = (event) => {
    event.preventDefault();
    const image = event.target.files[0];
    this.setState({
      image,
    });
  };

  onChange = (e) => this.setState({ [e.target.name]: e.target.value });

  onPostSubmit = (e) => {
    e.preventDefault();

    if (this.state.image === "") {
      console.error(`not an image, the image file is a ${typeof imageAsFile}`);
    }
    const imageName = uuidv4() + "-" + this.state.image.name;
    const uploadTask = this.storage
      .ref(`/images/${imageName}`)
      .put(this.state.image);
    //initiates the firebase side uploading
    uploadTask.on(
      "state_changed",
      (snapShot) => {
        //takes a snap shot of the process as it is happening
        console.log(snapShot);
      },
      (err) => {
        //catches the errors
        console.log(err);
      },
      () => {
        // gets the functions from storage refences the image storage in firebase by the children
        // gets the download url then sets the image from firebase as the value for the imgUrl key:
        this.storage
          .ref("images")
          .child(imageName)
          .getDownloadURL()
          .then((fireBaseUrl) => {
            this.setState({
              imageURL: fireBaseUrl,
            });

            console.log("Firebase image URL:", this.state.imageURL);

            let data = {
              caption: this.state.caption,
              imageURL: this.state.imageURL,
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
          });
      }
    );
  };

  render() {
    let { caption, city, country } = this.state;

    return (
      <Page
        middleComponent={
          <Form>
            <Col>
              <Row xs="6" md="3" lg="1">
                <Form.Group controlId="formImage">
                  <Image src={this.state.imageURL} roundedCircle fluid />
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
        }
      />
    );
  }
}

export default CreatePost;
