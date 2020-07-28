import history from "../Components/History";
import API from "../api/api";

export const LOGOUT = "LOGOUT";

export function logout() {
  return {
    type: LOGOUT,
  };
}

export const LOGGING_IN = "LOGGING IN";
export const LOGIN_SUCCESS = "LOGIN SUCCESSFUL";
export const LOGIN_FAIL = "LOGIN FAILED";

export function login(username, password) {
  return (dispatch) => {
    dispatch({ type: LOGGING_IN });

    API({
      method: "post",
      url: "/login",
      data: {
        username,
        password,
      },
      headers: {},
    })
      .then((response) => {
        console.log(response);
        dispatch({ type: LOGIN_SUCCESS });
        localStorage.setItem("user", username);
        localStorage.setItem("Authorization", response.headers.authorization);

        history.push("/");
        window.location.reload(true);
      })
      .catch((error) => {
        console.log(error);
        dispatch({ type: LOGIN_FAIL });
      });
  };
}

export const REGISTERING = "REGISTERING";
export const REGISTER_SUCCESSFUL = "REGISTER SUCCESSFUL";
export const REGISTER_FAIL_USERNAME = "REGISTER FAILED DUE TO USERNAME";
export const REGISTER_FAIL_EMAIL = "REGISTER FAILED DUE TO EMAIL";

export function register(data) {
  return (dispatch) => {
    dispatch({ type: REGISTERING });

    API({
      method: "post",
      url: "/api/applicationUsers/sign-up",
      data: JSON.stringify(data),
      headers: {},
    })
      .then((response) => {
        console.log(response);
        dispatch({ type: REGISTER_SUCCESSFUL });
        history.push("/sign-in");
        window.location.reload(true);
      })
      .catch((error) => {
        console.log(error);
        if (error.response) {
          if (error.response.data.error === "Username was already taken") {
            dispatch({ type: REGISTER_FAIL_USERNAME });
          } else if (error.response.data.error === "Email was already taken") {
            dispatch({ type: REGISTER_FAIL_EMAIL });
          } else {
            dispatch({ type: LOGIN_FAIL });
          }
        } else {
          dispatch({ type: LOGIN_FAIL });
        }
      });
  };
}
