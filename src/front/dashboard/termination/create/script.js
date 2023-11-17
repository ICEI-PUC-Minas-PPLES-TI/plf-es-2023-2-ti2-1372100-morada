import getPropertyCard from "../../../../assets/components/property-card.js";
import * as API from "../../../../assets/script/api.js";
import * as Alert from "../../../../assets/script/alert.js";

const cardContainer = document.getElementById("cardContainer");

const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get('id');
const propertyId = urlParams.get("propertyId")

API.get(`properties/` + propertyId)
  .then((response) =>{

    if(!response.ok){
      Alert.alert(
        "Não foi possivel iniciar uma recisão para essa propriedade",
        "dange",
        "default",
        0
      );
      throw new Error("Unable to retrive property")
    }

    return response.json();
  
  })
  .then((property) => {
    cardContainer.innerHTML = getPropertyCard(property);
  })


let rentalStart = document.getElementById("rentalStart")  
let idField = document.getElementById("processId");
let terminationDate = document.getElementById("terminationDate")
API.get("rentals/" + id)
  .then((response) =>{
    if(!response.ok){
      Alert.alert(
        "Não foi possivel iniciar uma recisão para essa propriedade",
        "dange",
        "default",
        0
      );
      throw new Error("Unable to retrive property")
    }

    return response.json();
  })
  .then((rent) => {
    rentalStart.value = convertArrayToDateTimeInputValue(rent.createdAt);
    idField.value = id;
    terminationDate.value = new Date()
  })

  function convertArrayToDateTimeInputValue(dateArray) {
    const [year, month, day, hour, minute, second] = dateArray;
    return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}T${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}:${String(second).padStart(2, '0')}`;
  }

 
