docReady(async function () {

    let items = await fetch('/api/cities')
        .then(res => res.json())
        .then(json => {
            return json;
        })
        .catch(function (error) {
            console.log(error);
        });

    document.getElementById("root").innerHTML = json2Table(items);
});


function json2Table(json) {

    let headerRow = `<tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Action</th>
                     </tr>`

    let rows = json
        .map(city => {
            return `
              <tr>
                <td>${city.id}</td>
                <td>${city.name}</td>
                <td>
                   <button onclick="addToRoute('${city.name}')">Start</button>
                </td>
              </tr>
              `
        });

    const table = `
        	<table>
		      <thead>
		    	<tr>${headerRow}</tr>
		      <thead>
		      <tbody>
		    	${rows.join('')}
		      <tbody>
	        <table>
        `;

    return table;
}