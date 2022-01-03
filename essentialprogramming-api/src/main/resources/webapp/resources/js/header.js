export function docReady(fn) {
    // see if DOM is already available
    if (document.readyState === "complete" || document.readyState === "interactive") {
        // call on next available tick
        setTimeout(fn, 1);
    } else {
        document.addEventListener("DOMContentLoaded", fn);
    }
}
export const loadComponent = async (element, index) => {
    const url = element.getAttribute('data-include');
    const response = await fetch(`${url}`)
    const html = await response.text();
    element.innerHTML = html;


}