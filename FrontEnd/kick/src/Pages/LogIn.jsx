import React, { Component } from "react";
import { connect } from "react-redux";
import { Form, Button, Nav, Alert, Badge } from "react-bootstrap";
import speaker from "bootstrap-icons/icons/speaker.svg";

import Page from "../Components/Page";

import history from "../Components/History";
import { login, resetRegistration } from "../Actions/AuthenticationAction";

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
    const value = evt.target.value;
    this.setState({
      [evt.target.name]: value,
    });
  }

  componentDidMount() {
    if (!this.props.registered) {
      this.props.resetRegistration();
    }
    console.log("login props.loggedIn", this.props.loggedIn);
    if (this.props.loggedIn) {
      history.push("/");
      window.location.reload(true);
    }
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
            <Badge className="home-warning-badge" variant="danger">Note: This is being run on a free Heroku dyno so you may have to wait for it to boot up after your first API request (~30 seconds) </Badge>
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
  resetRegistration,
};

export default connect(mapStateToProps, mapDispatchToProps)(SignIn);
