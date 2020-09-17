import React, { useState } from "react";
import {
  InputGroup,
  FormControl,
  Button,
  Modal,
  Toast,
  Row,
} from "react-bootstrap";
import AutoSuggest from "./AutoSuggest";

const CreateConversation = (props) => {
  const [currentSearch, setCurrentSearch] = useState("");
  const [totalSearch, setTotalSearch] = useState([]);

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
            totalSearch.map((s) => (
              <Toast>
                <Toast.Header>{s}</Toast.Header>
              </Toast>
            ))}
        </Row>
      </Modal.Body>
      <Modal.Body>
        {currentSearch !== "" && (
          <AutoSuggest
            feedURL={`/api/applicationUsers/search?search=username:*${currentSearch}*&`}
            onClick={(s) => {
              setTotalSearch(totalSearch.concat(s));
              console.log("Total search", totalSearch);
            }}
          />
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button
          variant="primary"
          onClick={(e) => {
            if (totalSearch.length > 0) {
              props.onCreate(e, totalSearch);
            } else {
              props.onClose();
            }
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
