import React, { useState, useEffect } from "react";

import Page from "../Components/Page";
import firebase from "../api/database";
import ConversationPane from "../Components/ConversationPane";
import Conversation from "../Components/Conversation";

const Message = (props) => {
  const db = firebase.firestore();
  const [ids, setIds] = useState([]);
  const [currentId, setCurrentId] = useState();
  const [currentUser, setCurrentUser] = useState("");

  useEffect(() => {
    console.log("In message useEffect");
    db.collection("conversation-details").onSnapshot(() => {
      if (props.meUser.username) {
        console.log("message props", props);
        setCurrentUser(props.meUser.username);

        var conversationDetails = db.collection("conversation-details");
        var query = conversationDetails.where(
          "users",
          "array-contains",
          props.meUser.username
        );

        console.log(
          "in messages, getting conversations",
          db.collection("conversations")
        );

        query
          .withConverter(usersConverter)
          .get()
          .then(function (querySnapshot) {
            let ids = [];
            console.log("Users converted", querySnapshot);
            querySnapshot.forEach(function (doc) {
              let users = doc.data().users;
              let index = users.indexOf(props.meUser.username);

              if (index > -1) {
                users.splice(index, 1);
              }
              console.log("doc", users.toString());
              ids.push([doc.id, users.toString()]);
            });
            setIds(ids);
          })
          .catch(function (error) {
            console.log("Error getting documents: ", error);
          });
        console.log("conversation ids", ids);
      }
    });
  });

  return (
    <Page
      rightComponent={
        <ConversationPane
          ids={ids}
          onSelect={setCurrentId}
          // db={db}
          currentUser={currentUser}
        />
      }
      middleComponent={
        <Conversation
          currentId={currentId}
          // db={db}
          currentUser={currentUser}
        />
      }
    />
  );
};

export default Message;

class Users {
  constructor(users) {
    this.users = users;
  }
  toString() {
    return this.users.toString();
  }
}

// Firestore data converter
var usersConverter = {
  toFirestore: function (users) {
    return {
      users: users,
    };
  },
  fromFirestore: function (snapshot, options) {
    const data = snapshot.data(options);
    return new Users(data.users);
  },
};
