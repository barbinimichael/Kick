import axios from "axios";

// production
// export default axios.create({
//   baseURL: `https://quiet-inlet-83310.herokuapp.com/`,
//   headers: {
//     Authorization: localStorage["Authorization"],
//     "Content-Type": "application/json",
//   },
// });

// local test
export default axios.create({
  baseURL: `http://localhost:8080/`,
  headers: {
    Authorization: localStorage["Authorization"],
    "Content-Type": "application/json",
  },
});
