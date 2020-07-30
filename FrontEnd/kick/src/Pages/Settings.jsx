import React, { Component } from "react";
import { Form, Col, Button } from "react-bootstrap";

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
    private: false,
    success: "",
    error: "",
  };

  changeAttr = (evt, attr) => {
    evt.preventDefault();
    console.log("setting state", this.state);
    console.log("attr", attr);
    API({
      method: "put",
      url: `/api/applicationUsers/${attr}`,
      data: this.state[attr],
      headers: {},
    })
      .then((response) => {
        this.setState({ success: attr, error: "" });
        console.log("setting state", "success");
      })
      .catch((error) => {
        this.setState({ success: "", error: attr });
      });
  };

  handleChange = (evt) => {
    const value = evt.target.value;
    this.setState({
      [evt.target.name]: value,
    });
  };

  render() {
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
              ></InLineForm>
              <InLineForm
                description="password"
                handleChange={this.handleChange}
                handleSubmit={this.changeAttr}
              ></InLineForm>
              <hr className="hr-line" />
              <InLineForm
                description="email"
                handleChange={this.handleChange}
                handleSubmit={this.changeAttr}
              ></InLineForm>
              <InLineForm
                description="city"
                handleChange={this.handleChange}
                handleSubmit={this.changeAttr}
              ></InLineForm>
              <InLineForm
                description="country"
                handleChange={this.handleChange}
                handleSubmit={this.changeAttr}
              ></InLineForm>
              <Form>
                <Form.Row className="align-items-center">
                  <Col className="mb-2">Change private status</Col>
                  <Col>
                    <Form.Check
                      type="checkbox"
                      className="mb-2 mr-sm-2"
                      id="inlineFormCheck"
                      label="Private"
                      name="private"
                    />
                  </Col>
                  <Col>
                    {" "}
                    <Button
                      type="submit"
                      onClick={(e) => this.handleSubmit(e, "private")}
                    >
                      Submit
                    </Button>
                  </Col>
                </Form.Row>
              </Form>
            </div>
            <h1></h1>
            <hr className="hr-line" />
            <div className="space-evenly">
              <a target="_blank" href="https://github.com/barbinimichael/Kick">
                About
              </a>
              <a
                target="_blank"
                href="https://quiet-inlet-83310.herokuapp.com/"
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

export default Settings;
