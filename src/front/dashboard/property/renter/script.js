import * as API from "../../../assets/script/api.js";
import * as Alert from "../../../../assets/script/alert.js";

let rents = document.getElementById("content");

API.get("rentals/userRents")
    .then((response) => response.json())
    .then((rent) =>{

        if(rent.length == 0){
            rents.innerHTML = `<p class="text-center">Você não recebeu nenhuma proposta por seus imoveis.</p>`;
            return;
        }

        rent.forEach((element) => {

            let currentRentalId = element.rentalId
            let propertiesId = element.propertyId
            let options = ""

            console.log("rentalId" + currentRentalId);
            console.log("propertyId" + propertiesId)
            console.log(`/dashboard/termination/create?id=${currentRentalId}&propertyId=${propertiesId}`);


            if(currentRentalId !== undefined){
                options +=
                `
                <button type="button" class="btn btn-outline-primary" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <i class="fa-solid fa-ellipsis"></i>
                </button>
                <div class="dropdown-menu">
                    <a class="dropdown-item" href="/dashboard/payment/owner?id=${currentRentalId}">Pagamentos</a>
                    <a class="dropdown-item" href="/dashboard/renegotiation/owner?id=${currentRentalId}">Renegociações</a>
                    <a class="dropdown-item" href="/dashboard/renegotiation/create?id=${currentRentalId}">Renegociar do valor do aluguel</a>
                    <a class="dropdown-item" href="/dashboard/termination/create/?id=${currentRentalId}&propertyId=${propertiesId}">Rescindir contrato de aluguel</a>
                </div>
                `
            }

            rents.innerHTML += 
            `
            <div class="card mb-3">
            <div class="row">
                <div class="col-3">
                    <img src="${element.photo_base64}" style="width: 100%; height: 15vw; object-fit: cover;">
                </div>
                <div class="col-9">
                    <div class="card-body">
                        <h5 class="card-title mt-2">${element.street}, ${element.neighborhood}</h5>
                        <p class="card-text text-truncate">${element.description}</p>
                        <a href="/property/?id=${element.id}" class="btn btn-primary">Visualizar Imóvel</a>
                        ${options}
                    </div>
                </div>
            </div>
        </div>
            `            
        });

    })

