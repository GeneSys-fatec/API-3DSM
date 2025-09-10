import React from "react"
import NavbarPrincipal from "../components/headers/NavbarPrincipal"
import BarraLateral from "../components/BarraLateral"

export default class Equipes extends React.Component {
    render() {
        return (
            <div>
                <NavbarPrincipal></NavbarPrincipal>
                <div className="flex">
                    <BarraLateral></BarraLateral>
                </div>
            </div>
        )
    }
}