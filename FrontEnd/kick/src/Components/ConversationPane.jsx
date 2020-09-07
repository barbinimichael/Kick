import React, { useState, useEffect, useCallback } from "react";
import { Tab, Row, Col, Nav, Button, Image } from "react-bootstrap";
import plusCircleFill from "bootstrap-icons/icons/pencil.svg";
import CreateConversation from "./CreateConversation";

const ConversationPane = (props) => {
  const [currentUser, setCurrentUser] = useState("");
  const [seePopup, setSeePopup] = useState(false);
  const [search, setSearch] = useState("");

  useEffect(() => {
    if (props.ids[0] && props.currentUser) {
      console.log("Conversation pane current user", props.currentUser);
      console.log("Conversation pane ids", props.ids);
      setCurrentUser(props.currentUser);
      props.onSelect(props.ids[0][0]);
    }
  }, [props.ids, props.currentUser]);

  const handleClick = useCallback(
    (e) => {
      console.log("HANDLE CLICK");
      e.preventDefault();
      if (props.db) {
        console.log("add conversation");
        props.db
          .collection("conversation-details")
          .add({ users: [props.currentUser, search] })
          .then((documentRef) => {
            props.onSelect(documentRef);
          });
        setSeePopup(false);
      }
    },
    [props, currentUser, search, currentUser]
  );

  return (
    <React.Fragment>
      <div>
        <h3 className="center">
          Conversations{" "}
          <Button
            variant="outline-primary"
            size="lg"
            onClick={() => setSeePopup(true)}
          >
            <Image fluid src={plusCircleFill} alt=""></Image>
          </Button>
        </h3>
        <Tab.Container id="left-tabs-example" defaultActiveKey="0">
          <Row>
            <Col>
              <Nav
                variant="pills"
                className="flex-column"
                onSelect={(selectedKey) =>
                  props.onSelect(props.ids[selectedKey][0])
                }
              >
                {props.ids ? (
                  props.ids.map((id, index) => (
                    <Nav.Item key={index}>
                      <Nav.Link eventKey={index}>{id[1]}</Nav.Link>
                    </Nav.Item>
                  ))
                ) : (
                  <React.Fragment></React.Fragment>
                )}
              </Nav>
            </Col>
          </Row>
        </Tab.Container>
      </div>
      <CreateConversation
        show={seePopup}
        onClose={() => setSeePopup(false)}
        onCreate={handleClick}
        onChange={setSearch}
      />
    </React.Fragment>
  );
};

export default ConversationPane;
