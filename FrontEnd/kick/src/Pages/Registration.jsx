import React, { Component } from "react";
import { Form, Button, Col, Nav, Alert } from "react-bootstrap";
import { connect } from "react-redux";
import speaker from "bootstrap-icons/icons/speaker.svg";

import Page from "../Components/Page";
import { register, resetLogin } from "../Actions/AuthenticationAction";

class Registration extends Component {
  state = {
    username: "",
    password: "",
    email: "",
    firstName: "",
    lastName: "",
    city: "",
    country: "",
    privateProfile: false,
    error: false,
    missingValue: "",
  };

  handleRegistration = (evt) => {
    evt.preventDefault();
    let data = { ...this.state };
    delete data.error;
    delete data.missingValue;

    console.log("registration data", data);

    // check valid email format
    if (
      !data.email.includes("@") ||
      !data.email.includes(".") ||
      !(data.email.indexOf(".") > data.email.indexOf("@"))
    ) {
      this.setState({ missingValue: "a valid email format" });
      return;
    }

    // check valid password format
    if (
      !/\d/.test(data.password) ||
      !/[~`!@#$%^&*+=\-[\]\\';,/{}|\\":<>?]/g.test(data.password) ||
      !/[a-z]/.test(data.password) ||
      !(data.password.length > 7) ||
      !(data.password.length < 21)
    ) {
      this.setState({ missingValue: "a valid password" });
      return;
    }

    // check no missing fields
    for (const property in data) {
      if (data[property] === "") {
        this.setState({ missingValue: property });
        return;
      }
    }
    this.setState({ missingValue: "" });
    this.props.register(data);
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

  componentDidMount() {
    this.props.resetLogin();
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
                </Form.Row>

                <Form.Row>
                  <Form.Group as={Col} controlId="formGridPassword">
                    <Form.Control
                      type="password"
                      placeholder="Password"
                      onChange={this.handleChange}
                      name="password"
                    />
                    <Form.Text className="text-muted">
                      Password must be 8-20 characters. Include numbers,
                      letters, and special characters.
                    </Form.Text>
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
                    label="Set privateProfile account"
                    onChange={this.handleChecked}
                    name="privateProfile"
                  />
                </Form.Group>

                {this.props.errorRegister ? (
                  <Alert variant="danger">
                    <p className="italic mb-0 center">An error has occurred</p>
                  </Alert>
                ) : (
                  <React.Fragment></React.Fragment>
                )}

                {this.state.missingValue ? (
                  <Alert variant="danger">
                    <p className="italic mb-0 center">
                      {this.state.missingValue} is required
                    </p>
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
    errorRegister: state.errorRegister,
    errorUsername: state.errorUsername,
    errorEmail: state.errorEmail,
  };
};

const mapDispatchToProps = {
  register,
  resetLogin,
};

export default connect(mapStateToProps, mapDispatchToProps)(Registration);
