import React, { Component } from "react";
import { Form, Col, Button, Alert } from "react-bootstrap";
import { connect } from "react-redux";

import { logout } from "../Actions/AuthenticationAction";
import InLineForm from "../Components/InLineForm";
import Page from "../Components/Page";
import API from "../api/api";

class Settings extends Component {
  state = {
    username: "",
    password: "",
    email: "",
    city: "",
    country: "",
    privateProfile: false,
    success: "",
    error: "",
  };

  changeAttr = (evt, attr) => {
    evt.preventDefault();
    API({
      method: "put",
      url: `/api/applicationUsers/${attr}`,
      data: this.state[attr],
      headers: {},
    })
      .then((response) => {
        this.setState({ success: attr, error: "" });
        console.log("setting state", "success");

        if (attr === "username" || attr === "password") {
          setTimeout(
            function () {
              //Start the timer
              this.props.logout();
            }.bind(this),
            1000
          );
          return;
        }
        this.props.handleChange();
      })
      .catch((error) => {
        this.setState({ success: "", error: attr });
      });
  };

  handleChange = (evt) => {
    const value =
      evt.target.name === "privateProfile"
        ? evt.target.checked
        : evt.target.value;
    this.setState({
      [evt.target.name]: value,
    });
  };

  deleteAccount = (evt) => {
    evt.preventDefault();
    API({
      method: "delete",
      url: `/api/applicationUsers`,
      headers: {},
    })
      .then((response) => {
        this.setState({ success: "account deactivation", error: "" });
        setTimeout(
          function () {
            //Start the timer
            this.props.logout();
          }.bind(this),
          1000
        );
      })
      .catch((error) => {
        this.setState({ success: "", error: "account deactivation" });
      });
  };

  render() {
    console.log("render props", this.props);
    return (
      <Page
        middleComponent={
          <React.Fragment>
            <h1>Settings</h1>
            <hr className="hr-line" />
            <div className="center">
              <div>
                <p>
                  Note, changing these settings will redirect back to login.
                </p>
              </div>
              <InLineForm
                description="username"
                handleChange={this.handleChange}
                handleSubmit={this.changeAttr}
                current={this.props.meUser.username}
              ></InLineForm>
              <InLineForm
                description="password"
                handleChange={this.handleChange}
                handleSubmit={this.changeAttr}
                current="********"
              ></InLineForm>
              <hr className="hr-line" />
              <InLineForm
                description="email"
                handleChange={this.handleChange}
                handleSubmit={this.changeAttr}
                current={this.props.meUser.email}
              ></InLineForm>
              <InLineForm
                description="city"
                handleChange={this.handleChange}
                handleSubmit={this.changeAttr}
                current={this.props.meUser.city}
              ></InLineForm>
              <InLineForm
                description="country"
                handleChange={this.handleChange}
                handleSubmit={this.changeAttr}
                current={this.props.meUser.country}
              ></InLineForm>
              <Form>
                <Form.Row className="align-items-center">
                  <Col className="mb-2">Change private status</Col>
                  <Col>
                    <Form.Check
                      type="checkbox"
                      className="mb-2 mr-sm-2"
                      id="inlineFormCheck"
                      label="privateProfile"
                      name="privateProfile"
                      defaultChecked={this.props.meUser.privateProfile}
                      onChange={this.handleChange}
                    />
                  </Col>
                  <Col>
                    {" "}
                    <Button
                      type="submit"
                      onClick={(e) => this.changeAttr(e, "privateProfile")}
                    >
                      Submit
                    </Button>
                  </Col>
                </Form.Row>
                <br></br>
                {this.state.success !== "" ? (
                  <Form.Row>
                    <Col>
                      <Alert variant="primary" className="mb-2 mr-sm-2">
                        {this.state.success} changed successfully
                      </Alert>
                    </Col>
                  </Form.Row>
                ) : (
                  <React.Fragment></React.Fragment>
                )}
                {this.state.error !== "" ? (
                  <Form.Row>
                    <Col>
                      <Alert variant="danger" className="mb-2 mr-sm-2">
                        An error occurred when changing {this.state.error}
                      </Alert>
                    </Col>
                  </Form.Row>
                ) : (
                  <React.Fragment></React.Fragment>
                )}
              </Form>
            </div>
            <br></br>
            <hr className="hr-line" />
            <div className="center">
              <Form>
                <Form.Row>
                  <Col>
                    <Button
                      variant="danger"
                      className="mb-2 mr-sm-2"
                      onClick={this.deleteAccount}
                    >
                      Delete Account
                    </Button>
                  </Col>
                </Form.Row>
              </Form>
            </div>
            <hr className="hr-line" />
            <div className="space-evenly">
              <a
                target="_blank"
                href="https://github.com/barbinimichael/Kick"
                rel="noopener noreferrer"
              >
                About
              </a>
              <a
                target="_blank"
                href="https://quiet-inlet-83310.herokuapp.com/"
                rel="noopener noreferrer"
              >
                API
              </a>
            </div>
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
  logout,
};

export default connect(mapStateToProps, mapDispatchToProps)(Settings);
