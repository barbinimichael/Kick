import React, { Component } from "react";
import speaker from "bootstrap-icons/icons/speaker.svg";
import { Navbar, Nav, Button } from "react-bootstrap";
import { connect } from "react-redux";
import personCircle from "bootstrap-icons/icons/person-circle.svg";
import gear from "bootstrap-icons/icons/gear-fill.svg";

import { logout } from "../Actions/AuthenticationAction";

class NavigationBar extends Component {
  handleSignOut = () => {
    this.props.logout();
  };

  render() {
    return (
      <Navbar bg="light" expand="lg" fixed="top">
        <Navbar.Brand href="/">
          <img
            src={speaker}
            width="30"
            height="30"
            className="d-inline-block align-top"
            alt="Kick logo"
          />{" "}
          Kick
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav>
            <Nav.Link href="/message">Message</Nav.Link>
          </Nav>
          <Nav>
            <Nav.Link href="/explore">Explore</Nav.Link>
          </Nav>
          <Nav>
            <Nav.Link href="/search">Search</Nav.Link>
          </Nav>
          <Nav>
            <Nav.Link href="/create-post">Create Post</Nav.Link>
          </Nav>

          <Nav className="ml-auto">
            <Navbar.Brand href={`/user/${this.props.meUser.username}`}>
              <img
                src={personCircle}
                width="30"
                height="30"
                className="d-inline-block align-top"
                alt="User profile"
              />
            </Navbar.Brand>
            <Navbar.Brand href="/settings">
              <img
                src={gear}
                width="30"
                height="30"
                className="d-inline-block align-top"
                alt="Settings"
              />
            </Navbar.Brand>
            <Button onClick={this.props.logout}>Sign Out</Button>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    loggedIn: state.loggedIn,
  };
};

const mapDispatchToProps = {
  logout,
};

export default connect(mapStateToProps, mapDispatchToProps)(NavigationBar);
