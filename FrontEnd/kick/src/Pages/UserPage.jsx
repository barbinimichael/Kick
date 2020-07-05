import React, { Component } from "react";
import { Container, Row, Col } from "react-bootstrap";

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
      <div>
        <Container fluid>
          <Row>
            <Col lg="3"></Col>
            <Col lg="6">
              {this.props.match.params.username === "me" ? (
                <React.Fragment>
                  <UserCard user={this.props.meUser} />
                  <Feed
                    feedURL={"/api/posts/user/" + this.props.meUser.username}
                  />
                </React.Fragment>
              ) : (
                <React.Fragment>
                  <UserCard user={this.state.user} />
                  <Feed
                    feedURL={
                      "/api/posts/user/" + this.props.match.params.username
                    }
                  />
                </React.Fragment>
              )}
            </Col>
            <Col lg="3"></Col>
          </Row>
        </Container>
      </div>
    );
  }
}

export default UserPage;
