import React, { Component } from "react";

import Page from "../Components/Page";
import UserFeed from "../Components/UserFeed";

class InfluencersPage extends Component {
  render() {
    return (
      <Page
        middleComponent={
          <UserFeed
            feedURL={`/api/followings/influencers/${this.props.match.params.username}`}
            meUser={this.props.meUser}
          />
        }
      />
    );
  }
}

export default InfluencersPage;
