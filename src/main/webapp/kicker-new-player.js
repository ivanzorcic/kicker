export default class KickerNewPlayer extends HTMLElement {
    constructor() {
        super();
        this.innerHTML = `
        <input type="text">
        <button type="button">add</button>
        `
    }
}