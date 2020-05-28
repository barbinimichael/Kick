import React, { Component } from "react";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import logo from "../logo.svg";

class Post extends Component {
  state = {};
  render() {
    return (
      <Card>
        <Card.Img variant="top" src={logo} />
        <Card.Body>
          <Card.Title>{this.props.post.username}</Card.Title>
          <Card.Text>{this.props.post.caption}</Card.Text>
          <Card.Subtitle className="mb-2 text-muted">
            {this.props.post.city}, {this.props.post.country}
          </Card.Subtitle>
          <Button variant="primary">Go somewhere</Button>
        </Card.Body>
      </Card>
    );
  }
}

export default Post;
