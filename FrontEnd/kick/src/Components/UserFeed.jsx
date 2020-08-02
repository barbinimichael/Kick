import React, { Component } from "react";

import API from "../api/api";
import UserCard from "./UserCard";

class UserFeed extends Component {
  constructor(props) {
    super(props);
    this.state = { feed: [], page: -1, totalPages: 0 };
    this.createFeed();
  }

  componentDidUpdate(props) {
    if (this.props !== props) {
      this.setState({ feed: [] });
      this.createFeed();
    }
  }

  createFeed = () => {
    // check that not requesting if no more pages
    if (this.state.page >= this.state.totalPages) {
      return;
    }
    this.setState({ page: this.state.page + 1 });

    API({
      method: "get",
      url: this.props.feedURL + `&page=${this.state.page}&size=10`,
    })
      .then((response) => {
        let feed = response.data.content;
        this.setState({ feed: this.state.feed.concat(feed) });
        this.setState({ totalPages: response.data.totalPages });
      })
      .catch((error) => {
        console.log(error);
      });
  };

  componentDidMount() {
    document.addEventListener("scroll", this.handleScroll);
  }

  handleScroll = (e) => {
    if (
      document.getElementById("feed").getBoundingClientRect().bottom <=
      window.innerHeight + 1
    ) {
      this.createFeed();
    }
  };

  componentWillUnmount() {
    document.removeEventListener("scroll", this.handleScroll);
  }

  render() {
    return (
      <div onScroll={this.handleScroll} id="feed">
        {this.state.feed.map((user) => (
          <UserCard user={user} meUsername={this.props.meUser.username} />
        ))}
      </div>
    );
  }
}

export default UserFeed;
