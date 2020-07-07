import React, { Component } from "react";
import { Link } from "react-router-dom";

import { Container, Row, Col } from "react-bootstrap";
import Jumbotron from "react-bootstrap/Jumbotron";
import Image from "react-bootstrap/Image";
import Badge from "react-bootstrap/Badge";
import logo from "../logo.svg";

import API from "../api/api";

class UserCard extends Component {
  state = { update: false, following: false };

  componentDidUpdate(props) {
    if (this.props !== props) {
      this.setState({ update: true });
    }
  }

  componentDidMount() {
    this.checkFollowing();
  }

  checkFollowing = () => {
    API({
      method: "get",
      url: `/api/followings/check/${this.props.user.username}`,
    })
      .then((response) => {
        console.log("Following", response);
        this.setState({ following: response.data });
        this.setState({ update: false });
      })
      .catch((error) => {
        console.log(error);
      });
  };

  render() {
    if (this.state.update) {
      this.checkFollowing();
    }

    let followerWord = "Followers";
    if (
      this.props.user.whereIsInfluencer &&
      this.props.user.whereIsInfluencer.length === 1
    ) {
      followerWord = "Follower";
    }

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
            Public
          </Badge>
        )}
        {this.state.following ? (
          <Badge pill variant="info">
            Following
          </Badge>
        ) : (
          <Badge pill variant="danger">
            Not Following
          </Badge>
        )}
        <Container>
          <Row>
            <Col align="right">
              {this.props.user.whereIsInfluencer ? (
                <Link to={`/followers/${this.props.user.username}`}>
                  {this.props.user.whereIsInfluencer.length} {followerWord}
                </Link>
              ) : (
                "0 Followers"
              )}
            </Col>
            <Col align="left">
              {this.props.user.whereIsInfluencer ? (
                <Link to={`/influencers/${this.props.user.username}`}>
                  {this.props.user.whereIsInfluencer.length} Following
                </Link>
              ) : (
                "0 Following"
              )}
            </Col>
          </Row>
        </Container>
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
