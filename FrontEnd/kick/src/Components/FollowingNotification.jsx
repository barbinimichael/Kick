import React, { Component } from "react";
import { Link } from "react-router-dom";
import API from "../api/api";

import { Button, Toast, Col, Row } from "react-bootstrap";
import personFill from "bootstrap-icons/icons/person-fill.svg";
import { Time } from "./Time";

class FollowingNotification extends Component {
  state = { show: true };

  handleClose = () => {
    this.setState({ show: false });
    this.deleteCall();
  };

  deleteCall = () => {
    API({
      method: "delete",
      url: `/api/followingNotifications/single/${this.props.notification.id}`,
    }).catch((error) => {
      console.log(error);
    });
  };

  handleAccept = () => {
    this.setState({ show: false });
    API({
      method: "post",
      url: `/api/followings/${this.props.notification.theFollowingId}/true`,
    })
      .then(() => {
        this.deleteCall();
      })
      .catch((error) => {
        console.log(error);
      });
  };

  handleDelete = () => {
    this.setState({ show: false });
    this.deleteCall();
  };

  render() {
    return (
      <Toast show={this.state.show} onClose={this.handleClose}>
        <Toast.Header>
          <img src={personFill} className="rounded mr-2" alt="" />
          <strong className="mr-auto">
            <Link to={`/user/${this.props.notification.following}`}>
              {this.props.notification.following}
            </Link>{" "}
            wants to follow you
          </strong>
          <small>
            <Time time={this.props.notification.time} />
          </small>
        </Toast.Header>
        <Toast.Body>
          <Row>
            <Col align="right">
              <Button onClick={this.handleAccept} size="sm">
                Accept
              </Button>
            </Col>
            <Col align="left">
              <Button
                onClick={this.handleDelete}
                variant="outline-warning"
                size="sm"
              >
                Delete
              </Button>
            </Col>
          </Row>
        </Toast.Body>
      </Toast>
    );
  }
}

export default FollowingNotification;
