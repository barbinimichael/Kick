import React, { Component } from "react";

import Page from "../Components/Page";
import UnderConstruction from "../Components/UnderConstruction";

class Settings extends Component {
  render() {
    return (
      <Page
        middleComponent={
          <React.Fragment>
            <h1>Settings</h1>
            <div className="center">
              <UnderConstruction description="Settings"></UnderConstruction>
            </div>
            <hr className="hr-line" />
            <div className="space-evenly">
              <a target="_blank" href="https://github.com/barbinimichael/Kick">
                About
              </a>
              <a
                target="_blank"
                href="https://quiet-inlet-83310.herokuapp.com/"
              >
                API
              </a>
            </div>
          </React.Fragment>
        }
      />
    );
  }
}

export default Settings;
