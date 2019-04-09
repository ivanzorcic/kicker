const baseUrl = window.location.host === "localhost:3000" ? "http://localhost:8080/kicker/resources" : "/kicker/resources";

fetch(baseUrl + "/ranking")
    .then(response => response.json())
    .then(ranking => console.log(ranking))