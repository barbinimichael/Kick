import React, { Component } from "react";

import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Nav from "react-bootstrap/Nav";

import Page from "../Components/Page";
import history from "../Components/History";
import API from "../api/api";

class SignIn extends Component {
  state = { username: "", password: "", failed: false, login: false };

  constructor(props) {
    super(props);
    this.handleAuthentication = this.handleAuthentication.bind(this);
    this.handleChange = this.handleChange.bind(this);
  }

  async handleAuthentication(e) {
    e.preventDefault();
    await API({
      method: "post",
      url: "/login",
      data: {
        username: this.state.username,
        password: this.state.password,
      },
      headers: {},
    })
      .then((response) => {
        console.log(response);
        localStorage.setItem("user", this.state.username);
        localStorage.setItem("Authorization", response.headers.authorization);
        this.setState({ login: true });
        history.push("/");
        window.location.reload(true);
      })
      .catch((error) => {
        console.log(error);
        this.setState({ failed: true });
      });
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

export default SignIn;
