import getPropertyCard from "../../../../assets/components/property-card.js";
import * as API from "../../../../assets/script/api.js";
import * as Alert from "../../../../assets/script/alert.js";

const cardContainer = document.getElementById("cardContainer");

const urlParams = new URLSearchParams(window.location.search);
let dados;
let propertyId = urlParams.get("id");


API.get(`properties/` + propertyId)
  .then((response) => {
    if (!response.ok) {
      Alert.alert(
        "Não foi possivel realizar uma proposta para este imóvel.",
        "danger",
        "default",
        0
      );
      throw new Error("Unable to retrieve property");
    }

    return response.json();
  })
  .then((property) => {
    dados = property;
    cardContainer.innerHTML = getPropertyCard(property);

    console.log(property.rentValue.toString());

    let valorSugerido = document.getElementById("ValorSugerido");
    console.log(valorSugerido.value);
    if (valorSugerido) {
      valorSugerido.value = `R$${property.rentValue.toString()}`;
    }
  });

const valorDaPropostaInput = document.getElementById("proposalValue");
valorDaPropostaInput.addEventListener("input", () => {
let inputValue = valorDaPropostaInput.value;

  inputValue = inputValue.replace(/\D/g, "");

  inputValue = parseFloat(inputValue);
  inputValue = isNaN(inputValue) ? 0 : inputValue;

  inputValue = (inputValue / 100).toFixed(2);
  inputValue = `${inputValue}`;

  valorDaPropostaInput.value = inputValue;
});

const btnSend = document.getElementById("btnSend");
const btnCancel = document.getElementById("btnCancel");

btnCancel.addEventListener("click", () => {
  // window.location.href = `/property/?id=${id}`;
  console.log("id"+id)
  console.log("value"+document.getElementById("proposalValue").value);  
});

document.getElementById("formOffer").addEventListener("submit", function (event){
  event.preventDefault();

  let rentValue = document.getElementById("proposalValue").value
  console.log(rentValue);
  if(rentValue <= 0){
    console.log("Debug");
    Alert.alert("Não é possivel enviar uma proposta para o imovel sem o valor do preencha o campo de valor","danger");
    return;
  }
  API.post('offers', {
    rentValue,
    propertyId
  })
  .then(response =>{
    if(response.status == 201){
      Alert.alert("Oferta enviada com sucesso", "success")

    }else{
      let data = response.json()
      console.log("Debug 2")
      Alert.alert(data.message, "danger");
    }
  })


})
