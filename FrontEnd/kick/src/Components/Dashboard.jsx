import React, { Component } from "react";
import { Toast } from "react-bootstrap";
class Dashboard extends Component {
  state = {
    notifications: [
      { id: 1, message: "abc" },
      { id: 2, message: "blah" },
      { id: 3, message: "blah" },
      { id: 4, message: "blah" },
      { id: 5, message: "blah" },
      { id: 6, message: "blah" },
      { id: 7, message: "blah" },
      { id: 8, message: "blah" },
      { id: 9, message: "blah" },
      { id: 10, message: "blah" },
      { id: 11, message: "abc" },
      { id: 12, message: "blah" },
      { id: 13, message: "blah" },
      { id: 14, message: "blah" },
      { id: 15, message: "blah" },
      { id: 16, message: "blah" },
      { id: 17, message: "blah" },
      { id: 18, message: "blah" },
      { id: 19, message: "blah" },
      { id: 20, message: "blah" },
      { id: 21, message: "abc" },
      { id: 22, message: "blah" },
      { id: 23, message: "blah" },
      { id: 24, message: "blah" },
      { id: 25, message: "blah" },
      { id: 26, message: "blah" },
      { id: 27, message: "blah" },
      { id: 28, message: "blah" },
      { id: 29, message: "blah" },
      { id: 30, message: "blah" },
    ],
  };

  render() {
    return (
      <div className="sticky-top overflow-auto">
        <h1>Dashboard</h1>
        {this.state.notifications.map((notification) => (
          <Toast key={notification.id}>
            <Toast.Body>{notification.message}</Toast.Body>
          </Toast>
        ))}
      </div>
    );
  }
}

export default Dashboard;
