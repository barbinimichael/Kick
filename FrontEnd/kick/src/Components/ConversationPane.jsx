import React, { useState, useEffect, useCallback } from "react";
import { Tab, Row, Col, Nav, Button, Image } from "react-bootstrap";
import plusCircleFill from "bootstrap-icons/icons/pencil.svg";

const ConversationPane = (props) => {
  const [currentUser, setCurrentUser] = useState("");

  useEffect(() => {
    if (props.ids[0] && props.currentUser) {
      setCurrentUser(props.currentUser);
      props.onSelect(props.ids[0][0]);
    }
  }, [props]);

  const handleClick = useCallback(
    (e) => {
      e.preventDefault();
      if (props.db) {
        console.log("add conversation");
        props.db
          .collection("conversation-details")
          .add({ users: [currentUser, "someother guy"] });
      }
    },
    [props.db, currentUser]
  );

  return (
    <div>
      <h3 className="center">
        Conversations{" "}
        <Button variant="outline-primary" size="lg" onClick={handleClick}>
          <Image fluid src={plusCircleFill} alt=""></Image>
        </Button>
      </h3>
      <Tab.Container id="left-tabs-example" defaultActiveKey="0">
        <Row>
          <Col>
            <Nav variant="pills" className="flex-column">
              {props.ids ? (
                props.ids.map((id, index) => (
                  <Nav.Item key={index}>
                    <Nav.Link
                      eventKey={index}
                      onSelect={() => props.onSelect(id[0])}
                    >
                      {id[1]}
                    </Nav.Link>
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
  );
};

export default ConversationPane;
