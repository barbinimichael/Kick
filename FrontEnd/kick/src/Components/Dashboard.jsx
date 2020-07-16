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
        <FollowingNotification key={index} notification={notification} />
      )
    );

    let allNotificationComponents = likeNotifications.concat(
      followingNotifications
    );
    let allNotifications = this.state.likeNotifications.concat(
      this.state.followingNotifications
    );

    let list = [];
    for (let j = 0; j < allNotifications.length; j++)
      list.push({
        component: allNotificationComponents[j],
        notification: allNotifications[j],
      });

    //2) sort:
    list.sort(function (a, b) {
      return a.notification.time < b.notification.time
        ? -1
        : a.notification.time == b.notification.time
        ? 0
        : 1;
      //Sort could be modified to, for example, sort on the age
      // if the name is the same.
    });

    //3) separate them back out:
    for (var k = 0; k < list.length; k++) {
      allNotificationComponents[k] = list[k].component;
    }

    return allNotificationComponents;
  };

  render() {
    return (
      <div className="sticky-top overflow-auto">
        <h1>Dashboard</h1>
        {this.createNotifications()}
      </div>
    );
  }
}

export default Dashboard;
