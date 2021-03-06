import React, { Component } from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import { connect } from "react-redux";

import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";

import NavigationBar from "./Components/NavigationBar";
import CreatePost from "./Pages/CreatePost";
import Home from "./Pages/Home";
import SignIn from "./Pages/LogIn";
import PrivateRoute from "./Components/PrivateRoute";
import Registration from "./Pages/Registration";
import Explore from "./Pages/Explore";
import Message from "./Pages/Message";
import Search from "./Pages/Search";
import UserPage from "./Pages/UserPage";
import FollowersPage from "./Pages/FollowersPage";
import InfluencersPage from "./Pages/InfluencersPage";
import PostPage from "./Pages/PostPage";
import Settings from "./Pages/Settings";
import CommentPage from "./Pages/CommentPage";

import NoMatch from "./Components/NoMatch";
import API from "./api/api";
import { login, logout } from "./Actions/AuthenticationAction";
import history from "./Components/History";

class App extends Component {
  state = { user: [] };

  componentDidMount() {
    console.log("React verion", React.version);
    if (this.props.loggedIn === undefined) {
      API({
        method: "get",
        url: "api/applicationUsers/check",
      })
        .then((response) => {
          console.log("Authentication checked");
          console.log(response);
          if (window.location.pathname !== "/") {
            history.push("/");
            window.location.reload(true);
          }
        })
        .catch((error) => {
          console.log("Could not check authentication");
          this.props.logout();
        });
    }
    this.getUserInfo();
  }

  getUserInfo = () => {
    if (this.props.loggedIn) {
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
  };

  render() {
    return (
      <Router>
        {this.props.loggedIn ? (
          <NavigationBar meUser={this.state.user} />
        ) : (
          <div></div>
        )}
        <Switch>
          <PrivateRoute
            path="/"
            exact
            component={Home}
            meUser={this.state.user}
          />
          <PrivateRoute path="/create-post" exact component={CreatePost} />
          <PrivateRoute
            path="/message"
            exact
            component={Message}
            meUser={this.state.user}
          />
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
          <PrivateRoute
            path="/comment/:postId"
            component={CommentPage}
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
          <PrivateRoute
            path="/settings"
            component={Settings}
            meUser={this.state.user}
            handleChange={this.getUserInfo}
          />
          <Route component={NoMatch} />
        </Switch>
      </Router>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    loggedIn: state.loggedIn,
    error: state.error,
  };
};

const mapDispatchToProps = {
  login,
  logout,
};

export default connect(mapStateToProps, mapDispatchToProps)(App);
