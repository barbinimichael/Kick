import React, { useState } from "react";
import { Toast } from "react-bootstrap";

const UserSuggestionToast = (props) => {
  const [showToast, setShowToast] = useState(true);

  return (
    <React.Fragment>
      <Toast
        show={showToast}
        onClose={() => {
          setShowToast(false);
          props.onDelete();
        }}
      >
        <Toast.Header>{props.user}</Toast.Header>
      </Toast>
    </React.Fragment>
  );
};

export default UserSuggestionToast;
