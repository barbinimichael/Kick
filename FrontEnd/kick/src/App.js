import React, { Component } from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";

import NavigationBar from "./Components/NavigationBar";
import CreatePost from "./Pages/CreatePost";
import Home from "./Pages/Home";
import SignIn from "./Pages/SignIn";
import PrivateRoute from "./Components/PrivateRoute";
import Registration from "./Pages/Registration";
import Explore from "./Pages/Explore";
import Message from "./Pages/Message";
import Search from "./Pages/Search";

import NoMatch from "./Components/NoMatch";
import API from "./api/api";

class App extends Component {
  componentDidMount() {
    API({
      method: "get",
      url: "api/applicationUsers/self",
    })
      .then((response) => {
        this.setState({ user: response.data });
      })
      .catch((error) => {
        console.log(error);
      });
  }

  render() {
    return (
      <Router>
        {localStorage["Authorization"] ? <NavigationBar /> : <div></div>}
        <Switch>
          <PrivateRoute path="/" exact component={Home} />
          <PrivateRoute path="/create-post" exact component={CreatePost} />
          <PrivateRoute path="/message" exact component={Message} />
          <PrivateRoute path="/explore" exact component={Explore} />
          <PrivateRoute path="/search" exact component={Search} />
          <Route path="/sign-in" exact component={SignIn} />
          <Route path="/register" exact component={Registration} />
          <Route component={NoMatch} />
        </Switch>
      </Router>
    );
  }
}

export default App;
