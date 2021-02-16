$(window).adaptTo("foundation-registry").register("foundation.validation.validator", {
  selector: "[data-foundation-validation^='maxlimit']",
  validate: function(el) {
    var max = el.getAttribute("data-limitvalue");
    var msg = el.getAttribute("data-errorMsg");
    max = parseInt(max);
    if (el.items.length > max){
        return msg+" "+max;
    }
  }
});