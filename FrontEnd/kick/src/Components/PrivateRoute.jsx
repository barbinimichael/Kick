import React, { useEffect } from "react";
import { Redirect, Route } from "react-router-dom";
import API from "../api/api";
import { useDispatch } from "react-redux";

import { LOGOUT } from "../Actions/AuthenticationAction";

const PrivateRoute = ({ component: Component, ...rest }) => {
  const dispatch = useDispatch();

  useEffect(() => {
    API({
      method: "get",
      url: "api/applicationUsers/check",
    })
      .then(() => {})
      .catch(() => {
        dispatch({ type: LOGOUT });
      });
  });

  return (
    <Route
      {...rest}
      render={(props) =>
        localStorage["Authorization"] ? (
          <Component {...rest} {...props} />
        ) : (
          <Redirect
            to={{ pathname: "/sign-in", state: { from: props.location } }}
          />
        )
      }
    />
  );
};

export default PrivateRoute;
