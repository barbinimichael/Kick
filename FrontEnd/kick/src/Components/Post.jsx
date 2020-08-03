import React, { Component } from "react";
import { Link } from "react-router-dom";

import { Card, Button, Image, FormControl, InputGroup } from "react-bootstrap";
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
      displayComments = [
        comments[comments.length - 1].username +
          " : " +
          comments[comments.length - 1].comment,
      ];
    }

    return displayComments;
  };

  onDeleteSubmit = (event) => {
    event.preventDefault();
    console.log(this.state.comment);
    this.props.handleUserDeleted(this.props.post.id);
  };

  render() {
    return (
      <Card>
        <Card.Header>
          <Link to={`/user/${this.props.post.username}`}>
            {this.props.post.username}
          </Link>
          {"    "}
          {this.props.myPost ? (
            <Button
              variant="danger"
              className="ml-auto float-right"
              onClick={this.onDeleteSubmit}
            >
              Delete post
            </Button>
          ) : (
            <React.Fragment></React.Fragment>
          )}
        </Card.Header>
        <Card.Img variant="top" src={logo} />
        <Card.Body>
          <Button variant="link" onClick={this.onLike}>
            {this.props.liked ? (
              <Image src={heartFill} alt="" />
            ) : (
              <Image src={heart} alt="" />
            )}
          </Button>
          <Link to={`/comment/${this.props.post.id}`}>
            <img src={chat} alt="comments" />
          </Link>
          <Card.Title>{this.props.post.likes.length} likes</Card.Title>
          <Card.Title>
            {this.props.post.username + " : " + this.props.post.caption}
          </Card.Title>
          {this.setComments().map((comment, index) => (
            <Card.Text key={index}>{comment}</Card.Text>
          ))}
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
