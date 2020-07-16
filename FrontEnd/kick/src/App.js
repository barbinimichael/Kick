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
import UserPage from "./Pages/UserPage";
import FollowersPage from "./Pages/FollowersPage";
import InfluencersPage from "./Pages/InfluencersPage";
import PostPage from "./Pages/PostPage";

import NoMatch from "./Components/NoMatch";
import API from "./api/api";
import history from "./Components/History";

class App extends Component {
  state = { user: [] };

  componentDidMount() {
    this.getSelfInformation();
  }

  getSelfInformation = () => {
    API({
      method: "get",
      url: "api/applicationUsers/self",
    })
      .then((response) => {
        this.setState({ user: response.data });
      })
      .catch((error) => {
        console.log(error);
        this.handleLogout();
      });
  };

  handleLogout = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("Authorization");
    history.push("/sign-in");

    if (localStorage["checkedLogin"] === "") {
      localStorage.setItem("checkedLogin", true);
      document.location.reload(true);
    }
  };

  render() {
    return (
      <Router>
        {localStorage["Authorization"] ? <NavigationBar /> : <div></div>}
        <Switch>
          <PrivateRoute
            path="/"
            exact
            component={Home}
            meUser={this.state.user}
          />
          <PrivateRoute path="/create-post" exact component={CreatePost} />
          <PrivateRoute path="/message" exact component={Message} />
          <PrivateRoute path="/explore" exact component={Explore} />
          <PrivateRoute
            path="/search"
            exact
            component={Search}
            meUser={this.state.user}
          />
          <PrivateRoute
            path="/user/:username"
            component={UserPage}
            meUser={this.state.user}
          />
          <Route path="/sign-in" exact component={SignIn} />
          <Route path="/register" exact component={Registration} />
          <PrivateRoute
            path="/followers/:username"
            component={FollowersPage}
            meUser={this.state.user}
          />
          <PrivateRoute
            path="/influencers/:username"
            component={InfluencersPage}
            meUser={this.state.user}
          />
          <PrivateRoute path="/post/:postId" component={PostPage} />
          <Route component={NoMatch} />
        </Switch>
      </Router>
    );
  }
}

export default App;
