import React from "react"
import NavbarPrincipal from "../components/headers/NavbarPrincipal"
import BarraLateral from "../components/BarraLateral"
import NavbarProjetos from "../components/NavbarProjetos"

export default class Dashboard extends React.Component {
    render() {
        return (
            <div>
                <NavbarPrincipal></NavbarPrincipal>
                <div className="flex">
                    <BarraLateral></BarraLateral>
                    <NavbarProjetos></NavbarProjetos>
                </div>
            </div>
        )
    }
}