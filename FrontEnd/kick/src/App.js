import React, { Component } from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";

import NavigationBar from "./Components/NavigationBar";
import CreatePost from "./Components/CreatePost";
import Home from "./Components/Home";
import SignIn from "./Components/SignIn";
import PrivateRoute from "./Components/PrivateRoute";
import Registration from "./Components/Registration";
import Explore from "./Components/Explore";
import Message from "./Components/Message";

import NoMatch from "./Components/NoMatch";

class App extends Component {
  render() {
    return (
      <Router>
        {localStorage["Authorization"] ? <NavigationBar /> : <div></div>}
        <Switch>
          <PrivateRoute path="/" exact component={Home} />
          <PrivateRoute path="/create-post" exact component={CreatePost} />
          <PrivateRoute path="/message" exact component={Message} />
          <PrivateRoute path="/explore" exact component={Explore} />
          <Route path="/sign-in" exact component={SignIn} />
          <Route path="/register" exact component={Registration} />
          <Route component={NoMatch} />
        </Switch>
      </Router>
    );
  }
}

export default App;
