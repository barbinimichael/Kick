import React, { Component } from "react";

import Page from "../Components/Page";
import UserCard from "../Components/UserCard";
import Feed from "../Components/Feed";

import API from "../api/api";

class UserPage extends Component {
  state = { user: [], me: false, checkedMe: false };

  componentDidUpdate(props) {
    if (this.props !== props) {
    }
  }

  componentDidMount() {
    if (!this.state.checkedMe) {
      this.setState({ checkedMe: true });
      if (
        this.props.match.params.username === "me" ||
        this.props.match.params.username === this.props.meUser.username
      ) {
        this.setState({ me: true });
      } else {
        console.log("made call");
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
  }

  render() {
    return (
      <Page
        middleComponent={
          <React.Fragment>
            <UserCard
              user={this.state.me ? this.props.meUser : this.state.user}
              meUsername={this.props.meUser.username}
            />
            <Feed
              feedURL={
                "/api/posts/user/" +
                (this.state.me
                  ? this.props.meUser.username
                  : this.props.match.params.username)
              }
              myPosts={this.state.me}
            />
          </React.Fragment>
        }
      />
    );
  }
}

export default UserPage;
