import React, { Component } from "react";

class Time extends Component {
  convertTime = (time) => {
    let dif = Math.abs(time - new Date()) / 86400000;

    if (dif < 1) {
      return "Today";
    } else {
      return Math.floor(dif) + "days ago";
    }
  };

  render() {
    return <React.Fragment>{this.convertTime(this.props.time)}</React.Fragment>;
  }
}

export default Time;
