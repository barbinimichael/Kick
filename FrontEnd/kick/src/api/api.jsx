import axios from "axios";

// http://localhost:53463/
// https://quiet-inlet-83310.herokuapp.com/

export default axios.create({
  baseURL: `https://api.kick-share.com/`,
  headers: {
    Authorization: localStorage["Authorization"],
    "Content-Type": "application/json",
  },
});
