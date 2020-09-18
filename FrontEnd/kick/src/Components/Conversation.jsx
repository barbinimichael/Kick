import React, { useState, useEffect, useCallback } from "react";
import { Col, Form, Button, InputGroup, Row } from "react-bootstrap";
import { secondsToDate } from "./Time";

const Conversation = (props) => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");

  useEffect(() => {
    if (!props.db || !props.currentId) {
      return;
    }
    const unsubscribe = props.db
      .collection("conversations")
      .doc(props.currentId.toString())
      .collection("messages")
      .orderBy("time", "asc")
      .withConverter(messageConverter)
      .onSnapshot((querySnapshot) => {
        var currentMessages = [];
        querySnapshot.forEach(function (doc) {
          currentMessages.push(doc.data());
        });
        setMessages(currentMessages);
      });

    return () => {
      unsubscribe();
    };
  }, [props.currentId, props.currentUser, props.db]);

  const handleSubmit = useCallback(
    (e) => {
      // e.preventDefault();
      if (props.db && newMessage !== "" && props.currentId) {
        console.log("add message", props.currentId);
        props.db
          .collection("conversations")
          .doc(props.currentId.toString())
          .collection("messages")
          .add({
            user: props.currentUser,
            message: newMessage,
            time: new Date(),
          });
        setNewMessage("");
      }
    },
    [props.db, props.currentId, newMessage, props.currentUser]
  );

  return (
    <React.Fragment>
      <h1 className="center">Message</h1>
      <hr className="hr-line" />
      <div className="extra-space Sidebar sticky-top wrap">
        {messages.map((message) => {
          if (message.user === props.currentUser) {
            return (
              <h1 key={message.time}>
                <span className="speech-bubble">
                  {message.message}
                  <p className="sub-text">
                    {secondsToDate(message.time.seconds)}
                  </p>
                </span>
              </h1>
            );
          } else {
            return (
              <h1 key={message.time}>
                <span className="gray-speech-bubble">
                  {message.message}
                  <p className="sub-text">
                    {message.user}, {secondsToDate(message.time.seconds)}
                  </p>
                </span>
              </h1>
            );
          }
        })}
      </div>
      <Row className="sticky-top fixed-bottom">
        <Col xl="11" lg="9" md="9" sm="9" xs="9">
          <InputGroup className="mb-3">
            <Form.Control
              size="lg"
              type="text"
              onChange={(e) => {
                setNewMessage(e.target.value);
              }}
              value={newMessage}
            />
          </InputGroup>
        </Col>
        <Col xl="1" lg="3" md="3" sm="3" xs="3">
          <Button onClick={handleSubmit}>Submit</Button>
        </Col>
      </Row>
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
