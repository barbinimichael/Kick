import axios from "axios";

// http://localhost:53463/

export default axios.create({
  baseURL: `http://localhost:8080/`,
  headers: {
    Authorization: localStorage["Authorization"],
    "Content-Type": "application/json",
  },
});
