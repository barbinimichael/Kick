import React, { Component } from "react";
import { Container, Row, Col } from "react-bootstrap";
import InputGroup from "react-bootstrap/InputGroup";
import FormControl from "react-bootstrap/FormControl";
import Form from "react-bootstrap/Form";

import Feed from "../Components/Feed";

class Search extends Component {
  state = {
    search: "",
    selectedOption: "userToggle",
  };

  searchTerm = (search) => {
    return "?search=" + search;
  };

  onChange = (e) => this.setState({ [e.target.name]: e.target.value });

  onRadioToggle = (e) => {
    this.setState({ selectedOption: e.target.value });
  };

  render() {
    console.log("state", this.state);

    let { search } = this.state;
    return (
      <div>
        <Container fluid>
          <Row>
            <Col lg="3"></Col>
            <Col lg="6">
              <Row className="sticky-top">
                <InputGroup className="mb-3">
                  <FormControl
                    placeholder="Search..."
                    aria-label="Search..."
                    aria-describedby="search"
                    name="search"
                    value={search}
                    onChange={this.onChange}
                  />
                </InputGroup>
              </Row>
              <Row>
                <Col align="right">
                  <Form.Check
                    label="Users"
                    type="radio"
                    id="user-radio"
                    value="userToggle"
                    onChange={this.onRadioToggle}
                    checked={this.state.selectedOption === "userToggle"}
                  />
                </Col>
                <Col align="left">
                  <Form.Check
                    label="Posts"
                    type="radio"
                    id="post-radio"
                    value="postToggle"
                    onChange={this.onRadioToggle}
                    checked={this.state.selectedOption === "postToggle"}
                  />
                </Col>
              </Row>
              {(this.state.search !== "") &
              (this.state.selectedOption === "postToggle") ? (
                <Feed
                  feedURL={`/api/posts/search?search=${this.state.search}`}
                />
              ) : (
                <React.Fragment></React.Fragment>
              )}
              {(this.state.search !== "") &
              (this.state.selectedOption === "userToggle") ? (
                <h1>/api/posts/search?search=${this.state.search}</h1>
              ) : (
                <React.Fragment></React.Fragment>
              )}
            </Col>
            <Col lg="3"></Col>
          </Row>
        </Container>
      </div>
    );
  }
}

export default Search;
