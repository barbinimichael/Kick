import axios from "axios";
import { connect } from "react-redux";
import { logout } from "../Actions/AuthenticationAction";

// local test
export default axios.create({
  baseURL: `https://quiet-inlet-83310.herokuapp.com/`,
  headers: {
    Authorization: localStorage["Authorization"],
    "Content-Type": "application/json",
  },
});
