import React from "react";
import { Figure } from "react-bootstrap";
import caution from "bootstrap-icons/icons/exclamation-triangle-fill.svg";

const UnderConstruction = (props) => {
  return (
    <Figure className="center">
      <Figure.Image width={30} height={30} alt="171x180" src={caution} />
      <Figure.Caption className="figure-caption text-center">
        {props.description} are currently under construction
      </Figure.Caption>
    </Figure>
  );
};

export default UnderConstruction;
