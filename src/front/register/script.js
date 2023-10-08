import * as Alert from '../assets/script/alert.js';
import * as API from '../assets/script/api.js';

document.getElementById("register-form").addEventListener("submit", function (event) {

    event.preventDefault();

    const name = document.getElementById("name").value;
    const cpf = document.getElementById("cpf").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch(API.BASE_URL + '/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name,
            cpf,
            email,
            password
        }),
    }).then(response => {

        if (response.status === 201) {

            window.location.href = "../login";

        } else if (response.status === 400) {

            response.json().then(data => {
                Alert.alert(data.message, "danger");
            });

        }

    }).catch(error => {
        console.log(error);
    });

});