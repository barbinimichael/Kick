import React, { Component } from "react";
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
  state = {
    comment: "",
  };

  onLike = () => {
    // if not liked, like, otherwise unlike
    if (!this.props.liked) {
      this.props.handleUserLiked(this.props.post.id);
    } else {
      this.props.handleUserUnLiked(this.props.post.id);
    }
  };

  onCommentChange = (event) => {
    this.setState({ comment: event.target.value });
  };

  onCommentSubmit = (event) => {
    event.preventDefault();
    console.log(this.state.comment);
    this.props.handleUserCommented(this.props.post.id, this.state.comment);
  };

  setComments = () => {
    let comments = this.props.post.comments;
    let displayComments = [""];
    if (comments.length !== 0) {
      displayComments = [comments[comments.length - 1].comment];
    }

    return displayComments;
  };

  render() {
    return (
      <Card>
        <Card.Header>{this.props.post.username}</Card.Header>
        <Card.Img variant="top" src={logo} />
        <Card.Body>
          <Button variant="link" onClick={this.onLike}>
            {this.props.liked ? (
              <Image src={heartFill} alt="" />
            ) : (
              <Image src={heart} alt="" />
            )}
          </Button>
          <Button variant="link">
            <img src={chat} alt="" />
          </Button>
          <Card.Title>{this.props.post.likes.length} likes</Card.Title>
          {this.setComments().map((comment, index) => (
            <Card.Text key={index}>{comment}</Card.Text>
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
            onChange={this.onCommentChange}
          />
          <InputGroup.Append>
            <Button variant="outline-secondary" onClick={this.onCommentSubmit}>
              Post
            </Button>
          </InputGroup.Append>
        </InputGroup>
      </Card>
    );
  }
}

export default Post;
