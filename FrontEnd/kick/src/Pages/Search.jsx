import React, { Component } from "react";
import { Row, Col } from "react-bootstrap";
import InputGroup from "react-bootstrap/InputGroup";
import FormControl from "react-bootstrap/FormControl";
import Form from "react-bootstrap/Form";

import Page from "../Components/Page";
import Feed from "../Components/Feed";
import UserFeed from "../Components/UserFeed";

class Search extends Component {
  state = {
    search: "",
    selectedOption: "userToggle",
  };

  searchTerm = (search) => {
    return "?search=" + search;
  };

  onChange = (e) => {
    this.setState({ [e.target.name]: e.target.value });
  };

  onRadioToggle = (e) => {
    this.setState({ selectedOption: e.target.value });
  };

  render() {
    console.log("Search", this.state.search);
    return (
      <Page
        middleComponent={
          <React.Fragment>
            <Row className="sticky-top">
              <InputGroup className="mb-3">
                <FormControl
                  placeholder="Search..."
                  name="search"
                  onChange={this.onChange}
                  value={this.state.search}
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
                feedURL={`/api/posts/search?search=${this.state.search}&`}
              />
            ) : (
              <React.Fragment></React.Fragment>
            )}
            {(this.state.search !== "") &
            (this.state.selectedOption === "userToggle") ? (
              <UserFeed
                feedURL={`/api/applicationUsers/search?search=${this.state.search}&`}
                // feedURL={`/api/applicationUsers/search?search=username:*${this.state.search}*&`}
                meUser={this.props.meUser}
              />
            ) : (
              <React.Fragment></React.Fragment>
            )}
          </React.Fragment>
        }
      />
    );
  }
}

export default Search;
