import axios from "axios";

export default axios.create({
  baseURL: `https://quiet-inlet-83310.herokuapp.com/`,
  headers: {
    Authorization: localStorage["Authorization"],
    "Content-Type": "application/json",
  },
});
