export class Utils {

    constructor() {

    }

    static getRequestParameter(parameterName){
        if(parameterName = (new RegExp('[?&]' + encodeURIComponent(parameterName) + '=([^&]*)')).exec(location.search))
            return decodeURIComponent(parameterName[1]);
    }

}