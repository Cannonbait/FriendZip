import React from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

import "./App.css";

const CLIENT_ID = process.env.REACT_APP_CLIENT_ID!;

const SCOPES = [
  "openid",
  "email",
  "https://www.googleapis.com/auth/calendar.events.readonly",
  "https://www.googleapis.com/auth/calendar.readonly",
].join(" ");

function SuccessPage() {
  // TODO Call backend to fetch calendars
  return <div>You are signed in!</div>;
}

function Callback() {
  // TODO Call backend API with access_token
  return <div>Callback</div>;
}

function UnauthorizedUser() {
  return (
    <div>
      <div>Please sign in</div>
      <div>
        <button
          onClick={() => {
            const params = new URLSearchParams({
              client_id: CLIENT_ID,
              response_type: "code",
              access_type: "offline",
              redirect_uri: "http://localhost:3000/callback",
              scope: SCOPES,
            });
            window.location.href = `https://accounts.google.com/o/oauth2/v2/auth?${params}`;
          }}
        >
          Press here to sign in
        </button>
      </div>
    </div>
  );
}

function App() {
  return (
    <Router>
      <Switch>
        <Route path="/success">
          <SuccessPage />
        </Route>
        <Route path="/callback">
          <Callback />
        </Route>
        <Route path="/">
          <UnauthorizedUser />
        </Route>
      </Switch>
    </Router>
  );
}

export default App;
