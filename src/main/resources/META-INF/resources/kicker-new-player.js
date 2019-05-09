export default class KickerNewPlayer extends HTMLElement {
    constructor() {
        super();
        this.innerHTML = `
        <input type="text">
        <button type="button">add player</button>
        `
    }

    connectedCallback() {
        this.querySelector("button").onclick = () => this.addPlayer();
    }

    addPlayer() {
        const name = this.querySelector("input").value;
        if (!name || name.trim().length == 0) {
            alert("no name set: '" + name + "'");
        } else {
            fetch("/players", {
                method: 'POST',
                body: name,
                headers: {
                    'Content-Type': 'text/plain'
                }
            }).then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    response.json()
                            .then(json => alert(name + " was not added. Try again. \n" + (json ? json.kickerException : "")))
                            .catch(_ => alert(name + " was not added. Try again."));
                }
            }).catch(_ => alert(name + " was not added. Try again."));
        }
    }
}