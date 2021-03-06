import history from "../Components/History";
import {
  LOGOUT,
  LOGGING_IN,
  LOGIN_SUCCESS,
  LOGIN_FAIL,
  LOGIN_RESET,
  REGISTERING,
  REGISTER_SUCCESSFUL,
  REGISTER_FAIL_USERNAME,
  REGISTER_FAIL_EMAIL,
  REGISTER_FAIL,
  REGISTER_RESET,
} from "../Actions/AuthenticationAction";

const AuthenciationReduer = (state = { loggedIn: undefined }, action) => {
  switch (action.type) {
    case REGISTER_RESET:
      state = {
        ...state,
        registering: false,
        registered: false,
        errorUsername: false,
        errorEmail: false,
        errorRegister: false,
      };
      break;

    case REGISTER_FAIL:
      state = {
        ...state,
        registering: false,
        registered: false,
        errorUsername: false,
        errorEmail: false,
        errorRegister: true,
      };
      break;

    case REGISTER_FAIL_USERNAME:
      state = {
        ...state,
        registering: false,
        registered: false,
        errorUsername: true,
        errorEmail: false,
      };
      break;

    case REGISTER_FAIL_EMAIL:
      state = {
        ...state,
        registering: false,
        registered: false,
        errorUsername: false,
        errorEmail: true,
      };
      break;

    case REGISTERING:
      state = {
        ...state,
        registering: true,
        registered: false,
      };
      break;

    case REGISTER_SUCCESSFUL:
      state = {
        ...state,
        registering: false,
        registered: true,
        errorUsername: false,
        errorEmail: false,
      };
      break;

    case LOGOUT:
      state = {
        ...state,
        loggingIn: false,
        loggedIn: false,
        error: false,
        registering: false,
        registered: false,
        errorUsername: false,
        errorEmail: false,
      };
      localStorage.removeItem("user");
      localStorage.removeItem("Authorization");
      history.push("/sign-in");
      history.go(0);
      break;

    case LOGIN_RESET:
      state = {
        ...state,
        loggingIn: false,
        loggedIn: false,
        error: false,
      };
      break;

    case LOGGING_IN:
      state = {
        ...state,
        loggingIn: true,
        loggedIn: false,
        error: false,
      };
      break;

    case LOGIN_SUCCESS:
      state = {
        ...state,
        loggingIn: false,
        loggedIn: true,
        error: false,
        registering: false,
        registered: false,
        errorUsername: false,
        errorEmail: false,
        errorRegister: false,
      };
      break;

    case LOGIN_FAIL:
      state = {
        ...state,
        loggingIn: false,
        loggedIn: false,
        error: true,
      };
      break;

    default:
      state = {
        loggingIn: false,
        loggedIn: false,
        error: false,
        registering: false,
        registered: false,
        errorUsername: false,
        errorEmail: false,
      };
      break;
  }

  return state;
};

export default AuthenciationReduer;
