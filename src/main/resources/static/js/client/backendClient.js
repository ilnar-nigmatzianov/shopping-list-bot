'use strict';
import axios from 'axios'

export default class BackendClient {
    set id(id) {
        this._id = id;
    }

    get id() {
        return this._id;
    }

    bought(id) {
        axios.put('/lists/items/' + id)
            .then(function (response) {
                // handle success
                console.log(response);
            })
            .catch(function (error) {
                // handle error
                console.log(error);
            });
        console.log(id + ' checked for list ' + this._id);
    }
}