import React from "react";

type props = {
    titulo: string
}

export default class BotaoFormulario extends React.Component<props> {
    constructor(props: any) {
        super(props);
    }
    render() {
        return ( 
            <button type="submit" className="w-80 bg-blue-950 text-white rounded-sm outline-none cursor-pointer p-2 m-2">{this.props.titulo}</button>
        )
    }
}