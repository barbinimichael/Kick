import React, { Component } from "react";
import { Form, Col, Button, Alert, Modal } from "react-bootstrap";
import { connect } from "react-redux";

import { checkEmail, checkPassword } from "../Components/CheckValidInput";
import { logout } from "../Actions/AuthenticationAction";
import InLineForm from "../Components/InLineForm";
import Page from "../Components/Page";
import API from "../api/api";
import WarningPopup from "../Components/WarningPopup";

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
    popup: false,
  };

  changeAttr = (evt, attr) => {
    evt.preventDefault();

    // check not empty
    if (attr === "") {
      this.setState({ error: attr });
      return;
    }

    // check password
    if (attr === "password") {
      let error = checkPassword(this.state[attr]);
      if (error !== "") {
        this.setState({ error });
        return;
      }
    }

    // check email
    if (attr === "email") {
      let error = checkEmail(this.state[attr]);
      if (error !== "") {
        this.setState({ error });
        return;
      }
    }

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
        this.setState({ popup: false });
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
    console.log("render state", this.state);
    return (
      <Page
        middleComponent={
          <React.Fragment>
            <h1>Settings</h1>
            <hr className="hr-line" />
            <div className="">
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
              <Form>
                <Form.Text className="text-muted">
                  Password must be 8-20 characters. Include numbers, letters,
                  and special characters.
                </Form.Text>
              </Form>
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
                        {this.state.error} is required
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
                      onClick={() => {
                        this.setState({ popup: true });
                      }}
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
            <WarningPopup
              show={this.state.popup}
              onClose={() => {
                this.setState({ popup: false });
              }}
              onDelete={this.deleteAccount}
            ></WarningPopup>
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
