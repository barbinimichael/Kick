import React, { Component } from "react";
import API from "../api/api";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import Image from "react-bootstrap/Image";
import heart from "bootstrap-icons/icons/heart.svg";
import heartFill from "bootstrap-icons/icons/heart-fill.svg";
import chat from "bootstrap-icons/icons/chat.svg";
import logo from "../logo.svg";

class Post extends Component {
  state = {
    liked: this.props.liked,
    numLikes: this.props.post.likes.length,
    firstUpdate: false,
  };

  handleLike = () => {
    API({
      method: "post",
      url: `api/posts/${this.props.post.id}/likePosts`,
    }).then(() => {
      this.setState({
        liked: true,
        numLikes: this.props.post.likes.length + 1,
      });
      this.forceUpdate();
    });
  };

  static getDerivedStateFromProps(props, state) {
    console.log("derive", state);
    if (!state.firstUpdate || !state.liked) {
      return {
        liked: props.liked,
        numLikes: props.post.likes.length,
        firstUpdate: true,
      };
    } else {
      return state;
    }
  }

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
          <Card.Text>{this.props.post.caption}</Card.Text>
          <Card.Subtitle className="mb-2 text-muted">
            {this.props.post.city}, {this.props.post.country}
          </Card.Subtitle>
          <footer>{this.props.post.postDate}</footer>
        </Card.Body>
      </Card>
    );
  }
}

export default Post;
