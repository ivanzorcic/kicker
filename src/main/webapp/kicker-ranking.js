import baseUrl from "./baseUrl.js";

export default class KickerRanking extends HTMLElement {
    constructor() {
        super();
        this.innerHTML = `
        <table>
        <thead>
            <tr>
                <th>rank</th>
                <th>name</th>
                <th>elo</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td colspan="3">Loading...</td>
            </tr>
        </tbody>
        </table>
        `;
    }

    connectedCallback() {
        fetch(baseUrl + "/ranking")
            .then(response => response.json())
            .then(ranking => this.renderRanking(ranking));
    }

    renderRanking(ranking){
        this.querySelector("tbody").innerHTML = ranking.map((player, i) =>
            `
            <tr>
                <td>${i !== 0 && ranking[i-1].elo == player.elo ? "" : '#' + (i+1)}</td>
                <td>${player.name}</td>
                <td>${player.elo}</td>
            </tr>
            `
        ).reduce((acc, cur) => acc + cur, "");
    }
}