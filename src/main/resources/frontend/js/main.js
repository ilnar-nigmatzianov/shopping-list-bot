'use strict';

import Client from './client/backendClient';

let client = new Client();
client.id = document.getElementById('list').dataset.id;
let buttons = document.getElementsByClassName('checklist-button');

Array.prototype.forEach.call(buttons, function (button) {
    button.addEventListener("click", function (event) {
        client.bought(event.target.dataset.id);
        event.target.parentElement.classList.add('checked');
        // itemChecked(event.target.dataset.id);

    });
});