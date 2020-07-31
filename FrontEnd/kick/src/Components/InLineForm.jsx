import React from "react";
import { Form, Button, InputGroup, FormControl, Col } from "react-bootstrap";

const InLineForm = (props) => {
  return (
    <Form>
      <Form.Row className="align-items-center">
        <Col className="mb-2">Change {props.description}</Col>
        <Col>
          <Form.Label htmlFor="inlineFormInputGroup" srOnly></Form.Label>
          <InputGroup className="mb-2">
            <FormControl
              id={props.description}
              placeholder={props.current}
              type={props.description}
              name={props.description}
              onChange={props.handleChange}
            />
          </InputGroup>
        </Col>
        <Col className="mb-2">
          <Button
            type="submit"
            onClick={(e) => props.handleSubmit(e, props.description)}
          >
            Submit
          </Button>
        </Col>
      </Form.Row>
    </Form>
  );
};

export default InLineForm;
