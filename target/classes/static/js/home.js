let facilityCount = 0;
function addFacility() {
    facilityCount++;
    const container = document.getElementById('facilityContainer');
    const newFacility = document.createElement('div');
    newFacility.innerHTML = `
        <label for="facilities[${facilityCount}].name">Facility Name:</label>
        <input type="text" name="facilities[${facilityCount}].name" required/><br><br>
        <label for="facilities[${facilityCount}].cost">Facility Cost:</label>
        <input type="number" name="facilities[${facilityCount}].cost"/><br><br>
        <label for="facilities[${facilityCount}].available">Available:</label>
        <input type="checkbox" name="facilities[${facilityCount}].available"/><br><br>
    `;
    container.appendChild(newFacility);
}
