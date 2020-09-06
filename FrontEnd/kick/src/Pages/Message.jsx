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
    if (props.meUser.username) {
      setCurrentUser(props.meUser.username);
    }
  }, [props.meUser.username]);

  useEffect(() => {
    console.log("In message useEffect");
    console.log("Current user in message useEffect", props.meUser.username);
    if (!props.meUser.username) {
      return;
    }
    const unsubscribe = db
      .collection("conversation-details")
      .where("users", "array-contains", props.meUser.username)
      .withConverter(usersConverter)
      .onSnapshot((querySnapshot) => {
        console.log("querysnapshot", querySnapshot);
        let newIds = [];
        querySnapshot.forEach(function (doc) {
          let users = doc.data().users;
          let index = users.indexOf(props.meUser.username);
          if (index > -1) {
            users.splice(index, 1);
          }
          console.log("doc", doc);
          console.log("user string", users.toString());
          newIds.push([doc.id, users.toString()]);
        });
        setIds(newIds);
        // if (props.meUser.username) {
        //   setCurrentUser(props.meUser.username);
        //   var newConversationDetails = db.collection("conversation-details");
        //   var query = newConversationDetails.where(
        //     "users",
        //     "array-contains",
        //     props.meUser.username
        //   );
        //   query
        //     .withConverter(usersConverter)
        //     .get()
        //     .then(function (querySnapshot) {
        //       let ids = [];
        //       console.log("Users converted", querySnapshot);
        //       querySnapshot.forEach(function (doc) {
        //         let users = doc.data().users;
        //         let index = users.indexOf(props.meUser.username);
        //         if (index > -1) {
        //           users.splice(index, 1);
        //         }
        //         console.log("doc", users.toString());
        //         ids.push([doc.id, users.toString()]);
        //       });
        //       setIds(ids);
        //     })
        //     .catch(function (error) {
        //       console.log("Error getting documents: ", error);
        //     });
        // }
      });
    return () => {
      unsubscribe();
    };
  }, [props.meUser.username, db]);

  return (
    <Page
      rightComponent={
        <ConversationPane
          ids={ids}
          onSelect={setCurrentId}
          db={db}
          currentUser={currentUser}
        />
      }
      middleComponent={
        <Conversation currentId={currentId} db={db} currentUser={currentUser} />
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
