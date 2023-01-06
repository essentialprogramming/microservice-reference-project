export class Utils {

    constructor() {

    }

    static getRequestParameter(parameterName){
        let requestParam = (new RegExp('[?&]' + encodeURIComponent(parameterName) + '=([^&]*)')).exec(location.search);
        if (requestParam)
            return decodeURIComponent(requestParam[1]);

        return '';
    }

    static formToJSON(formID) {
        const form = document.getElementById(formID);
        let rawData = new FormData(form);

        let data = {};
        for (let pair of rawData.entries()) {
            data[pair[0]] = pair[1];
        }
        return data;
    }

    static fillForm(form, data){
        if (form && data) {
            let inputs = form.elements;
            inputs = [...inputs].filter(input => input.nodeName === "INPUT" && !['submit', 'radio', 'checkbox'].includes(input.type));
            // Iterate over the form controls
            for (let i = 0; i < inputs.length; i++) {
                // Update input
                inputs[i].value = ( `${data[inputs[i].name]}` === 'undefined' || `${data[inputs[i].name]}` === 'null' )
                    ? ''
                    : `${data[inputs[i].name]}`
            }
        }
    }
}