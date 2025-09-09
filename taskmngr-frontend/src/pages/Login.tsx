import React from "react";
import NavbarLogin from "../components/headers/NavbarLogin";
import FormularioLogin from "../components/FormularioLogin";

export default class Login extends React.Component{
    render() {
        return (
            <div>
                <NavbarLogin></NavbarLogin>
                <FormularioLogin></FormularioLogin>
            </div>
        )
    }
}