import React from "react";
import FormularioCadastro from "../components/FormularioCadastro";
import NavbarLogin from "../components/headers/NavbarLogin";

export default class Cadastro extends React.Component{
    render() {
        return (
            <div>
                <NavbarLogin></NavbarLogin>
                <FormularioCadastro></FormularioCadastro>
            </div>
        )
    }
}