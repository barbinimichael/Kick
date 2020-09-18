import React, { useState, useEffect } from "react";
import { InputGroup, FormControl, Button, Modal, Row } from "react-bootstrap";
import deepEqual from "deep-equal";

import AutoSuggest from "./AutoSuggest";
import UserSuggestionToast from "./UserSuggestionToast";

const CreateConversation = (props) => {
  const [currentSearch, setCurrentSearch] = useState("");
  const [totalSearch, setTotalSearch] = useState([]);
  const [previousConversations, setPreviousConversations] = useState([]);

  useEffect(() => {
    if (props.ids[0]) {
      let users = [];
      props.ids.map((id) => users.push(id[1]));
      console.log("users", users);
      setPreviousConversations(users);
    }
  }, [props.ids]);

  return (
    <Modal
      show={props.show}
      onClose={props.onClose}
      size="lg"
      aria-labelledby="contained-modal-title-vcenter"
      centered
    >
      <Modal.Header>
        <Modal.Title id="contained-modal-title-vcenter">
          Create a conversation with...
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <InputGroup className="mb-3">
          <FormControl
            placeholder="To..."
            name="search"
            onChange={(e) => {
              // props.onChange(e.target.value);
              setCurrentSearch(e.target.value);
            }}
            value={props.search}
          />
        </InputGroup>
      </Modal.Body>
      <Modal.Body>
        <Row inline>
          {totalSearch.length > 0 &&
            totalSearch.map((s, index) => (
              <UserSuggestionToast
                key={index}
                user={s}
                onDelete={() => {
                  console.log("Total search before deletion", totalSearch);
                  let newSearch = totalSearch;
                  newSearch = newSearch.filter((search) => search !== s);
                  setTotalSearch(newSearch);
                  console.log("Total search after deletion", totalSearch);
                }}
              />
            ))}
        </Row>
      </Modal.Body>
      <Modal.Body>
        {currentSearch !== "" && (
          <AutoSuggest
            feedURL={`/api/applicationUsers/search?search=${currentSearch}&`}
            onClick={(s) => {
              if (!totalSearch.includes(s)) {
                setTotalSearch(totalSearch.concat(s));
              }

              console.log("Total search", totalSearch);
            }}
          />
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button
          variant="primary"
          onClick={(e) => {
            console.log(
              "Previous Conversations from create conversation",
              previousConversations
            );
            console.log(
              "Clicked add conversation, current user",
              props.currentUser
            );
            if (totalSearch.length > 0) {
              let unique = true;
              let sortTotalSearch = totalSearch.sort();
              sortTotalSearch = sortTotalSearch.filter(
                (e) => e !== props.currentUser
              );

              previousConversations.map(
                (c) =>
                  (unique = unique && !deepEqual(c.sort(), sortTotalSearch))
              );

              if (unique && sortTotalSearch.length > 0) {
                props.onCreate(e, sortTotalSearch);
                return;
              }
            }
            console.log("message already exists!");
            props.onClose();
          }}
        >
          Create conversation
        </Button>
        <Button variant="secondary" onClick={props.onClose}>
          Cancel
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default CreateConversation;
