import React from "react";
import { InputGroup, FormControl, Button, Modal } from "react-bootstrap";

const CreateConversation = (props) => {
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
              props.onChange(e.target.value);
            }}
            value={props.search}
          />
        </InputGroup>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="primary" onClick={props.onCreate}>
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
