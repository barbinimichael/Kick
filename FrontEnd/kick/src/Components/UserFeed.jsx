import React, { Component } from "react";

import API from "../api/api";
import UserCard from "./UserCard";
import { Spinner } from "react-bootstrap";

class UserFeed extends Component {
  constructor(props) {
    super(props);
    this.state = { feed: [], page: 0, totalPages: 1 };
    this.createFeed();
  }

  componentDidUpdate(props) {
    if (this.props.feedURL !== props.feedURL) {
      this.setState({ feed: [], page: 0, totalPages: 1 }, this.createFeed);
    }
  }

  createFeed = () => {
    // check that not requesting if no more pages
    if (this.state.page >= this.state.totalPages) {
      console.log("reached end of user feed");
      return;
    }

    API({
      method: "get",
      url: this.props.feedURL + `page=${this.state.page}&size=10`,
    })
      .then((response) => {
        console.log(response);
        let feed = response.data.content;
        this.setState({
          feed: this.state.feed.concat(feed),
          totalPages: response.data.totalPages,
          page: this.state.page + 1,
        });
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
    console.log("User feed state", this.state);
    return (
      <div onScroll={this.handleScroll} id="feed">
        {this.state.feed.map((user) => (
          <UserCard user={user} meUsername={this.props.meUser.username} />
        ))}
        {this.state.page < this.state.totalPages ? (
          <div className="center">
            <Spinner animation="border" variant="primary" />
            <Spinner animation="border" variant="secondary" />
            <Spinner animation="border" variant="success" />
            <Spinner animation="border" variant="danger" />
            <Spinner animation="border" variant="warning" />
            <Spinner animation="border" variant="info" />
            <Spinner animation="border" variant="light" />
            <Spinner animation="border" variant="dark" />
          </div>
        ) : (
          <React.Fragment></React.Fragment>
        )}
      </div>
    );
  }
}

export default UserFeed;
