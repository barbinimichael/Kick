import React, { Component } from "react";
import { Form, Button, Col, Nav, Alert } from "react-bootstrap";
import { connect } from "react-redux";
import speaker from "bootstrap-icons/icons/speaker.svg";

import Page from "../Components/Page";
import { register } from "../Actions/AuthenticationAction";

class Registration extends Component {
  state = {
    username: "",
    password: "",
    email: "",
    firstName: "",
    lastName: "",
    city: "",
    country: "",
    private: false,
    error: false,
  };

  handleRegistration = (evt) => {
    evt.preventDefault();
    this.props.register(this.state);
  };

  handleChange = (evt) => {
    const value = evt.target.value;
    this.setState({
      [evt.target.name]: value,
    });
  };

  handleChecked = (evt) => {
    const value = evt.target.checked;
    this.setState({
      [evt.target.name]: value,
    });
  };

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
            <div className="sign-in-border">
              <Form>
                <Form.Row>
                  <Form.Group as={Col} controlId="formGridUsername">
                    <Form.Control
                      type="username"
                      placeholder="Username"
                      onChange={this.handleChange}
                      name="username"
                    />
                  </Form.Group>

                  <Form.Group as={Col} controlId="formGridPassword">
                    <Form.Control
                      type="password"
                      placeholder="Password"
                      onChange={this.handleChange}
                      name="password"
                    />
                  </Form.Group>
                </Form.Row>

                {this.props.errorUsername ? (
                  <Alert variant="danger">
                    <p className="italic mb-0">
                      This username has already been chosen
                    </p>
                  </Alert>
                ) : (
                  <React.Fragment></React.Fragment>
                )}

                <Form.Row>
                  <Form.Group as={Col} controlId="formGridEmail">
                    <Form.Control
                      type="email"
                      placeholder="Email"
                      onChange={this.handleChange}
                      name="email"
                    />
                  </Form.Group>
                </Form.Row>

                {this.props.errorEmail ? (
                  <Alert variant="danger">
                    <p className="italic mb-0">
                      This email has already been associated with an account
                    </p>
                  </Alert>
                ) : (
                  <React.Fragment></React.Fragment>
                )}

                <Form.Row>
                  <Form.Group as={Col} controlId="formGridFirstName">
                    <Form.Control
                      type="firstName"
                      placeholder="First name"
                      onChange={this.handleChange}
                      name="firstName"
                    />
                  </Form.Group>

                  <Form.Group as={Col} controlId="formGridLastName">
                    <Form.Control
                      type="lastName"
                      placeholder="Last name"
                      onChange={this.handleChange}
                      name="lastName"
                    />
                  </Form.Group>
                </Form.Row>

                <Form.Row>
                  <Form.Group as={Col} controlId="formGridCity">
                    <Form.Control
                      type="city"
                      placeholder="City"
                      onChange={this.handleChange}
                      name="city"
                    />
                  </Form.Group>

                  <Form.Group as={Col} controlId="formGridCountry">
                    <Form.Control
                      type="country"
                      placeholder="Country"
                      onChange={this.handleChange}
                      name="country"
                    />
                  </Form.Group>
                </Form.Row>

                <Form.Group id="formGridPrivate">
                  <Form.Check
                    type="checkbox"
                    label="Set private account"
                    onChange={this.handleChecked}
                    name="private"
                  />
                </Form.Group>

                {this.props.error ? (
                  <Alert variant="danger">
                    <p className="italic mb-0">An error has occurred</p>
                  </Alert>
                ) : (
                  <React.Fragment></React.Fragment>
                )}

                <Button
                  variant="primary"
                  type="submit"
                  onClick={this.handleRegistration}
                >
                  Register!
                </Button>
              </Form>
            </div>
            <Nav className="justify-content-center" activeKey="/sign-in">
              <Nav.Item>
                <Nav.Link href="/sign-in">Already an account? Log in!</Nav.Link>
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
    error: state.error,
    errorUsername: state.errorUsername,
    errorEmail: state.errorEmail,
  };
};

const mapDispatchToProps = {
  register,
};

export default connect(mapStateToProps, mapDispatchToProps)(Registration);
