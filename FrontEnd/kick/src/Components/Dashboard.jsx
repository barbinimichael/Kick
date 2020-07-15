import React, { Component } from "react";
import LikeNotification from "./LikeNotification";
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

  render() {
    return (
      <div className="sticky-top overflow-auto">
        <h1>Dashboard</h1>
        {this.state.likeNotifications.map((notification, index) => (
          <LikeNotification key={index} notification={notification} />
        ))}
      </div>
    );
  }
}

export default Dashboard;
