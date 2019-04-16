import baseUrl from "./baseUrl.js";

export default class KickerAddGame extends HTMLElement {
    constructor() {
        super();
        this.innerHTML = `
        <div>
          Loading Players...
        </div>
        `;
    }

    connectedCallback() {
        fetch(baseUrl + "/players")
            .then(response => response.json())
            .then(players => this.init(players));
    }

    init(players) {
        const playersAsOptions = players.map(p =>
                `<option value="${p.id}">${p.name}</option>
             `
            )
            .reduce((acc, cur) => acc + cur, "");
        this.querySelector("div")
            .innerHTML = `
        <select id="t1p1">
            <option hidden  disabled selected value> -- </option>
            ${playersAsOptions}
        </select>
        <select id="t1p2">
            <option hidden disabled selected value> -- </option>
            ${playersAsOptions}
        </select>
        <select id="team1ScoreGame1">
            <option hidden  disabled selected value> -- : -- </option>
            <option value="10">10:0</option>
            <option value="9">9:1</option>
            <option value="8">8:2</option>
            <option value="7">7:3</option>
            <option value="6">6:4</option>
            <option value="5">5:5</option>
            <option value="4">4:6</option>
            <option value="3">3:7</option>
            <option value="2">2:8</option>
            <option value="1">1:9</option>
            <option value="0">0:10</option>
        </select>
        <select id="team1ScoreGame2">
            <option hidden  disabled selected value> -- : -- </option>
            <option value="10">10:0</option>
            <option value="9">9:1</option>
            <option value="8">8:2</option>
            <option value="7">7:3</option>
            <option value="6">6:4</option>
            <option value="5">5:5</option>
            <option value="4">4:6</option>
            <option value="3">3:7</option>
            <option value="2">2:8</option>
            <option value="1">1:9</option>
            <option value="0">0:10</option>
        </select>
        <select id="t2p1">
            <option hidden  disabled selected value> -- </option>
            ${playersAsOptions}
        </select>
        <select id="t2p2">
            <option hidden disabled selected value> -- </option>
            ${playersAsOptions}
        </select>
        <button type="button">add game</button>
        `
        this.querySelector("button").onclick = () => this.newGame();
    }

    newGame() {
        const newGame = {
            "team1Player1": parseInt(this.querySelector("#t1p1").value),
            "team1Player2": parseInt(this.querySelector("#t1p2").value),
            "team2Player1": parseInt(this.querySelector("#t2p1").value),
            "team2Player2": parseInt(this.querySelector("#t2p2").value),
            "team1ScoreGame1": parseInt(this.querySelector("#team1ScoreGame1").value),
            "team1ScoreGame2": parseInt(this.querySelector("#team1ScoreGame2").value)
        };
        if (!newGame.team1Player1 || !newGame.team1Player2 || !newGame.team2Player1 || !newGame.team2Player2 || !newGame.team1ScoreGame1 || !newGame.team1ScoreGame2) {
            alert("select players and results");
        } else {
            fetch(baseUrl + "/games", {
                method: 'POST',
                body: JSON.stringify(newGame),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    response.json()
                        .then(json => {
                            alert((!!json && !!json.kickerException) ? json.kickerException : "Game was not added. Try again.");
                        });
                }
            });
        }
    }
}