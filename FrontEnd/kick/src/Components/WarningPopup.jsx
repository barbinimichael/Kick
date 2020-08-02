import React from "react";
import { Modal, Button } from "react-bootstrap";

const WarningPopup = (props) => {
  return (
    <Modal
      {...props}
      size="lg"
      aria-labelledby="contained-modal-title-vcenter"
      centered
    >
      <Modal.Header>
        <Modal.Title id="contained-modal-title-vcenter">
          Are you sure you want to delete your account?
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>
          Warning: deleting your account is permanent. You will not be able to
          recover your account after deleting.
        </p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="danger" onClick={props.onDelete}>
          Delete account
        </Button>
        <Button variant="secondary" onClick={props.onClose}>
          Cancel
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default WarningPopup;
