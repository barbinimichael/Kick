import React, { Component } from "react";
import { connect } from "react-redux";
import { Form, Button, Nav } from "react-bootstrap";

import Page from "../Components/Page";
import { login } from "../Actions/AuthenticationAction";

class SignIn extends Component {
  state = { username: "", password: "", failed: false };

  constructor(props) {
    super(props);
    this.handleAuthentication = this.handleAuthentication.bind(this);
    this.handleChange = this.handleChange.bind(this);
  }

  async handleAuthentication(e) {
    e.preventDefault();
    console.log("username", this.state.username);
    console.log("username", this.state.password);
    this.props.login(this.state.username, this.state.password);
  }

  handleChange(evt) {
    const value = evt.target.value;
    this.setState({
      [evt.target.name]: value,
    });
  }

  render() {
    return (
      <Page
        middleComponent={
          <React.Fragment>
            <Form>
              <Form.Group controlId="formBasicUsername">
                <Form.Label>Username</Form.Label>
                <Form.Control
                  type="username"
                  placeholder="Enter username"
                  onChange={this.handleChange}
                  name="username"
                />
              </Form.Group>

              <Form.Group controlId="formBasicPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Password"
                  onChange={this.handleChange}
                  name="password"
                />
              </Form.Group>
              {this.state.failed === true ? (
                <Form.Text className="text-muted">
                  The username or password you provided was incorrect
                </Form.Text>
              ) : (
                <React.Fragment />
              )}
              <Button
                variant="primary"
                type="submit"
                onClick={this.handleAuthentication}
              >
                Submit
              </Button>
            </Form>

            <Nav className="justify-content-center" activeKey="/home">
              <Nav.Item>
                <Nav.Link href="/register">
                  Don't have an account? Create one
                </Nav.Link>
              </Nav.Item>
            </Nav>
          </React.Fragment>
        }
      />
    );
  }
}

const mapStateToProps = (state) => {
  return {
    loggedIn: state.loggedIn,
  };
};

const mapDispatchToProps = {
  login,
};

export default connect(mapStateToProps, mapDispatchToProps)(SignIn);
