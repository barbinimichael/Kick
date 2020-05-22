import React, { Component } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Nav from "react-bootstrap/Nav";
import history from "./History";
import logo from "../logo.svg";

class SignIn extends Component {
  state = { username: "", password: "" };

  handleAuthentication = () => {
    localStorage.setItem("user", "username");
    localStorage.setItem("Authorization", "password");
    history.push("/");
  };

  render() {
    return (
      <div>
        <Container>
          <Row>
            <Col lg="3"></Col>
            <Col lg="6">
              <Form>
                <Form.Group controlId="formBasicEmail">
                  <Form.Label>Email address</Form.Label>
                  <Form.Control type="email" placeholder="Enter email" />
                  <Form.Text className="text-muted">
                    We'll never share your email with anyone else.
                  </Form.Text>
                </Form.Group>

                <Form.Group controlId="formBasicPassword">
                  <Form.Label>Password</Form.Label>
                  <Form.Control type="password" placeholder="Password" />
                </Form.Group>
                <Form.Group controlId="formBasicCheckbox">
                  <Form.Check type="checkbox" label="Remember Me" />
                </Form.Group>
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
            </Col>
            <Col lg="3"></Col>
          </Row>
        </Container>
      </div>
    );
  }
}

export default SignIn;
