import React, { Component } from "react";

import Page from "../Components/Page";
import UserCard from "../Components/UserCard";
import Feed from "../Components/Feed";

import API from "../api/api";

class UserPage extends Component {
  state = { user: [] };

  componentDidMount() {
    API({
      method: "get",
      url: `/api/applicationUsers/username/${this.props.match.params.username}`,
    })
      .then((response) => {
        console.log("user profile fetch", response);
        this.setState({ user: response.data });
      })
      .catch((error) => {
        console.log(error);
      });
  }

  render() {
    return (
      <Page
        middleComponent={
          <React.Fragment>
            <UserCard
              user={this.state.user}
              meUsername={this.props.meUser.username}
            />
            <Feed
              feedURL={
                "/api/posts/user/" + this.props.match.params.username + "?"
              }
              myPosts={
                this.props.match.params.username === this.props.meUser.username
              }
            />
          </React.Fragment>
        }
      />
    );
  }
}

export default UserPage;
