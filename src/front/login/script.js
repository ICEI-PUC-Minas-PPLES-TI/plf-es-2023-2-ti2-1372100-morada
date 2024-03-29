import * as Alert from '../assets/script/alert.js';
import * as API from '../assets/script/api.js';

document.getElementById("login-form").addEventListener("submit", function (event) {

    event.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    API.login(email, password, message => {
        Alert.alert(message, "danger")
    });

});