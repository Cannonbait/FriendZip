import React, { useEffect, useState } from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import jwtDecode from "jwt-decode";
import { parseISO, format } from "date-fns";

import "./App.css";
import { useMutation, useQuery } from "react-query";
import * as queryString from "querystring";

const CLIENT_ID = process.env.REACT_APP_CLIENT_ID!;

const SCOPES = [
  "openid",
  "email",
  "https://www.googleapis.com/auth/calendar.events.readonly",
  "https://www.googleapis.com/auth/calendar.readonly",
].join(" ");

interface Friend {
  id: string;
  name: string;
}

function getUsers(): Promise<Friend[]> {
  return fetch("http://localhost:8080/friends").then((r) => r.json());
}

function sendPlanRequest(
  accessToken: string,
  userId: string,
  friendId: string
): Promise<PlansResponse> {
  return fetch("http://localhost:8080/zip", {
    method: "POST",
    body: JSON.stringify({ requesterId: userId, peerId: friendId }),
  }).then((r) => r.json());
}

type PlansResponse = Slot[];

interface Slot {
  startTime: string;
  endTime: string;
}

const DATE_FORMAT = "EEE HH:mm";

function SuccessPage() {
  const [friendId, setFriendId] = useState<string | null>(null);
  const [isSubmitting, setSubmitting] = useState(false);
  const [response, setResponse] = useState<PlansResponse | null>(null);

  const { data, isLoading } = useQuery("users", getUsers);

  const handleSubmit = async () => {
    const userId = window.localStorage.getItem("user_id");
    const accessToken = window.localStorage.getItem("token");

    if (userId && accessToken && friendId) {
      setSubmitting(true);
      const plansResponse = await sendPlanRequest(
        accessToken,
        userId,
        friendId
      );
      setResponse(plansResponse);
      setSubmitting(false);
    } else {
      console.log({ userId, accessToken, friendId });
    }
  };

  if (response) {
    return (
      <div>
        <h3>Possible timeslots:</h3>
        {response.map((slot) => (
          <div key={slot.startTime}>
            {format(parseISO(slot.startTime), DATE_FORMAT)} -{" "}
            {format(parseISO(slot.endTime), DATE_FORMAT)}
          </div>
        ))}
      </div>
    );
  }

  return (
    <div>
      You are signed in!
      <div>Please select a friend: </div>
      <select onChange={(event) => setFriendId(event.target.value)}>
        <option>---</option>
        {data?.map((friend) => {
          return (
            <option value={friend.id} key={friend.id}>
              {friend.name}
            </option>
          );
        })}
      </select>
      {isSubmitting && "IsSubmittingBoi!!!"}
      <button onClick={handleSubmit}>Make plans!</button>
    </div>
  );
}

function Callback() {
  useEffect(() => {
    const fn = async () => {
      const { code } = queryString.parse(window.location.search.substr(1));
      const tokenResponse = await fetch(
        `http://localhost:8080/authenticate?accessCode=${code}`,
        {
          method: "POST",
        }
      ).then((r) => r.json());

      const token = jwtDecode(tokenResponse.id_token) as {
        sub: string;
        email: string;
      };

      window.localStorage.setItem("user_id", token.email);
      window.localStorage.setItem("token", tokenResponse.access_token);
      window.location.href = "/success";
    };
    fn();
  }, []);
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

export function App() {
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
