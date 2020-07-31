import React, { Component } from "react";
import { Card, Button, Image } from "react-bootstrap";

import Page from "../Components/Page";
import logo from "../logo.svg";
import API from "../api/api";

class CommentPage extends Component {
  state = { comment: "", comments: [] };

  componentDidMount() {
    API({
      method: "get",
      url: `/api/posts/${this.props.match.params.postId}/comments`,
    })
      .then((response) => {
        console.log("comment fetch", response);
        this.setState({ comments: response.data.content });
      })
      .catch((error) => {
        console.log(error);
      });
  }

  deleteComment = (evt, id) => {
    evt.preventDefault();
    API({
      method: "delete",
      url: `/api/commentPosts/${id}`,
    })
      .then((response) => {
        console.log("comment deleted", response);
        window.location.reload(true);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  //   <Card.Body>
  //   <Card.Text>Add a button to comment and like</Card.Text>
  //   </Card.Body>

  render() {
    return (
      <Page
        middleComponent={
          <React.Fragment>
            {this.state.comments.length > 0 ? (
              this.state.comments.map((comment, index) => (
                <Card key={index}>
                  <Card.Header>
                    <Image width="30" height="30" variant="top" src={logo} />
                    {comment.username} : {comment.comment}
                    {comment.username === this.props.meUser.username ? (
                      <Button
                        className="ml-auto float-right"
                        onClick={(e) => this.deleteComment(e, comment.id)}
                      >
                        Delete
                      </Button>
                    ) : (
                      <React.Fragment />
                    )}
                  </Card.Header>
                </Card>
              ))
            ) : (
              <React.Fragment>{"No comments"}</React.Fragment>
            )}
          </React.Fragment>
        }
      />
    );
  }
}

export default CommentPage;
