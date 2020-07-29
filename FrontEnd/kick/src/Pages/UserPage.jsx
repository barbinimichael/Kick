import React, { Component } from "react";

import Page from "../Components/Page";
import UserCard from "../Components/UserCard";
import Feed from "../Components/Feed";

import API from "../api/api";

class UserPage extends Component {
  state = { user: [] };

  componentDidMount() {
    if (this.props.match.params.username !== "me") {
      API({
        method: "get",
        url: `/api/applicationUsers/username/${this.props.match.params.username}`,
      })
        .then((response) => {
          console.log("user profile fetch", response);
          let user = response.data;
          this.setState({ user });
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }

  render() {
    return (
      <Page
        middleComponent={
          <React.Fragment>
            {this.props.match.params.username === "me" ? (
              <React.Fragment>
                <UserCard
                  user={this.props.meUser}
                  meUsername={this.props.meUser.username}
                />
                <Feed
                  feedURL={"/api/posts/user/" + this.props.meUser.username}
                />
              </React.Fragment>
            ) : (
              <React.Fragment>
                <UserCard
                  user={this.state.user}
                  meUsername={this.props.meUser.username}
                />
                <Feed
                  feedURL={
                    "/api/posts/user/" + this.props.match.params.username
                  }
                />
              </React.Fragment>
            )}
          </React.Fragment>
        }
      />
    );
  }
}

export default UserPage;
