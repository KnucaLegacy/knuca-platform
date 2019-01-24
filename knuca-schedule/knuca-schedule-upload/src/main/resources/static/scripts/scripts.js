var x = document.getElementsByClassName("magic");
for (j=0; j < x.length; j++) { x[j].style.display = "none";}

function nBar(elmnt) {
    elmnt.style.display = "none";
    /*  elmnt.style.margin-left == 0; */
    for (i = 0; i < x.length; i++) {
        x[i].style.display = "block";
    }
}