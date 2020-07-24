import AuthenticationReducer from "./Reducers/AuthenticationReducer";
import { createStore, applyMiddleware } from "redux";
import logger from "redux-logger";
import thunk from "redux-thunk";

export default createStore(
  AuthenticationReducer,
  {},
  applyMiddleware(logger, thunk)
);
