export interface Tarefa {
  tarId: string;
  tarTitulo: string;
  tarStatus: string;
  usuId: string;
  usuNome: string;
  tarPrazo: string;
  tarPrioridade: "Alta" | "Média" | "Baixa";
  tarDescricao: string;
  tarAnexo?: File | null;
}

export type NovaTarefa = Omit<Tarefa, "tarId">;

export type Coluna = {
  id: string;
  titulo: string;
  ordem: number;
  corClasse: string;
  corFundo: string;
};

export type Usuario = {
  usuId: string;
  usuNome: string;
  usuEmail?: string;
  usuCaminhoFoto?: string;
  usuDataCriacao?: string;
  usuDataAtualizacao?: string;
};

export type Projeto = {
  projId: string;
  projNome: string;
  projDescricao?: string;
  projStatus?: string;
  projDataCriacao?: string;
  projDataAtualizacao?: string;
  equId?: string;
  equNome?: string;
};

export type Equipe = {
  equId: string;
  equNome: string;
  equDescricao?: string;
  equDataCriacao?: string;
  equDataAtualizacao?: string;
  equMembros?: Usuario[];
  usuarioIds?: string[];
};

export type Anexo = {
  arquivoNome: string;
  arquivoCaminho: string;
  arquivoTipo: string;
  arquivoTamanho?: number;
};

export type Comentario = {
  comId: string;
  usuId: string;
  usuNome: string;
  comMensagem: string;
  comDataCriacao: string;
  comDataAtualizacao: string;
  comResposta?: string | null;
  tarId: string;
  replies?: Comentario[];
};
export interface EquipeComProjetos extends Equipe {
  projetos: Projeto[];
}