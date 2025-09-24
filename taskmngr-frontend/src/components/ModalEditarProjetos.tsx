import React from 'react';
import { createPortal } from 'react-dom';

type Projeto = {
  id: string;
  nome: string;
  descricao?: string;
  dataCriacao?: string; 
};

type ModalProps = {
  isOpen: boolean;
  onClose: () => void;
  projeto: Projeto; 
  onSaved?: (projeto: Projeto) => void; 
};

type ModalState = {
  projectName: string;
  description: string;
  dataCriacao: string;
  dataAtualizacao: string;
  saving: boolean;
  error?: string | null;
};

export default class ModalEditarProjetos extends React.Component<ModalProps, ModalState> {
  private modalRef = React.createRef<HTMLDivElement>();

  constructor(props: ModalProps) {
    super(props);
    this.state = {
      projectName: props.projeto?.nome || '',
      description: props.projeto?.descricao || '',
      dataCriacao: props.projeto?.dataCriacao || '',
      dataAtualizacao: new Date().toISOString().slice(0, 10),
      saving: false,
      error: null,
    };
  }

  componentDidMount() {
    if (this.props.isOpen) {
      document.addEventListener('mousedown', this.handleOutsideClick);
    }
  }

  componentDidUpdate(prevProps: ModalProps) {
    if (this.props.isOpen && !prevProps.isOpen) {
      document.addEventListener('mousedown', this.handleOutsideClick);
      const { projeto } = this.props;
      this.setState({
        projectName: projeto?.nome || '',
        description: projeto?.descricao || '',
        dataCriacao: projeto?.dataCriacao || '',
        dataAtualizacao: new Date().toISOString().slice(0, 10),
        error: null,
      });
    } else if (!this.props.isOpen && prevProps.isOpen) {
      document.removeEventListener('mousedown', this.handleOutsideClick);
    }

    if (this.props.projeto?.id !== prevProps.projeto?.id && this.props.isOpen) {
      const { projeto } = this.props;
      this.setState({
        projectName: projeto?.nome || '',
        description: projeto?.descricao || '',
        dataCriacao: projeto?.dataCriacao || '',
        dataAtualizacao: new Date().toISOString().slice(0, 10),
        error: null,
      });
    }
  }

  componentWillUnmount() {
    document.removeEventListener('mousedown', this.handleOutsideClick);
  }

  private handleOutsideClick = (event: MouseEvent) => {
    if (this.modalRef.current && !this.modalRef.current.contains(event.target as Node)) {
      this.props.onClose();
    }
  };

  handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = event.target;
    this.setState({ [name]: value } as unknown as Pick<ModalState, keyof ModalState>);
  };


  atualizarProjeto = () => {
    const { projeto } = this.props;
    const { projectName, description, dataCriacao, dataAtualizacao } = this.state;

    this.setState({ saving: true, error: null });

    const token = localStorage.getItem('token');

    fetch(`http://localhost:8080/projeto/atualizar/${projeto.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
      body: JSON.stringify({
        proj_nome: projectName,
        proj_descricao: description,
        proj_dataCriacao: dataCriacao || null,
        proj_dataAtualizacao: dataAtualizacao || new Date().toISOString().slice(0, 10),
      }),
    })
      .then(async (response) => {
        if (!response.ok) {
          const errorBody = await response.text();
          throw new Error(`Erro ao atualizar projeto: ${response.statusText}. Resposta do servidor: ${errorBody}`);
        }
        this.props.onSaved?.({
          id: projeto.id,
          nome: projectName,
          descricao: description,
          dataCriacao,
        });
        this.props.onClose();
      })
      .catch((error) => {
        console.error("Falha ao atualizar projeto:", error);
        this.setState({ error: "Não foi possível salvar as alterações." });
        alert("Não foi possível salvar as alterações.");
      })
      .finally(() => this.setState({ saving: false }));
  };

  handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();

    if (!this.state.projectName.trim()) {
      alert('Por favor, insira o nome do projeto.');
      return;
    }

    this.atualizarProjeto();
  };

  render() {
    const { isOpen, onClose } = this.props;

    if (!isOpen) {
      return null;
    }

    const modalContent = (
      <div className="fixed inset-0 bg-gray-600/60 flex items-center justify-center z-50 p-4 font-sans">
        <div ref={this.modalRef} className="bg-white rounded-lg shadow-2xl w-full max-w-3xl p-8 flex flex-col gap-4">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-gray-800">Editar Projeto</h2>
            <button onClick={onClose} className="text-gray-400 hover:text-gray-600 text-3xl transition-colors duration-200">
              &times;
            </button>
          </div>

          <form onSubmit={this.handleSubmit} className='flex flex-col gap-y-5'>
            <div>
              <label htmlFor="projectName" className="block text-sm font-medium text-gray-700 mb-1">Nome do Projeto</label>
              <input
                type="text"
                id="projectName"
                name="projectName"
                placeholder="Nome do Projeto"
                value={this.state.projectName}
                onChange={this.handleChange}
                className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
              />
            </div>

            <div>
              <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">Descrição</label>
              <textarea
                id="description"
                name="description"
                rows={3}
                placeholder=""
                value={this.state.description}
                onChange={this.handleChange}
                className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
              ></textarea>
            </div>

            {/* <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Datas</label>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs text-gray-500 mb-1">Data de Criação</label>
                  <input
                    type="date"
                    name="dataCriacao"
                    value={this.state.dataCriacao}
                    onChange={this.handleChange}
                    className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2 text-gray-700"
                  />
                </div>
                <div>
                  <label className="block text-xs text-gray-500 mb-1">Data de Atualização</label>
                  <input
                    type="date"
                    name="dataAtualizacao"
                    value={this.state.dataAtualizacao}
                    onChange={this.handleChange}
                    className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2 text-gray-700"
                  />
                </div>
              </div>
            </div> */}

            <div className="flex justify-center mt-6 gap-3">
              <button
                type="button"
                onClick={onClose}
                className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300 focus:outline-none"
                disabled={this.state.saving}
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors duration-200 disabled:opacity-60"
                disabled={this.state.saving}
              >
                {this.state.saving ? 'Salvando...' : 'Salvar alterações'}
              </button>
            </div>
          </form>
        </div>
      </div>
    );

    return createPortal(modalContent, document.body);
  }
}

