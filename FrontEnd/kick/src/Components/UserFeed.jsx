import React, { Component } from "react";

import API from "../api/api";
import UserCard from "./UserCard";

class UserFeed extends Component {
  state = { feed: [], update: false };

  componentDidMount() {
    this.createFeed();
  }

  componentDidUpdate(props) {
    if (this.props !== props) {
      this.setState({ update: true });
    }
  }

  createFeed = () => {
    console.log("props", this.props.feedURL);
    API({
      method: "get",
      url: this.props.feedURL,
    })
      .then((response) => {
        let feed = response.data.content;
        this.setState({ feed });
        this.setState({ update: false });
      })
      .catch((error) => {
        console.log(error);
      });
  };

  render() {
    console.log("feed", this.state.feed);

    if (this.state.update) {
      this.createFeed();
    }

    return (
      <div>
        {this.state.feed.map((user) => (
          <UserCard user={user} />
        ))}
      </div>
    );
  }
}

export default UserFeed;
