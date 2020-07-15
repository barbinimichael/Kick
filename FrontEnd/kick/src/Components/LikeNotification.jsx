import React, { Component } from "react";
import { Link } from "react-router-dom";
import API from "../api/api";

import Toast from "react-bootstrap/Toast";
import heartFill from "bootstrap-icons/icons/heart-fill.svg";
import Time from "./Time";

class LikeNotification extends Component {
  state = { show: true };

  handleClose = () => {
    this.setState({ show: false });

    API({
      method: "get",
      url: `/api/likeNotifications/single/${this.props.notification.thePostId}`,
    }).catch((error) => {
      console.log(error);
    });
  };

  render() {
    return (
      <Toast show={this.state.show} onClose={this.handleClose}>
        <Toast.Header>
          <img src={heartFill} className="rounded mr-2" alt="" />
          <strong className="mr-auto"></strong>
          <small>
            <Time time={this.props.notification.time} />
          </small>
        </Toast.Header>
        <Toast.Body>
          <Link to={`/user/${this.props.notification.userLiked}`}>
            {this.props.notification.userLiked}
          </Link>{" "}
          liked your{" "}
          <Link to={`/post/${this.props.notification.thePostId}`}>post</Link>
        </Toast.Body>
      </Toast>
    );
  }
}

export default LikeNotification;
