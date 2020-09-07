import React, { Component } from "react";

export class Time extends Component {
  convertTime = (time) => {
    let dif = Math.abs(new Date() - time) / 86400000;

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

export const secondsToDate = (seconds) => {
  const date = new Date(0);
  date.setUTCSeconds(seconds);
  if ((new Date() - date) / 86400000 < 1) {
    return "Today, " + date.toLocaleTimeString();
  } else {
    return date.toLocaleDateString();
  }
};
