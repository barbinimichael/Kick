import history from "../Components/History";
import {
  LOGOUT,
  LOGGING_IN,
  LOGIN_SUCCESS,
  LOGIN_FAIL,
} from "../Actions/AuthenticationAction";

const AuthenciationReduer = (
  state = { loggedIn: false, loggingIn: false, error: false },
  action
) => {
  switch (action.type) {
    case LOGOUT:
      console.log("user reducer, called LOGOUT");
      state = {
        ...state,
        loggingIn: false,
        loggedIn: false,
        error: false,
      };
      localStorage.removeItem("user");
      localStorage.removeItem("Authorization");
      history.push("/sign-in");
      break;

    case LOGGING_IN:
      console.log("user reducer, called LOGGING IN");
      state = {
        ...state,
        loggingIn: true,
        loggedIn: false,
        error: false,
      };
      break;

    case LOGIN_SUCCESS:
      console.log("user reducer, called LOGIN SUCCESS");
      state = {
        ...state,
        loggingIn: false,
        loggedIn: true,
        error: false,
      };
      break;

    case LOGIN_FAIL:
      console.log("user reducer, called LOGIN FAIL");
      state = {
        ...state,
        loggingIn: false,
        loggedIn: false,
        error: true,
      };
      break;

    default:
      console.log("authentication reducer, reached default case");
      state = {
        ...state,
        loggingIn: false,
        loggedIn: true,
        error: false,
      };
  }

  return state;
};

export default AuthenciationReduer;
