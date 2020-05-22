import React, { Component } from "react";
import logo from "../logo.svg";
import { Navbar, Nav, Button } from "react-bootstrap";

import history from "./History";

class NavigationBar extends Component {
  handleSignOut = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("Authorization");
    history.push("/sign-in");
    window.location.reload(false);
  };

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
            <Nav.Link href="message">Message</Nav.Link>
          </Nav>
          <Nav>
            <Nav.Link href="explore">Explore</Nav.Link>
          </Nav>
          <Nav>
            <Nav.Link href="create-post">Create Post</Nav.Link>
          </Nav>
          <Nav className="ml-auto">
            <Button onClick={this.handleSignOut}>Sign Out</Button>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
    );
  }
}

export default NavigationBar;
