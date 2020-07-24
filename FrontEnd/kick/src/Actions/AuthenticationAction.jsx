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
