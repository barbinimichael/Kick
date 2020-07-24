import axios from "axios";
import { connect } from "react-redux";
import { logout } from "../Actions/AuthenticationAction";

export default axios.create({
  baseURL: `https://quiet-inlet-83310.herokuapp.com/`,
  headers: {
    Authorization: localStorage["Authorization"],
    "Content-Type": "application/json",
  },
});

// const defaultAxios = axios.create({
//   baseURL: `https://quiet-inlet-83310.herokuapp.com/`,
//   headers: {
//     Authorization: localStorage["Authorization"],
//     "Content-Type": "application/json",
//   },
// });

// defaultAxios.interceptors.response.use(
//   function (response) {
//     // Any status code that lie within the range of 2xx cause this function to trigger
//     // Do something with response data
//     return response;
//   },
//   function (error) {
//     // Any status codes that falls outside the range of 2xx cause this function to trigger
//     // Do something with response error
//     console.log("Http error: ", error);
//     if (this.props.loggedIn) {
//       console.log("logged in");
//     }
//     return Promise.reject(error);
//   }
// );

// const mapStateToProps = (state) => {
//   return {
//     loggedIn: state.loggedIn,
//   };
// };

// const mapDispatchToProps = {
//   logout,
// };

// export default connect(mapStateToProps, mapDispatchToProps)(defaultAxios);
