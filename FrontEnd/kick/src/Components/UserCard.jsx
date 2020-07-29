import React, { Component } from "react";
import { Link } from "react-router-dom";

import {
  Container,
  Row,
  Col,
  Image,
  Badge,
  Jumbotron,
  Button,
} from "react-bootstrap";
import logo from "../logo.svg";

import API from "../api/api";

class UserCard extends Component {
  state = { following: "not following", influencerCount: 0, followingCount: 0 };

  componentDidUpdate(props) {
    if (this.props !== props) {
      this.checkFollowing();
      this.checkFollowerCount();
      this.checkInfluencerCount();
    }
  }

  checkFollowerCount = () => {
    API({
      method: "get",
      url: `/api/applicationUsers/followingCount/${this.props.user.username}`,
    })
      .then((response) => {
        console.log("Following count", response);
        this.setState({ followingCount: response.data });
      })
      .catch((error) => {
        console.log(error);
      });
  };

  checkInfluencerCount = () => {
    API({
      method: "get",
      url: `/api/applicationUsers/influencerCount/${this.props.user.username}`,
    })
      .then((response) => {
        console.log("Influencer count", response);
        this.setState({ influencerCount: response.data });
      })
      .catch((error) => {
        console.log(error);
      });
  };

  checkFollowing = () => {
    API({
      method: "get",
      url: `/api/followings/check/${this.props.user.username}`,
    })
      .then((response) => {
        console.log("Following", response);
        this.setState({ following: response.data });
      })
      .catch((error) => {
        console.log(error);
      });
  };

  handleFollow = () => {
    API({
      method: "post",
      url: `/api/followings/${this.props.user.username}`,
    })
      .then(() => {
        this.checkFollowing();
      })
      .catch((error) => {
        console.log(error);
      });
  };

  handleDeleteFollow = () => {
    API({
      method: "delete",
      url: `/api/followings/follower/deleting/influencer/${this.props.meUsername}/${this.props.user.username}`,
    })
      .then(() => {
        this.checkFollowing();
      })
      .catch((error) => {
        console.log(error);
      });
  };

  createFollowingBanner = () => {
    if (this.props.meUsername === this.props.user.username) {
      return (
        <Badge pill variant="primary">
          You!
        </Badge>
      );
    }

    if (this.state.following === "following") {
      return (
        <Button onClick={this.handleDeleteFollow} size="sm">
          UnFollow
        </Button>
      );
    } else if (this.state.following === "requested following") {
      return (
        <Button className="mb-0" onClick={this.handleDeleteFollow} size="sm">
          Cancel follow request
        </Button>
      );
    } else {
      return (
        <Button onClick={this.handleFollow} size="sm">
          Follow
        </Button>
      );
    }
  };

  render() {
    let followerWord = "Followers";
    if (
      this.props.user.whereIsInfluencer &&
      this.props.user.whereIsInfluencer.length === 1
    ) {
      followerWord = "Follower";
    }

    console.log("User card", this.props.user);

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
        {"    "}
        {!this.props.user.privateProfile ? (
          <Badge pill variant="success">
            Public
          </Badge>
        ) : (
          <React.Fragment>
            <Badge pill variant="warning">
              Private
            </Badge>
          </React.Fragment>
        )}
        {"    "}
        {this.createFollowingBanner()}
        <div>
          {this.props.user.city}, {this.props.user.country}
        </div>
        <Container>
          <Row>
            <Col align="right">
              {this.state.following === "following" &&
              this.props.user.whereIsInfluencer ? (
                <Link to={`/followers/${this.props.user.username}`}>
                  {this.state.followingCount} {followerWord}
                </Link>
              ) : (
                <React.Fragment>
                  {this.state.followingCount} {followerWord}
                </React.Fragment>
              )}
            </Col>
            <Col align="left">
              {this.state.following === "following" &&
              this.props.user.whereIsFollower ? (
                <Link to={`/influencers/${this.props.user.username}`}>
                  {this.state.influencerCount} Following
                </Link>
              ) : (
                <React.Fragment>
                  {this.state.influencerCount} Following
                </React.Fragment>
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
