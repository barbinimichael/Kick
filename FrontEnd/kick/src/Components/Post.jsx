import React, { Component } from "react";
import API from "../api/api";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import Image from "react-bootstrap/Image";
import FormControl from "react-bootstrap/FormControl";
import InputGroup from "react-bootstrap/InputGroup";
import heart from "bootstrap-icons/icons/heart.svg";
import heartFill from "bootstrap-icons/icons/heart-fill.svg";
import chat from "bootstrap-icons/icons/chat.svg";
import logo from "../logo.svg";

class Post extends Component {
  constructor(props) {
    super(props);
    console.log("og props", this.props);

    this.state = {
      liked: "",
      numLikes: "",
      firstUpdate: false,
      comment: "",
      submitted: false,
    };
  }

  static getDerivedStateFromProps(props, state) {
    console.log("derive", state);
    console.log("props", props);
    if (state.liked === "" || state.liked === undefined) {
      return {
        liked: props.liked,
        numLikes: props.post.likes.length,
        firstUpdate: true,
      };
    } else {
      return state;
    }
  }

  handleLike = () => {
    console.log("state 1", this.state);

    if (!this.state.liked) {
      API({
        method: "post",
        url: `api/posts/${this.props.post.id}/likePosts`,
      }).then(() => {
        this.setState({
          liked: true,
          numLikes: this.state.numLikes + 1,
        });
      });
    } else {
      API({
        method: "delete",
        url: `api/posts/${this.props.post.id}/likePosts`,
      }).then(() => {
        this.setState({
          liked: false,
          numLikes: this.state.numLikes - 1,
        });
      });
    }
  };

  handleCommentChange = (event) => {
    this.setState({ comment: event.target.value });
  };

  handleCommentSubmit = (event) => {
    event.preventDefault();
    console.log(this.state.comment);

    API({
      method: "post",
      url: `api/posts/${this.props.post.id}/commentPosts`,
      data: this.state.comment,
    }).then(() => {
      this.setState({ submitted: true });
    });
  };

  setComments = () => {
    let comments = this.props.post.comments;
    let displayComments = [""];
    if (comments.length !== 0) {
      displayComments = [comments[comments.length - 1].comment];
      if (this.state.submitted) {
        displayComments.push(this.state.comment);
        this.setState({ submitted: false });
      }
      console.log("displaytComments", displayComments);
    }
    console.log("All comments", comments);

    return displayComments;
  };

  render() {
    console.log("render", this.state);
    return (
      <Card>
        <Card.Header>{this.props.post.username}</Card.Header>
        <Card.Img variant="top" src={logo} />
        <Card.Body>
          <Button variant="link" onClick={this.handleLike}>
            {this.state.liked ? (
              <Image src={heartFill} alt="" />
            ) : (
              <Image src={heart} alt="" />
            )}
          </Button>
          <Button variant="link">
            <img src={chat} alt="" />
          </Button>
          <Card.Title>{this.state.numLikes} likes</Card.Title>
          {this.setComments("").map((comment) => (
            <Card.Text>{comment}</Card.Text>
          ))}
          <Card.Text>{this.props.post.caption}</Card.Text>
          <Card.Subtitle className="mb-2 text-muted">
            {this.props.post.city}, {this.props.post.country}
          </Card.Subtitle>
          <footer>{this.props.post.postDate}</footer>
        </Card.Body>
        <InputGroup className="mb-3">
          <FormControl
            placeholder="Add a comment..."
            aria-label="Comment"
            aria-describedby="basic-addon2"
            onChange={this.handleCommentChange}
          />
          <InputGroup.Append>
            <Button
              variant="outline-secondary"
              onClick={this.handleCommentSubmit}
            >
              Post
            </Button>
          </InputGroup.Append>
        </InputGroup>
      </Card>
    );
  }
}

export default Post;
