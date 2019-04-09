export default class KickerAddGame extends HTMLElement {
    constructor() {
        super();
        this.innerHTML = `
        <select>
            <option value="volvo">Manfred</option>
            <option value="saab">Rudi</option>
        </select>
        <select>
            <option value="volvo">Manfred</option>
            <option value="saab">Rudi</option>
        </select>
        <select>
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
        <select>
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
        <select>
            <option value="volvo">Manfred</option>
            <option value="saab">Rudi</option>
        </select>
        <select>
            <option value="volvo">Manfred</option>
            <option value="saab">Rudi</option>
        </select>
        `;
    }
}