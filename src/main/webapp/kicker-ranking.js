import baseUrl from './baseUrl.js'

export default class KickerRanking extends HTMLElement {
    constructor() {
        super();
        this.innerHTML = `
        <table>
            <tr>
                <th>rank</th>
                <th>name</th>
                <th>elo</th>
            </tr>
            <tr>
                <td colspan="3">Loading...</td>
            </tr>
        </table>
        `;
    }

    connectedCallback() {
        fetch(baseUrl + "/ranking")
            .then(response => response.json())
            .then(ranking => console.log(ranking));
    }
}