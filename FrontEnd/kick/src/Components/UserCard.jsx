import React, { Component } from "react";
import Card from "react-bootstrap/Card";
import logo from "../logo.svg";
import { Link } from "react-router-dom";

class UserCard extends Component {
  render() {
    return (
      <Card>
        <Card.Header>
          <Link to={`/user/${this.props.user.username}`}>
            {this.props.user.username}
          </Link>
        </Card.Header>
        <Card.Img variant="top" src={logo} />
        <Card.Body>
          <Card.Title>{this.props.user.biography} likes</Card.Title>
          <Card.Text>
            {this.props.user.firstName} {this.props.user.lastName}
          </Card.Text>
          <Card.Subtitle className="mb-2 text-muted">
            {this.props.user.privateProfile ? "Private" : "Public"}
          </Card.Subtitle>
        </Card.Body>
      </Card>
    );
  }
}

export default UserCard;
