import React, { Component } from "react";
import { connect } from "react-redux";
import { Form, Button, Nav, Alert } from "react-bootstrap";
import speaker from "bootstrap-icons/icons/speaker.svg";

import Page from "../Components/Page";

import { login } from "../Actions/AuthenticationAction";

class SignIn extends Component {
  state = { username: "", password: "" };

  constructor(props) {
    super(props);
    this.handleAuthentication = this.handleAuthentication.bind(this);
    this.handleChange = this.handleChange.bind(this);
  }

  async handleAuthentication(e) {
    e.preventDefault();
    this.props.login(this.state.username, this.state.password);
  }

  handleChange(evt) {
    console.log("handle login input change", evt.target.value);
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
            <img
              src={speaker}
              width="30"
              height="30"
              className="align-baseline"
              alt="Kick logo"
            />{" "}
            <span className="large-text">Kick</span>
            <p className="italic">
              <small>Make sharing easier</small>
            </p>
            <div className="sign-in-border">
              <Form>
                <Form.Group>
                  {this.props.registered ? (
                    <Alert variant="success">
                      Registration successful, login!
                    </Alert>
                  ) : (
                    <React.Fragment></React.Fragment>
                  )}
                </Form.Group>
                <Form.Group controlId="formBasicUsername">
                  <Form.Control
                    type="username"
                    placeholder="Username"
                    onChange={this.handleChange}
                    name="username"
                  />
                </Form.Group>

                <Form.Group controlId="formBasicPassword">
                  <Form.Control
                    type="password"
                    placeholder="Password"
                    onChange={this.handleChange}
                    name="password"
                  />
                </Form.Group>
                <Form.Group controlId="formErrorNotification">
                  {this.props.error === true ? (
                    <Form.Text className="text-muted">
                      The username or password you provided was incorrect
                    </Form.Text>
                  ) : (
                    <React.Fragment />
                  )}
                </Form.Group>
                <Button
                  variant="primary"
                  type="submit"
                  onClick={this.handleAuthentication}
                >
                  Submit
                </Button>
              </Form>
            </div>
            <Nav className="justify-content-center" activeKey="/register">
              <Nav.Item>
                <Nav.Link href="/register">
                  Don't have an account? Create one!
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
    error: state.error,
    registered: state.registered,
  };
};

const mapDispatchToProps = {
  login,
};

export default connect(mapStateToProps, mapDispatchToProps)(SignIn);
