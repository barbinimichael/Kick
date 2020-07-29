import React, { Component } from "react";
import LikeNotification from "./LikeNotification";
import FollowingNotification from "./FollowingNotification";
class Dashboard extends Component {
  state = { likeNotifications: [], followingNotifications: [] };

  componentDidUpdate(prevProps) {
    if (this.props.meUser !== prevProps.meUser) {
      console.log("dashboard props", this.props.meUser);
      this.setState({ likeNotifications: this.props.meUser.likeNotifications });
      this.setState({
        followingNotifications: this.props.meUser.followingNotifications,
      });
    }
  }

  createNotifications = () => {
    let likeNotifications = this.state.likeNotifications.map(
      (notification, index) => (
        <LikeNotification key={index} notification={notification} />
      )
    );

    let followingNotifications = this.state.followingNotifications.map(
      (notification, index) => (
        <FollowingNotification
          key={index + likeNotifications.length}
          notification={notification}
        />
      )
    );

    let sort = function (a, b) {
      return a.time < b.time ? -1 : a.time === b.time ? 0 : 1;
    };

    likeNotifications.sort(sort);
    followingNotifications.sort(sort);

    let allNotificationComponents = followingNotifications.concat(
      likeNotifications
    );

    return allNotificationComponents;
  };

  render() {
    return (
      <div className="sticky-top overflow-auto">
        <h3 className="center">Notifications</h3>
        {this.createNotifications()}
      </div>
    );
  }
}

export default Dashboard;
