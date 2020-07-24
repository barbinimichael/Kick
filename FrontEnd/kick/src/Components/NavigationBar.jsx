import React, { Component } from "react";
import { connect } from "react-redux";
import { Navbar, Nav, Button } from "react-bootstrap";

import personCircle from "bootstrap-icons/icons/person-circle.svg";
import logo from "../logo.svg";
import { logout } from "../Actions/AuthenticationAction";

class NavigationBar extends Component {
  render() {
    return (
      <Navbar bg="light" expand="lg" fixed="top">
        <Navbar.Brand href="/">
          <img
            src={logo}
            width="30"
            height="30"
            className="d-inline-block align-top"
            alt="React Bootstrap logo"
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
            <Navbar.Brand href="/user/me">
              <img
                src={personCircle}
                width="30"
                height="30"
                className="d-inline-block align-top"
                alt="React Bootstrap logo"
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
