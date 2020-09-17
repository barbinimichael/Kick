import React, { useState, useEffect } from "react";

import API from "../api/api";
import { Button } from "react-bootstrap";

const AutoSuggest = (props) => {
  const [feed, setFeed] = useState([]);

  useEffect(() => {
    API({
      method: "get",
      url: props.feedURL + `size=10`,
    })
      .then((response) => {
        let newFeed = response.data.content;
        console.log(props.feedURL);
        console.log(newFeed);
        setFeed(newFeed);
      })
      .catch((error) => {
        console.log(error);
      });
  }, [props.feedURL]);

  return (
    <React.Fragment>
      {feed &&
        feed.map((s) => (
          <Button
            block
            key={s.username}
            variant="light"
            onClick={() => {
              props.onClick(s.username);
            }}
          >
            {s.username}
          </Button>
        ))}
    </React.Fragment>
  );
};

export default AutoSuggest;
