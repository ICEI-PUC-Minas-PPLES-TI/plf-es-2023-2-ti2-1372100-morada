import * as API from '../../../assets/script/api.js';

loadProperties();

function loadProperties() {

    let propertiesElement = document.getElementById("properties");

    API.get("properties/user")
        .then(response => response.json())
        .then(properties => {

            if (properties.length == 0) {
                propertiesElement.innerHTML = `<p class="text-center">Você não possui imóveis geridos pelo Morada.</p>`;
                return;
            }

            properties.forEach(property => {

                propertiesElement.innerHTML += `
                    <div class="card mb-3">
                        <div class="row">
                            <div class="col-3">
                                <img src="${property.photoBase64}" style="width: 100%; height: 15vw; object-fit: cover;">
                            </div>
                            <div class="col-9">
                                <div class="card-body">
                                    <h5 class="card-title">${property.street}, ${property.neighborhood}</h5>
                                    <p class="card-text">${property.description}</p>
                                    <a href="/property/?id=${property.id}" class="btn btn-primary">Visualizar Imóvel</a>
                                </div>
                            </div>
                        </div>
                    </div>
                `;

            });

        });

}