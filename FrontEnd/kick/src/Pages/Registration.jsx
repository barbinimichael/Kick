import React, { Component } from "react";
import { Image, Form, Button, Col, Nav, Alert, Badge } from "react-bootstrap";
import { connect } from "react-redux";
import speaker from "bootstrap-icons/icons/speaker.svg";
import eyeFill from "bootstrap-icons/icons/eye-fill.svg";
import eye from "bootstrap-icons/icons/eye.svg";

import history from "../Components/History";
import Page from "../Components/Page";
import { register, resetLogin } from "../Actions/AuthenticationAction";
import { checkEmail, checkPassword } from "../Components/CheckValidInput";

class Registration extends Component {
  state = {
    username: "",
    password: "",
    confirmPassword: "",
    email: "",
    firstName: "",
    lastName: "",
    city: "",
    country: "",
    privateProfile: false,
    error: false,
    missingValue: "",
    passwordConfirmError: "",
    showValue: false,
  };

  handleRegistration = (evt) => {
    evt.preventDefault();
    let data = { ...this.state };
    delete data.confirmPassword;
    delete data.passwordConfirmError;
    delete data.error;
    delete data.missingValue;
    delete data.showValue;

    let missingValue = checkEmail(data.email);
    if (missingValue !== "") {
      this.setState({ missingValue });
      return;
    }

    missingValue = checkPassword(data.password);
    if (missingValue !== "") {
      this.setState({ missingValue });
      return;
    }

    // check password and confirmed password match
    if (this.state.password !== this.state.confirmPassword) {
      this.setState({ passwordConfirmError: "Passwords do not match!" });
      return;
    } else {
      this.setState({ passwordConfirmError: "" });
    }

    // check no missing fields
    for (const property in data) {
      if (data[property] === "") {
        this.setState({ missingValue: property });
        return;
      }
    }

    console.log("missing value", this.state.missingValue);

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
    if (this.props.loggedIn) {
      history.push("/");
      window.location.reload(true);
    }
  }

  showPassword = () => {
    this.setState({ showPassword: !this.state.showPassword });
    console.log("password show after click", this.state.showPassword);
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
                </Form.Row>
                <Form.Row>
                  <Form.Group as={Col} controlId="formGridPassword">
                    <Form.Control
                      type={this.state.showPassword ? "text" : "password"}
                      placeholder="Password"
                      onChange={this.handleChange}
                      name="password"
                    />
                  </Form.Group>
                  <Form.Group controlId="formGridShow">
                    <Button
                      variant="outline-primary"
                      onClick={this.showPassword}
                    >
                      {this.state.showPassword ? (
                        <Image src={eyeFill} />
                      ) : (
                        <Image src={eye} />
                      )}
                    </Button>
                  </Form.Group>
                </Form.Row>
                <Form.Row>
                  <Form.Group as={Col} controlId="formGridConfirmPassword">
                    <Form.Control
                      type={this.state.showPassword ? "text" : "password"}
                      placeholder="Confirm password"
                      onChange={this.handleChange}
                      name="confirmPassword"
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
                {this.state.passwordConfirmError ? (
                  <Alert variant="danger">
                    <p className="italic mb-0 center">
                      {this.state.passwordConfirmError}
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
            <Badge className="home-warning-badge" variant="danger">Note: This is being run on a free Heroku dyno so you may have to wait for it to boot up after your first API request (~30 seconds) </Badge>
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
    loggedIn: state.loggedIn,
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
