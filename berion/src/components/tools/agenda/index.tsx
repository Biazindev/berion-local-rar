import React, { useEffect, useState } from "react";
import { gapi } from "gapi-script";
import { Container, EventList, Title,EventItem } from "./styles";

const CLIENT_ID = "SEU_CLIENT_ID.apps.googleusercontent.com";
const API_KEY = "SUA_API_KEY";
const SCOPES = "https://www.googleapis.com/auth/calendar.readonly";

interface Event {
  id: string;
  summary: string;
  start: {
    dateTime: string;
  };
  end: {
    dateTime: string;
  };
}

const Agenda = () => {
  const [events, setEvents] = useState<Event[]>([]);

  useEffect(() => {
    const start = async () => {
      function startGapi() {
        gapi.client.init({
          apiKey: API_KEY,
          clientId: CLIENT_ID,
          discoveryDocs: [
            "https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest",
          ],
          scope: SCOPES,
        });

        gapi.auth2.getAuthInstance().signIn().then(() => {
          gapi.client.calendar.events
            .list({
              calendarId: "primary",
              timeMin: new Date().toISOString(),
              showDeleted: false,
              singleEvents: true,
              orderBy: "startTime",
            })
            .then((response: any) => {
              setEvents(response.result.items);
            });
        });
      }

      gapi.load("client:auth2", startGapi);
    };

    start();
  }, []);

  return (
    <Container>
      <Title>📅 Agenda</Title>
      <EventList>
        {events.length === 0 && <p>Carregando eventos...</p>}
        {events.map((event) => (
          <EventItem key={event.id}>
            <strong>{event.summary}</strong>
            <p>
              {new Date(event.start.dateTime).toLocaleString()} →{" "}
              {new Date(event.end.dateTime).toLocaleString()}
            </p>
          </EventItem>
        ))}
      </EventList>
    </Container>
  );
};

export default Agenda;
