import React from "react";

import Page from "../Components/Page";

const NoMatch = () => {
  return (
    <Page
      middleComponent={
        <div>
          <h1>404 Error- Page Not Found</h1>
        </div>
      }
    />
  );
};

export default NoMatch;
