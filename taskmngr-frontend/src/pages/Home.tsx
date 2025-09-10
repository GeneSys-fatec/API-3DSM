import React from "react";
import NavbarPrincipal from "../components/headers/NavbarPrincipal";
import BarraLateral from "../components/BarraLateral";
import BarraLateralProjetos from "../components/BarraLateralProjetos";
import NavbarProjetos from "../components/NavbarProjetos";


export default class Home extends React.Component {
    render() {
        return (
            <div>
                <NavbarPrincipal></NavbarPrincipal>
                <div className="flex">
                    <BarraLateral></BarraLateral>
                    <BarraLateralProjetos></BarraLateralProjetos>
                    <NavbarProjetos></NavbarProjetos>
                </div>
            </div>
        )
    }
}