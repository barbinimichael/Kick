import React, { useState, useEffect, useCallback } from "react";
import { Row, Col, Badge, Form, Button } from "react-bootstrap";
import firebase from "../api/database";

const Conversation = (props) => {
  const [messages, setMessages] = useState([]);
  const [currentUser, setCurrentUser] = useState("");
  const [newMessage, setNewMessage] = useState([]);

  useEffect(() => {
    if (props.db && props.currentId) {
      console.log(props);
      setCurrentUser(props.currentUser);
      console.log("conversation currentId", props.currentId);
      var currentMessages = [];
      props.db
        .collection("conversations")
        .doc(props.currentId.toString())
        .collection("messages")
        .withConverter(messageConverter)
        .get()
        .then(function (querySnapshot) {
          querySnapshot.forEach(function (doc) {
            currentMessages.push(doc.data());
          });
          setMessages(currentMessages);
        });
    }
  }, [props]);

  console.log("conversation messages", messages);

  const handleSubmit = useCallback(
    (e) => {
      e.preventDefault();
      if (props.db) {
        console.log("add conversation");
        props.db
          .collection("conversations")
          .doc(props.currentId.toString())
          .collection("messages")
          .add({
            user: "mbarbzzz",
            message: newMessage,
            time: new Date(),
          });
      }
    },
    [props.db, props.currentId, newMessage]
  );

  return (
    <React.Fragment>
      <h1 className="center">Message</h1>
      <hr className="hr-line" />
      {messages.map((message) => {
        console.log("Current message user", currentUser);
        console.log("Message user", message.user);
        if (message.user === currentUser) {
          return (
            <h1 className="right" key={message.time}>
              <Badge pill variant="primary">
                {message.message}
              </Badge>
            </h1>
          );
        } else {
          return (
            <h1 className="left" key={message.time}>
              <Badge pill variant="secondary">
                {message.message}
              </Badge>
            </h1>
          );
        }
      })}
      <Form.Group>
        <Form.Row>
          <Col lg="9">
            <Form.Control
              size="lg"
              type="text"
              onChange={(e) => {
                setNewMessage(e.target.value);
              }}
            />
          </Col>
          <Col>
            <Button onClick={handleSubmit}>Submit</Button>
          </Col>
        </Form.Row>
      </Form.Group>
    </React.Fragment>
  );
};

export default Conversation;

class Message {
  constructor(user, message, time) {
    this.user = user;
    this.message = message;
    this.time = time;
  }
  toString() {
    return this.users + ", " + this.message + ", " + this.time;
  }
}

// Firestore data converter
var messageConverter = {
  toFirestore: function (message) {
    return {
      user: message.user,
      message: message.message,
      time: message.time,
    };
  },
  fromFirestore: function (snapshot, options) {
    const data = snapshot.data(options);
    return new Message(data.user, data.message, data.time);
  },
};
