
    let nom = document.getElementById("nickname");
    nom.setAttribute('onClick','window.location.href = \'detalle-usuario.jsp\';');

    var selectRow = null;

    function editar(a){

        console.log(a);
    }

    function llamarDetalleUsuario(){
        window.location.href ='detalle-usuario.jsp';
    }