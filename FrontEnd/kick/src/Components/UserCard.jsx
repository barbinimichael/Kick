import React, { Component } from "react";
import Jumbotron from "react-bootstrap/Jumbotron";
import Image from "react-bootstrap/Image";
import Badge from "react-bootstrap/Badge";
import logo from "../logo.svg";
import { Link } from "react-router-dom";

class UserCard extends Component {
  render() {
    return (
      <Jumbotron>
        <Image
          src={logo}
          width="75"
          height="75"
          className="d-inline-block center"
          alt="React Bootstrap logo"
        />
        <Link to={`/user/${this.props.user.username}`}>
          {this.props.user.username}
        </Link>
        {"  | "}
        {this.props.user.city}, {this.props.user.country}
        {"  "}
        {this.props.user.privateProfile ? (
          <Badge pill variant="warning">
            Private
          </Badge>
        ) : (
          <Badge pill variant="success">
            Private
          </Badge>
        )}
        <hr className="hr-line" />
        <p className="less-margin">
          <b>
            {this.props.user.firstName} {this.props.user.lastName}
          </b>
        </p>
        <p className="less-margin">{this.props.user.biography}</p>
      </Jumbotron>
    );
  }
}

export default UserCard;
